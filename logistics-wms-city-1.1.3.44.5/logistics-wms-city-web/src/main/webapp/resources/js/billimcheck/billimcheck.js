var billimcheck = {};
billimcheck.user = {};
billimcheck.tansforArrayToMap = function(oriData){
	var str = "";
	for(var i = 0,length = oriData.length;i<length;i++){
		str = str+"\""+oriData[i].itemvalue +"\":\""+oriData[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};

billimcheck.deleterow = function(){
	var obj = $('#itemDetail');
	var item = obj.datagrid('getChecked');
	if(item.length < 1){
		alert("请勾选验收单明细");
		return;
	}
	
	var tipStr = "";
	for (var i = item.length - 1; i >= 0; i--) {
    	var sourceType = item[i].sourceType;
    	var boxNo = item[i].boxNo;
    	var itemNo = item[i].itemNo;
    	var sizeNo = item[i].sizeNo;
    	if(sourceType == 1){
    		tipStr = "箱号："+boxNo+",商品："+itemNo+",尺码："+sizeNo+"是收货单明细, 不能删除";
    		break;
    	}
    }
	
	if(tipStr!=""){
		alert(tipStr);
		return;
	}
	
    for (var i = item.length - 1; i >= 0; i--) {
        var index = obj.datagrid('getRowIndex', item[i]);
        obj.datagrid('deleteRow', index);
    }
	
//	var row = obj.datagrid('getSelected');
//	if(row==null){
//		alert("请选择要删除的记录",1);
//		return;
//	}
//	row.receiptNo=$("#sImportNo").val();
//	if(row.sourceType==1){
//		alert("该明细是收货单明细, 不能删除",1);
//		return;
//	}
//	var index = obj.datagrid('getRowIndex', row);
//	$('#itemDetail').datagrid('deleteRow',index);
};


billimcheck.status = {};
billimcheck.statusFormatter = function(value, rowData, rowIndex){
	return billimcheck.status[value];
};
//==================委托业主====================
billimcheck.ownerNo = {};
billimcheck.ownerNoFormatter = function(value, rowData, rowIndex){
	return billimcheck.ownerNo[value];
};
billimcheck.tansforOwner = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};
//==================委托业主====================

billimcheck.exportExcel = function(){
	exportExcelBaseInfo('dataGridJG','/bill_im_check/do_export.htm?businessType=9','委托业主管理信息导出');
};

billimcheck.onLoadSuccess = function(data){
	if(data.footer[1] != null){
		billimcheck.mainSumFoot = data.footer[1];
		}else{
			var rows = $('#dataGridJG').datagrid('getFooterRows');
   			rows[1] = billimcheck.mainSumFoot;
			$('#dataGridJG').datagrid('reloadFooter');
		}
};

billimcheck.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billimcheck.openUI = function(opt){
	$('#openUI').window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$("#opBtn").show();
	$("#importNoBtn").attr("disabled",false);
	$('#openUI').window('open');
};
billimcheck.closeUI = function(opt){
	$('#openUI').window('close');
	$("#opt").val("");
};
billimcheck.addUI = function(opt){
	billimcheck.openUI("新增");
	billimcheck.clearAll();
	billimcheck.enableCombox();
	$("#locno").attr('readOnly',false);
	$("#containerType+ span input.combo-text").attr("readOnly",false);
	$("#containerType+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#containerType+ span").css("background-color","white");
	$("#containerType+ span input.combo-text").css("background-color","white");
	
	$("#useType+ span input.combo-text").attr("readOnly",false);
	$("#useType+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#useType+ span").css("background-color","white");
	$("#useType+ span input.combo-text").css("background-color","white");
	$("#opBtn").hide();
	$("#opt").val("add");
	$("#save_main").show();
	$("#edit_main").hide();
};
billimcheck.check = function(){
	var checkRows = $("#dataGridJG").datagrid('getChecked');
	if(checkRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	var checkIds = [];
	for(var i=0;i<checkRows.length;i++){
		if(checkRows[i].status!='10'){
			alert(checkRows[i].checkNo+'非建单状态的单据不能审核!',1);
			return ;
		}
		checkIds[checkIds.length] = {locno:checkRows[i].locno,ownerNo:checkRows[i].ownerNo,checkNo:checkRows[i].checkNo};
	}

	var messageTip = "";
	var num = 0;
	for(var idx=0;idx<checkRows.length;idx++){
		num = billimcheck.checkAuditIsTip(checkRows[idx]);
		if(num > 0){
			messageTip += checkRows[idx].checkNo+',';
		}
	}
	if(messageTip != ''){
		messageTip = '【'+messageTip.substring(0, messageTip.length-1)+'】';
		messageTip += '存在差异，是否继续审核？';
	}else{
		messageTip = '您确定要审核选中的单据吗？';
	}
	$.messager.confirm('确认',messageTip,function(r){
		if(!r){
			return;
		}
		wms_city_common.loading("show","正在审核......");
		$.ajax({
		  type: 'POST',
		  url: BasePath+'/bill_im_check/check',
		  data: {
		  	datas:JSON.stringify(checkIds),
		  },
		  cache: true,
		  async:false, // 一定要
		  success: function(data){
			  wms_city_common.loading();
			  if(data.result=="success"){
			  	 alert('审核成功!');
			  	 billimcheck.searchData();
			  }else{
			  	alert(data.msg);
			  }
		  }
     	});
	 	$("#btn-audit").linkbutton('enable');
	});
	
	
//	if(checkRows.length>1){
//		alert('只能审核一条记录!',1);
//		return;
//	}else if(checkRows[0].status!='10'){
//		alert('非建单状态的单据不能审核!',1);
//		return ;
//	}else{
//		
//		var messageTip = "";
//		var num = billimcheck.checkAuditIsTip(checkRows[0]);
//		if(num > 0){
//			messageTip = "该单存在差异,";
//		}
//		
//		$.messager.confirm('确认',messageTip+'您确定要审核选中的单据吗？',function(r){
//			
//			if(!r){
//				return;
//			}
//			wms_city_common.loading("show","正在审核......");
//			$.ajax({
//			  type: 'POST',
//			  url: BasePath+'/bill_im_check/check',
//			  data: {
//			  	locno:checkRows[0].locno,
//			  	checkNo:checkRows[0].checkNo,
//			  	ownerNo:checkRows[0].ownerNo
//			  },
//			  cache: true,
//			  async:false, // 一定要
//			  success: function(data){
//				  wms_city_common.loading();
//				  if(data.result=="success"){
//				  	 alert('审核成功!');
//				  	 billimcheck.searchData();
//				  }else{
//				  	alert(data.msg);
//				  }
//			  }
//	     	});
//		 	$("#btn-audit").linkbutton('enable');
//		});
//	}
	
};

billimcheck.checkAuditIsTip = function(checkRows){
	var num = 0;
	var url = BasePath+'/bill_im_check_dtl/selectCheckDtlDiffCount';
	var params = {
		locno : checkRows.locno,
		checkNo : checkRows.checkNo,
		ownerNo : checkRows.ownerNo
	};
	ajaxRequestAsync(url,params,function(data){
		num = data.result;
	});
	return num;
};

billimcheck.manage = function(opt){
	var opt = $("#opt").val();
	billimcheck.saveDetail();
};
billimcheck.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bill_im_check/list.json?locno='+billimcheck.user.locno,'title':'验收单列表','pageNumber':1 });
};

billimcheck.editUI = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status!='10'){
		alert('非建单状态的单据不能修改!',1);
		return ;
	}else if(checkedRows[0].businessType=='2'){
		alert('装箱验收的单据不能修改!',1);
		return ;
	}else{
		billimcheck.loadDetail(checkedRows[0],"edit");
	}
};


