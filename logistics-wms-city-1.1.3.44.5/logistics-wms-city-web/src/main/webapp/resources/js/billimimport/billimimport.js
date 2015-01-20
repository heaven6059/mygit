function parsePage(){
        //工具条快捷搜索
        toolSearch();
};
var billimimport = {};

billimimport.locno;

billimimport.curRowIndex = -1;
billimimport.preType = 0;

billimimport.exportDataGridDtl1Id = 'moduleView';
billimimport.preColNamesDtl1 = [
		                   {title:"商品编码",field:"itemNo",width:150},
		                   /*{title:"颜色",field:"colorNoStr",width:80},
		                   {title:"商品名称",field:"itemName",width:150},*/
		                   {title:"箱号",field:"boxNo",width:120},
		                   {title:'装车单号',field:"deliverNo",width:120},
		                   {title:"品牌",field:"brandNoStr",width:100}
                    ];
billimimport.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billimimport.sizeTypeFiledName = 'sizeKind';

billimimport.getDate = function(n){
	var nowDate = new Date();
	var newDate = new Date();
	var newTimes = nowDate.getTime() + (n*24*3600000);
	newDate.setTime(newTimes);
	var y = newDate.getFullYear();
	var m = newDate.getMonth()+1;
	var d = newDate.getDate();
	var dateStr = y+"-";
	dateStr += (m<10?("0"+m):(m))+"-";
	dateStr += (d<10?("0"+d):(d));
	return dateStr;
};


//将数组封装成一个map
billimimport.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};


billimimport.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  async:false,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billimimport.loadDataGrid = function(){
	//$('#dataGridJG').datagrid({'url':BasePath+'/billimimport/list.json?locno='+billimimport.locno+'','title':'预到货通知单列表','pageNumber':1 });
	$( "#dataGridJG").datagrid( 'options' ).url=BasePath+'/billimimport/list.json?locno='+billimimport.locno;
    $( "#dataGridJG").datagrid( 'load' );
};

billimimport.loadDataDetailGrid = function(rowData){
	var queryMxURL=BasePath+'/bill_im_import_dtl/listBoxNo';
	var queryData = {"importNo": rowData.importNo,
	                 "locno": rowData.locno,
	                 "ownerNo": rowData.ownerNo
					};
    //3.加载明细
    $( "#moduleEdit").datagrid( 'options' ).queryParams= queryData;
    $( "#moduleEdit").datagrid( 'options' ).url=queryMxURL;
    $( "#moduleEdit").datagrid( 'load' );

};


