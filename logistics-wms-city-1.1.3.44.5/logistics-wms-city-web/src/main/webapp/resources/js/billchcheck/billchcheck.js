var billchcheck = {};
billchcheck.locno;
//状态
billchcheck.statusData;
billchcheck.planTypeData;
billchcheck.searchDirectParams ={};
//页面加载
$(document).ready(function(){
	billchcheck.initLocData();
	billchcheck.initStatusData();
	billchcheck.initPlanTypeData();
	billchcheck.initAssignNo();
	
	//创建日期初始为前两天
	$("#createtm_start").datebox('setValue',getDateStr(-2));
	//$("#requestDateStart").datebox('setValue',getDateStr(-2));
	
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#directForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
	
	
	//wms_city_common.initWareNo(billchcheck.locno,"wareNo","areaNo","stockNo");
	wms_city_common.initWareNoForCascade(
						billchcheck.locno,
						['wareNo'],
						['areaNo'],
						['stockNo'],
						null,
						[true],
						null
						);
	$('#dataGridJGCheckDtl').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billchcheck.itemQty = data.footer[1].itemQty;
		   			}else{
		   				var rows = $('#dataGridJGCheckDtl').datagrid('getFooterRows');
			   			rows[1]['itemQty'] = billchcheck.itemQty;
			   			$('#dataGridJGCheckDtl').datagrid('reloadFooter');
		   			}
		   		}
			}
	);
	
	$('#dataGridJGCheck').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billchcheck.itemQtyPlan = data.footer[1].itemQty;
		   			}else{
		   				var rows = $('#dataGridJGCheck').datagrid('getFooterRows');
			   			rows[1]['itemQty'] = billchcheck.itemQtyPlan;
			   			$('#dataGridJGCheck').datagrid('reloadFooter');
		   			}
		   		}
			}
	);
	
});

//仓库
billchcheck.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billchcheck.locno = data.locno;
		}
	});
};

//状态信息
billchcheck.initStatusData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_CH_CHECK_STATUS',
		success : function(data) {
			billchcheck.statusData=billchcheck.converStr2JsonObj2(data);
		}
	});
};

billchcheck.initPlanTypeData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PLAN_TYPE',
		success : function(data) {
			billchcheck.planTypeData=billchcheck.converStr2JsonObj2(data);
		}
	});
};
//盘点人员
billchcheck.initAssignNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#assignNo').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150 ,
			     filter: function(q, row){
			 		var opts = $('#assignNo').combobox('options');
			 		return row[opts.textField].indexOf(q) == 0;
			 	}

			  }).combobox("select",data[0].workerNo);
		}
	});
};

billchcheck.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//格式化
billchcheck.statusFormatter = function(value,rowData,rowIndex){
	return billchcheck.statusData[value];
};

//格式化
billchcheck.planTypeFormatter = function(value,rowData,rowIndex){
	return billchcheck.planTypeData[value];
};

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
//查询

billchcheck.searchDirectArea = function(){
	var fromObj=$('#directForm');
	var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
	var createtm_start =  $('#requestDateStart').datebox('getValue');
	var createtm_end =  $('#requestDateEnd').datebox('getValue');
	if(!isStartEndDate(createtm_start,createtm_end)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }   
	var fromObjStr=convertArray($('#directForm').serializeArray());
	billchcheck.searchDirectParams = eval("(" +fromObjStr+ ")");
	var queryMxURL=BasePath+'/bill_ch_check/findDirect4check?locno='+billchcheck.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= billchcheck.searchDirectParams;
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    billchcheck.selectAllCellCountAndStockCount(billchcheck.searchDirectParams);
};

billchcheck.selectAllCellCountAndStockCount = function(params){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		data:params,
		url:BasePath+'/bill_ch_check/selectAllCellCountAndStockCount',
		success : function(data) {
			if(data.result=="success"){
				$("#allStockCount").val(data.stockCount);
				$("#allCellCount").val(data.cellCount);
			}else{
				alert(data.msg,2);
			}
		}
	});
};