billimcheck.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		$('#openUIShow').window('close');
		billimcheck.openUI("修改");
		$("#save_detail").linkbutton('enable');
		$("#importNoBtn").attr('disabled',true);
		billimcheck.disableCombox();
		$("#sImportNo").attr('readOnly',true);
		$("#opBtn").show();
		$("#edit_main").show();
		$("#save_main").hide();
		$('#dataForm').form('load',rowData);
		//清空列表
	    deleteAllGridCommon("itemDetail");
		$('#itemDetail').datagrid({
			'url':BasePath+'/bill_im_check_dtl/getByPage.json?checkNo='+rowData.checkNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo
		});
	}else{
		$('#openUI').window('close');
		$('#openUIShow').window('open');
		$('#dataFormShow').form('load',rowData);
		//var url = BasePath+'/bill_im_check_dtl/getByPage.json?checkNo='+rowData.checkNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo;
		var url = BasePath+'/bill_im_check_dtl/getByPageDtl.json?checkNo='+rowData.checkNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&receiptNo="+rowData.sImportNo;
		$( "#itemDetailShow").datagrid( 'options' ).url=url;
	    $( "#itemDetailShow").datagrid( 'load' );
		//$('#itemDetailShow').datagrid({'url':BasePath+'/bill_im_check_dtl/getByPage.json?checkNo='+rowData.checkNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo,'pageNumber':1 });
	}
};



