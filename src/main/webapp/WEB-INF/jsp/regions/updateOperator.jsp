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
            	<label class="layui-form-label">运维人员账号:</label>
            	<div class="layui-input-block">
                	<input type="text" name="operatorName" lay-verify="required" autocomplete="off" class="layui-input"/>
                	<input type="hidden" name="operatorId">
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">用户名:</label>
            	<div class="layui-input-block">
                	<input type="text" name="operatorRealName" lay-verify="required" autocomplete="off" class="layui-input"/>
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">电话:</label>
            	<div class="layui-input-block">
                	<input type="text" name="operatorPhone" lay-verify="required|phone" autocomplete="off" class="layui-input"/>
            	</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
		//var formData='${param.formData}';
		//var formDataObj=JSON.parse(formData);
		var formDataObj=JSON.parse($("#editJson",parent.document).val());
		$("input[name='operatorName']").val(formDataObj.userName);
		$("input[name='operatorRealName']").val(formDataObj.realName);
		$("input[name='operatorPhone']").val(formDataObj.telephone);
		$("input[name='operatorId']").val(formDataObj.id);
		/*for(var key in formDataObj){
			console.log(key);
			console.log($("input[name="+key+"]").length);
			if($("input[name="+key+"]".length)){
			//console.log(1);
				//$("input[name="+key+"]".val(formDataObj[key]);
			}
		}*/
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/regions/updataOptor","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
	</script>
  </body>
</html>
