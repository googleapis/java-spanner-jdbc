/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.jdbc.quickperf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.google.cloud.jdbc.quickperf.config.Config;
import com.google.cloud.jdbc.quickperf.config.ConfigParser;
import com.google.cloud.jdbc.quickperf.config.QueryParam;
import com.google.cloud.spanner.Dialect;
import com.google.cloud.spanner.jdbc.CloudSpannerJdbcConnection;

import net.datafaker.Faker;

public class QuickPerf extends Thread {
    private static String BREAK_STR = "###################################################################################################";
    private static String DEFAULT_TAG = "perftest_" + (new Random()).nextInt(300);

    // TODO: make measurementfile configurable
    private static String MEASURES_FILE_NAME = "measures.txt";

    // perf measurement
    private float measures[];

    // configs
    Config config;

    private List<String> sampledValueList = new ArrayList<String>();

    private int progress;

    public static void main(String[] args)
            throws SQLException, InterruptedException, StreamReadException, DatabindException, IOException {
        Options options = new Options();

        options.addOption(QuickPerf.addOption("c", "config", true, "Config File"));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        ZonedDateTime testStartTimestamp = ZonedDateTime.now();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        // System.out.println("#### " + cmd.getOptionValue("config"));

        Config config = ConfigParser.parseConfigFile(cmd.getOptionValue("config"));

        float measures[] = new float[config.getIterations() * config.getThreads()];

        List<QuickPerf> threadList = new ArrayList<QuickPerf>();
        for (int i = 0; i < config.getThreads(); i++) {
            QuickPerf thread = new QuickPerf(config);
            if (config.getSamplingQuery() != null) {
                thread.runSampling();
            }
            thread.start();
            threadList.add(thread);
        }

        // ProgressBar Tracker Thread
        ProgressTracker progressTracker = progressTracker = new ProgressTracker(threadList,
                config.getIterations() * config.getThreads());

        progressTracker.start();

        progressTracker.join();

        int i = 0;
        for (QuickPerf thread : threadList) {
            thread.join();

            if (i == 0) {
                measures = thread.getMeasures();
            } else {
                measures = ArrayUtils.addAll(measures, thread.getMeasures());
            }
            i++;
        }

        // write to file before its sorted
        if (config.getWriteMetricToFile()) {
            try {
                writeMeasuresToFile(measures, MEASURES_FILE_NAME);
            } catch (IOException e) {
                System.err.println("An error occurred while writing the file: " + e.getMessage());
            }
        }

        System.out.println("\n" + BREAK_STR);
        System.out.println("Query: " + config.getQuery());
        System.out.println("Params: " + config.paramsToString());
        System.out.println("Tag: " + DEFAULT_TAG);
        if (config.getBatchSize() > 0) {
            System.out.println("Batching Enabled (size): " + config.getBatchSize());
        }
        System.out.println(String.format("Start: %s End: %s", testStartTimestamp, ZonedDateTime.now()));
        System.out.println(
                String.format(
                        "Finished with a total of %s runs across %s Threads.\nLatencies (ms):  p50 = %s, p95 = %s, p99 = %s, min = %s,  max = %s",
                        config.getIterations() * config.getThreads(), config.getThreads(),
                        calcPerc(measures, 50), calcPerc(measures, 95), calcPerc(measures, 99), getMin(measures),
                        getMax(measures)));
        System.out.println(BREAK_STR);

    }

    public QuickPerf(Config config) {
        this.config = config;
    }