billimcheck.editMainInfo = function(){
	var validateForm=  $('#dataForm').form('validate');
    if(validateForm==false){
        return ;
    }
    var checkWorker = $("#checkWorkerForAdd").combobox('getValue');
	if(checkWorker==null||checkWorker==""){
		alert("请选择验收人",2);
		return;
	}
    var checkDate = $("#checkEndDate_form").datebox('getValue');
    if(checkDate==null||checkDate==""){
    	alert("验收日期不能为空");   
        return; 
    }
	if(!isStartEndDate(checkDate,wms_city_common.getCurDate())){    
		alert("验收日期不能大于当前日期");   
        return;   
    }   
	var checkWorkerText = $("#checkWorkerForAdd").combobox("getText");
    var checkWorkerName = checkWorkerText.substr(checkWorkerText.indexOf("→")+1,checkWorkerText.length);
	wms_city_common.loading("show","正在修改......");
    var url = BasePath+'/bill_im_check/editMainInfo';
    $('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
//				param.createtm = billimcheck.user.currentDate19Str;
//				param.creator = billimcheck.user.loginName;
				param.editor = billimcheck.user.loginName;
				param.editorName = billimcheck.user.username;
				param.checkName = checkWorkerName;
//				param.edittm = billimcheck.user.currentDate19Str;
				param.locno = billimcheck.user.locno;
			},
			success: function(data){
				  wms_city_common.loading();
				  var a = eval("(" +data+ ")");
				  if(a.result=="true"){
					  billimcheck.searchData();
					  alert('修改成功!');
					  return;
				  }else{
				  	alert(a.msg);
				  	return;
				  }
		    },
			error:function(){
				wms_city_common.loading();
				alert('修改失败!,请联系管理员!',2);
			}
	   });
};


billimcheck.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
billimcheck.disableCombox = function(){
	$("#dataForm input[id=ownerNo]+ span input.combo-text").attr("readOnly",true);
	$("#dataForm input[id=ownerNo]+ span input.combo-text+ span span").removeClass("combo-arrow");
	$("#dataForm input[id=ownerNo]+ span").css("background-color","#EBEBE4");
	$("#dataForm input[id=ownerNo]+ span input.combo-text").css("background-color","#EBEBE4");
};
billimcheck.enableCombox = function(){
	$("#dataForm input[id=ownerNo]+ span input.combo-text").attr("readOnly",false);
	$("#dataForm input[id=ownerNo]+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#dataForm input[id=ownerNo]+ span").css("background-color","white");
	$("#dataForm input[id=ownerNo]+ span input.combo-text").css("background-color","white");
};

billimcheck.checkExistFun = function(url,checkColumnJsonData){
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


billimcheck.saveMainInfo = function(){
	var validateForm=  $('#dataForm').form('validate');
    if(validateForm==false){
        return ;
    }
    var checkWorker = $("#checkWorkerForAdd").combobox('getValue');
	if(checkWorker==null||checkWorker==""){
		alert("请选择验收人",2);
		return;
	}
	
    var checkDate = $("#checkEndDate_form").datebox('getValue');
    if(checkDate==null||checkDate==""){
    	alert("验收日期不能为空");   
        return; 
    }
	if(!isStartEndDate(checkDate,wms_city_common.getCurDate())){    
		alert("验收日期不能大于当前日期");   
        return;   
    }   
    
    //明细信息
    var tempFlag = billimcheck.endEdit("itemDetail");
	if(!tempFlag){
		alert('数据验证没有通过!',1);
		return;
	}
	
//	var tempObj = $('#itemDetail');
//	var inserted = tempObj.datagrid('getChanges', "inserted");
//	var updated = tempObj.datagrid('getChanges', "updated");
//	var del = tempObj.datagrid('getChanges', "deleted");
//	
//	var addTipStr = billimcheck.checkDetailIsPass(inserted);
//    if(addTipStr != ""){
//		alert(addTipStr);
//		return;
//	}
//    
//    var updateTipStr = billimcheck.checkDetailIsPass(updated);
//    if(updateTipStr != ""){
//		alert(updateTipStr);
//    }	
//   
//   //校验重复的记录   
//    var repeatArry =  billimcheck.getRepeat(tempObj.datagrid('getRows'));
//       if(repeatArry!=null&&repeatArry.length>0){
//              var repeatBox = "";
//              for(var i=0;i<repeatArry.length;i++){
//                  repeatBox=repeatBox+repeatArry[i]+"<Br>";
//              }
//              wms_city_common.beginEditAllLine("itemDetail");
//              alert('保存失败,商品'+repeatBox+"已经存在，请重新选择",2);
//              return;
//      }
	var checkWorkerText = $("#checkWorkerForAdd").combobox("getText");
    var checkWorkerName = checkWorkerText.substr(checkWorkerText.indexOf("→")+1,checkWorkerText.length);
    wms_city_common.loading("show","正在保存......");
    var url = BasePath+'/bill_im_check/saveMainInfo';
    $('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
//				param.createtm = billimcheck.user.currentDate19Str;
				param.creator = billimcheck.user.loginName;
				param.editor = billimcheck.user.loginName;
				param.creatorName = billimcheck.user.username;
				param.editorName = billimcheck.user.username;
//				param.edittm = billimcheck.user.currentDate19Str;
				param.locno = billimcheck.user.locno;
				param.checkName = checkWorkerName;
//				param.inserted = encodeURIComponent(JSON.stringify(inserted));
//				param.updated = encodeURIComponent(JSON.stringify(updated));
//				param.del = encodeURIComponent(JSON.stringify(del));
			},
			success: function(r){
				wms_city_common.loading();
				  var r = eval("(" +r+ ")");
				
				if(r && r.result=="success"){
					billimcheck.disableCombox();
					$("#dataForm input[id=checkNo]").val(r.checkNo);
					$("#sImportNo").attr('readOnly',true);
					$("#sImportNo+ input[type=button]").attr("disabled",true);
					//$("#save_detail").linkbutton('enable');
					//$("#add_row").linkbutton('enable');
					$("#opBtn").show();
					$("#edit_main").show();
					$("#save_main").hide();
					billimcheck.searchData();
					alert('新增成功!');
					deleteAllGridCommon('itemDetail');
					$('#itemDetail').datagrid({
						'url':BasePath+'/bill_im_check_dtl/getByPage.json?checkNo='+r.checkNo+'&locno='+billimcheck.user.locno+'&ownerNo='+$("#ownerNo").combobox('getValue')
					});
					 return;
				}else if(r && r.result=="fail"){
					 alert(r.msg,2);
				}
				
		    },
			error:function(){
				wms_city_common.loading();
				alert('保存失败!,请联系管理员!',2);
			}
	   });
};

