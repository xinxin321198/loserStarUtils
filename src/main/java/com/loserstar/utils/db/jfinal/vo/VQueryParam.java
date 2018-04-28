/**
 * author: loserStar
 * date: 2018年4月20日上午9:12:50
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.vo;

import java.util.Map;

/**
 * author: loserStar
 * date: 2018年4月20日上午9:12:50
 * remarks: controller接收参数用的类，带分页
 */
public class VQueryParam {
	private int page=1;
	private int size = 20;
	private Map<String, String> data;
	
	
	
	/**
	 * 
	 */
	public VQueryParam() {
		super();
	}
	
	/**
	 * @param page
	 * @param size
	 */
	public VQueryParam(int page, int size) {
		super();
		this.page = page;
		this.size = size;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the data
	 */
	public Map<String, String> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
}
