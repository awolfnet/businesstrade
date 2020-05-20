package net.awolf.businesstrade.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;
import com.ta.util.TALogger;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.adapter.CargoListAdapter;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.Cargo;
import net.awolf.businesstrade.controller.Chat;
import net.awolf.businesstrade.controller.Market;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.datatable.CargoModel;
import net.awolf.businesstrade.model.datatable.MarketModel;
import net.awolf.businesstrade.model.response.CargoListModel;
import net.awolf.businesstrade.model.response.MarketListModel;
import net.awolf.businesstrade.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Activity_CargoActivity extends TAActivity {
    private final String TAG="Activity_CargoActivity";

    private static Activity_CargoActivity _instance=null;
    public static Activity_CargoActivity getInstance()
    {
        return _instance;
    }

    private final int MSG_HIDE_PROGRESS=10;
    private final int MSG_START=9;
    private final int MSG_MARKETDONE=1;
    private final int MSG_PRODUCTDONE=2;
    private final int MSG_MERCHANTDONE=3;


    private String marketLocation=Market.getInstance().getLocation();
    private String marketType=Market.getInstance().getMarketType();
    private String userToken= UserInfo.getInstance().getUserToken();

    private List<MarketModel> marketList=new ArrayList<MarketModel>();

    @TAInjectView(id = R.id.baseDrawerLayout)
    private DrawerLayout mDrawerLayout;

    @TAInjectView(id = R.id.lv_cargo_list)
    private ListView lvCargo;

    @TAInjectView(id = R.id.btn_clear_cargo)
    private Button btnClearCargo;

    @TAInjectView(id = R.id.right_drawer)
    private ListView lvChoose;

    @TAInjectView(id=R.id.tv_choose_market)
    private TextView tvChooseMarket;

    @TAInjectView(id=R.id.et_CargoDescription)
    private EditText etCargoDescript;

    @Override
    protected void onAfterSetContentView() {
        super.onAfterSetContentView();
        Utils.sendMessage(mHandler,MSG_START);
        lvChoose.setOnItemClickListener(lvChoose_Onclick);
        etCargoDescript.setOnEditorActionListener(etCargoDescription_OnEditorAction);

    }

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        _instance=this;
    }

    public void btnFindCargo_OnClick(View view)
    {
        String text=etCargoDescript.getText().toString();
        if(text.isEmpty())
        {
            Utils.makeToast("请输入车辆特征");
        }else
        {
            Cargo.getInstance().setCargoDescription(text);
            getCargoList();
        }
    }

    public void btnClearCargo_OnClick(View view)
    {
        etCargoDescript.getText().clear();
        Cargo.getInstance().setCargoDescription("");
        getCargoList();
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

    private void getCargoList()
    {
        showProgress();

        String marketName=Market.getInstance().getMarketName();
        String cargoDescription= Cargo.getInstance().getCargoDescription();


        Method.getInstance().getCargoList(userToken, marketName, cargoDescription, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Context context=Activity_CargoActivity.getInstance();
                CargoListAdapter cargoListAdapter=new CargoListAdapter(context, (CargoListModel) o);
                cargoListAdapter.setCargoButtonOnClickListenerCallBack(cargoButtonOnClick);
                lvCargo.setAdapter(cargoListAdapter);
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
                Utils.sendMessage(mHandler,MSG_HIDE_PROGRESS);
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

    public void ChooseMarket_OnClick(View view)
    {
        showMarketList();
    }


    TextView.OnEditorActionListener etCargoDescription_OnEditorAction=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH) {

                String text=etCargoDescript.getText().toString();
                if(text.isEmpty())
                {
                    Utils.makeToast("请输入车辆特征");
                }else
                {
                    TALogger.d(TAG,text);
                    Cargo.getInstance().setCargoDescription(text);
                    getCargoList();
                }
            }
            return true;
        }
    };

    AdapterView.OnItemClickListener lvChoose_Onclick=new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String str = (String) ((TextView) view).getText();
            tvChooseMarket.setText(str);
            Market.getInstance().setMarketName(str);
            mDrawerLayout.closeDrawer(lvChoose);
            getCargoList();

//            showProgress();
//            Utils.sendMessage(mHandler,MSG_PRODUCTDONE);
        }
    };



    private CargoListAdapter.CargoButtonOnClick cargoButtonOnClick =new CargoListAdapter.CargoButtonOnClick(){
        @Override
        public void btnChat_OnClick(CargoModel cargoInfo) {
            int cargoOwnerID=cargoInfo.getCargo_owner_id();
            if(cargoOwnerID==0)
            {
                Utils.makeToast("该车辆尚未认领，无法聊天");
            }else
            {
                Cargo.getInstance().setCargo(cargoInfo);
                Cargo.getInstance().setCargoFavorite(null);

                Chat.getInstance().setTalkerType(1);
                Chat.getInstance().setTalkerUserID(cargoInfo.getCargo_owner_id());
                Chat.getInstance().setTalkerName(cargoInfo.getCargo_plate());

                Utils.makeToast("开始与" + cargoInfo.getCargo_plate() + "聊天");
                doActivity(R.string.ChatActivity);
            }
        }

        @Override
        public void btnFavorite_OnClick(CargoModel cargoInfo) {
            addCargoToFavorite(String.valueOf(cargoInfo.getCargo_id()));
        }

        @Override
        public void btnDialer_OnClick(CargoModel cargoInfo) {
            dialerPhoneNumber(cargoInfo.getDriver_contact());
        }

        @Override
        public void btnWebsite_OnClick(CargoModel cargoInfo) {
            dialerPhoneNumber(cargoInfo.getDriver_contact());

        }
    };

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void dialerPhoneNumber(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private void addCargoToFavorite(String cargoID)
    {
        Method.getInstance().addCargoToFavorite(userToken, cargoID, new IMethodCallback() {
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
                    Utils.sendMessage(mHandler,MSG_HIDE_PROGRESS);
                    break;
                }
                case MSG_PRODUCTDONE:

                    break;
                case MSG_MERCHANTDONE:
                {

                }
                case MSG_HIDE_PROGRESS:
                    hideProgress();;
            }
            super.handleMessage(msg);
        }
    };

}
