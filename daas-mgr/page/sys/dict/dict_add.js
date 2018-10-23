layui.config({
	base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,layer = layui.layer;
  
	//监听提交
	form.on('submit(tijiao)', function(data){
        T.reqAjax({btnId:"subBtn", method:"post", param:data.field, url:"dict/create",success:function(res){
        	layer.msg('添加成功');
        	data.form.reset();
        },error:function(res){
        	$.tBox.error(res.message);
        }})
    	return false;
	});
  
});