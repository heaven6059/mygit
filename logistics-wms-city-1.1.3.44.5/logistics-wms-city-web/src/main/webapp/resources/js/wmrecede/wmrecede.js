/**
 * 退厂通知单
 * 
 */

var wmrecede = {};

wmrecede.locno;

wmrecede.curDialogId;
wmrecede.curRowIndex = -1;
wmrecede.preType = 0;

wmrecede.exportDataGridDtl1Id = 'moduleView';
wmrecede.preColNamesDtl1 = [
		                   {title:"商品编码",field:"itemNo",width:140, align:'left'},
		                   {title:"商品名称",field:"itemName",width:160, align:'left'},
		                   {title:"颜色",field:"colorNoStr",width:90, align:'left'},
		                   {title:"品牌",field:"brandNoStr",width:90, align:'left'},
                    ];
wmrecede.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
wmrecede.sizeTypeFiledName = 'sizeKind';

//批发退货
wmrecede.recedeType_11='11';
//批发召回
wmrecede.recedeType_12='12';
//自营退货
wmrecede.recedeType_13='13';
//自营召回
wmrecede.recedeType_14='14';

//商品类型-召回
wmrecede.defcell_itemType_6='6';
//储位品质-残品
wmrecede.defcell_areaQuality_A='A';


//将数组封装成一个map
wmrecede.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

wmrecede.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//加载查询
wmrecede.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/wmrecede/list.json?locno='+wmrecede.locno,'title':'退厂通知单列表','pageNumber':1 });
};
//加载明细
wmrecede.loadDataDetailGrid = function(rowData){
	var fromObj=$('#dataEditForm');
	var recedeType=$('input[id=recedeType]',fromObj).combobox("getValue");
	var sysNo=$('input[id=sysNo]',fromObj).combobox("getValue");
	var supplierNo=$('input[id=supplierNo]',fromObj).combobox("getValue");
	//储位品质
	var areaQuality;
	//储位商品类型
	var itemType;
	
	var url=BasePath+'/wmrecededtl/list.json?recedeNo='+rowData.recedeNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+'&sysNo='+sysNo+'&supplierNo='+supplierNo;
	if(recedeType==wmrecede.recedeType_12 || recedeType==wmrecede.recedeType_14){
		itemType=wmrecede.defcell_itemType_6;
		url=url+'&itemType='+itemType;
	}else if(recedeType==wmrecede.recedeType_11 || recedeType==wmrecede.recedeType_13){
		areaQuality=wmrecede.defcell_areaQuality_A;
		url=url+'&areaQuality='+areaQuality;
	}	
	
	$('#moduleEdit').datagrid(
			{
				'url':url,
				'pageNumber':1 
			}
	);
	//$("#moduleEdit").datagrid( 'load' );
};

wmrecede.loadDetail = function(locno){
	$('#dataForm').form('load',BasePath+'/wmrecede/get?locno='+locno);
	$("#locno").attr('readOnly',true);
};

