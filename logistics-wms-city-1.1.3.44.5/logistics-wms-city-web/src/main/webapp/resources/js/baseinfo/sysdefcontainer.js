var sysdefcontainer = {};
sysdefcontainer.user;
//仓库编码
sysdefcontainer.locno;

sysdefcontainer.tansforArrayToMap = function(oriData){
	var str = "";
	for(var i = 0,length = oriData.length;i<length;i++){
		str = str+"\""+oriData[i].itemvalue +"\":\""+oriData[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};
//==================容器类型=======================
//　B-物流箱；C-原装箱；P-栈板；R-笼车
sysdefcontainer.containerType = {};
sysdefcontainer.containerTypeFormatter = function(value, rowData, rowIndex){
	return sysdefcontainer.containerType[value];
};
//==================容器类型=======================

//容器前缀编号   BS-物流箱；BS-原装箱；PS-栈板；RS-笼车
sysdefcontainer.containerPrefixSelect = {    
	    "BS":"物流箱",
	    "CS":"原装箱",
	    "PS":"栈板",
	    "RS":"笼车"
};
sysdefcontainer.containerPrefixFormatter = function(value, rowData, rowIndex){
	return sysdefcontainer.containerPrefixSelect[value];
};
sysdefcontainer.containerPrefix = [{    
    "text":"物流箱", 
    "value":"BS" 
},{    
    "text":"原装箱", 
    "value":"CS"   
},{    
    "text":"栈板", 
    "value":"PS"   
},{    
    "text":"笼车", 
    "value":"RS"   
}];
sysdefcontainer.initContainerPrefix = function(){
	$('#containerPrefix').combobox({
	     valueField:"value",
	     textField:"textForSearch",
	     data:sysdefcontainer.containerPrefix,
	     panelHeight:150,
	     loadFilter:function(data){
		    	if(data){
		       		 for(var i = 0;i<data.length;i++){
		       			 var tempData = data[i];
		       			 tempData.textForSearch = tempData.value+'→'+tempData.text;
		       		 }
		       	 }
		    	return data;
		    }
	  });
	$('#containerPrefixCondition').combobox({
		valueField:"value",
		textField:"textForSearch",
		data:sysdefcontainer.containerPrefix,
		panelHeight:150,
		loadFilter:function(data){
	    	if(data){
	    		var temp = [];
	       		 temp[temp.length] = {value:'',textForSearch:'全选'};
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.textForSearch = tempData.value+'→'+tempData.text;
	       			temp[temp.length] = tempData;
	       		 }
	       		 return temp;
	       	 }
	    	return data;
	    }
	});
	$('#containerPrefix').combobox("select",sysdefcontainer.containerPrefix[0].value);  
};
//==================容器状态 =====================
//0：禁用；1：正常
sysdefcontainer.manageStatus = {};
sysdefcontainer.manageStatusFormatter = function(value, rowData, rowIndex){
	return sysdefcontainer.manageStatus[value];
};
//==================容器状态 =====================


//==================容器用途====================
//"0":"客户物流箱","1":"拣货物流箱"
sysdefcontainer.useType = {};
sysdefcontainer.useTypeFormatter = function(value, rowData, rowIndex){
	return sysdefcontainer.useType[value];
};
//==================容器用途====================

sysdefcontainer.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
sysdefcontainer.openUI = function(opt){
	$('#openUI').window({
		title:opt
	});
	$("#info_save").show();
	$('#openUI').window('open');
};
sysdefcontainer.closeUI = function(opt){
	$("#openUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};
sysdefcontainer.addUI = function(opt){
	sysdefcontainer.openUI("新增");
	sysdefcontainer.clearAll();
	$("#locno").attr('readOnly',false);
//	$("#containerType+ span input.combo-text").attr("readOnly",false);
//	$("#containerType+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#containerType").combobox('enable');
	$("#useType").combobox('enable');
//	$("#useType+ span input.combo-text").attr("readOnly",false);
//	$("#useType+ span input.combo-text+ span span").addClass("combo-arrow");

	$("#opt").val("add");
};

sysdefcontainer.editUI = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		sysdefcontainer.loadDetail(checkedRows[0],"edit");
	}
};

sysdefcontainer.manage = function(opt){
	var opt = $("#opt").val();
	if(opt=="add"){
		sysdefcontainer.save();
	}else{
		sysdefcontainer.update();
	}
};

sysdefcontainer.initLoc = function(){
	$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:BasePath+'/initCache/getCurrentUser',
			success : function(data) {
				sysdefcontainer.locno = data.locno;
			}
	});
};
sysdefcontainer.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/sys_defcontainer/list.json?locno='+sysdefcontainer.locno,'title':'容器资料列表','pageNumber':1 });
};

sysdefcontainer.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		sysdefcontainer.openUI("修改");
	}else{
		sysdefcontainer.openUI("详情");
		$("#info_save").hide();
	}
	
	
	$('#dataForm').form('load',rowData);
	$("#locno").attr('readOnly',true);
	
//	$("#containerType+ span input.combo-text").attr("readOnly",true);
//	$("#containerType+ span input.combo-text+ span span").removeClass("combo-arrow");
	$("#containerType").combobox('disable');
	$("#useType").combobox('disable');
