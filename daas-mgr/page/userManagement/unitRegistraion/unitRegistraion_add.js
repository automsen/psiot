layui.config({
	base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer; 
	
	   
	  //手机、邮箱校验规则   
	  form.verify({   
	        phone: [/^1[3|4|5|7|8]\d{9}$/, '手机必须11位，只能是数字！']  
	        ,email: [/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']  
	  }); 
	  
	  //名称长度校验
	  form.verify({
		  lengthOrgName: function(value){  
	          if(value.length > 40){  
	            return '不能超过40个字符';  
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
		// 绑定 当前机构为父机构
		var user = T.getUserLcal();
		if(user == null ||user == ''){
			return false;
		}			
		var orgId = JSON.parse(user).orgId;
		if( orgId != '' && orgId != null){
			p.parentOrgId = orgId
		}
		
		var orgExpand = JSON.stringify(p);
        T.reqAjax({btnId:"subBtn", method:"post", param:orgExpand, url:"org",contentType: "application/json",dataType: 'json',success:function(res){
        	layer.msg('添加成功');
        	data.form.reset();
        },error:function(res){
        	$.tBox.error(res.message);
        }})
		return false;
	});
  
});

