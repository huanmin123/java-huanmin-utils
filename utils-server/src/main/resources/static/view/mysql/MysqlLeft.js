
const MysqlLeft={
    data: function () {
        return {
            list: [
                {
                    name: 'SQL分析',
                    url: '/sql-analyse',
                    icon: 'icon-home',
                    type: 'node'
                },
                {
                    name: 'binlog处理',
                    url: '/binlog-handle',
                    icon: 'icon-home',
                    type: 'node'
                },
                {
                    name: 'sql语句生成',
                    url: '/sql-builder',
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
        "list-menu": ListMenu
    }

}