//查询预到货通知单
billimimport.searchBillImImport = function(){
	
	$('#searchForm input[name=locno]').val(billimimport.locno);
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/billimimport/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清楚查询条件
billimimport.searchBillImImportClear = function(){
	$('#supplierNoCondition').combobox("loadData",[]);
	$('#brandNo').combobox("loadData",[]);
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

//修改预到货通知单主表信息
billimimport.modifyFloc = function(id){
    var fromObj=$('#'+id);
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
     //判断发货日期，预到货日期是否小于当前日期
     var curDate = wms_city_common.getCurDate();
     var orderDate = $("#orderDate_dataEditForm").datebox('getValue');
     var requestDate = $("#requestDate_dataEditForm").datebox('getValue');
     if(!wms_city_common.isStartLessThanEndDate(curDate,orderDate)){
     	alert("发货日期不能小于当前日期!",1);
     	return;
     }
     if(!wms_city_common.isStartLessThanEndDate(curDate,requestDate)){
     	alert("预到货日期不能小于当前日期!",1);
     	return;
     }
    wms_city_common.loading("show");
    var url = BasePath+'/billimimport/modifyImImport';
    fromObj.form('submit', {
   			url: url,
   			onSubmit: function(){
				
   			},
   			success: function(data){
   				var resultData = $.parseJSON(data);
   				wms_city_common.loading();
   				if(resultData.result=='success'){
   					alert('修改成功!');
					$("#showEditDialog").window('close'); 
					billimimport.loadDataGrid();
					//bmdefloc.clearAll();
					return;
	       		 }else{
	       			 if(resultData.msg==null){
	       				 alert('修改异常,请联系管理员!',2);
	       			 }else{
	       				 alert(resultData.msg,2);
	       			 }        			 
	       		 }
   				
   		    },
   			error:function(){
   				wms_city_common.loading();
   				alert('修改失败,请联系管理员!',2);
   			}
   	});
    
};

//弹出详情页面
billimimport.showEdit = function(rowData,flag){
	
	//清空datagrid数据
	$('#moduleEdit').datagrid('clearData');
	
	var formId='dataEditForm';
	
	 billimimport.clearAll(formId);
	
	//仓库编码设置为只读，不可修改
	$('#importNo_dataEditForm').attr("readOnly",true);

	$('#detailEditDiv').show();
	
	if(flag == 1){
		$('#toolDivForUpdate,#zoneInfoToolEditDiv').show();
		if(rowData.status!=null && rowData.status!="" && rowData.status!="10" ){
			//先全部展示
			$('#btn-over-edit,#btn-add-edit,#zoneInfoToolEditDiv').show();
			if(rowData.status=="90" || rowData.status=="91"){//结案或异常结案时，隐藏结案按钮；
				$('#btn-over-edit').hide();//结案按钮隐藏
			}
			$('#btn-add-edit,#zoneInfoToolEditDiv').hide();
			$("#btn-edit").show();
			$("#btn-add-add").hide();
		}else{
			$('#btn-over-edit,#btn-add-edit,#zoneInfoToolEditDiv').show();
		}
	}else{
		$('#toolDivForUpdate,#zoneInfoToolEditDiv').hide();
	}
	
	//设置信息
	billimimport.setDetail(rowData,formId);
	//明细信息
	billimimport.loadDataDetailGrid(rowData);
	
	$("#showEditDialog").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	//弹窗
	$("#showEditDialog").window('open'); 
};


//弹出修改页面
billimimport.showModify = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length != 1){
		alert('请选择一条记录！',1);
		return;
	}else if(checkedRows[0].status!='10'){
		alert('非建单状态的数据不能修改！',1);
		return;
	}
	billimimport.showEdit(checkedRows[0],1);
};

//弹出新增页面
billimimport.showAdd=function(){
	//$('#toolDivForUpdate,#zoneInfoToolEditDiv').show();
	//$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
	
	var formId = 'dataForm';
	
	billimimport.clearAll(formId);
	
	$('#importNo_dataForm').val("系统自动生成");
	$("#detailDiv,#btn-modify").hide();
	$("#btn-add").show();
	
	$('#supplierNo_dataForm').combobox('enable');
	$('#supplierNoHide_dataForm').attr("disabled",true);
	
	$('#ownerNo_dataForm').combobox('enable');
	$('#ownerNoHide_dataForm').attr("disabled",true);
	
	$('#sysNo_dataForm').combobox('enable');
	$('#sysNoHide_dataForm').attr("disabled",true);
	
	$('#businessType_dataForm').combobox('enable');
	$('#businessTypeHide_dataForm').attr("disabled",true);
	
	//$("#info_save").show();
	$("#showDialog").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$("#btn-edit").hide();
	$("#btn-add-add").show();
	$("#module").datagrid('clearData');
	$('#showDialog').window('open');  
};

//审核操作
billimimport.auditImImport = function(){
	
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	$.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){ 
        if (r) {
        	var noStrs = [];
        	var delTip = "";
        	$.each(checkedRows, function(index, item){
        		noStrs.push(item.importNo+"#"+item.ownerNo);
        		if(item.status!=null&&item.status!=""&&item.status!="10"){
        			delTip = delTip+ "单据:"+item.importNo+"；";
        		}
        	}); 
        	if(delTip!=null && delTip!=""){
        		delTip = delTip+"已审核，不能再次操作！";
        		alert(delTip,1);
        		return;
        	}
        	
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
        	var data={
        	    "noStrs":noStrs.join(",")
        	};
        	
        	//4. 审核
        	wms_city_common.loading("show");
		    url = BasePath+'/billimimport/auditImImport';
		    billimimport.ajaxRequest(url,data,function(result,returnMsg){
		    	wms_city_common.loading();
        		 if(result.flag=='true'){
        			 //4.审核成功,重新加载数据
        			 alert('审核成功!');
        			 billimimport.loadDataGrid();
        		 }else if(result.flag=='warn'){
        			 alert(result.resultMsg,1);
        		 }else{
        			 alert('审核失败,请联系管理员!',2);
        		 }
        	}); 
        }
	});   
   
};

