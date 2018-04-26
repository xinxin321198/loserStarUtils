/**
 * author: loserStar
 * date: 2018年4月26日上午10:06:25
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.servlet;

/**
 * author: loserStar
 * date: 2018年4月26日上午10:06:25
 * remarks:
 */
public class ServletNote {
/**
 
--------------------------------------web.xml中JSP的配置------------------------------------------------------------
<welcome-file-list>、
		<!--设置默认首页-->
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.htm</welcome-file>
        <welcome-file>index.jsp</welcome-file>
		<welcome-file>myservlet</welcome-file>
</welcome-file-list>
--------------------------------------web.xml中servlet配置---------------------------------------------------------------------------------------
<!--配置和映射servlet-->
<servlet>
	<!--servlet注册的名字-->
	<servlet-name>myservlet</servlet-name>
	<!--servlet的全类名-->
	<servlet-class>com.lxx.HelloWorldServlet</servlet-class>

	<!--servlet初始化的参数-->
	<!--String value = getServletConfig().getInitParameter("name");获取指定参数名的初始化参数-->
	<!--Enumeration<String> names = getServletConfig().getInitParameterNames();获取初始化参数的集合-->
	<init-param>
		<param-name>name</param-name>
		<param-value>value</param-value>
	</init-param>

	<!--servlet的创建时机，根据数字越小越早加载，如果为正数：servlet被容器加载时创建。如果为负数：第一次被请求时创建-->
	<load-on-startup>3</load-on-startup>
</servlet>

<!--servlet的映射-->
<servlet-mapping>
	<!--要映射的servlet-->
	<servlet-name>myservlet</servlet-name>
	<!--映射出来访问的路径，/代表当前web应用的根目录-->
	<url-pattern>/index.html</url-pattern>
</servlet-mapping>

<servlet-mapping>
	<servlet-name>myservlet</servlet-name>
	<!--所有以html结尾的都有它处理-->
	<url-pattern>*.html</url-pattern>
</servlet-mapping>

<servlet-mapping>
	<servlet-name>myservlet</servlet-name>
	<!--会匹配到/login这样的路径型url，不会匹配到模式为*.jsp这样的后缀型url-->
	<url-pattern>/</url-pattern>
</servlet-mapping>

<servlet-mapping>
	<servlet-name>myservlet</servlet-name>
	<!-会匹配所有url：路径型的和后缀型的url(包括/login,*.jsp,*.js和*.html等--->
	<url-pattern>/*</url-pattern>
</servlet-mapping>

<servlet-mapping>
	<servlet-name>myservlet</servlet-name>
	<!--非法的配置-->
	<url-pattern>/*.html</url-pattern>
</servlet-mapping>



------------------------获取servlet初始化参数,直接在init方法的参数中添加----------------------------------------------------
public void init(ServletConfig servletConfig) {
	
}



--------------------获取servletContext-------------------------------------------------------------------
----------------------web.xml中配置
<!--配置当前web应用的初始化参数-->
<context-param>
	<param-name>driver</param-name>
	<param-value>com.mysql.jdbc.Driver</param-value>
</context-param>

------------------------servlet中获取------------------------------------
ServletContext servletContext = servletConfig.getServletContext();
String driver = servletContext.getInitParameter("driver");

																						------------------------------------------------------form表单的action路径------------------------------------------------------------------------------
<from action="./"></from>--当前请求的路径开始
<from action="/"></from>--servlet container的ROOT路径开始
<from action=""></from>--当前请求的路径开始







																						-----------------------------------------------------Drective编译指令----------------------------------------------------------------------------------

<%@page language="java"|
		extends="className"|
		import="importList"|
		buffer="none|8kb"|--不缓冲|默认8K
		session"true|false"|--是否可以使用session
		autoFlush="true|false"|--缓冲器自动清除，默认true
		isThreadSafe="true|false"|--默认false
		info="info"|--描述信息
		errorPage"url"|--错误之后显示的页面
		isErrorPage="true|false"|--是不是错误显示页面
		contentType="text/html;charset=gb2312"
		
%>
<%@include fiel="URL"%>--包含其它页面，编译期间，不能传参数(静态包含)
<%@taglib%>--自定义标签库

																						-----------------------------------------------------Action 运行期间指令------------------------------------------------------------------------------


																																		javabean
<jsp:useBean id="cd" class="bean.Class" type="java.lang.Object" scope="request|session|appliction">--<%java.lang.Object cd =new bean.Class()%>--与javabean关联，声明/实例化一个javabean，实例名是cd
	<jsp:setProperty name="cd" property="count" value="23"><%cd.setCount(23)%>--bean的实例名为cd的count属性为23
	<jsp:getProperty name="cd" property="count"><%out.write(cd.getCount())%>--得到bean的实例名为cd的count属性的值
	
	<jsp:setProperty name="cd" property="iii" value="<%=request.getparameter("number")%>"/>--把参数number的值，赋值给iii
		等同于
	<jsp:setProperty name="cd" property="iii" param="number"/>--参数类型String JSP自动帮转其他类型
</jsp:useBean>

<jsp:inclue page="URLSpec" flush="true">--动态包含，不被编译，可以传参数
	<jsp:param name="ParamName" value="value"/>
</jsp:include>
	
<jsp:forward page="URLSpec" flush="true">
	<jsp:param name="paramName" value="value"/>
</sjp:forward>     与response.sendRedirect("URL");区别
	forward相当于在当前页面显示目标页面，在服务器跳转返回，request是同一个对象，request.getParameter("valueName")可取到URL里所传的参数。SendRedirect是直接跳转到目标页面，重新访问一个页面
jsp:plugin（嵌入applet程序）









request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";//获取项目地址

																						-------------------------------------------------------------------------------JSP内置对象-----------------------------------------------------

																																		out
	缓冲输的输出流，javax.sevlet.jsp.JspWriter的一个实例
	println();向客户端输出各种类型数据
	newLine();换行符
	close();关闭输出流
	flush()  Flush the stream. 
	clearBuffer();清楚缓冲区里的数据，同时把数据输出到客户端
	clear();清楚缓冲区数据，不把数据输出到客户端
	getBufferSize();返回缓冲区
	
	
																												request（范围：一个请求）
	一个JSP页面的请求，javax.servlet.http.HttpServletRequest
	String getParameter(String name);拿到参数
	getMethod();返回传送数据的方法
	public Enumeration getParameterNames();得到表单内所有元素的name属性值，再通过遍历enumeration，用getParameter("name")取得所对应的name的value的值
	public String[] getParameterValues(String name);获得指定参数的所有值,<input name="name">,取name属性
	getRequestURL();获得请求的客户端地址
	getRemoteAdd();获得客户端IP
	getRemoteHost();获得客户端名称
	getRemotePort();获得客户端的端口号
	getServerName();获得服务器名字
	getServletName();获得客户端请求脚本文件路径
	getServerPort();获得服务器端的端口
	
	setAttribute(String name, Object o) Stores an attribute in this request. 
	getAttribute(String name) Returns the value of the named attribute as an Object, or null if no attribute of the given name exists. 
 
	
	
																																	response
	返回给客户端的响应，javax.servlethttp.HttpServletResponse
	addCookie(Cookie cookie);添加一个Cookie对象，用于在客户端保存信息
	addHeader(String name,String value)添加HTTP头信息，该信息发送到客户端
	ServletOutputStream getOutputStream()返回可以向客户端发送二进制数据的输出流
	PrintWriter getWriter() 返回可以向客户端发送字符数据的对象
	String getCharacterEncoding() 得到发送的相应数据的字符编码
	getContentType()返回发送的相应数据的MIME类型	
	containsHeader(String name)判断指定名字的HTTP文件头是否存在
	sendError(int );想客户端发送错误信息
	sendRedirect(String URL);重定向JSP文件，加上“/”相对于servlet container的root的RUL，没有“/”是相对于请求的ROOT路径（与<jsp:forward page="url"/>区别）
	setContentType(String contentType);设置编码
			
				
				
				
																											pageContext(一般服务器调用)
  findAttribute
  getAttribute
  getAttributesScope
  getAttributeNamesInScope
  
  
  
  
																												session(范围：浏览器不关闭)
	setAttribute(String name, Object value) 以键值对的方式储存进session
	getAttribute(String name) 得到session，返回object
	 String getId()  Returns a string containing the unique identifier assigned to this session
	 boolean isNew() 是否是新建的session
	  int getMaxInactiveInterval() 不活动状态的最大存活时间，秒
	   void invalidate() 是当前的session失效
 
 

																								application（servletContext）（范围：服务器不关闭就一直存在）
	int getMajorVersion() 返回servletAPI版本号
	URL getResource(String path) 返回URL
	String getServerInfo() 返回servlet容器
	String getRealPath(String path) 返回绝对文件路径
	//getRealPath("/test.txt");//根目录下的test.txt
	getResourceAsStream(String path);//path的/为当前WEB应用的根目录
	//InputStream is = servletContext.getResourceAsStream("/WEB-INF/classes/jdbc.properties");


																								cookie(存放在本地的键值对，类似于session):
	servlet中使用：
	Cookie cookie = new Cookie(String key,Object value);
	cookie.setMaxAge(15);
	resp.addCookie(cookie);//添加进response，输出到客户端
	Cookie[] cookies = req.getCookies();//从request中取得cookie的数组


	
	

																														config
																														exception
	<%@ page isErrorPage="true"%>加入指令才能使用
page





																						------------------------------------------------------------------------------JSP和servlet互相跳转-----------------------------------------------------

servlet:
		getServletConfig().getServletContext().getRequestDispatcher("jspURL").forward(request,response);加”./“或者""(直接写文件名)路径相对于servlet container的root下的项目的路径，加"/"路径相对于当前请求路径
		或
		RequestDispatcher rd = req.getRequestDispatcher(url);
		rd.forward(req.resp);
		
JSP:
		<jsp:forward page="URL"/>



																						------------------------------------------------------------------------servlet过滤器(filter)-----------------------------------------------------

	所有servlet过滤器类都必须实现javax.servlet.Filter接口，访问servlet之前校验信息
	
	destroy()//销毁
	doFilter(ServletRequest request, ServletResponse response, FilterChain chain)//继续执行请求
	init(FilterConfig filterConfig) 读web.xml中的初始化参数

web.xml中的配置：
<filter>
	<filter-name>LoginFilter</filter-name>
	<filter-class>com.lxx.LoginFilter</filter-class>	
</filter>

<filter-mapping>
	<filter-name>LoginFilter</filter-name>
	<url-pattern>/*</url-pattern> <!--所有请求都过滤-->
</filter-mapping>



-------------------------------------------------------------------------Servlet监听器-----------------------------------------------------
ServletContextListener:
void contextDestroyed(ServletContextEvent sce) web应用结束时后调用
void contextInitialized(ServletContextEvent sce) web应用启动前时调用

ServletContextAttributeListener:
void attributeAdded(ServletContextAttributeEvent scab) 添加完之后调用
void attributeRemoved(ServletContextAttributeEvent scab) 属性移除掉之后调用
void attributeReplaced(ServletContextAttributeEvent scab) 替换掉之后调用

HttpSessionListener:
void sessionCreated(HttpSessionEvent se) 创建一个session时候调用
void sessionDestroyed(HttpSessionEvent se) 一个session失效后调用

HttpSessionAttributeListener：
attributeAdded(HttpSessionBindingEvent se) 
attributeRemoved(HttpSessionBindingEvent se) 
attributeReplaced(HttpSessionBindingEvent se) 


web.xml中的配置:
<listener>
	<listener-class>
		com.lxx.xxxx
	</listener-class>
</listener>

----------------------------------EL表达式------------------------------------------
${param.xx}  相对于request.getParameter("xx");
${bean.attribute} 得到javabean的属性
${seesionScope.attribute} 得到session里的值
${paramVlues.xxx[3]} 得到表单中同一个name的多个元素的值  相对于request.getParameterValues("xxx");

支持运算符
+-* /%
支持比较符
== != < > <= >=
支持逻辑运算符
&&
||
!

------------------------------------------自定义标签库-----------------------------------------
一。创建标签处理类
二。创建标签库描述文件
三。JSP中引入标签库，然后插入标签<mm:hello/>


 
 
 
 */
}
