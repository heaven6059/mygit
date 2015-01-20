var billconqualitychange = {};
billconqualitychange.locno;
//状态
billconqualitychange.flagData= {
	"10":"未上传",
	"13":"已上传"
};

billconqualitychange.exportDataGridDtl1Id = 'dataGridJG_detail';
billconqualitychange.preColNamesDtl1 = [
		                   {title:"商品编码",field:"itemNo",width:120},
		                   {title:"商品名称",field:"itemName",width:150},
		                   {title:"颜色",field:"colorName",width:120},
		                   {title:"储位编码",field:"sCellNo",width:120},
		                   {title:"原品质",field:"sQuality",width:120},
		                   {title:"转换品质",field:"dQuality",width:120}
                    ];
billconqualitychange.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billconqualitychange.sizeTypeFiledName = 'sizeKind';

//页面加载
$(document).ready(function(){
	//初始化仓库信息
	billconqualitychange.initLocData();
	
	billconqualitychange.loadDataGrid();
});

//仓库
billconqualitychange.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billconqualitychange.locno = data.locno;
		}
	});
};

billconqualitychange.flagFormatter = function(value,rowData,rowIndex){
	return billconqualitychange.flagData[value];
};


//查询区域信息
billconqualitychange.searchArea = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_con_quality_change/list.json?locno='+billconqualitychange.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
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

//加载数据
billconqualitychange.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_con_quality_change/list.json?locno='+billconqualitychange.locno,
    			'pageNumber':1,
    			onLoadSuccess:function(data){
	    			$('#dataGridJG').datagrid('selectRow', 0);
	    		}
    		});
};




billconqualitychange.detail = function(rowData,rowIndex){
	billconqualitychange.curRowIndex=rowIndex;
	
	//billconqualitychange.initGridHead(rowData.sysNo);
	billconqualitychange.initGridHead("TM");
	billconqualitychange.loadDataDetailViewGrid(rowData);
   
};


billconqualitychange.initGridHead = function(sysNo){
     var beforeColArr = billconqualitychange.preColNamesDtl1;
	 var afterColArr = billconqualitychange.endColNamesDtl1; 
	 var columnInfo = billconqualitychange.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billconqualitychange.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

billconqualitychange.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billconqualitychange.sizeTypeFiledName
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
billconqualitychange.loadDataDetailViewGrid = function(rowData){
	var changeNo="";
	var ownerNo="";
	if(rowData!=null){
		changeNo = rowData.changeNo;
		ownerNo = rowData.ownerNo;
	}
	$('#dataGridJG_detail').datagrid(
    	{
    			'url':BasePath+'/bill_con_quality_change_dtl/findDetail?locno='+billconqualitychange.locno+'&ownerNo='+ownerNo+'&changeNo='+changeNo,
    			'pageNumber':1
    	});
};
