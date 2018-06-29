package com.devchallenge.cssminification.controller;

import com.devchallenge.cssminification.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/{cacheName}/remove")
    public void removeCache(@PathVariable String cacheName, @RequestParam(required = false) String key) {
        cacheService.removeCache(cacheName, key);
    }
}
