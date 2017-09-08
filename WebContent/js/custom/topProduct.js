/**
 * 
 */

// 产品版本号默认界面
var version_defalt = '<tr><td>标准版本</td><td><button type="button"class="btn btn-primary top_handle"id="btn_add_version_num">+</button></td><td><button type="button"class="btn btn-danger top_handle"id="btn_add_delete_tr">删除</button></td></tr><tr><td>DTU</td><td><button type="button"class="btn btn-primary top_handle"id="btn_add_version_num">+</button></td><td><button type="button"class="btn btn-danger top_handle"id="btn_add_delete_tr">删除</button></td></tr><tr><td>MQTT</td><td><button type="button"class="btn btn-primary top_handle"id="btn_add_version_num">+</button></td><td><button type="button"class="btn btn-danger top_handle"id="btn_add_delete_tr">删除</button></td></tr><tr><td>自定义版本</td><td><button type="button"class="btn btn-primary top_handle"id="btn_add_version_num">+</button></td><td><button type="button"class="btn btn-danger top_handle"id="btn_add_delete_tr">删除</button></td></tr>';
var version_wait='<tr><td colspan="3" style="text-align:center"><img src="picture/loading40.gif" alt=""></td></tr>';
// 页面加载完成时执行
$(document).ready(function() {
	$("#a_product").click(function() {
		get_product_list();
	});
});

function get_product_list() {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opProduct",
		// data: my_param,
		dataType : "json",
		success : function(result) {
			var data = result.data;
			product_list(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
	});
}

function product_list(data) {
	$('#product').dataTable().fnDestroy();
	$('#product')
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
									"defaultContent" : "<button class='btn btn-info top_handle' data-toggle='modal' data-target='#handle_product'>操作</button>"
								} ]
					});
	// 给按钮解除绑定点击事件
	$('#product tbody').off();
	// 给按钮绑定点击事件
	$('#product tbody').on('click', 'button', function() {

		var tr = $(this).parents("tr");
		var th = tr.find("td");
		var cars = new Array();
		var n = 0;
		th.each(function() {
			var ths = $(this);
			cars[n] = ths.text();
			n++;
		});
		$("#handle_product_module_id").val(cars[0]);
		$("#handle_product_sale_module").val(cars[1]);
		$("#handle_product_inner_module").val(cars[2]);
		$("#handle_product_count").val(cars[3]);
		
		get_version_by_id(cars[1]);
		
		return 0;
	});
}

//加载版本
function get_version_by_id(id){
	$("#btn_modify_version_name").attr("disabled", true);
	$("#tbody_modify_product_version").append(version_wait);
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opProduct?id="+id,
		// data: my_param,
		dataType : "json",
		success : function(result) {
			$("#btn_modify_version_name").attr("disabled", false);
			$("#tbody_modify_product_version").empty();
			var version = result.version;
			for(n in version){
				var td1='<td>'+version[n].version_name+'</td>';
				var td2='';
				var arr=version[n].version_num;
				for(m in arr){
					td2=td2+'<div class="btn-group"><button name="version_num"class="btn btn-default">'
					+ arr[m]
					+ '</button><button id="btn_modify_product_delete_num"class="btn btn-default">-</button></div>';
				}
				td2='<td><button type="button"class="btn btn-primary top_handle"id="btn_modify_version_num">+</button>'+td2+'</td>';
				var td3='<td><button type="button"class="btn btn-danger top_handle"id="btn_modify_delete_tr">删除</button></td>';
				var tr='<tr>'+td1+td2+td3+'</tr>';
				$("#tbody_modify_product_version").append(tr);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_modify_version_name").attr("disabled", false);
			$("#tbody_modify_product_version").empty();
		}
	});
}

