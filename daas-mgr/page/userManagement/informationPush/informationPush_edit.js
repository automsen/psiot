layui.config({
	base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
	
	var dataField = T.local.get("dataField");
  	var appId = dataField.appId;
  	$("#appId").val(appId);
	
	
	//监听提交
	form.on('submit(tijiao)', function(data){
		var p = data.field;
		var app = JSON.stringify(p);
        T.reqAjax({btnId:"subBtn", method:"put", param:app, url:"application",contentType: "application/json",dataType: 'json',success:function(res){
        	layer.msg('修改成功');
        	data.form.reset();
        },error:function(res){
        	$.tBox.error(res.message);
        }})
		return false;
	});
	
	$("#s_form").loadToDom({url:"application/"+appId});
});