package com.android.d.parttimejob.Activity.Recruit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.recruit.InfoAbstract;
import com.android.d.parttimejob.Entry.recruit.InfoDetail;
import com.android.d.parttimejob.Entry.recruit.WorkAddress;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.Status;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.android.d.parttimejob.R.id.id_recruit_detail_phone;

/**
 * 招聘信息详情页面
 * Created by D on 2016/7/15.
 */
public class RecruitDetailActivity extends Activity implements View.OnClickListener {

    private NavigationBar navigationBar;  //导航栏
    private View progressBackground;    //加载时的背景
    private ProgressDialog progressDialog;


    private TextView position;   //工作地点
    private TextView updateTime;//更新时间
    private TextView description;//兼职简介
    private TextView salary;    //工资
    private TextView category;  //兼职类别
    private TextView recruitNumber;//招聘人数
    private TextView recruitedNumber;//已招聘人数
    private TextView genderRequest; //性别要求
    private TextView startWorkTime;    //开始工作时间
    private TextView workTime;     //工作时段
    private TextView workDetail;   //工作内容
    private TextView contactPerson;//联系人姓名
    private TextView contactPhone; //联系电话


    private TextView companyName;  //发布公司名称
    private TextView workAddr;  //工作地址

    private boolean isCollected = false;

    private ImageView collectBtn;  //收藏
//    private ImageView contactBtn;  //咨询
    private ImageView phoneBtn;    //打电话
    private Button applyBtn;      //报名

    private RelativeLayout companyBtn;//公司

    private ImageView locateImg;


    private InfoAbstract infoAbstract;
    private InfoDetail infoDetail;

    private String infoId = "";
    private static final String URL = App.PREFIX + "Part-timeJob/InfoServlet";

    private HttpURLHandler httpURLHandler = new HttpURLHandler(this,
            new TypeToken<InfoDetail>() {
            }.getType()) {
        @Override
        public <T> void handleData(T obj) {
            infoDetail = (InfoDetail) obj;

            if (infoDetail != null) {

                Log.i("detail_infoDetail", infoDetail.toString());
                //根据infoDetail完成数据
                finishDetail();

                //取消加载精度条
                progressBackground.setVisibility(View.GONE);
                progressDialog.dismiss();

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruit_detail);
        //初始化
        init();
        //注册监听器
        registerListener();
        //显示加载进度条
        showLoadingProgress();
        //获得上一个activity传来的数据
        infoAbstract = getData();
        //根据上一个爱吃提不提也传来的数据完成部分内容
        finishPart(infoAbstract);
        //判断此招聘信息是否被申请，若是，则无法申请
        judgeIsApplied();
        //判断是否被收藏
        judgeIsCollected();
        //新建一个线程，开始网络获取数据
        Log.i("detail_infoId", String.valueOf(infoId));
        //网络加载其余数据
        loadData();

    }

    /**
     * 判断是否被收藏
     */
    private void judgeIsCollected() {
        ArrayList<String> list = App.getCollectedInfoList();

        for (int i = 0; i < list.size(); i++) {
            if (infoId.equals(list.get(i))) {
                collectBtn.setImageResource(R.drawable.star_full);
                isCollected = true;
            }
        }

    }

    /**
     * 判断此招聘信息是否被申请，若是，则无法申请
     */
    private void judgeIsApplied() {

        HashMap<String, String> map = App.getEnrolledInfoList();

        Set<String> set = map.keySet();

        for (String id : set) {

            if (id.equals(infoId)) {

                applyBtn.setEnabled(false);
                String status = map.get(infoId);

                if (status.equals(Status.ENROLLED)) {
                    applyBtn.setText("已经报名");
                }
                if (status.equals(Status.EMPLOYED)) {
                    applyBtn.setText("已经录用");
                }
                if (status.equals(Status.WORKED)) {
                    applyBtn.setText("已经工作");
                }
                if (status.equals(Status.FINISHED)) {
                    applyBtn.setText("已经完成");
                }

                break;
            }

        }


    }

    /**
     * 从网络加载数据
     */
    private void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("action", "detail");
        params.put("infoId", infoId);

        Log.i("detail_loadData_infoId", infoId);
        HttpURLTask urlTask = new HttpURLTask(URL, params, httpURLHandler);
        urlTask.start();
    }

