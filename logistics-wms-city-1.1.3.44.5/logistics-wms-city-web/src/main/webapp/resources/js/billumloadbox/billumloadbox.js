var billumloadbox = {};
billumloadbox.umCheckStatus11 = "11";
billumloadbox.status10= "10";
billumloadbox.status11= "11";
billumloadbox.status13= "13";
billumloadbox.lookupcode_box_status="UMLOADBOX_STATUS";
billumloadbox.lookupcode_untread_status= "CITY_UM_CHECK_STATUS";
//退仓类型编码
billumloadbox.lookupcode_untread_type = "ITEM_TYPE";

billumloadbox.owner_data;
billumloadbox.typeData;
billumloadbox.locno;
$(document).ready(function(){
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billumloadbox.initCurrentUser();
	billumloadbox.initBoxStatus();
	billumloadbox.initOwner();
	billumloadbox.initUntreadType();
	billumloadbox.initBoxUser();
	billumloadbox.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',{},billumloadbox.initQuality);
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch_box'),"brandNoObj":$('#brandNo_box')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	$('#checkGridDetail').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data != null) {
						if(data.footer != null){
							if(data.footer[1].isselectsum){
								billumloadbox.itemQty = data.footer[1].itemQty;
								billumloadbox.checkQty = data.footer[1].checkQty;
								billumloadbox.difQty = data.footer[1].difQty;
				   			}else{
				   				var rows = $('#checkGridDetail').datagrid('getFooterRows');
					   			rows[1]['itemQty'] = billumloadbox.itemQty;
					   			rows[1]['checkQty'] = billumloadbox.checkQty;
					   			rows[1]['difQty'] = billumloadbox.difQty;
					   			$('#checkGridDetail').datagrid('reloadFooter');
				   			}
						}
					}
		   			
		   		}
			}
		);
	$('#loadBoxDtl_view').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data != null) {
						if(data.footer != null){
							if(data.footer[1].isselectsum){
								billumloadbox.itemQty4BoxDtl = data.footer[1].itemQty;
								billumloadbox.boxingQty4BoxDtl = data.footer[1].boxingQty;
				   			}else{
				   				var rows = $('#loadBoxDtl_view').datagrid('getFooterRows');
					   			rows[1]['itemQty'] = billumloadbox.itemQty4BoxDtl;
					   			rows[1]['boxingQty'] = billumloadbox.boxingQty4BoxDtl;
					   			$('#loadBoxDtl_view').datagrid('reloadFooter');
				   			}
						}
					}
		   			
		   		}
			}
		);
	
	$('#boxDtlGrid_view').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data != null) {
						if(data.footer != null){
							if(data.footer[1].isselectsum){
								billumloadbox.itemQty4BoxDtl = data.footer[1].itemQty;
				   			}else{
				   				var rows = $('#boxDtlGrid_view').datagrid('getFooterRows');
					   			rows[1]['itemQty'] = billumloadbox.itemQty4BoxDtl;
					   			$('#boxDtlGrid_view').datagrid('reloadFooter');
				   			}
						}
					}
		   			
		   		}
			}
		);
	
});
billumloadbox.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//==================商品类型=======================
billumloadbox.areaQuality = {};
billumloadbox.qualityFormatter = function(value, rowData, rowIndex){
	return billumloadbox.areaQuality[value];
};
billumloadbox.initQuality = function(data){
	wms_city_common.comboboxLoadFilter(
				["quality_search"],
				null,
				null,
				null,
				true,
				[true],
				BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
				{},
				billumloadbox.areaQuality,
				null);
};
billumloadbox.tansforListToMap = function(oriData){
	var str = "";
	for(var i = 0,length = oriData.length;i<length;i++){
		str = str+"\""+oriData[i].itemvalue +"\":\""+oriData[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};


billumloadbox.initBoxUser = function(){
	wms_city_common.comboboxLoadFilter(
			["boxCreator","boxAuditor"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true,true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null); 
};

billumloadbox.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billumloadbox.locno = data.locno;
	});
};
//搜索验收单单
billumloadbox.searchCheckData = function(){
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
	
	var startCreatetm = $('#startCreatetmCondition').datebox('getValue');
	var endCreatetm = $('#endCreatetmCondition').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var startAudittm = $('#startAudittmCondition').datebox('getValue');
	var endAudittm = $('#endAudittmCondition').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_loadbox/selectCheck4LoadBox?locno='+billumloadbox.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billumloadbox.locno;
	reqParam['status'] = billumloadbox.status11+','+billumloadbox.status13;
	billumloadbox.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};

