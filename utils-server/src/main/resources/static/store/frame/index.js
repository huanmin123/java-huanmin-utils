import state from "/static/store/frame/state.js";
import mutations from "/static/store/frame/mutations.js";
import actions from "/static/store/frame/actions.js";
import getters from "/static/store/frame/getters.js";

const moduleFrame = {
    namespaced: true,
    state:state,
    mutations:mutations,
    actions:actions,
    getters:getters
}
export default moduleFrame