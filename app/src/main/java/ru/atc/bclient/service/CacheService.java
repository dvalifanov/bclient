package ru.atc.bclient.service;

import org.springframework.stereotype.Service;

@Service
public class CacheService {
    public int getSize(String entityName) {
        return net.sf.ehcache.CacheManager.ALL_CACHE_MANAGERS.get(0).getCache(entityName).getSize();
    }
}
