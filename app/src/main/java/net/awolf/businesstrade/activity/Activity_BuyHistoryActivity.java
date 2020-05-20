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
import net.awolf.businesstrade.adapter.CargoOrderHistoryListAdapter;
import net.awolf.businesstrade.adapter.OrderHistoryListAdapter;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.response.CargoOrderHistoryListModel;
import net.awolf.businesstrade.model.response.OrderHistoryListModel;
import net.awolf.businesstrade.util.Utils;

import java.util.ArrayList;

public class Activity_BuyHistoryActivity extends TAActivity {
    private final String TAG="Activity_BuyHistoryActivity";

    @TAInjectView(id = R.id.baseDrawerLayout)
    private DrawerLayout mDrawerLayout;

    @TAInjectView(id = R.id.right_drawer)
    private ListView lvChoose;

    @TAInjectView(id = R.id.lv_buyhistory_list)
    private ListView lvBuyHistory;

    @TAInjectView(id = R.id.tv_choose_order)
    private TextView tv_choose_order;

    private String userToken= UserInfo.getInstance().getUserToken();

    private static Activity_BuyHistoryActivity instance=null;
    public static Activity_BuyHistoryActivity getInstance() {
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

    public void ChooseOrderType_OnClick(View view)
    {
        showOrderTypeList();
    }

    private void showOrderTypeList()
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
            tv_choose_order.setText(str);
            if(position==0)
            {
                //选择商户
                showOrderHistoryList();
            }else
            {
                //选择车辆
                showCargoOrderHistoryList();
            }
            lvBuyHistory.setAdapter(null);

            mDrawerLayout.closeDrawer(lvChoose);
        }
    };

    private void showCargoOrderHistoryList()
    {
        Method.getInstance().getBuyCargoHistoryList(userToken, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Context context=Activity_BuyHistoryActivity.getInstance();
                CargoOrderHistoryListAdapter cargoListAdapter=new CargoOrderHistoryListAdapter(context, (CargoOrderHistoryListModel)o);
                lvBuyHistory.setAdapter(cargoListAdapter);
            }

            @Override
            public void onServerFailure(String response) {

            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                Utils.makeToast(unfulfilMessage);
            }

            @Override
            public void onException(String errorMessage) {

            }

            @Override
            public void onFinish() {
                hideProgress();
            }
        });
        hideProgress();
    }

    private void showOrderHistoryList()
    {
        Method.getInstance().getBuyHistoryList(userToken, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Context context=Activity_BuyHistoryActivity.getInstance();
                OrderHistoryListAdapter orderHistoryListAdapter= new OrderHistoryListAdapter(context, (OrderHistoryListModel) o);
                lvBuyHistory.setAdapter(orderHistoryListAdapter);
            }

            @Override
            public void onServerFailure(String response) {

            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {
                Utils.makeToast(unfulfilMessage);
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


}
