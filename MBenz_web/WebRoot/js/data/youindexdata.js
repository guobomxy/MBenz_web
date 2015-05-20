var path = window.location.href.replace("youindex.jsp", "");
var indexdata = 
[
    
    { text: '产品下载',isexpand:false, children: [
         {url:path+"platform/product-data!youdowndata.action",text:"产品下载分析"},
         {url:path+"changepwd.jsp",text:"修改密码"}
    ]	
    }
   
];