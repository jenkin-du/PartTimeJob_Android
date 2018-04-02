package com.android.d.parttimejob.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.Communication.Contact;
import com.android.d.parttimejob.Entry.Communication.Datagram;
import com.android.d.parttimejob.Entry.Communication.OnlineRecord;
import com.android.d.parttimejob.Entry.recruit.InfoAbstract;
import com.android.d.parttimejob.Task.SendMessageTask;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * 获得系统当前时间
 * Created by Administrator on 2016/8/24.
 */
public class Util {

    /**
     * 获取当前时间
     */
    public static String getNowTime() {

        Date now = new Date(); //获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = dateFormat.format(now);

        return time;
    }


    /**
     * 获得ip地址
     */
    public static String getIpAdd(Context context) {
        // 判断wifi是否开启
        String ipString = "";
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();

            // 格式转换
            ipString = (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF)
                    + "." + ((ipAddress >> 16) & 0xFF) + "."
                    + ((ipAddress >> 24) & 0xFF);
        } else {
            // 如果wifi没有开启的话，就获取3G的IP
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface
                        .getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();


                    // 遍历所有的网卡设备，一般移动设备上只有2张网卡，其中一张是环回网卡
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // 过滤掉环回网卡和IPv6
                        if (!inetAddress.isLoopbackAddress()
                                && !(inetAddress instanceof Inet6Address)) {

                            ipString = inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {
                Log.e("TAG", "getIpAddress()" + ex.toString());
            }
        }
        return ipString;
    }

    /**
     * 经本机的ip地址发送给服务器
     */
    public static void notifyOnline(final Context context) {


        final OnlineRecord record = new OnlineRecord();
        record.setId(App.mPluralist.getId());
        record.setIp(getIpAdd(context));
        //通知上线
        String request = Action.ON_LINE;

        String jsonDatagram = DatagramParser.toJsonDatagram(request, null, record);

        SendMessageTask task = new SendMessageTask("192.168.191.1", 2345, jsonDatagram, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {
                String jsonDatagram = (String) msg.obj;
                Datagram datagram1 = JSONParser.toJavaBean(jsonDatagram, new TypeToken<Datagram>() {
                }.getType());

                if (datagram1 != null) {

                    String response = datagram1.getResponse();
                    if (response == null || !response.equals(Status.SUCCESSFUL)) {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        task.start();
    }


    /**
     * 将数据封装成数据包形式发送出去
     */

    public static Datagram PacketData(String request, String jsonStream) {

        Datagram datagram = new Datagram();
        datagram.setJsonStream(jsonStream);
        datagram.setRequest(request);

        return datagram;

    }


    /**
     * 从服务器获取图片
     */
    public static void getImageByName(final String imageName, String url, final ImageView userHead) {


        //获取头像
        HashMap<String, String> imageParam = new HashMap<>();
        imageParam.put("action", "getImage");
        imageParam.put("imageName", imageName);
        HttpURLTask imageTask = new HttpURLTask(url, imageParam, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 200) {
                    String imageCode = (String) msg.obj;
                    byte[] buffer = Base64.decode(imageCode, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    //设置头像
                    if (bitmap != null) {
                        userHead.setImageBitmap(bitmap);
                    }
                    //缓存图像
                    storeImage(imageName, bitmap);
                }
            }
        });
        imageTask.start();
    }

    /**
     * 从服务器获取图片
     */
    public static void getImageByPhone(final String phone, String url, final ImageView userHead) {

        //获取头像
        HashMap<String, String> imageParam = new HashMap<>();
        imageParam.put("action", "getImage");
        imageParam.put("phone", phone);
        HttpURLTask imageTask = new HttpURLTask(url, imageParam, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 200) {
                    String imageCode = (String) msg.obj;
                    byte[] buffer = Base64.decode(imageCode, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    //设置头像
                    if (bitmap != null) {
                        userHead.setImageBitmap(bitmap);
                        //缓存图像
                        storeImage(phone + System.currentTimeMillis(), bitmap);
                    }

                }
            }
        });
        imageTask.start();
    }

    /**
     * 计算更新时间
     */
    public static String getRightTime(String time) {

        String rightTime = "";
        String nowTime = getNowTime();

        int cYear = getOkNumber(nowTime.substring(0, 4));
        int cMonth = getOkNumber(nowTime.substring(5, 7));
        int cDay = getOkNumber(nowTime.substring(8, 10));

        int year = getOkNumber(time.substring(0, 4));
        int month = getOkNumber(time.substring(5, 7));
        int day = getOkNumber(time.substring(8, 10));

        if (cYear - year > 0) {
            rightTime = String.valueOf(cYear - year) + "年前";
            return rightTime;
        } else if (cMonth - month > 0) {
            rightTime = String.valueOf(cMonth - month) + "月前";
            return rightTime;
        } else if (cDay - day == 1) {
            rightTime = "昨天";
            return rightTime;
        } else if (cDay - day > 0) {
            rightTime = String.valueOf(cDay - day) + "天前";
            return rightTime;
        } else {
            rightTime = time.substring(11);
            return rightTime;
        }
//        } else if (cHour - hour > 0) {
//            rightTime = String.valueOf(cHour - hour) + "小时前";
//            return rightTime;
//        } else if (cMinute - minute > 0) {
//            rightTime = String.valueOf(cMinute - minute) + "分钟前";
//            return rightTime;
//        } else if (cSecond - second > 0) {
//            rightTime = "刚刚";
//            return rightTime;
//        }
        // return rightTime;
    }

