var itemchdifferentreport = {};
itemchdifferentreport.user = {};
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
itemchdifferentreport.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
itemchdifferentreport.searchData = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/item_ch_different_report/dtl_list.json?locno='+itemchdifferentreport.user.locno;
	
    //3.加载明细
    $( "#mainDataGrid").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#mainDataGrid").datagrid( 'options' ).url=queryMxURL;
    $( "#mainDataGrid").datagrid( 'load' );
};
//清除查询条件
itemchdifferentreport.searchClear = function(){
	$('#searchForm').form("clear");
};
itemchdifferentreport.do_export = function(){
	var url = '/item_ch_different_report/do_export_dtl?locno='+itemchdifferentreport.user.locno;
	exportExcelBaseInfo("mainDataGrid",url,"商品盘点差异报表");
};
$(document).ready(function(){
	itemchdifferentreport.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){itemchdifferentreport.user=u;});
	
	$('#mainDataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				itemchdifferentreport.itemQty = data.footer[1].itemQty;
		   				itemchdifferentreport.realQty = data.footer[1].realQty;
		   				itemchdifferentreport.diffQty = data.footer[1].diffQty;
		   			}else{
		   				var rows = $('#mainDataGrid').datagrid('getFooterRows');
			   			rows[1]['itemQty'] = itemchdifferentreport.itemQty;
			   			rows[1]['realQty'] = itemchdifferentreport.realQty;
			   			rows[1]['diffQty'] = itemchdifferentreport.diffQty;
			   			$('#mainDataGrid').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
});