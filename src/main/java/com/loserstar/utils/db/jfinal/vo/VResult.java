/**
 * author: loserStar
 * date: 2018年4月22日下午10:14:18
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.vo;

/**
 * author: loserStar
 * date: 2018年4月22日下午10:14:18
 * remarks:controller返回用的对象
 */
public class VResult {
	/**
	 * 执行成功还是失败的标识
	 */
	private boolean flag;
	/**
	 * 消息
	 */
	private String msg;
	/**
	 * 返回的数据
	 */
	private Object data;
	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * 设置flag=false
	 * 设置msg的值
	 * @param msg
	 */
	public void error(String msg) {
		this.setFlag(false);
		this.setMsg(msg);
	}
	/**
	 * 设置flag=true
	 * 设置msg的值
	 * @param msg
	 */
	public void ok(String msg) {
		this.setFlag(true);
		this.setMsg(msg);
	}
}
