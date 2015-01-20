var store = {};

store.statusData = [{    
    "label":"0",
    "text":"正常", 
    "value":"0→正常" 
},{    
    "label":"1",
    "text":"关闭", 
    "value":"1→关闭"  
}];

store.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

store.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/store2/list.json','title':'原客户列表','pageNumber':1 });
};

store.loadDetail = function(storeNo){
	var url = BasePath+'/store2/get';
	var reqParam={
		   "storeNo":storeNo
     };
	store.ajaxRequest(url,reqParam,function(data){
		store.initBeforeEdit(data.storeType);
		$('#dataForm').form('load',data);
		$("#storeNo").attr('readOnly',true);
	});
};

store.checkExistFun = function(url,checkColumnJsonData){
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

//查询客户信息
store.searchStore = function(){
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/store2/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
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

//清楚查询条件
store.searchStoreClear = function(){
	$('#searchForm').form("clear");
};

store.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/store2/get_count.json';
         var checkDataNo={"storeNo":$("#storeNo").val()};
	     if(store.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('客户编码已存在,不能重复!',1);
	    	  $("#storeNo").focus();
	    	  return;
	      }
			 
		 //3. 保存
	     var saveFn = function(returnData){
	         var url = BasePath+'/store2/post';
	         fromObj.form('submit', {
					url: url,
					onSubmit: function(param){
						param.storeNoHead = '0';
						param.storeCode = '0';
						param.dtsNo = '0';
						param.createtm = returnData.currentDate19Str;
						param.creator = returnData.loginName;
						param.editor = returnData.loginName;
						param.edittm = returnData.currentDate19Str;
					},
					success: function(){
						 alert('新增成功!');
						 //4.保存成功,清空表单
						 store.loadDataGrid();
						 store.clearAll();
						 return;
				    },
					error:function(){
						alert('新增失败,请联系管理员!',2);
					}
			   });
	     };
	     store.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

store.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   
     //2.绑定数据
    var updateFn = function(returnData){
        var url = BasePath+'/store2/put';
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.editor = returnData.loginName;
				param.edittm = returnData.currentDate19Str;
   			},
   			success: function(){
   				 alert('修改成功!');
   				 //3.保存成功,清空表单
   				store.loadDataGrid();
   				store.clearAll();
   				 return;
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	   });
    };
    store.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},updateFn);
};

store.deleteByNo = function(){
	 //1.判断是否选择了记录
	 var storeNo=$("#storeNo").val();
	 if(storeNo==null||storeNo==''){
		 alert('请选择要删除的记录!',1);
		 return;
	 }
	 
     //2.绑定数据
     var url = BasePath+'/store2/delete';
	 var data={
			    "storeNo":$("#storeNo").val()
	  };
	 //3. 删除
	 store.ajaxRequest(url,data,function(result,returnMsg){
		 if(returnMsg=='success'){
			 //4.删除成功,清空表单
			 store.loadDataGrid();
			 store.clearAll();
			 alert('删除成功!');
		 }else{
			 alert('删除失败,请联系管理员!',2);
		 }
	}); 
};

store.initBeforeAdd = function(){
	var tempObj = $('#sysNo');
	tempObj.combobox('select', 'N');
	$("#noColumnztbm").val('0');
	store.initStoreType(store.storeTypeIdArr);
	var typeObj = $('#storeType');
	typeObj.combobox('enable');
	$('#zoneNo').combobox('enable');
	$('#status').combobox('enable');
	//storeType==11
	$("#storeName").attr('readOnly',false);
	$("#storeLname").attr('readOnly',false);
	$("#telno").attr('readOnly',false);
	$("#faxno").attr('readOnly',false);
	$("#zipno").attr('readOnly',false);
	$("#email").attr('readOnly',false);
	$("#address").attr('readOnly',false);
	$("#cman").attr('readOnly',false);
	$("#manager").attr('readOnly',false);
	$("#areaTotal").attr('readOnly',false);
	$("#area").attr('readOnly',false);
};

