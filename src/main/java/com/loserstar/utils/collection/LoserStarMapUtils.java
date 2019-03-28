/**
 * author: loserStar
 * date: 2019年3月28日下午3:41:27
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.collection;

import java.util.Map;
import java.util.Map.Entry;

/**
 * author: loserStar
 * date: 2019年3月28日下午3:41:27
 * remarks:Map的工具类
 */
public class LoserStarMapUtils {

	/**
	 * 遍历填充map中为null的key
	 * @param map
	 */
	public static Map<String, Object> fullNullKeysToEmptyString(Map<String, Object> map) {
		for (Entry<String, Object> entry : map.entrySet()) { 
			Object value = entry.getValue();
			if (value==null||value.equals("null")) {
				entry.setValue("");
			}
		}
		return map;
	}
}
