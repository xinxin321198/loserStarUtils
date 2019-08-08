/**
 * author: loserStar
 * date: 2018年4月25日下午6:46:46
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.proerties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * author: loserStar
 * date: 2018年4月25日下午6:46:46
 * remarks:
 */
public class LoserStarPropertiesUtil {

	/**
	 * 读取配置
	 * @param path
	 * @return
	 */
	public static Properties getProperties(String path) {
		Properties properties = new Properties(); 
		try {
			properties.load(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	
	/**
	 * 打印配置
	 */
	public static void printPropertiesInfo(Properties properties) {
		 Set<Map.Entry<Object,Object>> entrySet = properties.entrySet();
		 for (Map.Entry<Object, Object> entry : entrySet) {
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
	}
}
