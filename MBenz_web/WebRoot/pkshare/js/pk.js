	function bclose(){
		$("#gg").css({display:"none"});
	}

	function plus(i){
		$.MsgBox.Msg("点赞成功！");
		if(i==1){
			soft = soft1;
		}else if(i==2){
			soft = soft2;
		}else if(i==3){
			soft = soft3;
		}
		if(length==2){
			document.getElementById("pl1").onclick=function(){};
			document.getElementById("pl2").onclick=function(){};
		}else if(length==3){
			document.getElementById("pl1").onclick=function(){};
			document.getElementById("pl2").onclick=function(){};
			document.getElementById("pl3").onclick=function(){};
		}
		$.ajax({
			type : "POST",
			url : '../pkshare!proplus.action',
			data : {pid:soft.id,pkid:pkid},
			dataType : 'html',
			success : function(msg){
				var data = eval("("+msg+")");
			}
		});
	}
	function downapp(){
		if(isWeiXin()==true){
			$(".js-mask, .js-pop").show();
		}else{
			var url="http://g1.zmapp.com:1088/zmapptask/ml.html";
  			document.getElementById("ifile").src=url;
		}
	}
	function hd(){
		$(".js-mask, .js-pop").hide();
	}
	function clearm(){
		$(".mask1, .popup1").hide();
	}
	function icontool(p,id){
		var iconUrl = '';
		var hp = 'http://122.225.203.70:18191/';
		var hp2 = 'http://market.zmapp.com/';
		var ico = p.substr(0, 4);
		var pkpid = parseInt(id);
		if(pkpid<1000000){
			iconUrl = hp2+p;
		}else{
			if(ico=='http'){
				iconUrl =  p;
			}else{
				iconUrl = hp+p;
			}
		}
		
		return iconUrl;
	}
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
	function isWeiXin(){
		var ua = window.navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){
			return true;
		}else{
			return false;
		}
	} 
