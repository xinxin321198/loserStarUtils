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

import com.jfinal.plugin.activerecord.Model;
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
	 * @return 返回一个root节点下列表树，不包含root节点
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
	 * 把List<Record>对象 构建成一个树结构
	 * @param allList 所有数据
	 * @param parent 根节点Record对象
	 * @param rootFiledKey 根节点Record的主要key
	 * @param parentFiledKey 子节点的Record中所属父节点的key
	 * @param childrenListFiled 构建完成之后子节点对象集合的key
	 * @return 返回一个完整树结构对象，包含了root对象在里面
	 * @throws Exception
	 */
	public static  Record buildTreeHasRoot(List<Record> allList,Record parent,String rootFiledKey,String parentFiledKey,String childrenListFiled) throws Exception {
			List<Record> sub = buildTree(allList, parent, rootFiledKey, parentFiledKey, childrenListFiled);
			parent.set(childrenListFiled, sub);
			return parent;
	}
	
	/**
	 * 把树结构按深度优先转换为list
	 * @param tree 树结构的record
	 * @param reurnList 转换之后输出到该对象中
	 * @param childrenListFiledKey 子节点的key
	 */
	public static void treeToList(Record tree,List<Record> reurnList,String childrenListFiledKey) {
		if (reurnList==null) {
			reurnList = new ArrayList<Record>();
		}
		reurnList.add(tree);
		if (tree.get(childrenListFiledKey)!=null) {
			List<Record> subList = tree.get(childrenListFiledKey);
			tree.remove(childrenListFiledKey);
			for (Record sub : subList) {
				treeToList(sub, reurnList, childrenListFiledKey);
			}
		}
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
			String s = record.getStr(fieldName);
			if (null!=s) {
				resultList.add(s);
			}
		}
		return resultList;
	}
	
	/**
	 * 得到List<Model>里某个字段的值的集合
	 * @param list
	 * @return
	 */
	public static <T extends Model<T>> List<String> getFieldValueListForModel(List<T> list,String fieldName){
		List<String> resultList = new ArrayList<String>();
		for (T t : list) {
			String s = t.getStr(fieldName);
			if (null!=s) {
				resultList.add(s);
			}
		}
		return resultList;
	}
	
	/**
	 * 批量把jfinal的List Record 转为jfinal的List Model 对象
	 * @param list
	 * @param class1
	 * @return
	 */
	public static <T extends Model<?>> List<T>  toModelList(List<Record> list,Class<T> class1){
		List<T> modelList = new ArrayList<T>();
		try {
			for (Record record : list) {
				T t = class1.newInstance();
				t.put(record);
				modelList.add(t);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return modelList;
	}
	/**
	 * 批量把jfinal的Record转为jfinal的Model对象
	 * @param list
	 * @param class1
	 * @return
	 */
	public static <T extends Model<?>> T toModel(Record record,Class<T> class1){
		T t = null;
		try {
			if(record!=null) {
				t = class1.newInstance();
				t.put(record);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 批量把jfinal的List Model 转为jfinal的List Record 对象
	 * @param list
	 * @return
	 */
	public static <T extends Model<?>> List<Record> toRecordList(List<T> list) {
		List<Record> recordList = new ArrayList<Record>();
		for (T t : list) {
			recordList.add(t.toRecord());
		}
		return recordList;
	}
	
	/**
	 * 批量把jfinal的 Model 转为jfinal的 Record 对象
	 * @param model 泛型对象，决定返回值得类型
	 * @return
	 */
	public static <T extends Model<?>> Record toRecordList(T model) {
		return model.toRecord();
	}
}