//生成任务
billumloadbox.createLoadBox = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要生成任务的记录!',1);
		return;
	}
	var isNoTz = 'N';
	var keyStr = [];
	var untreadMmNos =[];
	var allOk =true;
	var untreadMmNo;
	var ownerNo;
	var untreadType;
	var firstTypeAndQuality;
	var typeAndQuality; 
	$.each(checkedRows,function(index,item){
		var status = item.status;
		if(status!=billumloadbox.umCheckStatus11 && status!=billumloadbox.status13){
			allOk = false;
			return false;
		}
		if(index==0){
			untreadMmNo = item.untreadMmNo;
			ownerNo = item.ownerNo;
			untreadType = item.itemType;
			firstTypeAndQuality = item.itemType+"-"+item.quality;
		}
		typeAndQuality = item.itemType+"-"+item.quality;
		if((item.untreadMmNo == '-' || item.untreadMmNo =='' || item.untreadMmNo == null)
				&& firstTypeAndQuality != typeAndQuality){
			isNoTz = 'Y';
			return false;
		}
		keyStr.push(item.checkNo);
		untreadMmNos.push(item.untreadMmNo);
	});
	if(!allOk){
		alert("非验收状态的数据不能生成任务!",1);
		return;
	};
	
	var repate = billumloadbox.getRepeat(untreadMmNos);
	if(repate.length>1){
		alert("必须是相同的通知单号才能生成任务!",1);
		return;
	}
	
	//如果通知单为空，请选择属性和品质相同的验收单
	if(isNoTz == 'Y'){
		alert('请选择属性和品质相同的验收单!',1);
		return;
	}
	
	$.messager.confirm("确认","你确定要 将这"+checkedRows.length+"条数据生成任务吗？", function (r){  
		
        if (r) {
            var url = BasePath+'/bill_um_loadbox/createLoadBox';
            var data={
            	"untreadMmNo":untreadMmNo,
            	"ownerNo":ownerNo,
            	"untreadType":untreadType,
            	"keyStr":keyStr.join(",")
            };
            wms_city_common.loading("show","正在生成任务......");
        	$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				data:data,
				dataType : "json",
				url:url,
				success : function(data) {
					 wms_city_common.loading();
					if(data.result=="success"){
						alert('任务生成成功!');
		 				billumloadbox.refresh("dataGridJG");
		 				billumloadbox.refresh("loadBoxGrid");
					}else{
						alert(data.msg,2);
					}
				}
			});
        }  
    });
};

billumloadbox.getRepeat = function(tempary){
	var ary=[];
	for(var i = 0;i<tempary.length;i++){
		if(tempary[i]!=null||tempary[i]!=""){
			ary.push(tempary[i]);
		}
	}
	var res = [];  
	ary.sort();  
	for(var i = 0;i<ary.length;){
	 var count = 0;  
	 for(var j=i;j<ary.length;j++){
	  if(ary[i]== ary[j]) {  
	   count++;  
	  }    
	 }
	 if(count>=1){
	 	res.push(ary[i]); 
	 }
	 i+=count;   
	};
	return res;
};
//查询箱主档
billumloadbox.searchArea = function(){
	var startCreatetm = $('#boxCreatetmStart').datebox('getValue');
	var endCreatetm = $('#boxCreatetmEnd').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var startAudittm = $('#boxAudittmStart').datebox('getValue');
	var endAudittm = $('#boxAudittmEnd').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    }
	
	var fromObjStr=wms_city_common.convertArray($('#loadBoxForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_loadbox/mainlist.json?locno='+billumloadbox.locno;
    $( "#loadBoxGrid").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#loadBoxGrid").datagrid( 'options' ).url=queryMxURL;
    $( "#loadBoxGrid").datagrid( 'load' );
}
//清空
billumloadbox.searchClear = function(id){
	$('#'+id).form("clear");
};
//初始化箱明细状态
//格式化装箱主档状态
billumloadbox.boxStatusFormatter  =function(value, rowData, rowIndex){
	return billumloadbox.box_status[value];
};
billumloadbox.box_status={};
billumloadbox.initBoxStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["boxStatus"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumloadbox.lookupcode_box_status,
			{},
			billumloadbox.box_status,
			null);
	
};
billumloadbox.initOwner= function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/entrust_owner/get_biz',
		success : function(data) {
			billumloadbox.owner_data = billumloadbox.converStr2JsonObj(data);
			$('#boxOwnerNo').combobox({
			    data:data,
			    valueField:'ownerNo',    
			    textField:'textFieldName',
			    panelHeight:200,
			    loadFilter:function(data){
			       	 if(data.length>0){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.textFieldName = tempData.ownerNo+'→'+tempData.ownerName;
			       		 }
			       	 }
			    	 return data;
			    }
			});
		}
	});
};

