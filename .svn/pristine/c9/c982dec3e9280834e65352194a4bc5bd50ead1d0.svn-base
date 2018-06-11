<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		.layui-input-block {
  			margin-left:130px;
  		}
  		.layui-form-label {
  			width:100px;
  		}
  	</style>
  </head> 
  <body>
    <div>
		<form class="layui-form">
        	<div class="layui-form-item">
            	<label class="layui-form-label">卡号:</label>
            	<div class="layui-input-block">
                	<input type="text" name="cardNo" lay-verify="required" autocomplete="off" class="layui-input"/>
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">卡串码:</label>
            	<div class="layui-input-block">
                	<input type="text" name="cardNumber" lay-verify="required" autocomplete="off" class="layui-input"/>
            	</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/card/saveAdd","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
	</script>
  </body>
</html>
