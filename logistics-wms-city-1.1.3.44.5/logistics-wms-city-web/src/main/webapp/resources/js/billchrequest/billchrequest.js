var billchrequest = {};
billchrequest.user = {};
billchrequest.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billchrequest.owner = {};
billchrequest.ownerFormatter = function(value, rowData, rowIndex){
	return billchrequest.owner[value];
};
billchrequest.status = {};
billchrequest.statusFormatter = function(value, rowData, rowIndex){
	return billchrequest.status[value];
};
billchrequest.planType = {};
billchrequest.planTypeFormatter = function(value, rowData, rowIndex){
	return billchrequest.planType[value];
};
//清除查询条件
billchrequest.searchClear = function(){
	$('#searchForm').form("clear");
};
//查询信息
billchrequest.searchBillChRequest = function(){
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_ch_plan/list.json?sort=plan_No&order=desc&statusA=90&locno='+billchrequest.user.locno;
	
    $( "#dataGridBillChRequest").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridBillChRequest").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridBillChRequest").datagrid( 'load' );
    
};
billchrequest.direct = function(){
	var checkedRows = $("#dataGridBillChRequest").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	if(len < 1){
		alert('请选择需要定位的计划单!',1);
		return;
	}else if(len > 1){
		alert('只能定位一条计划单!',1);
		return;
	}else{
		var temp = checkedRows[0];
		if(temp.status != '11'){
			alert("单据["+temp.planNo+"]不能定位!<br>只能定位已审核的单据!",1);
			return;
		}
		$.messager.confirm("确认","您确定要定位这条单据码？", function (r){
			if(r){
				var param = {
						locno:billchrequest.user.locno,
						planNo:temp.planNo,
						planType:temp.planType,
						ownerNo:temp.ownerNo,
						quality:temp.quality,
						itemType:temp.itemType
				};
				wms_city_common.loading("show","正在定位......");
				var url = BasePath+'/bill_ch_check_direct/direct';
				$.post(url, param, function(result) {
					if(result.result == 'success'){
						alert('定位成功!');
						$("#dataGridBillChRequest").datagrid('load');
						temp.status = '15';
						billchrequest.loadDetail(temp);
					}else if(result.result == 'fail'){
						alert('定位失败!',2);
					}else{
						alert(result.result,2);
					}
					wms_city_common.loading();
				}, "JSON").error(function() {
			    	alert('定位失败!',1);
			    	wms_city_common.loading();
			    });
			}
		});
	}
};
billchrequest.loadDetail = function(rowData){
	if(rowData.status == '10' || rowData.status == '11'){
		 $( "#dataGridBillChRequestDtl").datagrid( 'loadData',[]);
		return;
	}
	var queryMxURL=BasePath+'/bill_ch_check_direct/dtl_list.json?locno='+billchrequest.user.locno;
	
    //3.加载明细
    $( "#dataGridBillChRequestDtl").datagrid( 'options' ).queryParams={planNo:rowData.planNo,planType:rowData.planType};
    $( "#dataGridBillChRequestDtl").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridBillChRequestDtl").datagrid( 'load' );
};
billchrequest.checkDirectStatus = {};
billchrequest.checkDirectStatusFormatter = function(value, rowData, rowIndex){
	return billchrequest.checkDirectStatus[value];
};
billchrequest.initCheckDirectStatus = function(data){
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billchrequest.checkDirectStatus = temp;
};
//取消定位
billchrequest.cancelDirect = function(){
	var parentRow = $("#dataGridBillChRequest").datagrid("getSelected");//对应的盘点计划单
	var checkedRows = $("#dataGridBillChRequestDtl").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	var paramStr = '';
	if(len < 1){
		alert('请选择需要取消的定位单!',1);
		return;
	}
	if(parentRow == null || parentRow.status != '15'){
		alert('只能取消盘点计划单为【发起】状态下的定位单!',1);
		return;
	}
	for(var index=0;index<len;index++){
		if(checkedRows[index].status != '10'){
			alert('只能取消【新建】状态的定位单!',1);
			return;
		}
		paramStr += checkedRows[index].ownerNo+"_"+checkedRows[index].directSerial+"_"+checkedRows[index].status+"_"+checkedRows[index].cellNo+"_"+checkedRows[index].itemNo+"_"+checkedRows[index].sizeNo;
		if(index != (len-1)){
			paramStr += "|";
		}
	}
	$.messager.confirm("确认","您确定要取消这"+len+"条定位单吗？", function (r){
		if(r){
			var param = {
					paramStr:paramStr,
					locno:billchrequest.user.locno,
					planNo:parentRow.planNo
			};
			
			var url = BasePath+'/bill_ch_check_direct/cancelDirect';
			wms_city_common.loading("show","正在取消定位......");
			$.post(url, param, function(result) {
				if(result.result == 'success'){
					alert('取消定位成功!');
					$( "#dataGridBillChRequestDtl").datagrid( 'load' );
				}else if(result.result == 'fail'){
					alert('取消定位失败!',2);
				}else{
					alert(result.result+',取消定位失败!',2);
				}
				wms_city_common.loading();
			}, "JSON").error(function() {
		    	alert('取消定位失败!',1);
		    	wms_city_common.loading();
		    });
		}
	});
};
//恢复定位
billchrequest.regainDirect = function(){
	var parentRow = $("#dataGridBillChRequest").datagrid("getSelected");//对应的盘点计划单
	var checkedRows = $("#dataGridBillChRequestDtl").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	var paramStr = '';
	if(len < 1){
		alert('请选择需要恢复的定位单!',1);
		return;
	}
	if(parentRow == null || parentRow.status != '15'){
		alert('只能恢复盘点计划单为【发起】状态下的定位单!',1);
		return;
	}
	for(var index=0;index<len;index++){
		if(checkedRows[index].status != '16'){
			alert('只能恢复【取消】状态的定位单!',1);
			return;
		}
		paramStr += checkedRows[index].ownerNo+"_"+checkedRows[index].directSerial+"_"+checkedRows[index].status+"_"+checkedRows[index].cellNo+"_"+checkedRows[index].itemNo+"_"+checkedRows[index].sizeNo;
		if(index != (len-1)){
			paramStr += "|";
		}
	}
	$.messager.confirm("确认","您确定要恢复这"+len+"条定位单吗？", function (r){
		if(r){
			var param = {
					paramStr:paramStr,
					locno:billchrequest.user.locno,
					planNo:parentRow.planNo
			};
			
			var url = BasePath+'/bill_ch_check_direct/regainDirect';
			wms_city_common.loading("show","正在恢复定位......");
			$.post(url, param, function(result) {
				if(result.result == 'success'){
					alert('恢复定位成功!');
					$( "#dataGridBillChRequestDtl").datagrid( 'load' );
				}else if(result.result == 'fail'){
					alert('恢复定位失败!',2);
				}else{
					alert(result.result+',恢复定位失败!',2);
				}
				wms_city_common.loading();
			}, "JSON").error(function() {
		    	alert('恢复定位失败!',1);
		    	wms_city_common.loading();
		    });
		}
	});
};
$(document).ready(function(){
	billchrequest.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){billchrequest.user=u;});
	billchrequest.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CHECK_DIRECT_STATUS',{},true,billchrequest.initCheckDirectStatus);
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNoSearch]',$('#searchForm'))},
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	$('#dataGridBillChRequestDtl').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data == null || data.footer == null || data.footer[1] == null){
						return;
					}
		   			if(data.footer[1].isselectsum){
		   				billchrequest.itemQty = data.footer[1].itemQty;
		   			}else{
		   				var rows = $('#dataGridBillChRequestDtl').datagrid('getFooterRows');
			   			rows[1]['itemQty'] = billchrequest.itemQty;
			   			$('#dataGridBillChRequestDtl').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNoCondition"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[true],
			BasePath+'/entrust_owner/get_biz',
			{},
			billchrequest.owner,
			null);
	$('#ownerNoCondition').combobox('setValue',"BL");
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CHECKNUM_STATUS',
			{},
			billchrequest.status,
			null);
	//初始化盘点类型
	wms_city_common.comboboxLoadFilter(
			["planTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PLAN_TYPE',
			{},
			billchrequest.planType,
			null);
});

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