/**
 * 
 */
// 页面加载完成时执行
$(document).ready(function() {
	$("#a_custom").click(function() {
		get_custom_list();
	});
});

// 从数据库获取客户表
function get_custom_list() {
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
				if (item.tax == "NULL") {
					item.tax = "无";
				}
			});
			custom_list(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}

// 显示客户管理表
function custom_list(data) {
	$('#custom').dataTable().fnDestroy();
	$('#custom')
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
									"data" : null,
									"defaultContent" : "<button class='btn btn-info top_handle' data-toggle='modal' data-target='#handle_custom'>操作</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#custom tbody').off();
	// 给按钮绑定点击事件
	$('#custom tbody').on('click', 'button', function() {

		var tr = $(this).parents("tr");
		var th = tr.find("td");
		var cars = new Array();
		var n = 0;
		th.each(function() {
			var ths = $(this);
			cars[n] = ths.text();
			n++;
		});
		get_custom_by_name(cars[0])
		return 0;
	});
}

function get_custom_by_name(id) {
	$
			.ajax({
				type : "GET",
				url : "/kingcomDingDan/opCustom?id=" + id,
				// data: my_param,
				dataType : "json",
				success : function(result) {
					var data = result.data;
					$("#handle_custom_name").val("");
					$("#handle_custom_id").val("");
					$("#handle_custom_addr").val("");
					$("#tbody_handle_custom_contacts").empty();
					$("#handle_custom_mes").val("");
					$("#m_tax_name").val("");
					$("#m_tax_num").val("");
					$("#m_tax_addr").val("");
					$("#m_tax_bank").val("");
					$("#m_tax_account").val("");
					$("#m_tax_phone").val("");
					$("#m_tax_fax").val("");
					$
							.each(
									data,
									function(idx, item) {
										if (item.contacts == "NULL") {
											item.contacts = "无";
										}
										if (item.mes == "NULL") {
											item.mes = "无";
										}
										if (item.tax == "NULL") {
											item.tax = "无";
										}

										$("#handle_custom_name").val(
												item.custom_name);
										$("#handle_custom_addr").val(item.addr);
										$("#handle_custom_id").val(item.id);

										var ar1 = new Array();
										var ar2 = new Array();
										var ss1 = item.contacts;
										ar1 = ss1.split("|");
										for (var i = 0; i < ar1.length; i++) {
											var ss2 = ar1[i];
											ar2 = ss2.split(",");
											if (ar2.length == 4) {
												$(
														"#tbody_handle_custom_contacts")
														.append(
																'<tr><th><input value="'
																		+ ar2[0]
																		+ '" class="form-control" placeholder="联系人"></th><th><input value="'
																		+ ar2[1]
																		+ '" class="form-control" placeholder="联系电话"></th><th><input value="'
																		+ ar2[2]
																		+ '" class="form-control" placeholder="职位"></th><th><input value="'
																		+ ar2[3]
																		+ '" class="form-control" placeholder="QQ"></th><th><button class="btn btn-danger">删除</button></th></tr>');
											} else if (ar2.length == 5) {
												$(
														"#tbody_handle_custom_contacts")
														.append(
																'<tr><th><input value="'
																		+ ar2[0]
																		+ '" class="form-control" placeholder="联系人"></th><th><input value="'
																		+ ar2[1]
																		+ '-'
																		+ ar2[2]
																		+ '" class="form-control" placeholder="联系电话"></th><th><input value="'
																		+ ar2[3]
																		+ '" class="form-control" placeholder="职位"></th><th><input value="'
																		+ ar2[4]
																		+ '" class="form-control" placeholder="QQ"></th><th><button class="btn btn-danger">删除</button></th></tr>');
											}

										}
										$("#handle_custom_mes").val(item.mes);
										var tax1 = new Array();
										var cars4 = item.tax;
										tax1 = cars4.split(",");
										if (tax1.length == 7) {
											$("#m_tax_name").val(tax1[0]);
											$("#m_tax_num").val(tax1[1]);
											$("#m_tax_addr").val(tax1[2]);
											$("#m_tax_bank").val(tax1[3]);
											$("#m_tax_account").val(tax1[4]);
											$("#m_tax_phone").val(tax1[5]);
											$("#m_tax_fax").val(tax1[6]);
										}
									});

				},
				error : function(xhr, ajaxOptions, thrownError) {

				}
			});
}

