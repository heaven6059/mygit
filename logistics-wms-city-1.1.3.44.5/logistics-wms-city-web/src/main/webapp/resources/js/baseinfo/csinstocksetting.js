var csinstocksetting = {};
csinstocksetting.locno;
//状态
csinstocksetting.status = {};

csinstocksetting.statusSelectData= [
 {"value":"0","text":"停用"},
 {"value":"1","text":"启用"}
];

$(document).ready(function(){
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNo"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[false],
			BasePath+'/entrust_owner/get_biz',
			{},
			null,
			null);
	//初始化仓别
	csinstocksetting.loadLoc();
	//加载列表数据
	//csinstocksetting.searchArea();
	//初始化状态下拉框
	wms_city_common.comboboxLoadFilter(
			["search_status","status"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_INSTOCKSETTING_STATUS',
			{},
			csinstocksetting.status,
			null);
	
	$("#info_add").click(csinstocksetting.save_do);
	$("#info_edit").click(csinstocksetting.edit_do);
	
	//上架范围
	$("input[name='instockScope']").change(csinstocksetting.delAllCellLine);
	
	//生效对象
	$("input[name='detailType']").change(csinstocksetting.delAllItemLine);
	$("#info_add").show();
	$("#info_edit").hide();
	//$("input[name='setType']").change(csinstocksetting.clearDetailType);
	//初始化仓区
	wms_city_common.comboboxLoadFilter(
			["wareNoByAreaCon","wareNoByStockCon","wareNoByCellCon"],
			'wareNo',
			'wareName',
			'valueAndText',
			false,
			[true,true,true],
			BasePath+'/cm_defware/get_biz',
			{locno:csinstocksetting.locno},
			null,
			null);
});

//选择使用场景，清空生效对象中的数据
csinstocksetting.clearDetailType = function(){
	$("#itemDetail").datagrid('loadData', { total: 0, rows: [] });
};

//加载仓别信息
csinstocksetting.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			csinstocksetting.locno = data.locno;
		}
	});
};
//状态
csinstocksetting.statusFormatter = function(value, rowData, rowIndex){
	return csinstocksetting.status[value];
};
//适用场景
csinstocksetting.setType = {"0":"进货上架","1":"退仓上架"};
csinstocksetting.setTypeFormatter = function(value, rowData, rowIndex){
	return csinstocksetting.setType[value];
};

//储位行对象
function CellTr(data){
	this.tr = $("<tr></tr>");
	$('<td width="5%"><input type="checkBox" name="cellcheck"></td>').appendTo(this.tr );
	var inputTd = $('<td width="40%"></td>').appendTo(this.tr );
	$('<input style="width:100px" name="cellCode" value="'+data.CODE+'">').bind("blur",this.getInfo).appendTo(inputTd);
	$('<td width="60%">'+data.NAME+'</td>').appendTo(this.tr );
};
CellTr.prototype = {
	"getInfo":function(){
		var curObj = $(this);
		var curValue = curObj.val();
		if(curValue!=""){
			var flag = true;
			$("input[name='cellCode']").not(curObj).each(function () {
			    if (curObj.val() == $(this).val()){
			    	 alert("编号已经存在，请重新输入");
					 curObj.parent().parent().children().eq(2).html("");
					 curObj.val("");
			    	 flag = false ;
			    }
   			 });
   			 if(!flag){
   			 	return;
   			 }
			var curSelect = $("input[name='instockScope']:checked");
			$.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{
					"type":curSelect.val(),
					"locNo":csinstocksetting.locno,
					"code":curValue
				},
				dataType : "json",
				url:BasePath+'/cs_instock_setting/selectCellByCode',
				success : function(data) {
					if(data!=null){
						curObj.parent().parent().children().eq(2).html(data.name);
					}else{
						alert("输入的编号有误，请重新输入");
						curObj.parent().parent().children().eq(2).html("");
						curObj.val("");
					}
				}
			});
		}
	}
};

