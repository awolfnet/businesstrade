package net.awolf.businesstrade.activity;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.Merchant;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.datatable.MerchantFavoriteModel;
import net.awolf.businesstrade.model.datatable.MerchantModel;
import net.awolf.businesstrade.util.CashierInputFilter;
import net.awolf.businesstrade.util.Utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class Activity_NewOrderActivity extends TAActivity {

    @TAInjectView(id = R.id.etMerchantName)
    private EditText etMerchantName;

    @TAInjectView(id = R.id.etMerchantPhone)
    private EditText etMerchantPhone;

    @TAInjectView(id = R.id.etBuyerPhone)
    private EditText etBuyerPhone;

    @TAInjectView(id = R.id.etOrderDate)
    private EditText etOrderDate;

    @TAInjectView(id = R.id.etList)
    private EditText etList;

    @TAInjectView(id = R.id.etComment)
    private EditText etComment;

    @TAInjectView(id = R.id.etAmount)
    private EditText etAmount;

    @TAInjectView(id = R.id.switchAmount)
    private Switch switchAmount;

    @TAInjectView(id = R.id.btnNewOrder)
    private Button btnNewOrder;

    @TAInjectView(id = R.id.btnCancel)
    private Button btnCancel;

    private String userToken=UserInfo.getInstance().getUserToken();

    private MerchantModel merchantInfo=null;
    private MerchantFavoriteModel merchantFavoriteInfo=null;

    @Override
    protected void onAfterSetContentView() {
        merchantInfo = Merchant.GetInstance().getMerchant();
        merchantFavoriteInfo=Merchant.GetInstance().getMerchantFavorite();

        if(merchantInfo!=null)
        {
            etMerchantName.setText(merchantInfo.getMerchant_name());
            etMerchantPhone.setText(merchantInfo.getMerchant_contact());
        }

        if(merchantFavoriteInfo!=null)
        {
            etMerchantName.setText(merchantFavoriteInfo.getMerchant_name());
            etMerchantPhone.setText(merchantFavoriteInfo.getMerchant_contact());
        }

        etBuyerPhone.setText(UserInfo.getInstance().getUserName());
        etOrderDate.setText(Utils.getDateString("yyyy年MM月dd日"));

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
                int merchantID=0;
                if(merchantInfo!=null)
                {
                    merchantID=merchantInfo.getMerchantid();
                }

                if(merchantFavoriteInfo!=null)
                {
                    merchantID=merchantFavoriteInfo.getMerchant_id();
                }

                int buyerID= UserInfo.getInstance().getUserID();
                String buyList=etList.getText().toString();
                String buyerComment=etComment.getText().toString();
                BigDecimal amount=new BigDecimal(etAmount.getText().toString());
                int amountType=0;
                if(switchAmount.isChecked())
                {
                    amountType=1;
                }else
                {
                    amountType=0;
                }
                CreateNewMerchantOrder(merchantID,buyerID,buyList,buyerComment,amount,amountType);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void CreateNewMerchantOrder(int merchantID, int buyerID, String buyList, String buyerComment, BigDecimal amount,int amountType)
    {
        showProgress();
        Method.getInstance().newMerchantOrder(userToken, merchantID, buyerID, buyList, buyerComment, amount, amountType, new IMethodCallback() {
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
