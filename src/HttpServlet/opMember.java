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
import javax.servlet.http.HttpSession;

//常量
import Constent.mes_data;
//处理json
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class opMember
 */
@WebServlet("/opMember")
public class opMember extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public opMember() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
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

		// 打开数据库查询数据
		try {
			Connection con = Start_database();
			if (con != null) {
				String sql = "select * from member";
				PreparedStatement check = con.prepareStatement(sql);
				ResultSet rs = check.executeQuery();
				while (rs.next()) {
					String user_account = rs.getString("user_account");
					String user_password = rs.getString("user_password");
					String role = rs.getString("role");
					String email_account = rs.getString("email_account");
					String email_password = rs.getString("email_password");
					String user_name = rs.getString("user_name");
					String belong = rs.getString("belong");
					json_data.put("user_account", user_account);
					json_data.put("user_password", user_password);
					json_data.put("role", role);
					json_data.put("email_account", email_account);
					json_data.put("email_password", email_password);
					json_data.put("user_name", user_name);
					json_data.put("belong", belong);
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
		// TODO Auto-generated method stub
		// doGet(request, response);
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
					log("进入添加成员");
					String name = jo.getString("user_name");
					String account = jo.getString("user_account");
					String password = jo.getString("user_password");
					String email_account = jo.getString("email_account");
					String email_password = jo.getString("email_password");
					String role = jo.getString("role");
					if (role.equals("0")) {
						role = "middle";
					} else if (role.equals("1")) {
						role = "top";
					} else if (role.equals("2")) {
						role = "tax";
					}
					String belong = "NULL";
					// 打开数据库查询数据
					Connection con = Start_database();
					if (con != null) {
						String sql = "select * from member for update";
						PreparedStatement check = con.prepareStatement(sql);
						ResultSet rs = check.executeQuery();
						int n = 0;
						while (rs.next()) {
							String user_account = rs.getString("user_account");
							if (account.equals(user_account)) {
								n = 1;
								break;
							}
						}
						if (n == 1) {
							// 用户名已经存在 返回0
							result = "0";
						} else if (n == 0) {
							sql = "insert into member value(?,?,?,?,?,?,?)";
							check = con.prepareStatement(sql);
							check.setString(1, account);
							check.setString(2, password);
							check.setString(3, role);
							check.setString(4, email_account);
							check.setString(5, email_password);
							check.setString(6, name);
							check.setString(7, belong);
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
					log("进入删除成员");
					String account = jo.getString("user_account");
					Connection con = Start_database();
					if (con != null) {
						String sql = "delete from member where user_account=?";
						PreparedStatement check = con.prepareStatement(sql);
						check.setString(1, account);
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
					log("进入修改成员");
					String name = jo.getString("user_name");
					String account = jo.getString("user_account");
					String password = jo.getString("user_password");
					String email_account = jo.getString("email_account");
					String email_password = jo.getString("email_password");
					String role = jo.getString("role");
					if (role.equals("0")) {
						role = "middle";
					} else if (role.equals("1")) {
						role = "top";
					}else if (role.equals("2")) {
						role = "tax";
					}
					String belong = "NULL";
					Connection con = Start_database();
					if (con != null) {
						String sql = "update member set user_password=?,role=?,email_account=?,email_password=?,user_name=?,belong=? where user_account=?";
						PreparedStatement check = con.prepareStatement(sql);
						check.setString(1, password);
						check.setString(2, role);
						check.setString(3, email_account);
						check.setString(4, email_password);
						check.setString(5, name);
						check.setString(6, belong);
						check.setString(7, account);
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
