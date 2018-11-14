/**
 * author: loserStar
 * date: 2018年4月18日上午11:50:31
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.base.imp;

import java.util.ArrayList;
import java.util.List;

import com.loserstar.utils.string.LoserStarStringUtils;
/**
 * 
 * author: loserStar
 * date: 2018年11月14日下午4:49:11
 * remarks:拼接sql的where条件用的工具类
 */
public class WhereHelper {
	/**
	 * 自定义的字符串的where后面的每个条件，直接拼and  id=xx或者or id=xxx
	 */
	private List<String> strWhereList = new ArrayList<String>(); 
	/**
	 * 排序的语句
	 */
	private String orderStr;
	
	
	/**
	 * @return the strWhereList
	 */
	public List<String> getStrWhereList() {
		return strWhereList;
	}


	/**
	 * @return the orderStr
	 */
	public String getOrderStr() {
		return orderStr;
	}

	public WhereHelper addStrWhere(String s) {
		this.strWhereList.add(s);
		return this;
	}
	
	public WhereHelper addIn(InStr inStr) {
		if (inStr!=null&&inStr.toString()!=null) {
			this.strWhereList.add(inStr.toString());
		}
		return this;
	}
	
	
	public WhereHelper addStrOrder(String orderStr) {
		this.orderStr = orderStr;
		return this;
	}
	
	/**
	 * 去除字符串开头的where、and、or关键字
	 * @param string
	 * @return
	 */
	private String removePrefix(String string) {
		string = string.trim();
		string = LoserStarStringUtils.cutPrefix(string, "where ");
		string = LoserStarStringUtils.cutPrefix(string, "WHERE ");
		string = LoserStarStringUtils.cutPrefix(string, "and ");
		string = LoserStarStringUtils.cutPrefix(string, "AND ");
		string = LoserStarStringUtils.cutPrefix(string, "or ");
		string = LoserStarStringUtils.cutPrefix(string, "OR ");
		return string;
	}
	
	/**
	 * 拼出最终的sql语句
	 */
	@Override
	public String toString() {
		StringBuffer resultStr = new StringBuffer();//记录最终的sql
		StringBuffer andStr = new StringBuffer();//记录and条件
		//遍历stirng条件，处理删除每个条件开头的where和and和or关键字
		for (int i = 0; i <this.strWhereList.size(); i++) {
			String string = this.strWhereList.get(i);
			if (i==0) {
				//去除字符串开头的where、and、or关键字
				string = removePrefix(string);
			}
			andStr.append(" "+string);
		}
		resultStr.append(andStr.toString());
		//拼接排序
		if (this.orderStr!=null&&!this.orderStr.equals("")) {
			resultStr.append(" "+this.orderStr);
		}
		return resultStr.toString();
	}
	
}