wmrecede.checkExistFun = function(url,checkColumnJsonData){
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

//查询退厂通知单
wmrecede.searchWmRecede = function(){
	
	 var startCreatetm =  $('#createtmBeginCondition').datebox('getValue');
		var endCreatetm =  $('#createtmEndCondition').datebox('getValue');
		if(!isStartEndDate(startCreatetm,endCreatetm)){    
			alert("创建日期开始日期不能大于结束日期");   
	       return;   
	   }   
		
		var startAudittm =  $('#audittmBeginCondition').datebox('getValue');
		var endAudittm =  $('#audittmEndCondition').datebox('getValue');
		if(!isStartEndDate(startAudittm,endAudittm)){    
			alert("审核日期开始日期不能大于结束日期");   
	       return;   
	   }
		
		var startRecede =  $('#recedeDateBeginCondition').datebox('getValue');
		var endRecede =  $('#recedeDateEndCondition').datebox('getValue');
		if(!isStartEndDate(startRecede,endRecede)){    
			alert("退厂日期开始日期不能大于结束日期");   
	       return;   
	   } 
		
	$('#searchForm input[name=locno]').val(wmrecede.locno);
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/wmrecede/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清除查询条件
wmrecede.searchWmRecedeClear = function(){
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

//修改退厂通知单主表信息
wmrecede.modifyWmrecede = function(id){
    var fromObj=$('#dataEditForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    wms_city_common.loading("show","正在修改退厂通知单......");
    var url = BasePath+'/wmrecede/modifyWmrecede';
    fromObj.form('submit', {
   			url: url,
   			onSubmit: function(){
				
   			},
   			success: function(data){
   				if(data=="success"){
   					alert('修改成功!');
					wmrecede.loadDataGrid();
				}else{
					alert('修改异常!',2);
				}
   				wms_city_common.loading();
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   				wms_city_common.loading();
   			}
   	});
};

//弹出详情页面
wmrecede.showEdit = function(rowData,flag){
	
	//清空datagrid数据
	$('#moduleEdit').datagrid('clearData');
	
	var fromObj=$('#dataEditForm');
	
	//仓库编码设置为只读，不可修改
	$('input[name=recedeNo]',fromObj).attr("readOnly",true);

	$('#detailEditDiv').show();
	
//	if(flag == 1){
//		$('#toolDivForUpdate,#zoneInfoToolEditDiv').show();
		//$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
//		if(rowData.status!=null&&rowData.status!=""&&rowData.status!="11"){
//			$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').hide();
			//$('#moduleEdit').datagrid({toolbar: '#'});
//		}else{
//			$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').show();
			//$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
//		}
//	}else{
//		$('#toolDivForUpdate,#zoneInfoToolEditDiv').hide();
		//$('#moduleEdit').datagrid({toolbar: '#'});
//	}
	
//	//如果不是新建状态，则按钮都需要隐藏起来
//	if(rowData.status!=null&&rowData.status!=""&&rowData.status!="11"){
//		$('#moduleEdit').datagrid({toolbar: '#'});
//		$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').hide();
//	}else{//新建状态
//		$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').show();
//		$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
//	}
	
	//设置信息
	wmrecede.setDetail(rowData,fromObj);
	//明细信息
	wmrecede.loadDataDetailGrid(rowData);
	
	//弹窗
	$("#showEditDialog").window('open'); 
};

//弹出修改页面
wmrecede.showModify = function(){
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
		wmrecede.showEdit(checkedRows[0],1);
	});
	
};
//隐藏显示按钮
wmrecede.showHideBtn = function(type){
	
};

//弹出新增页面
wmrecede.showAdd=function(){
	var fromObj=$('#dataForm');
	
	wmrecede.clearAll(fromObj);
	
	$('#recedeNo').val("系统自动生成");
	
	$("#detailDiv").hide();
	$("#btn-add").show();
	
	$('input[id=ownerNo]',fromObj).combobox('enable');
	$('input[id=ownerNoHide]',fromObj).attr("disabled",true);
	
	$('input[id=supplierNo]',fromObj).combobox('enable');
	$('input[id=supplierNoHide]',fromObj).attr("disabled",true);
	
	$('input[id=sysNo]',fromObj).combobox('enable');
	$('input[id=sysNoHide]',fromObj).attr("disabled",true);
	
	$('#showDialog').window('open');  
};

//审核操作
wmrecede.auditWmrecede = function(){
	
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	var noStrs = [];
	var delTip = "";
	$.each(checkedRows, function(index, item){
		noStrs.push(item.ownerNo+"#"+item.recedeNo);
		if(item.status!=null&&item.status!="10"){
			delTip = delTip + "[" + item.recedeNo + "]";
		}
	}); 
	if(delTip!=null && delTip!=""){
		delTip = "单据:" + delTip + " 已审核，不能再次操作！";
		alert(delTip,1);
		return;
	}
	var data={"noStrs":noStrs.join(",")};
	$.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){ 
        if (r) {
        	//4. 审核
		    url = BasePath+'/wmrecede/auditWmrecede';
		    wms_city_common.loading("show");
		    wmrecede.ajaxRequest(url,data,function(result,returnMsg){
		    	wms_city_common.loading(); 
		    	if(result.flag=='true'){
        			 //4.审核成功,重新加载数据
        			 alert('审核成功!');
        			 wmrecede.loadDataGrid();
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
wmrecede.saveMain=function(){
	var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return;
     }
     var curDate = wms_city_common.getCurDate();
     var recedeDate = $("#recedeDate").datebox('getValue');
     if(!wms_city_common.isStartLessThanEndDate(curDate,recedeDate)){
      	alert("退厂日期不能小于当前日期!",1);
      	return;
      }
//     //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
//     var checkUrl=BasePath+'/wmrecede/get_count.json';
//     
//     var recedeNoVal = $("input[id=recedeNo]",fromObj).val();
//     var checkDataNo = {"recedeNo":recedeNoVal};
//     if(wmrecede.checkExistFun(checkUrl,checkDataNo)){
//    	  alert('退厂通知单号已存在,不能重复！',1);
//    	  $("input[id=recedeNo]",fromObj).focus();
//    	  return;
//     }
     
     //3. 保存
     wms_city_common.loading("show","正在生成退厂通知单......");
     var url = BasePath+'/wmrecede/addWmRecede';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
				
			},
			success: function(data){
				
				var dataObj = eval('(' + data + ')'); 
				
				if(dataObj.returnMsg){
					
					 alert('新增成功!');
				     
				     var recedeNoVal = dataObj.recedeNo;
				     var locnoVal = dataObj.locno;
				     var ownerNoVal = $("input[id=ownerNo]",fromObj).combobox('getValue');
				     var getDataNo = {"recedeNo":recedeNoVal,"ownerNo":ownerNoVal,"locno":locnoVal};
					 
					 //获取信息
					 $.ajax({
							async : true,
							cache : false,
							type : 'POST',
							dataType : "json",
							data: getDataNo,
							url:BasePath+'/wmrecede/get_biz',
							success : function(data) {
								
								wmrecede.clearAll(fromObj);
								
								wmrecede.setDetail(data[0],fromObj);
								$('input[id=recedeNo]',fromObj).attr('readOnly',true);
								
								$('#module').datagrid('clearData');
								
								$("#detailDiv").show();
								$("#btn-add").hide();
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

//获取明细信息
wmrecede.setDetail = function(rowData,fromObj){
	
	$("input[id=recedeNo]",fromObj).val(rowData.recedeNo);
	
	$("input[id=recedeType]",fromObj).combobox("select",rowData.recedeType);
	
	$("input[id=recedeDate]",fromObj).datebox("setValue",rowData.recedeDate);
	
	$("input[id=sysNo]",fromObj).combobox("select",rowData.sysNo);
	$("input[id=sysNo]",fromObj).combobox('disable');
	$("input[id=sysNoHide]",fromObj).attr("disabled",false);
	$("input[id=sysNoHide]",fromObj).val(rowData.sysNo);
	
	$("input[id=supplierNo]",fromObj).combobox("select",rowData.supplierNo);
	$("input[id=supplierNo]",fromObj).combobox('disable');
	$("input[id=supplierNoHide]",fromObj).attr("disabled",false);
	$("input[id=supplierNoHide]",fromObj).val(rowData.supplierNo);
	
	$("input[id=ownerNo]",fromObj).combobox("select",rowData.ownerNo);
	$("input[id=ownerNo]",fromObj).combobox('disable');
	$("input[id=ownerNoHide]",fromObj).attr("disabled",false);
	$("input[id=ownerNoHide]",fromObj).val(rowData.ownerNo);
	
	//$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
	
	$("input[id=creator]",fromObj).val(rowData.creator);
	$("input[id=createtm]",fromObj).val(rowData.createtm);
	$("input[id=editor]",fromObj).val(rowData.editor);
	$("input[id=edittm]",fromObj).val(rowData.edittm);
	$("input[id=memo]",fromObj).val(rowData.memo);
	
};

//删除
wmrecede.deleteWmrecede=function(){
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
        		noStrs.push(item.recedeNo+"#"+item.locno+"#"+item.ownerNo);
        		if(item.status!=null&&item.status!=""&&item.status!="10"){
        			delTip = "单据:"+item.recedeNo+"；";
        		}
        	}); 
        	if(delTip!=null && delTip!=""){
        		delTip = delTip+"当前状态不允许删除！";
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
		    url = BasePath+'/wmrecede/deleteWmrecede';
		    wms_city_common.loading("show");
		    wmrecede.ajaxRequest(url,data,function(result,returnMsg){
		    	wms_city_common.loading();
		    	if(result=='success'){
        			 //4.删除成功,清空表单
        			 wmrecede.loadDataGrid();
        			 alert('删除成功!');
        		}else{
        			alert('删除失败,请联系管理员!',2);
        		}
        	}); 
        }
	});     
};

//加载供应商
wms_city_common.init_Supplier_Text = {};
wmrecede.loadSupplier = function(formObj){
	wms_city_common.comboboxLoadFilter(
			["supplierNoCondition"],
			"supplierNo",
			"supplierName",
			"supplierName",
			false,
			[true],
			BasePath+'/supplier/get_all',
			{},
			wms_city_common.init_Supplier_Text,
			null);
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/supplier/get_all',
		success : function(data) {
			$('input[id=supplierNo]',formObj).combobox({
			    data:data,
			    valueField:'supplierNo',    
			    textField:'textFieldName',
			    panelHeight:150,
			    onSelect:function(data){
			    },
			    loadFilter:function(data){
			       	 if(data.length>0){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.textFieldName = tempData.supplierNo+'→'+tempData.supplierName;
			       		 }
			       	 }
			    		 return data;
			    }
			});
			if(null!=data){
				$('input[id=supplierNo]',formObj).combobox("select",data[0].supplierNo);  
			}
		}
	});
};

//加载品牌库
wmrecede.loadSysNo = function(formObj){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			$('input[id=sysNo]',formObj).combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150,
			    onSelect:function(returnData){
			    	//$("input[id=sysNoName]",formObj).val(returnData.itemname);
			    }
			});
			if(null!=data){
				$('input[id=sysNo]',formObj).combobox("select",data[0].itemvalue);
			}
		}
	});
};
//格式化退厂类型
wmrecede.columnRECEDE_TYPEFormatter = function(value, rowData, rowIndex){
	return wmrecede.init_RECEDE_TYPEText[value];
};