//新增一行储位信息
csinstocksetting.addCellLine  = function(){
	var data = {"CODE":"","NAME":""};
	new CellTr(data).tr.appendTo($("#myCellTable"));
};
//删除一行储位信息
csinstocksetting.delCellLine = function(){
	$("input[name='cellcheck']:checked").parent().parent().remove();
};
//删除所有储位信息
csinstocksetting.delAllCellLine = function(){
	//$("input[name='cellcheck']").parent().parent().remove();
	var settingNo = $("#settingNo").val();
	if(settingNo==""){
		$("#cellDetail").datagrid({data:{ total: 0, rows: [] }});
		return;
	}
	var instockScope = $("input[name='instockScope']:checked").val();
	$.ajax({
		  cache: true,
		  async : true,
		  type: 'POST',
		  url: BasePath+'/cs_instock_setting/selectCellBySettingNo',
		  data:{
			"locNo":csinstocksetting.locno,
			"settingNo":settingNo,
			"type":instockScope
		  },
		 
		  success: function(data){
		  	var myData = data.data;
		  	if(myData!=null){
		  		csinstocksetting.loadDatagrid('cellDetail', data);
		  	}else{
		  		$("#cellDetail").datagrid({data:{ total: 0, rows: [] }});
		  	}
		  }
	});
};


