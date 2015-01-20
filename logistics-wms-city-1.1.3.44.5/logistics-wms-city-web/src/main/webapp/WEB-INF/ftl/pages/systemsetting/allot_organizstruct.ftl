<#include  "/WEB-INF/ftl/common/header.ftl" >
<script>
	function getCheckedStruc(){
		var node = $('#resourceTree').tree('getSelected');
		
		if(node == null){
			alert('请选择一个节点');
			return false;
		}
		
		var organizNo = "";
		var organizName = "";
		
		try{
			organizNo = node.attributes.no;
		}catch(e){
			organizNo ="";
		}
		organizName =node.text ;
		window.top.frames["mbdif"].initOrganizStruct(organizNo,organizName);
		closewindow();
	}
	
	function loadNodeData(){};
</script>

<div style="padding:10px;">
	<input type="button" onclick="getCheckedStruc()" value="确定" class="btn-add-normal"/>
	<br/>
	<br/>
	<ul id="resourceTree">
		<script>onloadResourceTree('queryOrgTree.htm',false);</script>
	</ul>
</div>

