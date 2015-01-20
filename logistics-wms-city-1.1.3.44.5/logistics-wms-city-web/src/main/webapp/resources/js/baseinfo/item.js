var item = {};

item.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

item.loadDetail = function(itemNo){
	var url = BasePath+'/item/get';
	var reqParam={
		   "itemNo":itemNo
     };
	item.ajaxRequest(url,reqParam,function(data){
		$('#dataForm').form('load',data);
		$('#dataForm_other').form('load',data);
	});
	$('#dataGridItemBarcode').datagrid({'url':BasePath+'/item_barcode/get_biz?itemNo='+itemNo});
	$('#dataGridItemTypeinfo').datagrid({'url':BasePath+'/item_typeinfo/get_biz?itemNo='+itemNo});
	$('#dataGridItemPack').datagrid({'url':BasePath+'/item_pack/get_biz?itemNo='+itemNo});
};

item.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150,
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 //tempData[tempData.length] = {itemvalue:'',itemnamedetail:'全选'};
	    	 for(var i=0;i<data.length;i++){
	    		tempData[tempData.length] = data[i];
	    	 }
	    	 return tempData;
	     },
	     onLoadSuccess:function(){
	    	 //var tempObj = $(this);
	    	 //tempObj.combobox('select', 'N');
	     }
	  });
};

//查询商品信息
item.searchItem = function(){
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/item/list.json';
	
    //3.加载明细
    $( "#dataGridItem").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridItem").datagrid( 'load' );
    
};

//清除查询条件
item.searchItemClear = function(){
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

item.exportExcel=function(){
	exportExcelBaseInfo('dataGridItem','/item/do_export.htm','商品信息导出');
};

parseParam=function(param){
	    var paramStr="";
	   {
	        $.each(param,function(i){
	        	paramStr+='&'+i+'='+param[i];
	        });
	    }
    return paramStr.substr(1);
}; 

//商品品类
item.initCategory = function(){
	$('#cateNoStrCondition').combobox({
		 url:BasePath+'/category/get_biz',
	     valueField:"cateName",
	     textField:"valueAndText",
	     panelHeight:150,
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 tempData[0] = {cateName:'',valueAndText:'全选'};
	    	 var temp;
	    	 for(var idx=0;idx<data.length;idx++){
	    		 temp = data[idx];
	    		 temp.valueAndText = temp.cateName;
	    		 tempData[tempData.length] = temp;
	    	 }
	    	 return tempData;
	     }
	});
};

$(document).ready(function(){
	var objs = [];
	objs.push(
//			{"sysNoObj":$('input[id=sysNo]',$('#dataForm'))},//新增
//			{"sysNoObj":$('input[id=sysNo]',$('#dataEditForm'))},//修改
//			{"sysNoObj":$('input[id=sysNo]',$('#dataViewForm'))},//详情
			{"sysNoObj":$('input[id=sysNo]',$('#dataForm_other'))},
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	item.initCategory();
});