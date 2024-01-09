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


    //路由
    await addJsScripts([
        requestPrefix + '/routes/index.js',
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


// 主要js导入
    await addJsScript(requestPrefix + "/js/core/main.js")

}(load)()






