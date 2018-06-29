package com.devchallenge.cssminification.service;


import com.devchallenge.cssminification.browser.WebBrowserCssLogTracker;
import com.yahoo.platform.yui.compressor.CssCompressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Service
public class CssMinificationService {

    @Autowired
    private WebBrowserCssLogTracker webBrowserLogger;

    public String minifyCss(String url) {
        List<String> entries = webBrowserLogger.fetchNetworkLogs(url);

        StringWriter stringWriter = new StringWriter();
        for (String cssUrl : entries) {
            try (StringReader reader = new StringReader(cssUrl)) {
                CssCompressor cssCompressor = new CssCompressor(reader);
                cssCompressor.compress(stringWriter, Integer.MAX_VALUE);
            } catch (IOException e) {
                throw new RuntimeException("error occurred while compressing css file", e);
            }
        }
        return stringWriter.toString();

    }

}
