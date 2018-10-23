layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form,T = layui.T,$ = layui.$,layer = layui.layer;
	var orgId = "1";
	var equipType = "0201";
	var meterAddr = "";

	selectMeterList(orgId,equipType);
//	var activePowerGauge = echarts.init(document.getElementById('activePowerGauge'));
//	var reactivePowerGauge = echarts.init(document.getElementById('reactivePowerGauge'));
//	var powerFactorGauge = echarts.init(document.getElementById('powerFactorGauge'));
//	var Pmax = 5;
	// 选择仪表类型
	$(document).on("click",".first-btn", function(e){
		var d = $(e.target).parent();
		if(d.hasClass("unusable")){
			$.tBox.error("No Data");
			return false;
		}
		$(".first-btn").removeClass("this");
		d.addClass("this");
		if(d.attr("id")=="elecBtn"){
			equipType = "0201";
		}else if(d.attr("id")=="waterBtn"){
			equipType = "0202";
		}else if(d.attr("id")=="gasBtn"){
			equipType = "0203";
		}
		meterAddr = "";
		selectMeterList(orgId,equipType);
		return false;
	});	

	// 选择单个仪表
	$(document).on("change","#meterSelect", function(){
		meterAddr = $("#meterSelect").val();
		selectMeterInfo(meterAddr,orgId,equipType);
		return false;
	});

    $(document).on("click","#refreshData", function(e){
    	selectReadLast(meterAddr);
    });
    // 下发读数命令
    $(document).on("click","#readData", function(e){
    	sendReadCmd(meterAddr);
    });
    
    // 开关——开
    $(document).on("click","#on", function(e){
    	sendOnCmd(meterAddr);
    });
    
    // 开关——关
    $(document).on("click","#off", function(e){
    	sendOffCmd(meterAddr);
    });
    
    // 下发读数命令
    function sendReadCmd(meterAddr){
		T.reqAjax(
        		{
        			method:"post", 
        			url:"/directive/realTimeRead/"+meterAddr,
        			success:function(res){
        				if (res.statusCode=="000000"){
        					$.tBox.success("read command has been sended，please refresh later");
        				}
        },error:function(res){
        	$.tBox.error(res.message);
        }});
    }
    
    // 
    function sendOnCmd(meterAddr){
		T.reqAjax(
        		{
        			method:"post", 
        			url:"/directive/openMeter/"+meterAddr,
        			success:function(res){
        				if (res.statusCode=="000000"){
        					$.tBox.success("on command has been sended，please refresh later");
        				}
        },error:function(res){
        	$.tBox.error(res.message);
        }});
    }
    
    // 
    function sendOffCmd(meterAddr){
		T.reqAjax(
        		{
        			method:"post", 
        			url:"/directive/closeMeter/"+meterAddr,
        			success:function(res){
        				if (res.statusCode=="000000"){
        					$.tBox.success("off command has been sended，please refresh later");
        				}
        },error:function(res){
        	$.tBox.error(res.message);
        }});
    }
	// 查询仪表列表
	function selectMeterList(orgId,equipType){
		var _this = $("#meterSelect");
		$(_this).empty();
		if (equipType=="0201"){
			$(".elec").css('display', '');
			$(".water").css('display', 'none');
			$(".gas").css('display', 'none');
		}else if (equipType=="0202"){
			$(".elec").css('display', 'none');
			$(".water").css('display', '');
			$(".gas").css('display', 'none');
		}else if (equipType=="0203"){
			$(".elec").css('display', 'none');
			$(".water").css('display', 'none');
			$(".gas").css('display', '');
		}
		$("#totalOrgEquipment").html("--");
		$(".meterInfo").html("--");
		$(".readValue").html("--");
		$(".readTime").html("--");
		T.reqAjax(
        		{
        			method:"get", 
        			url:"/equip/select?equipType="+equipType+"&orgId="+orgId,
        			success:function(res){
        				var data = res.data;
						if(data.length == 0){
							$(_this).append("<option value=''>none</option>");
							$("#btnDiv").html("");
						}else {
							for(var i=0; i<data.length; i++){
								$(_this).append("<option value="+data[i].commAddr+">"+data[i].equipName+"</option>");
							}
							meterAddr = data[0].commAddr;
							showMeterInfo(data[0]);
						}
						$("#totalOrgEquipment").html(data.length);
        			},error:function(res){
        				$(_this).append("<option value=''>none</option>");
        	        	$.tBox.error(res.message);
        	        }
        		});
	}
	
	// 查询单个仪表
	function selectMeterInfo(meterAddr,orgId,equipType){
		var url;
		url = "equip/select?orgId="+orgId+"&equipType="+equipType+"&commAddr="+meterAddr;
		$(".meterInfo").html("--");
		$(".readValue").html("--");
		$(".readTime").html("--");
		T.reqAjax(
        		{
        			method:"get", 
        			url:url,
        			success:function(res){
        				if (res.statusCode=="000000"){
        					var meterlist = res.data;
        					var meter = meterlist[0];
        					showMeterInfo(meter);
        				}
        },error:function(res){
        	$.tBox.error(res.message);
        }});
	}
	
	// 显示仪表信息
	function showMeterInfo(meter) {
	    $("#commAddr").html(meter.commAddr);
	    $("#modelName").html(meter.modelName);
	    var netType = "";
	    if (meter.netType == "0901"){
	    	netType = "RS485转以太网-I";
	    }else if (meter.netType == "0902"){
	    	netType = "RS485转以太网-II";
	    }else if (meter.netType == "0903"){
	    	netType = "RS485转GPRS-I";
	    }else if (meter.netType == "0904"){
	    	netType = "RS485转GPRS-II";
	    }else if (meter.netType == "0905"){
	    	netType = "LoRaWan";
	    }else if (meter.netType == "0906"){
	    	netType = "NB-IoT";
	    }else if (meter.netType == "0907"){
	    	netType = "ethernet";
	    }else if (meter.netType == "0908"){
	    	netType = "GPRS";
	    }else if (meter.netType == "0909"){
	    	netType = "NB-UDP";
	    }else if (meter.netType == "0910"){
	    	netType = "Wi-Fi";
	    }
	    $("#btnDiv").html("<button type='button' class='btn-off' id='readData'>read</button>"+"<button type='button' class='btn-off' id='refreshData'>refresh</button>");
	    $("#netType").html(netType);
	    if (meter.equipType == "0201") {
	        var elecMeterType = "";
	        if (meter.elecMeterType != null) {
	            if (meter.elecMeterType == "0401") {
	                elecMeterType = "Monophasic meter";
	            } else if (meter.elecMeterType == "0402") {
	                elecMeterType = "Three-phase meter";
	            }
	        }
	        $("#elecMeterType").html(elecMeterType);
	        $("#voltageRatio").html(meter.voltageRatio);
	        $("#currentRatio").html(meter.currentRatio);
	    }

	    var isRemoteControl = "";
	    if (meter.isRemoteControl != null) {
	        if (meter.isRemoteControl == 0) {
	            isRemoteControl = "No";
	        } else if (meter.isRemoteControl == 1) {
	            isRemoteControl = "Yes";
	        }
	    }
	    $("#isRemoteControl").html(isRemoteControl);

	    var isCostControl = "";
	    if (meter.isCostControl != null) {
	        if (meter.isCostControl == 0) {
	            isCostControl = "No";
	        } else if (meter.isCostControl == 1) {
	            isCostControl = "Yes";
	        }
	    }
	    $("#isCostControl").html(isCostControl);

	    selectReadLast(meter.commAddr);
	}
	
	// 查询最近读数
	function selectReadLast(meterAddr){
		T.reqAjax({
			method:"get",
			url:"readLast?meterAddr="+meterAddr,
			success:function(res){
				if (res.statusCode=="000000"){
					$.each(res.data,function(index,record){
						var dom=$(".readValue#"+record.itemCode);
						if (dom!=null){
							if(record.readValue!=null){
								dom.html(record.readValue);
							}
						}
						if(record.itemCode=="isOff"){
							if(record.readValue==0){
								$("#isOff").html("On"+"<button type='button' class='btn-off' id='off'>sw-Off</button>");
							}else if(record.readValue==1){
								$("#isOff").html("Off"+"<button type='button' class='btn-off' id='on'>sw-On</button>");
							}
						}
						var dom=$(".readTime#"+record.itemCode);
						if (dom!=null){
							if(record.readTime!=null){
								var time = new Date(record.readTime);
								dom.html(time.format("yyyy-MM-dd HH:mm:ss"));
							}
						}
					});
//					name='有功功率';
//					if(null==data.data.activep){
//						name+='无读数';
//					}
//					unit='kW';
//					activePowerGauge.setOption(drawGauge4(data.data.activep,name,unit,0,Pmax));
//					name='无功功率';
//					if(null==data.data.reactivep){
//						name+='无读数';
//					}
//					unit='kvar';
//					min=0;
//					max=20;
//					reactivePowerGauge.setOption(drawGauge5(data.data.reactivep,name,unit,min,max));
//					name='功率因数';
//					if(null==data.data.pf){
//						name+='无读数';
//					}
//					unit='';
//					powerFactorGauge.setOption(drawGauge6(data.data.pf,name,unit,0,1));
				}
				else {
					$.tBox.error("Failed Query");
				}
			},error:function(res){
	        	$.tBox.error(res.message);
	        }
		});
	}
