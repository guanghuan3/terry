package com.terry.archer.cache;

import com.terry.archer.utils.CommonUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存操作工具类
 */
@Service
public class MemoryCacheSupport {

    /**
     * 存在内存中的缓存对象信息
     * {缓存类=缓存对象实例}格式存储
     */
    private static Map<Class<? extends ICache>, ICache> memoryCache = new HashMap<>();

    /**
     * 存在内存中的缓存数据信息
     * {缓存名称=缓存数据}格式存储
     */
    private static Map<String, Object> memoryData = new HashMap<>();

    /**
     * 根据ICache为父类的缓存类刷新操作
     * 当缓存类型不存在实例时，可以通过缓存类子类的allowDynamic参数的返回值判断是否动态加载缓存类到缓存数据中
     * @param cacheType
     * @return
     */
    public Object refreshCache(Class<? extends ICache> cacheType) {
        ICache cache = memoryCache.get(cacheType);
        if (CommonUtil.isEmpty(cache)) {
            // 动态添加缓存和缓存数据
            try {
                cache = cacheType.newInstance();
                if (cache.allowDynamic()) {
                    memoryCache.put(cacheType, cache);
                } else {
                    // 如果不允许动态创建缓存  则直接返回空
                    return null;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Object cacheData = cache.refreshCache();
        memoryData.put(cache.getCacheName(), cacheData);
        return cacheData;
    }

    /**
     * 耍新所有缓存数据
     * @return
     */
    public boolean refreshAllCacheData() {
        Collection<ICache> caches = memoryCache.values();
        if (CommonUtil.isNotEmpty(caches)) {
            Map<String, Object> tempCacheData = new HashMap<>();
            for (ICache cache : caches) {
                tempCacheData.put(cache.getCacheName(), cache.refreshCache());
            }

            if (CommonUtil.isNotEmpty(tempCacheData)) {
                memoryData.clear();
                memoryData.putAll(tempCacheData);
                return true;
            }
        }
        return false;
    }

    public Object getCacheData(Class<? extends ICache> cacheType) {
        ICache cache = memoryCache.get(cacheType);
        if (CommonUtil.isNotEmpty(cache)) {
            return getCacheData(cache.getCacheName());
        }
        return null;
    }

    public Object getCacheData(String cacheName) {
        if (memoryData.containsKey(cacheName)) {
            return memoryData.get(cacheName);
        } else {
            return null;
        }
    }

    public void clearCacheData() {
        memoryData.clear();
    }

    public void clearCacheData(String cacheName) {
        if (memoryData.containsKey(cacheName)) {
            memoryData.remove(cacheName);
        }
    }
}
