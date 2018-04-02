package com.android.d.parttimejob.Fragment.MainFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Activity.Communication.AddFriendActivity;
import com.android.d.parttimejob.Activity.Main.MainActivity;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.DataBase.Dao.CommunicationDao;
import com.android.d.parttimejob.Entry.Communication.ChatRecord;
import com.android.d.parttimejob.Entry.Communication.Contact;
import com.android.d.parttimejob.Entry.Communication.Datagram;
import com.android.d.parttimejob.Entry.Communication.FriendRequest;
import com.android.d.parttimejob.Entry.Communication.Message;
import com.android.d.parttimejob.Entry.Communication.OnlineRecord;
import com.android.d.parttimejob.Fragment.CommunicationFragment.ContactsFragment;
import com.android.d.parttimejob.Fragment.CommunicationFragment.MessageFragment;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Task.SendMessageTask;
import com.android.d.parttimejob.Util.Action;
import com.android.d.parttimejob.Util.DatagramParser;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.Status;
import com.android.d.parttimejob.Util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 聊天界面
 * Created by D on 2016/5/31.
 */
public class CommunicationFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ImageView mUserHead;
    private ImageView mAddFriendImage;

    private ImageView mNewsIndicator;
    private ImageView mContactsIndicator;

    private TextView mNewsTV;
    private TextView mContactsTV;

    //viewpager
    private ViewPager mPager;
    private static List<Fragment> mFragmentList;


    private static String TAG = "MainActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.communication, container, false);

        //初始化
        init(view);
        //添加fragment到此布局中
        addFragment();
        //设置点击事件监听器
        setClickListener();

        return view;
    }


    /**
     * 设置监听事件
     */
    private void setClickListener() {
        mUserHead.setOnClickListener(this);
        mAddFriendImage.setOnClickListener(this);
    }


    /**
     * 添加消息和通讯录fragment
     */
    private void addFragment() {

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };

        mPager.setAdapter(adapter);


    }


    /**
     * 初始化
     */
    private void init(View view) {

        mUserHead = (ImageView) view.findViewById(R.id.id_usr_head_image_com);
        mAddFriendImage = (ImageView) view.findViewById(R.id.id_com_add_friend);
        mPager = (ViewPager) view.findViewById(R.id.id_communication_pager);
        mContactsTV = (TextView) view.findViewById(R.id.id_communication_contacts);
        mNewsTV = (TextView) view.findViewById(R.id.id_communication_news);
        mContactsIndicator = (ImageView) view.findViewById(R.id.id_bar_indicator_blow_contacts);
        mNewsIndicator = (ImageView) view.findViewById(R.id.id_bar_indicator_blow_news);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ContactsFragment());
        mFragmentList.add(new MessageFragment());

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new ContactsFragment());

        //从sqlite中回复数据
        restoreData();

        //设置头像
        String imageName = App.mPluralist.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, getActivity());
        if (bitmap != null) {
            mUserHead.setImageBitmap(bitmap);
        }

        mPager.addOnPageChangeListener(this);
        mPager.setCurrentItem(0);

        mNewsTV.setOnClickListener(this);
        mContactsTV.setOnClickListener(this);
        mNewsTV.setTextColor(getResources().getColor(R.color.text_color));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            //根据当前选项卡设置当前显示tab文字高亮
            mNewsTV.setTextColor(getResources().getColor(R.color.text_color));
            mContactsTV.setTextColor(getResources().getColor(R.color.onclick));
            //根据当前选项卡设置指示器显示位置
            mNewsIndicator.setVisibility(ImageView.VISIBLE);
            mContactsIndicator.setVisibility(ImageView.INVISIBLE);
        }
        if (position == 1) {
            //根据当前选项卡设置当前显示tab文字高亮
            mNewsTV.setTextColor(getResources().getColor(R.color.onclick));
            mContactsTV.setTextColor(getResources().getColor(R.color.text_color));
            //根据当前选项卡设置指示器显示位置
            mNewsIndicator.setVisibility(ImageView.INVISIBLE);
            mContactsIndicator.setVisibility(ImageView.VISIBLE);
        }

        Log.i("tag", "onPageSelected: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_communication_contacts:
                mPager.setCurrentItem(1);
                break;

            case R.id.id_communication_news:

                mPager.setCurrentItem(0);
                break;

            //弹出侧栏
            case R.id.id_usr_head_image_com:
                MainActivity.slidingMenu.toggle(true);

                break;

            //添加朋友
            case R.id.id_com_add_friend:

                Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    /**
     * 弹出dialog提示好友请求加好友
     */
    private void showRequestDialog(final FriendRequest request) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("好友申请");

        String message = request.getFriendName() + "申请加你为好友";
        builder.setMessage(message);

        //同意按钮
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //同意添加好友
                agreeAddFriend(request);

            }
        });

        //拒绝按钮
        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //拒绝添加好友
                refuseAddFriend();
            }
        });

        //创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /**
     * 拒绝添加好友
     */
    private void refuseAddFriend() {


    }

    /**
     * 同意添加好友
     */
    private void agreeAddFriend(final FriendRequest request) {

        //通过网络将同意添加好友的消息传递出去
        Datagram datagram = new Datagram();
        datagram.setRequest(Action.ADD_FRIEND);

        if (request.getMyId().contains("C")){
            request.setStatus("OK");
        }else if (request.getMyId().contains("P")){
            request.setStatus(Action.AGREE);
        }

        datagram.setJsonStream(JSONParser.toJSONString(request));
        String jsonDatagram = JSONParser.toJSONString(datagram);

        final SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonDatagram, new Handler() {

            @Override
            public void handleMessage(android.os.Message msg) {

                String jsonData = (String) msg.obj;
                if (jsonData != null && !jsonData.equals("")) {

                    Contact contact = DatagramParser.getEntity(jsonData, new TypeToken<Contact>() {
                    }.getType());
                    contact.setChatting(true);

                    Log.i(TAG, "handleMessage: contact " + contact.toString());
                    //更新UI
                    addFriends(contact, request, false);
                }
            }
        });
        task.start();

    }

    /**
     * 通过网络将刚添加的朋友的信息查找出来
     */
    private void obtainFriendDetail(final FriendRequest request) {

        String friendId = request.getFriendId();

        Datagram datagram = new Datagram();
        datagram.setRequest(Action.QUERY_FRIEND);

        datagram.setJsonStream(friendId);
        String jsonData = JSONParser.toJSONString(datagram);

        SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonData, new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {

                String jsonData = (String) msg.obj;
                if (jsonData != null && !jsonData.equals("")) {

                    Contact contact = DatagramParser.getEntity(jsonData, new TypeToken<Contact>() {
                    }.getType());

                    //更新UI
                    addFriends(contact, request, true);
                }
            }
        });
        task.start();
    }

    /**
     * 更新UI新信息
     */
    private void addFriends(Contact contact, FriendRequest request, boolean isMyRequest) {

        if (contact != null) {
            String friendId = contact.getId();
            App.mFriends.add(friendId);
            //设置正在聊天
            contact.setChatting(true);

            //将聊天记录放在全局变量中
            ChatRecord record = new ChatRecord();

            if (isMyRequest) {
                record.setMyId(App.mPluralist.getId());
                record.setFriendId(friendId);
            } else {
                record.setMyId(friendId);
                record.setFriendId(App.mPluralist.getId());
            }


            record.setTime(request.getTime());
            record.setMessage(request.getRequestReason());

            ArrayList<ChatRecord> records = new ArrayList<>();
            records.add(record);

            App.mChatRecordsMap.put(friendId, records);
            //添加朋友
            ((ContactsFragment) mFragmentList.get(1)).addFriend(contact);
            //将消息传到MessageFragment中更新UI
            ((MessageFragment) mFragmentList.get(0)).addMessage(request, contact);
        }

    }

    /**
     * 处理来自广播的消息
     *
     * @param context context
     * @param intent  intent
     */
    public void ReceiveBroadcast(Context context, Intent intent) {

        Bundle bundle = intent.getBundleExtra(Status.MESSAGE);
        if (bundle != null) {

            FriendRequest request = bundle.getParcelable(Status.FRIEND_REQUEST);
            ChatRecord record = bundle.getParcelable(Status.CHAT_RECORD);
            OnlineRecord online = bundle.getParcelable(Action.ON_LINE);
            OnlineRecord offline = bundle.getParcelable(Action.OFF_LINE);

            if (request != null) {

                String friendId;
                String status = request.getStatus();

                Log.i(TAG, "onReceive: request: " + request.toString());
                switch (status) {
                    case Action.REQUEST:

                        friendId = request.getMyId();
                        //是否已经存在
                        if (!App.mFriends.contains(friendId) && !App.mPluralist.getId().equals(friendId)) {
                            showRequestDialog(request);
                        }
                        break;
                    case Action.AGREE:
                        friendId = request.getFriendId();


                        //是否已经存在
                        if (!App.mFriends.contains(friendId) && !App.mPluralist.getId().equals(friendId)) {
                            //将朋友的详细信息获取
                            obtainFriendDetail(request);

                        }

                        break;
                    case Action.DELETE:
                        friendId = request.getMyId();
                        //删除好友
                        MessageFragment.deleteMessage(friendId);
                        ContactsFragment.deleteContact(friendId);
                        App.mChatRecordsMap.remove(friendId);
                        if (App.mFriends.contains(friendId)) {
                            App.mFriends.remove(friendId);
                        }
                        //删除好友聊天记录
                        CommunicationDao.deleteFriends(getActivity(), App.mPluralist.getId(), friendId);
                        Toast.makeText(getActivity(), "好友将你删除", Toast.LENGTH_SHORT).show();
                        break;
                }


            }

            //更新朋友消息
            if (record != null) {

                //更新消息
                ((MessageFragment) mFragmentList.get(0)).updateMessage(record);

                String friendId = record.getMyId();
                for (int i = 0; i < App.mContacts.size(); i++) {
                    if (friendId.equals(App.mContacts.get(i).getId())) {

                        if (!App.mContacts.get(i).isChatting()) {
                            ArrayList<ChatRecord> records = App.mChatRecordsMap.get(friendId);
                            if (records == null) {
                                records = new ArrayList<>();
                                records.add(record);

                                App.mChatRecordsMap.put(friendId, records);
                            }
                            App.mChatRecordsMap.get(friendId).add(record);
                        }
                        break;
                    }
                }

            }
            //上线
            if (online != null) {

                Log.i(TAG, "onReceive: online   " + online.toString());
                String friendId = online.getId();
                ContactsFragment.notifyContactOnLine(friendId, true);
            }
            //下线
            if (offline != null) {

                Log.i(TAG, "onReceive: offline   " + offline.toString());
                String friendId = offline.getId();
                ContactsFragment.notifyContactOnLine(friendId, false);
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 加载全局朋友数据数据
     */
    public void restoreData() {

        App.mContacts.addAll(CommunicationDao.getContexts(getActivity(), App.mPluralist.getId()));
        for (int i = 0; i < App.mContacts.size(); i++) {
            App.mFriends.add(App.mContacts.get(i).getId());
        }


        HashMap<String, ArrayList<ChatRecord>> recordsMap = CommunicationDao.getChatRecordMap(getActivity(), App.mFriends);

        if (recordsMap != null) {
            App.mChatRecordsMap = recordsMap;
        }


        Contact contact;
        String friendId;
        ChatRecord record;
        ArrayList<ChatRecord> records;
        String name = "";
        String message = "";
        String time = "";


        for (int i = 0; i < App.mContacts.size(); i++) {

            contact = App.mContacts.get(i);
            // TODO: 2017/5/18 根据是否在聊天设置聊天列表
            if (true){

                friendId = contact.getId();
                name = contact.getName();

                records = App.mChatRecordsMap.get(friendId);
                if (records != null) {
                    record = records.get(records.size() - 1);

                    message = record.getMessage();
                    time = record.getTime();
                }

                Message msg = new Message();
                msg.setLastMessage(message);
                msg.setTime(Util.getRightTime(time));
                msg.setId(friendId);
                msg.setName(name);

                App.mMessages.add(msg);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //设置头像
        String imageName = App.mPluralist.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, getActivity());
        if (bitmap != null) {
            mUserHead.setImageBitmap(bitmap);
        }
    }
}
