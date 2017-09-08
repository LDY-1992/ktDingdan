/**
 * 
 */

// 点击查询按钮
$("#btn_query_duizhang").click(function() {

	$("#a_get_excel").attr("disabled", true);
	$("#btn_query_duizhang").attr("disabled", true);
	$("#btn_query_duizhang").text("正在查询");
	$("#p_duizhang_warn").text("");

	var date1 = $("#input_duizhang_date1").val();
	var date2 = $("#input_duizhang_date2").val();
	// console.log(date1+date2+pay);

	if (date1.length <= 0) {
		$("#p_duizhang_warn").text("请选择开始日期");
		$("#btn_query_duizhang").attr("disabled", false);
		$("#btn_query_duizhang").text("查询");
		return 0;
	}

	if (date2.length <= 0) {
		$("#p_duizhang_warn").text("请选择结束日期");
		$("#btn_query_duizhang").attr("disabled", false);
		$("#btn_query_duizhang").text("查询");
		return 0;
	}

	if (date2 < date1) {
		$("#p_duizhang_warn").text("开始日期要早于结束日期");
		$("#btn_query_duizhang").attr("disabled", false);
		$("#btn_query_duizhang").text("查询");
		return 0;
	}
	get_duizhang_list(date1, date2);
});

// 获取订单数据
function get_duizhang_list(date1, date2) {
	$.ajax({
		type : "GET",
		url : "/kingcomDingDan/opDuizhang?date1=" + date1 + "&date2=" + date2,
		// data: my_param,
		dataType : "json",
		success : function(result) {
			$("#btn_query_duizhang").attr("disabled", false);
			$("#btn_query_duizhang").text("查询");
			if (result == "-1") {
				$("#p_duizhang_warn").text("查询出错");
			} else {
				$("#a_get_excel").attr("href", result.file_name);
				$("#a_get_excel").attr("disabled", false);

				var all = result.all;
				var sheet1 = result.sheet1;
				var sheet2 = result.sheet2;
				var sheet3 = result.sheet3;
				var sheet4 = result.sheet4;
				var sheet5 = result.sheet5;
				var sheet6 = result.sheet6;
				create_table_duizhang(all, sheet1, sheet2, sheet3, sheet4,sheet5,sheet6);
			}

		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#p_duizhang_warn").text("本地网络错误");
			$("#btn_query_duizhang").attr("disabled", false);
			$("#btn_query_duizhang").text("查询");
		}
	});
}

// 显示对账表格
function create_table_duizhang(all, sheet1, sheet2, sheet3, sheet4,sheet5,sheet6) {
	$('#table_duizhang').dataTable().fnDestroy();
	$('#table_duizhang').DataTable({
		data : all,
		ordering: false,
		columns : [ {
			"data" : "custom_cor"
		}, {
			"data" : "cdate"
		}, {
			"data" : "products"
		}, {
			"data" : "all_money"
		} ]
	});

	$('#table_duizhang_sheet1').dataTable().fnDestroy();
	$('#table_duizhang_sheet1').DataTable({
		data : sheet1,
		ordering: false,
		columns : [ {
			"data" : "custom_cor"
		}, {
			"data" : "cdate"
		}, {
			"data" : "products"
		}, {
			"data" : "all_money"
		} ]
	});

	$('#table_duizhang_sheet2').dataTable().fnDestroy();
	$('#table_duizhang_sheet2').DataTable({
		data : sheet2,
		ordering: false,
		columns : [ {
			"data" : "custom_cor"
		}, {
			"data" : "cdate"
		}, {
			"data" : "products"
		}, {
			"data" : "all_money"
		} ]
	});

	$('#table_duizhang_sheet3').dataTable().fnDestroy();
	$('#table_duizhang_sheet3').DataTable({
		data : sheet3,
		ordering: false,
		columns : [ {
			"data" : "custom_cor"
		}, {
			"data" : "cdate"
		}, {
			"data" : "products"
		}, {
			"data" : "all_money"
		} ]
	});

	$('#table_duizhang_sheet4').dataTable().fnDestroy();
	$('#table_duizhang_sheet4').DataTable({
		data : sheet4,
		ordering: false,
		columns : [ {
			"data" : "custom_cor"
		}, {
			"data" : "cdate"
		}, {
			"data" : "products"
		}, {
			"data" : "all_money"
		} ]
	});
	
	$('#table_duizhang_sheet5').dataTable().fnDestroy();
	$('#table_duizhang_sheet5').DataTable({
		data : sheet5,
		ordering: false,
		columns : [ {
			"data" : "custom_cor"
		}, {
			"data" : "cdate"
		}, {
			"data" : "products"
		}, {
			"data" : "all_money"
		} ]
	});
	
	$('#table_duizhang_sheet6').dataTable().fnDestroy();
	$('#table_duizhang_sheet6').DataTable({
		data : sheet6,
		ordering: false,
		columns : [ {
			"data" : "custom_cor"
		}, {
			"data" : "cdate"
		}, {
			"data" : "products"
		}, {
			"data" : "all_money"
		} ]
	});
}