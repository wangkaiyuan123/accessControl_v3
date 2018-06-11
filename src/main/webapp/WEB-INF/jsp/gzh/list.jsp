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
    		 <table class="layui-table" id="tableList" lay-filter="tableList">
    		 </table>
    	</div>
    	<script type="text/html" id="barDemo">
        	<a class="edit layui-btn layui-btn-mini layui-btn-warm" lay-event="edit"><i class="layui-icon">&#xe642;</i>修改</a>
        	<a class="del layui-btn layui-btn-danger layui-btn-mini" lay-event="del"><i class="layui-icon">&#xe640;</i>删除</a>
    	</script>
    	<script type="text/javascript">
    		changeTitle();
    		layui.use('table', function() {
            	var table = layui.table;
            	var tableIns= table.render({
                	elem:'#tableList',
                	limits: [10,20,50,100],
					limit: 10,
                	url:contextPath+'/gzh/getList',
                	request: {
                		limitName: 'rows'
                	},
                	cols:[[
                		{field:'left', width:100,align:'center',title:'序号'},
                    	{field:'weixinkey', width:200,align:'center',title:'关键字'},
                    	{field:'value', width:500,align:'center',title:'值'},
                    	{field:'name', width:300,align:'center',title:'名称'},
                    	{field:'right', width:300, align:'center', toolbar: '#barDemo',title:'操作'}
                	]],
                	page:true,
                	height:'full-100',
                	id:'tableList',
                	even:true,
                	done:function(res,curr,count){
                    	//调用排序函数
                    	sortTable(curr,count,0);	
                	}
               });
               table.on('tool(tableList)', function(obj){
  					var data = obj.data; //获得当前行数据
  					var layEvent = obj.event; //获得 lay-event 对应的值
  					var listStr=JSON.stringify(data);
  					$("#editJson",parent.document).val(listStr);
  					if(layEvent === 'edit'){
  						$(this).createModal({"title":"修改","modalHeight":320,"modalWidth":520,"url":contextPath+"/gzh/update"});	
  					}else if(layEvent === 'del'){
  						$(this).createModal({"title":"删除","modalHeight":150,"modalWidth":330,"url":contextPath+"/gzh/delete"});
  					}
  				});
  					
            });
            //新增：
            $(".add").click(function(){
            	$(this).createModal({"title":"新增","modalHeight":320,"modalWidth":520,"url":contextPath+"/gzh/add"});
            });
    	</script>
  </body>
</html>
