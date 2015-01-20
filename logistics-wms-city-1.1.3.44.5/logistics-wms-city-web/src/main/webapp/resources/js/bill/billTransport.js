var billTransport = {};

var tableID='mainDataGridEdit';

var sysNo="";
var maxType=0;
var startType=0;
var curMaxType=0;
var curRowIndex=-1;
var type=0;

callBackBill=function(curRowIndex,rowData){
	
	if(type==1||type==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			editBillTransport(rowData.billNo,rowData.sysNo,curRowIndex);
			type=0;
		}else{
			if(type==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
	
};

//上单
billTransport.preBill=function(){
	if(curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	type=1;
    preBill('mainDataGrid',curRowIndex,1,callBackBill);
};


//下单
billTransport.nextBill=function(){
	if(curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	type=2;
    preBill('mainDataGrid',curRowIndex,2,callBackBill);
};

//新单
billTransport.newBill=function(){
	billTransport.canelBill();
	$("#pid").attr('value',"");
	
}

//审单
billTransport.shBill=function(){
	
}

billTransport.resize=function(){
	$('#mainTab').tabs('resize');
}




//清空
billTransport.canelBill=function(){
	//非常重要这条============================================================================================
	$( "#mainDataGridEdit").datagrid( 'options' ).url=""; 
	$("#pid").attr('value',"");
	$("#dataForm").form("reset");
	//1.清空表头
	$("#mainDataGridEdit").datagrid({
        columns:[]
    }); 
	//2.删除所有行
	deleteAllGridCommon(tableID);
	deleteAllGridCommon(tableID);
	
}

//还原
billTransport.restore=function(){
	var billNo=$("#billNo").val();
	$("#pid").attr('value',billNo);
	var sysNo=$("#sysNo").combobox('getValue');
	if((billNo!=null&&billNo!='')&&(sysNo!=null&&sysNo!='')){
		editBillTransport(billNo,sysNo,curRowIndex);
	}
}


var preColNames=[
                 {title:"序列号",field:"seqId" },
                 {title:"商品编码",field:"itemNo",width:120,editor:{options:{required:true}}},
                 {title:"商品名称",field:"itemName"},
                 {title:"颜色",field:"colorNo"}, 
                 {title:"箱号",field:"boxNo"}, 
                 {title:"类别名称",field:"cateNo"}
                ];
 var   endColNames =[
                    {title:"合计数量",field:"allCounts",editor:{type:"numberbox",options:{precision:2} }},
                    {title:"单价",field:"cost"},
                    {title:"金额",field:"allCost"}
                  ] ; 
 var sizeTypeFiledName="sizeKind"; 



//动态生成表头
billTransport.changecolumn =function(itemvalue,flag){
	$( "#mainDataGridEdit").datagrid( 'options' ).url=""; 
    //所属品牌
    sysNo=itemvalue;
                      

     var getSqlURl=BasePath+'/initCache/getBrandList.htm';
      var queryParams;
       $.ajax({
               type: 'POST',
               url: getSqlURl,
               data: {sysNo:itemvalue,preColNames:JSON.stringify(preColNames),endColNames:JSON.stringify(endColNames),sizeTypeFiledName:sizeTypeFiledName},
               cache: true,
               async:false,
               success: function(returnData,msg){
                	queryParams=returnData.returnCols;
                	maxType=returnData.maxType;
                	startType=returnData.startType;
			  }
      });
 
 
     //1.动态加载表头
      $("#mainDataGridEdit").datagrid({
          columns:queryParams
     });  
        
     if(queryParams!=null&&queryParams!=[]&&queryParams!=''){
       //2.动态修改列editor
    	billTransport.updateEditor();
    	deleteAllGridCommon(tableID);
    	if(flag==1){
    	       //3.默认新增一条
    	       addDataGridCommon(tableID);
    	}

     }else{
       deleteAllGridCommon(tableID);
     }

};

//导出
exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var excelTitle='入库订单明细导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill('mainDataGridEdit',sysNo,preColNames,endColNames,sizeTypeFiledName,excelTitle);
}

//加载表格之前改变editor
billTransport.updateEditor=function(){
      var e = $("#mainDataGridEdit").datagrid('getColumnOption', 'itemNo');
      e.editor={  type : 'combobox',
    	  options:{
    		  loader: billTransport.myloader,
    		  mode: 'remote',
    		  panelHeight: 'auto',
    		  valueField: 'id',
    		  textField: 'name',
    		  onSelect:billTransport.selectItemHandel,
    		  onBeforeLoad: billTransport.onBeforeLoad
    	  }
     };
}

//4.检搜商品 
billTransport.myloader = function(param,success,error){
    
	var q = param.q || '';
	if (q.length < 1){return false;}
	$.ajax({
		url:BasePath+'/item/list.json',
        data: {
            maxRows: 20,
            q: q,
            sysNo:sysNo
        },
        panelHeight: 'auto',
        type: 'POST',
	    async:false,
		success: function(data){
			var items = $.map(data.rows, function(item){
				return {
					id: item.itemNo,
					name: item.itemNo
				};
			});
			success(items);
		}
	});
};


//5.选择下拉框	
billTransport.selectItemHandel= function(record){
    var id=record.id;
    var name=record.name;
    var rowIndex=getRowIndex(this);
    
    //根据商品编号查询相关商品信息
    var getSqlURl=BasePath+'/item/get';
 	$.ajax({
		  type: 'POST',
		  url: getSqlURl,
		  data: {itemNo:id},
		  cache: true,
		  async:false, 
		  success: function(returnData,msg){
		      
		      
		      //1.商品名称
			  var itemNameEditor = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':'itemName'});
			  itemNameEditor.target.val(returnData.itemName);
			  //2.颜色
			  var colorNoEditor = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':'colorNo'});
			  colorNoEditor.target.val(returnData.colorNoStr);
			  //3.类别
			  var cateNoEditor = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':'cateNo'});
			  cateNoEditor.target.val(returnData.cateNoStr);
			  //4.单价
			  var costEditor = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':'cost'});
			  costEditor.target.val(returnData.cost);
			  //5.尺码
			  var sizeKindEditor = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':'sizeKind'});
			  sizeKindEditor.target.val(returnData.sizeKind);
			  //$(sizeKindEditor.target).focus();
			  
			   
		  }
     });
};

//6.新增绑定事件
billTransport.onBeforeLoad=function(param){
    var rowIndex=getRowIndex(this);
    billTransport.keyupHandel(rowIndex);
};

billTransport.onEditHandel=function(rowIndex){
    billTransport.keyupHandel(rowIndex);
};

//公用的绑定方法 (1)新增的时候需要绑定 (2)双击编辑的时候需要绑定 各自传的参数不一样
billTransport.keyupHandel=function(rowIndex){
    
    var ed = $('#mainDataGridEdit').datagrid('getEditors', rowIndex);
    if(ed!=null&&ed.length>3){
       var countED = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':'allCounts'});
       var costED = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':'cost'});
       var allCostED = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':'allCost'});
       for ( var i = startType; i < (maxType+startType); i++){
                var e = ed [i];
                $(e.target).bind("keyup",function(w){
                    var allCounts=0;
                    for(var k=1;k<=maxType;k++){
                        var filedTemp="v"+k;
                        var tempEdit = $('#mainDataGridEdit').datagrid('getEditor',{'index':rowIndex,'field':filedTemp});
                        var tempValue=tempEdit.target.val();
                        if(tempValue!=null&&tempValue!=''){
                            allCounts+=parseInt(tempValue);
                        }
                    }
                    countED.target.val(allCounts);
                    //计算总金额
                    var cost=costED.target.val();
                    var costtemp=0.0;
                    if(cost!=null&&cost!=''){
                       costtemp=accMul(cost,allCounts);
                    }
                    allCostED.target.val(costtemp);
                });
              
       }
    }
};



//保存
billTransport.saveBillTransport= function(dataGridId){
	endEditCommon(dataGridId);
	
	
	var flag= $('#dataForm').form('validate');
    
    if(flag==false){
      return ;
    }
    var effectRow = new Object();
    
    var rows = $("#mainDataGridEdit").datagrid("getRows"); 
    if(rows.length<1){
    	alert('请录入明细');
    	return;
    }
    
 // 操作类型 是保存save 还是编辑edit
    var operateFlag="save";
    var pid=$("#pid").val();
    if(pid!=null&&pid!=''){
    	operateFlag='edit';
    }else{
    	operateFlag='save';
    }

    
    var url = BasePath+'/billTransport/saveZhuCongList';
    $('#dataForm').form('submit', {
		url: url,
		onSubmit: function(param){
			param.mxList=JSON.stringify(rows);
			param.operateFlag=operateFlag;
		},
		success: function(){
			 alert('保存成功!');
			 
			
	    },
		error:function(){
			alert('新增失败,请联系管理员!',2);
		}
   });
    
    
    
};

//进入编辑界面或者查询明细

function editBillTransport(billNo,sysNo,rowIndex){
	if (billNo=='' ||billNo== null){
        
        return ;
     }
	
	curRowIndex=rowIndex;
    
	 //1.设置给保存用来判断是新增还是编辑
	 $("#pid").attr('value',billNo);

	
	
	 //1.填充主档信息
	 //$('#dataForm' ).form('load' ,BasePath+ '/billTransport/get?billNo='+billNo);
		var url = BasePath+'/billTransport/get';
		var reqParam={
			   "billNo":billNo
	     };
		ajaxRequest(url,reqParam,function(data){
			data.storeNoFrom = data.storeNoFrom+'→'+data.storeNoFromStr;
			data.supplierNo = data.supplierNo+'→'+data.supplierNoStr;
			$('#dataForm').form('load',data);
			
			//底部单据状态显示栏
			$('#statusSp').html(data.statusStr);
			$('#statusTransSp').html(data.statusTransStr);
			$('#creatorSp').html(data.creator);
			$('#createtmSp').html(formatDate(data.createtm));
			$('#auditorSp').html(data.auditor);
			$('#auditdtSp').html(formatDate(data.auditdt));
		});
	 
	 billTransport.changecolumn(sysNo,2);
	 
	    var queryMxURL=BasePath+ '/billTransport/getMxList.htm' ;
	    
	   
	    //3.加载明细
	    var queryParams={billNo:billNo};
	    $( "#mainDataGridEdit").datagrid( 'options' ).queryParams=queryParams;
	    $( "#mainDataGridEdit").datagrid( 'options' ).url=queryMxURL;
	    $( "#mainDataGridEdit").datagrid( 'load' );
	    
	    $("#mainDataGridEdit").datagrid({
	    	   loadFilter: function (data){
	    		   if(data.errorCode!=null&&data.errorCode!=''){
	    			    showErrorWindow(data);
	    		   }else{
	    			   $('#mainTab').tabs( 'select' ,"单据明细" );
	    		   }
	    		   return data;
	    		}
	     });
	    $( "#mainDataGridEdit").datagrid('options').queryParams="";
	    

	    
	    
}


//刷新
function reload(){
    var tempObj = $('#mainDataGrid');
    tempObj.datagrid( 'reload' );
}

//删除 1==在查询界面删除  2==在编辑界面删除
billTransport.removeBillTransport=function(flag){
	
	var deleteBillNum='';
	if(flag==1){
		var checkedItems = $('#mainDataGrid').datagrid('getChecked');
		var billNoArray = [];
		$.each(checkedItems, function(index, item){
			billNoArray.push(item.billNo);
		});               
		deleteBillNum=billNoArray.join(",");
	}else{
		deleteBillNum=$("#billNo").val();
	}
	
	if(deleteBillNum!=null&&deleteBillNum!=''){
		   $.messager.confirm('提示','是否确定要删除所选信息及其明细?',function(r){
		          if(r){
		              	var url = BasePath+'/billTransport/removeBillTransport.htm';
						var data={
							"billNoArray":deleteBillNum
						};
						ajaxRequest(url,data,function(result){
							if(!result) return ;
							if(result == "success"){
								if(flag==1){
									reload();
								}else{
									billTransport.newBill();
								}
								return ;
							}else{
								alert('提示','删除失败,请联系管理员');
							}
						});
						
		          }
		   });
		   
		 
		}else{
		    alert('提示','请先选择需要删除的记录!','warning');
		}
}
	




billTransport.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150",
	     onSelect:function(record){
	    	 billTransport.changecolumn(record.itemvalue,1);
	     }
	  });
};

