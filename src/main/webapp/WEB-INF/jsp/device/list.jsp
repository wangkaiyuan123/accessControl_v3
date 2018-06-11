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
        		<a class="layui-btn layui-btn-small" data-type="getCheckDataReset"><i class="layui-icon">&#xe631;</i>批量清空卡号</a>
            	<a class="layui-btn layui-btn-danger layui-btn-small" data-type="getCheckDataDelete"><i class="layui-icon">&#xe640;</i>批量删除</a>
            	<a class="layui-btn layui-btn-danger layui-btn-small" data-type="getCheckDataRestart"><i class="layui-icon">&#xe640;</i>批量重启</a>
            	<a class="layui-btn layui-btn-normal layui-btn-small add" ><i class="layui-icon">&#xe654;</i>新增</a>
        	</div>
    	</div>
    	<div class="iframeContent">
    		 <table class="layui-table" id="tableList" lay-filter="tableList">
    		 </table>
    	</div>
    	<script type="text/html" id="barDemo">
        	<a class="edit layui-btn layui-btn-mini layui-btn-warm" lay-event="edit"><i class="layui-icon">&#xe642;</i>变更模式</a>
        	<a class="del layui-btn layui-btn-danger layui-btn-mini" lay-event="del"><i class="layui-icon">&#xe640;</i>删除</a>
			<a class="del layui-btn layui-btn-mini" lay-event="reset"><i class="layui-icon">&#xe631;</i>清空卡号</a>
    	</script>
    	<script type="text/javascript">
    		changeTitle();
    		layui.use('table', function() {
            	var table = layui.table;
            	var tableIns= table.render({
                	elem:'#tableList',
                	limits: [10,20,50,100],
					limit: 10,
                	url:contextPath+'/device/getDeviceList',
                	request: {
                		limitName: 'rows'
                	},
                	cols:[[
                		{field:'select',checkbox:true,width:100,align:'center',title:'选择'},
                		{field:'left',width:100,align:'center',title:'序号'},
                    	//{field:'deviceNo', width:200,align:'center',title:'设备号'},
                    	{field:'deviceMac',width:200,align:'center',title:'mac地址'},
                    	{field:'deviceAddress', width:300,align:'center',title:'地址'},
                    	{field:'authotity', width:150,align:'center',title:'模式'},
                    	{field:'addPerson', width:200,align:'center',title:'设备添加人'},
                    	{field:'online', width:150,align:'center',title:'设备状态'},
                    	{field:'right', width:300, align:'center', toolbar: '#barDemo',fixed:'right',title:'操作'}
                	]],
                	page:true,
                	height:'full-100',
                	id:'tableList',
                	even:true,
                	done:function(res,curr,count){
                    	//调用排序函数
                    	sortTable(curr,count,1);	
                    	//改变视图显示：
                    	changeView();
                	}
               });
               table.on('tool(tableList)', function(obj){
  					var data = obj.data; //获得当前行数据
  					var layEvent = obj.event; //获得 lay-event 对应的值
  					var listStr=JSON.stringify(data);
  					$("#editJson",parent.document).val(listStr);
  					if(layEvent === 'edit'){
  						$(this).createModal({"title":"变更模式","modalHeight":220,"modalWidth":450,"url":contextPath+"/device/change"});	
  					}else if(layEvent === 'del'){
  						$(this).createModal({"title":"删除","modalHeight":150,"modalWidth":330,"url":contextPath+"/device/delete"});
  					}else if(layEvent === 'reset'){
  						$(this).createModal({"title":"清空卡号","modalHeight":150,"modalWidth":330,"url":contextPath+"/device/clear"});
  					}
  				});
  				var active ={
  					getCheckDataReset: function(){
  						var checkStatus = table.checkStatus('tableList'),
      					data = checkStatus.data;
      					var listStr=JSON.stringify(data);
  						$("#editJson",parent.document).val(listStr);
      					if(!data.length){
      						layer.msg('请选择要操作的行');
      					}else{
      						$(this).createModal({"title":"清空卡号","modalHeight":150,"modalWidth":330,"url":contextPath+"/device/clearBatch"});	
      					}
  					},
  					getCheckDataDelete:function(){
  						var checkStatus = table.checkStatus('tableList'),
      					data = checkStatus.data;
      					var listStr=JSON.stringify(data);
  						$("#editJson",parent.document).val(listStr);
      					if(!data.length){
      						layer.msg('请选择要操作的行');
      					}else{
      						$(this).createModal({"title":"删除","modalHeight":150,"modalWidth":330,"url":contextPath+"/device/deleteBatch"});	
      					}
  					},
  					getCheckDataRestart:function(){
  						var checkStatus = table.checkStatus('tableList'),
      					data = checkStatus.data;
      					var listStr=JSON.stringify(data);
  						$("#editJson",parent.document).val(listStr);
      					if(!data.length){
      						layer.msg('请选择要操作的行');
      					}else{
      						$(this).createModal({"title":"重启设备","modalHeight":150,"modalWidth":330,"url":contextPath+"/device/restartBatch"});	
      					}
  					}
  					
  				}
  				$('.layui-btn').on('click', function(){
    				var type = $(this).data('type');
    				active[type] ? active[type].call(this) : '';
  				});
  				/*table.on('checkbox(tableList)', function(obj){
   	 				//console.log(obj);
  				});	*/	
            });
            //新增：
            $(".add").click(function(){
            	$(this).createModal({"title":"新增","modalHeight":350,"modalWidth":520,"url":contextPath+"/device/add"});
            });
            //改变视图显示：
            function changeView(){
            	var len=$(".layui-table-body tbody>tr").length;	
            	for(var i=0;i<len;i++){
            		var tdHtml=$(".layui-table-body tbody>tr").eq(i).children("td:eq(4)").children("div");
            		if(tdHtml.html()==0){
            			tdHtml.html("非授权模式");
            		}else if(tdHtml.html()==1){
            			tdHtml.html("授权模式");
            		}
            	}
            }
    	</script>
  </body>
</html>
