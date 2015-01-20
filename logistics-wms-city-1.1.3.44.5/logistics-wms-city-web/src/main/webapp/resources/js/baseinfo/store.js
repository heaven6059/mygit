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
//store.loadDataGrid = function(){
//	$('#dataGridJG').datagrid({'url':BasePath+'/store/list.json','title':'客户列表','pageNumber':1 });
//};

store.loadDataGrid = function(node){
	if(!node)return;
	$("#dataGridJG").datagrid('load');
	/*var runCount = 0;
	var queryParams = {storeNoHead:node.id};
    $('#dataGridJG').datagrid({'url':BasePath+'/store/list.json?storeNoHead='+node.id,'title':"客户列表",'pageNumber':1,queryParams:queryParams,
    	onLoadSuccess:function(data){
        	runCount++;
            if(data.total == 0 && runCount<2){
            	// 没有记录的话.表格加载本身
            	queryParams = {storeNo:node.id};
            	$('#dataGridJG').datagrid({'url':BasePath+'/store/list.json?storeNo='+node.id,'title':"客户列表",'pageNumber':1,queryParams:queryParams });
            }
       }	
    });*/
};

store.loadDetail = function(storeNo){
	var url = BasePath+'/store/get';
	var reqParam={
		   "storeNo":storeNo
     };
	store.ajaxRequest(url,reqParam,function(data){
		store.initBeforeEdit(data.storeType);
		$('#dataForm').form('load',data);
		$("#storeNo").attr('readOnly',true);
		$("#status").combobox('disable');
		$("#sysNo").combobox('disable');
		$("#zoneNo").combobox('disable');
		$("#storeType").combobox('disable');
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

store.do_export = function(){
	var exportColumns = $("#dataGridJG").datagrid('options').columns[0];
	var str = '[';
	for(var index=0;index<exportColumns.length;index++){
		var temp = exportColumns[index];
		if(index == 0) {
		} else {
			str +=  "{\"field\":\""+temp.field+"\",\"title\":\""+temp.title+"\"}";
			if(index != exportColumns.length-1){
				str += ",";
			}
		}
	}
	str += "]";
	var searchUrl = BasePath+'/store/list.json?queryStoreType=11,22';
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var data = eval("(" +fromObjStr+ ")");
	data.page = 1;
	data.rows = $("#dataGridJG").datagrid('options').pageSize;
	data.isLimitLocno = 'yes';
	data.locno = store.user.locno;
	var result = {};
	$.ajax({
		  async:false,
		  type: 'POST',
		  url: searchUrl,
		  data: data,
		  cache: true,
		  success: function(r){
			  result = r;
		  }
	});
	if(!result || result == null || result.total == 0){
		alert("没有可以导出的数据!",1);
		return;
	}else if(result.total > 10000){
		alert("数据大于10000条不能导出!",1);
		return;
	}
	
	$("#storeNo_export").val($("#storeNoCondition").val());
	$("#storeCode_export").val($("#storeCodeCondition").val());
	$("#storeName_export").val($("#storeNameCondition").val());
	$("#cman_export").val($("#cmanCondition").val());
	$("#status_export").val($("#statusCondition").combobox('getValue'));
	$("#sysNo_export").val($("#sysNoCondition").combobox('getValue'));
	$("#zoneNo_export").val($("#zoneNoCondition").combobox('getValue'));
	$("#storeType_export").val($("#storeTypeCondition").combobox('getValue'));
	$("#circleNo_export").val($("#circleNoCondition").combobox('getValue'));
	$("#queryStoreType").val("11,22");
	$("#isLimitLocno_export").val('yes');
	$("#locno_export").val(store.user.locno);
	$("#queryStoreType").val("11,22");
	$("#exportColumnsCondition_export").val(str);
	$("#exportForm").submit();
	
};


//查询客户信息
store.searchStore = function(){
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/store/list.json';
	
    //3.加载明细
	var params = eval("(" +fromObjStr+ ")");
	var text = $('#sysNoCondition').combobox('getText');
	if($('#sysNoCondition').combobox('getValue')==""&&text!="全选"){
		params['sysNo'] = text;
	}
	params['queryStoreType'] = '11,22';
	params['isLimitLocno'] = 'yes';
	params['locno'] = store.user.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams=params;
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
         var checkUrl=BasePath+'/store/get_count.json';
         var checkDataNo={"storeNo":$("#storeNo").val()};
	     if(store.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('客户编码已存在,不能重复!',1);
	    	  $("#storeNo").focus();
	    	  return;
	      }
			 
		 //3. 保存
	     var saveFn = function(returnData){
	         var url = BasePath+'/store/post';
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
        var url = BasePath+'/store/put';
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
     var url = BasePath+'/store/delete';
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
	//typeObj.combobox('enable');
	//$('#zoneNo').combobox('enable');
	//$('#status').combobox('enable');
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
	$('#zoneNo').combobox('disable');
	$('#status').combobox('disable');
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
	     panelHeight:150,
	     onLoadSuccess:function(){
	    	 //var tempObj = $(this);
	    	 //tempObj.combobox('select', 'N');
	     }
	  });
	
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150,
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 tempData[tempData.length] = {itemvalue:'',itemnamedetail:'全选'};
	    	 for(var i=0;i<data.length;i++){
	    		tempData[tempData.length] = data[i];
	    	 }
	    	 return tempData;
	     },
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
	     panelHeight:"auto",
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 tempData[tempData.length] = {zoneNo:'',zoneName:'全选'};
	    	 for(var i=0;i<data.length;i++){
	    		tempData[tempData.length] = data[i];
	    	 }
	    	 return tempData;
	     }
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
	     panelHeight:150,
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 tempData[tempData.length] = {itemvalue:'',itemnamedetail:'全选'};
	    	 for(var i=0;i<data.length;i++){
	    		tempData[tempData.length] = data[i];
	    	 }
	    	 return tempData;
	     }
	  });
};

