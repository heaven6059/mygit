var billomrecheckjoin = {};
billomrecheckjoin.locno;
//状态
billomrecheckjoin.status = {
	"14":"已交接",
	"13":"未交接"
};

billomrecheckjoin.exportDataGridDtl1Id = 'dataGridJG_checked';
billomrecheckjoin.preColNamesDtl1 = [
						   {title:"复核单号",field:"recheckNo",width:120},
						   {title:"箱号",field:"labelNo",width:120},
		                   {title:"商品编码",field:"itemNo",width:120},
		                   {title:"商品名称",field:"itemName",width:150},
		                   {title:"颜色",field:"colorName",width:120}
                    ];
billomrecheckjoin.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billomrecheckjoin.sizeTypeFiledName = 'sizeKind';


$(document).ready(function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billomrecheckjoin.locno = data.locno;
		}
	});
});

billomrecheckjoin.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billomrecheckjoin.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//状态
billomrecheckjoin.statusFormatter = function(value, rowData, rowIndex){
	return billomrecheckjoin.status[value];
};

//弹出查询箱号窗口
billomrecheckjoin.showSearchDialog  = function(){
	$("#searchDialog").form("clear");
	$("#dataGridJG_Search").datagrid("uncheckAll");
	$("#dataGridJG_Search").datagrid("clearData");
	$("#showDialog").window('open'); 
};

//查询箱号
billomrecheckjoin.searchRecheckNo = function(){
	var pp = $('#tt').tabs('getSelected'); 
	if(pp.attr("name")=="check"){
		$("#searchStatus").val("13");
	}else{
		$("#searchStatus").val("14");
	}
	var fromObjStr=convertArray($('#searchDialog').serializeArray());
	var queryMxURL=BasePath+'/bill_om_recheck_join/findRecheckNo?locno='+billomrecheckjoin.locno;
	
    $( "#dataGridJG_Search").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG_Search").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_Search").datagrid( 'load' );
    
};

billomrecheckjoin.searchNoChecked = function(){
	var fromObjStr=convertArray($('#noCheckSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_recheck_join/findNoReCheck?locno='+billomrecheckjoin.locno;
	$( "#dataGridJG_NoChecked").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	$( "#dataGridJG_NoChecked").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_NoChecked").datagrid( 'load');
   
};

billomrecheckjoin.searchClearNochecked = function(){
	$("#noCheckSearchForm").form("clear");
};



billomrecheckjoin.searchClearchecked = function(){
	$("#checkedForm").form("clear");
};

//确认
billomrecheckjoin.searchAdd = function(){
	var rowData = $("#dataGridJG_Search").datagrid("getChecked");
	if(rowData.length>0){
		var pp = $('#tt').tabs('getSelected'); 
		if(pp.attr("name")=="check"){
			$("#searchRecheckNo").val(rowData[0].RECHECK_NO);
		}else{
			$("#searchRecheckedNo").val(rowData[0].RECHECK_NO);
		}
		
	}
	$("#showDialog").window('close'); 
};

//清空
billomrecheckjoin.searchClear = function(){
	$("#searchDialog").form("clear");
};

billomrecheckjoin.searchLocClear = function(){
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

//交接确认
billomrecheckjoin.sendCheck = function(){
	var checkedRows = $("#dataGridJG_NoChecked").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要交接的的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要交接这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.LABELNO);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_om_recheck_join/sendReCheck';
        	var data={
        	    "rowIdstr":keyStr.join(",")
        	};
        	billomrecheckjoin.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 alert("交接成功");
        			 billomrecheckjoin.searchNoChecked();
        		 }else{
        			 alert('交接失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    }); 
};

billomrecheckjoin.statusSearchFormatter = function(value,rowData,rowIndex){
	return billomrecheckjoin.status[value];
};

billomrecheckjoin.statusFormatter = function(value,rowData,rowIndex){
	return billomrecheckjoin.status["13"];
};

billomrecheckjoin.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billomrecheckjoin.searchChecked = function(){
    billomrecheckjoin.loadDetail();
};

//尺码横排
billomrecheckjoin.loadDetail = function(rowData,rowIndex){
	
	billomrecheckjoin.curRowIndex=rowIndex;
	
	//billomrecheckjoin.initGridHead(rowData.sysNo);
	billomrecheckjoin.initGridHead("TM");
	
	billomrecheckjoin.loadDataDetailViewGrid(rowData);
   
};


billomrecheckjoin.initGridHead = function(sysNo){
     var beforeColArr = billomrecheckjoin.preColNamesDtl1;
	 var afterColArr = billomrecheckjoin.endColNamesDtl1; 
	 var columnInfo = billomrecheckjoin.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billomrecheckjoin.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

billomrecheckjoin.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billomrecheckjoin.sizeTypeFiledName
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
billomrecheckjoin.loadDataDetailViewGrid = function(rowData){
	var url=BasePath+'/bill_om_recheck_join/findReChecked?locno='+billomrecheckjoin.locno+'&recheckNo='+$("#searchRecheckedNo").val()
				+'&labelNo='+$("#searchLabelNo").val()+'&itemNo='+$("#searchItemNo").val();
    $('#dataGridJG_checked').datagrid(
			{
				'url':url,
				'pageNumber':1 
			}
	);
};
