package com.android.d.parttimejob.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.d.parttimejob.R;

/**
 * 自定义输入框
 * Created by DJS on 2017/1/22.
 */
public class InputBox extends LinearLayout {


    private TextView mTitleTV;
    private EditText mContentET;


    public InputBox(Context context) {
        this(context, null);
    }

    public InputBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.input_box, this, true);
        //初始化
        initView(view);
        //获得自定义属性
        getAttrs(context, attrs);
    }

    /**
     * 获得自定义属性
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void getAttrs(Context context, AttributeSet attrs) {

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.InputBox);

        String title = arr.getString(R.styleable.InputBox_input_title);
        String content = arr.getString(R.styleable.InputBox_input_content);
        String hint = arr.getString(R.styleable.InputBox_input_hint);
        int type=arr.getInt(R.styleable.InputBox_input_type,0);

        arr.recycle();

        mTitleTV.setText(title);
        mContentET.setText(content);
        mContentET.setHint(hint);



        switch (type){
            case 0:
                mContentET.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 1:
                mContentET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case 2:
                mContentET.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            default:
                break;
        }

    }

    /**
     * 初始化视图
     */
    private void initView(View view) {

        mTitleTV = (TextView) view.findViewById(R.id.id_input_box_title);
        mContentET = (EditText) view.findViewById(R.id.id_input_box_content);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title){
        mTitleTV.setText(title);
    }

    /**
     * 设置内容
     */
    public void setContent(String content){
        mContentET.setText(content);
    }

    /**
     * 得到内容
     */
    public String getContent(){
        return mContentET.getText().toString();
    }

    /**
     * 设置提示
     */
    public void setHint(String hint){
        mContentET.setHint(hint);
    }
}
