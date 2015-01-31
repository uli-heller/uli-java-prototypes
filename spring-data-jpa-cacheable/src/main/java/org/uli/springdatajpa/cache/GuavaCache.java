package org.uli.springdatajpa.cache;

import org.springframework.cache.support.SimpleValueWrapper;

public class GuavaCache implements org.springframework.cache.Cache {

    private final String name;
    private final com.google.common.cache.Cache<Object, ValueWrapper> store;

    public GuavaCache(String name, com.google.common.cache.Cache<Object, ValueWrapper> cache) {
        this.name = name;
        this.store = cache;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public com.google.common.cache.Cache<Object, ValueWrapper> getNativeCache() {
        return this.store;
    }
    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper value = this.store.getIfPresent(key);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        this.store.put(key, new SimpleValueWrapper(value));
    }

    @Override
    public void evict(Object key) {
        this.store.invalidate(key);
    }

    @Override
    public void clear() {
        this.store.invalidateAll();
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        @SuppressWarnings("unchecked")
        T result = (T) this.get(key).get();
        return result;
    }
}
