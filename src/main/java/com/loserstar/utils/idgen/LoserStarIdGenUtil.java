/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.loserstar.utils.idgen;

import java.security.SecureRandom;
import java.util.UUID;

import com.loserstar.utils.encodes.LoserStarEncodes;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author ThinkGem
 * @version 2013-01-15
 */
public class LoserStarIdGenUtil {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return LoserStarEncodes.encodeBase62(randomBytes);
	}
	
	/**
	 * 返回当前时间的毫秒数的十六进制的字符串当做uuid，并且会进行进程的阻塞以防止统一毫秒生成的uuid相同
	 * @return
	 */
	public static String uuidTimeHex() {
		return SnowflakeIdWorker.FakeGuid();
	}
	
	/**
	 * 返回十进制的毫秒数
	 * @return
	 */
	public static Long uuidTimeLong() {
		return SnowflakeIdWorker.FakeId();
	}
	
	public static void main(String[] args) {
		System.out.println("生成随机的long");
		for (int i=0; i<10; i++){
			System.out.println(LoserStarIdGenUtil.randomLong());
		}
		int length = 5;
		System.out.println("生成长度为："+length+"的随机字节码，并进行base62编码");
		for (int i=0; i<10; i++){
			System.out.println(LoserStarIdGenUtil.randomLong() + "  " + LoserStarIdGenUtil.randomBase62(length));
		}
		System.out.println("生成当前时间戳的十六进制字符串（会阻塞进程防止统一毫秒时间内值重复）：");
		for (int i=0; i<10; i++){
			System.out.println(LoserStarIdGenUtil.uuidTimeHex());
		}
		System.out.println("生成当前时间戳（会阻塞进程防止统一毫秒时间内值重复）：");
		for (int i=0; i<10; i++){
			System.out.println(LoserStarIdGenUtil.uuidTimeLong());
		}
	}

}
