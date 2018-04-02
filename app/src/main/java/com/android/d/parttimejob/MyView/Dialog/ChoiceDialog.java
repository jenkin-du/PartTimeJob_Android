package com.android.d.parttimejob.MyView.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.d.parttimejob.R;

/**
 * 选择对话框
 * Created by DJS on 2017/2/11.
 */
public class ChoiceDialog extends Dialog implements View.OnClickListener {


    private Context mContext;

    private TextView mTakePhotoTV;
    private TextView mChoosePictureTV;

    private ChoiceListener mListener;

    public ChoiceDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setOnChoiceListener(ChoiceListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化控件
        init();
        //设置监听器
        setOnListener();
    }

    /**
     * 初始化控件
     */
    private void init() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.choice, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(view);

        mTakePhotoTV = (TextView) view.findViewById(R.id.id_choice_take_photo);
        mChoosePictureTV = (TextView) view.findViewById(R.id.id_choice_choose_picture);

    }

    /**
     * 设置监听器
     */
    private void setOnListener() {

        mTakePhotoTV.setOnClickListener(this);
        mChoosePictureTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //选择第一个
            case R.id.id_choice_take_photo:
                mListener.onFirstSelected();
                break;
            //选择第二个
            case R.id.id_choice_choose_picture:
                mListener.onSecondSelected();
                break;
        }

    }

    public interface ChoiceListener {

        void onFirstSelected();
        void onSecondSelected();
    }
}

