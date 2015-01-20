
var billStoreRule = {};
billStoreRule.operStatus='0';//0新增,1修改

//billStoreRule.statusData = [{    
//    "value":"0",
//    "text":"0->禁用",
//},{    
//    "value":"1",    
//    "text":"1->启用"
//}];

//店分组依据
billStoreRule.storeBasicData = [{    
    "value":"1",
    "text":"预发货量"
},{    
    "value":"2",    
    "text":"门店编码"
}];

//排序规则
billStoreRule.sortData = [{    
    "value":"1",
    "text":"大->小"
},{    
    "value":"2",    
    "text":"小->大"
}];

//箱分货依据
billStoreRule.cargoBasicData = [{    
    "value":"1",
    "text":"总订货量"
},{    
    "value":"2",    
    "text":"门店销售级别"
}];

//箱类型
billStoreRule.boxTypeData = [{    
    "value":"1",
    "text":"独码优先"
},{    
    "value":"2",    
    "text":"配码优先"
}];

//箱排序规则
billStoreRule.boxSortData = [{    
    "value":"1",
    "text":"大箱号先分配"
},{    
    "value":"2",    
    "text":"小箱号先分配"
}];


//箱分组依据
billStoreRule.boxBasicData = [{    
    "value":"1",
    "text":"划入货量大组"
},{    
    "value":"2",    
    "text":"划入门店数多"
}];


//序号
billStoreRule.ruleNoData = [
{"value":"1","text":"1","label":"001"},
{"value":"2","text":"2","label":"001"},
{"value":"3","text":"3","label":"002"},
{"value":"4","text":"4","label":"002"},
{"value":"5","text":"5","label":"003"},
{"value":"6","text":"6","label":"003"},
{"value":"7","text":"7","label":"003"},
{"value":"8","text":"8","label":"003"},
{"value":"9","text":"9","label":"004"},
{"value":"10","text":"10","label":"004"},
{"value":"11","text":"11","label":"004"},
{"value":"12","text":"12","label":"004"},
{"value":"14","text":"14","label":"005"},
{"value":"15","text":"15","label":"005"},
{"value":"16","text":"16","label":"005"},
{"value":"17","text":"17","label":"006"},
{"value":"18","text":"18","label":"006"},
{"value":"19","text":"19","label":"006"},
{"value":"20","text":"20","label":"006"}
];

//模板
billStoreRule.tempNoData = [
{"value":"001","text":"001"},
{"value":"002","text":"002"},
{"value":"003","text":"003"},
{"value":"004","text":"004"},
{"value":"005","text":"005"},
{"value":"006","text":"006"}
];


//ajax公共请求
billStoreRule.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//加载Grid数据Utils
billStoreRule.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//保存
billStoreRule.save = function(){
	 if(billStoreRule.operStatus=='1'){
		 billStoreRule.do_update();
	 }else{
		 billStoreRule.do_save();
	 }
};

//新增
billStoreRule.add = function(){
	$('#showAddDialog').window({title:'新增'});  
	$("#showAddDialog").window('open');
	billStoreRule.isDisableGroup(true);
	$("#tempNo").combobox('enable');
	$("#ruleNo").combobox('enable');
	billStoreRule.clearAll();
	billStoreRule.operStatus='0';
	billStoreRule.showHideBtn('0');
};

//修改
billStoreRule.edit = function(item,type){
	
	billStoreRule.clearAll();
	$('#showAddDialog').window({title:'查看/修改'});  
	$("#showAddDialog").window('open');
	$("#tempNo").combobox('disable');
	$("#ruleNo").combobox('disable');
	billStoreRule.loadDetail(item);
	billStoreRule.operStatus='1';
	billStoreRule.showHideBtn(type);
};

