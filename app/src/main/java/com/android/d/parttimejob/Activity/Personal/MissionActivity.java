package com.android.d.parttimejob.Activity.Personal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Fragment.MissionFragment.EmployedFragment;
import com.android.d.parttimejob.Fragment.MissionFragment.EnrolledFragment;
import com.android.d.parttimejob.Fragment.MissionFragment.FinishedFragment;
import com.android.d.parttimejob.Fragment.MissionFragment.WorkedFragment;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 任务页面
 * Created by Administrator on 2016/8/19.
 */
public class MissionActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private NavigationBar mNavigationBar;//导航栏

    //四个页面的标题栏
    private TextView enrolledBar;// 已报名的标题栏
    private TextView employedBar;// 已录取的标题栏
    private TextView workedBar;// 已工作的标题栏
    private TextView finishedBar;// 已完成的标题栏

    //每个工作状态的数量
    private TextView enrolledNumTv;//已报名的数量
    private TextView employedNumTV;//已录取的数量
    private TextView workedNumTV;//已工作的数量
    private TextView finishedNumTV;//已完成的数量

    //指示器
    private ImageView enrolledIndicator;//已报名的指示器
    private ImageView employedIndicator;//已录取的指示器
    private ImageView workedIndicator;//已工作的指示器
    private ImageView finishedIndicator;//已完成的指示器


    //viewPager
    private ViewPager mPager;
    private List<Fragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission);
        //初始化
        initView();
        //注册监听器
        registerClickListener();
        //addFragment
        addFragments();
        //显示每个状态的数量
        showStatusNumber();
    }

    /**
     * 显示每个状态的数量
     */
    private void showStatusNumber() {

        int enrolledNum = 0;
        int employedNum = 0;
        int workedNum = 0;
        int finishedNum = 0;

        HashMap<String, String> map = App.getEnrolledInfoList();
        Set<String> set = map.keySet();

        for (String id : set) {

            String status = map.get(id);
            switch (status) {

                case Status.ENROLLED:
                    enrolledNum++;
                    break;
                case Status.EMPLOYED:
                    employedNum++;
                    break;
                case Status.WORKED:
                    workedNum++;
                    break;
                case Status.FINISHED:
                    finishedNum++;
                    break;

            }
        }

        enrolledNumTv.setText(enrolledNum+"");
        employedNumTV.setText(employedNum+"");
        workedNumTV.setText(workedNum+"");
        finishedNumTV.setText(finishedNum+"");

    }


    /**
     * 增加fragment
     */
    private void addFragments() {

        fragmentList.add(new EnrolledFragment());
        fragmentList.add(new EmployedFragment());
        fragmentList.add(new WorkedFragment());
        fragmentList.add(new FinishedFragment());


        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        mPager.setAdapter(adapter);
        mPager.setCurrentItem(0);

    }

    /**
     * 注册监听器
     */
    private void registerClickListener() {
        //导航栏
        mNavigationBar.registerListener(new NavigationBar.OnClickListener() {
            //返回
            @Override
            public void onClickBack() {
                MissionActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });

        mPager.addOnPageChangeListener(this);

        enrolledBar.setOnClickListener(this);
        employedBar.setOnClickListener(this);
        workedBar.setOnClickListener(this);
        finishedBar.setOnClickListener(this);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        App.addActivity(this);

        mNavigationBar = (NavigationBar) findViewById(R.id.id_mission_navigation_bar);

        mPager = (ViewPager) findViewById(R.id.id_mission_view_pager);

        fragmentList = new ArrayList<>();

        enrolledBar = (TextView) findViewById(R.id.id_mission_enrolled);
        employedBar = (TextView) findViewById(R.id.id_mission_employed);
        workedBar = (TextView) findViewById(R.id.id_mission_worked);
        finishedBar = (TextView) findViewById(R.id.id_mission_finished);

        enrolledNumTv = (TextView) findViewById(R.id.id_mission_enrolled_num);
        employedNumTV = (TextView) findViewById(R.id.id_mission_employed_mun);
        workedNumTV = (TextView) findViewById(R.id.id_mission_worked_num);
        finishedNumTV = (TextView) findViewById(R.id.id_mission_finished_num);

        enrolledIndicator = (ImageView) findViewById(R.id.id_mission_enrolled_indicator);
        employedIndicator = (ImageView) findViewById(R.id.id_mission_employed_indicator);
        workedIndicator = (ImageView) findViewById(R.id.id_mission_worked_indicator);
        finishedIndicator = (ImageView) findViewById(R.id.id_mission_finished_indicator);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_mission_enrolled:
                setCurrentItem(0);

                break;
            case R.id.id_mission_employed:
                setCurrentItem(1);

                break;
            case R.id.id_mission_worked:
                setCurrentItem(2);

                break;
            case R.id.id_mission_finished:
                setCurrentItem(3);

                break;
            default:
                break;
        }
    }


    private void setCurrentItem(int position) {

        mPager.setCurrentItem(position);

        switch (position) {
            case 0:

                enrolledIndicator.setVisibility(View.VISIBLE);

                employedIndicator.setVisibility(View.INVISIBLE);
                workedIndicator.setVisibility(View.INVISIBLE);
                finishedIndicator.setVisibility(View.INVISIBLE);

                enrolledBar.setTextColor(getResources().getColor(R.color.text_color));

                employedBar.setTextColor(getResources().getColor(R.color.onclick));
                workedBar.setTextColor(getResources().getColor(R.color.onclick));
                finishedBar.setTextColor(getResources().getColor(R.color.onclick));

                break;
            case 1:

                employedIndicator.setVisibility(View.VISIBLE);

                enrolledIndicator.setVisibility(View.INVISIBLE);
                workedIndicator.setVisibility(View.INVISIBLE);
                finishedIndicator.setVisibility(View.INVISIBLE);

                employedBar.setTextColor(getResources().getColor(R.color.text_color));

                enrolledBar.setTextColor(getResources().getColor(R.color.onclick));
                workedBar.setTextColor(getResources().getColor(R.color.onclick));
                finishedBar.setTextColor(getResources().getColor(R.color.onclick));

                break;
            case 2:

                workedIndicator.setVisibility(View.VISIBLE);

                employedIndicator.setVisibility(View.INVISIBLE);
                enrolledIndicator.setVisibility(View.INVISIBLE);
                finishedIndicator.setVisibility(View.INVISIBLE);

                workedBar.setTextColor(getResources().getColor(R.color.text_color));

                employedBar.setTextColor(getResources().getColor(R.color.onclick));
                enrolledBar.setTextColor(getResources().getColor(R.color.onclick));
                finishedBar.setTextColor(getResources().getColor(R.color.onclick));

                break;
            case 3:

                finishedIndicator.setVisibility(View.VISIBLE);

                employedIndicator.setVisibility(View.INVISIBLE);
                workedIndicator.setVisibility(View.INVISIBLE);
                enrolledIndicator.setVisibility(View.INVISIBLE);

                finishedBar.setTextColor(getResources().getColor(R.color.text_color));

                employedBar.setTextColor(getResources().getColor(R.color.onclick));
                workedBar.setTextColor(getResources().getColor(R.color.onclick));
                enrolledBar.setTextColor(getResources().getColor(R.color.onclick));


                break;

            default:
                break;

        }
    }
}
