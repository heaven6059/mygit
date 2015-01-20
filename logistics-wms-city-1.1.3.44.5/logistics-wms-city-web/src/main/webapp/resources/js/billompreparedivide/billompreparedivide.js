var billompreparedivide = {};
billompreparedivide.locno;
billompreparedivide.user = {};
billompreparedivide.currentDate;

//状态
billompreparedivide.status = {};
//委托业主
billompreparedivide.ownnerData={};


$(document).ready(function(){
	//初始化新增明细页面单据来源
	billompreparedivide.initBillSource();
	//初始化业主
	billompreparedivide.loadOwner();
	//初始化仓库
	billompreparedivide.loadLoc();
	//加载列表数据
	billompreparedivide.initstatus();
	billompreparedivide.initBrand();
	billompreparedivide.initUsers();
	
	//加载品牌
	var objs1 = [];
	objs1.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=search_brandNo]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs1);
	
	var objs2 = [];
	objs2.push({
		"sysNoObj" : $('#sysNoBox'),
		"brandNoObj" : $('#brandNoBox')
	});
	wms_city_common.loadSysNo4Cascade(objs2);
	
	wms_city_common.loadBrands('brandNoExp', true);
	
	billompreparedivide.expStatus();
	
	//初始化商品类别
	wms_city_common.comboboxLoadFilter(
			["cateCodeBox"],
			'cateCode',
			'cateName',
			'valueAndText',
			false,
			[true],
			BasePath+'/category/getCategory4Simple?cateLevelid=1',
			{},
			null,
			null);
	
	$('#dataGridJG_detail_view').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billompreparedivide.receiptQty = data.footer[1].receiptQty;
		   				billompreparedivide.checkQty = data.footer[1].checkQty;
		   				billompreparedivide.divideQty = data.footer[1].divideQty;
		   			}else{
		   				var rows = $('#dataGridJG_detail_view').datagrid('getFooterRows');
			   			rows[1]['receiptQty'] = billompreparedivide.receiptQty;
			   			rows[1]['checkQty'] = billompreparedivide.checkQty;
			   			rows[1]['divideQty'] = billompreparedivide.divideQty;
			   			$('#dataGridJG_detail_view').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
		
			billompreparedivide.wareArea(
			'wareNoCondition',
			'areaNoCondition',
			true
		);	
			//初始化三级大类
		wms_city_common.cateForMultipleCascade(
				'cateOneCondition',
				'cateTwoCondition',
				'cateThreeCondition',
				true
	    );
		//将创建日期查询条件设置为两天前
		$('#createtmStart').datebox('setValue',getDateStr(-2));
});

/***********************************************初始化******************************************************/

billompreparedivide.initUsers = function(){
	wms_city_common.comboboxLoadFilter(
			["search_creator","search_auditor"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true,true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};


//初始化状态下拉框
billompreparedivide.initstatus = function(){
	wms_city_common.comboboxLoadFilter(
			["search_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PREPAREDIVIDE_STATUS',
			{},
			billompreparedivide.status,
			null);
};
//初始化新增明细页面单据来源
billompreparedivide.initBillSource = function(){
	var data=[{    
	    "text":"入库验收", 
	    "value":"1"   
	},{    
	    "text":"退仓验收", 
	    "value":"2"   
	}];;
	$('#billSourceBox').combobox({
	     valueField:"value",
	     textField:"text",
	     data:data,
	     onSelect : billompreparedivide.selectBillSourceBox
	     
	  });
};

//选择单据来源
billompreparedivide.selectBillSourceBox = function(record) {
	var adviceNoBoxLabel=$('#adviceNoBoxLabel');
	if(record.value=='1'){
		adviceNoBoxLabel.text("预到货通知单：");
	}else if(record.value=='2'){
		adviceNoBoxLabel.text("店退仓单：");		
	}
};

//加载仓库信息
billompreparedivide.loadLoc = function(){
		$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billompreparedivide.locno = data.locno;
			billompreparedivide.currentDate = data.currentDate10Str;
		}
	});
};
//加载委托业主信息
billompreparedivide.loadOwner = function(){
	wms_city_common.comboboxLoadFilter(
			["ownerNo","ownerNo_view","search_ownerNo"],
			'ownerNo',
			'ownerName',
			'ownerName',
			false,
			[false,false,true],
			BasePath+'/entrust_owner/get_biz',
			{},
			billompreparedivide.ownnerData,
			null);
};


