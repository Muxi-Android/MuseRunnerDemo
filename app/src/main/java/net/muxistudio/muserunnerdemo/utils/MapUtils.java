package net.muxistudio.muserunnerdemo.utils;


/*
3d地图绘制路线
 */

import android.graphics.Color;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapUtils {



    //   定位蓝点
    public static MyLocationStyle point(MyLocationStyle myLocationStyle){
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
        //   连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（默认1秒1次定位）
        myLocationStyle.interval(2000);// 设置连续定位模式下的定位间隔
        return myLocationStyle;
        //   aMap.setMyLocationStyle(myLocationStyle);
    }

    public static LatLng getLocation(AMap aMap){
        LatLng L = new LatLng(aMap.getMyLocation().getLatitude(),aMap.getMyLocation().getLongitude());
        return L;
    }

//    //   绘制椭圆
//    public static void draw(AMap aMap,List<LatLng> latLngs){
//        draw(fullLine(aMap,start,end));
//    }


    //   绘制
    public static AMap fullLine(AMap aMap,AMapLocation start,AMapLocation end){
        aMap.addPolyline((new PolylineOptions())
                .add(new LatLng(start.getLatitude(),start.getLongitude()), new LatLng(end.getLatitude(),end.getLongitude()) )
                .width(10)
                .color(Color.RED));
        return aMap;
    }


    //
    public static List<LatLng> getLatLngs(List<LatLng> latLngs, AMapLocation location){
        latLngs.add(new LatLng(location.getLatitude(),location.getLongitude()));
        return latLngs;
    }

    //   画圈
    private void addPolylineInPlayGround(AMap aMap,List<LatLng> points) {
        List<Integer> colorList = new ArrayList<>();
        int[] colors = new int[]{Color.argb(255, 0, 255, 0),
                Color.argb(255, 255, 255, 0),
                Color.argb(255, 255, 0, 0)};
        Random random = new Random();
        for (int i = 0; i < points.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
        }
        aMap.addPolyline(new PolylineOptions()
                .colorValues(colorList)
                .addAll(points)
                .useGradient(true)
                .width(10));
    }

    private List<LatLng> readLatLngs(double[] coords) {
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < coords.length; i += 2) {
            points.add(new LatLng(coords[i+1], coords[i]));
        }
        return points;
    }

    //   控件设置
    public static void uiSetting(UiSettings uiSettings){
        //   缩放
        uiSettings.setZoomControlsEnabled(true);
//        uiSettings.setZoomPosition();

        //   指南针
        uiSettings.setCompassEnabled(true);

        //   比例尺
        uiSettings.setScaleControlsEnabled(true);
    }


}


/*
刷新地图
aMap.invalidate();

在activity中：
1.onCreate中创建地图
aMapView.onCreate(savedInstanceState);

2.onDestroy中销毁地图
aMapView.onDestroy();

3.onResume重新绘制加载地图
mMapView.onResume();

4.onPause暂停地图的绘制
mMapView.onPause();

5.onSaveInstanceState保存地图当前的状态
mMapView.onSaveInstanceState(outState);

6.
if(aMap == null){
   aMap = mapView.getMap();
}
7.
一条线一条线画的画递归
8.
private UiSettings mUiSettings;
mUiSettings = aMap.getUiSettings();
9.
动态获得坐标
points = readLatLngs(points)；
将坐标存入list
addPolylineInPlayGround(aMap,points)；
画圆
 */