package com.terry.archer.cache;

public abstract class AbstractCache implements ICache{

    /**
     * 需要默认的空参构造函数
     */
    public AbstractCache(){}

    /**
     * 默认缓存名称
     * @return
     */
    @Override
    public String getCacheName() {
        return this.getClass().getName();
    }

    /**
     * 默认不存在的缓存不允许动态加载到缓存集合重
     * @return
     */
    public boolean allowDynamic() {
        return false;
    }

}