billimcheck.saveDetail = function(){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = billimcheck.endEdit("itemDetail");
	if(!tempFlag){
		alert('数据验证没有通过!',1);
		return;
	}
	
	var tempObj = $('#itemDetail');
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var updated = tempObj.datagrid('getChanges', "updated");
	var del = tempObj.datagrid('getChanges', "deleted");
	/*
	if(inserted.length<1 && updated.length<1){
			alert('没有数据改动!',1);
			return;
		}*/
	
	
	
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    
    var addTipStr = billimcheck.checkDetailIsPass(inserted);
    if(addTipStr != ""){
		alert(addTipStr);
		return;
	}
    
    var updateTipStr = billimcheck.checkDetailIsPass(updated);
    if(updateTipStr != ""){
		alert(updateTipStr);
		return;
	}
    var checkWorkerText = $("#checkWorkerForAdd").combobox("getText");
    var checkWorkerName = checkWorkerText.substr(checkWorkerText.indexOf("→")+1,checkWorkerText.length);
   //校验重复的记录
    var repeatArry =  billimcheck.getRepeat(tempObj.datagrid('getRows'));
       if(repeatArry!=null&&repeatArry.length>0){
              var repeatBox = "";
              for(var i=0;i<repeatArry.length;i++){
                  repeatBox=repeatBox+repeatArry[i]+"<Br>";
              }
              wms_city_common.beginEditAllLine("itemDetail");
              alert('保存失败,商品'+repeatBox+"已经存在，请重新选择",2);
              return;
      }
     //2.绑定数据
       wms_city_common.loading("show","正在保存......");
    var url = BasePath+'/bill_im_check/save_detail';
    fromObj.form('submit', {
		url: url,
		onSubmit: function(param){
			param.editor = billimcheck.user.loginName;
			param.editorName = billimcheck.user.username;
			param.edittm = billimcheck.user.currentDate19Str;
			param.locno = billimcheck.user.locno;
		    param.checkName = checkWorkerName;
			param.inserted = encodeURIComponent(JSON.stringify(inserted));
			param.updated = encodeURIComponent(JSON.stringify(updated));
			param.del = encodeURIComponent(JSON.stringify(del));
		},
		success: function(result){
			  wms_city_common.loading();
			//wms_city_common.beginEditAllLine("itemDetail");
			var resultDate = $.parseJSON(result);
			if(resultDate.result=="success"){
				alert('保存成功!');
				billimcheck.loadDataGrid();
				billimcheck.closeUI();
			}else{
				alert(resultDate.msg,1);
			}
			
			
			
//   				if(resultDate.result=="fail"){
//   					 alert(resultDate.msg,2);
//   				}else{
//   					if(resultDate.repeat!=null&&resultDate.repeat.length>0){
//   						var restr = "";
//   						for(var i=0;i<resultDate.repeat.length;i++){
//   							restr = restr+resultDate.repeat[i]+"<br>";
//   						}
//   						alert('保存失败,商品'+restr+"已经存在，请重新选择",2);
//   					}else{
//   						alert('保存成功!');
//						billimcheck.loadDataGrid();
//						billimcheck.closeUI();
//   					}
//   				}
			
	    },
		error:function(){
			wms_city_common.loading();
			alert('修改失败,请联系管理员!',2);
		}
   });
};