//加载品牌
billompreparedivide.initBrand = function(){
	$('#brandNo').combobox({    
		 valueField:'id',    
		 textField:'text',
		 panelHeight:150,
		 url:BasePath+'/brand/queryBrandTree'
	}); 
};

/***********************************************初始化end******************************************************/

//加载Grid数据Utils
billompreparedivide.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

/***********************************************新增******************************************************/
//弹出新增窗口
billompreparedivide.addInfo = function(){
	 //清空所有数据
	 $("#detailForm").form("clear");
	 //仓库，委托业主初始化
	 $("#ownerNo").combobox("select",$("#ownerNo").combobox("getData")[0].ownerNo);
	 //清空下拉列表
	 $('#dataGridJG_detail').datagrid('loadData',[]);
	 billompreparedivide.showReceiptNo("1");
	 
	$("#ownerNo").combobox("enable");
	$("#ownerNoHide").attr("disabled",true);

	$("#main_btn").show();
	$("#sub_con").hide();
	 $('#detailDialog').window({
		title:"新增",
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	 });
	 $("#addBtn").show();
	 $("#editBtn").hide();
	 $("#detailDialog").window('open'); 
};

//新增主表信息
billompreparedivide.save_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    wms_city_common.loading("show");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_om_preparedivide/addMain',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert('新增成功!');
				billompreparedivide.showSub(data.data);
				 $("#addBtn").hide();
	 			 $("#editBtn").show();
				billompreparedivide.searchArea();
			}else{
				alert('新增失败,请联系管理员!',2);
			}
		}
	});
};
//修改主表信息
billompreparedivide.edit_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    wms_city_common.loading("show");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_om_preparedivide/editMain',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert('修改成功!');
				billompreparedivide.showSub(data.data);
				$("#addBtn").hide();
	 			$("#editBtn").show();
				billompreparedivide.searchArea();
			}else{
				alert(data.msg,2);
				//alert('修改失败,请联系管理员!',2);
			}
		}
	});
};

billompreparedivide.showSub = function(data){
	//设置主表信息
	$("#receiptNo").val(data.receiptNo);
	$("#receiptNoHide").val(data.receiptNo);
	//仓库
	$("#locNo").combobox("disable");
	//委托业主
	$("#ownerNo").combobox("disable");
	$("#ownerNoHide").attr("disabled",false);
	$("#ownerNoHide").val(data.ownerNo);
	$("#main_btn").hide();
	$("#sub_con").show();
	$("#opBtn").show();
};

/***********************************************新增end******************************************************/


/***********************************************删除******************************************************/
billompreparedivide.del = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keyStr = [];
	var ownerNo ;
	var locNo;
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!="10"){
			allOk = false;
			return false;
		}
		keyStr.push(item.receiptNo);
		if(index==0){
			ownerNo = item.ownerNo;
			locNo = item.locno;
		}
	});   
	if(!allOk){
		alert("非建单状态的数据不能删除",1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
            var url = BasePath+'/bill_om_preparedivide/deleteReceipt';
        	var data={
        	    "keyStr":keyStr.join(","),
        	    "ownerNo":ownerNo,
        	    "locNo":locNo
        	};
        	wms_city_common.loading("show","正在删除......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					 wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('删除成功!');
						 billompreparedivide.loadDataGrid();
        		 	}else{
        			 	alert(data.msg,2);
        		 	}
				  },
		   		error:function(){
		   				wms_city_common.loading();
		   				alert('删除失败,请联系管理员!',2);
		   			}
			});
        }  
    });  
};

/***********************************************删除end******************************************************/

/***********************************************审核***********************************************************/
billompreparedivide.audit=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	
	var keyStr = [];
	var ownerNo ;
	var locNo;
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!="10"){
			allOk = false;
			return false;
		}
		keyStr.push(item.receiptNo);
		if(index==0){
			ownerNo = item.ownerNo;
			locNo = item.locno;
		}
	});  
	if(!allOk){
		alert("非建单状态的数据不能审核",1);
		return;
	}             
    var url = BasePath+'/bill_om_preparedivide/auditMain';
	var data={
	    "keyStr":keyStr.join(","),
	    "ownerNo":ownerNo,
	    "locNo":locNo
	};
	
	$.messager.confirm("确认","你确定要审核这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	wms_city_common.loading("show","正在审核......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					 wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('审核成功!');
						 billompreparedivide.loadDataGrid();
        		 	}else{
        			 	alert(data.msg,2);
        		 	}
				  },
		   		error:function(){
		   				wms_city_common.loading();
		   				alert('审核失败,请联系管理员!',2);
		   			}
			});
        }  
    });
};
billompreparedivide.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billompreparedivide.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//加载数据
billompreparedivide.loadDataGrid = function(){
	
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_om_preparedivide/findMainRecipt?locno='+billompreparedivide.locno,
    			'pageNumber':1
    });        
};

