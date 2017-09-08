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
//常量
import Constent.mes_data;
//处理json
import net.sf.json.JSONObject;
//SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public login() {
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
		log("进入登录验证");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String acceptjson = "";
		String result="-1";
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
				String account = jo.getString("account");
				String password = jo.getString("password");
				// 打开数据库查询数据
				Connection con = Start_database();
				if (con != null) {
					String sql = "select * from member where role!='tax'";
					PreparedStatement check = con.prepareStatement(sql);
					ResultSet rs = check.executeQuery();
					while (rs.next()) {
						String user_name = rs.getString("user_account");
						String user_password = rs.getString("user_password");
						if (account.equals(user_name)) {
							if (password.equals(user_password)) {
								// 用户名和密码正确
								request.getSession().setAttribute("account", account);
								request.getSession().setMaxInactiveInterval(12*60*60);
								String role = rs.getString("role");
                                result=role;
                                break;
							} else {
								// 用户名正确，密码错误
								result="1";
								break;
							}
						}
					}
					if(result.equals("-1")){
						result="0";
					}
					con.close();
					out.print(result);
				}else{
					out.print(result);
				}

			}
		} catch (Exception e) {
			out.print(result);
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
