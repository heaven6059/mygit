var brand = {};

brand.statusData = [{    
    "label":"0",
    "text":"未启用", 
    "value":"0→未启用",   
},{    
    "label":"1",    
    "text":"启用", 
    "value":"1→启用"   
}];

brand.initStatus = function(){
	$('#status').combobox({
	     valueField:"label",
	     textField:"value",
	     data:brand.statusData,
	     panelHeight:"auto"
	  });
};

brand.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

brand.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/brand/list.json','title':'品牌列表','pageNumber':1 });
};

brand.loadDetail = function(brandNo){
	$('#dataForm').form('load',BasePath+'/brand/get?brandNo='+brandNo);
	$("#brandNo").attr('readOnly',true);
};

brand.checkExistFun = function(url,checkColumnJsonData){
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
brand.searchBrand = function(){
	
	 $('#dataForm').form("clear");
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/brand/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
   
    
};

//清楚查询条件
brand.searchBrandClear = function(){
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

brand.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/brand/get_count.json';
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
	     brand.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

brand.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   
     //2.绑定数据
    var updateFn = function(returnData){
        var url = BasePath+'/brand/put';
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.editor = returnData.loginName;
				param.edittm = returnData.currentDate19Str;
   			},
   			success: function(){
   				 alert('修改成功!');
   				 //3.保存成功,清空表单
   				brand.loadDataGrid();
   				brand.clearAll();
   				 return;
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	   });
    };
    brand.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},updateFn);
};

brand.deleteByNo = function(){
	 //1.判断是否选择了记录
	 var brandNo=$("#brandNo").val();
	 if(brandNo==null||brandNo==''){
		 alert('请选择要删除的记录!',1);
		 return;
	 }
	 
     //2.绑定数据
     var url = BasePath+'/brand/delete';
	 var data={
			    "brandNo":$("#brandNo").val()
	  };
	 //3. 删除
	 brand.ajaxRequest(url,data,function(result,returnMsg){
		 if(returnMsg=='success'){
			 //4.删除成功,清空表单
			 brand.loadDataGrid();
			 brand.clearAll();
			 alert('删除成功!');
		 }else{
			 alert('删除失败,请联系管理员!',2);
		 }
	}); 
};

brand.clearAll = function(){
	$('#dataForm').form("clear");
	$("#brandNo").attr('readOnly',false);
};

brand.initBrandClass = function(){
	$('#brandClass').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=BRAND_CLASS',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	  });
};

brand.initBrand = function(){
	/*$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });
	
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });*/
	wms_city_common.comboboxLoadFilter(
			["sysNo","sysNoCondition"],
			null,
			null,
			null,
			true,
			[false,true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
			{},
			null,
			null);
};


$(document).ready(function(){
	brand.initBrandClass();//初始化品牌级别列表数据
	brand.initStatus();//初始化品牌状态列表数据
	brand.initBrand();
});

//导出
exportExcel=function(){

	exportExcelBaseInfo('dataGridJG','/brand/do_export.htm','品牌管理信息导出');
};