billompreparedivide.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status!="10"){
		alert("非建单状态的数据不能修改");
		return;
	}else{
		billompreparedivide.instockDetail(checkedRows[0],"edit");
	}
};

//单击一行查看收货明细
billompreparedivide.instockDetail = function(rowData,type){
	billompreparedivide.showReceiptNo("2");
	$('#detailDialog').window({
		title:"预分货单明细"
	 });
	$("#detailDialog").window('open'); 
	$("#detailForm").form('load',rowData);
	$("#locNo").combobox("disable");
	$("#locNoHide").attr("disabled",false);
	
	$("#ownerNo").combobox("disable");
	$("#ownerNoHide").attr("disabled",false);	
	
	$("#main_btn").hide();
	if(type=="edit"&&rowData.status=="10"){
		$("#opBtn").show();
		$("#addBtn").hide();
		$("#editBtn").show();
	}else{
		$("#opBtn").hide();
	}
	$("#sub_con").show();
	var queryMxURL=BasePath+'/bill_om_preparedivide_dtl/findPrepareDivideDetail?receiptNo='+rowData.receiptNo+'&locno='+rowData.locno;
	$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail").datagrid( 'load' );
};

//选择发货通知单
billompreparedivide.selectExpNo = function(){
	$('#searchExpNoForm').form('clear');
	billompreparedivide.searchExp();
	$("#showExpDialog").window('open');
	
};

//查询发货通知单列表
billompreparedivide.searchExp = function(){
		var startCreatetm =  $('#startCreatetmExp').datebox('getValue');
		var endCreatetm =  $('#endCreatetmExp').datebox('getValue');
		if(!isStartEndDate(startCreatetm,endCreatetm)){    
			alert("创建日期开始日期不能大于结束日期");   
	        return;   
	    }
		
		var queryMxURL=BasePath+'/billomexp/list_divide_pre';
		var reqParam ={};
		reqParam['locno'] = billompreparedivide.locno;
		reqParam['expNo'] = $("#expNo").val();
		reqParam['status'] = $('#statusExp').combobox('getValue');
		reqParam['brandNo'] = $('#brandNoExp').combobox('getValue');
		reqParam['startCreatetmExp'] = $('#startCreatetmExp').val();
		reqParam['endCreatetmExp'] = $('#endCreatetmExp').val();
		
		billompreparedivide.loadGridDataUtil('expNoDataGrid', queryMxURL, reqParam);
	
};

//选择发货通知单添加到父窗口中
billompreparedivide.selectBillOmExp = function(){
	var checkedRows = $('#expNoDataGrid').datagrid('getChecked');
	if(checkedRows.length == 0){
		alert("请先选择发货通知单!");
		return;
	}
	
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条发货通知单吗？", function (r){  
		if (r) {
			wms_city_common.loading("show");
			$('#mainDataGrid3').datagrid('loadData', { total: 0, rows: [] });
			$.each(checkedRows, function(index, item){
				var  rowData = {
						"locno":billompreparedivide.locno,
						"ownerNo":item.ownerNo,
						"expNo":item.expNo
				};
				//把选择的商品编码行记录插入到父窗体中
				billompreparedivide.insertRowAtEnd("mainDataGrid3",rowData);
			});
			wms_city_common.loading();
		}
	});
	//关闭窗口
    billompreparedivide.closeWindow('showExpDialog');
};

billompreparedivide.insertRowAtEnd = function(gid,rowData){
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

//关闭弹出窗
billompreparedivide.closeWindow = function(id){
	$('#'+id).window('close');  
};

//清空
billompreparedivide.searchClear = function(id){
	$('#'+id).form("clear");
};


billompreparedivide.expStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusExp"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OEM_STATUS',
			{},
			null,
			null);
};

/***********************************************格式化******************************************************/
//状态
billompreparedivide.statusFormatter = function(value, rowData, rowIndex){
	return billompreparedivide.status[value];
};
//委托业主
billompreparedivide.ownerFormatter = function(value, rowData, rowIndex){
	return billompreparedivide.ownnerData[value];
};


