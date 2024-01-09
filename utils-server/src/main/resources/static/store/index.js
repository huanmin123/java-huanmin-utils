



// 需要加上命名空间,否则会报错,因为不知道是哪个模块的,所以需要加上命名空间,格式为:模块名/方法名
//监听state的变化需要在computed事件中使用,不能在其他事件中使用,否则会导致监听不到数据的变化

const store = createStore({
    modules: {
        frame: moduleFrame,
    }
})