//查看数据明细
billStoreRule.loadDetail = function(item){
	var ruleNo = item.ruleNo;
	var tempNo = item.tempNo;
	var reqParam={"locno":billStoreRule.locno,"tempNo":tempNo,"ruleNo":ruleNo};
	var url = BasePath+'/bill_store_rule/get';
	billStoreRule.ajaxRequest(url,reqParam,function(data){
		if(data.boxFlag == '1'){
			$('#boxFlag').attr('checked',true);
		}
		if(data.storeSort!=null&&data.storeSort!=''){
			billStoreRule.isDisableGroup(false);
		}
		
		if(data.groupA == '0')data.groupA = '';
		if(data.groupB == '0')data.groupB = '';
		if(data.groupC == '0')data.groupC = '';
		if(data.groupD == '0')data.groupD = '';
		if(data.groupE == '0')data.groupE = '';
		if(data.groupF == '0')data.groupF = '';
		if(data.groupG == '0')data.groupG = '';
		if(data.groupH == '0')data.groupH = '';
		if(data.groupI == '0')data.groupI = '';
		if(data.groupJ == '0')data.groupJ = '';
		
		$('#dataForm').form('load',data);
	});
	//$('#dataForm').form('load',BasePath+'/bill_store_rule/get?ruleNo='+ruleNo);
};


//修改退仓收货单
billStoreRule.toUpdate = function(){
	
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
		billStoreRule.edit(item,'0');
	});
	
};


//隐藏显示按钮
billStoreRule.showHideBtn = function(type){
	if(type=='1'){
		$('#info_save').hide();
	}else{
		$('#info_save').show();
	}
};


//加载dataGrid
billStoreRule.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bill_store_rule/list.json?locno='+billStoreRule.locno,'pageNumber':1});
};

//关闭
billStoreRule.closeWindow = function(){
	$('#showAddDialog').window('close');  
};


//新增
billStoreRule.do_save = function(){
	 var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
     var checkUrl=BasePath+'/bill_store_rule/get_count.json';
     var checkDataNo={"locno":billStoreRule.locno,
    		 "ruleNo":$("#ruleNo").combobox('getValue'),
    		 "tempNo":$("#tempNo").combobox('getValue')};
     if(billStoreRule.checkExistFun(checkUrl,checkDataNo)){
    	  alert('规则编码已存在,不能重复!',1);
    	  return;
     }
     var checkRuleName={
    		 "locno":billStoreRule.locno,
    		 "ruleNameCheck":$("#ruleName").val()};
     if(billStoreRule.checkExistFun(checkUrl,checkRuleName)){
    	 alert('规则名称已存在,不能重复!',1);
    	 return;
     }
     
     //验证分组排序
     var storeSort = $('#storeSort').combobox('getValue');
     if(storeSort!=null&&storeSort!=''){
    	 var checkGroup = billStoreRule.checkGroupRange(storeSort);
         if(checkGroup){
        	 return;
         }
     }
		 
	 //3. 保存
     var saveFn = function(returnData){
         var url = BasePath+'/bill_store_rule/post?locno='+billStoreRule.locno;
         wms_city_common.loading("show");
         fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					//是否拼箱为一组
					var boxFlag = "0";
					if($("#boxFlag").attr("checked")=='checked'){
						boxFlag = "1";
					}
					param.boxFlag = boxFlag;
					//param.createtm = returnData.currentDate19Str;
					//param.creator = returnData.loginName;
					//param.editor = returnData.loginName;
					//param.edittm = returnData.currentDate19Str;
				},
				success: function(){
					wms_city_common.loading();
					 //4.保存成功,清空表单
					 alert('新增成功!');
					 billStoreRule.closeWindow();
					 billStoreRule.clearAll();
					 billStoreRule.loadDataGrid();
					 return;
			    },
				error:function(){
					wms_city_common.loading();
					alert('新增失败,请联系管理员!',2);
				}
		   });
     };
     billStoreRule.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

