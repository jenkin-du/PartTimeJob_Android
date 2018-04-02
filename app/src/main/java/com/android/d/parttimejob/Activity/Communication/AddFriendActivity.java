package com.android.d.parttimejob.Activity.Communication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.d.parttimejob.Adapter.ContactsSearchedAdapter;
import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.Entry.Communication.Contact;
import com.android.d.parttimejob.Entry.Communication.SearchedPerson;
import com.android.d.parttimejob.MyView.Communication.MyProgressDialog;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.DatagramParser;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.Status;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 添加好友页面
 * Created by Administrator on 2016/8/29.
 */
public class AddFriendActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private NavigationBar mNavigationBar;//导航栏
    private EditText mSearchEdit;//搜索框
    private ImageView mDeleteImg;//删除
    private Button mSearchBtn;//搜索

    private ListView mResultListView;//结果列表

    private ArrayList<SearchedPerson> mPersonList;
    private ContactsSearchedAdapter mAdapter;

    private MyProgressDialog progressDialog;
    private static final int RequestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        //初始化
        init();
        //注册监听器
        setOnClickListener();

    }

    /**
     * 注册监听器
     */
    private void setOnClickListener() {
        mNavigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                AddFriendActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {
            }
        });

        mDeleteImg.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);

        mResultListView.setOnItemClickListener(this);
    }

    /**
     * 初始化
     */
    private void init() {
        App.addActivity(this);

        mNavigationBar = (NavigationBar) findViewById(R.id.id_add_friend_navi_bar);
        mSearchEdit = (EditText) findViewById(R.id.id_add_friend_search_edit);
        mResultListView = (ListView) findViewById(R.id.id_add_friend_result_listView);
        mDeleteImg = (ImageView) findViewById(R.id.id_add_friend_delete_img);
        mSearchBtn = (Button) findViewById(R.id.id_add_friend_search_btn);

        mPersonList = new ArrayList<>();
        mAdapter = new ContactsSearchedAdapter(mPersonList, this);
        mResultListView.setAdapter(mAdapter);

        progressDialog = new MyProgressDialog(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //删除编辑内容
            case R.id.id_add_friend_delete_img:

                mSearchEdit.setText("");
                break;

            //搜索
            case R.id.id_add_friend_search_btn:

                progressDialog.show();

                String s = mSearchEdit.getText().toString();

                boolean isNumber = checkIsNumber(s);
                if (isNumber) {
                    if (s.length() != 11) {
                        Toast.makeText(AddFriendActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
                        //根据搜索条件加载数据
                        loadSearchedData(s);
                    }
                } else {
                    //根据搜索条件加载数据
                    loadSearchedData(s);
                }

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //从扫描的二维码中获得数据
            Bundle bundle = data.getExtras();
            String code = bundle.getString("result");

            try {
                Contact friend = JSONParser.toJavaBean(code,Contact.class);
                String friendId = friend.getId();
                if (friendId != null) {
                    Intent intent = new Intent(AddFriendActivity.this, NewFriendActivity.class);
                    intent.putExtra("friendId", friendId);
                    startActivity(intent);
                }

            } catch (Exception e) {

                Toast.makeText(AddFriendActivity.this,"二维码错误",Toast.LENGTH_SHORT).show();
            }


        }
    }

    /**
     * 根据搜索条件加载数据
     */
    private void loadSearchedData(String condition) {

        String url = App.PREFIX + "Part-timeJob/PluralistServlet";

        final HashMap<String, String> params = new HashMap<>();
        params.put("action", "search");
        params.put("condition", condition);

        HttpURLTask task = new HttpURLTask(url, params, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 200) {

                    String jsonDatagram = (String) msg.obj;
                    String response = DatagramParser.getResponse(jsonDatagram);

                    if (response.equals(Status.SUCCESSFUL)) {

                        ArrayList<SearchedPerson> persons = DatagramParser.getEntity(jsonDatagram, new TypeToken<ArrayList<SearchedPerson>>() {
                        }.getType());

                        mPersonList.clear();
                        mPersonList.addAll(persons);

                        mAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(AddFriendActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(AddFriendActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        });
        task.start();


    }

    /**
     * 判断字符串中是否为数字
     */
    private boolean checkIsNumber(String s) {

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(s).matches();
    }


    /**
     * 点击item
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SearchedPerson person = mPersonList.get(position);
        String friendId = person.getpId();
        String imageName=person.getImageName();

        Intent intent = new Intent(AddFriendActivity.this, NewFriendActivity.class);
        intent.putExtra("friendId", friendId);
        intent.putExtra("imageName",imageName);
        startActivity(intent);

    }


}
