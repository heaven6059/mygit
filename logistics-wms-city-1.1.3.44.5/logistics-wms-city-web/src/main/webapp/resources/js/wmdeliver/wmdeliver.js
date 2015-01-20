/**
 * 退厂确认（配送单）
 * 
 */

var wmdeliver = {};

wmdeliver.locno;

//加载Grid数据Utils
wmdeliver.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//将数组封装成一个map
wmdeliver.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

wmdeliver.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//加载查询
wmdeliver.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/wmdeliver/list.json?locno='+wmdeliver.locno,'title':'退厂配送单列表','pageNumber':1 });
};

wmdeliver.loadDataEditDetailGrid = function(rowData){
	var url = BasePath+'/wmdeliverdtl/findBillWmDeliverDtlGroupByPage.json';
	var queryParams = {deliverNo:rowData.deliverNo,locno:rowData.locno,ownerNo:rowData.ownerNo};
	wmdeliver.loadGridDataUtil('module', url, queryParams);
};

//加载明细
wmdeliver.loadDataDetailGrid = function(rowData){
	
	$('#moduleEdit').datagrid('clearData');
	var fromObj=$('#dataEditForm');
	$('input[name=deliverNo]',fromObj).attr("readOnly",true);
	$('#dataEditForm').form('load',rowData);
	
	var url = BasePath+'/wmdeliverdtl/dtl_findBillWmDeliverDtlByPage.json';
	var queryParams = {deliverNo:rowData.deliverNo,locno:rowData.locno,ownerNo:rowData.ownerNo};
	wmdeliver.loadGridDataUtil('moduleEdit', url, queryParams);
	
	//弹窗
	$("#showEditDialog").window('open'); 
	wmdeliver.setBtnStyleDisable();
	
};

wmdeliver.loadDetail = function(locno){
	$('#dataForm').form('load',BasePath+'/wmdeliver/get?locno='+locno);
	$("#locno").attr('readOnly',true);
};

