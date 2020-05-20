package net.awolf.businesstrade.fragment;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.activity.Activity_MainActivity;
import net.awolf.businesstrade.controller.Market;
import net.awolf.businesstrade.util.Const;


public class CityFragment extends Fragment {

    private Button btn1;
    private Button btn2;
    private Button btn3;

    private WebView wv_city_ad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city, container, false);
        findView(rootView);
        fragmentSetup();

        return rootView;
    }

    private void findView(View rootView) {
        btn1=(Button)rootView.findViewById(R.id.btn_1);
        btn2=(Button)rootView.findViewById(R.id.btn_2);
        btn3=(Button)rootView.findViewById(R.id.btn_3);
        wv_city_ad=(WebView)rootView.findViewById(R.id.wv_city_ad);
    }

    private void fragmentSetup() {
        wv_city_ad.loadUrl(Const.Config.HOST+Const.Config.HOME+Const.Config.ADPAGE+"?page=city");
        setButtonListener();
    }



    private void setButtonListener() {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_1:
                        Market.getInstance().setLocation(getString(R.string.city_liaoning));
                        break;
                    case R.id.btn_2:
                        Market.getInstance().setLocation(getString(R.string.city_jilin));
                        break;
                    case R.id.btn_3:
                        Market.getInstance().setLocation(getString(R.string.city_heilongjing));
                        break;
                    default:
                        break;
                }
                Activity_MainActivity.getInstance().showHomeFragment();
            }
        };

        btn1.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
        btn3.setOnClickListener(onClickListener);

    }
}
