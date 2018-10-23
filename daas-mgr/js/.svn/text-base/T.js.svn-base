layui.define(["jquery","laypage","layer","laytpl","config","cookie","form","util","i18n"], function(exports){
	var $ = layui.$;
	var jQuery = layui.$;
	var laypage = layui.laypage;
	var layer = layui.layer;
	var laytpl = layui.laytpl;
	var config = layui.config;
	var cookie = layui.cookie;
	var form = layui.form;
	var util = layui.util;
	var i18n = layui.i18n;
    //do something
    var T = {
		webRoot:config.servicePath,
		openLoad: function(){
			var msg_index = layer.load(1, {shade: false});
			return msg_index;
		},
		colseLoad:function(msg_index){
			
			if(msg_index != null && msg_index != "" && msg_index != undefined){
				layer.close(msg_index);
			}
			
		},
		parseURL:function(url){
			var url = url.split("?")[1];
		    var para = url.split("&");
		    var len = para.length;
		    var res = {};
		    var arr = [];
		    for(var i=0;i<len;i++){
		        arr = para[i].split("=");
		        res[arr[0]] = arr[1];
		    }
		    return res;
		},
		route: function(url){
			//判断用户登录是否超时
			//如果不超时，继续访问， 如果超时，则按情况是否调整到登录页
			window.location.href = url;
		},
		getRootPath: function(){
			 //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
		    var curWwwPath=window.document.location.href;
		    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
		    var pathName=window.document.location.pathname;
		    var pos=curWwwPath.indexOf(pathName);
		    //获取主机地址，如： http://localhost:8083
		    var localhostPaht=curWwwPath.substring(0,pos);
		    //获取带"/"的项目名，如：/uimcardprj
		    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
		    return localhostPaht;
		},
		scrollTop:function(){
			$('body,html').animate({'scrollTop':0},200);
		},
		/**==== 公共的业务代码，如获取区县 ===**/
	    reqAjax:function(obj){
			obj = obj || {};
			var opt = $.extend({btnId:"", method:"get", url:"", async:true, cache:false, global:true, param:{}, contentType:"application/json", success:false, error:false}, obj);
			
			if($("#"+opt.btnId).hasClass("layui-btn-disabled")){
				return false;
			}
			
			$("#"+opt.btnId).addClass("layui-btn-disabled");
			
			var url = (opt.url.indexOf("http:") > -1) ? opt.url : (T.webRoot + opt.url);
			$.ajax({type : opt.method, url : url, global:opt.global, data : opt.param, async : opt.async, cache : opt.cache, contentType:opt.contentType, dataType : "json",
				success : function(res) {
					$("#"+opt.btnId).removeClass("layui-btn-disabled");
					//000000代表返回成功消息
					if(res.statusCode == "000000"){
						//保存最后一次操作成功的时间
						T.local.set(config.token_key + "_time", {last_time:new Date().getTime()})
						if(opt.success){
							opt.success(res);
							return;
						}
					}else{
						if(opt.error){
							opt.error(res);
							return;
						}
						
						//$.tBox.error("系统异常");
					}
				},
				error : function(){
					$("#"+opt.btnId).removeClass("layui-btn-disabled");
					if(opt.error){
						var res = {
							statusCode:"111111",
							message:"系统异常"
						}
						opt.error(res);
						return;
					}
					//$.tBox.error("系统异常");
				}
			});
			
	    },
		ajaxGloal:function(){
			var msg_index = null;
			$.ajaxSetup({
				cache:false,
				headers: {
		            "Authorization":config.authHeader + " " + T.local.getNoRemove(config.token_key)
		        },
		        beforeSend:function(XMLHttpRequest){
		        	//msg_index = T.openLoad();
		        },
		        complete:(function() {
		        	//T.colseLoad(msg_index);
		        })
		    });
			
			$(document).ajaxSend(function(){
				//T.local.set(config.token_key + "_time", {last_time:new Date().getTime()})
				msg_index = T.openLoad();
			});
			
			$(document).ajaxStop(function(){
			    setTimeout(function(){
			        layer.close(msg_index);
			    },100)
			});
			
		      /**
		     *  异常绑定
		     */
		    $(document).ajaxError(function(e,error){
		    	
		    	if (error.status == '401') {
		    		
		    		var resp = error.responseText
		    		
		    		resp = JSON.parse(resp);
		    		if (resp.statusCode == "010999") {
		    			var rootPath = T.getRootPath();
		    			var currentTime = new Date().getTime();
		    			var lastTime = T.local.get(config.token_key + "_time")
		    			var token = T.local.getNoRemove(config.token_key + "_refresh");
		    			if (token == null) {
	    					if(parent!= null){
								window.parent.location.href = rootPath +"/login.html";
							}else{
								window.location.href = rootPath +"/login.html";
							}
	    				}
		    			
		    			if (((currentTime - lastTime) / (1000 * 60)) >= 30){
		    				if(parent!= null){
								window.parent.location.href = rootPath +"/login.html";
							}else{
								window.location.href = rootPath +"/login.html";
							}
		    				return;
		    			}
		    			
	    				$.get(config.webRoot + config.refresh + "/refresh?reToken=" + T.local.getNoRemove(config.token_key + "_refresh"), function(res){
	    					if (res.statusCode == '000000') {
	    						var data = res.data;
	    						T.local.set(config.token_key,data.accessToken);
				        		T.local.set(config.token_key + "_refresh", data.refreshToken);
	    					}else {
	    						if(parent!= null){
									window.parent.location.href = rootPath +"/login.html";
								}else{
									window.location.href = rootPath +"/login.html";
								}
	    					}
	    				})
		    		}
		    		
		    	}
		    	/**
		    	 *  页面访问失效或 页面token失效
		    	 */
		    	/*if(error.status == '401' || error.status == '0'){
		    		if(parent!= null){
						window.parent.location.href = config.cookie_domain +"login.html";
					}else{
						window.location.href = config.cookie_domain +"login.html";
					}
		    	}*/
			});
		},
		init:function(){
			
			//T.interceptor();
//			//国际化
			i18n.init();
			T.ajaxGloal();
			T.event();
			//初始化表格
			T.initTable();
			//初始化
			T.initControl();
		},
		interceptor:function(){
		
		   var url = document.location.toString();
	　　　　var arrUrl = url.split("//");

	　　　　var start = arrUrl[1].indexOf("/");
	　　　　var relUrl = arrUrl[1].substring(start);//stop省略，截取从start开始到结尾的所有字符

	　　　　if(relUrl.indexOf("?") != -1){
	　　　　　　relUrl = relUrl.split("?")[0];
	　　　　}
			var paths = config.excludePath;
			
			if (relUrl == "/"){
				return;
			}
			
			for (var path in paths) {
				if (relUrl.indexOf(paths[path]) > -1) {
					return false;
				}
			}
			var token = T.local.getNoRemove(config.token_key);
			if(token == null || token == '' ){
				if(parent!= null){
					window.parent.location.href = config.cookie_domain+ "/login.html";
				}else{
					window.location.href = config.cookie_domain+ "/login.html";
				}
			}
		},
		interv:null,
			cons:{
				isClick:false
			},
		getUserLcal:function(){
			return T.local.getNoRemove(config.token_key + "_d");
		},
		cookie:{
			set:function(name, value){
				cookie(name, value, {path: config.cookie_domain, expires: config.cookie_expires})
			},
			get:function(name){
				var cookieValue = cookie(name);
				return cookieValue;
			}
		},
		local:{
			set:function(name, value){
				localStorage.setItem(name,JSON.stringify(value));
			},
			get:function(name){
				var value = localStorage.getItem(name);
				localStorage.removeItem(name);
				return JSON.parse(value);
			},
			getNoRemove:function(name){
				var value = localStorage.getItem(name);
				return value != '' && value!= null ?JSON.parse(value):'';
			},
			remove:function(name){
				localStorage.removeItem(name);
			}
		},
		//为.T-event-click对象绑定 确认弹出框
		event:function(){
			$("body").on("click", ".T-click-prompt", function(){
				var opt = T.extOption("", this)
				$.tBox.confirm(opt.msg, function(){
					T.extAjax(opt);
				})
			}).on("click", ".T-click-layer-page", function(){
				var _this = this;
				var opt = T.extOption(null, _this);
				T.local.set("dataField", opt.dataField);
				var index = layer.open({title : opt.title, type : 2, content : opt.url, success : function(layero, index){
						setTimeout(function(){
							layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
								tips: 3
							});
						},300);
					},
					cancel: function(index, layero){
					  var tableObj = $(_this).closest("table");
					  if(tableObj.length > 0 && !opt.norefresh){
						  tableObj.tbGrid("refresh");
					  }
					  
					  if (tableObj.length == 0 && !opt.norefresh){
						  if($("table").length > 0){
							  $("table").tbGrid("refresh");
						  }
					  }
					  layer.close(index);
					  return false; 
					}
				})			
				layer.full(index);
			});
		},
		extOption: function(obj, _this){
			obj = obj || {};
			obj = $.extend($.parse.options(_this), obj);
			obj._this=_this;
			var opt = $.extend({method:"get", url:"", async:true, cache:false, param:{}, msg:"请填写提示语！", title:"", _this: "", success:false},obj)
			return opt;
		},
		extAjax: function(opt){
			//type:提交方式;url:发送请求的地址;data:要发送到服务器的数据;
			//async: false=同步（默认是true=异步; cache:默认值是true，dataType为script和json时默认为 false,设置为false将不缓存此页面;
			//dataType:json，指定服务器返回的数据类型;success:请求成功后的回调函数。
			var _this = opt._this;
			$.ajax({type : opt.method, url : T.webRoot + opt.url, data : opt.param, async : opt.async, cache : opt.cache, dataType : "json",
				success : function(res) {
					//000000代表返回成功消息
					if(res.statusCode == "000000"){
						T.local.set(config.token_key + "_time", {last_time:new Date().getTime()})
						if(opt.success){
							opt.success(_this, res);
						}
						
						var table = $(_this).closest("table");
						if(table.length > 0){
							table.tbGrid("refresh");
						}
						return;
					}else{
						if(opt.error){
							opt.error(_this, res);
							return;
						}
						//$.tBox.error("系统错误");
					}
				},
				error : function(){
					//$.tBox.error("系统异常");
				}
			});
		},
		//列表
		initTable:function(){
			var dtG = $(".T-dtGrid");
			if(dtG.length > 0){
				dtG.tbGrid();
			}
			//$("table").resizableColumns({resizeFromBody:true, syncHandlers: true, maxWidth: null, minWidth: "10"});
		},
		//初始化控件
		initControl:function(){
			//下拉框
			var t_select = $(".T-select");
			if(t_select.length > 0){
				t_select.each(function(){
					//T.js方法，加载下拉框数据
					$(this).loadSelect({complete:function(obj, res){
						form.render();
					},error:function(obj, res){
						
					}});
				})
			}
			
			//初始化dom值设置
			var t_form = $(".T-form");
			if(t_form.length > 0){
				t_select.each(function(){
					$(this).loadToDom();
				})
			}
			
		},
		base64: {
				_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
			    // public method for encoding
			    encode : function (input) {
			        var output = "";
			        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
			        var i = 0;
			        input = T.base64._utf8_encode(input);
			        while (i < input.length) {
			            chr1 = input.charCodeAt(i++);
			            chr2 = input.charCodeAt(i++);
			            chr3 = input.charCodeAt(i++);
			            enc1 = chr1 >> 2;
			            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			            enc4 = chr3 & 63;
			            if (isNaN(chr2)) {
			                enc3 = enc4 = 64;
			            } else if (isNaN(chr3)) {
			                enc4 = 64;
			            }
			            output = output +
			            T.base64._keyStr.charAt(enc1) + T.base64._keyStr.charAt(enc2) +
			            T.base64._keyStr.charAt(enc3) + T.base64._keyStr.charAt(enc4);
			        }
			        return output;
			    },
			 
			    // public method for decoding
			    decode : function (input) {
			        var output = "";
			        var chr1, chr2, chr3;
			        var enc1, enc2, enc3, enc4;
			        var i = 0;
			        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
			        while (i < input.length) {
			            enc1 = T.base64._keyStr.indexOf(input.charAt(i++));
			            enc2 = T.base64._keyStr.indexOf(input.charAt(i++));
			            enc3 = T.base64._keyStr.indexOf(input.charAt(i++));
			            enc4 = T.base64._keyStr.indexOf(input.charAt(i++));
			            chr1 = (enc1 << 2) | (enc2 >> 4);
			            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			            chr3 = ((enc3 & 3) << 6) | enc4;
			            output = output + String.fromCharCode(chr1);
			            if (enc3 != 64) {
			                output = output + String.fromCharCode(chr2);
			            }
			            if (enc4 != 64) {
			                output = output + String.fromCharCode(chr3);
			            }
			        }
			        output = T.base64._utf8_decode(output);
			        return output;
			    },
			 
			    // private method for UTF-8 encoding
			    _utf8_encode : function (string) {
			        string = string.replace(/\r\n/g,"\n");
			        var utftext = "";
			        for (var n = 0; n < string.length; n++) {
			            var c = string.charCodeAt(n);
			            if (c < 128) {
			                utftext += String.fromCharCode(c);
			            } else if((c > 127) && (c < 2048)) {
			                utftext += String.fromCharCode((c >> 6) | 192);
			                utftext += String.fromCharCode((c & 63) | 128);
			            } else {
			                utftext += String.fromCharCode((c >> 12) | 224);
			                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
			                utftext += String.fromCharCode((c & 63) | 128);
			            }
			 
			        }
			        return utftext;
			    },
			 
			    // private method for UTF-8 decoding
			    _utf8_decode : function (utftext) {
			        var string = "";
			        var i = 0;
			        var c = c1 = c2 = 0;
			        while ( i < utftext.length ) {
			            c = utftext.charCodeAt(i);
			            if (c < 128) {
			                string += String.fromCharCode(c);
			                i++;
			            } else if((c > 191) && (c < 224)) {
			                c2 = utftext.charCodeAt(i+1);
			                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
			                i += 2;
			            } else {
			                c2 = utftext.charCodeAt(i+1);
			                c3 = utftext.charCodeAt(i+2);
			                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
			                i += 3;
			            }
			        }
			        return string;
			    }
		}
  }
    
  //原生对象方法扩展 ----------------start ----------------------
  Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"H+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
  }
  //原生对象方法扩展 ----------------end ----------------------

  //自定$扩展方法-----------------start ----------------------
	$.extend({
		isEmpty:function(value){//判断是否为空
			if(value == null || value === "" || $.trim(value) == "" || typeof(value) == undefined){
				return true;
			}
			return false;
		},
		formatDatestamp:function(time, format) {//格式化时间
			if ($.isEmpty(time)) {
				return "";
			}
			var d = new Date(time);
			return d.format(format);
		},
		interceptStr:function(value){//截取字符串长度
			if ($.trim(value).length > 20) {
				return value.substring(0, 20) + "...";
			}
			return value;
		}
	})

	//自定$扩展方法----------------- end ----------------------

	//自定义$对象扩展方法 --------------start ----------------
	$.fn.extend({
			datepicker:function(){//初始化时间控件，依赖datetimepicker控件
				var opt = $.extend({weekStart:1,todayBtn:1,autoclose:1,todayHighlight:1,startView:2,minView:2,forceParse:0},$.parse.tableOptions(this))
			},
			otips : function(opt, ex_opt) {//tip提示
				if(typeof(opt) == "string"){
					opt = $.extend({title:opt}, ex_opt);
				}
				
				opt = $.extend({title:"请输入标题", template:'<div class="tooltip error" for="#" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>', placement:"top", trigger:"manual", timeout:"4000"}, opt)
				if ($(this).next(".tooltip").length > 0) {
					return;
				}
				$(this).tooltip({
					title : opt.title,
					placement : opt.placement,
					trigger : opt.trigger,
					template : opt.template.replace("#",this[0].name)
				}).tooltip('show');
				var _this = this;
				setTimeout(function() {
					$(_this).tooltip("destroy")
				}, opt.timeout)
			},
			setform:function (jsonValue) {
	            var obj = this;
	            $.each(jsonValue, function (name, value) {
	                var _input = obj.find("input:[name=" + name + "]");
	                if (_input.attr("type")== "radio" || _input.attr("type")== "checkbox") {
	                	_input.each(function(){
	                     if($(this).val()==value)
	                        $(this).attr("checked", "checked");
	                   });
	                }else{
	                  obj.find("[name="+name+"]").val(value);
	                }
	            });
	        },
	        //结果集的填充（jsonValue后台数据）
	        setValueToDom:function (jsonValue) {
	            var obj = this;
	            $.each(jsonValue, function (name, value) {
	            	//T-name
	                var _tag = obj.find("[T-name=" + name + "]");
	                
	                if(_tag.length > 0){
	                	
	                	if($.isEmpty(value)){
	 	                   value = _tag.attr("T-default");
	 	                }
	                	
	                	var _tag_type = _tag.attr("T-type");
		                
		                if (_tag_type == "time") {
		                	value = util.toDateString(value, "yyyy-MM-dd HH:mm:ss")
		                }
		                
		                if(_tag_type == "date") {
		                	value = util.toDateString(value, "yyyy-MM-dd")
		                }
	 	                
	 	                if(_tag[0].tagName == "INPUT"){
	 	                	if (_tag.attr("type")== "radio" || _tag.attr("type")== "checkbox") {
	 	                		_tag.each(function(){
	 		                     if($(this).val() == value)
	 		                        $(this).attr("checked", "checked");
	 		                   });
	 		                }else{
	 		                	_tag.val(value);
	 		                }
	 	                }else if(_tag[0].tagName == "SELECT"){
	 	                	_tag.val(value);
	 	                }else if(_tag[0].tagName == "SPAN"){
	 	                	_tag.text(value);
	 	                }else{
	 	                	_tag.text(value);
	 	                }
	                }
	                
	                //查找带有T-name-swith的便签
	            	var _tag_swith = obj.find("[T-name-swith=" + name + "]");
	                
	                //如果标签没有找到，跳出当前循环，继续循环
	                if (_tag_swith.length > 0){
	                	
	                	//如果有获取T-name-swith的标签下的T-case的所在的标签
		                var caseObj = $(_tag_swith[0]).find("[T-case]");
		                _tag_swith.empty();
		                //遍历T-case所在的标签，判断T-case属性的值，如果value 和 T-case属性中的值相等，则填充
		                caseObj.each(function(){
		                	var _this = this;
		                	if ($(this).attr("T-case") == value){
		                		_tag_swith.text($(this).text());
		                		return false;
		                	}
		                })
	                }
	                
	            });
	            
	            form.render('select');
	        },
	        //根据id得到T-options的url，ajax进入后台
	        //[searchFormId:表单id;searchBtn:查询按纽id]
			loadToDom:function(obj){
				var _this = this;
				obj = obj || {};
				obj = $.extend($.parse.options(_this), obj);
				var opt = $.extend({method:"get", url:"", async:true, cache:false, param:{}, success:false, complete:false}, obj);
				//type:提交方式;url:发送请求的地址;data:发送到服务器的数据。将自动转换为请求字符串格式 contentType:默认值: "application/x-www-form-urlencoded"。发送信息至服务器时内容编码类型
				//async: false=同步（默认是true=异步; cache:默认值是true，dataType为script和json时默认为 false,设置为false将不缓存此页面;
				//dataType:json，指定服务器返回的数据类型;success:请求成功后的回调函数。
				$.ajax({type : opt.method, url : T.webRoot + opt.url, data : opt.param, async : opt.async, cache : opt.cache, dataType : "json",
					success : function(res) {
						//000000代表返回成功消息
						if(res.statusCode == "000000"){
							if(opt.success){
								opt.success(_this, res);
								return;
							}
							
							/*var formObj = $(_this).serialObj();
							formObj = $.extend(formObj, res.data);*/
							//得到后台的数据
							_this.setValueToDom(res.data);
							
							if (opt.complete) {
								opt.complete(_this, res);
							}
						}else{
							//$.tBox.error("系统错误");
						}
					},
					error : function(){
						//$.tBox.error("系统异常");
					}
				});
			},
			//下拉框的数据加载
			loadSelect : function(obj){
				var _this = this;
				obj = obj || {};
				//$.parse解析“T-options”属性。并将后置函数合并到参数中
				obj = $.extend($.parse.options(_this), obj);
				//isShowSel:true;下拉框第一条默认多一条请选择，false则没有
				var opt = $.extend({method:"get", url:"", async:true, cache:false, param:{}, value:"", name:"", selected:"", isShowSel:true, selTitle:"--请选择--", onchange:false, success:false, complete:false, error:false}, obj)
				//type:提交方式;url:发送请求的地址;data:发送到服务器的数据。
				//async: false=同步（默认是true=异步; cache:默认值是true，dataType为script和json时默认为 false,设置为false将不缓存此页面;
				//dataType:json，指定服务器返回的数据类型;success:请求成功后的回调函数。
				$.ajax({type : opt.method, url : T.webRoot + opt.url, data : opt.param, async : opt.async, cache : opt.cache, dataType : "json",
					success : function(res) {
						//000000代表返回成功消息
						if(res.statusCode == "000000"){
							var data = res.data;
							$(_this).empty();
							
							if(data.length == 0){
								$(_this).append("<option value=''>无</option>");
							}else{
								if(opt.isShowSel || opt.isShowSel == 'true'){
									$(_this).append("<option value=''>" + opt.selTitle + "</option>");
								}
							}
							
							for(var i=0; i<data.length; i++){
								$(_this).append("<option value="+data[i][opt.value]+">"+data[i][opt.name]+"</option>")
							}
							
							if(opt.selected.length > 0){
								$(_this).val(opt.selected)
							}
							
							if(opt.success){
								opt.success(_this);
							}
							
							if(opt.onchange){
								$(_this).change(function(){
									opt.onchange(_this);
								});
							}
							if(opt.complete){
								opt.complete(_this, res)
							}
						}
					},
					error:function(obj){
						console.log("loadSelect load error");
						if(opt.error){
							opt.error(_this, obj)
						}
					}
				});
				
			},
			loadSelectFormData:function(opt, data){
				var _this = this;
				
				$(_this).empty();
				
				if(data.length == 0){
					$(_this).append("<option value=''>无</option>");
				}else{
					if(opt.isShowSel){
						$(_this).append("<option value=''>--请选择--</option>");
					}
				}
				
				for(var i=0; i<data.length; i++){
					$(_this).append("<option value="+data[i][opt.value]+">"+data[i][opt.name]+"</option>")
				}
				
				if(opt.selected.length > 0){
					$(_this).val(opt.selected)
				}
				
				if(opt.success){
					opt.success(_this);
				}
				
				if(opt.onchange){
					$(_this).change(function(){
						opt.onchange(_this);
					});
				}
				if(opt.complete){
					opt.complete(_this, res)
				}
			},
			serialObj : function() {//将form表单内容转化成obj
				var serObj = {};
				var elements = $(this).serializeArray()
				$.each(elements, function(index) {
					
					/*if (this['value'] == "" || this['value'] == null || this['value'] == undefined) {
						return;
					}*/
					
					if (serObj[this['name']]) {
						serObj[this['name']] = serObj[this['name']] + ","
								+ this['value'];

					} else {
						serObj[this['name']] = this['value'];
					}
				});
				return serObj;
			},
			showData : function(tempId, data) {//该方法将调用jquery.templ.js 模板引擎
				$(this).empty();
				var parentObj = $(this).parent();
				parentObj.parent().find("p").remove();
				if (data == null || typeof (data) == undefined || data.length == 0) {
					parentObj.after("<p align='center' id='nodata' style='color: red;'>暂无数据</p>");
					return;
				}
				$(this).append($("#" + tempId).tmpl(data));
			},
			/**
			 * obj,如果为string类型，则是方法名；如果是对象类型，则是json类型的参数列表。
			 * f_obj，方法参数
			 */
			tbGrid : function(obj, f_obj){
				if(typeof obj == "string"){
					/**
					 * 根据方法名从方法列表中取出对应方法并执行。
					 * this[0]是".T-dtGrid"对象列表中的第一个对象
					 */
					return $.fn.tbGrid.method[obj](this[0], f_obj);
				}
				
				obj = obj || {};
				
				return this.each(function(){
					//获取当前对象上存储的名为dtGrid的临时数据。此临时数据在刷新页面之后将不复存在。它是存在页面缓存中。
					var dtGrid = $.data(this, "dtGrid");
					var opts = {};
					//如果dtGrid不存在，则dtGrid.options从传入参数obj继承过来
					if(dtGrid){
						opts = $.extend(true, dtGrid.options, obj);
						dtGrid.options = opts;
					//如果dtGrid存在，则重新生成dtGrid。此时dtGrid从传入参数obj继承以及从当前对象的“T-options”属性继承
					}else{
						//获取对象的"T-options"属性值
						var opt_obj = $.parse.tableOptions(this);
						obj = $.extend(true, opt_obj, obj);
						
						dtGrid = $.extend(true, {options:{method:"get", page:1, rows:10, templ:"templ", url:"", param:{}, searchFormId:"", 
							searchBtn:"", pagination:"pagination", showPage:true, success:"", complete:"", isShowNoData:true, layout:[]}}, {options:obj});
						$.data(this, "dtGrid", dtGrid);
					}
					//为当前对象的查询按钮绊定事件
					$(this).tbGrid("bindSearch", dtGrid.options.searchBtn);
					//为当前对象加载数据
					$(this).tbGrid("loadData");
					
					$(this).tbGrid("bindCheckBox");
					
					$(this).tbGrid("bindRow");
				})
				
			}
	})
	
	//T-dtGrid列表展示数据
	$.fn.tbGrid.method = {
		data	 : {},
		getRow   :function(idKey,value){
			if(idKey == '' || value == '' || $.fn.tbGrid.method.data.length ==0){
				return null;
			}
			var row = {};
			 $.fn.tbGrid.method.data.each(function(){
				if(this[idKey] == value){
					row = this;
					return false;
				}
			 });
			 return row;
		},
		loadData : function(obj){
			var arg_length = arguments.length;
			var dtGrid_data = $.data(obj, "dtGrid");
			var _options = dtGrid_data.options;
			var params = $("#"+_options.searchFormId).serialObj();
			params = $.extend(_options.param, params);
			//type:提交方式;url:发送请求的地址;data:页数，行数，分页数据; contentType:默认值: "application/x-www-form-urlencoded"。发送信息至服务器时内容编码类型
			//async: false=同步（默认是true=异步; cache:默认值是true，dataType为script和json时默认为 false,设置为false将不缓存此页面;
			//dataType:json，指定服务器返回的数据类型;success:请求成功后的回调函数。
			$.ajax({type : _options.method, url : _options.url.indexOf("http") > -1 ? _options.url : (T.webRoot + _options.url),data : {page:_options.page, rows:_options.rows, paramObj:params} ,contentType : "application/x-www-form-urlencoded; charset=utf-8",async : true,cache : false,dataType : "json",success : function(res) {
				res = res.data;
				if(res.page > 1 && res.data.length == 0){
					_options.page = res.page - 1;
					$(obj).tbGrid("loadData");
					$.fn.tbGrid.method.data = {};
					return;
				}	
				var data = res.data;
				$.fn.tbGrid.method.data = data;
				if(typeof _options.success == "string"){
					$("#"+_options.pagination).hide();
					if($(obj).is("table")){
						var tbody = $(obj).find("tbody");
						//tbody.showData(_options.templ, res.data);
						tbody.empty();
						var parentObj = tbody.parent();
						parentObj.parent().find("p").remove();
						if (data == null || typeof (data) == undefined || data.length == 0) {
							
							if(dtGrid_data.options.isShowNoData == true || dtGrid_data.options.isShowNoData == 'true'){
								parentObj.after("<p align='center' id='nodata' style='color: red;'>暂无数据</p>");
							}
							if(typeof _options.complete == "function"){
								_options.complete(res);
							}
							return;
						}
						
						laytpl($("#" + _options.templ).html()).render(res,function(html){
							tbody.append(html);
						})
						
					}else{
						$(obj).empty();
						$(obj).parent().find("p").remove();
						if (data == null || typeof (data) == undefined || data.length == 0) {
							
							if(dtGrid_data.options.isShowNoData == true || dtGrid_data.options.isShowNoData == 'true'){
								parentObj.after("<p align='center' id='nodata' style='color: red;'>暂无数据</p>");
							}
						
							if(typeof _options.complete == "function"){
								_options.complete(res);
							}
							return;
						}
						//$("#"+_options.pagination).show();
						//$(obj).append($("#" + _options.templ).tmpl(data));
						laytpl($("#" + _options.templ).html()).render(res,function(html){
							tbody.append(html);
						})
					}
					
					if (_options.showPage){
						$("#"+_options.pagination).show();
					}
					
					if(typeof _options.complete == "function"){
					
						_options.complete(res);
					}
				}else{
					_options.success(res);
				}
				
				form.render('checkbox');
				form.render('radio');
				form.render('select');
				
				layui.use(['dict'],function(){
					layui.dict.init();
					
				})
				laypage.render({
				    elem: _options.pagination //注意，这里的 test1 是 ID，不用加 # 号
				    ,count: res.totalRecord //数据总数，从服务端得到res.totalRecord
				    ,limit: res.rows,
				    layout: _options.layout.length > 0 ? _options.layout: ['count', 'prev', 'page', 'next','limit'],
				    curr:res.page,
				    first: _options.first,
				    last: _options.last,
				    jump: function(objPage, first){
				        //obj包含了当前分页的所有参数，比如：
				        //首次不执行
				        if(!first){
				        	_options.page = objPage.curr;
				        	_options.rows = objPage.limit;
							$(obj).tbGrid("loadData");
				        }
				      }
				 });
				}
			});
		},
		bindSearch : function(obj, id){
			if($.isEmpty(id)){
				return;
			}
			var _options = $.data(obj, "dtGrid").options;
			$("#" + id).unbind("click")
			$("#" + id).click(function() {
				_options.page = 1;
				$(obj).tbGrid("loadData");
			})
		},
		bindRow: function(obj){
			//var trObj = $(obj).find('tbody tr');
			$(obj).on("click", ".layui-form-checkbox", function(){
				var trObj = $(this).closest("tr");
				
				if($(this).hasClass("layui-form-checked")){
					trObj.css("background-color","rgb(242, 242, 242)")
				}else{
					trObj.css("background-color","")
				}
				return false;
			})
			
			/*$(obj).on("click", "*[T-title]", function(){
				var title = $(this).attr("T-title");
				layer.open({
				  type: 0,
				  skin: 'layui-layer-demo', //样式类名
				  closeBtn: 1, //不显示关闭按钮
				  anim: 0,
				  shadeClose: false, //开启遮罩关闭
				  content: title
				});
				return false;
			})*/
			
			$("table td").each(function(){
				  if(this.offsetWidth < this.scrollWidth){
					  $(this).css("cursor","pointer")
				  }
			  })
			
			var isCancel = false;
			var timer = null;
			$("table").on("mouseenter", "td", function(){
				  clearTimeout(timer);
		          if(this.offsetWidth < this.scrollWidth){
		              var that = this;
		              var text = $(this).text();
		              
		              timer = setTimeout(function() {
		            	  index = layer.tips(text, that,{
			                  tips: 2,
			                  time: 2000
			              });
		              }, 500);
		              
		              
		          }else {
		        	  clearTimeout(timer);
		        	  //layer.close(index)
		        	  layer.closeAll('tips')
		        	  /*if (index != null) {
		        		  layer.closeAll('tips')
		        		  layer.close(index)
		        	  }*/
		          }
			});
		  
		  $("table").on("click", "td", function(){
			  clearTimeout(timer);
	          if(this.offsetWidth < this.scrollWidth){
	              var that = this;
	              var text = $(this).text();
	              layer.open({
					  type: 0,
					  skin: 'layui-layer-demo', //样式类名
					  closeBtn: 1, //不显示关闭按钮
					  anim: 0,
					  shade:0.01,
					  shadeClose: false, //开启遮罩关闭
					  content: text
				  });
	          }
	       });

			
			$(obj).on("click", "tbody tr", function(){
				
				var radioObj = $(this).find("input[type='radio']");
				
				if (radioObj.length > 0){
					
					$(this).css("background-color","rgb(242, 242, 242)")
					//var backGroundColor = $(this).css("background-color");
					$(this).siblings().css("background-color","")
					
					radioObj[0].checked = true;
				}
				
				var boxObj = $(this).find("input[type='checkbox']");
				
				if (boxObj.length > 0){
					if(boxObj[0].checked){
						$(this).css("background-color","")
						boxObj[0].checked = false;
					}else{
						$(this).css("background-color","rgb(242, 242, 242)")
						boxObj[0].checked = true;
					}
				}
				
				form.render('checkbox');
				form.render('radio');
			})
		},
		bindCheckBox: function(obj){
			//全选
			form.on('checkbox(T-allChoose)', function(data){
				var child = $(data.elem).closest('table').find('tbody input[type="checkbox"]:not([name="show"])');
				child.each(function(index, item){
					item.checked = data.elem.checked;
					
					if(item.checked){
						$(this).closest("tr").css("background-color","rgb(242, 242, 242)")
					}else{
						$(this).closest("tr").css("background-color","");
					}
				});
				
				form.render('checkbox');
				form.render('radio');
			});
		},
		bindRadio: function(obj){
			form.on('radio(T-sinleChoose)', function(data){
			  console.log(data.elem); //得到radio原始DOM对象
			  console.log(data.value); //被点击的radio的value值
			});  
		},
		getCheckValues:function(obj){
			var checkChild = $(obj).find('tbody input[type="checkbox"]:not([name="show"])');
			var checkAttr = new Array();
			checkChild.each(function(index, item){
				if(item.checked){
					checkAttr.push($(this).val());
				}
			})
			
			var redioChild = $(obj).find('tbody input[type="radio"]:not([name="show"])');
			var radioAttr = new Array();
			redioChild.each(function(index, item){
				if(item.checked){
					radioAttr.push($(this).val());
				}
			})
			
			
			var resObj = {}
			
			resObj.checkVal = checkAttr.join(",");
			resObj.radioVal = radioAttr.join(",");
			return resObj;
		},
		//重新载入
		reload : function(obj){
			$(obj).tbGrid("loadData");
		},
		//刷新当前页
		refresh:function(obj){
			var _options = $.data(obj, "dtGrid").options;
			_options.page = 1;
			$(obj).tbGrid("loadData");
		}
	}

	//解析器，解析传入对象的T-options熟悉，将其转换为json对象。
	$.parse = {
		tableOptions:function(obj){
			var t_options = $.trim($(obj).attr("T-options"));
			
			var opt_obj = {};
			if(t_options){
				if(t_options.substring(0,1)!="{"){
					t_options="{"+t_options+"}";
				}
			}else{
				t_options = "{}";
			}
			
			opt_obj = (new Function("return " + t_options))();
			
			return opt_obj;
		},
		options:function(obj){
			var t_options = $.trim($(obj).attr("T-options"));
			
			var opt_obj = {};
			if(t_options){
				if(t_options.substring(0,1)!="{"){
					t_options="{"+t_options+"}";
				}
			}else{
				t_options = "{}";
			}
			
			opt_obj = (new Function("return " + t_options))();
			
			return opt_obj;
		}
	}

	/** 自定义提示框 **/
	$.tBox = {defaults:{}};

	$.extend($.tBox, {
		info:function(msg,options) {
			layer.msg(msg);
		},
		success:function(msg,options) {
			layer.msg(msg);
		},
		error:function(msg,options) {
			if(msg == '' || msg == null){
				return false;
			}
			layer.msg(msg);
		},
		confirm:function(opt,callback){
			
			if(typeof opt == "string"){
				opt = {msg:opt}
			}
			
			opt = $.extend($.tBox.defaults, opt)
			
			layer.confirm(opt.msg, {title:"提示", shade:0, btn: ['确认','取消']}, function(index){
				if (typeof (callback) == 'function') {
					callback();
					layer.close(index);
				}
				}, function(index){
					layer.close(index);
			});
		}

  })
  
  T.init();
  exports('T', T);
});
