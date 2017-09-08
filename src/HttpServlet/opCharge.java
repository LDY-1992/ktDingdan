package HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Constent.mes_data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class opCharge
 */
@WebServlet("/opCharge")
public class opCharge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public opCharge() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
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
						String sql = "select * from member where role!='tax'";
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
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
