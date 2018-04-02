package com.android.d.parttimejob.Application;

import android.app.Activity;
import android.app.Application;

import com.android.d.parttimejob.Entry.Communication.ChatRecord;
import com.android.d.parttimejob.Entry.Communication.Contact;
import com.android.d.parttimejob.Entry.Communication.Message;
import com.android.d.parttimejob.Entry.Pluralist;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自定义全局类
 * Created by Administrator on 2016/8/11.
 */
public class App extends Application {


    public static String mCity = "南京市";
    //将已报名后的招聘信息id号保存起来
    private static HashMap<String, String> mEnrolledList;
    //收藏的列表
    private static ArrayList<String> mCancelledList;

    public static String PREFIX = "http://192.168.191.1:8080/";
    public static String IP = "192.168.191.1";
    public static int PORT = 2345;

    public static Pluralist mPluralist;

    //全局消息
    public static HashMap<String, ArrayList<ChatRecord>> mChatRecordsMap;
    //全局消息列表
    public static ArrayList<Message> mMessages;
    //全局联系人列表
    public static ArrayList<Contact> mContacts;
    //全局朋友id
    public static ArrayList<String> mFriends;


    //用于保存所有的activity
    public static ArrayList<Activity> mActivities;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        init();
    }


    public static HashMap<String, String> getEnrolledInfoList() {
        return mEnrolledList;
    }

    public static void setEnrolledInfoList(HashMap<String, String> enrolledInfoList) {
        App.mEnrolledList = enrolledInfoList;
    }

    public static ArrayList<String> getCollectedInfoList() {
        return mCancelledList;
    }

    public static void setCollectedInfoList(ArrayList<String> collectedInfoList) {
        App.mCancelledList = collectedInfoList;
    }

    /**
     * 添加activity
     */
    public static void addActivity(Activity activity) {

        if(!mActivities.contains(activity)){
            mActivities.add(activity);
        }
    }

    /**
     * 退出所有的activity
     */
    public static void exitAllActivity(){

        if (mActivities!=null){
            for (Activity activity:mActivities){
                activity.finish();
            }
        }

    }
    private void init() {

        //维护几个全局变量，用于后续操作
        mPluralist = new Pluralist();
        mPluralist.setGender("");
        mPluralist.setSchool("");
        mPluralist.setEmail("");
        mPluralist.setEducationBackground("");
        mPluralist.setExperience("");
        mPluralist.setFeature("");


        mEnrolledList = new HashMap<>();
        mCancelledList = new ArrayList<>();

        mChatRecordsMap = new HashMap<>();
        mMessages = new ArrayList<>();
        mContacts = new ArrayList<>();
        mFriends = new ArrayList<>();
        mActivities = new ArrayList<>();
    }

}
