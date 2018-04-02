package com.android.d.parttimejob.Activity.Personal;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.MyTextWatcher;

/**
 * 个人特点
 * Created by DJS on 2017/1/20.
 */
public class ResumeFeatureActivity extends Activity {

    private NavigationBar naviBar;//导航栏
    private EditText featureET;//申请理由
    private TextView wordLeftTV;//显示写入剩余的字数


    private static final int TOTAL_COUNT = 140;//总共可以输入的字数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_feature);
        //初始化
        init();
        //注册监听器
        registerListener();

    }

    /**
     * 注册监听器
     */
    private void registerListener() {
        naviBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {

                ResumeFeatureActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

                String features = featureET.getText().toString();
                App.mPluralist.setFeature(features);

                ResumeFeatureActivity.this.finish();
            }
        });

        //申请理由文字监听
        featureET.addTextChangedListener(new MyTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                naviBar.setRightTextVisiable(true);

                String text = s.toString();
                int count = text.length();

                if (count <= TOTAL_COUNT) {
                    int leftCount = TOTAL_COUNT - count;
                    String str = "还可以输入" + String.valueOf(leftCount) + "个字";
                    wordLeftTV.setText(str);
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void init() {
        App.addActivity(this);

        naviBar = (NavigationBar) findViewById(R.id.id_feature_navi_bar);
        featureET = (EditText) findViewById(R.id.id_feature_reason);
        wordLeftTV = (TextView) findViewById(R.id.id_feature_Text_left);

        featureET.setText(App.mPluralist.getFeature());

        String str = "还可以输入" + (TOTAL_COUNT - featureET.getText().toString().length()) + "个字";
        wordLeftTV.setText(str);

    }
}
