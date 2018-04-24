/**
 * author: loserStar
 * date: 2018年4月18日上午10:55:44
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.json;

import com.jfinal.json.JFinalJson;
import com.jfinal.json.JFinalJsonFactory;

/**
 * 基于jfinal自己的json工具类
 * author: loserStar
 * date: 2018年4月18日上午10:55:44
 * remarks:
 */
public class LoserStarJfinalJsonUtil {
	private static final JFinalJson JfinalJson = (JFinalJson) JFinalJsonFactory.me().getJson();
	
	public static String toJson(Object object) {
		return JfinalJson.toJson(object);
	}
	
}
