var path = window.location.href.replace("index.jsp", "");
var indexdata = 
[
   
    { text: '平台数据分析',isexpand:false, children: [ 
        {url:path+"platform/marketdata!count.action",text:"市场总体数据"},
        {text: '市场产品',isexpand:false, children: [
                {url:path+"platform/marketproduct!cpdata.action",text:"CP产品数据表"},
                {url:path+"platform/marketproduct!movedata.action",text:"移动基地产品数据表"},
                {url:path+"platform/marketproduct!countall.action",text:"全部产品数据表"}
          ]},
        {text: '渠道',isexpand:false, children: [
                 {url:path+"platform/channel!count.action",text:"掌盟互娱"},
                 {url:path+"platform/channel!newcount.action",text:"拇指市场"},
                 {url:path+"platform/channel!countgame.action",text:"游戏淘"},
                 {url:path+"platform/channel!data.action",text:"渠道数据管理"}
         ]},
        {text: '产品分析',isexpand:false, children: [
                 /*{url:path+"platform/product-data!cpdata.action",text:"CP产品数据分析"},*/
                 {url:path+"platform/product-data!newcpdata.action",text:"CP产品数据分析"},
                 {url:path+"platform/product-total!cpdata.action",text:"产品汇总统计"},
                 {url:path+"platform/product-data!alldata.action",text:"总体数据分析"},
                 {url:path+"platform/product-data!importdata.action",text:"导入CP产品收入"},
                 {url:path+"platform/product-data!cptdata.action",text:"CPT产品数据"}
        ]},
        {text: '基地投诉分析',isexpand:false, children: [
                 {url:path+"platform/complain!importdata.action",text:"投诉导入"},
                 {url:path+"platform/complain!analyse.action",text:"投诉分析"}
        ]},
        {text: '运维报表',isexpand:false, children: [
                 {url:path+"platform/reportform!month.action",text:"月报表"},
                 {url:path+"platform/reportform!monthwrite.action",text:"月报表录入"},
                 {url:path+"platform/reportform!trend.action",text:"趋势分析"}
        ]},
        {text: '收益',isexpand:false, children: [
                 {url:path+"platform/earning!cpearning.action",text:"自接CP收益录入"}
        ]}
        ]
    },
    { text: '关键词',isexpand:false, children: [ 
         {url:path+"key/allkey!count.action",text:"搜索日报"},
         {url:path+"key/allkey!keycount.action",text:"关键词搜索统计"},
         {url:path+"key/allkey!keytimecount.action",text:"时间段统计"}
    ]
    }
];

