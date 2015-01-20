/**
 * 出库订单（发货通知）
 */
var billomexp = {};

billomexp.locno;

billomexp.curRowIndex = -1;
billomexp.preType = 0;

billomexp.exportDataGridDtl1Id = 'moduleView';
billomexp.preColNamesDtl1 = [
						   {title:"客户名称",field:"storeName",width:170},
						   {title:"合同号",field:"poNo",width:100},
		                   {title:"商品编码",field:"itemNo",width:150},
		                   {title:"商品名称",field:"itemName",width:150},
		                   //{title:"款号",field:"styleNo",width:80},
		                   {title:"颜色",field:"colorNoStr",width:80},
		                   {title:"品牌",field:"brandNoStr",width:100}
                    ];
billomexp.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billomexp.sizeTypeFiledName = 'sizeKind';

//将数组封装成一个map
billomexp.converStr2JsonObj = function(data){
	var str = "";
	if(data != null) {
		for(var i = 0,length = data.length;i<length;i++){
			str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
			if(i<length-1){
				str = str+",";
			}
		}
	}
	return eval("({"+str+"})");
};

billomexp.statusData = [{    
    "label":"10",
    "text":"审核", 
    "value":"审核",   
},{    
    "label":"11",    
    "text":"建单", 
    "value":"建单"   
}];


//状态
billomexp.statusDataText = {
	"10":"审核",
	"11":"建单"
};

//状态
billomexp.columnStatusDataTextFormatter = function(value,rowData,rowIndex){
	return billomexp.statusDataText[value];
};

billomexp.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//加载查询
billomexp.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/billomexp/expList.json?locno='+billomexp.locno+'','title':'发货通知列表','pageNumber':1 });
};
//加载明细
billomexp.loadDataDetailGrid = function(rowData){
	$('#moduleEdit').datagrid(
			{
				'url':BasePath+'/billomexpdtl/dtllist.json?expNo='+rowData.expNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo,
//				'queryParams': {
//					expNo: rowData.expNo,
//					locno: rowData.locno,
//					ownerNo: rowData.ownerNo
//				},
				'pageNumber':1 
			}
	);
	//$("#moduleEdit").datagrid( 'load' );
};

//billomexp.loadDetail = function(locno){
//	$('#dataForm').form('load',BasePath+'/billomexp/get?locno='+locno);
//	$("#locno").attr('readOnly',true);
//};

billomexp.checkExistFun = function(url,checkColumnJsonData){
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
//查询出库订单
billomexp.searchBillOmExp = function(){
	var createTmStart =  $('#createtmBeginCondition').datebox('getValue');
	var createTmEnd =  $('#createtmEndCondition').datebox('getValue');
	if(!isStartEndDate(createTmStart,createTmEnd)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var outstockDateStart =  $('#expDateBeginCondition').datebox('getValue');
	var outstockDateEnd =  $('#expDateEndCondition').datebox('getValue');
	if(!isStartEndDate(outstockDateStart,outstockDateEnd)){    
		alert("出货日期开始日期不能大于结束日期");   
        return;   
    }
	
	//$('#searchForm input[name=locno]').val(billomexp.locno);
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/billomexp/expList.json';
	var param = eval("(" +fromObjStr+ ")");
	param.locno = billomexp.locno;
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= param;
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清楚查询条件
billomexp.searchBillOmExpClear = function(){
	$('#searchForm').form("clear");
	$('#brandNo').combobox("loadData",[]); 
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

//修改主表信息
billomexp.modifyBillOmExp = function(id){
    var fromObj=$('#'+id);
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
    //3. 保存
    wms_city_common.loading("show");
    var url = BasePath+'/billomexp/modifyBillOmExp';
    billomexp.ajaxRequest(url,fromObj.serialize(),function(data,returnMsg){
		 if(data.result=='success'){
			 alert('修改成功!');
			 $("#showEditDialog").window('close'); 
			 billomexp.loadDataGrid();
		 }else{
			 alert(data.msg,2);
		 }
		 wms_city_common.loading();
	}); 
    
    
//    fromObj.form('submit', {
//   		url: url,
//   		onSubmit: function(){
//			
//   		},
//   		success: function(data){
//   			alert(data.msg)
//   			wms_city_common.loading();
//   			if(data.result=="success"){
//   				alert('修改成功!');
//				$("#showEditDialog").window('close'); 
//				billomexp.loadDataGrid();
//				return;
//			}else{
//				alert(data.msg,2);
//			}
//   		},
//   		error:function(){
//   			wms_city_common.loading();
//   			alert('修改失败,请联系管理员!',2);
//   		}
//   	});
    
};

//弹出详情页面
billomexp.showEdit = function(rowData,flag){
	//清空datagrid数据
	$('#moduleEdit').datagrid('clearData');
	
	var fromObj=$('#dataEditForm');
	//仓库编码设置为只读，不可修改
	$('input[name=expNo]',fromObj).attr("readOnly",true);

	$('#detailEditDiv').show();
	
	if(flag == 1){
		$('#toolDivForUpdate,#zoneInfoToolEditDiv').show();
		//$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
		if(rowData.status!=null&&rowData.status!=""&&rowData.status!="10"){
			$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').hide();
			//$('#moduleEdit').datagrid({toolbar: '#'});
		}else{
			$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').show();
			//$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
		}
	}else{
		$('#toolDivForUpdate,#zoneInfoToolEditDiv').hide();
		//$('#moduleEdit').datagrid({toolbar: '#'});
	}
	
//	if(rowData.status!=null&&rowData.status!=""&&rowData.status!="11"){
//		$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').hide();
//		$('#moduleEdit').datagrid({toolbar: ''});
//	}else{
//		$('#btn-aduit-edit,#btn-add-edit,#zoneInfoToolEditDiv').show();
//		$('#moduleEdit').datagrid({toolbar: '#zoneInfoToolEditDiv'});
//	}
	
	//设置信息
	billomexp.setDetail(rowData,fromObj);
	//明细信息
	billomexp.loadDataDetailGrid(rowData);
	//弹窗
	$("#showEditDialog").window('open'); 
};

//弹出修改页面
billomexp.showModify = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length != 1){
		alert('请选择一条记录！',1);
		return;
	}else if(checkedRows[0].status!='10'){
		alert('非建单状态的数据不能修改！',1);
		return;
	}
	billomexp.showEdit(checkedRows[0],1);
};

//弹出新增页面
billomexp.showAdd=function(){
	var fromObj=$('#dataForm');
	
	billomexp.clearAll(fromObj);
	
	$('#expNo').val("系统自动生成");
	
	$("#detailDiv,#btn-modify").hide();
	$("#btn-add").show();
	
	$('input[id=ownerNo]',fromObj).combobox('enable');
	$('input[id=ownerNoHide]',fromObj).attr("disabled",true);
	$('input[id=expType]',fromObj).combobox('enable');
	$('input[id=expTypeHide]',fromObj).attr("disabled",true);
	$('input[id=sysNo]',fromObj).combobox('enable');
	$('input[id=sysNoHide]',fromObj).attr("disabled",true);
	$('input[id=businessType]',fromObj).combobox('enable');
	$("#classType").combobox("enable");
	
	//关闭window关闭提示的tip
	$('#showDialog').window({
		onBeforeClose:function(){
			$('.tooltip').remove();
		}
	});
	
	$('#showDialog').window('open');  
	 
};

//审核操作
auditImImport = function(){
	var fromObj=$('#dataEditForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
          return;
    }
    $.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){  
        if (r) {
        	var data={
        		"ownerNo":$("input[id=ownerNo]",fromObj).combobox('getValue'),
        		"locno":$("input[id=locno]",fromObj).combobox('getValue'),
        		"expNo":$("input[id=expNo]",fromObj).val()
        	};
		    url = BasePath+'/billomexp/auditImImport';
		    billomexp.ajaxRequest(url,data,function(result,returnMsg){
        		 if(result=='success'){
        			alert('审核成功!');
        			//关闭窗口
        		    $("#showEditDialog").window('close'); 
        			 //4.审核成功
        			 billomexp.loadDataGrid();
        		 }else{
        			 alert('审核失败,请联系管理员!',2);
        		 }
        	}); 
        }
	});     
};

