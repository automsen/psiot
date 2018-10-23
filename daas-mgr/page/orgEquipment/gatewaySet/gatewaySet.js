
layui.config({
	base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
	//能源类型
	var equipType = "0201";
	//当前选中网关
	var thisGateway = "";
	//下发页面选中仪表
	meterAddrList = "";
	
	//下发仪表关联网关读取设置命令id
	var commandMeterId = "";
	//下发网关命令项读取设置命令
	var commandId = "";
	
	$(document).on("click","#searchGateway",function(){
		takePageData();
	});
	
	//网关模糊搜索
	$(document).on("click","#searchGatewayByAddr",function(){
		var orgEquipmentExpand = {};
		var commAddr = $("#equipNetAddr").val();
		orgEquipmentExpand.commAddr = commAddr;
		takePageData(orgEquipmentExpand);
	});
	
	//网关下仪表模糊搜索
	$(document).on("click","#searchMeterByNetAdd",function(){
		var orgEquipmentExpand = {};
		var commAddr = $("#equipMeterAddr").val();
		orgEquipmentExpand.commAddr = commAddr;
		orgEquipmentExpand.netEquipAddr = thisGateway;
		//takePageData(orgEquipmentExpand);
		pageAjax2(orgEquipmentExpand);
	});
	
	//点击网关事件
	$(document).on("click", ".demo1", function(){
		$("#data").empty();
		var netEquipAddr = $(this).text();
		//点击选中事件
		$(".demo1").removeClass("layui-bg-blue");
		$(this).addClass("layui-bg-blue");
		thisGateway = netEquipAddr;
		commAddrAjax(netEquipAddr);
	});
	
	//下发仪表地址事件
	$(document).on("click", "#issueMeterAddr", function(){
		if(thisGateway != "" && thisGateway != null){
			var commAddrs = "";
			for(var i = 0; i < meterAddrList.length; i++){
				commAddrs += meterAddrList[i].commAddr+",";
			}
			commAddrs = commAddrs.substring(0,commAddrs.length-1);
			if(commAddrs == ""){
				$.tBox.error("该网关没有下联仪表！")
				return false;
			}
			T.reqAjax({
				method:"post", 
				param:{"netEquipAddr":thisGateway, "commAddrs":commAddrs}, 
				url:"directive/setMeter",
				success:function(res){
					var data = res.data;
					if(res.statusCode == "000000"){
						$.tBox.error("指令已下发！")
						commandMeterId = data.cmdId;
					}else{
						$.tBox.error("下发失败！")
					}
		        },error:function(res){
		        	if(res.message == null){
		        		$.tBox.error("系统异常");
		        	}else{
		        		$.tBox.error(res.message);
		        	}
		        	
		        }
			});
		}else{
			$.tBox.error("请选择网关");
			return false;
		}
		
	});
	
	//读取下联仪表信息
	$(document).on("click", "#readGatewayAddr", function(){
		if(thisGateway != "" && thisGateway != null){
			T.reqAjax({
				method:"get", 
				param:{"netEquipAddr":thisGateway}, 
				url:"directive/getMeter",
				success:function(res){
					var data = res.data;
					if(res.statusCode == "000000"){
						getMeterForCmdId(data.cmdId);
						commandMeterId = data.cmdId;
					}else{
						$.tBox.error("读取失败！")
					}
		        },error:function(res){
		        	if(res.message == null){
		        		$.tBox.error("系统异常");
		        	}else{
		        		$.tBox.error(res.message);
		        	}
		        }
			});
		}else{
			$.tBox.error("请选择网关");
			return false;
		}
		
	});
	
	//读取下发命令信息
	$(document).on("click", "#readData", function(){
		if(thisGateway != "" && thisGateway != null){
			T.reqAjax({
				method:"get", 
				param:{"netEquipAddr":thisGateway}, 
				url:"directive/dataItem",
				success:function(res){
					var data = res.data;
					if(res.statusCode == "000000"){
						getCmdListForCmdId(data.cmdId);
						commandId = data.cmdId;
					}else{
						$.tBox.error("读取失败！")
					}
		        },error:function(res){
		        	if(res.message == null){
		        		$.tBox.error("系统异常");
		        	}else{
		        		$.tBox.error(res.message);
		        	}
		        }
			});
		}else{
			$.tBox.error("请选择网关");
			return false;
		}
	});
	
	//下发选中命令项
	$(document).on("click","#issueData",function(){
		if(thisGateway != "" && thisGateway != null){
			var chkList = $("input[name]:checked");
			var powerdata = "";
			for(var i = 0; i < chkList.length; i++){
				var chk = chkList[i].id;
				powerdata += chk+",";
			}
			powerdata = powerdata.substring(0, powerdata.length-1);
			/*var jsonStr = {};
			jsonStr.netEquipAddr = thisGateway;
			jsonStr.powerdata = powerdata;
			jsonStr = JSON.stringify(jsonStr);*/
			T.reqAjax({
				method:"post", 
				param:null, 
				url:"directive/dataItem?netEquipAddr="+thisGateway+"&powerdata="+powerdata,
				success:function(res){
					var data = res.data;
					if(res.statusCode == "000000"){
						$.tBox.error("数据项已下发！")
						commandId = data.cmdId
					}else{
						$.tBox.error("下发失败！")
					}
		        },error:function(res){
		        	if(res.message == null){
		        		$.tBox.error("系统异常");
		        	}else{
		        		$.tBox.error(res.message);
		        	}
		        }
			});
		}else{
			$.tBox.error("请选择网关");
			return false;
		}
	})
	
	$(document).on("click","#issueRate",function(){
		var rateTime = $("#rateData").val();
		if(rateTime != ""){
			
		}else{
			
		}
		$.tBox.error("该功能暂未开放，敬请关注");
	});
	
	//左页面下发读取结果查询按钮
	$(document).on("click","#searchMeterResult",function(){
		isSuccessMeter();
	});
	
	//右页面下发读取结果查询按钮
	$(document).on("click","#searchCmdResult",function(){
		isSuccessCmd();
	});
	
	form.on('select(terminalType)', function(data){
		equipType = data.value;
		if(equipType == "0201"){
			$("#data").empty();
	    	$("#elec").empty();
			var htmlStr = $("#elecTempl").html();
	 		$("#elec").append(htmlStr);
			elecMeterType();
	    }else{
	    	$("#elec").empty();
	    	$("#data").empty();
	 		takePageData();
	    }
	    
	}); 
	
	//判断右边命令项下发指令是否成功
	function isSuccessMeter(){
		if(commandMeterId == ""){
			$.tBox.error("请先下发指令");
			return false;
		}
		T.reqAjax({
			method:"get", 
			param:{"cmdId":commandMeterId}, 
			url:"cmd/all",
			success:function(res){
				var data = res.data;
				data = data[0];
				if(data.reason == "000000"){
					$.tBox.error("下发成功！");
					getMeterForCmdId(commandMeterId);
				}else if(data.reason == null){
					$.tBox.error("下发中，请稍后再试")
				}else{
					$.tBox.error("下发失败！")
				}
	        },error:function(res){
	        	if(res.message == null){
	        		$.tBox.error("系统异常");
	        	}else{
	        		$.tBox.error(res.message);
	        	}
	        }
		});
	}
	
	//判断右边命令项下发指令是否成功
	function isSuccessCmd(){
		if(commandId == ""){
			$.tBox.error("请先下发指令");
			return false;
		}
		T.reqAjax({
			method:"get", 
			param:{"cmdId":commandId}, 
			url:"cmd/all",
			success:function(res){
				var data = res.data;
				data = data[0];
				if(data.reason == "000000"){
					$.tBox.error("下发成功！");
					getCmdListForCmdId(commandId);
				}else if(data.reason == null){
					$.tBox.error("下发中，请稍后再试")
				}else{
					$.tBox.error("下发失败！")
				}
	        },error:function(res){
	        	if(res.message == null){
	        		$.tBox.error("系统异常");
	        	}else{
	        		$.tBox.error(res.message);
	        	}
	        }
		});
	}
	
	//根据cmdId请求获取关联网关的数据库和仪表返回集联的所有仪表
	function getMeterForCmdId(cmdId){
		T.reqAjax({
			method:"get", 
			param:{"cmdId":cmdId}, 
			url:"cmd/all",
			success:function(res){
				var data = res.data;
				data = data[0];
				if(res.statusCode == "000000"){
					flowGatWay(data);
					$.tBox.error("读取命令已下发！")
				}else{
					$.tBox.error("读取失败！")
				}
	        },error:function(res){
	        	if(res.message == null){
	        		$.tBox.error("系统异常");
	        	}else{
	        		$.tBox.error(res.message);
	        	}
	        }
		});
	}
	
	//根据cmdId请求获取下联命令项
	function getCmdListForCmdId(cmdId){
		T.reqAjax({
			method:"get", 
			param:{"cmdId":cmdId}, 
			url:"cmd/all",
			success:function(res){
				var data = res.data;
				data = data[0];
				if(res.statusCode == "000000"){
					readData(data);
					$.tBox.error("读取命令已下发！")
				}else{
					$.tBox.error("读取失败！")
				}
	        },error:function(res){
	        	if(res.message == null){
	        		$.tBox.error("系统异常");
	        	}else{
	        		$.tBox.error(res.message);
	        	}
	        }
		});
	}
	
	//加载第一个机构下拉框后再加载下一个下拉保证能获取到页面数据
	$("#orgId").loadSelect({
		url:'org/all',
		value:'orgId',
		name:'orgName',
		isShowSel:false, 
		success:elecMeterType
	});
	
	function elecMeterType(){
		$("#elecMeterType").loadSelect({
			url:'dict/all?dictType=elec_meter_type',
			value:'dictCode',
			name:'dictName',
			isShowSel:false, 
			success:callBack
		})
	}
	
	function callBack(){
		form.render();
		takePageData();
	}
	
	//获取页面参数
	function takePageData(orgEquipmentExpand){
		if(orgEquipmentExpand == undefined){
			orgEquipmentExpand = {};
		}
		//页面实体
		orgEquipmentExpand.orgId = $("#orgId").val();
		//orgEquipmentExpand.
		var equipType = $("#equipType").val();
		var elecMeterType = $("#elecMeterType").val();
		//0101:终端设备;0102:集抄设备;
		orgEquipmentExpand.equipCateg = "0102";
		//equipType值0201：电表；0202：水表;0203:燃气表;0204:热量表;
		if(equipType == "0201"){
			orgEquipmentExpand.chirdEquipType = elecMeterType;
		}else{
			orgEquipmentExpand.chirdEquipType = equipType;
		}
		pageAjax(orgEquipmentExpand);
	}
	
	//页面请求数据ajax
	function pageAjax(orgEquipmentExpand){
		T.reqAjax({
			//btnId:"subBtn", 
			method:"get", 
			param:orgEquipmentExpand, 
			url:"equip/all",
			success:function(res){
				var data = res.data;
				flowGatWay(data);
	        },error:function(res){
	        	if(res.message == null){
	        		$.tBox.error("系统异常");
	        	}else{
	        		$.tBox.error(res.message);
	        	}
	        }
		});
	}
	
	//页面请求数据ajax
	function pageAjax2(orgEquipmentExpand){
		T.reqAjax({
			//btnId:"subBtn", 
			method:"get", 
			param:orgEquipmentExpand, 
			url:"equip/all",
			success:function(res){
				var data = res.data;
				flow(data);
	        },error:function(res){
	        	if(res.message == null){
	        		$.tBox.error("系统异常");
	        	}else{
	        		$.tBox.error(res.message);
	        	}
	        }
		});
	}
	
	//列表加载开始+++++++++++++++++++++++++
	function flowGatWay(data){
		$("#LAY_demo1").empty();
		layui.use('flow', function(){
			var flow = layui.flow;
			//网关加载开始
			flow.load({
				elem: '#LAY_demo1' //流加载容器
				,scrollElem: '#LAY_demo1' //滚动条所在元素，一般不用填，此处只是演示需要。
				,done: function(page, next){ //执行下一页的回调
					//模拟数据插入
					setTimeout(function(){
						var dataCommAddr = "";
						var lis = [];
						if(data.length > 0){
							if(data[0].cmdId != null && data[0].cmdId != undefined && data[0].cmdId != ""){//非null表示返回读取返回的参数
							 	var returnValue = data.returnValue;
								if(returnValue != null && returnValue != "" && returnValue != undefined){
									var returnValueList = JSON.parse(returnValue);
									for(var i = 0; i < returnValueList.length; i++){
								    	if(i==0){
								    		lis.push("<li class='layui-icon demo1 layui-bg-blue' style='color: #1E9FFF;cursor: pointer;'>"+ returnValueList[i].commAddr + "</li>")
								    	}else{
								    		lis.push("<li class='layui-icon demo1' style='color: #1E9FFF;cursor: pointer;'>"+ returnValueList[i].commAddr + "</li>")
								    	}
									};
									dataCommAddr = data[0].commAddr;
								}
							}else{//如果为null表示返回数据库参数
								for(var i = 0; i < data.length; i++){
									if(i==0){
							    		lis.push("<li class='layui-icon demo1 layui-bg-blue' style='color: #1E9FFF;cursor: pointer;'>"+ data[i].commAddr + "</li>")
							    	}else{
							    		lis.push("<li class='layui-icon demo1' style='color: #1E9FFF;cursor: pointer;'>"+ data[i].commAddr + "</li>")
							    	}
								};
								//获取第一个参数
								dataCommAddr = data[0].commAddr;
							}
						}
						commAddrAjax(dataCommAddr);
						if(dataCommAddr != ""){
							thisGateway = dataCommAddr;
						}
						var mun = Math.ceil(data.length/20);
						//执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
						//pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
						next(lis.join(''), page < mun); //假设总页数为 10
		      		});
				}
				,end:"没有更多设备"
	  		});
		});
	}		
	
	//点击网关触发事件逻辑
	function commAddrAjax(netEquipAddr){
		if(netEquipAddr != "" && netEquipAddr != null){
			var orgEquipmentExpand = {};
			orgEquipmentExpand.netEquipAddr = netEquipAddr;
			T.reqAjax({
				method:"get", 
				param:orgEquipmentExpand, 
				url:"equip/all",
				success:function(res){
					var data = res.data;
					flow(data);
					readData(netEquipAddr);
		        },error:function(res){
		        	if(res.message == null){
		        		$.tBox.error("系统异常");
		        	}else{
		        		$.tBox.error(res.message);
		        	}
		        }
			});
		}else{
			flow("");
			readData("");
		}
	}
	
	function flow(data){
		$("#LAY_demo2").empty();
		layui.use('flow', function(){
			var flow = layui.flow;
			//仪表加载开始
			flow.load({
				elem: '#LAY_demo2' //流加载容器
				,scrollElem: '#LAY_demo2' //滚动条所在元素，一般不用填，此处只是演示需要。
				,done: function(page, next){ //执行下一页的回调
					//模拟数据插入
					setTimeout(function(){
						var lis = [];
						if(data.cmdId != null && data.cmdId != undefined && data.cmdId != ""){
							//根据读取返回数据进行数据库和仪表返回数据进行对比然后返回数据到页面，数据项进行颜色区分
							for(var i = 0; i < data.length; i++){
								var status = data[i].childStatus;
								if(status == "0"){//0：已配置未下发
									lis.push("<li class='layui-bg-orange'><input name='' title='"+data[i].commAddr+"' type='checkbox' lay-skin='primary'></div></li>")
								}else if(status == "1"){//1：已配置已下发
									lis.push("<li class='layui-bg-green'><input name='' title='"+data[i].commAddr+"' type='checkbox' lay-skin='primary'></div></li>")
								}else{//2：未配置已下发	
									lis.push("<li class='layui-bg-red'><input name='' title='"+data[i].commAddr+"' type='checkbox' lay-skin='primary'></div></li>")
								}
					    	}
						}else{
							for(var i = 0; i < data.length; i++){
								lis.push("<li><input name='' title='"+data[i].commAddr+"' type='checkbox' checked lay-skin='primary'></div></li>")
					    	}
						}
					    var mun = Math.ceil(data.length/20);
						//执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
						//pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
						next(lis.join(''), page < mun); //假设总页数为 10
						form.render();
	      			});
	    		}
	    		,end:"没有更多设备"
	  		});
		});
	}
	
	//右边栏的数据加载
	function readData(data){
		$("#data").empty();
		if(data.cmdId != undefined && data.cmdId != ""){//数据库和仪表返回的并联项
			$("#activeGateway").text(data.meterAddr);
			if(equipType == "0201"){
				var elecTempl2 = $("#elecTempl2").html();
		    	$("#data").append(elecTempl2);
			}else{
				var waterTempl = $("#waterTemp").html();
		    	$("#data").append(waterTempl);
			}
			var returnValue = data.returnValue;
			var reason = data.reason;
			if(reason != "000000"){
				$.tBox.error("操作失败");
			}
			if(returnValue != null && returnValue != "" && returnValue != undefined){
				var returnValueList = JSON.parse(returnValue);
				for(var i = 0; i < returnValueList.length; i++){
					var returnValueObj = returnValueList[i];
					if(returnValueObj.childStatus == "0"){
						$("#"+returnValueObj.dataMarker).attr("checked",true);
						$("#"+returnValueObj.dataMarker).parent().find("span").addClass("layui-bg-orange");
					}else if(returnValueObj.childStatus == "1"){
						$("#"+returnValueObj.dataMarker).attr("checked",true);
						$("#"+returnValueObj.dataMarker).parent().find("span").addClass("layui-bg-green");
					}else{
						$("#"+returnValueObj.dataMarker).attr("checked",true);
						$("#"+returnValueObj.dataMarker).parent().find("span").addClass("layui-bg-red");
					}
				}
			}
			form.render();
		}else{//仅为数据库查出的数据项
			if(data != "" && data != undefined){
				$("#activeGateway").text(data);
				T.reqAjax({
					btnId:"subBtn", 
					method:"get", 
					param:{"commAddr":data}, 
					url:"insGroup/all",
					success:function(res){
						var data = res.data;
						if(equipType == "0201"){
							var elecTempl2 = $("#elecTempl2").html();
					    	$("#data").append(elecTempl2);
					    	for(var i=0; i < data.length; i++){
								$("#"+data[i].dataMarker).attr("checked",true);
							}
					    	form.render();
						}else{
							var waterTempl = $("#waterTemp").html();
					    	$("#data").append(waterTempl);
					    	for(var i=0; i < data.length; i++){
								$("#"+data[i].dataMarker).attr("checked",true);
							}
					    	form.render();
						}
			        },error:function(res){
			        	if(res.message == null){
			        		$.tBox.error("系统异常");
			        	}else{
			        		$.tBox.error(res.message);
			        	}
			        }
		        });
			}else{
				$("#activeGateway").text(data);
				if(equipType == "0201"){
					var elecTempl2 = $("#elecTempl2").html();
			    	$("#data").append(elecTempl2);
				}else{
					var waterTempl = $("#waterTemp").html();
			    	$("#data").append(waterTempl);
				}
				form.render();
			}
			
		}
	}
	
	//复选框监听事件；
	form.on('checkbox(radio)', function(data){
		var domData = data.elem;
		var id = domData.id;
		/*var arr = ["00000000","0000FF00"];
		if(id != null && id != undefined && id != ""){
			if(arr.indexOf(id) > -1){
				$("#"+id).attr("checked",true);
			}
		}*/
		if(id == "00000000"){
			$("#0000FF00").attr("checked",false);
		}else if(id == "0000FF00"){
			$("#00000000").attr("checked",false);
		}
		if(id == "00010000"){
			$("#0001FF00").attr("checked",false);
		}else if(id == "0001FF00"){
			$("#00010000").attr("checked",false);
		}
		if(id == "00020000"){
			$("#0002FF00").attr("checked",false);
		}else if(id == "0002FF00"){
			$("#00020000").attr("checked",false);
		}
		form.render();
	});
});

