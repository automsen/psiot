layui.config({
	base: 'js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['form', 'layer','T','config'], function(){
	var form = layui.form
	,T = layui.T
	,$ = layui.$
	,config=layui.config
	,layer = layui.layer;
	var telephone =$("#telephone").val();
	form.on('submit(tijiao)', function(data){
		var validateCode = $("#validateCode").val();
		if(validateCode == null || validateCode == ""){
			 $.tBox.error("验证码不能为空!");
			 return false;
		}
		// 先验证后提交
		var url = config.webRoot+ "/checkimagecode";
		 $.ajax({
		        type: "POST",
		        url: url,
		        dataType: 'json',
		        data:{"validateCode":validateCode},
		　　　　 // 允许携带证书
		        xhrFields: {
		             withCredentials: true
		        },
		　　　　 // 允许跨域
		        crossDomain: true,
		        success:function(result){
		        	if(result.statusCode=="000000"){
						T.reqAjax({
				        	btnId:"subBtn", 
				        	method:"post",
				        	contentType: "application/json",
				        	dataType: 'json', 
				        	param:JSON.stringify(data.field), 
				        	url:config.webRoot+"/auth/token",
				        	success:function(res){
					        	if (res.statusCode == 000000) {  //后台返回状态码，code=0的时候登录成功
					        		var token=res.data.accessToken;
					        		var user =res.data.user;
					        		T.local.remove(config.token_key);
					        		T.local.remove(config.token_key + "_refresh");
					        		T.local.remove(config.token_key + "_d");
					        		T.local.set(config.token_key, token.accessToken);
					        		T.local.set(config.token_key + "_refresh", token.refreshToken);
									T.local.set(config.token_key + "_d",user);
					　　　　　　　　 window.location.href = "index.html" ; //跳转连接 index.html页面
				　　　　　　　　　　} else {
				　　　　　　　　　　　　$("#prompt-3").css("display","block").html("&#xe60b;"+res.msg);//没登录时提示信息，后台做的判断
				　　　　　　　　　　}
					        },error:function(res){
					        	$.tBox.error(res.message);
					        }
				    	})
					}else{
						$.tBox.error("验证码错误");
						flushValidateCode();
					}
		        },
		        error:function(){
		    }
		})
         return false;
	});
	
	flushValidateCode();//进入页面就刷新生成验证码
		

	
});
/* 刷新生成验证码 */
function flushValidateCode(){
	layui.use(['config'],function(){
		var config = layui.config
		var validateImgObject = document.getElementById("codeValidateImg");
		validateImgObject.src = config.webRoot+"/getSysManageLoginCode?time=" + Date.parse(new Date());
	})
	
}