package com.terry.archer.cache;

public interface ICache {

    /**
     * 获取缓存名称
     * @return
     */
    String getCacheName();

    /**
     * 刷新缓存或初始化数据
     * @return
     */
    Object refreshCache();

    /**
     * 是否可以动态添加成为新的缓存
     * @return
     */
    boolean allowDynamic();

}