//新增一行
csinstocksetting.addItemLine = function(){
	var data = {"CODE":"","NAME":""};
	new MyTr(data).tr.appendTo($("#mytable"));
};
csinstocksetting.searchClear = function(formId){
	$("#"+formId).form("clear");
};
//生效对象新增弹窗
csinstocksetting.openItemWindow = function(){
	var itemType = $("input[name='detailType']:checked").val();
	var windowName = '';
	if(itemType == 0){
		windowName = 'Brand';
	}else if(itemType == 1){
		windowName = 'Category';
	}else if(itemType == 2){
		windowName = 'Item';
	}
	$("#dataGrid"+windowName+"Open").datagrid('loadData', { total: 0, rows: [] });
	csinstocksetting.searchClear("search"+windowName+"Form");
	$("#openUI"+windowName).window("open");
};
//上架范围新增弹窗
csinstocksetting.openCellWindow = function(){
	var cellType = $("input[name='instockScope']:checked").val();
	var windowName = '';
	if(cellType == 1){
		windowName = 'Area';
	}else if(cellType == 2){
		windowName = 'Stock';
	}else if(cellType == 3){
		windowName = 'Cell';
	}
	$("#dataGrid"+windowName+"Open").datagrid('loadData', { total: 0, rows: [] });
	csinstocksetting.searchClear("search"+windowName+"Form");
	$("#openUI"+windowName).window("open");
};
//生效对象删除选择行
csinstocksetting.delItem = function(){
	var checkItems = $('#itemDetail').datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请选择数据!");
		return;
	}
	$.each(checkItems,function(index,item){
		//获取某行的行号
		var i = $('#itemDetail').datagrid('getRowIndex',checkItems[index]);
		$('#itemDetail').datagrid('deleteRow',i);
	});
};
//上架范围删除选择行
csinstocksetting.delCell = function(){
	var checkItems = $('#cellDetail').datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请选择数据!");
		return;
	}
	$.each(checkItems,function(index,item){
		//获取某行的行号
		var i = $('#cellDetail').datagrid('getRowIndex',checkItems[index]);
		$('#cellDetail').datagrid('deleteRow',i);
	});
};
//选择窗口查询
csinstocksetting.search = function(type){
	var formObj = $("#search"+type+"Form");
	var url = '';
	var fromObjStr=convertArray(formObj.serializeArray());
	var params = eval("(" +fromObjStr+ ")");
	params.areaAttribute = "0";
	params.attributeType = "0";
	params.areaUsetypeArray = "'1','3','6'";
	params.joinArea = "true";
	
	if(type == 'Brand'){
		url = BasePath+'/brand/list.json?locno='+csinstocksetting.locno;
	}else if(type == 'Category'){
		url = BasePath+'/category/list.json?locno='+csinstocksetting.locno;
	}else if(type == 'Item'){
		url = BasePath+'/item/list.json?locno='+csinstocksetting.locno;
	}else if(type == 'Area'){
		url = BasePath+'/cm_defarea/list.json?locno='+csinstocksetting.locno;
	}else if(type == 'Stock'){
		url = BasePath+'/cm_defstock/list.json?locno='+csinstocksetting.locno;
	}else if(type == 'Cell'){
		url = BasePath+'/cm_defcell/list.json?locno='+csinstocksetting.locno;
	}
	
    $( "#dataGrid"+type+"Open").datagrid( 'options' ).queryParams= params;
    $( "#dataGrid"+type+"Open").datagrid( 'options' ).url=url;
    $( "#dataGrid"+type+"Open").datagrid( 'load' );
};
csinstocksetting.confirm = function(pType,cType){
	var checkedRows = $("#dataGrid"+cType+"Open").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的记录!',1);
		return;
	}
	var codeStr = '';
	var nameStr = '';
	if(cType == 'Brand'){
		codeStr = 'brandNo';
		nameStr = 'brandName';
	}else if(cType == 'Category'){
		codeStr = 'cateNo';
		nameStr = 'cateName';
	}else if(cType == 'Item'){
		codeStr = 'itemNo';
		nameStr = 'itemName';
	}else if(cType == 'Area'){
		codeStr = 'wareNo,areaNo';
		nameStr = 'areaName';
	}else if(cType == 'Stock'){
		codeStr = 'wareNo,areaNo,stockNo';
		nameStr = 'stockName';
	}else if(cType == 'Cell'){
		codeStr = 'cellNo';
		nameStr = 'cellName';
	}
	var codeStrArray = codeStr.split(',');
	var codeStrLen = codeStrArray.length; 
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条数据吗？", function (r){  
		if (r) {
			wms_city_common.loading("show");
			$.each(checkedRows, function(index, item){
				var code = '';
				var name = item[nameStr];
				for(var i=0;i<codeStrLen;i++){
					code += item[codeStrArray[i]];
				}
				var  rowData = {
						"CODE":code,
						"NAME":name
				};
				csinstocksetting.insertRowAtEnd(pType+"Detail",rowData);
				
				$('#openUI'+cType).window('close');
				
			}); 
			wms_city_common.loading();
		}
	});
};
csinstocksetting.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
	return  tempIndex;
};
csinstocksetting.selectAreaNoForSearchByArea = function(){
	var locno = csinstocksetting.locno;
	var wareNo = $("#wareNoByAreaCon").combobox("getValue");
	if(wareNo == ''){
		wms_city_common.comboboxLoadFilter(
				["areaNoCon"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[true],
				null,
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				["areaNoCon"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[true],
				BasePath+'/cm_defarea/get_biz',
				{"locno":locno,"wareNo":wareNo,"areaAttribute":"0","attributeType":"0","areaUsetypeArray":"'1','6'"},
				null,
				null);
	}
};
csinstocksetting.selectAreaNoForSearchByStock = function(){
	var locno = csinstocksetting.locno;
	var wareNo = $("#wareNoByStockCon").combobox("getValue");
	if(wareNo == ''){
		wms_city_common.comboboxLoadFilter(
				["areaNoByStockCon"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[true],
				null,
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				["areaNoByStockCon"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[true],
				BasePath+'/cm_defarea/get_biz',
				{"locno":locno,"wareNo":wareNo,"areaAttribute":"0","attributeType":"0","areaUsetypeArray":"'1','6'"},
				null,
				null);
	}
};
csinstocksetting.selectAreaNoForSearchByCell = function(){
	var locno = csinstocksetting.locno;
	var wareNo = $("#wareNoByCellCon").combobox("getValue");
	if(wareNo == ''){
		wms_city_common.comboboxLoadFilter(
				["areaNoByCellCon"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[true],
				null,
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				["areaNoByCellCon"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[true],
				BasePath+'/cm_defarea/get_biz',
				{"locno":locno,"wareNo":wareNo,"areaAttribute":"0","attributeType":"0","areaUsetypeArray":"'1','6'"},
				null,
				null);
	}
	csinstocksetting.selectStockNoForSearchByCell();
};
csinstocksetting.selectStockNoForSearchByCell = function(){
	var locno = csinstocksetting.locno;
	var wareNo = $("#wareNoByCellCon").combobox("getValue");
	var areaNo = $("#areaNoByCellCon").combobox("getValue");
	if(wareNo == '' || areaNo == ''){
		wms_city_common.comboboxLoadFilter(
				["stockNoByCellCon"],
				'stockNo',
				'stockNo',
				'valueAndText',
				false,
				[true],
				null,
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				["stockNoByCellCon"],
				'stockNo',
				'stockNo',
				'valueAndText',
				false,
				[true],
				BasePath+'/cm_defstock/get_biz',
				{"locno":locno,"wareNo":wareNo,"areaNo":areaNo},
				null,
				null);
	}
};
function MyTr(data){
	this.curValue="";
	this.tr = $("<tr></tr>");
	$('<td width="5%"><input type="checkBox" name="itemcheck"></td>').appendTo(this.tr );
	var inputTd = $('<td width="40%"></td>').appendTo(this.tr );
	$('<input style="width:100px" name="itemCode" value="'+data.CODE+'">').bind("focus",this.getCurInfo).bind("blur",this.getInfo).appendTo(inputTd);
	$('<td width="60%">'+data.NAME+'</td>').appendTo(this.tr );
};
MyTr.prototype = {
	"getInfo":function(){
		var curObj = $(this);
		var curValue = curObj.val();
		if(this.curValue==curValue){
			return;
		}
		if(curValue!=""){
			var flag = true;
			$("input[name='itemCode']").not(curObj).each(function () {
			    if (curObj.val() == $(this).val()){
			    	 alert("编号已经存在，请重新输入");
					 curObj.parent().parent().children().eq(2).html("");
					 curObj.val("");
			    	 flag = false ;
			    }
   			 });
   			 if(!flag){
   			 	return;
   			 }
			var curSelect = $("input[name='detailType']:checked");
			var setType = $("input[name='setType']:checked");
			$.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{
					"detailType":curSelect.val(),
					"setType":setType.val(),
					"locno":csinstocksetting.locno,
					"settingValue":curValue
				},
				dataType : "json",
				url:BasePath+'/cs_instock_setting/selectItemByCode',
				success : function(data) {
					if(data==null){
						alert("输入的编号有误，请重新输入");
						curObj.parent().parent().children().eq(2).html("");
						curObj.val("");
					}else{
						if(data.result=="success"){
							curObj.parent().parent().children().eq(2).html(data.name);
						}else{
							alert(data.msg);
							curObj.parent().parent().children().eq(2).html("");
							curObj.val("");
						}
					}
				}
			});
		}
	},
	"getCurInfo":function(){
		var curObj = $(this);
		this.curValue=curObj.val();
	}
};

//删除一行
csinstocksetting.delItemLine = function(){
	$("input[name='itemcheck']:checked").parent().parent().remove();
};
//删除所有行
csinstocksetting.delAllItemLine = function(){
	//$("input[name='itemcheck']").parent().parent().remove();
	var settingNo = $("#settingNo").val();
	if(settingNo == ""){
		$("#itemDetail").datagrid({data:{ total: 0, rows: [] }});
		return;
	}
	var detailType = $("input[name='detailType']:checked").val();
	$.ajax({
		  cache: true,
		  async : true,
		  type: 'POST',
		  url: BasePath+'/cs_instock_setting/selectItemBySettingNo',
		  data:{
			"locNo":csinstocksetting.locno,
			"settingNo":settingNo,
			"type":detailType
		  },
		  success: function(data){
		  	var myData = data.data;
		  	if(myData!=null){
		  		csinstocksetting.loadDatagrid('itemDetail', data);
		  	}else{
		  		$("#itemDetail").datagrid({data:{ total: 0, rows: [] }});
		  	}
		  }
	});
};

//新增
csinstocksetting.addInfo = function(){
	$('#showDialog').window({
		title:"新增",
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",true);
		}
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('enable');
		}
	});
	$("#settingNo").attr("disabled",false);
	$("#settingNo").val("");
	$("#settingName").val("");
	$("#settingDesc").val("");	
    $("#memo").val("");	
    
    $('input:radio[name=instockScope]').eq(0).attr("checked",true);
    
    //储位分配顺序
    $('input:radio[name=cellSort]').eq(0).attr("checked",true);
     $("#myCellTable").empty();
    //储位分配限定
    $('input:radio[name=limitedType]').eq(0).attr("checked",true);
    
    //适用场景
    $('input:radio[name=setType]').eq(0).attr("checked",true);
    
     //生效对象
    $('input:radio[name=detailType]').eq(0).attr("checked",true);
    $("#mytable").empty();
   //隐藏新增按钮，显示编辑按钮
	$("#info_add").show();
	$("#info_edit").hide();
	$("#showDialog").window('open'); 
	csinstocksetting.delAllCellLine();
	csinstocksetting.delAllItemLine();
	$("#status").combobox('select','1');
	csinstocksetting.hideCellAndItemBut(false);
};
//保存
csinstocksetting.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     var exist = false;
     var settingNo = $("#settingNo").val();
     $.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/cs_instock_setting/checkExist',
		  data:{
			  "locno":csinstocksetting.locno,
			  "settingNo":settingNo 	
		  },
		  cache: true,
		  success: function(data){
			if(data=="exist"){
				exist = true;
			}
		  }
	});
     if(exist){
    	 alert("该仓别下的策略编码["+settingNo+"]已经存在!",2);
    	 return;
     }
    //保存仓库信息
   csinstocksetting.saveCellsInfo();
   if($("#cellNoTrue").val() == ""){
   	alert("请添加上架范围!",2);
   	return;
   }
    //保存商品信息
    csinstocksetting.saveItemsInfo();
    if($("#selectValue").val() == ""){
    	alert("请添加生效对象!",2);
    	return;
    }
     if(!csinstocksetting.checkExist()){
     	return;
     }
	 //2. 保存
     wms_city_common.loading("show","正在保存......");
     var url = BasePath+'/cs_instock_setting/addCsInstockSetting';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				wms_city_common.loading();
				var json = eval("(" + data + ")"); 
				if(json.result=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 csinstocksetting.searchArea();
				}else if(json.result=="fail"){
					alert('新增失败,请联系管理员!',2);
				}else{
					alert(json.result,2);
				}
		    }
	   });
};
csinstocksetting.getItemData = function(){
	var data = $("#itemDetail").datagrid('getRows');
	return data;
};
csinstocksetting.getCellData = function(){
	var data = $("#cellDetail").datagrid('getRows');
	return data;
};
  //保存仓库信息
