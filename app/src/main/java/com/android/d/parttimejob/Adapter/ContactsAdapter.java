package com.android.d.parttimejob.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.d.parttimejob.Entry.Communication.Contact;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Util;

import java.util.ArrayList;

/**
 * 联系人
 * Created by Administrator on 2016/8/29.
 */
public class ContactsAdapter extends BaseAdapter {

    private ArrayList<Contact> mPersonList;

    private LayoutInflater mInflater;

    private Context mContext;

    public ContactsAdapter(ArrayList<Contact> mPersonList, Context context) {
        this.mPersonList = mPersonList;

        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPersonList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.contact_item, null);

            holder = new ViewHolder();
            holder.headImg = (ImageView) convertView.findViewById(R.id.id_contacts_person_head_img);
            holder.name = (TextView) convertView.findViewById(R.id.id_contacts_person_name);
            holder.status = (TextView) convertView.findViewById(R.id.id_contacts_person_status);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(mPersonList.get(position).getName());

        boolean isOnline = mPersonList.get(position).isOnline();
        if (isOnline) {

            holder.status.setText("在线");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.text_color_blue));
        } else {
            holder.status.setText("离线");
            holder.status.setTextColor(Color.GRAY);
        }

        holder.headImg.setImageResource(R.drawable.person);

        String imageName = mPersonList.get(position).getImageName();
        if (imageName != null) {
            Util.loadImageAsync(imageName, holder.headImg, mContext);
        }

        return convertView;
    }


    class ViewHolder {
        private ImageView headImg;
        private TextView name;
        private TextView status;
    }
}