//	//有功功率表盘
//	function drawGauge4(data,name,unit,min,max) {
//		return {
//		    tooltip : {
//		        formatter: "{b} : {c}"+unit
//		    },
//            pointer: {length: '10%', width: '1px', color: 'auto'},
//            axisLabel: {show: false, textStyle: {color: '#fff', fontSize: 7}},
//            series: [{
//            	radius: '75%',
//                min: min,
//                max: max,
//                splitNumber: 4,
//                axisLine: {
//                    lineStyle: {
//                        width: 12,
//                        color: [[0.66, '#91c7ae'], [0.83, '#dfcd55'], [1, '#c23531']]
//                    }
//                },
//                splitLine: {
//                    length: 15
//                },
//                type: 'gauge',
//                title:{
//                    offsetCenter:[0, '-120%'],
//                    fontWeight  :'bolder'
//                },
//                detail: {
//                    formatter: '{value}' + unit,
//                    //控制显示的结果的样式
//                    textStyle: {
//                        color: '#60839C',
//                        fontSize: 14
//                    },
//                    offsetCenter:[0, '80%'],
//                    fontWeight  :'bolder'
//                },
//                data: [{
//                    value: data,
//                    name: name,
//                    itemStyle: {borderWidth: '10px'}
//                }]
//            }]
//        };
//	}
//	//无功功率表盘
//	function drawGauge5(data,name,unit,min,max) {
//		return {
//		    tooltip : {
//		        formatter: "{b} : {c}"+unit
//		    },
//            pointer: {length: '10%', width: '1px', color: 'auto'},
//            axisLabel: {show: false, textStyle: {color: '#fff', fontSize: 7}},
//            series: [{
//            	radius: '75%',
//                min: min,
//                max: max,
//                splitNumber: 4,
//                axisLine: {
//                    lineStyle: {
//                        width: 12,
//                        color: [[0.66, '#91c7ae'], [0.83, '#dfcd55'], [1, '#c23531']]
//                    }
//                },
//                splitLine: {
//                    length: 15
//                },
//                type: 'gauge',
//                title:{
//                    offsetCenter:[0, '-120%'],
//                    fontWeight  :'bolder'
//                },
//                detail: {
//                    formatter: '{value}' + unit,
//                    //控制显示的结果的样式
//                    textStyle: {
//                        color: '#60839C',
//                        fontSize: 14
//                    },
//                    offsetCenter:[0, '80%'],
//                    fontWeight  :'bolder'
//                },
//                data: [{
//                    value: data,
//                    name: name,
//                    itemStyle: {borderWidth: '10px'}
//                }]
//            }]
//        };
//	}
//	//功率因数表盘
//	function drawGauge6(data,name,unit,min,max) {
//		return {
//		    tooltip : {
//		        formatter: "{b} : {c}"+unit
//		    },
//            pointer: {length: '10%', width: '1px', color: 'auto'},
//            axisLabel: {show: false, textStyle: {color: '#fff', fontSize: 7}},
//            series: [{
//            	radius: '75%',
//                min: min,
//                max: max,
//                splitNumber: 4,
//                axisLine: {
//                    lineStyle: {
//                        width: 12,
//                        color: [[0.75, '#c23531'], [0.9, '#dfcd55'], [1, '#91c7ae']]
//                    }
//                },
//                splitLine: {
//                    length: 15
//                },
//                type: 'gauge',
//                title:{
//                    offsetCenter:[0, '-120%'],
//                    fontWeight  :'bolder'
//                },
//                detail: {
//                    formatter: '{value}' + unit,
//                    //控制显示的结果的样式
//                    textStyle: {
//                        color: '#60839C',
//                        fontSize: 14
//                    },
//                    offsetCenter:[0, '80%'],
//                    fontWeight  :'bolder'
//                },
//                data: [{
//                    value: data,
//                    name: name,
//                    itemStyle: {borderWidth: '10px'}
//                }]
//            }]
//        };
//	}
});