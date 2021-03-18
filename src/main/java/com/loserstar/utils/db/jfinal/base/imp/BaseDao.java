package com.loserstar.utils.db.jfinal.base.imp;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.loserstar.utils.db.jfinal.base.imp.WhereHelper;

public abstract class BaseDao{
	public enum DBType{
		mysql,db2,oracle,sqlserver
	}
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
	
	protected String dataSourceName;//jfinal使用多数据源时候指定数据源名称
	
	/**
	 * 返回该表软删除使用的字段，查询列表的方法会自动过滤该字段值(如果未指定该字段(null或者空字符串)，则认为该表不具有软删除功能)
	 * @return
	 */
	protected abstract String getSoftDelField();
	
	/**
	 * 构造方法检查该有的变量是否有
	 */
	public BaseDao() {
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
	
	public BaseDao(String dataSourceName) {
		try {
			if (getTableName()==null||getTableName().equals("")) {
				throw new Exception(this.getClass().getName()+" 中的getTableName()方法没有指定表名称");
			}
			if (getPrimaryKey()==null||getPrimaryKey().equals("")) {
				throw new Exception(this.getClass().getName()+" 中的getPrimaryKey()方法没有指定表的主键名称");
			}
			this.dataSourceName = dataSourceName;
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
			}else {
				 result+=" "+(whereHelper.getOrderStr()==null?"":whereHelper.getOrderStr());
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
	 * 检测是否指定jfinal的数据源
	 * @return 指定返回true，未指定返回false
	 */
	private boolean CheckDataSourceName() {
		if (this.dataSourceName==null||this.dataSourceName.equals("")) {
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
			whereHelper.addStrWhere(" and "+getSoftDelWhere());
		}
		return whereHelper;
	}
	
	/**
	 * 得到软删除条件（软删除字段带表名前缀的条件）
	 */
	public String getSoftDelWhere() {
		return getSoftDelWhere(null);
	}
	
	/**
	 * 可以指定软删除字段的表名前缀（在多表连接的时候会指定，如果不指定的话就按照实现类中的表名输出）
	 * @param tableName
	 * @return
	 */
	public String getSoftDelWhere(String tableName) {
		String softDelPre = getTableName();
		if(tableName!=null&&!tableName.contentEquals("")) {
			softDelPre = tableName;
		}
		String softDelWhere = "  ("+softDelPre+"."+getSoftDelField()+"= '0' or "+softDelPre+"."+getSoftDelField()+" is null or "+softDelPre+"."+getSoftDelField()+" = 0)";
		return softDelWhere;
	}
	
	/**
	 * 执行查询sql
	 * @param sql
	 * @return
	 */
	public List<Record> getListBySql(String sql){
		List<Record> resultList = null;
		if (CheckDataSourceName()) {
			resultList = Db.use(this.dataSourceName).find(sql);
		}else {
			resultList = Db.find(sql);
		}
		return resultList;
	}

	/**
	 * 执行查询sql，参数化方式
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<Record> getListBySql(String sql,Object ...values){
		List<Record> resultList = null;
		if (CheckDataSourceName()) {
			resultList = Db.use(this.dataSourceName).find(sql,values);
		}else {
			resultList = Db.find(sql,values);
		}
		return resultList;
	}
	
	public <T extends Model<T>> List<T> getListBySql(String sql,Class<T> class1){
		List<T> tList = null;
		try {
			if (CheckDataSourceName()) {
					tList = class1.newInstance().use(this.dataSourceName).find(sql);
			}else {
				tList = class1.newInstance().find(sql);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tList;
	}
	
	public <T extends Model<T>> List<T> getListBySql(String sql,Class<T> class1,Object ...values){
		List<T> tList = null;
		try {
			if (CheckDataSourceName()) {
				tList = class1.newInstance().use(this.dataSourceName).find(sql,values);
			}else {
				tList = class1.newInstance().find(sql,values);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tList;
	}
	
	/**
	 * 设置某条记录的某个字段为null值
	 * @param tableName 表名
	 * @param fieldName 要设为null的字段名
	 * @param primaryId 主键ID的值
	 * @return
	 */
	public boolean updateFieldIsNull(String fieldName,Object primaryId) {
		String sql = "UPDATE "+getTableName()+" SET ? = NULL WHERE "+getPrimaryKey()+"=?";
		int row = CheckDataSourceName()?Db.use(this.dataSourceName).update(sql,fieldName,primaryId):Db.update(sql,fieldName,primaryId);
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
	public List<Record> getList(WhereHelper whereHelper,Object[] values){
		addSoftDelField(whereHelper);
		return getList_notSoftDel(whereHelper,values);
	}
	
	/**
	 * 查询列表
	 * 顺带转为jfinal实体
	 * new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param <T>
	 * @param whereHelper 查询条件
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> List<T> getList(WhereHelper whereHelper,Class<T> class1,Object[] values) {
		addSoftDelField(whereHelper);
		return getList_notSoftDel(whereHelper,class1,values);
	}
	
	/**
	 * 查询列表(不自动添加软删除过滤)
	 * @param whereHelper
	 * @return
	 */
	public List<Record> getList_notSoftDel(WhereHelper whereHelper,Object[] values){
		String sql ="select * from "+getTableName()+CheckWhereHelper(whereHelper);
		List<Record> resultList = null;
		if (values==null||values.length==0) {
			resultList = getListBySql(sql);
		}else {
			resultList = getListBySql(sql,values);
		}
		return resultList;
	}
	
	/**
	 * 查询列表(不自动添加软删除过滤)
	 * 顺带转为jfinal实体
	 * @param <T>
	 * @param whereHelper 查询条件
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model<T>> List<T> getList_notSoftDel(WhereHelper whereHelper,Class<T> class1,Object[] values) {
		String sql ="select * from "+getTableName()+CheckWhereHelper(whereHelper);
		List<T> tList = null;
			if (values==null||values.length==0) {
				tList = getListBySql(sql, class1);
			}else {
				tList = getListBySql(sql, class1, values);
			}
		return tList;
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据
	 * 	new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param whereHelper
	 * @return
	 */
	public Record getFirstList(WhereHelper whereHelper,DBType dbType,Object[] values){
		addSoftDelField(whereHelper);
		return getFirstList_notSoftDel(whereHelper,dbType,values);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据
	 * new一个whereHelper参数：如果设置过软删除字段自动过滤
	 * null:直接不添加软删除过滤
	 * @param <T>
	 * @param whereHelper
	 * @param dbType 数据库类型（不同数据库获取第一条数据的语句不一样）
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> T getFirstList(WhereHelper whereHelper,DBType dbType,Class<T> class1,Object[] values){
		addSoftDelField(whereHelper);
		return getFirstList_notSoftDel(whereHelper, dbType, class1,values);
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据(不自动添加软删除字段过滤)
	 * @param whereHelper
	 * @param dbType 数据库类型（不同数据库获取第一条数据的语句不一样）
	 * @return
	 */
	public Record getFirstList_notSoftDel(WhereHelper whereHelper,DBType dbType,Object[] values){
		List<Record> list = null;
		String sql ="";
		if(dbType.equals(DBType.db2)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" FETCH FIRST 1 ROWS ONLY";
		}else if(dbType.equals(DBType.mysql)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" LIMIT 1";
		}else if(dbType.equals(DBType.oracle)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" ROWNUM <= 1";
		}else if(dbType.equals(DBType.sqlserver)) {
			sql = "select TOP number|percent column_name(s) from "+getTableName()+CheckWhereHelper(whereHelper);
		}
		if (values==null||values.length==0) {
			list = getListBySql(sql);
		}else {
			list = getListBySql(sql, values);
		}
		if (list!=null&&list.size()!=0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 根据条件查询到的列表，获取第一条数据(不自动添加软删除字段过滤)
	 * @param <T>
	 * @param whereHelper
	 * @param dbType 数据库类型（不同数据库获取第一条数据的语句不一样）
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> T getFirstList_notSoftDel(WhereHelper whereHelper,DBType dbType,Class<T> class1,Object[] values){
		List<T> list = null;
		String sql ="";
		if(dbType.equals(DBType.db2)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" FETCH FIRST 1 ROWS ONLY";
		}else if(dbType.equals(DBType.mysql)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" LIMIT 1";
		}else if(dbType.equals(DBType.oracle)) {
			sql = "select * from "+getTableName()+CheckWhereHelper(whereHelper)+" ROWNUM <= 1";
		}else if(dbType.equals(DBType.sqlserver)) {
			sql = "select TOP number|percent column_name(s) from "+getTableName()+CheckWhereHelper(whereHelper);
		}
		if (values==null||values.length==0) {
			list = getListBySql(sql, class1);
		}else {
			list = getListBySql(sql, class1, values);
		}
		if (list!=null&&list.size()!=0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	
	/**
	 * 查询列表(分页)的列表数据(不自动添加软删除字段过滤)
	 * @param pageNumber
	 * @param pageSize
	 * @param whereHelper
	 * @return
	 */
	public Page<Record> getListPage_notSoftDel(int pageNumber,int pageSize,WhereHelper whereHelper,Object[] values){
		String sqlExceptSelect = "from "+getTableName()+CheckWhereHelper(whereHelper);
		if (values==null||values.length==0) {
			return CheckDataSourceName()?Db.use(this.dataSourceName).paginate(pageNumber, pageSize, "select *",sqlExceptSelect):Db.paginate(pageNumber, pageSize, "select *",sqlExceptSelect);
		}else {
			return CheckDataSourceName()?Db.use(this.dataSourceName).paginate(pageNumber, pageSize, "select *",sqlExceptSelect,values):Db.paginate(pageNumber, pageSize, "select *",sqlExceptSelect,values);
		}
	}
	
	/**
	 * 根据字符串主键id得到一条记录
	 * @param id 联合主键的多个主键值
	 * @return
	 */
	public Record getByIds(Object[] id) {
		String[] pKeys = getPrimaryKey().split(",");
		if (pKeys.length != id.length)
			throw new IllegalArgumentException("primary key number must equals id value number");
		
		List<Record> result = new ArrayList<Record>();
		String sql = "";
		if (CheckDataSourceName()) {
			sql = Db.use(this.dataSourceName).getConfig().getDialect().forDbFindById(getTableName(), pKeys);
			result = Db.use(this.dataSourceName).find(sql, id);
		}else {
			sql = Db.use().getConfig().getDialect().forDbFindById(getTableName(), pKeys);
			result = Db.find(sql, id);
		}
		
		return result.size() > 0 ? result.get(0) : null;
	}
	
	
	/**
	 * 根据字符串主键id得到一条记录
	 * @param <T>
	 * @param class1 要转的实体类的class
	 * @param id 联合主键的多个主键值
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> T getByIds(Class<T> class1,Object[] id) {
		T t = null;
		try {
			if (CheckDataSourceName()) {
					t = (T) class1.newInstance().use(this.dataSourceName).findByIdLoadColumns(id, "*");
			}else {
				t = (T) class1.newInstance().findByIdLoadColumns(id,"*");
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 根据字符串主键id得到一条记录
	 * @param id 主键
	 * @return
	 */
	public Record getById(Object id) {
		Object [] ids = {id};
		return getByIds(ids);
	}
	
	/**
	 * 根据字符串主键id得到一条记录
	 * @param <T>
	 * @param id 联合主键的多个主键值
	 * @param class1 要转的实体类的class
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T extends Model<T>> T getById(Object id,Class<T> class1) {
		Object [] ids = {id};
		return getByIds(class1, ids);
	}
	
	/**
	 * 新增
	 * @param record
	 * @return
	 */
	public boolean insert(Record record) {
		return CheckDataSourceName()?Db.use(this.dataSourceName).save(getTableName(),getPrimaryKey(), record):Db.save(getTableName(),getPrimaryKey(), record);
	}
	
	/**
	 * 新增
	 * @param <T>
	 * @param t jfinal实体对象
	 * @return
	 */
	public <T extends Model<T>> boolean insert(T t) {
		return t.save();
	}
	
	/**
	 * 修改
	 * @param record
	 * @return
	 */
	public boolean update(Record record) {
		return CheckDataSourceName()?Db.use(this.dataSourceName).update(getTableName(), getPrimaryKey(),record):Db.update(getTableName(), getPrimaryKey(),record);
	}
	
	/**
	 * 修改
	 * @param <T>
	 * @param t jfinal实体对象
	 * @return
	 */
	public <T extends Model<T>> boolean update(T t) {
		return t.update();
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
		record.set(getSoftDelField(), "0");
		flag = CheckDataSourceName()?Db.use(this.dataSourceName).update(getTableName(), getPrimaryKey(), record):Db.update(getTableName(), getPrimaryKey(), record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
}
