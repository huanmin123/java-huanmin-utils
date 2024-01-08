
export default {
    name: 'FrameSkeletonHeader',
    data: function () {
        return {
            count: 0
        }
    },
    template: `
      <div class="container-fluid" id="FrameSkeletonHeader">
          <div class="row" id="HeaderNav">
            <div class="col-md-9" id="FrameSkeletonHeaderItem">
                  <ul >
                    <li class=" pull-left  text-center" id="LOGO" >
                    </li>
                    <li class="pull-left  text-center ">
                      <i class="icon icon-home"></i> <a href="#/">主页</a>
                    </li>
                    <li class="pull-left  text-center "  >
                     <i class="icon icon-file-o"></i>
                      <a href="#/fileTool" >文档工具
                      </a>
                    </li>
                    <li class="pull-left  text-center "  >
                      <i class="icon icon-time"></i> 
                      <a href="#/timer" >定时器
                      </a>
                    </li>
                    <li class="pull-left  text-center ">
                      <a href="#/">联系</a>
                    </li>
                    <li class="pull-left text-center ">
                      <a href="#/">关于</a>
                    </li>
                  </ul>
            </div>
            <div class="col-md-3 " id="FrameSkeletonHeaderInform">
                  <ul >
                    <li class="pull-right text-center " id="FrameSkeletonHeaderInformText" >
                      <i class="icon icon-user" ></i> 
<!--                      <a href="#/" >用户嘻嘻嘻嘻</a>-->
                      <div class="dropdown " id="FrameSkeletonHeaderInformDropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="###">用户嘻嘻嘻嘻 <span class="caret"></span></a>
                        <ul class="dropdown-menu pull-right">
                          <li><a href="###">操作</a></li>
                          <li><a href="###">另一个操作</a></li>
                          <li><a href="###">更多操作</a></li>
                        </ul>
                      </div>
                      
                    </li>
                    <li class="pull-right text-center" >
                      <a href="#/"> <i class="icon icon-bell" ></i></a>
                    </li>     
                    <li class="pull-right text-center" >
                      <a href="#/"> <i class="icon icon-eye-open" ></i></a>
                    </li>
                  </ul>
            </div>
          </div>
      </div>
    `,
    mounted: function () {

    },
    methods: {

    }

}