/*步骤：
1.Load the Driver
			1 Class.forName()| Class.forName().newInstance()|new DriverName()
			2 实例化时自动向DriverManager注册，不用显示调用DriverManager.registerDriver方法
			
2.Connect to the DataBase
			1 DriverManager.getConnection()
			
3.Execute the SQL
      1 Connection.CreaterStatement()
      2 Statement.ExecuteQuery()
      3 Statement.ExecuteUpdate()
			
4.Retrieve the result 			
			1 循环取的内容 while(rs.next())

5. Show the result Data
			1 将数据库中的各种类型转换为java中的类型，getXXX方法
6.Close
  1 close the resultset/close the statement/close the connection
-------------------------------------------------------------------------------

代码：

Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");//自动在堆中new出jdbcdriver类的对象，等同于 new sun.jdbc.odbc.JdbcOdbcDriver();

Connection con = DriverManager.getConnection("jdbc:odbc:Book","admin","xyz");//多态，父类接口对象指向子类对象，drivermenager得到一个连接，数据库连接字符串

Statement stmt = con.CreaterStatement();//创建SQL语句

//ResultSet rs = stmt.ExecuteQuery("SELECT * FROM table1");//执行查询,返回游标，只在这条记录的上面

//stmt.ExecuteUpdate("INSERT INTO table1 values(值，值，值)");//执行更新
while(rs.next()){
	rs.getString("字段一");
	rs.getInt("字段二");
	.....
}
rs.close();
stmt.close();
con.close


------------------------------------------------------
//存储过程

-----------------------------------------------------
//批处理
stmt.addBatch("INSERT INTO table1 values(值，值，值)");
stmt.addBatch("INSERT INTO table1 values(值，值，值)");
stmt.addBatch("INSERT INTO table1 values(值，值，值)");
stmt.ExecuteBatch();

PreparedStatement ps = conn.prepareStatement("insert into table values(?,?,?)");
ps.setInt(1,11);
ps.setString(2,"asdf");
ps.setString(3,"bbbb");
ps.addBatch();


-----------------------------------------------------------
//设置参数
PreparedStatement pstmt = conn.PreparedStatement("INSERT INTO table1 values(?，?，?)");
pstmt.setInt(1,60);
pstmt.setString(2,"Hello");
pstmt.setString(3,"lxx");
pstmt.ExecuteUpdate();


----------------------------------------------------------------------------
//两条一起执行或两条都不执行,Transaction
conn.setAutoCommit(false);
stmt = conn.CreaterStatement();
stmt.addBatch("INSET INTO VALUES (值,值,值)");
stmt.ExecuteBatch();
stmt.commit();//手动提交
conn.setAutoCommit(true);//自动提交，默认自动提交

conn.rellback();//回滚

-----------------------------------------------------------------
//可滚动结果集
Statement stmt =conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

rs.next();//下一条
rs.last();//跳刀最后一条
rs.previous();//上一条
rs.absolute(3);//定位到第三行
rs.isLast();//是不是最后一条
rs.isAfterLast();//是不是最后一条的下一条
rs.getRow();//当前第几行


-------------------------------------------------------------
  DataSource
DriverManager的替代
连接池的实现
分布式的实现

RowSet
新的ResultSet
支持javabean
---------------------------------------------------------------
返回当前插入的那条记录的ID
String sql = "insert into article values (null, 0, ?, ?, ?, now(), 0)";
PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//返回当前插入的key
	Statement stmt = conn.createStatement();
	
	pstmt.setInt(1, -1);
	pstmt.setString(2, title);
	pstmt.setString(3, cont);
	pstmt.executeUpdate();

	ResultSet rsKey = pstmt.getGeneratedKeys();
	rsKey.next();
	int key = rsKey.getInt(1);
	rsKey.close();
	stmt.executeUpdate("update article set rootid = " + key + " where id = " + key);


 */