billchcheck.searchDirectClear = function(){
	$('#directForm').form("clear");
};

billchcheck.searchArea = function(){

	var createtm_start =  $('#createtm_start').datebox('getValue');
	var createtm_end =  $('#createtm_end').datebox('getValue');
	if(!isStartEndDate(createtm_start,createtm_end)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }   
	var fromObjStr=convertArray($('#checkForm').serializeArray());
	var queryMxURL=BasePath+'/bill_ch_check/main_list.json?locno='+billchcheck.locno;
    $( "#dataGridJGCheck").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGCheck").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGCheck").datagrid( 'load' );
};
billchcheck.searchLocClear = function(){
	$('#checkForm').form("clear");
};

//生成盘点单
billchcheck.createBillChCheck = function(){
	var fromObj=$('#createChCheckForm');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
	      return ;
	}
	var checkDate = $("#checkDate").datebox('getValue');
	if(checkDate==""){
		alert("盘点日期不能为空",1);
		return;
	}
	if(!planDateCheck(wms_city_common.getCurDate(),checkDate,7,7)){
		alert("盘点日期只能在当天前、后一周内的日期!"); 
		return;
	}
	
	var stockCount = $("#stockCount").val();
	var cellCount = $("#cellCount").val();
	if((stockCount==0||stockCount=="")&&(cellCount==0||cellCount=="")){
		alert("通道数量和储位数量至少输入一个",1);
		return;
	}
	
	if(stockCount=="0"){
		alert("通道数量必须大于0",1);
		return;
	}
	
	if(cellCount=="0"){
		alert("储位数量必须大于0",1);
		return;
	}
	
	var allData = $("#dataGridJG").datagrid("getRows");
	if(allData.length==0){
		alert("列表中没有数据无法生成",1);
		return ;
	}
	var data = billchcheck.searchDirectParams;
	data.checkDate = $("#checkDate").datebox('getValue');
	data.stockCount = $("#stockCount").val();
	data.cellCount = $("#cellCount").val();
	wms_city_common.loading("show","正在生成任务......");
	$.ajax({
		  type: 'POST',
		  url: BasePath+'/bill_ch_check/createBillChCheck',
		  data: data,
		  cache: true,
		  success: function(data){
		  	 if(data.result=='success'){
				 alert('生成任务成功!');
				 billchcheck.searchDirectArea();
				 billchcheck.searchArea();
	 		} else {
	 			 alert(data.errorCode,2);
		 	}
		  	wms_city_common.loading();
		}
	});
};

//分配盘点人员
billchcheck.distributionAssignNoBatch=  function(){
	var checkedRows = $("#dataGridJGCheck").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要分配的盘点单的记录!',1);
		return;
	}
	var checkNos = [];
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!='10'&&item.status!='20'){
			allOk = false;
			return false;
		}
		checkNos.push(item.checkNo);
	});       
	
	if(!allOk){
		alert("非初始状态的单据不能分配任务",1);
		return;
	}
	        
    var url = BasePath+'/bill_ch_check/distributionAssignNoBatch';
    //var assignText = $("#assignNo").combobox("getText");
    //var assignName = assignText.substr(assignText.indexOf("→")+1,assignText.length);
	var data={
	    "checkNos":checkNos.join(","),
	    "assignNo":$("#assignNo").combobox("getValue")
	    //"assignName":assignName
	};
	$.messager.confirm("确认","你确定要分配这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show","正在分配任务......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
				  	 if(data.result=='success'){
        				alert('分配成功!');
        				billchcheck.searchArea();
        		 	}else{
        			 	alert(data.msg,2);
        		 	}
				  	wms_city_common.loading();
				  }
			});
        }  
    });
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

//加载数据
billchcheck.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_ch_check_direct/list.json?locno='+billchcheck.locno,
    			'pageNumber':1
    		});
};

billchcheck.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};


