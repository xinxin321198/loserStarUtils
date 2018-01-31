package com.loserstar.utils.json;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

/**
 * 基于jodd的json类
 * author: loserStar
 * date: 2018年1月31日下午4:20:05
 * email:362527240@qq.com
 * remarks:
 */
public class JsonUtil {
	private static JsonSerializer jsonSerializer = new JsonSerializer();
	private static JsonParser jsonParser = new JsonParser();
	
	/**
	 * 序列化
	 * @param object
	 * @return
	 */
	public static String toJson(Object object) {
		  String json = jsonSerializer.serialize(object);
		  return json;
	}
	
	/**
	 * 反序列化
	 * @param json
	 * @param class1
	 * @return
	 */
	public static <T> T toModel(String json,Class<T> class1) {
		return jsonParser.parse(json, class1);
	}
}
