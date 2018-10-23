layui.define(["jquery","layer","laydate","config","form","dict"], function(exports){
	var $ = layui.$;
	var config = layui.config;
	var element= layui.element;
	var form = layui.form;
	var laydate = layui.laydate;
	var dict = layui.dict;
	var inlineHtmlHead 	= '<div class="layui-inline">';
	var inputHtmlHead 	= '<div class="layui-input-inline  float-left">';
	var divEnd	= 	 '</div">';
	var lableHtml 		= '<label class="layui-form-label">{lable}</label>';
	var readSpan = "<span>{value}</span>";
	
	
	
	
	function inputHtml(element,values,readonly,bindEvent){
		var inputHtmls = '<input type="text" name="{name}" lay-filter="{filter}" lay-verify="{required}" placeholder="{lable}" autocomplete="off" value="{values}" class="float-left layui-input">';

		var required = '';
		if(element.required && element.required != ''){
			required = element.required
		}
		if(readonly){
			return readSpan.replace("{value}",values);
		}else{
			inputHtmls  = inputHtmls.replace("{name}",element.name).replace("{lable}",element.lable);
			inputHtmls  = inputHtmls.replace("{values}",values).replace('{required}',required).replace('{filter}',element.name);;
		}
		// 判断是否需要绑定事件
		if(element.keyup && typeof element.keyup === 'string' && bindEvent){
			$(document).on('keyup','input[name="'+element.name+'"]',function(){
				try{
					var func = 	(new Function("return " + element.keyup))();
					if(typeof func  === 'function'){
						func.call(this,form,element);
					}
				}catch(e){
					throw e;
				}
			})
		}
		return inputHtmls;
		
	}
	
	function dateHtml(element,values,readonly){
		if(readonly){
			return readSpan.replace("{value}",values);
		}
		var dateHtmls = '<input readonly type="text" id="{id}" lay-verify="{required}"  name="{name}" placeholder="{lable}"  class="float-left layui-input">';
		var required = '';
		if(element.required && element.required != ''){
			required = element.required
		}
		dateHtmls  = dateHtmls.replace("{name}",element.name).replace("{lable}",element.lable).replace("{id}",element.name);
		dateHtmls  = dateHtmls.replace('{required}',required).replace('{filter}',element.name);;
		return dateHtmls;
	}
	
	
	
	// 代码块头部
	function _initInput(element,obj,readonly){
		var returnHtml = [];
		
		returnHtml.push(inlineHtmlHead);
		var lable = '';
		if(element.lable){
			lable = lableHtml.replace("{lable}",element.lable);
		}else{
			lable = lableHtml.replace("{lable}",'');
		}
		returnHtml.push(lable);			 // 表单标题
		returnHtml.push(inputHtmlHead);  // input 容器
		var values = '';
		if(obj && obj != ''){
			values = obj;
		}
		returnHtml.push(inputHtml(element,values,readonly,true));
		returnHtml.push(divEnd);	// 容器  </div>
		returnHtml.push(divEnd);	// 末尾</div>
		return returnHtml.join("");
	}
	
	
	// 代码块头部
	function _initDate(element,obj,readonly){
		var returnHtml = [];
		returnHtml.push(inlineHtmlHead);
		var lable = '';
		if(element.lable){
			lable = lableHtml.replace("{lable}",element.lable);
		}else{
			lable = lableHtml.replace("{lable}",'');
		}
		returnHtml.push(lable);			 // 表单标题
		returnHtml.push(inputHtmlHead);  // input 容器
		var values = '';
		if(obj && obj != ''){
			values = obj;
		}
		returnHtml.push(dateHtml(element,values,readonly,true));
		returnHtml.push(divEnd);	// 容器  </div>
		returnHtml.push(divEnd);	// 末尾</div>
		return returnHtml.join("");
	}
	
	function selectHtml(element,values,readonly,bindEvent){
		var selectHead = '<select name="{name}" lay-verify="{required}" lay-filter="{filter}"   class="float-left layui-select">';
		var selectEnd = '</select>';
		var optionsHtml = '<option value="{value}" {selected}>{text}</option>';
		
		var selectArr = []; 
		var required = '';
		if(element.required && element.required != ''){
			required = element.required
		}
		if(readonly){
			 if(element.data && element.data != '' && $.isArray(element.data)){
			 	 $.each(element.data,function(){
			 	 	if(this.value == values){
			 	 		selectArr.push(readSpan.replace("{value}",this.text));
			 	 	}
			 	 })
			 }else{
			 	selectArr.push(readSpan.replace("{value}",this.text));
			 }
		}else{
			selectHead  = selectHead.replace("{name}",element.name);
			selectHead  = selectHead.replace('{required}',required).replace('{filter}',element.name);
			selectArr.push(selectHead);   // 绑定文档对象
			// 生成option
			if(element.data && element.data != '' && $.isArray(element.data)){
			 	 $.each(element.data,function(){
			 	 	var tempOption = optionsHtml.replace("{value}",this.value).replace("{text}",this.text);
			 	 	if(this.value == values){
			 	 		tempOption = tempOption.replace("{selected}",'selected');
			 	 	}else{
			 	 		tempOption = tempOption.replace("{selected}",'');
			 	 	}
			 	 	selectArr.push(tempOption);
			 	 })
			 }
			 selectArr.push(selectEnd);
		}
		
		
		// 判断是否需要绑定事件
		if(element.change && typeof element.change === 'string' && bindEvent){
			form.on('select('+element.name+')',function(){
				try{
					var func = 	(new Function("return " + element.change))();
					if(typeof func  === 'function'){
						func.call(this,form,element);
					}
				}catch(e){
				}
			})
		}
		return selectArr.join("");
	}
	
	
	/**
	 *  初始化下拉框
	 * @param {Object} element 
	 * @param {Object} obj
	 */
	function _initSelect(element,obj,readonly){
		var returnHtml = [];
		returnHtml.push(inlineHtmlHead);
		var lable = '';
		if(element.lable){
			lable = lableHtml.replace("{lable}",element.lable);
		}else{
			lable = lableHtml.replace("{lable}",'');
		}
		returnHtml.push(lable);			 // 表单标题
		returnHtml.push(inputHtmlHead);  // input 容器
		var values = '';
		if(obj && obj != ''){
			values = obj;
		}
		
		/**
		 *   select data  [{text:'',value:''},...]
		 */
		returnHtml.push(selectHtml(element,values,readonly,true));
		returnHtml.push(divEnd);	// 容器  </div>
		returnHtml.push(divEnd);	// 末尾</div>
		return returnHtml.join("");
		
	}
	
	
	
	
	
	
	function _initTable(element,obj,readonly){
		var tableHead = '<table class="layui-table" id="{id}" name="{name}">';
		var theadHead = '<thead>';
		var theadEnd  = '</thead>';
		var tbodyHead = '<tbody>';
		var tbodyEnd  = '</tbody>';
		var tableEnd = '</table>';
		// 统计列
		var isTotal = false;
		if(element.totalColumnsName && $.isArray(element.totalColumnsName) && element.totalColumnsName.length>0){
			isTotal = true;
		}
		var isAddClone = element.hasAddBtn;
		
		var hasDelTrBtn = element.hasDelBtn;
		
		// 动态列头 不存在
		if(element.columns == '' || ! $.isArray(element.columns)){
			throw 'table has not find columns!';
		}
		var tableArr = [];
		var tempTr   = [];
		tableArr.push(tableHead.replace('{id}',element.id).replace('{name}',element.name))
		tableArr.push(theadHead);
		tableArr.push("<tr>");
		$.each(element.columns, function(n,i) {
						
			tableArr.push("<th>");
			tableArr.push(this.lable);
		
			tableArr.push("</th>");
			// 只绑定第一行的事件
	
			tempTr.push('<td>'+__initTableElement(this,'',readonly,true)+"</td>")
		});
		if(hasDelTrBtn){
			tableArr.push("<th>操作</th>");
		}
		tableArr.push("</tr>");
		
		
		tableArr.push(theadEnd);
		
		tableArr.push(tbodyHead);
		
		var trArr = [];
		
		//新增模式
		if(obj == ''|| obj == undefined){
			trArr.push('<tr>')
			trArr.push(tempTr.join(""))
			if(hasDelTrBtn){
					var delBtn = '<td><button class="layui-btn layui-btn-sm del'+element.id+'">'+
						    '<i class="layui-icon">&#xe640;</i>'+
						  '</button></td>';
					trArr.push(delBtn);
					tempTr.push(delBtn);
			}
			trArr.push("</tr>");
		}else{
		// 回显模式 清空模板
			$.each(obj,function(){
				var _obj = this;
				trArr.push("<tr>");
				$.each(element.columns, function() {
					trArr.push('<td>'+__initTableElement(this,_obj[this.name],readonly,false)+"</td>")
				});
				if(hasDelTrBtn){
					var delBtn ='<td><button class="layui-btn layui-btn-sm del'+element.id+'">'+
						    '<i class="layui-icon">&#xe640;</i>'+
						  '</button></td>';
						 if(!readonly){
						 	trArr.push(delBtn);
						 	tempTr.push(delBtn);
						 }
				}
				trArr.push("</tr>");
			})
		}
  		tableArr.push(trArr.join(''));
		tableArr.push(tbodyEnd);
		tableArr.push(tableEnd);
		// 添加行按钮 , 绑定事件
		if(isAddClone && !readonly){
			tableArr.push(inlineHtmlHead);
			tableArr.push('<div class="layui-input-inline  float-right">');
			tableArr.push('<button class="layui-btn add'+element.id+'">'+
    					'<i class="layui-icon">&#xe654;</i>'+
  						'</button>');
  			tableArr.push(divEnd);
  			tableArr.push(divEnd);
  			$(document).on("click",".add"+element.id,function(){
				$("#"+element.id).find('tbody').append("<tr>"+tempTr.join("")+"</tr>");
				form.render();
				return false;
			})
  			
		}
		
		// 绑定删除行事件
		if(hasDelTrBtn && !readonly){
			$(document).on("click",".del"+element.id,function(){
				$(this).parent().parent().remove();
				return false;
			})
		}
		// 合计事件
		if(isTotal && !readonly){
			$.each(element.totalColumnsName, function() {
				var _this = this;
				$(document).on("keyup","input[name='"+_this+"']",function(){
					var total = 0;
					$.each($("input[name='"+_this+"']"), function() {
						var vals = $(this).val();
						var currVal = 0;
						try{
							currVal = parseInt(vals);
						}catch(e){
							currVal = 0;
						}
						if(isNaN(currVal)){
							currVal = 0;
						}
						total = total + currVal;
					});
					try{
						$('input[name="total'+_this+'"]').val(total);
					}catch(e){
					}
					return false;
				})
			});
		}
		
		return tableArr.join("");
	}
	
	/**
	 *  不需要容器。生成元素本身
	 * @param {Object} element
	 * @param {Object} obj
	 * @param {Object} readonly
	 */
	function __radioHtml(element,values,readonly,bindEvent){
		var radioHtml = '<input type="radio" name="{name}" lay-filter="{filter}" value="{value}" title="{text}" {checked} {disabled}>';
		var radios = [];
		if(element.data && element.data != '' && $.isArray(element.data)){
		 	 $.each(element.data,function(n,i){
		 	 	var tempHtml = '';
			 	 tempHtml = radioHtml.replace("{name}",element.name).replace("{value}",this.value).
			 	 				replace("{text}",this.text).replace('{filter}',element.name);
			 	 if(readonly){
						tempHtml = tempHtml.replace("{disabled}",'disabled');		 	 	
			 	 }else{
			 	 	    tempHtml = tempHtml.replace("{disabled}",'');		 
			 	 }
			 	 if(values == this.value){
			 	 		tempHtml = tempHtml.replace("{checked}",'checked');		 
			 	 }else{
			 	 		tempHtml = tempHtml.replace("{checked}",'');	
			 	 }
			 	 // 默认选中第一个
			 	 if(values == ''&& n == 0){
			 	 		tempHtml = tempHtml.replace("{checked}",'checked');
			 	 }
			 	 radios.push(tempHtml);
		 	 })
		
		}else{
			radios.push("--");
		}
		
		// 判断是否需要绑定事件
		if(element.click && typeof element.click === 'string' && bindEvent){
			form.on('radio('+element.name+')',function(){ 
				try{
					var func = 	(new Function("return " + element.click))();
					if(typeof func  === 'function'){
						func.call(this);
					}
				}catch(e){
				}
			})
		}
		return radios.join("");
	}
	
	
	
	/**
	 * 
	 * @param {Object} element
	 * @param {string} obj    
	 * @param {boolean} readonly
	 */
	function _initRadio(element,obj,readonly){
		var returnHtml = [];
		returnHtml.push(inlineHtmlHead);
		var lable = '';
		if(element.lable){
			lable = lableHtml.replace("{lable}",element.lable);
		}else{
			lable = lableHtml.replace("{lable}",'');
		}
		returnHtml.push(lable);			 // 表单标题
		returnHtml.push(inputHtmlHead);  // input 容器
		var values = '';
		if(obj && obj != ''){
			values = obj;
		}
		/**
		 *   radio data  [{text:'',value:''},...]
		 */
		returnHtml.push(__radioHtml(element,values,readonly,true));
		returnHtml.push(divEnd);	// 容器  </div>
		returnHtml.push(divEnd);	// 末尾</div>
		return returnHtml.join("");
	}
	
	
	function _checkboxHtml(element,values,readonly,bindEvent){
		var checkboxHtml = '<input type="checkbox" name="{name}" title="{text}" lay-filter="{filter}" value="{value}" lay-skin="primary" {checked} {disabled}> ';
		var checkboxs = [];
		if(element.data && element.data != '' && $.isArray(element.data)){
		 	 $.each(element.data,function(n,i){
		 	 	var tempHtml = '';
			 	 tempHtml = checkboxHtml.replace("{name}",element.name).replace("{value}",this.value).
			 	 				replace("{text}",this.text).replace('{filter}',element.name);
			 	 if(readonly){
						tempHtml = tempHtml.replace("{disabled}",'disabled');		 	 	
			 	 }else{
			 	 	    tempHtml = tempHtml.replace("{disabled}",'');		 
			 	 }
			 	 if(values != ''){
			 	 	
			 	 	if(typeof values == 'string'){
			 	 		if(values == this.value){
			 	 			tempHtml = tempHtml.replace("{checked}",'checked');	
			 	 		}
			 	 	}else if ($.isArray(values)){
			 	 		// 选中包含
			 	 		if($.inArray(values,this.value) >=0){
			 	 			tempHtml = tempHtml.replace("{checked}",'checked');	
			 	 		}
			 	 	}else{
			 	 		tempHtml = tempHtml.replace("{checked}",'');	
			 	 	}
			 	 }else{
			 	 	tempHtml = tempHtml.replace("{checked}",'');	
			 	 }
			 	 checkboxs.push(tempHtml);
		 	 })
		}else{
			returnHtml.push("--");
		}
		// 判断是否需要绑定事件
		if(element.click && typeof element.click === 'string' && bindEvent){
			
			form.on("checkbox("+element.name+")",function(){
				try{
					var func = 	(new Function("return " + element.click))();
					if(typeof func  === 'function'){
						func.call(this);
					}
				}catch(e){
				}
				
			})
		}
		return checkboxs.join("");
	}
	
	/**
	 * 
	 * @param {Object} element
	 * @param {Array} obj
	 * @param {boolean} readonly
	 */
	function _initCheckbox(element,obj,readonly){
	
		var returnHtml = [];
		returnHtml.push(inlineHtmlHead);
		var lable = '';
		if(element.lable){
			lable = lableHtml.replace("{lable}",element.lable);
		}else{
			lable = lableHtml.replace("{lable}",'');
		}
		returnHtml.push(lable);			 // 表单标题
		returnHtml.push(inputHtmlHead);  // input 容器
		var values = '';
		if(obj && obj != ''){
			values = obj;
		}
		/**
		 *   checkbox data  [{text:'',value:''},...]
		 */
		returnHtml.push(_checkboxHtml(element,values,readonly,true));
		returnHtml.push(divEnd);	// 容器  </div>
		returnHtml.push(divEnd);	// 末尾</div>
		return returnHtml.join("");
	}
	
	/**
	 *  表单初始化值
	 */
	function __initElement(element,object,readonly){
		switch(element.type)
			{
				case 'input':
				  return _initInput(element,object,readonly);
				case 'select':
				  return _initSelect(element,object,readonly);
				case 'table':
				  return _initTable(element,object,readonly);
				case 'radio':
				  return _initRadio(element,object,readonly);
				case 'checkbox':
				  return _initCheckbox(element,object,readonly);
				case 'date':
				  return _initDate(element,object,readonly);
				default:
		    }
	}
	
	/**
	 *  表单初始化值
	 */
	function __initTableElement(element,object,readonly,hasBind){
		switch(element.type)
			{
				case 'input':
				  return inputHtml(element,object,readonly,hasBind);
				case 'select':
				  return selectHtml(element,object,readonly,hasBind);
				case 'radio':
				  return __radioHtml(element,object,readonly,hasBind);
				case 'checkbox':
				  return _checkboxHtml(element,object,readonly,hasBind);
				default:
		    }
	}
	
	/**
	 *  动态页面初始化
	 * @param {Object} obj   form 模板
	 * @param {Object} data  需要回显的值
	 */
	function _initPage(obj,data){
		var opt = {
				formId   : '',      // 需要提交的表单ID
				elementId: '',      // 容器ID
				columnName: '',      //  需要保存 到数据库中的字段name
				formData  :'' ,    //  需要初始化form的json对象
				readonly : false ,  // 是否默认只读模式 都不可选只可看
				initEvent: ''   ,   // 初始化时触发
				successEvent  : ''   ,   // 完成html 组装，未插入dom
				errorEvent    : '' ,     // 失败				 
				completeEvent : ''     //  加载完成      
		};
		
		opt = $.extend(opt, obj);
		
		var isReadonly = opt.readonly;
		
		if(!opt.formData || opt.formData == ''){
			throw  'formData can not be null!';
		}

		/*if(opt.columnName == '' || !opt.columnName){
			throw  'columnName can not be null!';
		}*/

		if(!$.isArray(opt.formData)){
			throw  'formData is not Array!';
		}
		var html = '';
		
		if(opt.initEvent && opt.initEvent != ''){
			try{
				opt.initEvent(data);
			}catch(e){
			}
		}
		
		$.each(opt.formData ,function(){
			var values = '';
			if(data ){
				try{
					values = data[this.name];
				}catch(e){
					values = '';
				}
			}
			$("#"+opt.elementId).append(__initElement(this,values,isReadonly));
			// 初始化日期格式
			if(this.type == 'date'){
				
						// 加入渲染
					laydate.render({
						 elem: '#'+this.name,
	  					 type: this.dateType== '' ?'date':this.dateType,
	  					 value: values != '' ?values : new Date(),
	  					 done: function(value, date){
						   		try{
									var func = 	(new Function("return " + this.done))();
									if(typeof func  === 'function'){
										func.call(this);
									}
								}catch(e){
								}
						 }
					})
			}
		})
		if(opt.successEvent && opt.successEvent != ''){
			try{
				opt.successEvent(html,data);  // 返回生成的字符串，和值
			}catch(e){
			}
		}
		form.render();
		if(opt.completeEvent && opt.completeEvent != ''){
			try{
				opt.completeEvent(data);
			}catch(e){
			}
		}
	}
	
	function loadElementForm(element){
		var nameIndex = {}; //数组 name 索引
		// 操作克隆对象 
		// jquery clone 对select有bug
//		var cloneForm = element.clone();
//		var tableArr = cloneForm.find("table");
//		cloneForm.find("table").remove();
		var fieldElem = element.find('input,select,textarea'); //获取所有表单域
		var resultObj = loadElementDom(fieldElem);
	
//	    if(tableArr != null){
//	    	$.each(tableArr, function() {
//	    		var itemName = this.id;
//		    	var trs = $(this).find("tbody tr");
//		    	var trArr = [];
//		    	$.each(trs, function() {
//					fieldElem = $(this).find('input,select,textarea');	    		
//		    		trArr.push(loadElementDom(fieldElem))
//		    	});
//		    	resultObj[itemName] = trArr;
//	    	});
//	    }
	    
		return resultObj;
	}
	
	function loadElementDom(ele){
	    	var tempObj = {};
		    layui.each(ele, function(_, item){
			      item.name = (item.name || '').replace(/^\s*|\s*&/, '');
			      if(!item.name) return;
			      //用于支持数组 name
			      if(/^.*\[\]$/.test(item.name)){
			        var key = item.name.match(/^(.*)\[\]$/g)[0];
			        nameIndex[key] = nameIndex[key] | 0;
			        item.name = item.name.replace(/^(.*)\[\]$/, '$1['+ (nameIndex[key]++) +']');
			      }
			      
			      if(/^checkbox|radio$/.test(item.type) && !item.checked) return;      
			      tempObj[item.name] = item.value;
		    });
		    return tempObj;
	}
	
	function setElementValue(ele,data){
	    	var tempObj = {};
		    layui.each(ele, function(_, item){
			      item.name = (item.name || '').replace(/^\s*|\s*&/, '');
			      if(!item.name) return;
			      //用于支持数组 name
			      if(/^.*\[\]$/.test(item.name)){
			        var key = item.name.match(/^(.*)\[\]$/g)[0];
			        nameIndex[key] = nameIndex[key] | 0;
			        item.name = item.name.replace(/^(.*)\[\]$/, '$1['+ (nameIndex[key]++) +']');
			      }
			      
			      if(/^checkbox|radio$/.test(item.type) && !item.checked) return;      
			      tempObj[item.name] = item.value;
		    });
		    return tempObj;
	}
	
	
	 //获取url中的参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]);
        
        var local = localStorage.getItem("dataField");
        if(local != '' && local != null){
        	localStorage.removeItem("dataField");
        	var tempObj = $.parseJSON(local);
        	return tempObj[name];
        }
        return null; //返回参数值
    }
    
    function setValueToDom(elem,jsonValue) {
    	var obj;
    	if(typeof elem == 'string'){
    		obj = $("#"+elem);
    	}else if(typeof elem == 'object'){
    		obj = elem;
    	}else{
    		throw 'elem is unidentifiable';
    	}
        $.each(jsonValue, function (name, value) {
        	//T-name
            var _tag = obj.find("[T-name=" + name + "]");
            if(_tag.length > 0){
                	var isDict = $(_tag).data("code");
                	var isTime = $(_tag).data("time");
                	var types =  _tag[0].tagName;
                	if(types == "INPUT"){
	 	                	if (_tag.attr("type")== "radio" || _tag.attr("type")== "checkbox") {
	 	                		_tag.each(function(){
		 		                     if($(this).val()==value)
		 		                        $(this).attr("checked", "checked");
		 		                });
	 		                }else{
	 		                	_tag.val(value);
	 		                }
 	                }else if(types == "SELECT"){
 	                	_tag.val(value);
 	                }else if(types == 'SPAN'){
 	                	if(isDict){
 	                		var text = dict.getTextByCode(isDict,value);
 	                		_tag.text(text);
	                	}else if(isTime){
	                		var unixTimestamp = new Date( parseInt(value)) ;
	                		_tag.text(unixTimestamp.format("yyyy年MM月dd日"));
	                	}else {
	                		_tag.text(value);
	                	}
                	}
            }
        });
     }
    // 时间戳转字符串
    function dateToStr(date){
        var oDate = new Date(date),  
        oYear = oDate.getFullYear(),  
        oMonth = oDate.getMonth()+1,  
        oDay = oDate.getDate(),  
        oHour = oDate.getHours(),  
        oMin = oDate.getMinutes(),  
        oSen = oDate.getSeconds(),  
        oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay) +' '+ getzf(oHour) +':'+ getzf(oMin) +':'+getzf(oSen);//最后拼接时间  
        return oTime;  
    }; 
    //补0操作
  function getzf(num){  
      if(parseInt(num) < 10){  
          num = '0'+num;  
      }  
      return num;  
  }
	var pageUtil = {
		initPage:_initPage,
		loadElementForm:loadElementForm,
		loadElementDom:loadElementDom,
		getUrlParam:getUrlParam,
		setValueToDom:setValueToDom,
		dateToStr:dateToStr
	}
	exports("pageUtil",pageUtil);
});