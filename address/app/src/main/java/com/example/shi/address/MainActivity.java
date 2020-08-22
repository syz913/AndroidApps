package com.example.shi.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final String TAG = "GpsActivity";
    LocationManager locationManager;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            Toast.makeText(this, "请打开网络或GPS定位功能!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
            return;
        }
        // 从GPS获取最近的最近的定位信息
        Location location = locationManager.getLastKnownLocation(
                LocationManager.GPS_PROVIDER);

        // 使用location根据EditText的显示
        updateView(location);
        // 设置每3秒获取一次GPS的定位信息
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                , 3000, 8, new LocationListener()
                {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                        // 当GPS定位信息发生改变时，更新位置
                        updateView(location);
                    }

                    @Override
                    public void onProviderDisabled(String provider)
                    {
                        updateView(null);
                    }

                    @Override
                    public void onProviderEnabled(String provider)
                    {
                        // 当GPS LocationProvider可用时，更新位置
                        updateView(locationManager
                                .getLastKnownLocation(provider));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras)
                    {
                    }
                });
    }


//        try {
//            Location location;
//            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if(location == null){
//                Log.d(TAG, "onCreate.location = null");
//                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            }
//            Log.d(TAG, "onCreate.location = " + location);
//            updateView(location);
//
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, locationListener);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, locationListener);
//        }catch (SecurityException  e){
//            e.printStackTrace();
//        }
   // }

//
//    @Override
//    protected void onDestroy() {
//        try{
//            locationManager.removeUpdates(locationListener);
//        }catch (SecurityException e){
//        }
//        super.onDestroy();
//    }
//
//    private LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.d(TAG, "onProviderDisabled.location = " + location);
//            updateView(location);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.d(TAG, "onStatusChanged() called with " + "provider = [" + provider + "], status = [" + status + "], extras = [" + extras + "]");
//            switch (status) {
//                case LocationProvider.AVAILABLE:
//                    Log.i(TAG, "AVAILABLE");
//                    break;
//                case LocationProvider.OUT_OF_SERVICE:
//                    Log.i(TAG, "OUT_OF_SERVICE");
//                    break;
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Log.i(TAG, "TEMPORARILY_UNAVAILABLE");
//                    break;
//            }
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.d(TAG, "onProviderEnabled() called with " + "provider = [" + provider + "]");
//            try {
//                Location location = locationManager.getLastKnownLocation(provider);
//                Log.d(TAG, "onProviderDisabled.location = " + location);
//                updateView(location);
//            }catch (SecurityException e){
//
//            }
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.d(TAG, "onProviderDisabled() called with " + "provider = [" + provider + "]");
//        }
//    };

    /**
     * 实时更新文本内容
     */
    // 更新EditText中显示的内容
    public void updateView(Location newLocation)
    {
        if (newLocation != null)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("实时的位置信息：\n\n");
            sb.append("经度：");
            sb.append(newLocation.getLongitude());
            sb.append("\n纬度：");
            sb.append(newLocation.getLatitude());
            editText.setText(sb.toString());
        }
        else
        {
            // 如果传入的Location对象为空则清空EditText
            editText.setText("");
        }
    }
}