csinstocksetting.saveCellsInfo = function(){
     /*var items = $("input[name='cellCode']");
     var checkedRows;
     var keyStr = [];
     for(var i=0,length=items.length;i<length;i++){
     	if(items.eq(i).val()!=""){
     		keyStr.push(items.eq(i).val());
     	}
     	
     }*/
	var keyStr = [];
	var data = csinstocksetting.getCellData();
	if(data != null){
		for(var i=0,length=data.length;i<length;i++){
	     	if(data[i]["CODE"]!=""){
	     		keyStr.push(data[i]["CODE"]);
	     	}
	     }
		if(keyStr.length>0){
			$("#cellNoTrue").val(keyStr.join(","));
		}else{
			$("#cellNoTrue").val("");
		}
	}else{
		$("#cellNoTrue").val("");
	}
};

  //保存商品信息
csinstocksetting.saveItemsInfo = function(){
	/*
     var items = $("input[name='itemCode']");
     var keyStr = [];
     for(var i=0,length=items.length;i<length;i++){
     	if(items.eq(i).val()!=""){
     		keyStr.push(items.eq(i).val());
     	}
     	
     }
     if(keyStr.length>0){
     	 $("#selectValue").val(keyStr.join(","));
     }*/
    var keyStr = [];
 	var data = csinstocksetting.getItemData();
 	if(data != null){
 		for(var i=0,length=data.length;i<length;i++){
 	     	if(data[i]["CODE"]!=""){
 	     		keyStr.push(data[i]["CODE"]);
 	     	}
 	     }
 		if(keyStr.length>0){
 			$("#selectValue").val(keyStr.join(","));
 		}else{
 			$("#selectValue").val("");
 		}
 	}else{
 		$("#selectValue").val("");
 	}
};


