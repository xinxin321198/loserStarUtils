/**
 * author: loserStar
 * date: 2018年3月30日上午10:53:08
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.base;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * author: loserStar
 * date: 2018年3月30日上午10:53:08
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
public class LoserStarJfinalBaseService {

	/**
	 * 根据sql得到单调数据
	 * @param sql
	 * @return
	 */
	public Record get(String sql) {
		List<Record> list = Db.find(sql);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 根据sql得到单调数据(参数化查询方式)
	 * @param sql
	 * @return
	 */
	public Record get(String sql,Object... paras) {
		List<Record> list = Db.find(sql,paras);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 设置某条记录的某个字段为null值
	 * @param tableName 表名
	 * @param fieldName 要设为null的字段名
	 * @param primaryId 主键ID的值
	 * @return
	 */
	public boolean updateFieldIsNull(String tableName,String fieldName,String primaryId) {
		String sql = "UPDATE "+tableName+" SET "+fieldName+" = NULL WHERE ID='"+primaryId+"'";
		int row = Db.update(sql);
		if (row<1) {
			return false;
		}
		return true;
	}
	
}
