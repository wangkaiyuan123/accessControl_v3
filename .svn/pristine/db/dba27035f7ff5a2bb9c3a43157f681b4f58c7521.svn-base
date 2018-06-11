<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  	<jsp:include page="/inc.jsp"/>
  	<style>
  		img {
  			width:220px;
  			height:220px;
  			border:1px solid #ddd;
  		}
  	</style>
  </head>  
  <body>
   	<div>
		<form class="layui-form">
			<div class="layui-form-item">
            	<label class="layui-form-label">姓名:</label>
            	<div class="layui-input-block">
                	<input type="text" name="name" lay-verify="required" autocomplete="off" class="layui-input" readonly/>
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">身份证号:</label>
            	<div class="layui-input-block">
                	<input type="text" name="identifyId" lay-verify="required" autocomplete="off" class="layui-input" readonly/>
            	</div>
        	</div>
        	<div class="layui-form-item">
        		<label class="layui-form-label">审批:</label>
        		<div class="layui-input-block">
      				<input type="radio" name="check" value="1" title="通过" lay-filter="radioch">
      				<input type="radio" name="check" value="0" title="不通过" lay-filter="radioch">
    			</div>
    			<input type="hidden" name="id" />
      			<input type="hidden" name="state" />	
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">图像:</label>
            	<div class="layui-input-block" style="width:220px;height:220px;">
            		<img src="${pageContext.request.contextPath}/img/tibet-1.jpg" alt="无相关图片" id="imgShow"/>
            	</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
    </div>
   	<script type="text/javascript">
   		var formData=JSON.parse($("#editJson",parent.document).val());
   		$("input[name='name']").val(formData.name);
   		$("input[name='identifyId']").val(formData.identifyId);
   		$("#imgShow").attr("src",formData.imgUrl);
   		for(var key in formData){
   			if(key=="id"){
   				$("input[name='id']").val(formData['id']);
   			}else if(key=="state"){
   				$("input[name='state']").val(formData['state']);
   			}
   		}
   		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            var submitForm =new submitObj({"type":"post","url":contextPath+"/applyrecord/getRecordCheck","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
   	</script>
  </body>
</html>
