/**
 * author: loserStar
 * date: 2018年4月18日下午3:09:02
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.base.imp;

import java.util.Arrays;
import java.util.List;

import com.loserstar.utils.string.LoserStarStringUtils;

/**
 * author: loserStar
 * date: 2018年4月18日下午3:09:02
 * remarks: 拼接sql中in条件的工具类
 */
public class InStr {
	public static  enum AndOr {
	    AND, OR
	}
	/**
	 * 过滤的模式是and还是or
	 */
	private AndOr mode=AndOr.AND;
	/**
	 * 字段名
	 */
	private String filedName;
	/**
	 * 值的集合，支持原生数组和容器类List
	 */
	private List<String> valueList;
	/**
	 * @param filedName
	 * @param valueList
	 */
	public InStr(AndOr mode,String filedName, List<String> valueList) {
		super();
		this.mode = mode;
		this.filedName = filedName;
		this.valueList = valueList;
	}
	
	public InStr(AndOr mode,String filedName, String[] valueList) {
		super();
		this.mode = mode;
		this.filedName = filedName;
		this.valueList = Arrays.asList(valueList);
	}


	/**
	 * @return the mode
	 */
	public AndOr getMode() {
		return mode;
	}




	/**
	 * @param mode the mode to set
	 */
	public void setMode(AndOr mode) {
		this.mode = mode;
	}




	/**
	 * @return the filedName
	 */
	public String getFiledName() {
		return filedName;
	}
	/**
	 * @param filedName the filedName to set
	 */
	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}
	/**
	 * @return the valueList
	 */
	public List<String> getValueList() {
		return valueList;
	}
	/**
	 * @param valueList the valueList to set
	 */
	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}


	@Override
	public String toString() {
		String in = "";
		if (mode==AndOr.AND) {
			in+=" and ";
		}else {
			in+=" or ";
		}
		in += " "+filedName;
		in += " in ("+LoserStarStringUtils.join(valueList, ",","'","'")+") ";
		return in;
	}
	
	
}
