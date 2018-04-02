package com.android.d.parttimejob.Activity.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.d.parttimejob.Activity.Main.LoginActivity;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.DataBase.Dao.CommunicationDao;
import com.android.d.parttimejob.DataBase.Dao.PluralistDao;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Status;

/**
 * 设置页面
 * Created by DJS on 2017/1/22.
 */
public class SettingActivity extends Activity implements View.OnClickListener {

    private RelativeLayout mPhoneRL;//修改电话
    private RelativeLayout mPasswordRL;//修改密码
    private RelativeLayout mHelpRL;//使用帮组
    private RelativeLayout mFeedbackRL;//意见反馈
    private RelativeLayout mUpdateRL;//检查更新

    private Button mQuitBtn;//退出登录

    private NavigationBar mNaviBar;//导航栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        //初始化
        init();
        //设置监听器
        setListener();
    }

    /**
     * 设置监听器
     */
    private void setListener() {

        mPasswordRL.setOnClickListener(this);
        mPhoneRL.setOnClickListener(this);
        mHelpRL.setOnClickListener(this);
        mFeedbackRL.setOnClickListener(this);
        mUpdateRL.setOnClickListener(this);

        mQuitBtn.setOnClickListener(this);

        mNaviBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                SettingActivity.this.finish();
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
        App.mActivities.add(this);

        mPhoneRL = (RelativeLayout) findViewById(R.id.id_setting_phone);
        mPasswordRL = (RelativeLayout) findViewById(R.id.id_setting_password);
        mHelpRL = (RelativeLayout) findViewById(R.id.id_setting_help);
        mFeedbackRL = (RelativeLayout) findViewById(R.id.id_setting_feedback);
        mUpdateRL = (RelativeLayout) findViewById(R.id.id_setting_update);

        mQuitBtn = (Button) findViewById(R.id.id_setting_quit_btn);
        mNaviBar = (NavigationBar) findViewById(R.id.id_setting_navigation_bar);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.id_setting_quit_btn:


                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("退出登录");
                builder.setMessage("是否真的要退出当前登录的用户？退出后该用户的数据将被清除");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除当前配置
                        SharedPreferences sp=getSharedPreferences(Status.PLURALIST,MODE_PRIVATE);
                        SharedPreferences.Editor editor= sp.edit();
                        editor.remove(Status.CURRENT_ID);
                        editor.apply();

                        //删除数据
                        PluralistDao dao=new PluralistDao(SettingActivity.this);
                        dao.delete();

                        CommunicationDao.deleteAll(SettingActivity.this);

                        App.exitAllActivity();

                        //返回登录界面
                        Intent intent=new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        SettingActivity.this.finish();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            //吐槽
            case R.id.id_setting_feedback:
                Intent intent=new Intent(SettingActivity.this,FeedbackActivity.class);
                startActivity(intent);
                break;
            //修改手机号
            case R.id.id_setting_phone:
                intent=new Intent(SettingActivity.this,ChangePhoneActivity.class);
                startActivity(intent);
                break;
            //修改手机号
            case R.id.id_setting_password:
                intent=new Intent(SettingActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
