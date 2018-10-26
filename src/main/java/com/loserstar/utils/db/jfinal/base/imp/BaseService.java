package com.loserstar.utils.db.jfinal.base.imp;

import java.util.List;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.loserstar.utils.idgen.SnowflakeIdWorker;

/**
 * 
 * author: loserStar
 * date: 2018年10月26日下午12:00:40
 * remarks:基础service
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
	public static final String EDIT_FLAG = "curd_flag";
	public static final String EDIT_FLAG_C = "c";
	public static final String EDIT_FLAG_U = "u";
	public static final String EDIT_FLAG_R = "r";
	public static final String EDIT_FLAG_D = "d";
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
	 * 检查whereHelper是否为null,是旧返回空字符串，不是就加上where 并且toString
	 * @param whereHelper
	 * @return
	 */
	private String CheckWhereHelper(WhereHelper whereHelper) {
		String result = "";
		if(whereHelper!=null) {
			if (whereHelper.getStrWhereList()!=null&&whereHelper.getStrWhereList().size()>0) {
				result = " where "+whereHelper.toString();
			}
		}
		return  result;
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
	 * 统计数量
	 * @param whereHelper
	 * @return
	 */
	public int getCount(WhereHelper whereHelper) {
		String sql ="select COUNT("+getPrimaryKey()+") count from "+getTableName()+CheckWhereHelper(whereHelper);
		return get(sql).getInt("count");
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
	public boolean updateFieldIsNull(String fieldName,long primaryId) {
		String sql = "UPDATE "+getTableName()+" SET "+fieldName+" = NULL WHERE "+getPrimaryKey()+"="+primaryId+"";
		int row = Db.update(sql);
		if (row<1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 查询列表
	 * new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param whereHelper 查询条件
	 * @return
	 */
	public List<Record> getList(WhereHelper whereHelper){
		addSoftDelField(whereHelper);
		String sql ="select * from "+getTableName()+CheckWhereHelper(whereHelper);
		return Db.find(sql);
	}
	
	/**
	 * 多表连接查询，默认查询出的字段使用*（此方法会造成如果两张表有相同名称的字段，会显示不全）
	 * @param joinHelper
	 * @param whereHelper
	 * @return
	 */
	public List<Record> getJoinList(JoinHelper joinHelper,WhereHelper whereHelper) {
		return getJoinList(null, joinHelper, whereHelper);
	}
	
	/**
	 * 多表连接查询，并且指定查询出的字段名称
	 * 	new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param selectFiled
	 * @param joinHelper
	 * @param whereHelper
	 * @return
	 */
	public List<Record> getJoinList(String selectFiled,JoinHelper joinHelper,WhereHelper whereHelper) {
		addSoftDelField(whereHelper);
		if (selectFiled==null||selectFiled.equals("")) {
			selectFiled = " * ";
		}
		String sql = "select "+selectFiled+" from "+getTableName();
		if (joinHelper!=null) {
			sql+=joinHelper.toString();
		}
		sql+=CheckWhereHelper(whereHelper);
		return Db.find(sql);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据
	 * 	new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param whereHelper
	 * @return
	 */
	public Record getFirstList(WhereHelper whereHelper){
		addSoftDelField(whereHelper);
		String sql ="select * from "+getTableName()+CheckWhereHelper(whereHelper)+" FETCH FIRST 1 ROWS ONLY";
		return get(sql);
	}
	
	/**
	 * 查询列表(分页)的列表数据
	 * 	new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param pageNumber 页码
	 * @param pageSize 每页多少条
	 * @param whereHelper 查询条件
	 * @return
	 */
	public Page<Record> getListPage(int pageNumber,int pageSize,WhereHelper whereHelper){
		addSoftDelField(whereHelper);
		String sqlExceptSelect = "from "+getTableName()+CheckWhereHelper(whereHelper);
		return Db.paginate(pageNumber, pageSize, "select *",sqlExceptSelect);
	}
	
	/**
	 * 根据主键id得到一条记录
	 * @param id
	 * @return
	 */
	public Record getById(String id) {
		return Db.findById(getTableName(), getPrimaryKey(), id);
	}
	public Record getById(long id) {
		return Db.findById(getTableName(), getPrimaryKey(), id);
	}
	
	/**
	 * 保存一条记录，根据是否有主键来决定新增还是修改(自动生成去横岗的guid)
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
	 * 保存一条记录，根据是否有主键来决定新增还是修改(自动生成自增的guid，用作排序)
	 * @param record
	 * @return
	 */
	public boolean saveLongGuid(Record record) {
		boolean flag = false;
		if (record.getStr(getPrimaryKey())==null||record.getStr(getPrimaryKey()).equals("")) {
			record.set(getPrimaryKey(), SnowflakeIdWorker.FakeGuid());
			flag = Db.save(getTableName(),getPrimaryKey(), record);
		}else {
			flag = Db.update(getTableName(),getPrimaryKey(), record);
		}
		return flag;
	}
	
	/**
	 * 新增
	 * @param record
	 * @return
	 */
	public boolean insert(Record record) {
		return Db.save(getTableName(),getPrimaryKey(), record);
	}
	
	/**
	 * 修改
	 * @param record
	 * @return
	 */
	public boolean update(Record record) {
		return Db.update(getTableName(), getPrimaryKey(),record);
	}
	
	/**
	 * 删除本表的所有数据
	 * @return
	 */
	public int deleteAll() {
		return Db.update("DELETE FROM "+getTableName());
	}
	
	/**
	 * 根据主键id删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id) {
		return Db.deleteById(getTableName(),getPrimaryKey(), id);
	}
	
	/**
	 * 根据主键软删除一条记录
	 * @param id
	 * @return
	 */
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
				flag = saveLongGuid(record);
			}
		}
		 return flag;
	}
	
	/**
	 * 添加一个curd_flag=r的K-V到record对象中
	 * @param record
	 * @return
	 */
	public static Record setCURDFlag(Record record,String curdValue) {
		record.set(EDIT_FLAG, curdValue);
		return record;
	}
	/**
	 * 批量的添加一个curd_flag=r的K-V到record对象中
	 * @param record
	 * @return
	 */
	public static List<Record> setCURDFlag(List<Record> records,String curdValue) {
		for (Record record : records) {
			record.set(EDIT_FLAG, curdValue);
		}
		return records;
	}
	
}
