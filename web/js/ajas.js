
$.ajax({
    methood :"get",
    url : "ranks.json",
    dataType :"json",
    success :function (data) {
        var names = [];
        var counts = [];
        for  (var i in data){
            names.push(data[i][0]);
            counts.push(data[i],[1]);
        }
        console.log(names);
        console.log(counts);

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
                data: names
            },
            yAxis: {},
            series: [{
                name: '唐诗数量',
                type: 'bar',    // bar 代表柱状图
                data: counts // 对应的每一个的数据
            }]
        };

        myChart.setOption(option);
    }

});