//退厂类型
wmrecede.init_RECEDE_TYPEText = {};
wmrecede.init_RECEDE_TYPE = function(formObj){
	wms_city_common.comboboxLoadFilter(
			["recedeTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECEDE_TYPE',
			{},
			wmrecede.init_RECEDE_TYPEText,
			null);
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECEDE_TYPE',
		success : function(data) {
			$('input[id=recedeType]',formObj).combobox({
			     valueField:"itemvalue",
			     textField:"itemnamedetail",
			     data:data,
			     panelHeight:150
			  });
			
		}
	});
};

//加载委托业主信息
wms_city_common.init_Owner_Text = {};
wmrecede.loadOwner = function(formObj){
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
			    textField:'textFieldName',
			    panelHeight:150,
			    loadFilter:function(data){
			       	 if(data.length>0){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.textFieldName = tempData.ownerNo+'→'+tempData.ownerName;
			       		 }
			       	 }
			    		 return data;
			    }
			});
			if(null!=data){
				$('input[id=ownerNo]',formObj).combobox("select",data[0].ownerNo); 
			}
		}
	});
};

//加载仓库信息
wmrecede.loadLoc = function(formObj){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			wmrecede.locno = data.locno;
		}
	});
};

//清理商品选择的form
wmrecede.itemClear = function(){
	$('#itemNo').val('');
	$('#itemName').val('');
	$('#barcode').val('');
};

