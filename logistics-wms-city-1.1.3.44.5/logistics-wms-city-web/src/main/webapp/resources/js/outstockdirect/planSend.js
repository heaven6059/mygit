
var outstockdirect = {};

//加载Grid数据Utils
outstockdirect.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//刷新数据列表
outstockdirect.reload = function(){
	$('#dataGridCellArea').datagrid('reload');
	deleteAllGridCommon('dataGridOutStock');
};

//单击储区查询移库发单信息
outstockdirect.clickCmDefarea = function(rowData){
	var queryMxURL=BasePath+'/outstockdirect/findOutstockDirectByPage.json';
	var cellType = $('input[name="cellType"]:checked').val();
	var reqParam = {locno:outstockdirect.locno,cellType:cellType,areaNo:rowData.wareAreaNo};
	outstockdirect.loadGridDataUtil('dataGridOutStock', queryMxURL, reqParam);
	$('#wareNoHidden').val(rowData.wareNo);
	$('#areaNoHidden').val(rowData.areaNo);
	
};

//单击单选按钮事件
outstockdirect.clickCellRadio = function(value){
	
	//deleteAllGridCommon('dataGridOutStock');
	//刷选下架指示表数据
//	var wareNo = $('#wareNoHidden').val();
//	var areaNo = $('#areaNoHidden').val();
//	var wareAreaNo = wareNo+areaNo;
//	if(wareAreaNo!=''&&wareAreaNo!=null){
//		var queryMxURL=BasePath+'/outstockdirect/findOutstockDirectByPage.json';
//		var reqParam = {locno:outstockdirect.locno,cellType:value,areaNo:wareAreaNo};
//		outstockdirect.loadGridDataUtil('dataGridOutStock', queryMxURL, reqParam);
//	}
	
	//刷选库区数据
	
	var queryMxURL2=BasePath+'/outstockdirect/findHmPlanCmDefareaByPage.json';
	var reqParam2 = {locno:outstockdirect.locno,cellType:value};
	outstockdirect.loadGridDataUtil('dataGridCellArea', queryMxURL2, reqParam2);
	deleteAllGridCommon('dataGridOutStock');
};

//发单
outstockdirect.sendPlanOutstockDirect = function(){
	
	//1.校验必填项
    var validateForm= $('#dataForm').form('validate');
    if(validateForm==false){
        return ;
    }
    
    //校验是否选择库区信息
    var selectDataGrid = $('#dataGridCellArea').datagrid('getSelected');
    if(selectDataGrid == null){
		alert("请选择库区信息");
		return ;
	}
    
    //是否存在下架指示数据
    var dataGridOutStock = $('#dataGridOutStock').datagrid('getChecked');
	if(dataGridOutStock.length < 1){
		alert("请选择发单明细进行发单!");
		return;
	}
    
	$.messager.confirm("确认","您确定要发单这"+dataGridOutStock.length+"条数据吗？", function (r){ 
		if (!r) {
			return;
		}
		
		//路径参数
	    var wareNo = $('#wareNoHidden').val();
	    var areaNo = $('#areaNoHidden').val();
	    var cellType = $('input[name="cellType"]:checked').val();
	    var outstockPeople = $('#loginuser').combobox('getValue');
	    var url = BasePath+'/outstockdirect/procOmPlanOutStockDirect';
	    var dataList = [];
	    $.each(dataGridOutStock, function(index, item){
			var params = {
					directSerial:item.directSerial
			};
			dataList[dataList.length] = params;
		});
	    
	    var params = {
	    		locno:outstockdirect.locno,
	    		wareNo:wareNo,
	    		areaNo:areaNo,
	    		outstockPeople:outstockPeople,
	    		creator:outstockdirect.loginName,
	    		cellType:cellType,
	    		datas:JSON.stringify(dataList)
	    };
	    wms_city_common.loading("show","正在发单......");
	    $.post(url, params, function(data) {
	    	wms_city_common.loading();
	    	if(data){
	    		alert('发单成功!');
				//清空所有的值
	    		outstockdirect.clearData();
			}else{
				alert(data);
			}
	    }, "JSON").error(function() {
	    	wms_city_common.loading();
	    	//alert('发单失败,请联系管理员!',2);
	    });
	});
	
   
    
};

