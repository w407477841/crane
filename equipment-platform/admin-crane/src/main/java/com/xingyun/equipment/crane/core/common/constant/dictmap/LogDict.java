package com.xingyun.equipment.core.enums.dictmap;

import com.xingyun.equipment.core.enums.dictmap.base.AbstractDictMap;

/**
 * 日志的字典
 *
 * @author wangcw
 * @date 2017-05-06 15:01
 */
public class LogDict extends AbstractDictMap {

    @Override
    public void init() {
        put("tips","备注");
    }

    @Override
    protected void initBeWrapped() {

    }
}
