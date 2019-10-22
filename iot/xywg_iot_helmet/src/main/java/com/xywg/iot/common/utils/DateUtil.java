package com.xywg.iot.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	
	public static final String DATE_FULL_STRING = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PART_STRING = "yyyy-MM-dd HH:mm";
    public static final String DATE_SMALL_STRING = "yyyy-MM-dd";
    
    public static String getCurrentFullDate(){
		return new SimpleDateFormat(DATE_FULL_STRING).format(Calendar.getInstance().getTime());
    }
    
    public static String getCurrentPartDate(){
		return new SimpleDateFormat(DATE_PART_STRING).format(Calendar.getInstance().getTime());
    }
    
    public static String getCurrentPartDate(int year){
    	Calendar instance = Calendar.getInstance();
    	instance.add(Calendar.YEAR, 1);
		return new SimpleDateFormat(DATE_PART_STRING).format(instance.getTime());
    }
    
    public static String getCurrentSmallDate(){
		return new SimpleDateFormat(DATE_SMALL_STRING).format(Calendar.getInstance().getTime());
    }
    
    public static long getCurrentTimeStamp(){
    	return Calendar.getInstance().getTimeInMillis();
    }
    
    public static Map<String,String> getCurrentIntervalDate(int seconds){
    	Calendar c = Calendar.getInstance();
    	String start = new SimpleDateFormat(DATE_FULL_STRING).format(c.getTime());
    	c.add(Calendar.SECOND, seconds);
    	String end = new SimpleDateFormat(DATE_FULL_STRING).format(c.getTime());
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("start", start);
    	map.put("end", end);
    	return map;
    }
    
    public static String Timestamp2String(Timestamp timestamp) {
		return new SimpleDateFormat(DATE_FULL_STRING).format(timestamp);
    }
    
}