    private void showLoadingProgress() {
        Log.i("detail_dialog", "--->showing dialog!!!!!!!");
        progressDialog.setMessage("loading…………");
        progressDialog.setProgressStyle(R.style.dialog);
        progressDialog.show();
    }

    /**
     * 根据简介完成部分数据
     */
    private void finishPart(InfoAbstract item) {

        if (item != null) {
            description.setText(item.getDescription());

            WorkAddress address = item.getAddress();
            String pos = address.getDistrict() + "/" + item.getDistance() + "m";
            position.setText(pos);

            updateTime.setText(item.getUpdateTime());
            salary.setText(item.getSalary());
            category.setText(item.getCategory());
            String time = item.getStartWorkTime() + "(共" + item.getWorkDays() + "天)";
            startWorkTime.setText(time);

            infoId = item.getiId();
        }
    }


    /**
     * 根据infoDetail完成数据
     */
    private void finishDetail() {
        //数据库中获得的信息设置
        recruitedNumber.setText(infoDetail.getRecruitedNumber() + "");
        recruitNumber.setText(infoDetail.getRecruitNumber() + "");
        genderRequest.setText(infoDetail.getGenderRequest());

        workTime.setText(infoDetail.getWorkTime());
        workDetail.setText(infoDetail.getWorkDetail());

        contactPerson.setText(infoDetail.getContactName());
        contactPhone.setText(infoDetail.getContactPhone());
        companyName.setText(infoDetail.getCompany());

        WorkAddress a1 = infoDetail.getAddress();
        WorkAddress a2 = infoAbstract.getAddress();
//        Log.i("detail_a1", a1.toString());
//        Log.i("detail_a2", a2.toString());
        if(a1!=null){
            String address = a1.getProvince() + a2.getCity() + a2.getDistrict() + a1.getDetailAddr();
            workAddr.setText(address);
        }



    }


    /**
     * 从上一个activity中获得数据
     */
    private InfoAbstract getData() {
        InfoAbstract item = null;
        Bundle bundle = getIntent().getBundleExtra("bundle");


        if (bundle != null && !bundle.isEmpty()) {
            item = bundle.getParcelable("infoAbstract");
            Log.i("detail_info_abstract", "--->" + (item != null ? item.toString() : null));
        }
        return item;
    }


    /**
     * 注册监听器
     */
    private void registerListener() {


        applyBtn.setOnClickListener(this);
        navigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                progressDialog.dismiss();
                RecruitDetailActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });
        collectBtn.setOnClickListener(this);
        phoneBtn.setOnClickListener(this);