billimcheck.getRepeat = function(tempary){
var ary=[];
for(var i = 0;i<tempary.length;i++){
	if(tempary[i].boxNo==null||tempary[i].boxNo==""){
		ary.push(tempary[i].itemNo+"/"+tempary[i].sizeNo);
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
 if(count>1){
 	res.push(ary[i]); 
 }
 i+=count;   
};
return res;
};

billimcheck.deleteRows = function(){
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keys = [];
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!="10"){
			allOk = false;
			return false;
        }
		keys.push(item.locno+"-"+item.ownerNo+"-"+item.checkNo);
	});   
	if(!allOk){
		alert("非建单状态的数据不能删除",1);
		return;
    } 
     //2.绑定数据
     var url = BasePath+'/bill_im_check/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){    
		     wms_city_common.loading("show","正在删除......");
		   	 billimcheck.ajaxRequest(url,data,true,function(data){
		   	 wms_city_common.loading();
				 if(data.result=='success'){
					 //4.删除成功,清空表单
					 billimcheck.loadDataGrid();
					 alert('删除成功!');
				 }else{
					 alert(data.msg,1);
				 }
			}); 
		    }    
	});  


};

billimcheck.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
	 var item = $('#itemDetail').datagrid('getRows');
     if (item) {
         for (var i = item.length - 1; i >= 0; i--) {
             var index = $('#itemDetail').datagrid('getRowIndex', item[i]);
             $('#itemDetail').datagrid('deleteRow', index);
         }
     }
	
};
billimcheck.clearSearch = function (){
	$("#searchForm").form('clear');
	$('#searchBrandNo').combobox("loadData",[]);
	//billimcheck.searchData();
};
billimcheck.searchData = function(){
	var startCreatetm =  $('#startCreatetm').datebox('getValue');
	var endCreatetm =  $('#endCreatetm').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }   
	var startAudittm =  $('#startAudittm').datebox('getValue');
	var endAudittm =  $('#endAudittm').datebox('getValue');
	if(!isStartEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    } 
	
	var checkStartDate =  $('#checkStartDate').datebox('getValue');
	var checkEndDate =  $('#checkEndDate').datebox('getValue');
	if(!isStartEndDate(checkStartDate,checkEndDate)){    
		alert("验收日期开始日期不能大于结束日期");   
        return;   
    } 
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_im_check/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	$.extend(reqParam, {locno: billimcheck.user.locno});
	$('#dataGridJG').datagrid({
		url:queryMxURL,
		queryParams:reqParam
	});
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
billimcheck.addrow = function(){
	billimcheck.insertRowAtEnd("itemDetail");
};

