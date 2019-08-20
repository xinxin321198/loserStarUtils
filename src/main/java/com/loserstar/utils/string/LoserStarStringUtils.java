package com.loserstar.utils.string;

import java.util.Collection;
import java.util.Iterator;
/**
 * 
 * author: loserStar
 * date: 2018年10月24日下午9:35:23
 * remarks:字符串处理
 */
public class LoserStarStringUtils {
	/**
	 * 判断字符串为null的话返回空字符串
	 * @param s
	 * @return
	 */
	public static String empty(String s) {
		return (null==s)?"":s;
	}
	
	/**
	 * 转换字符串
	 * @param object
	 * @return
	 */
	public static String toString(Object object) {
		return (null==object)?"":object.toString();
	}
	
	/**
	 * 去掉所有空格
	 * @param s
	 * @return
	 */
	public static String removeSpace(String s) {
		return removeSpecifiedString(s, " ");
	}
	
	/**
	 * 把字符串中指定的字符串剔除
	 * @param sourceStr 原字符串
	 * @param removeStr 需要剔除的字符串
	 * @return 剔除之后的最终结果
	 */
	public static String removeSpecifiedString(String sourceStr,String removeStr) {
		if (sourceStr!=null) {
			if (removeStr==null) {removeStr="";}
			return sourceStr.replace(removeStr, "");
		}else {
			return "";
		}
	}
	
	/**
	 * 把字符串中指定的字符剔除
	 * @param sourceStr 原字符串
	 * @param checkCharArray 需要剔除的字符数组
	 * @return 剔除之后的最终结果
	 */
	public static String removeCharArray(String sourceStr,char ...checkCharArray) {
		String resultStr = "";
		if (sourceStr!=null) {
			if (null==checkCharArray||checkCharArray.length<=0) {
				resultStr =  sourceStr;
			}else {
				char[] sourceCharArray = sourceStr.toCharArray();
				StringBuffer newText = new StringBuffer();
				for (int i = 0; i < sourceCharArray.length; i++) {
					char sourceChar = sourceCharArray[i];
					for (char checkChar : checkCharArray) {
						if (sourceChar!=checkChar) {
							newText.append(sourceChar);
						}
					}
				}
				resultStr = newText.toString();
			}
		}
		return resultStr;
	}
	
	/**
	 * 把字符串中指定的字符剔除
	 * @param sourceStr 原字符串
	 * @param checkCharArray 需要剔除的字符数组
	 * @return 剔除之后的最终结果
	 */
	public static String removeCharArray(String sourceStr,int ...checkCharArray) {
		char[] checkCharArray_c = new char[checkCharArray.length];
		for (int i = 0; i < checkCharArray.length; i++) {
			checkCharArray_c[i] = (char)checkCharArray[i];
		}
		return removeCharArray(sourceStr,checkCharArray_c);
	}
	
	/**
	 * 去除字符串中开头的某个字符串
	 * @param sourceStr 原字符串
	 * @param removeStr 要剔除的第一个字符串
	 */
	public static String cutPrefix(String string,String prefix) {
		if (string.startsWith(prefix)) {
			string = string.substring(prefix.length());
		}
		return string;
	}
	
	/**
	 * 去除字符串中结尾的某个字符串
	 * @param string
	 * @param suffix
	 * @return
	 */
	public static String cutSuffix(String string,String suffix) {
		if (string.endsWith(suffix)) {
			string = string.substring(0, string.length()-suffix.length());
		}
		return string;
	}
	
	
	/**
	 * 把一个集合以某个字符串分割输出，并且每一项添加个前缀和后缀
	 * form jodd StringUtil.join
	 * @param collection
	 * @param separator
	 * @param perfix 每一项的前缀
	 * @param suffix 每一项的后缀
	 * @return
	 */
	public static String join(final Collection<?> collection,String separator,String perfix,String suffix) {
		if (collection == null) {
			return null;
		}

		if (collection.size() == 0) {
			return "";
		}
		final StringBuilder sb = new StringBuilder(collection.size() * 16);
		final Iterator<?> it = collection.iterator();

		for (int i = 0; i < collection.size(); i++) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(perfix+it.next()+suffix);
		}
		return sb.toString();
	}
	
	/**
	 * 把一个数组以某个字符串分割输出，并且每一项添加个前缀和后缀
	 * form jodd StringUtil.join
	 * @param collection
	 * @param separator
	 * @param perfix 每一项的前缀
	 * @param suffix 每一项的后缀
	 * @return
	 */
	public static String join(final Object[] strings,String separator,String perfix,String suffix) {
		if (strings.length==0) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			if (i>0) {
				stringBuffer.append(separator);
			}
			stringBuffer.append(perfix+strings[i]+suffix);
		}
		return stringBuffer.toString();
	}
	
	public static String join(final int[] strings,String separator,String perfix,String suffix) {
		if (strings.length==0) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			if (i>0) {
				stringBuffer.append(separator);
			}
			stringBuffer.append(perfix+strings[i]+suffix);
		}
		return stringBuffer.toString();
	}
	public static Double toDouble(String s) {
		if (s==null||s.equals("")) {
			return 0d;
		}else {
			return Double.parseDouble(s);
		}
	}
	public static Integer toInteger(String s) {
		if (s==null||s.equals("")) {
			return 0;
		}else {
			return Integer.parseInt(s);
		}
	}
}
