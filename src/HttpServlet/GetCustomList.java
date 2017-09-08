package HttpServlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Constent.mes_data;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class GetCustomList
 */
@WebServlet("/GetCustomList")
public class GetCustomList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetCustomList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log("进入生成客户表");
		// 进行用户验证
		HttpSession session = request.getSession();
		log("session" + session.getAttribute("account"));
		if (session.getAttribute("account") == null) {
			return;
		}

		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String riqi = dateFormat.format(now);

			// 新建excel文件
			String file_name = "xls/" + riqi + "客户表.xls";
			// 打开文件
			String basepath = request.getSession().getServletContext().getRealPath("/");
			File test = new File(basepath + file_name);
			WritableWorkbook book = Workbook.createWorkbook(test);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("客户信息", 0);
			sheet.setColumnView(0, 40);
			sheet.setColumnView(1, 80);
			sheet.setColumnView(2, 40);
			sheet.setColumnView(3, 20);
			sheet.setColumnView(4, 60);

			Label label = new Label(0, 0, "客户名称");
			Label labe2 = new Label(1, 0, "客户地址");
			Label labe3 = new Label(2, 0, "联系人");
			Label labe4 = new Label(3, 0, "备注");
			Label labe5 = new Label(4, 0, "版本配置信息");

			sheet.addCell(label);
			sheet.addCell(labe2);
			sheet.addCell(labe3);
			sheet.addCell(labe4);
			sheet.addCell(labe5);

			int r = 1;

			Connection con = Start_database();
			String sql = "select * from custom";
			PreparedStatement check = null;
			check = con.prepareStatement(sql);
			ResultSet rs = check.executeQuery();
			while (rs.next()) {
				String c_name = rs.getString("custom_name");
				String addr = rs.getString("addr");
				String contacts = rs.getString("contacts");
				String mes = rs.getString("mes");
				String version = rs.getString("version");

				if (mes.equals("NULL")) {
					mes = "";
				}

				if (version == null) {
					version = "";
				}

				label = new Label(0, r, c_name);
				labe2 = new Label(1, r, addr);
				labe3 = new Label(2, r, contacts);
				labe4 = new Label(3, r, mes);
				labe5 = new Label(4, r, version);

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labe4);
				sheet.addCell(labe5);

				r = r + 1;
			}

			con.close();
			// 写入数据并关闭文件
			book.write();
			book.close();
			JSONObject json_data = new JSONObject();
			json_data.put("file_name", file_name);
			out.print(json_data);
		} catch (Exception e) {
			out.print("-1");
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
