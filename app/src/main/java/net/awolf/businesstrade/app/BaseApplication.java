package net.awolf.businesstrade.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.ta.TAApplication;

import java.io.File;

/**
 * Created by Work on 2017/8/7.
 */

public class BaseApplication extends TAApplication {

    private static Context context;
    private static TAApplication taApplication;
    private static TAApplication instance;

    @Override
    public void exitApp(Boolean isBackground) {
        super.exitApp(isBackground);
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context=getBaseContext().getApplicationContext();
        taApplication=getApplication();
        instance=this;
    }

    public static void InstallApk(File file)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(taApplication, "net.awolf.businesstrade.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        }
        instance.startActivity(intent);
//        if (instance.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
//            instance.startActivity(intent);
//        }
    }

    public static Context getContext(){
        return context;
    }

    public static TAApplication getTAApplication()
    {
        return taApplication;
    }

    public static TAApplication getInstance() {
        return instance;
    }
}
