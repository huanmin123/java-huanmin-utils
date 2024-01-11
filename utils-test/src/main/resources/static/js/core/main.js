

var app = createApp({

    setup() {
        return {

        }
    },
    //注册的名称必须全部小写,如果是多个单词,使用连字符-分隔
    //否则组件无法注册
    components: {
        "frames-keleton": FrameSkeleton,
        "frame-skeleton-header": FrameSkeletonHeader,
        "frame-skeleton-left": FrameSkeletonLeft,
        "frame-skeleton-body": FrameSkeletonBody,
        "frames-keleton-footer": FrameSkeletonFooter,
    },
    methods: {
        add() {
            store.commit('frame/increment')
        }

    },
    computed: { //监听变化
        count() {
            return store.state.frame.count
        }
    }
});
app.use(router)
app.use(store)
app.config.errorHandler = (err) => {
  console.error(err)
}
app.mount('#app')