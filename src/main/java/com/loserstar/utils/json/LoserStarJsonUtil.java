package com.loserstar.utils.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.json.FastJson;




/**
 * 该为使用fastJson作为底层实现，因为jodd那个在was生产机上升级了jdk之后就序列化不了
 * author: loserStar
 * date: 2019年1月16日上午10:36:01
 * remarks:json工具类,反序列化使用松散模式
 */
public class LoserStarJsonUtil {
	
	/**
	 * 简单序列化（不包含集合）
	 * @param object
	 * @return
	 */
	public static String toJsonSimple(Object object) {
		  String json = toJsonDeep(object);
		  return json;
	}
	
	/**
	 * 深度序列化（包含集合）
	 *
	 * @param object
	 * @return
	 */
	public static String toJsonDeep(Object object){
		String json =FastJson.getJson().toJson(object);
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
		String json = toJsonDeep(object);
		return json;
	}
	
	public static String toJson(Object object){
		String json = toJsonDeep(object);
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
		return FastJson.getJson().parse(json, class1);
	}
	
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a","aaaaaaaa" );
		map.put("b",10);
		map.put("c",1000.123f);
		map.put("d",1000.123d);
		Map<String, Object> map_sub1 = new HashMap<String, Object>();
		map_sub1.put("sub1_a", "sub1_aaa");
		map_sub1.put("sub1_b", 123);
		map_sub1.put("sub1_c", 123d);
		map.put("e", map_sub1);
		List<String> list = new ArrayList<String>();
		list.add("list_1");
		list.add("list_2");
		list.add("list_3");
		list.add("list_4");
		map.put("list", list);
		System.out.println(LoserStarJsonUtil.toJson(map));
		
		String jsonStr = "{\"d\":1000.123,\"e\":{\"sub1_a\":\"sub1_aaa\",\"sub1_c\":123.0,\"sub1_b\":123},\"b\":10,\"c\":1000.123,\"a\":\"aaaaaaaa\",\"list\":[\"list_1\",\"list_2\",\"list_3\",\"list_4\"]}";
		Map<String, Object> map2 = LoserStarJsonUtil.toModel(jsonStr, Map.class);
		System.out.println(map2.get("a"));
	}
}
