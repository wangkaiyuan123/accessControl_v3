<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<link  rel="stylesheet" href="${pageContext.request.contextPath}/css/delete.css"/>
  </head>  
  <body>
   	<p>确定要执行重置密码操作吗？</p>
   	<script type="text/javascript">
   		//var id=JSON.parse('${param.formData}').id;
   		var id=JSON.parse($("#editJson",parent.document).val()).id;
   		layui.use('layer', function(){
   			var layer = layui.layer;
   			var $ = layui.$;
   			var submitForm =new submitObj({"type":"post","data":{'operatorId':id},"url":contextPath+"/regions/resetPwd","refresh":true,"Isform":false});
   			submitForm.submit();	
   		})
   	</script>
  </body>
</html>
