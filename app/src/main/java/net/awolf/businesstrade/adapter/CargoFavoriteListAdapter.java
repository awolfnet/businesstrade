package net.awolf.businesstrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.model.datatable.CargoFavoriteModel;
import net.awolf.businesstrade.model.datatable.CargoModel;
import net.awolf.businesstrade.model.response.CargoFavoriteListModel;
import net.awolf.businesstrade.model.response.CargoListModel;

/**
 * Created by zhaohai on 2017/9/3.
 */

public class CargoFavoriteListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private CargoFavoriteListModel cargoList=null;

    private CargoButtonOnClick cargoButtonOnClickListenerCallBack;

    public CargoFavoriteListAdapter(Context context, CargoFavoriteListModel cargoList)
    {
        inflater = LayoutInflater.from(context);
        this.cargoList=cargoList;

    }

    @Override
    public int getCount() {
        return cargoList.size();
    }

    @Override
    public Object getItem(int i) {
        return cargoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 将写好的xml文件转化成一个view
        View contentView = inflater.inflate(R.layout.view_cargo_item, null);

        final int cargo_index=i;

        /*  车牌号 */
        TextView tv_cargo_plate=(TextView)contentView.findViewById(R.id.tv_cargo_plate);
        tv_cargo_plate.setText(cargoList.get(i).getCargo_plate());

        /*  车辆描述    */
        TextView tv_cargo_description=(TextView)contentView.findViewById(R.id.tv_cargo_description);
        tv_cargo_description.setText(cargoList.get(i).getCargo_description());

        /*  司机姓名  */
        TextView tv_cargo_driver=(TextView)contentView.findViewById(R.id.tv_cargo_driver);
        tv_cargo_driver.setText(cargoList.get(i).getCargo_driver());

        /*  司机联系方式  */
        TextView tv_cargo_contact=(TextView)contentView.findViewById(R.id.tv_cargo_contact);
        tv_cargo_contact.setText(cargoList.get(i).getDriver_contact());
        tv_cargo_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargoFavoriteModel cargoInfo=cargoList.get(cargo_index);
                cargoButtonOnClickListenerCallBack.btnDialer_OnClick(cargoInfo);
            }
        });

        /*  交谈  */
        Button btnChat=(Button)contentView.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargoFavoriteModel cargoInfo=cargoList.get(cargo_index);
                cargoButtonOnClickListenerCallBack.btnChat_OnClick(cargoInfo);
            }
        });

        /*  收藏  */
        Button btnFavorite=(Button)contentView.findViewById(R.id.btnFavorite);
        btnFavorite.setVisibility(View.GONE);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargoFavoriteModel cargoInfo=cargoList.get(cargo_index);
                cargoButtonOnClickListenerCallBack.btnFavorite_OnClick(cargoInfo);
            }
        });

        return contentView;
    }

    public void setCargoButtonOnClickListenerCallBack(CargoButtonOnClick cargoButtonOnClickListenerCallBack) {
        this.cargoButtonOnClickListenerCallBack = cargoButtonOnClickListenerCallBack;
    }

    public interface CargoButtonOnClick
    {
        public void btnChat_OnClick(CargoFavoriteModel cargoInfo);
        public void btnFavorite_OnClick(CargoFavoriteModel cargoInfo);
        public void btnDialer_OnClick(CargoFavoriteModel cargoInfo);
        public void btnWebsite_OnClick(CargoFavoriteModel cargoInfo);
    }
}
