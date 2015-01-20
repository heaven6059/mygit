var cmdefware = {};
cmdefware.locno;
cmdefware.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
cmdefware.openUI = function(opt){
	$('#openUI').window({
		title:opt
	});
	$("#info_save").show();
	$('#openUI').window('open');
};
cmdefware.closeUI = function(opt){
	$("#openUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};
cmdefware.addUI = function(opt){
	cmdefware.openUI("新增");
	cmdefware.clearAll();
	$("#wareNo").attr('readOnly',false);
	$("#wareNo").css('background','#fff');
	$("#wareNo").css('color','#000');
	$("#locno+ span input.combo-text").attr("readOnly",false);
	$("#locno+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#locno+ span").css("background-color","white");
	$("#locno+ span input.combo-text").css("background-color","white");
	$("#opt").val("add");
};

cmdefware.editUI = function(opt){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		cmdefware.loadDetail(checkedRows[0],"edit");
	}
};

cmdefware.manage = function(){
	
	var opt = $("#opt").val();
	if(opt && opt=="add"){
		cmdefware.save();
	}else{
		cmdefware.update();
	}
};
cmdefware.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/cm_defware/list.json?locno='+cmdefware.locno,'title':'仓区列表','pageNumber':1 });
};

cmdefware.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		cmdefware.openUI("修改");
	}else{
		cmdefware.openUI("详情");
		$("#info_save").hide();
	}
	
	$('#dataForm').form('load',rowData);
	$("#wareNo").attr('readOnly',true);
	$("#wareNo").css('background','#D4D0C8');
	$("#wareNo").css('color','#808080');
	$("#locno+ span input.combo-text").attr("readOnly",true);
	$("#locno+ span input.combo-text+ span span").removeClass("combo-arrow");
	$("#locno+ span").css("background-color","#EBEBE4");
	$("#locno+ span input.combo-text").css("background-color","#EBEBE4");
};

cmdefware.checkExistFun = function(url,checkColumnJsonData){
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

cmdefware.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
	     wms_city_common.loading("show","正在保存仓区......");
         var checkUrl=BasePath+'/cm_defware/get_countValidate.json';
         var checkDataNo={"locno":cmdefware.locno,"wareNo":$("#wareNo").val()};
	     if(cmdefware.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('仓区编码已经存在,不能重复!',1);
	    	  $("#wareNo").focus();
	    	  wms_city_common.loading();
	    	  return;
	      }
			 
		 //3. 保存
	     var saveFn = function(returnData){
	         var url = BasePath+'/cm_defware/post';
	         fromObj.form('submit', {
					url: url,
					onSubmit: function(param){
						param.createtm = returnData.currentDate19Str;
						param.creator = returnData.loginName;
						param.creatorName = returnData.username;
						param.editor = returnData.loginName;
						param.editorName = returnData.username;
						param.edittm = returnData.currentDate19Str;
						param.locno = returnData.locno;
						param.wareTaskType = '0';
					},
					success: function(){
						 alert('新增成功!');
						 //4.保存成功,清空表单
						 cmdefware.loadDataGrid();
						 cmdefware.closeUI();
						 wms_city_common.loading();
						 return;
				    },
					error:function(){
						alert('新增失败,请联系管理员!',2);
						wms_city_common.loading();
					}
			   });
	     };
	     cmdefware.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,saveFn);
};

cmdefware.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
     //2.绑定数据
    var updateFn = function(returnData){
    	wms_city_common.loading("show","正在修改仓区......");
        var url = BasePath+'/cm_defware/put';
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.editor = returnData.loginName;
				param.editorName = returnData.username;
				param.edittm = returnData.currentDate19Str;
				param.locno = returnData.locno;
   			},
   			success: function(){
   				 alert('修改成功!');
   				 //3.保存成功,清空表单
   				 cmdefware.loadDataGrid();
   				 cmdefware.closeUI();
   				wms_city_common.loading();
   				 return;
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   				wms_city_common.loading();
   			}
   	   });
    };
    cmdefware.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,updateFn);
};

cmdefware.deleteRows = function(){
	
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keys = [];
	$.each(checkedRows, function(index, item){
		keys[keys.length] = {wareNo:item.wareNo};
	});   
	
     //2.绑定数据
     var url = BasePath+'/cm_defware/delete_records';
	 var data={
			 locno:cmdefware.locno,
			 datas:JSON.stringify(keys)
	 };
	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){    
		    	wms_city_common.loading("show", "正在删除仓区......");
		   	 cmdefware.ajaxRequest(url,data,true,function(result){
				 if(result.flag=='success'){
					 //4.删除成功,清空表单
					 cmdefware.loadDataGrid();
					 cmdefware.clearAll();
					 alert('删除成功!');
				 }else if(result.flag=='warn'){
					 alert("仓区：【"+result.msg+"】存在库区明细,不能删除!");
				 }else{
					 alert('删除失败,请联系管理员!',2);
				 }
				 wms_city_common.loading();
			});   
		    }    
		}); 

};

cmdefware.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
cmdefware.searchClear = function(){
	$('#searchForm').form("clear");
};
cmdefware.searchWare = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/cm_defware/list.json?locno='+cmdefware.locno;
	var params = eval("(" +fromObjStr+ ")");
	params.sort = "ware_no";
    $( "#dataGridJG").datagrid( 'options' ).queryParams= params;
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

//数据转换 把数组和list转换为map
cmdefware.tansforArrayToMap = function(oriData){
	var str = "";
	for(var i = 0,length = oriData.length;i<length;i++){
		str = str+"\""+oriData[i].itemvalue +"\":\""+oriData[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};

cmdefware.initLocno = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			cmdefware.locno = data.locno;
		}
	});
};

$(document).ready(function(){
	cmdefware.initLocno();
	//初始化仓区
	wms_city_common.comboboxLoadFilter(
			["wareNoCondition"],
			'wareNo',
			'wareName',
			'valueAndText',
			false,
			[true],
			BasePath+'/cm_defware/get_biz',
			{"locno":cmdefware.locno},
			null,
			null);
	wms_city_common.closeTip("openUI");
});

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