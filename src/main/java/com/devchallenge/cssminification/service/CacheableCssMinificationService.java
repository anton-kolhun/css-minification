package com.devchallenge.cssminification.service;

import com.devchallenge.cssminification.config.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CacheableCssMinificationService extends CssMinificationService {

    @Autowired
    private CssMinificationService cssMinificationService;


    @Cacheable(value = CacheConfig.CSS_COMPRESSOR_CACHE, key = "#url", condition = "#noCache == false")
    public String minifyCss(String url, boolean noCache) {
        String result = cssMinificationService.minifyCss(url);
        return result;
    }

}
