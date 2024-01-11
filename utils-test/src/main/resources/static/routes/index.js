
const routes = [
    {
        path: '/',
        components:{
            FrameLeft: HomeLeft ,
            FrameBody: HomeBody
        }

    },
    {
        path: '/fileTool',
        components:{
            FrameLeft:FileToolLeft,
            FrameBody:FileToolBody
        },
        children:[
            {
                path:'pdfToWord',
                components: {
                    FileTool:PdfToWorldBody
                }
            },
            {
                path:'pdfToWordByImage',
                components: {
                    FileTool:PdfToWorldByImageBody
                }
            }

        ]
    },
    {
        path: '/mysqlTool',
        components:{
            FrameLeft:MysqlLeft,
            FrameBody:MysqlBody
        },
        children:[
            {
                path:'sql-analyse',
                components: {
                    MysqlTool:MysqlSqlAnalyseBody
                }
            },
            {
                path:'binlog-handle',
                components: {
                    MysqlTool:MysqlBinLogHandleBody
                }
            },
            {
                path:'sql-builder',
                components: {
                    MysqlTool:MysqlSqlBuilderBody
                }
            }

        ]
    },
    {
        path: '/timer',
        components:{
            FrameLeft: TimerLeft ,
            FrameBody: TimerBody
        }

    },
]
// 3. 创建路由实例并传递 `routes` 配置
// 你可以在这里输入更多的配置，但我们在这里
// 暂时保持简单
const router = createRouter({
    // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: VueRouter.createWebHashHistory(),
    routes, // `routes: routes` 的缩写
})