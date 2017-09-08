/**
 * 
 */
// 页面加载时检查登录凭证
window.onload = function() {
	$.get("/kingcomDingDan/Session_servlet", function(data, status) {
		console.log("数据: " + data + "\n状态: " + status);
		if (status == "success") {
			if (data == "1") {

			} else {
				window.location.href = "Login.html";
			}
		} else {
			window.location.href = "Login.html";
		}
	});
}

//显示提示信息模态框
function show_mes_alert(str){
	$("#h4_show_mes").text(str);
	$("#modal_show_mes_alert").modal('toggle');
}