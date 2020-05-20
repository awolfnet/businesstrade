package net.awolf.businesstrade.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.adapter.CargoOrderHistoryListAdapter;
import net.awolf.businesstrade.adapter.MessageListAdapter;
import net.awolf.businesstrade.controller.Chat;
import net.awolf.businesstrade.controller.Messages;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.model.datatable.MessageModel;
import net.awolf.businesstrade.model.request.ReachedMessageList;
import net.awolf.businesstrade.model.response.CargoOrderHistoryListModel;
import net.awolf.businesstrade.model.response.MessageListModel;
import net.awolf.businesstrade.model.response.UserinfoModel;
import net.awolf.businesstrade.util.Utils;

public class Activity_MessageActivity extends TAActivity {

    @TAInjectView(id = R.id.lvMessage)
    private ListView lvMessage;

    @TAInjectView(id = R.id.btnClear)
    private Button btnClear;

    @TAInjectView(id = R.id.btnPublish)
    private Button btnPublish;

    MessageListAdapter messageListAdapter=null;

    private String userToken;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {

    }

    @Override
    protected void onAfterSetContentView() {
        Context context=Activity_MessageActivity.this;
        MessageListModel messageList=Messages.GetInstance().getMessageList();
        if(messageList!=null)
        {
            messageListAdapter=new MessageListAdapter(context, Messages.GetInstance().getMessageList());
            lvMessage.setAdapter(messageListAdapter);
        }else
        {
            Utils.makeToast(Utils.getString(R.string.no_message));
        }

        lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageModel message=(MessageModel)lvMessage.getItemAtPosition(i);
                showMessage(message);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageListAdapter!=null)
                {
                    ReachedMessageList messageList=new ReachedMessageList();
                    for(int i=0;i<messageListAdapter.getCount();i++)
                    {
                        MessageModel message=(MessageModel)messageListAdapter.getItem(i);
                        messageList.add(message.getMid());
                    }
                    Messages.GetInstance().readMessage(messageList);
                    Messages.GetInstance().clearLocalMessage();
                    messageListAdapter.notifyDataSetChanged();
                }
                Utils.makeToast("清除成功");
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doActivity(R.string.PublishActivity);
            }
        });
    }

    private void showMessage(MessageModel message)
    {
        switch (message.getType())
        {
            case Messages.MESSAGE_TYPE_MAIL:
                showNormalDialog(message);
                break;
            case Messages.MESSAGE_TYPE_CHAT:
                showChat(message);
                break;

        }
    }

    private void showChat(MessageModel message)
    {
        showProgress();
        loadUserinfo(message.getFromid());
    }

    private void showNormalDialog(MessageModel message){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(Activity_MessageActivity.this);
        normalDialog.setIcon(R.drawable.ic_launcher);
        normalDialog.setTitle(message.getTitle());
        normalDialog.setMessage(message.getContent());
//        normalDialog.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //...To-do
//                    }
//                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    private void loadUserinfo(int talkerID)
    {
        final int mTalkerID=talkerID;
        userToken= UserInfo.getInstance().getUserToken();

        Method.getInstance().getUserinfo(userToken, mTalkerID, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                UserinfoModel userInfo=(UserinfoModel)o;
                String userNickname=userInfo.getUserNickname();
                String userPhone=userInfo.getUserPhone();
                String showName="";

                if(userNickname.isEmpty()==false) {
                    showName=userNickname;
                }else
                {
                    showName=userPhone;
                }

                Chat.getInstance().setTalkerName(showName);
                Chat.getInstance().setTalkerUserID(mTalkerID);
                Chat.getInstance().setTalkerType(0);

                Utils.makeToast("开始与" + showName + "聊天");
                doActivity(R.string.ChatActivity);
            }

            @Override
            public void onServerFailure(String response) {
                Utils.makeToast("发起聊天失败");
            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                Utils.makeToast("发起聊天失败");
            }

            @Override
            public void onException(String errorMessage) {
                Utils.makeToast("发起聊天失败");
            }

            @Override
            public void onFinish() {
                hideProgress();
            }
        });
    }
}