billimcheck.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('selectRow',tempIndex);
	tempObj.datagrid('beginEdit', tempIndex);
	
};
billimcheck.openimreceiptWin = function(){
	$('#receiptSelect').window({
		title:"收货单选择列表"
	});
	
	$("#receiptSelectSearchForm").form("clear");
	$("#dataGridJGreceiptSelect").datagrid('loadData', { total: 0, rows: [] });
	$('#receiptSelect').window('open');
	
};
billimcheck.openItemSlect = function(obj){
	var receiptNo = $('#sImportNo').val();
	if(!receiptNo){
		alert("请选择收货单号！");
		return;
	}
	$('#openUIItem').window({
			title:"商品选择列表",
			onBeforeClose:function(){
				$(".tooltip").remove();
			}
		});
	$('#openUIItem').window('open');
};
billimcheck.selectItemOK = function (){
	
	var dataList = [];
	var receiptNo = $("#dataForm input[id=sImportNo]").val();
	var checkNo = $("#dataForm input[id=checkNo]").val();
	wms_city_common.loading('show',"正在加载明细.......");
	$.ajax({
		  type: 'POST',
		  url: BasePath+'/bill_im_receipt_dtl/listReceiptDtlBox',
		  data: {locno:billimcheck.user.locno,receiptNo:receiptNo,checkNo:checkNo},
		  cache: false,
		  async: false,
		  success:function(data){
			  for(var i = 0;i<data.length;i++){
				  dataList[i] = {"boxNo":data[i].boxNo};
			  }
			  wms_city_common.loading();
		  },error:function(){
			  alert("读取数据失败!");
			  wms_city_common.loading();
		  }
	});
	
	
	var rowData = $("#dataGridJGItem").datagrid('getChecked');
	if(rowData==null||rowData==""){
		alert("请选择商品",2);
		return;
	}
	var data;
	for(var i=0,length=rowData.length;i<length;i++){
		data = rowData[i];
		data.sourceType = 0;
		data.poQty = 0;
		data.boxNo = dataList[0].boxNo;
		var tempObj = $('#itemDetail');
		tempObj.datagrid('appendRow',data);
//		var tempIndex = tempObj.datagrid('getRows').length - 1;
//		tempObj.datagrid('beginEdit', tempIndex);
		
//		data.boxNo = dataList[0].boxNo;
		//var ed = tempObj.datagrid('getEditor', {index:tempIndex,field:'boxNo'});
		//$(ed.target).combobox('loadData',dataList);
//		$(ed.target).combobox({    
//		    data:dataList,    
//		    valueField:'boxNo',    
//		    textField:'boxNo',
//		    panelHeight:150,
//		    panelWidth:"auto",
//		    required:true
//		});  
     }
	/*
	if(rowData==null){
			rowData= $("#dataGridJGItem").datagrid("getSelected");
		}
		var target = $('#itemDetail');
		var row = target.datagrid('getSelected');
		var index = target.datagrid('getRowIndex', row);
		//下面代码是通过editor赋值
		var ed = target.datagrid('getEditor', {index:index,field:'itemNo'});
		if(ed){
			$(ed.target).val(rowData.itemNo);  
		}
		var ed = target.datagrid('getEditor', {index:index,field:'itemName'});
		if(ed){
			$(ed.target).val(rowData.itemName);  
		}
		var ed = target.datagrid('getEditor', {index:index,field:'styleNo'});
		if(ed){
			$(ed.target).val(rowData.styleNo);  
		}
		var ed = target.datagrid('getEditor', {index:index,field:'sizeNo'});
		if(ed){
			$(ed.target).val(rowData.sizeNo);  
		}
		var ed = target.datagrid('getEditor', {index:index,field:'poQty'});
		if(ed){
			$(ed.target).val(rowData.receiptQty);  
		}
		var ed = target.datagrid('getEditor', {index:index,field:'boxNo'});
		if(ed){
			$(ed.target).val(rowData.boxNo);  
		}
		var ed = target.datagrid('getEditor', {index:index,field:'packQty'});
		if(ed){
			$(ed.target).val(rowData.packQty);  
		}
		var ed = target.datagrid('getEditor', {index:index,field:'sourceType'});
		if(ed){
			$(ed.target).val(type);  
		}*/
	
//	target.datagrid('updateRow',{
//		index: index,
//		row: {
//			itemName: rowData.itemName,
//			styleNo:rowData.styleNo,
//			sizeNo:rowData.sizeNo,
//			poQty:rowData.receiptQty,
//			boxNo:rowData.boxNo
//		}
//	});
	$('#openUIItem').window('close');
};
billimcheck.selectReceiptOK = function (rowData){
	if(rowData==null){
		rowData= $("#dataGridJGreceiptSelect").datagrid("getSelected");
	}
	$('#sImportNo').val(rowData.receiptNo);
	$('#supplierNo').val(rowData.supplierNo);
	$('#ownerNo').combobox('setValue', rowData.ownerNo);
	//加载收货单明细到验收单明细中
//	billimcheck.loadReceiptDtl(rowData.receiptNo);
	$('#receiptSelect').window('close');
};

billimcheck.loadReceiptDtl = function(receiptNo){
	wms_city_common.loading('show',"正在加载明细.......");
	deleteAllGridCommon("itemDetail");
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_im_check/selectAllDetailByReciptNo',
        data: {
	        receiptNo:receiptNo,
	        locno:billimcheck.user.locno
        },
        success: function(returnData){
        	var tempObj = $('#itemDetail');
        	var data;
        	for(var i=0,length=returnData.list.length;i<length;i++){
        		data = returnData.list[i];
        		data.sourceType=1;
        		data.checkQty = data.poQty;
        		tempObj.datagrid('appendRow',data);
        		
				//var tempIndex = i;
				//tempObj.datagrid('beginEdit', tempIndex);
				//var ed = tempObj.datagrid('getEditor', {index:tempIndex,field:'checkQty'});
				//$(ed.target).val(data.poQty);
        	}
        	wms_city_common.loading();
		}
      });
};


//查询商品信息
billimcheck.searchItem = function(){
	var receiptNo = $('#sImportNo').val();
	var itemSearchFormArr = $('#itemSearchForm').serializeArray();
	$.merge(itemSearchFormArr,[{name: 'receiptNo', value: receiptNo}]);
	var fromObjStr=convertArray(itemSearchFormArr);
	var queryMxURL=BasePath+'/bill_im_check/findItemNotInReceipt';
	
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
    
};

