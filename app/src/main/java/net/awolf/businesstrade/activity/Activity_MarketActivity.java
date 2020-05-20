package net.awolf.businesstrade.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;
import com.ta.util.TALogger;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.adapter.MerchantListAdapter;
import net.awolf.businesstrade.adapter.MerchantListAdapter.MerchantButtonOnClick;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.Chat;
import net.awolf.businesstrade.controller.Market;
import net.awolf.businesstrade.controller.Merchant;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.datatable.MarketModel;
import net.awolf.businesstrade.model.datatable.MerchantModel;
import net.awolf.businesstrade.model.datatable.ProductModel;
import net.awolf.businesstrade.model.response.MarketListModel;
import net.awolf.businesstrade.model.response.MerchantListModel;
import net.awolf.businesstrade.model.response.ProductListModel;
import net.awolf.businesstrade.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Activity_MarketActivity extends TAActivity {
    private final String TAG="Activity_MarketActivity";
    private static Activity_MarketActivity instance=null;
    public static Activity_MarketActivity getInstance() {
        return instance;
    }

    private String marketLocation=Market.getInstance().getLocation();
    private String marketType=Market.getInstance().getMarketType();
    private String userToken= UserInfo.getInstance().getUserToken();

    private List<MarketModel> marketList=new ArrayList<MarketModel>();
    private List<ProductModel> productList=new ArrayList<ProductModel>();


    @TAInjectView(id = R.id.baseDrawerLayout)
    private DrawerLayout mDrawerLayout;

    @TAInjectView(id = R.id.right_drawer)
    private ListView lvChoose;

    @TAInjectView(id = R.id.lv_merchant_list)
    private ListView lvMerchant;

    @TAInjectView(id = R.id.tv_choose_market)
    private TextView tv_choose_market;

    @TAInjectView(id = R.id.tv_choose_product)
    private TextView tv_choose_product;

    private final int MSG_ALLDONE=10;
    private final int MSG_START=9;
    private final int MSG_MARKETDONE=1;
    private final int MSG_PRODUCTDONE=2;
    private final int MSG_MERCHANTDONE=3;

    private int mDisplayViewID;

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        instance=this;
    }

    @Override
    protected void onAfterSetContentView() {
        TALogger.d(TAG, marketLocation);
        TALogger.d(TAG, marketType);

//        mDrawerLayout = (DrawerLayout) findViewById(R.id.baseDrawerLayout);
//        lvChoose = (ListView)findViewById(R.id.right_drawer);
//        lvMerchant=(ListView)findViewById(R.id.lv_merchant_list);

        lvChoose.setOnItemClickListener(lvChoose_Onclick);
        lvMerchant.setOnItemClickListener(lvMerchant_OnClick);

        Utils.sendMessage(mHandler,MSG_START);
    }

    public void ButtonOnClick(View view)
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("1天         ");
        list.add("2天         ");
        list.add("3天         ");
        list.add("4天         ");
        list.add("5天         ");
        list.add("6天         ");
        list.add("7天         ");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getInstance(), android.R.layout.simple_expandable_list_item_1, list);
        lvMerchant.setAdapter(adapter);
    }

    public void ChooseMarket_OnClick(View view)
    {
        mDisplayViewID=R.id.tv_choose_market;
        showMarketList();
    }

    public void ChooseProduct_OnClick(View view)
    {
        mDisplayViewID=R.id.tv_choose_product;
        showProductList();
    }

    private MerchantButtonOnClick merchantButtonOnClick =new MerchantButtonOnClick(){
        @Override
        public void btnChat_OnClick(MerchantModel merchantInfo) {

            String merchantName=merchantInfo.getMerchant_name();
            int merchantOwnerID=merchantInfo.getMerchant_owner_id();
            int userID = UserInfo.getInstance().getUserID();

//            if(merchantOwnerID==userID)
//            {
//                Utils.makeToast("不能跟自己聊天");
//            }else if(merchantOwnerID==0)
//            {
//                Utils.makeToast("该商户尚未认领，无法聊天");
//            }
//            else
//            {
//                Chat.getInstance().setTalkerName(merchantName);
//                Chat.getInstance().setTalkerUserID(merchantID);
//                Chat.getInstance().setTalkerType(0);
//
//                Merchant.GetInstance().setMerchant(merchantInfo);
//                Merchant.GetInstance().setMerchantFavorite(null);
//                Utils.makeToast("开始与" + merchantName + "聊天");
//                doActivity(R.string.ChatActivity);
//            }

            if(merchantOwnerID==0)
            {
                Utils.makeToast("该商户尚未认领，无法聊天");
            }
            else
            {
                Chat.getInstance().setTalkerName(merchantName);
                Chat.getInstance().setTalkerUserID(merchantOwnerID);
                Chat.getInstance().setTalkerType(0);

                Merchant.GetInstance().setMerchant(merchantInfo);
                Merchant.GetInstance().setMerchantFavorite(null);
                Utils.makeToast("开始与" + merchantName + "聊天");
                doActivity(R.string.ChatActivity);
            }

        }

        @Override
        public void btnFavorite_OnClick(MerchantModel merchantInfo) {
            addMerchantToFavorite(String.valueOf(merchantInfo.merchantid));
        }

        @Override
        public void btnDialer_OnClick(MerchantModel merchantInfo) {
            Utils.dialerPhoneNumber(merchantInfo.getMerchant_contact(),getInstance());
        }

        @Override
        public void btnWebsite_OnClick(MerchantModel merchantInfo) {
            //Utils.dialerPhoneNumber(merchantInfo.getMerchant_contact(),getInstance());
            if(merchantInfo.getMerchant_website()!=null && merchantInfo.getMerchant_website().length()!=0)
            {
                Utils.openURL(Activity_MarketActivity.instance,merchantInfo.getMerchant_website());
            }
        }
    };

    private void addMerchantToFavorite(String merchantID)
    {
        Method.getInstance().addMerchantToFavorite(userToken, merchantID, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Utils.makeToast("添加到收藏夹成功");
            }

            @Override
            public void onServerFailure(String response) {
                TALogger.d(TAG,"Server failure:"+response);
                Utils.makeToast(response);
            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                TALogger.d(TAG,"Server unfulfil:"+unfulfilMessage);
                Utils.makeToast(unfulfilMessage);
            }

            @Override
            public void onException(String errorMessage) {
                TALogger.d(TAG,"Exception:"+errorMessage);
                Utils.makeToast("连接服务器失败，请检查网络");
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void getMerchantList(String productName,String marketName)
    {

        Method.getInstance().getMerchantList(userToken, productName, marketName, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Context context=Activity_MarketActivity.getInstance();
                MerchantListAdapter merchantListAdapter=new MerchantListAdapter(context, (MerchantListModel)o);
                merchantListAdapter.setMerchantButtonOnClickListenerCallBack(merchantButtonOnClick);
                lvMerchant.setAdapter(merchantListAdapter);
            }

            @Override
            public void onServerFailure(String response) {
                TALogger.d(TAG,"Server failure:"+response);
                Utils.makeToast(response);
            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                TALogger.d(TAG,"Server unfulfil:"+unfulfilMessage);
                Utils.makeToast(unfulfilMessage);
            }

            @Override
            public void onException(String errorMessage) {
                TALogger.d(TAG,"Exception:"+errorMessage);
                Utils.makeToast("连接服务器失败，请检查网络");
            }

            @Override
            public void onFinish() {
                Utils.sendMessage(mHandler,MSG_MERCHANTDONE);
            }
        });
    }

    private void getMarketList()
    {
        showProgress();
        Method.getInstance().getMarketList(userToken, marketLocation, marketType, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                marketList=(MarketListModel)o;
            }

            @Override
            public void onServerFailure(String response) {
                TALogger.d(TAG,"Server failure:"+response);
                Utils.makeToast(response);
            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                TALogger.d(TAG,"Server unfulfil:"+unfulfilMessage);
                Utils.makeToast(unfulfilMessage);
                lvMerchant.setAdapter(null);
            }

            @Override
            public void onException(String errorMessage) {
                TALogger.d(TAG,"Exception:"+errorMessage);
                Utils.makeToast("连接服务器失败，请检查网络");
            }

            @Override
            public void onFinish() {
                Utils.sendMessage(mHandler,MSG_MARKETDONE);
            }
        });
    }

    private void showMarketList()
    {
        ArrayList<String> list = new ArrayList<String>();
        for(MarketModel market:marketList)
        {
            list.add(market.getMarket_name());
        }
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, list);

        lvChoose.setAdapter(adapter);
        mDrawerLayout.openDrawer(lvChoose);
    }

    private void getProductList()
    {
        showProgress();
        Method.getInstance().getProductList(userToken, marketType, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                productList=(ProductListModel)o;
            }

            @Override
            public void onServerFailure(String response) {
                TALogger.d(TAG,"Server failure:"+response);
                Utils.makeToast(response);
            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                TALogger.d(TAG,"Server unfulfil:"+unfulfilMessage);
                Utils.makeToast(unfulfilMessage);
            }

            @Override
            public void onException(String errorMessage) {
                TALogger.d(TAG,"Exception:"+errorMessage);
                Utils.makeToast("连接服务器失败，请检查网络");
            }

            @Override
            public void onFinish() {
                Utils.sendMessage(mHandler,MSG_PRODUCTDONE);
            }
        });
    }

    private void showProductList()
    {
        ArrayList<String> list = new ArrayList<String>();
        for(ProductModel product:productList)
        {
            list.add(product.getName());
        }
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, list);

        lvChoose.setAdapter(adapter);
        mDrawerLayout.openDrawer(lvChoose);
    }

    AdapterView.OnItemClickListener lvMerchant_OnClick=new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    AdapterView.OnItemClickListener lvChoose_Onclick=new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String str = (String) ((TextView) view).getText();
            TextView tv = (TextView) findViewById(mDisplayViewID);
            tv.setText(str);

            if(mDisplayViewID==R.id.tv_choose_product)
            {
                Market.getInstance().setProductName(str);
            }

            if(mDisplayViewID==R.id.tv_choose_market)
            {
                Market.getInstance().setMarketName(str);
            }

            mDrawerLayout.closeDrawer(lvChoose);

            showProgress();
            Utils.sendMessage(mHandler,MSG_PRODUCTDONE);
        }
    };

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START:
                {
                    showProgress();
                    getMarketList();
                    break;
                }
                case MSG_MARKETDONE:
                {
                    getProductList();
                    break;
                }
                case MSG_PRODUCTDONE:
                    getMerchantList(Market.getInstance().getProductName(),Market.getInstance().getMarketName());
                    break;
                case MSG_MERCHANTDONE:
                {
                    Utils.sendMessage(mHandler,MSG_ALLDONE);
                }
                case MSG_ALLDONE:
                    hideProgress();;
            }
            super.handleMessage(msg);
        }
    };

}
