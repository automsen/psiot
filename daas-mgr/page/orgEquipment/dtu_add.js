layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['element', 'jquery', 'form', 'layer', 'T', 'laydate','pageUtil'], function() {
	var form = layui.form,
		T = layui.T,
		$ = layui.$,
		laydate = layui.laydate,
		layer = layui.layer;
	var pageUtil = layui.pageUtil;
	var element = layui.element;
	
	laydate.render({ 
		elem: '#installationTime'
		,type: 'datetime'
	});
	
	//
	init();
	
	function init(){
		//加载页面默认样式
		setOneRecordHot();
		setOneRecordElec();
		setOneRecordWater();
		/*$("#hotInfo").append($("#addButton").html());
		$("#elecInfo").append($("#addButton").html());
		$("#waterInfo").append($("#addButton").html());*/
	}
	
	function setOneRecordHot(){
		var tr = $(".tr-hot-clone").clone();
		tr.removeClass("tr-hot-clone");
		$("#hotInfo").append(tr);
		form.render("select");
	}
	
	function setOneRecordElec(){
		var tr2 = $(".tr-elec-clone").clone();
		tr2.removeClass("tr-elec-clone");
		$("#elecInfo").append(tr2);
		form.render("select");
	}
	
	function setOneRecordWater(){
		var tr3 = $(".tr-water-clone").clone();
		tr3.removeClass("tr-water-clone");
		$("#waterInfo").append(tr3);
		form.render("select");
	}
	
	//默认选中热泵
	var equipmentIndex = 0;
	element.on('tab(test)', function(data){
		equipmentIndex = data.index;
	});
	
	//页面总计数
	$(document).on("click", ".add", function(){
		if(equipmentIndex == 0){
			setOneRecordHot();
		}else if(equipmentIndex == 1){
			setOneRecordElec();
		}else{
			setOneRecordWater();
		}
	});
	
	//获取提交的dtu下联设备
	function getDataFromTab(){
		var hots = $(".hot-item");
		var dteList = [];
		hots.each(function(){
			var hot = pageUtil.loadElementForm($(this));
			if(hot.dtuEquipNumber != "" ){
				hot.equipType = "150000";
				hot.equipNumber = hot.dtuEquipNumber;
				dteList.push(hot);
			}
		});
		
		var elecs = $(".elec-item");
		elecs.each(function(){
			var elec = pageUtil.loadElementForm($(this));
			if(elec.dtuEquipNumber != ""){
				elec.equipType = "110000";
				elec.equipNumber = elec.dtuEquipNumber;
				dteList.push(elec);
			}
		});
		
		var waters = $(".water-item");
		waters.each(function(){
			var water = pageUtil.loadElementForm($(this));
			if(water.dtuEquipNumber != ""){
				water.equipType = "120000";
				water.equipNumber = water.dtuEquipNumber;
				dteList.push(water);
			}
		});
		return dteList;
	}
	
	//监听提交
	form.on('submit(tijiao)', function(data) {
		var dteList = getDataFromTab();
		if(dteList.length > 5){
			layer.msg('下联设备数不得大于5');
			return false;
		}
		var p = data.field;
		p.childrenNum = dteList.length;
		p.workingMode = p.dtuWorkingMode0 + p.dtuWorkingMode1;
		p.functionDigit = p.dtuFunctionDigit0 + p.dtuFunctionDigit1;
		p.dteList = dteList;
		p.netNumber = p.equipNumber;
		p.isDirect = "1";
		p.netTypeCode = "306";
		var orgEquipment = JSON.stringify(p);
		T.reqAjax({
			btnId: "subBtn",
			method: "post",
			param: orgEquipment,
			url: "terminal/dtu",
			contentType: "application/json",
			dataType: 'json',
			success: function(res) {
				layer.msg('添加成功');
				data.form.reset();
			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		})
		return false;
	});
});