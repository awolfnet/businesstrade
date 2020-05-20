package net.awolf.businesstrade.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.model.local.ChatItem;
import net.awolf.businesstrade.util.ImageUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaohai on 2017/10/22.
 */

public class ChatListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ChatItem> chatList=null;
    private String talkerName="";
    private String myName="";

    public ChatListAdapter(Context context,List<ChatItem> chatList)
    {
        inflater = LayoutInflater.from(context);
        if(chatList==null) {
            this.chatList = new ArrayList<ChatItem>();
        }else
        {
            this.chatList=chatList;
        }
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = null;
        EditText etChat=null;
        ChatItem chat=chatList.get(i);

        if(chat.direction==ChatItem.ChatDirection.LEFT)
        {
            rootView=inflater.inflate(R.layout.view_chat_left, null);
            etChat=(EditText)rootView.findViewById(R.id.etChatLeft);
            TextView tvTalkerName=(TextView)rootView.findViewById(R.id.tvTalkerName);
            TextView tvTalkTime=(TextView)rootView.findViewById(R.id.tvTalkTime);
            tvTalkerName.setText(talkerName);
            etChat.setText(chat.getContent());
            tvTalkTime.setText(chat.chatTime);
        }else
        {
            switch (chat.getChatType())
            {
                default:
                case ChatItem.ChatType.TEXT:
                {
                    rootView=inflater.inflate(R.layout.view_chat_right, null);
                    etChat=(EditText)rootView.findViewById(R.id.etChatRight);
                    etChat.setText(chat.getContent());
                    break;
                }
                case ChatItem.ChatType.IMAGE:
                {
                    rootView=inflater.inflate(R.layout.view_chat_image_right, null);
                    ImageView ivImageChat=(ImageView)rootView.findViewById(R.id.ivImageChat);
                    Bitmap bitmap = null; //从本地取图片(在cdcard中获取)  //
                    try {
                        bitmap = ImageUtil.getLoacalBitmap(chat.getImageFile());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ivImageChat .setImageBitmap(bitmap); //设置Bitmap
                    break;
                }
                case ChatItem.ChatType.VOICE:
                {
                    rootView=inflater.inflate(R.layout.view_chat_voice_right, null);
                    ImageButton ibVoiceChat=(ImageButton)rootView.findViewById(R.id.ibVoiceChat);
                    break;
                }
            }

            TextView tvMyName=(TextView)rootView.findViewById(R.id.tvMyName);
            tvMyName.setText(myName);

        }
        return rootView;
    }

    public  void addChat(int direction,String content)
    {
        ChatItem chat =new ChatItem();
        chat.setDirection(direction);
        chat.setContent(content);
        this.chatList.add(chat);
        this.notifyDataSetChanged();
    }

    public void addChat(ChatItem chat)
    {
        this.chatList.add(chat);
        this.notifyDataSetChanged();
    }

    //TODO: 临时处理
    public void addVoiceChat()
    {

    }

    public void setTalkerName(String talkerName) {
        this.talkerName = talkerName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }
}
