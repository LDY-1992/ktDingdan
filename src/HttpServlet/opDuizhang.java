package HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

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

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Servlet implementation class opDuizhang
 */
@WebServlet("/opDuizhang")
public class opDuizhang extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String sheet1_title = "宋辉挂账不开票";
	private static String sheet2_title = "宋辉挂账开票";
	private static String sheet3_title = "客户私账漆伟辉";
	private static String sheet4_title = "客户公账开票";
	private static String sheet5_title = "代发有人科技月结";
	private static String sheet6_title = "客户私账徐总";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public opDuizhang() {
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

		log("进入对账查询");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject all_json_data = new JSONObject();
		JSONObject json_data = new JSONObject();
		JSONArray jsonarray_data = new JSONArray();
		JSONArray jsonarray_sheet1 = new JSONArray();
		JSONArray jsonarray_sheet2 = new JSONArray();
		JSONArray jsonarray_sheet3 = new JSONArray();
		JSONArray jsonarray_sheet4 = new JSONArray();
		JSONArray jsonarray_sheet5 = new JSONArray();
		JSONArray jsonarray_sheet6 = new JSONArray();

		String date1 = request.getParameter("date1");
		String date2 = request.getParameter("date2");
		date1 = date1.replace("-", "");
		date2 = date2.replace("-", "");

		// 打开数据库查询数据
		try {

			// 新建excel文件
			String file_name = "xls/" + date1 + "-" + date2 + "对账表.xls";
			// 打开文件
			String basepath = request.getSession().getServletContext().getRealPath("/");
			File test = new File(basepath + file_name);
			WritableWorkbook book = Workbook.createWorkbook(test);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("全部订单", 0);
			WritableSheet sheet1 = book.createSheet(sheet1_title, 1);
			WritableSheet sheet2 = book.createSheet(sheet2_title, 2);
			WritableSheet sheet3 = book.createSheet(sheet3_title, 3);
			WritableSheet sheet4 = book.createSheet(sheet4_title, 4);
			WritableSheet sheet5 = book.createSheet(sheet5_title, 5);
			WritableSheet sheet6 = book.createSheet(sheet6_title, 6);
			
			sheet.setColumnView(0, 40);
			sheet.setColumnView(1, 15);
			sheet.setColumnView(2, 50);
			sheet.setColumnView(3, 15);
			
			sheet1.setColumnView(0, 40);
			sheet1.setColumnView(1, 15);
			sheet1.setColumnView(2, 50);
			sheet1.setColumnView(3, 15);
			
			sheet2.setColumnView(0, 40);
			sheet2.setColumnView(1, 15);
			sheet2.setColumnView(2, 50);
			sheet2.setColumnView(3, 15);
			
			sheet3.setColumnView(0, 40);
			sheet3.setColumnView(1, 15);
			sheet3.setColumnView(2, 50);
			sheet3.setColumnView(3, 15);
			
			sheet4.setColumnView(0, 40);
			sheet4.setColumnView(1, 15);
			sheet4.setColumnView(2, 50);
			sheet4.setColumnView(3, 15);
			
			sheet5.setColumnView(0, 40);
			sheet5.setColumnView(1, 15);
			sheet5.setColumnView(2, 50);
			sheet5.setColumnView(3, 15);
			
			sheet6.setColumnView(0, 40);
			sheet6.setColumnView(1, 15);
			sheet6.setColumnView(2, 50);
			sheet6.setColumnView(3, 15);

			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
			// 以及单元格内容为test
			// Label label = new Label(0, 0, " test ");
			// 将定义好的单元格添加到工作表中
			// sheet.addCell(label);
			
			NumberFormat nf = new NumberFormat("#.##");    //设置数字格式
			WritableCellFormat wcfN = new WritableCellFormat(nf); //设置表单格式    
			Number labelNF = new Number(0, 0, 1.00,wcfN);
			
			Connection con = Start_database();
			if (con != null) {

				String sql = "select * from dingdan where date>=? and date<=? ORDER BY date";
				PreparedStatement check = null;
				check = con.prepareStatement(sql);
				check.setString(1, date1);
				check.setString(2, date2);
				ResultSet rs = check.executeQuery();
				double n = 0;
				double n1 = 0;
				double n2 = 0;
				double n3 = 0;
				double n4 = 0;
				double n5 = 0;
				double n6 = 0;

				int r = 1;
				int r1 = 1;
				int r2 = 1;
				int r3 = 1;
				int r4 = 1;
				int r5 = 1;
				int r6 = 1;

				Label label = new Label(0, 0, "客户名称");
				Label labe2 = new Label(1, 0, "发货日期");
				Label labe3 = new Label(2, 0, "产品清单");
				Label labe4 = new Label(3, 0, "总金额");

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labe4);

				label = new Label(0, 0, "客户名称");
				labe2 = new Label(1, 0, "发货日期");
				labe3 = new Label(2, 0, "产品清单");
				labe4 = new Label(3, 0, "总金额");

				sheet1.addCell(label);
				sheet1.addCell(labe2);
				sheet1.addCell(labe3);
				sheet1.addCell(labe4);

				label = new Label(0, 0, "客户名称");
				labe2 = new Label(1, 0, "发货日期");
				labe3 = new Label(2, 0, "产品清单");
				labe4 = new Label(3, 0, "总金额");

				sheet2.addCell(label);
				sheet2.addCell(labe2);
				sheet2.addCell(labe3);
				sheet2.addCell(labe4);

				label = new Label(0, 0, "客户名称");
				labe2 = new Label(1, 0, "发货日期");
				labe3 = new Label(2, 0, "产品清单");
				labe4 = new Label(3, 0, "总金额");

				sheet3.addCell(label);
				sheet3.addCell(labe2);
				sheet3.addCell(labe3);
				sheet3.addCell(labe4);

				label = new Label(0, 0, "客户名称");
				labe2 = new Label(1, 0, "发货日期");
				labe3 = new Label(2, 0, "产品清单");
				labe4 = new Label(3, 0, "总金额");

				sheet4.addCell(label);
				sheet4.addCell(labe2);
				sheet4.addCell(labe3);
				sheet4.addCell(labe4);

				label = new Label(0, 0, "客户名称");
				labe2 = new Label(1, 0, "发货日期");
				labe3 = new Label(2, 0, "产品清单");
				labe4 = new Label(3, 0, "总金额");

				sheet5.addCell(label);
				sheet5.addCell(labe2);
				sheet5.addCell(labe3);
				sheet5.addCell(labe4);

				label = new Label(0, 0, "客户名称");
				labe2 = new Label(1, 0, "发货日期");
				labe3 = new Label(2, 0, "产品清单");
				labe4 = new Label(3, 0, "总金额");

				sheet6.addCell(label);
				sheet6.addCell(labe2);
				sheet6.addCell(labe3);
				sheet6.addCell(labe4);

				while (rs.next()) {
					String id = rs.getString("id");

					String product = rs.getString("product");
					String pro_mes = "";
					String[] aa = product.split("\\|");
					for (int i = 0; i < aa.length; i++) {
						String[] bb = aa[i].split(",",-1);
						if (i < (aa.length - 1)) {
							pro_mes = pro_mes + bb[0] + "【" + bb[2] + "套"+bb[4]+"】+";
						} else {
							pro_mes = pro_mes + bb[0] + "【" + bb[2] + "套"+bb[4]+"】";
						}

					}

					String cdate = rs.getString("date");
					String custom_cor = rs.getString("custom_cor");
					String all_money = rs.getString("all_money");
					String pay = rs.getString("pay");
					pay=pay.replace("请内部核实", "");
                    
					if(!pay.equals(sheet5_title)){
						
						n = n + Double.parseDouble(all_money);
						json_data.put("products", pro_mes);
						json_data.put("cdate", cdate);
						json_data.put("custom_cor", custom_cor);
						json_data.put("all_money", all_money);
						jsonarray_data.add(json_data);
						
						label = new Label(0, r, custom_cor);
						labe2 = new Label(1, r, cdate);
						labe3 = new Label(2, r, pro_mes);
						labe4 = new Label(3, r, all_money);
						labelNF = new Number(3, r, Double.parseDouble(all_money));
						
						r = r + 1;

						sheet.addCell(label);
						sheet.addCell(labe2);
						sheet.addCell(labe3);
						sheet.addCell(labelNF);
					}

					if (pay.equals(sheet1_title)) {
						n1 = n1 + Double.parseDouble(all_money);
						json_data.put("products", pro_mes);
						json_data.put("cdate", cdate);
						json_data.put("custom_cor", custom_cor);
						json_data.put("all_money", all_money);
						jsonarray_sheet1.add(json_data);

						label = new Label(0, r1, custom_cor);
						labe2 = new Label(1, r1, cdate);
						labe3 = new Label(2, r1, pro_mes);
						labe4 = new Label(3, r1, all_money);
						labelNF = new Number(3, r1, Double.parseDouble(all_money));
						r1 = r1 + 1;

						sheet1.addCell(label);
						sheet1.addCell(labe2);
						sheet1.addCell(labe3);
						sheet1.addCell(labelNF);
					} else if (pay.equals(sheet2_title)) {
						n2 = n2 + Double.parseDouble(all_money);
						json_data.put("products", pro_mes);
						json_data.put("cdate", cdate);
						json_data.put("custom_cor", custom_cor);
						json_data.put("all_money", all_money);
						jsonarray_sheet2.add(json_data);

						label = new Label(0, r2, custom_cor);
						labe2 = new Label(1, r2, cdate);
						labe3 = new Label(2, r2, pro_mes);
						labe4 = new Label(3, r2, all_money);
						labelNF = new Number(3, r2, Double.parseDouble(all_money));
						r2 = r2 + 1;

						sheet2.addCell(label);
						sheet2.addCell(labe2);
						sheet2.addCell(labe3);
						sheet2.addCell(labelNF);
					} else if (pay.equals(sheet3_title)) {
						n3 = n3 + Double.parseDouble(all_money);
						json_data.put("products", pro_mes);
						json_data.put("cdate", cdate);
						json_data.put("custom_cor", custom_cor);
						json_data.put("all_money", all_money);
						jsonarray_sheet3.add(json_data);

						label = new Label(0, r3, custom_cor);
						labe2 = new Label(1, r3, cdate);
						labe3 = new Label(2, r3, pro_mes);
						labe4 = new Label(3, r3, all_money);
						labelNF = new Number(3, r3, Double.parseDouble(all_money));
						r3 = r3 + 1;

						sheet3.addCell(label);
						sheet3.addCell(labe2);
						sheet3.addCell(labe3);
						sheet3.addCell(labelNF);
					} else if (pay.equals(sheet4_title)) {
						n4 = n4 + Double.parseDouble(all_money);
						json_data.put("products", pro_mes);
						json_data.put("cdate", cdate);
						json_data.put("custom_cor", custom_cor);
						json_data.put("all_money", all_money);
						jsonarray_sheet4.add(json_data);

						label = new Label(0, r4, custom_cor);
						labe2 = new Label(1, r4, cdate);
						labe3 = new Label(2, r4, pro_mes);
						labe4 = new Label(3, r4, all_money);
						labelNF = new Number(3, r4, Double.parseDouble(all_money));
						r4 = r4 + 1;

						sheet4.addCell(label);
						sheet4.addCell(labe2);
						sheet4.addCell(labe3);
						sheet4.addCell(labelNF);
					} else if (pay.equals(sheet5_title)) {
						n5 = n5 + Double.parseDouble(all_money);
						json_data.put("products", pro_mes);
						json_data.put("cdate", cdate);
						json_data.put("custom_cor", custom_cor);
						json_data.put("all_money", all_money);
						jsonarray_sheet5.add(json_data);

						label = new Label(0, r5, custom_cor);
						labe2 = new Label(1, r5, cdate);
						labe3 = new Label(2, r5, pro_mes);
						labe4 = new Label(3, r5, all_money);
						labelNF = new Number(3, r5, Double.parseDouble(all_money));
						r5 = r5 + 1;

						sheet5.addCell(label);
						sheet5.addCell(labe2);
						sheet5.addCell(labe3);
						sheet5.addCell(labelNF);
					} else if (pay.equals(sheet6_title)) {
						n6 = n6 + Double.parseDouble(all_money);
						json_data.put("products", pro_mes);
						json_data.put("cdate", cdate);
						json_data.put("custom_cor", custom_cor);
						json_data.put("all_money", all_money);
						jsonarray_sheet6.add(json_data);

						label = new Label(0, r6, custom_cor);
						labe2 = new Label(1, r6, cdate);
						labe3 = new Label(2, r6, pro_mes);
						labe4 = new Label(3, r6, all_money);
						labelNF = new Number(3, r6, Double.parseDouble(all_money));
						r6 = r6 + 1;

						sheet6.addCell(label);
						sheet6.addCell(labe2);
						sheet6.addCell(labe3);
						sheet6.addCell(labelNF);
					}
				}
				con.close();

				label = new Label(0, r + 10, "");
				labe2 = new Label(1, r + 10, "");
				labe3 = new Label(2, r + 10, "总计");
				labe4 = new Label(3, r + 10, String.valueOf(n));
				labelNF = new Number(3, r+10, n);

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labelNF);

				label = new Label(0, r + 4, "");
				labe2 = new Label(1, r + 4, "");
				labe3 = new Label(2, r + 4, "宋辉挂账不开票");
				labe4 = new Label(3, r + 4, String.valueOf(n1));
				labelNF = new Number(3, r+4, n1);

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labelNF);

				label = new Label(0, r + 5, "");
				labe2 = new Label(1, r + 5, "");
				labe3 = new Label(2, r + 5, "宋辉挂账开票");
				labe4 = new Label(3, r + 5, String.valueOf(n2));
				labelNF = new Number(3, r+5, n2);

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labelNF);

				label = new Label(0, r + 6, "");
				labe2 = new Label(1, r + 6, "");
				labe3 = new Label(2, r + 6, "客户私账漆伟辉请内部核实");
				labe4 = new Label(3, r + 6, String.valueOf(n3));
				labelNF = new Number(3, r+6, n3);

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labelNF);

				label = new Label(0, r + 7, "");
				labe2 = new Label(1, r + 7, "");
				labe3 = new Label(2, r + 7, "客户公账开票请内部核实");
				labe4 = new Label(3, r + 7, String.valueOf(n4));
				labelNF = new Number(3, r+7, n4);

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labelNF);

				/*
				label = new Label(0, r + 8, "");
				labe2 = new Label(1, r + 8, "");
				labe3 = new Label(2, r + 8, "代发有人科技月结");
				labe4 = new Label(3, r + 8, String.valueOf(n5));
				labelNF = new Number(3, r+8, n5);

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labelNF);
				*/
				
				label = new Label(0, r + 8, "");
				labe2 = new Label(1, r + 8, "");
				labe3 = new Label(2, r + 8, "客户私帐徐总");
				labe4 = new Label(3, r + 8, String.valueOf(n6));
				labelNF = new Number(3, r+8, n6);

				sheet.addCell(label);
				sheet.addCell(labe2);
				sheet.addCell(labe3);
				sheet.addCell(labelNF);

				label = new Label(0, r1 + 3, "");
				labe2 = new Label(1, r1 + 3, "");
				labe3 = new Label(2, r1 + 3, "总计");
				labe4 = new Label(3, r1 + 3, String.valueOf(n1));
				labelNF = new Number(3, r1+3, n1);

				sheet1.addCell(label);
				sheet1.addCell(labe2);
				sheet1.addCell(labe3);
				sheet1.addCell(labelNF);

				label = new Label(0, r2 + 3, "");
				labe2 = new Label(1, r2 + 3, "");
				labe3 = new Label(2, r2 + 3, "总计");
				labe4 = new Label(3, r2 + 3, String.valueOf(n2));
				labelNF = new Number(3, r2+3, n2);

				sheet2.addCell(label);
				sheet2.addCell(labe2);
				sheet2.addCell(labe3);
				sheet2.addCell(labelNF);

				label = new Label(0, r3 + 3, "");
				labe2 = new Label(1, r3 + 3, "");
				labe3 = new Label(2, r3 + 3, "总计");
				labe4 = new Label(3, r3 + 3, String.valueOf(n3));
				labelNF = new Number(3, r3+3, n3);

				sheet3.addCell(label);
				sheet3.addCell(labe2);
				sheet3.addCell(labe3);
				sheet3.addCell(labelNF);

				label = new Label(0, r4 + 3, "");
				labe2 = new Label(1, r4 + 3, "");
				labe3 = new Label(2, r4 + 3, "总计");
				labe4 = new Label(3, r4 + 3, String.valueOf(n4));
				labelNF = new Number(3, r4+3, n4);

				sheet4.addCell(label);
				sheet4.addCell(labe2);
				sheet4.addCell(labe3);
				sheet4.addCell(labelNF);

				label = new Label(0, r5 + 3, "");
				labe2 = new Label(1, r5 + 3, "");
				labe3 = new Label(2, r5 + 3, "总计");
				labe4 = new Label(3, r5 + 3, String.valueOf(n5));
				labelNF = new Number(3, r5+3, n5);

				sheet5.addCell(label);
				sheet5.addCell(labe2);
				sheet5.addCell(labe3);
				sheet5.addCell(labelNF);
				
				label = new Label(0, r6 + 3, "");
				labe2 = new Label(1, r6 + 3, "");
				labe3 = new Label(2, r6 + 3, "总计");
				labe4 = new Label(3, r6 + 3, String.valueOf(n6));
				labelNF = new Number(3, r6+3, n6);

				sheet6.addCell(label);
				sheet6.addCell(labe2);
				sheet6.addCell(labe3);
				sheet6.addCell(labelNF);

				json_data.put("custom_cor", "");
				json_data.put("cdate", "");
				json_data.put("products", "总金额");
				json_data.put("all_money", String.valueOf(n));
				jsonarray_data.add(json_data);

				json_data.put("custom_cor", "");
				json_data.put("cdate", "");
				json_data.put("products", "总金额");
				json_data.put("all_money", String.valueOf(n1));
				jsonarray_sheet1.add(json_data);

				json_data.put("custom_cor", "");
				json_data.put("cdate", "");
				json_data.put("products", "总金额");
				json_data.put("all_money", String.valueOf(n2));
				jsonarray_sheet2.add(json_data);

				json_data.put("custom_cor", "");
				json_data.put("cdate", "");
				json_data.put("products", "总金额");
				json_data.put("all_money", String.valueOf(n3));
				jsonarray_sheet3.add(json_data);

				json_data.put("custom_cor", "");
				json_data.put("cdate", "");
				json_data.put("products", "总金额");
				json_data.put("all_money", String.valueOf(n4));
				jsonarray_sheet4.add(json_data);

				json_data.put("custom_cor", "");
				json_data.put("cdate", "");
				json_data.put("products", "总金额");
				json_data.put("all_money", String.valueOf(n5));
				jsonarray_sheet5.add(json_data);
				
				json_data.put("custom_cor", "");
				json_data.put("cdate", "");
				json_data.put("products", "总金额");
				json_data.put("all_money", String.valueOf(n6));
				jsonarray_sheet6.add(json_data);

				all_json_data.put("all", jsonarray_data);
				all_json_data.put("sheet1", jsonarray_sheet1);
				all_json_data.put("sheet2", jsonarray_sheet2);
				all_json_data.put("sheet3", jsonarray_sheet3);
				all_json_data.put("sheet4", jsonarray_sheet4);
				all_json_data.put("sheet5", jsonarray_sheet5);
				all_json_data.put("sheet6", jsonarray_sheet6);
				all_json_data.put("file_name", file_name);

				out.print(all_json_data);

			} else {
				out.print("-1");
			}

			// 写入数据并关闭文件
			book.write();
			book.close();

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
		// doGet(request, response);
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
