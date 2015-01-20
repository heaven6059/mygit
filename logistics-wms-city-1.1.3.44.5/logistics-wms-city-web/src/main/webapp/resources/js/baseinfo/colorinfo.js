/****颜色管理
 * author:liutao
 * creattime:2013.08.07
 * modify:
***********/

var colorinfo = {};

colorinfo.getCurrentUser = function(){
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

//新增一行
colorinfo.insertRow = function(gid){
	var dgObj = $('#'+gid);
	dgObj.datagrid('appendRow',{});
	var rowIndex = dgObj.datagrid('getRows').length - 1;
	dgObj.datagrid('beginEdit', rowIndex);
	//新增行，获取焦点
	var ed = dgObj.datagrid('getEditor', {index:rowIndex,field:'colorNo'});
	$(ed.target).focus();
};

//删除选中行
colorinfo.deleteRow = function(gid){
	var dgObj = $('#'+gid);
	var rowObj = dgObj.datagrid('getSelected');
	if(rowObj){
		var rowIndex = dgObj.datagrid('getRowIndex',rowObj);
		dgObj.datagrid('deleteRow',rowIndex);
	}
};

//保存当前所有修改
colorinfo.saveAllChanges = function(gid){
	var dgObj = $('#'+gid);
	
	//检查必录项是否完全录入
	var tempFlag = colorinfo.checkAllRequired(dgObj);
	if(!tempFlag){
		return;
	}
	
	//判断当前表格是否存在修改
	var chsize = dgObj.datagrid('getChanges').length;
	if(chsize<1){
		alert("没有改动数据",1);
		return;
	}
	
	var currentUser = colorinfo.getCurrentUser();
	//检查主键、唯一性索引冲突
	var inserted = dgObj.datagrid('getChanges', "inserted");
    if(inserted.length>0){
        var checkUrl=BasePath+'/color_info/get_count.json';
        var checkData={};
        for (var i = 0; i < inserted.length; i++) {
        	checkData.colorNo = inserted[i]['colorNo'];
        	
            //验证数据库是否存在相应编码
            if(colorinfo.checkIsUnique(checkUrl,checkData)){
        		alert('[颜色编码]不能重复!',1);
        		return;
    	    }
            inserted[i].createtm = currentUser.currentDate19Str;
            inserted[i].creator = currentUser.loginName;
            inserted[i].editor = currentUser.loginName;
            inserted[i].edittm = currentUser.currentDate19Str;
        }
    }
    
    //获取当前修改
    var deleted = dgObj.datagrid('getChanges', "deleted");
    var updated = dgObj.datagrid('getChanges', "updated");
    if(updated.length>0){
        for (var i = 0; i < updated.length; i++) {
        	updated[i].editor = currentUser.loginName;
        	updated[i].edittm = currentUser.currentDate19Str;
        }
    }
    var effectRow = {
    		inserted:JSON.stringify(inserted),
    		deleted:JSON.stringify(deleted),
    		updated:JSON.stringify(updated)
    };
    
    //提交当前修改
    $.post(BasePath+'/color_info/save', effectRow, function(result) {
        if(result.success){
            alert('保存成功!',1);
            tempObj.datagrid('acceptChanges');
        }
    }, "JSON").error(function() {
    	alert('保存失败!',2);
    });
};

colorinfo.checkAllRequired = function(dgObj){
	//逐行验证数据，结束编辑行
	var rowArr = dgObj.datagrid('getRows');
	for(var i = 0; i < rowArr.length; i++){
		var flag = dgObj.datagrid('validateRow', i);
		
		if(flag){
			dgObj.datagrid('endEdit', i);
		}else{
			alert("第[" + (i+1) + "]行存在必录项没有录入！");
			return false;
		}
	}
	return true;
};

colorinfo.checkIsUnique = function(url,checkColumnJsonData){
	var isUnique=false;
 	$.ajax({
		  type: 'POST',
		  url: url,
		  data: checkColumnJsonData,
		  cache: true,
		  async:false, // 一定要
		  success: function(totalData){
			  totalData = parseInt(totalData,10);
			  if(totalData>0){
				  isUnique=true;
			  }
		  }
     });
 	return isUnique;
};
colorinfo.initBrand = function(){
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });
};
colorinfo.searchClear = function(){
	$('#searchForm').form("clear");
	colorinfo.searchColor();
};
colorinfo.searchColor = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/color_info/list.json';
    $( "#dataGridCI").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridCI").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridCI").datagrid( 'load' );
};
//导出
exportExcel=function(){

	exportExcelBaseInfo('dataGridCI','/color_info/do_export.htm','颜色管理信息导出');
};
$(document).ready(function(){
	//colorinfo.initBrand();
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNoCondition]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
});
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
