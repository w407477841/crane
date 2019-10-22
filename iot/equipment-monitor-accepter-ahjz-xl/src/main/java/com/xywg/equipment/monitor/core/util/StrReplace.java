package com.xywg.equipment.monitor.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:20 2018/10/29
 * Modified By : wangyifei
 */
public class StrReplace {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrReplace.class);

    /**
     * 替换 内容key
     * @param data
     * @param oldkey
     * @param newKey
     * @return
     */
    public static String replaceKey(String data, String oldkey,String newKey){
        return data.replace(","+oldkey+":",","+ newKey+":");
    }

    /**
     *  替换 头
     * @param data
     * @param oldkey
     * @param newKey
     * @return
     */

    public static String replaceHead(String data, String oldkey,String newKey){
        return data.replace(oldkey+":", newKey+":");
    }

    /**
     * 修改空数据
     * @param data
     * @return
     */
    public static String removeBlank(String data){

        data  = data.replace(":,",":0,");
        if(data.endsWith(":")){
            data += "0";
        }
        return data;
    }


}
