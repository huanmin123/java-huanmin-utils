(function ($, window) {


    var HeaderNavHeight = $("#HeaderNav").height()  //获取HeaderNav的高度
    var FrameFooter = $("#FrameFooter").height() //获取Footer的高度


    function initFrameSize() {
        let h = $(window).height()
        $("#FrameLeft").css("height", h - HeaderNavHeight - FrameFooter - 28)
        $("#FrameBody").css("height", h - HeaderNavHeight - FrameFooter - 28)
    }

    initFrameSize()


    //监听屏幕变化
    $(window).resize(function () {
        initFrameSize()
    });


}(jQuery, window))