billTransport.initSupplier = function(){
	$('#supplierNo').combogrid({
		 panelWidth:450,   
         idField:'supplierNo',  
         textField:'textFieldName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/supplier/list.json',
         columns:[[  
             {field:'supplierNo',title:'供应商编码',width:140},  
             {field:'supplierName',title:'供应商名称',width:140}  
         ]],
         loadFilter:function(data){
        	 if(data && data.rows){
        		 for(var i = 0;i<data.rows.length;i++){
        			 var tempData = data.rows[i];
        			 tempData.textFieldName = tempData.supplierNo+'→'+tempData.supplierName;
        		 }
        	 }
     		 return data;
         } 
	  });
};

billTransport.initStore = function(){
	$('#storeNoFrom').combogrid({
		 panelWidth:450,   
         idField:'storeNo',  
         textField:'textFieldName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/store/list.json', 
         queryParams : {
				queryStoreType : '12,22'
			},
         columns:[[  
             {field:'storeNo',title:'客户编号',width:140},  
             {field:'storeName',title:'客户名称',width:140}  
         ]],
         loadFilter:function(data){
        	 if(data && data.rows){
        		 for(var i = 0;i<data.rows.length;i++){
        			 var tempData = data.rows[i];
        			 tempData.textFieldName = tempData.storeNo+'→'+tempData.storeName;
        		 }
        	 }
     		 return data;
         }
	  });
};



//切换选项卡刷新单据查询的dataGrid
function refreshTabs(){
	$('#mainTab').tabs({
		onSelect:function(title,index){
			
			$('#mainDataGrid').datagrid('resize', {
			     width:function(){return document.body.clientWidth;}
			});
			
		    $('#easyui-panel-id').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#mainDataGridEdit').datagrid('resize', {
		        width:function(){return document.body.clientWidth;}
		     });
		    
		},onLoad:function(panel){
			$('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		}
		
	});
}



$(document).ready(function(){

	billTransport.initBrand();//初始化所属品牌数据
	
	billTransport.initSupplier(); //初始化供应商
	
	billTransport.initStore(); //初始化树
	
	
});




function accMul(arg1, arg2) {
	var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
	try {
	m += s1.split(".")[1].length;
	} catch (e) {
	}
	try {
	m += s2.split(".")[1].length;
	} catch (e) {
	}
	return Number(s1.replace(".", "")) * Number(s2.replace(".", ""))/Math.pow(10, m);
}

