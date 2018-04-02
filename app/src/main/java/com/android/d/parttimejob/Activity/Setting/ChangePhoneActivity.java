package com.android.d.parttimejob.Activity.Setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.DataBase.Dao.PluralistDao;
import com.android.d.parttimejob.MyView.Communication.MyProgressDialog;
import com.android.d.parttimejob.MyView.InputBox;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.MyTextWatcher;
import com.android.d.parttimejob.Util.ReturnSet;
import com.android.d.parttimejob.Util.Status;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 修改手机号
 * Created by DJS on 2017/1/22.
 */
public class ChangePhoneActivity extends Activity {

    private NavigationBar mNaviBar;

    private InputBox mOldPhoneIB;
    private InputBox mNewPhoneIB;
    private InputBox mPasswordIB;

    private EditText mValidateCodeET;
    private Button mGetCodeBtn;

    private Button mOKBtn;


    private MyProgressDialog mProgressD;

    private boolean isValidateCodeOk;

    private static final String URL = App.PREFIX + "Part-timeJob/PluralistServlet";

    //短信验证有关
    private static final String APP_KEY = "1625c5ccfc5f6";
    private static final String APP_SECRET = "62e53e91f276666e853e4132f21d7207";
    //处理短信验证返回信息的handler
    private Handler SMSHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //处理来自短息验证的信息
            handleSMSMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone);
        //初始化
        init();
        //设置监听器
        setListener();
        //初始化短信验证，用于验证号码
        initSMSSDK();
    }


    /**
     * 初始化短信验证功能并注册
     */
    private void initSMSSDK() {
        //初始化SMSSDK
        SMSSDK.initSDK(ChangePhoneActivity.this, APP_KEY, APP_SECRET);

        /**
         * 处理短信验证监听接口回调
         */
        EventHandler eventHandler = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {
                //将验证短信的返回结果传出去，用于更新UI操作
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;

                SMSHandler.sendMessage(msg);
            }
        };

        //注册短信验证接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     * 设置监听器
     */
    private void setListener() {

        mNaviBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                ChangePhoneActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });

        mGetCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOldPhoneIB.getContent().length() == 11 ) {

                    String oldPhone = mOldPhoneIB.getContent();
                    mGetCodeBtn.setEnabled(false);

                    //处理倒计时为0市重新发送
                    handleCountdownMsg();

                    //发送电话获取验证码
                    SMSSDK.getVerificationCode("86", oldPhone);
                }
            }
        });

        /**
         * 验证验证码是否输入正确
         */
        mValidateCodeET.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String code = s.toString();

                if (code.length() == 4) {
                    //发送验证码验证是否正确
                    SMSSDK.submitVerificationCode("86", mOldPhoneIB.getContent(), code);
                }
            }
        });

        mOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressD.show();
                if (mNewPhoneIB.getContent().length() != 11) {
                    Toast.makeText(ChangePhoneActivity.this, "新号码错误", Toast.LENGTH_SHORT).show();
                    mProgressD.dismiss();
                } else if (!isValidateCodeOk) {
                    Toast.makeText(ChangePhoneActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    mProgressD.dismiss();
                } else {
                    //验证密码
                    checkPassword();
                }
            }


        });
    }

    /**
     * 更改密码
     */
    private void changePhone() {

        App.mPluralist.setPhone(mNewPhoneIB.getContent());
        PluralistDao dao = new PluralistDao(ChangePhoneActivity.this);
        dao.updatePhone(App.mPluralist.getId(), mNewPhoneIB.getContent());

        HashMap<String, String> params = new HashMap<>();
        params.put("action", "updatePhone");
        params.put("phone", mOldPhoneIB.getContent());
        params.put("pluralistId", App.mPluralist.getId());
        HttpURLTask task = new HttpURLTask(URL, params, new HttpURLHandler());
        task.start();
        Toast.makeText(ChangePhoneActivity.this,"成功修改手机号",Toast.LENGTH_SHORT).show();
        mProgressD.dismiss();
        this.finish();


    }

    /**
     * 验证密码
     */
    private boolean checkPassword() {

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("action", "validate2");
        paramsMap.put("phone", mOldPhoneIB.getContent());
        paramsMap.put("password", mPasswordIB.getContent());

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
                            Toast.makeText(ChangePhoneActivity.this, "账号不存在！", Toast.LENGTH_SHORT).show();
                            //密码错误
                            break;
                        case Status.WRONG:
                            mProgressD.dismiss();
                            Toast.makeText(ChangePhoneActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                            //密码正确
                            break;
                        case Status.RIGHT:
                            //更改密码
                            changePhone();
                    }
                } else {
                    Toast.makeText(ChangePhoneActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.start();

        return false;
    }

    /**
     * 初始化
     */
    private void init() {
        App.mActivities.add(this);

        mNaviBar = (NavigationBar) findViewById(R.id.id_change_phone_navi_bar);
        mOldPhoneIB = (InputBox) findViewById(R.id.id_change_phone_old_phone);
        mNewPhoneIB = (InputBox) findViewById(R.id.id_change_phone_new_phone);
        mPasswordIB = (InputBox) findViewById(R.id.id_change_phone_password);

        mOldPhoneIB.setContent(App.mPluralist.getPhone());

        mValidateCodeET = (EditText) findViewById(R.id.id_change_phone_validate_code);
        mGetCodeBtn = (Button) findViewById(R.id.id_change_phone_obtain_validate_code);
        mOKBtn = (Button) findViewById(R.id.id_change_phone_ok_btn);

        mProgressD = new MyProgressDialog(this);


    }

    /**
     * 处理倒计时为0时重新发送 短信验证
     */
    private void handleCountdownMsg() {

        final int second = 60;//重新发送短信验证码倒计时

        final Handler countdownHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                int leftSecond = msg.what;
                String text = leftSecond + "秒后重新获取";

                if (leftSecond > 0) {
                    mGetCodeBtn.setText(text);
                } else {
                    mGetCodeBtn.setText("重新获取验证码");
                    mGetCodeBtn.setEnabled(true);
                }
            }
        };

        Thread countdownThread = new Thread() {

            @Override
            public void run() {

                for (int i = second; i >= 0; i--) {

                    Message msg = new Message();
                    msg.what = i;

                    countdownHandler.sendMessage(msg);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        countdownThread.start();

    }

    /**
     * 处理来自短信验证的信息
     *
     * @param msg 包含短信验证结果的信息
     */
    private void handleSMSMessage(Message msg) {

        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;

        //操作完成
        if (result == SMSSDK.RESULT_COMPLETE) {
            //获取手机验证码成功
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                //验证码验证成功
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                isValidateCodeOk = true;
                HashMap<String, String> params = (HashMap) data;
                String phone = params.get("phone");
                Log.i("validate_ok-------phone", phone);
            }
        } else if (result == SMSSDK.RESULT_ERROR) {

            if (mValidateCodeET.getText().toString().length() == 4) {

                isValidateCodeOk = false;

            }

            ((Throwable) data).printStackTrace();
            //处理服务器传回来的错误代码
            handleErrorData((Throwable) data);

        }
    }

    /**
     * 处理短信验证错误服务器的错误信息
     *
     * @param error 错误信息
     */
    private void handleErrorData(Throwable error) {

        try {
            JSONObject object = new JSONObject(error.getMessage());

            String des = object.optString("detail");//错误描述
            int status = object.optInt("status");//错误代码
            if (status > 0 && !TextUtils.isEmpty(des)) {
                Toast.makeText(ChangePhoneActivity.this, des, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
