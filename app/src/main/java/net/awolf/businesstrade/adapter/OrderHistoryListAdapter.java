package net.awolf.businesstrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.model.datatable.OrderHistoryModel;
import net.awolf.businesstrade.model.response.OrderHistoryListModel;

import org.w3c.dom.Text;

/**
 * Created by zhaohai on 2017/10/14.
 */

public class OrderHistoryListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    OrderHistoryListModel orderHistoryList;

    public OrderHistoryListAdapter(Context context,OrderHistoryListModel orderHistoryList)
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
        View rootView = inflater.inflate(R.layout.view_buyhistory_item, null);

        OrderHistoryModel orderHistory=orderHistoryList.get(i);

        /*  商户名称    */
        TextView tv_merchant_name=(TextView)rootView.findViewById(R.id.tv_merchant_name);
        tv_merchant_name.setText(orderHistory.getMerchant_name());

        /*  订单列表    */
        TextView tv_orderlist=(TextView)rootView.findViewById(R.id.tv_orderlist);
        tv_orderlist.setText(orderHistory.getOrder_list());

        /*  订单编号    */
        TextView tv_orderid=(TextView)rootView.findViewById(R.id.tv_orderid);
        tv_orderid.setText(orderHistory.getOrder_id());

        /*  联系电话    */
        TextView tv_merchant_contact=(TextView)rootView.findViewById(R.id.tv_merchant_contact);
        tv_merchant_contact.setText(orderHistory.getMerchant_contact());

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
