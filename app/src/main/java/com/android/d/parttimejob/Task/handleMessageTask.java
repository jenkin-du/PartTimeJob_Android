package com.android.d.parttimejob.Task;

import android.os.Handler;
import android.os.Message;

import com.android.d.parttimejob.Util.DatagramParser;
import com.android.d.parttimejob.Util.Status;
import com.android.d.parttimejob.Util.StreamWapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**处理socket类
 * Created by Administrator on 2016/8/23.
 */
public class handleMessageTask extends Thread {

    private Socket socket;
    private Handler handler;

    public handleMessageTask(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void run() {

        try {
            OutputStream os=socket.getOutputStream();
            InputStream is=socket.getInputStream();

            //接受来自服务器的数据
            BufferedReader br= StreamWapper.toBufferedReader(is);
            String jsonDatagram=br.readLine();

            Message message=new Message();

            message.obj=jsonDatagram;
            handler.sendMessage(message);


            //回应服务器
            PrintWriter writer=StreamWapper.toPrintWriter(os);

            String jsonDatagram2= DatagramParser.toJsonDatagram(null, Status.SUCCESSFUL,null);
            writer.print(jsonDatagram2);
            writer.flush();

            os.close();
            is.close();
            writer.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
                socket=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
