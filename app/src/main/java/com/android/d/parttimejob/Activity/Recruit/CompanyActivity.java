package com.android.d.parttimejob.Activity.Recruit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.recruit.Company;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**公司信息页面
 * Created by Administrator on 2016/8/9.
 */
public class CompanyActivity extends Activity {

    private NavigationBar navigationBar;//导航栏

    private String companyId;//公司编号
    private TextView companyName;//公司名称
    private TextView companyAddress;//公司地址
    private TextView companyDescription;//公司简介

    private RatingBar ratingBar;//公司评分
    private TextView ratingTv;//评分数字

    private Company company;//公司

    private ProgressDialog progressDialog;
    private View progressBackground;

    private static final String url= App.PREFIX+"Part-timeJob/CompanyServlet";

    HttpURLHandler handler=new HttpURLHandler(this,new TypeToken<Company>(){}.getType()){
        @Override
        public <T> void handleData(T obj) {
           company= (Company) obj;
            if (company!=null){
                //将数据显示出来
                showDates();
                //取消加载
                progressDialog.dismiss();
                progressBackground.setVisibility(View.GONE);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_detail);
        //初始化
        init();
        //显示进度条
        showLoadingProgress();
        //获得上一个activity传过来的公司id
        getDate();
        //注册监听器
        registerListener();
        //从网络加载数据
        loadDates();

    }

    private void loadDates() {
        HashMap<String,String> params=new HashMap<>();
        params.put("action","company");
        params.put("companyId",companyId);
        HttpURLTask task=new HttpURLTask(url,params,handler);
        task.start();

    }


    /**
     * 注册监听器
     */
    private void registerListener() {
        navigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                CompanyActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });
    }

    /**
     * 初始化
     */
    private void init() {
        App.addActivity(this);

        navigationBar= (NavigationBar) findViewById(R.id.id_company_navi_bar);

        companyName= (TextView) findViewById(R.id.id_company_name);
        companyDescription= (TextView) findViewById(R.id.id_company_description);
        companyAddress= (TextView) findViewById(R.id.id_company_address);

        ratingBar= (RatingBar) findViewById(R.id.id_company_rating_bar);
        ratingTv= (TextView) findViewById(R.id.id_company_rating_bar_tv);

        progressDialog=new ProgressDialog(this);
        progressBackground = findViewById(R.id.id_company_progress_bg);
    }

    /**
     * 从上个activity获得数据
     */
    public void getDate() {
        Intent intent=getIntent();
        companyId=intent.getStringExtra("company_id");

    }

    /**
     * 将数据显示出来
     */
    private void showDates() {
        companyName.setText(company.getName());
        companyDescription.setText(company.getDescription());
        companyAddress.setText(company.getAddress());

        ratingTv.setText(String.valueOf(company.getSatisfaction()));
        ratingBar.setRating(company.getSatisfaction());
    }

    private void showLoadingProgress() {
        progressDialog.setMessage("loading…………");
        progressDialog.setProgressStyle(R.style.dialog);
        progressDialog.show();
    }

}
