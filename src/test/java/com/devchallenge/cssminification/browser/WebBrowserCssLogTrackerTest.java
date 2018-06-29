package com.devchallenge.cssminification.browser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebBrowserCssLogTrackerTest {

    @InjectMocks
    private WebBrowserCssLogTracker webBrowserCssLogTracker = new WebBrowserCssLogTracker();

    @Mock
    List<ChromeDriverLogTracker> chromeDriverLogTrackers;

    @Before
    public void init() {
        webBrowserCssLogTracker.destroy();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void fetchNetworkLogs() {
        //setup
        String url = "fake_url";
        List<String> testLogs = Collections.singletonList("test_log");
        ChromeDriverLogTracker chromeDriverLogTracker = mock(ChromeDriverLogTracker.class);
        Iterator<ChromeDriverLogTracker> mockedIterator = mock(Iterator.class);
        when(mockedIterator.hasNext()).thenReturn(true);
        when(mockedIterator.next()).thenReturn(chromeDriverLogTracker);
        when(chromeDriverLogTracker.startLogging()).thenReturn(true);
        when(chromeDriverLogTrackers.iterator()).thenReturn(mockedIterator);
        when(chromeDriverLogTracker.getLogs()).thenReturn(testLogs);

        //execute
        List<String> resultLogs = webBrowserCssLogTracker.fetchNetworkLogs(url);

        //verify
        verify(chromeDriverLogTracker).startLogging();
        verify(chromeDriverLogTracker).trackLogs(url);
        verify(chromeDriverLogTracker).getLogs();
        verify(chromeDriverLogTracker).endLogging();
        assertEquals(testLogs, resultLogs);
    }
}