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
        		<div class="layui-inline">
            		<label class="layui-form-label">小区名:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="communityName" lay-verify="required" autocomplete="off" class="layui-input"/>
            		</div>
            	</div>
            	<div class="layui-inline">
            		<label class="layui-form-label">省:</label>
            		<div class="layui-input-inline">
      					<select name="pName" lay-filter="pName">
      					</select>
    				</div>	
            	</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">市:</label>
            		<div class="layui-input-inline">
      					<select name="cName" lay-filter="cName">
      					</select>
    				</div>	
    			</div>
    			<div class="layui-inline">
    				<label class="layui-form-label">区/县:</label>
            		<div class="layui-input-inline">
      					<select name="dName" lay-filter="dName">
      					</select>
    				</div>		
    			</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">街道/乡镇:</label>
            		<div class="layui-input-inline">
      					<select name="sName" lay-filter="sName">
      					</select>
    				</div>
    			</div>
    			<div class="layui-inline">
    				<label class="layui-form-label">管理员账号:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="glyName" lay-verify="required" autocomplete="off" class="layui-input"/>
            		</div>	
    			</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">联系方式:</label>
            		<div class="layui-input-inline">
                		<input type="text" name="glyPhone" lay-verify="required" autocomplete="off" class="layui-input"/>
            		</div>		
    			</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            sendAjax(form,{level:1,parentId:''},'/community/getRegion','pName');
            form.on('select(pName)', function(data){
            	var dataAjax={level:2,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','cName');	
			});
			form.on('select(cName)', function(data){
            	var dataAjax={level:3,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','dName');	
			});
			form.on('select(dName)', function(data){
            	var dataAjax={level:4,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','sName');	
			});
            var submitForm =new submitObj({"type":"post","url":contextPath+"/community/saveCommunity","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
        function sendAjax(form,data,url,name){
        	$.ajax({
        		type:'post',
        		cache:false,
        		data:data,
        		url:contextPath+url,
        		success:function(data){
        			if(typeof data=="string"){
        				data=JSON.parse(data);
        			}
        			$("select[name='"+name+"']").html('<option value=""></option>');
        			var optHtml='';
        			$.each(data,function(index,list){
        				optHtml+="<option value="+list.id+">"+list.regionName+"</option>";
        			});
        			$("select[name='"+name+"']").append(optHtml);
        			form.render();
        		}
        	});
        };
	</script>
  </body>
</html>
