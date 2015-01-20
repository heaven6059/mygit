var bmdefowner = {};

//委托业主状态
bmdefowner.statusData = {};
bmdefowner.statusDataFormatter = function(value, rowData, rowIndex){
	return bmdefowner.statusData[value];
};

bmdefowner.exportExcel = function(){
	exportExcelBaseInfo('dataGridJG','/entrust_owner/do_export.htm?businessType=9','委托业主管理信息导出');
};

bmdefowner.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

bmdefowner.openUI = function(opt){
	$('#openUI').window({
		title:opt
	});
	$("#info_save").show();
	$('#openUI').window('open');
};
bmdefowner.closeUI = function(opt){
	$('#openUI').window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};
bmdefowner.addUI = function(opt){
	bmdefowner.openUI("新增");
	bmdefowner.clearAll();
	$("#ownerNo").attr('readOnly',false);
	$("#opt").val("add");
	$("#createFlag").combobox('select', '0');
	$("#createFlag_div").hide();
};
bmdefowner.manage = function(opt){
	var opt = $("#opt").val();
	if(opt=="add"){
		bmdefowner.save();
	}else{
		bmdefowner.update();
	}
};
bmdefowner.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/entrust_owner/list.json?businessType=9','title':'委托业主列表','pageNumber':1 });
};

bmdefowner.editUI = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		$("#opt").val("");
		bmdefowner.openUI("修改");
		$('#dataForm').form('load',checkedRows[0]);
		$("#ownerNo").attr('readOnly',true);
		$("#info_save").show();
	}
	$("#createFlag_div").hide();
};

bmdefowner.loadDetail = function(rowData){
	$("#opt").val("");
	bmdefowner.openUI("详情");
	$('#dataForm').form('load',rowData);
	$("#info_save").hide();
	$("#ownerNo").attr('readOnly',true);
	$("#createFlag_div").show();
};

bmdefowner.checkExistFun = function(url,checkColumnJsonData){
	console.info(checkColumnJsonData);
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

bmdefowner.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/entrust_owner/get_countValidate.json';
         var checkDataNo={"ownerNo":$("#ownerNo").val()};
	     if(bmdefowner.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('委托业主编号已存在,不能重复!',1);
	    	  $("#ownerNo").focus();
	    	  return;
	      }
			 
		 //3. 保存
	     var saveFn = function(returnData){
	         var url = BasePath+'/entrust_owner/post';
	         fromObj.form('submit', {
					url: url,
					onSubmit: function(param){
						param.createtm = returnData.currentDate19Str;
						param.creator = returnData.loginName;
						param.editor = returnData.loginName;
						param.edittm = returnData.currentDate19Str;
					},
					success: function(){
						 alert('新增成功!');
						 //4.保存成功,清空表单
						 bmdefowner.loadDataGrid();
						 bmdefowner.closeUI();
						 return;
				    },
					error:function(){
						alert('新增失败,请联系管理员!',2);
					}
			   });
	     };
	     bmdefowner.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

bmdefowner.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   
     //2.绑定数据
    var updateFn = function(returnData){
        var url = BasePath+'/entrust_owner/put';
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.editor = returnData.loginName;
				param.edittm = returnData.currentDate19Str;
   			},
   			success: function(){
   				 alert('修改成功!');
   				 //3.保存成功,清空表单
   				 bmdefowner.loadDataGrid();
   				bmdefowner.closeUI();
   				 return;
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	   });
    };
    bmdefowner.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},updateFn);
};

bmdefowner.deleteRows = function(){
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keys = [];
	$.each(checkedRows, function(index, item){
		keys.push(item.ownerNo);
	});         
	 
     //2.绑定数据
     var url = BasePath+'/entrust_owner/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };
	 //3. 删除
	 $.messager.confirm('确认删除', '您确定要删除吗？', function(r){
		 if (r){
				 bmdefowner.ajaxRequest(url,data,function(result,returnMsg){
					 if(returnMsg=='success'){
						 //4.删除成功,清空表单
						 bmdefowner.loadDataGrid();
						 alert('删除成功!');
					 }else{
						 alert('删除失败,请联系管理员!',2);
					 }
				}); 
			}
	 });
	
};

bmdefowner.searchBmdefowner = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/entrust_owner/list.json';
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

bmdefowner.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
bmdefowner.searchClear = function(){
	$('#searchForm').form("clear");
	searchForm('/entrust_owner/list.json');
};
bmdefowner.initCreateFlag = function(data){
	$('#createFlag').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CREATE_FLAG',
	     valueField:"itemvalue",
	     textField:"itemname",
	     data:data,
	     panelHeight:"auto"
	  });
};
bmdefowner.initStatus = function(data){
	$('#status').combobox({
	     valueField:"itemvalue",
	     textField:"itemname",
	     data:data,
	     panelHeight:"auto",
	  });
	bmdefowner.statusData = bmdefowner.tansforArrayToMap(data);
};
bmdefowner.tansforArrayToMap = function(oriData){
	var str = "";
	for(var i = 0,length = oriData.length;i<length;i++){
		str = str+"\""+oriData[i].itemvalue +"\":\""+oriData[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};
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
$(document).ready(function(){
	bmdefowner.loadDataGrid();
	bmdefowner.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=STATUS',{},bmdefowner.initStatus);
	bmdefowner.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CREATE_FLAG',{},bmdefowner.initCreateFlag);
	
});
