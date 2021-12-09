package com.terry.archer.cache;

import com.terry.archer.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisCacheSupport {

    @Autowired
    private RedisTemplate redisTemplate;

    private static Map<Class<? extends ICache>, ICache> cacheMap = new HashMap<>();

    public Object refreshCache(Class<? extends ICache> cacheType) {
        ICache cache = cacheMap.get(cacheType);
        if (CommonUtil.isEmpty(cache)) {
            try {
                cache = cacheType.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            if (cache.allowDynamic()) {
                cacheMap.put(cacheType, cache);
            }
        }

        Object data = cache.refreshCache();
        redisTemplate.opsForValue().set(cache.getCacheName(), data);
        return data;
    }

    public Object getCacheData(Class<? extends ICache> cacheType) {
        if (cacheMap.containsKey(cacheType)) {
            return null;
        } else {
            return null;
        }
    }

    public Object getCacheData(String cacheName) {
        return redisTemplate.opsForValue().get(cacheName);
    }

}