wmdeliver.checkExistFun = function(url,checkColumnJsonData){
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

//查询退厂确认单
wmdeliver.searchWmDeliver = function(){
	
	$('#searchForm input[name=locno]').val(wmdeliver.locno);
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/wmdeliver/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清除查询条件
wmdeliver.searchWmWmDeliverClear = function(){
	$('#searchForm').form("clear");
	$('#brandNo').combobox("loadData",[]);
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

//修改退厂确认单主表信息
wmdeliver.modifyWmDeliver = function(id){
    var fromObj=$('#'+id);
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    wms_city_common.loading("show","正在修改主信息......");
    var url = BasePath+'/wmdeliver/modifyWmDeliver';
    fromObj.form('submit', {
   			url: url,
   			onSubmit: function(params){
   				params.ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
   			},
   			success: function(data){
   				if(data=="success"){
   					alert('修改成功!');
					$("#showEditDialog").window('close'); 
					wmdeliver.loadDataGrid();
					//bmdefloc.clearAll();
				}else{
					alert('修改异常,请联系管理员!',2);
				}
   				wms_city_common.loading();
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   				wms_city_common.loading();
   			}
   	});
    
};

//弹出修改页面
wmdeliver.showEdit = function(rowData,flag){
	
	//清空datagrid数据
	$('#module').datagrid('clearData');
	var fromObj=$('#dataForm');
	//仓库编码设置为只读，不可修改
	$('input[name=deliverNo]',fromObj).attr("readOnly",true);
	$('#detailEditDiv').show();
	
		//设置信息
	//wmdeliver.setDetail(rowData,fromObj);
	$("#detailDiv").show();
	$('#dataForm').form('load',rowData);
	
	//明细信息
	wmdeliver.loadDataEditDetailGrid(rowData);
	
	if(flag == 1){
		$('#btn-add').hide();
		$('#btn-modify').show();
		//$('#detail-toolbar,#detailEditDiv').show();
		//$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
		if(rowData.status!=null&&rowData.status!=""&&rowData.status!="10"){
			//$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').hide();
			//$('#moduleEdit').datagrid({toolbar: '#'});
		}else{
			//$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').show();
			//$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
		}
	}else{
		$('#btn-add').show();
		$('#btn-modify').hide();
		//$('#moduleEdit').datagrid({toolbar: '#'});
	}
	
	//如果不是新建状态，则按钮都需要隐藏起来
//	if(rowData.status!=null&&rowData.status!=""&&rowData.status!="10"){
//		$('#moduleEdit').datagrid({toolbar: '#'});
//		$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').hide();
//	}else{//新建状态
//		$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').show();
//		$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
//	}
	
	//弹窗
	$("#showDialog").window('open'); 
	wmdeliver.setBtnStyleDisable();
};

//弹出修改页面
wmdeliver.showModify = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length != 1){
		alert('请选择一条记录！',1);
		return;
	}
	
	$.each(checkedRows,function(index,item){
		if(item.status != '10'){
			alert("只能修改状态为新建的单据!",1);
			return;
		}
		wmdeliver.showEdit(checkedRows[0],1);
	});
};

//弹出新增页面
showAdd=function(){
	var fromObj=$('#dataForm');
	
	wmdeliver.clearAll(fromObj);
	
	$('#deliverNo').val("系统自动生成");
	
	$("#detailDiv").hide();
	$('#btn-add').show();
	$('#btn-modify').hide();
	
	$('input[id=locno]',fromObj).combobox('enable');
	$('input[id=locnoHide]',fromObj).attr("disabled",true);
	
	$('input[id=ownerNo]',fromObj).combobox('enable');
	$('input[id=ownerNoHide]',fromObj).attr("disabled",true);
	
	$('input[id=supplierNo]',fromObj).combobox('enable');
	$('input[id=supplierNoHide]',fromObj).attr("disabled",true);
	
	deleteAllGridCommon('module');
	$('#showDialog').window('open');  
	wmdeliver.setBtnStyleEnable();
};

//审核操作
wmdeliver.auditWmDeliver = function(){
	
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	var checkUrl=BasePath+'/wmdeliverdtl/get_count.json';
	var isFun = "";
	var noStrs = [];
	var delTip = "";
	$.each(checkedRows, function(index, item){
		noStrs.push(item.ownerNo+"#"+item.deliverNo);
		if(item.status!=null&&item.status!=""&&item.status!="10"){
			delTip = delTip+ "单据:"+item.deliverNo+"；";
		}
		
		var checkData={
		    		"locno" : wmdeliver.locno,
		    		"deliverNo" : item.deliverNo,
		    		"ownerNo" : item.ownerNo
		};
		if(!wmdeliver.checkExistFun(checkUrl,checkData)){
			isFun += "[" + item.deliverNo + "]";
			
	    }
	}); 
	if(delTip!=null && delTip!=""){
		delTip = delTip+"已处理，不能再次操作！";
		alert(delTip,1);
		return;
	}
	if(isFun!=null && isFun!=""){
		isFun = "单据:" + isFun + "无可用明细，不允许审核！";
		alert(isFun,1);
		return;
	}
    //2.绑定数据
	var data={
	    "noStrs":noStrs.join(",")
	};
	
	$.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){ 
        if (r) {
        	//4. 审核
		    url = BasePath+'/wmdeliver/auditWmDeliver';
			wms_city_common.loading('show');
		    wmdeliver.ajaxRequest(url,data,function(result,returnMsg){
		    	wms_city_common.loading(); 
		    	if(result.flag=='true'){
        			 //4.审核成功,重新加载数据
        			 alert('审核成功!');
        			 wmdeliver.loadDataGrid();
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
saveMain=function(){
	var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return;
     }
     //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
//     var checkUrl=BasePath+'/wmdeliver/get_count.json';
//     
//     var deliverNoVal = $("input[id=deliverNo]",fromObj).val();
//     var checkDataNo = {"deliverNo":deliverNoVal};
//     if(wmdeliver.checkExistFun(checkUrl,checkDataNo)){
//    	  alert('退厂确认单号已存在,不能重复！',1);
//    	  $("input[id=deliverNo]",fromObj).focus();
//    	  return;
//     }
     
     //3. 保存
     wms_city_common.loading("show","正在保存主信息......");
     var url = BasePath+'/wmdeliver/addWmDeliver';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
				
			},
			success: function(returnData){
				
				var dataObj = eval('(' + returnData + ')'); 
				
				if(dataObj.returnMsg){
					
				     var ownerNoVal = $("input[id=ownerNo]",fromObj).combobox('getValue');
				     var getDataNo = {"deliverNo":dataObj.deliverNo,"ownerNo":ownerNoVal,"locno":dataObj.locno};
					
					 alert('新增成功!');
					 //获取信息
					 $.ajax({
							async : true,
							cache : false,
							type : 'POST',
							dataType : "json",
							data: getDataNo,
							url:BasePath+'/wmdeliver/get_biz',
							success : function(data) {
								
								wmdeliver.clearAll(fromObj);
								
								wmdeliver.setDetail(data[0],fromObj);
								
								$('#module').datagrid('clearData');
								
								$("#detailDiv").show();
								$("#btn-add").hide();
								$("#btn-modify").show();
							}
						});
				 }else{
					 alert('新增异常,请联系管理员!',2);
				 }
				wms_city_common.loading();
		    },
			error:function(){
				alert('新增失败,请联系管理员!',2);
				wms_city_common.loading();
			}
	   });  
};


wmdeliver.setBtnStyleDisable = function(){
	
	var dataForm = $('#dataForm');
	var dataEditForm = $('#dataEditForm');
	
	$('input[id=supplierNo]',dataForm).combobox('disable');
	$('input[id=ownerNo]',dataForm).combobox('disable');
	$('input[id=ownerName]',dataForm).attr("disabled",true);
	
	$('input[id=supplierNo]',dataEditForm).combobox('disable');
	$('input[id=ownerNo]',dataEditForm).combobox('disable');
	$('input[id=ownerName]',dataEditForm).attr("disabled",true);
};

wmdeliver.setBtnStyleEnable = function(){
	
	var dataForm = $('#dataForm');
	var dataEditForm = $('#dataEditForm');
	
	$('input[id=supplierNo]',dataForm).combobox('enable');
	$('input[id=ownerNo]',dataForm).combobox('enable');
	$('input[id=ownerName]',dataForm).attr("disabled",false);
	
	$('input[id=supplierNo]',dataEditForm).combobox('enable');
	$('input[id=ownerNo]',dataEditForm).combobox('enable');
	$('input[id=ownerName]',dataEditForm).attr("disabled",false);
};

//获取明细信息
wmdeliver.setDetail = function(rowData,fromObj){
	
	$("input[id=deliverNo]",fromObj).val(rowData.deliverNo);
	$("input[id=operateDate]",fromObj).datebox("setValue",rowData.operateDate);
	
	$("input[id=locno]",fromObj).combobox("select",rowData.locno);
	$("input[id=locno]",fromObj).combobox('disable');
	$("input[id=locnoHide]",fromObj).attr("disabled",false);
	$("input[id=locnoHide]",fromObj).val(rowData.locno);
	
	$("input[id=supplierNo]",fromObj).combobox("select",rowData.supplierNo);
	$("input[id=supplierNo]",fromObj).combobox('disable');
	$("input[id=supplierNoHide]",fromObj).attr("disabled",false);
	$("input[id=supplierNoHide]",fromObj).val(rowData.supplierNo);
	
	$("input[id=ownerNo]",fromObj).combobox("select",rowData.ownerNo);
	$("input[id=ownerNo]",fromObj).combobox('disable');
	$("input[id=ownerNoHide]",fromObj).attr("disabled",false);
	$("input[id=ownerNoHide]",fromObj).val(rowData.ownerNo);
	
	//$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
	
	$("input[id=memo]",fromObj).val(rowData.memo);
	$("input[id=creator]",fromObj).val(rowData.creator);
	$("input[id=createtm]",fromObj).val(rowData.createtm);
	$("input[id=editor]",fromObj).val(rowData.editor);
	$("input[id=edittm]",fromObj).val(rowData.edittm);
};

//删除
wmdeliver.deleteWmDeliver=function(){
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
        		noStrs.push(item.deliverNo+"#"+item.locno+"#"+item.ownerNo);
        		if(item.status!=null&&item.status!=""&&item.status!="10"){
        			delTip = "单据:"+item.deliverNo+"；";
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
		    url = BasePath+'/wmdeliver/deleteWmDeliver';
		    wms_city_common.loading('show');
		    wmdeliver.ajaxRequest(url,data,function(result,returnMsg){
		    	wms_city_common.loading();
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 wmdeliver.loadDataGrid();
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

//加载供应商
wmdeliver.loadSupplier = function(formObj){
	wms_city_common.comboboxLoadFilter(
			["supplierNoCondition"],
			"supplierNo",
			"supplierName",
			"supplierName",
			false,
			[true],
			BasePath+'/supplier/getSupplierByRecede',
			{locno:wmdeliver.locno},
			wms_city_common.init_Supplier_Text,
			null);
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/supplier/getSupplierByRecede?locno='+wmdeliver.locno,
		success : function(data) {
			$('input[id=supplierNo]',formObj).combobox({
			    data:data,
			    valueField:'supplierNo',    
			    textField:'valueAndText',
			    panelHeight:150,
			    loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.supplierNo+'→'+tempData.supplierName;
			       		 }
	       	 		}
	    			return data;
	   			 }, 
			    onSelect:function(data){}
			});
			/*if(null!=data){
				$('input[id=supplierNo]',formObj).combobox("select",data[0].supplierNo);
			}*/
		}
	});
};

//加载委托业主信息
wms_city_common.init_Owner_Text = {};
wmdeliver.loadOwner = function(formObj){
	wms_city_common.comboboxLoadFilter(
			["ownerNoCondition"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[true],
			BasePath+'/entrust_owner/get_biz',
			{},
			wms_city_common.init_Owner_Text,
			null);
	
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
			    textField:'valueAndText',
			    panelHeight:150,
			    loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.ownerNo+'→'+tempData.ownerName;
			       		 }
	       	 		}
	    			return data;
	   			 }, 
			    onSelect:function(data){
			    	$("input[id=ownerName]",formObj).val(data.ownerName);
			    }
			});
			if(null!=data){
				$('input[id=ownerNo]',formObj).combobox("select",data[0].ownerNo);
			}
		}
	});
};

