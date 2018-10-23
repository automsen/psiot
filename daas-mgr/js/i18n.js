layui.define(["jquery","config"], function(exports){
	 
	 var LANGUAGE_KEY = "_p85-tlg";  
	 
	 var CACHE_KEY = "_g1sa_cc";
	 
	 var $ = layui.jquery;
	 
	 
	 var config = layui.config;
	 
	 var base64 = layui.base64;
	 
	 var LOCAL ;
	 
	 var dtd ;
 	
 	function _init(){
 		dtd = $.Deferred();
		var tempLocal = localStorage.getItem(LANGUAGE_KEY);
	 	if(tempLocal == null || tempLocal == ''){
	 	 	// 初始化配置
			 LOCAL = config.language || _getDefaultLocal();  // 配置了默认的语言，否则取浏览器语言
			 localStorage.setItem(LANGUAGE_KEY,LOCAL);
	 	}else{
	 	 	// 替换页面
		 	 LOCAL = tempLocal;
	 	 }
	 	// 只有初始化使用异步
	 	$.when(getJsonObj(LOCAL)).done(function(cache){
	 		__loadPage(cache)
	 	})
	}
 	 
 	function getJsonObj(local){
	    var cache_lg = localStorage.getItem(CACHE_KEY);
	    if(cache_lg == null || cache_lg == ''){
	    	$.getJSON(config.cookie_domain+config.i18n_folder+"string_"+local+".json",function(res){
	    		var jsonStr = JSON.stringify(res);
	    		localStorage.setItem(CACHE_KEY,jsonStr) ;
	 			dtd.resolve(res);
	 		}).error(function(error){
	 			dtd.resolve(error);
	 		})
	    }else{
	    	dtd.resolve(JSON.parse(cache_lg));
	    }
	    return dtd.promise();
	}
		 
	
	function __loadPage(cache){
		try{
			// 切换返回消息的key i18n-message
	 		replaceIl8n('i18n-message',cache);
		}catch(e){
		}
		$('body').show();
	}
	
	
	 /**
	  *  获取页面元素
	  * @param {Object} attr
	  * @param {Object} value
	  */
	function replaceIl8n(attr,cache){
		$('['+attr+']').each(function(){
			var tagVal = $(this).attr(attr);
			var cacheVal = cache[tagVal];
        	// 判断缓存中是否存在
        	if(cacheVal){
        		if(this.tagName == 'INPUT'){
        			$(this).attr("placeholder",cacheVal);
        		}else{
        			$(this).html(cacheVal);
        		}
        	}
		})
	}
	
	/**
	 *  获取浏览器默认配置 
	 */
	function _getDefaultLocal(){
	    var lang = navigator.language||navigator.userLanguage;//常规浏览器语言和IE浏览器  
	    return  lang.substr(0, 2);//截取lang前2位字符  
	}

	function _getMessage(key){
		var cache_lg = null;
		try{
			 cache_lg = JSON.parse(localStorage.getItem(CACHE_KEY));
		}catch(e){
		}
		return cache_lg?cache_lg[key]:null;	 	
	}
 
	function _setLocal(key){
		// 设置语言
		localStorage.setItem(LANGUAGE_KEY,key);
		// 清空缓存
		localStorage.removeItem(CACHE_KEY);
		// 重新初始化
		if(parent!= null){
				window.parent.location.href = "index.html";
		}else{
			window.location.href =  "index.html";
		}
		
	}
	 	 
	var il8n =  {
	 		getMessage:_getMessage,
	 		setLocal  : _setLocal,
	 		init:_init
	}
	exports("i18n",il8n);
})
