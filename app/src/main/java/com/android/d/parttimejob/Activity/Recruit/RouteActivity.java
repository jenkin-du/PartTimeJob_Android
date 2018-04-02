package com.android.d.parttimejob.Activity.Recruit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;

import java.util.List;

/**
 * 路线规划
 * Created by DJS on 2017/2/10.
 */
public class RouteActivity extends Activity implements LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener {

    private NavigationBar mNaviBar;

    private MapView mMapView;
    private AMap aMap;

    private OnLocationChangedListener mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mapLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mapLocationClientOption;

    //缩放级别
    private static final float initZoomLevel = 14;

    //定位位置经纬度
    private LatLng LocationLatLng;
    private LatLng markerLatLng;

    //路线规划
    RouteSearch routeSearch;

    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);
        //初始化
        init(savedInstanceState);
        //设置监听器
        setListener();
    }

    /**
     * 设置监听器
     */
    private void setListener() {

        mNaviBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                RouteActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });

        routeSearch.setRouteSearchListener(this);
    }

    /**
     * 初始化
     */
    private void init(Bundle savedInstanceState) {

        App.mActivities.add(this);

        mNaviBar = (NavigationBar) findViewById(R.id.id_route_navi);
        mMapView = (MapView) findViewById(R.id.id_route_map);

        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        routeSearch = new RouteSearch(this);

        // 设置定位监听
        aMap.setLocationSource(this);
        //设置缩放图标位置
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        //设置缩放层级
        aMap.moveCamera(CameraUpdateFactory.zoomTo(initZoomLevel));
        //设置地图类型
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //设置我的位置按钮课件
        aMap.getUiSettings().setMyLocationButtonEnabled(true);

        //获得传过来的数据
        Intent intent = getIntent();
        double latitude = Double.valueOf(intent.getStringExtra("latitude"));
        double longitude = Double.valueOf(intent.getStringExtra("longitude"));

        markerLatLng = new LatLng(latitude, longitude);

        //设置标记点
        MarkerOptions options = new MarkerOptions();
        options.position(markerLatLng);
        Marker marker = aMap.addMarker(options);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        mapLocationClient.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapLocationClient.stopLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                aMapLocation.setLongitude(118.930828);
                aMapLocation.setLatitude(32.113681);

                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                LocationLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                //显示我的位置
                showMyLocation();
                //规划路线
                route();

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AMapErr", errText);
            }
        }
    }

    /**
     * 规划路线
     */
    private void route() {

        LatLonPoint startPoint = new LatLonPoint(LocationLatLng.latitude, LocationLatLng.longitude);
        LatLonPoint endPoint = new LatLonPoint(markerLatLng.latitude, markerLatLng.longitude);

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
//        (Route) routeSearch = routeSearch.calculateRoute(RouteDemoActivity.this, fromAndTo,mode);

        routeSearch.calculateWalkRouteAsyn(query);

    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mapLocationClient == null) {
            //初始化AMapLocationClient，并绑定监听
            mapLocationClient = new AMapLocationClient(getApplicationContext());
            //初始化定位参数
            mapLocationClientOption = new AMapLocationClientOption();
            //设置定位精度
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是否返回地址信息
            mapLocationClientOption.setNeedAddress(true);
            //是否只定位一次
            mapLocationClientOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mapLocationClientOption.setWifiActiveScan(true);
            //是否允许模拟位置
            mapLocationClientOption.setMockEnable(false);
            //定位时间间隔
            mapLocationClientOption.setInterval(2000);
            //给定位客户端对象设置定位参数
            mapLocationClient.setLocationOption(mapLocationClientOption);
            //绑定监听
            mapLocationClient.setLocationListener(this);
            //开启定位
            mapLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mapLocationClient != null) {
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }

    /**
     * 显示出我的位置
     */
    private void showMyLocation() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();

        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.location_marker));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {

                aMap.clear();
                List<WalkPath> walkPaths = result.getPaths();
                if (walkPaths.size() > 0) {

                    WalkPath walkPath = walkPaths.get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            result.getStartPos(),
                            result.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.setNodeIconVisibility(false);

                    if (isFirst) {
                        walkRouteOverlay.zoomToSpan();
                        isFirst = false;
                    }
                } else {
                    Toast.makeText(RouteActivity.this, "没有找到路线", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RouteActivity.this, "没有找到路线", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RouteActivity.this, errorCode, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
