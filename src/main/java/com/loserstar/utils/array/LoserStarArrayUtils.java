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

/**
 * 数组相关工具类
 * author: loserStar
 * date: 2018年4月17日下午5:03:25
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
}
