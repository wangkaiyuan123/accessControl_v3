<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-input-block {
  			width:360px;
  		}
  	</style>
  </head> 
  <body>
	<div>
		<form class="layui-form">
			<div class="layui-form-item">
				<label class="layui-form-label">模板名称：</label>
				<div class="layui-input-block">
					<input type="text" class="layui-input" lay-verify="required" autocomplete="off" name="name"/>
				</div>
			</div>
			<div class="layui-form-item layui-form-text">
    			<label class="layui-form-label">内容：</label>
    			<div class="layui-input-block">
      				<textarea placeholder="请输入内容" class="layui-textarea" name="content" lay-verify="required"></textarea>
    			</div>
			</div>
			<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
		</form>
	</div>
	<script type="text/javascript">
		var formData=JSON.parse($("#editJson",parent.document).val());
		$("input[name='name']").val(formData.name);
		$("textarea").val(formData.content);
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/message/updateMessage","refresh":true,"Isform":true});
			submitForm.submit(form);
        });	
	</script>    
  </body>
</html>
