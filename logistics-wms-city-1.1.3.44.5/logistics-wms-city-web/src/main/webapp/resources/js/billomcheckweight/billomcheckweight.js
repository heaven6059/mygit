var billomcheckweight = {};

//========================加载仓库信息 BEGIN======================================
billomcheckweight.locno;
billomcheckweight.loadLoc = function(formObj){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billomcheckweight.locno = data.locno;
		}
	});
};
//========================加载仓库信息 END======================================

billomcheckweight.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billomcheckweight.openUI = function(opt){
	$('#openUI').window({
		title:opt
	});
	$('#openUI').window('open');
};
billomcheckweight.closeUI = function(opt){
	$('#openUI').window('close');
	$("#opt").val("");
};
//预计重量
billomcheckweight.loadDataGrid = function(param){
	$("#locnoAdd").val(billomcheckweight.locno);
	var labelAdd =  $('#labelAdd').val();
	if(labelAdd!="") {
		var filldatagid = function(returnData){
				$('#dataGridJGAdd').datagrid({data:[]});
				$('#dataGridJGAdd').datagrid({data:returnData.lableDtl});
				var weight = 0;
				//debugger;
				if(returnData.lableDtl.length > 0) {
					for(var i=0;i<returnData.lableDtl.length;i++){
						weight = weight + returnData.lableDtl[i].itemWeight;
					}
			 	}
				$("#weight").val(weight);
//				$("#locnoAdd").val(returnData.locno);
				$("#containerNoAdd").val(returnData.containerNo);
				$("#containerTypeAdd").val(returnData.containerType);
				$("#labelNoAdd").val(returnData.labelNo);
		};
		if(param){
			billomcheckweight.ajaxRequest(
				BasePath+'/bill_om_check_weight/get_dtl_by_labelno.json',param,filldatagid);
		}else{
			billomcheckweight.ajaxRequest(
				BasePath+'/bill_om_check_weight/get_dtl_by_labelno.json',{},filldatagid);
		}
	}
};

//称重保存
billomcheckweight.save = function(){
	//1.校验必填项
	var fromObj=$('#dataForm');	
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
		return ;
	}
	//实际重量
	//var realWeight =  $('#realWeight').val().trim();
	
		var url = BasePath+'/bill_om_check_weight/save_billomcheckweight';
		wms_city_common.loading("show");
		fromObj.form('submit', {
					url: url,
					onSubmit: function(){
					
					},
					success: function(){
						wms_city_common.loading(); 
						alert('保存成功!');
						 //4.保存成功,清空表单
						$("#dataForm").form("clear");
						$('#dataGridJGAdd').datagrid('clearData');
						//	billomcheckweight.closeUI();
						return;
				    },
					error:function(){
						wms_city_common.loading();
						alert('新增失败,请联系管理员!',2);
					}
		});
	
	
};

$(document).ready(function(){
	//加载仓库信息
	billomcheckweight.loadLoc();
	//判断扫描箱号是否合法
	$("#labelAdd").blur(function(){
		billomcheckweight.searchItem('/bill_om_check_weight/get_dtl_by_labelno.json');
	});
	$("#weight").attr("disabled",true);
});

//称重保存查询
billomcheckweight.searchItem = function(searchURL){
//	var date = {locno:'006',name:'zzz',}
	var fromObjStr=convertArray($('#dataForm').serializeArray());
	billomcheckweight.loadDataGrid(eval("(" +fromObjStr+ ")"));
};

//称重查询
billomcheckweight.searchChecked = function(){
	var labelNo =  $('#labelNo').val().trim();
	if(labelNo=="") {
		alert("请输入扫描箱号");   
        return;   
	}
	
	var fromObjStr=convertArray($('#checkedForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_check_weight/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	$.extend(reqParam,{locno:billomcheckweight.locno});
	$( "#dataGridJG").datagrid( 'options' ).queryParams=reqParam;
	$( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load');
};

//清空
billomcheckweight.searchClearchecked = function(){
	$("#checkedForm").form("clear");
	$("#dataGridJG").datagrid('clearData');
};
//主要是推荐这个函数。它将jquery系列化后的值转为name:value的形式。
function convertArray(o) { 
	var v = {};
	for ( var i in o) {
		if (typeof (v[o[i].name]) == 'undefined')
			v[o[i].name] = o[i].value;
		else
			v[o[i].name] += "," + o[i].value;
	}
	return JSON.stringify(v);
} 
