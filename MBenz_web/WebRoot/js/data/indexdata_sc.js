var path = window.location.href.replace("index.jsp", "");
var indexdata = [ {
	text : '终端设备激活',
	isexpand : false,
	children : [ {
		url : path + "active/sum!count.action",
		text : "总激活量日报表"
	}, {
		url : path + "active/sum!Monthcount.action",
		text : "总激活量月报表"
	}, {
		url : path + "active/sum!selectData.action",
		text : "总激活量走势图"
	}, {
		url : path + "active/count!count.action",
		text : "渠道激活量日报表"
	}, {
		url : path + "active/count!Monthcount.action",
		text : "渠道激活量月报表"
	}, {
		url : path + "active/count!volumeChart.action",
		text : "渠道激活量走势图"
	}, {
		url : path + "active/terminal!count.action",
		text : "终端激活明细分析"
	}, {
		url : path + "count/user!count.action",
		text : "终端激活每日分析"
	}, {
		url : path + "count/device!systemCount.action",
		text : "终端设备系统分布"
	}, {
		url : path + "count/device!screenCount.action",
		text : "终端屏幕宽高分析"
	}, {
		url : path + "count/device!count.action",
		text : "终端渠道型号分析"
	} ]
}, {
	text : '市场用户分析',
	isexpand : false,
	children : [ {
		url : path + "user/register!count.action",
		text : "注册用户明细分析"
	}, {
		url : path + "user/area!count.action",
		text : "用户量激活量区域分析"
	} ]
}, {
	text : '平台数据分析',
	isexpand : false,
	children : [ {
		url : path + "platform/marketdatas!count.action",
		text : "市场总数据"
	},{
		url : path + "platform/marketdatas!dldaily.action",
		text : "产品下载明细"
	}, {
		text : '基地投诉分析',
		isexpand : false,
		children : [ {
			url : path + "platform/complain!importdata.action",
			text : "投诉导入"
		}, {
			url : path + "platform/complain!analyse.action",
			text : "投诉分析"
		} ]
	} ]
}, {
	text : '关键词',
	isexpand : false,
	children : [ {
		url : path + "key/allkey!count.action",
		text : "搜索日报"
	}, {
		url : path + "key/allkey!keycount.action",
		text : "关键词搜索统计"
	}, {
		url : path + "key/allkey!keytimecount.action",
		text : "时间段统计"
	} ]
}, {
	text : '新闻瀑布流',
	isexpand : false,
	children : [ {
		url : path + "news/news!total.action",
		text : "整体数据走势图"
	}, {
		url : path + "news/news!down.action",
		text : "下载数据表"
	}, {
		url : path + "news/news!count.action",
		text : "点击趋势图"
	} ]
} ];