//加载仓库信息
wmdeliver.loadLoc = function(formObj){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			wmdeliver.locno = data.locno;
		}
	});
};

//清理form
wmdeliver.clearAll = function(fromObj){
	fromObj.form("clear");
	//$("input[id=deliverNo]",fromObj).attr('readOnly',false);
};

//清理搜索箱号的form,
wmdeliver.clearFormById = function(){
	$('#recedeNoSel,#recheckNoSel').val('');
};

//清理form和不为空的提示
wmdeliver.clearFormAndTip = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};

//新增和修改页面的取消按钮
wmdeliver.closeShowWin = function(id){
	$('#'+id).window('close');
	//$('#locno,#locname').validatebox('reset');
	//$('#showDialog').window('destroy');
};


//格式化状态
wmdeliver.columnStatusFormatter = function(value, rowData, rowIndex){
	return wmdeliver.init_status_Text[value];
};

//状态
wmdeliver.init_status_Text = {};
wmdeliver.init_status = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition","status"],
			null,
			null,
			null,
			true,
			[true, false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_DELIVER_STATUS',
			{},
			wmdeliver.init_status_Text,
			null);
};

//格式化退厂类型
wmdeliver.columnRECEDE_TYPEFormatter = function(value, rowData, rowIndex){
	return wmdeliver.init_RECEDE_TYPEText[value];
};