//保存主表信息
billomexp.saveMain=function(){
	var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return;
     }
	 //3. 保存
     wms_city_common.loading("show");
     var url = BasePath+'/billomexp/addBillOmExp';
     fromObj.form('submit', {
		url: url,
		onSubmit: function(){
			
		},
		success: function(data){
			wms_city_common.loading();
			var dataObj = eval('(' + data + ')');	
			if(dataObj.returnMsg){
				alert('新增成功!');
				
			     var expNoVal = dataObj.expNo;
			     var locnoVal = dataObj.locno;
			     var ownerNoVal = $("input[id=ownerNo]",fromObj).combobox('getValue');
			     var expTypeVal = $("input[id=expType]",fromObj).combobox('getValue');
			     var checkDataNo={"expNo":expNoVal,"ownerNo":ownerNoVal,"locno":locnoVal,"expType":expTypeVal};
				 
				 //获取信息
				 $.ajax({
					 async : true,
					 cache : false,
					 type : 'POST',
					 dataType : "json",
					 data: checkDataNo,
					 url:BasePath+'/billomexp/get_biz',
					 success : function(data) {
						 billomexp.clearAll(fromObj);
						 billomexp.setDetail(data[0],fromObj);
						 $('input[id=expNo]',fromObj).attr('readOnly',true);
						 $("#detailDiv,#btn-modify").show();
						 //清空datagrid数据
						 $('#module').datagrid('clearData');
						 $("#btn-add").hide();
					}
				}); 
				 return;
			}else{
				alert('新增异常,请联系管理员!',2);
			}
		},
		error:function(){
			wms_city_common.loading();
			alert('新增失败,请联系管理员!',2);
		}
     });  
};

//获取明细信息
billomexp.setDetail = function(rowData,fromObj){
	$("input[id=expNo]",fromObj).val(rowData.expNo).focus();
	//$("input[id=expType]",fromObj).combobox("select",rowData.expType);
	$("input[id=importNo]",fromObj).val(rowData.importNo).focus();
	$("input[id=storeNo]",fromObj).val(rowData.storeNo).focus();
	$("#classType").combobox("select",rowData.classType);
	$("#classTypeUpdate").combobox("select",rowData.classType);
	$("#classTypeView").combobox("select",rowData.classType);
	
	$("input[id=sysNo]",fromObj).combobox("select",rowData.sysNo);
	$("input[id=sysNo]",fromObj).combobox('disable');
	$("input[id=sysNoHide]",fromObj).attr("disabled",false);
	$("input[id=sysNoHide]",fromObj).val(rowData.sysNo);
	
	$("input[id=expType]",fromObj).combobox("select",rowData.expType);
	$("input[id=expType]",fromObj).combobox('disable');
	$("input[id=expTypeHide]",fromObj).attr("disabled",false);
	$("input[id=expTypeHide]",fromObj).val(rowData.expType);
	$("input[id=ownerNo]",fromObj).combobox("select",rowData.ownerNo);
	$("input[id=ownerNo]",fromObj).combobox('disable');
	$("input[id=ownerNoHide]",fromObj).attr("disabled",false);
	$("input[id=ownerNoHide]",fromObj).val(rowData.ownerNo);
	//$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
	$("input[id=expDate]",fromObj).datebox("setValue",rowData.expDate);
	
	$("input[id=deliverType]",fromObj).combobox("select",rowData.deliverType);
	$("input[id=transportType]",fromObj).combobox("select",rowData.transportType);
	
	$("input[id=creator]",fromObj).val(rowData.creator);
	$("input[id=createtm]",fromObj).val(rowData.createtm);
	$("input[id=editor]",fromObj).val(rowData.editor);
	$("input[id=edittm]",fromObj).val(rowData.edittm);
	$("input[id=status]",fromObj).val(rowData.status);
	
	$("input[id=businessType]",fromObj).combobox("select",rowData.businessType);
	$("input[id=businessType]",fromObj).combobox('disable');
	//$("#classType").combobox("disable");
	//$("#classTypeUpdate").combobox("disable");
	$("#classTypeView").combobox("disable");
	
	$("input[id=expRemark]",fromObj).val(rowData.expRemark).focus();
	$("input[id=expRemark]",fromObj).attr("disabled",true);
};

