//项目域名前缀
const requestPrefix = "http://localhost:12345";

// 动态按照顺序加载js
async function load() {
    //插件
    await addJsScripts([
        requestPrefix + '/js/vue.global.js',
        requestPrefix + '/js/vue-router.global.js',
        requestPrefix + '/js/vuex.global.js',
        requestPrefix + '/js/core/global-variable.js',
    ]);
    //vuex
    //在插件导入后在导入主要js,进行插件的初始化配置等
    await addJsScripts([
        requestPrefix + '/store/frame/state.js',
        requestPrefix + '/store/frame/mutations.js',
        requestPrefix + '/store/frame/actions.js',
        requestPrefix + '/store/frame/getters.js',
        requestPrefix + '/store/frame/index.js',
        requestPrefix + '/store/index.js',
    ]);



    //全局组件
    await addJsScripts([
        requestPrefix + '/components/ListMenu.js',
    ]);

    //路由和view 组件
    await addJsScripts([
        requestPrefix + '/view/home/HomeLeft.js',
        requestPrefix + '/view/home/HomeBody.js',

        requestPrefix + '/view/timer/TimerLeft.js',

        requestPrefix + '/view/timer/TimerBody.js',
        requestPrefix + '/view/filetool/FileToolLeft.js',
        requestPrefix + '/view/filetool/FileToolBody.js',
        requestPrefix + '/view/filetool/pdftoworld/PdfToWorldBody.js',
        requestPrefix + '/view/filetool/pdftoworld/PdfToWorldByImageBody.js',

        requestPrefix + '/view/mysql/MySqlLeft.js',
        requestPrefix + '/view/mysql/MySqlBody.js',
        requestPrefix + '/view/mysql/body/MysqlBinLogHandleBody.js',
        requestPrefix + '/view/mysql/body/MysqlSqlAnalyseBody.js',
        requestPrefix + '/view/mysql/body/MysqlSqlBuilderBody.js',

        //路由绑定
        requestPrefix + '/routes/index.js',
    ]);




    //框架
    await addJsScripts([
        requestPrefix + '/frame/FrameSkeleton.js',
        requestPrefix + '/frame/FrameSkeletonHeader.js',
        requestPrefix + '/frame/FrameSkeletonLeft.js',
        requestPrefix + '/frame/FrameSkeletonBody.js',
        requestPrefix + '/frame/FrameSkeletonFooter.js',
    ]);




    // 主要js导入
    await addJsScript(requestPrefix + "/js/core/main.js")

    //初始化样式
    await addJsScript(requestPrefix + "/js/core/css-init.js")

}

(load)()