//收货单信息
billimcheck.searchReceipt = function(){
	var fromObjStr=convertArray($('#receiptSelectSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_im_check/findNoCheckedReciptNo?status=20';
	
    //3.加载明细
    $( "#dataGridJGreceiptSelect").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGreceiptSelect").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGreceiptSelect").datagrid( 'load' );
    
};
billimcheck.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		tempObj.datagrid('endEdit', i);
    	}else{
    		return false;
    	}
    }
    return true;
};
$.extend($.fn.datagrid.defaults.editors, {    
    textbutton: {    
        init: function(container, options){
            var input = $('<input type="text" name="itemNo" style="width: 140px;">').appendTo(container);
            $('<input type="button" value="..." style="width: 30px;"  onclick="javascript:billimcheck.openItemSlect(this);">').appendTo(container);
            return input;    
        },    
        getValue: function(target){ 
            return $(target).val();    
        },    
        setValue: function(target, value){
        	$(target).val(value);  
        	//$(target[1]).val("...");  
        },    
        resize: function(target, width){    
        }    
    },
    readonlytext: {    
        init: function(container, options){
            var input = $('<input type="text" class="datagrid-editable-input" readOnly="readOnly">').appendTo(container);    
            return input;    
        },    
        getValue: function(target){ 
            return $(target).val();    
        },    
        setValue: function(target, value){
        	$(target).val(value);  
        },    
        resize: function(target, width){    
        }    
    }
}); 

billimcheck.itemFormatter = function(value, rowData, rowIndex){
	if(value) return value;
	if(rowData && rowData.item){
		return rowData.item[this.field];
	}
	return '';
};
//========================尺码横排======================================
billimcheck.showDetail = function(rowData){
	console.info(rowData);
	billimcheck.initGridHead(rowData.sysNo);
	billimcheck.loadDataDetailViewGrid(rowData);
};
billimcheck.exportDataGridDtl1Id = 'itemDetailShow';
billimcheck.preColNamesDtl1 = [
		                   {title:"商品编码",field:"itemNo",width:120},
		                   {title:"商品名称",field:"itemName",width:150},
		                   {title:"款号",field:"styleNo",width:80},
		                   {title:"颜色",field:"colorNoStr",width:80},
		                   {title:"品牌",field:"brandNoStr",width:100}
                    ];
billimcheck.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billimcheck.sizeTypeFiledName = 'sizeKind';

billimcheck.initGridHead = function(sysNo){
    var beforeColArr = billimcheck.preColNamesDtl1;
	 var afterColArr = billimcheck.endColNamesDtl1; 
	 console.info(sysNo);
	 var columnInfo = billimcheck.getColumnInfo(sysNo,beforeColArr,afterColArr);
	 console.info(columnInfo.columnArr);
     $("#"+billimcheck.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};
billimcheck.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billimcheck.sizeTypeFiledName
        },
        cache: true,
        async: false,
        success: function(returnData){
         	resultData.columnArr = returnData.returnCols;
         	resultData.startType = returnData.startType;
         	resultData.maxType = returnData.maxType;
		}
      });
      return resultData;
};
billimcheck.loadDataDetailViewGrid = function(rowData){
	$('#itemDetailShow').datagrid(
			{
				'url':BasePath+'/bill_im_check_dtl/queryBillImCheckDtlList?checkNo='+rowData.checkNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo,
				'pageNumber':1 
			}
	);
};

billimcheck.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//========================尺码横排======================================

