package com.android.d.parttimejob.Util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 网络加载类
 * Created by Administrator on 2016/8/1.
 */
public class HttpURLTask extends Thread {


    private String REQUEST_METHOD = "GET";
    private Handler mHandler = null;
    private String mURL = null;
    private String params;

    public HttpURLTask() {

    }

    public HttpURLTask(String url, HashMap<String, String> params, Handler handler) {

        this.mHandler = handler;
        this.mURL = url + linkParams(params);
    }

    public HttpURLTask(String url, String method, HashMap<String, String> paramsMap, Handler handler) {

        if (method.toLowerCase().equals("get")) {
            new HttpURLTask(url, paramsMap, handler);
        } else if (method.toLowerCase().equals("post")) {
            REQUEST_METHOD = "POST";

            this.mHandler = handler;
            this.mURL = url;
            this.params = linkParams(paramsMap);

            Log.i("http_params", this.params);
            Log.i("url", url);
        }

    }


    @Override
    public void run() {
        //get方式
        if (REQUEST_METHOD.equals("GET")) {
            httpURLGetConnection(mURL, mHandler);
            //post方式
        } else if (REQUEST_METHOD.equals("POST")) {
            Log.i("method", REQUEST_METHOD);
            httpURLPostConnection(mURL, params, mHandler);
        }

    }

    private void httpURLPostConnection(String mURL, String params, Handler mHandler) {
        Message msg = new Message();

        URL url;

        HttpURLConnection conn = null;
        InputStream is = null;


        try {
            url = new URL(mURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setReadTimeout(50000);
            conn.setUseCaches(false);

            conn.connect();
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(params);

            dos.flush();
            dos.close();

            int responseCode = conn.getResponseCode();
            Log.i("code", String.valueOf(responseCode));
            String result = "";

            if (responseCode == 200) {
                is = conn.getInputStream();
                //将结果解析成字符串
                result = StreamParser.parseInputStream(is);

            }
            //将结果封装成消息，发送出去
            msg.obj = result;
            msg.what = responseCode;//将网络响应码传递出去
            mHandler.sendMessage(msg);

            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 利用get方式请求数据
     *
     * @param mURLStr  url
     * @param mHandler handler
     */
    private static void httpURLGetConnection(String mURLStr, Handler mHandler) {
        Message msg = new Message();

        URL url;

        HttpURLConnection conn = null;
        InputStream is = null;
        Log.i("url", mURLStr);

        try {
            url = new URL(mURLStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setReadTimeout(50000);

            int responseCode = conn.getResponseCode();
            Log.i("code", String.valueOf(responseCode));
            String result = "";

            if (responseCode == 200) {
                is = conn.getInputStream();
                //将结果解析成字符串
                result = StreamParser.parseInputStream(is);
            }
            //将结果封装成消息，发送出去
            msg.obj = result;
            msg.what = responseCode;//将网络响应码传递出去
            mHandler.sendMessage(msg);

            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 让参数连接起来
     *
     * @param params map型的参数
     * @return 连接起来的参数
     */
    private String linkParams(HashMap<String, String> params) {

        boolean requestGet = true;

        if (REQUEST_METHOD.equals("POST")) {
            requestGet = false;
        }

        StringBuilder urlSB;
        urlSB = new StringBuilder();
        if (!params.isEmpty()) {
            Set<String> set = params.keySet();
            Iterator<String> iterator = set.iterator();

            boolean first = true;
            while (iterator.hasNext()) {
                String request = iterator.next();
                String result = params.get(request);

                try {
                    if (first) {
                        if (requestGet) {
                            urlSB.append("?");
                        }
                        urlSB.append(request).append("=").append(URLEncoder.encode(result, "utf-8"));
                        first = false;
                    } else {
                        urlSB.append("&");
                        urlSB.append(request).append("=").append(URLEncoder.encode(result, "utf-8"));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }

        return urlSB.toString();
    }


    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public String getURLStr() {
        return mURL;
    }

    public void setURLStr(String mURLStr) {
        this.mURL = mURLStr;
    }
}
