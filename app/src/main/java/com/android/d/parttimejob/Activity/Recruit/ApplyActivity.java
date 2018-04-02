package com.android.d.parttimejob.Activity.Recruit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.Communication.Datagram;
import com.android.d.parttimejob.Entry.recruit.Recruit;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Task.SendMessageTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.MyTextWatcher;
import com.android.d.parttimejob.Util.Status;

/**
 * 申请页面
 * Created by Administrator on 2016/8/10.
 */
public class ApplyActivity extends Activity implements View.OnClickListener {

    private NavigationBar naviBar;//导航栏
    private EditText applyReason;//申请理由
    private TextView wordLeftTV;//显示写入剩余的字数
    private Button submitBtn;//提交

    private static final int TOTAL_COUNT = 140;//总共可以输入的字数

    private String infoId = "";//申请的招聘信息编号
    private String pluralistId = "";//申请者编号

    private ProgressDialog progressDialog;//加载进度

//    private static final String URL = App.PREFIX+"Part-timeJob/RecruitServlet";

//    //网络数据下载处理器
//    private HttpURLHandler handler = new HttpURLHandler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            // TODO: 2016/8/11  此处不完整，后续补齐，此处应该打开成功的页面
//            String res = (String) msg.obj;
//            if (res != null) {
//                switch (res) {
//
//                    case Status.SUCCESSFUL:
//                        Toast.makeText(ApplyActivity.this, "报名成功",
//                                Toast.LENGTH_SHORT).show();
//
//                        //将招聘信息存储起来
//                        saveRecruit();
//                        Log.i("apply_infoId", infoId);
//                        Log.i("apply_pId", pluralistId);
//                        break;
//
//                    case Status.FAILING:
//                        Toast.makeText(ApplyActivity.this, "报名失败",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case Status.EXISTENT:
//                        Toast.makeText(ApplyActivity.this, "已经报名",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//
//            progressDialog.dismiss();
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply);
        //初始化
        init();
        //从上一个activity中获得数据
        getData();
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
                progressDialog.dismiss();
                ApplyActivity.this.finish();
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
        applyReason.addTextChangedListener(new MyTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int count = text.length();

                if (count <= TOTAL_COUNT) {
                    int leftCount = TOTAL_COUNT - count;
                    String str = String.valueOf(leftCount) + "/" + TOTAL_COUNT;
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

        naviBar = (NavigationBar) findViewById(R.id.id_apply_navi_bar);
        applyReason = (EditText) findViewById(R.id.id_apply_reason);
        wordLeftTV = (TextView) findViewById(R.id.id_Text_left);
        submitBtn = (Button) findViewById(R.id.id_submit_btn);

        progressDialog = new ProgressDialog(this);
    }

    /**
     * 处理点击事件
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_submit_btn) {


            Recruit r = new Recruit();
            r.setInfoId(infoId);
            r.setApplyReason(applyReason.getText().toString());
            r.setPluralistId(pluralistId);

            Datagram datagram = new Datagram();
            datagram.setJsonStream(JSONParser.toJSONString(r));
            datagram.setRequest("APPLY");

            String jsonData = JSONParser.toJSONString(datagram);

            SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonData, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Toast.makeText(ApplyActivity.this, "报名成功",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //将招聘信息存储起来
                    saveRecruit();
                    ApplyActivity.this.finish();
                }
            });
            task.start();


            // TODO: 2016/8/11 应该自定义一个progressDialog
            //显示进度
            showLoadingProgress();
        }
    }


    /**
     * 将招聘信息存储起来
     */
    private void saveRecruit() {
        //将招聘信息存储起来
        App.getEnrolledInfoList().put(infoId, Status.ENROLLED);
    }

    /**
     * 从上一个activity中获得数据
     */
    public void getData() {

        infoId = getIntent().getStringExtra("infoId");
        pluralistId = App.mPluralist.getId();
    }

    private void showLoadingProgress() {
        progressDialog.setMessage("loading…………");
        progressDialog.setProgressStyle(R.style.dialog);
        progressDialog.show();
    }
}
