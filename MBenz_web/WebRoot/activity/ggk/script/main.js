/*
 刮刮卡游戏
 @jslover 2014-08-08
 */
 var req = true;
 
 var soft1;
 var soft2;
 var soft3;
 var gold;
 var times;
 var share;
 var downstauts=false;
 var downstate1=0;
 var downstate2=0;
 var downstate3=0;
 var hurl = "";	

 var Request = new Object();
 Request = GetRequest();
 var ucode = Request['code'];
 var adId = Request['adId'];
var hp = 'http://market.zmapp.com/';
function GetRequest(u) {
	var url = location.search; //获取url中"?"符后的字串
	if(u){
		url = u;
	}else{
		var hs = location.href.split("/");
		hurl = hs[0]+"//"+hs[2]+"/"+hs[3];
	}
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
    	var str = url.substr(1);
	    if (str.indexOf("&") != -1) {
	        strs = str.split("&");
	        for (var i = 0; i < strs.length; i++) {
	            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
	        }
	    } else {
	        theRequest[str.split("=")[0]] = unescape(str.split("=")[1]);
	    }
	}
	return theRequest;
}

function gtimes(p){
	if(p>=3){//当次数等于0 。不能继续抽奖
		$("#begin").show();
		document.getElementById('begini').onclick=function(){};
 		$("#begini").attr('src','image/nocount.png');
	}else{
		return;	
	}
}

function goback(){
	if(!(!window. zmqnxwebviewjs)){
			window.zmqnxwebviewjs.backToMarker();
		}
}

function gotoExchange(){
	if(!(!window. zmqnxwebviewjs)){
			window.zmqnxwebviewjs.toExchangeMarket();
		}
}

function pgd(){
	gold = parseInt(gold)+parseInt(ggd);
	$("#ugd").html(gold);
}

var ggd;
function reshow(){
	if(times>0){
		$.ajax({
					type: 'POST',
					url: '../../lottery!golotteryin.action',//点击色块返回中奖金额
					data: {user_code:ucode,ad_id:adId},
					dataType: 'html', 
					success: function(data){
							var res =eval("("+data+")");
							ggd = res.gold;
							if(ggd>0){
								$("#ifget").html("恭喜得到"+ggd+"金币！");
							}else{
								$("#ifget").html("谢谢您的参与！");
								ggd=0;
								return;
							}
					}
		});
		}
	$("#begin").hide();
}


function msgalert(){
		if(ggd>0){
			
			//alert("获得"+ggd+"金币！");
			$.MsgBox.Alert("温馨提示", "获得"+ggd+"金币！",rebback);
		}else{
			//alert("幸运女神没有眷顾你！");
			$.MsgBox.Alert("温馨提示", "幸运女神没有眷顾你！",rebback);
		}
	}
function rebback(){
	req = true;
}

function upugold(){
	$.ajax({
		type: 'POST',
		url: '../../lottery!upgold.action',//分享金币入库
		data: {user_code:ucode},
		dataType: 'html', 
		success: function(data){
			var re = eval("("+data+")");
			var code= re.code;
			var upgold = re.ugold;
			if(code==200){
				gold = parseInt(upgold);
				$("#ugd").html(gold);
			}
		}
	});
}

