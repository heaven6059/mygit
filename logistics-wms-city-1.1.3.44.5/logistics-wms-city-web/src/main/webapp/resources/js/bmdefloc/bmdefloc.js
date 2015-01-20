var bmdefloc = {};

bmdefloc.createFlagData = [{    
    "label":"0",
    "text":"手建", 
    "value":"0→手建",   
},{    
    "label":"1",    
    "text":"系统下发", 
    "value":"1→系统下发"   
}];

bmdefloc.initStatus = function(){
	$('#createFlag').combobox({
	     valueField:"label",
	     textField:"value",
	     data:bmdefloc.createFlagData,
	     panelHeight:"auto"
	});
	
	$('#createFlagCondition').combobox({
	     valueField:"label",
	     textField:"value",
	     data:bmdefloc.createFlagData,
	     panelHeight:"auto",
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 tempData[tempData.length] = {label:'',value:'全选'};
	    	 for(var i=0;i<data.length;i++){
	    		tempData[tempData.length] = data[i];
	    	 }
	    	 return tempData;
	     }
	 });
};

bmdefloc.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

bmdefloc.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bmdefloc/list.json','title':'仓库列表','pageNumber':1 });
};

bmdefloc.loadDetail = function(locno){
	$('#dataForm').form('load',BasePath+'/bmdefloc/get?locno='+locno);
	$("#locno").attr('readOnly',true);
};

bmdefloc.checkExistFun = function(url,checkColumnJsonData){
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

//查询仓库信息
bmdefloc.searchLoc = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bmdefloc/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清楚查询条件
bmdefloc.searchLocClear = function(){
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

bmdefloc.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/bmdefloc/get_count.json';
         var checkDataNo={"brandNo":$("#brandNo").val()};
	     if(brand.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('品牌编码已存在,不能重复!',1);
	    	  $("#brandNo").focus();
	    	  return;
	      }
			 
		 //3. 保存
	     var saveFn = function(returnData){
	         var url = BasePath+'/brand/post';
	         fromObj.form('submit', {
					url: url,
					onSubmit: function(param){
						param.brandNoHead = '0';
						param.sysNo = '0';
						param.createtm = returnData.currentDate19Str;
						param.creator = returnData.loginName;
						param.editor = returnData.loginName;
						param.edittm = returnData.currentDate19Str;
					},
					success: function(){
						 alert('新增成功!');
						 //4.保存成功,清空表单
						 brand.loadDataGrid();
						 brand.clearAll();
						 return;
				    },
					error:function(){
						alert('新增失败,请联系管理员!',2);
					}
			   });
	     };
	     bmdefloc.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

//修改
bmdefloc.modifyFloc = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    var url = BasePath+'/bmdefloc/modifyFloc';
    fromObj.form('submit', {
   			url: url,
   			onSubmit: function(){
				
   			},
   			success: function(data){
   				if(data=="success"){
   					alert('修改成功!');
					$("#showDialog").window('close'); 
					bmdefloc.loadDataGrid();
					//bmdefloc.clearAll();
					return;
				}else{
					alert('修改失败,请联系管理员!',2);
				}
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	});
    
};

//获取明细信息
bmdefloc.setDetail = function(rowData){
	//var fromObj=$('#dataForm');
	//$("input[id=locno]",fromObj).val(rowData.locno);
	$("#locno").val(rowData.locno);
	$("#locname").val(rowData.locname);
	$("#memo").val(rowData.memo);
	$('#createFlag').combobox("select",rowData.createFlag);
	//$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
	$("#creator").val(rowData.creator);
	$("#createtm").val(rowData.createtm);
	$("#editor").val(rowData.editor);
	$("#edittm").val(rowData.edittm);
	$("#locType").val(rowData.locType);
};

//弹出详情页面
bmdefloc.showEdit = function(rowData,flag){
	
	var titleName = "修改";
	if(flag == 1){
		$(".creatorDiv,#info_update,#info_close").show();
		$("#info_save").hide();
		$("#addToolBar").show();
	}else{
		titleName = "查看";
		$(".creatorDiv").show();
		$("#info_save,#info_update,#info_close").hide();
		$("#addToolBar").hide();
	}
	//设置标题
	$('#showDialog').window({ title:titleName });
	
	$('#locno,#locname').validatebox('reset');
	
	//仓库编码设置为只读，不可修改
	$('#locno').attr("readonly","readonly");
	
	//设置信息
	bmdefloc.setDetail(rowData);
	//弹窗
	$("#showDialog").window('open'); 
};

//弹出修改页面
bmdefloc.showModify = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length != 1){
		alert('请选择一条记录！',1);
		return;
	}
	bmdefloc.showEdit(checkedRows[0],1);
};