//退厂类型
wmdeliver.init_RECEDE_TYPE = function(){
	
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=RECEDE_TYPE',
		success : function(data) {
			wmdeliver.init_RECEDE_TYPEText=wmdeliver.converStr2JsonObj(data);
			$('#recedeType,#recedeTypeCondition').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"150",
			  });
			
		}
	});

};

//弹出选择箱号的页面
wmdeliver.addItemDetail_module = function(gid){
	$("#dataGridJGItem").datagrid('clearData');
	
	$('#showGirdName').val(gid);
	
	var fromObj=$('#dataForm');
	if(gid == 'moduleEdit'){
		fromObj=$('#dataEditForm');
	}
	$('#supplierNoSel').val($('input[id=supplierNoHide]',fromObj).val());
	$('#locnoSel').val(wmdeliver.locno);
	
	$('#openUIItem').show().window('open');
};

//查询箱号信息
wmdeliver.searchItem = function(){
	
	var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/wmrecheck/findLabelNoByRecheckNoPage';
	
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
    
};

//确定选择的箱号
wmdeliver.confirmItem = function(){
	var checkedRows = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的记录!',1);
		return;
	}
	
	
//    var noStrs = [];
//    var delTip = "";
    
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"个箱号吗？", function (r){
        if (r) {
        	
        	$.each(checkedRows, function(index, item){
        	    var  rowData = {
        	    		"supplierName":item.supplierName,
        	    		"containerNo":item.containerNo,
        	    		"recheckNo":item.recheckNo,
        	    		"boxNo":item.labelNo,
        	    		"supplierNo":item.supplierNo,
        	    		"sumQty":item.sumQty,
        	    		"recedeNo":item.recedeNo
        	    };
        		//把查询到的商品信息行记录插入到datagrid中
        	    wmdeliver.insertRowAtEnd('module',rowData);
            }); 
        	$('#openUIItem').window('close').hide();
        	
//        	//请求后台查询数据
//        	$.ajax({
//        		async : false,
//        		cache : false,
//        		type : 'POST',
//        		dataType : "json",
//        		data:{
//        			selectLabel:JSON.stringify(checkedRows)
//        		},
//        		url:BasePath+'/wmdeliverdtl/getLabelInfoDtlsList',
//        		success : function(returnData) {
//        		  	
//                  $.each(returnData, function(index, item){
//            	    var  rowData = {
//            	    		"boxNo":item.labelNo,
//            	    		"containerNo":item.containerNo,
//            	    		"itemNo":item.itemNo,
//            	    		"itemId":item.itemId,
//            	    		"itemName":item.itemName,
//            	    		"styleNo":item.styleNo,
//            	    		"sizeNo":item.sizeNo,
//            	    		"colorNoStr":item.colorNoStr,
//            	    		"itemQty":item.qty,
//            	    		"recheckNo":item.recheckNo,
//            	    		"recedeNo":item.recedeNo
//            	    };
//            		//把查询到的商品信息行记录插入到datagrid中
//            	    wmdeliver.insertRowAtEnd($('#showGirdName').val(),rowData);
//            	    
//            	    $('#openUIItem').window('close').hide();
//
//                }); 
//        			//wmdeliver.init_RECEDE_TYPEText=wmdeliver.converStr2JsonObj(data);
//        		}
//        	});
        	
//            $.each(checkedRows, function(index, item){
//        	    var  rowData = {
//        	    		"itemNo":item.itemNo,
//        	    		"styleNo":item.styleNo,
//        	    		"colorNoStr":item.colorNoStr,
//        	    		"sizeNo":item.sizeNo,
//        	    		"brandNoStr":item.brandNoStr
//        	    };
//        		//把选择的商品编码行记录插入到父窗体中
//        	    wmdeliver.insertRowAtEnd($('#showGirdName').val(),rowData);
//        	    
//        	    $('#openUIItem').window('close').hide();
//        	
////            	noStrs.push(item.deliverNo+"#"+item.locno+"#"+item.ownerNo+"#"+item.expType);
////            	if(item.status!=null&&item.status!=""&&item.status!="10"){
////            			delTip = "单据:"+item.deliverNo+"；";
////            	}
//            }); 
        	
        }
	});
    

};

