<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
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
        	<!--<div class="layui-form-item">
            	<div class="layui-inline">
            		<label class="layui-form-label">省:</label>
            		<div class="layui-input-inline">
      					<select name="pName" lay-filter="pName">
      					</select>
    				</div>	
            	</div>
            	<div class="layui-inline">
    				<label class="layui-form-label">市:</label>
            		<div class="layui-input-inline">
      					<select name="cName" lay-filter="cName">
      					</select>
    				</div>	
    			</div>
        	</div>
        	<div class="layui-form-item">
    			<div class="layui-inline">
    				<label class="layui-form-label">区/县:</label>
            		<div class="layui-input-inline">
      					<select name="dName" lay-filter="dName">
      					</select>
    				</div>		
    			</div>
    			<div class="layui-inline">
    				<label class="layui-form-label">街道/乡镇:</label>
            		<div class="layui-input-inline">
      					<select name="sName" lay-filter="sName">
      					</select>
    				</div>
    			</div>
        	</div>  -->
        	<div class="layui-form-item">
    			<!--<div class="layui-inline">
    				<label class="layui-form-label">小区:</label>
            		<div class="layui-input-inline">
      					<select name="xiaoqu" lay-filter="xiaoqu">
      					</select>
    				</div>		
    			</div>  -->
    			<div class="layui-inline">
    				<label class="layui-form-label">单元:</label>
            		<div class="layui-input-inline" style="width:190px;">
      					<select name="unitId" lay-filter="unitId">
      					</select>
    				</div>
    			</div>	
        	</div>
        	<input type="hidden" name="id"/>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        </form>
	</div>
	<script type="text/javascript">
		var formData=JSON.parse($("#editJson",parent.document).val());
		var id=formData.id;
		$("input[name='id']").val(id);
		layui.use('form', function() {
            var form = layui.form;
            var $ = layui.$;
            sendAjaxTwo(form);
            /*sendAjax(form,{level:1,parentId:''},'/community/getRegion','pName');
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
			form.on('select(sName)', function(data){
            	var dataAjax={level:5,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','xiaoqu');	
			});
			form.on('select(xiaoqu)', function(data){
            	var dataAjax={level:6,parentId:data.value};
  				sendAjax(form,dataAjax,'/community/getRegion','unitId');	
			});*/
            var submitForm =new submitObj({"type":"post","url":contextPath+"/person/saveAddUnits","refresh":true,"Isform":true});
			submitForm.submit(form);
        });
        /*function sendAjax(form,data,url,name){
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
        			form.render('select');
        		}
        	});
        };*/
        function sendAjaxTwo(form){
        	$.ajax({
        		type:'post',
        		cache:false,
        		data:{"regionId":formData.regionId},
        		url:contextPath+'/person/getCommunityUnits',
        		dataType:'json',
        		success:function(data){
        			$("select[name='unitId']").html('<option value=""></option>');
        			var optHtml='';
        			$.each(data,function(index,list){
        				optHtml+="<option value="+list.id+">"+list.unitName+"</option>";
        			});
        			$("select[name='unitId']").append(optHtml);
        			form.render('select');	
        		}
        	});
        }
	</script>
  </body>
</html>
