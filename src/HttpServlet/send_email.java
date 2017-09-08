package HttpServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//发送邮件包
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

//json格式处理包
import net.sf.json.JSONObject;

/**
 * Servlet implementation class send_email
 */
@WebServlet("/send_email")
public class send_email extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String host = "smtp.exmail.qq.com";

	private static final String rd_email = "rd@kingcomtek.com";

	private static final String songhui_email = "songhui@kingcomtek.com";
	private static final String baoxinjin_email = "baoxinjin@kingcomtek.com";
	private static final String gongguolong_email = "gongguolong@kingcomtek.com";
	private static final String zhouzhonghua_email = "zhouzhonghua@kingcomtek.com";
	private static final String tangfeng_email = "tangfeng@kingcomtek.com";
	private static final String lichen_email = "lichen@kingcomtek.com";
	private static final String xichaobo_email = "xichaobo@kingcomtek.com";
	private static final String wangzhiyong_email = "wangzhiyong@kingcomtek.com";
	private static final String zhouqinlong_email = "zhouqinlong@kingcomtek.com";
	private static final String lidongyang_email = "lidongyang@kingcomtek.com";
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public send_email() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String acceptjson = "";
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			br.close();
			acceptjson = sb.toString();
			if (acceptjson != "") {
				log("acceptjson="+acceptjson);
				
				JSONObject jo = JSONObject.fromObject(acceptjson);

				// 收件人的电子邮件
				String to1 = jo.getString("to");
				String to2 = to1.substring(1, to1.length()-1);
				String[] to=to2.split("&");
				for(int n=0;n<to.length;n++){
					to[n]=setEmail(to[n]);
				}
				
				String rd = jo.getString("rd");
				String content = jo.getString("content");
				String result = "没有结果";
				String subject="发货订单系统测试";
				// 发件人的电子邮件	
				String username = jo.getString("account");
				String password = jo.getString("password");
				log("kkkkk:"+to.length+"rd:"+rd +" content:"+content+" username:"+username+" password:"+password);
				EmailManager email = new EmailManager(host, username, password);
				boolean b;
				if(rd.equals("YES")){
					b=email.sendMail(username, to, rd_email, subject, content, "");
					log("发送抄送邮件");
				}else{
					b=email.sendMail(username, to, "", subject, content, "");
					log("发送不带抄送邮件");
				}
				if(b){
					result="1";
					log("发送成功");
				}else{
					result="0";
					log("发送失败");
				}
				out.print(result);
				
				
                /*
				Properties props = new Properties();

				// Setup mail server
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.auth", "true"); // 这样才能通过验证
				try {
					// Get session
					Session mailsession = Session.getDefaultInstance(props);

					// watch the mail commands go by to the mail server
					// mailsession.setDebug(sessionDebug);

					// Define message
					MimeMessage message = new MimeMessage(mailsession);
					message.setFrom(new InternetAddress(from));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
					message.setSubject("订单系统");
					message.setText("订单系统测试");
					// Send message
					message.saveChanges();
					Transport transport = mailsession.getTransport("smtp");
					transport.connect(host, username, password);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();
					result = "发送邮件成功";
				} catch (MessagingException mex) {
					mex.printStackTrace();
					result = "发送邮件失败";
				}
				log("发送邮件结果=" + result);
				*/
				
			}
		} catch (Exception e) {
			out.print("0");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	//设置发送人邮箱
	public String setEmail(String str){
		String ss=lidongyang_email;
		if(str.equals("songhui")){
			ss=songhui_email;
		}
		if(str.equals("baoxinjin")){
			ss=baoxinjin_email;
		}
		if(str.equals("gongguolong")){
			ss=gongguolong_email;
		}
		if(str.equals("zhouzhonghua")){
			ss=zhouzhonghua_email;
		}
		if(str.equals("tangfeng")){
			ss=tangfeng_email;
		}
		if(str.equals("lichen")){
			ss=lichen_email;
		}
		if(str.equals("xichaobo")){
			ss=xichaobo_email;
		}
		if(str.equals("wangzhiyong")){
			ss=wangzhiyong_email;
		}
		if(str.equals("zhouqinlong")){
			ss=zhouqinlong_email;
		}
		if(str.equals("lidongyang")){
			ss=lidongyang_email;
		}
		return ss;
	}
	// 发送邮件类
	public class EmailManager {

		private Properties props; // 系统属性
		private Session session; // 邮件会话对象
		private MimeMessage mimeMsg; // MIME邮件对象
		private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

		/**
		 * Constructor
		 * 
		 * @param smtp
		 *            邮件发送服务器
		 */
		public EmailManager() {
			props = System.getProperties();
			props.put("mail.smtp.auth", "false");
			session = Session.getDefaultInstance(props, null);
			session.setDebug(true);
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();
		}

		/**
		 * Constructor
		 * 
		 * @param smtp
		 *            邮件发送服务器
		 */
		public EmailManager(String smtp, String username, String password) {
			props = System.getProperties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", smtp);
			props.put("username", username);
			props.put("password", password);
			session = Session.getDefaultInstance(props, null);
			session.setDebug(true);
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();
		}

		/**
		 * 发送邮件
		 */
		public boolean sendMail(String from, String[] to, String copyto, String subject, String content,
				String filename) {
			try {
				// 设置发信人
				mimeMsg.setFrom(new InternetAddress(from));
				// 设置接收人
				for (int i = 0; i < to.length; i++) {
					mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to[i]));
				}
				// 设置抄送人
				if(copyto.length()!=0){
					mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto));
				}
					
				// 设置主题
				mimeMsg.setSubject(subject);
				// 设置正文
				BodyPart bp = new MimeBodyPart();
				bp.setContent(content, "text/html;charset=utf-8");
				mp.addBodyPart(bp);
				// 设置附件
				if(filename.length()!=0){
					bp = new MimeBodyPart();
					FileDataSource fileds = new FileDataSource(filename);
					bp.setDataHandler(new DataHandler(fileds));
					bp.setFileName(MimeUtility.encodeText(fileds.getName(), "UTF-8", "B"));
					mp.addBodyPart(bp);
				}
				
				mimeMsg.setContent(mp);
				mimeMsg.saveChanges();
				// 发送邮件
				if (props.get("mail.smtp.auth").equals("true")) {
					Transport transport = session.getTransport("smtp");
					transport.connect((String) props.get("mail.smtp.host"), (String) props.get("username"),
							(String) props.get("password"));
					transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
					if(copyto.length()!=0){
						transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
					}
					
					transport.close();
				} else {
					Transport.send(mimeMsg);
				}
				System.out.println("邮件发送成功");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}

}
