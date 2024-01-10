
const PdfToWorldBody={
    data: function () {
        return {
            list:  [
                {
                    name: "首页",
                    url: "/xxx",
                    active:true
                },
                {
                    name: "其他",
                    url: "/xxx",
                    active:false,
                },
                {
                    name: "其他",
                    url: "/xxx",
                    active:false,
                }
            ]
        }
    },
    template: `
      <tabs-nav :TabdNavData="list"></tabs-nav>
      <h1>PdfToWorldBody.js</h1>
    `,
    mounted: function () {

    },
    methods: {

    },
    components: {
        "tabs-nav": TabsNav
    }


}