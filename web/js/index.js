// var 是 variable
// document 是 js 中的 html 文档对象
// document.getElementById('main')) 找到 html 文档中的 id 为 main 的元素
// echarts.init 初始化画布元素
var myChart = echarts.init(document.getElementById('main'));

// 指定图表的配置项和数据
var option = {
    // 图标的标题
    title: {
        text: 'ECharts 唐诗'
    },
    tooltip: {},
    legend: {
        data:['唐诗数量']
    },
    // 横坐标
    xAxis: {
        data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
    },
    yAxis: {},
    series: [{
        name: '唐诗数量',
        type: 'bar',    // bar 代表柱状图
        data: [5, 20, 36, 10, 10, 20] // 对应的每一个的数据
    }]
};

myChart.setOption(option);
