<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
 	</head>
  	<body>
  		 <div class="iframeTitle">
        	<p class="lf"><i class="layui-icon" style="">&#xe68e;</i><b></b></p>
        	<div class="rt">
            	<a class="layui-btn layui-btn-normal layui-btn-small add" lay-event="del"><i class="layui-icon">&#xe654;</i>新增</a>
        	</div>
    	</div>
    	<div class="iframeContent">
    		<div class="outDiv">
        		<div class="fixedTableHead_container">
            		<table id="treetable" class="treetable">
                		<thead>
                    		<tr>
                        		<td width="100px" style="border-left:0">序号</td>
                        		<td width="200px">资源名称</td>
                        		<td width="200px">是否菜单</td>
                        		<td width="300px">链接地址</td>
                        		<td width="400px">操作</td>
                    		</tr>
                		</thead>
                		<tbody>
                		</tbody>
            		</table>
        		</div>
    		</div>
    	</div>
    	<script type="text/javascript">
    		function sendAjax(){
    			changeTitle();
    			$.ajax({
            		type:'get',
            		cache:false,
            		url:contextPath+'/sysResource/getAllResources',
            		success:function(data){
                    	if(typeof data=="string"){
                    		data=JSON.parse(data);
                    	}
                    	loopHtml(data);
                    	$("#treetable tbody").html(tbodyHtml);
                    	$("#treetable").treetable({ expandable: true});
                    	//$(".treetable").treetable()//{ expandable: true }	
                    	$(window).resize(function(){
    						containerHeight("body",".iframeContent",70);	
    					});
    					containerHeight("body",".iframeContent",70);
            		}
        		});
    		}
    		var tbodyHtml='';
    		function loopHtml(data){
    			$.each(data,function(index,list){
                    var listStr=JSON.stringify(list);
                    if(!list.isMenu){
                    	list.isMenu="否";	
                    }else{
                    	list.isMenu="是";
                    }
					tbodyHtml += "<tr data-tt-id='"+list.id+"' data-tt-parent-id='"+list.parentId+"'>"+
						"<td width='100px' style='text-align:left;border-left:0;'>"+(index+1)+"</td>"+
						"<td width='200px'>"+list.resourceName+"</td>"+
						"<td width='200px'>"+list.isMenu+"</td>"+
						"<td width='300px'>"+list.resourceUrl+"</td>"+
						"<td style='text-align:center;' width='400px'>"+
							"<a class='edit layui-btn layui-btn-mini layui-btn-warm' data-id="+listStr+"><i class='layui-icon'>&#xe642;</i>编辑</a>"+
							"<a class='del layui-btn layui-btn-danger layui-btn-mini' data-id="+list.id+"><i class='layui-icon'>&#xe640;</i>删除</a>"+
                        "</td>"+
                    "</tr>";
                    if(list.hasOwnProperty("children")){
						loopHtml(list.children);	
					}
               });		
    		}
    		sendAjax();
    		//新增：
    		$(".add").click(function(){
    			$(this).createModal({"title":"新增","modalHeight":400,"modalWidth":750,"url":contextPath+"/sysResource/add"});
    		});
    		//修改：
    		$(".treetable tbody").on("click",".edit",function(){
    			$(this).parent().parent().addClass("layui-table-click").siblings().removeClass("layui-table-click");
    			var data=$(this).attr("data-id");
    			$("#editJson",parent.document).val(data);
    			$(this).createModal({"title":"修改","modalHeight":400,"modalWidth":750,"url":contextPath+"/sysResource/update"});
    		})
    		//删除：
    		$(".treetable tbody").on("click",".del",function(){
    			$(this).parent().parent().addClass("layui-table-click").siblings().removeClass("layui-table-click");
    			var data=$(this).attr("data-id");
    			$(this).createModal({"title":"删除","modalHeight":150,"modalWidth":330,"url":contextPath+"/sysResource/delete","data":data});
    		});
    	</script>
  	</body>
</html>
