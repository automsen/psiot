layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer', 'T', 'config', 'dict'], function() {
	var form = layui.form,
		T = layui.T,
		$ = layui.$,
		layer = layui.layer;
	var dict = layui.dict;
	dict.init();
	var config = layui.config;
	var orgId;
	var equipTypeCode = "150000";
	var equipNumber = "";
	selectOrgList();
	setInterval(function() {
		selectReadLast(equipNumber);
	}, 30000);
	// 查询机构列表
	function selectOrgList() {
		var _this = $("#orgSelect");
		T.reqAjax({
			method: "get",
			url: config.servicePath + "/org/all",
			success: function(res) {
				var data = res.data;
				if(data.length == 0) {
					$(_this).append("<option value=''>无</option>");
					$("#readData").css('display', 'none');
					$("#refreshData").css('display', 'none');
					$.tBox.error(res.message);
				} else {
					for(var i = 0; i < data.length; i++) {
						$(_this).append("<option value=" + data[i].orgId + ">" + data[i].orgName + "</option>");
					}
					orgId = data[0].orgId;
					selectMeterList(orgId, equipTypeCode);
				}
			},
			error: function(res) {
				$(_this).append("<option value=''>无</option>");
				//$.tBox.error(res.message);
			}
		});
	}
	$(document).on("click", ".con_icon1", function(e) {
		var d = $(e.target).parent().parent();
		if(d.hasClass("unusable")) {
			$.tBox.error("暂无数据");
			return false;
		}
		$(".con_icon1").removeClass("this");
		d.addClass("this");
		if(d.attr("id") == "hotBtn") { //选择热力泵
			equipTypeCode = "150000";
		}
		equipNumber = "";
		selectMeterList(orgId, equipTypeCode);
		return false;
	});
	/*$(document).on("change","#orgSelect", function(){
		orgId = $("#orgSelect").val();
		selectMeterList(orgId,equipTypeCode);
		return false;
	});*/

	// 选择单个仪表
	$(document).on("change", "#meterSelect", function() {
		equipNumber = $("#meterSelect").val();
		selectMeterInfo(equipNumber, orgId, equipTypeCode);
		return false;
	});

	form.on('select(jigou)', function(data) {
		orgId = $("#orgSelect").val();
		selectMeterList(orgId, equipTypeCode);
		return false;
	});

	form.on('select(meterSelect)', function(data) {
		equipNumber = $("#meterSelect").val();
		selectMeterInfo(equipNumber, orgId, equipTypeCode);
		return false;
	});

	// 刷新读数
	$(document).on("click", "#refreshData", function(e) {
		selectReadLast(equipNumber);
	});

	// 下发读数命令
	$(document).on("click", "#readData", function(e) {
		layer.confirm('是否下发读数指令?', {
			title: '提醒'
		}, function(index) {
			//do something
			sendReadCmd(equipNumber);
			layer.close(index);
		});
	});

	// 开关——开
	$(document).on("click", "#on", function(e) {
		layer.confirm('是否接通?', {
			title: '提醒'
		}, function(index) {
			//do something
			sendOnCmd(equipNumber);
			layer.close(index);
		});
	});

	// 开关——关
	$(document).on("click", "#off", function(e) {
		layer.confirm('是否断开?', {
			title: '提醒'
		}, function(index) {
			//do something
			sendOffCmd(equipNumber);
			layer.close(index);
		});
	});

	$("#elecDataExport").click(function() {
		window.location.href = config.serviceData + "/export/term/last?orgId=" + orgId + "&meterType=110000";
	})

	$("#waterDataExport").click(function() {
		window.location.href = config.serviceData + "/export/term/last?orgId=" + orgId + "&meterType=120000";
	})

	// 下发读数命令
	function sendReadCmd(equipNumber) {
		T.reqAjax({
			method: "post",
			url: config.servicePath + "/directive/read/" + equipNumber,
			param: JSON.stringify({
				orgId: orgId
			}),
			success: function(res) {
				if(res.statusCode == "000000") {
					$.tBox.success("读数指令已下发，请稍后刷新数据");
				}
			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		});
	}

	// 
	function sendOnCmd(equipNumber) {
		T.reqAjax({
			method: "post",
			url: config.servicePath + "/directive/open/" + equipNumber,
			param: JSON.stringify({
				orgId: orgId
			}),
			success: function(res) {
				if(res.statusCode == "000000") {
					$.tBox.success("连通指令已下发，请稍后刷新结果");
				}
			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		});
	}

	// 
	function sendOffCmd(equipNumber) {
		T.reqAjax({
			method: "post",
			url: config.servicePath + "/directive/close/" + equipNumber,
			param: JSON.stringify({
				orgId: orgId
			}),
			success: function(res) {
				if(res.statusCode == "000000") {
					$.tBox.success("断开指令已下发，请稍后刷新结果");
				}
			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		});
	}

	// 查询仪表列表
	function selectMeterList(orgId, equipTypeCode) {
		var _this = $("#meterSelect");
		$(_this).empty();
		$("#tableInfo").css('display', '');
		$("#hotInfo").empty();
		$("#hotInfo2").empty();
		$("#elecDataExport").css('display', '');
		$("#waterDataExport").css('display', '');
		$("#readData").css('display', '');
		$("#refreshData").css('display', '');
		if(equipTypeCode == "110000") {
			$(".elec").css('display', '');
			$(".water").css('display', 'none');
			$(".gas").css('display', 'none');
			$(".hot").css('display', 'none');
		} else if(equipTypeCode == "120000") {
			$(".elec").css('display', 'none');
			$(".water").css('display', '');
			$(".gas").css('display', 'none');
			$(".hot").css('display', 'none');
		} else if(equipTypeCode == "130000") {
			$(".elec").css('display', 'none');
			$(".water").css('display', 'none');
			$(".gas").css('display', '');
			$(".hot").css('display', 'none');
		} else if(equipTypeCode == "150000") {
			$(".elec").css('display', 'none');
			$(".water").css('display', 'none');
			$("#tableInfo").css('display', 'none');
			$(".gas").css('display', 'none');
			$(".hot").css('display', '');
			var html = $("#hotTempl").html();
			var html2 = $("#hotTempl2").html();
			$("#hotInfo").append(html);
			$("#hotInfo2").append(html2);
			$("#elecDataExport").css('display', 'none');
			$("#waterDataExport").css('display', 'none');
		}
		$("#totalOrgEquipment").html("--");
		$(".meterInfo").html("--");
		$(".readValue").html("--");
		$(".readTime").html("--");

		T.reqAjax({
			method: "get",
			url: config.servicePath + "/terminal/orgId?equipTypeCode=" + equipTypeCode + "&orgId=" + orgId,
			success: function(res) {
				var data = res.data;
				if(data.length == 0) {
					$(_this).append("<option value=''>无</option>");
					$("#readData").css('display', 'none');
					$("#refreshData").css('display', 'none');
				} else {
					$(_this).append("<option value=''>直接选择或搜索选择</option>");
					for(var i = 0; i < data.length; i++) {
						$(_this).append("<option value=" + data[i].equipNumber + ">" + data[i].equipName + "</option>");
					}
					equipNumber = data[0].equipNumber;
					$(_this).val(equipNumber)
					selectMeterInfo(equipNumber, orgId, equipTypeCode);

					//showMeterInfo(data[0]);
				}
				$("#totalOrgEquipment").html(data.length);
				form.render('select');
			},
			error: function(res) {
				$(_this).append("<option value=''>无</option>");
				//$.tBox.error(res.message);
			}
		});

	}

	// 查询单个仪表
	function selectMeterInfo(equipNumber, orgId, equipTypeCode) {

		if(equipNumber == null || equipNumber == "") {
			return;
		}

		var url;
		url = config.servicePath + "terminal/all?orgId=" + orgId + "&equipTypeCode=" + equipTypeCode + "&equipNumber=" + equipNumber;
		$(".meterInfo").html("--");
		$(".readValue").html("--");
		$(".readTime").html("--");
		T.reqAjax({
			method: "get",
			url: url,
			success: function(res) {
				if(res.statusCode == "000000") {
					if(equipTypeCode == "150000"){
						var data = res.data[0];
						if(data != null){
							if(data.workingMode == "00"){
								data.workingMode = "禁止心跳,禁止上报";
							}else if(data.workingMode == "10"){
								data.workingMode = "开启心跳,禁止上报";
							}else if(data.workingMode == "01"){
								data.workingMode = "禁止心跳,开启抄读功能";
							}else if(data.workingMode == "11"){
								data.workingMode = "开启心跳,开启抄读功能";
							}
							if(data.functionDigit == "00"){
								data.functionDigit = "禁止ADR,LoRaWAN速率";
							}else if(data.functionDigit == "10"){
								data.functionDigit = "开启ADR,LoRaWAN速率";
							}else if(data.functionDigit == "01"){
								data.functionDigit = "禁止ADR,固定速率";
							}else if(data.functionDigit == "11"){
								data.functionDigit = "开启ADR,固定速率";
							}
							$("#s-form").setValueToDom(data);
							showMeterInfo(data);
						}
					}else{
						var meterlist = res.data;
						var meter = meterlist[0];
						showMeterInfo(meter);
						form.render('select');
					}
				}
			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		});
	}
	
	// 显示仪表信息
	function showMeterInfo(meter) {
		$("#equipNumber").html(meter.equipNumber);
		$("#modelName").html(meter.modelName);
		var netTypeCode = "";
		netTypeCode = dict.getTextByCode("net_type", meter.netTypeCode);
		//	    <a class="layui-btn layui-btn-danger layui-btn-mini T-click-prompt" T-options="url:'application/{{item.appId}}',method:'DELETE',msg:'确认删除?'">删除</a>	
		//	    $("#btnDiv").html("<button type='button' class='btn-off' id='readData'>读数</button>"+"<button type='button' class='btn-off' id='refreshData'>刷新</button>");
		$("#netTypeCode").html(netTypeCode);
		$("#orgTime").html(new Date(meter.createTime).format("yyyy-MM-dd HH:mm:ss"))
		if(meter.equipTypeCode == "110000") {
			var elecMeterTypeCode = "";
			if(meter.elecMeterTypeCode != null) {
				elecMeterTypeCode = dict.getTextByCode("elec_meter_type", meter.elecMeterTypeCode);
			}
			$("#elecMeterTypeCode").html(elecMeterTypeCode);
			$("#elecPt").html(meter.elecPt);
			$("#elecCt").html(meter.elecCt);
		}

		var isRemoteControl = "";
		if(meter.isRemoteControl != null) {
			if(meter.isRemoteControl == 0) {
				isRemoteControl = "不支持";
			} else if(meter.isRemoteControl == 1) {
				isRemoteControl = "支持";
			}
		}
		$("#isRemoteControl").html(isRemoteControl);

		var isCostControl = "";
		if(meter.isCostControl != null) {
			if(meter.isCostControl == 0) {
				isCostControl = "不支持";
			} else if(meter.isCostControl == 1) {
				isCostControl = "支持";
			}
		}
		$("#isCostControl").html(isCostControl);

		selectReadLast(meter.equipNumber);
	}

	// 查询最近读数
	function selectReadLast(equipNumber) {
		T.reqAjax({
			method: "get",
			url: config.serviceData + "/read/last/" + equipNumber,
			success: function(res) {
				if(res.statusCode == "000000") {

					var data = res.data;

					$(".readTime").each(function() {
						$(this).text(new Date(data.rTm).format("yyyy-MM-dd HH:mm:ss"))
					})

					for(var name in data) {

						if($("#" + name).length == 0) {
							continue;
						}
						if(name == "isOff") {
							if(data[name] == 0) {
								$("#" + name).html("通" + "<button type='button' class='btn-off' id='off'>断开</button>")
							} else if(data[name] == 1) {
								$("#" + name).html("断" + "<button type='button' class='btn-off' id='on'>接通</button>")
							}
						} else {
							$("#" + name).html(data[name])
							$("#" + name + "_two").html(data[name])
							$("#" + name + "_thrid").html(data[name])
							$("#" + name + "_si").html(data[name])
						}
					}

				} else {
					$.tBox.error("查询失败");
				}
			},
			error: function(res) {
				//$.tBox.error(res.message);
			}
		});
	}
});