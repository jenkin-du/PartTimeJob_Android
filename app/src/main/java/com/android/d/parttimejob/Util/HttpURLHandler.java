package com.android.d.parttimejob.Util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.reflect.Type;

/**
 * 网络输入流处理器
 * Created by Administrator on 2016/8/4.
 */
public class HttpURLHandler extends Handler {


    private Context context;//上下文信息
    private Type type;//反射类型

    public HttpURLHandler(Context context, Type type) {
        this.context = context;
        this.type = type;
    }

    public HttpURLHandler(Context context) {
        this.context = context;
    }

    public HttpURLHandler() {

    }

    @Override
    public void handleMessage(Message msg) {
        //获得网络响应码
        int responseCode = msg.what;

        if (responseCode == 200) {
            String jsonStr = (String) msg.obj;
            handleJSONString(jsonStr);
        } else {
            Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
        }
    }


    public <T> void handleJSONString(String jsonString) {

        T obj = JSONParser.toJavaBean(jsonString, type);
        handleData(obj);

    }

    public <T> void handleData(T obj) {

    }
}
