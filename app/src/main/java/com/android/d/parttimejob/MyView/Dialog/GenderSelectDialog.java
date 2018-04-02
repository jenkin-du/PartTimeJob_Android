package com.android.d.parttimejob.MyView.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.android.d.parttimejob.R;

/**
 * 性别选择器
 * Created by DJS on 2017/1/19.
 */
public class GenderSelectDialog extends Dialog {

    private Context mContext;
    private String mCurrentGender="";
    private RadioButton mMaleBtn;
    private RadioButton mFemaleBtn;
    private GenderSelectedListener mListener;

    public GenderSelectDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public GenderSelectDialog(Context context, String currentGender) {
        super(context);
        this.mContext = context;
        this.mCurrentGender = currentGender;
    }

    public void setOnSelectListener(GenderSelectedListener listener) {
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
     * 设置监听器
     */
    private void setOnListener() {

        mMaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaleBtn.setChecked(true);
                mFemaleBtn.setChecked(false);
                mListener.onMaleSelected();
            }
        });

        mFemaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaleBtn.setChecked(false);
                mFemaleBtn.setChecked(true);
                mListener.onFemaleSelected();
            }
        });
    }

    /**
     * 初始化
     */

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.gender_select, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(view);

        mMaleBtn = (RadioButton) view.findViewById(R.id.id_gender_male);
        mFemaleBtn = (RadioButton) view.findViewById(R.id.id_gender_female);

        if (mCurrentGender.equals("男")){
            mMaleBtn.setChecked(true);
        }else if(mCurrentGender.equals("女")){
            mFemaleBtn.setChecked(true);
        }
    }

    public interface GenderSelectedListener {

        void onMaleSelected();

        void onFemaleSelected();
    }
}
