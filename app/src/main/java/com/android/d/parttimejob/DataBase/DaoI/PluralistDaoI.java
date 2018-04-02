package com.android.d.parttimejob.DataBase.DaoI;

import com.android.d.parttimejob.Entry.Pluralist;



/**自定义接口
 * Created by Administrator on 2016/8/13.
 */
public interface PluralistDaoI {

    boolean insert(Pluralist pluralist);//插入

    boolean update(Pluralist pluralist);

    Pluralist query(String pId);//查询

    void delete();
}
