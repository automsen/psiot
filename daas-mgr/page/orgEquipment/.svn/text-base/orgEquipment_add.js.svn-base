layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer', 'T'], function() {
	var form = layui.form,
		T = layui.T,
		$ = layui.$,
		layer = layui.layer;

	form.on('select(netType)', function(data) {
		var NetType = data.value;
		/*if(NetType == "0905"){*/
		if(NetType == "0904") {
			$("#Type").empty();
			var htmlStr = $("#elecTemp3").html();
			$("#Type").append(htmlStr);
		} else
		if((NetType == "0902") || (NetType == "0903") || (NetType == "0905")) {
			$("#Type").empty();
			var htmlStr = $("#elecTemp5").html();
			$("#Type").append(htmlStr);
		} else
			/* if((NetType == "0902")||(NetType == "0904")||(NetType == "0907")||(NetType == "0910")){*/
			if(NetType == "0901") {
				$("#Type").empty();
				var htmlStr = $("#elecTemp4").html();
				$("#Type").append(htmlStr);
				var t_select = $(".modelNetType-select");
				if(t_select.length > 0) {
					t_select.each(function() {
						//T.js方法，加载下拉框数据
						$(this).loadSelect({
							url: "equip/all?netType=" + NetType,
							complete: function(obj, res) {
								form.render();
							},
							error: function(obj, res) {

							}
						});
					})
				}
			}
		else {
			$("#Type").empty();
		}
	});

	function twoEquip() {

		$("#elec").empty();
		var htmlStr = $("#elecTempl").html();
		$("#elec").append(htmlStr);
		//下拉框
		var t_select = $(".elecMeterType-select");
		if(t_select.length > 0) {
			t_select.each(function() {
				//T.js方法，加载下拉框数据
				$(this).loadSelect({
					complete: function(obj, res) {
						form.render();
					},
					error: function(obj, res) {

					}
				});
			})
		}

		var equipCateg = "0201";
		T.reqAjax({
			btnId: "subBtn",
			method: "get",
			url: "model/all?equipType=" + equipCateg,
			contentType: "application/json",
			dataType: 'json',
			success: function(res) {
				var data = res.data;

				$('#modelId').html('');
				var html = '';
				if(equipCateg == "0201") {
					for(var equip of data) {
						if(equip.equipType == equipCateg) {
							html += '<option value=' + equip.modelId + '>' + equip.modelName + '</option>';
						}
					}
					$('#modelId').append(html);
				}
				form.render('select');
			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		})

	}

	form.on('select(equipType)', function(data) {

		var equipCateg = data.value;
		T.reqAjax({
			btnId: "subBtn",
			method: "get",
			url: "model/all?equipType=" + equipCateg,
			contentType: "application/json",
			dataType: 'json',
			success: function(res) {
				var data = res.data;
				//console.log(data);	
				$('#modelId').html('');
				var html = '';
				if(equipCateg == "0201") {
					for(var equip of data) {
						if(equip.equipType == equipCateg) {
							html += '<option value=' + equip.modelId + '>' + equip.modelName + '</option>';
						}
					}
					$('#modelId').append(html);

					$("#elec").empty();
					var htmlStr = $("#elecTempl").html();
					$("#elec").append(htmlStr);
					//下拉框
					var t_select = $(".elecMeterType-select");
					if(t_select.length > 0) {
						t_select.each(function() {
							//T.js方法，加载下拉框数据
							$(this).loadSelect({
								complete: function(obj, res) {
									form.render();
								},
								error: function(obj, res) {

								}
							});
						})
					}

				} else if(equipCateg == "0202") {
					for(var equip of data) {
						if(equip.equipType == equipCateg) {
							html += '<option value=' + equip.modelId + '>' + equip.modelName + '</option>';
						}
					}
					$('#modelId').append(html);
					$("#elec").empty();
				} else if(equipCateg == "0211") {
					for(var equip of data) {
						if(equip.equipType == equipCateg) {
							html += '<option value=' + equip.modelId + '>' + equip.modelName + '</option>';
						}
					}
					$('#modelId').append(html);
					$("#elec").empty();
				} else if(equipCateg == "0212") {
					for(var equip of data) {
						if(equip.equipType == equipCateg) {
							html += '<option value=' + equip.modelId + '>' + equip.modelName + '</option>';
						}
					}
					$('#modelId').append(html);
					$("#elec").empty();
				} else {
					$("#elec").empty();
				}

				form.render('select');

			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		})

	});

	//监听提交
	form.on('submit(tijiao)', function(data) {
		var p = data.field;
		var orgEquipment = JSON.stringify(p);
		T.reqAjax({
			btnId: "subBtn",
			method: "post",
			param: orgEquipment,
			url: "equip",
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