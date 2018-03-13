package com.loserstar.utils.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * 基于JSON-java的json处理类
 * https://github.com/stleary/JSON-java
 * author: loserStar
 * date: 2018年3月13日下午2:42:44
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
public class LoserStarJsonUtil2 {

	public static String toJsonForObject(Object object){
		JSONObject jsonObject = new JSONObject(object);
		return jsonObject.toString();
	}
	
	public static String toJsonForMap(Map<?, ?> m){
		JSONObject jsonObject = new JSONObject(m);
		return jsonObject.toString();
	}
	
	
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("luoxinxin", "123");
		map.put("wangping", "456");
		map.put("loserStar", "999999");
		String[] liStrings = {"1","a","b","z"};
		map.put("list", liStrings);
		String json = LoserStarJsonUtil2.toJsonForMap(map);
		System.out.println(json);
	}
}
