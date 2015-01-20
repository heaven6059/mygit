/***
 ** 拣货任务分派
 **/
var outstockdirect = {};

outstockdirect.locno;


//加载Grid数据Utils
outstockdirect.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//查询
outstockdirect.searchLocateNoDataGrid = function(){
	
	var validateForm=  $('#searchForm').form('validate');
    if(validateForm==false){
        return ;
    }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/outstockdirect/queryLocateNoByMore.json';
	
    //3.加载明细
    $( "#locateNoDataGrid").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#locateNoDataGrid").datagrid( 'options' ).url=queryMxURL;
    $( "#locateNoDataGrid").datagrid( 'load' );
    
};


//清空查询条件
outstockdirect.clearSearchForm = function(){
	$("#searchForm").form('clear');
	$('#brandNo').combobox("loadData",[]); 
};



//将数组封装成一个map
outstockdirect.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

outstockdirect.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//加载查询
outstockdirect.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/billomexp/list.json','title':'出库订单列表','pageNumber':1 });
};
//加载明细
outstockdirect.loadDataDetailGrid = function(rowData){
	$('#moduleEdit').datagrid(
			{
				'url':BasePath+'/billomexpdtl/list.json?expNo='+rowData.expNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo,
//				'queryParams': {
//					expNo: rowData.expNo,
//					locno: rowData.locno,
//					ownerNo: rowData.ownerNo
//				},
				'pageNumber':1 
			}
	);
	//$("#moduleEdit").datagrid( 'load' );
};

outstockdirect.checkExistFun = function(url,checkColumnJsonData){
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

//查询出库订单
outstockdirect.searchBillOmExp = function(){
//	//var fromObjStr=convertArray($('#searchForm').serializeArray());
//	var queryMxURL=BasePath+'/billomexp/list.json';
//	
//    //3.加载明细
//    //$( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
//    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
//    $( "#dataGridJG").datagrid( 'load' );
    
};

//清楚条件
outstockdirect.clearForm = function(){
	$('#searchForm').form("clear");
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

//修改出库订单主表信息
outstockdirect.modifyBillOmExp = function(id){
    var fromObj=$('#'+id);
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    var url = BasePath+'/billomexp/modifyBillOmExp';
    fromObj.form('submit', {
   			url: url,
   			onSubmit: function(){
				
   			},
   			success: function(data){
   				if(data=="success"){
   					alert('修改成功!');
					$("#showEditDialog").window('close'); 
					outstockdirect.loadDataGrid();
					//bmdefloc.clearAll();
					return;
				}else{
					alert('修改异常,请联系管理员!',2);
				}
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	});
    
};

//弹出新增页面
showAdd=function(){
	var fromObj=$('#dataForm');
	outstockdirect.clearAll(fromObj);
	
	$("#detailDiv,#btn-modify").hide();
	$("#btn-add").show();
	
	$('input[id=locno]',fromObj).combobox('enable');
	$('input[id=locnoHide]',fromObj).attr("disabled",true);
	$('input[id=ownerNo]',fromObj).combobox('enable');
	$('input[id=ownerNoHide]',fromObj).attr("disabled",true);
	$('input[id=expType]',fromObj).combobox('enable');
	$('input[id=expTypeHide]',fromObj).attr("disabled",true);
	
	$('#showDialog').window('open');  
};

//审核操作
auditImImport = function(){
	var fromObj=$('#dataEditForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
          return;
    }
    $.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){  
        if (r) {
        	var data={
        		"ownerNo":$("input[id=ownerNo]",fromObj).combobox('getValue'),
        		"locno":$("input[id=locno]",fromObj).combobox('getValue'),
        		"expNo":$("input[id=expNo]",fromObj).val()
        	};
		    url = BasePath+'/billomexp/auditImImport';
		    outstockdirect.ajaxRequest(url,data,function(result,returnMsg){
        		 if(result=='success'){
        			alert('审核成功!');
        			//关闭窗口
        		    $("#showEditDialog").window('close'); 
        			 //4.审核成功
        			 outstockdirect.loadDataGrid();
        		 }else{
        			 alert('审核失败,请联系管理员!',2);
        		 }
        	}); 
        }
	});     
};

//保存主表信息
saveMain=function(){
	var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return;
     }
     //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
     var checkUrl=BasePath+'/billomexp/get_count.json';
     var expNoVal = $("input[id=expNo]",fromObj).val();
     var ownerNoVal = $("input[id=ownerNo]",fromObj).combobox('getValue');
     var locnoVal = $("input[id=locno]",fromObj).combobox('getValue');
     var expTypeVal = $("input[id=expType]",fromObj).combobox('getValue');
     var checkDataNo={"expNo":expNoVal,"ownerNo":ownerNoVal,"locno":locnoVal,"expType":expTypeVal};
     if(outstockdirect.checkExistFun(checkUrl,checkDataNo)){
    	  alert('出库订单号已存在,不能重复！',1);
    	  $("input[id=expNo]",fromObj).focus();
    	  return;
     }
     
	 //3. 保存
     var url = BasePath+'/billomexp/addBillOmExp';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
				
			},
			success: function(returnMsg){
				if(returnMsg=='success'){
					 alert('新增成功!');
					 //获取信息
					 $.ajax({
							async : true,
							cache : false,
							type : 'POST',
							dataType : "json",
							data: checkDataNo,
							url:BasePath+'/billomexp/get_biz',
							success : function(data) {
								
								outstockdirect.clearAll(fromObj);
								
								outstockdirect.setDetail(data[0],fromObj);
								
								$('input[id=expNo]',fromObj).attr('readOnly',true);
								
								$("#detailDiv,#btn-modify").show();
								$("#btn-add").hide();
							}
						});
					 
					 return;
				 }else{
					 alert('新增异常,请联系管理员!',2);
				 }
		    },
			error:function(){
				alert('新增失败,请联系管理员!',2);
			}
	   });  
};