//新插入一行明细
wmdeliver.insertRow_module = function(gid){
//	var fromObj=$('#dataForm');
//	if(gid == 'moduleEdit'){
//		fromObj=$('#dataEditForm');
//	}
	
	wmdeliver.insertRowAtEnd(gid);
	
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
//			   "deliverNoIsNull":"1"
//		},
//		success: function(jqData){
//			$(ed.target).combobox('loadData',jqData); 
//		}
//	});
	
};

wmdeliver.insertRowAtEnd = function(gid,rowData){
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

wmdeliver.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};

wmdeliver.endEdit = function(gid){
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
wmdeliver.save = function(id){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = wmdeliver.endEdit(id);
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
	var rowArr = tempObj.datagrid('getRows');
	if(rowArr.length < 1){
		alert('明细不能为空！',1);
		return;
	}
	
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动！',1);
		return;
	}
	
	
	var inserted = tempObj.datagrid('getChanges', "inserted");
	 var deleted = tempObj.datagrid('getChanges', "deleted");
    if(inserted.length>0||deleted.length>0){
        var hashObject = {};
        var hashLableNo = {};
        var noStrs = [];//标签号编码
        for (var i = 0; i < inserted.length; i++){
        	var noAndSize = inserted[i]['labelNo'];
       	 	if (hashObject[noAndSize]){
       	 		alert('标签号：【'+noAndSize+'】重复！',1);
       		 	return;
            } else {
                
            }
        	
       	 	var labelNo = inserted[i]['boxNo'];
       	 	if(!hashLableNo[labelNo]){
       	 		hashLableNo[labelNo] = true;
       	 		noStrs.push(labelNo);
       	 	}
       	 	
//        	checkData.boxNo = inserted[i]['boxNo'];
//            if(wmdeliver.checkExistFun(checkUrl,checkData)){
//        		alert('标签号【"+checkData.boxNo+"】已经存在!',1);
//        		return;
//    	    }
            
        }
        
        //2.绑定数据
    	var data={
    	    "noStrs":noStrs.join(","),
    	    "deliverNo":$("input[id=deliverNo]",fromObj).val(),
    	    "locno": wmdeliver.locno
    	};
    	//4.
    	
    	
    	
		 var updated = tempObj.datagrid('getChanges', "updated");
		 var effectRow = {
		    		inserted:JSON.stringify(inserted),
		    		deleted:JSON.stringify(deleted),
		    		updated:JSON.stringify(updated),
		    		"ownerNo":$("input[id=ownerNo]",fromObj).combobox('getValue'),
					"locno": wmdeliver.locno,
					"supplierNo":$("input[id=supplierNo]",fromObj).combobox('getValue'),
					"deliverNo":$("input[id=deliverNo]",fromObj).val()
		    };
		 	wms_city_common.loading('show');
		    $.post(BasePath+'/wmdeliverdtl/saveDtl', effectRow, function(result) {
		    	wms_city_common.loading();
		    	if(result.success){
		            alert('保存成功!',1);
		            tempObj.datagrid('acceptChanges');
		            wmdeliver.clearAll(fromObj);
		            $('#'+showWinObj).window('close');  
		            wmdeliver.loadDataGrid();
		        }else{
		        	alert(result.msg,1);
		        	return;
		        }
		    }, "JSON").error(function() {
		    	wms_city_common.loading();
		    	alert('保存失败!',1);
		    });
    	
//	    var url = BasePath+'/wmdeliverdtl/validateLabelNo';
//	    wmdeliver.ajaxRequest(url,data,function(result,returnMsg){
//    		 if(result.flag=='warn'){
//    			 alert(result.returnMsg,1);
//    		 }else if(result.flag=='fail'){
//    			 alert('校验标签号合法性异常！',2);
//    		 }else if(result.flag=='success'){
//    			   
//    		 }else{
//    			 alert('异常！',1);
//    		 }
//    	}); 
        
    }
   
};

