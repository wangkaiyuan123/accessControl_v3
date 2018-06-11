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
   		var formData=JSON.parse($("#editJson",parent.document).val());
   		var id=formData.id;
   		var unitArrays=formData.unitIds;
   		var regionId = formData.regionId;
   		console.info(formData);
   		if("物业人员"==formData.personType){
               $.ajax({
                     type:'post',
    			     cache:false,
    			     url:contextPath+'/person/queryAdmin',
    			     data:{tele:formData.telephone},
    			     dataType:'json',
    			     success:function(data){
    			        if(data.success){
    			           $("p").text("该物业人员已是管理员,是否执行删除？");
    			           //alert("该物业人员已是管理员,是否执行删除？");
    			        }
    			     }
               });   		    
   		}
   		var unitArrayIds = JSON.stringify(unitArrays);
   		layui.use('layer', function(){
   			var layer = layui.layer;
   			var $ = layui.$;
   			var submitForm =new submitObj({"type":"post","data":{'id':id,'unitArrays':unitArrayIds,'regionId':regionId},"url":contextPath+"/person/deletePerson ","refresh":true,"Isform":false});
   			submitForm.submit();	
   		})
   	</script>
  </body>
</html>
