package com.loserstar.utils.encodes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * author: loserStar
 * date: 2020年9月2日上午11:26:05
 * email:362527240@qq.com
 * remarks:sha1加密
 */
public class LoserStarSha1Utils {
	/**
	 * sha1加密
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static String getSHA1_jdk(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(str.getBytes());
		byte[] digest = md.digest();
		StringBuffer hexstr = new StringBuffer();
		String shaHex = "";
		for (int i = 0; i < digest.length; i++) {
			shaHex = Integer.toHexString(digest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexstr.append(0);
			}
			hexstr.append(shaHex);
		}
		return hexstr.toString();
	}
	/**
	 * sha1加密
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static String getSHA1_apache(String str) throws NoSuchAlgorithmException {
		return DigestUtils.shaHex(str);//他娘的，commons-codec-1.2又启用了
	}
	

}