//修改
billStoreRule.do_update = function(){
	var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    var checkUrl=BasePath+'/bill_store_rule/get_count.json';
    var checkRuleName={"locno":billStoreRule.locno,
    		"ruleNoCheck":$("#ruleNo").combobox('getValue'),
    		"ruleNameCheck":$("#ruleName").val()};
    if(billStoreRule.checkExistFun(checkUrl,checkRuleName)){
   	 alert('规则名称已存在,不能重复!',1);
   	 return;
    }
    
    //验证分组排序
    var storeSort = $('#storeSort').combobox('getValue');
    if(storeSort!=null&&storeSort!=''){
   	 var checkGroup = billStoreRule.checkGroupRange(storeSort);
        if(checkGroup){
       	 return;
        }
    }
    
	//3. 保存
    var saveFn = function(returnData){
        var url = BasePath+'/bill_store_rule/put?locno='+billStoreRule.locno;
        wms_city_common.loading("show");
        fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					
					//是否拼箱为一组
					var boxFlag = "0";
					if($("#boxFlag").is(':checked')){
						boxFlag = "1";
					}
					param.boxFlag = boxFlag;
					
					//如果为空，规则的值为0
					for(var i = 1;i <= 10; i++){
						var groupValue = $('#group'+i).val();
						if(groupValue==''){
							$('#group'+i).numberbox('setValue',0);
						}
					}
					
					//param.editor = returnData.loginName;
					//param.edittm = returnData.currentDate19Str;
				},
				success: function(){
					wms_city_common.loading();
					 //4.保存成功,清空表单
					 alert('修改成功!');
					 billStoreRule.closeWindow();
					 billStoreRule.clearAll();
					 billStoreRule.loadDataGrid();
					 return;
			    },
				error:function(){
					wms_city_common.loading();
					alert('修改失败,请联系管理员!',2);
				}
        });
    };
    billStoreRule.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
};

