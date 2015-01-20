
var billstatuslog = {};
billstatuslog.user = {};
billstatuslog.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billstatuslog.billType = {};
billstatuslog.billTypeFormatter = function(value, rowData, rowIndex){
	return billstatuslog.billType[value];
};
billstatuslog.initBillType = function(data){
	$('#billTypeCon').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:100
	}).combobox("select",data[0].itemvalue);
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billstatuslog.billType = temp;
};
//清除查询条件
billstatuslog.searchClear = function(){
	$('#searchForm').form("clear");
};
//查询信息
billstatuslog.search = function(){
	
	
	var starttime = $("#operatetmBeginCon").datebox('getValue');
	var endTime = $("#operatetmEndCon").datebox('getValue');
	if(isStartEndDate(starttime,endTime)){
		var fromObj=$('#searchForm');
	    //1.校验必填项
	    var validateForm= fromObj.form('validate');
	    if(validateForm==false){
	         return ;
	    }
		var fromObjStr=convertArray($('#searchForm').serializeArray());
		var queryMxURL=BasePath+'/bill_status_log/list_any.json?locno='+billstatuslog.user.locno;
	    //3.加载明细
	    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
	    $( "#dataGridJG").datagrid( 'load' );
	}else{
		alert("结束时间不能小于开始时间");
	}
	
    
};
//校验开始日期和结束日期的大小
function isStartEndDate(startDate,endDate){
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        //var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allStartDate = new Date(startDate);   
        //var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        var allEndDate = new Date(endDate);   
        if(allStartDate.getTime()>allEndDate.getTime()){   
             return false;   
        }   
     }   
     return true;   
} 

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
//JS初始化执行
$(document).ready(function(){
	billstatuslog.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){billstatuslog.user=u;});
	billstatuslog.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_STATUSLOG_TYPE',{},false,billstatuslog.initBillType);
});
