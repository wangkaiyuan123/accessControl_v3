<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<link  rel="stylesheet" href="${pageContext.request.contextPath}/css/delete.css"/>
  </head>  
  <body>
   	<p>确定要执行删除操作吗？</p>
   	<script type="text/javascript">
   		var id='${param.formData}';
   		layui.use('layer', function(){
   			var layer = layui.layer;
   			var $ = layui.$;
   			var submitForm =new submitObj({"type":"post","data":{'id':id},"url":contextPath+"/sysResource/deleteRecource","refresh":true,"Isform":false});
   			submitForm.submit();	
   		})
   	</script>
  </body>
</html>
