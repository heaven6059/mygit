var cscellpacksetting = {};
cscellpacksetting.locno;

cscellpacksetting.areaTypeData = {};

$(document).ready(function(){
	//初始化仓别
	cscellpacksetting.loadLoc();
	//加载列表数据
	//cscellpacksetting.loadDataGrid();

	//加载包装规格
	cscellpacksetting.loadPackSpec();
	$("#info_add").click(cscellpacksetting.save_do);
	$("#info_edit").click(cscellpacksetting.edit_do);
	//初始化委托业主
	wms_city_common.comboboxLoadFilter(
			["ownerNo"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[false],
			BasePath+'/entrust_owner/get_biz',
			{},
			cscellpacksetting.ownerNo,
			null);
	//初始化储位类型
	wms_city_common.comboboxLoadFilter(
			["areaType"],
			null,
			null,
			null,
			true,
			[false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_TYPE',
			{},
			cscellpacksetting.areaTypeData,
			null);
});

//加载仓别信息
cscellpacksetting.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			cscellpacksetting.locno = data.locno;
		}
	});
};
//========================委托业主========================
cscellpacksetting.ownerNo = {};
cscellpacksetting.ownerNoFormatter = function(value, rowData, rowIndex){
	return cscellpacksetting.ownerNo[value];
};
cscellpacksetting.tansforOwner = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//========================委托业主END========================
cscellpacksetting.closeUI = function(opt){
	$("#showDialog").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#showDialog').window('close');
	$("#lineNo").val("");
};
//加载数据
cscellpacksetting.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/cs_cell_pack_setting/getByPage.json?locno='+cscellpacksetting.locno,
    			'pageNumber':1
    		});
};

cscellpacksetting.loadPackSpec = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/item_pack/selectPackSpec',
		success : function(data) {
			$('#packSpec').combobox({
				 valueField:"PACKSPEC",
			     textField:"PACKSPEC",
			     data:data.data,
			     panelHeight:150,
			  }).combobox("select",data.data[0].PACKSPEC);
		}
	});
};
//将数组封装成一个map
cscellpacksetting.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

cscellpacksetting.areaTypeFormatter = function(value, rowData, rowIndex){
	return cscellpacksetting.areaTypeData[value];
};
//新增
cscellpacksetting.addInfo = function(){
	$('#showDialog').window({
		title:"新增",
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",true);
		}
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('enable');
			$(this).combobox('select',"");
		}
	});
	$("#limitQty").val("");
	$("input[name='limitQty']").val("");
	$("#info_add").show();
	$("#info_edit").hide();
	$("#showDialog").window('open'); 
};
//保存
cscellpacksetting.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     var save = true;
     if(!cscellpacksetting.checkExist()){
     	$.messager.confirm("确认","该仓别下的包装设置已经存在,是否更新", function (r){  
	        if (r) {
	        	cscellpacksetting.edit_do();
	        }
    	});
    	save = false;
     }
	 //2. 保存
	 if(save){
		wms_city_common.loading("show","正在保存......");
	 	var url = BasePath+'/cs_cell_pack_setting/addCsCellPackSetting';
	     fromObj.form('submit', {
				url: url,
				onSubmit: function(){
				},
				success: function(data){
					wms_city_common.loading();
					if(data=="success"){
						$("#showDialog").window('close'); 
						 alert('新增成功!');
						 cscellpacksetting.loadDataGrid();
					}else{
						alert('新增失败,请联系管理员!',2);
					}
			    }
		   });
	 }
};

cscellpacksetting.checkExist = function(){
	var areaType = $("#areaType").combobox("getValue");
	var packSpec = $("#packSpec").combobox("getValue");
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/cs_cell_pack_setting/checkExist',
		  data:{
			"areaType":areaType,
			"packSpec":packSpec 	
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

cscellpacksetting.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
cscellpacksetting.ajaxRequest2 = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//删除
cscellpacksetting.del = function(){
	//var rows = $("#dataGridJG").datagrid("getSelections");// 获取所有选中的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.locno+"|"+item.packSpec+"|"+item.areaType);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/cs_cell_pack_setting/deleteCsCellPackSetting';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	wms_city_common.loading("show","正在删除......");
        	cscellpacksetting.ajaxRequest(url,data,function(result){
        		wms_city_common.loading();
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 cscellpacksetting.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

cscellpacksetting.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

cscellpacksetting.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		cscellpacksetting.edit(checkedRows[0],"edit");
	}
};

//修改
cscellpacksetting.edit = function(rowData,type){
	var title = "详情";
	if(type=="edit"){
		title = "修改";
	}
	//设置标题
	$('#showDialog').window({
		title:title,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	//设置信息
	cscellpacksetting.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
};

cscellpacksetting.detail = function(rowData,type){
	$('#dataForm').form('load',rowData);
	
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('disable');
		}
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",false);
		}
	});
	$("#ownerNo").combobox('enable');
	
	//隐藏新增按钮，显示编辑按钮
	$("#info_add").hide();
	if(type=="edit"){
		$("#info_edit").show();
	}else{
		$("#info_edit").hide();
	}
};

//修改
cscellpacksetting.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    wms_city_common.loading("show","正在保存......");
    var url = BasePath+'/cs_cell_pack_setting/editCsCellPackSetting';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				 wms_city_common.loading();
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 cscellpacksetting.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};



//查询区域信息
cscellpacksetting.searchArea = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/cs_cell_pack_setting/getByPage.json?locno='+cscellpacksetting.locno;
	
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

cscellpacksetting.searchLocClear = function(){
	$('#searchForm').form("clear");
};

function convertArray(o) { 
	var v = {};
	for ( var i in o) {
		if (typeof (v[o[i].name]) == 'undefined')
			v[o[i].name] = o[i].value;
		else
			v[o[i].name] += "," + o[i].value;
	}
	return JSON.stringify(v);
};

