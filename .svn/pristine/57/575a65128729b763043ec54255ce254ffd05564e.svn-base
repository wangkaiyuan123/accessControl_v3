<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/lib/weui.min.css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/framework/jquery-weui/dist/css/jquery-weui.min.css"/> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/framework/jquery-weui/dist/js/jquery-weui.min.js"></script> 
		<script type="text/javascript">
			var citydata = ${regionList};
			var personType = ${personTypeList}
			console.log(personType);
			function aaa(){
			console.log(1);
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
  		<form  id="pageForm" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/weixinpage/checkRegister">
		<div class="weui-cells weui-cells_form" style="margin-bottom:25px;">
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>姓名</label>
    			</div>
    			<div class="weui-cell__bd">
      				<input class="weui-input" type="text" name="personName" id="personName" placeholder="请输入姓名">
    			</div>
  			</div>	
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>身份证号码</label>
    			</div>
    			<div class="weui-cell__bd">
      				<input class="weui-input"  name="personNumber" id="personNumber" placeholder="请输入身份证号">
    			</div>
  			</div>	
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>手机号</label>
    			</div>
    			<div class="weui-cell__bd">
      				<input class="weui-input" type="tel" name="personPhone" id="personPhone" placeholder="请输入手机号">
    			</div>
  			</div>	
  			<div class="weui-cell weui-cell_vcode">
    			<div class="weui-cell__hd">
      				<label class="weui-label"><span class="label_red">* </span>验证码</label>
    			</div>
    			<div class="weui-cell__bd">
      				<input class="weui-input" type="text" name="codeNumber" id="codeNumber" placeholder="请输入验证码">
    			</div>
    			<div class="weui-cell__ft">
      				<button class="weui-vcode-btn" style="box-sizing:border-box;" id="vcode" type="button">获取验证码</button>
    			</div>
  			</div> 
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>所在地区</label>
    			</div>
    			<div class="weui-cell__bd">
      				<input class="weui-input" type="text" name="personLocation" id="city-picker" >
    			</div>
  			</div>
  			<!--  
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>详细地址</label>
    			</div><option value="">单元</option>
    			<div class="weui-cell__bd">
      				<input class="weui-input" type="text" name="roadPicker" id="roadPicker" >
    			</div>
  			</div>-->
  			
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>街道</label>
    			</div>
    			<div class="weui-cell__bd">
    				<!--<select id="jiedao" class="regionSelect" onchange="changeStreet();">
    					<option value=""></option>
    				</select>  -->
    				<input id="jiedao" type="text" class="regionSelect weui-input" onchange="changeStreet();"/>
    			</div>
  			</div>
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>小区</label>
    			</div>
    			<div class="weui-cell__bd">
    				<!--<select id="xiaoqu" class="regionSelect" onchange="changeVillage();">
    					<option value=""></option>
    				</select>  -->
    				<input id="xiaoqu" type="text" class="regionSelect weui-input" onchange="changeVillage();"/>
    			</div>
  			</div>
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>单元</label>
    			</div>
    			<div class="weui-cell__bd">
    				<!--<select id="danyuan" class="regionSelect" name="personAddress">
    					<option value=""></option>
    				</select>  -->
    				<input id="danyuan" type="text" class="regionSelect weui-input" name="personAddress1"/>
    			</div>
  			</div>
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>用户类型</label>
    			</div>
    			<div class="weui-cell__bd">
      				<input class="weui-input" type="text" name="personType" id="userSelect">
    			</div>
  			</div>
  			<div class="weui-cell">
    			<div class="weui-cell__hd">
    				<label class="weui-label"><span class="label_red">* </span>身份证照片</label>
    			</div>
    			<div class="weui-cell__bd">
      				<!--<input class="weui-input" type="file" name="cardFile" id="cardFile" placeholder="请输入姓名">-->
      				<div class="weui-uploader__input-box">
            			<input id="uploaderInput" class="weui-uploader__input" type="file" accept="image/*" multiple="" name="cardFile" >
          			</div>
    			</div>
  			</div>				
  		</div>
  		<input  type="hidden" name="personAddress" id="realUnitId"/>
  		</form>
  		<div style="text-align:center;color:#e4393c;margin-bottom:15px;">
  			<span class="label_red">* </span>请认真填写以上信息，提交以后不可更改
  		</div>
  		<a href="javascript:;" class="weui-btn weui-btn_primary" id="submitButton" style="width: 94%;margin-left: 3%;margin-right: 3%;">提交</a>	<br><br>
  		<script type="text/javascript">
  			//地区选择
  			$("#city-picker").cityPicker({
    			title: "请选择所在地区"
  			});	
  			//用户类型：
  			$("#userSelect").select({
				title: "选择类型",
  				items: personType/*[
  					{
  						title: "1",
      					value: "001",
  					},{
  						title: "2",
      					value: "002",
  					}
  				]*/	
  			})
  			//11.14
  			$("#uploaderInput").on("change",function(){
  				var img = event.target.files[0]; 
  				var reader = new FileReader();  
  				reader.onload=function(e){
  					$(".weui-uploader__input-box").css({"background":"url("+e.target.result+") no-repeat","background-size":"100% 100%"});
  				}
        		reader.readAsDataURL(img);
        		
  			});
  			//获取验证码
  			var n=0;
  			$("#vcode").click(function(){
  				if(n==0){
  					var personPhone = $("#personPhone").val();
  					if(personPhone==null||personPhone==''){
  						alert("手机号不能为空!");
  						return;
  					}
  					if(!(/^1(3|4|5|7|8)\d{9}$/.test(personPhone))){ 
        				alert("手机号码有误，请重填");  
        				return; 
    				}
  					$(this).html("60s");
  					var timer = setInterval(function(){
  						var curhtml = parseInt($(this).html())-1;
  						if(curhtml==0){
  							$(this).html("获取验证码");
  							n=0;
  							clearInterval(timer);
  						}else{	
  							$(this).html(curhtml+"s");
  							n=1;	
  						}						
  					}.bind(this),1000);
  					//发送验证码
  					$.ajax({
		    			type:'post',
		    			url:"/weixin/getCode",
		    			data : {
		    				"phone" : personPhone
		    			},
		    			dataType : "json",
		    			success:function(obj){
		    				
		    			}
    				});
  				}
  			});
  			//提交
  			var clickNum=0;
  			$("#submitButton").click(function(){
  				
	  				var personName = $("#personName").val();
	  				var personNumber = $("#personNumber").val();
	  				var cardFile = $("#uploaderInput").val();
	  				var personPhone = $("#personPhone").val();
	  				var codeNumber = $("#codeNumber").val();
	  				var personLocation = $("#city-picker").val();
	  				var jiedao = $("#jiedao").val();
	  				var xiaoqu = $("#xiaoqu").val();
	  				//var danyuan = $("#danyuan").val();
	  				var danyuan = $("#danyuan").attr("data-values");
	  				var personType = $("#userSelect").val();
	  				if(personName==null||personName==''){
	  					alert("姓名不能为空!");
	  					return;
	  				}
	  				if(personNumber==null||personNumber==''){
	  					alert("身份证号不能为空!");
	  					return;
	  				}
	  				// 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
	   				var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x|Y|y)$)/;  
	   				if(reg.test(personNumber) === false){  
	       				alert("身份证输入不合法!");  
	       				return;  
	   				}
	  				if(personPhone==null||personPhone==''){
	  					alert("手机号不能为空!");
	  					return;
	  				}
	  				if(!(/^1(3|4|5|7|8)\d{9}$/.test(personPhone))){ 
	        			alert("手机号码有误，请重填");  
	        			return; 
	    			}
	  				if(codeNumber==null||codeNumber==''){
	  					alert("验证码不能为空!");
	  					return;
	  				}
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
	  				$("#realUnitId").val(danyuan);
	  				if(personType==null||personType==''){
	  					alert("用户类型不能为空!");
	  					return;
	  				}
	  				if(cardFile==null||cardFile==''){
	   					alert("身份证照片未上传!");  
	       				return; 
	   				}
	  				if(clickNum==0){
	  					clickNum++;
	  					//先验证，后提交
	  					$.ajax({
			    			type:'post',
			    			url:"${pageContext.request.contextPath}/weixinpage/checkPhoneAndIDCard",
			    			data : {
			    				"personPhone" : personPhone,
			    				"personNumber" : personNumber,
			    				"codeNumber" : codeNumber,
			    			},
			    			dataType : "json",
			    			success:function(obj){
			    				if(typeof obj=="string"){
	        						obj=JSON.parse(obj);
	        					}
	        					if(obj.success!=undefined&&obj.success){
	        						$("#pageForm").submit();
	        					}else{
	        						alert(obj.resultMsg);
	        						clickNum=0;
	        					}
			    			},error : function(XMLHttpRequest, textStatus, errorThrown) {
								clickNum=0;
							}
	    			});
	  				}
  			});
  		</script>
  	</body>
</html>
