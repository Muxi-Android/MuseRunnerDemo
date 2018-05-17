package com.example.kolibreath.muserunnerdemo.utils;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 高德sdk工具类
 */

public class LocationUtils {

    private static final String TAG = "LocationUtil";
    //  开始定位
    public final static int LOCATION_START = 0;
    //  定位完成
    public final static int LOCATION_FINISH = 1;
    //  停止定位
    public final static int LOCATION_STOP= 2;
    public final static String KEY_URL = "URL";
    private static SimpleDateFormat mSdf = null;

    public static AMapLocationClientOption setOption(){
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        // 设置定位场景:运动  （签到：SignIn；出行：Transport default：无场景）
        locationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        // 选择定位模式：高精度模式（会同时使用网络定位和GPS定位 优先返回最高精度的定位结果)
        // 低功耗：Battery_Saving 只会使用网络定位
        // 仅用设备定位模式：Device_Sensors 无需网络 只用GPS定位
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //定位间隔 默认2s
        locationOption.setInterval(2000);
        //是否返回逆地理地址信息
        locationOption.setNeedAddress(true);
        return locationOption;
    }

    //  获得速度
    public static int getSpeed(AMapLocation location){
        if(null == location){
            return 0;
        }
        if (location.getErrorCode() == 0) {
            return (int) location.getSpeed();
        }
        return 0;
    }

    //  获得精度
    public static int getAccuracy(AMapLocation location){
        if (null == location){
            return 0;
        }
        if (location.getErrorCode() == 0) {
            return (int) location.getAccuracy();
        }
        return 0;
    }

    //  获得步数
    public static int getCount(AMapLocation location){
        if (null == location){
            return 0;
        }

        return 0;
    }

    //  获得GPS状态
    public static String getGPSStatus(AMapLocation location){
        if (null == location){
            return "";
        }
        String str = "";
        if (location.getErrorCode() == 0){
            switch (location.getGpsAccuracyStatus()){
                case AMapLocation.GPS_ACCURACY_GOOD:
                    str = "信号良好";
                    break;
                case AMapLocation.GPS_ACCURACY_BAD:
                    str = "信号较差";
                    break;
                case AMapLocation.GPS_ACCURACY_UNKNOWN:
                    str = "无信号";
                    break;
            }
        }
        return str;
    }


    //  开始定位
    public static void startLocation(AMapLocationClient locationClient,AMapLocationClientOption locationOption,AMapLocationListener listener){
        // 高德推荐设置场景模式后最好调用一次stop再调用start以保证场景模式生效
        if (locationClient != null){
            locationClient.stopLocation();
            locationClient.setLocationOption(locationOption);
            locationClient.setLocationListener(listener);
            locationClient.startLocation();
        }

    }


    //  停止定位
    public static void stopLocation(AMapLocationClient locationClient){
        locationClient.stopLocation();
    }

    //  销毁定位
    public static void onDestroy(AMapLocationClient locationClient,AMapLocationClientOption locationOption){
        if (locationClient != null){
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    public synchronized static String getLocationStatus(AMapLocation location){
        if(null == location){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功
        if(location.getErrorCode() == 0){
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");

            sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
            // 获取当前提供定位服务的卫星个数
            sb.append("星    数    : " + location.getSatellites() + "\n");
            sb.append("地    址    : " + location.getAddress() + "\n");
            //定位完成的时间
            sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        return sb.toString();
    }

    public  static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (mSdf == null) {
            try {
                mSdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            mSdf.applyPattern(strPattern);
        }
        return mSdf == null ? "NULL" : mSdf.format(l);
    }
}
