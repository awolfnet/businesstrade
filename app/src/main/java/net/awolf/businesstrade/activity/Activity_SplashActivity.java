package net.awolf.businesstrade.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.ta.TAActivity;
import com.ta.TAApplication;
import com.ta.util.TALogger;
import com.ta.util.cache.TAExternalOverFroyoUtils;
import com.ta.util.cache.TAFileCache;
import com.ta.util.cache.TAFileCache.TACacheParams;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.FileHttpResponseHandler;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.app.BaseApplication;
import net.awolf.businesstrade.controller.Messages;
import net.awolf.businesstrade.helper.AdCacheDao;
import net.awolf.businesstrade.helper.AdCacheHelper;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.Market;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.datatable.AdModel;
import net.awolf.businesstrade.model.datatable.MessageModel;
import net.awolf.businesstrade.model.response.AdListModel;
import net.awolf.businesstrade.model.response.CurrentVersionModel;
import net.awolf.businesstrade.service.PullMessage;
import net.awolf.businesstrade.service.VMDaemonService;
import net.awolf.businesstrade.util.Const;
import net.awolf.businesstrade.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_SplashActivity extends TAActivity {

    private final String TAG = "SplashActivity";


    private final Handler mRunHandler = new Handler();

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private final int MSG_CHECKUPDATEDONE = 1;
    private final int MSG_ADCACHEDONE = 2;

    private final int MSG_SPLASHDONE = 10;


    private AsyncHttpClient asyncHttpClient;
    private FileHttpResponseHandler fileHttpResponseHandler;
    private String downloadFilename;

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);

        //注册Activity
        getTAApplication().registerActivity(R.string.BuyHistoryActivity, Activity_BuyHistoryActivity.class);
        getTAApplication().registerActivity(R.string.CargoActivity, Activity_CargoActivity.class);
        getTAApplication().registerActivity(R.string.ChatActivity, Activity_ChatActivity.class);
        getTAApplication().registerActivity(R.string.CityActivity, Activity_CityActivity.class);
        getTAApplication().registerActivity(R.string.FavoriteActivity, Activity_FavoriteActivity.class);
        getTAApplication().registerActivity(R.string.LoginActivity, Activity_LoginActivity.class);
        getTAApplication().registerActivity(R.string.MainActivity, Activity_MainActivity.class);
        getTAApplication().registerActivity(R.string.MarketActivity, Activity_MarketActivity.class);
        getTAApplication().registerActivity(R.string.MessageActivity, Activity_MessageActivity.class);
        getTAApplication().registerActivity(R.string.MineActivity, Activity_MineActivity.class);
        getTAApplication().registerActivity(R.string.NewCargoOrderActivity, Activity_NewCargoOrderActivity.class);
        getTAApplication().registerActivity(R.string.NewOrderActivity, Activity_NewOrderActivity.class);
        getTAApplication().registerActivity(R.string.RegisterActivity, Activity_RegisterActivity.class);
        getTAApplication().registerActivity(R.string.SellHistoryActivity, Activity_SellHistoryActivity.class);
        getTAApplication().registerActivity(R.string.SplashActivity, Activity_SplashActivity.class);
        getTAApplication().registerActivity(R.string.ShareActivity, Activity_ShareActivity.class);
        getTAApplication().registerActivity(R.string.PublishActivity, Activity_PublishActivity.class);
    }

    @Override
    protected void onAfterSetContentView() {
        super.onAfterSetContentView();

        TAApplication application = (TAApplication) BaseApplication.getApplication();
        // 缓存KEY
        TACacheParams cacheParams = new TACacheParams(this, Const.SYSTEMCACHE);
        TAFileCache fileCache = new TAFileCache(cacheParams);
        application.setFileCache(fileCache);

        Method.getInstance();
        UserInfo.getInstance().load();
        AdCacheHelper.getInstance();
        Market.getInstance();

        checkUpdate();
    }

    private void checkUpdate() {
        Method.getInstance().getCurrentVersion(new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                CurrentVersionModel currentVersion = (CurrentVersionModel) o;

                int localVersionCode = Utils.getVersionCode();

                if (currentVersion.getVersionCode() > localVersionCode) {
                    //网上有新版本
                    if (currentVersion.isForceUpdate()) {
                        //强制升级
                        String downloadURL = currentVersion.getDownloadURL();
                        initDownload();
                        startDownload(downloadURL);
                    } else {
                        showUploadDialog(currentVersion.getVersionName(), currentVersion.getDownloadURL());
                    }
                } else {
                    checkDone();
                }

            }

            @Override
            public void onServerFailure(String response) {
                TALogger.d(TAG, "Server failure:" + response);
                Utils.makeToast(response);
            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                TALogger.d(TAG, "Server unfulfil:" + unfulfilMessage);
                Utils.makeToast(unfulfilMessage);
            }

            @Override
            public void onException(String errorMessage) {
                TALogger.d(TAG, "Exception:" + errorMessage);
                Utils.MessageBox(
                        Activity_SplashActivity.this,
                        "连接到服务器",
                        "连接服务器失败，请检查网络",
                        "确定",
                        null,
                        new Utils.IMessageBoxCallback() {
                            @Override
                            public void OnPositive(DialogInterface dialog, int which) {
                                exitApp();
                            }

                            @Override
                            public void OnNegative(DialogInterface dialog, int which) {

                            }
                        }
                );
                Utils.makeToast("连接服务器失败，请检查网络");
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void showUploadDialog(String versionName, final String downloadURL) {

        Utils.MessageBox(
                Activity_SplashActivity.this,
                "发现新版本",
                "是否升级到新版本：" + versionName,
                "是",
                "否",
                new Utils.IMessageBoxCallback() {
                    @Override
                    public void OnPositive(DialogInterface dialog, int which) {
                        initDownload();
                        startDownload(downloadURL);
                    }

                    @Override
                    public void OnNegative(DialogInterface dialog, int which) {
                        checkDone();
                    }
                });
    }

    private void checkDone() {
        Utils.sendMessage(mHandler, MSG_CHECKUPDATEDONE);
    }

    private void getAdList() {
        Method.getInstance().getAdList(0, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                AdListModel adList = (AdListModel) o;
                doAdCache(adList);
                List<Map> list = new ArrayList<Map>();
                for (AdModel ad : adList) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("source", ad.getImgsrc());
                    map.put("link", ad.getImglink());
                    list.add(map);
                }
                doAdCache(list);
            }

            @Override
            public void onServerFailure(String response) {
                TALogger.d(TAG, "Server failure:" + response);
                Utils.makeToast(response);
            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                TALogger.d(TAG, "Server unfulfil:" + unfulfilMessage);
                Utils.makeToast(unfulfilMessage);
            }

            @Override
            public void onException(String errorMessage) {
                TALogger.d(TAG, "Exception:" + errorMessage);
                Utils.makeToast("连接服务器失败，请检查网络");
            }

            @Override
            public void onFinish() {
                Utils.sendMessage(mHandler, MSG_ADCACHEDONE);
            }
        });
    }

    private void splashDone() {
        //启动消息服务
        Messages.GetInstance().startPullingService(this);

        Intent daemonService = new Intent(this, VMDaemonService.class);

        this.startService(daemonService);

        String userToken = UserInfo.getInstance().getUserToken();
        if (userToken.length() != 0) {
            doActivity(R.string.MainActivity);
        } else {
            doActivity(R.string.LoginActivity);
        }
    }

    private void doAdCache(AdListModel adList) {
        AdCacheDao adCacheDao = new AdCacheDao();
        for (AdModel ad : adList) {
            adCacheDao.insert(ad);
        }
    }

    private void doAdCache(List<Map> adList) {
        AdCacheHelper.getInstance().setAdList(adList);
    }

    private void initDownload() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(true);
        dialog.setMax(100);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                stopDownload();
            }
        });
        dialog.show();

        downloadFilename = Utils.getDiskCacheDir(Activity_SplashActivity.this, Const.UPDATE_APKFILE).getAbsolutePath();


        asyncHttpClient = new AsyncHttpClient();

        fileHttpResponseHandler = new FileHttpResponseHandler(downloadFilename) {
            @Override
            public void onSuccess(byte[] binaryData) {
                super.onSuccess(binaryData);
                installApk(new File(downloadFilename));
                //Utils.makeToast(downloadFilename);

            }

            @Override
            public void onProgress(long totalSize, long currentSize, long speed) {
                super.onProgress(totalSize, currentSize, speed);
                long downloadPercent = currentSize * 100 / totalSize;
                dialog.setProgress((int) downloadPercent);
            }

            @Override
            public void onFailure(Throwable error) {
                Utils.alert(Activity_SplashActivity.this, "更新", "下载失败", new Utils.IMessageBoxCallback() {
                    @Override
                    public void OnPositive(DialogInterface dialog, int which) {
                        exitApp();
                    }

                    @Override
                    public void OnNegative(DialogInterface dialog, int which) {

                    }
                });
                super.onFailure(error);
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                super.onFinish();
            }
        };
    }

    private void startDownload(String downloadUrl) {
        fileHttpResponseHandler.setInterrupt(false);
        asyncHttpClient.download(downloadUrl, fileHttpResponseHandler);
    }

    private void stopDownload() {
        fileHttpResponseHandler.setInterrupt(true);
    }
