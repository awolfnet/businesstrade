package net.awolf.businesstrade.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.ta.TAActivity;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.util.Const;

public class Activity_ShareActivity extends TAActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_share);
        WebView wv_share=findViewById(R.id.wv_share);
        wv_share.loadUrl(Const.Config.HOST+Const.Config.HOME+Const.Config.SHARE_PAGE);
    }
}
