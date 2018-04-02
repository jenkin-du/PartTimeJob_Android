package com.android.d.parttimejob.Activity.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.DataBase.Dao.CommunicationDao;
import com.android.d.parttimejob.Entry.Communication.OnlineRecord;
import com.android.d.parttimejob.Fragment.CommunicationFragment.ContactsFragment;
import com.android.d.parttimejob.Fragment.MainFragment.CommunicationFragment;
import com.android.d.parttimejob.Fragment.MainFragment.MapFragment;
import com.android.d.parttimejob.Fragment.MainFragment.RecruitFragment;
import com.android.d.parttimejob.Fragment.MainFragment.SlidingFragment;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Server.CommunicationServer;
import com.android.d.parttimejob.Task.SendMessageTask;
import com.android.d.parttimejob.Util.Action;
import com.android.d.parttimejob.Util.DatagramParser;
import com.android.d.parttimejob.Util.Status;
import com.android.d.parttimejob.Util.Util;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;


/**
 * 主界面
 * Created by D on 2016/5/27.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //与fragment有关
    private MapFragment mMapFrag;
    private RecruitFragment mInfoFrag;
    private CommunicationFragment mCommunicationFrag;
    private SlidingFragment mSlidingFrag;

    //与按钮有关
    private TextView mMapBtn;
    private TextView mInfoBtn;
    private TextView mCommunicateBtn;

    public static SlidingMenu slidingMenu;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    private MessageReceiver receiver;

    private double exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //初始化控件
        init();
        //开启后台服务
        setService();
        //接受广播
        setBroadcastReceiver();
        //设置点击事件监听器
        setClickListener();
        //添加侧栏
        addSlidingMenu();
        //在MainActivity中第一次中加载Mapfragment
        loadDefaultMapFragment();
        //上线
        setOnline();

    }

    /**
     * 接受广播
     */
    private void setBroadcastReceiver() {
        receiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter(Status.NEW_MESSAGE);
        filter.setPriority(200);
        registerReceiver(receiver, filter);

    }


    /**
     * 上线
     */
    private void setOnline() {

        OnlineRecord onlineRecord = new OnlineRecord();
        onlineRecord.setId(App.mPluralist.getId());
        onlineRecord.setIp(Util.getIpAdd(getApplicationContext()));

        String jsonDatagram = DatagramParser.toJsonDatagram(Action.ON_LINE, null, onlineRecord);
        final  SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonDatagram, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String json = (String) msg.obj;
                if (json != null) {
                    ArrayList<String> onlineFriendIds = DatagramParser.getEntity(json, new TypeToken<ArrayList<String>>() {
                    }.getType());

                    if (onlineFriendIds != null) {
                        for (String friendId : onlineFriendIds)
                            ContactsFragment.notifyContactOnLine(friendId, true);
                    }
                }


            }
        });
        task.start();
    }

    private void setService() {

        Intent intent = new Intent();
        intent.setClass(this, CommunicationServer.class);
        startService(intent);
    }

    /**
     * 设置监听事件
     */
    private void setClickListener() {

        mMapBtn.setOnClickListener(this);
        mInfoBtn.setOnClickListener(this);
        mCommunicateBtn.setOnClickListener(this);

    }

    /**
     * 初始化
     */
    private void init() {
        App.addActivity(this);

        mMapBtn = (TextView) findViewById(R.id.map_btn);
        mInfoBtn = (TextView) findViewById(R.id.info_btn);
        mCommunicateBtn = (TextView) findViewById(R.id.communicate_btn);


        fragmentManager = getSupportFragmentManager();

    }


    /**
     * 第一次加载地图
     */
    private void loadDefaultMapFragment() {
        //通过fragmentManager找到容纳fragment的父容器，并获得父容器中的fragment类对象，
        //然后可以对获得的对象进行各种操作

        transaction = fragmentManager.beginTransaction();
        mMapFrag = (MapFragment) fragmentManager.findFragmentById(R.id.frame);

        //通过判断fragment是否别创建，然后对其更改操作，可以减小系统开销
        if (mMapFrag == null) {
            mMapFrag = new MapFragment();
        }
        transaction.add(R.id.frame, mMapFrag);

        //添加第二个fragment
        if (mInfoFrag == null) {
            mInfoFrag = new RecruitFragment();
        }
        transaction.add(R.id.frame, mInfoFrag);
        //隐藏第二个fragment
        transaction.hide(mInfoFrag);
        mInfoFrag.onPause();


        //添加第三个fragment
        if (mCommunicationFrag == null) {
            mCommunicationFrag = new CommunicationFragment();
        }
        transaction.add(R.id.frame, mCommunicationFrag);
        //隐藏第三个fragment
        transaction.hide(mCommunicationFrag);
        mCommunicationFrag.onPause();

        transaction.commit();

        //设置点击按钮的颜色
        setClickedColor(1);
    }

    /**
     * 加载地图fragment
     */
    private void loadMapFragment() {
        //通过fragmentManager找到容纳fragment的父容器，并获得父容器中的fragment类对象，
         //然后可以对获得的对象进行各种操作

        transaction = fragmentManager.beginTransaction();

        transaction.hide(mInfoFrag);
        mInfoFrag.onPause();
        transaction.hide(mCommunicationFrag);
        mCommunicationFrag.onPause();

        mMapFrag.onResume();
        transaction.show(mMapFrag);

        transaction.commit();
    }


    /**
     * 加载招聘信息界面
     */
    private void loadInfoFragment() {

        transaction = fragmentManager.beginTransaction();

        transaction.hide(mMapFrag);
        mMapFrag.onPause();
        transaction.hide(mCommunicationFrag);
        mCommunicationFrag.onPause();

        mInfoFrag.onResume();
        transaction.show(mInfoFrag);

        transaction.commit();
    }

    /**
     * 加载聊天界面
     */
    private void loadCommunicationFragment() {

        transaction = fragmentManager.beginTransaction();

        transaction.hide(mMapFrag);
        mMapFrag.onPause();
        transaction.hide(mInfoFrag);
        mInfoFrag.onPause();

        mCommunicationFrag.onResume();
        transaction.show(mCommunicationFrag);


        transaction.commit();
    }

    /**
     * 处理各种点击事件
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.map_btn:
                setClickedColor(1);

                loadMapFragment();
                break;
            case R.id.info_btn:
                setClickedColor(2);


                loadInfoFragment();
                break;
            case R.id.communicate_btn:

                setClickedColor(3);
                loadCommunicationFragment();
            default:
                break;
        }
    }

    /**
     * 当点击相应的按钮是改变颜色
     *
     * @param pos 按钮位置
     */
    private void setClickedColor(int pos) {

        mMapBtn.setTextColor(getResources().getColor(R.color.onclick));
        mInfoBtn.setTextColor(getResources().getColor(R.color.onclick));
        mCommunicateBtn.setTextColor(getResources().getColor(R.color.onclick));

        if (pos == 1) {
            mMapBtn.setTextColor(getResources().getColor(R.color.btn_bg));
        } else if (pos == 2) {
            mInfoBtn.setTextColor(getResources().getColor(R.color.btn_bg));
        } else if (pos == 3) {
            mCommunicateBtn.setTextColor(getResources().getColor(R.color.btn_bg));
        }

    }


    /**
     * 添加侧栏
     */
    private void addSlidingMenu() {

        transaction = fragmentManager.beginTransaction();
        mSlidingFrag = (SlidingFragment) fragmentManager.findFragmentById(R.id.frame_sliding);
        //通过判断fragment是否别创建，然后对其更改操作，可以减小系统开销
        if (mSlidingFrag == null) {
            mSlidingFrag = new SlidingFragment();
        }
        transaction.add(R.id.frame_sliding, mSlidingFrag);
        transaction.commit();


        slidingMenu = new SlidingMenu(this);  //新建一个slidingMenu对象，并设置好上下文
        slidingMenu.setMode(SlidingMenu.LEFT);  //设置侧栏位于左侧
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_margin);  //设置侧栏弹出宽度
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  //把侧栏附加到主界面上
        slidingMenu.setMenu(R.layout.sliding);//在侧栏添加布局资源

    }


    /**
     * 弹出左菜单
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:

                int num = App.getCollectedInfoList().size();
                SlidingFragment.mCollectionNum.setText(num + "");

                SlidingFragment.mSalary.setText(App.mPluralist.getSalary() + "");

                slidingMenu.toggle(true);

                break;
            //返回键
            case KeyEvent.KEYCODE_BACK:
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    //          finish();
                    App.exitAllActivity();
                }
                break;
        }

        return false;
    }


    /**
     * 下线
     */
    private void setOffline() {

        OnlineRecord onlineRecord = new OnlineRecord();
        onlineRecord.setId(App.mPluralist.getId());

        String jsonDatagram = DatagramParser.toJsonDatagram(Action.OFF_LINE, null, onlineRecord);
        SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonDatagram, new Handler());
        task.start();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        int num = App.getCollectedInfoList().size();
        SlidingFragment.mCollectionNum.setText(num + "");

        SlidingFragment.mSalary.setText(App.mPluralist.getSalary() + "");

    }


    /**
     * 广播接收器
     */
    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //在communication的fragment中接受来自服务器的消息
            mCommunicationFrag.ReceiveBroadcast(context, intent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭广播
        unregisterReceiver(receiver);

        Log.i("MainActivity---", "onDestroy: ");
        //下线
        setOffline();
        //保存数据
        CommunicationDao.saveContexts(MainActivity.this, App.mContacts);
        CommunicationDao.saveChatRecords(MainActivity.this, App.mChatRecordsMap);

        App.mFriends.clear();
        App.mMessages.clear();
        App.mChatRecordsMap.clear();
        App.mContacts.clear();

        Log.i("main", "onDestroy: ");
    }
}
