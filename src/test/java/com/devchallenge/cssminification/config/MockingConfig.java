package com.devchallenge.cssminification.config;

import com.devchallenge.cssminification.browser.WebBrowserCssLogTracker;
import com.devchallenge.cssminification.service.CssMinificationService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

public class MockingConfig {

    @Bean
    public WebBrowserCssLogTracker webBrowserLogger() {
        return Mockito.mock(WebBrowserCssLogTracker.class);
    }

    @Bean
    public CssMinificationService cssMinificationService() {
        return Mockito.mock(CssMinificationService.class);
    }
}
