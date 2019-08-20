/**
 * author: loserStar
 * date: 2018年4月17日下午5:03:25
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.loserstar.utils.string.LoserStarStringUtils;

/**
 * 数组相关工具类
 * author: loserStar
 * date: 2019年8月20日上午9:07:44
 * remarks:
 */
public class LoserStarArrayUtils {
	
	/**
	 * 根据一个基本数组创建一个List集合
	 * @param a
	 * @return
	 */
	public static <T> List<T> asList(T... a) {
		return Arrays.asList(a);
	}
	
	/**
	 * 正序
	 * @param a
	 */
	public static <T> void asc(T... a) {
		Arrays.sort(a);
	}
	
	/**
	 * 倒序
	 * @param a
	 */
	public static <T> void desc(T... a) {
		Arrays.sort(a,Collections.reverseOrder());
	}
	
	/**
	 * 基本类型List正序
	 * @param list
	 */
	public static <T extends Comparable<? super T>> void asc(List<T> list) {
		Collections.sort(list);
	}
	
	/**
	 * 基本类型List倒序
	 * @param list
	 */
	public static <T extends Comparable<? super T>> void desc(List<T> list) {
		Collections.sort(list);
		Collections.reverse(list);
	}
	
	/**
	 * 删除ArrayList中重复元素,生成新的list
	 * @param list
	 */
    public static <T> List<T> removeDuplicate(final List<T> list) {
        HashSet<T> h = new HashSet<T>(list);
        List<T> newList = new ArrayList<T>();
        newList.addAll(h);
        return newList;
    }
    
    /**
     * 检查souceList中是否包含checkList中的其中一个值
     * @param sourceList
     * @param checkList
     * @return
     */
    public static boolean checkIsContain(List<String> sourceList,List<String> checkList) {
    	boolean flag = false;
    	for (String string : checkList) {
			sourceList.contains(string);
			flag = true;
		}
    	return flag;
    }
    
    /**
	 * 根据一个开始的文本和结束文本，从list中按顺序把List中间的文本取出来
	 * @param list
	 * @param startString
	 * @param endString
	 * @return
	 */
    public static List<String> getRangeText(List<String> allList,String startString,String endString){
		List<String> newList = new ArrayList<String>();
		boolean isRead = false;
		for (String string : allList) {
			if (isRead) {
				if (string.equals(endString)) {
					isRead = false;
					break;
				}else {
					newList.add(string);
				}
			}
			if (string.equals(startString)) {
				isRead = true;
			}
		}
		return newList;
	}
    
    /**
     * 清除每一个元素的两边的空格
     * @param list
     * @return
     */
    public static List<String> trim(List<String> list){
    	List<String> newList = new ArrayList<String>();
    	for (String string : list) {
    		newList.add(string.trim());
		}
		return newList;
    }
    
    /**
     * 替换每一个元素的所有空格
     * @param list
     * @return
     */
    public static List<String> trimAll(List<String> list){
    	List<String> newList = new ArrayList<String>();
    	for (String string : list) {
    		newList.add(LoserStarStringUtils.removeSpecifiedString(string, " "));
		}
		return newList;
    }
    
    /**
     * 清理list中每一个元素的特殊字符
     * @param list
     * @param c
     * @return
     */
    public static List<String> removeChar(List<String> list,char ...cArray){
    	List<String> newList = new ArrayList<String>();
    	for (String string : list) {
    		newList.add(LoserStarStringUtils.removeCharArray(string, cArray));
		}
		return newList;
    }
    /**
     * 清理list中每一个元素的特殊字符
     * @param list
     * @param c
     * @return
     */
    public static List<String> removeChar(List<String> list,int ...cArray){
    	List<String> newList = new ArrayList<String>();
    	for (String string : list) {
    		newList.add(LoserStarStringUtils.removeCharArray(string, cArray));
    	}
    	return newList;
    }
}