//获取明细信息
outstockdirect.setDetail = function(rowData,fromObj){
	$("input[id=expNo]",fromObj).val(rowData.expNo);
	//$("input[id=expType]",fromObj).combobox("select",rowData.expType);
	$("input[id=sourceexpNo]",fromObj).val(rowData.sourceexpNo);
	$("input[id=storeNo]",fromObj).val(rowData.storeNo);
	
	$("input[id=expType]",fromObj).combobox("select",rowData.expType);
	$("input[id=expType]",fromObj).combobox('disable');
	$("input[id=expTypeHide]",fromObj).attr("disabled",false);
	$("input[id=expTypeHide]",fromObj).val(rowData.expType);
	
	$("input[id=locno]",fromObj).combobox("select",rowData.locno);
	$("input[id=locno]",fromObj).combobox('disable');
	$("input[id=locnoHide]",fromObj).attr("disabled",false);
	$("input[id=locnoHide]",fromObj).val(rowData.locno);
	
	$("input[id=ownerNo]",fromObj).combobox("select",rowData.ownerNo);
	$("input[id=ownerNo]",fromObj).combobox('disable');
	$("input[id=ownerNoHide]",fromObj).attr("disabled",false);
	$("input[id=ownerNoHide]",fromObj).val(rowData.ownerNo);
	//$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
	$("input[id=expDate]",fromObj).datebox("setValue",rowData.expDate);
	
	$("input[id=deliverType]",fromObj).combobox("select",rowData.deliverType);
	$("input[id=transportType]",fromObj).combobox("select",rowData.transportType);
	
	$("input[id=creator]",fromObj).val(rowData.creator);
	$("input[id=createtm]",fromObj).val(rowData.createtm);
	$("input[id=editor]",fromObj).val(rowData.editor);
	$("input[id=edittm]",fromObj).val(rowData.edittm);
};

//删除
deleteBillOmExp=function(){
	//var rows = $("#dataGridJG").datagrid("getSelections");// 获取所有选中的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","您确定要删除这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var noStrs = [];
        	var delTip = "";
        	$.each(checkedRows, function(index, item){
        		noStrs.push(item.expNo+"#"+item.locno+"#"+item.ownerNo+"#"+item.expType);
        		if(item.status!=null&&item.status!=""&&item.status!="11"){
        			delTip = "单据:"+item.expNo+"；";
        		}
        	}); 
        	if(delTip!=null && delTip!=""){
        		delTip = delTip+"已审核，不能删除！";
        		alert(delTip,1);
        		return;
        	}
        	
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
        	var data={
        	    "noStrs":noStrs.join(",")
        	};
        	
        	 //4. 删除
		    url = BasePath+'/billomexp/deleteBillOmExp';
		    outstockdirect.ajaxRequest(url,data,function(result,returnMsg){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 outstockdirect.loadDataGrid();
        			 alert('删除成功!');
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        	
        	
        	//3校验仓库下是否有绑定用户时
//        	var url = BasePath+'/bmdefloc/findIsLocUser';
//        	bmdefloc.ajaxRequest(url,data,function(result,returnMsg){
//        		 if(result=='success'){
//        			    //4. 删除
//        			    url = BasePath+'/bmdefloc/deleteFefloc';
//        	        	bmdefloc.ajaxRequest(url,data,function(result,returnMsg){
//        	        		 if(result=='success'){
//        	        			 //4.删除成功,清空表单
//        	        			 bmdefloc.loadDataGrid();
//        	        			 alert('删除成功!');
//        	        		 }else{
//        	        			 alert('删除失败,请联系管理员!',2);
//        	        		 }
//        	        	}); 
//        		 }else if(result=='warn'){
//        			 alert('仓库有绑定用户，不能删除!',0);
//        			 return;
//        		 }else {
//        			 alert('校验仓库下是否有绑定用户时异常,请联系管理员!',2);
//        			 return;
//        		 }
//        	}); 
        }
	});     
};

//加载委托业主信息
outstockdirect.loadOwner = function(formObj){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/entrust_owner/get_biz',
		success : function(data) {
			$('input[id=ownerNo]',formObj).combobox({
			    data:data,
			    valueField:'ownerNo',    
			    textField:'ownerNo',
			    panelHeight:150,
			    onSelect:function(data){
			    	$("input[id=ownerName]",formObj).val(data.ownerName);
			    }
			});
			//$('input[id=ownerNo]',formObj).combobox("select",data.rows[0].ownerNo);  
		}
	});
};


