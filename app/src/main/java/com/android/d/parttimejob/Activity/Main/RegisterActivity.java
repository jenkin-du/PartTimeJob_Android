package com.android.d.parttimejob.Activity.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
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
 * 注册Activity
 * Created by D on 2016/5/26.
 */
public class RegisterActivity extends Activity {

    private static final int PASSWORD_LENGTH = 8;

    private NavigationBar mNaviBar;

    private EditText mName;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mConfirmedPassword;

    private EditText mValidateCode;//验证码
    private Button mObtainValidateCodeBtn;//获取验证码

    private CheckBox mReadProtocol;//阅读兼职协议选择器
    private TextView mProtocol;//兼职协议

    private Button mRegisterBtn;//注册

    //输入框后面的小图标
    private ImageView mNameOkImg;
    private ImageView mPhoneOkImg;
    private ImageView mPwdOkImg;
    private ImageView mConfirmPwdOkImg;
    private ImageView mValidateCodeOkImg;

    //标记注册内容是否完成
    private boolean isNameOk = false;
    private boolean isPhoneOk = false;
    private boolean isPwdOk = false;
    private boolean isCPwdOk = false;
    private boolean isValidateCodeOk = false;
    private boolean isChecked = false;

    private String url = App.PREFIX + "Part-timeJob/PluralistServlet";

