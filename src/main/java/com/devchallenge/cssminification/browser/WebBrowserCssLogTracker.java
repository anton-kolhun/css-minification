package com.devchallenge.cssminification.browser;

import java.util.ArrayList;
import java.util.List;

public class WebBrowserCssLogTracker {


    private static int CHROME_DRIVER_POOL_SIZE = 10;
    private static int CHROME_DRIVER_POOL_AVAILABILITY_TIMEOUT = 100; //unit = second
    private static final String INTERCEPTION_CRITERIA = "text/css";

    private List<ChromeDriverLogTracker> chromeDriverLogTrackers;


    public WebBrowserCssLogTracker() {
        init();
    }


    private void init() {
        chromeDriverLogTrackers = new ArrayList<>();
        for (int i = 0; i < CHROME_DRIVER_POOL_SIZE; i++) {
            chromeDriverLogTrackers.add(new ChromeDriverLogTracker(INTERCEPTION_CRITERIA));
        }
    }


    public void destroy() {
        for (ChromeDriverLogTracker chromeDriverLogTracker : chromeDriverLogTrackers) {
            chromeDriverLogTracker.destroy();
        }
    }

    public List<String> fetchNetworkLogs(String url) {
        ChromeDriverLogTracker chromeLogTracker = getAvailableLogTracker();
        chromeLogTracker.trackLogs(url);
        List<String> logs = chromeLogTracker.getLogs();
        chromeLogTracker.endLogging();
        return logs;
    }


    private ChromeDriverLogTracker getAvailableLogTracker() {
        ChromeDriverLogTracker chromeDriver = getAvailableChromeDriverLogTracker();
        if (chromeDriver != null) {
            return chromeDriver;
        }
        int waitingCycles = 100;
        try {
            for (int i = 0; i < waitingCycles; i++) {
                Thread.sleep(CHROME_DRIVER_POOL_AVAILABILITY_TIMEOUT * 1000 / waitingCycles);
                chromeDriver = getAvailableChromeDriverLogTracker();
                if (chromeDriver != null) {
                    return chromeDriver;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Error occurred while accessing chrome driver", e);
        }
        throw new RuntimeException("There are no available initialized chrome drivers...");
    }

    private ChromeDriverLogTracker getAvailableChromeDriverLogTracker() {
        for (ChromeDriverLogTracker chromeDriver : chromeDriverLogTrackers) {
            if (chromeDriver.startLogging()) {
                return chromeDriver;
            }
        }
        return null;
    }


}