//保存主表信息
billimimport.saveMain=function(){
	var formId = 'dataForm';
	var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return;
     }
     
     //判断发货日期，预到货日期是否小于当前日期
     var curDate = wms_city_common.getCurDate();
     var orderDate = $("#orderDate_dataForm").datebox('getValue');
     var requestDate = $("#requestDate_dataForm").datebox('getValue');
     if(!wms_city_common.isStartLessThanEndDate(curDate,orderDate)){
     	alert("发货日期不能小于当前日期!",1);
     	return;
     }
     if(!wms_city_common.isStartLessThanEndDate(curDate,requestDate)){
     	alert("预到货日期不能小于当前日期!",1);
     	return;
     }
     
	 //3. 保存
     wms_city_common.loading("show");
     var url = BasePath+'/billimimport/addBillImImport';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
				
			},
			success: function(data){
				wms_city_common.loading();
				var dataObj = eval('(' + data + ')'); 
				
				if(dataObj.returnMsg){
					 alert('新增成功!');
					 
				     var importNoVal = dataObj.importNo;
				     var locnoVal = dataObj.locno;
				     var ownerNoVal = $("#ownerNo_dataForm").combobox('getValue');
				     var checkDataNo={"importNo":importNoVal,"ownerNo":ownerNoVal,"locno":locnoVal};
					 
					 //获取信息
					 $.ajax({
							async : true,
							cache : false,
							type : 'POST',
							dataType : "json",
							data: checkDataNo,
							url:BasePath+'/billimimport/get_biz',
							success : function(data) {
								
								billimimport.clearAll(formId);
								
								billimimport.setDetail(data[0],formId);
								
								$('#importNo_dataForm').attr('readOnly',true);
								
								$("#detailDiv,#btn-modify").show();
								
								//清空datagrid数据
								$('#module').datagrid('clearData');
								$("#btn-add-add").hide();
								$("#btn-edit").show();
								$("#ownerNo_dataForm").combobox('getValue')
							}
						});
					 
					 return;
				 }else{
					 alert('新增异常,请联系管理员!',2);
				 }
		    },
			error:function(){
				wms_city_common.loading();
				alert('新增失败,请联系管理员!',2);
			}
	   });  
};

//获取明细信息
billimimport.setDetail = function(rowData,formId){
	$('#'+formId).form('load',rowData);
	
	$("#supplierNo_"+formId).combobox('disable');
	$("#supplierNoHide_"+formId).attr("disabled",false);
	$("#sysNo_"+formId).combobox('disable');
	$("#sysNoHide_"+formId).attr("disabled",false);
	$("#ownerNo_"+formId).combobox('disable');
	$("#ownerNoHide_"+formId).attr("disabled",false);
	$("#businessType_"+formId).combobox('disable');
	$("#businessTypeHide_"+formId).attr("disabled",false);
};

//删除
deleteImImport=function(){
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
        		noStrs.push(item.importNo+"#"+item.locno+"#"+item.ownerNo);
        		if(item.status!=null&&item.status!=""&&item.status!="10"){
        			delTip = delTip+"单据:"+item.importNo+"；";
        		}
        	}); 
        	if(delTip!=null && delTip!=""){
        		delTip = delTip+"不是新建状态，不能删除！";
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
        	wms_city_common.loading("show");
		    url = BasePath+'/billimimport/deleteImImport';
		    billimimport.ajaxRequest(url,data,function(date){
		    	wms_city_common.loading();
        		 if(date.result=='success'){
        			 //4.删除成功,清空表单
        			 billimimport.loadDataGrid();
        			 alert('删除成功!');
        		 }else{
        			 if(date.msg==null){
        				 alert('删除失败,请联系管理员!',2);
        			 }else{
        				 alert(date.msg,2);
        			 }        			 
        		 }
        	}); 
        }
	});     
};