    private ProgressDialog progressDialog;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        //初始化控件
        initView();
        //初始化短信验证，用于验证号码
        initSMSSDK();
        //设置editView的文字监听器，监听文字变化
        setTextWatcher();
        //设置各个点击事件
        setClickEvent();


    }


    /**
     * 设置文字监听器,监听文字变化，用于输入正确与否判断
     */
    private void setTextWatcher() {
        /**
         * 验证名字是否正确
         */
        mName.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                if (name.length() < 2) {
                    mNameOkImg.setImageResource(R.drawable.error);//设置错误图标
                    mNameOkImg.setVisibility(View.VISIBLE);
                } else {
                    mNameOkImg.setVisibility(View.VISIBLE);
                    mNameOkImg.setImageResource(R.mipmap.ok);//设置错误图标
                    isNameOk = true;
                }
            }

        });

        /**
         * 验证电话是否存在
         */
        mPhone.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString();

                if (s.length() == 11) {

                    CheckPhoneThread thread = new CheckPhoneThread(phone, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            String status = (String) msg.obj;
                            if (status.equals(Status.EXISTENT)) {

                                mPhoneOkImg.setImageResource(R.drawable.error);//设置错误图标
                                mPhoneOkImg.setVisibility(View.VISIBLE);

                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "号码已被注册！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new HttpURLHandler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 200) {

                                String jsonStr = (String) msg.obj;
                                ReturnSet returnSet = JSONParser.toJavaBean(jsonStr, new TypeToken<ReturnSet>() {
                                }.getType());

                                String status = returnSet.getStatus();
                                if (status.equals(Status.EXISTENT)) {

                                    mPhoneOkImg.setImageResource(R.drawable.error);//设置错误图标
                                    mPhoneOkImg.setVisibility(View.VISIBLE);

                                    Toast.makeText(RegisterActivity.this, "号码已被注册！", Toast.LENGTH_SHORT).show();

                                } else if (status.equals(Status.NON_EXISTENT)) {
                                    //验证正确
                                    mPhoneOkImg.setImageResource(R.mipmap.ok);
                                    mPhoneOkImg.setVisibility(View.VISIBLE);
                                    isPhoneOk = true;
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    thread.start();
                } else {

                    mPhoneOkImg.setVisibility(View.INVISIBLE);
                }

            }
        });

        //
        /**
         * 验证密码是否输入正确
         */
        mPassword.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String pwd = s.toString();

                if (!pwd.equals("")) {

                    if (s.toString().length() < PASSWORD_LENGTH) {

                        mPwdOkImg.setImageResource(R.drawable.error);//设置错误图标
                        mPwdOkImg.setVisibility(View.VISIBLE);

                    } else {
                        mPwdOkImg.setImageResource(R.mipmap.ok);
                        mPwdOkImg.setVisibility(View.VISIBLE);
                        isPwdOk = true;
                    }
                } else {
                    mPwdOkImg.setImageResource(R.drawable.error);//设置错误图标
                    mPwdOkImg.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();

                }
            }
        });

        /**
         * 验证确认密码是否输入正确
         */
        mConfirmedPassword.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String password = mPassword.getText().toString();

                if (!password.equals("")) {

                    if (s.toString().equals(password)) {

                        mConfirmPwdOkImg.setImageResource(R.mipmap.ok);
                        mConfirmPwdOkImg.setVisibility(View.VISIBLE);//显示ok
                        isCPwdOk = true;
                    } else {

                        mConfirmPwdOkImg.setImageResource(R.drawable.error);//设置错误图标
                        mConfirmPwdOkImg.setVisibility(View.VISIBLE);//显示ok

                    }
                } else {
                    mPwdOkImg.setImageResource(R.drawable.error);
                    mPwdOkImg.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /**
         * 验证验证码是否输入正确
         */
        mValidateCode.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String code = s.toString();

                if (code.length() == 4) {
                    //发送验证码验证是否正确
                    SMSSDK.submitVerificationCode("86", mPhone.getText().toString(), code);
                }
            }
        });
    }


    /**
     * 设置点击事件
     */
    private void setClickEvent() {


        mNaviBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
//                RegisterActivity.this.finish();
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });

        //注册
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册
                register();
            }
        });

        //  阅读兼职协议
        mReadProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //勾选了就可以注册了
                RegisterActivity.this.isChecked = isChecked;
                if (isNameOk && isPhoneOk && isPwdOk && isCPwdOk && isChecked && isValidateCodeOk) {
                    mRegisterBtn.setEnabled(true);
                    mReadProtocol.setChecked(true);
                } else {
                    mRegisterBtn.setEnabled(false);
                    mReadProtocol.setChecked(false);
                    Toast.makeText(RegisterActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // TODO: 2016/8/15 打开兼职协议页面
        mProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //点击短信验证
        mObtainValidateCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isPhoneOk) {
                    String phone = mPhone.getText().toString();

                    mObtainValidateCodeBtn.setEnabled(false);

                    //处理倒计时为0市重新发送
                    handleCountdownMsg();

                    //发送电话获取验证码
                    SMSSDK.getVerificationCode("86", phone);
                } else {
                    //电话号码错误
                    Toast.makeText(RegisterActivity.this, "号码错误", Toast.LENGTH_SHORT).show();
                }


            }
        });
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
                    mObtainValidateCodeBtn.setText(text);
                } else {
                    mObtainValidateCodeBtn.setText("重新获取验证码");
                    mObtainValidateCodeBtn.setEnabled(true);
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
     * 点击注册按钮进行注册
     */
    private void register() {
        progressDialog.show();

        final String name = mName.getText().toString();
        final String phone = mPhone.getText().toString();
        final String password = mConfirmedPassword.getText().toString();

//        if(!isNameOk){
//
//            if (mName.getText().toString().equals("")){
//                Toast.makeText(RegisterActivity.this,"名字不能为空",Toast.LENGTH_SHORT).show();
//
//            }else if (mName.getText().toString().length()<2){
//                Toast.makeText(RegisterActivity.this,"名字不能太短",Toast.LENGTH_SHORT).show();
//
//            }
//        }else if (!isPhoneOk){
//            if (mPhone.getText().toString().equals("")){
//                Toast.makeText(RegisterActivity.this,"电话不能为空",Toast.LENGTH_SHORT).show();
//
//            }else if (mPhone.getText().toString().length()==11){
//                Toast.makeText(RegisterActivity.this,"号码已被注册",Toast.LENGTH_SHORT).show();
//
//            }
//        }else if (!isPwdOk){
//            if (mPassword.getText().toString().equals("")){
//                Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
//
//            }else if (mPassword.getText().toString().length()<8){
//                Toast.makeText(RegisterActivity.this,"密码必须大于8位",Toast.LENGTH_SHORT).show();
//
//            }
//        }else if(!isCPwdOk){
//            if (mValidateCode.getText().toString().equals("")){
//                Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
//
//            }else if (mValidateCode.getText().toString().length()<8){
//                Toast.makeText(RegisterActivity.this,"密码必须大于8位",Toast.LENGTH_SHORT).show();
//
//            }else if (!mValidateCode.getText().toString().equals(mPassword.getText().toString())){
//                Toast.makeText(RegisterActivity.this,"密码输入错误",Toast.LENGTH_SHORT).show();
//
//            }
//        }else if (!isValidateCodeOk){
//            if (mValidateCode.getText().toString().equals("")){
//                Toast.makeText(RegisterActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
//
//            }else if (mValidateCode.getText().toString().length()==4){
//                Toast.makeText(RegisterActivity.this,"验证码错误或或后台服务器出现错误",Toast.LENGTH_SHORT).show();
//
//            }else if (mValidateCode.getText().toString().length()<4){
//                Toast.makeText(RegisterActivity.this,"请输入完整验证码",Toast.LENGTH_SHORT).show();
//
//            }
//        }
        if (isNameOk && isPhoneOk && isPwdOk && isCPwdOk && isChecked && isValidateCodeOk) {

            //开启线程插入
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "insert");
            params.put("name", name);
            params.put("phone", phone);
            params.put("password", password);

            HttpURLTask task = new HttpURLTask(url, params, new HttpURLHandler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 200) {
                        String jsonStr = (String) msg.obj;
                        ReturnSet returnSet = JSONParser.toJavaBean(jsonStr, new TypeToken<ReturnSet>() {
                        }.getType());

                        String status = returnSet.getStatus();
                        //注册插入信息成功
                        if (status.equals(Status.SUCCESSFUL)) {
//                            //将记录存在本地数据库中，尽可能地避免网络操作
//                            Pluralist p = new Pluralist();
//                            p.setId(returnSet.getPluralist().getId());
//                            p.setName(name);
//                            p.setPhone(phone);
//                            p.setPassword(password);
//
//                            PluralistDao dao = new PluralistDao(RegisterActivity.this);
//                            dao.insert(p);

                            //打开下一个activity
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this, LoginActivity.class);
                            // TODO: 2016/8/17
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();//取消进度
                            startActivity(intent);
                        } else {
                            //插入失败
                            progressDialog.dismiss();//取消进度
                            Toast.makeText(RegisterActivity.this, "注册出现错误，请稍后重试一次！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.i("error code", String.valueOf(msg.what));
                        //网络异常
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            task.start();//开始线程

        }
    }


    /**
     * 初始化
     */
    private void initView() {
        App.addActivity(this);

        mNaviBar = (NavigationBar) findViewById(R.id.id_register_navi_bar);
        mName = (EditText) findViewById(R.id.id_register_name);
        mPhone = (EditText) findViewById(R.id.id_register_phone);
        mPassword = (EditText) findViewById(R.id.id_register_pwd);
        mConfirmedPassword = (EditText) findViewById(R.id.id_register_confirm_pwd);

        mValidateCode = (EditText) findViewById(R.id.id_register_validate_code);
        mObtainValidateCodeBtn = (Button) findViewById(R.id.id_register_obtain_validate_code);

        mReadProtocol = (CheckBox) findViewById(R.id.id_register_read_protocol);
        mProtocol = (TextView) findViewById(R.id.id_register_protocol);

        mRegisterBtn = (Button) findViewById(R.id.id_register_btn);

        mNameOkImg = (ImageView) findViewById(R.id.id_register_name_ok);
        mPhoneOkImg = (ImageView) findViewById(R.id.id_register_phone_ok);
        mPwdOkImg = (ImageView) findViewById(R.id.id_register_pwd_ok);
        mConfirmPwdOkImg = (ImageView) findViewById(R.id.id_register_confirm_pwd_ok);
        mValidateCodeOkImg = (ImageView) findViewById(R.id.id_register_validate_code_ok);

        progressDialog = new ProgressDialog(this);
    }


    /**
     * 自定义类，验证电话是否被注册过
     */
    class CheckPhoneThread extends Thread {


        private String phone;
        private Handler handler;
        private HttpURLHandler httpURLHandler;
        Message msg = new Message();


        public CheckPhoneThread(String phone, Handler handler, HttpURLHandler httpURLHandler) {
            this.phone = phone;
            this.handler = handler;
            this.httpURLHandler = httpURLHandler;
        }

        @Override
        public void run() {

            HashMap<String, String> params = new HashMap<>();
            params.put("action", "check");
            params.put("phone", phone);

            HttpURLTask task = new HttpURLTask(url, params, httpURLHandler);
            task.start();

        }
    }


    /**
     * 初始化短信验证功能并注册
     */
    private void initSMSSDK() {
        //初始化SMSSDK
        SMSSDK.initSDK(RegisterActivity.this, APP_KEY, APP_SECRET);

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

                mValidateCodeOkImg.setImageResource(R.mipmap.ok);
                mValidateCodeOkImg.setVisibility(View.VISIBLE);
                isValidateCodeOk = true;
                HashMap<String, String> params = (HashMap) data;
                String phone = params.get("phone");
                Log.i("validate_ok-------phone", phone);
            }
        } else if (result == SMSSDK.RESULT_ERROR) {

            if (mValidateCode.getText().toString().length() == 4) {
                //验证码验证错误
                mValidateCodeOkImg.setImageResource(R.drawable.error);
                mValidateCodeOkImg.setVisibility(View.VISIBLE);
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
                Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            RegisterActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}