var authority_module = {};

authority_module.checkExistFun = function(url,checkColumnJsonData){
	var checkExist=false;
 	$.ajax({
		  type: 'POST',
		  url: url,
		  data: checkColumnJsonData,
		  cache: true,
		  async:false, // 一定要
		  success: function(data){
			  if(data.success){
				  checkExist=true;
			  }
		  }
     });
 	return checkExist;
};

authority_module.insertRowauthority_module = function(gid){
	authority_module.insertRowAtEnd(gid);
};

authority_module.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
};

authority_module.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};

authority_module.endEdit = function(gid){
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

authority_module.save = function(id){
	
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = authority_module.endEdit(id);
	if(!tempFlag){
		alert('数据验证没有通过!',1);
		return;
	}
	
	var tempObj = $('#'+id);
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动!',1);
		return;
	}
	var inserted = tempObj.datagrid('getChanges', "inserted");
    if(inserted.length>0){
        var checkUrl=BasePath+'/authority_module/get_is_exist.json';
        var checkData={};
        for (var i = 0; i < inserted.length; i++) {
        	checkData.moduleName = inserted[i]['name'];
            if(authority_module.checkExistFun(checkUrl,checkData)){
        		alert('名称已经存在!',1);
        		return;
    	    }
        }
    }
    var deleted = tempObj.datagrid('getChanges', "deleted");
    var updated = tempObj.datagrid('getChanges', "updated");
    var effectRow = {
    		inserted:JSON.stringify(inserted),
    		deleted:JSON.stringify(deleted),
    		updated:JSON.stringify(updated),
    };
    $.post(BasePath+'/authority_module/save', effectRow, function(result) {
        if(result.success){
            alert('保存成功!',1);
            tempObj.datagrid('acceptChanges');
        }
    }, "JSON").error(function() {
    	alert('保存失败!',1);
    });
};