//清空数据
outstockdirect.clearData = function(){
	$('#loginuser').combobox('clear');
	$('#roleid').combobox('clear');
	//$('#dataGridCellArea').datagrid('reload');
	$('#dataGridOutStock').datagrid('reload');
};

//初始化下架人员
outstockdirect.initLoginuser = function(){
	
	$('#roleid').combobox({
		url:BasePath+'/authority_organization/role.json',
	    valueField:'roleId',    
	    textField:'roleName',
	    panelHeight:150,
	    onSelect:function(data){
	    	
	    	var loginName = "";
	    	if(data.userList!=null){
	    		loginName = data.userList[0].loginName;
	    	}
	    	$('#loginuser').combobox({
		   		 data:data.userList,
		   	     valueField:"loginName",
		   	     textField:"valueAndText",
		   	     panelHeight:200,
		   	     loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.loginName+'→'+tempData.username;
			       		 }
	       	 		}
	    			return data;
		   	     }
		   	}).combobox("select",loginName);
	    }
	
	});
	
//	$('#roleidCon').combobox({
//		 url:BasePath+'/authority_organization/role.json',
//	     valueField:"roleId",
//	     textField:"roleName",
//	     panelHeight:"auto",
//	     onSelect:function(record){
//	    	 var uList=[];
//	    	 $.each(record.userList,function(i,o){
//	    		 o.roleName=record.roleName;
//	    		 uList.push(o);
//	    	 });
//	    	 $('#loginUserDataGrid').datagrid({data:uList});
//	     }
//	});
	
//	$('#loginuser').combobox({
//	    url:BasePath+'/system_user/get_biz',
//	    valueField:'loginName',    
//	    textField:'valueAndText',
//	    panelHeight:150,
//	    loadFilter:function(data){
//    			if(data){
//		       		 for(var i = 0;i<data.length;i++){
//		       			 var tempData = data[i];
//		       			 tempData.valueAndText = tempData.loginName+'→'+tempData.username;
//		       		 }
//       	 		}
//    			return data;
//	   	},
//	    onSelect:function(data){
//	    	outstockdirect.loadDataUserInfoByRoleId(data);
//	    }
//	});
	
};


//根据角色ID查询对应下的所有用户信息 
outstockdirect.loadDataUserInfoByRoleId = function(data){
	$('#roleid').combobox({
		data:data.listAuthorityRoles,
	    valueField:'roleId',    
	    textField:'valueAndText',
	    panelHeight:150,
	    loadFilter:function(data){
    			if(data){
		       		 for(var i = 0;i<data.length;i++){
		       			 var tempData = data[i];
		       			 tempData.valueAndText = tempData.roleId+'→'+tempData.roleName;
		       		 }
       	 		}
    			return data;
	   	},
	});
	var data = $('#roleid').combobox('getData');
	if(data[0]!=null){
		$("#roleid ").combobox('select',data[0].roleId);
	}else{
		$("#roleid ").combobox('clear');
	}
};


//初始化当前登录的用户 信息
outstockdirect.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		outstockdirect.userid = data.userid;
		outstockdirect.loginName = data.loginName;
		outstockdirect.currentDate = data.currentDate19Str;
		outstockdirect.locno = data.locno;
	});
};

$(document).ready(function(){
	outstockdirect.initCurrentUser();
	outstockdirect.initLoginuser();

	$('#dataGridOutStock').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
							outstockdirect.itemTotalQty = data.footer[1].itemTotalQty;
			   			}else{
			   				var rows = $('#dataGridOutStock').datagrid('getFooterRows');
				   			rows[1]['itemTotalQty'] = outstockdirect.itemTotalQty;
				   			$('#dataGridOutStock').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
});