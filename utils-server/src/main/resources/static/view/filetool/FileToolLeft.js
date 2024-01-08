import listMenu  from "/static/components/ListMenu.js"

export default {
    name: 'FileToolLeft',
    data: function () {
        return {
            list: [
                {
                    name: 'PDF->Word(文字版)',
                    url: '/fileTool/pdfToWord',
                    icon: 'icon-file-powerpoint',
                    type: 'node'
                },
                {
                    name: 'PDF->Word(图片识别版)',
                    url: '/fileTool/pdfToWordByImage',
                    icon: 'icon-file-powerpoint',
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