csinstocksetting.checkExist = function(){
	var settingNo = $("#settingNo").val();
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/cs_instock_setting/checkExist',
		  data:{
			"settingNo":settingNo  	
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("该仓别下的策略编码已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

csinstocksetting.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//删除
csinstocksetting.del = function(){
	//var rows = $("#dataGridJG").datagrid("getSelections");// 获取所有选中的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.locno+"|"+item.settingNo);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/cs_instock_setting/deleteOsLineBuffer';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	wms_city_common.loading("show","正在删除......");
        	csinstocksetting.ajaxRequest(url,data,function(result){
        		wms_city_common.loading();
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
        			 csinstocksetting.searchArea();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

csinstocksetting.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

csinstocksetting.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		csinstocksetting.edit(checkedRows[0],"edit");
	}
};

//修改
csinstocksetting.edit = function(rowData,type){
	//设置标题
	var title = "详情";
	csinstocksetting.hideCellAndItemBut(true);
	if(type=="edit"){
		title = "修改";
		csinstocksetting.hideCellAndItemBut(false);
	}
	$('#showDialog').window({
		title:title,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	
	//设置信息
	csinstocksetting.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
};

//修改
csinstocksetting.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
     //保存仓库信息
    csinstocksetting.saveCellsInfo();
    if($("#cellNoTrue").val() == ""){
    	alert("请添加上架范围!",2);
    	return;
    }
    //保存商品信息
    csinstocksetting.saveItemsInfo();
    if($("#selectValue").val() == ""){
    	alert("请添加生效对象!",2);
    	return;
    }
	 //2. 保存
    wms_city_common.loading("show","正在保存......");
    var url = BasePath+'/cs_instock_setting/editCsInstockSetting';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				  wms_city_common.loading();
				var json = eval("(" + data + ")");  
				if(json.result=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 csinstocksetting.searchArea();
				}else if(json.result=="fail"){
					alert('修改失败,请联系管理员!',2);
				}else{
					alert(json.result,2);
				}
		    }
	});
};
csinstocksetting.loadDatagrid = function(id,data){
	var r = {};
	r.total = data.data.length;
	r.rows = data.data;
	$("#"+id).datagrid({data:r});
};
csinstocksetting.detail = function(rowData,optype){
	$('#dataForm').form('load',rowData);
	if(rowData.sameItemFlag == '1'){
		$("input[name='sameItemFlag']").attr('checked',true);
	}else{
		$("input[name='sameItemFlag']").attr('checked',false);
	}
	if(rowData.emptyCellFlag == '1'){
		$("input[name='emptyCellFlag']").attr('checked',true);
	}else{
		$("input[name='emptyCellFlag']").attr('checked',false);
	}
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",false);
		}
	});
	$("#settingNo").attr("disabled",true);	
	
	//获取商品详情
	var settingNo = $("#settingNo").val();
	
	//获取储位信息
	$("#myCellTable").empty();
	$.ajax({
		  cache: true,
		  async : true,
		  type: 'POST',
		  url: BasePath+'/cs_instock_setting/selectCellBySettingNo',
		  data:{
			"locNo":csinstocksetting.locno,
			"settingNo":settingNo,
			"type":$("input[name='instockScope']:checked").val()
		  },
		 
		  success: function(data){
		  	var myData = data.data;
		  	if(myData!=null){
		  		csinstocksetting.loadDatagrid('cellDetail', data);
		  		for(var i=0,length=myData.length;i<length;i++){
		  			new CellTr(myData[i]).tr.appendTo($("#myCellTable"));
		  		}
		  	}
		  }
	});
	
	//获取商品信息
	var type = $("input[name='detailType']:checked").val();
	$("#mytable").empty();
	$.ajax({
		  cache: true,
		  async : true,
		  type: 'POST',
		  url: BasePath+'/cs_instock_setting/selectItemBySettingNo',
		  data:{
			"locNo":csinstocksetting.locno,
			"settingNo":settingNo,
			"type":type
		  },
		  success: function(data){
		  	var myData = data.data;
		  	if(myData!=null){
		  		csinstocksetting.loadDatagrid('itemDetail', data);
		  		for(var i=0,length=myData.length;i<length;i++){
		  			new MyTr(myData[i]).tr.appendTo($("#mytable"));
		  		}
		  	}
		  }
	});
	//隐藏新增按钮，显示编辑按钮
	$("#info_add").hide();
	if(optype=="edit"){
		$("#info_edit").show();
	}else{
		$("#info_edit").hide();
	}
	
};

//查询区域信息
csinstocksetting.searchArea = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/cs_instock_setting/list.json?locno='+csinstocksetting.locno;
	
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

csinstocksetting.searchLocClear = function(){
	$('#searchForm').form("clear");
};

function convertArray(o) { 
	var v = {};
	for ( var i in o) {
		if (typeof (v[o[i].name]) == 'undefined')
			v[o[i].name] = o[i].value;
		else
			v[o[i].name] += "," + o[i].value;
	}
	return JSON.stringify(v);
};
csinstocksetting.hideCellAndItemBut = function(yes){
	if(yes){
		$("#addLineForCell").linkbutton('disable');
		$("#delLineForCell").linkbutton('disable');
		$("#addLineForItem").linkbutton('disable');
		$("#delLineForItem").linkbutton('disable');
	}else{
		$("#addLineForCell").linkbutton('enable');
		$("#delLineForCell").linkbutton('enable');
		$("#addLineForItem").linkbutton('enable');
		$("#delLineForItem").linkbutton('enable');
	}
};
csinstocksetting.closeWindow = function(id){
	$("#"+id).window('close');
};
 