function changedownstate(package_name, downstate,surl){
		var u = surl.split("?");
		var resp = GetRequest("?"+u[1]);
		var sgold = 0;
		var did = "";
		var ifdown = 1;
		if(package_name==soft1.package_name){
			sgold = soft1.gold;
			ifdown = soft1.isdown;
			did = "d1";
		}else if(package_name==soft2.package_name){
			sgold = soft2.gold;
			ifdown = soft2.isdown;
			did = "d2";
		}else if(package_name==soft3.package_name){
			sgold = soft3.gold;
			ifdown = soft3.isdown;
			did = "d3";
		}
		if(ucode==resp["user_code"]){
			if(downstate==5 && ifdown==0){
				$("#"+did).html("已领取");
				gold = parseInt(gold)+parseInt(sgold);
				$("#ugd").html(gold);
				document.getElementById(did).onclick=function(){};
			}else if(downstate==6 && ifdown==0){
				$("#"+did).html("已领取");
				document.getElementById(did).onclick=function(){};
			}else if(downstate==3||downstate==4){
				$("#"+did).html("下载中");
				document.getElementById(did).onclick=function(){};
			}else if(ifdown==1){
				$("#"+did).html("已领取");
				document.getElementById(did).onclick=function(){};
			}
		}else{
			if(downstate==5 || downstate==6 && ifdown==0){
				$("#"+did).html("已下载");
				document.getElementById(did).onclick=function(){};
			}else if(downstate==3||downstate==4){
				$("#"+did).html("下载中");
				document.getElementById(did).onclick=function(){};
			}else if(ifdown==1){
				$("#"+did).html("已领取");
				document.getElementById(did).onclick=function(){};
			}
		}
}

function changedownstates(request){
		var re = request.split("|");
		var package_name = re[0];
		var downstate = re[1];
		var surl = re[2];
		var u = surl.split("?");
		var resp = GetRequest("?"+u[1]);
		var sgold = 0;
		var did = "";
		var ifdown = 1;
		if(package_name==soft1.package_name){
			sgold = soft1.gold;
			ifdown = soft1.isdown;
			did = "d1";
		}else if(package_name==soft2.package_name){
			sgold = soft2.gold;
			ifdown = soft2.isdown;
			did = "d2";
		}else if(package_name==soft3.package_name){
			sgold = soft3.gold;
			ifdown = soft3.isdown;
			did = "d3";
		}
		if(ucode==resp["user_code"]){
			if(downstate==5 && ifdown==0){
				$("#"+did).html("已领取");
				gold = parseInt(gold)+parseInt(sgold);
				$("#ugd").html(gold);
				document.getElementById(did).onclick=function(){};
			}else if(downstate==6 && ifdown==0){
				$("#"+did).html("已领取");
				document.getElementById(did).onclick=function(){};
			}else if(downstate==3||downstate==4){
				$("#"+did).html("下载中");
				document.getElementById(did).onclick=function(){};
			}else if(ifdown==1){
				$("#"+did).html("已领取");
				document.getElementById(did).onclick=function(){};
			}
		}else{
			if(downstate==5 || downstate==6 && ifdown==0){
				$("#"+did).html("已下载");
				document.getElementById(did).onclick=function(){};
			}else if(downstate==3||downstate==4){
				$("#"+did).html("下载中");
				document.getElementById(did).onclick=function(){};
			}else if(ifdown==1){
				$("#"+did).html("已领取");
				document.getElementById(did).onclick=function(){};
			}
		}
}