    /**
     * 根据字符串数字获得正确的int型数字
     */
    private static int getOkNumber(String numStr) {

        String str = numStr;
        if (numStr.substring(0, 1).equals("0")) {
            str = numStr.substring(1);
        }
        return Integer.parseInt(str);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据种类获取颜色值
     *
     * @param category 种类
     * @return 颜色值
     */
    public static int getColorByCategory(String category) {

        int color = 0;

        switch (category) {
            case "其他":
                color = Color.rgb(135, 206, 235);
                break;
            case "家教":
                color = Color.rgb(144, 238, 144);//PaleGreen
                break;
            case "服务":
                color = Color.rgb(255, 165, 0);//橙色
                break;
            case "促销":
                color = Color.YELLOW;
                break;
            case "实习":
                color = Color.rgb(238, 59, 59);//棕色
                break;
            case "派单":
                color = Color.rgb(160, 32, 240);//紫色
                break;
            default:
                color = Color.rgb(135, 206, 235);
                break;

        }

        return color;
    }

    /**
     * 储存照片
     */
    public static String storeImage(String imageName, Bitmap bitmap) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.v("TestFile", "SD card is not avaiable/writeable right now.");
            return "";
        }


        String directory = Environment.getExternalStorageDirectory().getPath() + "/Part-timeJob/Image/";
        File file = new File(directory);
        FileOutputStream bout = null;

        if (!file.exists()) {
            file.mkdirs(); // 创建文件夹
        }

        String imagePath = directory + imageName;

        try {
            bout = new FileOutputStream(imagePath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);// 把数据写入文件
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bout != null) {
                    bout.flush();
                }
                if (bout != null) {
                    bout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imagePath;
    }


    /**
     * 取出照片
     *
     * @param imageName 图片名
     * @return 图片
     */
    public static Bitmap restoreImage(String imageName, Context context) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.v("TestFile", "SD card is not avaiable/writeable right now.");
            return null;
        }

        String directory = Environment.getExternalStorageDirectory().getPath() + "/Part-timeJob/Image/";
        String imagePath = directory + imageName;
        File imageFile = new File(imagePath);

        Bitmap bitmap = null;

        if (imageFile.exists()) {
            Uri uri = Uri.fromFile(imageFile);

            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                        .openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 删除图片
     */
    public static void deleteImage(String imageName) {

        String directory = Environment.getExternalStorageDirectory().getPath() + "/Part-timeJob/Image/";
        String imagePath = directory + imageName;
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            imageFile.delete();
        }
    }


    /**
     * 异步加载图片
     */
    public static void loadImageAsync(String imageName, ImageView userHead, Context context) {

        String url = App.PREFIX + "Part-timeJob/PluralistServlet";

        if (imageName != null && context != null && userHead != null) {
            Bitmap bitmap = restoreImage(imageName, context);
            if (bitmap != null) {
                userHead.setImageBitmap(bitmap);
            } else {
                getImageByName(imageName, url, userHead);
            }
        }

    }

    /**
     * 根据好友ID获取好友头像
     */
    public static String getImageNameById(String id) {

        ArrayList<Contact> contexts = App.mContacts;
        String imageName = "";

        if (id != null) {
            for (int i = 0; i < contexts.size(); i++) {
                if (contexts.get(i).getId().equals(id)) {
                    imageName = contexts.get(i).getImageName();
                    break;
                }
            }
        }


        return imageName;
    }


    /**
     * 对距离进行筛选
     */
    public static void selectDistance(ArrayList<InfoAbstract> items) {
        if (items == null) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {

            for (int j = i + 1; j < items.size(); j++) {

                if (items.get(j).getDistance() < items.get(i).getDistance()) {

                    InfoAbstract temp = items.get(j);
                    items.set(j, items.get(i));
                    items.set(i, temp);
                }
            }
        }

    }

    /**
     * 对薪水进行筛选
     */
    public static void selectSalary(ArrayList<InfoAbstract> items) {

        if (items == null) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {

            for (int j = i + 1; j < items.size(); j++) {

                int si = getStandardSalary(items.get(i).getSalary());
                int sj = getStandardSalary(items.get(j).getSalary());

                if (sj > si) {
                    InfoAbstract temp = items.get(j);
                    items.set(j, items.get(i));
                    items.set(i, temp);
                }
            }
        }
    }

    private static int getStandardSalary(String salary) {
        if (salary == null || salary.equals("")) {
            return 0;
        }

        int standardSalary = 0;
        int index = salary.indexOf("/");
        String suffix = salary.substring(index + 1);

        int prefix;
        switch (suffix) {
            case "月":
                prefix = Integer.valueOf(salary.substring(0, index - 2).trim());
                standardSalary = prefix / 30;
                break;
            case "天":
                prefix = Integer.valueOf(salary.substring(0, index - 2).trim());
                standardSalary = prefix;
                break;
            case "小时":
                prefix = Integer.valueOf(salary.substring(0, index - 2).trim());
                standardSalary = prefix * 30;
                break;
            default:
                break;
        }

        return standardSalary;
    }


}
