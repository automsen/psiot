layui.config({
	base: '../../js/' //你存放新模块的目录，注意，不是layui的模块目录
}).use(['jquery', 'form', 'layer', 'T', 'laytpl'], function() {
	var form = layui.form,
		T = layui.T,
		$ = layui.$,
		laytpl= layui.laytpl,
		layer = layui.layer;

	form.on('select(equipCateg)', function(data) {
		var equipCateg = data.value;

		T.reqAjax({
			btnId: "subBtn",
			method: "get",
			url: "dict/all?parentCode=" + equipCateg,
			contentType: "application/json",
			dataType: 'json',
			success: function(res) {
				var data = res.data;
				$('#equipType').html('');
				var html = '';
				if(equipCateg == "0101") {
					for(var equip of data) {
						if(equip.parentCode == equipCateg) {
							html += '<option value=' + equip.dictCode + '>' + equip.dictName + '</option>';
						}
					}
					$('#equipType').append(html);
					//twoEquip();

				} else if(equipCateg == "0102") {
					for(var equip of data) {
						if(equip.parentCode == equipCateg) {
							html += '<option value=' + equip.dictCode + '>' + equip.dictName + '</option>';
						}
					}
					$('#equipType').append(html);
				}
				form.render('select');
			},
			error: function(res) {
				$.tBox.error(res.message);
			}
		})
	});
	
	//下发按钮
	$(document).on("click", ".send", function() {
		openWin($(this)[0].id);
	})
	
	function openWin(equipId){
		T.reqAjax({
			method: "get",
			url: "terminal/dtu/" + equipId,
			contentType: "application/json",
			dataType: 'json',
			success: function(res) {
				var htmlTpl = $("#backTempl").html();
				laytpl(htmlTpl).render(res.data, function(html){
					layer.open({
						title: '下发确认',
						content: html,
						area: ['650px', '400px'],
						btn: ['确认', '取消'],
						success:function(layero, index){
							
						},
						yes: function(index, layero) {
							sendCmd(res.data);
							layer.close(index);
						}
					})
		    	});
			}
		})
	}
	
	function sendCmd(data){
		T.reqAjax({
			method: "post",
			url: "terminal/dtu/send/"+data.equipId+"/0",
			contentType: "application/json",
			dataType: 'json',
			//param:JSON.stringify(),
			success: function(res) {
				if(res.statusCode == "000000"){
					layer.msg('已下发');
				}else{
					layer.msg(res.message);
				}
			}
		});
	}

});