//
//    /**
//     * 访问网络下载apk
//     */
//    private class DownloadApk implements Runnable {
//        private ProgressDialog dialog;
//        InputStream is;
//        FileOutputStream fos;
//
//        public DownloadApk(ProgressDialog dialog) {
//            this.dialog = dialog;
//        }
//
//        @Override
//        public void run() {
//            OkHttpClient client = new OkHttpClient();
//            String url = versionDataBean.VERSION_URL;
//            DownloadManager.Request request = new DownloadManager.Request.Builder().get().url(url).build();
//            try {
//                Response response = client.newCall(request).execute();
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "开始下载apk");
//                    //获取内容总长度
//                    long contentLength = response.body().contentLength();
//                    //设置最大值
//                    dialog.setMax((int) contentLength);
//                    //保存到sd卡
//                    File apkFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".apk");
//                    fos = new FileOutputStream(apkFile);
//                    //获得输入流
//                    is = response.body().byteStream();
//                    //定义缓冲区大小
//                    byte[] bys = new byte[1024];
//                    int progress = 0;
//                    int len = -1;
//                    while ((len = is.read(bys)) != -1) {
//                        try {
//                            Thread.sleep(1);
//                            fos.write(bys, 0, len);
//                            fos.flush();
//                            progress += len;
//                            //设置进度
//                            dialog.setProgress(progress);
//                        } catch (InterruptedException e) {
//                            Message msg = Message.obtain();
//                            msg.what = SHOW_ERROR;
//                            msg.obj = "ERROR:10002";
//                            handler.sendMessage(msg);
//                            load2Login();
//                        }
//                    }
//                    //下载完成,提示用户安装
//                    installApk(apkFile);
//                }
//            } catch (IOException e) {
//                Message msg = Message.obtain();
//                msg.what = SHOW_ERROR;
//                msg.obj = "ERROR:10003";
//                handler.sendMessage(msg);
//                load2Login();
//            } finally {
//                //关闭io流
//                if (is != null) {
//                    try {
//                        is.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    is = null;
//                }
//                if (fos != null) {
//                    try {
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    fos = null;
//                }
//            }
//            dialog.dismiss();
//        }
//    }

    /**
     * 下载完成,提示用户安装
     */
    private void installApk(File file) {
        //调用系统安装程序
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivityForResult(intent, REQUEST_INSTALL_CODE);

        //安装应用
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        BaseApplication.InstallApk(file);

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(BaseApplication.getTAApplication(), "net.awolf.businesstrade.fileprovider", file);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
//            startActivity(intent);
//        }
    }

    private final Runnable mSplashDone = new Runnable() {
        @Override
        public void run() {
            Utils.sendMessage(mHandler, MSG_SPLASHDONE);
        }
    };

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CHECKUPDATEDONE: {
                    getAdList();
                    break;
                }
                case MSG_ADCACHEDONE: {
                    Utils.sendMessage(mHandler, MSG_SPLASHDONE);
                    break;
                }
                case MSG_SPLASHDONE:
                    splashDone();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
