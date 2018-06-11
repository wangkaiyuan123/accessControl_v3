<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<link  rel="stylesheet" href="${pageContext.request.contextPath}/css/delete.css"/>
  </head>  
  <body>
   	<p>确定要升级为管理员吗？</p>
   	<script type="text/javascript">
   		var formData=JSON.parse($("#editJson",parent.document).val());
   		var id=formData.id;
   		var personTypeId=formData.personTypeId;
   		var telephone = formData.telephone;
   		var name  = formData.name;
   		layui.use('layer', function(){
   			var layer = layui.layer;
   			var $ = layui.$;
   			var submitForm =new submitObj({"type":"post","data":{'id':id,'personTypeId':personTypeId,'telephone':telephone,'name':name},"url":contextPath+"/person/updateGly","refresh":true,"Isform":false});
   			submitForm.submit();	
   		})
   	</script>
  </body>
</html>
