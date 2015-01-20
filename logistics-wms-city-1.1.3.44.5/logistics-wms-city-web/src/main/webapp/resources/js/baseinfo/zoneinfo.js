var zoneinfo = {};


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
zoneinfo.searchDate = function(){
	var createtmBegin =  $('#createtmBeginCondition').datebox('getValue');
	var createtmEnd =  $('#createtmEndCondition').datebox('getValue');
	if(createtmBegin != '' && createtmEnd != ''){
		if(!isStartEndDate(createtmBegin,createtmEnd)){    
			alert("建档开始时间不能大于结束时间");   
	        return false;   
	    }
	}
	var edittmBegin =  $('#edittmBeginCondition').datebox('getValue');
	var edittmEnd =  $('#edittmEndCondition').datebox('getValue');
	if(edittmBegin != '' && edittmEnd != ''){
		if(!isStartEndDate(edittmBegin,edittmEnd)){    
			alert("修改开始时间不能大于结束时间");   
	        return false;   
	    }
	}
	return true;
};
//查询区域信息
zoneinfo.searchZoneinfo = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/zone_info/list.json';
	if(!zoneinfo.searchDate()){
		return;
	}
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清楚查询条件
zoneinfo.searchZoneinfoClear = function(){
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
zoneinfo.exportExcel = function(){
	exportExcelBaseInfo('dataGridJG','/zone_info/do_export.htm','区域管理信息导出');
};

zoneinfo.getCurrentUser = function(){
	var currentUser = null;
 	$.ajax({
		  type: 'POST',
		  url: BasePath+'/initCache/getCurrentUser',
		  data: {},
		  cache: true,
		  async:false, // 一定要
		  success: function(resultData){
			  currentUser = resultData;
		  }
     });
 	return currentUser;
};

zoneinfo.checkExistFun = function(url,checkColumnJsonData){
	var checkExist=false;
 	$.ajax({
		  type: 'POST',
		  url: url,
		  data: checkColumnJsonData,
		  cache: true,
		  async:false, // 一定要
		  success: function(totalData){
			  totalData = parseInt(totalData,10);
			  if(totalData>0){
				  checkExist=true;
			  }
		  }
     });
 	return checkExist;
};

zoneinfo.insertRowZoneInfo = function(gid){
	zoneinfo.insertRowAtEnd(gid);
};

zoneinfo.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
};

zoneinfo.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};

zoneinfo.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		tempObj.datagrid('endEdit', i);
    	}else{
    		return false;
    	}
    }
    return true;
};

zoneinfo.save = function(){
	
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = zoneinfo.endEdit('dataGridJG');
	if(!tempFlag){
		alert('数据验证没有通过!',1);
		return;
	}
	
	var tempObj = $('#dataGridJG');
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动!',1);
		return;
	}
	var currentUser = zoneinfo.getCurrentUser();
	var inserted = tempObj.datagrid('getChanges', "inserted");
    if(inserted.length>0){
        var checkUrl=BasePath+'/zone_info/get_count.json';
        var checkData={};
        for (var i = 0; i < inserted.length; i++) {
        	checkData.zoneNo = inserted[i]['zoneNo'];
            if(zoneinfo.checkExistFun(checkUrl,checkData)){
        		alert('编码不能重复!',1);
        		return;
    	    }
            inserted[i].createtm = currentUser.currentDate19Str;
            inserted[i].creator = currentUser.loginName;
            inserted[i].editor = currentUser.loginName;
            inserted[i].edittm = currentUser.currentDate19Str;
        }
    }
    var deleted = tempObj.datagrid('getChanges', "deleted");
    var updated = tempObj.datagrid('getChanges', "updated");
    if(updated.length>0){
        for (var i = 0; i < updated.length; i++) {
        	updated[i].editor = currentUser.loginName;
        	updated[i].edittm = currentUser.currentDate19Str;
        }
    }
    var effectRow = {
    		inserted:JSON.stringify(inserted),
    		deleted:JSON.stringify(deleted),
    		updated:JSON.stringify(updated),
    };
    $.post(BasePath+'/zone_info/save', effectRow, function(result) {
        if(result.success){
            alert('保存成功!',1);
            tempObj.datagrid('acceptChanges');
        }
    }, "JSON").error(function() {
    	alert('保存失败!',1);
    });
};
zoneinfo.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
zoneinfo.initZoneInfoForSearch = function(data){
	$('#zoneNoCondition').combobox({
	    data:data,
	    valueField:'zoneNo',    
	    textField:'zoneNoForSearch',
	    panelHeight:"auto",
	    loadFilter:function(data){
	    	if(data){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.zoneNoForSearch = tempData.zoneNo+'→'+tempData.zoneName;
	       		 }
	       	 }
	    	return data;
	    }
	});
	$('#zoneCodeCondition').combobox({
		data:data,
		valueField:'zoneCode',    
		textField:'zoneCodeForSearch',
		panelHeight:"auto",
		loadFilter:function(data){
			if(data){
				for(var i = 0;i<data.length;i++){
					var tempData = data[i];
					tempData.zoneCodeForSearch = tempData.zoneCode+'→'+tempData.zoneName;
				}
			}
			return data;
		}
	});
};
$(document).ready(function(){
	//zoneinfo.ajaxRequest(BasePath+'/zone_info/get_biz',{},false,zoneinfo.initZoneInfoForSearch);
	wms_city_common.comboboxLoadFilter(
			["zoneNoCondition"],
			'zoneNo',
			'zoneName',
			'valueAndText',
			false,
			[true],
			BasePath+'/zone_info/get_biz',
			{},
			null,
			null);
	wms_city_common.comboboxLoadFilter(
			["zoneCodeCondition"],
			'zoneCode',
			'zoneName',
			'valueAndText',
			false,
			[true],
			BasePath+'/zone_info/get_biz',
			{},
			null,
			null);
});