/***********************************************格式化******************************************************/


//查询区域信息
billompreparedivide.searchArea = function(){
	var audittmStart = $('#audittmStart').datebox('getValue');
	var audittmEnd = $('#audittmEnd').datebox('getValue');
	if(!isStartEndDate(audittmStart,audittmEnd)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    }
    
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_preparedivide/findMainRecipt?locno='+billompreparedivide.locno;
	
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
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

billompreparedivide.searchLocClear = function(){
	$('#searchForm').form("clear");
	$('#search_brandNo').combobox("loadData",[]);
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
} 


//新增箱号，弹出选择箱号对话框
billompreparedivide.showBoxDialog = function(){	
	//清除列表信息
	$('#dataGridJG_Box').datagrid('loadData',[]);
	
	//清空datagrid数据
	$('#mainDataGrid3').datagrid('clearData');

	billompreparedivide.clearSearchBox("init");
	$("#boxDialog").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$("#boxDialog").window('open');
	
	
};

//删除一行
billompreparedivide.deleterow = function(){
	var obj = $('#dataGridJG_detail');
	var row = obj.datagrid('getSelected');
	if(row==null){
		return;
	}
	var rows = obj.datagrid('getRows');
	var boxNo=row.boxNo;
	var delList=[];
	
	$.each(rows,function(index,item){
		if(boxNo==item.boxNo){
			delList.push(item);
		}
	});
	$.each(delList,function(index,item){
		var i = obj.datagrid('getRowIndex', item);
		$('#dataGridJG_detail').datagrid('deleteRow',i);
	});
	
};

//关闭详情框
billompreparedivide.coloseDetailDialog = function(){
	$('#detailDialog').window('close');
};

billompreparedivide.coloseDialog = function(id){
	$('#'+id).window('close');
};
//保存明细
billompreparedivide.edit = function(){	
	var tempObj = $('#dataGridJG_detail');

	var inserted = tempObj.datagrid('getChanges', "inserted");
	var deleted = tempObj.datagrid('getChanges', "deleted");
	var locno=billompreparedivide.locno;
	
    var fromObj=$('#detailForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   var repeatArry =  billompreparedivide.getRepeat(tempObj.datagrid('getRows'));
   if(repeatArry!=null&&repeatArry.length>0){
   		var repeatBox = "";
   		for(var i=0;i<repeatArry.length;i++){
   			repeatBox=repeatBox+repeatArry[i];
   		}
   		alert('保存失败,箱号'+repeatBox+"已经存在，请重新选择",2);
   		return;
   }
     //2.绑定数据
    var updateFn = function(returnData){
        var url = BasePath+'/bill_om_preparedivide/edit';
        wms_city_common.loading("show","正在保存......");
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.inserted = encodeURIComponent(JSON.stringify(inserted));
				param.deleted = encodeURIComponent(JSON.stringify(deleted));
				param.locno=locno;
   			},
   			success: function(result){
   				wms_city_common.loading();
   				var resultDate = $.parseJSON(result);
   				if(resultDate.result=="fail"){
   					 alert(resultDate.msg,2);
   				}else{
   					if(resultDate.repeat!=null&&resultDate.repeat.length>0){
   						var restr = "";
   						for(var i=0,length=resultDate.repeat.length;i<length;i++){
   							if(i==length-1){
   								restr+=resultDate.repeat[i];
   							}else{
   								restr+=resultDate.repeat[i]+",";
   							}
   						}
   						alert('保存失败,箱号'+restr+"已经存在，请重新选择",2);
   					}else{
   						alert('保存成功!');
   						tempObj.datagrid('acceptChanges');
						billompreparedivide.loadDataGrid();
						billompreparedivide.coloseDetailDialog();
   					}
   				}
   		    },
   			error:function(){
   				wms_city_common.loading();
   				alert('保存失败,请联系管理员!',2);
   			}
   	   });
    };
    billompreparedivide.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,updateFn);
};

billompreparedivide.getRepeat = function(tempary) {
var ary=[];
for(var i = 0;i<tempary.length;i++){
	ary.push(tempary[i].boxNo+'_'+tempary[i].importNo);
}
var res = [];  
ary.sort();  
for(var i = 0;i<ary.length;){
 var count = 0;  
 for(var j=i;j<ary.length;j++){
  if(ary[i]== ary[j]) {  
   count++;  
  }    
 }
 if(count>1){
 	res.push(ary[i]); 
 }
 i+=count;   
};
return res;
};


