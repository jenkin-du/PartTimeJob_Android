package com.android.d.parttimejob.Task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.d.parttimejob.Util.StreamWapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendMessageTask extends Thread{

    private String disName;//域名
    private int port;//端口号

    private String msg;
    private Handler handler;

    private Socket socket=null;




    public SendMessageTask(String disName, int port, String msg, Handler handler) {
        this.disName = disName;
        this.port = port;
        this.msg = msg+"\r\n";
        this.handler = handler;
    }

    @Override
    public void run() {

        Log.i("sendMessage", "run: message = "+msg);
        try {
            socket=new Socket(disName,port);
            //向服务器发送数据
            OutputStream os=socket.getOutputStream();
            PrintWriter writer= StreamWapper.toPrintWriter(os);
            writer.print(msg);
            writer.flush();

            //接受来自服务器的数据
            InputStream is=socket.getInputStream();
            BufferedReader br=StreamWapper.toBufferedReader(is);

            String jsonData=br.readLine();
            Message message=new Message();
            message.obj=jsonData;
            //将接受到的数据发送出去，用于更新UI
            handler.sendMessage(message);

            os.close();
            is.close();
            br.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (socket!=null){
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
