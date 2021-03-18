package com.loserstar.utils.encodes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 对称加密工具类
 * author: loserStar
 * date: 2021年3月8日下午4:16:51
 * email:362527240@qq.com
 * remarks:
 */
public class LoserStarAesUtils {

	/**
	 * 生成加密秘钥字节码
	 * @param encodeRules 加密规则，解密和加密时得一样
	 * @param filePath
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getKeyByte(String encodeRules) throws NoSuchAlgorithmException {
		  //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen=KeyGenerator.getInstance("AES");
        //2.根据ecnodeRules规则初始化密钥生成器
        //生成一个128位的随机源,根据传入的字节数组
        keygen.init(128, new SecureRandom(encodeRules.getBytes()));
          //3.产生原始对称密钥
        SecretKey original_key=keygen.generateKey();
          //4.获得原始对称密钥的字节数组
        byte [] raw=original_key.getEncoded();
        return raw;
	}
	
	
	/*
	   * 加密
	   * 1.构造密钥生成器
	   * 2.根据ecnodeRules规则初始化密钥生成器
	   * 3.产生密钥
	   * 4.创建和初始化密码器
	   * 5.内容加密
	   * 6.返回字符串
	   */
		/**
		 * 加密数据
		 * @param encodeRules 规则字符串（解密时需一致）
		 * @param content 加密内容
		 * @return
		 */
	    public static String AESEncode(byte [] raw,String content){
	        try {
	            //5.根据字节数组生成AES密钥
	            SecretKey key=new SecretKeySpec(raw, "AES");
	              //6.根据指定算法AES自成密码器
	            Cipher cipher=Cipher.getInstance("AES");
	              //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
	            cipher.init(Cipher.ENCRYPT_MODE, key);
	            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
	            byte [] byte_encode=content.getBytes("utf-8");
	            //9.根据密码器的初始化方式--加密：将数据加密
	            byte [] byte_AES=cipher.doFinal(byte_encode);
	          //10.将加密后的数据转换为字符串
	            //这里用Base64Encoder中会找不到包
	            //解决办法：
	            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
	            String AES_encode=new String(new BASE64Encoder().encode(byte_AES));
	          //11.将字符串返回
	            return AES_encode;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        //如果有错就返加nulll
	        return null;         
	    }
	    
	    /*
	     * 解密
	     * 解密过程：
	     * 1.同加密1-4步
	     * 2.将加密后的字符串反纺成byte[]数组
	     * 3.将加密内容解密
	     */
	    /**
	     * 解密数据
	     * @param encodeRules 规则字符串（加密时使用的规则字符串）
	     * @param content 解密的数据
	     * @return
	     */
	    public static String AESDncode(byte [] raw,String content){
	        try {
	            //5.根据字节数组生成AES密钥
	            SecretKey key=new SecretKeySpec(raw, "AES");
	              //6.根据指定算法AES自成密码器
	            Cipher cipher=Cipher.getInstance("AES");
	              //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
	            cipher.init(Cipher.DECRYPT_MODE, key);
	            //8.将加密并编码后的内容解码成字节数组
	            byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
	            /*
	             * 解密
	             */
	            byte [] byte_decode=cipher.doFinal(byte_content);
	            String AES_decode=new String(byte_decode,"utf-8");
	            return AES_decode;
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        //如果有错就返加nulll
	        return null;         
	    }
	    
	    public static void main(String[] args) throws NoSuchAlgorithmException {
	    	byte [] raw = getKeyByte("pbext");
			String s1 = AESEncode(raw,"[{\"collapse\":false,\"active\":false,\"text\":\"查看人员信息\",\"uri\":\"yhck/index\"},{\"collapse\":true,\"active\":false,\"text\":\"样本管理\",\"children\":[{\"collapse\":false,\"active\":false,\"text\":\"样本清单\",\"uri\":\"newSample/list\"}]},{\"collapse\":false,\"active\":false,\"text\":\"承诺书管理\",\"uri\":\"commitment/myCommitment-list\"},{\"collapse\":false,\"active\":false,\"text\":\"绩效考核\",\"children\":[{\"collapse\":false,\"active\":false,\"text\":\"计划确定\",\"uri\":\"app3/empAssign?newwin=1\"},{\"collapse\":false,\"active\":false,\"text\":\"检查评价\",\"uri\":\"app3/empApp?newwin=1\"},{\"collapse\":false,\"active\":false,\"text\":\"查看“两书”\",\"uri\":\"app3/empQuery?newwin=1\"},{\"collapse\":false,\"active\":false,\"text\":\"\\\"两书\\\"统计分析\",\"uri\":\"app3/empQuery3?newwin=1\"},{\"collapse\":false,\"active\":false,\"text\":\"红塔集团年度绩效指标\",\"uri\":\"std/list-upload\"}]},{\"collapse\":true,\"active\":false,\"text\":\"年度岗位任职考核\",\"children\":[{\"collapse\":false,\"active\":false,\"text\":\"计划确定\",\"uri\":\"stationPlan/stationPlanMgr?newwin=1\"},{\"collapse\":false,\"active\":false,\"text\":\"胜任能力考核及岗位任职评价\",\"uri\":\"postCheck/index?newwin=1\"},{\"collapse\":false,\"active\":false,\"text\":\"报审\",\"uri\":\"app_year/bs_html?newwin=1\"},{\"collapse\":false,\"active\":false,\"text\":\"考核评价结果及报审进度查询\",\"uri\":\"app_year/query_html?newwin=1\"}]},{\"collapse\":true,\"active\":false,\"text\":\"干部考核\",\"children\":[{\"collapse\":false,\"active\":false,\"text\":\"评价实施\",\"uri\":\"gbcp/cpxz\"},{\"collapse\":false,\"active\":false,\"text\":\"评测结果查看\",\"uri\":\"gbcp/resultView\"}]},{\"collapse\":true,\"active\":false,\"text\":\"因私出国（境）\",\"children\":[{\"collapse\":false,\"active\":false,\"text\":\"因私出国（境）\",\"uri\":\"abroad/data-list\"}]}]");
			System.out.println("加密后：");
			System.out.println(s1);
			String s2 = AESDncode(raw, s1);
			System.out.println("解密后：");
			System.out.println(s2);
			
	    }
}
