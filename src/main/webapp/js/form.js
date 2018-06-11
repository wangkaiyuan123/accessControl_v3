//form提交：
function submitObj(opts){
    this.type = opts.type;
    this.url = opts.url;
    this.data= opts.data;
    this.refresh=opts.refresh;
    this.Isform = opts.Isform;
    this.submit = function(form){
    	var that=this;
    	var clickNum=0;
        $("#submitForm",parent.document).click(function(){
        	if(that.Isform){
        		if(clickNum==0){
        		 clickNum++;
        		 form.on('submit(submit)', function(data){
                    //如果存在多选时使用data.field会出现问题。
                    //可以使用序列化
                   var ajaxData =$(".layui-form").serialize();
        			//var ajaxData = data.field;
        			//console.log(data.field);
                    //console.log(ajaxData);
                    $.ajax({
                        type:that.type,
                        cache:false,
                        url:that.url,
                        data:ajaxData,
                        success:function(data){
                        	console.log(data);
                        	if( typeof data=="string"){
                        		data=JSON.parse(data);                   		
                        	}
                        	var message=data.resultMsg;
                            if(data.success==true){
                                var closeTime = $(".modal",parent.document).height(); 
                                layer.msg(message,{},function(){
                                    //关闭模态框
                                    $(".modal",parent.document).animate({height:0,marginTop:0},closeTime,function(){
                                        $(".modalShadow",parent.document).css("display","none");
                                        $(this).remove();
                                    });
                                });
                                //刷新界面
                                if(that.refresh){
                                	 setTimeout(function(){
                                         var mainIframe = $("#mainIframe",parent.document).attr("src");
                                         $("#mainIframe",parent.document).attr("src",mainIframe);
                                     },2000+closeTime);
                                } 
                            }else{
                            	clickNum=0;
                                layer.msg(message);
                            }
                        }//bind(this)
                    })
                });//.bind(this)
                $("i.iSubmit").click();
        	  }
        	}else{
        		if(clickNum==0){
        			clickNum++;
	        		$.ajax({
	                    type:that.type,
	                    cache:false,
	                    url:that.url,
	                    data:that.data,
	                    success:function(data){
	                    	if( typeof data=="string"){
	                    		data=JSON.parse(data);                   		
	                    	}
	                    	var message=data.resultMsg;
	                        if(data.success==true){
	                            var closeTime = $(".modal",parent.document).height(); 
	                            //debugger;
	                            layer.msg(message,{},function(){
	                                //关闭模态框
	                                $(".modal",parent.document).animate({height:0,marginTop:0},closeTime,function(){
	                                    $(".modalShadow",parent.document).css("display","none");
	                                    $(this).remove();
	                                });
	                            });
	                            //刷新界面
	                            if(that.refresh){
	                            	 setTimeout(function(){
	                                     var mainIframe = $("#mainIframe",parent.document).attr("src");
	                                     $("#mainIframe",parent.document).attr("src",mainIframe);
	                                 },2000+closeTime);
	                            } 
	                        }else{
	                        	clickNum=0;
	                            layer.msg(message);
	                        }
	                    }//.bind(this)
	                })
        	  }
        	}
        });//.bind(this)
    }
}

