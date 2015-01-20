var supplier = {};

supplier.statusData = [{    
    "label":"0",
    "text":"有效", 
    "value":"0→有效" 
},{    
    "label":"1",    
    "text":"无效", 
    "value":"1→无效"   
}];

supplier.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

supplier.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/supplier/list.json','title':'供应商列表','pageNumber':1 });
};

supplier.loadDetail = function(supplierNo){
	$('#dataForm').form('load',BasePath+'/supplier/get?supplierNo='+supplierNo);
	$("#supplierNo").attr('readOnly',true);
};

supplier.checkExistFun = function(url,checkColumnJsonData){
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

//查询供应商信息
supplier.searchSupplier = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/supplier/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清除查询条件
supplier.searchSupplierClear = function(){
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

supplier.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/supplier/get_count.json';
         var checkDataNo={"supplierNo":$("#supplierNo").val()};
	     if(supplier.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('供应商编码已存在,不能重复!',1);
	    	  $("#supplierNo").focus();
	    	  return;
	      }
			 
		 //3. 保存
	     var saveFn = function(returnData){
	         var url = BasePath+'/supplier/post';
	         fromObj.form('submit', {
					url: url,
					onSubmit: function(param){
						param.supplierNoHead = '0';
						param.createtm = returnData.currentDate19Str;
						param.creator = returnData.loginName;
						param.editor = returnData.loginName;
						param.edittm = returnData.currentDate19Str;
					},
					success: function(){
						 alert('新增成功!');
						 //4.保存成功,清空表单
						 supplier.loadDataGrid();
						 supplier.clearAll();
						 return;
				    },
					error:function(){
						alert('新增失败,请联系管理员!',2);
					}
			   });
	     };
	     supplier.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

supplier.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   
     //2.绑定数据
    var updateFn = function(returnData){
        var url = BasePath+'/supplier/put';
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.editor = returnData.loginName;
				param.edittm = returnData.currentDate19Str;
   			},
   			success: function(){
   				 alert('修改成功!');
   				 //3.保存成功,清空表单
   				supplier.loadDataGrid();
   				supplier.clearAll();
   				 return;
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	   });
    };
    supplier.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},updateFn);
};

supplier.deleteByNo = function(){
	 //1.判断是否选择了记录
	 var supplierNo=$("#supplierNo").val();
	 if(supplierNo==null||supplierNo==''){
		 alert('请选择要删除的记录!',1);
		 return;
	 }
	 
     //2.绑定数据
     var url = BasePath+'/supplier/delete';
	 var data={
			    "supplierNo":$("#supplierNo").val()
	  };
	 //3. 删除
	 supplier.ajaxRequest(url,data,function(result,returnMsg){
		 if(returnMsg=='success'){
			 //4.删除成功,清空表单
			 supplier.loadDataGrid();
			 supplier.clearAll();
			 alert('删除成功!');
		 }else{
			 alert('删除失败,请联系管理员!',2);
		 }
	}); 
};

supplier.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
	$("#bizType").val(0);
};

supplier.initSupplierType = function(){
	$('#supplierType').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SUPPLIER_TYPE',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	  });
	
	$('#supplierTypeCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SUPPLIER_TYPE',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	  });
};

supplier.initStatus = function(){
	$('#supplierStatus').combobox({
	     valueField:"label",
	     textField:"value",
	     data:supplier.statusData,
	     panelHeight:"auto"
	  });
	
	$('#supplierStatusCondition').combobox({
	     valueField:"label",
	     textField:"value",
	     data:supplier.statusData,
	     panelHeight:"auto"
	  });
};

supplier.initBusinessType = function(){
	$('#businessType').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=BUSINESS_TYPE',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto",
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 for(var i=0;i<data.length;i++){
	    		 var tempObj = data[i];
	    		 var tempInt = parseInt(tempObj.itemvalue,10);
	    		 if(tempInt<8){
	    			 tempData[tempData.length] = tempObj;
	    		 }
	    	 }
	    	 return tempData;
	     }
	  });
};

supplier.initZone = function(){
	$('#zoneNo').combobox({
		url:BasePath+'/zone_info/get_biz',
	     valueField:"zoneNo",
	     textField:"zoneName",
	     panelHeight:"auto"
	  });
};

supplier.initTaxLevel = function(){
	$('#taxLevel').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=TAX_LEVEL',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	  });
};

supplier.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:200
	  });
	
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:200
	  });
};

supplier.exportExcel=function(){
	exportExcelBaseInfo('dataGridJG','/supplier/do_export.htm','供应商管理信息导出');
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
	//supplier.initSupplierType();
	//supplier.initStatus();
	supplier.initBusinessType();
	supplier.initZone();
	supplier.initTaxLevel();
	//supplier.initBrand();
	//初始化供应商类型
	wms_city_common.comboboxLoadFilter(
			["supplierTypeCondition","supplierType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SUPPLIER_TYPE',
			{},
			null,
			null);
	//初始化供应商状态
	wms_city_common.comboboxLoadFilter(
			["supplierStatusCondition","supplierStatus"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SUPPLIER_STATUS',
			{},
			null,
			null);
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNoCondition]',$('#searchForm'))},
			{"sysNoObj":$('input[id=sysNo]',$('#dataForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	$("#sysNo").combobox('disable');
	$("#supplierStatus").combobox('disable');
	$("#supplierType").combobox('disable');
	$("#zoneNo").combobox('disable');
	$("#taxLevel").combobox('disable');
	$("#businessType").combobox('disable');
});