/**
 * 数组是否重复，如重复则返回重复元素 
 * @return {}
 */
wmdeliver.isDouble = function() {
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


$(document).ready(function(){
	$("#createtmBeginCondition").datebox('setValue',getDateStr(-2));
	$('#showDialog,#showEditDialog,#openUIItem').show();
	
	wmdeliver.loadLoc();
	wmdeliver.loadOwner();
	wmdeliver.loadSupplier();//加载供应商
	
	wmdeliver.init_RECEDE_TYPE();//退厂类型
	wmdeliver.init_status();//初始化状态
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
	
	//待复核明细列表
	$('#moduleEdit').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					wmdeliver.itemQty = data.footer[1].itemQty;
				} else {
					var rows = $('#moduleEdit').datagrid('getFooterRows');
					rows[1]['itemQty'] = wmdeliver.itemQty;
					$('#moduleEdit').datagrid('reloadFooter');
				}
			}
		}
	});
	
});

//导出
exportExcel=function(){
	//getSelections
	var deliverNos="";
	var checkedRows = $('#dataGridJG').datagrid("getChecked");
	for(var i=0;i<checkedRows.length;i++){
		deliverNos +=checkedRows[i].deliverNo +",";
	}
	exportExcelBaseInfo('dataGridJG','/wmdeliver/doExportByDeliverNo?deliverNos='+deliverNos,'退厂配送单导出');
	
};