billumloadbox.initUntreadType = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumloadbox.lookupcode_untread_type,
		success : function(data) {
			billumloadbox.typeData = billumloadbox.converStr2JsonObj2(data);
		}
	});
};

billumloadbox.converStr2JsonObj= function(data){
	if(data==null){
		 return "";
	}
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};


billumloadbox.converStr2JsonObj2= function(data){
	if(data==null){
		 return "";
	}
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billumloadbox.boxOwnerFormatter  =function(value, rowData, rowIndex){
	return billumloadbox.owner_data[value];
}
billumloadbox.typeFormatter  =function(value, rowData, rowIndex){
	return billumloadbox.typeData[value];
}

//箱明细、拆箱
billumloadbox.splitBox = function(value, rowData, rowIndex){
	var checkedRows = $("#loadBoxGrid").datagrid("getChecked");
	var rowData = checkedRows[0];
	if(checkedRows.length < 1){
		alert('请选择要封箱的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能选择一条记录!',1);
		return;
	}else if(rowData.status!=billumloadbox.status10){
		alert('非建单状态的数据不封箱!',1);
		return;
	}
	if(rowData.noSealedCount>0){ //FR未封箱的
		$('#RfSplitBox').window('open');
		$("#loadRfBoxDtlForm").form("load",rowData);
		$("#rf_box_owenrName").val(billumloadbox.boxOwnerFormatter(rowData.ownerNo));
		$("#rf_box_itemType_name").val(billumloadbox.typeFormatter(rowData.itemType));
		$("#rf_itemType4fx").val(rowData.itemType);
	    var queryMxURL=BasePath+'/bill_um_box_dtl/dtllist.json?locno='+billumloadbox.locno+'&loadboxNo='+rowData.loadboxNo+'&ownerNo='+rowData.ownerNo+'&noSealed=Y';
	    $( "#RfboxDtlGridx").datagrid( 'options' ).url=queryMxURL;
	    $( "#RfboxDtlGridx").datagrid( 'load' );
	}else{
		$('#splitBox').window('open');
		$("#loadBoxDtlForm").form("load",rowData);
		$("#box_owenrName").val(billumloadbox.boxOwnerFormatter(rowData.ownerNo));
		$("#box_itemType_name").val(billumloadbox.typeFormatter(rowData.itemType));
		$("#itemType4fx").val(rowData.itemType);
		var queryMxURL=BasePath+'/bill_um_loadbox_dtl/dtllist.json?locno='+billumloadbox.locno+'&loadboxNo='+rowData.loadboxNo+'&ownerNo='+rowData.ownerNo;
	    $( "#loadBoxDtl").datagrid( 'options' ).url=queryMxURL;
	    $( "#loadBoxDtl").datagrid( 'load' );
	    
	    queryMxURL=BasePath+'/bill_um_box_dtl/dtllist.json?locno='+billumloadbox.locno+'&loadboxNo='+rowData.loadboxNo+'&ownerNo='+rowData.ownerNo;
	    $( "#boxDtlGrid").datagrid( 'options' ).url=queryMxURL;
	    $( "#boxDtlGrid").datagrid( 'load' );
	}
}

billumloadbox.loadBoxDtl= function(rowData){
	$('#splitBox_view').window('open');
	$("#loadBoxDtlForm_view").form("load",rowData);
	$("#box_owenrName_view").val(billumloadbox.boxOwnerFormatter(rowData.ownerNo));
	$("#box_owenrNo_view").val(rowData.ownerNo);
	$("#box_itemType_name_view").val(billumloadbox.typeFormatter(rowData.itemType));
	
	var queryMxURL=BasePath+'/bill_um_loadbox_dtl/dtllist.json?locno='+billumloadbox.locno+'&loadboxNo='+rowData.loadboxNo+'&ownerNo='+rowData.ownerNo;
    $( "#loadBoxDtl_view").datagrid( 'options' ).url=queryMxURL;
    $( "#loadBoxDtl_view").datagrid( 'load' );
    
    queryMxURL=BasePath+'/bill_um_box_dtl/dtllist.json?locno='+billumloadbox.locno+'&loadboxNo='+rowData.loadboxNo+'&ownerNo='+rowData.ownerNo;
    $( "#boxDtlGrid_view").datagrid( 'options' ).url=queryMxURL;
    $( "#boxDtlGrid_view").datagrid( 'load' );
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
//导出
billumloadbox.do_export = function(){
	var exportColumns = $("#loadBoxDtl_view").datagrid('options').columns[0];
	var str = '[';
	for(var index=0;index<exportColumns.length;index++){
		var temp = exportColumns[index];
		str +=  "{\"field\":\""+temp.field+"\",\"title\":\""+temp.title+"\"}";
		if(index != exportColumns.length-1){
			str += ",";
		}
	}
	str += "]";
	
	var loadboxNo = $("#box_loadBoxNo_view").val();
	var ownerNo = $("#box_owenrNo_view").val();
	
	var searchUrl = BasePath+'/bill_um_loadbox_dtl/dtllist.json?locno='+billumloadbox.locno+'&loadboxNo='+loadboxNo+'&ownerNo='+ownerNo;
	
	var fromObjStr=convertArray($('#loadBoxDtlForm_view').serializeArray());
	var data = eval("(" +fromObjStr+ ")");
	data.page = 1;
	data.rows = $("#loadBoxDtl_view").datagrid('options').pageSize;
	var resutl = {};
	$.ajax({
		  async:false,
		  type: 'POST',
		  url: searchUrl,
		  data: data,
		  cache: true,
		  success: function(r){
			  resutl = r;
		  }
	});
	if(!resutl || resutl == null || resutl.total == 0){
		alert("没有可以导出的数据!",1);
		return;
	}else if(resutl.total > 10000){
		alert("数据大于10000条不能导出!",1);
		return;
	}
	$("#locno_export").val(billumloadbox.locno);
	$("#ownerNo_export").val(ownerNo);
	$("#loadboxNo_export").val(loadboxNo);
	$("#exportColumnsCondition_export").val(str);
	$("#exportForm").submit();
};

billumloadbox.splitBoxOk =function(){
	var curObj = $('#loadBoxDtl');
	var rows = curObj.datagrid("getChecked");
	if(rows==null||rows.length==0){
		alert("请选择要封箱的记录!",1);
		return;
	}
	deleteAllGridCommon("splitBoxConfirmGrid");
	for(var i=0,length = rows.length;i<length;i++){
		$("#splitBoxConfirmGrid").datagrid("appendRow",rows[i]);
		$("#splitBoxConfirmGrid").datagrid("beginEdit",i);
	}
	$("#splitBoxConfirm").window("open");
};

billumloadbox.splitBoxDo = function(){
	var curObj = $('#splitBoxConfirmGrid');
	var rows = curObj.datagrid("getRows");
	var keyStr = [];
	for(var i=0,length = rows.length;i<length;i++){
		curObj.datagrid('endEdit',i);
		var checkQty = rows[i].checkQty;
		if(checkQty==""||checkQty==null){
			alert("第"+(i+1)+"行封箱数量不能为空");
			return ;
		}
		if(checkQty==0){
			alert("第"+(i+1)+"行封箱数量不能为0");
			return ;
		}
		if(checkQty<0){
			alert("第"+(i+1)+"行封箱数量不能为负");
			return ;
		}
		if(checkQty>rows[i].itemQty-rows[i].boxingQty){
			alert("第"+(i+1)+"行封箱数量不能大于剩余数量");
			return ;
		}
		keyStr.push(rows[i].itemNo+"|"+rows[i].sizeNo+"|"+rows[i].checkQty+"|"+rows[i].brandNo);
	}
	$.messager.confirm("确认","你确定要 将这"+rows.length+"条数据封箱吗？", function (r){  
			
	        if (r) {
	        	wms_city_common.loading("show","正在封箱......");
	            var url = BasePath+'/bill_um_box_dtl/createBoxDtl';
	            var data={
	            	"keyStr":keyStr.join(","),
	            	loadboxNo:$("#box_loadBoxNo").val(),
	            	itemType:$("#itemType4fx").val(),
	            	ownerNo:$("#box_owenrNo").val()
	            };
	        	$.ajax({
					async : false,
					cache : false,
					type : 'POST',
					data:data,
					dataType : "json",
					url:url,
					success : function(data) {
						wms_city_common.loading();
						if(data.result=="success"){
							alert('封箱成功!');
			 				billumloadbox.refresh("loadBoxDtl");
			 				billumloadbox.refresh("boxDtlGrid");
			 				billumloadbox.refresh("loadBoxGrid");
			 				billumloadbox.closeWindow("splitBoxConfirm");
						}else if(data.flag=="error"){
							alert(data.msg,2);
						}
						else{
							alert('封箱失败,请联系管理员',2);
						}
						
					}
				});
	        }  
	    });
};

billumloadbox.refresh = function(dataGridId){
	$("#"+dataGridId).datagrid('load');
};


billumloadbox.closeWindow = function(id){
	$("#"+id).window("close");
}

billumloadbox.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billumloadbox.audit = function(){
	var curObj = $('#loadBoxGrid');
	var checkedRows = curObj.datagrid("getChecked");
	if(checkedRows==null||checkedRows.length==0){
		alert("请选择要审核的记录!",1);
		return;
	}
	var keyStr = [];
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!="10"){
			allOk = false;
			return false;
		}
		keyStr.push(item.loadboxNo+"|"+item.ownerNo);
	});  
	if(!allOk){
		alert("非建单状态的数据不能审核",1);
		return;
	} 
    var url = BasePath+'/bill_um_loadbox/auditLoadBox';
	var data={
	    "keyStr":keyStr.join(",")
	};
	
	$.messager.confirm("确认","你确定要审核这"+checkedRows.length+"条数据", function (r){  
        if (r) {
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
				  	 if(data.result=='success'){
        				 alert('审核成功!');
        				 billumloadbox.refresh("loadBoxGrid");
        		 	}else{
        			 	alert('审核失败,请联系管理员!',2);
        		 	}
				  }
			});
        }  
    });
};

