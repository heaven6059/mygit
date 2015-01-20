
var billLocateRule = {};
billLocateRule.operStatus='0';//0新增,1修改

//加载Grid数据Utils
billLocateRule.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//关闭
billLocateRule.closeWindow = function(id){
	$('#'+id).window('close');  
};


//加载dataGrid
billLocateRule.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bill_locate_rule/list.json?locno='+billLocateRule.locno,'pageNumber':1 });
};


//搜索数据
billLocateRule.searchData=function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_locate_rule/list.json?locno='+billLocateRule.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	billLocateRule.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};

//搜索商品类别
billLocateRule.searchCategory=function(){
	
	var fromObjStr=convertArray($('#categorySearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_locate_rule_dtl/listCategoryFilter.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	
	//如果是修改储位策略必须过滤明细中已有的类别
	var inserted = $('#dataGridCategory').datagrid('getChanges', "inserted");
	var deleted = $('#dataGridCategory').datagrid('getChanges', "deleted");
	reqParam['inserted'] = JSON.stringify(inserted);
	reqParam['deleted'] = JSON.stringify(deleted);
	reqParam['locno'] = billLocateRule.locno;
	if(billLocateRule.operStatus=='1'){
		reqParam['ruleNo'] = $('#ruleNo').val();
	}else{
		reqParam['ruleNo'] = "";
	}
	billLocateRule.loadGridDataUtil('dataGridCategoryOpen', queryMxURL, reqParam);
	
};

//清除查询条件
billLocateRule.searchClear = function(id){
	$('#'+id).form("clear");
};

//新增
billLocateRule.add = function(){
	$('#showAddDialog').window({title:'新增'});  
	$("#showAddDialog").window('open');
	billLocateRule.loadAdd();
};

//修改
billLocateRule.edit = function(ruleNo,type){
	$('#showAddDialog').window({title:'查看/修改'});  
	$("#showAddDialog").window('open');
	billLocateRule.loadUpdate(ruleNo,type);
};

//选择商品类别
billLocateRule.showAddCategory = function(){
	$("#openCategory").window('open');
	$('#dataGridCategoryOpen').datagrid('loadData', { total: 0, rows: [] });
};


//商品类别选择
billLocateRule.selectCategory = function(){
	
	var checkItems = $('#dataGridCategoryOpen').datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请选择商品类别!");
		return;
	}
	
	
	var checkResult = false;
	var allDtlRows = $('#dataGridCategory').datagrid('getRows');
	
	//所有行颜色改为白色
	var allCateGoryRows = $('#dataGridCategoryOpen').datagrid('getRows');
	$.each(allCateGoryRows,function(index,item){
		$("#datagrid-row-r3-2-"+index).css("background","white"); 
	});
	
	//验证退仓收货单是否有重复箱号
	$.each(checkItems,function(index,item){
		var rowIndex = $('#dataGridCategoryOpen').datagrid('getRowIndex',item);
		if(allDtlRows.length > 0){
			$.each(allDtlRows,function(index2,item2){
				if(item.cateNo == item2.ruleCateno){
					$("#datagrid-row-r3-2-"+rowIndex).css("background","red"); 
					checkResult = true;
				}
			});
		}
	});
	
	if(checkResult){
		alert("已经存在相同的商品类别");
		return;
	}
	
	$.each(checkItems,function(index,item){
		var reqParas = {ruleCateno:item.cateNo,ruleCatename:item.cateName};
		$('#dataGridCategory').datagrid('appendRow',reqParas);
	});
	
	billLocateRule.closeWindow('openCategory');
	
};


//商品类别删除
billLocateRule.delCategory = function(){
	var checkItems = $('#dataGridCategory').datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请选择商品类别!");
		return;
	}
	$.each(checkItems,function(index,item){
		//获取某行的行号
		var i = $('#dataGridCategory').datagrid('getRowIndex',checkItems[index]);
		$('#dataGridCategory').datagrid('deleteRow',i);
	});
};

//查看数据明细
var categoryList = [];
billLocateRule.loadDetail = function(ruleNo){
	categoryList = [];
	var reqParam={ruleNo:ruleNo,locno:billLocateRule.locno};
	var url = BasePath+'/bill_locate_rule/get';
	ajaxRequest(url,reqParam,function(data){
		$('#dataForm').form('load',data);
		$('#curRuleVal').val(data.ruleFirst);
	});
	
	var urlDtl = BasePath+'/bill_locate_rule_dtl/list.json?locno='+billLocateRule.locno;
	billLocateRule.loadGridDataUtil('dataGridCategory', urlDtl, reqParam);
	
//	var url_dtl = BasePath+'/bill_locate_rule_dtl/get_biz';
//	ajaxRequestAsync(url_dtl,reqParam,function(data){
//		for(var i = 0;i < data.length;i++){
//			var r = {ruleCateno:data[i].ruleCateno};
//			categoryList[categoryList.length]=r;
//		}
//	});
	
};

//修改储位策略
billLocateRule.toUpdate = function(){
	
	// 获取勾选的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}
	
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	
	$.each(checkedRows,function(index,item){
		billLocateRule.edit(item.ruleNo,'0');
	});
	
};

//新增时执行
billLocateRule.loadAdd = function(){
	$("#ruleNo").attr('readOnly',false);
	billLocateRule.clearAll();
	billLocateRule.operStatus='0';
	billLocateRule.showHideBtn('0');
	$('#dataGridCategory').datagrid('loadData', { total: 0, rows: [] });
	//billLocateRule.loadCategoryDetail('0');
};