store.initStatus = function(){
	$('#status').combobox({
	     valueField:"label",
	     textField:"value",
	     data:store.statusData,
	     panelHeight:150
	 });
	
	
	$('#statusCondition').combobox({
	     valueField:"label",
	     textField:"value",
	     data:store.statusData,
	     panelHeight:150,
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


store.initDataTree = function(){
	$('#dataTreeId').tree({     
        url:BasePath+'/store/queryStoreTree.htm',  
        onClick : function (node) {
        	if(node.state && node.state=='closed'){
        		$(this).tree('expand', node.target);
        	}else{
        		store.clearAll();
        		store.loadDataGrid(node);
        	}
        },
        onExpand : function (node) {
        	$(this).tree('select', node.target);
        	store.clearAll();
        	store.loadDataGrid(node);
        }
    });
};
exportExcel=function(){
	exportExcelBaseInfo('dataGridJG','/store/do_export.htm','客户关系维护信息导出');
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
	store.initCurrentUser();
	//store.loadCircleNo();
	//store.initBrand();
	store.initStoreType(store.storeTypeIdArr);
	//store.initZone();
	store.initStatus();
	store.initStoreTypeCondition();
	store.initDataTree();
	
	var queryParams = {storeNoHead:'-1'};
	//$('#dataGridJG').datagrid({'url':BasePath+'/store/list.json','title':"客户列表",'pageNumber':1,queryParams:queryParams });
	//初始化商圈
	wms_city_common.comboboxLoadFilter(
			["circleNoCondition"],
			'circleNo',
			'circleName',
			'valueAndText',
			false,
			[true],
			BasePath+'/bmcircle/get_biz',
			{locno:store.user.locno},
			null,
			null);
	//初始化品牌库
	wms_city_common.comboboxLoadFilter(
			["sysNoCondition","sysNo"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
			{},
			null,
			null);
	//初始化品区域
	wms_city_common.comboboxLoadFilter(
			["zoneNoCondition","zoneNo"],
			'zoneNo',
			'zoneName',
			'valueAndText',
			false,
			[true,false],
			BasePath+'/zone_info/get_biz',
			{},
			null,
			null);
});
//========================用户 信息========================
store.user = {};
store.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		store.user = data;
	});
};
//==================商圈====================
store.loadCircleNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bmcircle/get_biz?locno='+store.user.locno,
		success : function(data) {
			$('#circleNoCondition').combobox({
				data:data,
			    valueField:'circleNo',    
			    textField:'valueAndText',
			    panelHeight:200,
				loadFilter:function(data){
					if(data){
						for(var i = 0;i<data.length;i++){
							var tempData = data[i];
							tempData.valueAndText = tempData.circleNo+'→'+tempData.circleName;
						}
					}
					return data;
				}
			});
		}
	});
};
//==================商圈====================