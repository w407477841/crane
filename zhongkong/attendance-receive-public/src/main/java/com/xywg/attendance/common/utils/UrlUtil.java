package com.xywg.attendance.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * url转换成map
 * @author hjy
 */
public class UrlUtil {

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

}
