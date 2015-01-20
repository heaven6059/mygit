var billimcheckrep = {};
billimcheckrep.preColNamesDtl1 = [
                           {title:"品牌",field:"brandName",width:80,align:'left'},
		                   {title:"业务类型",field:"statusName",width:80,align:'left'},
		                   {title:"供应商代码",field:"supplierNo",width:80,align:'left'},
		                   {title:"供应商名称",field:"supplierName",width:200,align:'left'},
		                   {title:"通知单号",field:"importNo",width:180},
		                   {title:"商品编码",field:"itemNo",width:150,align:'left'},
		                   {title:"商品名称",field:"itemName",width:200,align:'left'},
		                   {title:"商品大类",field:"cateName",width:70,align:'left'},
		                   {title:"年份",field:"yearsName",width:70,align:'left'},
		                   {title:"季节",field:"seasonName",width:70,align:'left'},
		                   {title:"性别",field:"genderName",width:70,align:'left'},
		                   {title:"入库日期",field:"reciveDate",width:80}       
                    ];

billimcheckrep.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount",align:'right'}
                        ] ;
billimcheckrep.sizeTypeFiledName = 'sizeKind';



//将数组封装成一个map
billimcheckrep.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};


billimcheckrep.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
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

//校验开始日期和结束日期的大小
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

/**
 * ===================================尺码横排======================================================
 */
/**
 * 获取动态表头
 */
billimcheckrep.getColumnInfo = function(params){
	var beforeColArr = billimcheckrep.preColNamesDtl1;
	var afterColArr = billimcheckrep.endColNamesDtl1; 
	var tempUrl = BasePath+'/bill_im_check_rep/initHead';
    params.preColNames = JSON.stringify(beforeColArr);
    params.endColNames = JSON.stringify(afterColArr);
    params.sizeTypeFiledName = billimcheckrep.sizeTypeFiledName;
    var resultColumns;
    wms_city_common.loading("show","正在加载尺码头信息");
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data:params,
        cache: true,
        async: false,
        success: function(returnData){
        	wms_city_common.loading();
        	resultColumns = returnData.header;
		}
      });
     return  resultColumns;
};

/**
 * 查询
 */
billimcheckrep.searchBillImCheckRep = function(){
	var starttime = $("#startTime").datebox('getValue');
	var endTime = $("#endTime").datebox('getValue');
	if(starttime==""){
		alert("请选择入库开始日期");
		return;
	}
	if(endTime==""){
		alert("请选择入库结束日期");
		return;
	}
	if(!isStartEndDate(starttime,endTime)){
		alert("结束时间不能小于开始时间");
		return;
	}
	if($("#sysNoSearch").combobox("getValue")==""){
		alert("请选择品牌库");
		return;
	}
//	if($("#search_itemType").combobox("getValue")==""){
//		alert("请选择业务类型");
//		return;
//	}
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var params = eval("(" +fromObjStr+ ")");
	 var resultColumns = billimcheckrep.getColumnInfo(params);
	 var queryMxURL=BasePath+'/bill_im_check_rep/createBillCheckInRep';
	 if(resultColumns==null){
		 alert("没有查询到数据,请重新选择！",1);
		    $("#dataGridJG").datagrid({
				 queryParams:params,
				 url:queryMxURL,
				 columns:{}
		    });
	 } else {
		    $("#dataGridJG").datagrid({
				 queryParams:params,
				 url:queryMxURL,
		        columns:resultColumns.returnCols
		    }); 
	 }
	
};

/**
 * 清空查询条件
 */

billimcheckrep.clearForm = function(){
	$("#searchForm").form('clear');
	$('#brandNameId').combobox("loadData",[]);
	$('#supplierNameId').combobox("loadData",[]);
	$('#cateTwoCondition').combobox("loadData",[]);
	$('#cateThreeCondition').combobox("loadData",[]);
	$('#seasonNameCondition').combobox("loadData",[]);
	$('#genderNameCondition').combobox("loadData",[]);
};

