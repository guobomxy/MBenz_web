var path = window.location.href.replace("index.jsp", "");
var indexdata = 
[
    
    { text: '掌盟书城',isexpand:false, children: [
         {url:path+"bookstore/book!gatherdata.action",text:"整体数据表"},
         {url:path+"bookstore/book!bookdata.action",text:"明细表"},
         {url:path+"bookstore/book!platdata.action",text:"小说平台明细表"},
         {url:path+"bookstore/book!searchword.action",text:"小说关键词日报表"},
         {url:path+"bookstore/book!searchwordweek.action",text:"小说关键词周报表"}
    ]	
    },
    { text: '起点书城',isexpand:false, children: [
         {url:path+"bookstore/qidian!gatherdata.action",text:"起点整体数据表"},
         {url:path+"bookstore/qidian!bookdata.action",text:"起点明细表"},
         {url:path+"bookstore/qidian!platdata.action",text:"起点小说平台明细表"},
         {url:path+"bookstore/qidian!searchword.action",text:"起点小说关键词日报表"},
         {url:path+"bookstore/qidian!searchwordweek.action",text:"起点小说关键词周报表"}
    ]	
    }
   
];