//删除主表信息
billomexp.deleteBillOmExp=function(){
	//var rows = $("#dataGridJG").datagrid("getSelections");// 获取所有选中的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","您确定要删除这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var noStrs = [];
        	var delTip = "";
        	$.each(checkedRows, function(index, item){
        		noStrs.push(item.expNo+"#"+item.locno+"#"+item.ownerNo+"#"+item.expType);
        		if(item.status!=null&&item.status!=""&&item.status!="10"){
        			delTip = "单据:"+item.expNo+"；";
        		}
        	}); 
        	if(delTip!=null && delTip!=""){
        		delTip = delTip+"已处理，不能删除！";
        		alert(delTip,1);
        		return;
        	}
            //2.绑定数据
        	var data={
        	    "noStrs":noStrs.join(",")
        	};
        	 //4. 删除
        	wms_city_common.loading("show");
		    url = BasePath+'/billomexp/deleteBillOmExp';
		    billomexp.ajaxRequest(url,data,function(data,returnMsg){
		    	wms_city_common.loading(); 
		    	if(data.result=='success'){
        			 //4.删除成功,清空表单
        			 billomexp.loadDataGrid();
        			 alert('删除成功!');
        		 }else{
        			 alert(data.msg,2);
        		 }
        	});
        }
	});     
};
//关单
billomexp.closeBillOmExp=function(id){
	$.messager.confirm("确认","您确定要将这条数据进行关单吗？", function (r){
		if (r) {
			var url = BasePath+'/billomexp/closeBillOmExp';
			var formObj = $('#'+id);
			var locno = billomexp.locno;
			var expNo = $('input[id=expNo]',formObj).val();
			var expType = $('input[id=expTypeHide]',formObj).val();
			var expStatus = $('input[id=status]',formObj).val();
			var data = {};
			if(expStatus=='90' || expStatus=='91'){
				alert("此单已完结,不能关单");
				return;
			}else if(expStatus == '10'){
				
			}else{
				var outStockDirectUrl = BasePath+'/outstockdirect/list.json';
				var outStockDtlUrl = BasePath+'/bill_om_outstock_dtl/list.json';
				var outStockUrl = BasePath+'/bill_om_outstock/list.json';
				var outStockDirect;
				var outStockDtl;
				var outStock;
				var outstockNo;
				data = {};
				data = {
						"locno":locno,
						"expNo":expNo
				};
				outStockDirect = billomexp.getObject(outStockDirectUrl, data);
				if(outStockDirect == null){
					alert('系统异常,请联系管理员!',2);
					return;
				}else if(outStockDirect.total > 0){
					for(var index=0;index<outStockDirect.total;index++){
						if(outStockDirect.rows[index].status != '13'){
							alert('拣货任务还没完成,不能关单!',2);
							return;//如果能查询到记录且状态全都不是13
						}
					}
					outStockDtl = billomexp.getObject(outStockDtlUrl, data);
					if(outStockDtl == null){
						alert('系统异常,请联系管理员!',2);
						return;
					}else if(outStockDtl.total == 0){
						alert('根据拣货任务未找到拣货单,不能关单!',2);
						return;//如果拣货任务为13且拣货单明细不存在记录
					}else{
						outstockNo = outStockDtl.rows[0].outstockNo;
						data = {
								"locno":locno,
								"outstockNo":outstockNo
						};
						outStock = billomexp.getObject(outStockUrl, data);
						if(outStock == null){
							alert('系统异常,请联系管理员!',2);
							return;
						}else if(outStock.total > 0){
							if(outStock.rows[0].status != '13'){
								alert('拣货单未完成,不能关单!',2);
								return;
							}
						}
					}
				}else{//如果拣货任务不存在记录
					//校验分货单 S***************************************************
					var divideDtl;
					var divide;
					var divideDtlUrl = BasePath+'/bill_om_divide_dtl/list.json';
					var divideUrl = BasePath+'/bill_om_divide/list.json';
					var divideNo;
					data = {};
					data = {
							"locno":locno,
							"expNo":expNo
					};
					divideDtl = billomexp.getObject(divideDtlUrl, data);
					if(divideDtl == null){
						alert('系统异常,请联系管理员!',2);
						return;
					}else if(divideDtl.total > 0){
						divideNo = divideDtl.rows[0].divideNo;
						data = {};
						data = {
								"locno":locno,
								"divideNo":divideNo
						};
						divide = billomexp.getObject(divideUrl, data);
						if(divide == null){
							alert('系统异常,请联系管理员!',2);
							return;
						}else if(divide.total > 0){
							if(divide.rows[0].status != '12'){
								alert('分货单未完成，不能关单!',2);
								return;
							}
						}
					}
					//校验分货单 E***************************************************
					//校验分货复核单 S***************************************************
					var recheckDtl;
					var recheck;
					var recheckDtlUrl = BasePath+'/bill_om_recheck_dtl/list.json';
					var recheckUrl = BasePath+'/bill_om_recheck/list.json';
					var recheckNo;
					data = {};
					data = {
							"locno":locno,
							"expNo":expNo
					};
					recheckDtl = billomexp.getObject(recheckDtlUrl, data);
					if(recheckDtl == null){
						alert('系统异常,请联系管理员!',2);
						return;
					}else if(recheckDtl.total > 0){
						recheckNo = recheckDtl.rows[0].recheckNo;
						data = {};
						data = {
								"locno":locno,
								"recheckNo":recheckNo
						};
						recheck = billomexp.getObject(recheckUrl, data);
						if(recheck == null){
							alert('系统异常,请联系管理员!',2);
							return;
						}else if(recheck.total > 0){
							if(recheck.rows[0].status != '13'){
								alert('分货复核单未完成,不能关单!',2);
								return;
							}
						}
					}
					//校验分货复核单 E***************************************************
					//校验装车单 S***************************************************
					var deliverDtl;
					var deliver;
					var deliverDtlUrl = BasePath+'/bill_om_deliver_dtl/list.json';
					var deliverUrl = BasePath+'/bill_om_deliver/list.json';
					var deliverNo;
					data = {};
					data = {
							"locno":locno,
							"expNo":expNo
					};
					deliverDtl = billomexp.getObject(deliverDtlUrl, data);
					if(deliverDtl == null){
						alert('系统异常,请联系管理员!',2);
						return;
					}else if(deliverDtl.total > 0){
						deliverNo = deliverDtl.rows[0].deliverNo;
						data = {};
						data = {
								"locno":locno,
								"deliverNo":deliverNo
						};
						deliver = billomexp.getObject(deliverUrl, data);
						if(deliver == null){
							alert('系统异常,请联系管理员!',2);
							return;
						}else if(deliver.total > 0){
							if(deliver.rows[0].status != '13'){
								alert('装车单未完成,不能关单!',2);
								return;
							}
						}
					}
					//校验装车单 E***************************************************
				}
			}
			//alert(locno+"#"+expNo+"#"+expType+"#"+expStatus);
			data = {
					"locno":locno,
					"expNo":expNo,
					"expType":expType,
					"status":"91"
			};
			wms_city_common.loading("show");
			billomexp.ajaxRequest(url,data,function(data,returnMsg){
				wms_city_common.loading();
				if(data.result=='success'){
					alert('关单成功!');
					//关闭窗口
					$("#showEditDialog").window('close'); 
					//关单成功
					billomexp.loadDataGrid();
				}else{
					alert(data.msg,2);
				}
			});
		}
	});
};
billomexp.getObject = function(url,data){
	var res = null;
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: data,
		  cache: true,
		  async:false, // 一定要
		  success: function(result){
			  res = result;
		  }
   });
	return res;
};
//加载品牌库
billomexp.loadSysNo = function(formObj){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			$('input[id=sysNo]',formObj).combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemname",
			    panelHeight:150
			});
			$('#sysNoSearch').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
			if(null!=data){
				$('input[id=sysNo]',formObj).combobox("select",data[0].itemvalue); 
			}
		}
	});
};

