package com.android.d.parttimejob.Fragment.MainFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Activity.Personal.AccountActivity;
import com.android.d.parttimejob.Activity.Personal.MissionActivity;
import com.android.d.parttimejob.Activity.Personal.ResumeActivity;
import com.android.d.parttimejob.Activity.Setting.SettingActivity;
import com.android.d.parttimejob.Activity.Recruit.CollectedInfoActivity;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**侧栏
 * Created by D on 2016/6/1.
 */
public class SlidingFragment extends Fragment implements View.OnClickListener{

    private LinearLayout mResumeLL;//简历
    private LinearLayout mMoneyBagLL;//钱包
    private LinearLayout mMissionLL;//任务
    private LinearLayout mCollectionLL;//收藏
    private LinearLayout mSettingLL;//设置


    public static TextView mCollectionNum;
    public static TextView mSalary;
    private  TextView mUserName;
    private TextView mPhone;

    private ImageView mUserHead;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.sliding_content,container,false);

        //初始化
        initView(view);
        //设置监听器
        setOnClickListener();
//        //加载兼职者的薪水
//        loadSalary();
        //加载已报名的信息的id
        loadEnrolledInfoList();
        //加载收藏的信息id
        loadCollectedInfoList();
        return view;
    }

    /**
     * 设置监听器
     */
    private void setOnClickListener() {
        mResumeLL.setOnClickListener(this);
        mMoneyBagLL.setOnClickListener(this);
        mMissionLL.setOnClickListener(this);
        mCollectionLL.setOnClickListener(this);
        mSettingLL.setOnClickListener(this);

    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mResumeLL = (LinearLayout) view.findViewById(R.id.id_sliding_resume);
        mMoneyBagLL= (LinearLayout) view.findViewById(R.id.id_sliding_money_bag);
        mMissionLL= (LinearLayout) view.findViewById(R.id.id_sliding_mission);
        mCollectionLL= (LinearLayout) view.findViewById(R.id.id_sliding_collection);
        mSettingLL= (LinearLayout) view.findViewById(R.id.id_sliding_setting);

        mCollectionNum= (TextView) view.findViewById(R.id.id_sliding_collection_number);
        mSalary= (TextView) view.findViewById(R.id.id_sliding_salary);
        mUserName= (TextView) view.findViewById(R.id.id_usr_name_sliding);
        mPhone= (TextView) view.findViewById(R.id.id_usr_phone_sliding);
        mUserHead= (ImageView) view.findViewById(R.id.id_usr_head_image_sliding);

        mSalary.setText(App.mPluralist.getSalary()+"");
        mPhone.setText(App.mPluralist.getPhone());
        mUserName.setText(App.mPluralist.getName());

        //设置头像
        String imageName = App.mPluralist.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, getActivity());
        if (bitmap != null) {
            mUserHead.setImageBitmap(bitmap);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mSalary.setText(App.mPluralist.getSalary()+"");
        mPhone.setText(App.mPluralist.getPhone());
        mUserName.setText(App.mPluralist.getName());

        //设置头像
        String imageName = App.mPluralist.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, getActivity());
        if (bitmap != null) {
            mUserHead.setImageBitmap(bitmap);
        }
    }

    /**
     * 处理点击事件
     */
    @Override
    public void onClick(View v) {

        Intent intent =new Intent();

        switch (v.getId()){
            //打开简历页面
            case R.id.id_sliding_resume:


                intent.setClass(getActivity(),ResumeActivity.class);
                startActivity(intent);

                break;
            //打开钱包页面
            case R.id.id_sliding_money_bag:

                intent.setClass(getActivity(), AccountActivity.class);
                startActivity(intent);
                break;
            //任务
            case R.id.id_sliding_mission:

                intent.setClass(getActivity(), MissionActivity.class);
                startActivity(intent);
                break;
            //收藏
            case R.id.id_sliding_collection:

                intent.setClass(getActivity(), CollectedInfoActivity.class);
                startActivity(intent);
                break;
            //设置
            case R.id.id_sliding_setting:
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 加载已经报名的info id list
     */
    private void loadEnrolledInfoList() {

        String pId = App.mPluralist.getId();

        String url = App.PREFIX + "Part-timeJob/RecruitServlet";

        HashMap<String, String> params = new HashMap<>();
        params.put("action", "getEnrolledInfoList");
        params.put("pluralistId", pId);

        HttpURLTask task = new HttpURLTask(url, params, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 200) {

                    String jsonStr = (String) msg.obj;

                    HashMap<String, String> map = JSONParser.toJavaBean(jsonStr, new TypeToken<HashMap<String, String>>() {
                    }.getType());

                    App.setEnrolledInfoList(map);


                } else {
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        });

        task.start();

    }

    /**
     * 加载收藏的信息id list
     */
    private void loadCollectedInfoList() {
        String pId = App.mPluralist.getId();

        String url = App.PREFIX + "Part-timeJob/RecruitServlet";

        HashMap<String, String> params = new HashMap<>();
        params.put("action", "getCollectedInfoList");
        params.put("pluralistId", pId);

        HttpURLTask task = new HttpURLTask(url, params, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 200) {

                    String jsonStr = (String) msg.obj;

                    ArrayList<String> list = JSONParser.toJavaBean(jsonStr, new TypeToken<ArrayList<String>>() {
                    }.getType());

                    App.setCollectedInfoList(list);

                } else {
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        });

        task.start();

    }


//    /**
//     * 获取兼职者的薪水
//     */
//    private void loadSalary() {
//
//        String url = App.PREFIX + "Part-timeJob/PluralistServlet";
//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("action", "getSalary");
//        params.put("pluralistId", App.ID);
//
//        HttpURLTask task = new HttpURLTask(url, params, new HttpURLHandler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//
//                if (msg.what == 200) {
//
//                    String jsonStr = (String) msg.obj;
//                    ResponseSet set = JSONParser.toJavaBean(jsonStr, new TypeToken<ResponseSet>() {
//                    }.getType());
//                    String status = set.getStatus();
//
//                    if (status.equals(Status.SUCCESSFUL)) {
//                        //将获取到的钱放在全局变量中
//                        double salary = (double) set.getObj();
//                        App.mSalary = salary + "";
//
//                    } else if (status.equals(Status.FAILING)) {
//                        Toast.makeText(getActivity(), "服务器出错", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        task.start();
//    }



}
