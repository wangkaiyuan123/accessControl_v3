<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<link  rel="stylesheet" href="${pageContext.request.contextPath}/css/delete.css"/>
  </head>  
  <body>
   	<p>确定要执行该操作吗？</p>
   	<script type="text/javascript">
   		var formData=JSON.parse($("#editJson",parent.document).val());
   		var id=formData.id;
   		for(var i=0;i<formData.length;i++){
			  id.push(formData[i].id);   			
   		}
   		layui.use('layer', function(){
   			var layer = layui.layer;
   			var $ = layui.$;
   			var submitForm =new submitObj({"type":"post","data":{'id':id},"url":contextPath+"/device/clearCard","refresh":true,"Isform":false});
   			submitForm.submit();	
   		})
   	</script>
  </body>
</html>