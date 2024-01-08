import {createRouter, createWebHashHistory} from '/static/js/vue-router-esm-browser.js';
import timerLeft from '/static/view/timer/TimerLeft.js'
import timerBody from '/static/view/timer/TimerBody.js'
import homeLeft from '/static/view/home/HomeLeft.js'
import homeBody from '/static/view/home/HomeBody.js'

import fileToolBody from '/static/view/filetool/FileToolBody.js'
import fileToolLeft from '/static/view/filetool/FileToolLeft.js'
import pdfToWorldBody from '/static/view/filetool/pdftoworld/PdfToWorldBody.js'

const routes = [
    {
        path: '/',
        components:{
            FrameLeft: homeLeft ,
            FrameBody: homeBody
        }

    },
    {
        path: '/fileTool',
        components:{
            FrameLeft:()=>import('/static/view/filetool/FileToolLeft.js'),
            FrameBody:()=>import('/static/view/filetool/FileToolBody.js')
        },
        children:[
            {
              path:'pdfToWord',
              components: {
                  FileTool:()=>import('/static/view/filetool/pdftoworld/PdfToWorldBody.js')
              }
            },
            {
                path:'pdfToWordByImage',
                components: {
                    FileTool:()=>import('/static/view/filetool/pdftoworld/PdfToWorldByImageBody.js')
                }
            }

        ]
    },
    {
        path: '/timer',
        components:{
            FrameLeft: timerLeft ,
            FrameBody: timerBody
        }

    },
]

const router = createRouter({
    // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: createWebHashHistory(),
    routes, // `routes: routes` 的缩写
})
export default router