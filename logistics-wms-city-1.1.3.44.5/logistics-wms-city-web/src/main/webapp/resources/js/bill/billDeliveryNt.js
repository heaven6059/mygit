/*分货单查询
 * author:lt
 * creat:2013-08-22
 */
billDeliveryNt = {};
billDeliveryNt.curRowIndex = -1;
billDeliveryNt.type = 0;
billDeliveryNt.sizeTypeFiledName = 'sizeKind';
billDeliveryNt.dtlDataGrid = 'DtlDataGrid';


billDeliveryNt.beforeColArr = [
	                   {title:"序列号",field:"seqId" },
	                   {title:"商品编码",field:"itemNo",width:120},
	                   {title:"商品名称",field:"itemName",width:120},
	                   {title:"箱号",field:"boxNo"}, 
	                   {title:"客户编码",field:"storeNo",width:60},
	                   {title:"客户名称",field:"storeName",width:120},
	                ];

/*可选列
*  {title:"地址",field:"address",width:150}, 
{title:"组别",field:"groupType" },
{title:"箱类型",field:"boxType" }, 
* {title:"大类名称",field:"cateName" },
{title:"包装单位",field:"packUnit" }, 
{title:"包装规格",field:"packSpec" }, 
{title:"包装数量",field:"packQty" }, 
{title:"出库数量",field:"outQty" },
{title:"入库数量",field:"inQty" },
{title:"原因代码",field:"reasonId" },
{title:"原因描述",field:"reason" },
{title:"返配的供应商编号",field:"supplierNo" }, 
{title:"供应商名称",field:"supplierName" },
{title:"审批原因代码",field:"vldTypeid" }
* {title:"颜色",field:"colorNo"}, 
* {title:"单价",field:"cost"},
* {title:"金额",field:"allCost"}
*/

billDeliveryNt.afterColArr = [{title:"合计",field:"allCount"}] ; 

//导出
billDeliveryNt.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var excelTitle='分货订单导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill(billDeliveryNt.dtlDataGrid,sysNo,billDeliveryNt.beforeColArr,
			billDeliveryNt.afterColArr,billDeliveryNt.sizeTypeFiledName,excelTitle);
};

//上单
billDeliveryNt.preBill=function(){
	if(billDeliveryNt.curRowIndex < 0){
		alert('不存在当前单据');
		return;
	}
	billDeliveryNt.type = 1;
    preBill('mainDataGrid',billDeliveryNt.curRowIndex,billDeliveryNt.type,billDeliveryNt.callBackBill);
};

//下单
billDeliveryNt.nextBill=function(){
	if(billDeliveryNt.curRowIndex < 0){
		alert('不存在当前单据');
		return;
	}
	billDeliveryNt.type=2;
    preBill('mainDataGrid',billDeliveryNt.curRowIndex,billDeliveryNt.type,billDeliveryNt.callBackBill);
};

billDeliveryNt.callBackBill=function(curRowIndex,rowData){
	if(billDeliveryNt.type==1||billDeliveryNt.type==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billDeliveryNt.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billDeliveryNt.type=0;
		}else{
			if(billDeliveryNt.type==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};


billDeliveryNt.status = [{    
    "label":"0",
    "text":"制单", 
    "value":"0→制单" 
},{    
    "label":"100",    
    "text":"完结", 
    "value":"100→完结"   
}];

billDeliveryNt.statusTrans = [{    
    "label":"0",
    "text":"未传输", 
    "value":"0→未传输" 
},{    
    "label":"1",    
    "text":"已传输", 
    "value":"1→已传输"   
}];

billDeliveryNt.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		type: 'POST',
		url: url,
		data: reqParam,
		cache: true,
		success: callback
	});
};

billDeliveryNt.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var resultData = {};
	var url = BasePath+'/initCache/getBrandList.htm';
	$.ajax({
		type: 'POST',
		url: url,
		data: {
				sysNo:sysNo,
				preColNames:JSON.stringify(beforeColArr),
		        endColNames:JSON.stringify(afterColArr),
		        sizeTypeFiledName:billDeliveryNt.sizeTypeFiledName
			},
		cache: true,
		async:false,
		success: function(returnData){
			resultData.columnArr = returnData.returnCols;
			resultData.startType = returnData.startType;
         	resultData.maxType = returnData.maxType;
		}
	});
	return resultData;
};

