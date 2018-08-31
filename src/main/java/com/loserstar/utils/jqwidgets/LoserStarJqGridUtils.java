/**
 * author: loserStar
 * date: 2018年8月24日下午12:00:46
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.jqwidgets;

import java.util.List;
import java.util.Map;

/**
 * author: loserStar
 * date: 2018年8月24日下午12:00:46
 * remarks:jqwidgets的grid工具类
 */
public class LoserStarJqGridUtils {

	/**
	 * 去除grid里行里面自己用的但是数据库用不到的一些值
	 * @param map
	 * @return
	 */
	public static Map<String, Object>removeJqGridColumns(Map<String, Object> map) {
		map.remove("uid");
		map.remove("boundindex");
		map.remove("uniqueid");
		map.remove("visibleindex");
		return map;
	}
	/**
	 * 去除grid里行里面自己用的但是数据库用不到的一些值
	 * @param mapList
	 * @return
	 */
	public static List<Map<String, Object>> removeJqGridColumns(List<Map<String, Object>> mapList){
		for (Map<String, Object> map : mapList) {
			map.remove("uid");
			map.remove("boundindex");
			map.remove("uniqueid");
			map.remove("visibleindex");
		}
		return mapList;
	}
}
