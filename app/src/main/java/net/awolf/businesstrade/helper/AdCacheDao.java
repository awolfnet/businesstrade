package net.awolf.businesstrade.helper;

import com.ta.TAApplication;
import com.ta.util.db.TASQLiteDatabase;

import net.awolf.businesstrade.app.BaseApplication;
import net.awolf.businesstrade.model.datatable.AdModel;

import java.util.ArrayList;
import java.util.List;

public class AdCacheDao {
    TAApplication taApplication;
    public AdCacheDao()
    {
        taApplication= BaseApplication.getApplication();
        // 得到数据库连接池
        TASQLiteDatabase tasqLiteDatabase = taApplication.getSQLiteDatabasePool().getSQLiteDatabase();
        // AdModel.class得到一个对象，AdModel，类信息里面有类名，字段，方法
        // 判断库有没有表
        if (!tasqLiteDatabase.hasTable(AdModel.class)) {
            tasqLiteDatabase.creatTable(AdModel.class);
        }
        // 从池中取到对象中，用完放回去
        taApplication.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);

    }

    // 添加
    // 需要传入的对象
    public void insert(AdModel adModel) {
        TASQLiteDatabase tasqLiteDatabase = taApplication.getSQLiteDatabasePool().getSQLiteDatabase();
        tasqLiteDatabase.insert(adModel);
        taApplication.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);

    }
    // 查询
    //返回用户的集合
    public ArrayList<AdModel> query(int pageLevel)
    {
        TASQLiteDatabase tasqLiteDatabase = taApplication.getSQLiteDatabasePool().getSQLiteDatabase();

        List<AdModel> list=tasqLiteDatabase.query(AdModel.class, true, "pagelevel="+pageLevel, null, null, null, null);
        taApplication.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        return new ArrayList<>(list);
    }

}