//清理
outstockdirect.clearAll = function(fromObj){
	$('#pickingPeopleDataGrid').datagrid('clearData');
	$('#roleid').combobox('setValue', "");
};

//清理form和不为空的提示
outstockdirect.clearFormAndTip = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};

//新增和修改页面的取消按钮
outstockdirect.closeShowWin = function(id){
	$('#'+id).window('close');
	//$('#locno,#locname').validatebox('reset');
	//$('#showDialog').window('destroy');
};

outstockdirect.init_IMPORT_STATUS = function(){
	$('#status').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=IMPORT_STATUS',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });
	
	$('#statusCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=IMPORT_STATUS',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });
};

//格式化出货通知单类型
outstockdirect.columninit_exp_typeFormatter = function(value, rowData, rowIndex){
	return outstockdirect.init_exp_typeText[value];
};

//出货通知单类型
outstockdirect.init_exp_type = function(){
	wms_city_common.comboboxLoadFilter(
			["expType"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=OEM_EXP_TYPE',
			{},
			outstockdirect.init_exp_typeText,
			null);
	
//	$('#expType,#expTypeCondition').combobox({
//		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=OEM_EXP_TYPE',
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     panelHeight:"150"
//	  });
	
//	$('#expTypeCondition').combobox({
//		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=OEM_EXP_TYPE',
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     panelHeight:"150"
//	});
};

//格式化运输方式
outstockdirect.columnTRANSPORT_TYPEFormatter = function(value, rowData, rowIndex){
	return outstockdirect.init_TRANSPORT_TYPEText[value];
};

//运输方式
outstockdirect.init_TRANSPORT_TYPE = function(){
	
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=TRANSPORT_TYPE',
		success : function(data) {
			outstockdirect.init_TRANSPORT_TYPEText=outstockdirect.converStr2JsonObj(data);
			$('#transportType,#transportTypeCondition').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"auto",
			  });
			
		}
	});
	
//	$('#transportType,#transportTypeCondition').combobox({
//		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=TRANSPORT_TYPE',
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     panelHeight:"150"
//	  });
	
//	$('#expTypeCondition').combobox({
//		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=TRANSPORT_TYPE',
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     panelHeight:"150"
//	});
};

//格式化配送方式
outstockdirect.columnDELIVER_TYPEFormatter = function(value, rowData, rowIndex){
	return outstockdirect.init_DELIVER_TYPEText[value];
};

//配送方式
outstockdirect.init_DELIVER_TYPE = function(){
	
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=DELIVER_TYPE',
		success : function(data) {
			outstockdirect.init_DELIVER_TYPEText=outstockdirect.converStr2JsonObj(data);
			$('#deliverType,#deliverTypeCondition').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"auto",
			  });
			
		}
	});
	
//	$('#deliverType,#deliverTypeCondition').combobox({
//		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=DELIVER_TYPE',
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     panelHeight:"150"
//	  });
	
//	$('#expTypeCondition').combobox({
//		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=DELIVER_TYPE',
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     panelHeight:"150"
//	});
};

//新插入一行明细
outstockdirect.insertRow_module = function(gid){
//	var fromObj=$('#dataForm');
//	if(gid == 'moduleEdit'){
//		fromObj=$('#dataEditForm');
//	}
	
	outstockdirect.insertRowAtEnd(gid);
	
//	var ed = $('#'+gid).datagrid('getEditor', {
//        index : tempIndex,
//        field : 'boxNo'
//	});  
//	$.ajax({
//		url: BasePath+'/con_box/get_biz',
//		async : true,
//		cache : false,
//		type : 'POST',
//		dataType : "json",
//		data: {"ownerNo":$("input[id=ownerNo]",fromObj).combobox('getValue'),
//			   "locno":$("input[id=locno]",fromObj).combobox('getValue'),
//			   "expNoIsNull":"1"
//		},
//		success: function(jqData){
//			$(ed.target).combobox('loadData',jqData); 
//		}
//	});
	
};

outstockdirect.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
	return  tempIndex;
};

outstockdirect.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};

outstockdirect.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		tempObj.datagrid('endEdit', i);
    	}else{
    		return false;
    	}
    }
    return true;
};

