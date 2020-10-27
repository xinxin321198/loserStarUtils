/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.loserstar.utils.encodes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 * @author calvin
 * @version 2013-01-15
 */
public class LoserStarEncodes {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return new String(Hex.encodeHex(input));
	}

	/**
	 * Hex解码.
	 * @throws Exception 
	 */
	public static byte[] decodeHex(String input) throws Exception {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}
	
	/**
	 * Base64编码.
	 */
	public static String encodeBase64(String input) {
		try {
			return new String(Base64.encodeBase64(input.getBytes(DEFAULT_URL_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

//	/**
//	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
//	 */
//	public static String encodeUrlSafeBase64(byte[] input) {
//		return Base64.encodeBase64URLSafe(input);
//	}

	/**
	 * Base64解码.
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input.getBytes());
	}
	
	/**
	 * Base64解码.
	 */
	public static String decodeBase64String(String input) {
		try {
			return new String(Base64.decodeBase64(input.getBytes()), DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * Base62编码。
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

/*	*//**
	 * Html 转码.
	 *//*
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	*//**
	 * Html 解码.
	 *//*
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	*//**
	 * Xml 转码.
	 *//*
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml10(xml);
	}*/

	/**
	 * Xml 解码.
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8. 
	 * @throws UnsupportedEncodingException 
	 */
	public static String urlEncode(String part) throws UnsupportedEncodingException {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 * @throws UnsupportedEncodingException 
	 */
	public static String urlDecode(String part) throws UnsupportedEncodingException {

		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
	}
	
	
	public static String unicodeStr2String(String unicodeStr) {
		int length = unicodeStr.length();
		int count = 0;
		//正则匹配条件，可匹配“\\u”1到4位，一般是4位可直接使用 String regex = "\\\\u[a-f0-9A-F]{4}";
		String regex = "\\\\u[a-f0-9A-F]{1,4}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(unicodeStr);
		StringBuffer sb = new StringBuffer();
		
		while(matcher.find()) {
			String oldChar = matcher.group();//原本的Unicode字符
			String newChar = unicode2String(oldChar);//转换为普通字符
			// int index = unicodeStr.indexOf(oldChar);
            // 在遇见重复出现的unicode代码的时候会造成从源字符串获取非unicode编码字符的时候截取索引越界等
			int index = matcher.start();
 
			sb.append(unicodeStr.substring(count, index - 1));//添加前面不是unicode的字符
			sb.append(newChar);//添加转换后的字符
			count = index+oldChar.length();//统计下标移动的位置
		}
		sb.append(unicodeStr.substring(count, length));//添加末尾不是Unicode的字符
		return sb.toString();
	}
	
	public static String unicode2String(String unicode) {
		StringBuffer string = new StringBuffer();
		String[] hex = unicode.split("\\\\u");
 
		for (int i = 1; i < hex.length; i++) {
			// 转换出每一个代码点
			int data = Integer.parseInt(hex[i], 16);
			// 追加成string
			string.append((char) data);
		}
 
		return string.toString();
	}
}
