<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  </head>
  <body>
	<div>
		<form class="layui-form">
			 <div class="layui-form-item">
    			<label class="layui-form-label">机构名称:</label>
    			<div class="layui-input-block">
      				<input type="text" name="regionName" lay-verify="required" autocomplete="off" class="layui-input" id="regionName">
      				<input type="hidden" name="id"  id="organId"/>
    			</div>
  			</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>    
	</div>
	<script type="text/javascript">
		//var formData='${param.formData}';
		//var formDataObj=JSON.parse(formData);
		var formDataObj=JSON.parse($("#editJson",parent.document).val());
		$("#regionName").val(formDataObj.regionName);
		$("#organId").val(formDataObj.id);
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/regions/updataRegion","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
	</script>   
  </body>
</html>