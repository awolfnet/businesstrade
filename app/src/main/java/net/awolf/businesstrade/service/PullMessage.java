package net.awolf.businesstrade.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ta.util.TALogger;

import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.Messages;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.response.MessageListModel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Work on 2017/8/11.
 */

public class PullMessage extends Service {
    private final String TAG="PullMessage";

    private final int MSG_PULLMESSAGE=1;
    private final int MSG_KEEPALIVE=2;
    private final int MSG_EXIT=100;

    private volatile boolean isPulling=false;

    private final Timer timer = new Timer();
    private TimerTask task;

    private String mUserToken= UserInfo.getInstance().getUserToken();
    private String mUsername=UserInfo.getInstance().getUserName();

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private int startID;

    public Handler getServiceHandler()
    {
        return mServiceHandler;
    }

    private Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TALogger.d(TAG,"HandleMessage:"+msg.arg1);

            //Pulling from here

            mUserToken= UserInfo.getInstance().getUserToken();
            mUsername=UserInfo.getInstance().getUserName();

            switch (msg.arg1)
            {
                case MSG_PULLMESSAGE:
                    if(isPulling!=true)
                    {
                        PullMessage();
                    }
                    break;
                case MSG_KEEPALIVE:
                    break;
                case MSG_EXIT:
                    stopSelf(startID);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            TALogger.d(TAG,"HandleMessage:"+msg.arg1);

            mUserToken= UserInfo.getInstance().getUserToken();
            mUsername=UserInfo.getInstance().getUserName();

            switch (msg.arg1)
            {
                case MSG_PULLMESSAGE:
                    PullMessage();
                    break;
                case MSG_KEEPALIVE:
                    break;
                case MSG_EXIT:
                    stopSelf(startID);
                    break;
            }


//            try {
//                TALogger.d(TAG,"start");
//                int i=0;
//                do
//                {
//                    TALogger.d(TAG,"I:"+i+".");
//                    //PullMessage();
//                    Thread.sleep(1000);
//                }while(i++<60);
//                TALogger.d(TAG,"done");
//            } catch (InterruptedException e) {
//                // Restore interrupt status.
//                Thread.currentThread().interrupt();
//            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
//        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//
//        mServiceLooper = thread.getLooper();
//        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TALogger.d(TAG,"Service start.");
        this.startID=startId;

//        mServiceHandler.post(loop);

//        Message msg = mServiceHandler.obtainMessage();
//        msg.arg1 = startId;
//        mServiceHandler.sendMessage(msg);


        task = new TimerTask() {
            @Override
            public void run() {

//                \
                Message message = timerHandler.obtainMessage();
                message.arg1 = MSG_PULLMESSAGE;
                timerHandler.sendMessage(message);
            }
        };

        timer.schedule(task, 5000, 5000);

        // If we get killed, after returning from here, restart
        return START_STICKY;

        //return super.onStartCommand(intent, flags, startId);
    }

    public void stopPullingMessage()
    {
        Message message = timerHandler.obtainMessage();
        message.arg1 = MSG_EXIT;
        timerHandler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onLowMemory() {

    }

    private Runnable loop=new Runnable() {
        @Override
        public void run() {
            TALogger.d(TAG,"start");
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = MSG_PULLMESSAGE;
            mServiceHandler.sendMessage(msg);
            TALogger.d(TAG,"done");
        }
    };

    private void PullMessage()
    {
        if(!mUserToken.isEmpty())
        {
            isPulling=true;
            Method.getInstance().pullMessage(mUserToken, Messages.GetInstance().getReachedMessageList(), new IMethodCallback() {
                @Override
                public void onSuccesse(Object o) {
                    Messages.GetInstance().appendReachedMessage((MessageListModel)o);
                }

                @Override
                public void onServerFailure(String response) {

                }

                @Override
                public void onUnfulfil(String unfulfilMessage) {

                }

                @Override
                public void onException(String errorMessage) {

                }

                @Override
                public void onFinish() {
                    isPulling=false;
                }
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