//加载委托业主信息
billomexp.loadOwner = function(formObj){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/entrust_owner/get_biz',
		success : function(data) {
			
			wms_city_common.init_Owner_Text= wms_city_common.converOwnerJsonObj(data);
			
			$('input[id=ownerNo]',formObj).combobox({
			    data:data,
			    valueField:'ownerNo',    
			    textField:'ownerName',
			    panelHeight:150,
			    loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.ownerNameForSearch = tempData.ownerNo+'→'+tempData.ownerName;
			       		 }
			       	 }
			    	return data;
			    }
			});
			if(null!=data){
				$('input[id=ownerNo]',formObj).combobox("select",data[0].ownerNo); 
			}
		}
	});
};

//加载仓库信息
billomexp.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billomexp.locno = data.locno;
		}
	});
};

//清理form
billomexp.clearAll = function(fromObj){
	fromObj.form("clear");
	//$("input[id=expNo]",fromObj).attr('readOnly',false);
};

//清理form和不为空的提示
billomexp.clearFormAndTip = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};

//新增和修改页面的取消按钮
billomexp.closeShowWin = function(id){
	$('#'+id).window('close');
	//$('#locno,#locname').validatebox('reset');
	//$('#showDialog').window('destroy');
};

billomexp.initStatus = function(){
	/*$('#status').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OEM_STATUS',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	  });*/
	
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OEM_STATUS',
			{},
			billomexp.status,
			null);
};

//========================业务类型BEGIN========================
billomexp.businessTypes = {};
billomexp.businessTypesFormatter = function(value, rowData, rowIndex){
	return billomexp.businessTypes[value];
};
//初始化状态
billomexp.businessTypeData = function(formObj){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OMEXP_BUSINESS_TYPE',
		success : function(data) {
			$('input[id=businessType]',formObj).combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
			billomexp.businessTypes = billomexp.businessTypeJsonObj(data);
		}
	});
	wms_city_common.comboboxLoadFilter(
			["businessTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OMEXP_BUSINESS_TYPE',
			{},
			null,
			null);
};
billomexp.businessTypeJsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//========================业务类型END========================

//格式化出货通知单类型
billomexp.columninit_exp_typeFormatter = function(value, rowData, rowIndex){
	return billomexp.init_exp_typeText[value];
};

//出货通知单类型
billomexp.init_exp_type = function(){
	
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=OEM_EXP_TYPE',
		success : function(data) {
			billomexp.init_exp_typeText=billomexp.converStr2JsonObj(data);
			$('#expType,#expTypeCondition').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"auto",
			  });
			
		}
	});

};

//格式化运输方式
billomexp.columnTRANSPORT_TYPEFormatter = function(value, rowData, rowIndex){
	return billomexp.init_TRANSPORT_TYPEText[value];
};

//运输方式
billomexp.init_TRANSPORT_TYPE = function(){
	
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=TRANSPORT_TYPE',
		success : function(data) {
			billomexp.init_TRANSPORT_TYPEText=billomexp.converStr2JsonObj(data);
			$('#transportType,#transportTypeCondition').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"auto",
			  });
			
		}
	});

};

//格式化配送方式
billomexp.columnDELIVER_TYPEFormatter = function(value, rowData, rowIndex){
	return billomexp.init_DELIVER_TYPEText[value];
};

