var itemcollectcontent = {};
itemcollectcontent.user = {};
itemcollectcontent.dataGridFooter={};
itemcollectcontent.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//清除查询条件
itemcollectcontent.searchClear = function(){
	$('#searchForm').form("clear");
	$('#brandNoCondition').combobox("loadData",[]);
	$('#cateTwoCondition').combobox("loadData",[]);
	$('#cateThreeCondition').combobox("loadData",[]);
};
//查询商品储位库存信息
itemcollectcontent.searchItemcollectcontent = function(){
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/item_collect_content/dtl_list.json?locno='+itemcollectcontent.user.locno;
	
    //3.加载明细
    $( "#dataGriditemcollectcontent").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGriditemcollectcontent").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGriditemcollectcontent").datagrid( 'load' );
    
};
itemcollectcontent.do_export = function(){
	var exportColumns = $("#dataGriditemcollectcontent").datagrid('options').columns[0];
	var str = '[';
	for(var index=0;index<exportColumns.length;index++){
		var temp = exportColumns[index];
		str +=  "{\"field\":\""+temp.field+"\",\"title\":\""+temp.title+"\"}";
		if(index != exportColumns.length-1){
			str += ",";
		}
	}
	str += "]";
	var searchUrl = BasePath+'/item_collect_content/exportlist.json?locno='+itemcollectcontent.user.locno;
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var data = eval("(" +fromObjStr+ ")");
	data.page = 1;
	data.rows = $("#dataGriditemcollectcontent").datagrid('options').pageSize;
	var result = {};
	$.ajax({
		  async:false,
		  type: 'POST',
		  url: searchUrl,
		  data: data,
		  cache: true,
		  success: function(r){
			  result = r;
		  }
	});
	if(!result || result == null || result.total == 0){
		alert("没有可以导出的数据!",1);
		return;
	}else if(result.total > 10000){
		alert("数据大于10000条不能导出!",1);
		return;
	}
	$("#sysNoCondition_export").val($("#sysNoSearch").combobox('getValue'));
	$("#brandNoCondition_export").val($("#brandNoCondition").combobox('getValue'));
	$("#itemTypeCondition_export").val($("#itemTypeCondition").combobox('getValue'));
	$("#qualityCondition_export").val($("#qualityCondition").combobox('getValue'));
	
	$("#itemNoCondition_export").val($("#itemNoCondition").val());
	$("#cateOneCondition_export").val($("#cateOneCondition").combobox('getValues'));
	$("#cateTwoCondition_export").val($("#cateTwoCondition").combobox('getValues'));
	$("#cateThreeCondition_export").val($("#cateThreeCondition").combobox('getValues'));
	
	$("#itemNameCondition_export").val($("#itemNameCondition").val());
	$("#barcodeCondition_export").val($("#barcodeCondition").val());
	$("#yearsNameCondition_export").val($("#yearsNameCondition").val());
	$("#seasonNameCondition_export").val($("#seasonNameCondition").combobox('getValues'));
	$("#genderNameCondition_export").val($("#genderNameCondition").combobox('getValues'));
	
	$("#locnoCondition_export").val(itemcollectcontent.user.locno);
	$("#exportColumnsCondition_export").val(str);
	$("#exportForm").submit();
};

//初始化表格加载事件
itemcollectcontent.initGridOnLoadSuccess = function() {
	$('#dataGriditemcollectcontent').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.footer!=undefined){
				if (data.footer[1].isselectsum) {
					itemcollectcontent.dataGridFooter.qty = data.footer[1].qty;
				} else {
					var rows = $('#dataGriditemcollectcontent').datagrid('getFooterRows');
					rows[1]['qty'] = itemcollectcontent.dataGridFooter.qty;
					$('#dataGriditemcollectcontent').datagrid('reloadFooter');
				}
			}			
		}
	});
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
$(document).ready(function(){
	itemcollectcontent.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){itemcollectcontent.user=u;});
	//初始化表格加载事件
	itemcollectcontent.initGridOnLoadSuccess();
	//品牌
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),brandNoObj:$("#brandNoCondition")}
			);
	wms_city_common.loadSysNo4Cascade(objs,[false]);
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
    );
	
	//初始化商品类别
//	wms_city_common.comboboxLoadFilter(
//			["cateNoCondition"],
//			'cateCode',
//			'cateName',
//			'valueAndText',
//			false,
//			[true],
//			BasePath+'/category/getCategory4Simple?cateLevelid=1',
//			{},
//			null,
//			null);
	//初始化商品属性
	wms_city_common.comboboxLoadFilter(
			["itemTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{},
			null,
			null);
	//初始化商品品质
	wms_city_common.comboboxLoadFilter(
			["qualityCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			null,
			null);
	//初始化性别和季节
	itemcollectcontent.initGenderAndSeason();
});
itemcollectcontent.initGenderAndSeason =  function(){
	$('#sysNoSearch').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonNameCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderNameCondition',true,false);

		}
	});
};