//修改时执行
billLocateRule.loadUpdate = function(ruleNo,type){
	$("#ruleNo").attr('readOnly',true);
	billLocateRule.loadDetail(ruleNo);
	billLocateRule.operStatus='1';
	billLocateRule.showHideBtn('1');
	//billLocateRule.loadCategoryDetail('1');
};


//清空文本
billLocateRule.clearAll = function(){
	$("#dataForm")[0].reset();
};


//隐藏显示按钮
billLocateRule.showHideBtn = function(type){
	if(type=='0'){
		$('#info_add').show();
		$('#info_save').hide();
	}else if(type=='1'){
		$('#info_add').hide();
		$('#info_save').show();
	} else {
		$('#info_add').hide();
		$('#info_save').hide();
	}
};


//新增保存
billLocateRule.doSave = function(type){
	
	var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return ;
    }
     
    var allRows = $("#dataGridCategory").datagrid("getRows");// 获取所有勾选checkbox的行
	if(allRows.length < 1){
		alert('商品类别信息不能为空!',1);
		return;
	}
     
    //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
	if(billLocateRule.operStatus == "0"){
		var checkUrl=BasePath+'/bill_locate_rule/get_count.json';
	    var checkDataNo={"ruleNo":$("#ruleNo").val(),locno:billLocateRule.locno};
	    if(billLocateRule.checkExistFun(checkUrl,checkDataNo)){
	    	alert('规则编码已存在,不能重复!',1);
	    	return;
	    }
	}
	
     
	 //3. 保存
	 wms_city_common.loading("show","正在保存......");
     var saveFn = function(returnData){
    	var url = BasePath+'/bill_locate_rule/addBillLocateRule';
    	if(type=='1'){
    		url = BasePath+'/bill_locate_rule/saveBillLocateRule';
    	}
         fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					
//					var dataList = [];
//					$.each(checkedRows, function(index, item){
//						var reqParam = {cateNo:item.cateNo,cateName:item.cateName};
//						dataList[dataList.length] = reqParam;
//					}); 
					
					var inserted = $('#dataGridCategory').datagrid('getChanges', "inserted");
					var deleted = $('#dataGridCategory').datagrid('getChanges', "deleted");
					
					param.createtm = returnData.currentDate19Str;
					param.creator = returnData.loginName;
					param.editor = returnData.loginName;
					param.edittm = returnData.currentDate19Str;
					param.inserted = JSON.stringify(inserted);
					param.deleted = JSON.stringify(deleted);
					param.operStatus = billLocateRule.operStatus;
					param.locno = billLocateRule.locno;
				},
				success: function(data){
					wms_city_common.loading();
					if(data){
						//4.保存成功,清空表单
						 alert('保存成功!');
						 billLocateRule.closeWindow('showAddDialog');
						 billLocateRule.clearAll();
						 billLocateRule.loadDataGrid();
						 return;
					}else{
						alert('新增失败,请联系管理员!',2);
					}
			    },
				error:function(){
					wms_city_common.loading();
					alert('新增失败,请联系管理员!',2);
				}
		   });
     };
     ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};


//删除
billLocateRule.doDel = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		var r = {locno:billLocateRule.locno,ruleNo:item.ruleNo};
        		keyStr[keyStr.length] = r;
        	});               
            //2.绑定数据
            var url = BasePath+'/bill_locate_rule/deleteLocateRule';
        	var data={
        	    "keyStr":JSON.stringify(keyStr)
        	};
        	//3. 删除
        	wms_city_common.loading("show","正在删除......");
        	ajaxRequest(url,data,function(result){
        		wms_city_common.loading();
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
        			 billLocateRule.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

//商品类别列表
billLocateRule.loadCategoryDetail = function(type){
	$('#dataGridCategory').datagrid({
		'url':BasePath+'/category/list.json',
		onLoadSuccess:function(data){
			if(type == "1"){
				var opts = $('#dataGridCategory').datagrid('getRows');  
				for(var i=0;i<opts.length;i++){
					for(var j=0;j<categoryList.length;j++){
						if(categoryList[j].ruleCateno==opts[i].cateNo){
							$('#dataGridCategory').datagrid('checkRow',i);
						}
					}
				}
			}
			return data;
        }
	});
};


//验证编码的唯一性
billLocateRule.checkExistFun = function(url,checkColumnJsonData){
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


//初始化状态
billLocateRule.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCon"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=LOCATE_RULE_STATUS',
			{},
			billLocateRule.Status,
			null);
};


//初始化用户信息
billLocateRule.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		billLocateRule.locno = data.locno;
	}); 
};


//========================初始化信息======================================
$(document).ready(function(){
	billLocateRule.initCurrentUser();
	billLocateRule.initStatus();
	//单选复选框
	$(':checkbox[name=ruleFirst]').each(function(){
		$(this).click(function(){
			var curRuleVal = $('#curRuleVal').val();
			if(curRuleVal==$(this).val()){
				$(':checkbox[name=ruleFirst]').removeAttr('checked');
				$('#curRuleVal').val("0");
			}else{
				$('#curRuleVal').val($(this).val());
			}
            if($(this).attr('checked')){
            	$(':checkbox[name=ruleFirst]').removeAttr('checked');
                $(this).attr('checked','checked');
            }
         });
    });
});