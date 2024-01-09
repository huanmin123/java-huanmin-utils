
// 用来异步操作的getters内的方法context.getters或者获取数据context.state

/**
 *       this.$store.dispatch({
 *             type: '模块/setFrameSkeletonBodyWidth',
 *             width: $("#FrameSkeletonBody").width()
 *         });
 * @type {Store}
 */
const actions= {
    setFrameSkeletonBodyWidth (state, width) {
        state.commit('setFrameSkeletonBodyWidth', width);
    },
    setFrameSkeletonBodyElement (state, element) {
        state.commit('setFrameSkeletonBodyElement', element);
    }
}
