package com.android.d.parttimejob.Activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.DataBase.Dao.PluralistDao;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Status;

/**
 * 启动页面
 * Created by Administrator on 2016/8/12.
 */
public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.
                FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.launch);
        App.addActivity(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isExit = getCurrentId();
                if (isExit) {
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        }, 0);
    }

    /**
     * 获得当前的id
     */
    private Boolean getCurrentId() {

        SharedPreferences sp = this.getSharedPreferences(Status.PLURALIST, MODE_PRIVATE);
        boolean isExit = sp.contains(Status.CURRENT_ID);
        if (isExit) {
            App.mPluralist.setId(sp.getString(Status.CURRENT_ID, ""));
            PluralistDao dao = new PluralistDao(LaunchActivity.this);
            App.mPluralist = dao.query(App.mPluralist.getId());

            Log.i("launch", "getCurrentId: "+App.mPluralist.toString());
            return true;
        } else {
            return false;
        }


    }
}
