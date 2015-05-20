///<reference path="jquery-1.5.1.js" />

(function($) {
    var itemIndex = 0;

    $.fn.autoSearchText = function(options) {
        //����Ϊ�ò�������Լ���Ĭ��ֵ
        var deafult = {
            width: 200,           //�ı����
            itemHeight: 150,      // �������
            minChar: 1,            //��С�ַ���(�ӵڼ�����ʼ����)
            datafn: null,         //�������ݺ���
            fn: null             //ѡ����󴥷��Ļص�����
        };
        var textDefault = $(this).val();
        var ops = $.extend(deafult, options);
        $(this).width(ops.width);
        var autoSearchItem = '<div id="autoSearchItem"><ul class="menu_v"></ul></div>';
        $(this).after(autoSearchItem);

        $('#autoSearchItem').width(ops.width + 2); //�������
        $('#autoSearchItem').height(ops.itemHeight); //�������

        $(this).focus(function() {
            if ($(this).val() == textDefault) {
                $(this).val('');
                $(this).css('color', 'black');
            }
        });
        var itemCount = $('li').length; //�����
        /*��갴�¼�ʱ����ʾ�����򣬲��һ�����ʱ�ı䱳��ɫ����ֵ�������*/
        $(this).bind('keyup', function(e) {
            if ($(this).val().length >= ops.minChar) {
                var position = $(this).position();
                $('#autoSearchItem').css({ 'visibility': 'visible', 'left': position.left, 'top': position.top + 24 });
                var data = ops.datafn($(this).val());
                initItem($(this), data);
                var itemCount = $('li').length;
                switch (e.keyCode) {
                    case 38:   //��
                        if (itemIndex > 1) {
                            itemIndex--;
                        }
                        $('li:nth-child(' + itemIndex + ')').css({ 'background': 'blue', 'color': 'white' });
                        $(this).val($('li:nth-child(' + itemIndex + ')').find('font').text());
                        break;
                    case 40: //��
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
        /*����հ״�����������*/
        $(document).click(function() {
            $('#autoSearchItem').css('visibility', 'hidden');
        });
    };
    /*��ȡ�ı����ֵ*/
    $.fn.getValue = function() {
        return $(this).val();
    };

    /*��ʼ������������,����ƹ�ÿ��ʱ���ı䱳��ɫ���ҽ����ֵ��ֵ�������*/
    function initItem(obj, data) {
        var str = "";
        if (data.length == 0) {
            //$('#autoSearchItem ul').html('<div style="text-align:center;color:red;">�޷�������<div>');
        	$('#autoSearchItem').css({ 'visibility': ''});
        }
        else {
            for (var i = 1; i <= data.length; i++) {
                //str += "<li><span>" + i + "/" + data.length + "</span>\r<font>" + data[i - 1] + "</font></li>";
                str += "<li><font>" + data[i - 1] + "</font></li>";
            }
            $('#autoSearchItem ul').html(str);
        }
        /*�����ʱ��ֵ��ֵ�������ı���*/
        $('li').each(function() {
            $(this).bind('click', function() {
                obj.val($(this).find('font').text());
                $('#autoSearchItem').css('visibility', 'hidden');
            });
        });
        /*��껮��ÿ��ʱ�ı䱳��ɫ*/
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