function downapp(p){
	var pid;
	var iconurl;
	var package;
	var softname;
	var hits;
	var softsize;
	var softversion;
	var softupdatetime;
	var author;
	var softgold=0;
	var url;
	var durl;
	if(p==1){
		pid = soft1.product_id;
		iconurl = soft1.icon_url;
		package = soft1.package_name;
		downstate = downstate1;
		softname = soft1.softname;
		hits = soft1.hits;
		softsize = soft1.softsize;
		softversion = soft1.softversion;
		softupdatetime = soft1.softupdatetime;
		author = soft1.author;
		softgold = soft1.gold;
		dourl = hp+soft1.path_file;
		$("#d1").html("下载中");
	}else if(p==2){
		pid = soft2.product_id;
		iconurl = soft2.icon_url;
		package = soft2.package_name;
		downstate = downstate2;
		softname = soft2.softname;
		hits = soft2.hits;
		softsize = soft2.softsize;
		softversion = soft2.softversion;
		softupdatetime = soft2.softupdatetime;
		author = soft2.author;
		softgold = soft2.gold;
		dourl = hp+soft2.path_file;
		$("#d2").html("下载中");
	}else if(p==3){
		pid = soft3.product_id;
		iconurl = soft3.icon_url;
		package = soft3.package_name;
		downstate = downstate3;
		softname = soft3.softname;
		hits = soft3.hits;
		softsize = soft3.softsize;
		softversion = soft3.softversion;
		softupdatetime = soft3.softupdatetime;
		author = soft3.author;
		softgold = soft3.gold;
		dourl = hp+soft3.path_file;
		$("#d3").html("下载中");
	}
	durl = dourl+"?papos=51&pamallid=7&paproductid="+pid+"&padetail=0&paactivityid="+adId;
	url = hurl+"/lottery!productGoldin.action?user_code="+ucode+"&ad_id="+adId+"&gold="+softgold+"&product_id="+pid;
	if(!(!window. zmqnxwebviewjs)){
			downstauts = true;
			window.zmqnxwebviewjs.downloadapp(pid,package,hp+iconurl,downstate,softname,hits,softsize,softversion,softupdatetime,author,url,durl);
	}
}

function fenx(){
	if(!(!window. zmqnxwebviewjs)){
			window.zmqnxwebviewjs.shareapp("6973","去哪下","115.236.43.210:8080/upload/icon/ICON_1395127110505.png","7",adId,ucode);
		}
}


