var cmdefarea = {};
cmdefarea.locno;
cmdefarea.user;

$(document).ready(function(){
	cmdefarea.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){cmdefarea.user = u;cmdefarea.locno = u.locno;});
	
	wms_city_common.initItemTypeAndQuality("itemType","areaQualityform");
	//初始化库区类型
	wms_city_common.comboboxLoadFilter(
			["areaTypeCondition","areaType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_TYPE',
			{},
			cmdefarea.areaType,
			null);
	//初始化库区用途
	wms_city_common.comboboxLoadFilter(
			["areaUsetypeCondition","areaUsetype"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREAUSE_TYPE',
			{},
			cmdefarea.areaUsetype,
			null);
	//初始化库区属性
	wms_city_common.comboboxLoadFilter(
			["areaAttributeCondition","areaAttribute"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_ATTRIBUTE',
			{},
			cmdefarea.areaAttribute,
			null);
	//初始化属性类型
	wms_city_common.comboboxLoadFilter(
			["attributeTypeCondition","attributeType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ATTRIBUTE_TYPE',
			{},
			cmdefarea.attributeType,
			null);
	//初始化仓区
	wms_city_common.initWareNoForCascade(
			cmdefarea.user.locno,
			['wareNoCondition','wareNo'],
			['areaNoCondition',null],
			null,
			null,
			[true,false],
			null
			);
	//初始化下架方式
	wms_city_common.comboboxLoadFilter(
			["oTypeCondition","oType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=O_TYPE',
			{},
			cmdefarea.oType,
			null);
	//初始化混载标志
	wms_city_common.comboboxLoadFilter(
			["mixFlagCondition","mixFlag"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=MIX_FLAG',
			{},
			cmdefarea.mixFlag,
			null);
	//初始化试算标志
	wms_city_common.comboboxLoadFilter(
			["bPick"],
			null,
			null,
			null,
			true,
			[false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=B_PICK',
			{},
			cmdefarea.bPick,
			null);
	//初始化品质
	wms_city_common.comboboxLoadFilter(
			["areaQuality"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			null,
			null);
	wms_city_common.closeTip("openUI");
});

cmdefarea.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//==================储区类型=======================
cmdefarea.areaType = {};
cmdefarea.areaTypeFormatter = function(value, rowData, rowIndex){
	return cmdefarea.areaType[value];
};
//==================储区类型=======================

//==================储区用途=======================
cmdefarea.areaUsetype = {};
cmdefarea.areaUsetypeFormatter = function(value, rowData, rowIndex){
	return cmdefarea.areaUsetype[value];
};
//==================储区用途=======================

//==================储区属性=======================
cmdefarea.areaAttribute = {};
cmdefarea.areaAttributeFormatter = function(value, rowData, rowIndex){
	return cmdefarea.areaAttribute[value];
};
//==================储区属性=======================

//==================属性类型=======================
cmdefarea.attributeType = {};
cmdefarea.attributeTypeFormatter = function(value, rowData, rowIndex){
	return cmdefarea.attributeType[value];
};
//==================属性类型=======================

//选择库区属性
cmdefarea.onSelectAreaAttribute = function(areaAttribute){
	
	$('#areaUsetype').combobox('enable');
	$('#attributeType').combobox('enable');
	
	//A代表属性类型；
	//B代表库区属性；
	//C代表库区用途。
	if(areaAttribute == "0"){
		
		//当B为作业区时，A不能变只能为存储区（变成灰色，不可编辑状态），C可为1,2,3,4,5,7,8。
		cmdefarea.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ATTRIBUTE_TYPE', {}, false, function(data){
			var dataList = [];
			for(var i = 0;i<data.length;i++){
				var tempData = data[i];
				if(tempData.itemvalue=='0' || tempData.itemvalue=='6'){
	       			dataList[dataList.length] = {itemvalue:tempData.itemvalue,itemname:tempData.itemname};
				}
			}
			$('#attributeType').combobox({
				data : dataList,
				valueField : 'itemvalue',
				textField : 'valueAndText',
				loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
				       		 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
			       	 }
			    	return data;
			    }
			}).combobox('setValue',"0");
		});
		
		cmdefarea.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREAUSE_TYPE', {}, false, function(data){
			var dataList = [];
			for(var i = 0;i<data.length;i++){
				var tempData = data[i];
				if(tempData.itemvalue=='1'||tempData.itemvalue=='2'||tempData.itemvalue=='3'||tempData.itemvalue=='4'||tempData.itemvalue=='5'||tempData.itemvalue=='7'||tempData.itemvalue=='8'){
	       			dataList[dataList.length] = {itemvalue:tempData.itemvalue,itemname:tempData.itemname};
				}
			}
			$('#areaUsetype').combobox({
				data : dataList,
				valueField : 'itemvalue',
				textField : 'valueAndText',
				loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
				       		 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
			       	 }
			    	return data;
			    }
			});
	    });
		
	}else if(areaAttribute == "1"){
		
		//当B为暂存区时，C不能变只能为普通存储区（变成灰色，不可编辑状态），A可为1,3,5,6,7。
		cmdefarea.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ATTRIBUTE_TYPE', {}, false, function(data){
			var dataList = [];
			for(var i = 0;i<data.length;i++){
				var tempData = data[i];
				if(tempData.itemvalue=='1'||tempData.itemvalue=='3'||tempData.itemvalue=='5'||tempData.itemvalue=='6'||tempData.itemvalue=='7'){
	       			dataList[dataList.length] = {itemvalue:tempData.itemvalue,itemname:tempData.itemname};
				}
			}
			$('#attributeType').combobox({
				data : dataList,
				valueField : 'itemvalue',
				textField : 'valueAndText',
				loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
				       		 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
			       	 }
			    	return data;
			    }
			});
		});
		
		cmdefarea.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREAUSE_TYPE', {}, false, function(data){
			var dataList = [];
			for(var i = 0;i<data.length;i++){
				var tempData = data[i];
				if(tempData.itemvalue=='1'){
	       			dataList[dataList.length] = {itemvalue:tempData.itemvalue,itemname:tempData.itemname};
				}
			}
			$('#areaUsetype').combobox({
				data : dataList,
				valueField : 'itemvalue',
				textField : 'valueAndText',
				loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
				       		 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
			       	 }
			    	return data;
			    }
			}).combobox('setValue',"1");;
	    });
		
	}else if(areaAttribute == "4"){
		//当B为虚拟区时，A只能为存储区（变成灰色，不可编辑状态），C只能为普通存储区（变成灰色，不可编辑状态）
		//当B为暂存区时，C不能变只能为普通存储区（变成灰色，不可编辑状态），A可为1,3,5,6,7。
		cmdefarea.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ATTRIBUTE_TYPE', {}, false, function(data){
			var dataList = [];
			for(var i = 0;i<data.length;i++){
				var tempData = data[i];
				if(tempData.itemvalue=='0'){
	       			dataList[dataList.length] = {itemvalue:tempData.itemvalue,itemname:tempData.itemname};
				}
			}
			$('#attributeType').combobox({
				data : dataList,
				valueField : 'itemvalue',
				textField : 'valueAndText',
				loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
				       		 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
			       	 }
			    	return data;
			    }
			}).combobox('setValue',"0");
		});
		
		cmdefarea.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREAUSE_TYPE', {}, false, function(data){
			var dataList = [];
			for(var i = 0;i<data.length;i++){
				var tempData = data[i];
				if(tempData.itemvalue=='1' || tempData.itemvalue=='5' || tempData.itemvalue=='7'){
	       			dataList[dataList.length] = {itemvalue:tempData.itemvalue,itemname:tempData.itemname};
				}
			}
			$('#areaUsetype').combobox({
				data : dataList,
				valueField : 'itemvalue',
				textField : 'valueAndText',
				loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
				       		 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
			       	 }
			    	return data;
			    }
			}).combobox('setValue',"1");
	    });
	}else{
		
		cmdefarea.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ATTRIBUTE_TYPE', {}, false, function(data){
			$('#attributeType').combobox({
				data : data,
				valueField : 'itemvalue',
				textField : 'valueAndText',
				loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
				       		 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
			       	 }
			    	return data;
			    }
			});
		});
		
		cmdefarea.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREAUSE_TYPE', {}, false, function(data){
			$('#areaUsetype').combobox({
				data : data,
				valueField : 'itemvalue',
				textField : 'valueAndText',
				loadFilter:function(data){
			    	if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
				       		 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
			       	 }
			    	return data;
			    }
			});
	    });
		
	}
	
};

