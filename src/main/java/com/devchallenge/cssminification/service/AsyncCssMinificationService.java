package com.devchallenge.cssminification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class AsyncCssMinificationService {


    @Autowired
    private CacheableCssMinificationService cacheableCssMinificationService;

    @Async
    public Future<String> minifyCss(String url, boolean noCache){
        String result = cacheableCssMinificationService.minifyCss(url, noCache);
        return new AsyncResult<>(result);
    }


}