//动态生成表格列
billDeliveryNt.setGridColumns = function(sysNo){
	var columnInfo = billDeliveryNt.getColumnInfo(sysNo,billDeliveryNt.beforeColArr,billDeliveryNt.afterColArr);
	$("#DtlDataGrid").datagrid({
		columns:columnInfo.columnArr
	});
};

//生成表格数据
billDeliveryNt.setGridData = function(billNo){
	var dg = $("#DtlDataGrid");
	var url = BasePath+ '/bill_delivery_nt_dtl/queryDeliveryNtDtlList';
	var queryParams = {"billNo":billNo};
	dg.datagrid('options').url = url;//更改请求的url
	dg.datagrid('load',queryParams);
	dg.datagrid('options').queryParams = '';
};

billDeliveryNt.loadDetail = function(billNo, sysNo,rowIndex){
	billDeliveryNt.curRowIndex=rowIndex;
	var url = BasePath+'/bill_delivery_nt/get';
	var reqParam = {
		"billNo":billNo,
	};
	//填充主表
	billDeliveryNt.ajaxRequest(url,reqParam,function(resultData){//resultData-服务器回传的数据
		$('#dltDataForm').form('load',resultData);
		//底部单据状态显示栏
		$('#statusSp').html(resultData.statusStr);
		$('#statusTransSp').html(resultData.statusTransStr);
		$('#creatorSp').html(resultData.creator);
		$('#createtmSp').html(formatDate(resultData.createtm));
		$('#auditorSp').html(resultData.auditor);
		$('#auditdtSp').html(formatDate(resultData.auditdt));
	});
	//动态生成表格列
	billDeliveryNt.setGridColumns(sysNo);
	//生成表格数据
	billDeliveryNt.setGridData(billNo);
	//切换Tab页
	$('#mainTab').tabs('select','单据明细');
};

//初始化品牌库
billDeliveryNt.InitSysNo = function(){
	$("#sysNo").combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"150"
	});
};
/*
//初始化发货方、收货方
billDeliveryNt.InitStore = function(){
	$("storeNO").combogrid({
		panelWidth:450, 
		idField:'storeNO',    
	    textField:'StoreName',    
	    url:'/store/list.json',
	    pagination : true,
        rownumbers:true,
        mode: 'remote',
	    columns:[[    
	        {field:'storeNO',title:'收货方编码',width:60},    
	        {field:'StoreName',title:'收货方名称',width:100}      
	    ]]
	});
	$("storeNoFrom").combogrid({
		panelWidth:450, 
		idField:'storeNoFrom',    
	    textField:'StoreName',    
	    url:'/store/list.json',
	    pagination : true,
        rownumbers:true,
        mode: 'remote',
	    columns:[[    
	        {field:'storeNO',title:'发货方编码',width:60},    
	        {field:'StoreName',title:'发货方名称',width:100}      
	    ]]
	});
};*/

//初始化等级定义
billDeliveryNt.InitGrandNo= function(){
	$("#gradeNo").combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=GRADE_NO',
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"150"
	});
};

//初始化属性定义
billDeliveryNt.InitPropertyNO = function(){
	$("#propertyNo").combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=PROPERTY_NO',
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"150"
	});
};

//切换选项卡刷新单据查询的dataGrid
function refreshTabs(){
	$('#mainTab').tabs({
		onSelect:function(title,index){
			
			$('#mainDataGrid').datagrid('resize', {
			     width:function(){return document.body.clientWidth;}
			});
			
		    $('#easyui-panel-id').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#DtlDataGrid').datagrid('resize', {
		        width:function(){return document.body.clientWidth;}
		     });
		    
		},onLoad:function(panel){
			$('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		}
		
	});
}

$(document).ready(function(){
	//数据下拉框初始化
	billDeliveryNt.InitSysNo();
	billDeliveryNt.InitGrandNo();
	billDeliveryNt.InitPropertyNO();
	//billDeliveryNt.InitStore();
	//设置文本框只读
	$("#storeName").attr("readonly",true);
	$("#supplierName").attr("readonly",true);
});
