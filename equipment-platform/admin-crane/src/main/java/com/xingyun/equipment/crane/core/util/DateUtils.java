package com.xingyun.equipment.crane.core.util;

import cn.hutool.core.date.DateUtil;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author hujingyun
 */
@SuppressWarnings("all")
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}
	
	/**
	 * 获取YYYYMM格式
	 *
	 * @return
	 */
	public static String getYearMonth() {
		return formatDate(new Date(), "yyyy-MM");
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getLastMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		String mon = formatDate(m, "yyyy-MM");
		return mon;
	}
	
	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getLast2Month() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -2);
		Date m = c.getTime();
		String mon = formatDate(m, "yyyyMM");
		return mon;
	}
	
	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getLastYearMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -1);
		Date m = c.getTime();
		String mon = formatDate(m, "yyyy-MM");
		return mon;
	}
	
	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getLastYear2Month() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -1);
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		String mon = formatDate(m, "yyyyMM");
		return mon;
	}
	
	/**
	 *获取上个月倒数第n天的日期
	 *
	 * @return
	 */
	public static String getLastMonthDay(Integer n) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Calendar cale = Calendar.getInstance();   
		cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
		//cale.getTime();
		
		c.setTime(cale.getTime());
		//c.add(Calendar.MONTH, -1);
		c.add(Calendar.DATE, -n);
		Date m = c.getTime();
		String date = formatDate(m, "yyyy-MM-dd");
		return date;
	}
	
	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}
	
	/**
	 * 获取DD格式
	 *
	 * @return
	 */
	public static Integer getToday() {
		return Integer.valueOf(formatDate(new Date(), "dd"));
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 能抛出异常的解析日期
	 * @param str
	 * @return
	 * @throws ParseException
     */
	public static Date convertDate(Object str) throws ParseException {
		if (str == null) {
			return null;
		}
		return parseDate(str.toString(), parsePatterns);
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 获取当前时间 i分钟后的时间
	 * @param i  分钟
	 * @return
	 */
	public  static  Date getFutureDate(int i){
		Calendar now=Calendar.getInstance();
		now.add(Calendar.MINUTE,i);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr=sdf.format(now.getTimeInMillis());
		Date date= null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// 获得本月第一天0点时间
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}


	/**
	 * 返回两个时间点内涉及的月份  yyyyMM
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author:  wangyifei
	 */
	public static List<String> getMonthsBetween(Date startTime,Date endTime){

		long betweenMonth = cn.hutool.core.date.DateUtil.betweenMonth(endTime, startTime, true);
		List<String> months = new ArrayList<>();
		months.add(cn.hutool.core.date.DateUtil.format(startTime,"yyyyMM"));
		for (long i = 0; i < betweenMonth; i++) {
			months.add(cn.hutool.core.date.DateUtil.format(DateUtil.offsetMonth(startTime, (int) i + 1),"yyyyMM"));
		}
		return months;
	}
	
	/** 
	* 根据日期获得本周所有日期（周一到周日以及上周日） 
	*  
	* @return 
	* @author hy 
	* @throws Exception 
	*/
	public static List<String> getWeekDaysByDate(Date date) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了 
		
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {  
	        cal.add(Calendar.DAY_OF_MONTH, -1);  
	     }  
		
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		String monday = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 1);
		String tuesday = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 1);
		String wednesday = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 1);
		String thursday = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 1);
		String friday = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 1);
		String saturday = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 1);
		String sunday = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, -7);
		String lastSunday = sdf.format(cal.getTime());
		List<String> list = new ArrayList<>();
		list.add(lastSunday);
		list.add(monday);
		list.add(tuesday);
		list.add(wednesday);
		list.add(thursday);
		list.add(friday);
		list.add(saturday);
		list.add(sunday);
		return list;
		
	}
	
	/** 
	* 根据日期获得前8天 
	*  
	* @return 
	* @author hy 
	* @throws Exception 
	*/
	public static List<String> get8DaysBefore(Date date) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		List<String> list = new ArrayList<>();
		
		cal.add(Calendar.DATE, -8);
		String day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		
		return list;
		
	}
	/** 
	* 根据日期获得前7天 
	*  
	* @return 
	* @author cmy
	* @throws Exception 
	*/
	public static List<String> get7DaysBefore(Date date) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		List<String> list = new ArrayList<>();
		
		cal.add(Calendar.DATE, -7);
		String day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
		cal.add(Calendar.DATE, 1);
		day = sdf.format(cal.getTime());
		list.add(day);
	
		return list;
		
	}

	/**
	 * 传一个小时数 查询当前时间前的几个小时 但是不能跨月 如果跨月就选当前月的0时
	 * @param hour
	 * @return
	 */
	public static String getBeginDate(Integer hour){
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date d=new Date();
		//开始时间
		Long time=d.getTime()-(1000*3600*hour);
		Calendar c=Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH,0);
		c.set(Calendar.HOUR,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		Long deadLine=c.getTimeInMillis();
		if(time<deadLine){
			return sdf.format(deadLine);
		}else{
			return sdf.format(time);
		}
	}

	public static List<String> getZhengShiFen() throws ParseException{
        List<String> list=new ArrayList<>();
        Date now=new Date();
        SimpleDateFormat sdfd=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
        String str=sdf.format(now).toString().substring(0, 1)+"0";
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.MINUTE, Integer.parseInt(str));
        calendar.set(Calendar.SECOND,0);
        Date d=new Date();
        d.setTime(calendar.getTimeInMillis());
        Date t=new Date();
        Long time=t.getTime()-1000*3600;
        t.setTime(time);
		for(int i=0;i<6;i++){
			list.add(sdfd.format(d.getTime()));
			d.setTime(d.getTime()-1000*60*10);
		}
		return list;
	}

	public static List<String> getZhengDian(){
		SimpleDateFormat sdfd=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date now=new Date();
		Calendar c=Calendar.getInstance();
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		now.setTime(c.getTimeInMillis());
		List<String> list=new ArrayList<>();
		for(int i=0;i<24;i++){
			list.add(sdfd.format(now));
			now.setTime(now.getTime()-1000*60*60);
		}
		return list;
	}
	
	public static List<String> getZhengDianByDate(String now) throws ParseException{
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   Date date = null; 
		   try { 
		    date = format.parse(now); 
		   } catch (ParseException e) { 
		    e.printStackTrace(); 
		   } 
		   System.out.println(date); 
		   List<String> list=new ArrayList<>();
		   for(int i=0;i<24;i++){
				list.add(format1.format(date));
				date.setTime(date.getTime()+1000*60*60);
			}
		return list;
	}


}
