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
import java.util.Date;

/**
 * author: loserStar
 * date: 2018年4月10日下午3:28:19
 * remarks: 日期相关工具类
 */
public class LoserStarDateUtils {
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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(dateStr);
	}
	
	/**
	 * 日期对象格式化为字符串输出
	 * @param date 日期对象
	 * @param pattern 格式化表达式
	 * @return
	 */
	public static String format(Date date,String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
}
