package com.loserstar.utils.date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/**
 * author: loserStar
 * date: 2018年7月25日下午2:44:53
 * remarks: 日期相关工具类
 */
public class LoserStarDateUtils {
	/**
	 * 带毫秒显示
	 */
	public static final String DISPLAY_MILLISECOND_FORMAT ="yyyy-MM-dd hh:mm:ss SSS";
	/**
	 * 带秒显示
	 */
	public final static String DISPLAY_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * UTC时间格式
	 */
	public final static String DISPLAY_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	/**
	 * GMT时间格式
	 */
	public final static String DISPLAY_GMT_FORMAT = "EEE MMM dd HH:mm:ss 'GMT' yyyy";
/*

字母	日期或时间元素	表示	示例
G	Era 标志符	Text	AD
y	年	Year	1996; 96
M	年中的月份	Month	July; Jul; 07
w	年中的周数	Number	27
W	月份中的周数	Number	2
D	年中的天数	Number	189
d	月份中的天数	Number	10
F	月份中的星期	Number	2
E	星期中的天数	Text	Tuesday; Tue
a	Am/pm 标记	Text	PM
H	一天中的小时数（0-23）	Number	0
k	一天中的小时数（1-24）	Number	24
K	am/pm 中的小时数（0-11）	Number	0
h	am/pm 中的小时数（1-12）	Number	12
m	小时中的分钟数	Number	30
s	分钟中的秒数	Number	55
S	毫秒数	Number	978
z	时区	General time zone	Pacific Standard Time; PST; GMT-08:00
Z	时区	RFC 822 time zone	-0800
 */
	