function shareqnxcallback(ad_id,ucode,sharetype){
	$.ajax({
					type: 'POST',
					url: '../../lottery!shareGold.action',//分享金币入库
					data: {user_code:ucode,ad_id:ad_id,share_type:sharetype},
					dataType: 'html', 
					success: function(data){
						var re = eval("("+data+")");
						var code= re.code;
						if(code==900){
							//$.MsgBox.Msg("温馨提示", "今天分享已用完，亲！明天再来吧！");
						}else if(code==200){
							gold = parseInt(gold)+50;
						$("#ugd").html(gold);
							$.MsgBox.Msg("温馨提示", "分享成功！获得50金币！");
						}
					}
	});
}	
(function($){
    //控制器
    var Controller = {
        init:function(){
            var _this = this;
            _this.initAnimate();
            //重要！事件要在动画结束后，否则andoid4.1卡
            setTimeout(function(){
                _this.initMask();
            },1700);
        }
        ,initAnimate:function(){
            //添加单次动画
            $('.main-wrap .logo').addClass('ani-logo');
            $('.main-wrap .gift').addClass('ani-gift');
            //重要！移除动画，否则andoid4.1卡
            setTimeout(function(){
                $('.main-wrap .logo').removeClass('ani-logo');
                $('.main-wrap .gift').removeClass('ani-gift');
            },1000);
        }
        //初始化遮罩
        ,initMask:function(){
            var timer = 0;
            $('#mask').wScratchPad({
                bg              : null,
                fg              : 'style/images/fg.png',
				fgblank         : 'style/images/fgblank.png',
                overlay         : 'none',
                size            : 50,
                scratchDown     : function(e, percent){
                    $('#code p').show();
										ReadyclearO5(percent);
                },
                scratchUp       : function(e, percent){
										ReadyclearO5(percent);
                },
                scratchMove     : function(e, percent){
										ReadyclearO5(percent);
                },
                cursor          : null
            });
            $('#mask img').remove();

        }
    };
    function ReadyclearO5(percent) {
    	if(percent>5){
    		if(req==true){
    			req=false;
	    		percent = 0;
	    		times = times-1;
	    		$.ajax({
						  	type: 'POST',
						 	 	url: '../../lottery!lotteryin.action',
						  	data: {user_code:ucode,ad_id:adId,gold:ggd},
						  	dataType: 'html',
						  	timeout:10000,
						 		success: function(data){
						 			var json = eval("("+data+")");
						 			if(json.times>=3 && json.code==500){
						 				$.MsgBox.Msg("温馨提示", "此次刮奖无效，明天再来吧！");
						 				reshow();
		    							gtimes(json.times);//计算刮刮卡次数
		    							ggd=0;
		    							pgd();//计算所得金币
						 			}else{
						 				$('#mask').wScratchPad('clear');
						 				setTimeout(function(){
		    						$('#mask').wScratchPad('reset');
		    							msgalert();
		    							reshow();
		    							gtimes(json.times);//计算刮刮卡次数
		    							pgd();//计算所得金币
		    					},1000);
						 			}
						 		},
						 		error: function(e){
						 			$.MsgBox.Msg("温馨提示", "让我打个盹，等会再试吧！");
						 		}
					});
					//setTimeout($('#mask').wScratchPad('reset'),1000);
					//$("#begin").show();
	    		//reshow();
	    		//gtimes();//计算刮刮卡次数
	    		//pgd();//计算所得金币
    		}
    	}
    }
    //展示UI
    var View = {};
    Controller.init();
    
    
    
    $.ajax({
	  	type: 'POST',
	 	 	url: '../../lottery!lottery.action',
	  	data: {user_code:ucode,ad_id:adId},
	  	dataType: 'html', 
	 		success: function(data){
	 				var res =eval("("+data+")");
	 				var code = res.code;
	 				gold = res.ugold;
	 				times = 3-res.times;
	 				var sde = eval(res.soft);
	 				if(sde){
		 				soft1 = sde[0];
		 				soft2 = sde[1];
		 				soft3 = sde[2];
		 				$("#ugd").html(gold);
		 				$("#im1").attr('outdata',hp+soft1.icon_url);
						$("#im2").attr('outdata',hp+soft2.icon_url);
						$("#im3").attr('outdata',hp+soft3.icon_url);
						$("#s1").html(soft1.gold);
						$("#s2").html(soft2.gold);
						$("#s3").html(soft3.gold);
						$("#sc1").html(soft1.softname);
						$("#sc2").html(soft2.softname);
						$("#sc3").html(soft3.softname);
						isdown();
						loadImg();
					}
					
		 				
	 				if(code==200){
	 						//alert("成功！");
	 						if(!(!window. zmqnxwebviewjs)){
  		 					window.zmqnxwebviewjs.initDownload(soft1.product_id,soft1.package_name);
  		 					window.zmqnxwebviewjs.initDownload(soft2.product_id,soft2.package_name);
  		 					window.zmqnxwebviewjs.initDownload(soft3.product_id,soft3.package_name);
 							}
	 				}else if(code==300){
	 					//alert("非法请求");
	 					$.MsgBox.Alert("温馨提示","非法请求！");
	 					document.getElementById('begini').onclick=function(){};
	 				}else if(code==400){
	 					$.MsgBox.Alert("温馨提示","访问无效！");
	 					document.getElementById('begini').onclick=function(){};
	 				}else if(code==500){
	 					//alert("今日抽奖次数已用完");
	 					document.getElementById('begini').onclick=function(){};
	 					$("#begini").attr('src','image/nocount.png');
	 				}else if(code==600){
	 					$.MsgBox.Alert("温馨提示","非法用户！");
	 					document.getElementById('begini').onclick=function(){};
	 				}
	 		}
		});
    function isdown(){
		if(soft1.isdown==1){
				$("#d1").html("已领取");
				document.getElementById('d1').onclick=function(){};
		}
		if(soft2.isdown==1){
				$("#d2").html("已领取");
				document.getElementById('d2').onclick=function(){};
		}
		if(soft3.isdown==1){
				$("#d3").html("已领取");
				document.getElementById('d3').onclick=function(){};
		}
	}
	function loadImg(){
       $('body img').attr("outdata", function(index, oldValue){
    	   if(oldValue!="")
			$('img')[index].src=oldValue;
		});
        
	}
	
})(Zepto);
