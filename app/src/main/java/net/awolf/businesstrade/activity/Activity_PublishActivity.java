package net.awolf.businesstrade.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.util.Utils;

public class Activity_PublishActivity extends TAActivity {
    private final String TAG = "Activity_PublishActivity";
    private static Activity_PublishActivity instance = null;

    public static Activity_PublishActivity getInstance() {
        return instance;
    }

    @TAInjectView(id=R.id.btnPublish)
    private Button btnPublish;

    @TAInjectView(id = R.id.etPublishMessage)
    private EditText etPublishMessage;

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        instance=this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
    }

    private void btnPublish_OnClick(View view)
    {
        String publishMessage=etPublishMessage.getText().toString();

        if (publishMessage.isEmpty()) {
            Utils.makeToast("请输入要发布的消息");
        }else
        {
            Utils.makeToast("无法发送消息");
        }
    }
}
