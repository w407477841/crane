package com.xingyun.equipment.cache;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:42 2019/6/26
 * Modified By : wangyifei
 */
public interface CacheLoader<T> {
    /**
     * 加载数据
     * @return
     */
    T load();

}
