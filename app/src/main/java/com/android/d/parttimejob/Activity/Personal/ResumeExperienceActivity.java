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

/**个人特点
 * Created by DJS on 2017/1/20.
 */
public class ResumeExperienceActivity extends Activity {

    private NavigationBar naviBar;//导航栏
    private EditText experienceET;//申请理由
    private TextView wordLeftTV;//显示写入剩余的字数


    private static final int TOTAL_COUNT = 140;//总共可以输入的字数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_experience);
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

                ResumeExperienceActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

               String experience= experienceET.getText().toString();
                App.mPluralist.setExperience(experience);

                ResumeExperienceActivity.this.finish();
            }
        });

        //申请理由文字监听
        experienceET.addTextChangedListener(new MyTextWatcher(){

            @Override
            public void afterTextChanged(Editable s) {

                naviBar.setRightTextVisiable(true);

                String text=s.toString();
                int count=text.length();

                if (count<=TOTAL_COUNT){
                    int leftCount=TOTAL_COUNT-count;
                    String str="还可以输入"+String.valueOf(leftCount)+"个字";
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

        naviBar = (NavigationBar) findViewById(R.id.id_experience_navi_bar);
        experienceET = (EditText) findViewById(R.id.id_experience_reason);
        wordLeftTV = (TextView) findViewById(R.id.id_experience_Text_left);

        experienceET.setText(App.mPluralist.getExperience());
        String str="还可以输入"+(TOTAL_COUNT-experienceET.getText().toString().length())+"个字";
        wordLeftTV.setText(str);


    }
}
