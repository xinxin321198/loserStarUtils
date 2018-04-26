package com.loserstar.utils.ObjectMapConvert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;


/**
 * author: lxx
 * version: 2016年5月9日下午4:54:17
 * email:362527240@qq.com
 * remarks:
 */
/**
 * @Description 对象属性操作工具类
 * @Package com.viathink.msswms.sample.utils.PropertiesUtils.java
 * @author LiuJunGuang
 * @date 2012-5-11 下午1:54:08
 * @version V1.0
 */
public class LoserStarObjMapConvertUtil {

	/**
	 * 根据对象列表和对象的某个属性返回属性的List集合
	 * 
	 * @param objList
	 *            对象列表
	 * @param propertyName
	 *            要操作的属性名称
	 * @return <pre>
	 * 	指定属性的List集合;
	 * 	如果objList为null或者size等于0抛出 IllegalArgumentException异常;
	 *  如果propertyName为null抛出 IllegalArgumentException异常
	 * </pre>
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public static <T> List<Object> getPropertyList(List<T> objList, String propertyName) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		if (objList == null || objList.size() == 0)
			throw new IllegalArgumentException("No objList specified");
		if (propertyName == null || "".equals(propertyName)) {
			throw new IllegalArgumentException("No propertyName specified for bean class '" + objList.get(0).getClass() + "'");
		}
		PropertyUtilsBean p = new PropertyUtilsBean();
		List<Object> propList = new LinkedList<Object>();
		for (int i = 0; i < objList.size(); i++) {
			T obj = objList.get(i);
			propList.add(p.getProperty(obj, propertyName));
		}
		return propList;
	}

	/**
	 * 将List列表中的对象的某个属性封装成一个Map对象，key值是属性名，value值是对象列表中对象属性值的列表
	 * 
	 * @param objList
	 *            对象列表
	 * @param propertyName
	 *            属性名称,可以是一个或者多个
	 * @return 返回封装了属性名称和属性值列表的Map对象，如果参数非法则抛出异常信息
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static <T> Map<String, List<Object>> getPropertiesMap(List<T> objList, String... propertyName)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (objList == null || objList.size() == 0)
			throw new IllegalArgumentException("No objList specified");
		if (propertyName == null || propertyName.length == 0) {
			throw new IllegalArgumentException("No propertyName specified for bean class '" + objList.get(0).getClass() + "'");
		}
		Map<String, List<Object>> maps = new HashMap<String, List<Object>>();
		for (int i = 0; i < propertyName.length; i++) {
			maps.put(propertyName[i], getPropertyList(objList, propertyName[i]));
		}
		return maps;
	}
	
	/**
	 * 把某种Map类型的父或子对象转为Map
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String,T> ConvertToMap(T t) throws Exception {
		if (t instanceof Map) {
			return (Map<String, T>) t;
		}else {
			throw new Exception("对象非Map类型，不能转换");
		}
	}

	/**
	 * 把某种Map的父对象或子对象的的List转为List<Map>
	 * @param objectList
	 * @return
	 * @throws Exception
	 */
	public static <T> List<Map<String, T>> ConvertListToMapList(List<T> list) throws Exception{
		List<Map<String, T>> resultList = new ArrayList<Map<String,T>>();
		for (T t: list) {
			resultList.add(ConvertToMap(t));
		}
		return resultList;
	}
	
	/**
	 * 某个List的父或子对象转为List
	 * @param t
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> ConvertToList(T t) throws Exception{
		if (t instanceof List) {
			return (List<T>)t;
		}else {
			throw new Exception("对象非List类型，不能转换");
		}
	}
}