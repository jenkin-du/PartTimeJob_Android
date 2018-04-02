package com.android.d.parttimejob.Activity.Setting;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.MyTextWatcher;

/**意见反馈
 * Created by DJS on 2017/1/22.
 */
public class FeedbackActivity extends Activity implements View.OnClickListener{
    private NavigationBar naviBar;//导航栏
    private EditText feedbackReason;//申请理由
    private TextView wordLeftTV;//显示写入剩余的字数
    private Button submitBtn;//提交

    private static final int TOTAL_COUNT = 140;//总共可以输入的字数


    //private static final String URL = App.PREFIX+"Part-timeJob/RecruitServlet";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
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
              //  progressDialog.dismiss();
                FeedbackActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });
        submitBtn.setOnClickListener(this);
        //申请理由文字监听
        feedbackReason.addTextChangedListener(new MyTextWatcher(){

            @Override
            public void afterTextChanged(Editable s) {
                String text=s.toString();
                int count=text.length();

                if (count<=TOTAL_COUNT){
                    int leftCount=TOTAL_COUNT-count;
                    String str=String.valueOf(leftCount)+"/"+TOTAL_COUNT;
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

        naviBar = (NavigationBar) findViewById(R.id.id_feedback_navi_bar);
        feedbackReason = (EditText) findViewById(R.id.id_feedback_reason);
        wordLeftTV = (TextView) findViewById(R.id.id_Text_left);
        submitBtn = (Button) findViewById(R.id.id_submit_btn);

    }

    /**
     * 处理点击事件
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_submit_btn) {

            Toast.makeText(FeedbackActivity.this,"吐槽成功",Toast.LENGTH_SHORT).show();
        }
    }



}