//保存明细
outstockdirect.save = function(id){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = outstockdirect.endEdit(id);
	if(!tempFlag){
		alert('数据验证没有通过！',1);
		return;
	}
	var fromObj=$('#dataForm');
	var showWinObj = 'showDialog';
	if(id == 'moduleEdit'){
		fromObj=$('#dataEditForm');
		showWinObj = 'showEditDialog';
	}
	var tempObj = $('#'+id);
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动！',1);
		return;
	}
	var inserted = tempObj.datagrid('getChanges', "inserted");
    if(inserted.length>0){
        var checkUrl=BasePath+'/billomexp/get_is_exist.json';
        var checkData={};
        var hashObject = {};
        for (var i = 0; i < inserted.length; i++) {
        	var noAndSize = inserted[i]['itemNo']+"_"+inserted[i]['sizeNo'];
        	 if (hashObject[noAndSize]){
        		 alert('商品编码_尺码：【'+noAndSize+'】重复！',1);
        		 return;
                 //return true;
             } else {
                 hashObject[noAndSize] = true;
             }
//        	checkData.moduleName = inserted[i]['boxNo'];
//            if(outstockdirect.checkExistFun(checkUrl,checkData)){
//        		alert('箱号已经存在!',1);
//        		return;
//    	    }
        }
    }
    var deleted = tempObj.datagrid('getChanges', "deleted");
    var updated = tempObj.datagrid('getChanges', "updated");
    var effectRow = {
    		inserted:JSON.stringify(inserted),
    		deleted:JSON.stringify(deleted),
    		updated:JSON.stringify(updated),
    		"ownerNo":$("input[id=ownerNo]",fromObj).combobox('getValue'),
			"locno":$("input[id=locno]",fromObj).combobox('getValue'),
			"expNo":$("input[id=expNo]",fromObj).val(),
			"expDate":$("input[id=expDate]",fromObj).datebox("getValue")
    };
    $.post(BasePath+'/billomexpdtl/save', effectRow, function(result) {
        if(result.success){
            alert('保存成功!',1);
            tempObj.datagrid('acceptChanges');
            outstockdirect.clearAll(fromObj);
            $('#'+showWinObj).window('close');  
            outstockdirect.loadDataGrid();
        }else{
        	alert('操作异常！',1);
        	return;
        }
    }, "JSON").error(function() {
    	alert('保存失败!',1);
    });
};

/**
 * 数组是否重复，如重复则返回重复元素 
 * @return {}
 */
outstockdirect.isDouble = function() {
    var hashObject = {};
    for (var i = 0; i < this.length; i++) {
        if (hashObject[this[i]]) {
            return true;
        } else {
            hashObject[this[i]] = true;
        }
    }
    return false;
};

//确定拣货人
outstockdirect.confirmPickingPeople = function(){
	var checkedRows = $("#pickingPeopleDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请至少选择一个人员信息！',1);
		return;
	}
	$.messager.confirm("确认","您确定要选择这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var noStrs = [];
        	$.each(checkedRows, function(index,item){
        		noStrs.push(item.loginName);
        	}); 
        	$('#pickingPeople').val(noStrs.join(","));
        	$('#showSelectPickingPeopleWin').window('close').hide();  
        }
	});   
	
};

//根据角色ID查询对应下的所有用户信息 
outstockdirect.loadDataUserInfoByRoleId = function(roleId){
	
	$('#pickingPeopleDataGrid').datagrid('clearData');
//	$('#pickingPeopleDataGrid').datagrid(
//			{
//				url:BasePath+'/outstockdirect/getUserListByRoleId',
//				queryParams: {
//					"roleId" : roleId
//				}
//				//'pageNumber':1 
//			}
//	);
	var queryMxURL=BasePath+'/outstockdirect/getUserListByRoleId?roleId='+roleId+'&locno='+outstockdirect.locno;
    //3.加载明细
    //$( "#pickingPeopleDataGrid").datagrid( 'options' ).queryParams= eval(queryData);
    $( "#pickingPeopleDataGrid").datagrid( 'options' ).url=queryMxURL;
    $( "#pickingPeopleDataGrid").datagrid( 'load' );
	
};

//加载岗位信息
outstockdirect.loadRoleList = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/queryRoleListForOther',
		success : function(data) {
			$('#roleid').combobox({
			    data:data,
			    valueField:'roleId',    
			    textField:'roleName',
			    panelHeight:150,
			    onSelect:function(data){
			    	
			    	outstockdirect.loadDataUserInfoByRoleId(data.roleId);
			    	
			    	//$("input[id=ownerName]",formObj).val(data.ownerName);
			    	
			    }
			});
			//$('input[id=ownerNo]',formObj).combobox("select",data.rows[0].ownerNo);  
		},error:function(){
			alert('加载岗位信息异常,请联系管理员!',2);
		}
	});
};


