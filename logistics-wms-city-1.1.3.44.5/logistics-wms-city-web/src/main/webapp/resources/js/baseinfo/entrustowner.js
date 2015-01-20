var entrustowner = {};

//委托业主状态
entrustowner.statusData = [{    
    "label":"0",
    "text":"有效", 
    "value":"0→有效" 
},{    
    "label":"1",    
    "text":"无效", 
    "value":"1→无效"   
}];

entrustowner.exportExcel = function(){
	exportExcelBaseInfo('dataGridJG','/entrust_owner/do_export.htm?businessType=9','委托业主管理信息导出');
};

entrustowner.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

entrustowner.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/entrust_owner/list.json?businessType=9','title':'委托业主列表','pageNumber':1 });
};

entrustowner.loadDetail = function(supplierNo){
	$('#dataForm').form('load',BasePath+'/entrust_owner/get?supplierNo='+supplierNo);
	$("#supplierNo").attr('readOnly',true);
};

entrustowner.checkExistFun = function(url,checkColumnJsonData){
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

entrustowner.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/entrust_owner/get_count.json';
         var checkDataNo={"supplierNo":$("#supplierNo").val()};
	     if(entrustowner.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('委托业主编号已存在,不能重复!',1);
	    	  $("#supplierNo").focus();
	    	  return;
	      }
			 
		 //3. 保存
	     var saveFn = function(returnData){
	         var url = BasePath+'/entrust_owner/post';
	         fromObj.form('submit', {
					url: url,
					onSubmit: function(param){
						param.supplierNoHead = '0';
						param.supplierCode = '0';
						param.taxpayingNo = '0';
						param.businessType = 9;
						param.createtm = returnData.currentDate19Str;
						param.creator = returnData.loginName;
						param.editor = returnData.loginName;
						param.edittm = returnData.currentDate19Str;
					},
					success: function(){
						 alert('新增成功!');
						 //4.保存成功,清空表单
						 entrustowner.loadDataGrid();
						 entrustowner.clearAll();
						 return;
				    },
					error:function(){
						alert('新增失败,请联系管理员!',2);
					}
			   });
	     };
	     entrustowner.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

entrustowner.update = function(){
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
   				 entrustowner.loadDataGrid();
   				 entrustowner.clearAll();
   				 return;
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	   });
    };
    entrustowner.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},updateFn);
};

entrustowner.deleteByNo = function(){
	 //1.判断是否选择了记录
	 var supplierNo=$("#supplierNo").val();
	 if(supplierNo==null||supplierNo==''){
		 alert('请选择要删除的记录!',1);
		 return;
	 }
	 
     //2.绑定数据
     var url = BasePath+'/entrust_owner/delete';
	 var data={
			    "supplierNo":$("#supplierNo").val()
	  };
	 //3. 删除
	 entrustowner.ajaxRequest(url,data,function(result,returnMsg){
		 if(returnMsg=='success'){
			 //4.删除成功,清空表单
			 entrustowner.loadDataGrid();
			 entrustowner.clearAll();
			 alert('删除成功!');
		 }else{
			 alert('删除失败,请联系管理员!',2);
		 }
	}); 
};

entrustowner.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};

entrustowner.initStatus = function(){
	$("#supplierStatus").combobox({
		valueField:'label',    
	    textField:'value',
	    data:entrustowner.statusData,
	    panelHeight:"auto"
	});
};

$(document).ready(function(){
	entrustowner.initStatus();//初始化委托业主状态
});