layui.config({
   base: '../../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','T'], function(){
  var form = layui.form
  ,T = layui.T
  ,$ = layui.$
  ,layer = layui.layer;
  	
    var dataField = T.local.get("dataField");
	var userId = dataField.userId;
	$("#userId").val(userId);
	$("#userDate").loadToDom({url:"user/"+userId});
	
});