billimcheckrep.loadSupplierNo = function(type,id,isSelectAll){
	if(type=="0"){
		wms_city_common.comboboxLoadFilter(
				[id],
				'supplierNo',
				'supplierName',
				'valueAndText',
				false,
				[isSelectAll],
				BasePath+'/supplier/get_all',
				{},
				null,
				null);
	}else if(type=="4" || type=="5"){
		wms_city_common.comboboxLoadFilter(
				[id],
				'storeNo',
				'storeName',
				'valueAndText',
				false,
				[isSelectAll],
				BasePath+'/store/get_all?storeType=22',
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				[id],
				'storeNo',
				'storeName',
				'valueAndText',
				false,
				[isSelectAll],
				null,
				{},
				null,
				null);
	}
};
/**
 * 导出excel
 */
//exportExcel=function(){
//	exportExcelRepInfo('dataGridJG','/bill_im_check_rep/do_export2','入库查询导出');
//};
billimcheckrep.exportExcel = function(){
//	exportExcelBill4Detail('dataGridJG',billimcheckrep.preColNamesDtl1,
//			billimcheckrep.endColNamesDtl1, billimcheckrep.sizeTypeFiledName,"入库查询");
	export4Size("dataGridJG", billimcheckrep.preColNamesDtl1, billimcheckrep.endColNamesDtl1, "入库查询");
};

function export4Size(dgId,preColNames,endColNames,fileName){
	
	var url=BasePath+'/bill_im_check_rep/do_export3';
	
	var dgObj = $("#"+dgId);
	var dataRow = dgObj.datagrid('getRows');
	if(dataRow.length > 0){
		var params = dgObj.datagrid('options').queryParams;
		$("#exportExcelForm").remove();
		$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
		var fromObj = $('#exportExcelForm');
		wms_city_common.loading("show","正在导出");
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.locno=billimcheckrep.locno;
				param.sysNo=$("#sysNoSearch").combobox("getValue");
				param.brandName=$("#brandNameId").combobox("getValue");
				param.reciveDateStart=$("#startTime").datebox('getValue');
				param.reciveDateEnd=$("#endTime").datebox('getValue');
				
				param.businessType=$("#search_itemType").combobox("getValue");
				param.seasonName=$("#seasonNameCondition").combobox("getValues");
				param.genderName=$("#genderNameCondition").combobox("getValues");
				param.years=$("#yearsNameCondition").val();
				
				param.supplierNo=$("#supplierNameId").combobox("getValue");
				param.importNo=$("#importNo").val();
				param.itemNo=$("#itemNo").val();
				param.itemName=$("#itemName").val();
				
				param.cateOne=$("#cateOneCondition").combobox("getValue");
				param.cateTwo=$("#cateTwoCondition").combobox("getValue");
				param.cateThree=$("#cateThreeCondition").combobox("getValue");
				
				param.preColNames = JSON.stringify(preColNames);
				param.endColNames = JSON.stringify(endColNames);
				param.fileName = fileName;

//				if (params != null && params != {}) {
//					$.each(params, function(i) {
//						param[i] = params[i];
//					});
//				}
				
			},
			success : function() {
			}
		});
		wms_city_common.loading();
	}else{
		alert("没有可导出的数据!");
		return false;
	}
}