//发单
outstockdirect.sendOrder = function(){
	
//	if($('#pickingPeople').combobox('getValue')=="" || $('#pickingPeople').combobox('getValue')==null){
//		alert("请选择拣货人！",1);
//		return;
//	}
//	$("#pickingPeople2").val($('#pickingPeople').combobox('getValue'));
	
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    
    var workQty =  $('#workQty').numberbox('getValue');
    if(workQty < 1){
    	alert("任务分配数不能为空且小于0");
    	return;
    }
    
    
//    if(($("input[id=batchNoForProc]",fromObj).val() == null || $("input[id=batchNoForProc]",fromObj).val()=="" 
//		 || $("input[id=locateNoForProc]",fromObj).val() == null || $("input[id=locateNoForProc]",fromObj).val()==""
//			|| $("input[id=operateTypeForProc]",fromObj).val() == null || $("input[id=operateTypeForProc]",fromObj).val()==""
//				|| $("input[id=locnoForProc]",fromObj).val() == null || $("input[id=locnoForProc]",fromObj).val()=="")){
//		alert("请双击选择波次信息或选择类型！",1);
//		return;
//	}
    
    //获取选中的tab
	var pp = $('#tt').tabs('getSelected');   
	var index = $('#tt').tabs('getTabIndex',pp);//easyui tabs获取选中索引
	var dataList = [];
	if(index == 0){
		
		var dataGridOutStock = $('#storeNoDataGrid').datagrid('getChecked');
		if(dataGridOutStock.length < 1){
			alert("请选择客户进行发单!");
			return;
		}
		
		$.each(dataGridOutStock, function(index, item){
			var params = {
				storeNo:item.storeNo
			};
			dataList[dataList.length] = params;
		});
		
	}else{
		
//		if($("input[id=areaNoForProc]",fromObj).val() == null || $("input[id=areaNoForProc]",fromObj).val()==""){
//			alert("请双击选择库区信息！",1);
//			return;
//		}
		
		var dataGridOutStock = $('#areaNoDataGrid').datagrid('getChecked');
		if(dataGridOutStock.length < 1){
			alert("请选择库区进行发单!");
			return;
		}
		
		$.each(dataGridOutStock, function(index, item){
			var params = {
				areaNo:item.areaNo
			};
			dataList[dataList.length] = params;
		});
		
		
//		if(($("input[id=batchNoForProc]",fromObj).val() == null || $("input[id=batchNoForProc]",fromObj).val()=="" 
//			 || $("input[id=locateNoForProc]",fromObj).val() == null || $("input[id=locateNoForProc]",fromObj).val()==""
//				|| $("input[id=operateTypeForProc]",fromObj).val() == null || $("input[id=operateTypeForProc]",fromObj).val()==""
//					|| $("input[id=locnoForProc]",fromObj).val() == null || $("input[id=locnoForProc]",fromObj).val()=="")
//		    ||(($("input[id=areaNoForProc]",fromObj).val() == null || $("input[id=areaNoForProc]",fromObj).val()=="") 
//		    	&& ($("input[id=storeNoForProc]",fromObj).val() == null || $("input[id=storeNoForProc]",fromObj).val()=="")
//		    	)){
//			alert("请双击选择库区信息！",1);
//			return;
//		}
	}
    
	var isFloor = "0";
	if($("#floorCut").attr("checked")){
		isFloor = "1";
	}
	var sortType = $('input[name="sortType"]:checked').val();
	var checkDataNo = {
			datas:JSON.stringify(dataList),
			batchNo : $("input[id=batchNoForProc]",fromObj).val(),
			locateNo : $("input[id=locateNoForProc]",fromObj).val(),
			operateType : $("#operateType").combobox('getValue'),
			locno : $("input[id=locnoForProc]",fromObj).val(),
			expType : $("input[id=expType]",fromObj).combobox('getValue'),
			pickingPeople :  $("input[id=pickingPeople2]",fromObj).val(),
			workQty :  $('#workQty').numberbox('getValue'),
			sortType : sortType,
			isFloor : isFloor
	};
	
	//获取信息
	wms_city_common.loading("show","正在切单......");
	$.ajax({
			async : true,
			cache : false,
			type : 'POST',
			dataType : "json",
			data: checkDataNo,
			url:BasePath+'/outstockdirect/procOmOutStockDirect',
			success : function(data) {
				wms_city_common.loading();
				if(data){
   					alert('发单成功!');
   					//清空所有的值
   					outstockdirect.clearAllParam();
				}else{
					alert(data,2);
				}
			},
   			error:function(data){
   				wms_city_common.loading();
   				alert(data.responseText,2);
   			}
   });
	
	
//    var url = BasePath+'/outstockdirect/procOmOutStockDirect';
//    fromObj.form('submit', {
//   			url: url,
//   			onSubmit: function(){
//				
//   			},
//   			success: function(data){
//   				if(data=="true"){
//   					alert('发单成功!');
//   					//清空所有的值
//   					outstockdirect.clearAllParam();
//				}else{
//					alert(data,2);
//				}
//   		    },
//   			error:function(){
//   				alert('发单失败,请联系管理员!',2);
//   			}
//   	});
	
};

//清空所有的值
outstockdirect.clearAllParam = function(){
	
	$('#storeNoDataGrid,#areaNoDataGrid,#outstockDirectDataGrid').datagrid('load');
	
	//清空datagrid数据
	//$('#locateNoDataGrid,#storeNoDataGrid,#areaNoDataGrid,#outstockDirectDataGrid').datagrid('load');
	//清空参数
	//$('#expTypeHide,#locateNoHide,#batchNoHide').val('');
	//$('#operateType').combobox('setValue', "");
	//form清空，包括隐藏的值
	//$('#searchForm').form("clear");
	
//	$('#batchNoForProc').val("");
//	$('#locateNoForProc').val("");
//	$('#operateTypeForProc').val("");
//	$('#locnoForProc').val("");
	$('#areaNoForProc').val("");
	$('#storeNoForProc').val("");
	$('#pickingPeople2').val("");
	
};

//搜索数据
outstockdirect.searchData = function(){
	$('#locateNoDataGrid').datagrid('load');
};


//选择拣货人
outstockdirect.selectPickingPeople = function(){
	$('#showSelectPickingPeopleWin').show().window('open');  
	$('#pickingPeopleDataGrid').datagrid('clearData');
	outstockdirect.loadRoleList();
};