//新增保存
bmdefloc.addFefloc = function(){
	//debugger;
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return;
     }
     //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
     var checkUrl=BasePath+'/bmdefloc/get_count.json';
     var locnoVal = $("input[id=locno]",fromObj).val();
     var checkDataNo={"locno":locnoVal};
     if(bmdefloc.checkExistFun(checkUrl,checkDataNo)){
    	  alert('仓库编码已存在,不能重复!',1);
    	  $("#locno").focus();
    	  return;
     }
     
	 //3. 保存
     var url = BasePath+'/bmdefloc/addDefloc';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
				
			},
			success: function(returnMsg){
				if(returnMsg=='success'){
					 alert('新增成功!');
					 $('#showDialog').window('close'); 
					 //4.保存成功,清空表单
					 bmdefloc.loadDataGrid();
					 //bmdefloc.clearAll();
					 return;
				 }else{
					 alert('新增异常,请联系管理员!',2);
				 }
		    },
			error:function(){
				alert('新增失败,请联系管理员!',2);
			}
	   });
};

//弹出新增页面
showAdd=function(){
	
	//设置标题
	$('#showDialog').window({ title:"新增" });
	$("#addToolBar").show();
	bmdefloc.clearFormAndTip();
	$('#locType').val('2');
	//仓库编码设置
	$('#locno').removeAttr("readonly");
	$(".creatorDiv,#info_update").hide();
	$("#info_save,#info_close").show();
	$('#showDialog').window('open');  
};

//删除
deleteFefloc=function(){
	//var rows = $("#dataGridJG").datagrid("getSelections");// 获取所有选中的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","您确定要删除这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var locnoStrs = [];
        	$.each(checkedRows, function(index, item){
        		locnoStrs.push(item.locno);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
        	var data={
        	    "locnoStrs":locnoStrs.join(",")
        	};
        	
        	//3校验仓库下是否有绑定用户时
        	var url = BasePath+'/bmdefloc/findIsLocUser';
        	bmdefloc.ajaxRequest(url,data,function(result,returnMsg){
        		 if(result=='success'){
        			    //4. 删除
        			    url = BasePath+'/bmdefloc/deleteFefloc';
        	        	bmdefloc.ajaxRequest(url,data,function(result,returnMsg){
        	        		 if(result=='success'){
        	        			 //4.删除成功,清空表单
        	        			 bmdefloc.loadDataGrid();
        	        			 alert('删除成功!');
        	        		 }else{
        	        			 alert('删除失败,请联系管理员!',2);
        	        		 }
        	        	}); 
        		 }else if(result=='warn'){
        			 alert('仓库有绑定用户，不能删除!',0);
        			 return;
        		 }else {
        			 alert('校验仓库下是否有绑定用户时异常,请联系管理员!',2);
        			 return;
        		 }
        	}); 
        }
	});     
};
//清理form
bmdefloc.clearAll = function(){
	$('#dataForm').form("clear");
	$("#locno").attr('readOnly',false);
};

//清理form和不为空的提示
bmdefloc.clearFormAndTip = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		//$(this).validatebox('reset');
	});
};

//取消按钮
bmdefloc.closeFefloc = function(){
	$('#showDialog').window('close');  
	//$('#locno,#locname').validatebox('reset');
	//$('#showDialog').window('destroy');
};

bmdefloc.initBrandClass = function(){
	$('#brandClass').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=BRAND_CLASS',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	  });
};

bmdefloc.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	  });
	
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	  });
};


$(document).ready(function(){
	
	$('#showDialog').show();
	
	bmdefloc.initStatus();//初始化创建方式
//	bmdefloc.initBrandClass();//初始化品牌级别列表数据
//	bmdefloc.initBrand();
});

//导出
exportExcel=function(){
	exportExcelBaseInfo('dataGridJG','/bmdefloc/do_export.htm','仓库信息导出');
};
