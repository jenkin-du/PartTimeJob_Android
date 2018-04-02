package com.android.d.parttimejob.Fragment.MainFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.android.d.parttimejob.Activity.Main.MainActivity;
import com.android.d.parttimejob.Activity.Recruit.RecruitDetailActivity;
import com.android.d.parttimejob.Adapter.RecruitAdapter;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.recruit.InfoAbstract;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 所有招聘信息展示页
 * Created by D on 2016/5/29.
 */
public class RecruitFragment extends Fragment implements View.OnClickListener {

    private ListView mInfoList;//信息列表
    private TextView locationTV;//定位按钮
    private ImageView mUserHead;//用户头像，点击可弹出用户详情页


    private RelativeLayout distanceLL;
    private RelativeLayout salaryLL;


    private String url = App.PREFIX + "Part-timeJob/InfoServlet";

    private LinearLayout mLinearLayout;//线性布局，用于容纳消息列表

    private ArrayList<InfoAbstract> items;
    private RecruitAdapter adapter;

    private InfoReceiver receiver;
    private LatLng location;

    private HttpURLHandler httpHandler = new HttpURLHandler(getActivity(),
            new TypeToken<ArrayList<InfoAbstract>>() {
            }.getType()) {
        @Override
        public <T> void handleData(T obj) {
            items = (ArrayList<InfoAbstract>) obj;

            if (items != null) {

                calculateDistance(items);

                Intent intent = new Intent("broadcast_items");
                intent.putParcelableArrayListExtra("items", items);
                getActivity().sendBroadcast(intent);

                // 设置数据适配器
                adapter = new RecruitAdapter(getActivity(), items);
                mInfoList.setAdapter(adapter);
////                //设置分割线
//                mInfoList.setDivider(new ColorDrawable(0x744f95c4));
//                mInfoList.setDividerHeight(1);
//                //添加listView
//                mLinearLayout.addView(mInfoList);

            } else {
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
            }

            //点击详情页
            if (items != null) {
                mInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        InfoAbstract item = items.get(position);
                        //将所在城市设置给地址
                        item.getAddress().setCity(App.mCity);

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("infoAbstract", item);
                        intent.putExtra("bundle", bundle);

                        Log.i("infoAbstract", item.toString());
                        intent.setClass(getActivity(), RecruitDetailActivity.class);
                        startActivity(intent);
                    }
                });

}
}
        };

    /**
     * 计算距离
     */
    private void calculateDistance(ArrayList<InfoAbstract> items) {

        double longitude;
        double latitude;

        LatLng latLng;
        int distance = 0;

        for (int i = 0; i < items.size(); i++) {

            longitude = Double.valueOf(items.get(i).getAddress().getLongitude());
            latitude = Double.valueOf(items.get(i).getAddress().getLatitude());

            latLng = new LatLng(latitude, longitude);

            if (location != null) {
                distance = (int) AMapUtils.calculateLineDistance(latLng,location);
                Log.i("distance", "calculateDistance: " + distance);
            } else {
                Log.i("distance", "calculateDistance: null");
            }

            items.get(i).setDistance(distance);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //绘制布局
        View view = inflater.inflate(R.layout.recruit, container, false);
        //初始化
        init(view);
        //设置点击事件监听器
        setClickListener();
        //网络异步操作获取数据
        loadNetWorkData();
        return view;
    }


    /**
     * 网络异步操作获取数据
     */
    public void loadNetWorkData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("action", "abstract");
        params.put("city", App.mCity);

        HttpURLTask task = new HttpURLTask(url, params, httpHandler);
        task.start();

    }


    /**
     * 设置监听事件
     */
    private void setClickListener() {

        mUserHead.setOnClickListener(this);
        locationTV.setOnClickListener(this);
        distanceLL.setOnClickListener(this);
        salaryLL.setOnClickListener(this);

    }

    /**
     * 初始化
     */
    private void init(View view) {

        mInfoList = (ListView) view.findViewById(R.id.id_infoList);
        mUserHead = (ImageView) view.findViewById(R.id.id_usr_head_image_info);
        locationTV = (TextView) view.findViewById(R.id.id_location);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.id_linear);
        distanceLL = (RelativeLayout) view.findViewById(R.id.id_select_distance);
        salaryLL = (RelativeLayout) view.findViewById(R.id.id_select_salary);

        //设置头像
        String imageName = App.mPluralist.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, getActivity());
        if (bitmap != null) {
            mUserHead.setImageBitmap(bitmap);
        }

        receiver=new InfoReceiver();
        IntentFilter filter=new IntentFilter("location");
        getActivity().registerReceiver(receiver,filter);
    }


    /**
     * 处理点击事件
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //弹出侧栏
            case R.id.id_usr_head_image_info:
                MainActivity.slidingMenu.toggle(true);

                break;
            //距离排序
            case R.id.id_select_distance:
                Util.selectDistance(items);
                adapter.notifyDataSetChanged();

                break;
            //薪水排序
            case R.id.id_select_salary:
                Util.selectSalary(items);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }



    @Override
    public void onResume() {
        super.onResume();

        //设置头像
        String imageName = App.mPluralist.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, getActivity());
        if (bitmap != null) {
            mUserHead.setImageBitmap(bitmap);
        }
    }

    /**
     *
     */
    class InfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

             location = intent.getParcelableExtra("latLng");
            if (location != null) {
                locationTV.setText(App.mCity);
                loadNetWorkData();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
