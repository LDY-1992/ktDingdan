/**
 * 
 */
// 页面加载完成时执行
$(document).ready(function() {
	$("#login_btn").click(function() {
		// 登录按钮不可点显示登录中
		$(this).attr("disabled", true);
		$(this).text("登录中");
		// 清空警告信息
		$("#login_mes").text("");
		// 验证用户名和密码
		var account = $("#account").val();
		var password = $("#password").val();
		if (account.length <= 0 || password.length <= 0) {
			$("#login_mes").text("用户名和密码不能为空！");
			$(this).attr("disabled", false);
			$(this).text("登录");
			return 0;
		}
		// 登录
		var json_data = {
			"account" : account,
			"password" : password
		};
		$.ajax({
			type : "POST",
			url : "/kingcomDingDan/login",
			data : JSON.stringify(json_data),
			success : function(data) {
				if (data == "-1") {
					$("#login_mes").text("系统出错！");
					$("#login_btn").attr("disabled", false);
					$("#login_btn").text("登录");
					return 0;
				}
				if (data == "0") {
					$("#login_mes").text("用户名不存在！");
					$("#login_btn").attr("disabled", false);
					$("#login_btn").text("登录");
					return 0;
				}
				if (data == "1") {
					$("#login_mes").text("密码错误！");
					$("#login_btn").attr("disabled", false);
					$("#login_btn").text("登录");
					return 0;
				}

				if (data == "top") {
					$("#login_mes").text(data);
					window.location.href = "top.html";
				}else if(data == "middle"){
					$("#login_mes").text(data);
					window.location.href = "middle.html";
				}else {
					$("#login_mes").text("用户没有权限");
					$("#login_btn").attr("disabled", false);
					$("#login_btn").text("登录");
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				$("#login_mes").text("本地网络错误！");
				$("#login_btn").attr("disabled", false);
				$("#login_btn").text("登录");
			}
		});
	});
});
