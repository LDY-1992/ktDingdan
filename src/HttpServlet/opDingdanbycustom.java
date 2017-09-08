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
 * Servlet implementation class opDingdanbycustom
 */
@WebServlet("/opDingdanbycustom")
public class opDingdanbycustom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public opDingdanbycustom() {
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
				log("进入查询订单表");
				response.setContentType("text/javascript;charset=UTF-8");
				PrintWriter out = response.getWriter();
				JSONObject all_json_data = new JSONObject();
				JSONObject json_data = new JSONObject();
				JSONArray jsonarray_data = new JSONArray();
				String custom_id = request.getParameter("custom_id");
				// 打开数据库查询数据
				try {
					Connection con = Start_database();
					if (con != null) {
						if (custom_id != null) {
							ResultSet rs;
							String sql = "select * from dingdan where custom_id=? ORDER BY id DESC LIMIT 3";
							PreparedStatement check = con.prepareStatement(sql);
							check.setString(1, custom_id);
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
							out.print("-1");
						}
					} else {
						out.print("-1");
					}
				} catch (Exception e) {
					out.print("-1");
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
