<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
	<title>新增绑定单元</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/lib/weui.min.css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/css/jquery-weui.min.css"/> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/framework/jquery-weui/dist/js/jquery-weui.min.js"></script> 
		<script type="text/javascript">
			var citydata = ${regionList};
			function aaa(){
				//$(".regionSelect").empty();
				$("#jiedao").val("");
	        	$("#jiedao").attr("data-values","");
				$("#xiaoqu").val("");
	        	$("#xiaoqu").attr("data-values","");
	        	$("#danyuan").val("");
	        	$("#danyuan").attr("data-values","");
				$("#jiedao").select("update",{
	        			title: "选择街道",
  						items: []
	        			});
	        	$("#xiaoqu").select("update",{
	        			title: "选择小区",
  						items: []
	        			});
	        	$("#danyuan").select("update",{
	        			title: "选择单元",
  						items: []
	        			});
				var personLocationIds = $("#city-picker").attr("data-codes");
  				var personLocationVal = $("#city-picker").val();
  				//console.log(personLocationIds);
  				//console.log(personLocationVal);
  				if(personLocationVal.indexOf("--")>=0||typeof(personLocationVal)==undefined||personLocationVal==''){
  					
  				}else{
  					//根据所在地区 获取街道
  					$.ajax({
		    			type:'post',
		    			url:"${pageContext.request.contextPath}/weixin/getStreetByLocation",
		    			data : {
		    				"personLocationIds" : personLocationIds
		    			},
		    			dataType : "json",
		    			success:function(obj){
		    				if(typeof obj=="string"){
	        						obj=JSON.parse(obj);
	        					}
	        					//console.log(obj);
	        					if(obj.success!=undefined&&obj.success){
	        						//console.log(obj.regionList);
	        						$("#jiedao").val("");
	        						$("#jiedao").attr("data-values","");
	        						$("#xiaoqu").val("");
	        						$("#xiaoqu").attr("data-values","");
	        						$("#danyuan").val("");
	        						$("#danyuan").attr("data-values","");
	        						var street = "";
	        						var xiaoqu = "";
	        						var danyuan = "";
	        						var list = obj.regionList;
	        						var arr=[];
	        						for(var i=0;i<list.length;i++){
	        							//street+='<option value="'+list[i].id+'">';
	        							//street+=list[i].regionName;
	        							//street+='</option>';
	        							arr[i]={};
	        							arr[i].title=list[i].regionName;
	        							arr[i].value=list[i].id;
	        						}
	        						console.log(arr);
	        						$("#jiedao").select("update",{
	        							title: "选择街道",
  										items: arr
	        						});
	        						//end
	        						if(list[0].sub&&list[0].sub.length>0){
	        							var xiaoqulist = list[0].sub;
	        							/*for(var j=0;j<xiaoqulist.length;j++){
	        								xiaoqu+='<option value="'+xiaoqulist[j].id+'">';
	        								xiaoqu+=xiaoqulist[j].regionName;
	        								xiaoqu+='</option>';
	        							}*/
	        							//11.14
	        							var arr2=[];
	        							for(var i=0;i<xiaoqulist.length;i++){
	        								arr2[i]={};
	        								arr2[i].title=xiaoqulist[i].regionName;
	        								arr2[i].value=xiaoqulist[i].id;
	        							}
	        							console.log(arr2);
	        							$("#xiaoqu").select("update",{
	        								title: "选择小区",
  											items: arr2
	        							});
	        							//end
	        							if(xiaoqulist[0].sub&&xiaoqulist[0].sub.length>0){
	        							var danyuanlist = xiaoqulist[0].sub;
	        							/*for(var k=0;k<danyuanlist.length;k++){
	        								danyuan+='<option value="'+danyuanlist[k].id+'">';
	        								danyuan+=danyuanlist[k].name;
	        								danyuan+='</option>';
	        							}*/
	        							//11.14
	        							var arr3=[];
	        							for(var i=0;i<danyuanlist.length;i++){
	        								arr3[i]={};
	        								arr3[i].title=danyuanlist[i].name;
	        								arr3[i].value=danyuanlist[i].id;
	        							}
	        							console.log(arr3);
	        							$("#danyuan").select("update",{
	        								title: "选择单元",
  											items: arr3
	        							});
	        							//end
	        						}
	        						}
	        						//$("#jiedao").append(street);
	        						//$("#xiaoqu").append(xiaoqu);
	        						//$("#danyuan").append(danyuan);
	        					}else{
	        						
	        					}
		    			}
    				});
  				}
			}
			function changeStreet(){
			console.log(2);
			$("#xiaoqu").select("update",{
	        			title: "选择小区",
  						items: []
	        			});
	        	$("#danyuan").select("update",{
	        			title: "选择单元",
  						items: []
	        			});
			//$("#xiaoqu").empty();$("#danyuan").empty();
			var personLocationIds = $("#jiedao").attr("data-values");
	        	$("#xiaoqu").val("");
	        	$("#xiaoqu").attr("data-values","");
	        	$("#danyuan").val("");
	        	$("#danyuan").attr("data-values","");
				//根据街道获取小区
  					$.ajax({
		    			type:'post',
		    			url:"${pageContext.request.contextPath}/weixin/getVillageByStreet",
		    			data : {
		    				"personLocationIds" : personLocationIds
		    			},
		    			dataType : "json",
		    			success:function(obj){
		    				if(typeof obj=="string"){
	        						obj=JSON.parse(obj);
	        					}
	        					//console.log(obj);
	        					if(obj.success!=undefined&&obj.success){
	        					console.log(2);
	        						//console.log(obj.regionList);
	        						var street = "";
	        						var danyuan="";
	        						var list = obj.regionList;
	        						/*for(var i=0;i<list.length;i++){
	        							street+='<option value="'+list[i].id+'">';
	        							street+=list[i].regionName;
	        							street+='</option>';
	        						}*/
	        						//11.14
	        						var arr2=[];
	        						for(var i=0;i<list.length;i++){
	        							arr2[i]={};
	        							arr2[i].title=list[i].regionName;
	        							arr2[i].value=list[i].id;
	        						}
	        						console.log(arr2);
	        						$("#xiaoqu").select("update",{
	        							title: "选择小区",
  										items: arr2
	        						});
	        						//end
	        						if(list[0].sub&&list[0].sub.length>0){
	        							var danyuanlist = list[0].sub;
	        							/*for(var j=0;j<danyuanlist.length;j++){
	        								danyuan+='<option value="'+danyuanlist[j].id+'">';
	        								danyuan+=danyuanlist[j].name;
	        								danyuan+='</option>';
	        							}*/
	        							//11.14
	        							var arr3=[];
	        							for(var i=0;i<danyuanlist.length;i++){
	        								arr3[i]={};
	        								arr3[i].title=danyuanlist[i].name;
	        								arr3[i].value=danyuanlist[i].id;
	        							}
	        							console.log(arr3);
	        							$("#danyuan").select("update",{
	        								title: "选择单元",
  											items: arr3
	        							});
	        							//end
	        						}
	        						//$("#xiaoqu").append(street);$("#danyuan").append(danyuan);
	        					}else{
	        						
	        					}
		    			}
    				});
			}
			function changeVillage(){
	        	$("#danyuan").select("update",{
	        			title: "选择单元",
  						items: []
	        			});
			//$("#danyuan").empty();
			var personLocationIds = $("#xiaoqu").attr("data-values");
				//根据街道获取小区
				$("#danyuan").val("");
	        	$("#danyuan").attr("data-values","");
  					$.ajax({
		    			type:'post',
		    			url:"${pageContext.request.contextPath}/weixin/getUnitByVillage",
		    			data : {
		    				"personLocationIds" : personLocationIds
		    			},
		    			dataType : "json",
		    			success:function(obj){
		    				if(typeof obj=="string"){
	        						obj=JSON.parse(obj);
	        					}
	        					//console.log(obj);
	        					if(obj.success!=undefined&&obj.success){
	        						//console.log(obj.regionList);
	        						var street = "";
	        						var list = obj.unitList;
	        						/*for(var i=0;i<list.length;i++){
	        							street+='<option value="'+list[i].id+'">';
	        							street+=list[i].unitName;
	        							street+='</option>';
	        						}*/
	        						//11.14
	        						var arr2=[];
	        						for(var i=0;i<list.length;i++){
	        							arr2[i]={};
	        							arr2[i].title=list[i].unitName;
	        							arr2[i].value=list[i].id;
	        						}
	        						$("#danyuan").select("update",{
	        							title: "选择单元",
  										items: arr2
	        						});
	        						//end
	        						//$("#danyuan").append(street);
	        					}else{
	        						
	        					}
		    			}
    				});
			}
		</script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/framework/jquery-weui/dist/js/city.js"></script>
		<style>
			.label_red {
				color:#e4393c;
			}
			.weui-picker-modal{margin-bottom:40px}
		</style>    
  	</head>
  	<body>
  		<!-- <form  id="pageForm" method="post" enctype="multipart/form-data"> -->
  		<!-- <div style="text-align:center;">
				<span >新增绑定单元页面</span>
			</div>-->
		<div class="weui-cells weui-cells_form" style="margin-bottom:25px;">
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>所在地区</label>
    			</div>
    			<div class="weui-cell__bd">
      				<input class="weui-input" type="text" name="personLocation" id="city-picker" >
    			</div>
  			</div>
  			
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>街道</label>
    			</div>
    			<div class="weui-cell__bd">
    				<input id="jiedao" type="text" class="regionSelect weui-input" onchange="changeStreet();"/>
    			</div>
  			</div>
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>小区</label>
    			</div>
    			<div class="weui-cell__bd">
    				<input id="xiaoqu" type="text" class="regionSelect weui-input" onchange="changeVillage();"/>
    			</div>
  			</div>
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>单元</label>
    			</div>
    			<div class="weui-cell__bd">
    				<input id="danyuan" type="text" class="regionSelect weui-input" />
    			</div>
  			</div>
  		</div>
  		<input  type="hidden" name="realUnitId" id="realUnitId"/>
  		<!-- </form> -->
  		<div style="text-align:center;color:#e4393c;margin-bottom:15px;">
  			<span class="label_red">* </span>请认真填写以上信息，提交以后不可更改
  		</div>
  		<a href="javascript:;" class="weui-btn weui-btn_primary" id="submitButton" style="width: 94%;margin-left: 3%;margin-right: 3%;">提交</a>	<br><br>
  		<script type="text/javascript">
  			//地区选择
  			$("#city-picker").cityPicker({
    			title: "请选择所在地区"
  			});	
  			//提交
  			$("#submitButton").click(function(){			 		
	  				var personLocation = $("#city-picker").val();
	  				var jiedao = $("#jiedao").val();
	  				var xiaoqu = $("#xiaoqu").val();
	  				var danyuan = $("#danyuan").attr("data-values");
	  				if(personLocation==null||personLocation==''||personLocation.indexOf("--")>=0||typeof(personLocation)==undefined){
	  					alert("所在地区不能为空!");
	  					return;
	  				}
	  				if(jiedao==null||jiedao==''||jiedao=='--'){
	  					alert("街道不能为空!");
	  					return;
	  				}
	  				if(xiaoqu==null||xiaoqu==''||xiaoqu=='--'){
	  					alert("小区不能为空!");
	  					return;
	  				}
	  				if(danyuan==null||danyuan==''||danyuan=='--'){
	  					alert("单元不能为空!");
	  					return;
	  				}
	  				
	  				//$("#realUnitId").val(danyuan);
	  				$.ajax({
			    			type:'post',
			    			url:"${pageContext.request.contextPath}/weixinpage/addNewUnit",
			    			data : {realUnitId:danyuan},
			    			dataType : "json",
			    			success:function(obj){	
			    				if(typeof obj=="string"){
			    					console.log(1);
	        						obj=JSON.parse(obj);
	        					}
	        					if(obj.success!=undefined&&obj.success){
	        						alert(obj.resultMsg);//之前不进入Success的原因是没有转换成JSON格式数据
									window.location.href="${pageContext.request.contextPath}/weixinpage/myCard";
	        					}else{
	        						alert(obj.resultMsg);
	        						window.location.reload();//重新加载
	        					}
	        					
			    			},error : function(XMLHttpRequest, textStatus, errorThrown) {	
							}
	    			});
	  				
  			});
  		</script>
  	</body>
</html>
