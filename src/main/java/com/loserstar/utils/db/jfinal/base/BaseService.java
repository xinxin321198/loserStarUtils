package com.loserstar.utils.db.jfinal.base;

import java.util.List;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

/**
 * 基础service
 * author: loserStar
 * date: 2018年1月25日下午7:51:13
 * email:362527240@qq.com
 * remarks:
 */
public  abstract class BaseService {
	/**
	 * 删除
	 */
	private static final String DEL = "1";
	/**
	 * 未删除
	 */
	private static final String NOT_DEL = "0";
	
	/**
	 * 批量curd时的标记名称
	 */
	private static final String EDIT_FLAG = "curdFlag";
	/**
	 * 返回具体表名称
	 * @return
	 */
	protected abstract String getTableName();
	/**
	 * 返回表对应的主键名称
	 * @return
	 */
	protected abstract String getPrimaryKey();
	
	/**
	 * 返回该表软删除使用的字段，查询列表的方法会自动过滤该字段值(如果未指定该字段(null或者空字符串)，则认为该表不具有软删除功能)
	 * @return
	 */
	protected abstract String getSoftDelField();
	/**
	 * 构造方法检查该有的变量是否有
	 */
	public BaseService() {
		try {
			if (getTableName()==null||getTableName().equals("")) {
				throw new Exception(this.getClass().getName()+" 中的getTableName()方法没有指定表名称");
			}
			if (getPrimaryKey()==null||getPrimaryKey().equals("")) {
				throw new Exception(this.getClass().getName()+" 中的getPrimaryKey()方法没有指定表的主键名称");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 检查whereHelper是否为null,是旧返回空字符串，不是就toString
	 * @param whereHelper
	 * @return
	 */
	private String CheckWhereHelper(WhereHelper whereHelper) {
		return  (whereHelper==null)?"": whereHelper.toString();
	}
	
	/**
	 * 检查软删除字段是否存在，存在返回true，不存在返回false
	 * @return
	 */
	private boolean CheckSoftDelField() {
		if(getSoftDelField()==null||getSoftDelField().equals("")) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 添加softDelField的过滤条件
	 * @param whereHelper
	 * @return
	 */
	private WhereHelper addSoftDelField(WhereHelper whereHelper) {
		if (CheckSoftDelField()) {
			if (whereHelper==null) {
				whereHelper = new WhereHelper();
			}
			whereHelper.addStrWhere(" and "+getSoftDelField()+"= "+NOT_DEL+"");
		}
		return whereHelper;
	}
	
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
	public boolean updateFieldIsNull(String fieldName,String primaryId) {
		String sql = "UPDATE "+getTableName()+" SET "+fieldName+" = NULL WHERE "+getPrimaryKey()+"='"+primaryId+"'";
		int row = Db.update(sql);
		if (row<1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 查询列表(如果设置过软删除字段自动过滤)
	 * @param whereHelper 查询条件
	 * @return
	 */
	public List<Record> getList(WhereHelper whereHelper){
		addSoftDelField(whereHelper);
		String sql ="select * from "+getTableName()+CheckWhereHelper(whereHelper);
		return Db.find(sql);
	}
	
	/**
	 * 查询列表(分页)的列表数据(如果设置过软删除字段自动过滤)
	 * @param pageNumber 页码
	 * @param pageSize 每页多少条
	 * @param whereHelper 查询条件
	 * @return
	 */
	public Page<Record> getListPage(int pageNumber,int pageSize,WhereHelper whereHelper){
		SqlPara sqlPara = new SqlPara();
		addSoftDelField(whereHelper);
		sqlPara.setSql("select * from "+getTableName()+CheckWhereHelper(whereHelper));
		return Db.paginate(pageNumber, pageSize, sqlPara);
	}
	
	/**
	 * 根据主键id得到一条记录
	 * @param id
	 * @return
	 */
	public Record getById(String id) {
		return Db.findById(getTableName(), getPrimaryKey(), id);
	}
	
	/**
	 * 保存一条记录，根据是否有主键来决定新增还是修改
	 * @param record
	 * @return
	 */
	public boolean save(Record record) {
		boolean flag = false;
		if (record.getStr(getPrimaryKey())==null||record.getStr(getPrimaryKey()).equals("")) {
			record.set(getPrimaryKey(), UUID.randomUUID().toString().replaceAll("-", ""));
			flag = Db.save(getTableName(),getPrimaryKey(), record);
		}else {
			flag = Db.update(getTableName(),getPrimaryKey(), record);
		}
		return flag;
	}
	
	/**
	 * 根据主键id删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id) {
		return Db.deleteById(getTableName(),getPrimaryKey(), id);
	}
	
	public boolean deleteSoftById(String id) {
		boolean flag = false;
		try {
		if (getSoftDelField()==null||getSoftDelField().equals("")) {
				throw new Exception(this.getClass().getName()+"没有指定该表的软删除字段，不允许进行软删除");
		}
		Record record = new Record();
		record.set(getPrimaryKey(), id);
		record.set(getSoftDelField(), DEL);
		flag = Db.update(getTableName(), getPrimaryKey(), record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 批量保存，根据flag标记来判断删除还是新增修改(c新增u修改r读取d删除)u在无id的情况下为新增
	 * @param kpiList
	 * @return
	 */
	public boolean batchCURDSaveList(List<Record> list) {
		boolean flag = true;
		 for (Record record : list) {
			 String recordFlag = record.getStr(EDIT_FLAG);
			 String id = record.getStr(getPrimaryKey());
			 if (recordFlag!=null&&recordFlag.equals("d")&&id!=null&&!id.equals("")) {
				 record.remove(EDIT_FLAG);
				flag  =deleteById(id);
			}else if(recordFlag!=null&&(recordFlag.equals("c")||recordFlag.equals("u"))) {
				record.remove(EDIT_FLAG);
				flag = save(record);
			}
		}
		 return flag;
	}
}