//==================下架方式=======================
cmdefarea.oType = {};
cmdefarea.oTypeFormatter = function(value, rowData, rowIndex){
	return cmdefarea.oType[value];
};
//==================下架方式=======================
//==================混载标志=======================
cmdefarea.mixFlag = {};
cmdefarea.mixFlagFormatter = function(value, rowData, rowIndex){
	return cmdefarea.mixFlag[value];
};
//==================混载标志=======================
//==================试算标识=======================
cmdefarea.bPick = {};
cmdefarea.bPickFormatter = function(value, rowData, rowIndex){
	return cmdefarea.bPick[value];
};
//==================试算标识=======================
cmdefarea.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
cmdefarea.openUI = function(opt){
	$('#openUI').window({
		title:opt
	});
	$("#info_save").show();
	$('#openUI').window('open');
};
cmdefarea.closeUI = function(opt){
	$('#openUI').window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};
cmdefarea.addUI = function(opt){
	cmdefarea.openUI("新增");
	cmdefarea.clearAll();
	//$("#wareNo").attr('readOnly',false);
	$("#wareNo").combobox('enable');
	$("#areaNo").attr('readOnly',false);
	
	//新增加载全部的库区类型组合
	cmdefarea.onSelectAreaAttribute('X');
	
	$("#locno+ span input.combo-text").attr("readOnly",false);
	$("#locno+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#locno+ span").css("background-color","white");
	$("#locno+ span input.combo-text").css("background-color","white");
	
	/*$("#wareNo+ span input.combo-text").attr("readOnly",false);
	$("#wareNo+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#wareNo+ span").css("background-color","white");
	$("#wareNo+ span input.combo-text").css("background-color","white");*/
	$("#opt").val("add");
	
	$('#oType').combobox('setValue', 'P');
	$('#bPick').combobox('setValue', '0');
	$("#info_save").show();
	$("#info_edit").hide();
};

