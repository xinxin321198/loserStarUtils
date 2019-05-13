/**
 * author: loserStar
 * date: 2019年3月28日下午3:41:27
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.loserstar.utils.string.LoserStarStringUtils;

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
	
	/**
	 * 把List<Map<String,Object>>对象 构建成一个树结构
	 * @param allList 所有数据
	 * @param parent 根节点Map对象
	 * @param rootFiledKey 根节点Map的主要key
	 * @param parentFiledKey 子节点的Map中所属父节点的key
	 * @param childrenListFiled 构建完成之后子节点对象集合的key
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> buildTree(List<Map<String, Object>> allList,Map<String, Object> parent,String rootFiledKey,String parentFiledKey,String childrenListFiled) throws Exception {
		List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
		//查找子项
		String parentFiled = LoserStarStringUtils.toString(parent.get(rootFiledKey));
		if (parentFiled==null||parentFiled.equals("")) {
			throw new Exception("没有找到parent对象里的"+rootFiledKey+"根节点名称");
		}
		for (Map<String, Object> record : allList) {
			String parenId = LoserStarStringUtils.toString(record.get(parentFiledKey));
			if (parenId!=null&&!parenId.equals("")) {
				if (parenId.equals(parentFiled)) {
					List<Map<String, Object>> childList_temp =  buildTree(allList, record,rootFiledKey, parentFiledKey, childrenListFiled);
					record.put(childrenListFiled, childList_temp);
					childrenList.add(record);
				}
			}else {
//				throw new Exception("主要字段为："+record.getStr(rootFiledKey)+"的对象里，没有找到"+parentFiledKey+"字段的值，确认不了其所属的父节点");
			}
		}
		return childrenList;
	}
}
