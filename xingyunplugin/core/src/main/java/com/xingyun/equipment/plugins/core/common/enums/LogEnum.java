package com.xingyun.equipment.plugins.core.common.enums;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:48 2019/7/2
 * Modified By : wangyifei
 */
public enum LogEnum {



    COMMON("%s--%s--%s");
    private static String HEADER = "enviroment";
    LogEnum(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public String format(String type,String ... content) {
        StringBuilder sb = new StringBuilder();
        if(content!=null){
            for( String c:content){
                sb.append(c);
            }
        }
        return String.format(this.getMsg(),HEADER,type,sb.toString());
    }

}
