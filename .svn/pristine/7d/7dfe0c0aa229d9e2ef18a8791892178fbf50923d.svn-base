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
			 	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
			 </form>
			 <div class="rt" style="width:380px;height:260px;overflow-y:auto;">
			 	<p>授权:</p>	
			 	<ul id="roleTree" class="ztree"></ul>
			 </div>
		</div>
		<script type="text/javascript">
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
					}
				});
			});	
			layui.use('form', function() {
            	var form = layui.form;
            	var $ = layui.$;
            	var submitForm =new submitObj({"type":"post","url":contextPath+"/role/saveAdd","refresh":true,"Isform":true});
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
