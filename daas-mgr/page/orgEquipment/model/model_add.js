layui.config({
	base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
	
	
	form.on('select(equipCateg)', function(data){
		
		var equipCateg = data.value;
		
		T.reqAjax({btnId:"subBtn", method:"get", url:"dict/all?parentCode="+equipCateg,contentType: "application/json",dataType: 'json',success:function(res){
			var data = res.data;
			$('#equipType').html('');
	          var html='';
	          if(equipCateg == "0101"){
	        	  for(var equip of data){  
	        		  if(equip.parentCode == equipCateg){
	        			  html += '<option value='+equip.dictCode+'>'+equip.dictName+'</option>';
	        		  }
	              }  
	              $('#equipType').append(html);
	          }else if(equipCateg == "0102"){
	        	  for(var equip of data){  
	        		  if(equip.parentCode == equipCateg){
	        			  html += '<option value='+equip.dictCode+'>'+equip.dictName+'</option>';
	        		  }
	              }  
	              $('#equipType').append(html);
	          }
	          form.render('select');
			
			
			
		},error:function(res){
        	$.tBox.error(res.message);
        }})
		
		
		  var baudRates = document.getElementById("baudRate"); 
          if(equipCateg == "0101"){
	    	  for (var i = 0; i < baudRates.options.length; i++){
		          if (baudRates.options[i].value == "2400")
		          {
		            baudRates.options[i].selected = true;  
		            break;  
		          } 
	    	  }
    	  }else if(equipCateg == "0102"){
    	  	  for (var i = 0; i < baudRates.options.length; i++){
		          if (baudRates.options[i].value == "9600")
		          {
		            baudRates.options[i].selected = true;  
		            break;  
		          }  
	    	  }
          }
	});  
	
	//监听提交
	form.on('submit(tijiao)', function(data){
		var p = data.field;
		var model = JSON.stringify(p);
        T.reqAjax({btnId:"subBtn", method:"post", param:model, url:"model",contentType: "application/json",dataType: 'json',success:function(res){
        	layer.msg('添加成功');
        	data.form.reset();
        },error:function(res){
        	$.tBox.error(res.message);
        }})
		return false;
	});
  
});

