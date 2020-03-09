/**
 * author: loserStar
 * date: 2019年4月11日上午10:05:20
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * author: loserStar
 * date: 2019年4月11日上午10:05:20
 * remarks:发送邮件的工具类
 */
public class LoserStarEmailUtil {

    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）, 
    //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    private static String myEmailAccount = "";
    private static String myEmailPassword = "";

    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
//    public static String myEmailSMTPHost = "smtp.qq.com";
    private static String myEmailSMTPHost = "";

    /**
     * 初始化参数
     * @param email 自己的邮箱地址，当做发送的邮箱账号 
     * @param emailPassword 自己的邮箱SMTP授权密码
     * @param emailSMTPHost 你所用的邮箱服务器地址
     */
    public static void init(String email,String emailPassword,String emailSMTPHost) {
    	myEmailAccount = email;
    	myEmailPassword = emailPassword;
    	myEmailSMTPHost = emailSMTPHost;
    }
	/**
	 * 发送一封邮件
	 * @param targetEmail 
	 * @param title
	 * @param content
	 * @throws Exception 
	 */
	public static void send(String targetEmail,String title,String content) throws Exception {
		// 1. 创建一封邮件
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = createProperties("smtp", myEmailSMTPHost, "true");                    // 参数配置
     // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log
     // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, targetEmail,title,content);
        
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        
     // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        // 
        //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
        //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
        //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
        //
        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
        //           (1) 邮箱没有开启 SMTP 服务;
        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
        //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
        //
        //    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
        transport.connect(myEmailAccount, myEmailPassword);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
    	transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
        
	}
	  
	/**
	 * 创建配置文件
	 * @param protocol 使用的协议（JavaMail规范要求）
	 * @param sendMail 发件人的邮箱的 SMTP 服务器地址
	 * @param auth 需要请求认证
	 * @return
	 * @throws Exception 
	 */
	private static Properties createProperties(String protocol,String sendMail,String auth) throws Exception {
		if (protocol==null||protocol.equals("")) {
			throw new Exception("相关参数没有传递，请先调用一次init(String email,String emailPassword,String emailSMTPHost) 初始化参数");
		}
		if (sendMail==null||sendMail.equals("")) {
			throw new Exception("相关参数没有传递，请先调用一次init(String email,String emailPassword,String emailSMTPHost) 初始化参数");
		}
		if (auth==null||auth.equals("")) {
			throw new Exception("相关参数没有传递，请先调用一次init(String email,String emailPassword,String emailSMTPHost) 初始化参数");
		}
		Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        return props;
	}
	/**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    private static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,String title,String content) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail,sendMail, "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, receiveMail, "UTF-8"));

        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(title, "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent(content, "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();
        return message;
    }
    
    public static void main(String[] args) {
		try {
//			LoserStarEmailUtil.init("879264734@qq.com", "jdnecqgbwhqkbehh", "smtp.qq.com");
			LoserStarEmailUtil.init("18077968@qq.com", "wgewpfdrhkbcbhdf", "smtp.qq.com");
			LoserStarEmailUtil.send("06000849@hongta.com", "测试loserStarEmailUtil封装", "这是内容，带个<br/>超链接试试<a href='http://www.baidu.com'>百度</a>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
