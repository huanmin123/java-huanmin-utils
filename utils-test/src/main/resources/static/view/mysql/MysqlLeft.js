
const MysqlLeft={
    data: function () {
        return {
            list: [
                {
                    name: 'SQL分析',
                    url: '/mysqlTool/sql-analyse',
                    icon: 'icon icon-leaf',
                    type: 'node'
                },
                {
                    name: 'binlog处理',
                    url: '/mysqlTool/binlog-handle',
                    icon: 'icon icon-print',
                    type: 'node'
                },
                {
                    name: 'sql语句生成',
                    url: '/mysqlTool/sql-builder',
                    icon: 'icon icon-cubes',
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