package com.loserstar.utils.json;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

/**
 * 基于jodd的json类
 * author: loserStar
 * date: 2019年1月15日下午5:35:04
 * remarks:json工具类，增加反序列化时候的空字符串判断
 */
public class LoserStarJsonUtil {
	private static JsonSerializer jsonSerializer = new JsonSerializer();
	private static JsonParser jsonParser = new JsonParser();
	
	/**
	 * 简单序列化（不包含集合）
	 * @param object
	 * @return
	 */
	public static String toJsonSimple(Object object) {
		  String json = toJson(object, false);
		  return json;
	}
	
	/**
	 * 深度序列化（包含集合）
	 *
	 * @param object
	 * @return
	 */
	public static String toJsonDeep(Object object){
		String json =toJson(object, true);
		return json;
	}
	
	/**
	 * 根据isDeep决定转换json是否包含集合属性
	 *
	 * @param object
	 * @param isDeep
	 * @return
	 */
	public static String toJson(Object object,boolean isDeep){
		String json = jsonSerializer.deep(isDeep).serialize(object);
		return json;
	}
	
	/**
	 * 反序列化
	 * @param json
	 * @param class1
	 * @return
	 */
	public static <T> T toModel(String json,Class<T> class1) {
		if (json==null||json.equals("")) {
			return null;
		}
		return jsonParser.parse(json, class1);
	}
}
