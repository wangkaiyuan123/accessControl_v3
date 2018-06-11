<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
		<style>
			.layui-form-label {
				width:150px;
			}
		</style>
 	</head>
  	<body>
  		 <div class="iframeTitle">
        	<p class="lf"><i class="layui-icon" style="">&#xe68e;</i><b></b></p>
    	</div>
    	<div class="iframeContent">
    		<table class="layui-table" id="tableList" lay-filter="tableList">
    		</table>
    	</div>
    	<script type="text/javascript">
    		changeTitle();
    		layui.use('table', function() {
            	var table = layui.table;
            	var tableIns= table.render({
                	elem:'#tableList',
                	limits: [10,20,50,100],
					limit:10,
                	url:contextPath+'/operaterecord/getList',
                	request: {
                		limitName: 'rows'
                	},
                	response: {
                		dataName: 'date'
                	},
                	cols:[[
                		{field:'left',width:100,align:'center',title:'序号'},
                    	{field:'operateName',width:150,align:'center',title:'操作名称'},
                    	{field:'operationResult',width:100,align:'center',title:'操作结果'},
                    	{field:'operatorName',width:150,align:'center',title:'操作人'},
                    	{field:'operateTime',width:200,align:'center',title:'操作时间'},
                    	{field:'content',width:500,align:'center',title:'内容'}
                	]],
                	page:true,
                	height:'full-95',
                	id:'tableList',
                	even:true,
                	done:function(res,curr,count){
                    	//调用排序函数
                    	sortTable(curr,count,0);
                	}
               });
            });
    	</script>
  </body>
</html>
