export default {
    name: 'FrameSkeleton',
    data: function () {
        return {}
    },
    template: `
      <div class="container-fluid  " id="Frame">
         <div class="row" id="FrameHeader">
               <slot name="header"></slot>
         </div>
         <div class="row " id="FrameCentre">
                <div id="FrameLeft">
                   <slot name="left"></slot>
                </div>
               <div  id="FrameBody">
                 <slot name="body"></slot>
               </div>

        </div>

          <div class="row" id="FrameFooter">

            <slot name="footer"></slot>
          </div>
      
      </div>
    `,
    mounted: function () {


    },
    methods: {},
    components: {}

}