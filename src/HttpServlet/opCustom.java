package HttpServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Constent.mes_data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class opCustom
 */
@WebServlet("/opCustom")
public class opCustom extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public opCustom() {
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

		log("进入查询成员表");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject all_json_data = new JSONObject();
		JSONObject json_data = new JSONObject();
		JSONArray jsonarray_data = new JSONArray();

		String get_id = request.getParameter("id");

		// 打开数据库查询数据
		try {
			Connection con = Start_database();
			if (con != null) {

				String sql = "select * from custom";
				PreparedStatement check = null;
				ResultSet rs = null;

				if (get_id != null) {
					sql = "select * from custom where id=?";
					check = con.prepareStatement(sql);
					check.setString(1, get_id);
					rs = check.executeQuery();
				} else {
					check = con.prepareStatement(sql);
					rs = check.executeQuery();
				}

				while (rs.next()) {
					String id = rs.getString("id");
					String custom_name = rs.getString("custom_name");
					String addr = rs.getString("addr");
					String contacts = rs.getString("contacts");
					String mes = rs.getString("mes");
					String tax = rs.getString("invoice_mes");
					String version = rs.getString("version");
					
					if(version==null){
						version="";
					}

					json_data.put("id", id);
					json_data.put("custom_name", custom_name);
					json_data.put("addr", addr);
					json_data.put("contacts", contacts);
					json_data.put("mes", mes);
					json_data.put("tax", tax);
					json_data.put("version", version);

					jsonarray_data.add(json_data);
				}
				con.close();
				all_json_data.put("data", jsonarray_data);
				out.print(all_json_data);
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
		// 进行用户验证
		HttpSession session = request.getSession();
		log("session" + session.getAttribute("account"));
		if (session.getAttribute("account") == null) {
			return;
		}

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String acceptjson = "";
		String result = "-1";
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
				JSONObject jo = JSONObject.fromObject(acceptjson);
				String active = jo.getString("active");
				if (active.equals("insert")) {
					log("进入添加客户");
					String custom_name = jo.getString("custom_name");
					String custom_addr = jo.getString("custom_addr");
					String custom_mes = jo.getString("custom_mes");
					String custom_contacts = jo.getString("custom_contacts");
					String tax = jo.getString("tax");

					String s = UUID.randomUUID().toString();
					String id = s.substring(0, 8);
					// 打开数据库查询数据
					Connection con = Start_database();
					if (con != null) {
						String sql = "select * from custom";
						PreparedStatement check = con.prepareStatement(sql);
						ResultSet rs = check.executeQuery();
						int n = 0;
						while (rs.next()) {
							String name = rs.getString("custom_name");
							if (custom_name.equals(name)) {
								n = 1;
								break;
							}
						}
						if (n == 1) {
							// 用户名已经存在 返回0
							result = "0";
						} else if (n == 0) {
							sql = "insert into custom value(?,?,?,?,?,?,?)";
							check = con.prepareStatement(sql);
							check.setString(1, custom_name);
							check.setString(2, custom_addr);
							check.setString(3, custom_contacts);
							check.setString(4, custom_mes);
							check.setString(5, tax);
							check.setString(6, id);
							check.setString(7, "");
							check.executeUpdate();
							// 用户插入成功 返回1
							result = "1";
						}
						con.close();
						out.print(result);
					} else {
						// 服务器错误返回 -1
						out.print(result);
					}
				} else if (active.equals("delete")) {
					log("进入删除客户");
					String custom_id = jo.getString("custom_id");
					Connection con = Start_database();
					if (con != null) {
						String sql = "delete from custom where id=?";
						PreparedStatement check = con.prepareStatement(sql);
						check.setString(1, custom_id);
						int n = check.executeUpdate();
						if (n == 0) {
							result = "0";
						} else {
							result = "1";
						}
						con.close();
						out.print(result);
					} else {
						// 服务器错误返回 -1
						log("服务器错误返回 -1");
						out.print(result);
					}
				} else if (active.equals("modify")) {
					log("进入修改客户");
					String custom_name = jo.getString("custom_name");
					String custom_id = jo.getString("custom_id");
					String custom_addr = jo.getString("custom_addr");
					String custom_mes = jo.getString("custom_mes");
					String custom_contacts = jo.getString("custom_contacts");
					String tax = jo.getString("tax");
					Connection con = Start_database();
					if (con != null) {

						String sql = "update custom set addr=?,contacts=?,mes=?,invoice_mes=?,custom_name=? where id=?";
						PreparedStatement check = con.prepareStatement(sql);
						check.setString(1, custom_addr);
						check.setString(2, custom_contacts);
						check.setString(3, custom_mes);
						check.setString(4, tax);
						check.setString(5, custom_name);
						check.setString(6, custom_id);
						check.executeUpdate();

						con.close();
						result="1";
						out.print(result);
					} else {
						// 服务器错误返回 -1
						log("服务器错误返回 -1");
						out.print(result);
					}
				}
			}
		} catch (Exception e) {
			// 服务器错误 返回-1
			out.print(result);
			e.printStackTrace();
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
}
