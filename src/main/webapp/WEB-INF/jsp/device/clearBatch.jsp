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
   		var Ids = [];
   		for(var i=0;i<formData.length;i++){
			  Ids.push(formData[i].id);   			
   		}
   		//alert(deviceIds.length);
   		var deviceIds = JSON.stringify(Ids);
   		//alert(deviceIds.substring(1,deviceIds.length-1));
   		layui.use('layer', function(){
   			var layer = layui.layer;
   			var $ = layui.$;
   			var submitForm =new submitObj({"type":"post","data":{'deviceIds':deviceIds.substring(1,deviceIds.length-1)},"url":contextPath+"/device/clearCardBatch","refresh":true,"Isform":false});
   			submitForm.submit();	
   		})
   	</script>
  </body>
</html>
