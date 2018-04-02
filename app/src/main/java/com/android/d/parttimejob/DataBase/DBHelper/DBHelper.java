package com.android.d.parttimejob.DataBase.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 自定义数据库帮助类
 * Created by Administrator on 2016/8/13.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int version = 1;
    public static final String DB_NAME="Part-time_Job.db";

    public static final String T_PLURALIST ="t_pluralist";
//    public static final String T_RECRUIT ="t_recruit";
    public static final String T_FRIEND ="t_friend";
    public static final String T_CHAT ="t_chat";

    private static final String CREATE_P_TABLE ="create table if not exists "+ T_PLURALIST +
            "(pluralist_id varchar(10) primary key, name varchar(20), phone varchar(11)," +
            "password varchar(50),gender varchar(2),age int,height int,education_background varchar(10)," +
            "salary float,head_image_name varchar(255),"+
            "email varchar(50),school varchar(50),feature varchar(255),experience varchar(255))";
//
//    private static final String CREATE_R_TABLE ="create table "+ T_RECRUIT +
//            "(pluralist_id varchar(10) primary key,info_id varchar(10),status varchar(10))";

    //创建好友表
    private static final String CREATE_F_TABLE ="create table if not exists "+ T_FRIEND +
            "(id integer primary key autoincrement ,my_id varchar(10) not null,friend_id varchar(10) not null,friend_name varchar(10),image_name varchar(50),is_chatting integer)";

    //创建消息表
    private static final String CREATE_C_TABLE ="create table if not exists "+ T_CHAT +
            "(id integer primary key autoincrement ,_my_id varchar(10) not null,_friend_id varchar(10) not null,my_id varchar(10) not null,friend_id varchar(10) not null,message text,time varchar(20))";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_F_TABLE);
        db.execSQL(CREATE_C_TABLE);
        db.execSQL(CREATE_P_TABLE);

        Log.i("DBHelper", "create Database------------->");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
