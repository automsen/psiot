layui.config({
	base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
	
	var dataField = T.local.get("dataField");
	var equipId = dataField.equipId;
	$("#equipId").val(equipId);
	

	form.on('select(modelNetType)', function(data){
		var modelNetType = data.value;
		if(modelNetType == "0901" || modelNetType == "0902"){
	    	 $("#elec").empty();
			 var htmlStr = $("#elecTempl").html();
	 		  $("#elec").append(htmlStr);
	    }else if(modelNetType == "0903" || modelNetType == "0904"){
	    	$("#elec").empty();
			 var htmlStr = $("#elecTemp2").html();
	 		  $("#elec").append(htmlStr);
	    }else if(modelNetType == "0905"){
	    	$("#elec").empty();
			 var htmlStr = $("#elecTemp3").html();
	 		  $("#elec").append(htmlStr);
	    }else{
	    	$("#elec").empty();
	    }
	});  
	
	//监听提交
	form.on('submit(tijiao)', function(data){
		var p = data.field;
		var orgEquipment = JSON.stringify(p);
        T.reqAjax({btnId:"subBtn", method:"put", param:orgEquipment, url:"equip",contentType: "application/json",dataType: 'json',success:function(res){
        	layer.msg('修改成功');
        	data.form.reset();
        },error:function(res){
        	$.tBox.error(res.message);
        }})
		return false;
	});
	
	$("#s_form").loadToDom({url:"equip/"+equipId});
});