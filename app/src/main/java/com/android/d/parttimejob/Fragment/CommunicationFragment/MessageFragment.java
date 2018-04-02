package com.android.d.parttimejob.Fragment.CommunicationFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.d.parttimejob.Activity.Communication.ChatActivity;
import com.android.d.parttimejob.Adapter.MessageAdapter;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.Communication.ChatRecord;
import com.android.d.parttimejob.Entry.Communication.Contact;
import com.android.d.parttimejob.Entry.Communication.FriendRequest;
import com.android.d.parttimejob.Entry.Communication.Message;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Util;

import java.util.ArrayList;


/**
 * 消息列表
 * Created by D on 2016/5/31.
 */
public class MessageFragment extends Fragment {


    private ListView mNewsList;

    private static ArrayList<Message> mMessageList;
    private static MessageAdapter mMessageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message, container, false);

        //初始化
        init(view);
        //设置点击事件
        setListener();


        return view;
    }


    /**
     * 设置点击事件
     */
    private void setListener() {

        mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("messageFragment", "onItemClick: message" + mMessageList.toString());
                Message message = mMessageList.get(position);
                String friendId = message.getId();

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("friendId", friendId);
                startActivity(intent);

            }
        });

        mNewsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

//                final Message message = mMessageList.get(position);
//                String friendId =message.getId();
//                for (int i=0;i<App.mContacts.size();i++){
//                    if (friendId.equals(App.mContacts.get(i).getId())){
//                        App.mContacts.get(i).setChatting(false);
//                    }
//                }
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("删除？");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        App.mMessages.remove(message);
//                        mMessageAdapter.notifyDataSetChanged();
//
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();

                return true;
            }
        });
    }


    /**
     * 初始化
     */
    private void init(View view) {
        //得到新闻列表的引用
        mNewsList = (ListView) view.findViewById(R.id.id_communication_message_list);


        mMessageList = App.mMessages;
        mMessageAdapter = new MessageAdapter(mMessageList, getActivity());
        mNewsList.setAdapter(mMessageAdapter);

    }


    /**
     * 将新添加的朋友消息添加到消息列表
     */
    public void addMessage(FriendRequest request, Contact contact) {

        if (request != null && contact != null) {

            Message message = new Message();
            message.setId(contact.getId());
            message.setName(contact.getName());
            message.setLastMessage(request.getRequestReason());
            message.setTime(Util.getRightTime(request.getTime()));

            //将消息保存在全局变量中
            App.mMessages.add(message);
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 将新消息更新消息列表
     */
    public void updateMessage(ChatRecord record) {

        if (record != null) {
            String friendId = record.getMyId();

            for (int i = 0; i < mMessageList.size(); i++) {

                if (mMessageList.get(i).getId().equals(friendId)) {

                    mMessageList.get(i).setTime(Util.getRightTime(record.getTime()));
                    mMessageList.get(i).setLastMessage(record.getMessage());
                    mMessageAdapter.notifyDataSetChanged();
                    break;
                }
            }

        }
    }


    /**
     * 根据我的聊天更新消息列表
     *
     * @param record 小消息
     */
    public static void updateMessageOnChatting(ChatRecord record) {
        String friendId = record.getFriendId();

        for (int i = 0; i < mMessageList.size(); i++) {
            if (mMessageList.get(i).getId().equals(friendId)) {
                mMessageList.get(i).setTime(Util.getRightTime(record.getTime()));
                mMessageList.get(i).setLastMessage(record.getMessage());

                mMessageAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 删除好友信息
     */
    public static void deleteMessage(String friendId) {

        for (int i = 0; i < App.mMessages.size(); i++) {
            if (App.mMessages.get(i).getId().equals(friendId)) {

                App.mMessages.remove(i);
                mMessageAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
