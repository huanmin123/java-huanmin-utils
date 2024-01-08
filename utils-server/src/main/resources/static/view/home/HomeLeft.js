import listMenu  from "/static/components/ListMenu.js"
export default {
    name: 'HomeLeft',
    data: function () {
        return {
            list: [
                {
                    name: '首页',
                    url: '/home',
                    icon: 'icon-home',
                    type: 'node'
                },
                {
                    name: '定时器',
                    url: '##',
                    icon: 'icon-time',
                    type: 'list',
                    open: true,
                    list: [
                        {
                            name: '定时器管理',
                            url: '/timerAdmin',
                            icon: 'icon-time',
                            type: 'node',
                        },
                        {
                            name: '定时器统计',
                            url: '/timerStatistics',
                            icon: 'icon-pie-chart',
                            type: 'node',
                        }
                    ]
                },
                {
                    name: '更新时间',
                    url: '',
                    icon: 'icon-time',
                    type: 'list',
                    open: true,
                    list: [
                        {
                            name: '更新时间管理',
                            url: '##',
                            icon: 'icon-pie-chart',
                            type: 'node',
                        },
                        {
                            name: '更新时间统计',
                            url: '##',
                            icon: 'icon-pie-chart',
                            type: 'node',
                        },
                        {
                            name: '更新时间统计',
                            url: '##',
                            icon: 'icon-pie-chart',
                            type: 'node',
                        }
                    ]
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