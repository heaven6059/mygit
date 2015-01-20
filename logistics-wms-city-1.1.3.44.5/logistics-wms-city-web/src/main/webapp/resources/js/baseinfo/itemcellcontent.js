var itemcellcontent = {};
itemcellcontent.user = {};
itemcellcontent.dataGridItemFooter={};
itemcellcontent.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
itemcellcontent.selectAreaNoForSearch = function(){
	var locno = itemcellcontent.user.locno;
	var wareNo = $('#wareNoCondition').combobox('getValue');
	if(wareNo == ''){
		wms_city_common.comboboxLoadFilter(
				["areaNoCondition"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[false],
				null,
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				["areaNoCondition"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[false],
				BasePath+'/cm_defarea/get_biz',
				{"locno":locno,"wareNo":wareNo},
				null,
				null);
	}
};
//清除查询条件
itemcellcontent.searchClear = function(){
	$('#searchForm').form("clear");
	$('#areaNoCondition').combotree('clear');
	$('#cateTwoCondition').combobox("loadData",[]);
	$('#cateThreeCondition').combobox("loadData",[]);
	$('#brandNoCondition').combobox("loadData",[]);

};
//查询商品储位库存信息
itemcellcontent.searchItemCellContent = function(){
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/item_cell_content/dtl_list.json?locno='+itemcellcontent.user.locno;
	
    //3.加载明细
    $( "#dataGridItemCellContent").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridItemCellContent").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridItemCellContent").datagrid( 'load' );
    
};
itemcellcontent.do_export = function(){
	exportExcelBaseInfo('dataGridItemCellContent','/item_cell_content/export.htm?locno='+itemcellcontent.user.locno,'商品储位库存导出');
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
itemcellcontent.initOwnerNo = function(data){
	$('#ownerNoCondition').combobox({
		valueField:"ownerNo",
	     textField:"ownerNameForAdd",
	     data:data,
	     panelHeight:150,
	     loadFilter:function(data){
		    	if(data){
		       		 for(var i = 0;i<data.length;i++){
		       			 var tempData = data[i];
		       			 tempData.ownerNameForAdd = tempData.ownerNo+'→'+tempData.ownerName;
		       		 }
		       	 }
		    	return data;
		    }
	});
};

//初始化表格加载事件
itemcellcontent.initGridOnLoadSuccess = function() {
	$('#dataGridItemCellContent').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.footer!=undefined){
				if (data.footer[1].isselectsum) {
					itemcellcontent.dataGridItemFooter.qty = data.footer[1].qty;
					itemcellcontent.dataGridItemFooter.cbqty = data.footer[1].cbqty;
					itemcellcontent.dataGridItemFooter.bulkqty = data.footer[1].bulkqty;
					itemcellcontent.dataGridItemFooter.instockQty = data.footer[1].instockQty;
					itemcellcontent.dataGridItemFooter.outstockQty = data.footer[1].outstockQty;
					itemcellcontent.dataGridItemFooter.unusualQty = data.footer[1].unusualQty;
					itemcellcontent.dataGridItemFooter.usableQty = data.footer[1].usableQty;
					itemcellcontent.dataGridItemFooter.schedulingQty = data.footer[1].schedulingQty;
				} else {
					var rows = $('#dataGridItemCellContent').datagrid('getFooterRows');
					rows[1]['qty'] = itemcellcontent.dataGridItemFooter.qty;
					rows[1]['cbqty'] = itemcellcontent.dataGridItemFooter.cbqty;
					rows[1]['bulkqty'] = itemcellcontent.dataGridItemFooter.bulkqty;
					rows[1]['instockQty'] = itemcellcontent.dataGridItemFooter.instockQty;
					rows[1]['outstockQty'] = itemcellcontent.dataGridItemFooter.outstockQty;
					rows[1]['unusualQty'] = itemcellcontent.dataGridItemFooter.unusualQty;
					rows[1]['usableQty'] = itemcellcontent.dataGridItemFooter.usableQty;
					rows[1]['schedulingQty'] = itemcellcontent.dataGridItemFooter.schedulingQty;
					$('#dataGridItemCellContent').datagrid('reloadFooter');
				}
			}			
		}
	});
};

$(document).ready(function(){
	itemcellcontent.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){itemcellcontent.user=u;});
	//初始化表格加载事件
	itemcellcontent.initGridOnLoadSuccess();
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),brandNoObj:$("#brandNoCondition")}
			);
	wms_city_common.loadSysNo4Cascade(objs,[false]);
	
//	//初始化商品类别
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
	
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
    );
	
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
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNoCondition"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[true],
			BasePath+'/entrust_owner/get_biz',
			{},
			null,
			null);
	//初始化仓区
	wms_city_common.comboboxLoadFilter(
			["wareNoCondition"],
			'wareNo',
			'wareName',
			'valueAndText',
			false,
			[true],
			BasePath+'/cm_defware/get_biz',
			{"locno":itemcellcontent.user.locno},
			null,
			null);
	//根据品牌初始化季节和性别
	itemcellcontent.initGenderAndSeason();
	
});

itemcellcontent.initGenderAndSeason =  function(){
	$('#sysNoSearch').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonNameCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderNameCondition',true,false);
		}
	});
};