//加载仓库信息
billimimport.loadLoc = function(formObj){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billimimport.locno = data.locno;
		}
	});
};
//========================业务类型BEGIN========================
billimimport.businessTypes = {};
billimimport.businessTypesFormatter = function(value, rowData, rowIndex){
	return billimimport.businessTypes[value];
};
billimimport.loadSupplierNo = function(type,id,isSelectAll){
	if(type=="0"){
		wms_city_common.comboboxLoadFilter(
				[id],
				'supplierNo',
				'supplierName',
				'valueAndText',
				false,
				[isSelectAll],
				BasePath+'/supplier/get_all',
				{},
				null,
				null);
	}else if(type=="4"){
		wms_city_common.comboboxLoadFilter(
				[id],
				'storeNo',
				'storeName',
				'valueAndText',
				false,
				[isSelectAll],
				BasePath+'/store/get_all?storeType=22',
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				[id],
				'storeNo',
				'storeName',
				'valueAndText',
				false,
				[isSelectAll],
				null,
				{},
				null,
				null);
	}
};

billimimport.businessTypeJsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//========================业务类型END========================

billimimport.clearAll = function(formId){
	$("#"+formId).form("clear");
};

//新增和修改页面的取消按钮
billimimport.closeShowWin = function(id){
	$('#'+id).window('close');;
};

billimimport.initBrandClass = function(){
	$('#brandClass').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=BRAND_CLASS',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });
};


//格式化状态
billimimport.init_status_Text = {};
billimimport.columnStatusFormatter = function(value, rowData, rowIndex){
	return billimimport.init_status_Text[value];
};

//校验开始日期和结束日期的大小
function isStartEndDate(startDate,endDate){
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){   
             return false;   
        }   
     }   
     return true;   
} 


//弹出选择箱号的页面
billimimport.addBoxDetail_module = function(gid){
	$("#dataGridJGItem").datagrid('clearData');
	
	$('#showGirdName').val(gid);
	
	var formId = 'dataForm';
	if(gid == 'moduleEdit'){
		formId = 'dataEditForm';
	}
	$('#locnoBox').val(billimimport.locno);
	$('#ownerNoBox').val($('#ownerNoHide_'+formId).val());
	$('#sysNoForItem').val($('#sysNoHide_'+formId).val());//品牌库
	$('#supplierNoHidden').val($('#supplierNo_'+formId).combobox('getValue'));//供应商
	$("#openUIItem").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUIItem').show().window('open');
};

//查询箱号信息
billimimport.searchBox = function(){
	
	var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/con_box_dtl/findCnBoxAndNum';
	
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
    
};

//确定选择的箱号
billimimport.confirmBox = function(){
	var checkedRows = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的记录!',1);
		return;
	}
    
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"个箱号信息吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show");
        	$.each(checkedRows, function(index, item){
        	    var  rowData = {
        	    		"boxNo":item.boxNo,
        	    		"importQty":item.qty
        	    };
        		//把选择的商品编码行记录插入到父窗体中
        	    billimimport.insertRowAtEnd($('#showGirdName').val(),rowData);
        	    wms_city_common.loading();
        	    $('#openUIItem').window('close').hide();

            }); 
        	
        }
	});
};

billimimport.insertRowAtEnd = function(gid,rowData){
	wms_city_common.loading("show");
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
	wms_city_common.loading();
	return  tempIndex;
};

billimimport.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};

