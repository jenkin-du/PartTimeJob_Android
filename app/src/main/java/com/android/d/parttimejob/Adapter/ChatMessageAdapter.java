package com.android.d.parttimejob.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.Communication.ChatRecord;
import com.android.d.parttimejob.MyView.Communication.FriendMessage;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Util;

import java.util.ArrayList;

/**
 * 聊天记录适配器
 * Created by Administrator on 2016/8/25.
 */
public class ChatMessageAdapter extends BaseAdapter {


    private ArrayList<ChatRecord> mChatRecordList;//聊天列表

    private Context mContext;


    public ChatMessageAdapter(Context mContext, ArrayList<ChatRecord> mChatRecordList) {
        this.mContext = mContext;
        this.mChatRecordList = mChatRecordList;
    }

    @Override
    public int getCount() {

        return mChatRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ChatRecord record = mChatRecordList.get(position);

        Log.i("getView", record.toString());

        if (convertView == null) {

            //判断是我的信息还是朋友的信息，然后分别将对应的信息显示出来
            if (record.getMyId().equals(App.mPluralist.getId())) {
                FriendMessage.MyMessage myMessage = new FriendMessage.MyMessage(mContext);
                myMessage.setMessage(record.getMessage());
                //异步加载图像
                Util.loadImageAsync(App.mPluralist.getImageName(), myMessage.mHeadImg, mContext);
                //新建出来view之后通过设置Tag来绑定到相应的视图上，可以优化内存
                convertView = myMessage;
                convertView.setTag(R.id.tag_first, myMessage);

            } else {
                FriendMessage friendMessage = new FriendMessage(mContext);
                friendMessage.setMessage(record.getMessage());

                //异步加载图像
                String imageName = Util.getImageNameById(record.getMyId());
                Util.loadImageAsync(imageName, friendMessage.mHeadImg, mContext);

                convertView = friendMessage;
                convertView.setTag(R.id.tag_second, friendMessage);
            }
        } else {

            if (record.getMyId().equals(App.mPluralist.getId())) {
                //将tag取出来，
                FriendMessage.MyMessage myMessage = (FriendMessage.MyMessage) convertView.getTag(R.id.tag_first);

                if (myMessage != null) {

                    //异步加载图像
                    Util.loadImageAsync(App.mPluralist.getImageName(),myMessage.mHeadImg,mContext);
                    myMessage.setMessage(record.getMessage());
                    convertView = myMessage;
                } else {
                    //如果没有设置，则新建
                    FriendMessage.MyMessage myMessage2 = new FriendMessage.MyMessage(mContext);
                    myMessage2.setMessage(record.getMessage());
                    //异步加载图像
                    Util.loadImageAsync(App.mPluralist.getImageName(), myMessage2.mHeadImg, mContext);
                    convertView = myMessage2;
                    convertView.setTag(R.id.tag_first, myMessage2);
                }

            } else {

                FriendMessage friendMessage = (FriendMessage) convertView.getTag(R.id.tag_second);

                if (friendMessage != null) {

                    friendMessage.setMessage(record.getMessage());
                    convertView = friendMessage;
                } else {

                    FriendMessage friendMessage2 = new FriendMessage(mContext);
                    friendMessage2.setMessage(record.getMessage());
                    //异步加载图像
                    String imageName = Util.getImageNameById(record.getMyId());
                    Util.loadImageAsync(imageName, friendMessage2.mHeadImg, mContext);

                    convertView = friendMessage2;
                    convertView.setTag(R.id.tag_second, friendMessage2);
                }
            }

        }


        return convertView;
    }


    /**
     * 使每一项不可点击
     *
     * @param position 位置
     * @return 是否可以点击
     */
    @Override
    public boolean isEnabled(int position) {

        return false;
    }
}
