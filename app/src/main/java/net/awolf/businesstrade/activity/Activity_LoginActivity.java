package net.awolf.businesstrade.activity;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;
import com.ta.util.TALogger;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.response.UserLoginModel;
import net.awolf.businesstrade.util.Utils;

public class Activity_LoginActivity extends TAActivity {
    private static final String TAG="LoginActivity";

    private EditText loginAccount;
    private EditText loginPassword;

    @TAInjectView(id=R.id.register)
    private Button btnRegister;

private Button btnLogin;

    @Override
    protected void onAfterSetContentView() {
        super.onAfterSetContentView();
        loginAccount=(EditText)findViewById(R.id.login_account);
        loginPassword=(EditText)findViewById(R.id.login_password);
        btnLogin=(Button)findViewById(R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                String username=loginAccount.getText().toString();
                String password=loginPassword.getText().toString();

                //发起登录
                userLogin(username,password);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doActivity(R.string.RegisterActivity);

            }
        });
    }

    protected void userLogin(String username,String password)
    {

        Method.getInstance().userLogin(username, password, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                UserLoginModel userLogin=(UserLoginModel)o;
                UserInfo.getInstance().setUserName(userLogin.getUsername());
                UserInfo.getInstance().setUserToken(userLogin.getToken());
                UserInfo.getInstance().setUserID(userLogin.getAccountID());

                UserInfo.getInstance().save();
                TALogger.d(TAG,"Userinfo saved.");

                //跳转到主界面
                doActivity(R.string.MainActivity);
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
                hideProgress();
            }
        });
    }

}
