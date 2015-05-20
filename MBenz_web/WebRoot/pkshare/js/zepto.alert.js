(function () {
    $.MsgBox = {
        Alert: function (title, msg,callback) {
            GenerateHtml("alert", title, msg);
            btnOk(callback);  //alert只是弹出消息，因此没必要用到回调函数callback
            btnNo();
        },
        Confirm: function (title, msg, callback) {
            GenerateHtml("confirm", title, msg);
            btnOk(callback);
            btnNo();
        },
        Msg: function (msg) {
            GenerateHtml(length, msg);
        }
    };

    /*//生成Html
    var GenerateHtml = function (type, title, msg) {

        var _html = "";

        _html += '<div id="mb_box"></div><div id="mb_con"><span id="mb_tit">' + title + '</span>';
        //_html += '<a id="mb_ico">x</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
        _html += '<div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';

        if (type == "alert") {
            _html += '<input id="mb_btn_ok" type="button" value="再刮一次" />';
        }
        if (type == "msg") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "confirm") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
        }
        _html += '</div></div>';

        //必须先将_html添加到body，再设置Css样式
        $("body").append(_html); GenerateCss();
    };*/
    
    
    var GenerateHtml = function (type, msg) {
        var _html = "";
        _html += '<div class="mask1"></div>';
        _html += '<div class="popup1" style="background-color:white;color:black;border-radius:25px;">';
		_html += '<div style="width:80%;font-size:12px;text-align:center;">点赞成功！<br/>快来看看各自的比例吧</div>';
		_html += '<hr style="margin:0px;height:2px;border:5px;background-color:#168bbb;color:blue;"/>';
		_html += '<table style="width:100%;text-align:center;"">';
		if(type==2){
			_html += '<tr >';
			_html += '<td width="50%" ><img src="'+icontool(soft1.icon,soft1.id)+'" width="45%"/></td>';
			_html += '<td><img src="'+icontool(soft2.icon,soft2.id)+'" width="45%"/></td>';
			_html += '</tr>';
			_html += '<tr style="height:10%;">';
			_html += '<td><div>'+soft1.name+'</div></td><td><div>'+soft2.name+'</div></td>';
			_html += '</tr>';
			_html += '<tr style="height:10%;">';
			_html += '<td><div>'+soft1.per+'</div></td><td><div>'+soft2.per+'</div></td>';
			_html += '</tr>';
		}else if(type==3){
			_html += '<tr >';
			_html += '<td width="33.3%" ><img id="pico" src="'+icontool(soft1.icon,soft1.id)+'" width="45%"/></td>';
			_html += '<td width="33.3%" ><img id="pict" src="'+icontool(soft2.icon,soft2.id)+'" width="45%"/></td>';
			_html += '<td><img src="'+icontool(soft3.icon,soft3.id)+'" width="45%"/></td>';
			_html += '</tr>';
			_html += '<tr style="height:10%;">';
			_html += '<td><div>'+soft1.name+'</div></td><td><div>'+soft2.name+'</div></td><td><div>'+soft3.name+'</div></td>';
			_html += '</tr>';
			_html += '<tr style="height:10%;">';
			_html += '<td><div>'+soft1.per+'</div></td><td><div>'+soft2.per+'</div></td><td><div>'+soft3.per+'</div></td>';
			_html += '</tr>';
		}
		_html += '</table>';
		_html += '<hr style="margin:0px;height:2px;border:5px;background-color:#168bbb;color:blue;"/>';
		_html += '<div style="text-align:center;font-size:20px;line-height:20px;background-color:#168bbb;margin-top:2px;margin-bottom:2px;width:44px;border-radius:4px;" onclick="clearm()">确定</div>';
		_html += '</div>';
		
		$("body").append(_html);
    };
    

    //生成Css
    var GenerateCss = function () {

        $("#mb_box").css({ width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
        });

        $("#mb_con").css({ zIndex: '999999', width: '250px', position: 'fixed',
            backgroundColor: 'White', borderRadius: '15px'
        });

        $("#mb_tit").css({ display: 'block', fontSize: '14px', color: '#444', padding: '10px 15px',
            backgroundColor: '#DDD', borderRadius: '15px 15px 0 0',
            borderBottom: '3px solid #009BFE', fontWeight: 'bold'
        });

        $("#mb_msg").css({ padding: '20px', lineHeight: '20px',
            borderBottom: '1px dashed #DDD', fontSize: '13px'
        });

        $("#mb_ico").css({ display: 'block', position: 'absolute', right: '10px', top: '9px',
            border: '1px solid Gray', width: '18px', height: '18px', textAlign: 'center',
            lineHeight: '16px', cursor: 'pointer', borderRadius: '12px', fontFamily: '微软雅黑'
        });

        $("#mb_btnbox").css({ margin: '15px 0 10px 0', textAlign: 'center' });
        $("#mb_btn_ok,#mb_btn_no").css({ width: '85px', height: '30px', color: 'white', border: 'none' });
        $("#mb_btn_ok").css({ backgroundColor: '#168bbb' });
        $("#mb_btn_no").css({ backgroundColor: 'gray', marginLeft: '20px' });


        //右上角关闭按钮hover样式
        /*$("#mb_ico").hover(function () {
            $(this).css({ backgroundColor: 'Red', color: 'White' });
        }, function () {
            $(this).css({ backgroundColor: '#DDD', color: 'black' });
        });*/

        var _widht = document.documentElement.clientWidth;  //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高

        var boxWidth = $("#mb_con").width();
        var boxHeight = $("#mb_con").height();

        //让提示框居中
        var ttop = (_height - boxHeight)<0?100:((_height - boxHeight)/2);
        $("#mb_con").css({ top: ttop + "px", left: (_widht - boxWidth) / 2 + "px" });
    };


    //确定按钮事件
    var btnOk = function (callback) {
        $("#mb_btn_ok").click(function () {
            $("#mb_box,#mb_con").remove();
            if (typeof (callback) == 'function') {
                callback();
            }
        });
    };

    //取消按钮事件
    var btnNo = function () {
        $("#mb_btn_no,#mb_ico").click(function () {
            $("#mb_box,#mb_con").remove();
        });
    };
})();