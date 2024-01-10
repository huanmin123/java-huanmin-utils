/**
 *
 * list: [
 *                 {
 *                     name: '首页',
 *                     url: '/home',
 *                     icon: 'icon-home',
 *                     type: 'node'
 *                 },
 *                 {
 *                     name: '定时器',
 *                     url: '',
 *                     icon: 'icon-time',
 *                     type: 'list',
 *                     open: true,
 *                     list: [
 *                         {
 *                             name: '定时器管理',
 *                             url: '/timerAdmin',
 *                             icon: 'icon-time',
 *                             type: 'node',
 *                         },
 *                         {
 *                             name: '定时器统计',
 *                             url: '/timerStatistics',
 *                             icon: 'icon-pie-chart',
 *                             type: 'node',
 *                         }
 *                     ]
 *                 }
 *     ]
 *
 *
 */

const  ListMenu={
    props: {
        ListMenuData: JSON
    },

    data: function () {
        return {
            count: 0
        }
    },
    template: `
      <nav class="menu" data-ride="menu" >
      <ul id="ListMenu" class="tree tree-menu  ListMenu" data-ride="tree" >
   
      </ul>
      </nav>
    `,
    mounted: function () {
        this.createNode(this.ListMenuData,"ListMenu")

        // 获取 tree 实例
        $('#ListMenu').tree({
            animate: true,
            initialState: 'normal',
        });


        $('#ListMenu').on('click', 'a', function() {
            $('#ListMenu li.active').removeClass('active');
            $(this).closest('li').addClass('active');
        });



    },
    methods: {
        createNode(item,clazz){
            let  thisf = this
            item.forEach(function (item,index) {
                $("."+clazz+"").append(`<li  class="${item.open?'open':''}"  >
                    <a href='#${item.url}' class="${clazz}-${index}">
                    <i class='icon ${item.icon}'></i>
                    ${item.name} 
                    </a>
             </li>`);
                if(item.type==="list"){
                    $("."+clazz+"-"+index+"").after(`<ul class="${clazz}-${index}-${index}">

                    </ul>`);
                    thisf.createNode(item.list,`${clazz}-${index}-${index}`);
                }

            })
        }

    },
    components: {


    }

}