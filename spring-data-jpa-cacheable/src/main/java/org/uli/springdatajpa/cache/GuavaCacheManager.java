package org.uli.springdatajpa.cache;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleCacheManager;

import com.google.common.cache.CacheBuilder;

@Slf4j
public class GuavaCacheManager extends SimpleCacheManager {
    
    public GuavaCacheManager() {
        super();
    }
    
    @Override
    public Cache getCache(String name) {
        Cache result = super.getCache(name);
        if (result == null) {
            log.debug(".: Creating a new cache '{}'", name);
            com.google.common.cache.Cache<Object, Cache.ValueWrapper> newCache = CacheBuilder.newBuilder().build();
            org.uli.springdatajpa.cache.GuavaCache guavaCache = new org.uli.springdatajpa.cache.GuavaCache(name, newCache);
            this.addCache(guavaCache);
            result = super.getCache(name);
        }
        return result;
    }
}
