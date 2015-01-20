/************颜色管理
 * author:liutao
 * creattime:2013.08.07
 * modify:
***********/

var sizeinfo = {};
sizeinfo.sizeCode = '0';
sizeinfo.sysNo = '0';


//查询供应商信息
sizeinfo.searchSizeinfo = function(){
	var starttime = $("#createtmBeginCondition").datebox('getValue');
	var endTime = $("#createtmEndCondition").datebox('getValue');
	var starttime1 = $("#edittmBeginCondition").datebox('getValue');
	var endTime1 = $("#edittmEndCondition").datebox('getValue');
	if(isStartEndDate(starttime,endTime)){
		if(isStartEndDate(starttime1,endTime1)){
			var fromObjStr=convertArray($('#searchForm').serializeArray());
			var queryMxURL=BasePath+'/size_info/list.json';
		    //3.加载明细
		    $( "#dataGridSI").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
		    $( "#dataGridSI").datagrid( 'options' ).url=queryMxURL;
		    $( "#dataGridSI").datagrid( 'load' );
		}else{
			alert("修改结束时间不能小于修改开始时间");
		}
	}else{
		
		alert("建档结束时间不能小于建档开始时间");
	}
	
    
};

//清楚查询条件
sizeinfo.searchSizeinfoClear = function(){
	$('#searchForm').form("clear");
	$('#sizeKind2Condition').numberbox('setValue', "");
	$('#hcolNoCondition').numberbox('setValue', "");
};

sizeinfo.getCurrentUser = function(){
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

sizeinfo.checkExistFun = function(url,checkColumnJsonData){
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

sizeinfo.insertRowSizeInfo = function(gid){
	sizeinfo.insertRowAtEnd(gid);
};

sizeinfo.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
};

sizeinfo.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};

sizeinfo.endEdit = function(gid){
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

sizeinfo.save = function(){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")
	//是不能获取到正在编辑状态的那一行的
	var tempFlag = sizeinfo.endEdit('dataGridSI');
	if(!tempFlag){
		alert('数据验证没有通过!',1);
		return;
	}
	
	var tempObj = $('#dataGridSI');
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动!',1);
		return;
	}
	var currentUser = sizeinfo.getCurrentUser();
	var inserted = tempObj.datagrid('getChanges', "inserted");
    if(inserted.length>0){
        var checkUrl=BasePath+'/size_info/get_count.json';
        var checkData={};
        for (var i = 0; i < inserted.length; i++) {
        	checkData.sizeNo = inserted[i]['sizeNo'];
        	checkData.sizeKind = inserted[i]['sizeKind'];
        	checkData.sizeKind2 = inserted[i]['sizeKind2'];
            //验证数据库是否存在相应编码
            if(sizeinfo.checkExistFun(checkUrl,checkData)){
        		alert('编码不能重复!',2);
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
    $.post(BasePath+'/size_info/save', effectRow, function(result) {
        if(result.success){
            alert('保存成功!',1);
            tempObj.datagrid('acceptChanges');
        }
    }, "JSON").error(function() {
    	alert('保存失败!',1);
    });
};

sizeinfo.exportExcel=function(){
	exportExcelBaseInfo('dataGridSI','/size_info/do_export.htm','尺寸管理信息导出');
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

sizeinfo.initBrand = function(){
	$('#brandCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
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
$(document).ready(function(){
	//sizeinfo.initBrand();
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=brandCondition]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
});