billimimport.endEdit = function(gid){
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
billimimport.save = function(id){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = billimimport.endEdit(id);
	if(!tempFlag){
		alert('数据验证没有通过！',1);
		return;
	}
	var formId='dataForm';
	var showWinObj = 'showDialog';
	if(id == 'moduleEdit'){
		formId='dataEditForm';
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
        var checkUrl=BasePath+'/billimimport/get_is_exist.json';
        var checkData={};
        var hashObject = {};
        for (var i = 0; i < inserted.length; i++) {
        	//checkData.moduleName = inserted[i]['boxNo'];
        	var boxNo = inserted[i]['boxNo'];
       	 	if (hashObject[boxNo]){
	       		 alert('箱号【'+boxNo+'】重复！',1);
	       		 return;
                //return true;
            } else {
                hashObject[boxNo] = true;
            }
        }
    }
    var deleted = tempObj.datagrid('getChanges', "deleted");
    var updated = tempObj.datagrid('getChanges', "updated");
    var effectRow = {
    		inserted:JSON.stringify(inserted),
    		deleted:JSON.stringify(deleted),
    		updated:JSON.stringify(updated),
    		"ownerNo":$("#ownerNo_"+formId).combobox('getValue'),
			"locno":billimimport.locno,
			"importNo":$("#importNo_"+formId).val()
    };
    wms_city_common.loading("show");
    $.post(BasePath+'/bill_im_import_dtl/saveImImportDetail', effectRow, function(result) {
    	wms_city_common.loading();
        if(result.flag=='true'){
            alert('保存成功!',1);
            tempObj.datagrid('acceptChanges');
            $('#'+showWinObj).window('close');  
            billimimport.loadDataGrid();
        }else if(result.flag=='warn'){
        	alert(result.msg,1);
        	return;
        }else{
        	alert('操作异常！',1);
        	return;
        }
    }, "JSON").error(function() {
    	wms_city_common.loading();
    	alert('保存失败!',1);
    });
};
billimimport.onLoadSuccess = function(data){
	if(data.footer[1] != null){
			billimimport.mainSumFoot = data.footer[1];
		}else{
			var rows = $('#dataGridJG').datagrid('getFooterRows');
   			rows[1] = billimimport.mainSumFoot;
			$('#dataGridJG').datagrid('reloadFooter');
		}
};
$(document).ready(function(){
	
	$('#showDialog,#showEditDialog,#openUIItem,#showViewDialog').show();
	billimimport.loadLoc();
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNoCondition","ownerNo_dataForm","ownerNo_dataEditForm","ownerNo_dataViewForm","ownerNoBox"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[true,false,false,false,true],
			BasePath+'/entrust_owner/get_biz',
			{},
			wms_city_common.init_Owner_Text,
			null);
	//初始化供应商(不含查询)
	wms_city_common.comboboxLoadFilter(
			["supplierNo_dataForm","supplierNo_dataEditForm","supplierNo_dataViewForm"],
			'supplierNo',
			'supplierName',
			'valueAndText',
			false,
			[false,false,false],
			BasePath+'/supplier/get_all',
			{},
			wms_city_common.init_Supplier_Text,
			null);
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
			billimimport.businessTypes,
			null);
	//初始化业务类型
	wms_city_common.comboboxLoadFilter(
			["businessTypeCondition","businessType_dataForm","businessType_dataEditForm","businessType_dataViewForm"],
			null,
			null,
			null,
			true,
			[true,false,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IMIMPORT_BUSINESS_TYPE',
			{},
			billimimport.businessTypes,
			null);
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IMPORTOR_STATUS',
			{},
			billimimport.init_status_Text,
			null);
	
	//$("#orderDateBeginCondition").datebox('setValue',billimimport.getDate(-6));
	//$("#orderDateEndCondition").datebox('setValue',billimimport.getDate(0));
	$("#createtmBeginCondition").datebox('setValue',billimimport.getDate(-2));
	
	$('#moduleView_dtl').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billimimport.poQty = data.footer[1].poQty;
		   				billimimport.receiptQty = data.footer[1].receiptQty;
		   				billimimport.importQty = data.footer[1].importQty;
		   				billimimport.differQty = data.footer[1].differQty;
		   			}else{
		   				var rows = $('#moduleView_dtl').datagrid('getFooterRows');
			   			rows[1]['poQty'] = billimimport.poQty;
			   			rows[1]['receiptQty'] = billimimport.receiptQty;
			   			rows[1]['importQty'] = billimimport.importQty;
			   			rows[1]['differQty'] = billimimport.differQty;
			   			$('#moduleView_dtl').datagrid('reloadFooter');
		   			}
		   		}
			}
		);

});

//导出
exportExcel=function(){
	exportExcelBaseInfo('dataGridJG','/billimimport/do_export.htm','预到货通知单导出');
};

/**
 * ===================================尺码横排======================================================
 */

