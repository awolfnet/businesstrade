package net.awolf.businesstrade.activity;

import android.os.Bundle;
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

public class Activity_RegisterActivity extends TAActivity {
    private final String TAG="Activity_RegisterActivity";

    @TAInjectView(id = R.id.etAccount)
    private EditText etAccount;

    @TAInjectView(id = R.id.etNickName)
    private EditText etNickName;

    @TAInjectView(id = R.id.etMail)
    private EditText etMail;

    @TAInjectView(id = R.id.etPassword)
    private EditText etPassword;

    @TAInjectView(id = R.id.etRepassword)
    private EditText etRepasswod;

    @TAInjectView(id = R.id.btnRegister)
    private Button btnRegister;

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
    }

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accountName=etAccount.getText().toString();
                String accountNickname=etNickName.getText().toString();
                String accountMail=etMail.getText().toString();
                String accountPassword=etPassword.getText().toString();
                String accountPassword2=etRepasswod.getText().toString();

                if(checkUserInfo(accountName,accountNickname,accountPassword,accountPassword2)==false)
                {
                    return;
                }

                showProgress();
                Method.getInstance().userRegister(accountName, accountNickname,accountPassword, accountMail, new IMethodCallback() {
                    @Override
                    public void onSuccesse(Object o) {
                        Utils.makeToast("注册成功");
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
        });
    }

    private boolean checkUserInfo(String account,String nickname,String password,String password2)
    {
        if(account.isEmpty())
        {
            Utils.makeToast("手机号不能为空");
            return false;
        }

        if(Utils.isMobileNO(account)==false)
        {
            Utils.makeToast("手机号格式不正确");
            return false;
        }

        if(nickname.isEmpty())
        {
            Utils.makeToast("姓名不能为空");
            return false;
        }

        if(password.compareTo(password2)!=0)
        {
            Utils.makeToast("两次输入密码不一致");
            return false;
        }

        return true;
    }

}