	/**
	 * 根据一个字符串生成一个日期
	 * @param dateStr 日期字符串
	 * @param pattern 格式化表达式
	 * @return
	 * @throws ParseException
	 * 
	 */
	public static Date fromString(String dateStr,String pattern) throws ParseException {
		if (pattern==null) {
			pattern = DISPLAY_SECOND_FORMAT;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(dateStr);
	}
	
	/**
	 * 来自于GMT的时间字符串生成日期
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date fromGMTString(String dateStr) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DISPLAY_GMT_FORMAT,Locale.ENGLISH);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return simpleDateFormat.parse(dateStr);
	}
	
	/**
	 * 来自于UTC格式的时间字符串生成日期
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date fromUTCString(String dateStr) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DISPLAY_UTC_FORMAT);
		return simpleDateFormat.parse(dateStr);
	}
	/**
	 * 字符串
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date fromString(String date) throws ParseException {
		Date d =  fromString(date, DISPLAY_SECOND_FORMAT);
		return d;
	}
	
	/**
	 * 日期对象格式化为字符串输出
	 * @param date 日期对象
	 * @return
	 */
	public static String format(Date date) {
		return format(date,null);
	}
	
	/**
	 * 日期对象格式化为字符串输出
	 * @param date 日期对象
	 * @param pattern 格式化表达式
	 * @return
	 */
	public static String format(Date date,String pattern) {
		if (pattern==null) {
			pattern = DISPLAY_SECOND_FORMAT;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * UTC时间2018-06-19T16:00:00.000Z  格式化为2018-06-19 16:00:00.000 
	 *
	 * @param date
	 * @param mat
	 * @return
	 * @throws ParseException 
	 */
	public static String formatFromUTCString(String date, String pattern) throws ParseException {
		if (pattern!=null) {
			pattern = DISPLAY_SECOND_FORMAT;
		}
		Date d = fromUTCString(date);
		return format(d);
	}
	
	/**
	 * 加减年
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date addYear(Date date,int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR,year);
		return calendar.getTime();
	}
	

	
	/**
	 * 加减月份
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date,int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,month);
		return calendar.getTime();
	}
	

	
	/**
	 * 加减星期
	 * @param date
	 * @param week
	 * @return
	 */
	public static Date addWeek(Date date,int week) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_MONTH,week);
		return calendar.getTime();
	}
	

	
	/**
	 * 加减天数
	 * @param date 当前时间
	 * @param day 天数
	 * @return
	 */
	public static Date addDay(Date date,int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH,day);
		return calendar.getTime();
	}
	
	

	
	/**
	 * 加减小时
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date addHours(Date date,int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY,hours);
		return calendar.getTime();
	}
	
	
	
	/**
	 * 加减分钟
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date addMinute(Date date,int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE,minute);
		return calendar.getTime();
	}

	
	/**
	 * 加减秒
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date addSecond(Date date,int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND,second);
		return calendar.getTime();
	}

	/**
	 * 加减毫秒
	 * @param date
	 * @param milliSecond
	 * @return
	 */
	public static Date addMilliSecond(Date date,int milliSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, milliSecond);
		return calendar.getTime();
	}
	
	
	/**
	 * 设置年
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date setYear(Date date,int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR,year);
		return calendar.getTime();
	}
	/**
	 * 设置月份（不从0计算，一月份传入1）
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date setMonth(Date date,int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH,month-1);
		return calendar.getTime();
	}
	/**
	 * 设置星期
	 * @param date
	 * @param week
	 * @return
	 */
	public static Date setWeek(Date date,int week) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.WEEK_OF_MONTH,week);
		return calendar.getTime();
	}
	/**
	 * 设置天数
	 * @param date 当前时间
	 * @param day 天数
	 * @return
	 */
	public static Date setDay(Date date,int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,day);
		return calendar.getTime();
	}
	/**
	 * 设置小时
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date setHours(Date date,int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,hours);
		return calendar.getTime();
	}
	/**
	 * 设置分钟
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date setMinute(Date date,int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE,minute);
		return calendar.getTime();
	}
	/**
	 * 设置秒
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date setSecond(Date date,int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND,second);
		return calendar.getTime();
	}
	
	/**
	 * 设置毫秒
	 * @param date
	 * @param milliSecond
	 * @return
	 */
	public static Date setMilliSecond(Date date,int milliSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, milliSecond);
		return calendar.getTime();
	}
	
	/**
	 * 设置当天的最大时间
	 * @param date
	 * @return
	 */
	public static Date setCurrentDayMaxTime(Date date) {
		date = LoserStarDateUtils.setHours(date, 23);
		date = LoserStarDateUtils.setMinute(date, 59);
		date = LoserStarDateUtils.setSecond(date, 59);
		date = LoserStarDateUtils.setMilliSecond(date, 999);
		return date;
	}
	/**
	 * 设置当月最大时间
	 * @param date
	 * @return
	 */
	public static Date setCurrentMonthMaxTime(Date date) {
		int month = getMoth(date);
		int maxDay = getMonthMaxDay(month);
		date = LoserStarDateUtils.setDay(date, maxDay);
		date = LoserStarDateUtils.setHours(date, 23);
		date = LoserStarDateUtils.setMinute(date, 59);
		date = LoserStarDateUtils.setSecond(date, 59);
		date = LoserStarDateUtils.setMilliSecond(date, 999);
		return date;
	}
	
	/**
	 * 设置当天最小时间
	 * @param date
	 * @return
	 */
	public static Date setCurrentDayMinTime(Date date) {
		date = LoserStarDateUtils.setHours(date, 0);
		date = LoserStarDateUtils.setMinute(date, 0);
		date = LoserStarDateUtils.setSecond(date, 0);
		date = LoserStarDateUtils.setMilliSecond(date, 0);
		return date;
	}

	/**
	 * 得到年份
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	
	/**
	 * 得到月份数(不从0计算，一月得到的值是1)
	 * @param date
	 * @return
	 */
	public static int getMoth(Date date) {
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH)+1;
	}

	/**
	 * 得到天数
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取某月中的最大天数(不用传入0，正常的月份数就行)
	 * @param month
	 * @return
	 */
	public static int getMonthMaxDay(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month-1);
		int maxDay = calendar.getActualMaximum(Calendar.DATE);
		return maxDay;
	}

	/**
	 * 得到小时
	 * @param date
	 * @return
	 */
	public static int getHour(Date date) {
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到分钟
	 * @param date
	 * @return
	 */
	public static int getMinute(Date date) {
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}
	/**
	 * 得到秒
	 * @param date
	 * @return
	 */
	public static int getSecond(Date date) {
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}
	
	/**
	 * 得到毫秒
	 * @param date
	 * @return
	 */
	public static int getMillisecond(Date date) {
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MILLISECOND);
	}
	
	/**
	 * 计算两个时间相差多少天
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static double calculateDayDifference(Date startDate,Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diff = endTime-startTime;
		long conversionRate = 1000*60*60*24;//1天=24小时*60分钟60秒*1000毫秒
		double day = diff/conversionRate;
		return day;
	}
	/**
	 * 计算两个时间相差多少小时
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static double calculateHoursDifference(Date startDate,Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diff = endTime-startTime;
		long conversionRate = 1000*60*60;//1小时=60分钟60秒*1000毫秒
		double hours = diff/conversionRate;
		return hours;
	}

	/**
	 * 计算两个时间相差多少分钟
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String calculateMinuteDifference(Date startDate,Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diff = endTime-startTime;
		long conversionRate = 1000*60;//1分钟=60*1000毫秒
		double second = diff/conversionRate;
		DecimalFormat decimalFormat = new DecimalFormat("0.00000");
		return decimalFormat.format(second);
	}
	/**
	 * 计算两个时间相差多少秒
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static double calculateSecondDifference(Date startDate,Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diff = endTime-startTime;
		long conversionRate = 1000;//1秒=1000毫秒
		double second = diff/conversionRate;
		return second;
	}

	/**
	 * 计算日期的相差时间
	 * @param endDate
	 * @param nowDate
	 * @return
	 */
	public static String getDatePoor(Date endDate, Date nowDate) {
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少天
	    long day = diff / nd;
	    // 计算差多少小时
	    long hour = diff % nd / nh;
	    // 计算差多少分钟
	    long min = diff % nd % nh / nm;
	    // 计算差多少秒//输出结果
	     long sec = diff % nd % nh % nm / ns;
	    return day + "天" + hour + "小时" + min + "分钟"+sec+"秒";
	}
	
	public static void main(String[] args) throws ParseException {
		//天数计算
		Date date = LoserStarDateUtils.addDay(new Date(),2);
		String formatDate = LoserStarDateUtils.format(date);
		System.out.println("加天数："+formatDate);
		
		//月份计算
		Date date2 = LoserStarDateUtils.addMonth(new Date(),2);
		String formatDat2 = LoserStarDateUtils.format(date2);
		System.out.println("加月份："+formatDat2);
		
		//年计算
		Date date3 = LoserStarDateUtils.addYear(new Date(), -2);
		String formatDat3 = LoserStarDateUtils.format(date3);
		System.out.println("加年："+formatDat3);
		
		//小时计算
		Date date4 = LoserStarDateUtils.addHours(new Date(), 12);
		String formatDat4 = LoserStarDateUtils.format(date4);
		System.out.println("加小时："+formatDat4);
		
		//分钟计算
		Date date5 = LoserStarDateUtils.addMinute(new Date(), -10);
		String formatDat5 = LoserStarDateUtils.format(date5);
		System.out.println("加分钟："+formatDat5);
		
		//秒计算
		Date date6 = LoserStarDateUtils.addSecond(new Date(), 20);
		String formatDat6 = LoserStarDateUtils.format(date6);
		System.out.println("加秒："+formatDat6);
		
		//星期计算
		Date date7 = LoserStarDateUtils.addWeek(new Date(), 2);
		String formatDat7 = LoserStarDateUtils.format(date7);
		System.out.println("加星期："+formatDat7);
		
		
		Date date8 = new Date();
		date8 = LoserStarDateUtils.setMilliSecond(date8, 999);
		System.out.println("设置毫秒："+LoserStarDateUtils.format(date8,LoserStarDateUtils.DISPLAY_MILLISECOND_FORMAT));
		
		
		String data9str = "Thu Feb 16 07:13:48 GMT 2015";
		Date date9 = LoserStarDateUtils.fromGMTString(data9str);
		System.out.println(data9str);
		System.out.println(date9);
		System.out.println(LoserStarDateUtils.format(date9));
		
		String date10str = "1990-07-28T03:30:30.112Z";
		Date date10 = LoserStarDateUtils.fromUTCString(date10str);
		String date10FormatStr = LoserStarDateUtils.formatFromUTCString(date10str,null);
		System.out.println("date10------------------");
		System.out.println(date10);
		System.out.println(date10FormatStr);
		System.out.println(LoserStarDateUtils.format(date10));
		
		System.out.println("计算时差---------------------------------------");
		long startDate = System.currentTimeMillis();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long  endDate = System.currentTimeMillis();
		double second = calculateSecondDifference(new Date(startDate), new Date(endDate));
		System.out.println("相差："+second+"秒");
		String minute  = calculateMinuteDifference(new Date(startDate), new Date(endDate));
		System.out.println("相差："+minute+"分钟");
		System.out.println();

		System.out.println("获取当月最大天数-------------------");
		System.out.println(LoserStarDateUtils.getMonthMaxDay(7));
		
		System.out.println("获得当前时间的月份数-------------");
		System.out.println(LoserStarDateUtils.getMoth(new Date()));
		
		System.out.println("设置当月最大时间-------------------");
		Date maxMonth = new Date();
		Date maxMonth2 = LoserStarDateUtils.setCurrentMonthMaxTime(maxMonth);
		System.out.println(LoserStarDateUtils.format(maxMonth2));
		
	}
}