//弹出详情页面
billimimport.showView = function(rowData,rowIndex){
	//清空datagrid数据
	$('#moduleView').datagrid('clearData');
	
	var formId = 'dataViewForm';
	
	billimimport.clearAll(formId);
	
	//仓库编码设置为只读，不可修改
	$('#importNo_'+formId).attr("readOnly",true);

	$('#detailViewDiv').show();
	
	//设置信息
	billimimport.setDetail(rowData,formId);
	//明细信息
	billimimport.loadDetail(rowData,rowIndex);
	$('#moduleView_dtl').datagrid({'url':BasePath+'/bill_im_import_dtl/dtllist.json?locno='+billimimport.locno+'&importNo='+$('#importNo_'+formId).val()+'&sort=od.box_no,od.item_no,od.size_no','pageNumber':1 });
	//弹窗
	$("#showViewDialog").window('open');
};


billimimport.loadDetail = function(rowData,rowIndex){
	
	billimimport.curRowIndex=rowIndex;
	
	
	billimimport.initGridHeadAndData(rowData);
   
};


//同时加载表头和表体
billimimport.initGridHeadAndData = function(rowData){
	
	 var beforeColArr = billimimport.preColNamesDtl1;
	 var afterColArr = billimimport.endColNamesDtl1; 
	 var columnInfo = billimimport.getColumnInfo(rowData.sysNo,beforeColArr,afterColArr);
	 
	 var url = BasePath+'/bill_im_import_dtl/queryBillImImportDtlDTOlList';
	 var queryParams = {importNo:rowData.importNo,sysNo:rowData.sysNo,ownerNo:rowData.ownerNo};
	 
	 $("#moduleView").datagrid({
		 queryParams:queryParams,
		 url:url,
         columns:columnInfo.columnArr
     }); 
};

