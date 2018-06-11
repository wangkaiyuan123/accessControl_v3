<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-input-block {
  			width:300px;
  		}
  	</style>
  </head>  
  <body>
   	<div>
		<form class="layui-form">
        	<div class="layui-form-item">
        		<label class="layui-form-label">模式:</label>
        		<div class="layui-input-block">
      				<input type="radio" name="style" value="1" title="授权模式 ">
      				<input type="radio" name="style" value="0" title="非授权模式">
    			</div>
    			<input type="hidden" name="id" />	
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
    </div>
   	<script type="text/javascript">
   		var formData=JSON.parse($("#editJson",parent.document).val());
   		$("input[name='id']").val(formData.id); 			
   		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/device/changeMode","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
   	</script>
  </body>
</html>
