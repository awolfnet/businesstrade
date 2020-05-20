package net.awolf.businesstrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.model.datatable.MerchantFavoriteModel;
import net.awolf.businesstrade.model.datatable.MerchantModel;
import net.awolf.businesstrade.model.response.MerchantFavoriteListModel;
import net.awolf.businesstrade.model.response.MerchantListModel;

/**
 * Created by Work on 2017/8/18.
 */

public class MerchantFavoriteListAdapter extends BaseAdapter{
    LayoutInflater inflater;

    private MerchantButtonOnClick merchantButtonOnClickListenerCallBack;

    private MerchantFavoriteListModel merchantList=null;

    public MerchantFavoriteListAdapter(Context context, MerchantFavoriteListModel marketList)
    {
        inflater = LayoutInflater.from(context);
        this.merchantList=marketList;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return merchantList.size();
    }

    public Object getItem(int position) {
        return merchantList.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // 将写好的xml文件转化成一个view
        View view = inflater.inflate(R.layout.view_merchant_item, null);

        final int merchant_index=position;
        /*  商户名 */
        TextView tv_merchant_name=(TextView)view.findViewById(R.id.tv_merchant_name);
        tv_merchant_name.setText(merchantList.get(position).getMerchant_name());

        /*  商户描述    */
        TextView tv_merchant_description=(TextView)view.findViewById(R.id.tv_merchant_description);
        tv_merchant_description.setText(merchantList.get(position).getMerchant_description());

        /*  商户联系方式  */
        TextView tv_merchant_contact=(TextView)view.findViewById(R.id.tv_merchant_contact);
        tv_merchant_contact.setText(merchantList.get(position).getMerchant_contact());
        tv_merchant_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MerchantFavoriteModel merchantInfo=merchantList.get(merchant_index);
                merchantButtonOnClickListenerCallBack.btnDialer_OnClick(merchantInfo);
            }
        });

        /*  商户网址    */
        TextView tv_merchant_website=(TextView)view.findViewById(R.id.tv_merchant_website);
        tv_merchant_website.setText(merchantList.get(position).getMerchant_website());

        /*  交谈  */
        Button btnChat=(Button)view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MerchantFavoriteModel merchantInfo=merchantList.get(merchant_index);
                merchantButtonOnClickListenerCallBack.btnChat_OnClick(merchantInfo);
            }
        });

        /*  收藏  */
        Button btnFavorite=(Button)view.findViewById(R.id.btnFavorite);
        btnFavorite.setVisibility(View.GONE);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MerchantFavoriteModel merchantInfo=merchantList.get(merchant_index);
                merchantButtonOnClickListenerCallBack.btnFavorite_OnClick(merchantInfo);
            }
        });

        return view;
    }

    public void setMerchantButtonOnClickListenerCallBack(MerchantButtonOnClick merchantButtonOnClickListenerCallBack) {
        this.merchantButtonOnClickListenerCallBack = merchantButtonOnClickListenerCallBack;
    }

    public interface MerchantButtonOnClick
    {
        public void btnChat_OnClick(MerchantFavoriteModel merchantInfo);
        public void btnFavorite_OnClick(MerchantFavoriteModel merchantInfo);
        public void btnDialer_OnClick(MerchantFavoriteModel merchantInfo);
        public void btnWebsite_OnClick(MerchantFavoriteModel merchantInfo);
    }
}
