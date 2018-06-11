<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
  		<jsp:include page="/inc.jsp"/>
  		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/Ztree/css/metroStyle/metroStyle.css"/>
  		<script type="text/javascript" src="${pageContext.request.contextPath}/framework/Ztree/js/jquery.ztree.core.min.js"></script>
    	<script type="text/javascript" src="${pageContext.request.contextPath}/framework/Ztree/js/jquery.ztree.excheck.min.js"></script>
  	</head>
  	<body>
   		<div>
			<form class="layui-form lf">
			 	<div class="layui-form-item">
			 		<label class="layui-form-label">角色代号</label>
			 		<div class="layui-input-block">
			 			<input type="text" name="roleCode" class="layui-input" lay-verify="required" autocomplete="off"/>
			 		</div>	
			 	</div>
			 	<div class="layui-form-item">
			 		<label class="layui-form-label">角色名称</label>
			 		<div class="layui-input-block">
			 			<input type="text" name="roleName" class="layui-input" lay-verify="required" autocomplete="off"/>
			 		</div>	
			 	</div>
			 	<div class="layui-form-item">
			 		<label class="layui-form-label">角色等级</label>
			 		<div class="layui-input-block">
			 			<input type="text" name="roleLevel" class="layui-input" lay-verify="required" autocomplete="off"/>
			 		</div>	
			 	</div>
			 	<input type="hidden" name="resourcesIds"/>
			 	<input type="hidden" name="id"/>
			 	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
			 </form>
			 <div class="rt" style="width:380px;height:260px;overflow-y:auto;">
			 	<p>授权:</p>	
			 	<ul id="roleTree" class="ztree"></ul>
			 </div>
		</div>
		<script type="text/javascript">
			var formData=JSON.parse($("#editJson",parent.document).val());
			var resourceIds=formData.resourceIds;
			var resourceIdsValue=resourceIds.join(",");
			$("input[name='resourcesIds']").val(resourceIdsValue);
			$("input[name='roleCode']").val(formData.roleCode);
			$("input[name='roleName']").val(formData.roleName);
			$("input[name='roleLevel']").val(formData.roleLevel);
			$("input[name='id']").val(formData.id);
			$(function(){
				$.ajax({
					type:'get',
					url:contextPath+'/sysResource/getAllResources',
					cache:false,
					success:function(data){
						if(typeof data=="string"){
							data=JSON.parse(data);
						}
						var setting={
							check: {
               					enable: true,
               					chkboxType: { "Y": "p", "N": "s" }
               				},
               				data:{
               					key:{
                   					name:"resourceName"
               					}
           					},
               				callback:{
               					onCheck: zTreeOnCheck
           					}
						}
						$.fn.zTree.init($("#roleTree"), setting, data);
						 var zTree = $.fn.zTree.getZTreeObj("roleTree");
						 //循环显示已有资源：
						 for(var i=0;i<resourceIds.length;i++){
						 	var node = zTree.getNodeByParam("id",resourceIds[i]);
						 	zTree.checkNode(node, true, true);	
						 	zTree.updateNode(node);
						 } 
					}
				});
			});	
			layui.use('form', function() {
            	var form = layui.form;
            	var $ = layui.$;
            	var submitForm =new submitObj({"type":"post","url":contextPath+"/role/updataRole","refresh":true,"Isform":true});
				submitForm.submit(form);
            })
			function zTreeOnCheck(event, treeId, treeNode){
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
           		var nodes = treeObj.getCheckedNodes(true);
           		var len=nodes.length;
           		var value="";
           		for(var i=0;i<len;i++){
           			value+=nodes[i].id+",";   //拼接的资源Id集合
           		}
           		value=value.slice(0,value.length-1); 
           		$("input[name='resourcesIds']").val(value);
			}
		</script>
  	</body>
</html>
