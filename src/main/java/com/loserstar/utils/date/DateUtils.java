/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.loserstar.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import jodd.datetime.JDateTime;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

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
		if (date==null) {
			formatDate = "";
		}else{
			if (pattern != null && pattern.length > 0) {
				formatDate = DateFormatUtils.format(date, pattern[0].toString());
			} else {
				formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
			}
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
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}
	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear(Date date) {
		return formatDate(date, "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth(Date date) {
		return formatDate(date, "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay(Date date) {
		return formatDate(date, "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 * @param str
	 * @return
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}

	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
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
	 * 获取时间戳
	 *JAVA时间戳长度是13位，如：1294890876859
	 *PHP时间戳长度是10位， 如：1294890859
	 * @param type 0 代表获取java时间戳，大于0 代表获取PHP时间戳
	 * @return
	 */
	public static String getTime(int index){
		String t = new Date().getTime()+"";
		if(index > 0){
			return t.substring(0, t.length()-3);
		}else{
			return t;
		}
	}

	/**
	 * 根据时间戳返回时间
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String getDateFromTimestamp(String time,String pattern){
	 	String date = new SimpleDateFormat(pattern).format(new Date(Long.parseLong(time) * 1000));
	 	return date;
	}

	/**
	 * 按月计算时间
	 *
	 * @param date
	 *            基本时间
	 * @param month
	 *            加、减的月数。正数是加月数，负数是减月数
	 * @return Date
	 * @autho bai
	 */
	public static Date caltMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		Date getDate = calendar.getTime();
		return getDate;
	}

	/**
	 *日期格式转换成时间戳格式
	 * 获取时间戳
	 *JAVA时间戳长度是13位，如：1294890876859
	 *PHP时间戳长度是10位， 如：1294890859
	 * @param type 0 代表获取java时间戳，大于0 代表获取PHP时间戳
	 * @return
	 */
	public static String getTimetamp(Date date,int index){
		String t = date.getTime()+"";
		if(index > 0){
			return t.substring(0, t.length()-3);
		}else{
			return t;
		}
	}

	/**
     * 增加日期中某类型的某数值。如增加日期
     * @param date 日期
     * @param dateType 类型
     * @param amount 数值
     * @return 计算后日期
     */
	private static Date addInteger(Date date, int dateType, int amount) {
		Date myDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(dateType, amount);
			myDate = calendar.getTime();
		}
		return myDate;
	}

	/**
	 * 增加日期的天数。失败返回null。
	 * @param date 日期
	 * @param dayAmount 增加数量。可为负数
	 * @return 增加天数后的日期
	 */
	public static Date addDay(Date date, int dayAmount) {
		return addInteger(date, Calendar.DATE, dayAmount);
	}

	/**
	 * 根据参照时间获取对应的时间区域
	 * @param periodType 时间区域类型
	 * @param d   24小时制  yyyy-MM-dd HH:mm:ss
	 * @return 开始时间  结束时间
	 */
	public static Date[] getDateStartEnd(int periodType, Date d){
		JDateTime jdt = d != null?new JDateTime(d):new JDateTime();
		return getDateStartEnd(periodType, jdt);
	}
	
	
	
	 /**
	  * 获得jdt日期与jdt周一相差的天数
	  * @param jdt
	  * @return
	  */
	private static  int getMondayPlus(JDateTime jdt) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(jdt.convertToDate());
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }
	
	 /**
	  * 获得jdt周- 周一的日期
	  * @param jdt
	  * @return
	  */
	private static Date getCurrentMonday(JDateTime jdt) {
        int mondayPlus = getMondayPlus(jdt);
        jdt.addDay(mondayPlus);
        return jdt.convertToDate();
    }
    
    /**
     * 获得jdt周- 周日  的日期
     * @param jdt
     * @return
     */
    private static Date getCurrentSunday(JDateTime jdt) {
        int sundayPlus = getMondayPlus(jdt)+6;
        jdt.addDay(sundayPlus);
        return jdt.convertToDate();
    }
    
    /**
     * 
     * 获取jdt 月-月初的日期
     * @param jdt
     * @return
     */
    private static Date getFirstDayOfMonth(JDateTime jdt){
    	 Calendar calendar = Calendar.getInstance();   
            calendar.setTime(jdt.convertToDate());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); 
            return calendar.getTime();
    }
    
    /**
     * 
     * 获取jdt 月-月末的日期
     * @param jdt
     * @return
     */
    public static Date getLastDayOfMonth(JDateTime jdt){   
        Calendar calendar = Calendar.getInstance();   
               calendar.setTime(jdt.convertToDate());
               calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
               return calendar.getTime();
}
    
    /**
     * 获取jdt 季度—季度开始日期
     * @param jdt
     * param flag flag=1,本季度；flag=2,上季度
     * @return
     */
    public static Date getFirstDayOfQuarter(JDateTime jdt,int flag) { 
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(jdt.convertToDate());
    	int currentMonth = cal.get(Calendar.MONTH) + 1;
    	switch(flag){
    	case 1:{//本季度
    	{if (currentMonth >= 1 && currentMonth <= 3) {
    		cal.set(Calendar.MONTH, 0);} 
    	else if (currentMonth >= 4 && currentMonth <= 6) {
    		cal.set(Calendar.MONTH, 3); }
    	else if (currentMonth >= 7 && currentMonth <= 9) {
    		cal.set(Calendar.MONTH, 6); }
    	else if (currentMonth >= 10 && currentMonth <= 12) {
    		cal.set(Calendar.MONTH,9); 
    	
    	}}
    	cal.set(Calendar.DATE, 1);
    	break;
    }
    	case 2:{//上季度
        	{if (currentMonth >= 1 && currentMonth <= 3) {
        		cal.set(Calendar.MONTH, 9); }
        	else if (currentMonth >= 4 && currentMonth <= 6) {
        		cal.set(Calendar.MONTH, 0); }
        	else if (currentMonth >= 7 && currentMonth <= 9) {
        		cal.set(Calendar.MONTH, 3); }
        	else if (currentMonth >= 10 && currentMonth <= 12) {
        		cal.set(Calendar.MONTH,6); 
        	cal.set(Calendar.DATE, 1);
    	}}
        	cal.set(Calendar.DATE, 1);
        	break;
    	}
    	}
    	return cal.getTime();
    	}
    
    /**
     * 获取jdt 季度—季度结束日期
     * @param flag flag=1,本季度；flag=2,上季度
     * @return
     */
    public static Date getEndtDayOfQuarter(JDateTime jdt,int flag) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(jdt.convertToDate());
    	int currentMonth = cal.get(Calendar.MONTH) + 1;
    	switch(flag){
    	case 1:{
    		{if (currentMonth >= 1 && currentMonth <= 3) { 
    	cal.set(Calendar.MONTH, 2); 
    	cal.set(Calendar.DATE, 31); 
    	} else if (currentMonth >= 4 && currentMonth <= 6) { 
    	cal.set(Calendar.MONTH, 5); 
    	cal.set(Calendar.DATE, 30); 
    	} else if (currentMonth >= 7 && currentMonth <= 9) { 
    	cal.set(Calendar.MONTH,8); 
    	cal.set(Calendar.DATE, 30); 
    	} else if (currentMonth >= 10 && currentMonth <= 12) { 
    	cal.set(Calendar.MONTH, 11); 
    	cal.set(Calendar.DATE, 31); 
    	} }
    		break;
    	}
    	case 2:{
    		{if (currentMonth >= 1 && currentMonth <= 3) { 
    	cal.set(Calendar.MONTH, 11); 
    	cal.set(Calendar.DATE, 31); 
    	} else if (currentMonth >= 4 && currentMonth <= 6) { 
    		cal.set(Calendar.MONTH, 2); 
        	cal.set(Calendar.DATE, 31); 
    	
    	} else if (currentMonth >= 7 && currentMonth <= 9) {
    		cal.set(Calendar.MONTH, 5); 
        	cal.set(Calendar.DATE, 30); 
    	
    	} else if (currentMonth >= 10 && currentMonth <= 12) { 
    		cal.set(Calendar.MONTH,8); 
        	cal.set(Calendar.DATE, 30); 
    	} }
    		break;
    	}
    	}
    	return cal.getTime(); 
    	}

    /**
	 * 根据参照时间获取对应的时间区域
	 * http://jodd.org/doc/jdatetime.html
	 * @param periodType 时间区域类型
	 * @param d   24小时制  yyyy-MM-dd HH:mm:ss
	 * @return 开始时间  结束时间
	 */
	public static Date[] getDateStartEnd(int periodType, JDateTime jdt){
		if(jdt==null)
			return null;
		Date[] startEnd=null;
		if(1 == periodType){//当天
			startEnd= new Date[]{jdt.convertToDate(),jdt.convertToDate()};
		}else if(2 == periodType){//前一天
			startEnd=new Date[]{jdt.addDay(-1).convertToDate(), jdt.convertToDate()};
		}else if(3 == periodType){//本周
			startEnd=new Date[]{getCurrentMonday(jdt),getCurrentSunday(jdt)};
		}else if(4 == periodType){//上周
			Date currentMonday=getCurrentMonday(jdt);
			JDateTime jdtPreMonday=new JDateTime(currentMonday).addDay(-7);
			startEnd=new Date[]{jdtPreMonday.convertToDate(),jdtPreMonday.addDay(6).convertToDate()};
		}else if(5 == periodType){//本月
			startEnd=new Date[]{getFirstDayOfMonth(jdt),getLastDayOfMonth(jdt)};
		}else if(6 == periodType){//上月
			Date currentFirstDayOfMonth=getFirstDayOfMonth(jdt);
			JDateTime jdtPreFirst=new JDateTime(currentFirstDayOfMonth).addMonth(-1);
			Date currentLastDayOfMonth=getLastDayOfMonth(jdt);
			JDateTime jdtPreLast=new JDateTime(currentLastDayOfMonth).addMonth(-1);
			startEnd=new Date[]{jdtPreFirst.convertToDate(),jdtPreLast.convertToDate()};
		}else if(7 == periodType){//本季度
			startEnd= new Date[]{getFirstDayOfQuarter(jdt,1),getEndtDayOfQuarter(jdt,1)};
		}else if(8 == periodType){//上季度
			startEnd= new Date[]{getFirstDayOfQuarter(jdt,2),getEndtDayOfQuarter(jdt,2)};
		}else if(9 == periodType){//今年
			jdt.setMonth(1);
			jdt.setDay(1);
			startEnd= new Date[]{jdt.convertToDate(), jdt.addYear(1).addDay(-1).convertToDate()};
		}else if(10 == periodType){//去年
			jdt.setMonth(1);
			jdt.setDay(1);
			startEnd= new Date[]{jdt.addYear(-1).convertToDate(), jdt.addYear(1).addDay(-1).convertToDate()};
		}else if(11 == periodType){//近3天
			startEnd=new Date[]{jdt.addDay(-2).convertToDate(),jdt.addDay(+2).convertToDate()};
		}else if(12 == periodType){//近一周
			startEnd= new Date[]{jdt.addDay(-6).convertToDate(),jdt.addDay(+6).convertToDate()};
		}else if(13 == periodType){//近一月 
			startEnd= new Date[]{jdt.addMonth(-1).addDay(+1).convertToDate(),jdt.addMonth(+1).addDay(-1).convertToDate()};
		}else if(14 == periodType){//近3月
			startEnd= new Date[]{jdt.addMonth(-2).addDay(+1).convertToDate(),jdt.addMonth(+2).addDay(-1).convertToDate()};
		}
		
		JDateTime jdtStart=null;
		JDateTime jdtEnd=null;
		if(startEnd!=null&&startEnd[0]!=null&&startEnd[1]!=null){
			jdtStart =new JDateTime(startEnd[0]);
			jdtEnd =new JDateTime(startEnd[1]);
			jdtStart.setTime(0, 0, 0, 000);
			jdtEnd.setTime(23, 59, 59, 999);
			startEnd[0]=jdtStart.convertToDate();
			startEnd[1]=jdtEnd.convertToDate();	
		}
		return startEnd;
	}
    
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(getTime(0));
//		System.out.println(getTime(1));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
//		System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		Date today=new Date();
		JDateTime jdtToday=new JDateTime(today);
		jdtToday=jdtToday.addDay(0);
		jdtToday=jdtToday.addYear(0);
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		System.out.println("today---"+sdf.format(today));
		System.out.println("jdttoday---"+jdtToday);
		Date[] returnDate=getDateStartEnd(12,jdtToday);
		System.out.println("start----"+sdf.format(returnDate[0]));
		System.out.println("end----"+sdf.format(returnDate[1]));
	}

}
