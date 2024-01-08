import listMenu  from "/static/components/ListMenu.js"

export default {
    name: 'TimerLeft',
    data: function () {
        return {
            list: [
                {
                    name: '定时器产品介绍',
                    url: '/timerDescribe',
                    icon: 'icon-home',
                    type: 'node'
                },
                {
                    name: '定时器管理',
                    url: '/timerAdmin',
                    icon: 'icon-home',
                    type: 'node'
                },
                {
                    name: '定时器统计',
                    url: '/timerStatistics',
                    icon: 'icon-home',
                    type: 'node'
                }
            ]
        }
    },
    template: `
      <list-menu :ListMenuData="list"></list-menu>
    `,
    mounted: function () {

    },
    methods: {

    },
    components: {
        "list-menu": listMenu
    }

}