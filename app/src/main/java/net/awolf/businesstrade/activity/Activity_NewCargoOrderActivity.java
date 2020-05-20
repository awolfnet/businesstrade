package net.awolf.businesstrade.activity;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.Cargo;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.datatable.CargoFavoriteModel;
import net.awolf.businesstrade.model.datatable.CargoModel;
import net.awolf.businesstrade.util.CashierInputFilter;
import net.awolf.businesstrade.util.Utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class Activity_NewCargoOrderActivity extends TAActivity {

    @TAInjectView(id = R.id.etPlate)
    private EditText etPlate;

    @TAInjectView(id = R.id.etDriverName)
    private EditText etDriverName;

    @TAInjectView(id = R.id.etLicence)
    private EditText etLicence;

    @TAInjectView(id = R.id.etDriverPhone)
    private EditText etDriverPhone;

    @TAInjectView(id = R.id.etOrderDate)
    private EditText etOrderDate;

    @TAInjectView(id = R.id.etComment)
    private EditText etComment;

    @TAInjectView(id = R.id.etAmount)
    private EditText etAmount;

    @TAInjectView(id = R.id.btnNewOrder)
    private Button btnNewOrder;

    @TAInjectView(id = R.id.btnCancel)
    private Button btnCancel;

    private String userToken= UserInfo.getInstance().getUserToken();

    private CargoModel cargoInfo=null;
    private CargoFavoriteModel cargoFavoriteInfo=null;

    @Override
    protected void onAfterSetContentView() {
        cargoInfo= Cargo.getInstance().getCargo();
        cargoFavoriteInfo=Cargo.getInstance().getCargoFavorite();

        if(cargoInfo!=null)
        {
            etPlate.setText(cargoInfo.getCargo_plate());
            etDriverName.setText(cargoInfo.getCargo_driver());
            etLicence.setText(cargoInfo.getDriver_licence());
            etDriverPhone.setText(cargoInfo.getDriver_contact());
        }

        if(cargoFavoriteInfo!=null)
        {
            etPlate.setText(cargoFavoriteInfo.getCargo_plate());
            etDriverName.setText(cargoFavoriteInfo.getCargo_driver());
            etLicence.setText(cargoFavoriteInfo.getDriver_licence());
            etDriverPhone.setText(cargoFavoriteInfo.getDriver_contact());
        }

        etOrderDate.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new java.util.Date()));

        InputFilter[] filters = { new CashierInputFilter() };
        etAmount.setFilters(filters);

        etAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if(Integer.parseInt(etAmount.getText().toString())==0)
                    {
                        etAmount.getText().clear();
                    }
                }else
                {
                    if(etAmount.getText().length()==0)
                    {
                        etAmount.setText("0");
                    }
                }
            }
        });

        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cargoID=0;
                if(cargoInfo!=null)
                {
                    cargoID=cargoInfo.getCargo_id();
                }
                if(cargoFavoriteInfo!=null)
                {
                    cargoID=cargoFavoriteInfo.getCargo_id();
                }

                int buyerID= UserInfo.getInstance().getUserID();
                String buyerComment=etComment.getText().toString();
                BigDecimal amount=new BigDecimal(etAmount.getText().toString());
                CreateNewCargoOrder(cargoID,buyerID,buyerComment,amount);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    private void CreateNewCargoOrder(int cargoID, int buyerID, String buyerComment, BigDecimal amount)
    {
        showProgress();
        Method.getInstance().newCargoOrder(userToken, cargoID,buyerID, buyerComment, amount, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Utils.makeToast("下单成功，单号"+(String)o);
                onBackPressed();
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
}
