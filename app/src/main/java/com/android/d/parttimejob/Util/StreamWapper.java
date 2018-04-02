package com.android.d.parttimejob.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**流的包装类
 * Created by Administrator on 2016/8/23.
 */
public class StreamWapper {

    public static PrintWriter toPrintWriter(OutputStream out){

        return new PrintWriter( new BufferedWriter( new OutputStreamWriter(out)),true);
    }

    public static BufferedReader toBufferedReader(InputStream in){

        return new BufferedReader( new InputStreamReader(in));
    }

}
