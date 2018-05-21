/**
 * author: loserStar
 * date: 2018年4月10日下午3:28:19
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * author: loserStar
 * date: 2018年4月10日下午3:28:19
 * remarks: 日期相关工具类
 */
public class LoserStarDateUtils {
	public static final String DEFAULT_DATE_FORMAT ="yyyy-MM-dd hh:mm:ss SSS";
	
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
			pattern = DEFAULT_DATE_FORMAT;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(dateStr);
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
			pattern = DEFAULT_DATE_FORMAT;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
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
	 * 加减小时
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date addHours(Date date,int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR,hours);
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
	
	
	
	public static void main(String[] args) {
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
		
	}
}
