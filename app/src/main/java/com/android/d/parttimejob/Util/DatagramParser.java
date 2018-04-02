package com.android.d.parttimejob.Util;


import com.android.d.parttimejob.Entry.Communication.Datagram;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * 数据包解析器
 * Created by Administrator on 2016/8/26.
 */
public class DatagramParser {

    /**
     * 装包
     */
    public static<T> String toJsonDatagram(String request,String response, T t){

        Datagram datagram=new Datagram();
        datagram.setRequest(request);
        datagram.setResponse(response);
        datagram.setJsonStream(JSONParser.toJSONString(t));

        return JSONParser.toJSONString(datagram);
    }

    /**
     * 从json格式的数据包流中获取Javabean对象
     */
    public static<T> T getEntity(String jsonDatagram,Type type){

    	Datagram datagram=JSONParser.toJavaBean(jsonDatagram, new TypeToken< Datagram>(){}.getType());
    	String jsonStream=datagram.getJsonStream();


        return JSONParser.toJavaBean(jsonStream,type);
    }

    /**
     * 从json格式的数据包流中获取Javabean对象
     */
    public static<T> T getEntity(String jsonDatagram,Class<T> cls){

        Datagram datagram=JSONParser.toJavaBean(jsonDatagram, Datagram.class);
        String jsonStream=datagram.getJsonStream();


        return JSONParser.toJavaBean(jsonStream,cls);
    }

    /**
     * 从json格式的数据包流中获取请求
     */
    public static String getRequest(String jsonDatagram){

    	Datagram datagram=JSONParser.toJavaBean(jsonDatagram, new TypeToken<Datagram>(){}.getType());

        return datagram.getRequest();
    }

    /**
     * 从json格式的数据包流中获取请求
     */
    public static String getResponse(String jsonDatagram){

        Datagram datagram=JSONParser.toJavaBean(jsonDatagram, new TypeToken<Datagram>(){}.getType());

        return datagram.getResponse();
    }
}
