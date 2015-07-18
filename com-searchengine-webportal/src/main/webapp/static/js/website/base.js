$(function(){
    var top = $('#whiteline').css('top');
    $('.navigation').on('mouseenter',function(){
        var offset = 45 * parseInt($(this).attr('data-id')) - 23 - parseInt(top);
        $('#whiteline').css('transform', 'translate(0,'+offset+'px)');
        $('#whiteline').css('-webkit-transform', 'translate(0,'+offset+'px)');
        $('#whiteline').css('-moz-transform', 'translate(0,'+offset+'px)');
        $('#whiteline').css('-ms-transform', 'translate(0,'+offset+'px)');
        $('#whiteline').css('-o-transform', 'translate(0,'+offset+'px)');
    }); 
    $('.navigation').on('mouseleave',function(){
        $('#whiteline').css('transform', 'translate(0,0)');
        $('#whiteline').css('-webkit-transform', 'translate(0,0)');
        $('#whiteline').css('-moz-transform', 'translate(0,0)');
        $('#whiteline').css('-ms-transform', 'translate(0,0)');
        $('#whiteline').css('-o-transform', 'translate(0,0)');
    }); 
    $('#mobile-btn').on('click',function(){
        if ($('.nav').css('display') == "block") {
          $('.nav').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){$('.nav').toggleClass('visible animated bounceOutUp');});
          $('.nav').toggleClass('bounceInDown bounceOutUp');
        } else {
          $('.nav').toggleClass('visible animated bounceInDown');
        }
        $(this).toggleClass('fa-list fa-angle-up animated fadeIn');
    });
});
