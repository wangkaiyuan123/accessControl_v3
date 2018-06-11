<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
		<style>
			.layui-table,.layui-table-view {
				margin:0;
			}
		</style>
 	</head>
  	<body>
  		 <div class="iframeTitle">
        	<p class="lf"><i class="layui-icon" style="">&#xe68e;</i><b></b></p>
        	<div class="rt">
            	<a class="layui-btn layui-btn-normal layui-btn-small add" lay-event="del"><i class="layui-icon">&#xe654;</i>新增</a>
        	</div>
    	</div>
    	<div class="iframeContent">
    		 <div class="search">
            	<div class="search-title"><span>搜索栏</span></div>
            	<form class="layui-form">
			 		<div class="layui-form-item">
    					<div class="layui-inline">
      						<label class="layui-form-label">身份证:</label>
      						<div class="layui-input-inline">
        						<input type="text" name="identifyId" autocomplete="off" class="layui-input">
      						</div>
    					</div>
    					<div class="layui-inline">
      						<label class="layui-form-label">电话:</label>
      						<div class="layui-input-inline">
        						<input type="text" name="telephone" autocomplete="off" class="layui-input">
      						</div>
    					</div>
    					<div class="layui-inline">
    						<button class="layui-btn searchBtn" type="button" lay-filter="search">查询</button>
    					</div>
  					</div>
  				</form>
        	</div>
        	<div class="searchRes">
        		<div class='treeContainer lf'>
        			<p>机构列表</p>
        			<ul id="organTree"></ul>
        		</div>
        		<div class="tableContainer">
        			<table class="layui-table" id="tableList" lay-filter="tableList">
    				</table>
        		</div>
        		<div class="clear"></div>
        	</div>
    	</div>
    	<input type="hidden" id="levelInput" value=""/>
    	<script type="text/html" id="barDemo">
			{{# if ($("#levelInput").val() > 5){}}
        		<a class="edit layui-btn layui-btn-mini layui-btn-warm" lay-event="edit"><i class="layui-icon">&#xe642;</i>编辑</a>
        		<a class="del layui-btn layui-btn-danger layui-btn-mini" lay-event="del"><i class="layui-icon">&#xe640;</i>删除</a>
				<a class="del layui-btn  layui-btn-mini" lay-event="unit"><i class="layui-icon">&#xe614;</i>绑定单元</a>
				<a class="del layui-btn  layui-btn-mini layui-btn-primary" lay-event="up"><i class="layui-icon">&#xe619;</i>升级为管理员</a>
			{{# }else { }}
				<a href="javascript:;"/>无相关操作</a>
			{{# } }}
    	</script>
    	<script type="text/javascript">
    		changeTitle();
    		//设置下部分高度：
    		containerHeight("body",".iframeContent",70);
    		containerHeight(".iframeContent",".searchRes",120);
    		containerHeight(".tableContainer","#organTree",30);	
    		$(window).resize(function(){
    			containerHeight("body",".iframeContent",70);
    			containerHeight(".iframeContent",".searchRes",120);
    			containerHeight(".tableContainer","#organTree",32);	
    		});
    		//organTree
    		$.ajax({
    			type:'post',
    			cache:false,
    			url:contextPath+'/person/getTrees',
    			dataType:'json',
    			success:function(data){
    				var firstId=data[0].id;
    				if(typeof data=="string"){
    					data=JSON.parse(data);
    				}
    				//递归调用查找regionName全部替换成name
    				function changeName(list){
    					for(var i=0;i<list.length;i++){
    						list[i].spread=true;
    						if(list[i].hasOwnProperty("regionName")){
    							list[i].name=list[i].regionName;
    						}
    						if(list[i].hasOwnProperty("children")){
    							changeName(list[i].children);
    						}
    					}
    				}
    				changeName(data);	
    				layui.use('tree', function(){
    					var $=layui.$;
    					layui.tree({
    						elem:'#organTree',
    						nodes:data,
    						click: function(node){
    							renderTable(node.id);
    							//console.log(node);
    							$("#levelInput").val(node.level);
  							}  
    					});
    					$("#organTree a").click(function(){
    						$("#organTree a").not(this).css("color","#333");
    						$(this).css("color","#009688");					
    					}); 
    					$("#organTree a:eq(0)").css("color","#009688").addClass("aColor");
    				});
    				renderTable(firstId);
    				$("#levelInput").val(data[0].level);
    			}
    		});
    		function renderTable(id){
    			layui.use('table', function() {
            		var table = layui.table;
            		var tableIns= table.render({
                		elem:'#tableList',
                		limits: [10,20,50,100],
						limit: 10,
                		url:contextPath+'/person/getPersonList',
                		where:{'regionId':id},
                		request: {
                			limitName: 'rows'
                		},
                		cols:[[
                			{field:'left', width:90,align:'center',title:'序号'},
                    		{field:'name', width:90,align:'center',title:'姓名'},
                    		{field:'unitName', width:200,align:'center',title:'单元号'},
                    		{field:'telephone', width:150,align:'center',title:'手机号'},
                    		{field:'cardId', width:150,align:'center',title:'门卡'},
                    		{field:'personType', width:100,align:'center',title:'用户类型'},
                    		{field:'state', width:100,align:'center',title:'用户状态'},
                    		{field:'right', width:400, align:'center', toolbar: '#barDemo',title:'操作'}
                		]],
                		page:true,
                		height:'full-190',
                		id:'tableList',
                		even:true,
                		done:function(res,curr,count){
                    		//调用排序函数
                    		sortTable(curr,count,0);
                    		//修改用户状态：
                    		changeState();
                		}
               		});
               	$(".searchBtn").click(function(){
               			tableIns.reload({
  							where: {
    							identifyId: $("input[name='identifyId']").val(),
    							telephone: $("input[name='telephone']").val()
  							}
						});
               	});
               	table.on('tool(tableList)', function(obj){
  						var data = obj.data; //获得当前行数据
  						var layEvent = obj.event; //获得 lay-event 对应的值
  						var listStr=JSON.stringify(data);
  						$("#editJson",parent.document).val(listStr);
  						if(layEvent === 'edit'){
  							$(this).createModal({
  								"title":"修改",
  								"modalHeight":450,
  								"modalWidth":720,
  								"url":contextPath+"/person/update",
  								"closed":function(){
            						$("#radioWeiXin",parent.document).val("");
            						$("#radioWeiXin",parent.document).attr("data-name","");
            						$("#radioWeiXin",parent.document).attr("data-number","");
            					}
  							});	
  						}else if(layEvent === 'del'){
  							$(this).createModal({"title":"删除","modalHeight":150,"modalWidth":330,"url":contextPath+"/person/delete"});
  						}else if(layEvent === 'unit'){
  							$(this).createModal({"title":"绑定单元","modalHeight":300,"modalWidth":400,"url":contextPath+"/person/addUnits"});
  						}else if(layEvent === 'up'){
  							$(this).createModal({"title":"提示","modalHeight":150,"modalWidth":330,"url":contextPath+"/person/updateForGly"});
  						}
  					});
  					
            	});
    		}
            //新增：
            $(".add").click(function(){
            	$(this).createModal({
            		"title":"新增",
            		"modalHeight":480,
            		"modalWidth":720,
            		"url":contextPath+"/person/add",
            		"closed":function(){
            			$("#radioWeiXin",parent.document).val("");
            			$("#radioWeiXin",parent.document).attr("data-name","");
            			$("#radioWeiXin",parent.document).attr("data-number","");
            		}
            	});//720,450
            });
            //修改用户状态：
            function changeState(){
            	var len=$(".layui-table-body tbody>tr").length;	
            	for(var i=0;i<len;i++){
            		var tdHtml=$(".layui-table-body tbody>tr").eq(i).children("td:eq(6)").children("div");
            		var tdUnit=$(".layui-table-body tbody>tr").eq(i).children("td:eq(2)").children("div").html();
            		$(".layui-table-body tbody>tr").eq(i).children("td:eq(2)").attr("title",tdUnit);
            		if(tdHtml.html()==1){
            			tdHtml.html("正常");
            		}else if(tdHtml.html()==2){
            			tdHtml.html("审核中");
            		}else if(tdHtml.html()==3){
            			tdHtml.html("冻结");
            		}
            	}
            }
    	</script>
  </body>
</html>
