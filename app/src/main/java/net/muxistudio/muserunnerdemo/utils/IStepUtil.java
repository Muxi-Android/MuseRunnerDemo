package net.muxistudio.muserunnerdemo.utils;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.kolibreath.muserunnerdemo.App;

import net.muxistudio.muserunnerdemo.utils.base.StepCount;
import net.muxistudio.muserunnerdemo.utils.base.StepValuePassListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zdf on 18-5-17.
 */

public class IStepUtil {

    private int stepNum;
    private int stepSpeed;

    public static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public int getTodayStepNum(){
        return stepNum;
    }

    public static void startStepService(){
        Intent intent = new Intent(App.getContext(),StepService.class);
        App.getContext().startService(intent);
    }

    public static void stopStepService(){
        Intent intent = new Intent(App.getContext(),StepService.class);
        App.getContext().stopService(intent);
    }

    public class StepService extends Service implements SensorEventListener{

        private SensorManager sensorManager;

        private int stepSensorType = -1;

        private StepCount mStepCount;

        private boolean hasRecord = false;

        private int hasStepCount;

        private int previousStepCount;

        public String CURRENT_DATE = "";

        @Override
        public void onCreate() {
            super.onCreate();

            initDate();

            new Thread(new Runnable() {
                public void run() {
                    startStepDetector();
                }
            }).start();

        }

        private void initDate() {
            CURRENT_DATE = getTodayDate();
            stepNum = com.example.kolibreath.muserunnerdemo.utils.SharedPreferencesUtils.readInteger(CURRENT_DATE);
        }

        private void startStepDetector() {
            if (sensorManager != null) {
                sensorManager = null;
            }
            sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

            sensorManager = (SensorManager) this
                    .getSystemService(SENSOR_SERVICE);
            //android4.4以后可以使用计步传感器
            int VERSION_CODES = Build.VERSION.SDK_INT;
            if (VERSION_CODES >= 19) {
                addCountStepListener();
            } else {
                addBasePedometerListener();
            }
        }

        private void addBasePedometerListener() {
            mStepCount = new StepCount();
            mStepCount.setSteps(stepNum);

            Sensor sensor = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            boolean isAvailable = sensorManager.registerListener(mStepCount.getStepDetector(), sensor,
                    SensorManager.SENSOR_DELAY_UI);

            mStepCount.initListener(new StepValuePassListener() {
                @Override
                public void stepChanged(int steps) {
                    stepNum = steps;
                    updateNum();
                }
            });
        }

        private void addCountStepListener() {
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            if (countSensor != null) {
                stepSensorType = Sensor.TYPE_STEP_COUNTER;
                sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else if (detectorSensor != null) {
                stepSensorType = Sensor.TYPE_STEP_DETECTOR;
                sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                addBasePedometerListener();
            }
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
                //获取当前传感器返回的临时步数
                int tempStep = (int) event.values[0];

                //首次如果没有获取手机系统中已有的步数则获取一次系统中APP还未开始记步的步数
                if (!hasRecord) {
                    hasRecord = true;
                    hasStepCount = tempStep;
                } else {
                    //获取APP打开到现在的总步数 = 本次系统回调的总步数-APP打开之前已有的步数
                    int thisStepCount = tempStep - hasStepCount;
                    //本次有效步数 =（APP打开后所记录的总步数-上一次APP打开后所记录的总步数）
                    int thisStep = thisStepCount - previousStepCount;
                    //总步数 = 现有的步数+本次有效步数
                    stepNum += (thisStep);
                    //记录最后一次APP打开到现在的总步数
                    previousStepCount = thisStepCount;

                }

            } else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
                if (event.values[0] == 1.0) {
                    stepNum++;
                }
            }
            updateNum();
        }

        private void updateNum() {
            com.example.kolibreath.muserunnerdemo.utils.SharedPreferencesUtils.storeInteger(CURRENT_DATE,stepNum);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //  no use
        }
    }
}
