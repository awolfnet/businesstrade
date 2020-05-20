package net.awolf.businesstrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.model.datatable.MessageModel;
import net.awolf.businesstrade.model.response.CargoOrderHistoryListModel;
import net.awolf.businesstrade.model.response.MessageListModel;

import org.w3c.dom.Text;

/**
 * Created by zhaohai on 2017/10/29.
 */

public class MessageListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    MessageListModel messageList;

    public MessageListAdapter(Context context, MessageListModel messageList)
    {
        inflater = LayoutInflater.from(context);
        this.messageList=messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        int index=messageList.size()-1;
        index-=i;
        return messageList.get(index);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 将写好的xml文件转化成一个view
        View rootView = inflater.inflate(R.layout.view_message_item, null);

        int index=messageList.size()-1;
        index-=i;

        MessageModel message=messageList.get(index);

        //消息时间
        TextView tv_message_time=(TextView)rootView.findViewById(R.id.tv_message_time);
        tv_message_time.setText(message.getTimestamps());
        //消息标题
        TextView tv_message_title=(TextView)rootView.findViewById(R.id.tv_message_title);
        tv_message_title.setText(message.getTitle());

        //消息内容
        TextView tv_message_content=(TextView)rootView.findViewById(R.id.tv_message_content);
        tv_message_content.setText(message.getContent());

        return  rootView;
    }
}
