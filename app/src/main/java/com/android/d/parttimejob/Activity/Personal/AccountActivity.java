package com.android.d.parttimejob.Activity.Personal;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;

/**账户页面
 * Created by Administrator on 2016/8/19.
 */
public class AccountActivity extends Activity {

    private TextView mSalaryTV;//薪水
    private NavigationBar mNaviBar;//标题栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        //初始化
        init();
        //设置监听器
       registerListener();

    }

    /**
     * 注册监听器
     */
    private void registerListener() {
        mNaviBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                AccountActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });
    }

    private void init() {

        App.addActivity(this);

        mSalaryTV= (TextView) findViewById(R.id.id_account_salary);
        mNaviBar= (NavigationBar) findViewById(R.id.id_account_navi_bar);

        mSalaryTV.setText(App.mPluralist.getSalary()+"");

    }
}
