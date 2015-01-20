var transitpointconfig = {};

transitpointconfig.getCurrentUser = function(){
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

transitpointconfig.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

transitpointconfig.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/transit_point_config/list.json','title':'中转仓配置列表','pageNumber':1 });
};

transitpointconfig.loadDetail = function(storeNoFrom,storeNo){
	var url = BasePath+'/transit_point_config/get';
	var reqParam={
		   "storeNoFrom":storeNoFrom,
		   "storeNo":storeNo
     };
	transitpointconfig.ajaxRequest(url,reqParam,function(data){
		$('#dataForm').form('load',data);
		//su.yq注释 2013-09-06 11:47
		//$("#storeNoFrom").combobox('disable');
		//$("#storeNo").combobox('disable');
	});
};

transitpointconfig.checkExistFun = function(url,checkColumnJsonData){
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

transitpointconfig.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
	     var storeNoFromVal = $("#storeNoFrom").combobox('getValue');
	     var storeNoVal = $("#storeNo").combobox('getValue');
	     var storeNoTargetVal = $("#storeNoTarget").combobox('getValue');
	     if(storeNoFromVal==storeNoVal || storeNoFromVal==storeNoTargetVal || storeNoVal==storeNoTargetVal){
	    	 alert('发货方、收货方、中转地三者不能相同!',1);
	    	 return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/transit_point_config/get_count.json';
         var checkDataNo={
        		 "storeNoFrom":$("#storeNoFrom").combobox('getValue'),
        		 "storeNo":$("#storeNo").combobox('getValue')
        	  };
	     if(transitpointconfig.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('中转仓配置已存在,不能重复!',1);
	    	  return;
	      }
			 
		 //3. 保存
	     var currentUser = transitpointconfig.getCurrentUser();
         var url = BasePath+'/transit_point_config/post';
         fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					param.createtm = currentUser.currentDate19Str;
					param.creator = currentUser.loginName;
					param.editor = currentUser.loginName;
					param.edittm = currentUser.currentDate19Str;
				},
				success: function(){
					 alert('新增成功!');
					 //4.保存成功,清空表单
					 transitpointconfig.loadDataGrid();
					 transitpointconfig.clearAll();
					 return;
			    },
				error:function(){
					alert('新增失败,请联系管理员!',2);
				}
		   });
};

transitpointconfig.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    
    var storeNoFromVal = $("#storeNoFrom").combobox('getValue');
    var storeNoVal = $("#storeNo").combobox('getValue');
    var storeNoTargetVal = $("#storeNoTarget").combobox('getValue');
    if(storeNoFromVal==storeNoVal || storeNoFromVal==storeNoTargetVal || storeNoVal==storeNoTargetVal){
	   	 alert('发货方、收货方、中转地三者不能相同!',1);
	   	 return ;
    }
   
     //2.绑定数据
    var currentUser = transitpointconfig.getCurrentUser();
     var url = BasePath+'/transit_point_config/put';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(param){
				param.storeNoFrom = $("#storeNoFrom").combobox('getValue');
				param.storeNo = $("#storeNo").combobox('getValue');
				param.editor = currentUser.loginName;
				param.edittm = currentUser.currentDate19Str;
			},
			success: function(){
				 alert('修改成功!');
				 //3.保存成功,清空表单
				 transitpointconfig.loadDataGrid();
				 transitpointconfig.clearAll();
				 return;
		    },
			error:function(){
				alert('修改失败,请联系管理员!',2);
			}
	   });
};

transitpointconfig.deleteByNo = function(){
	 //1.判断是否选择了记录
	 var storeNoFrom = $("#storeNoFrom").combobox('getValue');
     var storeNo = $("#storeNo").combobox('getValue');
	 if(storeNoFrom==null || storeNoFrom=='' || storeNo==null || storeNo==''){
		 alert('请选择要删除的记录!',1);
		 return;
	 }
	 
     //2.绑定数据
     var url = BasePath+'/transit_point_config/delete';
	 var data={
    		 "storeNoFrom":storeNoFrom,
    		 "storeNo":storeNo
	  };
	 //3. 删除
	 transitpointconfig.ajaxRequest(url,data,function(result,returnMsg){
		 if(returnMsg=='success'){
			 //4.删除成功,清空表单
			 transitpointconfig.loadDataGrid();
			 transitpointconfig.clearAll();
			 alert('删除成功!');
		 }else{
			 alert('删除失败,请联系管理员!',2);
		 }
	}); 
};

transitpointconfig.clearAll = function(){
	$('#dataForm').form("clear");
	$("#storeNoFrom").combobox('enable');
	$("#storeNo").combobox('enable');
};

transitpointconfig.initStoreNoFrom = function(){
	$('#storeNoFrom').combogrid({
		 panelWidth:450,   
        idField:'storeNo',  
        textField:'textFieldName',   
        pagination : true,
        rownumbers:true,
        mode: 'remote',
        url:BasePath+'/store/list.json',   
        columns:[[  
                  {field:'storeNo',title:'客户编码',width:140},  
                  {field:'storeName',title:'客户名称',width:140}  
         ]],
         loadFilter: function(data){
                	 if(data && data.rows){
                		 for(var i = 0;i<data.rows.length;i++){
                			 var tempData = data.rows[i];
                			 tempData.textFieldName = tempData.storeNo+'→'+tempData.storeName;
                		 }
                	 }
                 	return data;
         }
	  });
};

transitpointconfig.initStoreNo = function(){
	$('#storeNo').combogrid({
		 panelWidth:450,   
       idField:'storeNo',  
       textField:'textFieldName',   
       pagination : true,
       rownumbers:true,
       mode: 'remote',
       url:BasePath+'/store/list.json',   
       columns:[[  
                 {field:'storeNo',title:'客户编码',width:140},  
                 {field:'storeName',title:'客户名称',width:140}  
        ]],
        loadFilter: function(data){
               	 if(data && data.rows){
               		 for(var i = 0;i<data.rows.length;i++){
               			 var tempData = data.rows[i];
               			 tempData.textFieldName = tempData.storeNo+'→'+tempData.storeName;
               		 }
               	 }
                	return data;
        }
	  });
};

transitpointconfig.initStoreNoTarget = function(){
	$('#storeNoTarget').combogrid({
		 panelWidth:450,   
       idField:'storeNo',  
       textField:'textFieldName',   
       pagination : true,
       rownumbers:true,
       mode: 'remote',
       url:BasePath+'/store/list.json',   
       columns:[[  
                 {field:'storeNo',title:'客户编码',width:140},  
                 {field:'storeName',title:'客户名称',width:140}  
        ]],
        loadFilter: function(data){
               	 if(data && data.rows){
               		 for(var i = 0;i<data.rows.length;i++){
               			 var tempData = data.rows[i];
               			 tempData.textFieldName = tempData.storeNo+'→'+tempData.storeName;
               		 }
               	 }
                	return data;
        }
	  });
};

transitpointconfig.exportExcel=function(){
	exportExcelBaseInfo('dataGridJG','/transit_point_config/do_export.htm','中转仓配置管理信息导出');
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

$(document).ready(function(){
	transitpointconfig.initStoreNoFrom();
	transitpointconfig.initStoreNo();
	transitpointconfig.initStoreNoTarget();
});