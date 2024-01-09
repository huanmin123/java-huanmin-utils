// 用来获取state内的数据
//this.$store.getters["模块/getFrameSkeletonBodyWidth"]
const  getters={
    getFrameSkeletonBodyWidth: (state) => {
      return   state.FrameSkeletonBodyWidth
    },
    getFrameSkeletonBodyElement: (state) => {
        return  state.FrameSkeletonBodyElement
    }

}
