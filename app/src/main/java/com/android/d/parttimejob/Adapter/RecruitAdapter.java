package com.android.d.parttimejob.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.d.parttimejob.Entry.recruit.InfoAbstract;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Util;

import java.util.ArrayList;

/**
 * 自定义数据适配器
 * Created by D on 2016/5/31.
 */
public class RecruitAdapter extends BaseAdapter {

    private ArrayList<InfoAbstract> mList;
    private LayoutInflater mInflater;

    public RecruitAdapter(Context context, ArrayList<InfoAbstract> list) {

        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //文艺式加载布局
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.recruit_abstract, null);

            holder.distance = (TextView) convertView.findViewById(R.id.id_distance);
            holder.category = (TextView) convertView.findViewById(R.id.id_category);
            holder.description = (TextView) convertView.findViewById(R.id.id_description);
            holder.Place = (TextView) convertView.findViewById(R.id.id_place);
            holder.salary = (TextView) convertView.findViewById(R.id.id_salary);
            holder.startWorkTime = (TextView) convertView.findViewById(R.id.id_workTime);
            holder.updateTime = (TextView) convertView.findViewById(R.id.id_update_time);
            holder.cateImg = (ImageView) convertView.findViewById(R.id.id_abstract_image);

            convertView.setTag(holder);//将实例化的holder保存起来

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.category.setText(mList.get(position).getCategory());//设置类别
        holder.description.setText(mList.get(position).getDescription());//设置标题
        String place = mList.get(position).getAddress().getDistrict();
        holder.Place.setText(place);//设置地点
        String time = mList.get(position).getStartWorkTime() + "(" + mList.get(position).getWorkDays() + "天)";
        holder.startWorkTime.setText(time);//设置时间
        holder.salary.setText(mList.get(position).getSalary());//设置薪水
        holder.updateTime.setText(mList.get(position).getUpdateTime());//设置跟新时间

        holder.distance.setText(mList.get(position).getDistance()+" m");

        //根据种类设置背景
        GradientDrawable drawable = (GradientDrawable) holder.cateImg.getBackground();
        drawable.setColor(Util.getColorByCategory(mList.get(position).getCategory()));

        return convertView;
    }


    class ViewHolder {

        private TextView description;       //招聘简介
        private TextView startWorkTime;     //工作时间
        private TextView Place;             //工作地点
        private TextView salary;            //薪资
        private TextView updateTime;        //更新时间
        private TextView distance;          //距离
        public TextView category;           //类别

        public ImageView cateImg;//种类背景
    }
}
