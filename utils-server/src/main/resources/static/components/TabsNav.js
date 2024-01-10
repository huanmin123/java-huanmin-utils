/**

 [
 {
 name: "首页",
 url: "/home",
 active:true
 },
 {
 name: "其他",
 url: "/else",
 active:true,
 }
 ]
 */
const TabsNav = {
    props: {
        TabdNavData: JSON
    },

    data: function () {
        return {
            count: 0
        }
    },
    template: `
         <ul id="TabdNav" class="nav nav-tabs nav-justified TabdNav" style="font-size: 15px;">
       
        </ul>
    `,
    mounted: function () {
        console.log(this.TabdNavData)
        this.createItem(this.TabdNavData, "TabdNav")

        $('#TabdNav').on('click', 'li', function() {
            $('#TabdNav li.active').removeClass('active');
            $(this).closest('li').addClass('active');
        });
    },
    methods: {
        createItem(item, clazz) {
            let thisf = this
            item.forEach(function (item, index) {
               var dom= $("." + clazz + "").append(`
                     <li class="${item.active === true ? 'active' : ''}">
                        <a href="#${item.url}">${item.name}</a>
                     </li>
                `);
                if (index === 0) {
                    //隐藏左侧边框 ,
                    $(dom).find("li").eq(0).find("a").eq(0).css("border-left-width","0px")
                }
                //判断是最后一个
                if (index === item.length - 1) {
                    $(dom).find("li").eq(index).find("a").eq(0).css("border-right-width","0px")
                }
            })
        }
    },
    components: {}

}