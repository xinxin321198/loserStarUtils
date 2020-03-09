/**
 * author: loserStar
 * date: 2019年5月13日上午11:29:05
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Record;
import com.loserstar.utils.string.LoserStarStringUtils;


/**
 * author: loserStar
 * date: 2019年5月13日上午11:29:05
 * remarks:
 */
public class LoserStarJfinalRecordUtils extends LoserStarMapUtils{
	
	
	/**
	 * 把List<Record>对象 构建成一个树结构
	 * @param allList 所有数据
	 * @param parent 根节点Record对象
	 * @param rootFiledKey 根节点Record的主要key
	 * @param parentFiledKey 子节点的Record中所属父节点的key
	 * @param childrenListFiled 构建完成之后子节点对象集合的key
	 * @return
	 * @throws Exception
	 */
	public static  List<Record> buildTree(List<Record> allList,Record parent,String rootFiledKey,String parentFiledKey,String childrenListFiled) throws Exception {
		List<Record> childrenList = new ArrayList<Record>();
		//查找子项
		String parentFiled = LoserStarStringUtils.toString(parent.get(rootFiledKey));
		if (parentFiled==null||parentFiled.equals("")) {
			throw new Exception("没有找到parent对象里的"+rootFiledKey+"根节点名称");
		}
		for (Record record : allList) {
			String parenId = LoserStarStringUtils.toString(record.get(parentFiledKey));
			if (parenId!=null&&!parenId.equals("")) {
				if (parenId.equals(parentFiled)) {
					List<Record> childList_temp =  buildTree(allList, record,rootFiledKey, parentFiledKey, childrenListFiled);
					record.set(childrenListFiled, childList_temp);
					childrenList.add(record);
				}
			}else {
//				throw new Exception("主要字段为："+record.getStr(rootFiledKey)+"的对象里，没有找到"+parentFiledKey+"字段的值，确认不了其所属的父节点");
			}
		}
		return childrenList;
	}
	
	/**
	 * 剔除值为null或空的key，便于保存不报错
	 * @param record
	 * @return
	 */
	public static Record removeIsNullKey(Record record) {
		Record newRecord = new Record();
		Map<String, Object> map = record.getColumns();
		for (Map.Entry<String, Object> entry : map.entrySet()) { 
			if (entry.getValue()!=null&&!entry.getValue().equals("")) {
				newRecord.set(entry.getKey(), entry.getValue());
			}
		}
		return newRecord;
	}
	
	/**
	 * 遍历填充Record中为null的key为空字符串
	 * @param map
	 */
	public static Record fullNullKeysToEmptyString(Record record) {
		Record newRecord = new Record();
		Map<String, Object> map = record.getColumns();
		for (Map.Entry<String, Object> entry : map.entrySet()) { 
			if (entry.getValue()==null||entry.getValue().equals("null")) {
				newRecord.set(entry.getKey(), "");
			}else {
				newRecord.set(entry.getKey(), entry.getValue());
			}
		}
		return newRecord;
	}
	
	/**
	 * 遍历填充Record中为空字符串的字段为null（这样才会让jfinal去把该字段更新为null）
	 * @param map
	 */
	public static Record fullEmptyStringKeysToNull(Record record) {
		Record newRecord = new Record();
		Map<String, Object> map = record.getColumns();
		for (Map.Entry<String, Object> entry : map.entrySet()) { 
			if (entry.getValue()!=null) {
				if (entry.getValue().equals("")) {
					newRecord.set(entry.getKey(), null);
				}else {
					newRecord.set(entry.getKey(), entry.getValue());
				}
			}else {
				newRecord.set(entry.getKey(), entry.getValue());
			}
		}
		return newRecord;
	}
	
	/**
	 * 得到List<Record>里某个字段的值的集合
	 * @param list
	 * @return
	 */
	public static List<String> getFieldValueList(List<Record> list,String fieldName){
		List<String> resultList = new ArrayList<String>();
		for (Record record : list) {
			resultList.add(record.getStr(fieldName));
		}
		return resultList;
	}
	
	
}
