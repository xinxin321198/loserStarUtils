package com.loserstar.utils.string;

/**
 * 
 * author: loserStar
 * date: 2018年4月10日下午3:36:49
 * remarks:字符串处理
 */
public class LoserStarStringUtils {
	/**
	 * 判断字符串为null的话返回空字符串
	 * @param s
	 * @return
	 */
	public static String empty(String s) {
		return s==null?"":s;
	}
	
	/**
	 * 去掉所有空格
	 * @param s
	 * @return
	 */
	public static String removeSpace(String s) {
		if (s!=null) {
			return s.replace(" ", "");
		}else {
			return "";
		}
	}
}
