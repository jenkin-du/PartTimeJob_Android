package com.android.d.parttimejob.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 网络流解析器
 * Created by Administrator on 2016/7/31.
 */
public class StreamParser {

    /**
     * 该方法从输入流中获取字符串
     *
     * @param is 网络输入流
     * @return 字符串
     */
    public static String parseInputStream(InputStream is) {

        String result = "";
        InputStreamReader isr = null;

        try {
            String line;
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);

            line = br.readLine();
            while (line != null) {
                result += line;
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert isr != null;
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
