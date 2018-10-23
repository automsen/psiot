layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
	
    
  	form.on('select(equipCateg)', function(data){
		 var equipCateg = data.value;
		 var html='';
		 debugger;
		 if(equipCateg!=""){
		 	
		 	$("#equipType").loadSelect({url:"dict/all?parentCode="+equipCateg,value:'dictCode',name:'dictName',isShowSel:'false',success:function(){
		 		form.render('select');
		 	}})
		 	
			 /*T.reqAjax(
					 {
						 btnId:"subBtn", 
						 method:"get", 
						 url:"dict/all?parentCode="+equipCateg,
						 contentType: "application/json",
						 dataType: 'json',
						 success:function(res){
							 var data = res.data;
							 $('#equipType').html('');
							 for(var equip of data){
								 if(equip.parentCode == equipCateg){
									 html += '<option value='+equip.dictCode+'>'+equip.dictName+'</option>';
								 	}
							 }
							 $('#equipType').append(html);
							 form.render('select');
						 },error:function(res){
							 $.tBox.error(res.message);
						 }})*/
		 }
		 else {
			 $('#equipType').val("");
			 form.render('select');
		 }
	}); 
 
	
});