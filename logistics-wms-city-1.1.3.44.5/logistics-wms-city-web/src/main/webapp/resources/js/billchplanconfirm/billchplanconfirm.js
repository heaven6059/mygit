var billchplanconfirm = {};
billchplanconfirm.user = {};
billchplanconfirm.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billchplanconfirm.status = {};
billchplanconfirm.statusFormatter = function(value, rowData, rowIndex){
	return billchplanconfirm.status[value];
};
billchplanconfirm.planType = {};
billchplanconfirm.planTypeFormatter = function(value, rowData, rowIndex){
	return billchplanconfirm.planType[value];
};
//清除查询条件
billchplanconfirm.searchClear = function(){
	$('#searchForm').form("clear");
};
//查询信息
billchplanconfirm.searchbillchplanconfirm = function(){
	var startCreatetm =  $('#createtmStartCondition').datebox('getValue');
	var endCreatetm =  $('#createtmEndCondition').datebox('getValue');
	var audittmStart =  $('#audittmStartCondition').datebox('getValue');
	var audittmEnd =  $('#audittmEndCondition').datebox('getValue');
	var planDateStart =  $('#startPlanDateCondition').datebox('getValue');
	var planDatemEnd =  $('#endPlanDateCondition').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }   
	if(!isStartEndDate(audittmStart,audittmEnd)){    
		alert("审核日期开始日期不能大于结束日期");   
		return;   
	} 
	if(!isStartEndDate(planDateStart,planDatemEnd)){    
		alert("盘点日期开始日期不能大于结束日期");   
		return;   
	} 
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_ch_plan/list.json?sort=plan_No&order=desc&statusB=10&locno='+billchplanconfirm.user.locno;
	
    $( "#dataGridBillChPlanConfirm").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridBillChPlanConfirm").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridBillChPlanConfirm").datagrid( 'load' );
    
};
//加载Grid数据Utils
billchplanconfirm.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};
billchplanconfirm.loadDetail = function(rowData){
	billchplanconfirm.clearDetail();
	var status = rowData.status;
	var planNo = rowData.planNo;
	$("#planNoCheck").val(planNo);
	$("#ownerNoCheck").val(rowData.ownerNo);
	$("#planTypeCheck").combobox('setValue',rowData.planType);
	$("#statusCheck").combobox('setValue',rowData.status);
	$("#planDateCheck").datebox('setValue',rowData.planDate);
	$("#beginDateCheck").datebox('setValue',rowData.beginDate);
	
	if(status == '25'){
		$("#btn-diff").linkbutton('enable');
	}
	var checkDtlUrl = BasePath + '/bill_ch_check_dtl/dtl_listByPlanNo.json';
	var checkDtlParams = {
			locno:billchplanconfirm.user.locno,
			planNo:planNo
		};
	billchplanconfirm.loadGridDataUtil('dtl_check_dataGrid', checkDtlUrl, checkDtlParams);
	$("#showDetailDialog").window('open');
};
billchplanconfirm.clearDetail = function(){
	$("#btn-diff").linkbutton('disable');
	$("#planTypeCheck").combobox('disable');
	$("#statusCheck").combobox('disable');
	$("#planMainForm").form('clear');
	$("#dtl_check_dataGrid").datagrid('loadData',[]);
};
//关闭
billchplanconfirm.closeWindow = function(id){
	$('#'+id).window('close');  
};
billchplanconfirm.createDiff = function(){
		var status = $("#statusCheck").combobox('getValue');
		var url = BasePath+'/bill_ch_plan_confirm/createDiff';
		var param = {
				planNo:$("#planNoCheck").val(),
				locno:billchplanconfirm.user.locno,
				planStatus:status,
				ownerNo:$("#ownerNoCheck").val(),
				creator:billchplanconfirm.user.loginName,
				creatorName:billchplanconfirm.user.username
		};
		if(status != '25'){
			alert("只能选择【初盘/复盘】状态的计划单!",1);
			return;
		}
		$.messager.confirm("确认","您确定要结案吗？", function (r){
			if(!r){
				return;
			}
			wms_city_common.loading("show","正在结案......");
			$.post(url, param, function(result) {
				if(result.result == 'success'){
					alert('结案成功!');
					$("#dataGridBillChPlanConfirm").datagrid('load');
					billchplanconfirm.closeWindow('showDetailDialog');
				}else if(result.result == 'fail'){
					alert('结案失败!',2);
				}else{
					alert(result.result+",结案失败!",2);
				}
				wms_city_common.loading();
			}, "JSON").error(function() {
				alert('结案失败!',2);
				wms_city_common.loading();
		    });
		});
};
$(document).ready(function(){
	//创建日期初始为前两天
	$("#createtmStartCondition").datebox('setValue',getDateStr(-2));
	billchplanconfirm.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){billchplanconfirm.user=u;});
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNoSearch]',$('#searchForm'))},
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	$('#dtl_check_dataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer!=null){
			   			if(data.footer[1].isselectsum){
			   				billchplanconfirm.itemQty = data.footer[1].itemQty;
			   				billchplanconfirm.checkQty = data.footer[1].checkQty;
			   				billchplanconfirm.recheckQty = data.footer[1].recheckQty;
			   			}else{
			   				var rows = $('#dtl_check_dataGrid').datagrid('getFooterRows');
				   			rows[1]['itemQty'] = billchplanconfirm.itemQty;
				   			rows[1]['checkQty'] = billchplanconfirm.checkQty;
				   			rows[1]['recheckQty'] = billchplanconfirm.recheckQty;
				   			$('#dtl_check_dataGrid').datagrid('reloadFooter');
			   			}
					}else{
						var rows = $('#dtl_check_dataGrid').datagrid('getFooterRows');
						if(rows!=null){
							rows[0]['itemQty'] = "";
				   			rows[0]['checkQty'] = "";
				   			rows[0]['recheckQty'] = "";
				   			$('#dtl_check_dataGrid').datagrid('reloadFooter');
						}
					}
		   		}
			}
		);
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["statusCondition","statusCheck"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CHECKNUM_STATUS',
			{},
			billchplanconfirm.status,
			null);
	//初始化盘点类型
	wms_city_common.comboboxLoadFilter(
			["planTypeCondition","planTypeCheck"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PLAN_TYPE',
			{},
			billchplanconfirm.planType,
			null);
});
function isStartEndDate(startDate,endDate){   
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){   
             return false;   
        }   
     }   
     return true;   
}
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