package com.example.solamly.solamly.module.baidu_map;

import android.util.Log;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.example.solamly.solamly.Base.BaseActivity;
import com.example.solamly.solamly.R;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author SOLAMLY
 * @Date 2018/9/28 17:40
 * @Description:
 */

public class BaiduMapActivity extends BaseActivity {
    private static final String TAG = "BaiduMapActivity";

    @BindView(R.id.map)
    MapView mapView;

    private BaiduMap baiduMap;
    private static int MAP_TYPE = BaiduMap.MAP_TYPE_NORMAL;         //地图类型:普通地图和卫星地图

    private MyLocationConfiguration configuration;          //定位
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener = new MyLocationListener();
    private LocationClientOption option;

    @Override
    protected int setLayout() {
        return R.layout.activity_baidu_map;
    }

    protected void initView() {
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);          //设置地图类型
        baiduMap.setMyLocationEnabled(true);                    //开启定位图层

        initOption();

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.setLocOption(option);
        mLocationClient.requestHotSpotState();
        mLocationClient.start();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick({
            R.id.btn_map_type
    })
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_map_type:
                    /*  普通地图（包含3D地图） --> 卫星图 */
                if (MAP_TYPE == BaiduMap.MAP_TYPE_NORMAL) {
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    MAP_TYPE = BaiduMap.MAP_TYPE_SATELLITE;

                    /* 卫星图 --> 普通地图*/
                } else if (MAP_TYPE == BaiduMap.MAP_TYPE_SATELLITE) {
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    MAP_TYPE = BaiduMap.MAP_TYPE_NORMAL;
                }
                break;
        }
    }

    /**
     * 配置定位SDK参数
     *
     * @return
     */
    public void initOption() {
        option = new LocationClientOption();

        //Hight_Accuracy：高精度、Battery_Saving：低功耗、Device_Sensors：仅使用设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);        //设置定位模式，默认高精度

        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");       //设置返回经纬度坐标类型，默认GCJ02

        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(1000);       //设置发起定位请求的间隔，int类型，单位ms

        option.setOpenGps(true);        //设置是否使用gps，默认false : 使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);   //可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.SetIgnoreCacheException(false);        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5 * 60 * 1000);        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setEnableSimulateGps(false);        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        /* 获取地址信息时设置 */
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);

        /* 获取位置描述 */
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true
        option.setIsNeedLocationDescribe(true);

        /* 周边位置 */
        //可选，是否需要周边POI信息，默认为不需要，即参数为false
        //如果开发者需要获得周边POI信息，此处必须为true
        option.setIsNeedLocationPoiList(true);

    }

    /**
     * 实现定位的监听
     * 异步获取定位结果
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();     //维度
            double longitude = bdLocation.getLongitude();   //经度
            float radius = bdLocation.getRadius();          //定位精度

            String addr = bdLocation.getAddrStr();             //获取详细地址信息
            String country = bdLocation.getCountry();          //获取国家
            String province = bdLocation.getProvince();        //获取省份
            String city = bdLocation.getCity();                //获取城市
            String district = bdLocation.getDistrict();        //获取区县
            String street = bdLocation.getStreet();            //获取街道信息

            String locationDescribe = bdLocation.getLocationDescribe();    //获取位置描述信息   如：在百度大厦北侧
            List<Poi> poiList = bdLocation.getPoiList();                       //获取周边POI信息

            Log.i(TAG, "维度：" + latitude
                    + "\n经度:" + longitude
                    + "\n定位精度:" + radius);
            Log.i(TAG, "详细地址：" + addr + "\n描述信息：" + locationDescribe);
            for (Poi poi:poiList) {
                Log.i(TAG, "周边位置信息:" + poi.getName());
            }

            String coorType = bdLocation.getCoorType();            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = bdLocation.getLocType();               //获取定位类型、定位错误返回码

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient !=null) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }
}