/**
 * author: loserStar
 * date: 2018年7月17日上午9:54:14
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.base;

import java.util.ArrayList;
import java.util.List;

/**
 * author: loserStar
 * date: 2018年7月17日上午9:54:14
 * remarks:
 */
public class JoinHelper {
	private List<JoinTable> leftJoinTable = new ArrayList<>();
	
	public void addLeftJoin(JoinTable joinTable) {
		leftJoinTable.add(joinTable);
	}

	@Override
	public String toString() {
		String result =" ";
		for (JoinTable joinTable : leftJoinTable) {
			result+=joinTable.getJoinType()+" join "+joinTable.getJoinTableName()+" on "+joinTable.getJoinTableName()+"."+joinTable.getJoinTableField()+"="+joinTable.getSouceTableName()+"."+joinTable.getSouceTableField();
		}
		return result;
	}
	
}
