<!-- 定位 -->
<template>
    <div>
    </div>

</template>

<script setup lang='ts'>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import AMapLoader from "@amap/amap-jsapi-loader";

// 返回定位打卡列表
const router = useRouter();
const goBack = () => {
    router.push("position");
}

let map = null;

onMounted(() => {
    window._AMapSecurityConfig = {
        securityJsCode: "3bb27e8a128223c35f43cf5e04a8fedf", //申请的安全密钥
    };
    // 加载js API
    AMapLoader.load({
        key: "47e625684e574f55150900888756c8c0", // 申请好的Web端开发者Key，首次调用 load 时必填
        version: "2.0", // 指定要加载的 JSAPI 的版本
    })
        .then((AMap) => {
            // 创建地图对象
            map = new AMap.Map("container", {
                // 设置地图容器id
                viewMode: "3D", // 是否为3D地图模式
                zoom: 11, // 初始化地图级别
                pitch: 45, //角度
                terrain: true, //开启地形图
                center: [106.887703, 29.439325], // 初始化地图中心点位置
                // mapStyle: "amap://styles/whitesmoke"  //地图主题样式
            });
            AMap.plugin(["AMap.ToolBar", "AMap.Scale", "AMap.HawkEye", "AMap.Geolocation", "AMap.MapType", "AMap.MouseTool"], function () {
                //异步同时加载多个插件
                // 添加地图插件 
                map.addControl(new AMap.MapType()); // 实现默认图层与卫星图,实时交通图层之间切换
                map.addControl(new AMap.ToolBar()); // 工具条控件;范围选择控件
                map.addControl(new AMap.Scale()); // 显示当前地图中心的比例尺

                map.addControl(new AMap.HawkEye()); // 显示缩略图
                map.addControl(new AMap.Geolocation()); // 定位当前位置
                // 以下是鼠标工具插件
                const mouseTool = new AMap.MouseTool(map);
                // mouseTool.rule();// 用户手动绘制折线图,测量距离
                mouseTool.measureArea(); // 测量面积

            });
            //创建一个 Marker 实例：
            const marker = new AMap.Marker({
                position: new AMap.LngLat(116.39, 39.9), //经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
                title: "北京",
            });
            //将创建的点标记添加到已有的地图实例：
            map.add(marker);
            //创建点标记的点击事件
            marker.on("click", function (event) {
                console.log(event);
            });
            // 创建2个点（设置可以拖动）
            var m1 = new AMap.Marker({
                map: map, //将m1这个点添加到map地图里
                draggable: true, //配置点可以拖动
                position: new AMap.LngLat(116.482617, 39.929119),
            });
            var m2 = new AMap.Marker({
                map: map, //将m1这个点添加到map地图里
                draggable: true, //配置点可以拖动
                position: new AMap.LngLat(116.3283522, 39.866265),
            });
            // 让地图根据覆盖调整地图显示区域
            map.setFitView()
            // 线
            var line = new AMap.Polyline({
                strokeColor: "#80d8ff",//描边的颜色
                isOutline: true,//包含轮廓
                outerlineColor: 'white',
            })
            line.setMap(map)
            // 文本
            var text = new AMap.Text({
                text: "",
                style: {
                    'background-color': '#29b6f6',
                    'border-color': '#e1f5fe',
                    'font-size': "16px"
                }
            })
            text.setMap(map)
            // 计算
            function compute() {
                // 得到m1和m2的经纬度
                var p1 = m1.getPosition()
                var p2 = m2.getPosition()
                // text文本显示在这个经纬度中间
                var textPos = p1.divideBy(2).add(p2.divideBy(2))
                var distance = Math.round(p1.distance(p2))
                var path = [p1, p2]
                line.setPath(path)//绘制线
                text.setText('距离为' + distance + "米")
                text.setPosition(textPos)
            }
            compute()
            // 监听拖动点时触发方法。
            m1.on("dragging", compute)
            m2.on("dragging", compute)
        })

})
    .catch((e) => {
        console.log(e);
    });


onUnmounted(() => {
    map?.destroy();
});
</script>

<style scoped>
#container {
    margin-top: 20px;
    width: 100%;
    height: 500px;
    border: solid 1px gray;
}
</style>