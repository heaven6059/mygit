var category = {};

category.statusData = [{    
    "label":"0",
    "text":"禁用", 
    "value":"0→禁用" 
},{    
    "label":"1",    
    "text":"启用", 
    "value":"1→启用"   
}];

category.levelData = [{    
    "label":"1",
    "text":"大", 
    "value":"1→大" 
},{    
    "label":"2",
    "text":"中", 
    "value":"2→中"  
},{    
    "label":"3",
    "text":"小", 
    "value":"3→小"  
}];

category.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

category.loadDataGrid = function(node){
	if(!node)return;
	var runCount = 0;
	var queryParams = {headCateNo:node.id};
    $('#dataGridJG').datagrid({'url':BasePath+'/category/list.json?sysNo=&cateName=&cateNo=&headCateNo='+node.id,'title':"类别列表",'pageNumber':1,queryParams:queryParams,
    	onLoadSuccess:function(data){
        	runCount++;
            if(data.total == 0 && runCount<2){
               // 没有记录的话.表格加载本身
            	queryParams = {cateNo:node.id};
            	$('#dataGridJG').datagrid({'url':BasePath+'/category/list.json?sysNo=&cateName=&cateNo='+node.id,'title':"类别列表",'pageNumber':1,queryParams:queryParams });
            }
       }	
    });
};

category.loadDetail = function(cateNo){
	$('#dataForm').form('load',BasePath+'/category/get?cateNo='+cateNo);
	$("#cateNo").attr('readOnly',true);
};

category.checkExistFun = function(url,checkColumnJsonData){
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

//查询类别信息
category.searchCategory = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/category/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清除查询条件
category.searchCategoryClear = function(){
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

category.save = function(){
		var treeObj = $('#dataTreeId');
	    var fromObj=$('#dataForm');
	
	     //1. 先判断是否选择了树节点
	    var node = treeObj.tree('getSelected');
	    if(!node){
		  	  // 如果没有选中的话 ，就增添在根目录平级  这个要看业务需求，如果整棵树只有一个根节点，这里就提示请选择树
		   	 alert('请先选中上级类别!',1);
		   	 return;
	    }
	     //2.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //3.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/category/get_count.json';
         var checkDataNo={"cateNo":$("#cateNo").val()};
	     if(category.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('类别编码已存在,不能重复!',1);
	    	  $("#cateNo").focus();
	    	  return;
	      }
			 
		 //4. 保存
	     var saveFn = function(returnData){
	         var url = BasePath+'/category/post';
	         fromObj.form('submit', {
					url: url,
					onSubmit: function(param){
						$("#headCateNo").attr('value',node.id);
						param.createtm = returnData.currentDate19Str;
						param.creator = returnData.loginName;
						param.editor = returnData.loginName;
						param.edittm = returnData.currentDate19Str;
					},
					success: function(){
						 alert('新增成功!');
						 //5.保存成功自动刷新  刷新树--刷新右边表格
						 treeObj.tree('append', {
								parent: node.target,
								data: [{
									id: $('#cateNo').val(),
									text: $('#cateName').val()
								}]
						 });
						 category.loadDataGrid(node);
						 category.clearAll();
						 return;
				    },
					error:function(){
						alert('新增失败,请联系管理员!',2);
					}
			   });
	     };
	     category.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

category.update = function(){
	var treeObj = $('#dataTreeId');
    var fromObj=$('#dataForm');
    var node = treeObj.tree('getSelected');
    //1.判断是否有节点处于选中状态
    if(!node){
	   	 alert('没有树节点处于选中状态!',1);//树节点展开时，会清空form表单。正常情况下node不可能为空
	   	 return;
     }
    //2.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   
     //3.绑定数据
    var updateFn = function(returnData){
        var url = BasePath+'/category/put';
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.editor = returnData.loginName;
				param.edittm = returnData.currentDate19Str;
   			},
   			success: function(){
   				 alert('修改成功!');
   				 //4.保存成功自动刷新  刷新树--刷新右边表格
   				 var tempChildren = treeObj.tree('getChildren', node.target);
   				 if(tempChildren.length>0){
   					 treeObj.tree('reload', node.target);
   				 }else{
   					 treeObj.tree('update', {
   							target: node.target,
   							text: $('#cateName').val()
   					});
   				 }
   				 category.loadDataGrid(node);
   				 category.clearAll();
   				 return;
   		    },
   			error:function(){
   				alert('修改失败,请联系管理员!',2);
   			}
   	   });
    };
    category.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},updateFn);
};

