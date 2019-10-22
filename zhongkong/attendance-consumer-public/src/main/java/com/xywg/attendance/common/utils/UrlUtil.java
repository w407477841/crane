package com.xywg.attendance.common.utils;


import com.xywg.attendance.common.model.DataMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * url转换成map
 */
public class UrlUtil {

   /* *//**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param url
     * @author lzf
     */
    public static Map<String, String> changeUrl(String url) {
        Map<String, String> mapRequest = new HashMap<>(6);
        if (url.contains("?")) {

        } else {
            if (url.contains("&") && url.contains("=")) {
                String[] arrSplit = url.split("&");
                for (String strSplit : arrSplit) {
                    String[] arrSplitEqual = strSplit.split("=");
                    //解析出键值
                    if (arrSplitEqual.length > 1) {
                        //正确解析
                        mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
                    } else {
                        //只有参数没有值，不加入
                        mapRequest.put(arrSplitEqual[0], "");
                    }
                }
            }
        }
        return mapRequest;
    }


    /**
     * @param message
     * @param firstSpace  第一分隔符
     * @param secondSpace 第二分隔符
     * @return
     */
    public static Map<String, String> handleSeparate(String message, String firstSpace, String secondSpace) {
        Map<String, String> map = new HashMap<>(2);
        if (message.contains(firstSpace) && message.contains(secondSpace)) {
            String[] mess = message.split(firstSpace);
            for (String m : mess) {
                if (m.contains(secondSpace)) {
                    String[] messages = m.split(secondSpace);
                    if (messages.length > 1) {
                        map.put(messages[0], messages[1]);
                    } else {
                        map.put(messages[0], "");
                    }
                }
            }
        }
        return map;
    }


    /**
     * @param message
     * @param firstSpace  第一分隔符
     * @param secondSpace 第二分隔符
     * @return
     */
    public static List<DataMessage> handleSeparateList(String message, String firstSpace, String secondSpace, String thirdlySpace, String fourSpace) {

        List<DataMessage> list = new ArrayList<>();
        String[] messages = message.split(firstSpace);
        for (String str : messages) {
            DataMessage dataMessage = new DataMessage();
            Integer secondSpaceIndex = str.indexOf(secondSpace);
            if (secondSpaceIndex > 0 && secondSpaceIndex + 1 < str.length()) {
                String secondStr = str.substring(0, secondSpaceIndex);
                String secondStrTwo = str.substring(secondSpaceIndex + 1);
                dataMessage.setKeyName(secondStr);
                Map<String, String> fourSpaceMap = new HashMap<>(1);
                for (String thirdlyString : secondStrTwo.split(thirdlySpace)) {
                    Integer index = thirdlyString.indexOf(fourSpace);
                    if (index > 0) {
                        String thirdlyStrings0 = thirdlyString.substring(0, index);
                        if (index + 1 == thirdlyString.length()) {
                            fourSpaceMap.put(thirdlyStrings0, "");
                        } else {
                            fourSpaceMap.put(thirdlyStrings0, thirdlyString.substring(index + 1));
                        }
                    }
                }
                dataMessage.setMap(fourSpaceMap);
            }
            list.add(dataMessage);
        }

        return list;
    }


}
