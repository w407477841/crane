package com.xywg.equipment.monitor.modular.dlt.factory;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

public class BaseFactory {

    public static String getTableName(Class<? extends Model> clazz){
        TableName table  =   clazz.getAnnotation(TableName.class);
        if(table == null) {
            throw new RuntimeException();
        }

        return table.value()+"_"+ DateUtil.format(new Date(),"yyyyMM");
    }

    /**
     * 根据输入日期获取表名
     * @author sj
     * @param clazz
     * @param date
     * @return
     */
    public static String getTableName(Class<? extends Model> clazz,Date date){
        TableName table  =   clazz.getAnnotation(TableName.class);
        if(table == null) {
            throw new RuntimeException();
        }
        return table.value()+"_"+ DateUtil.format(date,"yyyyMM");
    }

}
