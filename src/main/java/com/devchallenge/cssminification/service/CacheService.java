package com.devchallenge.cssminification.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    public void removeCache(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (StringUtils.isEmpty(key)) {
            cache.clear();
        } else {
            cache.evict(key);
        }
    }

    public void removeCache(String cacheName) {
        removeCache(cacheName, null);
    }

}
