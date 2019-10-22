package com.xywg.equipment.monitor.modular.whf.factory;

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



}