//配送方式
billomexp.init_DELIVER_TYPE = function(){
	
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=DELIVER_TYPE',
		success : function(data) {
			billomexp.init_DELIVER_TYPEText=billomexp.converStr2JsonObj(data);
			$('#deliverType,#deliverTypeCondition').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"auto",
			  });
			
		}
	});
	
//	$('#deliverType,#deliverTypeCondition').combobox({
//		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=DELIVER_TYPE',
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     panelHeight:"150"
//	  });
	
//	$('#expTypeCondition').combobox({
//		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=DELIVER_TYPE',
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     panelHeight:"150"
//	});
};

//清空商品查询的form
billomexp.searchItemClear = function(){
	$('#itemNo,#tBarcode,#itemName').val('');
};

//弹出选择商品的页面
billomexp.addItemDetail_module = function(gid){
	
	$("#dataGridJGItem").datagrid('clearData');
	
	billomexp.searchItemClear(); 
	
	$('#showGirdName').val(gid);
	
	var fromObj=$('#dataForm');
	if(gid == "moduleEdit"){
		fromObj=$('#dataEditForm');
	}
	$('#sysNoForItem').val($('input[id=sysNoHide]',fromObj).val());
	
	$('#openUIItem').show().window('open');
};

//查询商品信息
billomexp.searchItem = function(){
	
	var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/item/findItemAndSize';
	
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
    
};

//确定选择的商品
billomexp.confirmItem = function(){
	var checkedRows = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的商品！',1);
		return;
	}
	
//    var noStrs = [];
//    var delTip = "";
    
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条商品吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show");
        	$.each(checkedRows, function(index, item){
        	    var  rowData = {
        	    		"itemNo":item.itemNo,
        	    		"itemName":item.itemName,
        	    		"tBarcode":item.tBarcode,
        	    		"styleNo":item.styleNo,
        	    		"colorNoStr":item.colorNoStr,
        	    		"sizeNo":item.sizeNo,
        	    		"brandNo":item.brandNo,
        	    		"brandNoStr":item.brandNoStr,
        	    		"tt":'tt'
        	    };
        		//把选择的商品编码行记录插入到父窗体中
        	    billomexp.insertRowAtEnd($('#showGirdName').val(),rowData);
        	    
        	    $('#openUIItem').window('close').hide();
        	
//            	noStrs.push(item.recedeNo+"#"+item.locno+"#"+item.ownerNo+"#"+item.expType);
//            	if(item.status!=null&&item.status!=""&&item.status!="11"){
//            			delTip = "单据:"+item.recedeNo+"；";
//            	}
            }); 
        	wms_city_common.loading();
        }
	});
    

};


//新插入一行明细
billomexp.insertRow_module = function(gid){
//	var fromObj=$('#dataForm');
//	if(gid == 'moduleEdit'){
//		fromObj=$('#dataEditForm');
//	}
	
	billomexp.insertRowAtEnd(gid);
	
//	var ed = $('#'+gid).datagrid('getEditor', {
//        index : tempIndex,
//        field : 'boxNo'
//	});  
//	$.ajax({
//		url: BasePath+'/con_box/get_biz',
//		async : true,
//		cache : false,
//		type : 'POST',
//		dataType : "json",
//		data: {"ownerNo":$("input[id=ownerNo]",fromObj).combobox('getValue'),
//			   "locno":$("input[id=locno]",fromObj).combobox('getValue'),
//			   "expNoIsNull":"1"
//		},
//		success: function(jqData){
//			$(ed.target).combobox('loadData',jqData); 
//		}
//	});
	
};

billomexp.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	if(gid == 'moduleEdit'){
		$("#moduleEdit").datagrid('addEditor', {
            field : 'storeName',
            editor : {
                type : 'validatebox',
                options : {
                    required : true
                }
            }
       });
		$("#moduleEdit").datagrid('addEditor', {
            field : 'poNo',
            editor : {
                type : 'validatebox',
                options : {
                    required : true
                }
            }
       });
	}
	tempObj.datagrid('beginEdit', tempIndex);
	return  tempIndex;
};

billomexp.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};

