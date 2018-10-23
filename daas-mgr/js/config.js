layui.define([], function(exports){
	
	var config = {
		// 登录
		//webRoot:"http://192.168.20.112:2100/auth_api",
		webRoot:"http://api.tw-iot.cn/auth_api",
		// 服务
		//servicePath:"http://192.168.20.7:2100/ps_api/",
		servicePath:"http://api.tw-iot.cn/ps_api/",
		// 服务
		//serviceData:"http://192.168.20.7:2100/data_api/",
		serviceData:"http://api.tw-iot.cn/data_api/",
		cookie_domain:"/daas-mgr/",
		//定义token name
		token_key:"_twps_qy",
		refresh:"/auth",
		authHeader:"busp",
		i18n_folder:'js/i18n/',
		excludePath:['login','welcome'],
		cookie_expires:10,
		language:'zh'
	}
	
	exports('config', config);
})