//删除
billStoreRule.do_del = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var keyStr = [];
        	var tempStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.ruleNo);
        		tempStr.push(item.tempNo);
        	});               
            //2.绑定数据
            var url = BasePath+'/bill_store_rule/deleteStoreRule';
        	var data={
        			"locno":billStoreRule.locno,
        			"keyStr":keyStr.join(","),
        			"tempStr":tempStr.join(",")
        	};
        	//3. 删除
        	wms_city_common.loading("show");
        	billStoreRule.ajaxRequest(url,data,function(result){
        		wms_city_common.loading(); 
        		if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
        			 billStoreRule.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

//验证编码的唯一性
billStoreRule.checkExistFun = function(url,checkColumnJsonData){
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

//验证店分组范围
billStoreRule.checkGroupRange = function(storeSort){
	
	for(var i = 1;i <= 10; i++){
		var groupAgo = $('#group'+i).val();
		var groupAgoV = billStoreRule.returnGroupValue(i);
		
		if(groupAgo==''||groupAgo==null){
			continue;
		}
		
		if(parseInt(groupAgo) == 0){
			alert(groupAgoV+"的值不能为0");
			return true;
		}
		
		var isNull = false;
		for(var j = i;j <= 10; j++){
			var groupAfter = $('#group'+j).val();
			var groupAfterV = billStoreRule.returnGroupValue(j);
			
			if(groupAfter==''||groupAfter==null){
				isNull = true;
				continue;
			}
			
			if(isNull == true){
				alert("请按照顺序输入分组规则");
				return true;
			}
			
			if(parseInt(groupAfter) <= 0){
				continue;
			}
			
			//从大到小
//			if(storeSort == "1"){
//				if(parseInt(groupAgo) < parseInt(groupAfter)){
//					alert(groupAfterV+"的值不能大于"+groupAgoV+"的值");
//					$('#group'+j)[0].focus();
//					return true;
//				}
//			}
//			//从小到大
//			if(storeSort == "2"){
//				if(parseInt(groupAgo) > parseInt(groupAfter)){
//					alert(groupAfterV+"的值不能小于"+groupAgoV+"的值");
//					$('#group'+j)[0].focus();
//					return true;
//				}
//			}
		}
		
	}
	return false;
};

//分组代号
billStoreRule.returnGroupValue = function(value){
	if(value=="1")return 'A';
	if(value=="2")return 'B';
	if(value=="3")return 'C';
	if(value=="4")return 'D';
	if(value=="5")return 'E';
	if(value=="6")return 'F';
	if(value=="7")return 'G';
	if(value=="8")return 'H';
	if(value=="9")return 'I';
	if(value=="10")return 'J';
};

//搜索数据
billStoreRule.searchData=function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_store_rule/list.json?locno='+billStoreRule.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	billStoreRule.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};

//清除查询条件
billStoreRule.searchClear = function(){
	$('#searchForm').form("clear");
	//$("#searchForm")[0].reset();   
};

//清空文本
billStoreRule.clearAll = function(){
	$('#boxFlag').attr('checked',false);
	$("#dataForm")[0].reset();
	$("#tempNo").combobox('clear');
	$("#ruleNo").combobox('clear');
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

//是否禁用分组文本
billStoreRule.isDisableGroup = function(isTrue){
	for(var i = 1;i <= 10;i++){
		$("#group"+i).attr('readOnly',isTrue);
//		if(isTrue == true){
//			$("#group"+i).numberbox('setValue', 0);
//		}
	}
};


//初始化状态信息
billStoreRule.initStatus = function(){
	
//	//状态
//	$('#status').combobox({
//	    valueField:"value",
//	    textField:"text",
//	    data:billStoreRule.statusData,
//	    editable:false,
//	    panelHeight:"auto"
//	});
//	
//	//状态
//	$('#statusCon').combobox({
//	    valueField:"value",
//	    textField:"text",
//	    data:billStoreRule.statusData,
//	    editable:false,
//	    panelHeight:"auto"
//	});
	wms_city_common.comboboxLoadFilter(
			["status","statusCon"],
			null,
			null,
			null,
			true,
			[false,true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_StoreRule',
			{},
			null,
			null);
	
	//初始化店分组依据
	$('#storeBasic').combobox({
	    valueField:"value",
	    textField:"text",
	    data:billStoreRule.storeBasicData,
	    editable:false,
	    panelHeight:"auto"
	});
	//初始化店排序规则
	$('#storeSort').combobox({
	    valueField:"value",
	    textField:"text",
	    data:billStoreRule.sortData,
	    editable:false,
	    panelHeight:"auto",
	    onSelect:function(record){
	    	billStoreRule.isDisableGroup(false);
	    }
	});
	//箱分货依据
	$('#cargoBasic').combobox({
	    valueField:"value",
	    textField:"text",
	    data:billStoreRule.cargoBasicData,
	    editable:false,
	    panelHeight:"auto"
	});
	//箱号
	$('#boxSort').combobox({
	    valueField:"value",
	    textField:"text",
	    data:billStoreRule.boxSortData,
	    editable:false,
	    panelHeight:"auto"
	});
	//箱类型
	$('#boxType').combobox({
	    valueField:"value",
	    textField:"text",
	    data:billStoreRule.boxTypeData,
	    editable:false,
	    panelHeight:"auto"
	});
	//店分组依据
	$('#cargoSort').combobox({
	    valueField:"value",
	    textField:"text",
	    data:billStoreRule.sortData,
	    editable:false,
	    panelHeight:"auto"
	});
	//一箱多组
	$('#boxBasic').combobox({
	    valueField:"value",
	    textField:"text",
	    data:billStoreRule.boxBasicData,
	    editable:false,
	    panelHeight:"auto"
	});
};

//billStoreRule.initTempNo = function (tempNoId,ruleNoId){
	//模板编码
//	$('#'+tempNoId).combobox({
//		 data:billStoreRule.tempNoData,
//	     valueField:"value",
//	     textField:"text",
//	     panelHeight:"auto",
//	     onSelect:function(d){
//			 billStoreRule.initRuleNo(ruleNoId,d.value);
//		 }
//	});
//};


billStoreRule.initRuleNo = function(ruleNoId,tempNoId){
	var tempNo = $("#"+tempNoId).combobox('getValue');
	var rowArray = [];
	for(var i = 0;i<billStoreRule.ruleNoData.length;i++){
		var tempData = billStoreRule.ruleNoData[i];
		if(tempData.label == tempNo){
			rowArray[rowArray.length]={text:tempData.text,value:tempData.value};
		}
	}
	$('#'+ruleNoId).combobox('clear');
	if(rowArray.length > 0) {
		$('#'+ruleNoId).combobox('select',rowArray[0].value);
		$('#'+ruleNoId).combobox('loadData',rowArray);  // 使用新的URL重新载入列表数据
	} else {
		$('#'+ruleNoId).combobox("loadData",[]);
	}
};

//JS初始化执行
$(document).ready(function(){
	billStoreRule.initCurrentUser();
	wms_city_common.comboboxLoadFilter(
			["tempNoCon","tempNo"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_StoreRule_tempNo',
			{},
			null,
			null);
	
	billStoreRule.initStatus();
	$('#info_save').click(billStoreRule.save);
});
//========================用户 信息========================
billStoreRule.locno;
billStoreRule.user = {};
billStoreRule.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billStoreRule.locno = data.locno;
		billStoreRule.user = data;
	});
};
//========================用户 信息END========================