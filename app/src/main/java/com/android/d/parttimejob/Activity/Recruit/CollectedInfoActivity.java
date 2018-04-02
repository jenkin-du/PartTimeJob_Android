package com.android.d.parttimejob.Activity.Recruit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Adapter.RecruitAdapter;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.recruit.InfoAbstract;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 收藏页面
 * Created by Administrator on 2016/8/21.
 */
public class CollectedInfoActivity extends Activity {

    private TextView backgroundTv;//背景数据

    private NavigationBar navigationBar;

    private ListView listView;
    private ArrayList<InfoAbstract> listItems;

    private static final String URL = App.PREFIX + "Part-timeJob/InfoServlet";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection);
        //初始化
        init();
        //注册监听器
        registerOnClickListener();

        progressDialog.show();
        //从网络下载数据
        loadData();


    }

    /**
     * 初始化
     */
    private void init() {
        App.addActivity(this);

        navigationBar = (NavigationBar) findViewById(R.id.id_collected_navigation_bar);
        listView = (ListView) findViewById(R.id.id_collected_listView);
        progressDialog = new ProgressDialog(this);

        backgroundTv = (TextView) findViewById(R.id.id_collected_background_tv);
    }


    /**
     * 从网络下载数据
     */
    private void loadData() {

        String pId =  App.mPluralist.getId();

        HashMap<String, String> params = new HashMap<>();
        params.put("action", "getCollectedInfo");

        if (pId != null) {
            params.put("pluralistId", pId);
        }

        HttpURLTask task = new HttpURLTask(URL, params, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 200) {

                    String jsonStr = (String) msg.obj;
                    listItems = JSONParser.toJavaBean(jsonStr, new TypeToken<ArrayList<InfoAbstract>>() {
                    }.getType());

                    if (listItems != null) {

                        Log.i("loadData", listItems.size() + "");

                        if (listItems.size() == 0) {
                            progressDialog.dismiss();
                            backgroundTv.setVisibility(View.VISIBLE);

                            listView.setVisibility(View.GONE);

                        } else {
                            listView.setVisibility(View.VISIBLE);
                            backgroundTv.setVisibility(View.INVISIBLE);
                            // 设置数据适配器
                            RecruitAdapter adapter = new RecruitAdapter(CollectedInfoActivity.this, listItems);
                            listView.setAdapter(adapter);
                            progressDialog.dismiss();
                        }

                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CollectedInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }

            }
        });
        task.start();

    }

    /**
     * 注册监听器
     */
    private void registerOnClickListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listItems != null && listItems.size() != 0) {
                    InfoAbstract item = listItems.get(position);
                    //将所在城市设置给地址
                    item.getAddress().setCity(App.mCity);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("infoAbstract", item);
                    intent.putExtra("bundle", bundle);

                    intent.putExtra("collection", true);

                    Log.i("infoAbstract", item.toString());
                    intent.setClass(CollectedInfoActivity.this, RecruitDetailActivity.class);
                    startActivity(intent);
                }


            }
        });


        navigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                CollectedInfoActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        loadData();
    }
}