billomexp.endEdit = function(gid){
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
//获取客户信息
billomexp.getStore = function(storeName){
	var store = null;
	var data = {"storeName":storeName,"storeType":'11',"status":'0'};
	var url = BasePath+'/store/listByName.josn';
 	$.ajax({
		  type: 'POST',
		  url: url,
		  data: data,
		  cache: true,
		  async:false, // 一定要
		  success: function(result){
			  store = result;
		  }
     });
 	return store;
};
//验证客户名称是否存在
billomexp.verifyStore = function(inserted,updated){
	var insertLen = inserted.length;
	var updatedLen = updated.length;
	var temp = null;
	var store = null;
	var storeName = null;
	if(insertLen > 0){
		for(var index=0;index<insertLen;index++){
			temp = inserted[index];
			storeName = temp['storeName'];
			store = billomexp.getStore(storeName);
			if(store){
				if(store.total == 0){
					alert('此客户名【'+storeName+'】没有记录,请输入精确的客户名!',1);
					return false;
				}else if(store.total == 1){
					temp['storeNo'] = store.rows[0]['storeNo'];
				}else{
					alert('此客户名【'+storeName+'】存在多条记录,请输入精确的客户名!',1);
	            	return false;
				}
			}else{
				alert('查询客户【'+storeName+'】失败!',1);
				return false;
			}
		}
	}
	if(updatedLen > 0){
		for(var index=0;index<updatedLen;index++){
			temp = updated[index];
			storeName = temp['storeName'];
			store = billomexp.getStore(storeName);
			if(store){
				if(store.total == 0){
					alert('此客户名【'+storeName+'】没有记录,请输入精确的客户名!',1);
					return false;
				}else if(store.total == 1){
					temp['storeNo'] = store.rows[0]['storeNo'];
				}else{
					alert('此客户名【'+storeName+'】存在多条记录,请输入精确的客户名!',1);
	            	return false;
				}
			}else{
				alert('查询客户【'+storeName+'】失败!',1);
				return false;
			}
		}
	}
	return true;
};
//保存明细
billomexp.save = function(id){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = billomexp.endEdit(id);
	if(!tempFlag){
		alert('数据验证没有通过！',1);
		return;
	}
	var fromObj=$('#dataForm');
	var showWinObj = 'showDialog';
	if(id == 'moduleEdit'){
		fromObj=$('#dataEditForm');
		showWinObj = 'showEditDialog';
	}
	var tempObj = $('#'+id);
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动！',1);
		return;
	}
	var inserted = tempObj.datagrid('getChanges', "inserted");
    var deleted = tempObj.datagrid('getChanges', "deleted");
    var updated = tempObj.datagrid('getChanges', "updated");
    if(!billomexp.verifyStore(inserted,updated)){
    	return;
    }
    var checkUrl=BasePath+'/billomexpdtl/get_count.json';
    var checkData={
    		"expNo" : $('input[id=expNo]',fromObj).val(),
    		"locno" : billomexp.locno,
    		"ownerNo" : $('input[id=ownerNoHide]',fromObj).val()
    };
    var hashObject = {};
    if(inserted.length>0){
        for (var i = 0; i < inserted.length; i++) {
        	var store_po_item_size = inserted[i]['storeNo']+"_"+inserted[i]['poNo']+"_"+inserted[i]['itemNo']+"_"+inserted[i]['sizeNo'];
        	if(hashObject[store_po_item_size]){
        		alert('客户_合同_商品编码_尺码：【'+store_po_item_size+'】重复！',1);
        		return;
        	}else{
        		hashObject[store_po_item_size] = true;
        	}
        	 //后台ajax校验查询
        	checkData.storeNo = inserted[i]['storeNo'];
        	checkData.poNo = inserted[i]['poNo'];
        	checkData.itemNo = inserted[i]['itemNo'];
        	checkData.sizeNo = inserted[i]['sizeNo'];
            if(billomexp.checkExistFun(checkUrl,checkData)){
        		alert('客户_合同_商品编码_尺码：【'+store_po_item_size+'】已存在！',1);
        		return;
    	    }
        }
    }
    if(updated.length>0){
        for (var i = 0; i < updated.length; i++) {
        	var store_po_item_size = updated[i]['storeNo']+"_"+updated[i]['poNo']+"_"+updated[i]['itemNo']+"_"+updated[i]['sizeNo'];
        	if(hashObject[store_po_item_size]){
        		alert('客户_合同_商品编码_尺码：【'+store_po_item_size+'】重复！',1);
        		return;
        	}else{
        		hashObject[store_po_item_size] = true;
        	}
        }
    }
    var effectRow = {
    		inserted:JSON.stringify(inserted),
    		deleted:JSON.stringify(deleted),
    		updated:JSON.stringify(updated),
    		"ownerNo":$("input[id=ownerNo]",fromObj).combobox('getValue'),
			"locno":billomexp.locno,
			"expNo":$("input[id=expNo]",fromObj).val(),
			"expDate":$("input[id=expDate]",fromObj).datebox("getValue")
    };
    wms_city_common.loading("show");
    $.post(BasePath+'/billomexpdtl/saveOmExpDtl', effectRow, function(data) {
    	wms_city_common.loading();
    	if(data.result == 'success'){
            alert('保存成功!',1);
            tempObj.datagrid('acceptChanges');
//            billomexp.clearAll();
            $('#'+showWinObj).window('close');  
            billomexp.loadDataGrid();
        }else{
        	alert(data.msg,1);
        	return;
        }
    }, "JSON").error(function() {
    	wms_city_common.loading();
    	alert('保存失败!',1);
    });
};

/**
 * 数组是否重复，如重复则返回重复元素 
 * @return {}
 */
billomexp.isDouble = function() {
    var hashObject = {};
    for (var i = 0; i < this.length; i++) {
        if (hashObject[this[i]]) {
            return true;
        } else {
            hashObject[this[i]] = true;
        }
    }
    return false;
};
billomexp.initFastFlag = function(){
	$('#fastFlagCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OEM_FAST_FLAG',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });
};
billomexp.initSourceType = function(){
	wms_city_common.comboboxLoadFilter(
			["sourceTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OM_EXP_SOURCE_TYPE',
			{},
			null,
			null);
};

billomexp.initSplitStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["splitStatusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OM_EXP_SPLIT_STATUS',
			{},
			null,
			null);
};

billomexp.getDate = function(n){
	var nowDate = new Date();
	var newDate = new Date();
	var newTimes = nowDate.getTime() + (n*24*3600000);
	newDate.setTime(newTimes);
	var y = newDate.getFullYear();
	var m = newDate.getMonth()+1;
	var d = newDate.getDate();
	var dateStr = y+"-";
	dateStr += (m<10?("0"+m):(m))+"-";
	dateStr += (d<10?("0"+d):(d));
	return dateStr;
};
billomexp.initCreatetmBegin = function(n){
	var newDate = billomexp.getDate(n);
	//var endDate = billomexp.getDate(0);
	$('#createtmBeginCondition').datebox('setValue', newDate);
	//$('#createtmEndCondition').datebox('setValue', endDate);
};


billomexp.onLoadSuccess = function(data){
	if(data.footer[1] != null){
		billomexp.mainSumFoot = data.footer[1];
	}else{
			var rows = $('#dataGridJG').datagrid('getFooterRows');
   			rows[1] = billomexp.mainSumFoot;
			$('#dataGridJG').datagrid('reloadFooter');
	}
};