//清理form
wmrecede.clearAll = function(fromObj){
	fromObj.form("clear");
//	$("input[id=recedeNo]",fromObj).attr('readOnly',false);
};

//清理form和不为空的提示
wmrecede.clearFormAndTip = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};

//新增和修改页面的取消按钮
wmrecede.closeShowWin = function(id){
	$('#'+id).window('close');
	//$('#locno,#locname').validatebox('reset');
	//$('#showDialog').window('destroy');
};


//格式化状态
wmrecede.columnStatusFormatter = function(value, rowData, rowIndex){
	return wmrecede.init_status_Text[value];
};

//状态
wmrecede.init_status_Text = {};
wmrecede.init_status = function(){
	wms_city_common.comboboxLoadFilter(
			["status","statusCondition"],
			null,
			null,
			null,
			true,
			[false,true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECEDE_STATUS',
			{},
			wmrecede.init_status_Text,
			null);
};


//弹出选择商品的页面
wmrecede.addItemDetail_module = function(gid){
	$("#dataGridJGItem").datagrid('clearData');
	
	wmrecede.itemClear();
	
	$('#showGirdName').val(gid);
	
	var fromObj=$('#dataForm');
	if(gid == 'moduleEdit'){
		fromObj=$('#dataEditForm');
	}
	//alert($('input[id=supplierNoHide]',fromObj).val());
	//$("#supplierNoSearItem").val($('input[id=supplierNoHide]',fromObj).val());
	
	var recedeType=$('input[id=recedeType]',fromObj).combobox("getValue")
	//储位品质
	var areaQuality;
	//储位商品类型
	var itemType;
	
	if(recedeType==wmrecede.recedeType_12 || recedeType==wmrecede.recedeType_14){
		itemType=wmrecede.defcell_itemType_6;
	}else if(recedeType==wmrecede.recedeType_11 || recedeType==wmrecede.recedeType_13){
		areaQuality=wmrecede.defcell_areaQuality_A;
	}
	
	$('#supplierNoForItem').val($('input[id=supplierNoHide]',fromObj).val());
	$('#sysNoForItem').val($('input[id=sysNoHide]',fromObj).val());
	$('#areaQualityForItem').val(areaQuality);
	$('#itemTypeForItem').val(itemType);
	$('#recedeTypeForItem').val(recedeType);
	
	$('#openUIItem').show().window('open');
};

//查询商品信息
wmrecede.searchItem = function(){
	var queryMxURL;
	var recedeType=$('#recedeTypeForItem').val();
	
	var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	if('12'==recedeType || '14'==recedeType){
		queryMxURL=BasePath+'/wmrecededtl/findItemAndSizeTest';//体验后删除  不控制库存  已这个为主
	}else{
		queryMxURL=BasePath+'/wmrecededtl/findItemAndSize'; //控制库存
	}
	
	
	var params = eval("(" +fromObjStr+ ")");
	params['locno'] = wmrecede.locno;
	
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams = params;
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
    
};

//确定选择的商品
wmrecede.confirmItem = function(){
	var checkedRows = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的商品!',1);
		return;
	}
	
//    var noStrs = [];
//    var delTip = "";
    
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条商品吗？", function (r){  
        if (r) {
            $.each(checkedRows, function(index, item){
        	    var  rowData = {
        	    		"itemNo":item.itemNo,
        	    		"itemName":item.itemName,
        	    		"colorNoStr":item.colorNoStr,
        	    		"sizeNo":item.sizeNo,
        	    		"brandNoStr":item.brandNoStr,
        	    		"packQty":item.qminOperatePacking,
        	    		"recedeQty":item.conContentQty,
        	    		"conContentQty":item.conContentQty
        	    };
        		//把选择的商品编码行记录插入到父窗体中
        	    wmrecede.insertRowAtEnd($('#showGirdName').val(),rowData);
        	    
        	    
        	
//            	noStrs.push(item.recedeNo+"#"+item.locno+"#"+item.ownerNo+"#"+item.expType);
//            	if(item.status!=null&&item.status!=""&&item.status!="11"){
//            			delTip = "单据:"+item.recedeNo+"；";
//            	}
            }); 
            $('#openUIItem').window('close').hide();
        	
        }
	});
    

};