//根据出库单类型，波次号，批次,作业类型 ，库区  查询拣货的商品信息
outstockdirect.loadDataOutstockDirectByArea = function(data){
	$('#outstockDirectDataGrid').datagrid('clearData');
//	$('#outstockDirectDataGrid').datagrid(
//			{
//				url:BasePath+'/outstockdirect/queryOutstockDirectByArea',
//				queryParams: data,
//				'pageNumber':1 
//			}
//	);
	
	var url = BasePath+'/outstockdirect/queryOutstockDirectByArea';
	outstockdirect.loadGridDataUtil('outstockDirectDataGrid', url, data);
	
	outstockdirect.setSearchForm(data,'areaNo');
	
};

//根据出库单类型，波次号，批次，作业类型,客户查询拣货的商品信息
outstockdirect.loadDataOutstockDirectByStore = function(data){
	$('#outstockDirectDataGrid').datagrid('clearData');
//	$('#outstockDirectDataGrid').datagrid(
//			{
//				url:BasePath+'/outstockdirect/queryOutstockDirectByStore',
//				queryParams: data,
//				'pageNumber':1 
//			}
//	);
	
	var url = BasePath+'/outstockdirect/queryOutstockDirectByStore';
	outstockdirect.loadGridDataUtil('outstockDirectDataGrid', url, data);

	//outstockdirect.setSearchForm(data,'storeNo');
	
};



//设置form的值，以便于发单的操作
outstockdirect.setSearchForm = function(data,flag){
	var fromObj=$('#searchForm');
	
//	$("input[id=batchNoForProc]",fromObj).val(data.batchNo);
//	$("input[id=locateNoForProc]",fromObj).val(data.locateNo);
//	$("input[id=operateTypeForProc]",fromObj).val(data.operateType);
//	$("input[id=locnoForProc]",fromObj).val(data.locno);
	
	if(flag =='areaNo'){
		$("input[id=areaNoForProc]",fromObj).val(data.areaNo);
		$("input[id=storeNoForProc]",fromObj).val('');
	}else if(flag =='storeNo'){
		$("input[id=areaNoForProc]",fromObj).val('');
		$("input[id=storeNoForProc]",fromObj).val(data.storeNo);
	}
	
	//$("input[id=supplierNo]",fromObj).combobox("select",rowData.supplierNo);
	
};

//根据出库单类型，波次号，批次，作业类型查询库区信息
outstockdirect.loadDataGridForAreaNo = function(data){
//	$('#areaNoDataGrid').datagrid(
//			{
//				url:BasePath+'/outstockdirect/queryAreaInfoByParam',
//				queryParams: data
////				'pageNumber':1 
//			}
//	);
	
	var url = BasePath+'/outstockdirect/queryAreaInfoByParam';
	outstockdirect.loadGridDataUtil('areaNoDataGrid', url, data);
	
	var fromObj=$('#searchForm');
	$("input[id=areaNoForProc]",fromObj).val('');
};

//根据出库单类型，波次号，批次，作业类型查询客户信息
outstockdirect.loadDataGridForStoreNo = function(data){
	
	var url = BasePath+'/outstockdirect/queryStoreInfoByParam';
	outstockdirect.loadGridDataUtil('storeNoDataGrid', url, data);
	
	
//	$('#storeNoDataGrid').datagrid(
//			{
//				url:BasePath+'/outstockdirect/queryStoreInfoByParam',
//				queryParams: data
////				'pageNumber':1 
//			}
//	);
};