billchcheck.loadDetail = function(rowData){
    $('#dataGridJGCheckDtl').datagrid(
		{
			'url':BasePath+'/bill_ch_check_dtl/dtl_list.json?locno='+billchcheck.locno+'&checkNo='+rowData.checkNo,
			'pageNumber':1
		});
};
//选择计划单号
billchcheck.selectPlanNo = function(rowData){
	$("#planNoDialog").window('open'); 
	var queryMxURL=BasePath+'/bill_ch_check_direct/selectPlanNo?status=10&planStatus=15&locno='+billchcheck.locno;
    $( "#dataPlanNo").datagrid( 'options' ).url=queryMxURL;
    $( "#dataPlanNo").datagrid( 'load' );
};

 billchcheck.checkPlanNo = function(rowData){
 	$("#planNo").val(rowData.planNo);
 	$("#planNoDialog").window('close');
 };
 
 //打印
 
billchcheck.printBillchcheck = function(){
	
	var checkedRows = $("#dataGridJGCheck").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length==0){
		alert("请选择需要打印的单据",1);
		return;
	}
	
	var key = [];
	$.each(checkedRows, function(index, item){
		key.push(item.locno+"|"+item.checkNo+"|"+item.ownerNo);
	});       
	
	var rowData = checkedRows[0];
	var url = BasePath+'/bill_ch_check_dtl/printBatch';
	wms_city_common.loading("show","正在获取打印信息......");
	$.ajax({
        type: 'POST',
        url: url,
        data: {
        		key:key.join(",")
        },
        cache: true,
        async: false,
        success: function(returnData){
        	wms_city_common.loading();
        	if(returnData.result=="success"){
        		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
             	if(LODOP==null){
             		return;
             	}
             	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>"
        		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
             	var data = returnData.data;
        		for(var i=0,length=data.length;i<length;i++){
        			
        			var headHtml =  getHtmlByTemplate4Head(data[i].main,data[i].cellCount);
        			var bodyHtml = getHtmlByTemplate4Body(data[i].list);
        			var footerHtml = getHtmlByTemplate4Footer(returnData.curDate);
        			
        			//设置表格内容
        			LODOP.ADD_PRINT_TABLE(120,0,"100%",390,strStyle+bodyHtml);
        			LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
        			
        			//设置表格头部
        			LODOP.ADD_PRINT_HTM(0,0,"100%",120,headHtml);
        			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
        			
        			//设置表格底部
        			LODOP.ADD_PRINT_HTM(730,0,"100%",50,footerHtml);
        			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);	
        			
        		 	//设置条码
        			LODOP.ADD_PRINT_BARCODE(40,820,250,40,"128A",data[i].main.checkNo);
        			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
        			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
        			
        			LODOP.NewPageA();
        		}
        		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
        	 	LODOP.PREVIEW();
        	}else{
        		alert(returnData.msg,2);
        	}
		}
      });
};

function getHtmlByTemplate4Head(data,cellCount){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='3'>盘点单</td></tr>";
	html += "<tr style='height:40px;'><td colspan='3' style='width:100%;'>盘点单号："+data.checkNo+"</td></tr>";
	html += "<tr><td width=20%>盘点类型：初盘/复盘</td><td width=60% style='text-align:center;'>盘点人员："+(data.assignNo==null?"":data.assignNo)+"</td><td width=20% style='text-align:right;padding-right:5px;'>储位总数："+cellCount+"</td></tr>";
	html += "</table>";
	return html;
}

function getHtmlByTemplate4Body(data){
	var html = "<table border='1' cellspacing='0' cellpadding='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'><thead><tr bgcolor='#fff'>";
	html += "<td>序号</td><td>储位</td><td>商品编码</td><td>商品名称</td><td>颜色</td><td>尺码</td><td>实盘数量</td></tr></thead><tbody>";
	var colorName;
	var itemName;
	var sizeNo;
	for(var i=0,length=data.length;i<length;i++){
		colorName = data[i].colorName==null?'':data[i].colorName;
		itemName = data[i].itemName==null?'':data[i].itemName;
		sizeNo = data[i].sizeNo==null?'':data[i].sizeNo;
		html += "<tr bgcolor='#fff'>";
		html += "<td>"+(i+1)+"</td>";
		html += "<td>"+data[i].cellNo+"</td>";
		html += "<td>"+data[i].itemNo+"</td>";
		html += "<td>"+itemName+"</td>";
		html += "<td>"+colorName+"</td>";
		html += "<td>"+sizeNo+"</td>";
		html += "<td>"+data[i].realQty+"</td>";
		html += "</tr>";
	}
	html = html + "<tfoot><tr><td style='text-align:13px;text-align:center;' colspan='7'><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html += "</tbody></table>";
	return html;
}

