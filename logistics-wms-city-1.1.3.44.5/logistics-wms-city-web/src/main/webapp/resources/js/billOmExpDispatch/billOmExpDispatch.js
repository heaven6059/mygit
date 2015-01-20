
var billOmExpDispatch = {};

//加载Grid数据Utils
billOmExpDispatch.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
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

//清除查询条件
billOmExpDispatch.searchClear = function(id){
	$('#'+id).form("clear");	
	if(id=='searchFormTab1'){
		$('#brandNoTab1').combobox("loadData",[]); 
	}else if(id=='searchFormTab2'){
		$('#brandNoTab2').combobox("loadData",[]); 
	}else if(id=='searchFormTab3'){
		$('#brandNoTab3').combobox("loadData",[]); 
	}
};

//普通调度查询
billOmExpDispatch.searchDataTab1 = function(){
	var fromObjStr=convertArray($('#searchFormTab1').serializeArray());
	var queryMxURL=BasePath+'/billomexpdispatch/listBillOmExpDispatch.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billOmExpDispatch.locno;
	billOmExpDispatch.loadGridDataUtil('dataGridJG_tabl', queryMxURL, reqParam);
};

//续调查询
billOmExpDispatch.searchDataTab2 = function(){
	var fromObjStr=convertArray($('#searchFormTab2').serializeArray());
	var queryMxURL=BasePath+'/billomexpdispatch/listBillOmLocate.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billOmExpDispatch.locno;
	reqParam['isContinue'] = "1";
	billOmExpDispatch.loadGridDataUtil('dataGridJG_tab2', queryMxURL, reqParam);
};

//异常调度查询
billOmExpDispatch.searchDataTab3 = function(){
	var fromObjStr=convertArray($('#searchFormTab3').serializeArray());
	var queryMxURL=BasePath+'/bill_om_direct_log/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billOmExpDispatch.locno;
	billOmExpDispatch.loadGridDataUtil('dataGridJG_tab3', queryMxURL, reqParam);
};

//加载出库调度明细
billOmExpDispatch.loadDetailDtl = function(rowData){
	var queryMxURL=BasePath+'/billomexpdtl/listBillOmExpDtlDispatch.json';
	var reqParam = {expNo:rowData.expNo,locno:rowData.locno};
	billOmExpDispatch.loadGridDataUtil('dataGridDtl_tabl', queryMxURL, reqParam);
	//billOmExpDispatch.getBillOmExpTotalQty(rowData);
	$('#itemQtySp').html(rowData.totalItemQty);
	$('#expQtySp').html(rowData.totalExpQty);
};

//填充总数量信息
billOmExpDispatch.loadTotalQty = function(rowData){
	$('#volumeSp').html(rowData.totalVolumeQty);
	$('#weightSp').html(rowData.totalWeightQty);
	$('#differenceQtySp').html(rowData.totalDifferenceQty);
	$('#noenoughQtySp').html(rowData.totalNoenoughQty);
};


//出库调度试图汇总数据
billOmExpDispatch.getBillOmExpTotalQty=function(rowData){
    var getFn = function(returnData){
    	if(returnData.flag=="success"){
    		var data = returnData.exp;
    		billOmExpDispatch.loadTotalQty(data);
    	}else{
    		alert(returnData.msg);
    	}
    };
    var reqParas = {expNo:rowData.expNo,locno:billOmExpDispatch.locno};
	ajaxRequest(BasePath+'/billomexpdispatch/findBillOmExpViewTotalQty',reqParas,getFn);
};

//出库调度
billOmExpDispatch.omExpDispatch = function(){
	
    //校验是否选择出库单
    var checkedRows = $('#dataGridJG_tabl').datagrid('getChecked');
    if(checkedRows.length < 1){
		alert("请选择出库单");
		return ;
	}
    
    //传递的参数变量
    var ownerNo = "";
    var expType = "";
    var expNo = "";
    var divideFlag = "0";
	if($("#divideFlag").is(':checked')){
		divideFlag = "1";
	}
    
    //验证选择的订单类型是否相同
    var isNotEqual = false;
    $.each(checkedRows, function(index, item){
		if(index == 0){
			expType = item.expType;
			ownerNo = item.ownerNo;
		}else{
			if(expType!=item.expType){
				isNotEqual = true;
				return false;
			}
		}
		expNo += item.expNo+",";
	}); 
    
    if(isNotEqual){
    	alert("请选择相同的订单类型!");
    	return;
    }
    
    if(expNo!=""&&expNo!=null){
    	expNo = expNo.substring(0, expNo.length-1);
    }
    
    //调度
    $.messager.confirm("确认","你确定要调度这"+checkedRows.length+"条数据", function (r){  
    	if(!r){
    		return;
    	}
    	//wms_city_common.loading('show');
    	//路径参数
        var url = BasePath+'/billomexpdispatch/procBillOmExpDispatchQuery';
        var params = {
        		locno:billOmExpDispatch.locno,
        		ownerNo:ownerNo,
        		expNo:expNo,
        		expType:expType,
        		divideFlag:divideFlag,
        		creator:billOmExpDispatch.creator
        };
        wms_city_common.loading("show","正在调度......");
        $.post(url, params, function(data) {
        	 wms_city_common.loading();
        	if(data.flag == "success"){
            	alert('调度成功!');
        		$('#dataGridJG_tabl').datagrid("reload");
        		$('#dataGridDtl_tabl').datagrid("reload");
        		wms_city_common.loading();
        	}else if(data.flag=='warn'){
            	alert(data.msg,1);
            	wms_city_common.loading();
            	return;
            }else{
            	alert('操作异常！',1);
            	wms_city_common.loading();
            	return;
            }
        	
//        	if(data){
//        		alert('调度成功!');
//        		$('#dataGridJG_tabl').datagrid("reload");
//        		$('#dataGridDtl_tabl').datagrid("reload");
//    		}else{
//    			alert('调度异常,请联系管理员!',2);
//    		}
        	
        }, "JSON").error(function() {
        	alert('调度失败,请联系管理员!',2);
        });
    	
    });
    
};

