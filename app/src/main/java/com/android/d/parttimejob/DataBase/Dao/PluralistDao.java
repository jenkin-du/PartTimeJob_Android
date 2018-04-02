package com.android.d.parttimejob.DataBase.Dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.d.parttimejob.DataBase.DBHelper.DBHelper;
import com.android.d.parttimejob.DataBase.DaoI.PluralistDaoI;
import com.android.d.parttimejob.Entry.Pluralist;


public class PluralistDao implements PluralistDaoI {

    private Context context;

    public PluralistDao(Context context) {
        this.context = context;
    }

    @Override
    public boolean insert(Pluralist pluralist) {
        boolean isOK = false;

        DBHelper dbHelper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.version);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (pluralist.getId() != null) {

            values.put("pluralist_id", pluralist.getId());
            values.put("phone", pluralist.getPhone());
            values.put("password", pluralist.getPassword());
            values.put("name", pluralist.getName());
            values.put("salary", pluralist.getSalary());
            values.put("gender", pluralist.getGender());
            values.put("age", pluralist.getAge());
            values.put("height", pluralist.getHeight());
            values.put("education_background", pluralist.getEducationBackground());
            values.put("head_image_name", pluralist.getImageName());
            values.put("email", pluralist.getEmail());
            values.put("school", pluralist.getSchool());
            values.put("feature", pluralist.getFeature());
            values.put("experience", pluralist.getExperience());

            Log.i("Dao", "insert: " + values.toString());
            long count = db.insert(DBHelper.T_PLURALIST, null, values);
            if (count == 1) {
                isOK = true;
            }
        }
        return isOK;
    }

    @Override
    public boolean update(Pluralist pluralist) {
        boolean isOK = false;


        DBHelper dbHelper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (pluralist.getId() != null) {


            values.put("phone", pluralist.getPhone());
            values.put("password", pluralist.getPassword());
            values.put("name", pluralist.getName());
            values.put("salary", pluralist.getSalary());
            values.put("gender", pluralist.getGender());
            values.put("age", pluralist.getAge());
            values.put("height", pluralist.getHeight());
            values.put("education_background", pluralist.getEducationBackground());
            values.put("head_image_name", pluralist.getImageName());
            values.put("email", pluralist.getEmail());
            values.put("school", pluralist.getSchool());
            values.put("feature", pluralist.getFeature());
            values.put("experience", pluralist.getExperience());


            db.update(DBHelper.T_PLURALIST, values, "pluralist_id=?", new String[]{pluralist.getId()});
            long count = db.insert(DBHelper.T_PLURALIST, null, values);
            if (count == 1) {
                isOK = true;
            }
        }
        return isOK;
    }

    /**
     * 修改电话
     */
    public void updatePhone(String id, String phone) {

        DBHelper dbHelper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("phone", phone);
        db.update(DBHelper.T_PLURALIST, values, "pluralist_id=?", new String[]{id});
    }

    @Override
    public Pluralist query(String pId) {


        Pluralist p = new Pluralist();

        DBHelper dbHelper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.version);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        String where = "pluralist_id=?";
        String[] args = new String[]{pId};
        cursor = db.query(DBHelper.T_PLURALIST, null, where, args, null, null, null, null);

        while (cursor.moveToNext()) {
            p.setId(pId);
            p.setName(cursor.getString(cursor.getColumnIndex("name")));
            p.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            p.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
            p.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            p.setFeature(cursor.getString(cursor.getColumnIndex("feature")));
            p.setSchool(cursor.getString(cursor.getColumnIndex("school")));
            p.setEducationBackground(cursor.getString(cursor.getColumnIndex("education_background")));
            p.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            p.setExperience(cursor.getString(cursor.getColumnIndex("experience")));
            p.setImageName(cursor.getString(cursor.getColumnIndex("head_image_name")));
            p.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            p.setSalary(cursor.getFloat(cursor.getColumnIndex("salary")));
        }

        cursor.close();

        return p;
    }

    @Override
    public void delete() {

        DBHelper dbHelper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.version);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //删除以前的数据
        String sql = "delete from t_pluralist ";
        db.execSQL(sql);
    }

    /**
     * 更新图片
     */
    public boolean updateImage(String id, String imageName) {
        boolean isOK = false;


        DBHelper dbHelper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("head_image_name", imageName);

        db.update(DBHelper.T_PLURALIST, values, "pluralist_id=?", new String[]{id});
        long count = db.insert(DBHelper.T_PLURALIST, null, values);
        if (count == 1) {
            isOK = true;
        }
        return isOK;
    }

}