$(document).ready(function(){
	
	$('#showDialog,#showEditDialog,#showViewDialog').show();
	
	billomexp.loadLoc();
	billomexp.loadOwner();
	//billomexp.loadSysNo();//加载品牌库
	billomexp.initSourceType();//初始化来源类型
	billomexp.initSplitStatus();//初始化拆分状态
	billomexp.init_exp_type();//初始化出货通知单类型
	billomexp.init_TRANSPORT_TYPE();//运输方式
	billomexp.init_DELIVER_TYPE();//配送方式
	billomexp.initStatus();//初始化状态列表数据
	billomexp.businessTypeData();
//	billomexp.init_IMPORT_STATUS();//初始化状态
//	billomexp.initBrand();
	billomexp.initFastFlag();
	billomexp.initCreatetmBegin(-2);
	
	wms_city_common.comboboxLoadFilter(
			["classType","classTypeUpdate","classTypeView","classTypeCondition"],
			null,
			null,
			null,
			true,
			[false,false,false,true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=EXP_CLASS',
			{},
			null,
			null);
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNo]',$('#dataForm'))},//新增
			{"sysNoObj":$('input[id=sysNo]',$('#dataEditForm'))},//修改
			{"sysNoObj":$('input[id=sysNo]',$('#dataViewForm'))},//详情
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	//总计
	$('#moduleView_dtl').datagrid({
		'onLoadSuccess' : function(data) {
			if (data.footer[1].isselectsum) {
				billomexp.itemQty = data.footer[1].itemQty;
				billomexp.scheduleQty = data.footer[1].scheduleQty;
				billomexp.locateQty = data.footer[1].locateQty;
				billomexp.realQty = data.footer[1].realQty;
				billomexp.deliverQty = data.footer[1].deliverQty;
			} else {
				var rows = $('#moduleView_dtl').datagrid('getFooterRows');
				rows[1]['itemQty'] = billomexp.itemQty;
				rows[1]['scheduleQty'] = billomexp.scheduleQty;
				rows[1]['locateQty'] = billomexp.locateQty;
				rows[1]['realQty'] = billomexp.realQty;
				rows[1]['deliverQty'] = billomexp.deliverQty;
				$('#moduleView_dtl').datagrid('reloadFooter');
			}
		}
	});
	$('#moduleView').datagrid({
		'onLoadSuccess' : function(data) {
			if (data.footer != null && data.footer[1] != null){
				billomexp.sumFooterMap = data.footer[1];
			}else if(data.footer != null){
				data.footer[1] = billomexp.sumFooterMap;
				$('#moduleView').datagrid('reloadFooter');
			}
		}
	});
	
	
	//$('#classTypeCondition').combobox('select','0');
//	billomexp.loadSysNo();
	//不加会致使页面双击window标题错位
	$('#showDialog,#showEditDialog,#showViewDialog,#openUIItem').window({    
	    resizable:false,
	    draggable:false,
	    collapsible:false,
	    closed:true,
	    maximized:true,
	    minimizable:false,
	    maximizable:false,
	    modal:true   
	});
});
billomexp.sumFooterMap = {};
//导出
exportExcel=function(){
	billomexp.exportExcelBill('moduleView',billomexp.sysNo,billomexp.preColNamesDtl1,billomexp.endColNamesDtl1,billomexp.sizeTypeFiledName,'发货通知单导出');
};

billomexp.exportExcelBill = function(dataGridId, sysNo, preColNames, endColNames,
		sizeTypeFiledName, excelTitle) {

	var url = BasePath + '/billomexpdtl/do_export4Horizontal?expNo='+$('input[name=expNo]',$('#dataViewForm')).val()+"&locno="+billomexp.locno;

	var $dg = $("#" + dataGridId + "");

	var tempTotal = -1;
	var paginationObj = $dg.datagrid('getPager').data("pagination");
	if (paginationObj) {
		tempTotal = paginationObj.options.total;
		if (tempTotal > 10000) {
			alert('导出文件数据超出限制最大数量10000!', 1);
			return;
		}
	}

	$("#exportExcelForm").remove();

	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
	;

	var fromObj = $('#exportExcelForm');

	var dataRow = $dg.datagrid('getRows');

	if (dataRow.length > 0) {
		if (tempTotal > dataRow.length) {
			var dataGridUrl = $dg.datagrid('options').url;
			var dataGridQueryParams = $dg.datagrid('options').queryParams;
			dataGridQueryParams.page = 1;
			dataGridQueryParams.rows = tempTotal;
			$.ajax({
				type : 'POST',
				url : dataGridUrl,
				data : dataGridQueryParams,
				cache : true,
				async : false, // 一定要
				success : function(resultData) {
					dataRow = resultData.rows;
				}
			});
		}
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.sysNo = sysNo;
				param.preColNames = JSON.stringify(preColNames);
				param.endColNames = JSON.stringify(endColNames);
				param.sizeTypeFiledName = sizeTypeFiledName;
				param.fileName = excelTitle;
				param.dataRow = JSON.stringify(dataRow);

			},
			success : function() {

			}
		});
	} else {
		alert('数据为空，不能导出!', 1);
	}

}

/**
 * ===================================尺码横排======================================================
 */

//弹出详情页面
billomexp.showView = function(rowData,rowIndex){
	$('#statusView').val(rowData.status);
	$('#showViewDialog').panel('resize',{
			 width:document.body.clientWidth-15,
			 height:document.body.clientHeight-15,
	});
	
	
	//清空datagrid数据
	$('#moduleView').datagrid('clearData');
	
	var fromObj=$('#dataViewForm');
	//仓库编码设置为只读，不可修改
	$('input[name=expNo]',fromObj).attr("readOnly",true);

	$('#detailViewDiv').show();
	
	//设置信息
	billomexp.setDetail(rowData,fromObj);
	//明细信息
	billomexp.loadDetail(rowData,rowIndex);

	
	var queryMxURL = BasePath+'/billomexpdtl/dtllist.json?locno='+billomexp.locno+'&expNo='+$('input[name=expNo]',fromObj).val();
	 $( "#moduleView_dtl").datagrid( 'options' ).url=queryMxURL;
	 $( "#moduleView_dtl").datagrid( 'load' );
	    
	//弹窗
	$("#showViewDialog").window('open');
};


billomexp.loadDetail = function(rowData,rowIndex){
	
	billomexp.curRowIndex=rowIndex;
	
//	billomexp.initGridHead(rowData.sysNo);
//	
//	billomexp.loadDataDetailViewGrid(rowData);
  
	billomexp.initGridHeadAndData(rowData);
	billomexp.sysNo = rowData.sysNo;
};


