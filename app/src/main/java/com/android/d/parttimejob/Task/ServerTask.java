package com.android.d.parttimejob.Task;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerTask extends Thread {

    public static ServerSocket server = null;

    private Executor cachedThreadPool = Executors.newCachedThreadPool();

    private Handler handler;

    public static boolean isEnable = true;

    public ServerTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {

        try {

            server = new ServerSocket(5432);
            Log.i("serverTask", "开启服务器-----------------");

            while (isEnable) {
                Socket socket = server.accept();
                //加入线程池，执行线程任务
                cachedThreadPool.execute(new handleMessageTask(socket, handler));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
