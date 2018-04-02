package com.android.d.parttimejob.Fragment.MainFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.android.d.parttimejob.Activity.Main.MainActivity;
import com.android.d.parttimejob.Activity.Recruit.RecruitDetailActivity;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.recruit.InfoAbstract;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Util;

import java.util.ArrayList;

import static com.android.d.parttimejob.R.id.id_info_window_update_time;


/**
 * 地图模块
 * Created by D on 2016/5/28.
 */
public class MapFragment extends Fragment implements LocationSource, AMapLocationListener, View.OnClickListener {


    private AMap aMap;
    private OnLocationChangedListener mListener;
    private MapView mMapView;
    //声明AMapLocationClient类对象
    private AMapLocationClient mapLocationClient;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mapLocationClientOption;

    //我的位置图标
    private ImageView myLocationImage;

    //我的头像
    private ImageView headImg;

    //定位位置经纬度
    public static LatLng myLocLatLng;

    //缩放级别
    private static final float initZoomLevel = 14;
    private float zoomLevel;

    //广播接收器
    private InfoAbstractReceiver receiver;

    //标记列表
    private ArrayList<Marker> markers;

    private boolean first = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //初始化
        View view = init(inflater, savedInstanceState);
        //设置监听器
        setListener();

        return view;

    }

    /**
     * 设置监听器
     */
    private void setListener() {
        //设置点击监听器
        myLocationImage.setOnClickListener(this);
        headImg.setOnClickListener(this);

        //设置相机移动监听器
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //获得缩放层级
                zoomLevel = cameraPosition.zoom;
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });

        //设置地图点击监听器
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //关闭infowindow
                for (int i = 0; i < markers.size(); i++) {
                    Marker marker = markers.get(i);
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                    }
                }
            }
        });

        //infoWindows的点击事件
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                InfoAbstract info = (InfoAbstract) marker.getObject();
                if (info != null) {

                    //将所在城市设置给地址
                    info.getAddress().setCity(App.mCity);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("infoAbstract", info);
                    intent.putExtra("bundle", bundle);

                    Log.i("infoAbstract", info.toString());
                    intent.setClass(getActivity(), RecruitDetailActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化
     */
    private View init(LayoutInflater inflater, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map, null);
        myLocationImage = (ImageView) view.findViewById(R.id.id_map_my_location);
        headImg = (ImageView) view.findViewById(R.id.id_map_head_img);
        mMapView = (MapView) view.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("broadcast_items");

        receiver = new InfoAbstractReceiver();
        getActivity().registerReceiver(receiver, intentFilter);

        markers = new ArrayList<>();

        zoomLevel = initZoomLevel;
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

        //设置头像
        String imageName = App.mPluralist.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, getActivity());
        if (bitmap != null) {
            headImg.setImageBitmap(bitmap);
        }

        return view;

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                aMapLocation.setLongitude(118.930828);
                aMapLocation.setLatitude(32.113681);

                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                myLocLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                //显示我的位置
                showMyLocation();

                if (first) {
//                    App.mCity = aMapLocation.getCity();
                    App.mCity="南京市";
                    Intent intent = new Intent("location");
                    intent.putExtra("latLng", myLocLatLng);
                    getActivity().sendBroadcast(intent);

                    first = false;
                }


            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AMapErr", errText);
            }
        }
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mapLocationClient == null) {
            //初始化AMapLocationClient，并绑定监听
            mapLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
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

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mapLocationClient != null) {
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mapLocationClient.onDestroy();
        //注销广播
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();

        //设置头像
        String imageName = App.mPluralist.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, getActivity());
        if (bitmap != null) {
            headImg.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapLocationClient.stopLocation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
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

    /**
     * 点击事件处理器
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //我的位置
            case R.id.id_map_my_location:
                //然后可以移动到定位点,使用animateCamera就有动画效果
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocLatLng, 10));
                //设置缩放层级
                aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                break;

            //点击我的头像，弹出侧栏
            case R.id.id_map_head_img:
                MainActivity.slidingMenu.toggle(true);
                break;
            default:
                break;
        }
    }

    /**
     * 广播
     */
    private class InfoAbstractReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            final ArrayList<InfoAbstract> items = intent.getParcelableArrayListExtra("items");

            if (items != null) {

                MarkerOptions options;
                LatLng latLng;


                for (int i = 0; i < items.size(); i++) {

                    InfoAbstract item = items.get(i);
                    double latitude = Double.valueOf(item.getAddress().getLatitude());
                    double longitude = Double.valueOf(item.getAddress().getLongitude());
                    latLng = new LatLng(latitude, longitude);

                    String category = item.getCategory();
                    options = new MarkerOptions();
                    options.position(latLng)
                            .title(items.get(i).getCategory())
                            .icon(BitmapDescriptorFactory.fromBitmap(getPin(category)));

                    Marker marker = aMap.addMarker(options);
                    marker.setObject(item);
                    markers.add(marker);

                    //加载自定义info window
                    aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            View infoWindow = inflater.inflate(R.layout.info_window, null);

                            //渲染
                            render(infoWindow, marker);
                            return infoWindow;
                        }
                    });
                }
            }
        }
    }

    /**
     * 渲染
     *
     * @param infoWindow view
     */
    private void render(View infoWindow, Marker marker) {

        TextView descriptionTV = (TextView) infoWindow.findViewById(R.id.id_info_window_description);
        TextView workTimeTV = (TextView) infoWindow.findViewById(R.id.id_info_window_workTime);
        TextView distanceTV = (TextView) infoWindow.findViewById(R.id.id_info_window_distance);
        TextView salaryTV = (TextView) infoWindow.findViewById(R.id.id_info_window_salary);
        TextView updateTimeTV = (TextView) infoWindow.findViewById(id_info_window_update_time);

        InfoAbstract item = (InfoAbstract) marker.getObject();

        if (item != null) {

            descriptionTV.setText(item.getDescription());
            String time = item.getStartWorkTime() + "(" + item.getWorkDays() + "天)";
            workTimeTV.setText(time);
            //计算距离
            double latitude = Double.valueOf(item.getAddress().getLatitude());
            double longitude = Double.valueOf(item.getAddress().getLongitude());

            LatLng latlng = new LatLng(latitude, longitude);
            float distance = AMapUtils.calculateLineDistance(myLocLatLng, latlng);
            distanceTV.setText(((int) distance) + " m");

            salaryTV.setText(item.getSalary());
            updateTimeTV.setText(item.getUpdateTime());
        }


    }


    /**
     * 根据类型获得相应的pin
     *
     * @param category 类型
     * @return 位图
     */
    private Bitmap getPin(String category) {


        //加载xml布局文件
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.map_pin, null);
        //获得布局文件中的TextView
        TextView categoryTV = (TextView) view.findViewById(R.id.id_pin_category);
        ImageView imageView = (ImageView) view.findViewById(R.id.id_pin_img);
        //设置city的文本信息
        categoryTV.setText(category);
        //根据种类设置背景
        GradientDrawable drawable = (GradientDrawable) imageView.getBackground();
        drawable.setColor(Util.getColorByCategory(category));


        //启用绘图缓存
        view.setDrawingCacheEnabled(true);
        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
        view.measure(View.MeasureSpec.makeMeasureSpec(Util.dip2px(getActivity(), 30), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(Util.dip2px(getActivity(), 30), View.MeasureSpec.EXACTLY));
        //这个方法也非常重要，设置布局的尺寸和位置
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        //获得绘图缓存中的Bitmap
        view.buildDrawingCache();

        return view.getDrawingCache();
    }


}
