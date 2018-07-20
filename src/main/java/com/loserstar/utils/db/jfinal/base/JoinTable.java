/**
 * author: loserStar
 * date: 2018年7月17日上午9:55:55
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.base;

/**
 * author: loserStar
 * date: 2018年7月17日上午9:55:55
 * remarks:
 */
public class JoinTable {
	/**
	 * 连接方式，内连接，左连接，右连接，全连接
	 */
	public static  enum JoinType {
	    INNER, LEFT,RIGTH,ALL
	} 
	
	/**
	 * 连接类型，inner、left、rigth、all
	 */
	private JoinType joinType;
	/**
	 * 连接的表名
	 */
	private String joinTableName;
	/**
	 * 连接的表的关联字段
	 */
	private String joinTableField;
	/**
	 * 原表名
	 */
	private String souceTableName;
	/**
	 * 原表名关联字段
	 */
	private String souceTableField;
	
	
	/**
	 * @param joinType 连接类型，inner、left、rigth、all
	 * @param joinTableName 连接的表名
	 * @param joinTableField 连接的表的关联字段
	 * @param souceTableName 原表名
	 * @param souceTableField 原表名关联字段
	 */
	public JoinTable(JoinType joinType, String joinTableName, String joinTableField, String souceTableName,
			String souceTableField) {
		super();
		this.joinType = joinType;
		this.joinTableName = joinTableName;
		this.joinTableField = joinTableField;
		this.souceTableName = souceTableName;
		this.souceTableField = souceTableField;
	}
	/**
	 * @return the joinType
	 */
	public JoinType getJoinType() {
		return joinType;
	}
	/**
	 * @param joinType the joinType to set
	 */
	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}
	/**
	 * @return the joinTableName
	 */
	public String getJoinTableName() {
		return joinTableName;
	}
	/**
	 * @param joinTableName the joinTableName to set
	 */
	public void setJoinTableName(String joinTableName) {
		this.joinTableName = joinTableName;
	}
	/**
	 * @return the joinTableField
	 */
	public String getJoinTableField() {
		return joinTableField;
	}
	/**
	 * @param joinTableField the joinTableField to set
	 */
	public void setJoinTableField(String joinTableField) {
		this.joinTableField = joinTableField;
	}
	/**
	 * @return the souceTableField
	 */
	public String getSouceTableField() {
		return souceTableField;
	}
	/**
	 * @param souceTableField the souceTableField to set
	 */
	public void setSouceTableField(String souceTableField) {
		this.souceTableField = souceTableField;
	}
	/**
	 * @return the souceTableName
	 */
	public String getSouceTableName() {
		return souceTableName;
	}
	/**
	 * @param souceTableName the souceTableName to set
	 */
	public void setSouceTableName(String souceTableName) {
		this.souceTableName = souceTableName;
	}
	
	
}
