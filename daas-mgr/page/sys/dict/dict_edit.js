layui.config({
	base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
  	
  	//var getUrlData = T.parseURL(window.location.href);
  	//$("#dictId").val(getUrlData.dictId);
  	var dataField = T.local.get("dataField");
  	var dictId = dataField.dictId;
  	$("#dictId").val(dictId);
	//监听提交
	form.on('submit(tijiao)', function(data){
        T.reqAjax({btnId:"subBtn", method:"post", param:data.field, url:"dict/update",success:function(res){
        	layer.msg('修改成功');
        	data.form.reset();
        },error:function(res){
        	$.tBox.error(res.message);
        }})
		return false;
	});
	
	$("#s_form").loadToDom({url:"dict/detail/"+dictId});
	
	
});