// 点击添加模块
$("#btn_add_product").click(function() {
	// 添加按钮不可点击，清空警告信息。
	$("#btn_add_product").attr("disabled", true);
	$("#btn_add_product").text("添加中");
	$("#add_product_warnmes").text("");
	// 检验输入数据合法性
	var sale_module = $("#add_product_sale_module").val();
	var inner_module = $("#add_product_inner_module").val();
	var count = $("#add_product_count").val();

	// 构成版本json字符串
	var json_add_version = {
		"version" : []
	};

	var tr = $('#tbody_add_product_version').children("tr");
	tr.each(function() {
		var v1 = $(this).find("td:eq(0)").text();
		var td = $(this).find("td:eq(1)");
		var bs = td.find("button[name='version_num']");
		var bsa = new Array();
		var n = 0;
		bs.each(function() {
			bsa[n] = $(this).text();
			n = n + 1;
		});
		var json_tr = {
			"version_name" : v1,
			"version_num" : bsa
		};
		json_add_version.version.push(json_tr);
	});

	// console.log("json_add_version=" + JSON.stringify(json_add_version));

	if (sale_module.length <= 0) {
		$("#btn_add_product").attr("disabled", false);
		$("#btn_add_product").text("添加模块");
		$("#add_product_warnmes").text("销售型号不能为空");
		return 0;
	}
	if (inner_module.length <= 0) {
		$("#btn_add_product").attr("disabled", false);
		$("#btn_add_product").text("添加模块");
		$("#add_product_warnmes").text("内部型号不能为空");
		return 0;
	}
	if (count.length > 0) {
		if (!isNumber(count)) {
			$("#btn_add_product").attr("disabled", false);
			$("#btn_add_product").text("添加模块");
			$("#add_product_warnmes").text("产品库存必须为数字");
			return 0;
		}
	}
	// 处理信息
	var json_data = {
		"active" : "insert",
		"sale_module" : sale_module,
		"inner_module" : inner_module,
		"count" : count,
		"version" : json_add_version
	};
	// ajax提交
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opProduct",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_add_product").attr("disabled", false);
			$("#btn_add_product").text("添加模块");
			if (data == "-1") {
				$("#add_product_warnmes").text("服务器出错");
			} else if (data == "0") {
				$("#add_product_warnmes").text("销售型号已经存在");
			} else if (data == "1") {
				$("#add_product_warnmes").text("");
				$("#add_product_sale_module").val("");
				$("#add_product_inner_module").val("");
				$("#add_product_count").val("");
				$("#input_add_version_name").val("");
				$("#input_add_version_num").val("");

				$("#tbody_add_product_version").empty();
				$("#tbody_add_product_version").append(version_defalt);

				$("#add_product").modal('toggle');
				get_product_list();
			}

		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_add_product").attr("disabled", false);
			$("#btn_add_product").text("添加模块");
			$("#add_product_warnmes").text("本地网络出错");

		}
	});
});

// 点击删除按钮
$("#btn_delete_product").click(function() {
	// 删除按钮不可点击，清空警告信息。
	$("#btn_delete_product").attr("disabled", true);
	$("#btn_delete_product").text("删除中");
	$("#handle_product_warnmes").text("");
	// 检验输入数据合法性
	var module_id = $("#handle_product_module_id").val();

	if (module_id.length <= 0) {
		$("#btn_delete_product").attr("disabled", false);
		$("#btn_delete_product").text("删除模块");
		$("#handle_product_warnmes").text("模块ID不能为空");
		return 0;
	}

	// 处理信息
	var json_data = {
		"active" : "delete",
		"module_id" : module_id
	};
	// ajax提交
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opProduct",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_delete_product").attr("disabled", false);
			$("#btn_delete_product").text("删除模块");
			if (data == "-1") {
				$("#handle_product_warnmes").text("服务器出错");
			} else if (data == "0") {
				$("#add_product_warnmes").text("模块ID不存在");
			} else if (data == "1") {
				$("#handle_product_warnmes").text("");
				$("#handle_product").modal('toggle');
				get_product_list();
			}

		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_delete_product").attr("disabled", false);
			$("#btn_delete_product").text("删除模块");
			$("#handle_product_warnmes").text("本地网络出错");

		}
	});
});

