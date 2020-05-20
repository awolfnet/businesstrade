package net.awolf.businesstrade.activity;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.adapter.CargoFavoriteListAdapter;
import net.awolf.businesstrade.adapter.MerchantFavoriteListAdapter;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.Cargo;
import net.awolf.businesstrade.controller.Chat;
import net.awolf.businesstrade.controller.Merchant;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.datatable.CargoFavoriteModel;
import net.awolf.businesstrade.model.datatable.MerchantFavoriteModel;
import net.awolf.businesstrade.model.response.CargoFavoriteListModel;
import net.awolf.businesstrade.model.response.MerchantFavoriteListModel;
import net.awolf.businesstrade.util.Utils;

import java.util.ArrayList;

public class Activity_FavoriteActivity extends TAActivity {
    private final String TAG="Activity_FavoriteActivity";

    @TAInjectView(id = R.id.baseDrawerLayout)
    private DrawerLayout mDrawerLayout;

    @TAInjectView(id = R.id.right_drawer)
    private ListView lvChoose;

    @TAInjectView(id = R.id.lv_favorite_list)
    private ListView lvFavorite;

    @TAInjectView(id = R.id.tv_choose_favorite)
    private TextView tv_choose_favorite;

    private String userToken= UserInfo.getInstance().getUserToken();

    private static Activity_FavoriteActivity instance=null;
    public static Activity_FavoriteActivity getInstance() {
        return instance;
    }

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        instance=this;
    }

    @Override
    protected void onAfterSetContentView() {
        super.onAfterSetContentView();

        lvChoose.setOnItemClickListener(lvChoose_Onclick);
    }

    public void ChooseFavorite_OnClick(View view)
    {
        showFavoriteTypeList();
    }

    private void showFavoriteTypeList()
    {
        ArrayList<String> list = new ArrayList<String>();

        list.add(0,"商户");
        list.add(1,"车辆");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);

        lvChoose.setAdapter(adapter);
        mDrawerLayout.openDrawer(lvChoose);
    }

    AdapterView.OnItemClickListener lvChoose_Onclick=new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showProgress();
            String str = (String) ((TextView) view).getText();
            tv_choose_favorite.setText(str);
            if(position==0)
            {
                //选择商户
                showMerchantFavorite();
            }else
            {
                //选择车辆
                showCargoFavorite();
            }
//            lvFavorite.setAdapter(null);

            mDrawerLayout.closeDrawer(lvChoose);
        }
    };

    private void showCargoFavorite()
    {
        Method.getInstance().getCargoFavoriteList(userToken, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Context context=Activity_FavoriteActivity.getInstance();
                CargoFavoriteListAdapter cargoListAdapter=new CargoFavoriteListAdapter(context, (CargoFavoriteListModel) o);
                cargoListAdapter.setCargoButtonOnClickListenerCallBack(cargoButtonOnClick);
                lvFavorite.setAdapter(cargoListAdapter);
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
                hideProgress();
            }
        });
    }

    private void showMerchantFavorite()
    {
        Method.getInstance().getMerchantFavoriteList(userToken, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Context context=Activity_FavoriteActivity.getInstance();
                MerchantFavoriteListAdapter merchantListAdapter= new MerchantFavoriteListAdapter(context, (MerchantFavoriteListModel) o);
                merchantListAdapter.setMerchantButtonOnClickListenerCallBack(merchantButtonOnClick);
                lvFavorite.setAdapter(merchantListAdapter);
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
                hideProgress();
            }
        });
    }

    private CargoFavoriteListAdapter.CargoButtonOnClick cargoButtonOnClick =new CargoFavoriteListAdapter.CargoButtonOnClick(){
        @Override
        public void btnChat_OnClick(CargoFavoriteModel cargoInfo) {
            Chat.getInstance().setTalkerName(cargoInfo.getCargo_plate());
            Chat.getInstance().setTalkerUserID(cargoInfo.getCargo_owner_id());
            Chat.getInstance().setTalkerType(1);

            Cargo.getInstance().setCargo(null);
            Cargo.getInstance().setCargoFavorite(cargoInfo);
            Utils.makeToast("开始与" + cargoInfo.getCargo_plate() + "聊天");
            doActivity(R.string.ChatActivity);
        }

        @Override
        public void btnFavorite_OnClick(CargoFavoriteModel cargoInfo) {

        }

        @Override
        public void btnDialer_OnClick(CargoFavoriteModel cargoInfo) {
            Utils.dialerPhoneNumber(cargoInfo.getDriver_contact(),getInstance());
        }

        @Override
        public void btnWebsite_OnClick(CargoFavoriteModel cargoInfo) {

        }
    };

    private MerchantFavoriteListAdapter.MerchantButtonOnClick merchantButtonOnClick=new MerchantFavoriteListAdapter.MerchantButtonOnClick() {
        @Override
        public void btnChat_OnClick(MerchantFavoriteModel merchantInfo) {
            Chat.getInstance().setTalkerName(merchantInfo.getMerchant_name());
            Chat.getInstance().setTalkerUserID(merchantInfo.getMerchant_owner_id());
            Chat.getInstance().setTalkerType(0);

            Merchant.GetInstance().setMerchant(null);
            Merchant.GetInstance().setMerchantFavorite(merchantInfo);
            Utils.makeToast("开始与" + merchantInfo.getMerchant_name() + "聊天");
            doActivity(R.string.ChatActivity);
        }

        @Override
        public void btnFavorite_OnClick(MerchantFavoriteModel merchantInfo) {

        }

        @Override
        public void btnDialer_OnClick(MerchantFavoriteModel merchantInfo)
        {
            Utils.dialerPhoneNumber(merchantInfo.getMerchant_contact(),getInstance());
        }

        @Override
        public void btnWebsite_OnClick(MerchantFavoriteModel merchantInfo)
        {

        }
    };
}
