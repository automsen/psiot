layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
	
	var dataField = T.local.get("dataField");
  	var equipId = dataField.equipId;
  	var equipType = dataField.equipType;
  
  	$("#equipId").val(equipId);

  	if(equipType == "0201")
  		{
  		twoEquip();
  		}
  	  	/*freshForm(equipType);*/

  	form.on('select(equipType)', function(data){
		var equipType = data.value;
		freshForm(equipType);
	});  
	
	function freshForm(equipType){
		if(equipType == "0201"){
	    	 $("#elec").empty();
			 var htmlStr = $("#elecTempl").html();
	 		  $("#elec").append(htmlStr);
	 		   //下拉框
				var t_select = $(".elecMeterType-select");
				if(t_select.length > 0){
					t_select.each(function(){
						//T.js方法，加载下拉框数据
						$(this).loadSelect({complete:function(obj, res){
							form.render();
						},error:function(obj, res){
							
						}});
					})
				}
	    }else{
	    	$("#elec").empty();
	    }
	}
	
	function twoEquip(){		 
	    	 $("#elec").empty();
			 var htmlStr = $("#elecTempl").html();
	 		  $("#elec").append(htmlStr);
	 		   //下拉框
				var t_select = $(".elecMeterType-select");
				if(t_select.length > 0){
					t_select.each(function(){
						//T.js方法，加载下拉框数据
						$(this).loadSelect({complete:function(obj, res){
							form.render();
						},error:function(obj, res){
							
						}});
					})
				}
			
				
              
	}
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