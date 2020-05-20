package net.awolf.businesstrade.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.ta.TAApplication;
import com.ta.common.AndroidVersionCheckUtils;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.activity.Activity_MessageActivity;
import net.awolf.businesstrade.activity.Activity_SplashActivity;
import net.awolf.businesstrade.app.BaseApplication;
import net.awolf.businesstrade.method.IMethodCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import static android.content.Context.ACTIVITY_SERVICE;

public class Utils {

    private static final String TAG = "Utils";
    private static Context context = BaseApplication.getApplication().getBaseContext();

    public static ArrayList<String> getFromHashMap(ArrayList<HashMap> list, String key) {
        ArrayList<String> s = new ArrayList<String>();
        int i = 0;
        for (i = 0; i < list.size(); i++) {
            s.add(list.get(i).get(key).toString());
        }
        return s;
    }

    public static String getNow(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        // "[1]"代表第1位为数字1，"[3458]"代表第二位可以为3、4、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /*
     * 获取版本号(显示版本号)
     */
    public static String getVersion() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }
    }

    /*
     * 获取版本代码(内部版本号)
     */
    public static int getVersionCode() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    /*
     *
     */
    public static File getExternalCacheDir() {
        if (AndroidVersionCheckUtils.hasFroyo()) {
            return context.getExternalCacheDir();
        }
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /*
     * 生成Toast提示组件 alertMsg 用于提示的信息
     */
    public static void makeToast(String alertMsg) {
        Toast.makeText(context, alertMsg, Toast.LENGTH_SHORT).show();
    }

    public static void makeToast(Context context, String alertMsg, int duration, int gravity, int xOffset,
                                 int yOffset) {
        Toast toast = Toast.makeText(context, alertMsg, Toast.LENGTH_LONG);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        toast.show();
    }

    public static void sendMessage(Handler h, int msg) {
        Message message = new Message();
        message.what = msg;
        h.sendMessage(message);

    }

    public static synchronized void saveSetting(String key, String value) {
        SharedPreferences share = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static synchronized void saveSetting(String key, int value) {
        SharedPreferences share = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static synchronized void saveSetting(String key, long value) {
        SharedPreferences share = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static synchronized void saveSetting(String key, Boolean value) {
        SharedPreferences share = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static synchronized String readSetting(String key) {
        SharedPreferences share = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        return share.getString(key, "");
    }

    public static synchronized int readSettingInt(String key) {
        SharedPreferences share = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        return share.getInt(key, 0);

    }

    public static synchronized long readSettingLong(String key) {
        SharedPreferences share = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        long r = 0;
        try {
            r = share.getLong(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public static synchronized Boolean readSettingBoolean(String key) {
        SharedPreferences share = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        return share.getBoolean(key, false);

    }

    public static void openURL(Context context, String url) {
        Uri uri = Uri.parse(url);
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception ex) {
            Log.e(TAG, "failed open url:" + url);
            ex.printStackTrace();
        }
    }

    public static void getTopActivity() {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

    }

    public static void getCameraPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public static String getTempImageFile() {
        String fileName = "";
        String path = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Camera" + File.separator;
        fileName = path + getDateString("yyyymmddhhmmss") + ".jpg";
        return fileName;
    }

    public static String getDateString(String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static void showNotification(String title, String content, int notifyId, Class<?> cls) {
        //出处http://blog.csdn.net/plain_maple/article/details/52526988
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Bitmap LargeBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        PendingIntent pi = null;

        //2.通过Notification.Builder来创建通知
        mBuilder.setContentTitle(title)
                .setContentText(content)
                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(LargeBitmap)
                //设置默认声音和震动
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)//点击后取消
                .setWhen(System.currentTimeMillis())//设置通知时间
        ;
        if (cls != null) {
            //3.定义一个PendingIntent，点击Notification后启动一个Activity
            pi = PendingIntent.getActivity(
                    context,
                    100,
                    new Intent(context, cls),
                    PendingIntent.FLAG_CANCEL_CURRENT
            );
            mBuilder.setContentIntent(pi);  //3.关联PendingIntent
        }

        Notification mNotification = mBuilder.getNotification();
        //4.通过通知管理器来发起通知，ID区分通知
        mNotificationManager.notify(notifyId, mNotification);


//
//        mBuilder.setContentTitle(title)//设置通知栏标题
//                .setContentText(content)
//                //.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
//                //.setNumber(number) //设置通知集合的数量
//                .setTicker(ticker) //通知首次出现在通知栏，带上升动画效果的
//                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//                //.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
//                //.setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
//                ;
//
//        if (cls != null) {
//            Intent intent = new Intent(context, cls);
//        }
//
//        mNotificationManager.notify(notifyId, mBuilder.build());

    }

    public static String getDate(String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static String getString(int resourceID) {
        return context.getResources().getString(resourceID);
    }

    public static void alert(Context context, String title, String message, final IMessageBoxCallback callback)
    {
        MessageBox(context,title,message,"确定",null,callback);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void dialerPhoneNumber(String phoneNum,Activity activity) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        //BaseApplication.getContext().startActivity(intent);
        //BaseApplication.getInstance().startActivity(intent);
        //BaseApplication.getApplication().startActivity(intent);
        //startActivity(intent);
        activity.startActivity(intent);
    }


    public static void MessageBox(Context context, String title, String message, String postiveLabel, String negativeLabel, final IMessageBoxCallback callback) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setIcon(R.drawable.ic_launcher);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);

        if (callback != null) {
            if (postiveLabel!=null && !postiveLabel.isEmpty()) {
                normalDialog.setPositiveButton(postiveLabel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.OnPositive(dialog, which);
                            }
                        });
            }

            if ( negativeLabel!=null && !negativeLabel.isEmpty()) {
                normalDialog.setNegativeButton(negativeLabel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.OnNegative(dialog, which);
                            }
                        });
            }
        }

        // 显示
        normalDialog.show();

    }

    public static String base64Encode(String plainText)
    {
        String encodedString = Base64.encodeToString(plainText.getBytes(), Base64.DEFAULT);
        return encodedString;
    }

    public static String base64Decode(String base64Data)
    {
        String decodedString =new String(Base64.decode(base64Data,Base64.DEFAULT));
        return decodedString;
    }

    public static File getExternalCacheDir(Context context) {
        if (AndroidVersionCheckUtils.hasFroyo()) {
            return context.getExternalCacheDir();
        } else {
            String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
            return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        }
    }
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = !"mounted".equals(Environment.getExternalStorageState()) && isExternalStorageRemovable() ? context.getCacheDir().getPath() : getExternalCacheDir(context).getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public static boolean isExternalStorageRemovable() {
        return AndroidVersionCheckUtils.hasGingerbread() ? Environment.isExternalStorageRemovable() : true;
    }

    public interface IMessageBoxCallback {
        public void OnPositive(DialogInterface dialog, int which);

        public void OnNegative(DialogInterface dialog, int which);
    }
}
