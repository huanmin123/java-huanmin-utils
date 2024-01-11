
 // 用来修改state内的数据,同步操作,不能异步操作,异步操作用actions,
 //this.$store.commit('模块/setFrameSkeletonBodyWidth', $("#FrameSkeletonBody").width());
 //store.commit('frame/increment') 修改数据
const mutations= {
    setFrameSkeletonBodyWidth (state, width) {
        state.FrameSkeletonBodyWidth=width;
    },
    setFrameSkeletonBodyElement (state, element) {
        state.FrameSkeletonBodyElement=element;
    },
    increment (state) {
        state.count++
    }

 }
