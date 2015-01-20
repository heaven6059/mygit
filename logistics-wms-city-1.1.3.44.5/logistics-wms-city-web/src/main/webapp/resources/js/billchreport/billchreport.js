
var billchreport = {};
billchreport.user;
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
billchreport.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//加载Grid数据Utils
billchreport.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};
//搜索数据
billchreport.searchData = function(){
	
	var planDateStart =  $('#startPlanDateCondition').datebox('getValue');
	var planDatemEnd =  $('#endPlanDateCondition').datebox('getValue');
	
	if(!isStartEndDate(planDateStart,planDatemEnd)){    
		alert("盘点日期开始日期不能大于结束日期");   
		return;   
	} 
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_ch_report/list.json?locno='+billchreport.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	billchreport.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
};


//清除Form表单
billchreport.clearForm = function(id){
	$('#'+id).form("clear");
};
// JS初始化执行
$(document).ready(function(){
	billchreport.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){billchreport.user=u;});
	//初始化品牌库
	wms_city_common.comboboxLoadFilter(
			["sysNoCondition"],
			'itemvalue',
			'itemnamedetail',
			'valueAndText',
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
			{},
			null,
			null);
	//品牌库绑定级联事件
	wms_city_common.sysBind(
			'sysNoCondition',
			['brandNoCondition','yearsCondition','seasonCondition','genderCondition'],
			['/brand/get_bizDy',
			 '/lookupdtl/selectLookupdtlBySysNo?lookupcode=YEARS',
			 '/lookupdtl/selectLookupdtlBySysNo?lookupcode=SEASON',
			 '/lookupdtl/selectLookupdtlBySysNo?lookupcode=GENDER'
			 ],
			[["brandNo","brandName"],["itemval","itemname"],["itemval","itemname"],["itemval","itemname"]],
			[true,true,true,true]);
	multipleBoxBindAllSelectCancel('brandNoCondition','brandNo');
	multipleBoxBindAllSelectCancel('yearsCondition','itemval');
	multipleBoxBindAllSelectCancel('seasonCondition','itemval');
	multipleBoxBindAllSelectCancel('genderCondition','itemval');
	//初始化仓区
	wms_city_common.initWareNoForCascade(
			billchreport.user.locno,
			['wareNoCondition'],
			['areaNoCondition'],
			['stockNoCondition'],
			null,
			[true],
			null
			);
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
    );
	$('#mainDataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.total > 0){
				if(data.footer != undefined){
					billchreport.footer = data.footer[0];
				}else{
					var rows = $('#mainDataGrid').datagrid('getFooterRows');
					rows[0] = billchreport.footer;
					$('#mainDataGrid').datagrid('reloadFooter');
				}
			}
					
		}
	});
	
});
billchreport.footer;

billchreport.do_export = function(){
	exportExcelBaseInfo('mainDataGrid','/bill_ch_report/export.htm?locno='+billchreport.user.locno,'盘点查询报表导出');
};

/**
 * 为多选下拉框绑定【当选择‘全选’时,取消其他值;当选择其他值时,取消‘全选’】的事件
 * @param id
 * @param valueName
 */
function multipleBoxBindAllSelectCancel(id,valueName){
	$('#'+id).combobox({
		onSelect:function(record){
			var obj = $('#'+this.id);
			var values = obj.combobox('getValues');
	    	var value=record[valueName];
	    	var newValues=[];
	    	if(value == ''){
	    		newValues.push(value);	
	    	}else{
	    		for(var idx=0;idx<values.length;idx++){
	    			if(values[idx]!=''){
	    				newValues.push(values[idx]);
	    			}
	    		}
	    	}
	    	obj.combobox('setValues',newValues);
    	}
		
	});
}
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