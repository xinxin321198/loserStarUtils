package com.loserstar.utils.db.jfinal.base.imp;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.loserstar.utils.db.jfinal.base.IDBUtils;
import com.loserstar.utils.string.LoserStarStringUtils;

/**
 * 
 * author: loserStar
 * date: 2018年8月23日上午11:05:16
 * remarks:
 */
public class DB2DBUtils implements IDBUtils{

	@Override
	public List<Record> getTableColumnList(String tableSchema, String tableName) {
		List<Record> columnList = Db.find("SELECT * FROM SYSIBM.SYSCOLUMNS WHERE TBCREATOR='"+tableSchema+"' AND TBNAME = '"+tableName+"'");
		return columnList;
	}

	@Override
	public List<String> getTableColumnNameList(String tableSchema, String tableName) {
		List<Record> columnList = getTableColumnList(tableSchema, tableName);
		List<String> columnNameList = new ArrayList<String>();
		for (Record record : columnList) {
			columnNameList.add(record.getStr("name"));
		}
		return columnNameList;
	}


	@Override
	public String toSelectFiledStr(String tableSchema, JoinHelper joinHelper) {
		String resultStr = "";
		List<JoinTable> joinTableList = joinHelper.getJoinTableList();
		for (JoinTable joinTable : joinTableList) {
			List<String> sourceTableColumnNameList = getTableColumnNameList(tableSchema, joinTable.getSouceTableName());
			for (String string : sourceTableColumnNameList) {
				resultStr+=joinTable.getSouceTableName()+"."+string+" as "+joinTable.getSouceTableName()+"_"+string+",";
			}
			List<String> joinTableColumnNameList = getTableColumnNameList(tableSchema, joinTable.getJoinTableName());
			for (String string : joinTableColumnNameList) {
				resultStr+=joinTable.getJoinTableName()+"."+string+" as "+joinTable.getJoinTableName()+"_"+string+",";
			}
		}
		resultStr = LoserStarStringUtils.cutSuffix(resultStr, ",");
		return resultStr;
	}

}
