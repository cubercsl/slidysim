package cn.cubercsl.slidysim;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import cn.cubercsl.slidysim.results.gen.DaoMaster;
import cn.cubercsl.slidysim.results.gen.DaoSession;


public class MyApplication extends Application {

    private static MyApplication application;
    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        setDataBase();
    }

    private void setDataBase() {
        helper = new DaoMaster.DevOpenHelper(this, "results", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        // for debug
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