//新插入一行明细
wmrecede.insertRow_module = function(gid){
//	var fromObj=$('#dataForm');
//	if(gid == 'moduleEdit'){
//		fromObj=$('#dataEditForm');
//	}
	
	wmrecede.insertRowAtEnd(gid);
	
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
//			   "recedeNoIsNull":"1"
//		},
//		success: function(jqData){
//			$(ed.target).combobox('loadData',jqData); 
//		}
//	});
	
};

wmrecede.insertRowAtEnd = function(gid,rowData){
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

wmrecede.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};

wmrecede.endEdit = function(gid){
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


////验证编辑行
//wmrecede.endEdit = function(gid){
//	var tempObj = $('#'+gid);
//	var rowArr = tempObj.datagrid('getRows');
//	
//	
//    for (var i = 0; i < rowArr.length; i++) {
//    	if(tempObj.datagrid('validateRow', i)){
//    		var originQty = tempObj.datagrid('getEditor', {index:i,field:'originQty'});
//    		if(originQty!=null){
//    			var itemNo = rowArr[i].itemNo;
//    			var conContentQty = rowArr[i].conContentQty;
//            	var originQty = tempObj.datagrid('getEditor', {index:i,field:'originQty'});
//            	if(originQty!=null){
//            		if(originQty.target.val() > conContentQty){
//                		$(originQty.target).focus();
//                		return "商品"+itemNo+":计划数量不能大于库存数量;";
//                	}else{
//                		tempObj.datagrid('endEdit', i);
//                	}
//            	}
//    		}
//    	}else{
//    		return '数据验证没有通过!';
//    	}
//    }
//    return "";
//};

//保存明细
wmrecede.save = function(id){
	//正在编辑状态的那一行的
	var isOk = true;
	var tempObj = $('#'+id);
	var rows = tempObj.datagrid("getRows");
	
	wmrecede.endEdit(id);
	
	$.each(rows,function(index,item){
		//var ed = tempObj.datagrid('getEditor', {index:index,field:'recedeQty'});
		var value = item.recedeQty;
		var conContentQty = item.conContentQty;
		if(value==""||value=="0"){
			alert("退厂数量不能为空或0");
			$(ed.target).focus();
			isOk = false;
			return false;
		}
		
		//验证是否大于0
		if(value < 0){
			alert("商品"+item.itemNo+"尺码"+item.sizeNo+"退厂数量不能小于0!");
			$(ed.target).focus();
			isOk = false;
			return false;
		}
	});
	if(!isOk){
		return ;
	}
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	var tempFlag = wmrecede.endEdit(id);
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
	
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动！',1);
		return;
	}
	
	var inserted = tempObj.datagrid('getChanges', "inserted");
    if(inserted.length>0){
        var checkUrl=BasePath+'/wmrecededtl/get_count.json';
        var checkData={
        		"recedeNo" : $('input[id=recedeNo]',fromObj).val(),
        		"locno" : wmrecede.locno,
        		"ownerNo" : $('input[id=ownerNoHide]',fromObj).val()
        };
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
        	//后台ajax校验查询
         	checkData.itemNo = inserted[i]['itemNo'];
         	checkData.sizeNo = inserted[i]['sizeNo'];
             if(wmrecede.checkExistFun(checkUrl,checkData)){
         		alert('商品编码_尺码：【'+noAndSize+'】已存在！',1);
         		return;
     	    }
        }
    }
    var deleted = tempObj.datagrid('getChanges', "deleted");
    var updated = tempObj.datagrid('getChanges', "updated");
    var effectRow = {
    		inserted:JSON.stringify(inserted),
    		deleted:JSON.stringify(deleted),
    		updated:JSON.stringify(updated),
    		"ownerNo":$("input[id=ownerNo]",fromObj).combobox('getValue'),
			"locno": wmrecede.locno,
			"recedeNo":$("input[id=recedeNo]",fromObj).val()
    };
    wms_city_common.loading("show","正在保存明细......");
    $.post(BasePath+'/wmrecededtl/save', effectRow, function(result) {
        if(result.success){
            alert('保存成功!',1);
            tempObj.datagrid('acceptChanges');
            wmrecede.clearAll(fromObj);
            $('#'+showWinObj).window('close');  
            wmrecede.loadDataGrid();
        }else{
        	alert('操作异常！',1);
        	return;
        }
        wms_city_common.loading();
    }, "JSON").error(function() {
    	alert('保存失败!',1);
    	wms_city_common.loading();
    });
};

