package com.devchallenge.cssminification.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final int DEFAULT_TTL_SEC = 60;
    public static final String CSS_COMPRESSOR_CACHE = "css-minification";

    @Value("${cache.ttl:" + CacheConfig.DEFAULT_TTL_SEC + "}")
    private String cacheTtl;


    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        NetworkConfig network = config.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        config
                .getMapConfig(CSS_COMPRESSOR_CACHE)
                .setTimeToLiveSeconds(Integer.parseInt(cacheTtl))
                .setMaxSizeConfig(new MaxSizeConfig(25,
                        MaxSizeConfig.MaxSizePolicy.FREE_HEAP_PERCENTAGE));

        return Hazelcast.newHazelcastInstance(config);
    }


    @Bean
    public CacheManager hazelCastCacheManager() {
        return new HazelcastCacheManager(hazelcastInstance());
    }

}