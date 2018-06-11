<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
  		<jsp:include page="/inc.jsp"/>
  		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/Ztree/css/metroStyle/metroStyle.css"/>
  		<script type="text/javascript" src="${pageContext.request.contextPath}/framework/Ztree/js/jquery.ztree.core.min.js"></script>
    	<script type="text/javascript" src="${pageContext.request.contextPath}/framework/Ztree/js/jquery.ztree.excheck.min.js"></script>
    	<style>
    		#resTree {
    			position:absolute;
    			width:178px;
    			height:150px;
    			overflow:auto;
    			z-index:1000;
    			border:1px solid #ddd;
    			border-top:0;
    			position:absolute;
    			background:#fff;
    			display:none;
    		}
    		#supRes:hover {
    			cursor:pointer;
    		}
    	</style>
  	</head> 
  	<body>
		<div>
		<form class="layui-form">
        	<div class="layui-form-item">
        		<div class="layui-inline">
        			<label class="layui-form-label">上级资源:</label>
        			<div class="layui-input-inline">
        				<input type="text" name="parent" autocomplete="off" class="layui-input" readonly id="supRes"/>
        				<input type="hidden" name="parentId" value=""/>
        				<i class="layui-icon" style="position:absolute;top:15px;right:8px;">&#xe625;</i>
        				<ul id="resTree" class="ztree"></ul>	
        			</div>
        		</div>
        		<div class="layui-inline">
        			<label class="layui-form-label">
        				<button class="layui-btn layui-btn-danger" type="button" id="reset">清空选择</button>	
        			</label>
        		</div>	
        	</div>
        	<div class="layui-form-item">
        		<div class="layui-inline">
        			<label class="layui-form-label">资源名称</label>
        			<div class="layui-input-inline">
        				<input type="text" name="resourceName" autocomplete="off" class="layui-input" lay-verify="required"/>	
        			</div>
        		</div>
        		<div class="layui-inline">
        			<label class="layui-form-label">链接地址</label>
        			<div class="layui-input-inline">
        				<input type="text" name="resourceUrl" autocomplete="off" class="layui-input"/>	
        			</div>
        		</div>
        	</div>
        	<div class="layui-form-item">
        		<div class="layui-inline">
        			<label class="layui-form-label">排序</label>
        			<div class="layui-input-inline">
        				<input type="text" name="sort" autocomplete="off" class="layui-input" lay-verify="required"/>	
        			</div>
        		</div>
        		<div class="layui-inline">
        			<label class="layui-form-label">图标</label>	
        			<div class="layui-input-inline" style="width:140px;">
        				<input type="text" name="iconCls" autocomplete="off" class="layui-input" readonly id="iconCls"/>
        				
        			</div>
        		</div>
        		<div class="layui-inline">
        			<button class="layui-btn layui-btn-danger" type="button" id="resetIcon">清空图标</button>		
        		</div>
        	</div>
        	<div class="layui-form-item">
        		<div class="layui-inline">
        			<label class="layui-form-label">是否依赖</label>
        			<div class="layui-input-inline">
        				<input type="radio" name="isRequired" value="0" title="否"/>
        				<input type="radio" name="isRequired" value="1" title="是"/>	
        			</div>
        		</div>
        		<div class="layui-inline">
        			<label class="layui-form-label">是否菜单</label>
        			<div class="layui-input-inline">
        				<input type="radio" name="isMenu" value="0" title="否"/>
        				<input type="radio" name="isMenu" value="1" title="是"/>	
        			</div>
        		</div>
        	</div>
        	<i lay-submit="" lay-filter="submit" class="iSubmit"></i>
        	<input type="hidden" name="id"/>
        </form>
       <script type="text/javascript">
			var formData=JSON.parse($("#editJson",parent.document).val());
			$("input[name='resourceName']").val(formData.resourceName);
			$("input[name='resourceUrl']").val(formData.resourceUrl);
			$("input[name='sort']").val(formData.sort);
			$("input[name='iconCls']").val(formData.iconCls);
			$("input[name='id']").val(formData.id);
			$("#supRes").val(formData.parentName);
            $("input[name='parentId']").val(formData.parentId);
			if(formData.isRequired){
				$("input[name='isRequired']").eq(1).prop("checked",true);	
			}else{
				$("input[name='isRequired']").eq(0).prop("checked",true);
			}
			if(formData.isMenu){
				$("input[name='isMenu']").eq(1).prop("checked",true);	
			}else{
				$("input[name='isMenu']").eq(0).prop("checked",true);
			}
       		layui.use('form', function() {
            	var form = layui.form;
            	var $ = layui.$;
            	$("#supRes").click(function(){
            		$("#resTree").toggle();	
            	});
				$("#reset").click(function(){
            		$("#supRes").val("");
            		$("#supRes").attr("data-id",""); 
            		$("#resTree").css("display","none");	 	
            	});
      			loadTree();
      			var submitForm =new submitObj({"type":"post","url":contextPath+"/sysResource/updateRecourse","refresh":true,"Isform":true});
				submitForm.submit(form);
            });
            function loadTree(){
            	$.ajax({
            		type:'get',
            		url:contextPath+"/sysResource/getAllResources",
            		cache:false,
            		success:function(data){
            			if(typeof data=="string"){
            				data=JSON.parse(data);
            			}
            			var setting = {
           					data:{
               					key:{
                   					name:"resourceName"
               					}
           					},
           					callback:{
               					onClick:onclick
           					}
      					};
      					 function onclick(e, treeId, treeNode){
           					var zTree = $.fn.zTree.getZTreeObj("resTree");
           					nodes = zTree.getSelectedNodes();
           					v = "";
          					v=nodes[0].resourceName;
           					$("#supRes").val(v);
           					$("input[name='parentId']").val(nodes[0].id);
       					}
            			$.fn.zTree.init($("#resTree"), setting, data);
            		}
            	})
            }
           //图标：
           $("#iconCls").click(function(){
           		$(this).createModal({
					"title":"选择图标",
					"modalHeight":400,
					"modalWidth":650,
					"url":contextPath+"/sysResource/showIconCls",
					"sec":true,
					"closed":function(){
						var icon = $("#iconClass",parent.document).val();
						$("#iconCls").val(icon);	
					}
				});
           });
            $("#resetIcon").click(function(){
           		$("#iconCls").val("");	
           		$("#iconClass",parent.document).val("");
           });
       </script>	             
  	</body>
</html>
