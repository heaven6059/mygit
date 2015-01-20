var bmlocnogroup = {};
bmlocnogroup.locno;
bmlocnogroup.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bm_locno_group/list.json?locno='+bmlocnogroup.locno,'title':'仓库组别列表','pageNumber':1 });
};
bmlocnogroup.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
bmlocnogroup.initLocno = function(data){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			bmlocnogroup.locno = data.locno;
		}
	});
};
//弹出新增页面
bmlocnogroup.showAdd=function(){
	
	//设置标题
	$('#showDialog').window({ title:"新增" });
	
	$('#sysNo_dataForm,#businessType_dataForm,#groupCode_dataForm').combobox({ disabled: false });
	$('#personNum_dataForm').removeAttr("disabled");
	
	bmlocnogroup.clearFormAndTip();
	//仓库编码设置
	$('#circleNo').removeAttr("readonly");
	$("#creatorDiv,#editorDiv,#info_update").hide();
	$("#info_save,#info_close").show();
	$('#showDialog').window('open'); 
};
bmlocnogroup.clearFormAndTip = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
};
//弹出修改页面
bmlocnogroup.showModify = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length != 1){
		alert('请选择一条记录！',1);
		return;
	}
	bmlocnogroup.showEdit(checkedRows[0],1);
};
//弹出详情页面
bmlocnogroup.showEdit = function(rowData,flag){
	
	var titleName = "修改";
	if(flag == 1){
		$("#creatorDiv,#editorDiv,#info_update,#info_close").show();
		$("#info_save").hide();
		$('#personNum_dataForm').removeAttr("disabled");
	}else{
		titleName = "查看";
		$("#creatorDiv,#editorDiv").show();
		$("#info_save,#info_update,#info_close").hide();
		
		$('#personNum_dataForm').attr({ "disabled": "disabled" });
	}
	
	//设置标题
	$('#showDialog').window({ title:titleName });
	
	$('#sysNo_dataForm,#businessType_dataForm,#groupCode_dataForm').combobox({ disabled: true });
	
	//设置信息
	bmlocnogroup.setDetail(rowData);
	//弹窗
	$("#showDialog").window('open'); 
};

bmlocnogroup.setDetail = function(rowData){
	$('#sysNo_dataForm').combobox("select",rowData.sysNo);
	$('#businessType_dataForm').combobox("select",rowData.businessType);
	$('#groupCode_dataForm').combobox("select",rowData.groupCode);
	$("#personNum_dataForm").val(rowData.personNum);
	//$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
	$("#creator").val(rowData.creator);
	$("#createtm").val(rowData.createtm);
	$("#editor").val(rowData.editor);
	$("#edittm").val(rowData.edittm);
};

bmlocnogroup.checkExistFun = function(url,checkColumnJsonData){
	var checkExist=false;
 	$.ajax({
		  type: 'POST',
		  url: url,
		  data: checkColumnJsonData,
		  cache: true,
		  async:false, // 一定要
		  success: function(totalData){
			  totalData = parseInt(totalData,10);
			  if(totalData>0){
				  checkExist=true;
			  }
		  }
     });
 	return checkExist;
};

bmlocnogroup.clearAll = function(formId){
	$("#"+formId).form("clear");
};

bmlocnogroup.loadSysName = function(sysName){
	$('#sysNameHide_dataForm').val(sysName);
};

