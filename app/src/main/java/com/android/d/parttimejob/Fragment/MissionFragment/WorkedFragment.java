package com.android.d.parttimejob.Fragment.MissionFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Activity.Recruit.RecruitDetailActivity;
import com.android.d.parttimejob.Adapter.RecruitAdapter;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.recruit.InfoAbstract;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.Status;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**已录用页面
 * Created by Administrator on 2016/8/19.
 */
public class WorkedFragment extends Fragment {

    private TextView backgroundTV;

    private ListView listView;
    private ArrayList<InfoAbstract> listItems;

    private static final String URL = App.PREFIX+"Part-timeJob/InfoServlet";

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mission_status,container,false);
        //初始化
        initView(view);
        //加载进度
        progressDialog.show();
        //注册监听器
        registerOnClickListener();
        //从网络下载数据
        loadData();


        return view;
    }

    /**
     * 从网络下载数据
     */
    private void loadData() {

        String pId=  App.mPluralist.getId();

        HashMap<String,String> params=new HashMap<>();
        params.put("action", "recruitStatus");

        if (pId!=null){
            params.put("pluralistId", pId);
            Log.i("myapp.pid", pId);
        }

        params.put("status", Status.WORKED);

        HttpURLTask task=new HttpURLTask(URL,params,new HttpURLHandler(){
            @Override
            public void handleMessage(Message msg) {

                if (msg.what==200){

                    String jsonStr= (String) msg.obj;
                    listItems= JSONParser.toJavaBean(jsonStr,new TypeToken<ArrayList<InfoAbstract>>(){}.getType());

                    if (listItems!=null){

                        if (listItems.size()==0){
                            backgroundTV.setVisibility(View.VISIBLE);

                            progressDialog.dismiss();
                        }else {
                            backgroundTV.setVisibility(View.INVISIBLE);
                            // 设置数据适配器
                            RecruitAdapter adapter = new RecruitAdapter(getActivity(), listItems);
                            listView.setAdapter(adapter);
                            progressDialog.dismiss();
                        }

                    }

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                }
//                //点击详情页
//                if (listItems != null) {
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            InfoAbstract item = listItems.get(position);
//                            //将所在城市设置给地址
//                            item.getAddress().setCity(App.city);
//
//                            Intent intent = new Intent();
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable("infoAbstract", item);
//                            intent.putExtra("bundle", bundle);
//
//                            Log.i("infoAbstract", item.toString());
//                            intent.setClass(getActivity(), RecruitDetailActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//                }
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

                if (listItems != null) {
                    InfoAbstract item = listItems.get(position);
                    //将所在城市设置给地址
                    item.getAddress().setCity(App.mCity);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("infoAbstract", item);
                    intent.putExtra("bundle", bundle);

                    Log.i("infoAbstract", item.toString());
                    intent.setClass(getActivity(), RecruitDetailActivity.class);
                    startActivity(intent);
                }


            }
        });
    }

    /**
     * 初始化
     */
    private void initView(View view) {
        listView= (ListView) view.findViewById(R.id.id_mission_status_list);

        progressDialog=new ProgressDialog(getActivity());

        backgroundTV= (TextView) view.findViewById(R.id.id_mission_status_background_tv);
    }



}
