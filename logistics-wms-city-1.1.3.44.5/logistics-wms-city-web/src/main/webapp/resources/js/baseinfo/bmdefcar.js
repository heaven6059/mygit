var bmdefcar = {};
bmdefcar.user;
//加载车辆类型代码
bmdefcar.loadCartypeNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bm_defcartype/list.json',
		success : function(data) {
			$('#cartypeNo').combobox({
			    data:data.rows,
			    valueField:'cartypeNo',    
			    textField:'cartypeName',
			    panelHeight:"auto",
			    onSelect:function(data){
			    	$("#cartypeName").val(data.cartypeName);
			    }
			});
			$('#cartypeNo').combobox("select",data.rows[0].cartypeNo);  
		}
	});
};

bmdefcar.exportExcel = function(){
	exportExcelBaseInfo('dataGridJG','/bm_defcar/do_export.htm?businessType=9','委托业主管理信息导出');
};

bmdefcar.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
bmdefcar.openUI = function(opt){
	$('#openUI').window({
		title:opt
	});
	$("#info_save").show();
	$('#openUI').window('open');
};
bmdefcar.closeUI = function(opt){
	$('#openUI').window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};
bmdefcar.addUI = function(opt){
	bmdefcar.openUI("新增");
	bmdefcar.clearAll();
	$("#opt").val("add");
	$("#carNo").attr('readOnly',false);
	$("#locno").attr('readOnly',false);
};

bmdefcar.editUI = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		bmdefcar.loadDetail(checkedRows[0],"edit");
	}
};

bmdefcar.manage = function(){
	
	var opt = $("#opt").val();
	if(opt && opt=="add"){
		bmdefcar.save();
	}else{
		bmdefcar.update();
	}
};
bmdefcar.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bm_defcar/list.json?locno='+bmdefcar.locno,'title':'车辆列表','pageNumber':1 });
};

bmdefcar.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		bmdefcar.openUI("修改");
	}else{
		bmdefcar.openUI("详情");
		$("#info_save").hide();
	}
	$('#dataForm').form('load',rowData);
	$("#carNo").attr('readOnly',true);
	$("#locno").attr('readOnly',true);
};

bmdefcar.checkExistFun = function(url,checkColumnJsonData){
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

bmdefcar.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/bm_defcar/get_count.json';
         var checkDataNo={"carNo":$("#carNo").val(),"locno":bmdefcar.locno};
	     if(bmdefcar.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('车辆编号已存在,不能重复!',1);
	    	  $("#supplierNo").focus();
	    	  return;
	      }
			 
		 //3. 保存
         var url = BasePath+'/bm_defcar/post';
         fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					param.createtm = bmdefcar.user.currentDate19Str;
					param.creator = bmdefcar.user.loginName;
					param.editor = bmdefcar.user.loginName;
					param.edittm = bmdefcar.user.currentDate19Str;
					param.locno = bmdefcar.user.locno;
				},
				success: function(r){
						try{
							eval("("+r+")");
							alert('新增成功!');
							 //4.保存成功,清空表单
							 bmdefcar.loadDataGrid();
							 bmdefcar.closeUI();
							 return;
						}catch(e){
							alert('新增失败,请联系管理员!',2);
					}
					
			    },
				error:function(){
					alert('新增失败,请联系管理员!',2);
				}
		   });
	    
};

bmdefcar.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
     //2.绑定数据
    var updateFn = function(returnData){
        var url = BasePath+'/bm_defcar/put';
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.editor = returnData.loginName;
				param.edittm = returnData.currentDate19Str;
				param.locno = returnData.locno;
   			},
   			success: function(){
   				 alert('修改成功!');
   				 //3.保存成功,清空表单
   				 bmdefcar.loadDataGrid();
   				 bmdefcar.closeUI();
   				 return;
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	   });
    };
    bmdefcar.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},updateFn);
};

bmdefcar.deleteRows = function(){
	
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keys = [];
	$.each(checkedRows, function(index, item){
		keys.push(item.locno+"-"+item.carNo);
	});   
     //2.绑定数据
     var url = BasePath+'/bm_defcar/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){    
		   	 bmdefcar.ajaxRequest(url,data,function(result,returnMsg){
				 if(returnMsg=='success'){
					 //4.删除成功,清空表单
					 bmdefcar.loadDataGrid();
					 bmdefcar.clearAll();
					 alert('删除成功!');
				 }else{
					 alert('删除失败,请联系管理员!',2);
				 }
			}); 
		    }    
	});  





};

bmdefcar.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
//=====================车辆类型 =============================
//车辆类型   0-运输车；1-高位叉车；2-平移叉车3-液压叉车
bmdefcar.carClass = {};
bmdefcar.initCarClass = function(data){
	$('#carClass').combobox({
		  valueField:"itemvalue",
		  textField:"itemname",
		  data:data,
		  panelHeight:"auto",
	  });
	bmdefcar.carClass = bmdefcar.tansforArrayToMap(data);
};
//车辆类型格式化
bmdefcar.carClassFormatter = function(value, rowData, rowIndex){
	return bmdefcar.carClass[value];
};
//======================车辆类型==============================


//数据转换 把数组和list转换为map
bmdefcar.tansforArrayToMap = function(oriData){
	var str = "";
	for(var i = 0,length = oriData.length;i<length;i++){
		str = str+"\""+oriData[i].itemvalue +"\":\""+oriData[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};

bmdefcar.initLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			bmdefcar.locno = data.locno;
		}
	});
};
$(document).ready(function(){
	 bmdefcar.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},function(u){bmdefcar.user = u;});
	bmdefcar.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CAR_CLASS',{},bmdefcar.initCarClass);
	bmdefcar.loadCartypeNo();
	bmdefcar.initLoc();
	bmdefcar.loadDataGrid();
});