// 修改联系人按钮点击事件
$("#btn_handle_contacts")
		.click(
				function() {
					$("#tbody_handle_custom_contacts")
							.append(
									'<tr><th><input class="form-control" placeholder="联系人"></th><th><input class="form-control" placeholder="联系电话"></th><th><input class="form-control" placeholder="职位"></th><th><input class="form-control" placeholder="QQ"></th><th><button class="btn btn-danger">删除</button></th></tr>');
				});
// 给联系人删除按钮解除绑定点击事件
$('#table_handle_custom_contacts tbody').off();
// 给联系人删除按钮绑定点击事件
$('#table_handle_custom_contacts tbody').on('click', 'button', function() {
	var tr = $(this).parents("tr");
	$(tr).remove();
	return 0;
});

// 添加联系人按钮点击事件
$("#btn_add_contacts")
		.click(
				function() {
					$("#tbody_add_custom_contacts")
							.append(
									'<tr><th><input class="form-control" placeholder="联系人"></th><th><input class="form-control" placeholder="联系电话"></th><th><input class="form-control" placeholder="职位"></th><th><input class="form-control" placeholder="QQ"></th><th><button class="btn btn-danger">删除</button></th></tr>');
				});
// 给联系人删除按钮解除绑定点击事件
$('#table_add_custom_contacts tbody').off();
// 给联系人删除按钮绑定点击事件
$('#table_add_custom_contacts tbody').on('click', 'button', function() {
	var tr = $(this).parents("tr");
	$(tr).remove();
	return 0;
});

// 添加客户按钮点击事件
$("#btn_add_custom")
		.click(
				function() {
					// 登录按钮不可点显示登录中
					$(this).attr("disabled", true);
					$(this).text("执行中");
					// 清空警告信息
					$("#add_custom_warnmes").text("");
					// 验证用户名和密码
					var custom_name = $("#add_custom_name").val();
					var custom_addr = $("#add_custom_addr").val();
					var custom_mes = $("#add_custom_mes").val();
					var custom_contacts = "";
					$('#tbody_add_custom_contacts tr').each(function() {
						var cus = "";
						$(this).find("input").each(function() {
							cus = cus + $(this).val() + ",";
						});
						if (cus.length > 0) {
							cus = cus.slice(0, cus.length - 1);
							custom_contacts = custom_contacts + cus + "|";
						}

					});
					if (custom_contacts.length > 0) {
						custom_contacts = custom_contacts.slice(0,
								custom_contacts.length - 1);
					}

					// console.log("custom_contacts:" + custom_contacts);

					var tax = $("#tax_name").val() + "," + $("#tax_num").val()
							+ "," + $("#tax_addr").val() + ","
							+ $("#tax_bank").val() + ","
							+ $("#tax_account").val() + ","
							+ $("#tax_phone").val() + "," + $("#tax_fax").val();
					// 检查输入信息
					if (custom_name.length <= 0) {
						$("#add_custom_warnmes").text("请填写客户名称！");
						$(this).attr("disabled", false);
						$(this).text("添加客户");
						return 0;
					}
					if (custom_addr.length <= 0) {
						$("#add_custom_warnmes").text("请填写客户地址！");
						$(this).attr("disabled", false);
						$(this).text("添加客户");
						return 0;
					}

					if (custom_mes.length <= 0) {
						custom_mes = "NULL";
					}
					if (custom_contacts.length <= 0) {
						custom_contacts = "NULL";
					}
					if (tax.length <= 0) {
						tax = "NULL";
					}
					// 处理信息
					var json_data = {
						"active" : "insert",
						"custom_name" : custom_name,
						"custom_addr" : custom_addr,
						"custom_mes" : custom_mes,
						"custom_contacts" : custom_contacts,
						"tax" : tax
					};
					$
							.ajax({
								type : "POST",
								url : "/kingcomDingDan/opCustom",
								data : JSON.stringify(json_data),
								success : function(data) {
									$("#btn_add_custom")
											.attr("disabled", false);
									$("#btn_add_custom").text("添加客户");

									if (data == "-1") {
										$("#add_custom_warnmes").text("服务器错误！");
									}
									if (data == "0") {
										$("#add_custom_warnmes").text(
												custom_name + "客户已经存在！");
									}
									if (data == "1") {
										$("#add_custom_warnmes").text("");
										// 清空输入框信息
										$("#add_custom_name").val("");
										$("#add_custom_addr").val("");
										$("#add_custom_mes").val("");
										$("#tbody_add_custom_contacts").empty();
										$("#tbody_add_custom_contacts")
												.append(
														'<tr><th><input class="form-control" placeholder="联系人"></th><th><input class="form-control" placeholder="联系电话"></th><th><input class="form-control" placeholder="职位"></th><th><input class="form-control" placeholder="QQ"></th><th><button class="btn btn-danger">删除</button></th></tr>');
										$("#tax_name").val("");
										$("#tax_num").val("");
										$("#tax_addr").val("");
										$("#tax_bank").val("");
										$("#tax_account").val("");
										$("#tax_phone").val("");
										$("#tax_fax").val("");
										$("#add_custom").modal('toggle');
										// 刷新客户表
										get_custom_list();
									}
								},
								error : function(xhr, ajaxOptions, thrownError) {
									$("#add_custom_warnmes").text("本地网络错误！");
									$("#btn_add_custom")
											.attr("disabled", false);
									$("#btn_add_custom").text("添加客户");
								}
							});
				});