    private void runSampling() {
        System.out.println("Running Sampling... ");

        String connectionUrl = createConnectionURL(config);

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            try (Statement statement = connection.createStatement()) {
                boolean hasResults = statement.execute(config.getSamplingQuery());

                if (hasResults == false) {
                    System.out.println("Nothing sampled");
                    return;
                }

                ResultSet rs = statement.getResultSet();
                while (rs.next()) {
                    String value = rs.getString(1);
                    sampledValueList.add(value);
                }

                System.out.println(String.format("Finished sampling %s records", sampledValueList.size()));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String createConnectionURL(Config config) {
        if (config.isIsEmulator()) {
            return String.format(
                    "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s?autoConfigEmulator=true",
                    config.getProject(), config.getInstance(), config.getDatabase());
        } else {
            return String.format(
                    "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
                    config.getProject(), config.getInstance(), config.getDatabase());
        }
    }

    public void run() {
        if (config.getBatchSize() > 0) {
            int val = (int) Math.ceil((double) config.getIterations() / config.getBatchSize());
            measures = new float[val];
        } else {
            measures = new float[config.getIterations()];
        }

        String connectionUrl = createConnectionURL(config);

        try {
            Connection connection = DriverManager.getConnection(connectionUrl);

            // determin database dialect to set right tagging syntax
            boolean isGoogleSQL;
            if (connection.unwrap(CloudSpannerJdbcConnection.class).getDialect()
                    .equals(Dialect.GOOGLE_STANDARD_SQL)) {
                isGoogleSQL = true;
            } else {
                isGoogleSQL = false;
            }

            connection.setAutoCommit(false);

            // if there is DML switch to r/w transaction mode and apply transaction tagging.
            // Otherwise set to read-only mode.
            if (config.getQuery().contains("INSERT") ||
                    config.getQuery().contains("UPDATE")
                    || config.getQuery().contains("DELETE")) {
                // read-write
                connection.createStatement().execute("SET TRANSACTION READ WRITE");

                if (isGoogleSQL) {
                    connection.createStatement().execute(String.format("SET TRANSACTION_TAG = '%s'", DEFAULT_TAG));
                } else {
                    connection.createStatement()
                            .execute(String.format("SET SPANNER.TRANSACTION_TAG = '%s'", DEFAULT_TAG));
                }

            } else {
                // read-only
                // connection.createStatement().execute("SET TRANSACTION READ ONLY");
                connection.setAutoCommit(true);
            }

            PreparedStatement statement = connection.prepareStatement(config.getQuery());
            int batchCounter = config.getBatchSize();
            int batchRound = 0;

            for (int i = 0; i < config.getIterations(); i++) {
                if (config.getBatchSize() == 0) {
                    // single statements
                    try {
                        if (config.getQueryParams() != null) {
                            statement = parametrizeStatement(statement, config.getQueryParams());
                        }

                        if (isGoogleSQL) {
                            connection.createStatement().execute(String.format("SET STATEMENT_TAG='%s'",
                                    DEFAULT_TAG));
                        } else {
                            connection.createStatement().execute(String.format("SET SPANNER.STATEMENT_TAG='%s'",
                                    DEFAULT_TAG));
                        }

                        boolean hasResults = false;
                        long start = 0;
                        long stop = 0;
                        if (connection.getAutoCommit()) {
                            // read-only
                            start = System.nanoTime();
                            hasResults = statement.execute();
                            stop = System.nanoTime() - start;
                        } else {
                            start = System.nanoTime();
                            hasResults = statement.execute();
                            connection.commit();
                            stop = System.nanoTime() - start;
                        }

                        if (hasResults) {
                            statement.getResultSet().close();
                        }

                        measures[i] = stop / 1000000;
                        progress++;
                    } catch (Exception e) {
                        if (e.getMessage().contains("ALREADY_EXISTS:")) {
                            System.out.println("duplicate key - retry");
                            i--;
                        } else {
                            throw e;
                        }
                    }
                } else if (config.getQuery().contains("INSERT") ||
                        config.getQuery().contains("UPDATE")
                        || config.getQuery().contains("DELETE")) {
                    // batching
                    try {
                        if (config.getQueryParams() != null) {
                            statement = parametrizeStatement(statement, config.getQueryParams());
                        }

                        statement.addBatch();

                        if (batchCounter == 0 || i == config.getIterations() - 1) {

                            if (isGoogleSQL) {
                                connection.createStatement().execute(String.format("SET STATEMENT_TAG='%s'",
                                        DEFAULT_TAG));
                            } else {
                                connection.createStatement().execute(String.format("SET SPANNER.STATEMENT_TAG='%s'",
                                        DEFAULT_TAG));
                            }

                            long start = 0;
                            long stop = 0;
                            if (connection.getAutoCommit()) {
                                // read-only
                                start = System.nanoTime();
                                int[] cnt = statement.executeBatch();
                                stop = System.nanoTime() - start;
                            } else {
                                start = System.nanoTime();
                                int[] cnt = statement.executeBatch();
                                connection.commit();
                                stop = System.nanoTime() - start;
                            }

                            batchCounter = config.getBatchSize();

                            measures[batchRound] = stop / 1000000;
                            batchRound++;
                        }

                        progress++;
                        batchCounter--;
                    } catch (Exception e) {
                        if (e.getMessage().contains("ALREADY_EXISTS:")) {
                            System.out.println("duplicate key - retry");
                            i--;
                        } else {
                            throw e;
                        }
                    }
                } else {
                    System.out.println("Batching is only allowed for DML. Set batchSize=0 to disable batching.");
                    System.exit(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public float[] getMeasures() {
        return measures;
    }

    public static Option addOption(String option, String longOption, boolean hasArgs, String desc) {
        Option opt = new Option(option, longOption, hasArgs, desc);
        opt.setRequired(true);

        return opt;
    }

    public static Option addOption(String option, String longOption, boolean hasArgs, String desc, boolean required) {
        Option opt = new Option(option, longOption, hasArgs, desc);
        opt.setRequired(required);

        return opt;
    }

    public static float calcPerc(float[] nums, double percentile) {
        int n = nums.length;
        Arrays.sort(nums);

        double index = (percentile / 100) * (n - 1);

        if (index == Math.floor(index)) {
            return nums[(int) index];
        } else {
            int lowerIndex = (int) Math.floor(index);
            int upperIndex = (int) Math.ceil(index);
            float lowerValue = nums[lowerIndex];
            float upperValue = nums[upperIndex];
            return (float) ((1 - (index - lowerIndex)) * lowerValue + (index - lowerIndex) * upperValue);
        }
    }

    public static float getMax(float[] measures) {
        if (measures == null || measures.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }

        Arrays.sort(measures);
        return measures[measures.length - 1];
    }

    public static float getMin(float[] measures) {
        if (measures == null || measures.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }

        Arrays.sort(measures);
        return measures[0];
    }

    public static float[] appendFloatArray(float[] originalArray, float[] elementsToAppend) {
        int originalLength = originalArray.length;
        int elementsLength = elementsToAppend.length;

        float[] resultArray = new float[originalLength + elementsLength];
        System.arraycopy(originalArray, 0, resultArray, 0, originalLength);
        System.arraycopy(elementsToAppend, 0, resultArray, originalLength, elementsLength);

        return resultArray;
    }

    private PreparedStatement parametrizeStatement(PreparedStatement statement, List<QueryParam> paramList)
            throws SQLException {
        for (QueryParam param : paramList) {
            if (param.getValue().contains("#i")) {
                // integer plus integer with custom range
                int value = replaceInt(param.getValue());
                statement.setInt(param.getOrder(), value);
            } else if (param.getValue().contains("#d")) {
                // double
                Double value = replaceDouble(param.getValue());
                statement.setDouble(param.getOrder(), value);
            } else if (param.getValue().contains("#s")) {
                // String
                String value = replaceString(param.getValue());
                statement.setString(param.getOrder(), value);
            } else if (param.getValue().contains("#ps")) {
                // Sampled Query - String
                String value = replaceSampleQueryString(param.getValue());
                statement.setString(param.getOrder(), value);
            } else if (param.getValue().contains("#pi")) {
                // Sampled Query - Integer
                Long value = replaceSampleQueryInt(param.getValue());
                statement.setLong(param.getOrder(), value);
            }
        }

        return statement;
    }

    private int replaceInt(String value) {
        Faker f = new Faker();
        // integer with min, max
        String pattern = "#i\\((\\d+),(\\d+)\\)#";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(value);

        while (matcher.find()) {
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));

            return f.number().numberBetween(min, max);
        }

        return Integer.valueOf(value.replaceFirst("#i", String.valueOf(new SecureRandom().nextInt(Integer.MAX_VALUE))));
    }

    private Double replaceDouble(String value) {
        Faker f = new Faker();

        return Double.valueOf(value.replaceFirst("#i", String.valueOf(f.number().randomDouble(2, 0,
                999999999))));
    }

    private String replaceString(String value) {
        return value.replaceFirst("#s", UUID.randomUUID().toString());
    }

    private String replaceSampleQueryString(String value) {
        int randomIndex = new Random().nextInt(sampledValueList.size());
        return value.replaceFirst("#ps", sampledValueList.get(randomIndex));
    }

    private Long replaceSampleQueryInt(String value) {
        int randomIndex = new Random().nextInt(sampledValueList.size());

        return Long.parseLong(value.replaceFirst("#pi", sampledValueList.get(randomIndex)));
    }

    public int getProgress() {
        return progress;
    }

    public static void writeMeasuresToFile(float[] array, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (float value : array) {
                writer.write(Float.toString(value));
                writer.newLine();
            }
        }
    }
}