//打印
billumloadbox.printByBox = function(){
	// 获取勾选的行
	var checkedRows = $("#boxDtlGrid_view").datagrid("getSelected");
	if(checkedRows == null){
		alert('请在箱明细中选择要打印的记录!',1);
		return;
	}
	billumloadbox.loadPrint(checkedRows,"edit");
};
billumloadbox.loadPrint = function(rowData,type){
	var loadboxNo = $("#box_loadBoxNo_view").val();//装箱单号
	
	var rows = $('#boxDtlGrid_view').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].loadboxNo+"|"+rows[i].boxNo+"|"+rows[i].creator+"|"+rows[i].createtm);
	}
	var url = BasePath+'/bill_um_box_dtl/printByBox';
	var params = {
			keys:keys.join(",")
	};
	var resultData;
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: url,
        data: params,
        success: function(result){
        	resultData = result;
		}
      });
	if(resultData.result!="success"){
		alert(resultData.msg);
		return;
     }else{
    	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
      	if(LODOP==null){
      		return;
      	}
     	var data = resultData.data;
 		for(var i=0,length = data.length;i<length;i++){
 			LODOP.NewPage();
 			LODOP.SET_PRINT_PAGESIZE(1,'1000','1600',"");
    		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",data[i].html);
    		LODOP.ADD_PRINT_BARCODE(100,120,220,40,"128A",data[i].boxNo);
 		}
    	LODOP.PREVIEW();
     }
};

billumloadbox.RfSplitBoxOk = function(){
	$.messager.confirm("确认","你确定封箱吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show","正在封箱......");
        	$.ajax({
      		  type: 'POST',
      		  url: BasePath+'/bill_um_box_dtl/createRfNoSealed',
      		  data: {
      			  loadboxNo:$("#rf_box_loadBoxNo").val(),
      			  itemType:$("#rf_itemType4fx").val(),
      		  },
      		  cache: true,
      		  success: function(data){
      			wms_city_common.loading();
      		  	 if(data.result=='success'){
      				 alert('封箱成功!');
      				 billumloadbox.closeWindow("RfSplitBox");
      				 billumloadbox.refresh("loadBoxGrid");
      		 	}else{
      			 	alert('封箱失败,请联系管理员!',2);
      		 	}
      		  }
      	});
        }  
    });
	
};
