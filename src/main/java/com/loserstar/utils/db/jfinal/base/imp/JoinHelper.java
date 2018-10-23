/**
 * author: loserStar
 * date: 2018年7月17日上午9:54:14
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.base.imp;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * author: loserStar
 * date: 2018年10月23日下午5:10:16
 * remarks:
 */
public class JoinHelper {
	private List<JoinTable> joinTableList = new ArrayList<>();
	
	public void addLeftJoin(JoinTable joinTable) {
		joinTableList.add(joinTable);
	}
	


	/**
	 * @return the joinTableList
	 */
	public List<JoinTable> getJoinTableList() {
		return joinTableList;
	}





	@Override
	public String toString() {
		String result =" ";
		for (JoinTable joinTable : joinTableList) {
			result+=joinTable.getJoinType()+" join "+joinTable.getJoinTableName()+" on "+joinTable.getJoinTableName()+"."+joinTable.getJoinTableField()+"="+joinTable.getSouceTableName()+"."+joinTable.getSouceTableField()+" ";
		}
		return result;
	}
	
}