//保存
wmdeliver.saveDtlInfo = function(id){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = billomdeliver.endEdit(id);
	if(!tempFlag){
		alert('数据验证没有通过！',1);
		return;
	}
	var fromObj=$('#dataForm');
	var showWinObj = 'openUI';
	
	var tempObj = $('#'+id);
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动！',1);
		return;
	}
	//新增
	var inserted = tempObj.datagrid('getChanges', "inserted");
    //删除
	var deleted = tempObj.datagrid('getChanges', "deleted");
    //更新
	var updated = tempObj.datagrid('getChanges', "updated");
    
    
    var checkUrl=BasePath+'/wmdeliverdtl/get_count.json';
    var checkData={
    		"locno" : billomdeliver.locno,
    		"deliverNo" : $('#deliverNo').val(),
    		"ownerNo" : $('#ownerNo').combobox('getValue')
    };
    var hashObject = {};
    if(inserted.length>0){
        for (var i = 0; i < inserted.length; i++) {
        	var no = inserted[i]['recheckNo']+"_"+inserted[i]['containerNo']+"_"+inserted[i]['boxNo'];
        	if(hashObject[no]){
        		alert('复核单号_容器号_箱号：【'+no+'】重复！',1);
        		return;
        	}else{
        		hashObject[no] = true;
        	}
        	 //后台ajax校验查询
        	checkData.containerNo = inserted[i]['containerNo'];
        	checkData.boxNo = inserted[i]['boxNo'];
        	checkData.storeNo = inserted[i]['storeNo'];
            if(billomdeliver.checkExistFun(checkUrl,checkData)){
        		alert('复核单号_容器号_箱号：【'+no+'】已存在！',1);
        		return;
    	    }
        }
    }
	  var effectRow = {
		inserted:JSON.stringify(inserted),
		deleted:JSON.stringify(deleted),
		updated:JSON.stringify(updated),
		"locno" : billomdeliver.user.locno,
		"deliverNo" : $('#deliverNo').val(),
		"ownerNo" : $('#ownerNo').combobox('getValue'),
		"createtm" : billomdeliver.user.currentDate19Str,
		"creator" : billomdeliver.user.loginName,
		"edittm" : billomdeliver.user.currentDate19Str,
		"editor" : billomdeliver.user.loginName
	};
    $.post(BasePath+'/bill_om_deliver_dtl/saveDtlInfo', effectRow, function(result) {
        if(result.success){
            alert('装车单明细保存成功！',1);
            tempObj.datagrid('acceptChanges');
            billomdeliver.clearAll(fromObj);
            $('#'+showWinObj).window('close');  
            billomdeliver.searchData();
        }else{
        	alert('操作异常！',1);
        	return;
        }
    }, "JSON").error(function() {
    	alert('保存失败!',1);
    });
    
};
