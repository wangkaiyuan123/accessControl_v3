<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/inc.jsp"/>
		<style>
			.radioWeinXin:hover {
				cursor:pointer;
			}
			.weixinIcon {
				color:#5FB878;
			}
			.layui-form-item {
				margin:0;
			}
		</style>
 	</head>
  	<body>
    	<div class="iframeContent">
    		 <div class="search">
            	<div class="search-title"><span>搜索栏</span></div>
            	<form class="layui-form">
			 		<div class="layui-form-item">
    					<div class="layui-inline">
      						<label class="layui-form-label" style="width:150px;">关键字(昵称/账号):</label>
      						<div class="layui-input-inline">
        						<input type="text" name="account" autocomplete="off" class="layui-input">
      						</div>
    					</div>
    					<div class="layui-inline">
    						<button class="layui-btn searchBtn" type="button" lay-filter="search">查询</button>
    					</div>
  					</div>
  				</form>
        	</div>
    		 <table class="layui-table" id="tableList" lay-filter="tableList">
    		 </table>
    	</div>
    	<script type="text/javascript">
    		var openId='${param.opendId}';
    		layui.use(['table','form'], function() {
            	var table = layui.table;
            	var tableIns= table.render({
                	elem:'#tableList',
                	limits: [10,20,50,100],
					limit: 10,
                	url:contextPath+'/person/getNoWeiXinUser',
                	request: {
                		limitName: 'rows'
                	},
                	cols:[[
                		{field:'radio',width:91,align:'center',title:'选择'},//91
                		{field:'left',width:91,align:'center',title:'序号'},//91
                    	{field:'nickname',width:200,align:'center',title:'昵称'},//200
                    	{field:'account',width:100,align:'center',title:'账号'},//200
                	]],
                	page:true,
                	height:'full-135',
                	id:'tableList',
                	even:true,
                	done:function(res,curr,count){
                    	//调用排序函数
                    	sortTable(curr,count,1);
                    	//单选框
                    	changeRadio(res);
                	}
               });
                $(".searchBtn").click(function(){
               		tableIns.reload({
  						where: {
    						account: $("input[name='account']").val()
    					}
					});
               });
           });
           function changeRadio(res){
           		//寻找openid对应的account(修改使用);
           		var editAccount="";
           		if(openId!=""){
           			for(var j=0;j<res.data.length;j++){
           				if(res.data[j].openid==openId){
           					editAccount=res.data[j].account;
           					$("#radioWeiXin",parent.document).val(openId);
           					$("#radioWeiXin",parent.document).attr("data-name",res.data[j].nickname);
           					$("#radioWeiXin",parent.document).attr("data-number",res.data[j].account);
           					break;
           				}		
           			}
           		}
           		//end
           		var len=$(".layui-table-body tr").length;
           		var tr=$(".layui-table-body tr");
           		var tdHtml='<div class="radioWeinXin">'+
      							'<input type="radio" name="radioWeinXin">'+
      							'<i class="layui-icon weixinIcon" style="font-size:20px;">&#xe63f;</i>'+	
    						'</div>';
    			var tdHtmlChecked='<div class="radioWeinXin">'+
      							'<input type="radio" name="radioWeinXin" checked>'+
      							'<i class="layui-icon weixinIcon" style="font-size:20px;">&#xe643;</i>'+	
    						'</div>';
    			//填充单选框的值：
    			var weixinNum=$("#radioWeiXin",parent.document).attr("data-number");
           		for(var i =0;i<len;i++){
           			var num=tr.eq(i).children("td:eq(3)").children("div").html();
           			tr.eq(i).children("td:eq(0)").children("div").html(tdHtml);	
           			if((weixinNum==num ||editAccount==num) && num){
           				tr.eq(i).children("td:eq(0)").children("div").html(tdHtmlChecked);	
           			}else{
           				tr.eq(i).children("td:eq(0)").children("div").html(tdHtml);	
           			}		
           		}
           		//点击事件：
           		$(".layui-table-body").on("click",".radioWeinXin",function(){
           			//其余的设置成未选中：
           			$(".radioWeinXin").not(this).children("input").prop("checked",false).end().children("i").html("&#xe63f;");
           			if(!$(this).children("input").prop("checked")){
           				$(this).children("i").html("&#xe643;");
           				var trIndex=$(this).parent().parent().parent().index();
           				var weixinId=res.data[trIndex].openid;
           				var weixinNum=res.data[trIndex].account;
           				var weixinName=$(this).parent().parent().parent().children("td:eq(2)").children("div").html();
           				$("#radioWeiXin",parent.document).val(weixinId);
           				$("#radioWeiXin",parent.document).attr("data-name",weixinName);
           				$("#radioWeiXin",parent.document).attr("data-number",weixinNum);
           			}else{
           				//$(this).children("i").html("&#xe63f;");	
           			}	
           		});
           }
    	</script>
  </body>
</html>
