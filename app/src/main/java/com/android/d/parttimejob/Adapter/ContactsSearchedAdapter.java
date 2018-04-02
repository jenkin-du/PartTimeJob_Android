package com.android.d.parttimejob.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.d.parttimejob.Entry.Communication.SearchedPerson;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.Util;

import java.util.ArrayList;

/**联系人
 * Created by Administrator on 2016/8/29.
 */
public class ContactsSearchedAdapter extends BaseAdapter {

    private ArrayList<SearchedPerson> mPersonList;

    private LayoutInflater mInflater;
    private Context mContext;

    public ContactsSearchedAdapter(ArrayList<SearchedPerson> personList, Context context) {
        this.mPersonList=personList;

        this.mContext=context;
        mInflater =LayoutInflater.from(context);
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

        if (convertView==null){

            convertView=mInflater.inflate(R.layout.contact_searched_person,null);

            holder=new ViewHolder();
            holder.headImg= (ImageView) convertView.findViewById(R.id.id_contacts_searched_person_head_img);
            holder.name= (TextView) convertView.findViewById(R.id.id_contacts_searched_person_name);
            holder.school= (TextView) convertView.findViewById(R.id.id_contacts_searched_person_school);

            convertView.setTag(holder);

        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        Log.i("ContactsSearchedAdapter", mPersonList.get(position).getName());
        holder.name.setText(mPersonList.get(position).getName());
        holder.school.setText(mPersonList.get(position).getSchool());


        holder.headImg.setImageResource(R.drawable.person);
        holder.headImg.setTag(mPersonList.get(position).getImageName());
        Util.loadImageAsync(mPersonList.get(position).getImageName(),holder.headImg,mContext);

        return convertView;
    }


    class ViewHolder{
        private ImageView headImg;
        private TextView name;
        private TextView school;
    }
}