/**
 * 数组是否重复，如重复则返回重复元素 
 * @return {}
 */
wmrecede.isDouble = function() {
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

wmrecede.initBrand = function(){
	wms_city_common.loadBrands('brandNoIt', true);
};

wmrecede.converBrandJsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].brandNo +"\":\""+data[i].brandName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};


$(document).ready(function(){
	$("#createtmBeginCondition").datebox('setValue',getDateStr(-2));
	$('#showDialog,#showEditDialog,#openUIItem,#showViewDialog').show();
	
	wmrecede.loadLoc();
	wmrecede.loadOwner();
	wmrecede.loadSupplier();//加载供应商
	wmrecede.initBrand();
	wmrecede.init_RECEDE_TYPE();//退厂类型
	wmrecede.init_status();//初始化状态
	
//	wmrecede.loadSysNo();//加载品牌库
//	wms_city_common.loadSysNo('#sysNoSearch');
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNo]',$('#dataForm'))},//新增
			{"sysNoObj":$('input[id=sysNo]',$('#dataEditForm'))},//修改
			{"sysNoObj":$('input[id=sysNo]',$('#dataViewForm'))},//详情
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	wms_city_common.loading("xx");
});

//导出
exportExcel=function(){
	var recedeNos="";
	var checkedRows = $('#dataGridJG').datagrid("getChecked");
	for(var i=0;i<checkedRows.length;i++){
		recedeNos +=checkedRows[i].recedeNo +",";
	}
	exportExcelBaseInfo('dataGridJG','/wmrecede/doExportByRecedeNo?recedeNos='+recedeNos,'退厂通知单导出');
};