//        contactBtn.setOnClickListener(this);

        companyBtn.setOnClickListener(this);

        locateImg.setOnClickListener(this);
    }


    /**
     * 初始化
     */
    private void init() {

        App.addActivity(this);

        navigationBar = (NavigationBar) findViewById(R.id.id_navi_bar);
        progressBackground = findViewById(R.id.id_progress_bg);
        progressDialog = new ProgressDialog(this);

        position = (TextView) findViewById(R.id.id_recruit_detail_palce);
        updateTime = (TextView) findViewById(R.id.id_recruit_detail_updateTime);
        description = (TextView) findViewById(R.id.id_recruit_detail_description);
        salary = (TextView) findViewById(R.id.id_recruit_detail_salary);
        category = (TextView) findViewById(R.id.id_recruit_detail_category);
        recruitedNumber = (TextView) findViewById(R.id.id_recruit_detail_recruited_number);
        recruitNumber = (TextView) findViewById(R.id.id_recruit_detail_recruit_number);
        genderRequest = (TextView) findViewById(R.id.id_recruit_detail_sex_request);
        startWorkTime = (TextView) findViewById(R.id.id_recruit_detail_start_time);
        workTime = (TextView) findViewById(R.id.id_recruit_detail_work_time);
        workDetail = (TextView) findViewById(R.id.id_recruit_detail_work_detail);
        companyName = (TextView) findViewById(R.id.id_recruit_detail_company_name);
        workAddr = (TextView) findViewById(R.id.id_recruit_detail_company_addr);
        contactPerson = (TextView) findViewById(R.id.id_recruit_detail_company_contact_person);
        contactPhone = (TextView) findViewById(R.id.id_recruit_detail_company_contact_phone);


//        contactBtn = (ImageView) findViewById(R.id.id_recruit_detail_contact);
        collectBtn = (ImageView) findViewById(R.id.id_recruit_detail_collect);
        phoneBtn = (ImageView) findViewById(id_recruit_detail_phone);
        applyBtn = (Button) findViewById(R.id.id_recruit_detail_apply);


        companyBtn = (RelativeLayout) findViewById(R.id.id_recruit_detail_company);

        locateImg = (ImageView) findViewById(R.id.id_recruit_detail_location);
    }


    /**
     * 处理点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //打开公司详情页
            case R.id.id_recruit_detail_company:

                if (infoDetail != null) {
                    String companyId = infoDetail.getCompanyId();

                    Intent intent = new Intent();
                    intent.putExtra("company_id", companyId);
                    intent.setClass(RecruitDetailActivity.this, CompanyActivity.class);
                    startActivity(intent);
                }
                break;

            //申请报名
            case R.id.id_recruit_detail_apply:
                if (infoId != null) {

                    Intent intent = new Intent(RecruitDetailActivity.this, ApplyActivity.class);
                    intent.putExtra("infoId", infoId);
                    startActivity(intent);
                }

                break;

            //收藏
            case R.id.id_recruit_detail_collect:

                //收藏功能
                collectionFunction();
                break;

            //定位
            case R.id.id_recruit_detail_location:

                Intent intent = new Intent(RecruitDetailActivity.this, RouteActivity.class);
                String longitude = infoAbstract.getAddress().getLongitude();
                String latitude = infoAbstract.getAddress().getLatitude();

                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
                break;

            //打电话
            case R.id.id_recruit_detail_phone:

                Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + infoDetail.getContactPhone()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(phoneIntent);

                break;
            default:
                break;
        }
    }

    /**
     * 收藏功能
     */
    private void collectionFunction() {
        String url = App.PREFIX+"Part-timeJob/RecruitServlet";

        HashMap<String, String> params = new HashMap<>();

        if (!isCollected) {
            params.put("action", "collect");
            isCollected = true;

            collectBtn.setImageResource(R.drawable.star_full);
        } else {
            params.put("action", "cancelCollect");
            isCollected = false;

            collectBtn.setImageResource(R.drawable.star_empty);
        }


        params.put("infoId", infoId);
        params.put("pluralistId",  App.mPluralist.getId());

        final HttpURLTask task = new HttpURLTask(url, params, new HttpURLHandler() {

            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 200) {
                    String status = (String) msg.obj;

                    if (status.equals(Status.SUCCESSFUL)) {
                        if (isCollected) {
                            //添加到全局变量中
                            App.getCollectedInfoList().add(infoId);

                            Toast.makeText(RecruitDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            //从全局变量中删除
                            App.getCollectedInfoList().remove(infoId);

                            Toast.makeText(RecruitDetailActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                        }
                    } else if (status.equals(Status.FAILING)) {

                        Toast.makeText(RecruitDetailActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        if (isCollected) {
                            isCollected = false;

                            collectBtn.setImageResource(R.drawable.star_empty);
                        } else {
                            isCollected = true;

                            collectBtn.setImageResource(R.drawable.star_full);
                        }
                    }
                } else {
                    if (isCollected) {
                        isCollected = false;

                        collectBtn.setImageResource(R.drawable.star_empty);
                    } else {
                        isCollected = true;

                        collectBtn.setImageResource(R.drawable.star_full);
                    }
                    Toast.makeText(RecruitDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }


            }
        });
        task.start();
    }


    @Override
    protected void onResume() {
        super.onResume();

        judgeIsApplied();
    }
}