//	$("#useType + span input.combo-text").attr("readOnly",true);
//	$("#useType + span input.combo-text + span span").removeClass("combo-arrow");

};
//$("#containerType+ span input.combo-text").attr("readOnly",true);
//$("#containerType+ span input.combo-text+ span span").removeClass("combo-arrow");
//containerType下一个span(跟containerType同级) 里面的样式为combo-text的input 下一个span(和input同级) 里面的span
//以上选择对应的html结构如下
//<input data-options="required:true,validType:['vnChinese[\'委托业主编号不能包含中文\']','vLength[0,10,\'最多只能输入10个字符\']']" id="containerType" style="width: 150px; display: none;" class="easyui-combobox combobox-f combo-f" comboname="containerType"><span class="combo" style="width: 148px; height: 20px;"><input type="text" class="combo-text validatebox-text validatebox-invalid" autocomplete="off" style="width: 126px; height: 20px; line-height: 20px;" title=""><span><span class="combo-arrow" style="height: 20px;"></span></span><input type="hidden" class="combo-value" name="containerType" value=""></span>
sysdefcontainer.checkExistFun = function(url,checkColumnJsonData){
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

sysdefcontainer.save = function(){
	    var fromObj=$('#dataForm');
	    
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
         //2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
         var checkUrl=BasePath+'/sys_defcontainer/get_countValidate.json';
         var checkDataNo={"locno":sysdefcontainer.user.locno,"containerType":$("#containerType").combobox('getValue'),"useType":$("#useType").combobox('getValue')};
	     if(sysdefcontainer.checkExistFun(checkUrl,checkDataNo)){
	    	  alert('容器资料编号已存在,不能重复!',1);
	    	  $("#locno").focus();
	    	  return;
	      }
			 
		 //3. 保存
         var url = BasePath+'/sys_defcontainer/saveSysDefcontainer';
         fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					param.createtm = sysdefcontainer.user.currentDate19Str;
					param.creator = sysdefcontainer.user.loginName;
					param.editor = sysdefcontainer.user.loginName;
					param.edittm = sysdefcontainer.user.currentDate19Str;
					param.locno = sysdefcontainer.user.locno;
				},
				success: function(r){
					if(r){
						 alert('新增成功!');
						 //4.保存成功,清空表单
						 sysdefcontainer.loadDataGrid();
						 sysdefcontainer.closeUI();
						 return;
					}else{
						alert('新增失败,请联系管理员!',2);
					}
					
			    },
				error:function(){
					alert('新增失败,请联系管理员!',2);
				}
		   });
	     
};

sysdefcontainer.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   
     //2.绑定数据
    var url = BasePath+'/sys_defcontainer/moditfySysDefcontainer';
    fromObj.form('submit', {
		url: url,
		onSubmit: function(param){
			param.editor = sysdefcontainer.user.loginName;
			param.locno = sysdefcontainer.user.locno;
			param.containerType = $("#containerType").combobox('getValue');
			param.useType = $("#useType").combobox('getValue');
		},
		success: function(r){
			if(r && r=='success'){
			 alert('修改成功!');
			 //3.保存成功,清空表单
			 sysdefcontainer.loadDataGrid();
			 sysdefcontainer.closeUI();
			 return;
			}else{
			 alert('修改失败,请联系管理员!');
			}
			
	    },
		error:function(){
			alert('修改失败,请联系管理员!',2);
		}
   });
};

sysdefcontainer.deleteRows = function(){
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keys = [];
	$.each(checkedRows, function(index, item){
		keys.push(item.locno+"-"+item.containerType+"-"+item.useType);
	});   
     //2.绑定数据
     var url = BasePath+'/sys_defcontainer/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){    
		   	 sysdefcontainer.ajaxRequest(url,data,true,function(result){
				 if(result=='success'){
					 //4.删除成功,清空表单
					 sysdefcontainer.loadDataGrid();
					 sysdefcontainer.closeUI();
					 alert('删除成功!');
				 }else{
					 alert('删除失败,请联系管理员!',2);
				 }
			}); 
		    }    
		});  



};

sysdefcontainer.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
sysdefcontainer.searchClear = function(){
	$('#searchForm').form("clear");
};
sysdefcontainer.searchContainer = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/sys_defcontainer/list.json?locno='+sysdefcontainer.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
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
} 

$(document).ready(function(){
	sysdefcontainer.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){sysdefcontainer.user= u;});
	//sysdefcontainer.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=USE_TYPE',{},false,sysdefcontainer.initUseType);
	//sysdefcontainer.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CONTAINER_TYPE',{},false,sysdefcontainer.initContainerType);
	//sysdefcontainer.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=MANAGE_STATUS',{},false,sysdefcontainer.initManageStatus);
	sysdefcontainer.initContainerPrefix();
	sysdefcontainer.initLoc();
	sysdefcontainer.loadDataGrid();
	//初始化容器类型
	wms_city_common.comboboxLoadFilter(
			["containerTypeCondition","containerType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CONTAINER_TYPE',
			{},
			sysdefcontainer.containerType,
			null);
	//初始化容器用途
	wms_city_common.comboboxLoadFilter(
			["useTypeCondition","useType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=USE_TYPE',
			{},
			sysdefcontainer.useType,
			null);
	//初始化容器状态
	wms_city_common.comboboxLoadFilter(
			["manageStatusCondition","manageStatus"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=MANAGE_STATUS',
			{},
			sysdefcontainer.manageStatus,
			null);
});
