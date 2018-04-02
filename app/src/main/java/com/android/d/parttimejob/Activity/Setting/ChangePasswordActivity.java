package com.android.d.parttimejob.Activity.Setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.MyView.Communication.MyProgressDialog;
import com.android.d.parttimejob.MyView.InputBox;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.ReturnSet;
import com.android.d.parttimejob.Util.Status;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * 修改密码
 * Created by DJS on 2017/1/24.
 */
public class ChangePasswordActivity extends Activity {


    private NavigationBar mNaviBar;

    private InputBox mOldPasswordIB;
    private InputBox mNewPasswordIB;

    private Button mOKBtn;

    private MyProgressDialog mProgressD;

    private static final String URL = App.PREFIX + "Part-timeJob/PluralistServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        //初始化
        init();
        //设置监听器
        setListener();
    }

    private void setListener() {

        mOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOldPasswordIB.getContent().equals("")){
                    Toast.makeText(ChangePasswordActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(mNewPasswordIB.getContent().equals("")){
                    Toast.makeText(ChangePasswordActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    checkPassword();
                }
            }
        });

        mNaviBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                ChangePasswordActivity.this.finish();
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

        mNaviBar = (NavigationBar) findViewById(R.id.id_change_password_navi_bar);
        mOldPasswordIB = (InputBox) findViewById(R.id.id_change_password_old_password);
        mNewPasswordIB = (InputBox) findViewById(R.id.id_change_password_new_password);
        mOKBtn = (Button) findViewById(R.id.id_change_password_ok_btn);

        mProgressD=new MyProgressDialog(this);
    }

    /**
     * 验证密码
     */
    private boolean checkPassword() {

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("action", "validate2");
        paramsMap.put("phone", App.mPluralist.getPhone());
        paramsMap.put("password", mOldPasswordIB.getContent());


        HttpURLTask task = new HttpURLTask(URL, paramsMap, new HttpURLHandler() {

            @Override
            public void handleMessage(Message msg) {
                int responseCode = msg.what;

                if (responseCode == 200) {
                    String jsonStr = (String) msg.obj;
                    ReturnSet set = JSONParser.toJavaBean(jsonStr, new TypeToken<ReturnSet>() {
                    }.getType());
                    String status = set.getStatus();

                    //账号不存在
                    switch (status) {
                        case Status.NON_EXISTENT:
                            mProgressD.dismiss();
                            Toast.makeText(ChangePasswordActivity.this, "账号不存在！", Toast.LENGTH_SHORT).show();
                            //密码错误
                            break;
                        case Status.WRONG:
                            mProgressD.dismiss();
                            Toast.makeText(ChangePasswordActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                            //密码正确
                            break;
                        case Status.RIGHT:
                            //更改密码
                            changePassword();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }


        });
        task.start();

        return false;
    }

    //更改密码
    private void changePassword() {

        HashMap<String,String> param=new HashMap<>();
        param.put("action","changePassword");
        param.put("password",mNewPasswordIB.getContent());
        param.put("pluralistId",App.mPluralist.getId());

        HttpURLTask task=new HttpURLTask(URL,param,new HttpURLHandler(){
            @Override
            public void handleMessage(Message msg) {

                Toast.makeText(ChangePasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                ChangePasswordActivity.this.finish();
            }
        });
        task.start();
    }
}