// 删除用户按钮
$("#btn_delete_custom").click(function() {
	// 删除按钮不可点显示删除中
	$("#btn_delete_custom").attr("disabled", true);
	$("#btn_modify_custom").attr("disabled", true);
	$("#btn_delete_custom").text("删除中");
	// 清空警告信息
	$("#handle_custom_warnmes").text("");
	// 获取用户名
	var custom_id = $("#handle_custom_id").val();
	var custom_name = $("#handle_custom_name").val();
	// 检验用户名
	if (custom_name.length <= 0) {
		$("#btn_delete_custom").attr("disabled", false);
		$("#btn_modify_custom").attr("disabled", false);
		$("#btn_delete_custom").text("删除客户");
		$("#handle_custom_warnmes").text("用户名不能为空");
		return 0;
	}
	// 处理信息
	var json_data = {
		"active" : "delete",
		"custom_id" : custom_id
	};
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opCustom",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_delete_custom").attr("disabled", false);
			$("#btn_modify_custom").attr("disabled", false);
			$("#btn_delete_custom").text("删除客户");
			if (data == "-1") {
				$("#handle_custom_warnmes").text("服务器错误！");
			} else {
				$("#handle_custom_warnmes").text("");
				// 清空输入框信息
				$("#handle_custom_id").val("");
				$("#handle_custom_name").val("");
				$("#handle_custom_addr").val("");
				$("#handle_custom_mes").val("");
				$("#tbody_handle_custom_contacts").empty();
				$("#tax_name").val("");
				$("#tax_num").val("");
				$("#tax_addr").val("");
				$("#tax_bank").val("");
				$("#tax_account").val("");
				$("#tax_phone").val("");
				$("#tax_fax").val("");
				$("#handle_custom").modal('toggle');
				// 刷新用户表
				get_custom_list();
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_delete_custom").attr("disabled", false);
			$("#btn_modify_custom").attr("disabled", false);
			$("#btn_delete_custom").text("删除客户");
			$("#handle_custom_warnmes").text("本地网络错误");
		}
	});
});
// 修改用户按钮
$("#btn_modify_custom").click(
		function() {
			// 修改按钮不可点显示修改中
			$("#btn_delete_custom").attr("disabled", true);
			$("#btn_modify_custom").attr("disabled", true);
			$("#btn_modify_custom").text("修改中");
			// 清空警告信息
			$("#handle_custom_warnmes").text("");
			// 获取用户名
			var custom_id = $("#handle_custom_id").val();
			var custom_name = $("#handle_custom_name").val();
			var custom_addr = $("#handle_custom_addr").val();
			var custom_mes = $("#handle_custom_mes").val();
			var custom_contacts = "";
			$('#tbody_handle_custom_contacts tr').each(function() {
				var cus = "";
				$(this).find("input").each(function() {
					// console.log("v1:" +
					// $(this).val());
					cus = cus + $(this).val() + ",";
				});
				if (cus.length > 4) {
					cus = cus.slice(0, cus.length - 1);
					custom_contacts = custom_contacts + cus + "|";
				}
			});
			if (custom_contacts.length > 0) {
				custom_contacts = custom_contacts.slice(0,
						custom_contacts.length - 1);
			}
			var tax = $("#m_tax_name").val() + "," + $("#m_tax_num").val()
					+ "," + $("#m_tax_addr").val() + ","
					+ $("#m_tax_bank").val() + "," + $("#m_tax_account").val()
					+ "," + $("#m_tax_phone").val() + ","
					+ $("#m_tax_fax").val();
			// 检验用户名
			if (custom_name.length <= 0) {
				$("#btn_delete_custom").attr("disabled", false);
				$("#btn_modify_custom").attr("disabled", false);
				$("#btn_modify_custom").text("修改客户");
				$("#handle_custom_warnmes").text("客户名不能为空");
				return 0;
			}
			if (custom_addr.length <= 0) {
				$("#btn_delete_custom").attr("disabled", false);
				$("#btn_modify_custom").attr("disabled", false);
				$("#btn_modify_custom").text("修改客户");
				$("#handle_custom_warnmes").text("客户地址不能为空");
				return 0;
			}

			if (custom_mes.length <= 0) {
				custom_mes = "NULL";
			}
			if (custom_contacts.length <= 0) {
				custom_contacts = "NULL";
			}
			if (tax.length <= 0) {
				tax = "NULL";
			}
			// 处理信息
			var json_data = {
				"active" : "modify",
				"custom_name" : custom_name,
				"custom_id" : custom_id,
				"custom_addr" : custom_addr,
				"custom_mes" : custom_mes,
				"custom_contacts" : custom_contacts,
				"tax" : tax
			};
			$.ajax({
				type : "POST",
				url : "/kingcomDingDan/opCustom",
				data : JSON.stringify(json_data),
				success : function(data) {
					$("#btn_delete_custom").attr("disabled", false);
					$("#btn_modify_custom").attr("disabled", false);
					$("#btn_modify_custom").text("修改客户");
					if (data == "-1") {
						$("#handle_custom_warnmes").text("服务器错误！");
					} else if (data == "0") {
						$("#handle_custom_warnmes").text("用户名已经存在！");
					} else {
						$("#handle_custom_warnmes").text("");
						// 清空输入框信息
						$("#handle_custom_name").val("");
						$("#handle_custom_id").val("");
						$("#handle_custom_addr").val("");
						$("#handle_custom_mes").val("");
						$("#tbody_handle_custom_contacts").empty();
						$("#tax_name").val("");
						$("#tax_num").val("");
						$("#tax_addr").val("");
						$("#tax_bank").val("");
						$("#tax_account").val("");
						$("#tax_phone").val("");
						$("#tax_fax").val("");
						$("#handle_custom").modal('toggle');
						// 刷新客户表
						get_custom_list();
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					$("#btn_delete_custom").attr("disabled", false);
					$("#btn_modify_custom").attr("disabled", false);
					$("#btn_modify_custom").text("修改客户");
					$("#handle_custom_warnmes").text("本地网络错误");
				}
			});
		});

//获取客户表Excel
$("#btn_create_custom_list").click(function() {
	$("#btn_create_custom_list").attr("disabled", true);
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/GetCustomList",
		dataType : "json",
		success : function(data) {
			$("#btn_create_custom_list").attr("disabled", false);
			console.log("data="+data);
			if(data=="-1"){
				
			}else{
				//console.log("data="+data);
				$("#btn_download_custom_list").attr("href", data.file_name);
				$("#btn_download_custom_list").attr("disabled", false);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_create_custom_list").attr("disabled", false);
		}
	});
});