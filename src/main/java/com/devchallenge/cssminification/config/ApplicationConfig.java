package com.devchallenge.cssminification.config;

import com.devchallenge.cssminification.browser.WebBrowserCssLogTracker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PreDestroy;

@Configuration
@Import({AsyncConfig.class, CacheConfig.class})
public class ApplicationConfig {

    @Value("${os.name}")
    private String osName;


    @Bean
    public WebBrowserCssLogTracker webBrowserLogger() {
        if (osName.toLowerCase().contains("mac")) {
            System.setProperty("webdriver.chrome.driver", "lib/macos/chromedriver");
        } else if (osName.toLowerCase().contains("windows")) {
            System.setProperty("webdriver.chrome.driver", "lib/windows/chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", "lib/linux/chromedriver");
        }
        return new WebBrowserCssLogTracker();
    }

    @PreDestroy
    public void destroy() {
        webBrowserLogger().destroy();
    }

}