//出库续调
billOmExpDispatch.continueOmExpDispatch = function(){
	
	 //校验是否选择波次信息
    var checkedRows = $('#dataGridJG_tab2').datagrid('getChecked');
    if(checkedRows.length < 1){
		alert("请选择波次信息");
		return ;
	}
	
	//续调
    $.messager.confirm("确认","你确定要续调这"+checkedRows.length+"条数据", function (r){  
    	if(!r){
    		return;
    	}
    	
    	//路径参数
        var url = BasePath+'/billomexpdispatch/procBillOmExpContinueDispatchQuery';
        var locateList = [];
        $.each(checkedRows, function(index, item){
        	var params = {
            		locno:billOmExpDispatch.locno,
            		locateNo:item.locateNo,
            		creator:billOmExpDispatch.creator
            };
        	locateList[locateList.length] = params;
    	}); 
       
        var data = {datas:JSON.stringify(locateList)};
        wms_city_common.loading("show","正在续调......");
        $.post(url, data, function(data) {
        	 wms_city_common.loading();
        	if(data.flag == "success"){
        		alert('续调成功!');
        		$('#dataGridJG_tab2').datagrid("reload");
    		}else{
    			alert(data.msg,2);
    		}
        }, "JSON").error(function() {
        	alert('续调失败,请联系管理员!',2);
        });
    	
    });
};
billOmExpDispatch.expType = {};
billOmExpDispatch.expTypeFormatter = function(value, rowData, rowIndex){
	return billOmExpDispatch.expType[value];
};
//初始化订单类型
billOmExpDispatch.initExpType = function(data){
	wms_city_common.comboboxLoadFilter(
			["expTypeTab1"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=OEM_EXP_TYPE',
			{},
			billOmExpDispatch.expType,
			null);
};

billOmExpDispatch.initOwnerNo = function(){
//	$.ajax({
//		type : 'POST',
//		dataType : "json",
//		url:BasePath+'/entrust_owner/get_biz',
//		success : function(data) {
//			$('#ownerNoTab2').combobox('loadData',data);
//			billOmExpDispatch.ownerFt = billOmExpDispatch.converStr2JsonObj2(data);
//		}
//	});
	wms_city_common.comboboxLoadFilter(
			["ownerNoTab2"],
			'ownerNo',
			'ownerName',
			'ownerName',
			false,
			[true],
			BasePath+'/entrust_owner/get_biz',
			{},
			billOmExpDispatch.ownerFt,
			null);
};

//初始化用户信息
billOmExpDispatch.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		billOmExpDispatch.locno = data.locno;
		billOmExpDispatch.creator = data.loginName;
	}); 
};

//委托业主
billOmExpDispatch.ownerFt = {};
billOmExpDispatch.ownerFormatter = function(value, rowData, rowIndex){
	return billOmExpDispatch.ownerFt[value];
};

