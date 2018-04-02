package com.android.d.parttimejob.MyView.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.d.parttimejob.R;

/**
 * 输入对话框
 * Created by DJS on 2017/1/20.
 */
public class InputDialog extends Dialog {

    private Context mContext;
    private TextView mTitleTV;
    private EditText mInputEdit;

    private Button mCancelBtn;
    private Button mOKBtn;

    private String mTitle;
    private String mInput;

    private InputClickListener mListener;

    public InputDialog(Context context, String title, String input) {
        super(context);

        this.mContext = context;
        this.mTitle = title;
        this.mInput = input;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.input_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);

        mTitleTV = (TextView) view.findViewById(R.id.id_input_title);
        mInputEdit = (EditText) view.findViewById(R.id.id_input_edit);

        mCancelBtn = (Button) view.findViewById(R.id.id_input_cancel);
        mOKBtn = (Button) view.findViewById(R.id.id_input_ok);


        mTitleTV.setText(mTitle);
        mInputEdit.setText(mInput);


        mOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOkClick(mInputEdit.getText().toString());
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClick();
            }
        });
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        mTitleTV.setText(title);
    }

    /**
     * 设置内容
     */
    public void setContent(String content) {
        mInputEdit.setText(content);
    }

    /**
     * 获得内容
     */
    public String getContent() {
        return mInputEdit.getText().toString();
    }


    public void setOnInputListener(InputClickListener listener){
        mListener=listener;
    }

    public interface InputClickListener {
        void onCancelClick();//点击取消
        void onOkClick(String content);//点击确定
    }
}
