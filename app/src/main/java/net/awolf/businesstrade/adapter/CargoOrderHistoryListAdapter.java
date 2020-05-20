package net.awolf.businesstrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.model.datatable.CargoOrderHistoryModel;
import net.awolf.businesstrade.model.datatable.OrderHistoryModel;
import net.awolf.businesstrade.model.response.CargoOrderHistoryListModel;
import net.awolf.businesstrade.model.response.OrderHistoryListModel;

/**
 * Created by zhaohai on 2017/10/27.
 */

public class CargoOrderHistoryListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    CargoOrderHistoryListModel orderHistoryList;

    public CargoOrderHistoryListAdapter(Context context, CargoOrderHistoryListModel orderHistoryList)
    {
        inflater = LayoutInflater.from(context);
        this.orderHistoryList=orderHistoryList;
    }

    @Override
    public int getCount() {
        return orderHistoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderHistoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 将写好的xml文件转化成一个view
        View rootView = inflater.inflate(R.layout.view_cargoorderhistory_item, null);

        CargoOrderHistoryModel orderHistory=orderHistoryList.get(i);

        /*  车牌    */
        TextView tv_cargo_plate=(TextView)rootView.findViewById(R.id.tv_cargo_plate);
        tv_cargo_plate.setText(orderHistory.getCargo_plate());

        /*  订单留言    */
        TextView tv_ordercomment=(TextView)rootView.findViewById(R.id.tv_ordercomment);
        tv_ordercomment.setText(orderHistory.getOrder_comment());

        /*  订单编号    */
        TextView tv_orderid=(TextView)rootView.findViewById(R.id.tv_orderid);
        tv_orderid.setText(orderHistory.getOrder_id());

        /*  车主电话    */
        TextView tv_cargo_contact=(TextView)rootView.findViewById(R.id.tv_cargo_contact);
        tv_cargo_contact.setText(orderHistory.getCargo_contact());

        /*  下单日期    */
        TextView tv_orderdate=(TextView)rootView.findViewById(R.id.tv_orderdate);
        tv_orderdate.setText(orderHistory.getOrder_date().toString());

        /*  订单金额    */
        TextView tv_amount=(TextView)rootView.findViewById(R.id.tv_amount);
        tv_amount.setText(orderHistory.getOrder_amount().toString());

        /*  买家姓名    */
        TextView tv_buyer_name=(TextView)rootView.findViewById(R.id.tv_buyer_name);
        tv_buyer_name.setText(orderHistory.getBuyer_name());

        /*  买家联系方式  */
        TextView tv_buyer_contact=(TextView)rootView.findViewById(R.id.tv_buyer_contact);
        tv_buyer_contact.setText(orderHistory.getBuyer_contact());

        return rootView;
    }
}
