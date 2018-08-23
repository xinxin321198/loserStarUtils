/**
 * author: loserStar
 * date: 2018年8月23日上午10:58:17
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.db.jfinal.base;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.loserstar.utils.db.jfinal.base.imp.JoinHelper;

/**
 * author: loserStar
 * date: 2018年8月23日上午10:58:17
 * remarks:
 */
public interface IDBUtils {
	/**
	 * 获取表字段
	 * @param schema 表空间
	 * @param tableName 表名称
	 * @return
	 */
	public List<Record> getTableColumnList(String schema,String tableName);
	
	/**
	 * 获取表字段的名称集合
	 * @param schema 表空间
	 * @param tableName 表名称
	 * @return
	 */
	public List<String> getTableColumnNameList(String schema,String tableName);
	
	/**
	 * 输出多表连接时使用的查询字段
	 * @param joinHelper
	 * @return
	 */
	public String toSelectFiledStr(String tableSchema,JoinHelper joinHelper) ;
}
