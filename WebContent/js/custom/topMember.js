/**
 * 
 */
// 页面加载完成时执行
$(document).ready(function() {
	$("#a_member").click(function() {
		get_user_list();
		get_default_user_list();
	});
});

// 从数据库获取成员表
function get_user_list() {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opMember",
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
				if (item.role == "tax") {
					item.role = "开票人";
				}
				if (item.email_account == "NULL") {
					item.email_account = "无";
				}
				if (item.email_password == "NULL") {
					item.email_password = "无";
				}
			});
			user_list(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}
// 显示人员管理表
function user_list(data) {
	$('#member').dataTable().fnDestroy();
	$('#member')
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
									"data" : "user_password"
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
									"defaultContent" : "<button class='btn btn-info top_handle' data-toggle='modal' data-target='#handle_member'>操作</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#member tbody').off();
	// 给按钮绑定点击事件
	$('#member tbody').on('click', 'button', function() {

		var tr = $(this).parents("tr");
		var th = tr.find("td");
		var cars = new Array();
		var n = 0;
		th.each(function() {
			var ths = $(this);
			cars[n] = ths.text();
			n++;
		});
		$("#handle_member_user_name").val(cars[0]);
		$("#handle_member_user_account").val(cars[1]);
		$("#handle_member_user_password").val(cars[2]);
		$("#handle_member_email_account").val(cars[3]);
		$("#handle_member_email_password").val(cars[4]);
		if (cars[6] == "管理员") {
			$("#handle_member_role").val("1");
		}
		if (cars[6] == "负责人") {
			$("#handle_member_role").val("0");
		}
		return 0;
	});
}
// 添加成员按钮点击事件
$("#btn_add_member").click(function() {
	// 登录按钮不可点显示登录中
	$(this).attr("disabled", true);
	$(this).text("执行中");
	// 清空警告信息
	$("#add_member_mes").text("");
	// 验证用户名和密码
	var user_name = $("#add_member_user_name").val();
	var user_account = $("#add_member_user_account").val();
	var user_password = $("#add_member_user_password").val();
	var email_account = $("#add_member_email_account").val();
	var email_password = $("#add_member_email_password").val();
	var role = $("#add_member_role").val();
	// 检查输入信息
	if (user_name.length <= 0) {
		$("#add_member_mes").text("请填写姓名！");
		$(this).attr("disabled", false);
		$(this).text("添加成员");
		return 0;
	}
	if (user_account.length <= 0) {
		$("#add_member_mes").text("请填写用户名！");
		$(this).attr("disabled", false);
		$(this).text("添加成员");
		return 0;
	}
	if (user_password.length <= 0) {
		$("#add_member_mes").text("请填写密码！");
		$(this).attr("disabled", false);
		$(this).text("添加成员");
		return 0;
	}

	if (email_account.length <= 0) {
		email_account = "NULL";
	} else if (!isEmail(email_account)) {
		$("#add_member_mes").text("请正确填写邮箱账号！");
		$(this).attr("disabled", false);
		$(this).text("添加成员");
		return 0;
	}
	if (email_password.length <= 0) {
		email_password = "NULL";
	}
	// 处理信息
	var json_data = {
		"active" : "insert",
		"user_name" : user_name,
		"user_account" : user_account,
		"user_password" : user_password,
		"email_account" : email_account,
		"email_password" : email_password,
		"role" : role
	};
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opMember",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_add_member").attr("disabled", false);
			$("#btn_add_member").text("添加成员");

			if (data == "-1") {
				$("#add_member_mes").text("服务器错误！");
			}
			if (data == "0") {
				$("#add_member_mes").text(user_name + "用户名已经存在！");
			}
			if (data == "1") {
				$("#add_member_mes").text(user_name + "添加成功！");
				// 清空输入框信息
				$("#add_member_user_name").val("");
				$("#add_member_user_account").val("");
				$("#add_member_user_password").val("");
				$("#add_member_email_account").val("");
				$("#add_member_email_password").val("");
				// 刷新用户表
				get_user_list();
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#add_member_mes").text("本地网络错误！");
			$("#btn_add_member").attr("disabled", false);
			$("#btn_add_member").text("添加成员");
		}
	});
});
// 删除用户按钮
$("#btn_delete_member").click(function() {
	// 删除按钮不可点显示删除中
	$("#btn_delete_member").attr("disabled", true);
	$("#btn_modify_member").attr("disabled", true);
	$("#btn_delete_member").text("删除中");
	// 清空警告信息
	$("#handle_member_mes").text("");
	// 获取用户名
	var user_account = $("#handle_member_user_account").val();
	// 检验用户名
	if (user_account.length <= 0) {
		$("#btn_delete_member").attr("disabled", false);
		$("#btn_modify_member").attr("disabled", false);
		$("#btn_delete_member").text("删除成员");
		$("#handle_member_mes").text("用户名不能为空");
		return 0;
	}
	// 处理信息
	var json_data = {
		"active" : "delete",
		"user_account" : user_account
	};
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opMember",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_delete_member").attr("disabled", false);
			$("#btn_modify_member").attr("disabled", false);
			$("#btn_delete_member").text("删除成员");
			if (data == "-1") {
				$("#handle_member_mes").text("服务器错误！");
			} else {
				$("#handle_member_mes").text(user_account + "删除成功！");
				// 清空输入框信息
				$("#handle_member_user_name").val("");
				$("#handle_member_user_account").val("");
				$("#handle_member_user_password").val("");
				$("#handle_member_email_account").val("");
				$("#handle_member_email_password").val("");

				$("#handle_member").modal('toggle');
				// 刷新用户表
				get_user_list();
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_delete_member").attr("disabled", false);
			$("#btn_modify_member").attr("disabled", false);
			$("#btn_delete_member").text("删除成员");
			$("#handle_member_mes").text("本地网络错误");
		}
	});
});
// 修改用户按钮
$("#btn_modify_member").click(function() {
	// 修改按钮不可点显示修改中
	$("#btn_delete_member").attr("disabled", true);
	$("#btn_modify_member").attr("disabled", true);
	$("#btn_modify_member").text("修改中");
	// 清空警告信息
	$("#handle_member_mes").text("");
	// 获取用户名
	var user_account = $("#handle_member_user_account").val();
	var user_name = $("#handle_member_user_name").val();
	var user_password = $("#handle_member_user_password").val();
	var email_account = $("#handle_member_email_account").val();
	var email_password = $("#handle_member_email_password").val();
	var role = $("#handle_member_role").val();

	// 检验用户名
	if (user_account.length <= 0) {
		$("#btn_delete_member").attr("disabled", false);
		$("#btn_modify_member").attr("disabled", false);
		$("#btn_modify_member").text("修改成员");
		$("#handle_member_mes").text("用户名不能为空");
		return 0;
	}
	if (user_name.length <= 0) {
		$("#btn_delete_member").attr("disabled", false);
		$("#btn_modify_member").attr("disabled", false);
		$("#btn_modify_member").text("修改成员");
		$("#handle_member_mes").text("姓名不能为空");
		return 0;
	}
	if (user_password.length <= 0) {
		$("#btn_delete_member").attr("disabled", false);
		$("#btn_modify_member").attr("disabled", false);
		$("#btn_modify_member").text("修改成员");
		$("#handle_member_mes").text("密码不能为空");
		return 0;
	}
    
	if(email_account.length<=0){
		email_account = "NULL";
	}
	else if (email_account == "无") {
		email_account = "NULL";
	}else if (!isEmail(email_account)) {
		$("#btn_delete_member").attr("disabled", false);
		$("#btn_modify_member").attr("disabled", false);
		$("#btn_modify_member").text("修改成员");
		$("#handle_member_mes").text("邮箱格式不符");
		return 0;
	}
	if (email_password == "无") {
		email_password = "NULL";
	}
	// 处理信息
	var json_data = {
		"active" : "modify",
		"user_name" : user_name,
		"user_account" : user_account,
		"user_password" : user_password,
		"email_account" : email_account,
		"email_password" : email_password,
		"role" : role
	};
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opMember",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_delete_member").attr("disabled", false);
			$("#btn_modify_member").attr("disabled", false);
			$("#btn_modify_member").text("修改成员");
			if (data == "-1") {
				$("#handle_member_mes").text("服务器错误！");
			} else {
				$("#handle_member_mes").text(user_account + "修改成功！");
				// 清空输入框信息
				$("#handle_member_user_name").val("");
				$("#handle_member_user_account").val("");
				$("#handle_member_user_password").val("");
				$("#handle_member_email_account").val("");
				$("#handle_member_email_password").val("");

				$("#handle_member").modal('toggle');
				// 刷新用户表
				get_user_list();
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_delete_member").attr("disabled", false);
			$("#btn_modify_member").attr("disabled", false);
			$("#btn_modify_member").text("修改成员");
			$("#handle_member_mes").text("本地网络错误");
		}
	});
});

