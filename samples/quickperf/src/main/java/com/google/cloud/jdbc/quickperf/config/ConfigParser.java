package com.google.cloud.jdbc.quickperf.config;


import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigParser {

    public static Config parseConfigFile(String configFile) throws StreamReadException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(new File(configFile), Config.class);
    }
}