//function exportExcelBill4Detail(dataGridId,preColNames,endColNames,sizeTypeFiledName,excelTitle){
//	
//	var url=BasePath+'/bill_im_check_rep/do_export4Detail';
//	
//	var $dg = $("#"+dataGridId+"");
//	
//	var tempTotal = -1;
//	var paginationObj = $dg.datagrid('getPager').data("pagination");
//    if(paginationObj){
//    	tempTotal = paginationObj.options.total; 
//        if(tempTotal>10000){
//        	alert('导出文件数据超出限制最大数量10000!',1);
//        	return;
//        }
//    }
//	$("#exportExcelForm").remove();
//	
//	$("<form id='exportExcelForm' method='post'></form>").appendTo("body"); ;
//	
//	var fromObj=$('#exportExcelForm');
//	
//	var dataRow=$dg.datagrid('getRows');
//	if(dataRow.length>0){
//		if(tempTotal>dataRow.length){
//			var dataGridUrl = $dg.datagrid( 'options' ).url;
//			var dataGridQueryParams = $dg.datagrid( 'options' ).queryParams;
//			dataGridQueryParams.page = 1;
//			dataGridQueryParams.rows = tempTotal;
//		 	$.ajax({
//				  type: 'POST',
//				  url: dataGridUrl,
//				  data: dataGridQueryParams,
//				  cache: true,
//				  async:false, // 一定要
//				  success: function(resultData){
//					  dataRow = resultData.rows;
//				  }
//		     });
//		}
//	    fromObj.form('submit', {
//			url: url,
//			onSubmit: function(param){
//				param.locno=billimcheckrep.locno;
//				param.sysNo=$("#sysNoSearch").combobox("getValue");
//				param.brandName=$("#brandNameId").combobox("getValue");
//				param.reciveDateStart=$("#startTime").datebox('getValue');
//				param.reciveDateEnd=$("#endTime").datebox('getValue');
//				
//				param.businessType=$("#search_itemType").combobox("getValue");
//				param.supplierNo=$("#supplierNameId").combobox("getValue");
//				param.itemNo=$("#itemNo").val();
//				param.itemName=$("#itemName").val();
//				
//				param.importNo=$("#importNo").val();
//				//param.itemCate=$("#itemCateId").combobox("getValue");
//				
//				param.cateOne=$("#cateOneCondition").combobox("getValue");
//				param.cateTwo=$("#cateTwoCondition").combobox("getValue");
//				param.cateThree=$("#cateThreeCondition").combobox("getValue");
//				
//				param.preColNames=JSON.stringify(preColNames);
//				param.endColNames=JSON.stringify(endColNames);
//				param.sizeTypeFiledName=sizeTypeFiledName;
//				param.fileName=excelTitle;
//				param.dataRow=JSON.stringify(dataRow);
//			},
//			success: function(){
//				
//		    }
//	   });
//	}else{
//		alert('数据为空，不能导出!',1);
//	}
//}

/**
 * 初始化开始
 */
$(document).ready(function(){
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),brandNoObj:$("#brandNameId")}
			);
	wms_city_common.loadSysNo4Cascade(objs,[false]);
	
	billimcheckrep.loadLoc();
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[0] != null){
		   				billimcheckrep.footer = data.footer[1];
		   			}else{
		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
			   			rows[0] = billimcheckrep.footer;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	//初始化商品类别
//	wms_city_common.comboboxLoadFilter(
//			["itemCateId"],
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
	
	//初始化商品类别
	wms_city_common.comboboxLoadFilter(
			["search_itemType"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IM_UM_TYPE',
			{},
			null,
			null);
	//根据品牌初始季节和性别
	billimcheckrep.initGenderAndSeason();
	
	$("#startTime").datebox('setValue',billimcheckrep.getDate(-6));
	$("#endTime").datebox('setValue',billimcheckrep.getDate(0));
});
billimcheckrep.initGenderAndSeason =  function(){
	$('#sysNoSearch').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderNameCondition',true,false);
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonNameCondition',true,false);
		}
	});
};
//加载仓库信息
billimcheckrep.loadLoc = function(){
		$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billimcheckrep.locno = data.locno;
			billimcheckrep.currentDate = data.currentDate10Str;
		}
	});
};
billimcheckrep.getDate = function(n){
	var nowDate = new Date();
	var newDate = new Date();
	var newTimes = nowDate.getTime() + (n*24*3600000);
	newDate.setTime(newTimes);
	var y = newDate.getFullYear();
	var m = newDate.getMonth()+1;
	var d = newDate.getDate();
	var dateStr = y+"-";
	dateStr += (m<10?("0"+m):(m))+"-";
	dateStr += (d<10?("0"+d):(d));
	return dateStr;
};