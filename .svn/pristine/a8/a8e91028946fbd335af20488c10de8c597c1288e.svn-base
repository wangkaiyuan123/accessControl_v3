<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
  	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
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
            	<label class="layui-form-label">小区名:</label>
            	<div class="layui-input-block">
                	<input type="text" name="communityName" lay-verify="required" autocomplete="off" class="layui-input"/>
                	<input type="hidden" value="" name="id"/>
            	</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">省:</label>
            	<div class="layui-input-block">
      				<select name="pName" lay-filter="pName">
      				</select>
    			</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">市:</label>
            	<div class="layui-input-block">
      				<select name="cName" lay-filter="cName">
      				</select>
    			</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">区/县:</label>
            	<div class="layui-input-block">
      				<select name="dName" lay-filter="dName">
      				</select>
    			</div>
        	</div>
        	<div class="layui-form-item">
            	<label class="layui-form-label">街道/乡镇:</label>
            	<div class="layui-input-block">
      				<select name="sName" lay-filter="sName">
      				</select>
    			</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
		var formData=JSON.parse($("#editJson",parent.document).val());
		$("input[name='communityName']").val(formData.communityName);
		$("input[name='id']").val(formData.id);
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            sendAjax(form,{level:1,parentId:''},'/community/getRegion','pName',true);  
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
            var submitForm =new submitObj({"type":"post","url":contextPath+"/community/updateCommunity","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
        function sendAjax(form,data,url,name,firstRender){
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
        			if(firstRender){
        				if(name=="pName"){
        					$("select[name='pName']").val(formData.provinceId);
        					sendAjax(form,{level:2,parentId:formData.provinceId},'/community/getRegion','cName',true);	
        				}else if(name=="cName"){
        					$("select[name='cName']").val(formData.cityId);
        					sendAjax(form,{level:3,parentId:formData.cityId},'/community/getRegion','dName',true);	
        				}else if(name=="dName"){
        					$("select[name='dName']").val(formData.districtId);
        					sendAjax(form,{level:4,parentId:formData.districtId},'/community/getRegion','sName',true);
        				}else if(name=="sName"){
        					$("select[name='sName']").val(formData.regionId);
        				}
        			}
        			form.render('select');
        		}
        	});
        };
	</script>
  </body>
</html>
