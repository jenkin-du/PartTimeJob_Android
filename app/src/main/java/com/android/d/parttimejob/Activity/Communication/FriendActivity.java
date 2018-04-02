package com.android.d.parttimejob.Activity.Communication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.Communication.Datagram;
import com.android.d.parttimejob.Entry.Communication.Friend;
import com.android.d.parttimejob.Entry.Communication.FriendRequest;
import com.android.d.parttimejob.Fragment.CommunicationFragment.ContactsFragment;
import com.android.d.parttimejob.Fragment.CommunicationFragment.MessageFragment;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Task.SendMessageTask;
import com.android.d.parttimejob.Util.Action;
import com.android.d.parttimejob.Util.DatagramParser;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.Status;
import com.android.d.parttimejob.Util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * 好友详情
 * Created by Administrator on 2016/12/1.
 */
public class FriendActivity extends Activity {

    private NavigationBar mNavigationBar;

    public ImageView mHeadImage;
    private TextView mNameTV;
    private TextView mGenderTV;
    private TextView mBirthTV;
    private TextView mHeightTV;
    private TextView mEducationTV;
    private TextView mSchoolTV;
    private TextView mEmailTV;
    private TextView mPhoneTV;

    private Button mDeleteBtn;


    private String mFriendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);

        //初始化
        init();
        //设置监听器
        setListener();
        //从上一个activity中获取数据
        getData();
        //从网络获取数据
        loadFriendData();
    }

    /**
     * 设置监听器
     */
    private void setListener() {

        mNavigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                //退出
                FriendActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }

        });

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
                builder.setTitle("删除好友");
                builder.setMessage("好友将从你的好友列表中删除并且你也将会从好友的列表中删除！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除好友
                        deleteFriend();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    /**
     * 删除好友
     */
    private void deleteFriend() {

        FriendRequest request = new FriendRequest();
        request.setMyId(App.mPluralist.getId());
        request.setFriendId(mFriendId);
        request.setStatus(Action.DELETE);

        Datagram datagram = new Datagram();
        datagram.setRequest(Action.ADD_FRIEND);
        datagram.setJsonStream(JSONParser.toJSONString(request));

        String json = JSONParser.toJSONString(datagram);
        SendMessageTask task = new SendMessageTask(App.IP, App.PORT, json, new Handler() {
            @Override
            public void handleMessage(Message msg) {
            }
        });
        task.start();

        //删除好友
        MessageFragment.deleteMessage(mFriendId);
        ContactsFragment.deleteContact(mFriendId);
        App.mChatRecordsMap.remove(mFriendId);
        App.mFriends.remove(mFriendId);

        this.finish();
    }

    /**
     * 从网络获取数据
     */
    private void loadFriendData() {

        String url = App.PREFIX + "Part-timeJob/PluralistServlet";

        HashMap<String, String> params = new HashMap<>();
        params.put("action", "findFriend");
        params.put("friendId", mFriendId);

        HttpURLTask task = new HttpURLTask(url, params, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 200) {
                    String jsonDatagram = (String) msg.obj;
                    String response = DatagramParser.getResponse(jsonDatagram);
                    Log.i("jsonDatagram", jsonDatagram);
                    if (response.equals(Status.SUCCESSFUL)) {

                        Friend friend = DatagramParser.getEntity(jsonDatagram, new TypeToken<Friend>() {
                        }.getType());
                        //将数据表现在视图上
                        setData(friend);

                    } else {
                        Toast.makeText(FriendActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(FriendActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.start();


    }

    /**
     * 将数据表现在视图上
     */
    private void setData(Friend friend) {


        //异步加载图片
        Util.loadImageAsync(friend.getImageName(),mHeadImage,this);

        mNameTV.setText(friend.getName());
        mGenderTV.setText(friend.getGender());
        mHeightTV.setText(friend.getHeight());
        mBirthTV.setText(friend.getAge());
        mPhoneTV.setText(friend.getPhone());
        mSchoolTV.setText(friend.getSchool());
        mEducationTV.setText(friend.getEducation());
        mEmailTV.setText(friend.getEmail());

    }

    /**
     * 初始化控件
     */
    private void init() {

        App.addActivity(this);

        mNameTV = (TextView) findViewById(R.id.id_friend_name);
        mNavigationBar = (NavigationBar) findViewById(R.id.id_friend_navigation_bar);
        mHeadImage = (ImageView) findViewById(R.id.id_friend_head_img);
        mGenderTV = (TextView) findViewById(R.id.id_friend_gender);
        mBirthTV = (TextView) findViewById(R.id.id_friend_birth);
        mEducationTV = (TextView) findViewById(R.id.id_friend_education);
        mEmailTV = (TextView) findViewById(R.id.id_friend_email);
        mSchoolTV = (TextView) findViewById(R.id.id_friend_school);
        mPhoneTV = (TextView) findViewById(R.id.id_friend_phone);
        mHeightTV = (TextView) findViewById(R.id.id_friend_height);
        mDeleteBtn = (Button) findViewById(R.id.id_friend_delete);
    }

    /**
     * 获得上一个activity传过来的数据
     */
    public void getData() {
        mFriendId = getIntent().getStringExtra("friendId");
    }
}
