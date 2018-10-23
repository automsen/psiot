layui.define(["jquery","config"], function(exports){
	var dictList;
	var dictListKey = "_f1gaea_dc";
	var attrName = 'T-dict'
	var valueKey = "value";
	var base64 = layui.base64;
	var dictUrl = layui.config.servicePath+"/dict/all";	
	var T = layui.T;
	var $ = layui.jquery;
	var dtd = $.Deferred();
	
	
	function _loadLocalDict(){
		T.reqAjax({ method:"get",async: false, url:dictUrl,success:function(res){
			if(res.statusCode == '000000'){
				dictList = res.data;
				var tempStr = JSON.stringify(dictList);
				localStorage.setItem(dictListKey,tempStr);
				dtd.resolve(dictList)
			}else{
				$.tBox.error(res.message);
			}
        },error:function(res){
        	$.tBox.error(res.message);
        }})
		return dtd.promise();
	}
	

	function loadKeyByData( key, data){
		var resultList = [];
		$.each(dictList, function() {
			if(this.dictType == key){
				resultList.push(this);
			}
		});
		return resultList;
	}
	
	function loadKeyByData( key, val,data){
		var text = '';
		$.each(data, function() {
			if(this.dictType == key && this.dictCode == val){
				text = this.dictName;
				return false;
			}
		});
		return text;
	}
	/**
	 * 
	 * @param {boolean} isRefresh  是否刷新本地缓存
	 */
	function _init(isRefresh){
		// 刷新清空本地缓存
		if(isRefresh){
			localStorage.removeItem(dictListKey);		
		}
		// 读取页面缓存   ，如果本地内存没有值
		if(dictList == null || dictList == ''){
			dictList = localStorage.getItem(dictListKey);
			if(dictList != null && dictList != ''){
				dictList = JSON.parse(dictList);
			}
		}
		// 读取ajax ，本地没值，缓存没值
		if(dictList == null || dictList == ''){
			$.when(_loadLocalDict()).then(function(data){
				_refreshDomDict(data);
			})
		}else{
			_refreshDomDict(dictList);			
		}
	}
	
	function _getTextByCode(code,key){
		if(dictList == null || dictList == ''){
			dictList = localStorage.getItem(dictListKey);
			if(dictList != null && dictList != ''){
				dictList = JSON.parse(dictList);
			}else{
				return null;
			}
		}
		return loadKeyByData(code,key,dictList);
	}
	

	
	// 只做回显
	function _refreshDomDict(dictList){
		var key = null;
		$('['+attrName+']').each(function(){
			var tagName = this.tagName;
			if(tagName == 'INPUT' || tagName == 'SELECT' )
				return;
			var code = $(this).attr(attrName);
			var value= $(this).data(valueKey);
			var text = loadKeyByData(code,value,dictList);
			this.innerHTML = text;
		})
	}
	
	var dict = {
		init:_init,
		getTextByCode:_getTextByCode
	}
	exports("dict",dict);
});