cmdefarea.editUI = function(){
	$("#info_save").hide();
	$("#info_edit").show();
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		cmdefarea.loadDetail(checkedRows[0],"edit");
	}
};

cmdefarea.manage = function(opt){
	var opt = $("#opt").val();
	if(opt=="add"){
		cmdefarea.save();
	}else{
		cmdefarea.update();
	}
};
cmdefarea.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/cm_defarea/list.json?locno='+cmdefarea.locno,'title':'库区列表','pageNumber':1 });
};

cmdefarea.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		cmdefarea.openUI("修改");
		$("#info_save").hide();
		$("#info_edit").show();
	}else{
		cmdefarea.openUI("详情");
		$("#info_save").hide();
		$("#info_edit").hide();
	}
	//修改加载对应库区属性的组合
	cmdefarea.onSelectAreaAttribute(rowData.areaAttribute);
	$("#itemType").combobox("select",rowData.itemType);
	$('#dataForm').form('load',rowData);
	$("#wareNo").combobox('disable');
	$("#areaNo").attr('readOnly',true);
	
	$("#locno+ span input.combo-text").attr("readOnly",true);
	$("#locno+ span input.combo-text+ span span").removeClass("combo-arrow");
	$("#locno+ span").css("background-color","#EBEBE4");
	$("#locno+ span input.combo-text").css("background-color","#EBEBE4");
	
	/*$("#wareNo+ span input.combo-text").attr("readOnly",true);
	$("#wareNo+ span input.combo-text+ span span").removeClass("combo-arrow");
	$("#wareNo+ span").css("background-color","#EBEBE4");
	$("#wareNo+ span input.combo-text").css("background-color","#EBEBE4");*/
};
cmdefarea.checkExistFun = function(url,checkColumnJsonData){
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

cmdefarea.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/cm_defarea/get_count.json';
         var checkDataNo={"locno":cmdefarea.user.locno,"wareNo":$("#wareNo").combobox('getValue'),"areaNo":$("#areaNo").val()};
	     if(cmdefarea.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('库区主键已存在,不能重复!',1);
	    	  $("#locno").focus();
	    	  return;
	      }
			 
		 //3. 保存
	     wms_city_common.loading("show","正在保存......");
         var url = BasePath+'/cm_defarea/addDefcartype';
         fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					param.locno = cmdefarea.user.locno;
				},
				success: function(data){
					if(data=="success"){
						alert('新增成功!');
						//4.保存成功,清空表单
						cmdefarea.loadDataGrid();
						cmdefarea.closeUI();
					} else if(data=="length") {
						alert('新增失败,库区编码不符合规范!',2);
					} else {
						alert('新增失败,请联系管理员!',2);
					}
					wms_city_common.loading();
			    },
				error:function(){
					alert('新增失败,请联系管理员!',2);
					wms_city_common.loading();
				}
		   });
	     
};

