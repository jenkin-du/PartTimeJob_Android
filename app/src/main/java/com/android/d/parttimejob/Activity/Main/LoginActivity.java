package com.android.d.parttimejob.Activity.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.DataBase.Dao.PluralistDao;
import com.android.d.parttimejob.Entry.Pluralist;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.MyTextWatcher;
import com.android.d.parttimejob.Util.ReturnSet;
import com.android.d.parttimejob.Util.Status;
import com.android.d.parttimejob.Util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;


public class LoginActivity extends Activity {

    private EditText mNameEdit;
    private EditText mPwdEdit;
    private TextView mRegisterText;//注册
    private TextView mForgetPwdText;//忘记密码

    private ImageView mUserHead;//头像

    private ProgressDialog progressDialog;//登录进度

    private static final String URL = App.PREFIX + "Part-timeJob/PluralistServlet";

    //登录
    private Button mLoginBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        //初始化
        initView();
        //设置EditView提示信息字体大小
        setHintTextSize();
        //设置注册监听事件
        setClickEvent();

    }

    /**
     * 实现各种点击事件
     */
    private void setClickEvent() {
        //注册
        mRegisterText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                return false;
            }
        });

        //登录
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登录判断
                Login(v);
            }
        });
        // TODO: 2016/8/15 忘记密码
        //忘记密码
        mForgetPwdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //异步加载图片
        mNameEdit.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length()==11){
                    //获取头像
                    Util.getImageByPhone(s.toString(),URL,mUserHead);
                }
            }
        });

    }

    /**
     * 设置输入框提示字体大小
     */
    private void setHintTextSize() {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("手机号码");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        mNameEdit.setHint(new SpannableString(ss));

        // 新建一个可以添加属性的文本对象
        SpannableString ss2 = new SpannableString("8-20位数字或字母");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(14, true);
        // 附加属性到文本
        ss2.setSpan(ass2, 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        mPwdEdit.setHint(new SpannableString(ss2));

    }

    /**
     * 初始化视图
     */
    private void initView() {

        App.addActivity(this);

        mNameEdit = (EditText) findViewById(R.id.id_edit_name);
        mPwdEdit = (EditText) findViewById(R.id.id_edit_pwd);
        mForgetPwdText = (TextView) findViewById(R.id.id_forget_pwd);
        mRegisterText = (TextView) findViewById(R.id.id_register);
        mLoginBtn = (Button) findViewById(R.id.id_login);
        mUserHead= (ImageView) findViewById(R.id.id_login_head_img);

        progressDialog = new ProgressDialog(this);
    }

    /**
     * 登录判断
     */
    public void Login(View view) {
        //显示进度条
        progressDialog.show();

        final String phone = mNameEdit.getText().toString();
        final String pwd = mPwdEdit.getText().toString();

        if (phone.length() != 11) {
            //电话长度不够
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "号码错误", Toast.LENGTH_SHORT).show();
        } else {

            if (!"".equals(pwd)) {
                //检查电话是否存在以及验证密码
                checkPhoneAndPassword(phone, pwd);
                //密码为空
            } else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 检查电话是否存在以及验证密码
     */
    private void checkPhoneAndPassword(final String phone, final String pwd) {


        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("action", "validate");
        paramsMap.put("phone", phone);
        paramsMap.put("password", pwd);

        HttpURLTask task = new HttpURLTask(URL, paramsMap, new Handler() {

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
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "账号不存在！", Toast.LENGTH_SHORT).show();
                            //密码错误
                            break;
                        case Status.WRONG:
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                            //密码正确
                            break;
                        case Status.RIGHT:

                            //将登陆者的id号保存在全局变量里，用于后续操作
                            App.mPluralist = set.getPluralist();
                            Pluralist p = App.mPluralist;
                            String pId = p.getId();
                            //保存配置文件
                            SharedPreferences sp = LoginActivity.this.getSharedPreferences(Status.PLURALIST, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Status.CURRENT_ID, pId);
                            editor.apply();

                            //将数据插入本地数据库中
                            PluralistDao dao = new PluralistDao(LoginActivity.this);
                            boolean isIn = dao.insert(p);//插入
                            if (!isIn) {
                                Toast.makeText(LoginActivity.this, "本地存储出现问题", Toast.LENGTH_SHORT).show();
                            }



                            //关闭进度条，并跳转
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            break;
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
            App.exitAllActivity();
        }

        return true;
    }
}