//新增保存
bmlocnogroup.addLocnoGroup = function(){
	var formId = 'dataForm';
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return;
     }
     
     if(!validateNumber($('#personNum_dataForm').val())){
     	alert('人数必须是正整数!');
     	return;
     }
     
	 //3. 保存
     wms_city_common.loading("show");
     var url = BasePath+'/bm_locno_group/addLocnoGroup?locno='+bmlocnogroup.locno;
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
				
			},
			success: function(data){
				wms_city_common.loading();
				
				var dataObj = eval('(' + data + ')'); 
				
				if(dataObj.flag =='success'){
					 alert('新增成功!');
					 bmlocnogroup.clearAll(formId);
					 $('#showDialog').window('close'); 
					 //4.保存成功,清空表单
					 bmlocnogroup.loadDataGrid();
					 return;
				 }else{
					 if(null!=dataObj.resultMsg  && ''!=dataObj.resultMsg){
						 alert(dataObj.resultMsg,1);
					 }else{
						 alert('新增异常,请联系管理员!',2);
					 }
				 }
		    },
			error:function(){
				alert('新增失败,请联系管理员!',2);
			}
	   });
};
//修改
bmlocnogroup.modifyLocnoGroup = function(){
	var formId = 'dataForm';
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    
    if(!validateNumber($('#personNum_dataForm').val())){
    	alert('人数必须是正整数!');
    	return;
    }
    
    //3. 保存
    wms_city_common.loading("show");
    var url = BasePath+'/bm_locno_group/modifyLocnoGroup?locno='+bmlocnogroup.locno;
    fromObj.form('submit', {
   			url: url,
   			onSubmit: function(){
				
   			},
   			success: function(data){
   				
   				wms_city_common.loading();
   				
   				var dataObj = eval('(' + data + ')'); 
				
				if(dataObj.flag =='success'){
					 alert('修改成功!');
					 bmlocnogroup.clearAll(formId);
					 $('#showDialog').window('close'); 
					 //4.保存成功,清空表单
					 bmlocnogroup.loadDataGrid();
					 return;
				 }else{
					 if(null!=dataObj.resultMsg  && ''!=dataObj.resultMsg){
						 alert(dataObj.resultMsg,1);
					 }else{
						 alert('修改异常,请联系管理员!',2);
					 }
				 }
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	});
    
};

//删除
bmlocnogroup.deleteLocnoGroup=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","您确定要删除这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var locnogroupStrs = [];
        	$.each(checkedRows, function(index, item){
        		locnogroupStrs.push(item.locno+"#"+item.sysNo+"#"+item.groupCode+"#"+item.businessType);
        	});               
            //2.绑定数据
        	var data={
        	    "locnogroupStrs":locnogroupStrs.join(",")
        	};
        	wms_city_common.loading("show");
        	//3校验仓库下是否有绑定用户时
        	var url = BasePath+'/bm_locno_group/deleteLocnoGroup';
        	bmlocnogroup.ajaxRequest(url,data,function(result,returnMsg){
        		          wms_city_common.loading();
						  if(result.flag =='success'){
								 alert('删除成功!');
								 //4.保存成功,清空表单
								 bmlocnogroup.loadDataGrid();
								 return;
						   }else{
								 if(null!=result.resultMsg  && ''!=result.resultMsg){
									 alert(result.resultMsg,1);
								 }else{
									 alert('删除异常,请联系管理员!',2);
								 }
						   }
        	}); 
        }
	});     
};
//查询仓库组别信息
bmlocnogroup.searchLocnoGroup = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bm_locno_group/list.json?locno='+bmlocnogroup.locno;
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};
//清楚查询条件
bmlocnogroup.searchClear = function(){
	$('#searchForm').form("clear");
	searchForm('/bmlocnogroup/list.json?locno='+bmlocnogroup.locno);
};
//取消按钮
bmlocnogroup.closeCircle = function(){
	$('#showDialog').window('close');
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

function validateNumber(vnumber){
    var patrn=/^[1-9]\d*$/; 
 	if (!patrn.exec(vnumber)) {
        return false;   
    }else
    {
        return true;  
    }   
}

//组别
bmlocnogroup.groupCodes = {};
bmlocnogroup.groupCodesFormatter = function(value, rowData, rowIndex){
	return bmlocnogroup.groupCodes[value];
};

//品牌库
bmlocnogroup.sysNames = {};
bmlocnogroup.sysNameFormatter = function(value, rowData, rowIndex){
	return bmlocnogroup.sysNames[value];
};

//业务类型
bmlocnogroup.businessTypes = {};
bmlocnogroup.businessTypesFormatter = function(value, rowData, rowIndex){
	return bmlocnogroup.businessTypes[value];
};

$(document).ready(function(){
	
	bmlocnogroup.initLocno();
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNo_dataEditForm]',$('#dataEditForm'))},//修改
			{"sysNoObj":$('input[id=sysNo_dataViewForm]',$('#dataViewForm'))},//详情
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	//初始化品牌库(新增)
	wms_city_common.comboboxLoadFilter(
			["sysNo_dataForm"],
			null,
			null,
			null,
			true,
			[false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
			{},
			bmlocnogroup.sysNames,
			null);
	
	//初始化组别
	wms_city_common.comboboxLoadFilter(
			["groupCodeCondition","groupCode_dataForm","groupCode_dataEditForm","groupCode_dataViewForm"],
			null,
			null,
			null,
			true,
			[true,false,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_LOCNO_GROUP_CODE',
			{},
			bmlocnogroup.groupCodes,
			null);
	
	//初始化业务类型
	wms_city_common.comboboxLoadFilter(
			["businessTypeCondition","businessType_dataForm"],
			'paperType',
			'billName',
			'valueAndText',
			false,
			[true],
			BasePath+'/bill_acc_control/listBillAccControlGroupByBillName',
			{},
			bmlocnogroup.businessTypes,
			null);
	
});