function getHtmlByTemplate4Footer(curDate){
	var html = "<table border='0' cellspacing='0' cellpadding='0' width='100%'>";
	html += "<tr><td style='text-align:right;width:70%;'>打印时间："+curDate+"</td></tr>";
	html += "</table>";
	return html;
}

/**盘点日期限制在当前天的前后几天范围内**/
function planDateCheck(curDate,planDate,beforeDay,afterDay){
	if(curDate.length > 0 && planDate.length > 0){
		var arrCurDate = curDate.split("-");
		var arrPlanDate = planDate.split("-");
		var befDate = new Date(arrCurDate[0],arrCurDate[1],arrCurDate[2]);   
		var aftDate = new Date(arrCurDate[0],arrCurDate[1],arrCurDate[2]);
        var allPlanDate = new Date(arrPlanDate[0],arrPlanDate[1],arrPlanDate[2]);   
        befDate.setTime(befDate.getTime()-(beforeDay*24*60*60*1000));
        aftDate.setTime(aftDate.getTime()+(afterDay*24*60*60*1000));
        if(allPlanDate.getTime() >= befDate.getTime() && allPlanDate.getTime() <= aftDate.getTime()){
        	return true;
        }
	}
	return false;
}
billchcheck.restoreCheck = function(){
	var checkedRows = $("#dataGridJGCheck").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	if(len==0){
		alert("请选择需要还原的单据",1);
		return;
	}
	var checkNos = '';
	var values = '';
	for(var idx=0;idx<len;idx++){
		if(checkedRows[idx].status != '10' && checkedRows[idx].status != '20'){
			alert("只能还原【新建】或【已发单】状态的单据",1);
			return;
		}
		checkNos += checkedRows[idx].checkNo+'|';
		values += checkedRows[idx].checkNo+'_'+checkedRows[idx].planNo+'|';
	}
	checkNos = checkNos.substring(0,checkNos.length-1);
	values = values.substring(0,values.length-1);
	$.messager.confirm("确认","您确定要还原这"+len+"条单据吗？", function (r){
		if(r){
			var url_1 = BasePath+'/bill_ch_check/hasBegunCheck';
			var params_1 = {
					locno:billchcheck.locno,
					checkNos:checkNos
			}
			wms_city_common.loading("show", "正在检测判定点明细......");
			$.post(url_1, params_1, function(result) {
				var url_2 = BasePath+'/bill_ch_check/restoreCheck';
				var params_2 = {
						locno:billchcheck.locno,
						values:values
				}
				wms_city_common.loading();
				if(result.result == 'success'){
					$.messager.confirm("确认",result.msg, function (r1){
						if(r1){
							updateStatus(url_2, params_2);
						}
					});
				}else if(result.result != null){
					alert(result.msg,1);
					return;
				}else{
					updateStatus(url_2, params_2);
				}
			}, "JSON").error(function() {
		    	alert('还原失败!',1);
		    	wms_city_common.loading();
		    });
		}
	});
}
function updateStatus(url_2, params_2){
	wms_city_common.loading("show", "正在还原盘点单......");
	$.post(url_2, params_2, function(r2) {
		billchcheck.searchArea();
		wms_city_common.loading();
		if(r2.result == null){
			alert('还原成功');
		}else{
			alert(r2.msg,1);
		}
	}, "JSON").error(function() {
    	alert('还原失败!',1);
    	wms_city_common.loading();
    });
}