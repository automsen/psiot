layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['element', 'jquery', 'form', 'layer', 'T', 'laydate','pageUtil', 'laytpl'], function() {
	var form = layui.form,
		T = layui.T,
		$ = layui.$,
		laydate = layui.laydate,
		laytpl= layui.laytpl,
		layer = layui.layer;
	var pageUtil = layui.pageUtil;
	var element = layui.element;
	
	laydate.render({ 
		elem: '#installationTime'
		,type: 'datetime'
	});
	
	var dataField = T.local.get("dataField");
	var equipId = dataField.equipId;
  	$("#equipId").val(equipId);
	
	//
	init();
	
	function init(){
		$("#s_form").loadToDom({
			url: "terminal/dtu/" + equipId,
			complete:function(_this, res){
				var data = res.data;
				if(data.installTime != "" && data.installTime != null){
					laydate.render({ 
						elem: '#installTime',
						type: 'datetime',
						value: new Date(data.installTime)
					});
				}else{
					laydate.render({ 
						elem: '#installTime',
						type: 'datetime'
					});
				}
				if(data.dteList.length > 0){
					var dteList = data.dteList;
					for(var i=0; i<dteList.length; i++){
						//dteList[i].manufacturer = layui.dict.getTextByCode("manufacturer",dteList[i].manufacturer);
						if(dteList[i].equipType == "150000"){
							setHotInfo(dteList[i]);
						}else if(dteList[i].equipType == "110000"){
							setElecInfo(dteList[i])
						}else{
							setWaterInfo(dteList[i])
						}
					}
				}
				var dtuWorkingMode = data.workingMode.split("");
				var dtuFunctionDigit = data.functionDigit.split("");
				$("#dtuWorkingMode0 option").each(function() {
				    if($(this).val() == dtuWorkingMode[0]) {
				        $(this).attr("selected","selected");
				    }else {
			        	$(this).removeAttr("selected")
			        }
			    })
				
				$("#dtuWorkingMode1 option").each(function() {
				    if($(this).val() == dtuWorkingMode[1]) {
				        $(this).attr("selected","selected");
				    }else {
			        	$(this).removeAttr("selected")
			        }
			    })
				
				$("#dtuFunctionDigit0 option").each(function() {
				    if($(this).val() == dtuFunctionDigit[0]) {
				        $(this).attr("selected","selected");
				    }else {
			        	$(this).removeAttr("selected")
			        }
			    })
				
				$("#dtuFunctionDigit1 option").each(function() {
				    if($(this).val() == dtuFunctionDigit[1]) {
				        $(this).attr("selected","selected");
				    }else {
			        	$(this).removeAttr("selected")
			        }
			    })
				form.render();
			}
		});
	}
	
	function setHotInfo(data){
		var tplhtml = $("#hot").html();
		laytpl(tplhtml).render(data, function(html){
    		$("#hotInfo").append(html);
    	});
	}
	
	function setElecInfo(data){
		var tplhtml = $("#elec").html();
		laytpl(tplhtml).render(data, function(html){
    		$("#elecInfo").append(html);
    	});
	}
	
	function setWaterInfo(data){
		var tplhtml = $("#water").html();
		laytpl(tplhtml).render(data, function(html){
    		$("#waterInfo").append(html);
    	});
	}
	
	//监听提交
	form.on('submit(tijiao)', function(data) {
		var p = data.field;
		p.workingMode = p.dtuWorkingMode0 + p.dtuWorkingMode1;
		p.functionDigit = p.dtuFunctionDigit0 + p.dtuFunctionDigit1;
		var orgEquipment = JSON.stringify(p);
		T.reqAjax({
			btnId: "subBtn",
			method: "put",
			param: orgEquipment,
			url: "terminal/dtu",
			contentType: "application/json",
			dataType: 'json',
			success: function(res) {
				layer.msg('修改成功');
				data.form.reset();
			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		})
		return false;
	});
});