$(document).ready(function(){
	//创建日期初始为前两天
	$("#startCreatetm").datebox('setValue',getDateStr(-2));
	 billimcheck.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){
			 billimcheck.user=u;
			 $("#itemSearchForm input[name=locno]").val(u.locno);
			 $("#receiptSelectSearchForm input[name=locno]").val(u.locno);
		 });
	 
	$('#storeTree').combotree({    
	    url: BasePath+'/store/queryStoreTree',    
	    required: true   
	});  
	//初始化onLoadSuccess
	$('#itemDetail').datagrid({
		
		'onLoadSuccess':function(){
	   			//wms_city_common.beginEditAllLine("itemDetail");
				//billimcheck.beginEditAllReceiptDtlLine("itemDetail");
	   		}
	});
	//加载品牌
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=searchBrandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	objs = [];
	objs.push(
			{"sysNoObj":$('#sysNo'),"brandNoObj":$('input[id=brandNo]',$('#itemSearchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CHECK_STATUS',
			{},
			billimcheck.status,
			null);
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNoCondition","ownerNo"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[true,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billimcheck.ownerNo,
			null);
	//初始化员工
	wms_city_common.comboboxLoadFilter(
			["creatorCondition","auditorCondition","checkWorkerCondition","checkWorkerForAdd","checkWorkerForShow"],
			'workerNo',
			'workerName',
			'valueAndText',
			false,
			[true,true,true,false,false],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
	
	//初始化验收单业务类型
	wms_city_common.comboboxLoadFilter(
			["businessType"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IM_CHECK_BUSINESS_TYPE',
			{},
			billimcheck.status,
			null);
	//初始化货主
	
	$('#itemDetailShow').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billimcheck.poQty = data.footer[1].poQty;
		   				billimcheck.checkQty = data.footer[1].checkQty;
		   				billimcheck.directQty = data.footer[1].directQty;
		   				billimcheck.difQty = data.footer[1].difQty;
		   				billimcheck.divideQty = data.footer[1].divideQty;
		   				
		   			}else{
		   				var rows = $('#itemDetailShow').datagrid('getFooterRows');
			   			rows[1]['poQty'] = billimcheck.poQty;
			   			rows[1]['checkQty'] = billimcheck.checkQty;
			   			rows[1]['directQty'] = billimcheck.directQty;
			   			rows[1]['difQty'] = billimcheck.difQty;
			   			rows[1]['divideQty'] = billimcheck.divideQty;
			   			$('#itemDetailShow').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
});

//箱号选择清空
billimcheck.searchLocClear = function(){
	$("#receiptSelectSearchForm").form("clear");
};
//关闭单号选择框
billimcheck.closeReciiptSelect=function(){
	$("#receiptSelect").window("close");
};

//商品选择
billimcheck.searchItemClear = function(){
	$("#itemSearchForm").form("clear");
	$('#brandNo').combobox("loadData",[]);
};
billimcheck.closeItemSelect = function(){
	$("#openUIItem").window("close");
};


billimcheck.checkWorker = function(rowData){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#checkWorkerForAdd').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150
			  });
		}
	});
};

billimcheck.difQty = function(value, rowData, rowIndex){
	return Math.abs(rowData.checkQty-rowData.poQty);
};

billimcheck.beginEditAllReceiptDtlLine = function(id){
	var curObj = $("#"+id);
	var rows = curObj.datagrid("getRows");
	for(var i=0,length = rows.length;i<length;i++){
		var ed = curObj.datagrid('getEditor', {index:i,field:'boxNo'});
		if(ed!=null){
			var originalBoxNo = rows[i].originalBoxNo;
			if(originalBoxNo!=""&&originalBoxNo!=null){
				$(ed.target).combobox('disable');
			}
		}
		//curObj.datagrid('beginEdit', i);
   	}
};

billimcheck.onClickRowReceiptDtl = function(rowIndex, rowData){
	var curObj = $("#itemDetail");
	curObj.datagrid('beginEdit', rowIndex);
	var ed = curObj.datagrid('getEditor', {index:rowIndex,field:'boxNo'});
	var originalBoxNo = rowData.originalBoxNo;
	if((originalBoxNo!=""&&originalBoxNo!=null)||rowData.sourceType==0){
		var receiptNo = $("#dataForm input[id=sImportNo]").val();
		var checkNo = $("#dataForm input[id=checkNo]").val();
		$(ed.target).combobox('reload',BasePath+'/bill_im_receipt_dtl/listReceiptDtlBox?locno='+billimcheck.user.locno+"&receiptNo="+receiptNo+"&checkNo="+checkNo);
	}else{
		$(ed.target).combobox('disable');
	}
};

billimcheck.checkDetailIsPass = function(listDatas){
	var tipStr = "";
	$.each(listDatas, function(index, item){
		
		if(item.boxNo=="" || item.boxNo==null){
			tipStr = "箱号："+item.boxNo + "商品："+item.itemNo + "尺码："+item.sizeNo+"请选择箱号";
			return;
		}
		
		if(item.checkQty == "" || item.checkQty == null){
			tipStr = "箱号："+item.boxNo + "商品："+item.itemNo + "尺码："+item.sizeNo+"验收数量不能为空!";
			return;
		}
		
		if(!/^[0-9]*$/.test(item.checkQty)){
			tipStr = "箱号："+item.boxNo + "商品："+item.itemNo + "尺码："+item.sizeNo+":验收数量不是数字类型;";
			return;
		}
		
		if(item.checkQty < 0){
			tipStr = "箱号："+item.boxNo + "商品："+item.itemNo + "尺码："+item.sizeNo+"验收数量不能为负数!";
			return;
		}
		
		if(item.checkQty == "0" && item.poQty == "0"){
			tipStr = "箱号："+item.boxNo + "商品："+item.itemNo + "尺码："+item.sizeNo+"串款验收数量必须大于0";
			return;
		}
		
	});
	return tipStr;
};

