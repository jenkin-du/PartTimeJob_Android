package com.android.d.parttimejob.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.d.parttimejob.R;


/**
 * 自定义导航栏
 * Created by Administrator on 2016/8/9.
 */
public class NavigationBar extends LinearLayout implements View.OnClickListener {


    private OnClickListener listener;

    private TextView mTitle;//标题
    private ImageView mBackBtn;//返回按钮
    private ImageView mImg;//设置按钮
    private TextView mRightText;//右边文字

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.navigation_bar,this,true);
        //初始化
        initView(view);
        //获得自定义属性
        getAttrs(context, attrs);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    /**
     * 注册监听器
     */
    public void registerListener(OnClickListener listener) {
        this.listener = listener;
    }


    /**
     * 初始化
     */
    private void initView(View view) {

        mTitle = (TextView) view.findViewById(R.id.id_view_navi_title);
        mBackBtn = (ImageView) view.findViewById(R.id.id_view_navi_back);
        mImg = (ImageView) view.findViewById(R.id.id_view_navi_img);
        mRightText = (TextView) view.findViewById(R.id.id_view_navi_right_text);

        mImg.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mRightText.setOnClickListener(this);

    }


    /**
     * 获得自定义属性
     */
    private void getAttrs(Context context, AttributeSet attrs) {

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar);
        boolean imgVisible = arr.getBoolean(R.styleable.NavigationBar_img_visible, false);
        String text = arr.getString(R.styleable.NavigationBar_title_text);
        String rightText=arr.getString(R.styleable.NavigationBar_right_text);
        boolean textVisible=arr.getBoolean(R.styleable.NavigationBar_right_text_visible,false);
        int resId=arr.getResourceId(R.styleable.NavigationBar_img_res,R.drawable.settings_blue);
        int color=arr.getColor(R.styleable.NavigationBar_right_text_color, getResources().getColor(R.color.bar_bg_blue));

        mTitle.setText(text);
        mImg.setImageResource(resId);
        mRightText.setText(rightText);
        mRightText.setTextColor(color);

        if (imgVisible) {
            mImg.setVisibility(VISIBLE);
        }

        if (textVisible){
            mRightText.setVisibility(VISIBLE);
        }

        arr.recycle();
    }

    /**
     * 设置文字
     */
    public void setTitle(String text) {
        mTitle.setText(text);
    }

    /**
     * 设置右边文字
     */
    public void setRightText(String text){
        mRightText.setText(text);
    }

    /**
     * 设置右边文字颜色
     */
    public void setRightTextColor(int colorId){
        mRightText.setTextColor(getResources().getColor(colorId));
    }

    /**
     * 设置右边文字可见性
     */
    public void setRightTextVisiable(boolean visibility){

        if (visibility){
            mRightText.setVisibility(VISIBLE);
        }else {
            mRightText.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        if(listener!=null){

            if (v.getId() == R.id.id_view_navi_back) {
                listener.onClickBack();
            } else if (v.getId() == R.id.id_view_navi_img) {
                listener.onClickImg();
            }else if (v.getId()==R.id.id_view_navi_right_text){
                listener.onClickRightText();
            }
        }
    }


    /**
     * 自定义点击接口
     */
    public interface OnClickListener {
        void onClickBack();//点击返回

        void onClickImg();//点击设置

        void onClickRightText();//点击右边文字
    }

    public void setRightImageVisible(boolean visible){

        if (visible){
            mImg.setVisibility(VISIBLE);
        }else {
            mImg.setVisibility(INVISIBLE);
        }
    }
}
