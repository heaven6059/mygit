var billconqualitychange = {};
billconqualitychange.locno;
//状态
billconqualitychange.flagData= {
	"10":"未上传",
	"13":"已上传"
};


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

billconqualitychange.detail = function(rowData){
	if(rowData==null){
		 $( "#dataGridJG_detail").datagrid( 'loadData',[] );
		 return;
	}
	var queryMxURL=BasePath+'/bill_con_quality_change_dtl/list.json?locno='+billconqualitychange.locno+'&changeNo='+rowData.changeNo+'&ownerNo='+rowData.ownerNo;
    $( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail").datagrid( 'load' );
};