/**
 * ============================================尺码横排===================================================
 */

//弹出详情页面
wmrecede.showView = function(rowData,rowIndex){
	//清空datagrid数据
	$('#moduleView').datagrid('clearData');
	
	var fromObj=$('#dataViewForm');
	//仓库编码设置为只读，不可修改
	$('input[name=recedeNo]',fromObj).attr("readOnly",true);

	$('#detailViewDiv').show();
	
	//设置信息
	wmrecede.setDetail(rowData,fromObj);
	//明细信息
	wmrecede.loadDetail(rowData,rowIndex);
	//弹窗
	$("#showViewDialog").window('open');
};


wmrecede.loadDetail = function(rowData,rowIndex){
	
	wmrecede.curRowIndex=rowIndex;
	
//	wmrecede.initGridHead(rowData.sysNo);
//	
//	wmrecede.loadDataDetailViewGrid(rowData);
	
	wmrecede.initGridHeadAndData(rowData);
   
};

//同时加载表头和表体
wmrecede.initGridHeadAndData = function(rowData){
	
	 var beforeColArr = wmrecede.preColNamesDtl1;
	 var afterColArr = wmrecede.endColNamesDtl1; 
	 var columnInfo = wmrecede.getColumnInfo(rowData.sysNo,beforeColArr,afterColArr);
	 
	 var url = BasePath+'/wmrecededtl/queryBillWmRecedeDtlDTOlList';
	 var queryParams = {recedeNo:rowData.recedeNo,locno:rowData.locno,ownerNo:rowData.ownerNo};
	 
	 $("#moduleView").datagrid({
		 queryParams:queryParams,
		 url:url,
         columns:columnInfo.columnArr
     }); 
};

wmrecede.initGridHead = function(sysNo){
    var beforeColArr = wmrecede.preColNamesDtl1;
	 var afterColArr = wmrecede.endColNamesDtl1; 
	 var columnInfo = wmrecede.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+wmrecede.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

wmrecede.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:wmrecede.sizeTypeFiledName
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

//加载明细
wmrecede.loadDataDetailViewGrid = function(rowData){
	$('#moduleView').datagrid(
			{
				'url':BasePath+'/wmrecededtl/queryBillWmRecedeDtlDTOlList?recedeNo='+rowData.recedeNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo,
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

//导入   2014-02-21  luo.hl
wmrecede.showImport = function(formId,dalogId){
	wmrecede.curDialogId = dalogId;
	var fromObj = $("#"+formId);
	var recedeNo = $('input[id=recedeNo]',fromObj).val();
	var ownerNo = $('input[id=ownerNoHide]',fromObj).val();
	$("#iframe").attr("src","../wmrecededtl/iframe?recedeNo="+recedeNo+"&ownerNo="+ownerNo+"&"+new Date());
	$("#showImportDialog").window('open'); 
}

function importSuccess(){
	wmrecede.closeShowWin(wmrecede.curDialogId);
	wmrecede.closeShowWin("showImportDialog");
	alert("导入成功!");
}

function loading(type,msg){
	wms_city_common.loading(type,msg);
}

wmrecede.downloadTemp = function (){
	window.open("../wmrecededtl/downloadTemple");
}
