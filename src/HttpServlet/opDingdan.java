package HttpServlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Constent.mes_data;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Date;
import java.util.Properties;
import java.text.SimpleDateFormat;

/**
 * Servlet implementation class opDingdan
 */
@WebServlet("/opDingdan")
public class opDingdan extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String host = "smtp.exmail.qq.com";
	private static final String[] rd_empty = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public opDingdan() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 进行用户验证
		HttpSession session = request.getSession();
		log("session" + session.getAttribute("account"));
		if (session.getAttribute("account") == null) {
			return;
		}
		log("进入查询订单表");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject all_json_data = new JSONObject();
		JSONObject json_data = new JSONObject();
		JSONArray jsonarray_data = new JSONArray();
		String req_id = request.getParameter("id");
		// 打开数据库查询数据
		try {
			Connection con = Start_database();
			if (con != null) {
				if (req_id != null) {
					ResultSet rs;
					String sql = "select * from dingdan where id=?";
					PreparedStatement check = con.prepareStatement(sql);
					check.setString(1, req_id);
					rs = check.executeQuery();
					while (rs.next()) {
						String id = rs.getString("id");
						String send_cor = rs.getString("send_cor");
						String send_person = rs.getString("send_person");
						String send_phone = rs.getString("send_phone");
						String custom_cor = rs.getString("custom_cor");
						String custom_addr = rs.getString("custom_addr");
						String custom_contacts = rs.getString("custom_contacts");
						String product = rs.getString("product");
						String pay = rs.getString("pay");
						String all_money = rs.getString("all_money");
						String traffic = rs.getString("traffic");
						String date = rs.getString("date");
						String mes = rs.getString("mes");
						String state = rs.getString("state");
						String charge = rs.getString("charge_person");
						String tip = rs.getString("tip");
						String cdate = rs.getString("cdate");

						json_data.put("id", id);
						json_data.put("send_cor", send_cor);
						json_data.put("send_person", send_person);
						json_data.put("send_phone", send_phone);
						json_data.put("custom_cor", custom_cor);
						json_data.put("custom_addr", custom_addr);
						json_data.put("custom_contacts", custom_contacts);
						json_data.put("product", product);
						json_data.put("pay", pay);
						json_data.put("all_money", all_money);
						json_data.put("traffic", traffic);
						json_data.put("date", date);
						json_data.put("mes", mes);
						json_data.put("state", state);
						json_data.put("charge_person", charge);
						json_data.put("tip", tip);
						json_data.put("cdate", cdate);

						jsonarray_data.add(json_data);
					}
					con.close();
					all_json_data.put("data", jsonarray_data);
					out.print(all_json_data);
				} else {
					String sql = "select * from dingdan ORDER BY date DESC LIMIT 30";
					PreparedStatement check = con.prepareStatement(sql);
					ResultSet rs = check.executeQuery();
					while (rs.next()) {
						String id = rs.getString("id");
						String send_cor = rs.getString("send_cor");
						String send_person = rs.getString("send_person");
						String send_phone = rs.getString("send_phone");
						String custom_cor = rs.getString("custom_cor");
						String custom_addr = rs.getString("custom_addr");
						String custom_contacts = rs.getString("custom_contacts");
						String product = rs.getString("product");
						String pro_mes = "";
						String[] aa = product.split("\\|");
						for (int i = 0; i < aa.length; i++) {
							String[] bb = aa[i].split(",", -1);

							if (i < (aa.length - 1)) {
								pro_mes = pro_mes + bb[0] + "【" + bb[2] + "套" + bb[4] + "】+";
							} else {
								pro_mes = pro_mes + bb[0] + "【" + bb[2] + "套" + bb[4] + "】";
							}

						}
						String pay = rs.getString("pay");
						pay = pay.replace("请内部核实", "");
						String traffic = rs.getString("traffic");
						String date = rs.getString("date");
						String mes = rs.getString("mes");
						String state = rs.getString("state");
						String cdate = rs.getString("cdate");
						String all_money = rs.getString("all_money");

						json_data.put("id", id);
						json_data.put("send_cor", send_cor);
						json_data.put("send_person", send_person);
						json_data.put("send_phone", send_phone);
						json_data.put("custom_cor", custom_cor);
						json_data.put("custom_addr", custom_addr);
						json_data.put("custom_contacts", custom_contacts);
						json_data.put("product", pro_mes);
						json_data.put("pay", pay);
						json_data.put("traffic", traffic);
						json_data.put("date", date);
						json_data.put("mes", mes);
						json_data.put("state", state);
						json_data.put("cdate", cdate);
						json_data.put("all_money", all_money);

						jsonarray_data.add(json_data);
					}
					con.close();
					all_json_data.put("data", jsonarray_data);
					out.print(all_json_data);
				}
			} else {
				out.print(all_json_data);
			}
		} catch (Exception e) {
			out.print(all_json_data);
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String s1 = "";
		log("session" + session.getAttribute("account"));
		if (session.getAttribute("account") == null) {
			return;
		}
		s1 = session.getAttribute("account").toString();
		log("进入操作订单表");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String acceptjson = "";
		String result = "-1";
		Connection con = Start_database();

		try {
			con.setAutoCommit(false);
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
				JSONObject jo = JSONObject.fromObject(acceptjson);
				String active = jo.getString("active");
				if (active.equals("insert")) {
					log("进入添加订单");

					String tax = jo.getString("tax");

					String content = jo.getString("content");
					String send_cor = jo.getString("send_cor");
					String send_person = jo.getString("send_person");
					String send_phone = jo.getString("send_phone");
					String custom_name = jo.getString("custom_name");
					String custom_addr = jo.getString("custom_addr");
					String contacts = jo.getString("contacts");
					String product = jo.getString("product");
					String pay = jo.getString("pay");
					String money = jo.getString("money");
					String tip = jo.getString("tip");
					String traffic = jo.getString("traffic");
					String date = jo.getString("date");
					String mes = jo.getString("mes");
					String state = "进行";
					String chaosong = jo.getString("chaosong");
					String person = jo.getString("person");
					String custom_id = jo.getString("custom_id");
					String version = jo.getString("default");

					String get_person = "";
					String person1[] = person.split("-");
					for (int i = 0; i < person1.length; i++) {
						String person2[] = person1[i].split("&");
						get_person = get_person + person2[1] + "-";
					}
					get_person = get_person.substring(0, get_person.length() - 1);
					String s2[] = get_person.split("-");

					Date now = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
					String riqi = dateFormat.format(now);
					String id = "";

					ResultSet rs = null;
					String sql = "select * from dingdan where id>? ORDER BY id for update";
					PreparedStatement check = con.prepareStatement(sql);
					check.setString(1, riqi + "000");
					rs = check.executeQuery();

					String id_a = "";
					int m = 0;
					long mm = Long.parseLong(riqi + "000");
					while (rs.next()) {
						id_a = rs.getString("id");
						m = m + 1;

						long mm1 = mm + m;
						long mm2 = Long.parseLong(id_a);

						if (mm2 > mm1) {
							id = Long.toString(mm1);
							break;
						}

					}

					if (id_a.equals("")) {
						id = riqi + "001";
					} else {
						if (id.equals("")) {
							long nn1 = Long.parseLong(id_a) + 1;
							id = Long.toString(nn1);
						}
					}

					sql = "insert into dingdan value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					check = con.prepareStatement(sql);
					check.setString(1, id);
					check.setString(2, send_cor);
					check.setString(3, send_person);
					check.setString(4, send_phone);
					check.setString(5, custom_name);
					check.setString(6, custom_addr);
					check.setString(7, contacts);
					check.setString(8, product);
					check.setString(9, pay);
					check.setString(10, traffic);
					check.setString(11, date);
					check.setString(12, mes);
					check.setString(13, state);
					check.setString(14, person);
					check.setString(15, money);
					check.setString(16, tip);
					check.setString(17, riqi);
					check.setString(18, custom_id);
		
					check.executeUpdate();
					
					//给客户增加版本配置文件
					sql="select * from custom where id=?";
					check = con.prepareStatement(sql);
					check.setString(1, custom_id);
					rs = check.executeQuery();
					while (rs.next()) {
						String get_version = rs.getString("version");
						if(get_version!=null && get_version.length()>0){
							JSONObject get_v = JSONObject.fromObject(get_version);
							JSONObject v = JSONObject.fromObject(version);
							
							JSONArray get_v_arr=get_v.getJSONArray("version");
							JSONArray v_arr=v.getJSONArray("version");
							
							JSONArray tem_arr=new JSONArray();
							
							for(int n=0;n<v_arr.size();n++){
								JSONObject j=v_arr.getJSONObject(n);
								String n1_name=j.getString("module");
								String v1_num=j.getString("version_num");
								int pp=0;
								for(int p=0;p<get_v_arr.size();p++){
									JSONObject j1=get_v_arr.getJSONObject(p);
									String n2_name=j1.getString("module");
									//String v2_num=j1.getString("version_num");
									if(n1_name.equals(n2_name)){
										j1.put("version_num", v1_num);
										pp=1;
									}
								}
								if(pp==0){
									tem_arr.add(j);
								}
							}
							
							for(int cc=0;cc<tem_arr.size();cc++){
								get_v_arr.add(tem_arr.getJSONObject(cc));
							}
							
							JSONObject end_json=new JSONObject();
							end_json.put("version", get_v_arr);
							
							sql="update custom set version=? where id=?";
							check = con.prepareStatement(sql);
							check.setString(1, end_json.toString());
							check.setString(2, custom_id);
							check.executeUpdate();
							
						}else{
							sql="update custom set version=? where id=?";
							check = con.prepareStatement(sql);
							check.setString(1, version);
							check.setString(2, custom_id);
							check.executeUpdate();
						}
					}
					// 修改产品库存
					String[] get_pp = product.split("\\|");
					for (int i = 0; i < get_pp.length; i++) {
						String get_pp1[] = get_pp[i].split(",", -1);
						sql = "select * from product where sale_module=?";
						check = con.prepareStatement(sql);
						check.setString(1, get_pp1[0]);
						rs = check.executeQuery();
						while (rs.next()) {
							String count = rs.getString("count");
							if (count != null) {
								if(count.isEmpty()){
									count="0";
								}
								int a = Integer.parseInt(count);
								int b = Integer.parseInt(get_pp1[2]);
								
								int c = a - b;
								
								String l = "" + c;
								sql = "update product set count=? where sale_module=?";
								check = con.prepareStatement(sql);
								check.setString(1, l);
								check.setString(2, get_pp1[0]);
								check.executeUpdate();

							}
						}
					}

					boolean b1 = SendEmail(s1, s2, chaosong, content, product, id);
					boolean b2 = false;
					if (tax.endsWith("YES")) {

						String tax_person = jo.getString("tax_person");
						String tax_person1[] = tax_person.split("-");
						String get_taxperson = "";
						for (int i = 0; i < tax_person1.length; i++) {
							String tax_person2[] = tax_person1[i].split("&");
							get_taxperson = get_taxperson + tax_person2[1] + "-";
						}
						get_taxperson = get_taxperson.substring(0, get_taxperson.length() - 1);
						String s3[] = get_taxperson.split("-");

						b2 = Send_taxEmail(s1, s3, product, custom_name, id, money);
						if (b1) {
							if (b2) {
								result = "001";
							} else {
								result = "002";
							}
						} else {
							if (b2) {
								result = "003";
							} else {
								result = "004";
							}
						}

					} else {
						if (b1) {
							result = "001";
						} else {
							result = "005";
						}
					}

					if (result.equals("001")) {

					} else {
						con.rollback();
					}

					out.print(result);
					con.commit();

					// 启动线程发送邮件
					/*
					 * Thread_sendEmail myThread1 = new Thread_sendEmail(s1, s2,
					 * chaosong, content, product, id); myThread1.start();
					 * 
					 * if (tax.endsWith("YES")) {
					 * 
					 * String tax_person = jo.getString("tax_person"); String
					 * tax_person1[] = tax_person.split("-"); String
					 * get_taxperson = ""; for (int i = 0; i <
					 * tax_person1.length; i++) { String tax_person2[] =
					 * tax_person1[i].split("&"); get_taxperson = get_taxperson
					 * + tax_person2[1] + "-"; } get_taxperson =
					 * get_taxperson.substring(0, get_taxperson.length() - 1);
					 * String s3[] = get_taxperson.split("-");
					 * 
					 * Thread_send_taxEmail myThread2 = new
					 * Thread_send_taxEmail(s1, s3, product, custom_name, id,
					 * money); myThread2.start(); }
					 */

				} else if (active.equals("delete")) {
					log("进入删除订单");
					String dingdan_id = jo.getString("id");
					String sql = "";
					PreparedStatement check = null;
					ResultSet rs = null;
					// 撤销删除库存
					sql = "select * from dingdan where id=?";
					check = con.prepareStatement(sql);
					check.setString(1, dingdan_id);
					rs = check.executeQuery();
					while (rs.next()) {
						String product_tem = rs.getString("product");

						String[] get_pp = product_tem.split("\\|");
						for (int i = 0; i < get_pp.length; i++) {
							String get_pp1[] = get_pp[i].split(",", -1);
							sql = "select * from product where sale_module=?";
							check = con.prepareStatement(sql);
							check.setString(1, get_pp1[0]);
							rs = check.executeQuery();
							while (rs.next()) {
								String count = rs.getString("count");
								if (count != null) {
									if(count.isEmpty()){
										count="0";
									}
									int a = Integer.parseInt(count);
									int b = Integer.parseInt(get_pp1[2]);

									int c = a + b;
									String l = "" + c;
									sql = "update product set count=? where sale_module=?";
									check = con.prepareStatement(sql);
									check.setString(1, l);
									check.setString(2, get_pp1[0]);
									check.executeUpdate();

								}
							}
						}
					}

					sql = "delete from dingdan where id=?";
					check = con.prepareStatement(sql);
					check.setString(1, dingdan_id);
					int n = check.executeUpdate();
					if (n == 0) {
						result = "0";
					} else {
						result = "1";
					}
					con.commit();
					out.print(result);

				} else if (active.equals("modify")) {

				} else if (active.equals("query")) {
					JSONObject all_json_data = new JSONObject();
					JSONObject json_data = new JSONObject();
					JSONArray jsonarray_data = new JSONArray();

					String date1 = jo.getString("date1");
					String date2 = jo.getString("date2");
					date1 = date1.replace("-", "");
					date2 = date2.replace("-", "");

					String sql = "select * from dingdan where date>=? and date<=?";
					PreparedStatement check = null;
					check = con.prepareStatement(sql);
					check.setString(1, date1);
					check.setString(2, date2);
					ResultSet rs = check.executeQuery();
					while (rs.next()) {
						String id = rs.getString("id");
						String send_cor = rs.getString("send_cor");
						String send_person = rs.getString("send_person");
						String send_phone = rs.getString("send_phone");
						String custom_cor = rs.getString("custom_cor");
						String custom_addr = rs.getString("custom_addr");
						String custom_contacts = rs.getString("custom_contacts");
						String product = rs.getString("product");
						String pro_mes = "";
						String[] aa = product.split("\\|");
						for (int i = 0; i < aa.length; i++) {
							String[] bb = aa[i].split(",", -1);

							if (i < (aa.length - 1)) {
								pro_mes = pro_mes + bb[0] + "【" + bb[2] + "套" + bb[4] + "】+";
							} else {
								pro_mes = pro_mes + bb[0] + "【" + bb[2] + "套" + bb[4] + "】";
							}

						}
						String pay = rs.getString("pay");
						pay = pay.replace("请内部核实", "");
						String traffic = rs.getString("traffic");
						String date = rs.getString("date");
						String mes = rs.getString("mes");
						String state = rs.getString("state");
						String cdate = rs.getString("cdate");
						String all_money = rs.getString("all_money");

						json_data.put("id", id);
						json_data.put("send_cor", send_cor);
						json_data.put("send_person", send_person);
						json_data.put("send_phone", send_phone);
						json_data.put("custom_cor", custom_cor);
						json_data.put("custom_addr", custom_addr);
						json_data.put("custom_contacts", custom_contacts);
						json_data.put("product", pro_mes);
						json_data.put("pay", pay);
						json_data.put("traffic", traffic);
						json_data.put("date", date);
						json_data.put("mes", mes);
						json_data.put("state", state);
						json_data.put("cdate", cdate);
						json_data.put("all_money", all_money);

						jsonarray_data.add(json_data);
					}

					all_json_data.put("data", jsonarray_data);
					out.print(all_json_data);
				}
			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 服务器错误 返回-1
			out.print(result);
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Connection Start_database() {
		String SqlUrl = mes_data.DATABASE_URL;
		String username = mes_data.USER_NAME;
		String password = mes_data.PASSWORD;
		Connection con;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		try {
			con = DriverManager.getConnection(SqlUrl, username, password);
			return con;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// return null;
	}

	// 发送邮件
	public boolean SendEmail(String s1, String[] s2, String chaosong, String content, String products, String id) {
		String email_account = "";
		String email_password = "";
		String get_email = "";
		boolean b = false;
		Connection con = Start_database();
		try {

			if (con != null) {
				String sql = "select * from member where user_account=?";
				PreparedStatement check = con.prepareStatement(sql);
				check.setString(1, s1);
				ResultSet rs = check.executeQuery();
				while (rs.next()) {
					email_account = rs.getString("email_account");
					email_password = rs.getString("email_password");
				}
				for (int n = 0; n < s2.length; n++) {
					sql = "select * from member where user_account=?";
					check = con.prepareStatement(sql);
					check.setString(1, s2[n]);
					rs = check.executeQuery();
					while (rs.next()) {
						get_email = get_email + rs.getString("email_account") + "-";
					}
				}

				if (get_email.length() > 0) {
					get_email = get_email.substring(0, get_email.length() - 1);
				}
				log("get_email=" + get_email);
				String[] get_emails = get_email.split("-");
				EmailManager email = new EmailManager(host, email_account, email_password);

				String title = "";
				// products=products.replace("*", "+");
				String[] get_pp = products.split("\\|");
				log("products=" + products + "  length=" + get_pp.length);
				for (int i = 0; i < get_pp.length; i++) {
					String get_pp1[] = get_pp[i].split(",", -1);
					log("get_pp1=length=" + get_pp1.length);
					if (i < (get_pp.length - 1)) {
						title = title + get_pp1[0] + "【" + get_pp1[2] + "套】+";
					} else {
						title = title + get_pp1[0] + "【" + get_pp1[2] + "套】";
					}

				}
				title = title + "订单号【" + id + "】";
				log("title=" + title);

				if (chaosong.equals("YES")) {

					String get_rdEmail = "";
					String[] get_rdEmails = null;
					sql = "select * from default_member";
					check = con.prepareStatement(sql);
					rs = check.executeQuery();
					while (rs.next()) {
						get_rdEmail = get_rdEmail + rs.getString("email_account") + "-";
					}
					if (get_rdEmail.length() > 0) {
						get_rdEmail = get_rdEmail.substring(0, get_rdEmail.length() - 1);
						get_rdEmails = get_rdEmail.split("-");
					}
					log("get_rdEmail=" + get_rdEmail + " length=" + get_rdEmails.length);

					b = email.sendMail(email_account, get_emails, get_rdEmails, title, content, "");
					log("发送正常抄送邮件");
				} else {
					b = email.sendMail(email_account, get_emails, rd_empty, title, content, "");
					log("发送正常不带抄送邮件");
				}
				if (b) {

					log("发送正常邮件成功");
				} else {

					log("发送正常邮件失败");
				}

			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return b;

	}

	// 发送邮件
	public boolean Send_taxEmail(String s1, String[] s2, String products, String custom_name, String id, String money) {
		String email_account = "";
		String email_password = "";
		String get_email = "";
		boolean b = false;
		try {

			Connection con = Start_database();
			if (con != null) {
				String sql = "select * from member where user_account=?";
				PreparedStatement check = con.prepareStatement(sql);
				check.setString(1, s1);
				ResultSet rs = check.executeQuery();
				while (rs.next()) {
					email_account = rs.getString("email_account");
					email_password = rs.getString("email_password");
				}
				for (int n = 0; n < s2.length; n++) {
					sql = "select * from member where user_account=?";
					check = con.prepareStatement(sql);
					check.setString(1, s2[n]);
					rs = check.executeQuery();
					while (rs.next()) {
						get_email = get_email + rs.getString("email_account") + "-";
					}
				}

				// 查询开票信息
				String tax_mes = "";
				String content = "";
				sql = "select * from custom where custom_name=?";
				check = con.prepareStatement(sql);
				check.setString(1, custom_name);
				rs = check.executeQuery();
				while (rs.next()) {
					tax_mes = rs.getString("invoice_mes");
				}
				if (tax_mes.length() > 0) {
					String tt[] = tax_mes.split(",");
					if (tt.length == 7) {
						for (int i = 0; i < 7; i++) {
							switch (i) {
							case 0:
								content = content + "【名称】" + tt[i] + "<br>";
								break;
							case 1:
								content = content + "【税号】" + tt[i] + "<br>";
								break;
							case 2:
								content = content + "【地址】" + tt[i] + "<br>";
								break;
							case 3:
								content = content + "【开户银行】" + tt[i] + "<br>";
								break;
							case 4:
								content = content + "【账号】" + tt[i] + "<br>";
								break;
							case 5:
								content = content + "【电话】" + tt[i] + "<br>";
								break;
							case 6:
								content = content + "【传真】" + tt[i] + "<br>";
								break;
							}
						}
					}
				}
				con.close();
				// 生成开票信息单
				// 新建excel文件
				String file_name = "文施开票信息单-" + id + custom_name + ".xls";
				// 打开文件
				// String basepath =
				// request.getSession().getServletContext().getRealPath("/");
				File test = new File(file_name);

				try {
					WritableWorkbook book = Workbook.createWorkbook(test);
					// 生成名为“第一页”的工作表，参数0表示这是第一页
					WritableSheet sheet = book.createSheet(id, 0);
					sheet.mergeCells(0, 0, 7, 0);
					WritableCellFormat wc = new WritableCellFormat();
					wc.setAlignment(Alignment.CENTRE);
					Label labe0 = new Label(0, 0, "开具增值税发票信息单", wc);
					sheet.addCell(labe0);
					Label labe1 = new Label(0, 2, "客户", wc);
					Label labe2 = new Label(1, 2, "品名", wc);
					Label labe3 = new Label(2, 2, "规格型号", wc);
					Label labe4 = new Label(3, 2, "单位", wc);
					Label labe5 = new Label(4, 2, "数量", wc);
					Label labe6 = new Label(5, 2, "含税单价", wc);
					Label labe7 = new Label(6, 2, "含税金额", wc);
					Label labe8 = new Label(7, 2, "商品和服务税收分类与编码", wc);
					sheet.addCell(labe1);
					sheet.addCell(labe2);
					sheet.addCell(labe3);
					sheet.addCell(labe4);
					sheet.addCell(labe5);
					sheet.addCell(labe6);
					sheet.addCell(labe7);
					sheet.addCell(labe8);

					String pp[] = products.split("\\|");
					String labe3_mes = "";
					int n = 0;
					String nn = "";
					for (int i = 0; i < pp.length; i++) {
						String pp2[] = pp[i].split(",", -1);
						n = n + Integer.parseInt(pp2[2]);
						if (i == 0) {
							labe3_mes = pp2[0];
						}

					}
					nn = n + "";
					int m = Integer.parseInt(money) / n;
					String mm = "￥" + m + "";
					labe1 = new Label(0, 3, custom_name, wc);
					labe2 = new Label(1, 3, "无线模块", wc);
					labe3 = new Label(2, 3, labe3_mes, wc);
					labe4 = new Label(3, 3, "片", wc);
					labe5 = new Label(4, 3, nn, wc);
					labe6 = new Label(5, 3, mm, wc);
					labe7 = new Label(6, 3, "￥" + money, wc);
					labe8 = new Label(7, 3, "移动通信终端设备零件", wc);
					sheet.addCell(labe1);
					sheet.addCell(labe2);
					sheet.addCell(labe3);
					sheet.addCell(labe4);
					sheet.addCell(labe5);
					sheet.addCell(labe6);
					sheet.addCell(labe7);
					sheet.addCell(labe8);

					sheet.mergeCells(0, 10, 7, 10);
					labe1 = new Label(0, 10, "后面附上资料：1.客户订单（销售合同） 2.仓库出库单  3.签回的送货单", wc);
					sheet.addCell(labe1);

					labe1 = new Label(7, 11, "申请人  宋辉", wc);
					labe2 = new Label(7, 12, "", wc);
					sheet.addCell(labe1);
					sheet.addCell(labe2);

					// 写入数据并关闭文件
					book.write();
					book.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (get_email.length() > 0) {
					get_email = get_email.substring(0, get_email.length() - 1);
				}
				log("get_email_tax=" + get_email);
				String[] get_emails = get_email.split("-");
				EmailManager email = new EmailManager(host, email_account, email_password);

				b = email.sendMail(email_account, get_emails, rd_empty, custom_name + "开票申请", content,
						test.getAbsolutePath());

				if (b) {

					log("发送税单邮件成功");
				} else {

					log("发送税单邮件失败");
				}

			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return b;

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
			session.setDebug(false);
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
			session.setDebug(false);
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();
		}

		/**
		 * 发送邮件
		 */
		public boolean sendMail(String from, String[] to, String[] copyto, String subject, String content,
				String filename) {
			try {
				// 设置发信人
				mimeMsg.setFrom(new InternetAddress(from));
				// 设置接收人
				// log("to.length=" + to.length);
				for (int i = 0; i < to.length; i++) {
					// mimeMsg.setRecipients(Message.RecipientType.TO,
					// InternetAddress.parse(to[i]));
					mimeMsg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to[i]));
				}
				// 设置抄送人
				if (copyto != null) {
					for (int i = 0; i < copyto.length; i++) {
						mimeMsg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto[i]));
					}
				}

				// 设置主题
				mimeMsg.setSubject(subject);
				// 设置正文
				BodyPart bp = new MimeBodyPart();
				bp.setContent(content, "text/html;charset=utf-8");
				mp.addBodyPart(bp);
				// 设置附件
				if (filename.length() != 0) {
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
					if (copyto != null) {
						transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
					}

					transport.close();
				} else {
					Transport.send(mimeMsg);
				}
				// System.out.println("邮件发送成功");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}

	}

	class Thread_sendEmail extends Thread {
		public String s1;
		public String s2[];
		public String chaosong;
		public String content;
		public String products;
		public String id;

		public Thread_sendEmail(String s1, String s2[], String chaosong, String content, String products, String id) {
			this.s1 = s1;
			this.s2 = s2;
			this.chaosong = chaosong;
			this.content = content;
			this.products = products;
			this.id = id;
		}

		public void run() {
			SendEmail(s1, s2, chaosong, content, products, id);
		}
	}

	class Thread_send_taxEmail extends Thread {
		public String s1;
		public String s2[];
		public String products;
		public String custom_name;
		public String id;
		public String money;

		public Thread_send_taxEmail(String s1, String s2[], String products, String custom_name, String id,
				String money) {
			this.s1 = s1;
			this.s2 = s2;
			this.products = products;
			this.custom_name = custom_name;
			this.id = id;
			this.money = money;
		}

		public void run() {
			Send_taxEmail(s1, s2, products, custom_name, id, money);
		}
	}
}