store.initBeforeEdit = function(storeType){
	store.initStoreType(null);
	var typeObj = $('#storeType');
	//su.yq注释 2013-09-06 11:25
	//typeObj.combobox('disable');
	//$('#zoneNo').combobox('disable');
	//$('#status').combobox('disable');
	typeObj.attr('readOnly',true);
	$("#zoneNo").attr('readOnly',true);
	$("#status").attr('readOnly',true);
	if(storeType==11){
		$("#storeName").attr('readOnly',true);
		$("#storeLname").attr('readOnly',true);
		$("#telno").attr('readOnly',true);
		$("#faxno").attr('readOnly',true);
		$("#zipno").attr('readOnly',true);
		$("#email").attr('readOnly',true);
		$("#address").attr('readOnly',true);
		$("#cman").attr('readOnly',true);
		$("#manager").attr('readOnly',true);
		$("#areaTotal").attr('readOnly',true);
		$("#area").attr('readOnly',true);
	}else{
		$("#storeName").attr('readOnly',true);
		$("#storeLname").attr('readOnly',true);
		$("#telno").attr('readOnly',true);
		$("#faxno").attr('readOnly',true);
		$("#zipno").attr('readOnly',true);
		$("#email").attr('readOnly',true);
		$("#address").attr('readOnly',true);
		$("#cman").attr('readOnly',true);
		$("#manager").attr('readOnly',true);
		$("#areaTotal").attr('readOnly',true);
		$("#area").attr('readOnly',true);
	}
};

store.clearAll = function(){
	$('#dataForm').form("clear");
	$("#storeNo").attr('readOnly',false);
	store.initBeforeAdd();
};

store.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150",
	     onLoadSuccess:function(){
	    	 //var tempObj = $(this);
	    	 //tempObj.combobox('select', 'N');
	     }
	  });
	
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150",
	     onLoadSuccess:function(){
	    	 //var tempObj = $(this);
	    	 //tempObj.combobox('select', 'N');
	     }
	  });
};

store.initZone = function(){
	$('#zoneNo').combobox({
		url:BasePath+'/zone_info/get_biz',
	     valueField:"zoneNo",
	     textField:"zoneName",
	     panelHeight:"auto"
	 });
	
	$('#manageZoneNo').combobox({
		url:BasePath+'/zone_info/get_biz',
	     valueField:"zoneNo",
	     textField:"zoneName",
	     panelHeight:"auto"
	 });
	
	$('#zoneNoCondition').combobox({
		url:BasePath+'/zone_info/get_biz',
	     valueField:"zoneNo",
	     textField:"zoneName",
	     panelHeight:"auto"
	 });
};

store.storeTypeIdArr = ['12','20','22'];

store.initStoreType = function(idArr){
	$('#storeType').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=STORE_TYPE',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto",
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 for(var i=0;i<data.length;i++){
	    		 var tempObj = data[i];
	    		 if(idArr){
	    	    	 for(var j=0;j<idArr.length;j++){
	    	    		 var tempId = idArr[j];
	    	    		 if(tempObj.itemvalue==tempId){
	    	    			 tempData[tempData.length] = tempObj;
	    	    		 }
	    	    	 }
	    		 }else{
	    			 tempData[tempData.length] = tempObj;
	    		 }
	    	 }
	    	 return tempData;
	     }
	  });
};

store.initStoreTypeCondition = function(){
	$('#storeTypeCondition').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=STORE_TYPE',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto",
	     loadFilter:function(data){
	    	 return data;
	     }
	  });
};

store.initStatus = function(){
	$('#status').combobox({
	     valueField:"label",
	     textField:"value",
	     data:store.statusData,
	     panelHeight:"auto"
	 });
	
	
	$('#statusCondition').combobox({
	     valueField:"label",
	     textField:"value",
	     data:store.statusData,
	     panelHeight:"auto"
	 });
};

exportExcel=function(){
	exportExcelBaseInfo('dataGridJG','/store2/do_export.htm','原客户关系维护信息导出');
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
	store.initBrand();
	store.initStoreType(store.storeTypeIdArr);
	store.initZone();
	store.initStatus();
	store.initStoreTypeCondition();
	
});