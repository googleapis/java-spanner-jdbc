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

import java.util.List;

public class ProgressTracker extends Thread {
    private static int SLEEP_TIME_INIT = 2000;
    private static int SLEEP_TIME_POLL = 200;

    private final List<QuickPerf> threadList;

    private int maxIt;
    private int currentIt = 0;

    public ProgressTracker(List<QuickPerf> threadList, int maxIt) {
        this.threadList = threadList;
        this.maxIt = maxIt;
    }

    public void run() {
        sleep(SLEEP_TIME_INIT);
        while (currentIt < maxIt) {
            currentIt = 0;
            for (QuickPerf thread : threadList) {
                currentIt = currentIt + thread.getProgress();

                int percent = (int) Math.ceil(((double) currentIt / maxIt) * 100.0);
                print_progress(percent);
            }
            sleep(SLEEP_TIME_POLL);
        }
        print_progress(100); 
    }

    public void print_progress(int percent) {
        StringBuilder bar = new StringBuilder("Progress: [");

        for (int i = 0; i < 50; i++) {
            if (i < (percent / 2)) {
                bar.append("=");
            } else if (i == (percent / 2)) {
                bar.append(">");
            } else {
                bar.append(" ");
            }
        }

        bar.append("]   " + percent + "%     ");
        System.out.print("\r" + bar.toString());
    }

    private void sleep(int sleeptime) {
        try {
            Thread.sleep(sleeptime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
