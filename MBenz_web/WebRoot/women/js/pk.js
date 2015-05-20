	var hurl = "";
	var downstate1 = 0;
	var downstate2 = 0;
	var downstate3 = 0;
	var downstate4 = 0;
	var downstate5 = 0;
	var downstate6 = 0;
	var Request = new Object();
	Request = GetRequest();
	var ucode = Request['code'];
	var adId = Request['adId'];
	
	$(function(){
		if(!(!window.zmqnxwebviewjs)){
			window.zmqnxwebviewjs.initDownload('5193','com.dangdang.buy2');
			window.zmqnxwebviewjs.initDownload('1000543','com.achievo.vipshop');
			window.zmqnxwebviewjs.initDownload('7440','com.taobao.taobao');
			window.zmqnxwebviewjs.initDownload('1000605','com.thestore.main');
			window.zmqnxwebviewjs.initDownload('6085','com.tuan800.tao800');
			window.zmqnxwebviewjs.initDownload('2000393','com.taobao.ju.android');
		}
	});
	
	setbodyH();
	var timeoutProcess;
	function setbodyH(){
		var win = $(window);
		if(win.height()>0 && win.height()>300){
			document.getElementById("main").style.height= (win.height()-95) +  "px";
			clearTimeout(timeoutProcess);
		}else{
			timeoutProcess = setTimeout(setbodyH,358);
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
		var mallid;
		if(p==1){
			pid = "5193";
			iconurl = "http://122.225.203.70:19191/upload/icon/ICON_1420710136623.png";
			package = "com.dangdang.buy2";
			package1 = "5193";
			softname = "当当网";
			hits = "36348";
			softsize = "8780.61";
			softversion = "5.7.5";
			softupdatetime = "2015-01-26 14:49";
			author = "当当";
			softgold = "30";
			dourl = "http://market.zmapp.com/soft/1420710135154.apk";
			downstate = downstate1;
			mallid = "7";
		}else if(p==2){
			pid = "1000543";
			iconurl = "http://p16.qhimg.com/t01adc6ecd161910420.png";
			package = "com.achievo.vipshop";
			package2 = "1000543";
			softname = "唯品会";
			hits = "22838118";
			softsize = "7.41";
			softversion = "2.12.3";
			softupdatetime = "2015-02-12 13:41:17";
			author = "广州品唯软件有限公司";
			softgold = "20";
			dourl = "http://api.np.mobilem.360.cn/redirect/down/?from=zhangmeng&appid=21972";
			downstate = downstate2;
			mallid = "1";
		}else if(p==3){
			pid = "7440";
			iconurl = "http://122.225.203.70:19191/upload/icon/1389346830211.png";
			package = "com.taobao.taobao";
			package3 = "7440";
			softname = "淘宝";
			hits = "11477";
			softsize = "5935.84";
			softversion = "3.4.2";
			softupdatetime = "2014-12-31 10:43";
			author = "淘宝";
			softgold = "30";
			dourl = "http://market.zmapp.com/soft/1423705613027.apk";
			downstate = downstate3;
			mallid = "7";
		}else if(p==4){
			pid = "1000605";
			iconurl = "http://p18.qhimg.com/t0170444f0539726a86.png";
			package = "com.thestore.main";
			package4 = "1000605";
			softname = "1号店";
			hits = "14457";
			softsize = "8.24";
			softversion = "4.0.3";
			softupdatetime = "2015-03-06 13:54:10.01";
			author = "纽海信息技术（上海）有限公司";
			softgold = "20";
			dourl = "http://api.np.mobilem.360.cn/redirect/down/?from=zhangmeng&appid=17557";
			downstate = downstate4;
			mallid = "1";
		}else if(p==5){
			pid = "6085";
			iconurl = "http://122.225.203.70:19191/upload/icon/ICON_1425289725869.png";
			package = "com.tuan800.tao800";
			package5 = "6085";
			softname = "折800";
			hits = "19070";
			softsize = "9244.08";
			softversion = "3.6.4";
			softupdatetime = "2015-03-02 17:48";
			author = "团博百众（北京）科技";
			softgold = "30";
			dourl = "http://market.zmapp.com/soft/1425289724888.apk";
			downstate = downstate5;
			mallid = "7";
		}else if(p==6){
			pid = "2000393";
			iconurl = "http://122.225.203.70:18191/upload/baiduIcon/20150306/ICON_4706365322118761.png";
			package = "com.taobao.ju.android";
			package6 = "2000393";
			softname = "聚划算";
			hits = "265412";
			softsize = "11.47";
			softversion = "3.2.0";
			softupdatetime = "2015-03-06 09:34:15";
			author = "淘宝";
			softgold = "20";
			dourl = "http://gdown.baidu.com/data/wisegame/b82b1737e4832f39/juhuasuan_315.apk";
			downstate = downstate6;
			mallid = "3";
		}
		durl = dourl+"?papos=51&pamallid="+mallid+"&paproductid="+pid+"&padetail=0&paactivityid="+adId;
		url = hurl+"/lottery!productGoldin.action?user_code="+ucode+"&ad_id="+adId+"&gold="+softgold+"&product_id="+pid+"&son=duang";
		if(!(!window. zmqnxwebviewjs)){
				//window.zmqnxwebviewjs.down();
				window.zmqnxwebviewjs.downloadapp(pid,package,iconurl,downstate,softname,hits,softsize,softversion,softupdatetime,author,url,durl);
		}
	}
	function changedownstate(package_name, downstate,surl){
		var ss = package_name.trim();
		var did = "";
		if(package_name==package1){
			did ="p1";
		}else if(package_name==package2){
			did ="p2";
		}else if(package_name==package3){
			did ="p3";
		}else if(package_name==package4){
			did ="p4";
		}else if(package_name==package5){
			did ="p5";
		}else if(package_name==package6){
			did ="p6";
		}
		if(downstate==3||downstate==4){
			$("#"+did).html("下载中");
			document.getElementById(did).onclick=function(){};
		}else if(downstate==5||downstate==6){
			$("#"+did).html("已领取");
			document.getElementById(did).onclick=function(){};
		}
		
	}
	function hd(){
		$(".js-mask, .js-pop").hide();
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
	function GetRequest(u) {
		var url = location.search;
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
		if(ua.match(/MicroMessenger/i) == "micromessenger"){
			return true;
		}else{
			return false;
		}
	} 
