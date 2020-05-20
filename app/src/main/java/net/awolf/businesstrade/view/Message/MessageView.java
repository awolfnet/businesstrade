package net.awolf.businesstrade.view.Message;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.model.datatable.MessageModel;
import net.awolf.businesstrade.util.Utils;

/**
 * Created by Work on 2017/8/16.
 */

public class MessageView extends LinearLayout {

    private Context mContext;
    TextView tv_message_time;
    TextView tv_message_title;
    TextView tv_message_content;

    public MessageView(Context context) {
        super(context);
        mContext=context;
        initView();
    }

    public MessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initView();
    }


    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.common_messageview, this);
        tv_message_time=(TextView)findViewById(R.id.tv_message_time);
        tv_message_title=(TextView)findViewById(R.id.tv_message_title);
        tv_message_content=(TextView)findViewById(R.id.tv_message_content);
    };

    public void showMessage(MessageModel message)
    {
        if(message==null)
        {
            tv_message_time.setText(Utils.getDate("yyyy/MM/dd hh:mm:ss"));
            tv_message_time.setText("暂无最新消息");
            tv_message_content.setText(message.getContent());
        }else {
            tv_message_time.setText(message.getTimestamps());
            tv_message_title.setText(message.getTitle());
            tv_message_content.setText(message.getContent());
        }
    }
}
