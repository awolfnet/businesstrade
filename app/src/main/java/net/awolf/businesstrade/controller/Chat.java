package net.awolf.businesstrade.controller;

import android.os.Handler;
import android.os.Message;

import com.ta.util.TALogger;

import net.awolf.businesstrade.activity.Activity_MainActivity;
import net.awolf.businesstrade.activity.Activity_MessageActivity;
import net.awolf.businesstrade.activity.Activity_SplashActivity;
import net.awolf.businesstrade.adapter.ChatListAdapter;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.model.datatable.MessageModel;
import net.awolf.businesstrade.model.local.ChatItem;
import net.awolf.businesstrade.model.response.MessageListModel;
import net.awolf.businesstrade.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaohai on 2017/9/10.
 */

public class Chat {
    private final String TAG="Chat";
    public static final int CHAT_ARRIVE=1;

    private static Chat mInstance=new Chat();
    public static synchronized Chat getInstance()
    {
        if(mInstance==null)
        {
            mInstance=new Chat();
        }

        return mInstance;
    }

    private int userID=UserInfo.getInstance().getUserID();
    private String mUserToken=UserInfo.getInstance().getUserToken();

    private String talkerName="";
//    private String talkerDescription="";
    private int talkerUserID=0;
    /*  0:Merchant,1:Cargo*/
    private int talkerType=0;

    public static class TalkerType
    {
        public static final int MERCHANT=0;
        public static final int CARGO=1;
    }

    private List<ChatItem> chatList=null;

    private ChatListAdapter chatListAdapter=null;

    public String getTalkerName() {
        return talkerName;
    }
    public void setTalkerName(String talkerName) {
        this.talkerName = talkerName;
    }

//    public String getTalkerDescription() {
//        return talkerDescription;
//    }
//    public void setTalkerDescription(String talkerDescription) {
//        this.talkerDescription = talkerDescription;
//    }

    public int getTalkerUserID() {
        return talkerUserID;
    }
    public void setTalkerUserID(int talkerUserID) {
        this.talkerUserID = talkerUserID;
    }

    /*  0:Merchant,1:Cargo*/
    public int getTalkerType() {
        return talkerType;
    }
    public void setTalkerType(int talkerType) {
        this.talkerType = talkerType;
    }

    public ChatListAdapter getChatListAdapter() {
        return chatListAdapter;
    }

    public void setChatListAdapter(ChatListAdapter chatListAdapter) {
        this.chatListAdapter = chatListAdapter;
    }
    public void disposeChatListAdapter()
    {
        this.chatListAdapter=null;
    }

    public void beginChat(ChatListAdapter chatListAdapter)
    {
        this.setChatListAdapter(chatListAdapter);
        loadChatHistory();
    }

    public void endChat()
    {
        this.disposeChatListAdapter();
        this.chatList.clear();
    }

    public List<ChatItem> newChatList()
    {
        if(chatList==null)
        {
            chatList=new ArrayList<ChatItem>();
        }else
        {
            if(chatList.size()>0) {
                chatList.clear();
            }
        }

        return chatList;
    }

    public void showChat()
    {
        if(chatListAdapter!=null) {
            chatListAdapter.notifyDataSetChanged();
        }
    }

    private void appendChatItem(ChatItem chat)
    {
        this.chatList.add(chat);
    }

    public void appendChatItem(int direction,String content)
    {
        ChatItem chat=new ChatItem();
        chat.setDirection(direction);
        chat.setContent(content);

        appendChatItem(chat);
    }

    private ChatItem messageToChat(MessageModel message)
    {
        ChatItem chat=new ChatItem();
        chat.setContent(message.getContent());
        if(message.getFromid()==userID)
        {
            chat.setDirection(1);
        }else {
            chat.setDirection(0);
        }
        chat.setChatTime(message.getTimestamps());
        return chat;
    }

    private void chatArrive(MessageModel message)
    {
        if(isChatting(message))
        {
            ChatItem chat=messageToChat(message);
            appendChatItem(chat);
            showChat();
            //不标记为已读
            //Messages.GetInstance().readMessage(message.getMid());
        }else
        {
            alertChat(message);
        }
    }

    private boolean isChatting(MessageModel message)
    {
        if(chatListAdapter==null)
        {
            return false;
        }else
        {
            if(message.getFromid()==talkerUserID || message.getAid()==talkerUserID) {
                return true;
            }else
            {
                return false;
            }
        }
    }

    private void alertChat(MessageModel message)
    {
        if(Activity_MainActivity.getInstance().isTopActivity())
        {
            Activity_MainActivity.getInstance().showMessage(message);
        }

        Utils.showNotification(message.getTitle(),message.getContent(),message.getType(), Activity_SplashActivity.class);
    }

    public void addLeftChat(String content)
    {
        appendChatItem(0,content);
        showChat();
    }

    public void addRightChat(String content)
    {
        appendChatItem(1,content);
        showChat();
    }

    private void loadChatHistory()
    {
        Method.getInstance().getChatHistory(mUserToken, talkerUserID, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                MessageListModel messageList=(MessageListModel)o;
                for(MessageModel message:messageList)
                {
                    sendMessage(CHAT_ARRIVE,message);
                }
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

    private void loadTalkerInfo()
    {

    }

    public void sendMessage(int msg,Object o)
    {
        Message message=this.messageHandler.obtainMessage();
        message.arg1=msg;
        message.obj=o;
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
            switch (msg.arg1)
            {
                case CHAT_ARRIVE:
                    chatArrive((MessageModel)msg.obj);
                    break;
            }

            super.handleMessage(msg);
        }
    };
    public Handler getMessageHandler() {
        return messageHandler;
    }
}