//加载作业类型
outstockdirect.loadDataOperateType = function(rowData){
	
	var fromObj=$('#searchForm');
	$("input[id=batchNoForProc]",fromObj).val(rowData.batchNo);
	$("input[id=locateNoForProc]",fromObj).val(rowData.locateNo);
	$("input[id=operateTypeForProc]",fromObj).val(rowData.operateType);
	$("input[id=locnoForProc]",fromObj).val(rowData.locno);
	
	//清空datagrid数据
	$('#storeNoDataGrid,#areaNoDataGrid,#outstockDirectDataGrid').datagrid('clearData');
	//清空参数
	$('#expTypeHide,#locateNoHide,#batchNoHide').val('');
	//$('#operateType').combobox('setValue', "");
	
	$.ajax({
		async : true,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/outstockdirect/queryOperateTypeByParam',
		data:{
			"lookupCode":"CHECK_OPERATE_TYPE",
			"locno":rowData.locno,
			"expType":rowData.expType,
			"locateNo":rowData.locateNo,
			"batchNo":rowData.batchNo
		},
		success : function(data) {
			$('#expTypeHide').val(rowData.expType);
	    	$('#locateNoHide').val(rowData.locateNo);
	    	$('#batchNoHide').val(rowData.batchNo);
			$('#operateType').combobox('loadData',data);
			$('#operateType').combobox({
			    data:data,
			    valueField:'operateType',    
			    textField:'itemname',
			    panelHeight:150,
			    onSelect:function(data){
			    	
			    	if(data.operateType == 'B'){
			    		$('#workTitle').html('每任务商品数：');
			    	}else{
			    		$('#workTitle').html('每任务箱数：');
			    	}
			    	
			    	//清空datagrid数据
			    	$('#storeNoDataGrid,#areaNoDataGrid,#outstockDirectDataGrid').datagrid('clearData');
			    	deleteAllGridCommon('outstockDirectDataGrid');
			    	
			    	//清空参数
			    	$('#expTypeHide,#locateNoHide,#batchNoHide').val('');
			    	
			    	// 获取选中的 tab panel 和它的 tab 对象   
//			    	var pp = $('#tt').tabs('getSelected');   
//			    	var tab = pp.panel('options').tab;    // 相应的 tab 对象
//			    	var tab_id = pp.panel('options').id;    // 相应的 tab对象的id属性的值   
//			    	var index = $('#tt').tabs('getTabIndex',pp);//easyui tabs获取选中索引
//			    	alert("index："+index);
			    	$('#expTypeHide').val(rowData.expType);
			    	$('#locateNoHide').val(rowData.locateNo);
			    	$('#batchNoHide').val(rowData.batchNo);
			    	
			    	//获取选中的tab
			    	var pp = $('#tt').tabs('getSelected');   
			    	var index = $('#tt').tabs('getTabIndex',pp);//easyui tabs获取选中索引
			    	var queryData ={
			    				"locno": outstockdirect.locno,
			    	    		"expType" :  $('#expTypeHide').val(),
			    				"locateNo":  $('#locateNoHide').val(),
			    				"batchNo" :  $('#batchNoHide').val(),
			    				"operateType": $("#operateType").combobox('getValue')
			    	};
			    	if(index==0){//按客户
			    	    	//清空datagrid数据
						    $('#storeNoDataGrid').datagrid('clearData');
			    	    	outstockdirect.loadDataGridForStoreNo(queryData);
			    	}else if(index==1){//按库区
			    	    	//清空datagrid数据
						    $('#areaNoDataGrid').datagrid('clearData');
			    	    	outstockdirect.loadDataGridForAreaNo(queryData);
			    	}else{
			    	    	alert('未选中Tab！',1);
			    	}
			         
			    }
			});
			
			
			if(null!=data && null!=data[0]){
				$('#operateType').combobox("select",data[0].operateType);  
			}
		}
	});
};

//加载波次号和批次
outstockdirect.loadDataGridForLocateNo = function(expType){
	
	var queryParams = {locno:outstockdirect.locno,expType:expType};
	var queryMxURL=BasePath+'/outstockdirect/queryLocateNoByMore.json';
	outstockdirect.loadGridDataUtil('locateNoDataGrid', queryMxURL, queryParams);
	
//	var url = BasePath+'/outstockdirect/queryLocateNoByExpType';
//	var queryParams = {locno:outstockdirect.locno,expType:expType};
//	outstockdirect.loadGridDataUtil('locateNoDataGrid', url, queryParams);
	
//	$('#locateNoDataGrid').datagrid(
//			{
//				'url':BasePath+'/outstockdirect/queryLocateNoByExpType?expType='+expType+"&locno="+outstockdirect.locno
////				'queryParams': {
////					expNo: rowData.expNo,
////					locno: rowData.locno,
////					ownerNo: rowData.ownerNo
////				},
////				'pageNumber':1 
//			}
//	);
};

//加载出货类型
outstockdirect.loadExpType = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/lookupdtl/selectOutstockDirectExpType?lookupcode=OEM_EXP_TYPE&locno='+outstockdirect.locno,
		success : function(data) {
			$('#expType').combobox({
			    data:data,
			    valueField:'itemval',    
			    textField:'itemname',
			    panelHeight:150,
			    onSelect:function(returnData){
			    
			    	$('#expTypeName').val(returnData.itemname);
			    	
			    	//清空datagrid数据
			    	$('#locateNoDataGrid,#storeNoDataGrid,#areaNoDataGrid,#outstockDirectDataGrid').datagrid('clearData');
			    	//清空参数
			    	$('#expTypeHide,#locateNoHide,#batchNoHide').val('');
			    	$('#operateType').combobox('setValue', "");
			    	
			    	//设置波次号和批次的数据
			    	outstockdirect.loadDataGridForLocateNo(returnData.itemval);
			    }
			});
			if(null!=data[0]){
				$('#expType').combobox("select",data[0].itemval);
			}
		}
	});
};

