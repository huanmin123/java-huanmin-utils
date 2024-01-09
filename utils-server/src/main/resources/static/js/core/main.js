

var app = createApp({
    setup() {
        const message = ref('Hello vue!')

        function increment() {
            this.$store.commit('increment')
            console.log(this.$store.state.count)
        }


        return {
            message
        }
    }
});
app.use(router)
app.use(store)
app.mount('#app')