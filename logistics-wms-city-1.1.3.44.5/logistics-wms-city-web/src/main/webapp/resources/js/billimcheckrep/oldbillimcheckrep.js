var billimcheckrep = {};
billimcheckrep.preColNamesDtl1 = [
		                   {title:"品牌",field:"brandName",width:80},
		                   {title:"供应商代码",field:"supplierNo",width:80},
		                   {title:"供应商名称",field:"supplierName",width:200},
		                   {title:"商品编码",field:"itemNo",width:150},
		                   {title:"商品名称",field:"itemName",width:200},
		                   {title:"商品类别",field:"cateName",width:70},
		                   {title:"入库日期",field:"reciveDate",width:80},
		                   {title:"预到货通知单号",field:"importNo",width:130}
		                   
                    ];

billimcheckrep.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount",align:'right'}
                        ] ;

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
billimcheckrep.getColumnInfo = function(beforeColArr,afterColArr){
	 var beforeColArr = billimcheckrep.preColNamesDtl1;
	 var afterColArr = billimcheckrep.endColNamesDtl1; 
	var tempUrl = BasePath+'/initCache/getBrandList2';
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        },
        cache: true,
        async: false,
        success: function(returnData){
         	$("#dataGridJG").datagrid({
                columns:returnData.returnCols
            }); 
		}
      });
};

/**
 * 查询
 */
billimcheckrep.searchBillImCheckRep = function(){
	var starttime = $("#startTime").datebox('getValue');
	var endTime = $("#endTime").datebox('getValue');
	if(isStartEndDate(starttime,endTime)){
		var fromObjStr=convertArray($('#searchForm').serializeArray());
		var queryMxURL=BasePath+'/bill_im_check_rep/createBillCheckInRep';
	    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
	    $( "#dataGridJG").datagrid( 'load' );
	}else{
		alert("结束时间不能小于开始时间");
	}
	
};

/**
 * 清空查询条件
 */

billimcheckrep.clearForm = function(){
	$("#searchForm").form('clear');
};

/**
 * 初始化品牌
 */
billimcheckrep.initBrand = function(data){
	$('#brandNameId').combobox({
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
};
/**
 * 初始化商品类别
 */
billimcheckrep.initCate = function(data){
	$("#itemCateId").combobox({
		data:data,
		valueField:"cateNo",
		textField:"valueAndText",
	    panelHeight:150,
	    loadFilter:function(data){
			if(data){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.valueAndText = tempData.cateNo+'→'+tempData.cateName;
	       		 }
	 		}
			return data;
	   }
	});
};
/**
 * 初始化供应商
 */
billimcheckrep.initSupplier = function(data){
	$("#supplierNameId").combobox({
		data:data,
		valueField:"supplierNo",
		textField:"valueAndText",
	    panelHeight:150,
	    loadFilter:function(data){
			if(data){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.valueAndText = tempData.supplierNo+'→'+tempData.supplierName;
	       		 }
	 		}
			return data;
	   }
	});
};
/**
 * 导出excel
 */
exportExcel=function(){
	exportExcelRepInfo('dataGridJG','/bill_im_check_rep/do_export2','入库查询导出');
};


/**
 * 初始化开始
 */
$(document).ready(function(){
	billimcheckrep.ajaxRequest(BasePath+'/category/get_biz',{},billimcheckrep.initCate);
	billimcheckrep.ajaxRequest(BasePath+'/supplier/get_biz',{},billimcheckrep.initSupplier);
	billimcheckrep.ajaxRequest(BasePath+'/brand/get_biz',{},billimcheckrep.initBrand);
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1] != null){
		   				billimcheckrep.footer1 = data.footer[1];
		   			}else{
		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
			   			rows[1] = billimcheckrep.footer1;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	
});