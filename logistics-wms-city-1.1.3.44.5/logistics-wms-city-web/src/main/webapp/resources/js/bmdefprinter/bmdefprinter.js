var bmdefprinter = {};
bmdefprinter.user;
//委托业主状态
bmdefprinter.statusData = {};
bmdefprinter.statusDataFormatter = function(value, rowData, rowIndex){
	return bmdefprinter.statusData[value];
};

bmdefprinter.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  type: 'POST',
		  async:async,
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

bmdefprinter.openUI = function(opt){
	$('#openUI').window({
		title:opt,
		close:false,
		onBeforeClose:function(){
			$(".tooltip-content").remove();
		} 
	});
	$("#info_save").show();
	$('#openUI').window('open');
};
bmdefprinter.closeUI = function(opt){
	$('#openUI').window('close');
	$("#opt").val("");
};
bmdefprinter.addUI = function(opt){
	bmdefprinter.openUI("新增");
	bmdefprinter.clearAll();
	$("#ownerNo").attr('readOnly',false);
	$("#opt").val("add");
	
};
bmdefprinter.manage = function(opt){
	var opt = $("#opt").val();
	if(opt=="add"){
		bmdefprinter.save();
	}else{
		bmdefprinter.update();
	}
};
bmdefprinter.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bm_defprinter/list.json?locno='+bmdefprinter.user.locno,'pageNumber':1 });
};

bmdefprinter.editUI = function(){
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
		bmdefprinter.openUI("修改");
		$('#dataForm').form('load',checkedRows[0]);
		$("#printerNo").attr('readOnly',true);
	}
};

bmdefprinter.loadDetail = function(rowData){
	$("#opt").val("");
	bmdefprinter.openUI("详情");
	$('#dataForm').form('load',rowData);
	$("#info_save").hide();
	$("#ownerNo").attr('readOnly',true);
};

bmdefprinter.checkExistFun = function(url,checkColumnJsonData){
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

bmdefprinter.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/bm_defprinter/get_count.json';
         var checkDataNo={"printerNo":$("#printerNo").val(),"locno":bmdefprinter.user.locno};
	     if(bmdefprinter.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('打印机编号已存在,不能重复!',1);
	    	  $("#ownerNo").focus();
	    	  return;
	      }
		  var moduleid = $("#moduleid").val();
		  var reg =/^[0-9a-zA-Z]+$/;
		  if(!reg.test(moduleid)){
			  alert('模块编号只能是数字或者字母!',1);
			  return;
		  }
	     
		 //3. 保存
         var url = BasePath+'/bm_defprinter/post';
         fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					param.createtm = bmdefprinter.user.currentDate19Str;
					param.creator = bmdefprinter.user.loginName;
					param.editor = bmdefprinter.user.loginName;
					param.edittm = bmdefprinter.user.currentDate19Str;
					param.locno = bmdefprinter.user.locno;
				},
				success: function(){
					 alert('新增成功!');
					 //4.保存成功,清空表单
					 bmdefprinter.loadDataGrid();
					 bmdefprinter.closeUI();
					 return;
			    },
				error:function(){
					alert('新增失败,请联系管理员!',2);
				}
		   });
};

bmdefprinter.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    var moduleid = $("#moduleid").val();
	var reg =/^[0-9a-zA-Z]+$/;
	  if(!reg.test(moduleid)){
		  alert('模块编号只能是数字或者字母!',1);
		  return;
	  }
     //2.绑定数据
    var url = BasePath+'/bm_defprinter/put';
    fromObj.form('submit', {
		url: url,
		onSubmit: function(param){
			param.editor = bmdefprinter.user.loginName;
			param.edittm = bmdefprinter.user.currentDate19Str;
			param.locno = bmdefprinter.user.locno;
		},
		success: function(){
			 alert('修改成功!');
			 //3.保存成功,清空表单
			 bmdefprinter.loadDataGrid();
			bmdefprinter.closeUI();
			 return;
	    },
		error:function(){
			alert('修改失败,请联系管理员!',2);
		}
   });
};

bmdefprinter.deleteRows = function(){
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keys = [];
	$.each(checkedRows, function(index, item){
		keys.push(bmdefprinter.user.locno+"-"+item.printerNo);
	});         
	 
     //2.绑定数据
     var url = BasePath+'/bm_defprinter/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };
	 //3. 删除
	 $.messager.confirm('确认删除', '您确定要删除吗？', function(r){
		 if (r){
				 bmdefprinter.ajaxRequest(url,data,true,function(result){
					 if(result=='success'){
						 //4.删除成功,清空表单
						 bmdefprinter.loadDataGrid();
						 alert('删除成功!');
					 }else{
						 alert('删除失败,请联系管理员!',2);
					 }
				}); 
			}
	 });
	
};

bmdefprinter.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};


bmdefprinter.searchClear = function(){
	$('#searchForm').form("clear");
	 $("#searchForm input[name=locno]").val(bmdefprinter.user.locno);
	searchForm('/bm_defprinter/list.json');
};
bmdefprinter.initCreateFlag = function(data){
	$('#createFlag').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CREATE_FLAG',
	     valueField:"itemvalue",
	     textField:"itemname",
	     data:data,
	     panelHeight:"auto"
	  });
};
bmdefprinter.initStatus = function(data){
	$('#status').combobox({
	     valueField:"itemvalue",
	     textField:"itemname",
	     data:data,
	     panelHeight:"auto",
	  });
	bmdefprinter.statusData = bmdefprinter.tansforArrayToMap(data);
};
bmdefprinter.tansforArrayToMap = function(oriData){
	var str = "";
	for(var i = 0,length = oriData.length;i<length;i++){
		str = str+"\""+oriData[i].itemvalue +"\":\""+oriData[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};
bmdefprinter.printerType={};
bmdefprinter.printerTypeFormatter = function(value,row,index){
	return bmdefprinter.printerType[value];
};
bmdefprinter.status = {};
bmdefprinter.statusFormatter = function(value,row,index){
	return bmdefprinter.status[value];
};
$(document).ready(function(){
	 bmdefprinter.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){
		 bmdefprinter.user=u;
		 $("#searchForm input[name=locno]").val(u.locno);
		 });
	bmdefprinter.loadDataGrid();
	bmdefprinter.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CREATE_FLAG',{},bmdefprinter.initCreateFlag);
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["status"],
			null,
			null,
			null,
			true,
			[false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=PRINT_STATUS',
			{},
			bmdefprinter.status,
			null);
	//初始打印类型
	wms_city_common.comboboxLoadFilter(
			["printerTypeCondition","printerType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=PRINT_TYPE',
			{},
			bmdefprinter.printerType,
			null);
});


bmdefprinter.searchForm = function(){
	var itemSearchFormArr = $('#searchForm').serializeArray();
	var fromObjStr=convertArray(itemSearchFormArr);
	var queryMxURL=BasePath+'/bm_defprinter/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

