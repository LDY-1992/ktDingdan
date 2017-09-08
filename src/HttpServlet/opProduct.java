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
 * Servlet implementation class opProduct
 */
@WebServlet("/opProduct")
public class opProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public opProduct() {
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
		// 进行用户验证
		HttpSession session = request.getSession();
		log("session" + session.getAttribute("account"));
		if (session.getAttribute("account") == null) {
			return;
		}

		log("进入查询产品表");
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
				if (req_id == null) {
					String sql = "select * from product";
					PreparedStatement check = null;
					ResultSet rs = null;

					check = con.prepareStatement(sql);
					rs = check.executeQuery();

					while (rs.next()) {
						String module_id = rs.getString("id");
						String sale_module = rs.getString("sale_module");
						String inner_module = rs.getString("inner_module");
						String count = rs.getString("count");
						
						if(count==null){
							count="0";
						}
						json_data.put("module_id", module_id);
						json_data.put("sale_module", sale_module);
						json_data.put("inner_module", inner_module);
						json_data.put("count", count);

						jsonarray_data.add(json_data);
					}
					con.close();
					all_json_data.put("data", jsonarray_data);
					out.print(all_json_data);
				}else{
					String sql = "select * from product where sale_module=?";
					PreparedStatement check = null;
					ResultSet rs = null;

					check = con.prepareStatement(sql);
					check.setString(1, req_id);
					rs = check.executeQuery();
					String version="0";
					while (rs.next()) {
						version = rs.getString("version");
					}
					con.close();
					if(version==null){
						version="0";
					}
					out.print(version);
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
		// TODO Auto-generated method stub
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
					log("进入添加产品");
					String sale_module = jo.getString("sale_module");
					String inner_module = jo.getString("inner_module");
					String count = jo.getString("count");
					String version=jo.getString("version");
					String s = UUID.randomUUID().toString();
					String id = s.substring(0, 8);
					// 打开数据库查询数据
					Connection con = Start_database();
					if (con != null) {
						String sql = "select * from product";
						PreparedStatement check = con.prepareStatement(sql);
						ResultSet rs = check.executeQuery();
						int n = 0;
						while (rs.next()) {
							String sale = rs.getString("sale_module");
							if (sale_module.equals(sale)) {
								n = 1;
								break;
							}
						}
						if (n == 1) {
							// 产品销售型号已经存在 返回0
							result = "0";
						} else if (n == 0) {
							sql = "insert into product value(?,?,?,?,?)";
							check = con.prepareStatement(sql);
							check.setString(1, id);
							check.setString(2, sale_module);
							check.setString(3, inner_module);
							check.setString(4, count);
							check.setString(5, version);
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
					log("进入删除产品");
					String custom_id = jo.getString("module_id");
					Connection con = Start_database();
					if (con != null) {
						String sql = "delete from product where id=?";
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
					log("进入修改产品");
					String sale_module = jo.getString("sale_module");
					String inner_module = jo.getString("inner_module");
					String module_id = jo.getString("module_id");
					String count = jo.getString("count");
					String version = jo.getString("version");
					Connection con = Start_database();
					if (con != null) {
						String sql = "";
						PreparedStatement check = null;
						ResultSet rs = null;

						sql = "update product set sale_module=?,inner_module=?,count=?,version=? where id=?";
						check = con.prepareStatement(sql);
						check.setString(1, sale_module);
						check.setString(2, inner_module);
						check.setString(3, count);
						check.setString(4, version);
						check.setString(5, module_id);

						int n=check.executeUpdate();
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
