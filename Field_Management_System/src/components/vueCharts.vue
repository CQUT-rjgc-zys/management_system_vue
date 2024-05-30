 <template>
    <div>
      <e-charts id="main" class="chart" :option="props.option" />
    </div>
  </template>
   
  <script setup lang="ts">
    import * as echarts from 'echarts';
    /** 接受父组件传来的值  */
    const props = defineProps({
      option: {
        type: Object,
        require: true
      }
    });
   
    onMounted(() => {
      var chartDom = document.getElementById('main');
      var myChart = echarts.init(chartDom);
      props.option && myChart.setOption(props.option);
    });
  </script>
   
  <style scoped>
    .chart {
      width: 600px;
      height: 400px;
    }
  </style>