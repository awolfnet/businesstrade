package net.awolf.businesstrade.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.ta.annotation.TAInjectView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.activity.Activity_MainActivity;
import net.awolf.businesstrade.util.Const;


public class MineFragment extends Fragment {

    private Button btn1;
    private Button btn2;
    private Button btn3;

    private WebView wv_mine_ad;

    Activity_MainActivity mainActivity=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        findView(rootView);
        fragmentSetup();

        return rootView;
    }

    private void findView(View rootView) {

        btn1=(Button)rootView.findViewById(R.id.btn_1);
        btn2=(Button)rootView.findViewById(R.id.btn_2);
        btn3=(Button)rootView.findViewById(R.id.btn_3);
        wv_mine_ad=(WebView)rootView.findViewById(R.id.wv_mine_ad);
        mainActivity=Activity_MainActivity.getInstance();
    }

    private void fragmentSetup() {
        wv_mine_ad.loadUrl(Const.Config.HOST+Const.Config.HOME+Const.Config.ADPAGE+"?page=mine");
        setButtonListener();
    }


    private void setButtonListener() {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_1:
                        mainActivity.showSellHistoryActivity();
                        break;
                    case R.id.btn_2:
                        mainActivity.showBuyHistoryActivity();
                        break;
                    case R.id.btn_3:
                        mainActivity.showFavoriteActivity();
                        break;
                    default:
                        break;
                }

            }
        };

        btn1.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
        btn3.setOnClickListener(onClickListener);
    }

}