//创建tab
$(function(){ 
	$('#tt').tabs({
        border:true,
        onSelect:function(title){
        	
        	//获取选中的tab
	    	var pp = $('#tt').tabs('getSelected');   
	    	var index = $('#tt').tabs('getTabIndex',pp);//easyui tabs获取选中索引
        	
        	//判断参数不为空时，才去查询数据
        	if(""!=$('#expTypeHide').val() && ""!=$('#locateNoHide').val()
        		&& ""!=$('#batchNoHide').val() && ""!=$("#operateType").combobox('getValue')){
        		
        		//清空datagrid数据
    	    	$('#storeNoDataGrid,#areaNoDataGrid').datagrid('clearData');
        		$('#outstockDirectDataGrid').datagrid('load', { });
        		
    	    	var queryData ={
    	    			"expType" :  $('#expTypeHide').val(),
    	    			"locno": outstockdirect.locno,
    					"locateNo":  $('#locateNoHide').val(),
    					"batchNo" :  $('#batchNoHide').val(),
    					"operateType": $("#operateType").combobox('getValue')
    	    	};
    	    	
    	    	//加载客户数据
	    		var rows = $('#outstockDirectDataGrid').datagrid('getFooterRows');
	    		if(rows!=null){
	    			rows[0]['itemQty'] = 0;
	    			rows[1]['itemQty'] = 0;
	    			$('#outstockDirectDataGrid').datagrid('reloadFooter');
	    		}
	    		
	    		var rows2 = $('#outstockDirectDataGrid').datagrid('getFooterRows');
	    		if(rows2!=null){
	    			rows2[0]['itemQty'] = 0;
	    			rows2[1]['itemQty'] = 0;
	    			$('#outstockDirectDataGrid').datagrid('reloadFooter');
	    		}
    	 
    	    	if(index==0){//按客户
    	    		outstockdirect.loadDataGridForStoreNo(queryData);
    	    		$('#floorTitle').show();
    	    	}else if(index==1){//按库区
    	    		//加载库区数据
    	    		outstockdirect.loadDataGridForAreaNo(queryData);
    	    		$('#floorTitle').hide();
    	    	}else{
    	    		alert('未选中Tab！',1);
    	    	}
    	    	
        	}
        	
        	if(index==0){//按客户
	    		$('#floorTitle').show();
	    	}else if(index==1){//按库区
	    		//加载库区数据
	    		$('#floorTitle').hide();
	    	}
        }
    });
	
});

//加载仓库信息
outstockdirect.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			outstockdirect.locno = data.locno;
			$('#searchForm input[name=locno]').val(data.locno);
		}
	});
};

//初始化拣货人
outstockdirect.initCheckUser = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#pickingPeople').combobox({
			     valueField:"workerNo",
			     textField:"workerName",
			     data:data,
			     panelHeight:"auto",
			}).combobox("select",data[0].workerNo);
		} 
	});
};

$(document).ready(function(){
	
	outstockdirect.loadLoc();//加载仓库信息
	outstockdirect.loadExpType();//初始化出货单类型3
	outstockdirect.initCheckUser();
	
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')});
	wms_city_common.loadSysNo4Cascade(objs);
	
	
	$('#outstockDirectDataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					outstockdirect.itemQty = data.footer[1].itemQty;
				} else {
					var rows = $('#outstockDirectDataGrid').datagrid('getFooterRows');
					rows[1]['itemQty'] = outstockdirect.itemQty;
					$('#outstockDirectDataGrid').datagrid('reloadFooter');
				}
			}
			
		}
	});
	
//	$('#operateType').combobox({
//	    valueField:'operateType',    
//	    textField:'itemname',
//	    panelHeight:"auto",
//	    onSelect:function(data){
//	    	
//	    	if(data.operateType == 'B'){
//	    		$('#workTitle').html('每任务商品数：');
//	    	}else{
//	    		$('#workTitle').html('每任务箱数：');
//	    	}
//	    	
//	    	//清空datagrid数据
//	    	$('#storeNoDataGrid,#areaNoDataGrid,#outstockDirectDataGrid').datagrid('clearData');
//	    	//清空参数
//	    	$('#expTypeHide,#locateNoHide,#batchNoHide').val('');
//	    	
//	    	// 获取选中的 tab panel 和它的 tab 对象   
////	    	var pp = $('#tt').tabs('getSelected');   
////	    	var tab = pp.panel('options').tab;    // 相应的 tab 对象
////	    	var tab_id = pp.panel('options').id;    // 相应的 tab对象的id属性的值   
////	    	var index = $('#tt').tabs('getTabIndex',pp);//easyui tabs获取选中索引
////	    	alert("index："+index);
//	    	
//	    	
////	    	$('#expTypeHide').val(rowData.expType);
////	    	$('#locateNoHide').val(rowData.locateNo);
////	    	$('#batchNoHide').val(rowData.batchNo);
//	    	
//	    	//获取选中的tab
//	    	var pp = $('#tt').tabs('getSelected');   
//	    	var index = $('#tt').tabs('getTabIndex',pp);//easyui tabs获取选中索引
//	    	var queryData ={
//	    				"locno": outstockdirect.locno,
//	    	    		"expType" :  $('#expTypeHide').val(),
//	    				"locateNo":  $('#locateNoHide').val(),
//	    				"batchNo" :  $('#batchNoHide').val(),
//	    				"operateType": $("#operateType").combobox('getValue')
//	    	};
//	    	if(index==0){//按客户
//	    	    	//清空datagrid数据
//				    $('#storeNoDataGrid').datagrid('clearData');
//	    	    	outstockdirect.loadDataGridForStoreNo(queryData);
//	    	}else if(index==1){//按库区
//	    	    	//清空datagrid数据
//				    $('#areaNoDataGrid').datagrid('clearData');
//	    	    	outstockdirect.loadDataGridForAreaNo(queryData);
//	    	}else{
//	    	    	alert('未选中Tab！',1);
//	    	}
//	         
//	    }
//	});

});