billimimport.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billimimport.sizeTypeFiledName
        },
        cache: true,
        async: false,
        success: function(returnData){
         	resultData.columnArr = returnData.returnCols;
         	resultData.startType = returnData.startType;
         	resultData.maxType = returnData.maxType;
		}
      });
      return resultData;
};
function getHtmlByTemplate(data,importNo,supplierName,sDate,expDate,transNo){
	var sizeCols = data["sizeCols"];
	var colLen = 6+sizeCols.length;
	var colArray = [];
	var colMap = {};
	colArray[0] = "brandNoStr";
	colMap["brandNoStr"] = "品牌";
	colArray[1] = "itemNo";
	colMap["itemNo"] = "商品编码";
	colArray[2] = "itemName";
	colMap["itemName"] = "商品名称";
	for(var index=3;index<colLen-3;index++){
		colArray[index] = sizeCols[index-3];
		colMap[sizeCols[index-3]] = sizeCols[index-3];
	}
	colArray[colLen-3] = "allCount";
	colMap["allCount"] = "总数";
	colArray[colLen-2] = "unitName";
	colMap["unitName"] = "单位";
	colArray[colLen-1] = "boxNo";
	colMap["boxNo"] = "箱号";
	
	
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='3'>预到货通知单</td></tr>";
	html += "<tr style='height:40px;'><td colspan='3' style='width:60%;'>单据号条码：</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;据&nbsp;&nbsp;&nbsp;&nbsp;号："+importNo+"</td><td>源预到货通知单："+transNo+"</td><td>&nbsp;</td></tr>";
	html += "<tr><td>供&nbsp;&nbsp;&nbsp;&nbsp;应&nbsp;&nbsp;&nbsp;&nbsp;商："+supplierName+"</td><td>源&nbsp;&nbsp;发&nbsp;&nbsp;货&nbsp;&nbsp;日&nbsp;&nbsp;期："+sDate+"</td><td style='text-align:right;'>预到货日期："+expDate+"</td></tr>";
	html += "</table>";
	html += "<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'><tr bgcolor='#fff'>";
	for(var index=0;index<colLen;index++){
		html += "<td>"+colMap[colArray[index]]+"</td>";
	}
	html += "</tr>";
	var temp;
	var total = 0;
	for(var i=0;i<data["total"];i++){
		temp = null;
		temp = data["rows"][i];
		html += "<tr bgcolor='#fff'>";
		for(var j=0;j<colLen;j++){
			if(temp[colArray[j]]){
				html += "<td>"+temp[colArray[j]]+"</td>";
			}else{
				if('allCount' == colArray[j]){
					html += "<td>0</td>";
				}else{
					html += "<td>&nbsp;</td>";
				}
			}
			
		}
		html += "</tr>";
		total += temp["allCount"];
	}
	html += "</table>";
	html += "<table  style='width:100%;'>";
	html += "<tr><td style='text-align:right;width:70%;'>总箱数："+data["total"]+"</td><td style='text-align:center;'>件数："+total+"</td></tr>";
	html += "</table>";
	return html;
}
billimimport.printBillImImport2 = function(){
	var importNo = $('#importNo_dataViewForm').val();
	var transNo = $('#transNo_dataViewForm').val();
	var supplierName = $('#supplierNo_dataViewForm').combobox("getText");
	var sDate = $('#orderDate_dataViewForm').datebox('getValue');;
	var expDate = $('#requestDate_dataViewForm').datebox('getValue');;
	var headInfo = getHeadInfo(importNo,transNo,supplierName, sDate, expDate);//头部信息
	var bodyHtml = "<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>";
	var titleHtml = $("#detailViewDiv .datagrid-view2 table").eq(0).html(); //标题信息
	var dataHtml = $("#detailViewDiv .datagrid-view2 table").eq(1).html();  //数据
	var footHtml = $("#detailViewDiv .datagrid-view2 table").eq(2).html();  //汇总信息
	var resulthtml = headInfo+bodyHtml+titleHtml+dataHtml+footHtml+"</table><style>tr{background:#fff}</style>";
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP==null)return;
	LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
	LODOP.ADD_PRINT_HTM(0,0,"100%","100%",resulthtml);
	LODOP.ADD_PRINT_BARCODE(53,110,250,40,"128A",importNo);
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
};
function removeHtml(value,beginStr,endStr){
	
	var beginIdx = value.indexOf(beginStr);
	var endIdx = value.indexOf(endStr,beginIdx);
	var temp = '';
	while(beginIdx >= 0 && endIdx > beginIdx){
		temp = value.substring(beginIdx,endIdx+endStr.length);
		value = value.replace(temp,'');
		beginIdx = value.indexOf(beginStr);
		endIdx = value.indexOf(endStr,beginIdx);
	}
	return value;
}
function getHeadInfo(importNo,transNo,supplierName,sDate,expDate){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='3'>预到货通知单</td></tr>";
	html += "<tr style='height:40px;'><td colspan='3' style='width:60%;'>单据号条码：</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;据&nbsp;&nbsp;&nbsp;&nbsp;号："+importNo+"</td><td>源预到货通知单："+transNo+"</td><td>&nbsp;</td></tr>";
	html += "<tr><td>供&nbsp;&nbsp;&nbsp;&nbsp;应&nbsp;&nbsp;&nbsp;&nbsp;商："+supplierName+"</td><td>源&nbsp;&nbsp;发&nbsp;&nbsp;货&nbsp;&nbsp;日&nbsp;&nbsp;期："+sDate+"</td><td style='text-align:right;'>预到货日期："+expDate+"</td></tr>";
	html += "</table>";
	return html;
}

billimimport.receipt= function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	$("#dataGridJG").datagrid("uncheckAll");
	var params={};
	$.messager.confirm("确认","您确定要对这"+checkedRows.length+"个单号收货吗？", function (r){  
        if (r) {
        	for(var i=0;i<checkedRows.length;i++){
        		var rowdata=checkedRows[i];
        		if(rowdata.status!='11'){
        			alert("请选择已审核的单据收货！");
        			return;
        		}else{
        			params['listIm['+i+'].importNo']=rowdata.importNo;
        			params['listIm['+i+'].locno']=rowdata.locno;
        			params['listIm['+i+'].ownerNo']=rowdata.ownerNo;
        			params['listIm['+i+'].sumQty']=rowdata.sumQty;
        			params['listIm['+i+'].boxNoQty']=rowdata.boxNoQty;
        		}
        	}

        	$.ajax({
        		cache : false,
        		type : 'POST',
        		data:params,
        		dataType : "json",
//        		contentType:"application/json",
        		url:BasePath+'/billimimport/toBillimReceipt',
        		success : function(data) {
        			if(data.result=='success'){
        				billimimport.loadDataGrid();
        				alert("收货成功！");
        			}else{
        				alert("收货失败！");
        			}
        		}
        	});
        	}
        });
};