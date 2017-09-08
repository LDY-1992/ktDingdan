/**
 * 
 */
// 订单显示
// 页面加载完成时执行
var tips = '<label for="">注意事项</label> <br> <input type="checkbox" checked="checked" value="发货提供快递单号，无快递单号，发货流程未闭环">发货提供快递单号，无快递单号，发货流程未闭环<br> <input type="checkbox" checked="checked" value="如发现金讯货号与文施内部代码不一致，请互相提醒检查">如发现金讯货号与文施内部代码不一致，请互相提醒检查<br> <input type="checkbox" checked="checked" value="随货带发票，请到财务处拿"> 随货带发票，请到财务处拿<br> <input type="checkbox" value="软件版本请于研发内部沟通">软件版本请于研发内部沟通<br> <input type="checkbox" value="G32默认带小辣椒天线"> G32默认带小辣椒天线';
var current_version = null;
$(document).ready(function() {
	$("#a_dingdan").click(function() {
		get_dingdan_list();
	});
});

function get_dingdan_list() {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opDingdan",
		// data: my_param,
		dataType : "json",
		success : function(result) {
			var data = result.data;
			dingdan_list(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}

function dingdan_list(data) {
	$('#dingdan').dataTable().fnDestroy();
	$('#dingdan')
			.DataTable(
					{
						data : data,
						columns : [
								{
									"data" : "date"
								},
								{
									"data" : "custom_cor"
								},
								{
									"data" : "id"
								},
								{
									"data" : "product"
								},
								{
									"data" : "all_money"
								},
								{
									"data" : "pay"
								},
								{
									"data" : null,
									"defaultContent" : "<button class='btn btn-info top_handle' id='btn_dingdan_list_table_detail'>详细信息</button>&nbsp&nbsp<button class='btn btn-danger top_handle' id='btn_dingdan_list_table_delete'>删除</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#dingdan tbody').off();
	// 给按钮绑定点击事件
	$('#dingdan tbody').on('click', '#btn_dingdan_list_table_detail',
			function() {

				$("#p_dingdan_send_cor").text("");
				$("#p_dingdan_send_person").text("");
				$("#p_dingdan_send_phone").text("");
				$("#p_dingdan_custom_cor").text("");
				$("#p_dingdan_custom_contacts").text("");
				$("#p_dingdan_custom_addr").text("");
				$("#p_dingdan_pay").text("");
				$("#p_dingdan_all_money").text("");
				$("#p_dingdan_traffic").text("");
				$("#p_dingdan_date").text("");
				$("#p_dingdan_state").text("");
				$("#p_dingdan_mes").text("");
				$("#table_modal_dingdan_products tbody").empty();

				var tr = $(this).parents("tr");
				var th = tr.find("td");
				var cars = new Array();
				var n = 0;
				th.each(function() {
					var ths = $(this);
					cars[n] = ths.text();
					n++;
				});
				$("#p_dingdan_id").text(cars[2]);
				query_dingdan_id(cars[2]);
				return 0;
			});

	$('#dingdan tbody').on('click', '#btn_dingdan_list_table_delete',
			function() {

				var tr = $(this).parents("tr");
				var th = tr.find("td");
				var cars = new Array();
				var n = 0;
				th.each(function() {
					var ths = $(this);
					cars[n] = ths.text();
					n++;
				});
				$("#span_delete_alert_dingdan").text(cars[2]);
				$("#modal_delete_alert").modal('toggle');

				return 0;
			});
}

$("#btn_modal_delete_alert_dingdan").click(function() {

	$("#btn_modal_delete_alert_dingdan").attr("disabled", true);
	$("#btn_modal_delete_alert_dingdan").text("正在删除");
	$("#p_delete_alert_warnmes").text("");

	var id = $("#span_delete_alert_dingdan").text();

	if (id.length <= 0) {
		$("#btn_modal_delete_alert_dingdan").attr("disabled", false);
		$("#btn_modal_delete_alert_dingdan").text("删除订单");
		$("#p_delete_alert_warnmes").text("系统出错");
		return 0;
	}
	var json_data = {
		"active" : "delete",
		"id" : id
	};
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opDingdan",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_modal_delete_alert_dingdan").attr("disabled", false);
			$("#btn_modal_delete_alert_dingdan").text("删除订单");

			if (data == "0") {
				$("#p_delete_alert_warnmes").text("订单删除失败。");
			} else if (data == "-1") {
				$("#p_delete_alert_warnmes").text("服务器出错。");
			} else {
				$("#modal_delete_alert").modal('toggle');
				get_dingdan_list();
			}

		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_modal_delete_alert_dingdan").attr("disabled", false);
			$("#btn_modal_delete_alert_dingdan").text("删除订单");
			$("#p_delete_alert_warnmes").text("本地网络出错");
		}
	});
});

// 订单号查询订单
function query_dingdan_id(id) {
	$("#modal_dingdan_mes").modal('toggle');
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opDingdan?id=" + id,
		// data: my_param,
		dataType : "json",
		success : function(result) {
			var data = result.data;
			$.each(data, function(idx, item) {
				if (item.pay == "NULL") {
					item.pay = "无";
				}
				if (item.traffic == "NULL") {
					item.traffic = "无";
				}
				if (item.date == "NULL") {
					item.date = "无";
				}
				if (item.mes == "NULL") {
					item.mes = "无";
				}

				$("#p_dingdan_id").text(item.id);
				$("#p_dingdan_send_cor").text(item.send_cor);
				$("#p_dingdan_send_person").text(item.send_person);
				$("#p_dingdan_send_phone").text(item.send_phone);
				$("#p_dingdan_custom_cor").text(item.custom_cor);
				$("#p_dingdan_custom_contacts").text(item.custom_contacts);
				$("#p_dingdan_custom_addr").text(item.custom_addr);

				var ar1 = new Array();
				var ar2 = new Array();
				var ss1 = item.product;
				ar1 = ss1.split("|");
				for (var i = 0; i < ar1.length; i++) {
					ar2 = ar1[i].split(",");
					$("#table_modal_dingdan_products tbody").append(
							'<tr><th>' + ar2[0] + '</th><th>' + ar2[1]
									+ '</th><th>' + ar2[2] + '</th><th>'
									+ ar2[3] + '</th><th>' + ar2[4]
									+ '</th></tr>');
				}

				$("#p_dingdan_pay").text(item.pay);
				$("#p_dingdan_all_money").text(item.all_money);
				$("#p_dingdan_traffic").text(item.traffic);
				$("#p_dingdan_date").text(item.date);
				$("#p_dingdan_state").text(item.state);
				$("#p_dingdan_mes").text(item.mes);

				$("#div_detail_dingdan_tip").empty();
				var tip = item.tip;
				var tips = new Array();
				tips = tip.split("|");
				for (var n = 0; n < tips.length; n++) {
					var i = n + 1;
					$("#div_detail_dingdan_tip").append(
							'<p class="modal_p_dingdan_mes text-primary">' + i
									+ ')' + tips[n]);
				}

				$("#p_dingdan_charge").text(item.charge_person);
			});
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}

// 添加产品按钮点击事件
$("#btn_add_dingdan_product")
		.click(
				function() {
					$("#tbody_add_dingdan_product")
							.append(
									'<tr><th><input id="input_dingdan_sale_module" class="form-control" placeholder="产品名称（内部型号）"></th><th><input class="form-control" placeholder="产品名称（销售型号）"></th><th><input class="form-control" id="input_dingdan_sale_count" placeholder="产品数量"></th><th><input class="form-control" id="input_dingdan_sale_price" placeholder="产品单价"></th><th><input class="form-control" id="input_dingdan_product_version" placeholder="产品版本号"></th><th><button class="btn btn-danger">删除</button></th></tr>');
				});
// 给添加产品按钮解除绑定点击事件
$('#tbody_add_dingdan_product').off();
// 给添加产品按钮绑定点击事件
$('#tbody_add_dingdan_product').on('click', 'button', function() {
	var tr = $(this).parents("tr");
	$(tr).remove();
	return 0;
});

// 弹出模块选择模态窗
$('#tbody_add_dingdan_product').on('click', '#input_dingdan_sale_module',
		function() {
			var my_tr = $(this).parents("tr");
			$("#modal_select_product").modal('toggle');
			get_select_product_list(my_tr);
		});

// 点击模块版本号弹出历史记录
$('#tbody_add_dingdan_product')
		.on(
				'click',
				'#input_dingdan_product_version',
				function() {
					$('#div_list_version').empty();
					$("#input_product_last_version").val('');
					$("#input_product_custom_version").val('');
					var v = $(this);
					var tr = v.parent().parent();
					var input = tr.find('input:eq(0)').val();
					if (input.length <= 0) {
						return 0;
					}
					// console.log("input="+input);

					$
							.ajax({
								type : "GET",
								url : "/kingcomDingDan/opProduct?id=" + input,
								// data: my_param,
								dataType : "json",
								success : function(result) {

									var version = result.version;
									for (n in version) {
										var lab = '<label>'
												+ version[n].version_name
												+ '</label>';
										var opt = '';
										var arr = version[n].version_num;
										for (m in arr) {
											opt = opt + '<option>' + arr[m]
													+ '</option>';
										}
										var sel = '<div class="input-group"><select class="form-control">'
												+ opt
												+ '</select><span class="input-group-addon" id="btn_chose_common_version">选择</span></div>';

										var all = '<div class="form-group col-md-6">'
												+ lab + sel + '</div>';
										$('#div_list_version').append(all);
									}
								},
								error : function(xhr, ajaxOptions, thrownError) {

								}
							});

					$("#modal_select_product_version").modal('show');

					var cus_id = $("#input_dingdan_custom_id").val();

					if (cus_id.length > 0) {
						$.ajax({
							type : "GET",
							url : "/kingcomDingDan/opGetVersionById?id="
									+ cus_id,
							// data: my_param,
							dataType : "json",
							success : function(result) {

								var version = result.version;
								for (n in version) {
									if (version[n].module == input) {
										$('#input_product_last_version').val(
												version[n].version_num);
										break;
									}
								}
							},
							error : function(xhr, ajaxOptions, thrownError) {

							}
						});
					}

					// 选择上次选择版本
					$("#btn_chose_last_version").off();
					$("#btn_chose_last_version").click(function() {
						var s11 = $("#input_product_last_version").val();
						v.val(s11);
						$("#modal_select_product_version").modal('hide');
					});
					// 选择自定义版本
					$("#btn_chose_custom_version").off();
					$("#btn_chose_custom_version").click(function() {
						var s12 = $("#input_product_custom_version").val();
						v.val("临时版本："+s12);
						$("#modal_select_product_version").modal('hide');
					});
					$('#div_list_version').off();
					$('#div_list_version').on(
							'click',
							'#btn_chose_common_version',
							function() {
								var sel = $(this).parent('div').find(
										'select:eq(0)').val();
								var v_n=$(this).parent('div').parent('div').find('label:eq(0)').text();
								v.val(v_n+"："+sel);
								$("#modal_select_product_version")
										.modal('hide');
							});

				});

// 自动计算总金额
$('#tbody_add_dingdan_product').on('input propertychange',
		'#input_dingdan_sale_count', function() {
			var tr = $('#tbody_add_dingdan_product').find("tr");
			var sum = 0;
			tr.each(function() {
				var inp = $(this).find("input");
				var n = 0;
				var n1 = 0;
				var n2 = 0;
				inp.each(function() {
					if (n == 2) {
						n1 = $(this).val();
					}
					if (n == 3) {
						n2 = $(this).val();
					}
					n = n + 1;
				});
				sum = sum + (n1 * n2);
			});
			$("#input_dingdan_all_money").val(sum.toFixed(2));
		});

$('#tbody_add_dingdan_product').on('input propertychange',
		'#input_dingdan_sale_price', function() {
			var tr = $('#tbody_add_dingdan_product').find("tr");
			var sum = 0;
			tr.each(function() {
				var inp = $(this).find("input");
				var n = 0;
				var n1 = 0;
				var n2 = 0;
				inp.each(function() {
					if (n == 2) {
						n1 = $(this).val();
					}
					if (n == 3) {
						n2 = $(this).val();
					}
					n = n + 1;
				});
				sum = sum + (n1 * n2);
			});
			$("#input_dingdan_all_money").val(sum.toFixed(2));
		});

// 添加注意事项
$("#btn_add_tip").click(
		function() {
			var tip = $("#input_add_tip").val();
			if (tip.length > 0) {
				$("#div_dingdan_tip").append(
						'<br> <input type="checkbox" checked="checked" value="'
								+ tip + '">' + ' ' + tip);
			}
		});

// 显示客户列表
$("#input_dingdan_custom_name").click(function() {
	$("#modal_select_custom").modal('toggle');
	get_select_custom_list();
});

// 从数据库获取客户表
function get_select_custom_list() {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opCustom",
		// data: my_param,
		dataType : "json",
		success : function(result) {
			var data = result.data;
			$.each(data, function(idx, item) {
				if (item.contacts == "NULL") {
					item.contacts = "无";
				} else {

				}
				if (item.mes == "NULL") {
					item.mes = "无";
				}
			});
			table_select_custom_list(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}

// 显示客户管理表
function table_select_custom_list(data) {
	$('#table_select_custom').dataTable().fnDestroy();
	$('#table_select_custom')
			.DataTable(
					{
						data : data,
						columns : [
								{
									"data" : "id"
								},
								{
									"data" : "custom_name"
								},
								{
									"data" : "addr"
								},
								{
									"data" : "contacts"
								},
								{
									"data" : null,
									"defaultContent" : "<button class='btn btn-info top_handle'>选择</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#table_select_custom tbody').off();
	// 给按钮绑定点击事件
	$('#table_select_custom tbody').on(
			'click',
			'button',
			function() {

				var tr = $(this).parents("tr");
				var th = tr.find("td");
				var cars = new Array();
				var n = 0;
				th.each(function() {
					var ths = $(this);
					cars[n] = ths.text();
					n++;
				});

				// var name=cars[0]+"-"+cars[1];
				$("#input_dingdan_custom_id").val(cars[0]);
				$("#input_dingdan_custom_name").val(cars[1]);
				$("#input_dingdan_custom_addr").val(cars[2]);

				$("#select_dingdan_contacts").empty();
				var ar1 = new Array();
				var ss1 = cars[3];
				ar1 = ss1.split("|");
				for (var i = 0; i < ar1.length; i++) {
					var ss2 = new Array();
					ss2 = ar1[i].split(",");
					var ss3 = ss2[0] + " " + ss2[1];

					$("#select_dingdan_contacts").append(
							'<option>' + ss3 + '</option>');
				}

				$("#modal_select_custom").modal('toggle');
				queryDingdanbycustom(cars[0]);
				return 0;
			});
}

function get_select_product_list(my_tr) {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opProduct",
		// data: my_param,
		dataType : "json",
		success : function(result) {
			var data = result.data;
			table_select_product_list(data, my_tr);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}

function table_select_product_list(data, my_tr) {
	$('#table_select_product').dataTable().fnDestroy();
	$('#table_select_product')
			.DataTable(
					{
						data : data,
						columns : [
								{
									"data" : "module_id"
								},
								{
									"data" : "sale_module"
								},
								{
									"data" : "inner_module"
								},
								{
									"data" : "count"
								},
								{
									"data" : null,
									"defaultContent" : "<button class='btn btn-info top_handle'>选择</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#table_select_product tbody').off();
	// 给按钮绑定点击事件
	$('#table_select_product tbody').on('click', 'button', function() {

		var tr = $(this).parents("tr");
		var th = tr.find("td");
		var cars = new Array();
		var n = 0;
		th.each(function() {
			var ths = $(this);
			cars[n] = ths.text();
			n++;
		});

		my_tr.children('th').children('input').eq(0).val(cars[1]);
		my_tr.children('th').children('input').eq(1).val(cars[2]);
		$("#modal_select_product").modal('toggle');

		return 0;
	});
}

// 查询最近三次订单
function queryDingdanbycustom(custom_id) {
	$("#p_mes_lastDingdan").text("正在查询【" + custom_id + "】最近三次订单。。。");
	$
			.ajax({
				type : "GET",
				url : "/kingcomDingDan/opDingdanbycustom?custom_id="
						+ custom_id,
				dataType : "json",
				success : function(result) {

					if (result == "-1") {
						$("#p_mes_lastDingdan").text(
								"查询【" + custom_id + "】订单失败，服务器出错");
					} else {
						$("#p_mes_lastDingdan").text(
								"客户【" + custom_id + "】最近三次订单");
						var data = result.data;
						$("#tbody_last_dingdan").empty();
						$
								.each(
										data,
										function(idx, item) {
											var ar1 = new Array();
											var ar2 = new Array();
											var ss1 = item.product;
											ar1 = ss1.split("|");
											var tbody = '';
											for (var i = 0; i < ar1.length; i++) {
												ar2 = ar1[i].split(",");
												tbody = tbody + '<tr>';
												for (var p = 0; p < ar2.length; p++) {
													tbody = tbody + '<th>'
															+ ar2[p] + '</th>';
												}
												tbody = tbody + '</tr>';
											}
											var content = '<table class="table table-bordered"><thead><th>产品名称（销售型号）</th><th>产品名称（内部型号）</th><th>产品数量</th><th>产品单价</th><th>产品版本号</th></thead><tbody><tr>'
													+ tbody
													+ '</tr></tbody></table>';
											$("#tbody_last_dingdan")
													.append(
															'<tr><td>'
																	+ item.id
																	+ '</td><td>'
																	+ item.date
																	+ '</td><td>'
																	+ item.custom_contacts
																	+ '</td><td>'
																	+ content
																	+ '</td><td><button class="btn btn-info top_handle" id="btn__dingdan_lastest_three">详细信息</button></td></tr>');
										});
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					$("#p_mes_lastDingdan").text(
							"查询【" + custom_cor + "】订单失败，本地网络错误");
				}
			});
}

//
$(document).on("click", "#btn__dingdan_lastest_three", function() {
	var tr = $(this).parents("tr");
	var th = tr.find("td");
	var cars = new Array();
	var n = 0;
	th.each(function() {
		var ths = $(this);
		cars[n] = ths.text();
		n++;
	});
	query_dingdan_id(cars[0]);
});

//
$(document).on("click", "#btn_dingdan_delete_member", function() {
	var div = $(this).parent("div").parent("div");
	$(div).remove();
});
//
$("#btn_dingdan_add_member").click(function() {
	get_select_member_list();
});

// 从数据库获取成员表
function get_select_member_list() {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opCharge",
		// data: my_param,
		dataType : "json",
		success : function(result) {
			var data = result.data;
			$.each(data, function(idx, item) {
				if (item.role == "top") {
					item.role = "管理员";
				}
				if (item.role == "middle") {
					item.role = "负责人";
				}
				if (item.email_account == "NULL") {
					item.email_account = "无";
				}
				if (item.email_password == "NULL") {
					item.email_password = "无";
				}
			});
			table_select_member(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}
// 显示人员管理表
function table_select_member(data) {
	$('#table_select_member').dataTable().fnDestroy();
	$('#table_select_member')
			.DataTable(
					{
						data : data,
						columns : [
								{
									"data" : "user_name"
								},
								{
									"data" : "user_account"
								},
								{
									"data" : "email_account"
								},
								{
									"data" : "email_password"
								},
								{
									"data" : "role"
								},
								{
									"data" : null,
									"defaultContent" : "<button class='btn btn-info top_handle'>选择</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#table_select_member tbody').off();
	// 给按钮绑定点击事件
	$('#table_select_member tbody')
			.on(
					'click',
					'button',
					function() {

						var tr = $(this).parents("tr");
						var th = tr.find("td");
						var cars = new Array();
						var n = 0;
						th.each(function() {
							var ths = $(this);
							cars[n] = ths.text();
							n++;
						});
						if (cars[2] == "无") {
							$("#p_select_member_warnmes").text("请完善该负责人邮箱信息");
							return 0;
						}
						if (cars[3] == "无") {
							$("#p_select_member_warnmes").text("请完善该负责人邮箱信息");
							return 0;
						}
						$("#div_dingdan_add_member")
								.append(
										'<div class="col-md-2"><div class="input-group"><input name="'
												+ cars[1]
												+ '" class="form-control" disabled="true" value="'
												+ cars[0]
												+ '"><div class="input-group-addon" id="btn_dingdan_delete_member">X</div></div></div>');
						$("#modal_select_member").modal('toggle');

						return 0;
					});
}

//
$(document).on("click", "#btn_dingdan_delete_tax", function() {
	var div = $(this).parent("div").parent("div");
	$(div).remove();
});
//
$("#btn_dingdan_add_tax").click(function() {
	get_select_tax_list();
});

// 从数据库获取成员表
function get_select_tax_list() {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opTax",
		// data: my_param,
		dataType : "json",
		success : function(result) {
			var data = result.data;
			$.each(data, function(idx, item) {
				if (item.role == "top") {
					item.role = "管理员";
				} else if (item.role == "middle") {
					item.role = "负责人";
				} else if (item.role == "tax") {
					item.role = "开票人";
				}
				if (item.email_account == "NULL") {
					item.email_account = "无";
				}
				if (item.email_password == "NULL") {
					item.email_password = "无";
				}
			});
			table_select_tax(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}
// 显示人员管理表
function table_select_tax(data) {
	$('#table_select_tax').dataTable().fnDestroy();
	$('#table_select_tax')
			.DataTable(
					{
						data : data,
						columns : [
								{
									"data" : "user_name"
								},
								{
									"data" : "user_account"
								},
								{
									"data" : "email_account"
								},
								{
									"data" : "email_password"
								},
								{
									"data" : "role"
								},
								{
									"data" : null,
									"defaultContent" : "<button class='btn btn-info top_handle'>选择</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#table_select_tax tbody').off();
	// 给按钮绑定点击事件
	$('#table_select_tax tbody')
			.on(
					'click',
					'button',
					function() {

						var tr = $(this).parents("tr");
						var th = tr.find("td");
						var cars = new Array();
						var n = 0;
						th.each(function() {
							var ths = $(this);
							cars[n] = ths.text();
							n++;
						});
						if (cars[2] == "无") {
							$("#p_select_tax_warnmes").text("请完善该开票人邮箱信息");
							return 0;
						}
						/*
						 * if(cars[3]=="无"){
						 * $("#p_select_tax_warnmes").text("请完善该开票人邮箱信息");
						 * return 0; }
						 */
						$("#div_dingdan_add_tax")
								.append(
										'<div class="col-md-2"><div class="input-group"><input name="'
												+ cars[1]
												+ '" class="form-control" disabled="true" value="'
												+ cars[0]
												+ '"><div class="input-group-addon" id="btn_dingdan_delete_tax">X</div></div></div>');
						$("#modal_select_tax").modal('toggle');

						return 0;
					});
}

var dingdan_json_data = "";

$("#btn_confirm_dingdan_submit").click(
		function() {

			$("#btn_confirm_dingdan_submit").attr("disabled", true);
			$("#btn_confirm_dingdan_submit").text("正在提交");

			$.ajax({
				type : "POST",
				url : "/kingcomDingDan/opDingdan",
				data : JSON.stringify(dingdan_json_data),
				success : function(data) {
					$("#btn_confirm_dingdan_submit").attr("disabled", false);
					$("#btn_confirm_dingdan_submit").text("提交订单");
					$("#modal_confirm_dingdan").modal('toggle');

					if (data == "001") {

						show_mes_alert("订单添加成功！");
						$("#input_dingdan_custom_id").val("");
						$("#input_dingdan_custom_name").val("");
						$("#input_dingdan_custom_addr").val("");
						$("#select_dingdan_contacts").empty();
						$("#tbody_add_dingdan_product").empty();
						$("#input_dingdan_all_money").val("");
						$("#input_dingdan_date").val("");
						$("#input_dingdan_mes").val("");
						$("#div_dingdan_add_member").empty();
						$("#div_dingdan_add_tax").empty();
						$("#p_mes_lastDingdan").text("");
						$("#tbody_last_dingdan").empty();
						$("#select_dingdan_pay").find("option[text='宋辉挂账不开票']")
								.attr("selected", true);
						$("#input_add_tip").val("");
						$("#div_dingdan_tip").empty();
						$("#div_dingdan_tip").append(tips);
						$("#chaosong").attr("checked", false);

					} else if (data == "-1") {
						show_mes_alert("订单添加失败，服务器出错！");
					} else if (data == "002") {
						show_mes_alert("订单添加失败，负责人邮件发送成功，税单邮件发送失败。");
					} else if (data == "003") {
						show_mes_alert("订单添加失败，负责人邮件发送失败，税单邮件发送成功。");
					} else if (data == "004") {
						show_mes_alert("订单添加失败，负责人邮件发送失败，税单邮件发送失败。");
					} else if (data == "005") {
						show_mes_alert("订单添加失败，负责人邮件发送失败。");
					} else {
						show_mes_alert("未知错误。");
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					$("#p_confirm_dingdan_warnmes").text("本地网络错误！");
					$("#btn_confirm_dingdan_submit").attr("disabled", false);
					$("#btn_confirm_dingdan_submit").text("提交订单");
				}
			});

		});

// 生成订单按钮点击事件
$("#btn_add_dingdan")
		.click(
				function() {

					$("#btn_add_dingdan").attr("disabled", true);
					$("#btn_add_dingdan").text("正在生成");
					$("#p_dingdan_warnmes").text("");
					// 订单
					var send_cor = $("#input_dingdan_send_cor").val();
					var send_person = $("#input_dingdan_send_person").val();
					var send_phone = $("#input_dingdan_send_phone").val();

					var custom_id = $("#input_dingdan_custom_id").val();
					var custom_name = $("#input_dingdan_custom_name").val();
					var custom_addr = $("#input_dingdan_custom_addr").val();
					var contacts = $("#select_dingdan_contacts").val();

					var product = "";

					$('#table_add_dingdan_product tr').each(function() {
						var product1 = "";
						$(this).find("input").each(function() {
							product1 = product1 + $(this).val() + ",";
						});
						if (product1.length > 5) {
							product1 = product1.slice(0, product1.length - 1);
							product1 = product1 + "|";
							product = product + product1;
						}
					});
					if (product.length > 0) {
						product = product.slice(0, product.length - 1);
					}

					var pay = $("#select_dingdan_pay").val();
					var money = $("#input_dingdan_all_money").val();
					var traffic = $("#select_dingdan_traffic").val();
					var date = $("#input_dingdan_date").val();

					var mes = $("#input_dingdan_mes").val();
					var tip = "";
					$('#div_dingdan_tip input').each(function() {
						if ($(this).is(":checked") == true) {
							tip = tip + $(this).val() + "|";
						}
					});
					if (tip.length > 0) {
						tip = tip.slice(0, tip.length - 1);
					}

					// 负责人
					var chaosong = "";
					if ($("#chaosong").is(":checked") == true) {
						chaosong = "YES";
					} else {
						chaosong = "NO";
					}
					var person = "";
					$("#div_dingdan_add_member input").each(
							function() {
								person = person + $(this).val() + "&"
										+ $(this).attr("name") + "-";
							});
					if (person.length > 0) {
						person = person.slice(0, person.length - 1);
					}

					var tax = "NO";
					var tax_person = "";
					$("#div_dingdan_add_tax input").each(
							function() {
								tax_person = tax_person + $(this).val() + "&"
										+ $(this).attr("name") + "-";
							});
					if (tax_person.length > 0) {
						tax = "YES";
						tax_person = tax_person.slice(0, tax_person.length - 1);
					}
					// 检验输入合法性
					if (send_cor.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请填写寄件单位名称！");
						return 0;
					}

					if (send_person.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请填写寄件人！");
						return 0;
					}

					if (send_phone.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请填写寄件人电话！");
						return 0;
					}

					if (custom_name.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请选择收件单位！");
						return 0;
					}

					if (contacts.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请选择收件人-电话！");
						return 0;
					}

					if (product.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请填写产品！");
						return 0;
					}

					if (money.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请填写总金额！");
						return 0;
					} else if (!isNumber(money)) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("总金额必须为数字，并且最多两位小数！");
						return 0;
					}

					if (date.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请填写发货日期！");
						return 0;
					} else {
						date = date.replace(/\-/g, "");
					}

					if (person.length <= 0) {
						$("#btn_add_dingdan").attr("disabled", false);
						$("#btn_add_dingdan").text("生成订单");
						$("#p_dingdan_warnmes").text("请选择负责人！");
						return 0;
					}

					if (mes.length <= 0) {
						mes = "NULL";
					}

					// 清空信息
					$("#p_confirm_send_cor").text("");
					$("#p_confirm_send_person").text("");
					$("#p_confirm_send_phone").text("");
					$("#p_confirm_custom_cor").text("");
					$("#p_confirm_custom_addr").text("");
					$("#p_confirm_custom_contacts").text("");

					$("#p_confirm_pay").text("");
					$("#p_confirm_all_money").text("");
					$("#p_confirm_traffic").text("");
					$("#p_confirm_date").text("");

					$("#tbody_confirm_product").empty();
					$("#td_confirm_tip").empty();

					// 添加信息

					$("#btn_add_dingdan").attr("disabled", false);
					$("#btn_add_dingdan").text("生成订单");
					$("#p_dingdan_warnmes").text("");

					$("#p_confirm_send_cor").text(send_cor);
					$("#p_confirm_send_person").text(send_person);
					$("#p_confirm_send_phone").text(send_phone);
					$("#p_confirm_custom_cor").text(custom_name);
					$("#p_confirm_custom_addr").text(custom_addr);
					$("#p_confirm_custom_contacts").text(contacts);

					var json_default = {
						"version" : []
					};

					var ar1 = new Array();
					ar1 = product.split("|");
					for (var i = 0; i < ar1.length; i++) {
						var ar2 = new Array();
						ar2 = ar1[i].split(",");
						if (ar2[2].length <= 0) {
							$("#btn_add_dingdan").attr("disabled", false);
							$("#btn_add_dingdan").text("生成订单");
							$("#p_dingdan_warnmes").text("请填写产品数量！");
							return 0;
						} else {
							if (!isInteger(ar2[2])) {
								$("#btn_add_dingdan").attr("disabled", false);
								$("#btn_add_dingdan").text("生成订单");
								$("#p_dingdan_warnmes").text("产品数量必须为正整数！");
								return 0;
							}
						}

						if (ar2[3].length > 0) {
							if (!isNumber(ar2[3])) {
								$("#btn_add_dingdan").attr("disabled", false);
								$("#btn_add_dingdan").text("生成订单");
								$("#p_dingdan_warnmes").text(
										"单价必须为数字，并且最多两位小数！");
								return 0;
							}
						}

						var json_v = {
							"module" : ar2[0],
							"version_num" : ar2[4]
						};
						json_default.version.push(json_v);

						$("#tbody_confirm_product")
								.append(
										'<tr><td><p style="font-size: 18px; color: blue; margin-bottom: 0px;">'
												+ ar2[0]
												+ '</td><td><p style="font-size: 18px; color: blue; margin-bottom: 0px;">'
												+ ar2[1]
												+ '</td><td><p style="font-size: 18px; color: blue; margin-bottom: 0px;">'
												+ ar2[2]
												+ '</td><td><p style="font-size: 18px; color: blue; margin-bottom: 0px;">'
												+ ar2[3]
												+ '</td><td><p style="font-size: 18px; color: blue; margin-bottom: 0px;">'
												+ ar2[4] + '</td></tr>');
					}

					$("#p_confirm_pay").text(pay);
					$("#p_confirm_all_money").text(money);
					$("#p_confirm_traffic").text(traffic);
					$("#p_confirm_date").text(date);

					var tips = new Array();
					tips = tip.split("|");
					for (var n = 0; n < tips.length; n++) {
						var i = n + 1;
						$("#td_confirm_tip").append(
								'<p style="font-size: 18px; color: blue; margin-bottom: 0px;">'
										+ i + ')' + tips[n]);
					}

					$("#modal_confirm_dingdan").modal('toggle');

					var content = $("#email_content").html();
					encodeURI(content);
					var json_data = {
						"active" : "insert",
						"send_cor" : send_cor,
						"send_person" : send_person,
						"send_phone" : send_phone,
						"custom_name" : custom_name,
						"custom_id" : custom_id,
						"custom_addr" : custom_addr,
						"contacts" : contacts,
						"product" : product,
						"pay" : pay,
						"money" : money,
						"traffic" : traffic,
						"date" : date,
						"mes" : mes,
						"tip" : tip,
						"chaosong" : chaosong,
						"person" : person,
						"content" : content,
						"tax" : tax,
						"tax_person" : tax_person,
						"default" : json_default
					};
					dingdan_json_data = json_data;
				});

// 按日期查询订单
$("#btn_query_dingdan").click(function() {
	$("#btn_query_dingdan").attr("disabled", true);
	$("#btn_query_dingdan").text("正在查询");
	$("#p_dingdan_warn").text("");
	var date1 = $("#input_dingdan_date1").val();
	var date2 = $("#input_dingdan_date2").val();
	if (date1.length <= 0) {
		$("#btn_query_dingdan").attr("disabled", false);
		$("#btn_query_dingdan").text("查询");
		$("#p_dingdan_warn").text("请选择开始日期");
		return 0;
	}
	if (date2.length <= 0) {
		$("#btn_query_dingdan").attr("disabled", false);
		$("#btn_query_dingdan").text("查询");
		$("#p_dingdan_warn").text("请选择结束日期");
		return 0;
	}
	var json_data = {
		"active" : "query",
		"date1" : date1,
		"date2" : date2
	};
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opDingdan",
		data : JSON.stringify(json_data),
		dataType : "json",
		success : function(result) {
			$("#btn_query_dingdan").attr("disabled", false);
			$("#btn_query_dingdan").text("查询");
			$("#p_dingdan_warn").text("");
			var data = result.data;
			dingdan_list(data);

		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_query_dingdan").attr("disabled", false);
			$("#btn_query_dingdan").text("查询");
			$("#p_dingdan_warn").text("本地网络出错");
		}
	});

});