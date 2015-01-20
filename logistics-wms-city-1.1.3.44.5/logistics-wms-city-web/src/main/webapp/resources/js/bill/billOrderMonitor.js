var billOrder = {};

billOrder.initGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billOrder.initDataTree = function(billNo){
	var tempObj = $('#dataGridOrder_trackDtl');
	tempObj.tree({     
        url:BasePath+'/bill_order_track_dtl/queryOrderTrackDtlList.htm?billNo='+billNo,  
        onClick : function (node) {
        	if(node.state && node.state=='closed'){
        		$(this).tree('expand', node.target);
        	}
        },
        onExpand : function (node) {
        	$(this).tree('select', node.target);
        },
        onLoadSuccess : function(node,data){
        	var nodeArr = tempObj.tree('getRoots');
        	for(var i=0;i<nodeArr.length;i++){
        		tempObj.tree('collapse',nodeArr[i].target);
        	}
        }
    });
};

billOrder.initTrackData = function(rowIndex, rowData){
	billOrder.initDataTree(rowData.billNo);
    var url = BasePath+ '/bill_order_track/list.json';
    var queryParams = {billNo:rowData.billNo};
    billOrder.initGridDataUtil('dataGridOrder_track',url,queryParams);
};

billOrder.initDataGrid = function(){
	var tempObj = $('#dataGridOrder');
	tempObj.datagrid({    
	    url:BasePath+'/billTransport/list.json',    
	    pagination:true,
	    pageSize:30,
	    rownumbers:true,
	    singleSelect:true,
	    columns:[[    
	        {field:'billNo',title:'入库订单号',width:140,
				styler: function(value,row,index){
					if (index == '1'){
						return 'color:red;';
					}
				}
	        }
	    ]],
	    onSelect:function(rowIndex, rowData){
	    	billOrder.initTrackData(rowIndex, rowData);
         }
	});  
	var pager = tempObj.datagrid('getPager');
	pager.pagination({    
		displayMsg:'',
		showRefresh:false,
		beforePageText:'',
		afterPageText:'',
		showPageList:false
	});  
};

$(document).ready(function(){
	billOrder.initDataGrid();
	//入库订单时效监控查询切换选项卡改变dataGirad宽度
	$('#main_orderId').tabs({
		onSelect:function(title,index){
			if(index=='1'){
				$('#dataGridOrder_track').datagrid('resize', {
					width:document.body.clientWidth-2045
				});
			}
		}
	});
});