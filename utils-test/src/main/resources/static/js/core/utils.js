
const addJsScriptLog=false;
function addJsScript(url) {
    return new Promise(resolve => {
        var script = document.createElement('script');
        script.src = url;
        script.type = 'text/javascript';
        script.onload = function() {
            resolve(true);
        };
        document.getElementsByTagName('body')[0].appendChild(script);
    });
}

async  function addJsScripts(urls) {
    for (var i = 0; i < urls.length; i++) {
        const result =await  addJsScript(urls[i])
        if (result&&addJsScriptLog) {
            console.log("addJsScripts加载js成功:" + urls[i])
        }
    }
    return true;
}

//实现阻塞函数 ,通过计算时间差来实现
function sleep(time) {
    var startTime = new Date().getTime() + parseInt(time, 10);
    while (new Date().getTime() < startTime) {
    }
}




//滚动条还有多少距离到底部
function getScrollBottomHeight() {
    return $(document).height() - $(window).height() - $(window).scrollTop();
}