// 点击修改按钮
$("#btn_modify_product").click(function() {
	// 修改按钮不可点击，清空警告信息。
	$("#btn_modify_product").attr("disabled", true);
	$("#btn_modify_product").text("修改中");
	$("#handle_product_warnmes").text("");
	// 检验输入数据合法性
	var module_id = $("#handle_product_module_id").val();
	var sale_module = $("#handle_product_sale_module").val();
	var inner_module = $("#handle_product_inner_module").val();
	var count = $("#handle_product_count").val();
	
	// 构成版本json字符串
	var json_add_version = {
		"version" : []
	};

	var tr = $('#tbody_modify_product_version').children("tr");
	tr.each(function() {
		var v1 = $(this).find("td:eq(0)").text();
		var td = $(this).find("td:eq(1)");
		var bs = td.find("button[name='version_num']");
		var bsa = new Array();
		var n = 0;
		bs.each(function() {
			bsa[n] = $(this).text();
			n = n + 1;
		});
		var json_tr = {
			"version_name" : v1,
			"version_num" : bsa
		};
		json_add_version.version.push(json_tr);
	});

	//console.log("json_add_version=" + JSON.stringify(json_add_version));

	if (sale_module.length <= 0) {
		$("#btn_modify_product").attr("disabled", false);
		$("#btn_modify_product").text("修改模块");
		$("#handle_product_warnmes").text("销售型号不能为空");
		return 0;
	}
	if (inner_module.length <= 0) {
		$("#btn_modify_product").attr("disabled", false);
		$("#btn_modify_product").text("修改模块");
		$("#handle_product_warnmes").text("内部型号不能为空");
		return 0;
	}
	if (module_id.length <= 0) {
		$("#btn_modify_product").attr("disabled", false);
		$("#btn_modify_product").text("修改模块");
		$("#handle_product_warnmes").text("模块ID不能为空");
		return 0;
	}

	if (count.length > 0) {
		if (!isNumber(count)) {
			$("#btn_modify_product").attr("disabled", false);
			$("#btn_modify_product").text("修改模块");
			$("#handle_product_warnmes").text("产品库存必须为数字");
			return 0;
		}
	}

	// 处理信息
	var json_data = {
		"active" : "modify",
		"module_id" : module_id,
		"sale_module" : sale_module,
		"inner_module" : inner_module,
		"count" : count,
		"version" : json_add_version
	};
	// ajax提交
	$.ajax({
		type : "POST",
		url : "/kingcomDingDan/opProduct",
		data : JSON.stringify(json_data),
		success : function(data) {
			$("#btn_modify_product").attr("disabled", false);
			$("#btn_modify_product").text("修改模块");
			if (data == "-1") {
				$("#handle_product_warnmes").text("服务器出错");
			} else if (data == "0") {
				$("#handle_product_warnmes").text("模块ID不存在");
			} else if (data == "1") {
				$("#handle_product_warnmes").text("");
				$("#handle_product").modal('toggle');
				get_product_list();
			}

		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#btn_modify_product").attr("disabled", false);
			$("#btn_modify_product").text("修改模块");
			$("#handle_product_warnmes").text("本地网络出错");

		}
	});
});

// 添加版本操作
$("#btn_add_version_name")
		.click(
				function() {
					var t1 = $("#input_add_version_name").val();
					if (t1.length > 0) {
						$('#tbody_add_product_version')
								.append(
										'<tr><td>'
												+ t1
												+ '</td><td><button type="button"class="btn btn-primary top_handle"id="btn_add_version_num">+</button></td><td><button type="button"class="btn btn-danger top_handle"id="btn_add_delete_tr">删除</button></td></tr>');
					}
					$("#input_add_version_name").val("");

				});
$('#tbody_add_product_version').off();
// 删除版本
$('#tbody_add_product_version').on('click', '#btn_add_delete_tr', function() {

	var tr = $(this).parents("tr");
	$(tr).remove();
});
// 删除版本号
$('#tbody_add_product_version').on('click', '#btn_add_product_delete_num',
		function() {
			var div = $(this).parent("div");
			$(div).remove();
		});
// 添加版本号
$('#tbody_add_product_version')
		.on(
				'click',
				'#btn_add_version_num',
				function() {
					var t1 = $("#input_add_version_num").val();
					if (t1.length > 0) {
						var td = $(this).parent("td");
						$(td)
								.append(
										'<div class="btn-group"><button name="version_num"class="btn btn-default">'
												+ t1
												+ '</button><button id="btn_add_product_delete_num"class="btn btn-default">-</button></div>');
					}
					$("#input_add_version_num").val("");
				});

//修改版本操作
$("#btn_modify_version_name")
.click(
		function() {
			var t1 = $("#input_modify_version_name").val();
			if (t1.length > 0) {
				$('#tbody_modify_product_version')
						.append(
								'<tr><td>'
										+ t1
										+ '</td><td><button type="button"class="btn btn-primary top_handle"id="btn_modify_version_num">+</button></td><td><button type="button"class="btn btn-danger top_handle"id="btn_modify_delete_tr">删除</button></td></tr>');
			}
			$("#input_modify_version_name").val("");

		});
$('#tbody_modify_product_version').off();
//删除版本
$('#tbody_modify_product_version').on('click', '#btn_modify_delete_tr', function() {

var tr = $(this).parents("tr");
$(tr).remove();
});
//删除版本号
$('#tbody_modify_product_version').on('click', '#btn_modify_product_delete_num',
function() {
	var div = $(this).parent("div");
	$(div).remove();
});
//添加版本号
$('#tbody_modify_product_version')
.on(
		'click',
		'#btn_modify_version_num',
		function() {
			var t1 = $("#input_modify_version_num").val();
			if (t1.length > 0) {
				var td = $(this).parent("td");
				$(td)
						.append(
								'<div class="btn-group"><button name="version_num"class="btn btn-default">'
										+ t1
										+ '</button><button id="btn_modify_product_delete_num"class="btn btn-default">-</button></div>');
			}
					$("#input_modify_version_num").val("");
		});