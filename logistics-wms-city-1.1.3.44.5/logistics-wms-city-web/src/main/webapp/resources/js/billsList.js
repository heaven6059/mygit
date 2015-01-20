    function closeWindow(menuName){
         parent.$('#centerFrame').tabs('close',menuName);
     }
     
     // 添加
     function addDataGrid(){
          $("#mainDataGridEdit").datagrid('appendRow', {});
          var rows = $("#mainDataGridEdit").datagrid('getRows');
          $("#mainDataGridEdit").datagrid('beginEdit', rows.length - 1);
          $("#mainDataGridEdit").datagrid('selectRow', rows.length - 1);
          
     }
     
     // 删除
     function removeDataGrid(){
           var row = $("#mainDataGridEdit").datagrid('getSelected');
            if (row) {
                   var rowIndex = $("#mainDataGridEdit").datagrid('getRowIndex', row);
                   $("#mainDataGridEdit").datagrid('deleteRow', rowIndex);
                   if((rowIndex-1)>=0){
                	   $("#mainDataGridEdit").datagrid('selectRow', rowIndex-1);
                   }
                  
           }
     }
     
  
     // 结束编辑 
     function endEdit(){
            var rows = $("#mainDataGridEdit").datagrid('getRows');
            for ( var i = 0; i < rows.length; i++) {
                $("#mainDataGridEdit").datagrid('endEdit', i);
            }
     }
	    
     
   // 保存 主从表一起保存
   function  saveDataGrid(){
        endEdit();
        
        
         var flag= $("#dataFrom").form('validate');
         
         if(flag==false){
           return ;
         }
         
        
        var effectRow = new Object();
        // 操作类型 是保存save 还是编辑edit
        var pid=$("#pid").val();
        if(pid!=null&&pid!=''){
           effectRow["operateFlag"]='edit';
        }else{
           effectRow["operateFlag"]='save';
        }
        
        
        
         // 主表信息
        var stocknum=$("#stocknum").val();
        var factoryid=$("#factoryid").val();
        var factoryname=$("#factoryname").val();
        var xuantype=$("#xuantype").val();
        var stockid=$("#stockid").val();
        var stockname=$("#stockname").val();
        var basetype=$("#basetype").val();
        var createdate=$('#createdate').datetimebox('getValue');	
        
        var mainInfo=new Object();
        mainInfo.stocknum=stocknum;
        mainInfo.factoryid=factoryid;
        mainInfo.factoryname=factoryname;
        mainInfo.xuantype=xuantype;
        mainInfo.stockid=stockid;
        mainInfo.stockname=stockname;
        mainInfo.basetype=basetype;
        mainInfo.createdate=createdate;
        
        effectRow["mainInfo"]=JSON.stringify(mainInfo);
        
        if($("#mainDataGridEdit").datagrid('getChanges').length) {
        
            var inserted = $("#mainDataGridEdit").datagrid('getChanges', "inserted");
            var deleted = $("#mainDataGridEdit").datagrid('getChanges', "deleted");
            var updated = $("#mainDataGridEdit").datagrid('getChanges', "updated");
            

            
            // 明细信息
            if (inserted.length) {
                effectRow["inserted"] = JSON.stringify(inserted);
            }
            if (deleted.length) {
                effectRow["deleted"] = JSON.stringify(deleted);
            }
            if (updated.length) {
                effectRow["updated"] = JSON.stringify(updated);
            }
           
        }
       
        $.post(saveMainMxVo, effectRow, function(rsp) {
                if(rsp.success){
                    $.messager.alert("提示", "提交成功！");
                    $("#mainDataGridEdit").datagrid('acceptChanges');
                }
                $("#mainDataGridEdit").reload();
            }, "JSON").error(function() {
                $.messager.alert("提示", "提交错误了！");
            }); 

       
        
     
    }
   
     //从主页面双击进入编辑页面
      function editBillList(pid){
    	  
    	  clearAll();
    	 
    	  
    	  if(pid==''||pid==null){
    		  
    	     return ;
    	  }
    	  
      
          $('#mainTab').tabs('select',"单据明细管理");
          
          $("#stocknum").attr('value',pid);
          $("#stocknum").attr('disabled',true);
          $("#stocknum").attr('class','grn'); 
          $("#pid").attr('value',pid);
          
          
         var url = queryMainURL;
		 var data={
				"stocknum":pid
		  };
	
		 ajaxRequest(url,data,function(result){
			
			
			$("#factoryid").attr("value",result.stockVo.factoryid);
			$("#factoryname").attr("value",result.stockVo.factoryname);
			$("#xuantype").attr("value",result.stockVo.xuantype);
			$("#stockid").attr("value",result.stockVo.stockid);
			$("#stockname").attr("value",result.stockVo.stockname);
			$("#basetype").attr("value",result.stockVo.basetype);
			$("#createdate").datebox("setValue",result.stockVo.createdate);
			
		});
          
          
          
          // 加载明细
          var queryParams={  id:pid  };
          $("#mainDataGridEdit").datagrid('options').queryParams=queryParams; 
          $("#mainDataGridEdit").datagrid('options').url=queryMxURL;
          $("#mainDataGridEdit").datagrid('load');
          
      } 
      
      // 返回前一个页面
      function returnTab_1(){
         $('#mainTab').tabs('select',"单据清单查询");
      }
      
      function addStockVo(){
           clearAll();
           $("#stocknum").attr('onfocus',true);
           $('#mainTab').tabs('select',"单据明细管理");
      } 

     // 清空
     function  clearAll(){
     
            $("#pid").attr('value',"");
            //主档信息清空
            $("#stocknum").attr('value',"");
            $("#stocknum").attr('disabled',false);
            $("#stocknum").attr('class','while'); 
          	$("#factoryid").attr("value","");
			$("#factoryname").attr("value","");
			$("#xuantype").attr("value","");
			$("#stockid").attr("value","");
			$("#stockname").attr("value","");
			$("#basetype").attr("value","");
			$("#createdate").datebox("setValue","");
			
			// 清明细
			$("#mainDataGridEdit").datagrid('loadData',{total:0,rows:[]});
     }
     
     

     //发达ajax请求
     function ajaxRequest(url,reqParam,callback){
 	$.ajax({
 		  type: 'POST',
 		  url: url,
 		  data: reqParam,
 		  cache: true,
 		  success: callback
 	});
   }  
     
    


    

