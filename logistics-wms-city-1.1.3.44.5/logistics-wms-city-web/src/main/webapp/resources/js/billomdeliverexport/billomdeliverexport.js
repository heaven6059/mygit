var billomdeliverexport = {};
billomdeliverexport.user={};
billomdeliverexport.columns = {};
billomdeliverexport.preColNamesDtl1 = [{field : 'brandName',title : '品牌',width : 80,align:'left'},
                                    {field : 'expNo',title : '发货通知单',width : 160,align:'left'},
                       			 	{field : 'storeNo',title : '客户编码',width : 80,align:'left'},
                    			 	{field : 'storeName',title : '客户名称',width : 200,align:'left'},
                    			 	{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
                    			 	{field : 'itemName',title : '商品名称',width : 200,align:'left'},
                    			 	{field : 'cateName',title : '商品大类',width : 80,align:'left'},
                    			 	{field:"yearsName",title:"年份",width:70,align:'left'},
                    			 	{field:"seasonName",title:"季节",width:70,align:'left'},
                    			 	{field:"genderName",title:"性别",width:70,align:'left'},
                    			 	{field : 'expDate',title : '发货日期',width : 120,align:'left'}];
billomdeliverexport.endColNamesDtl1 = [
                                  {field : 'total',title : '合计',width : 40,align:'right'}
                            ] ;
billomdeliverexport.sizeTypeFiledName = 'sizeKind';
billomdeliverexport.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billomdeliverexport.brand = {};
billomdeliverexport.initBrand = function(data){
	$('#brandNoCon').combobox({
		data:data,
		valueField:"brandNo",
		textField:"valueAndText",
		panelHeight:150,
	    loadFilter:function(data){
			if(data){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.valueAndText = tempData.brandNo+'→'+tempData.brandName;
	       		 }
	 		}
			return data;
	   }
	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].brandNo] = data[index].brandName;
	}
	billomdeliverexport.brand = temp;
};
billomdeliverexport.store = {};
billomdeliverexport.initStore = function(data){
	
};
//初始化客户信息
billomdeliverexport.initStoreNo = function(){
	$('#storeNoCon').combogrid({
		 panelWidth:450,   
         idField:'storeNo',  
         textField:'textFieldName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/store/list.json?storeType=11',   
         columns:[[  
			{field:'storeNo',title:'客户编码',width:140},  
			{field:'storeName',title:'客户名称',width:140}  
         ]],
         loadFilter:function(data){
        	 if(data && data.rows){
        		 for(var i = 0;i<data.rows.length;i++){
        			 var tempData = data.rows[i];
        			 tempData.textFieldName = tempData.storeNo+'→'+tempData.storeName;
        		 }
        	 }
     		 return data;
         }
	});
};
billomdeliverexport.searchClear = function(id){
	$('#searchForm').form("clear");
	$('#brandNoCon').combobox("loadData",[]);
	$('#seasonNameCondition').combobox("loadData",[]);
	$('#genderNameCondition').combobox("loadData",[]);
	$('#cateTwoCondition').combobox("loadData",[]);
	$('#cateThreeCondition').combobox("loadData",[]);
};
billomdeliverexport.search = function(){
	var starttime = $("#startExpDateCon").datebox('getValue');
	var endTime = $("#endExpDateCon").datebox('getValue');
	if(starttime==""){
		alert("请选择开始日期");
		return;
	}
	if(endTime==""){
		alert("请选择结束日期");
		return;
	}
	if(isStartEndDate(starttime,endTime)){
		var validate = $('#searchForm').form('validate');
	if(!validate){
		return;
	}
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_deliver_export/list.json?locno='+billomdeliverexport.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	if(reqParam.sysNo == null || reqParam.sysNo == ''){
		alert("请选择品牌库!");
		 return 
	}
	var resultColumns = billomdeliverexport.getColumnInfo(reqParam);
	if(resultColumns==null){
		 alert("没有查询到数据",1);
		 $("#dataGridJG").datagrid({
			 queryParams:reqParam,
			 url:queryMxURL,
			 columns:{}
	    });
		 return;
	}
	$("#dataGridJG").datagrid({
		 queryParams:reqParam,
		 url:queryMxURL,
      columns:resultColumns.returnCols
  });
	/*var tempObj = $('#dataGridJG');
	
	
    tempObj.datagrid( 'options' ).queryParams = reqParam;
    tempObj.datagrid( 'options' ).url = queryMxURL;
    tempObj.datagrid( 'load' );*/
    }else{
    	alert("结束时间不能小于开始时间");
    }
	
};
billomdeliverexport.setColumns = function(){
	var sizeCols = [];
	billomdeliverexport.ajaxRequest(BasePath+'/bill_om_deliver_export/get_size?locno='+billomdeliverexport.user.locno,{},false,function(r){sizeCols=r;});
	var colLen = 9;
	if(sizeCols && sizeCols != null && sizeCols.length > 0){
		colLen += sizeCols.length;
	}
	var temp = new Array(colLen);
	if(colLen > 9){
		temp = [{field : 'brandName',title : '品牌',width : 80,align:'left'},
				{field : 'expNo',title : '发货通知单',width : 160,align:'left'},
			 	{field : 'storeNo',title : '客户编码',width : 80,align:'left'},
			 	{field : 'storeName',title : '客户名称',width : 200,align:'left'},
			 	{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
			 	{field : 'itemName',title : '商品名称',width : 200,align:'left'},
			 	{field : 'cateName',title : '商品类别',width : 80,align:'left'},
			 	{field : 'expDate',title : '发货日期',width : 120,align:'left'}];
		for(var i=6;i<colLen-1;i++){
			temp[i] = {field : sizeCols[i-6].sizeNo,title : sizeCols[i-6].sizeCode,width : 40,align:'right'};
		}
		temp[colLen-1] = {field : 'total',title : '合计',width : 60,align:'right'};
	}else{
		temp = [{field : 'brandName',title : '品牌',width : 80,align:'left'},
			 	{field : 'expNo',title : '发货通知单',width : 160,align:'left'},
			 	{field : 'storeNo',title : '客户编码',width : 80,align:'left'},
			 	{field : 'storeName',title : '客户名称',width : 200,align:'left'},
			 	{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
			 	{field : 'itemName',title : '商品名称',width : 200,align:'left'},
			 	{field : 'cateName',title : '商品类别',width : 80,align:'left'},
			 	{field : 'expDate',title : '发货日期',width : 120,align:'left'},
			 	{field : 'total',title : '合计',width : 60,align:'right'}
			 	];
	}
	
	//billomdeliverexport.columns = temp;
	$("#dataGridJG").datagrid({
		columns:[temp]
	});
};
//function exportExcelBill4Detail(dataGridId,preColNames,endColNames,sizeTypeFiledName,excelTitle){
//	var url=BasePath+'/bill_om_deliver_export/do_export4Detail';
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
//				param.locno=billomdeliverexport.user.locno;
//				param.sysNo=$("#sysNoCon").combobox("getValue");
//				param.brandNo=$("#brandNoCon").combobox("getValue");
//				param.startExpDate=$("#startExpDateCon").datebox('getValue');
//				param.endExpDate=$("#endExpDateCon").datebox('getValue');
//				
//				param.storeNo=$("#storeNoCon").combobox("getValue");
//				param.itemNo=$("#itemNoCon").val();
//				param.barcode=$("#barcodeCon").val();
//				param.itemName=$("#itemNameCon").val();
//				param.expNo=$("#expNoCon").val();
//				
//				param.cateOne=$("#cateOneCondition").combobox("getValue");
//				param.cateTwo=$("#cateTwoCondition").combobox("getValue");
//				param.cateThree=$("#cateThreeCondition").combobox("getValue");
//				param.yearsName=$("#yearsNameCondition").val();
//				param.seasonName=$("#seasonNameCondition").val();
//				param.genderName=$("#genderNameCondition").val();
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
function export4Size(dgId,preColNames,endColNames,fileName){
	
	var url=BasePath+'/bill_om_deliver_export/do_export3';
	
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
				
				param.locno=billomdeliverexport.user.locno;
				param.sysNo=$("#sysNoCon").combobox("getValue");
				param.brandNo=$("#brandNoCon").combobox("getValue");
				param.startExpDate=$("#startExpDateCon").datebox('getValue');
				param.endExpDate=$("#endExpDateCon").datebox('getValue');
				
				param.storeNo=$("#storeNoCon").combobox("getValue");
				param.yearsName=$("#yearsNameCondition").val();
				param.seasonName=$("#seasonNameCondition").combobox("getValues");
				param.genderName=$("#genderNameCondition").combobox("getValues");
				
				param.expNo=$("#expNoCon").val();
				param.cateOne=$("#cateOneCondition").combobox("getValue");
				param.cateTwo=$("#cateTwoCondition").combobox("getValue");
				param.cateThree=$("#cateThreeCondition").combobox("getValue");
				
				param.itemNo=$("#itemNoCon").val();
				param.barcode=$("#barcodeCon").val();
				param.itemName=$("#itemNameCon").val();
				
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
billomdeliverexport.do_export = function(){
	export4Size("dataGridJG", billomdeliverexport.preColNamesDtl1, billomdeliverexport.endColNamesDtl1, "出库查询");
//	exportExcelBill4Detail('dataGridJG',billomdeliverexport.preColNamesDtl1,
//			billomdeliverexport.endColNamesDtl1, billomdeliverexport.sizeTypeFiledName,"出库查询");
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
$(document).ready(function(){
	billomdeliverexport.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(user){billomdeliverexport.user=user;});
	//billomdeliverexport.setColumns(["235"]);
	billomdeliverexport.initStoreNo();
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoCon'),"brandNoObj":$('input[id=brandNoCon]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs,[false]);
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[0] != null){
		   				billomdeliverexport.footer1 = data.footer[1];
		   			}else{
		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
			   			rows[0] = billomdeliverexport.footer1;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	$("#startExpDateCon").datebox('setValue',billomdeliverexport.getDate(-6));
	$("#endExpDateCon").datebox('setValue',billomdeliverexport.getDate(0));
	//初始化商品类别
//	wms_city_common.comboboxLoadFilter(
//			["cateNoCon"],
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
	//初始化季节、性别
	billomdeliverexport.initGenderAndSeason('#sysNoCon');
});

billomdeliverexport.initGenderAndSeason =  function(sysNoId){
	$(sysNoId).combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonNameCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderNameCondition',true,false);
		}
	});
};

/**
 * 获取动态表头
 */
billomdeliverexport.getColumnInfo = function(params){
	var beforeColArr = billomdeliverexport.preColNamesDtl1;
	var afterColArr = billomdeliverexport.endColNamesDtl1; 
	var tempUrl = BasePath+'/bill_om_deliver_export/initHead';
	params.locno=billomdeliverexport.user.locno;
    params.preColNames = JSON.stringify(beforeColArr);
    params.endColNames = JSON.stringify(afterColArr);
    params.sizeTypeFiledName = billomdeliverexport.sizeTypeFiledName;
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
billomdeliverexport.getDate = function(n){
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