//同时加载表头和表体
billomexp.initGridHeadAndData = function(rowData){
	
	 var beforeColArr = billomexp.preColNamesDtl1;
	 var afterColArr = billomexp.endColNamesDtl1; 
	 var columnInfo = billomexp.getColumnInfo(rowData.sysNo,beforeColArr,afterColArr);
	 
	 var url = BasePath+'/billomexpdtl/queryBillOmExpDtlDTOlList';
	 var queryParams = {expNo:rowData.expNo,sysNo:rowData.sysNo,ownerNo:rowData.ownerNo};
	 
	 $("#moduleView").datagrid({
		 queryParams:queryParams,
		 url:url,
         columns:columnInfo.columnArr
     }); 
};


billomexp.initGridHead = function(sysNo){
    var beforeColArr = billomexp.preColNamesDtl1;
	 var afterColArr = billomexp.endColNamesDtl1; 
	 var columnInfo = billomexp.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billomexp.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

billomexp.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billomexp.sizeTypeFiledName
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

//加载明细
billomexp.loadDataDetailViewGrid = function(rowData){
	$('#moduleView').datagrid(
			{
				'url':BasePath+'/billomexpdtl/queryBillOmExpDtlDTOlList?expNo='+rowData.expNo+'&sysNo='+rowData.sysNo+'&ownerNo='+rowData.ownerNo,
//				'queryParams': {
//					expNo: rowData.expNo,
//					locno: rowData.locno,
//					ownerNo: rowData.ownerNo
//				},
				'pageNumber':1 
			}
	);
	//$("#moduleEdit").datagrid( 'load' );
};
billomexp.controllEdit = function(rowData,rowIndex){
	var dg = $('#moduleEdit');
	//dg.datagrid('removeEditor',"storeName,poNo");
	/*dg.datagrid('addEditor', {
        field : 'storeName',
        editor : {
            type : 'validatebox',
            options : {
                required : true
            }
        }
   });
	dg.datagrid('addEditor', {
        field : 'poNo',
        editor : {
            type : 'validatebox',
            options : {
                required : true
            }
        }
   });*/
	//var row = $dg.datagrid('getSelected');
	row = rowData;
    if(row!=null&&row.tt!=undefined&&row.tt!=''){
    	dg.datagrid('addEditor', {
	            field : 'storeName',
	            editor : {
	                type : 'validatebox',
	                options : {
	                    required : true
	                }
	            }
	       });
    	dg.datagrid('addEditor', {
	            field : 'poNo',
	            editor : {
	                type : 'validatebox',
	                options : {
	                    required : true
	                }
	            }
	       });
    }else{
      dg.datagrid('removeEditor',"storeName,poNo");
    }
    dg.datagrid('selectRow', rowIndex);
    var tempIndex = dg.datagrid('getRows').length;
    for(var i=0;i<tempIndex;i++){
    	dg.datagrid('endEdit', i);
    }
  	dg.datagrid('beginEdit', rowIndex);
	
};

//手工关闭
billomexp.overExpBill = function(){
	
	var formObj = $('#dataViewForm');
	var locno = billomexp.locno;
	var expNo = $('input[id=expNo]',formObj).val();
	var expType = $('input[id=expType]',formObj).combobox('getValue');
	
	//确认是否删除
	$.messager.confirm('确认', '您确定要关闭'+expNo+'单据吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在处理......");
		var url = BasePath+'/billomexp/overExpBill';
		var params = {
				locno:locno,
				expNo:expNo,
				expType:expType
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('关闭成功!');
				$("#showViewDialog").window('close');
				$("#dataGridJG").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('关闭失败!', 1);
			wms_city_common.loading();
		});
	});
	
};


//转存储发货
billomexp.toClass0ForExp = function(){
	var formObj = $('#dataViewForm');
	var locno = billomexp.locno;
	var expNo = $('input[id=expNo]',formObj).val();
	var expType = $('input[id=expType]',formObj).combobox('getValue');
	var classType = $('input[id=classTypeView]',formObj).combobox('getValue');
	var stauts = $('#statusView').val();
	
	if(classType == "0"){
		alert("只能拆分分货类型为非<存储发货>的单据!");
		return;
	}
	if(stauts == "10"){
		alert("建单状态的单据不能拆分!");
		return;
	}
	
	//确认是否转换
	$.messager.confirm('确认', expNo+'您确定要转存储发货吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在处理......");
		var url = BasePath+'/billomexp/toClass0ForExp';
		var params = {
				locno:locno,
				expNo:expNo,
				expType:expType
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('拆分成功!');
				$("#showViewDialog").window('close');
				$("#dataGridJG").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('拆分失败!', 1);
			wms_city_common.loading();
		});
	});
	
};
//转分货发货
billomexp.toClass1ForExp = function(){
	var formObj = $('#dataViewForm');
	var locno = billomexp.locno;
	var expNo = $('input[id=expNo]',formObj).val();
	var expType = $('input[id=expType]',formObj).combobox('getValue');
	var classType = $('input[id=classTypeView]',formObj).combobox('getValue');
	var stauts = $('#statusView').val();
	
	if(classType != "0"){
		alert("只能拆分分货类型为【存储发货】的单据!");
		return;
	}
	if(!(stauts == "12"||stauts == "16" || stauts == "40" || stauts == "50")){
		alert("只有状态为【部分分配】、【部分调度】、【部分复核】、【部分装车】的单据允许做转分货发货!");
		return;
	}
	
	
	//确认是否转换
	$.messager.confirm('确认', expNo+'您确定要转分货发货吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在处理......");
		var url = BasePath+'/billomexp/toClass1ForExp';
		var params = {
				locno:locno,
				expNo:expNo,
				expType:expType
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('拆分成功!');
				$("#showViewDialog").window('close');
				$("#dataGridJG").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('拆分失败!', 1);
			wms_city_common.loading();
		});
	});
	
};