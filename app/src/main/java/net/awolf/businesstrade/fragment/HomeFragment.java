package net.awolf.businesstrade.fragment;

import android.os.Bundle;
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

public class HomeFragment extends Fragment {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;

    private WebView wv_home_ad;

    Activity_MainActivity mainActivity=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        findView(rootView);
        fragmentSetup();

        return rootView;
    }

    private void findView(View rootView) {
        btn1=(Button)rootView.findViewById(R.id.btn_1);
        btn2=(Button)rootView.findViewById(R.id.btn_2);
        btn3=(Button)rootView.findViewById(R.id.btn_3);
        btn4=(Button)rootView.findViewById(R.id.btn_4);
        btn5=(Button)rootView.findViewById(R.id.btn_5);
        wv_home_ad=(WebView)rootView.findViewById(R.id.wv_home_ad);

        mainActivity=Activity_MainActivity.getInstance();
    }

    private void fragmentSetup() {
        wv_home_ad.loadUrl(Const.Config.HOST+Const.Config.HOME+Const.Config.ADPAGE+"?page=home");
        setButtonListener();
    }

    private void setButtonListener() {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_1:
                        Market.getInstance().setMarketType(getString(R.string.market_metal));
                        showMarketActivity();
                        break;
                    case R.id.btn_2:
                        Market.getInstance().setMarketType(getString(R.string.market_stone));
                        showMarketActivity();
                        break;
                    case R.id.btn_3:
                        Market.getInstance().setMarketType(getString(R.string.market_wood));
                        showMarketActivity();
                        break;
                    case R.id.btn_4:
                        Market.getInstance().setMarketType("");
                        showCargoActivity();
                        break;
                    case R.id.btn_5:
                        showMineFragment();
                        break;
                    default:
                        break;
                }
            }
        };

        btn1.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
        btn3.setOnClickListener(onClickListener);
        btn4.setOnClickListener(onClickListener);
        btn5.setOnClickListener(onClickListener);
    }

    private void showMarketActivity()
    {
        mainActivity.showMarketActivity();
    }

    private void showMineFragment()
    {
        mainActivity.showMineFragment();
    }

    private void showCargoActivity()
    {
        mainActivity.showCargoActivity();
    }


}
