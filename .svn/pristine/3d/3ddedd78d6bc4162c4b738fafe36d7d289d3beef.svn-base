<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/framework/layui/css/layui.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/framework/layui/layui.js"></script>
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-form-selected dl {
  			height:110px;
  		}	
  	</style>
  </head> 
  <body>
    <div>
		<form class="layui-form">
			<div class="layui-form-item">
            	<label class="layui-form-label">关键字:</label>
            	<div class="layui-input-block">
                	<input type="text" name="weixinkey" lay-verify="required" autocomplete="off" class="layui-input"/>
                	<input type="hidden" name="id"/>
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">值:</label>
            	<div class="layui-input-block">
                	<input type="text" name="value" lay-verify="required" autocomplete="off" class="layui-input"/>
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">名称:</label>
            	<div class="layui-input-block">
                	<input type="text" name="name" lay-verify="required" autocomplete="off" class="layui-input"/>
            	</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
		</form>
	</div>
	<script type="text/javascript">
		var formData=JSON.parse($("#editJson",parent.document).val());
		for(var key in formData){
			$("input[name='"+key+"']").val(formData[key]);
		}
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/gzh/updateMsg","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
	</script>
  </body>
</html>
