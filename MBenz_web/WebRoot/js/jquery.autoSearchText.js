///<reference path="jquery-1.5.1.js" />

(function($) {
    var itemIndex = 0;

    $.fn.autoSearchText = function(options) {
        //以下为该插件的属性及其默认值
        var deafult = {
            width: 200,           //文本框宽
            itemHeight: 150,      // 下拉框高
            minChar: 1,            //最小字符数(从第几个开始搜索)
            datafn: null,         //加载数据函数
            fn: null             //选择项后触发的回调函数
        };
        var textDefault = $(this).val();
        var ops = $.extend(deafult, options);
        $(this).width(ops.width);
        var autoSearchItem = '<div id="autoSearchItem"><ul class="menu_v"></ul></div>';
        $(this).after(autoSearchItem);

        $('#autoSearchItem').width(ops.width + 2); //设置项宽
        $('#autoSearchItem').height(ops.itemHeight); //设置项高

        $(this).focus(function() {
            if ($(this).val() == textDefault) {
                $(this).val('');
                $(this).css('color', 'black');
            }
        });
        var itemCount = $('li').length; //项个数
        /*鼠标按下键时，显示下拉框，并且划过项时改变背景色及赋值给输入框*/
        $(this).bind('keyup', function(e) {
            if ($(this).val().length >= ops.minChar) {
                var position = $(this).position();
                $('#autoSearchItem').css({ 'visibility': 'visible', 'left': position.left, 'top': position.top + 24 });
                var data = ops.datafn($(this).val());
                initItem($(this), data);
                var itemCount = $('li').length;
                switch (e.keyCode) {
                    case 38:   //上
                        if (itemIndex > 1) {
                            itemIndex--;
                        }
                        $('li:nth-child(' + itemIndex + ')').css({ 'background': 'blue', 'color': 'white' });
                        $(this).val($('li:nth-child(' + itemIndex + ')').find('font').text());
                        break;
                    case 40: //下
                        if (itemIndex < itemCount) {
                            itemIndex++;
                        }
                        $('li:nth-child(' + itemIndex + ')').css({ 'background': 'blue', 'color': 'white' });
                        $(this).val($('li:nth-child(' + itemIndex + ')').find('font').text());
                        break;
                    case 13:  //Enter
                        if (itemIndex > 0 && itemIndex <= itemCount) {
                            $(this).val($('li:nth-child(' + itemIndex + ')').find('font').text());
                            $('#autoSearchItem').css('visibility', 'hidden');
                            ops.fn($(this).val());
                        }
                        break;
                    default:
                        break;
                } 
            }
        });
        /*点击空白处隐藏下拉框*/
        $(document).click(function() {
            $('#autoSearchItem').css('visibility', 'hidden');
        });
    };
    /*获取文本框的值*/
    $.fn.getValue = function() {
        return $(this).val();
    };

    /*初始化下拉框数据,鼠标移过每项时，改变背景色并且将项的值赋值给输入框*/
    function initItem(obj, data) {
        var str = "";
        if (data.length == 0) {
            //$('#autoSearchItem ul').html('<div style="text-align:center;color:red;">无符合数据<div>');
        	$('#autoSearchItem').css({ 'visibility': ''});
        }
        else {
            for (var i = 1; i <= data.length; i++) {
                //str += "<li><span>" + i + "/" + data.length + "</span>\r<font>" + data[i - 1] + "</font></li>";
                str += "<li><font>" + data[i - 1] + "</font></li>";
            }
            $('#autoSearchItem ul').html(str);
        }
        /*点击项时将值赋值给搜索文本框*/
        $('li').each(function() {
            $(this).bind('click', function() {
                obj.val($(this).find('font').text());
                $('#autoSearchItem').css('visibility', 'hidden');
            });
        });
        /*鼠标划过每项时改变背景色*/
        $('li').each(function() {
            $(this).hover(
                function() {
                    $('li:nth-child(' + itemIndex + ')').css({ 'background': 'white', 'color': 'black' });
                    itemIndex = $('li').index($(this)[0]) + 1;
                    $(this).css({ 'background': 'blue', 'color': 'white' });
                    obj.val($('li:nth-child(' + itemIndex + ')').find('font').text());
                },
                function() {
                    $(this).css({ 'background': 'white', 'color': 'black' });
                }
            );
        });
    };
})(jQuery);