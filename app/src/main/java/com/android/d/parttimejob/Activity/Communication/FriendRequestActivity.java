package com.android.d.parttimejob.Activity.Communication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.Communication.FriendRequest;
import com.android.d.parttimejob.MyView.Communication.MyProgressDialog;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Task.SendMessageTask;
import com.android.d.parttimejob.Util.Action;
import com.android.d.parttimejob.Util.DatagramParser;
import com.android.d.parttimejob.Util.Util;


public class FriendRequestActivity extends Activity {
    private NavigationBar mNavigationBar;

    private EditText mRequestReasonET;
    private Button mSendBtn;

    private String friendId;

    private MyProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);
        //初始化
        init();
        //从上一个activity中获取数据
        getData();
        //设置点击事件
        setListener();
    }

    /**
     * 设置点击事件
     */
    private void setListener() {

        mNavigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                FriendRequestActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();

                String requestReason = mRequestReasonET.getText().toString();
                if (!requestReason.equals("")) {

                    //将添加朋友的信息封装
                    FriendRequest request = new FriendRequest();
                    request.setTime(Util.getNowTime());
                    request.setStatus(Action.REQUEST);
                    request.setMyId(App.mPluralist.getId());
                    request.setFriendId(friendId);
                    request.setRequestReason(requestReason);


                    Log.i("request", "onClick: "+request.toString());
                    //将数据包封装成json数据格式
                    String jsonDatagram = DatagramParser.toJsonDatagram(Action.ADD_FRIEND, null, request);

                    SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonDatagram, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {

                            mDialog.dismiss();
                            Toast.makeText(FriendRequestActivity.this, "申请成功,等待对方同意", Toast.LENGTH_SHORT).show();

                        }
                    });
                    task.start();

                } else {
                    mDialog.dismiss();
                    Toast.makeText(FriendRequestActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    /**
     * 从上一个activity中获取数据
     */
    private void getData() {
        friendId = getIntent().getStringExtra("friendId");
    }

    /**
     * 初始化
     */
    private void init() {
        App.addActivity(this);

        mNavigationBar = (NavigationBar) findViewById(R.id.id_friend_request_navi_bar);
        mRequestReasonET = (EditText) findViewById(R.id.id_friend_request_validate_message);
        mSendBtn = (Button) findViewById(R.id.id_friend_request_send_btn);

        mDialog = new MyProgressDialog(this);
    }
}
