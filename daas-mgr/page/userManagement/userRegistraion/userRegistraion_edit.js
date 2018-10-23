layui.config({
	base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
	
	debugger;
	var dataField = T.local.get("dataField");
  	var userId = dataField.userId;
  	$("#userId").val(userId);
	
	 //手机、邮箱校验规则   
	  form.verify({   
	        phone: [/^1[3|4|5|7|8]\d{9}$/, '手机必须11位，只能是数字！']  
	        ,email: [/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']  
	  }); 
	
	
	//密码验证
	form.verify({
		password: [/(.+){6,12}$/, '密码必须6到12位'],
		confirm_password: function(value) {
			if(value != $("#password").val()){
			$("#confirm_password").val("");
			return '确认密码与密码不一致';
			}
		} 
	});
	
	//名称长度校验
	  form.verify({
		  lengthUserName: function(value){  
	          if(value.length > 60){  
	            return '不能超过60个字符';  
	          }  
	      },
	      lengthRealName: function(value){  
	          if(value.length > 10){  
	            return '不能超过10个字符';  
	          }  
	      },
  	  });  
  	  
	//监听提交
	form.on('submit(tijiao)', function(data){
		var p = data.field;
		var orgEquipment = JSON.stringify(p);
        T.reqAjax({btnId:"subBtn", method:"put", param:orgEquipment, url:"user",contentType: "application/json",dataType: 'json',success:function(res){
        	layer.msg('修改成功');
        	data.form.reset();
        },error:function(res){
        	$.tBox.error(res.message);
        }})
		return false;
	});
	
	$("#s_form").loadToDom({url:"user/"+userId});
});