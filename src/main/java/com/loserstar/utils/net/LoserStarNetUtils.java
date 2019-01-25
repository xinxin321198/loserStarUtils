/**
 * author: loserStar
 * date: 2019年1月24日上午11:16:21
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.net;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.loserstar.utils.string.LoserStarStringUtils;

/**
 * author: loserStar
 * date: 2019年1月24日上午11:16:21
 * remarks:网络相关的工具
 */
public class LoserStarNetUtils {

	/**
	 * 获取当前机器的名称
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalName() throws UnknownHostException {
		String serverName =  LoserStarStringUtils.toString(Inet4Address.getLocalHost().getHostName());
		return serverName;
	}
	
	/**
	 * 根据服务器名称，判断是测试机还是生产还是本地（红塔集团项目使用）
	 * 生产机：product
	 * 测试：test
	 * 本地：local
	 * @param serverName
	 * @return
	 * @throws Exception
	 */
	public static String judgeServerType() throws Exception {
		String serverName = getLocalName();
		if (serverName==null||serverName.equals("")) {
			throw new Exception("没有传入serverName，无法判断是生产机还是测试机还是本地");
		}
		String type = "";
		if (serverName.equalsIgnoreCase("c1ep1vm14.hongta.com")) {
			//生产机
			type = "product";
		} else if (serverName.equalsIgnoreCase("c1ep1vm23.hongta.com")) {
			//测试机
			type = "test";
		} else {
			//其它未知（默认为本地）
			type = "local";
		}
		return type;
	}
}