//获取默认人员表
function get_default_user_list() {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opDefault",
		// data: my_param,
		dataType : "json",
		success : function(result) {
			var data = result.data;
			default_user_list(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}
// 显示人员管理表
function default_user_list(data) {
	$('#default_member').dataTable().fnDestroy();
	$('#default_member')
			.DataTable(
					{
						data : data,
						columns : [
								{
									"data" : "name"
								},
								{
									"data" : "email_account"
								},
								{
									"data" : null,
									"defaultContent" : "<button class='btn btn-info top_handle' data-toggle='modal' data-target='#handle_default_member'>操作</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#default_member tbody').off();
	// 给按钮绑定点击事件
	$('#default_member tbody').on('click', 'button', function() {

		var tr = $(this).parents("tr");
		var th = tr.find("td");
		var cars = new Array();
		var n = 0;
		th.each(function() {
			var ths = $(this);
			cars[n] = ths.text();
			n++;
		});
		
		$("#handle_default_member_user_name").val(cars[0]);
		$("#handle_default_member_email_account").val(cars[1]);
		
		return 0;
	});
}
//添加默认成员按钮点击事件
$("#btn_add_default_member").click(function() {
	// 登录按钮不可点显示登录中
	$(this).attr("disabled", true);
	$(this).text("执行中");
	// 清空警告信息
	$("#add_default_member_mes").text("");
	// 验证用户名和密码
	var name = $("#add_default_member_user_name").val();
	var email_account = $("#add_default_member_email_account").val();
	
	// 检查输入信息
	if (name.length <= 0) {
		$("#add_default_member_mes").text("请填写姓名！");
		$(this).attr("disabled", false);
		$(this).text("添加默认成员");
		return 0;
	}

	if (email_account.length <= 0) {
		$("#add_default_member_mes").text("请填写邮箱！");
		$(this).attr("disabled", false);
		$(this).text("添加默认成员");
		return 0;
	} else if (!isEmail(email_account)) {
		$("#add_default_member_mes").text("请正确填写邮箱账号！");
		$(this).attr("disabled", false);
		$(this).text("添加默认成员");
		return 0;
	}
	
	// 处理信息
	var json_data = {
		"active" : "insert",
		"name" : name,
		"email_account" : email_account
	};
	
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opDefault",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_add_default_member").attr("disabled", false);
			$("#btn_add_default_member").text("添加默认成员");

			if (data == "-1") {
				$("#add_default_member_mes").text("服务器错误！");
			}
			if (data == "0") {
				$("#add_default_member_mes").text(name + "用户名已经存在！");
			}
			if (data == "1") {
				//$("#add_default_member_mes").text(name + "添加成功！");
				// 清空输入框信息
				$("#add_default_member_user_name").val("");
				$("#add_default_member_email_account").val("");
				$("#add_default_member").modal('toggle');
				// 刷新用户表
				get_default_user_list();
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#add_default_member_mes").text("本地网络错误！");
			$("#btn_add_default_member").attr("disabled", false);
			$("#btn_add_default_member").text("添加默认成员");
		}
	});
});

//删除默认用户按钮
$("#btn_delete_default_member").click(function() {
	// 删除按钮不可点显示删除中
	$("#btn_delete_default_member").attr("disabled", true);
	$("#btn_modify_default_member").attr("disabled", true);
	$("#btn_delete_default_member").text("删除中");
	// 清空警告信息
	$("#handle_default_member_mes").text("");
	// 获取用户名
	var name = $("#handle_default_member_user_name").val();
	// 检验用户名
	if (name.length <= 0) {
		$("#btn_delete_default_member").attr("disabled", false);
		$("#btn_modify_default_member").attr("disabled", false);
		$("#btn_delete_default_member").text("删除默认人员");
		$("#handle_default_member_mes").text("用户名不能为空");
		return 0;
	}
	// 处理信息
	var json_data = {
		"active" : "delete",
		"name" : name
	};
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opDefault",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_delete_default_member").attr("disabled", false);
			$("#btn_modify_default_member").attr("disabled", false);
			$("#btn_delete_default_member").text("删除默认人员");
			if (data == "-1") {
				$("#handle_default_member_mes").text("服务器错误！");
			} else {
				//$("#handle_default_member_mes").text(name + "删除成功！");
				// 清空输入框信息
				$("#handle_default_member_user_name").val("");
				$("#handle_default_member_email_account").val("");
				$("#handle_default_member").modal('toggle');
				// 刷新用户表
				get_default_user_list();
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_delete_default_member").attr("disabled", false);
			$("#btn_modify_default_member").attr("disabled", false);
			$("#btn_delete_default_member").text("删除默认人员");
			$("#handle_default_member_mes").text("本地网络错误");
		}
	});
});
// 修改默认用户按钮
$("#btn_modify_default_member").click(function() {
	// 修改按钮不可点显示修改中
	$("#btn_delete_default_member").attr("disabled", true);
	$("#btn_modify_default_member").attr("disabled", true);
	$("#btn_modify_default_member").text("修改中");
	// 清空警告信息
	$("#handle_default_member_mes").text("");
	// 获取用户名
	var name = $("#handle_default_member_user_name").val();
	var email_account = $("#handle_default_member_email_account").val();

	// 检验用户名
	if (name.length <= 0) {
		$("#btn_delete_default_member").attr("disabled", false);
		$("#btn_modify_default_member").attr("disabled", false);
		$("#btn_modify_default_member").text("修改默认人员");
		$("#handle_default_member_mes").text("用户名不能为空");
		return 0;
	}
	
	if(email_account.length<=0){
		$("#btn_delete_default_member").attr("disabled", false);
		$("#btn_modify_default_member").attr("disabled", false);
		$("#btn_modify_default_member").text("修改默认人员");
		$("#handle_default_member_mes").text("邮箱不能为空");
		return 0;
	}else if (!isEmail(email_account)) {
		$("#btn_delete_default_member").attr("disabled", false);
		$("#btn_modify_default_member").attr("disabled", false);
		$("#btn_modify_default_member").text("修改默认人员");
		$("#handle_default_member_mes").text("邮箱格式不正确");
		return 0;
	}
	// 处理信息
	var json_data = {
		"active" : "modify",
		"name" : name,
		"email_account" : email_account
	};
	
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opDefault",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_delete_default_member").attr("disabled", false);
			$("#btn_modify_default_member").attr("disabled", false);
			$("#btn_modify_default_member").text("修改默认人员");
			if (data == "-1") {
				$("#handle_default_member_mes").text("服务器错误！");
			} else {
				//$("#handle_default_member_mes").text(user_account + "修改成功！");
				// 清空输入框信息
				$("#handle_default_member_user_name").val("");
				$("#handle_default_member_email_account").val("");
				$("#handle_default_member").modal('toggle');
				// 刷新用户表
				get_default_user_list();
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_delete_default_member").attr("disabled", false);
			$("#btn_modify_default_member").attr("disabled", false);
			$("#btn_modify_default_member").text("修改默认人员");
			$("#handle_default_member_mes").text("本地网络错误");
		}
	});
});