//显示单号输入框
billompreparedivide.showReceiptNo = function(type){
	if(type=="1"){
		$("#receiptNoHide").val("系统自动生成");
	}
	$("#receiptNoHide").attr("disabled",true);
};

//查询箱号主表信息
billompreparedivide.searchBox = function(){
	var ownerNo=$("#ownerNoHide").val();
	var locno=billompreparedivide.locno;
	var billSource=$("#billSourceBox").combobox("getValue");
	
	if(billSource==''){
		alert("请选择单据来源。");
		return false;
	}
	//发货通知单单号
	var checkedItems = $('#mainDataGrid3').datagrid('getRows');
	var keyStr = [];
	$.each(checkedItems, function(index, item){
		keyStr.push(item.expNo);
	});
	var expNos = keyStr.join(",");
	$('#expNos').val(expNos);
	
	var fromObjStr=convertArray($('#boxForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_preparedivide/findBoxNo4Divide?locNo='+locno+'&ownerNo='+ownerNo;
	var params = eval("(" +fromObjStr+ ")");
	
    $( "#dataGridJG_Box").datagrid( 'options' ).queryParams= params;
    $( "#dataGridJG_Box").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_Box").datagrid( 'load' );
};


billompreparedivide.clearSearchBox = function(type){
	$("#boxForm").form("clear");
	$('#billSourceBox').combobox("setValue","1");
	$('#brandNoBox').combobox("loadData",[]);
	//清空datagrid数据
	$('#mainDataGrid3').datagrid('clearData');
};

billompreparedivide.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};


//详情
billompreparedivide.showDetail = function(rowData){
	$('#detailDialogView').window({
		title:"预分货单明细"
	 });
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	$("#ownerNo_view").combobox("disable");
	var queryMxURL=BasePath+'/bill_om_preparedivide_dtl/selectItemDetail?receiptNo='+rowData.receiptNo+'&locno='+rowData.locno;
	$( "#dataGridJG_detail_view").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail_view").datagrid( 'load' );
};

//添加箱记录
billompreparedivide.batchSelectBoxOk = function(){
	var checkedRows = $("#dataGridJG_Box").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择箱号!',1);
		return;
	}
	$.each(checkedRows,function(index,item){
		var  rowData = {
	    		"boxNo":item.boxNo,
	    		"quality":item.quality,
	    		"itemType":item.itemType,
	    		"cellNo":item.cellNo,
	    		"receiptQty":item.receiptQty
        };

         $("#dataGridJG_detail").datagrid('appendRow', rowData);
			    	
	});
	$('#boxDialog').window('close'); 
};
//加载库区

billompreparedivide.wareArea = function(firstId,secondId,isShowAllSelect){
		wms_city_common.bind4MultipleBoxSelect(
				firstId,
				'/cm_defware/get_biz?locno='+billompreparedivide.locno,
				{},
				['wareNo','wareName','wareNo'],
				true,
				true,
				[secondId],
				['/cm_defarea/get_storeroom?locno='+billompreparedivide.locno],
				[['areaNo','areaName']],
				['wareNo'],
				[isShowAllSelect],
				"-",
				''
		);
    };
    
//手工关闭预分货单
billompreparedivide.overPrepareDivide = function(){
	
	var checkRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkRows.length < 1){
		alert("请选择需要关闭的数据!");
		return;
	}
	
	var tipStr = "";
	var dataList = [];
	$.each(checkRows, function(index, item){
		if(item.status == '10' || item.status == '91'){
			tipStr = "不能关闭建单或已关闭的单据!单号:"+item.receiptNo;
			return false;
		}
		var params = {locno:item.locno,ownerNo:item.ownerNo,receiptNo:item.receiptNo};
		dataList[dataList.length] = params;
	});
	
	if(tipStr!=""){
		alert(tipStr);
		return false;
	}
	
	var queryParams = {
		datas : JSON.stringify(dataList)
	};
	
	$.messager.confirm("确认","您确定要手工关闭选择的数据?", function (r){ 
		if(r){
			wms_city_common.loading("show","正在处理....");
			var url = BasePath+'/bill_om_preparedivide/overPrepareDivide';
			ajaxRequest(url,queryParams,function(data){
				if(data.result=="success"){
					$('#dataGridJG').datagrid('load');
					alert("关闭成功!");
				}else{
					alert(data.msg,2);
				}
				wms_city_common.loading();
			});
		}
	});
};