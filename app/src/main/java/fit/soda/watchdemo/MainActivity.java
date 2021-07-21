package fit.soda.watchdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public AMapLocationClient mLocationClient;
    public AMapLocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.hello_tv);

        AndPermission.with(this)
                .runtime()
                .permission(Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION,
                        Permission.READ_PHONE_STATE,
                        Permission.ACCESS_BACKGROUND_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        tv.setText("权限拿到了");
                        getLocation();
                    }
                }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                tv.setText("权限没有");
            }
        }).start();

        HttpApi api = new HttpApi();
        new Thread(new Runnable() {
            @Override
            public void run() {
                api.mock();
            }
        }).start();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());

        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setInterval(1000);
        option.setNeedAddress(true);
        option.setLocationCacheEnable(false);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(option);


        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    Log.i("sodapanda", aMapLocation.toStr());
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);

        mLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLocationClient.stopLocation();
    }
}