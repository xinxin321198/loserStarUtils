package com.loserstar.utils.pinyin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


import jodd.util.CharUtil;
import jodd.util.StringUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

	private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

	static {
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	public static String getFullPinyin(String word) {
		return getPinyin(word, false);
	}

	public static String getFirstLetterPinyin(String word) {
		return getPinyin(word, true);
	}

	private static String getPinyin(String word, boolean isFirstLetter) {
		if (StringUtil.isBlank(word)) {
			return null;
		}
		if (isFirstLetter) {
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		} else {
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		}
		
		StringBuffer buf = new StringBuffer();
		char[] chs = word.toCharArray();
		for (char c : chs) {
			if (CharUtil.isAlphaOrDigit(c)) {
				buf.append(c);
			} else {
				try {
					String[] result = PinyinHelper.toHanyuPinyinStringArray(c, format);
					if (result != null && result.length > 0) {
						String pinyin = StringUtil.capitalize(result[0]);
						if (!StringUtil.isEmpty(pinyin)) {
							buf.append(isFirstLetter ? pinyin.charAt(0) : pinyin);
						}
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					System.out.println("生成(" + word + ")拼音时产生错误"+e.getMessage());
				}
			}
		}
		return buf.toString();
	}
	
	
	
	/**
	 * 获取一段文字的所有拼音组合情况,以list<String>形式返回  ，并且去掉重复（使用HashSet的方式）
	 * @param s
	 * @return
	 */
	public static List<String> getFullHardPinyins(String s){  
		 if(s==null){  
		     s="";//null时处理，后边处理时报错  
		 }  
		 String[][] allPinyins=new String[s.length()][];//存放整个字符串的各个字符所有可能的拼音情况，如果非汉字则是它本身  
		 char[] words=s.toCharArray();//把这段文字转成字符数组  
		 for(int i=0;i<words.length;i++){  
		     allPinyins[i]=PinyinUtil.getAllPinyins(words[i],false);//每个字符的所有拼音情况  
		 }  
		 String[] resultArray=PinyinUtil.recursionArrays(allPinyins,allPinyins.length,0);//用递归，求出这个2维数组每行取一个数据组合起来的所有情况  
		 HashSet<String> returnHashSet = new HashSet<String>(Arrays.asList(resultArray));
		 List<String> returnList = new ArrayList<String>(returnHashSet);
		 return returnList;//返回数组支持的固定大小的list(asList注意事项详见我的其他博客，可new LinkedList<String>(Arrays.asList()))来实现对结果随意操作  
	}  
	
	
	/**
	 * 获取一段文字的首字母拼音组合情况,以list<String>形式返回 ，并且去掉重复（使用重新new一个list的循环遍历判断的方法）
	 * @param s
	 * @return
	 */
	public static List<String> getFirstHardPinyins(String s){  
		if(s==null){  
			s="";//null时处理，后边处理时报错  
		}  
		String[][] allPinyins=new String[s.length()][];//存放整个字符串的各个字符所有可能的拼音情况，如果非汉字则是它本身  
		char[] words=s.toCharArray();//把这段文字转成字符数组  
		for(int i=0;i<words.length;i++){  
			allPinyins[i]=PinyinUtil.getAllPinyins(words[i],true);//每个字符的所有拼音情况  
		}  
		String[] resultArray=PinyinUtil.recursionArrays(allPinyins,allPinyins.length,0);//用递归，求出这个2维数组每行取一个数据组合起来的所有情况  

		//去重复
		List<String> returnList = new ArrayList<String>();
		for (String string : resultArray) {
			boolean flag = false;//是否已经有此字符串在returnList中了
			for (String string2 : returnList) {
				if (string.equals(string2)) {
					flag = true;//有
				}
			}
			if (!flag) {
				returnList.add(string);
			}
		}
		return returnList;//返回数组支持的固定大小的list(asList注意事项详见我的其他博客，可new LinkedList<String>(Arrays.asList()))来实现对结果随意操作  
	}  
	
	/**
	 * 获取包含一个字符的拼音（多音字则以数组形式返回多个）,非汉字则返回字符本身 
	 * @param word
	 * @param isFirstLetter 是否返回拼音首字母简称，true是，false不是
	 * @return
	 */
	public static String[] getAllPinyins(char word,boolean isFirstLetter){  
	    HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();   //创建拼音输入格式类  
	    pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);//指定格式中的大小写属性为小写  
	    pinyinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//指定音标格式无音标  
	    pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_V);//指定用v表示ü  
		if (isFirstLetter) {
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		} else {
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		}
		
	    String[] formatPinyin=null;  
	    try {  
	        formatPinyin = PinyinHelper.toHanyuPinyinStringArray(word, pinyinFormat);//获取对应的汉字拼音，不是汉字返回空数组  
	    } catch (BadHanyuPinyinOutputFormatCombination e) {//会抛出异常，捕获异常  
	        //logger.error(e.getMessage());  
	        e.printStackTrace();  
	    }  
	    if(formatPinyin.length==0){  //如果非汉字，就返回自己
	        formatPinyin=new String[1];  
	        formatPinyin[0]=String.valueOf(word);
	    }  
	    
	    if (isFirstLetter) {//如果是取简称
	    	for(int i=0;i<formatPinyin.length;i++){
	    		formatPinyin[i] =  String.valueOf(formatPinyin[i].charAt(0));
	    	}
		}
	    for (int i = 0; i < formatPinyin.length; i++) {
			formatPinyin[i] = StringUtil.capitalize(formatPinyin[i]);
		}
	    return formatPinyin;  
	}  
	
	/** 
	 * 用递归方法，求出这个二维数组每行取一个数据组合起来的所有情况，返回一个字符串数组 
	 * @param s 求组合数的2维数组 
	 * @param len 此二维数组的长度，省去每一次递归长度计算的时间和空间消耗，提高效率 
	 * @param cursor 类似JDBC、数据库、迭代器里的游标，指明当前从第几行开始计算求组合数，此处从0开始（数组第一行） 
	 *                 避免在递归中不断复制剩余数组作为新参数，提高时间和空间的效率 
	 * @return String[] 以数组形式返回所有的组合情况 
	 */  
	public static String[] recursionArrays(String[][] s,int len,int cursor){  
	       if(cursor<=len-2){//递归条件,直至计算到还剩2行  
	           int len1 = s[cursor].length;  
	           int len2 = s[cursor+1].length;  
	           int newLen = len1*len2;//上下2行的总共的组合情况  
	           String[] temp = new String[newLen];//存上下2行中所有的组合情况  
	           int index = 0;  
	           for(int i=0;i<len1;i++){//嵌套循环遍历，求出上下2行中，分别取一个数据组合起来的所有情况  
	               for(int j=0;j<len2;j++){  
	                   temp[index] = s[cursor][i] + s[cursor+1][j];  
	                   index ++;  
	               }  
	           }  
	           s[cursor+1]=temp;//把当前计算到此行的所有组合结果放在cursor+1行  
	           cursor++;//游标指向下一行，即上边的数据结果  
	           return PinyinUtil.recursionArrays(s,len,cursor);  
	       }else{  
	        return s[len-1];//返回最终的所有组合结果  
	       }  
	}  

	public static void main(String... args) throws BadHanyuPinyinOutputFormatCombination {
		String word = "卡迪1 娜贝4·，可儿克、【里】奇 娅DRAGONSEA 法力藤长";
		System.out.println(getFullPinyin(word));
		System.out.println(getFirstLetterPinyin(word));
		List<String> strings = getFullHardPinyins("大王");
		for (String string : strings) {
			System.out.println(string);
		}
		
		List<String> first = getFirstHardPinyins("大王");
		for (String string : first) {
			System.out.println(string);
		}
		
	}
}
