
export default {
    data: function () {
        return {
            count: 0
        }
    },
    template: `
        <div class="container-fluid" id="FrameSkeletonFooter">
        <div id="FrameSkeletonFooterContext" class="text-center">
          ©版权归属: 胡安民
        </div>
        <div class="wrapper">
          <svg
              class="waves"
              viewBox="0 24 150 28"
              preserveAspectRatio="none"
              shape-rendering="auto"
          >
            <defs>
              <path
                  id="gentle-wave"
                  d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"
              />
            </defs>
            <g class="parallax">
              <use xlink:href="#gentle-wave" x="48" y="0" fill="rgba(181, 4, 44,0.9)" />
              <use xlink:href="#gentle-wave" x="48" y="3" fill="rgba(194, 4, 47,0.5)" />
              <use xlink:href="#gentle-wave" x="48" y="5" fill="rgba(205, 4, 50,0.3)" />
              <use xlink:href="#gentle-wave" x="48" y="7" fill="rgba(224, 4, 54,0.1)" />
            </g>
          </svg>
        </div>
        </div>
      
    `,
    mounted: function () {

    },
    methods: {

    },
    components: {

    }

}