cmdefarea.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
     //2.绑定数据
    wms_city_common.loading("show","正在修改库区......");
    var url = BasePath+'/cm_defarea/update';
    fromObj.form('submit', {
		url: url,
		onSubmit: function(param){
			param.editor = cmdefarea.user.loginName;
			param.editorName = cmdefarea.user.username;
			//param.edittm = cmdefarea.user.currentDate19Str;
			param.locno = cmdefarea.user.locno;
			param.wareNo = $('#wareNo').combobox('getValue');
		},
		success: function(){
			 alert('修改成功!');
			 //3.保存成功,清空表单
			 cmdefarea.loadDataGrid();
			cmdefarea.closeUI();
			wms_city_common.loading();
			 return;
	    },
		error:function(){
			alert('修改失败,请联系管理员!',2);
			wms_city_common.loading();
		}
   });
};

cmdefarea.deleteRows = function(){
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keys = [];
	$.each(checkedRows, function(index, item){
		keys[keys.length] = {locno:item.locno,wareNo:item.wareNo,areaNo:item.areaNo};
		//keys.push(item.locno+"-"+item.wareNo+"-"+item.areaNo);
	});   
     //2.绑定数据
     var url = BasePath+'/cm_defarea/delete_records';
     var data={
			 locno:cmdefarea.locno,
			 datas:JSON.stringify(keys)
	 };
//	 var data={
//			    "ids":keys.join(",")
//	 };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){    
		    wms_city_common.loading("show", "正在删除库区......");
		   	 cmdefarea.ajaxRequest(url,data,true,function(result){
		   		 
		   		if(result.flag=='success'){
					//4.删除成功,清空表单
		   			cmdefarea.loadDataGrid();
					alert('删除成功!');
				}else if(result.flag=='warn'){
					alert("库区：【"+result.msg+"】存在通道明细,不能删除!");
				}else if(result.flag=='hasSettingNo'){
					alert("库区：【"+result.msg+"】已被上架策略【"+result.settingNo+"】引用,不能删除!");
				}else{
					alert('删除失败,请联系管理员!',2);
				}
		   		 
//				 if(returnMsg=='success'){
//					 //4.删除成功,清空表单
//					 cmdefarea.loadDataGrid();
//					 alert('删除成功!');
//				 }else{
//					 alert('删除失败,请联系管理员!',2);
//				 }
		   		wms_city_common.loading();
			}); 
		    }    
		}); 

};

cmdefarea.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
cmdefarea.searchClear = function(){
	$('#searchForm').form("clear");
	searchForm('/cm_defarea/list.json?locno='+cmdefarea.locno);
};
cmdefarea.searchArea = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/cm_defarea/list.json?locno='+cmdefarea.locno;
	var params = eval("(" +fromObjStr+ ")");
	params.sort='CONCAT(ware_no,area_no)';
    $( "#dataGridJG").datagrid( 'options' ).queryParams= params;
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

