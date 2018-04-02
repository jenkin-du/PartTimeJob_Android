package com.android.d.parttimejob.Activity.Communication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.d.parttimejob.Adapter.ChatMessageAdapter;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.Communication.ChatRecord;
import com.android.d.parttimejob.Entry.Communication.Contact;
import com.android.d.parttimejob.Entry.Communication.FriendRequest;
import com.android.d.parttimejob.Fragment.CommunicationFragment.MessageFragment;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Task.SendMessageTask;
import com.android.d.parttimejob.Util.Action;
import com.android.d.parttimejob.Util.DatagramParser;
import com.android.d.parttimejob.Util.MyTextWatcher;
import com.android.d.parttimejob.Util.Status;
import com.android.d.parttimejob.Util.Util;

import java.util.ArrayList;

/**
 * 聊天界面
 * Created by Administrator on 2016/9/4.
 */
public class ChatActivity extends Activity {

    private NavigationBar mNavigationBar;
    private EditText mNewMessageET;
    private Button mSendMessageBtn;

    private ListView mMessageListView;
    private ChatMessageAdapter mAdapter;
    private ArrayList<ChatRecord> mChatRecords;

    private NewMessageReceiver mReceiver;
    private static String mFriendId = "";

    private Contact mContact;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_ui);
        //初始化
        init();
        //从上一个activity中获得数据
        setData();
        //设置监听器
        setListener();


    }


    /**
     * 设置监听器
     */
    private void setListener() {
        //标题栏
        mNavigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                ChatActivity.this.finish();
            }

            @Override
            public void onClickImg() {

                Intent intent = new Intent(ChatActivity.this, FriendActivity.class);
                intent.putExtra("friendId", mFriendId);
                startActivity(intent);

            }

            @Override
            public void onClickRightText() {


            }
        });

        //发送消息
        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = mNewMessageET.getText().toString();
                if (!message.equals("")) {
                    //将新消息展现在界面上
                    setMessageOnUI(message);
                    //将消息发送出去
                    sendMessage(message);
                }
            }

        });

        mNewMessageET.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    mSendMessageBtn.setEnabled(true);
                }
            }
        });
    }


    /**
     * 将消息发送出去
     */
    private void sendMessage(String message) {

        ChatRecord record = new ChatRecord();
        record.setMyId(App.mPluralist.getId());
        record.setFriendId(mFriendId);
        record.setMessage(message);
        record.setTime(Util.getNowTime());

        String jsonDatagram = DatagramParser.toJsonDatagram(Action.COMMUNICATE, null, record);
        SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonDatagram, new Handler() {
            @Override
            public void handleMessage(Message msg) {
            }
        });
        task.start();


    }

    /**
     * 将新消息展现在界面上
     */
    private void setMessageOnUI(String message) {

        ChatRecord record = new ChatRecord();
        record.setMyId(App.mPluralist.getId());
        record.setFriendId(mFriendId);
        record.setMessage(message);
        record.setTime(Util.getNowTime());

        mChatRecords.add(record);
        mAdapter.notifyDataSetChanged();
        //让列表滚动到底部
        mMessageListView.setSelection(mMessageListView.getBottom());

        //清空发送框
        mSendMessageBtn.setEnabled(false);
        mNewMessageET.setText("");

        //更新全局消息列表
        MessageFragment.updateMessageOnChatting(record);

    }

    /**
     * 初始化
     */
    private void init() {
        App.addActivity(this);

        mNavigationBar = (NavigationBar) findViewById(R.id.id_chat_navi_bar);
        mNewMessageET = (EditText) findViewById(R.id.id_chat_new_message);
        mSendMessageBtn = (Button) findViewById(R.id.id_chat_send_btn);
        mMessageListView = (ListView) findViewById(R.id.id_chat_list_view);

        mReceiver = new NewMessageReceiver();
        IntentFilter filter = new IntentFilter(Status.NEW_MESSAGE);
        filter.setPriority(100);
        registerReceiver(mReceiver, filter);
    }


    /**
     * 从全局变量中获取数据
     */
    public void setData() {

        mFriendId = getIntent().getStringExtra("friendId");
        if (mFriendId != null) {
            for (int i = 0; i < App.mContacts.size(); i++) {
                if (mFriendId.equals(App.mContacts.get(i).getId())) {
                    mContact = App.mContacts.get(i);
                }
            }
        }

        if (mContact != null) {
            String friendName = mContact.getName();
            mNavigationBar.setTitle(friendName);
            //设置正在聊天
            mContact.setChatting(true);
        }


        //恢复聊天记录
        mChatRecords = App.mChatRecordsMap.get(mFriendId);
        if (mChatRecords == null) {
            mChatRecords = new ArrayList<>();
            App.mChatRecordsMap.get(mFriendId).addAll(mChatRecords);
        }

        mAdapter = new ChatMessageAdapter(ChatActivity.this, mChatRecords);
        mMessageListView.setAdapter(mAdapter);
        mMessageListView.setSelection(mMessageListView.getBottom());

        if (mFriendId.substring(0,1).equals("C")){
            mNavigationBar.setRightImageVisible(false);
        }
    }


    /**
     * 新消息接收器
     */
    class NewMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            bundle = intent.getBundleExtra(Status.MESSAGE);
            if (bundle != null) {

                ChatRecord record = bundle.getParcelable(Status.CHAT_RECORD);
                FriendRequest request = bundle.getParcelable(Status.FRIEND_REQUEST);
                Log.i("ChatActivity", "onReceive: record" + record);
                if (record != null) {
                    //将新消息更新消息
                    updateMessage(record);
                }

                if (request != null) {
                    String status = request.getStatus();
                    if (status.equals(Action.DELETE)) {
                        //结束聊天
                        String friendId = request.getMyId();
                        if (friendId.equals(mFriendId)) {
                            ChatActivity.this.finish();
                        }
                    }
                }

                //截断广播
                abortBroadcast();
            }

        }
    }


    /**
     * 将新消息更新消息
     */
    private void updateMessage(ChatRecord record) {

        String friendId = record.getMyId();
        if (ChatActivity.mFriendId.equals(friendId)) {

            mChatRecords.add(record);
            mAdapter.notifyDataSetChanged();
            //让列表滚动到底部
            mMessageListView.setSelection(mMessageListView.getBottom());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);

        mContact.setChatting(false);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.i("ChatActivity", "onKeyDown: finish()");
                ChatActivity.this.finish();
                return false;

            default:
                return super.onKeyDown(keyCode, event);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (!App.mFriends.contains(mFriendId)) {
            this.finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!App.mFriends.contains(mFriendId)) {
            this.finish();
        }

    }


}
