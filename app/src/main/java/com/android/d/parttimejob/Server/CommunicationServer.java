package com.android.d.parttimejob.Server;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.d.parttimejob.Entry.Communication.ChatRecord;
import com.android.d.parttimejob.Entry.Communication.FriendRequest;
import com.android.d.parttimejob.Entry.Communication.OnlineRecord;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Task.ServerTask;
import com.android.d.parttimejob.Util.Action;
import com.android.d.parttimejob.Util.DatagramParser;
import com.android.d.parttimejob.Util.Status;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * 通信服务
 * Created by Administrator on 2016/8/23.
 */
public class CommunicationServer extends Service {

    private Intent broadcastIntent;


    private static int ID = 1000;
    Bundle bundle;


    @Override
    public void onCreate() {
        super.onCreate();

        broadcastIntent = new Intent();
        bundle=new Bundle();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        ServerTask task = new ServerTask(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //处理数据包信息
                handleDatagram(msg);
            }
        });
        task.start();

        return START_STICKY;
    }


    /**
     * 处理数据包信息
     */
    private void handleDatagram(Message msg) {

        String jsonDatagram = (String) msg.obj;
        String request = DatagramParser.getRequest(jsonDatagram);

        OnlineRecord onlineRecord;


        //先判断应用是否在前台
        // boolean isForward = isRunningForeground(getApplicationContext());

        //聊天消息
        switch (request) {
            case Action.COMMUNICATE:

                Log.i("server", "COMMUNICATE: "+jsonDatagram);
//                ChatRecord record = DatagramParser.getEntity(jsonDatagram, new TypeToken<ChatRecord>() {
//                }.getType());
                ChatRecord record=DatagramParser.getEntity(jsonDatagram,ChatRecord.class);

                //发广播
                bundle.clear();
                bundle.putParcelable(Status.CHAT_RECORD, record);
                broadcastIntent.setAction(Status.NEW_MESSAGE);
                broadcastIntent.putExtra(Status.MESSAGE,bundle);
                sendOrderedBroadcast(broadcastIntent,null);

                //发通知
//            if (!isForward) {
////
////                String ticker = "你有一条新消息";
////
////                String friendId = record.getMyId();
////                String title = App.mFriendMap.get(friendId).getName();
////                String content = record.getMessage();
////                //发送通知
////                launchNotification(title, content, ticker, null);
//            }


                //添加好友
                break;
            case Action.ADD_FRIEND:

                FriendRequest friendRequest = DatagramParser.
                        getEntity(jsonDatagram, new TypeToken<FriendRequest>() {
                        }.getType());

                bundle.clear();
                bundle.putParcelable(Status.FRIEND_REQUEST, friendRequest);
                broadcastIntent.setAction(Status.NEW_MESSAGE);
                broadcastIntent.putExtra(Status.MESSAGE,bundle);
                sendOrderedBroadcast(broadcastIntent,null);
                break;

            //上线
            case Action.ON_LINE:

                 onlineRecord = DatagramParser.getEntity(jsonDatagram, new TypeToken<OnlineRecord>() {
                }.getType());

                bundle.clear();
                bundle.putParcelable(Action.ON_LINE, onlineRecord);
                broadcastIntent.setAction(Status.NEW_MESSAGE);
                broadcastIntent.putExtra(Status.MESSAGE,bundle);
                sendOrderedBroadcast(broadcastIntent,null);
                break;
            //下线
            case Action.OFF_LINE:
                 onlineRecord = DatagramParser.getEntity(jsonDatagram, new TypeToken<OnlineRecord>() {
                }.getType());

                bundle.clear();
                bundle.putParcelable(Action.OFF_LINE, onlineRecord);
                broadcastIntent.setAction(Status.NEW_MESSAGE);
                broadcastIntent.putExtra(Status.MESSAGE,bundle);
                sendOrderedBroadcast(broadcastIntent,null);
                break;
            default:
                break;



        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ServerTask.isEnable = false;
        try {
            ServerTask.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 启动通知
     */
    private void launchNotification(String title, String content, String ticker, Context context) {


        //获得系统服务
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //获得通知管理器
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();

        if (context != null) {

            //点击打开
            Intent intent = new Intent();
            intent.setClass(CommunicationServer.this, context.getClass());

            PendingIntent pendingIntent = PendingIntent.getActivity(CommunicationServer.this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);

        }


        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker);


        //发出通知
        ID++;
        Notification notification = builder.build();
        manager.notify(ID, notification);

    }


    /**
     * 判断应用是否在前台
     */
    private boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName())) {
            return true;
        }

        return false;
    }
}