billOmExpDispatch.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
billOmExpDispatch.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//缺量格式化
billOmExpDispatch.formatNoenoughQty = function(value, rowData, rowIndex){
	var noenoughQty=rowData.itemQty-rowData.differenceQty;
	if(noenoughQty>0){
		return noenoughQty;
	}else{
		return 0;		
	}
};
//========================初始化信息======================================
$(document).ready(function(){
	billOmExpDispatch.initCurrentUser();
	billOmExpDispatch.initOwnerNo();
	billOmExpDispatch.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=OEM_EXP_TYPE',{},true,billOmExpDispatch.initExpType);
	
//	var objs = [];
//	objs.push(
//			{"sysNoObj":$('#sysNoSearch1'),"brandNoObj":$('#brandNoTab1')},
//			{"sysNoObj":$('#sysNoSearch2'),"brandNoObj":$('#brandNoTab2')},
//			{"sysNoObj":$('#sysNoSearch3'),"brandNoObj":$('#brandNoTab3')}
//			);
//	wms_city_common.loadSysNo4Cascade(objs);
	
	var objs1 = [{"sysNoObj":$('#sysNoSearch1'),"brandNoObj":$('#brandNoTab1')}];
	wms_city_common.loadSysNo4Cascade(objs1);
	var objs2 = [{"sysNoObj":$('#sysNoSearch2'),"brandNoObj":$('#brandNoTab2')}];
	wms_city_common.loadSysNo4Cascade(objs2);
	var objs3 = [{"sysNoObj":$('#sysNoSearch3'),"brandNoObj":$('#brandNoTab3')}];
	wms_city_common.loadSysNo4Cascade(objs3);
	
	
	$('#dataGridJG_tabl').datagrid({
		view: detailview,
		detailFormatter: function(index, row) {
			return '<div class="ddv" style="padding:5px 2;width:400px;"></div>';
		},
		onExpandRow: function(index, row) {
			var ddv = $(this).datagrid('getRowDetail', index).find('div.ddv');
			/*ddv.panel({
				height: 80,
				border: false,
				cache: false,
				href: BasePath + '/billomexpdtl/selectStore.json?expNo=' + row.expNo + "&ownerNo=" + row.ownerNo + "&locno=" + row.locno,
				onLoad: function() {
					$('#dataGridJG_tabl').datagrid('fixDetailRowHeight', index);
				}
			});*/
			ddv.empty();
			$.ajax({
				async : true,
				cache : true,
				type : 'GET',
				dataType : "json",
				url : BasePath + '/billomexpdtl/selectStore.json?expNo=' + row.expNo + "&ownerNo=" + row.ownerNo + "&locno=" + row.locno,
				success : function(data) {
					$("<div><table width='100%' border='0'><tr style='height:25px;'><th width='30%'>客户编码</th><th width='30%'>客户名称</th><th width='30%'>合同号</th></tr></table></div>").appendTo(ddv);
					var condiv = $("<div></div>").appendTo(ddv);
					var table = $("<table width='100%' border='0'></table>").appendTo(condiv);
					var data = data.rows;
					for(var i=0,length=data.length;i<length;i++){
						$("<tr style='height:25px'><td width='30%'>"+data[i].storeNo+"</td><td width='30%'>"+data[i].storeName+"</td><td width='30%'>"+data[i].poNo+"</td></tr>").appendTo(table);
					}
					$('#dataGridJG_tabl').datagrid('fixDetailRowHeight', index);
				}
			});
			$('#dataGridJG_tabl').datagrid('fixDetailRowHeight', index);
		}
	});
	
	$('#dataGridDtl_tabl').datagrid({
		'onLoadSuccess':function(data){
			if(data.footer[1].isselectsum){
				billOmExpDispatch.noenoughQty = data.footer[1].noenoughQty;
				billOmExpDispatch.itemQty = data.footer[1].itemQty;
				billOmExpDispatch.differenceQty = data.footer[1].differenceQty;
				billOmExpDispatch.usableQty = data.footer[1].usableQty;
		   	}else{
		   		var rows = $('#dataGridDtl_tabl').datagrid('getFooterRows');
			   	rows[1]['noenoughQty'] = billOmExpDispatch.noenoughQty;
			   	rows[1]['itemQty'] = billOmExpDispatch.itemQty;
				rows[1]['differenceQty'] = billOmExpDispatch.differenceQty;
				rows[1]['usableQty'] = billOmExpDispatch.usableQty;
			   	$('#dataGridDtl_tabl').datagrid('reloadFooter');
		   	}
		}
	});
	
	/*$('#dataGridJG_tabl').datagrid({
		view: detailview,
		detailFormatter: function(index, row) {
			return '<div style="padding:2px"><table class="ddv" style="height:150px"></table></div>';
		},
		onExpandRow: function(index, row) {
			var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
			ddv.datagrid({
				url:BasePath+'/billomexpdtl/selectStore.json?expNo=' + row.expNo+"&ownerNo="+row.ownerNo+"&locno="+row.locno,
				fitColumns: true,
				singleSelect: true,
				rownumbers: true,
				height: 150,
				columns: [[{
					field: 'storeNo',
					title: '客户编码',
					width:50,
					align: 'center'
				},
				{
					field: 'storeName',
					title: '客户名称',
					width: 100,
					align: 'left'
				}]],
				onLoadSuccess: function() {
						$('#dataGridJG_tabl').datagrid('fixDetailRowHeight', index);
				}
			});
			//$('#dataGridJG_tabl').datagrid('fixDetailRowHeight', index);
		}
	});*/
});