category.deleteByNo = function(){
	var treeObj = $('#dataTreeId');
    var node = treeObj.tree('getSelected');
    //1.判断是否有节点处于选中状态
    if(!node){
	   	 alert('没有树节点处于选中状态!',1);//树节点展开时，会清空form表单。正常情况下node不可能为空
	   	 return;
     }    
	 
	 //2.判断是否选择了记录
	 var cateNo=$("#cateNo").val();
	 if(cateNo==null||cateNo==''){
		 alert('请选择要删除的记录!',1);
		 return;
	 }
	 
	 //3.判断是否是叶子节点
     var checkUrl=BasePath+'/category/get_count.json';
     var checkDataNo={"headCateNo":cateNo};
     if(category.checkExistFun(checkUrl,checkDataNo)){
    	  alert('该节点不是叶子节点!',1);
    	  return;
      }
	 
     //4.绑定数据
     var url = BasePath+'/category/delete';
	 var data={
			    "cateNo":$("#cateNo").val()
	  };
	 //5. 删除
	 category.ajaxRequest(url,data,function(result,returnMsg){
		 if(returnMsg=='success'){
			 //6.保存成功自动刷新  刷新树--刷新右边表格
			 treeObj.tree('remove', node.target);
			 category.loadDataGrid(node);
			 category.clearAll();
			 alert('删除成功!');
		 }else{
			 alert('删除失败,请联系管理员!',2);
		 }
	}); 
};

category.clearAll = function(){
	$('#dataForm').form("clear");
	$("#cateNo").attr('readOnly',false);
};

category.initDataTree = function(){
	$('#dataTreeId').tree({     
        url:BasePath+'/category/queryCategoryTree.htm',  
        onClick : function (node) {
        	if(node.state && node.state=='closed'){
        		$(this).tree('expand', node.target);
        	}else{
            	category.clearAll();
            	category.loadDataGrid(node);
        	}
        },
        onExpand : function (node) {
        	$(this).tree('select', node.target);
        	category.clearAll();
        	category.loadDataGrid(node);
        }
    });
};

category.initStatus = function(){
	$('#status').combobox({
	     valueField:"label",
	     textField:"value",
	     data:category.statusData,
	     panelHeight:"auto"
	  });
};

category.initLevel= function(){
	$('#cateLevelid').combobox({
	     valueField:"label",
	     textField:"value",
	     data:category.levelData,
	     panelHeight:"auto"
	  });
};

category.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	 });
	
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"valueAndText",
	     panelHeight:150,
	     loadFilter:function(data){
			if(data){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.valueAndText = tempData.itemnamedetail;
	       		 }
   	 		}
			return data;
	   	} 
	 });
};

category.initGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

$(document).ready(function(){
	//category.initDataTree();//初始化树数据
//	category.initLevel();//初始化类别级别列表数据
	category.initStatus();//初始化状态列表数据
	//category.initBrand();//初始化所属品牌数据
	var queryParams = {headCateNo:'-1'};
	$('#dataGridJG').datagrid({'url':BasePath+'/category/list.json?sysNo=&cateName=&headCateNo=-1','title':"类别列表",'pageNumber':1,queryParams:queryParams});
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNoCondition]',$('#searchForm'))},
			{"sysNoObj":$('input[id=sysNo]',$('#dataForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
});

//导出
exportExcel=function(){

	exportExcelBaseInfo('dataGridJG','/category/do_export.htm','类别管理信息导出');
};
