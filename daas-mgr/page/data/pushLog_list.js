layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer','config','T'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,util = layui.util
	,config = layui.config
	,layer = layui.layer;
	
	function setThisMonthFirstDay(){
		var today = new Date();
	    today.setHours(0);
	    today.setMinutes(0);
	    today.setSeconds(0);
	    today.setMilliseconds(0);
	    return today;
	}
	
    layui.use('laydate', function(){
		  var laydate = layui.laydate;
		  //执行一个laydate实例
		  laydate.render({
			  elem: '#startTime',
			  event: 'click',
			  isclear: true,
			  type:'datetime',
			  value:setThisMonthFirstDay()
			  
	  	  });
		  laydate.render({
			  elem: '#endTime',
			  event: 'click',
			  isclear: true,
			  type:'datetime',
			  value: new Date()
	  	  });
	});
    
    var user = T.local.getNoRemove(config.token_key + "_d");
    
    $('#appId').loadSelect({param:{orgId:user.orgId}})
    
    setTimeout(function(){
    	$("#logList").tbGrid({url:config.serviceData + "/read/push", rows:50, param:{appId:$("appId").val(), startTime:util.toDateString(setThisMonthFirstDay(), "yyyy-MM-dd HH:mm:ss"), endTime:util.toDateString(new Date(),"yyyy-MM-dd HH:mm:ss")}});
    	form.render();
    },200);
    
});




