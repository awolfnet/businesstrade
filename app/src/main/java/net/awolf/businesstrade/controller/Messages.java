package net.awolf.businesstrade.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ta.util.TALogger;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.activity.Activity_MainActivity;
import net.awolf.businesstrade.activity.Activity_MessageActivity;
import net.awolf.businesstrade.activity.Activity_SplashActivity;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.model.datatable.MessageModel;
import net.awolf.businesstrade.model.request.ReachedMessageList;
import net.awolf.businesstrade.model.response.MessageListModel;
import net.awolf.businesstrade.service.PullMessage;
import net.awolf.businesstrade.util.Utils;

/**
 * Created by zhaohai on 2017/10/20.
 */

public class Messages {
    private final String TAG="Messages";
    public static final int MSG_MESSAGE_ARRIVE=1;

    public static final int MESSAGE_TYPE_CONFIG=0;
    public static final int MESSAGE_TYPE_MAIL=1;
    public static final int MESSAGE_TYPE_ALERT=2;
    public static final int MESSAGE_TYPE_EVENT=3;
    public static final int MESSAGE_TYPE_CHAT=4;

    public static final int MESSAGE_STATUS_UNREACH=0;
    public static final int MESSAGE_STATUS_UNREAD=1;
    public static final int MESSAGE_STATUS_REACH=2;
    public static final int MESSAGE_STATUS_READ=3;

    public static final int MESSAGE_STYLE_CRITICAL = 0;
    public static final int MESSAGE_STYLE_QUESTION = 1;
    public static final int MESSAGE_STYLE_EXCLAMATION = 2;
    public static final int MESSAGE_STYLE_INFORMATION = 3;

    private String mUserToken="";

    private Intent serviceIntent=null;

    private static Messages mInstance=new Messages();
    public static synchronized Messages GetInstance()
    {
        if(mInstance==null)
        {
            mInstance=new Messages();
        }
        return mInstance;
    }


    public volatile MessageListModel messageList=null;

    public MessageListModel getMessageList() {
        return messageList;
    }
    public void setMessageList(MessageListModel messageList) {
        this.messageList = messageList;
    }

    public void readMessage(int messageID) {
        ReachedMessageList messageList=new ReachedMessageList();
        messageList.add(messageID);
        this.readMessage(messageList);
    }

    public void readMessage(ReachedMessageList messageList)
    {
        mUserToken=UserInfo.getInstance().getUserToken();
        Method.getInstance().readMessage(mUserToken,messageList , new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                TALogger.d(TAG,"Message read.");
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

            }
        });
    }
    

    public ReachedMessageList getReachedMessageList()
    {
        ReachedMessageList list=new ReachedMessageList();

        if(messageList!=null)
        {
            for(MessageModel message : messageList)
            {
                list.add(message.mid);
            }
        }

        return list;
    }

    public void appendReachedMessage(MessageListModel reachedMessageList)
    {
        if(this.messageList==null)
            this.messageList=new MessageListModel();

        for(MessageModel message : reachedMessageList)
        {
            if(Contains(message))
            {
               continue;
            }else
            {
                this.messageList.add(message);
                notifyMessageArrived(message);
            }
        }
    }

    public synchronized boolean Contains(MessageModel message)
    {
        return this.messageList.contains(message);
    }

    public MessageModel getLastMessage()
    {
        if(messageList!=null) {
            return messageList.get(messageList.size()-1);
        }else
        {
            MessageModel message=new MessageModel();
            message.setTimestamps(Utils.getDate("yyyy/MM/dd hh:mm:ss"));
            message.setTitle(Utils.getString(R.string.no_message));
            message.setContent("");
            return message;
        }

    }

    public void clearLocalMessage()
    {
        this.messageList.clear();
        this.messageList=null;
    }

    private void notifyMessageArrived(MessageModel message)
    {
        switch (message.getType())
        {
            case MESSAGE_TYPE_MAIL:
                alertMessage(message);
                break;
            case MESSAGE_TYPE_ALERT:
                break;
            case MESSAGE_TYPE_CONFIG:
                break;
            case MESSAGE_TYPE_EVENT:
                break;
            case MESSAGE_TYPE_CHAT:
                Chat.getInstance().sendMessage(Chat.CHAT_ARRIVE,message);
                break;
        }
    }

    public Handler getMessageHandler() {
        return messageHandler;
    }
    public void sendMessage(int msg)
    {
        Message message=this.messageHandler.obtainMessage();
        message.arg1=msg;
        this.messageHandler.sendMessage(message);
    }
    public void sendMessage(int arg1,int arg2,int what)
    {
        Message message=this.messageHandler.obtainMessage();
        message.arg1=arg1;
        message.arg2=arg2;
        message.what=what;

        this.messageHandler.sendMessage(message);
    }
    private Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TALogger.d(TAG,"HandleMessage:"+msg.arg1);
            super.handleMessage(msg);
        }
    };

    public void startPullingService(Activity activity)
    {
        if(serviceIntent==null)
        {
            serviceIntent= new Intent(activity, PullMessage.class);
        }
        activity.startService(serviceIntent);
    }

    public void stopPullingService(Activity activity)
    {
        activity.stopService(serviceIntent);
    }

    private void alertMessage(MessageModel message)
    {
        if(Activity_MainActivity.getInstance().isTopActivity())
        {
            Activity_MainActivity.getInstance().showMessage(message);
        }

        Utils.showNotification(message.getTitle(),message.getContent(),message.getType(), Activity_SplashActivity.class);
    }
}
