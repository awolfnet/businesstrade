package net.awolf.businesstrade.method;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ta.util.TALogger;
import com.ta.util.http.RequestParams;


import net.awolf.businesstrade.exception.ModelException;
import net.awolf.businesstrade.exception.ServerException;
import net.awolf.businesstrade.exception.UnfulfilException;
import net.awolf.businesstrade.http.HttpClient;
import net.awolf.businesstrade.http.IHttpCallBack;
import net.awolf.businesstrade.model.request.AccountRegisterInfo;
import net.awolf.businesstrade.model.request.CargoOrderDetailModel;
import net.awolf.businesstrade.model.request.ChatContent;
import net.awolf.businesstrade.model.request.MerchantOrderDetailModel;
import net.awolf.businesstrade.model.request.ReachedMessageList;
import net.awolf.businesstrade.model.response.AdListModel;
import net.awolf.businesstrade.model.response.CargoFavoriteListModel;
import net.awolf.businesstrade.model.response.CargoListModel;
import net.awolf.businesstrade.model.response.CargoOrderHistoryListModel;
import net.awolf.businesstrade.model.response.CurrentVersionModel;
import net.awolf.businesstrade.model.response.ErrorModel;
import net.awolf.businesstrade.model.response.MerchantFavoriteListModel;
import net.awolf.businesstrade.model.response.MarketListModel;
import net.awolf.businesstrade.model.response.MerchantListModel;
import net.awolf.businesstrade.model.response.MessageListModel;
import net.awolf.businesstrade.model.response.OrderHistoryListModel;
import net.awolf.businesstrade.model.response.ProductListModel;
import net.awolf.businesstrade.model.response.ResponseModel;
import net.awolf.businesstrade.model.response.UserLoginModel;
import net.awolf.businesstrade.model.response.UserinfoModel;
import net.awolf.businesstrade.util.Const;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Work on 2017/8/5.
 */

public class Method {
    private static final String TAG = "Method";

    private String methodHost;
    private String methodPath;
    private String methodExtention;

    private static final String METHOD_USERLOGIN="UserLogin";
    private static final String METHOD_USERREGIESTER="CreateAccount";
    private static final String METHOD_KEEPONLINE="PullMessage";
    private static final String METHOD_GETADLIST="GetAdList";
    private static final String METHOD_GETMARKETLIST="GetMarketList";
    private static final String METHOD_GETPRODUCTLIST="GetProductList";
    private static final String METHOD_GETMERCHANTLIST="GetMerchantList";
    private static final String METHOD_GETCARGOLIST="GetCargoList";
    private static final String METHOD_GETMERCHANTFAVORITELIST="GetMerchantFavoriteList";
    private static final String METHOD_ADDMERCHANTTOFAVORITE="AddMerchantToFavorite";
    private static final String METHOD_ADDCARGOTOFAVORITE="AddCargoToFavorite";
    private static final String METHOD_GETCARGOFAVORITELIST="GetCargoFavoriteList";
    private static final String METHOD_NEWMERCHANTORDER="NewMerchantOrder";
    private static final String METHOD_NEWCARGOORDER="NewCargoOrder";
    private static final String METHOD_GETBUYHISTORY="GetBuyHistory";
    private static final String METHOD_PULLMESSAGE="PullMessage";
    private static final String METHOD_SENDCHAT="SendChat";
    private static final String METHOD_GETSELLHISTORY="GetSellHistory";
    private static final String METHOD_GETCHATHISTORY="GetChatHistory";
    private static final String METHOD_READMESSAGE="ReadMessage";
    private static final String METHOD_GETBUYCARGOHISTORY="GetBuyCargoHistory";
    private static final String METHOD_GETSELLCARGOHISTORY="GetSellCargoHistory";
    private static final String METHOD_GETUSERINFO="GetUserinfo";
    private static final String METHOD_GETCURRENTVERSION="GetCurrentVersion";

    private static Method mInstance=new Method(Const.Config.HOST,Const.Config.METHOD_PATH,Const.Config.EXTENTION);

    private HttpClient http;

    public static Method getInstance()
    {
        if(mInstance==null )
            mInstance=new Method(Const.Config.HOST,Const.Config.METHOD_PATH,Const.Config.EXTENTION);
        return mInstance;
    }

    public Method(String methodHost,String methodPath,String methodExtention)
    {
        http=new HttpClient();
        this.methodHost=methodHost;
        this.methodPath=methodPath;
        this.methodExtention=methodExtention;
    }

    private void setMethod(String method)
    {
        http.setURL(this.methodHost+this.methodPath+method+this.methodExtention);
    }

    /*
    拆解HTTP报文
     */
    public String dismantlehtHTTPResponse(String HTTPResponse) throws JSONException, ServerException, UnfulfilException {
        JSONObject jsonObj=null;
        boolean responseSuccess;
        String responesMessage="";
        int responseCode=0;

        try
        {
            jsonObj=new JSONObject(HTTPResponse);
            responseSuccess=jsonObj.getBoolean("success");
            responseCode=jsonObj.getInt("code");
            responesMessage=jsonObj.getString("message");

            switch (responseCode)
            {
                case Const.ResponseCode.SUCCESS:
                {
                    break;
                }
                case Const.ResponseCode.FAIL:
                {
                    ErrorModel errorModel=new Gson().fromJson(responesMessage,ErrorModel.class);
                    throw new ServerException(errorModel.getErrorCode(),errorModel.getErrorMessage());
                }
                case Const.ResponseCode.UNFULFIL:
                {
                    throw new UnfulfilException(responesMessage);
                }
                case Const.ResponseCode.EXCEPTION:
                {
                    TALogger.e(TAG,"Server exception.");
                    TALogger.d(TAG,"HTTP response string:" + HTTPResponse);
                    throw new ServerException(responseCode, "服务器异常");
                }

                default:throw new ServerException(Const.ResponseCode.UNKNOW, "未知服务器错误");
            }

        }catch(JSONException e)
        {
            TALogger.e(TAG, "Server returns JSON syntax exception.");
            TALogger.d(TAG,"HTTP response string:" + HTTPResponse);
            throw(e);
        } catch (ServerException e) {
            TALogger.e(TAG,"Server returns failed.");
            TALogger.d(TAG,"Server response string:" + responesMessage);
            throw(e);
        } catch (UnfulfilException e) {
            TALogger.e(TAG,"Server returns unfulfil.");
            TALogger.d(TAG,"Server response string:" + responesMessage);
            throw(e);
        }

        return responesMessage;
    }

    /*
    解析数据模型
     */
    public <T>T dismantleModel(String responseMessage,Type typeOfT) throws ModelException {

        ResponseModel<T> res=null;
        try
        {
            res=new Gson().fromJson(responseMessage, typeOfT);

        }catch(JsonSyntaxException e)
        {
            TALogger.e(TAG, "Convert response json to class was failed.");
            TALogger.d(TAG,"Response json:"+responseMessage);
            throw new ModelException("Convert response json to class was failed.");
        }

        return res.getMessage();
    }

    public <T>T dismantleResponse(String response, Type typeOfT) throws ModelException, ServerException, JSONException
    {
        JSONObject jsonObj=null;
        String errorMessage="";
        int responseCode=0;

        try
        {
            jsonObj=new JSONObject(response);
            responseCode=jsonObj.getInt("code");
            if(responseCode!= Const.ResponseCode.SUCCESS)
            {
                errorMessage=jsonObj.getString("message");

                throw new ServerException(responseCode,errorMessage);
            }

        }catch(JSONException e)
        {
            TALogger.e(TAG, "Json syntax exception.");
            TALogger.e(TAG,"Response string:" + response);
            throw(e);
        }


        ResponseModel<T> res=null;
        try
        {
            res=new Gson().fromJson(response, typeOfT);

        }catch(JsonSyntaxException e)
        {
            TALogger.e(TAG, "Convert response json to class was failed.");
            TALogger.e(TAG,"Response json:"+response);
            throw new ModelException("Convert response json to class was failed.");
        }

        return res.getMessage();
    }

    /*
    新用户注册
     */
    public void userRegister(String account,String nickname,String password,String mail,final IMethodCallback callback)
    {
        setMethod(METHOD_USERREGIESTER);

        AccountRegisterInfo registerInfo=new AccountRegisterInfo();
        registerInfo.setAccount_name(account);
        registerInfo.setAccount_nickname(nickname);
        registerInfo.setAccount_password(password);
        registerInfo.setAccount_mail(mail);

        String json=new Gson().toJson(registerInfo);

        RequestParams params = new RequestParams();
        params.put("model", json);

        try {
            http.postRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        UserLoginModel userLogin=dismantleModel((String)o,new TypeToken<ResponseModel<UserLoginModel>>(){}.getType());
                        callback.onSuccesse(userLogin);
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }
                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        } catch (Exception e) {
            callback.onException(e.getMessage());
        }
    }

    /*
    用户登录
     */
    public void userLogin(String account,String password,final IMethodCallback callback)
    {
        setMethod(METHOD_USERLOGIN);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("password", password));

        try {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        UserLoginModel userLogin=dismantleModel((String)o,new TypeToken<ResponseModel<UserLoginModel>>(){}.getType());
                        callback.onSuccesse(userLogin);
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }
                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        } catch (Exception e) {
            callback.onException(e.getMessage());
        }
    }

    /*
    维持在线
     */
    public void keepOnline(String token,final IMethodCallback callback)
    {
        setMethod(METHOD_KEEPONLINE);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        try {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        String responseMessage=dismantlehtHTTPResponse((String)o);
                        TALogger.d(TAG,responseMessage);
                        callback.onSuccesse(responseMessage);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }
                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        } catch (Exception e) {
            callback.onException(e.getMessage());
        }
    }

    /*
    获取广告列表
     */
    public void getAdList(int pageLevel,final IMethodCallback callback)
    {
        setMethod(METHOD_GETADLIST);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pagelevel", pageLevel+""));


        try {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        AdListModel adList=dismantleModel((String)o,new TypeToken<ResponseModel<AdListModel>>(){}.getType());
                        callback.onSuccesse(adList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }
                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        } catch (Exception e) {
            callback.onException(e.getMessage());
        }
    }

    /*
    获取市场列表
     */
    public void getMarketList(String token,String marketLocation,String marketType,final IMethodCallback callback)
    {
        setMethod(METHOD_GETMARKETLIST);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("location", marketLocation));
        params.add(new BasicNameValuePair("type", marketType));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        MarketListModel marketList=dismantleModel((String)o,new TypeToken<ResponseModel<MarketListModel>>(){}.getType());
                        callback.onSuccesse(marketList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取货物列表
     */
    public void getProductList(String token,String marketType,final IMethodCallback callback)
    {
        setMethod(METHOD_GETPRODUCTLIST);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", marketType));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        ProductListModel productList=dismantleModel((String)o,new TypeToken<ResponseModel<ProductListModel>>(){}.getType());
                        callback.onSuccesse(productList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取商户列表
     */
    public void getMerchantList(String token,String productName,String marketName,final IMethodCallback callback)
    {
        setMethod(METHOD_GETMERCHANTLIST);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("product_name", productName));
        params.add(new BasicNameValuePair("market_name", marketName));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        MerchantListModel merchantList=dismantleModel((String)o,new TypeToken<ResponseModel<MerchantListModel>>(){}.getType());
                        callback.onSuccesse(merchantList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }



    /*
    获取车辆列表
     */
    public void getCargoList(String token,String marketName,String cargoDescription,final IMethodCallback callback)
    {
        setMethod(METHOD_GETCARGOLIST);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("market_name", marketName));
        params.add(new BasicNameValuePair("cargo_description", cargoDescription));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        CargoListModel cargoList=dismantleModel((String)o,new TypeToken<ResponseModel<CargoListModel>>(){}.getType());
                        callback.onSuccesse(cargoList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    添加商户到收藏夹
     */
    public void addMerchantToFavorite(String token,String merchantId,final IMethodCallback callback)
    {
        setMethod(METHOD_ADDMERCHANTTOFAVORITE);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("merchant_id", merchantId));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        String responseMessage=dismantlehtHTTPResponse((String)o);
                        callback.onSuccesse(responseMessage);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }
                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    添加车辆到收藏夹
     */
    public void addCargoToFavorite(String token,String cargoID,final IMethodCallback callback)
    {
        setMethod(METHOD_ADDCARGOTOFAVORITE);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("cargo_id", cargoID));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        String responseMessage=dismantlehtHTTPResponse((String)o);
                        callback.onSuccesse(responseMessage);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }
                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取商户收藏夹列表
     */
    public void getMerchantFavoriteList(String token,final IMethodCallback callback)
    {
        setMethod(METHOD_GETMERCHANTFAVORITELIST);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        MerchantFavoriteListModel favoriteList=dismantleModel((String)o,new TypeToken<ResponseModel<MerchantFavoriteListModel>>(){}.getType());
                        callback.onSuccesse(favoriteList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取车辆收藏夹
     */
    public void getCargoFavoriteList(String token,final IMethodCallback callback)
    {
        setMethod(METHOD_GETCARGOFAVORITELIST);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        CargoFavoriteListModel cargoFavoriteList=dismantleModel((String)o,new TypeToken<ResponseModel<CargoFavoriteListModel>>(){}.getType());
                        callback.onSuccesse(cargoFavoriteList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    下商户订单
     */
    public void newMerchantOrder(String token,int merchantID, int buyerID, String buyList, String buyerComment, BigDecimal amount,int amountType,final IMethodCallback callback)
    {
        setMethod(METHOD_NEWMERCHANTORDER);

        MerchantOrderDetailModel orderDetail=new MerchantOrderDetailModel();

        orderDetail.amount=amount;
        orderDetail.amounttype=amountType;
        orderDetail.buyerid=buyerID;
        orderDetail.comment=buyerComment;
        orderDetail.merchantid=merchantID;
        orderDetail.orderdate=null;
        orderDetail.orderlist=buyList;

        String json=new Gson().toJson(orderDetail);

        RequestParams params = new RequestParams();

        params.put("model", json);
        params.put("token",token);

        try
        {
            http.postRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        String OrderID=dismantleModel((String)o,new TypeToken<ResponseModel<String>>(){}.getType());
                        callback.onSuccesse(OrderID);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }


    /*
    下车辆订单
     */
    public void newCargoOrder(String token,int cargoID, int buyerID, String buyerComment, BigDecimal amount,final IMethodCallback callback)
    {
        setMethod(METHOD_NEWCARGOORDER);

        CargoOrderDetailModel orderDetail=new CargoOrderDetailModel();

        orderDetail.amount=amount;
        orderDetail.cargoid=cargoID;
        orderDetail.buyerid=buyerID;
        orderDetail.comment=buyerComment;
        orderDetail.orderdate=null;

        String json=new Gson().toJson(orderDetail);

        RequestParams params = new RequestParams();

        params.put("model", json);
        params.put("token",token);

        try
        {
            http.postRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        String OrderID=dismantleModel((String)o,new TypeToken<ResponseModel<String>>(){}.getType());
                        callback.onSuccesse(OrderID);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取购买历史记录列表
     */
    public void getBuyHistoryList(String token,final IMethodCallback callback)
    {
        setMethod(METHOD_GETBUYHISTORY);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        OrderHistoryListModel orderHistoryList=dismantleModel((String)o,new TypeToken<ResponseModel<OrderHistoryListModel>>(){}.getType());
                        callback.onSuccesse(orderHistoryList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取销售历史记录列表
     */
    public void getSellHistoryList(String token,final IMethodCallback callback)
    {
        setMethod(METHOD_GETSELLHISTORY);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        OrderHistoryListModel orderHistoryList=dismantleModel((String)o,new TypeToken<ResponseModel<OrderHistoryListModel>>(){}.getType());
                        callback.onSuccesse(orderHistoryList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    拉取消息
     */
    public void pullMessage(String token, ReachedMessageList messageList, final IMethodCallback callback)
    {
        setMethod(METHOD_PULLMESSAGE);

        String json=new Gson().toJson(messageList);

        RequestParams params = new RequestParams();

        params.put("model", json);
        params.put("token",token);

        try
        {
            http.postRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        MessageListModel messageList=dismantleModel((String)o,new TypeToken<ResponseModel<MessageListModel>>(){}.getType());
                        callback.onSuccesse(messageList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }

    }

    /*
    发送聊天内容
     */
    public void sendChat(String token,int userID,int sourceID,String chatContent,final IMethodCallback callback)
    {
        setMethod(METHOD_SENDCHAT);

        ChatContent chat=new ChatContent();

        chat.userid=userID;
        chat.content=chatContent;

        String json=new Gson().toJson(chat);

        RequestParams params = new RequestParams();

        params.put("model", json);
        params.put("token",token);

        try
        {
            http.postRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        String response=dismantleModel((String)o,new TypeToken<ResponseModel<String>>(){}.getType());
                        callback.onSuccesse(response);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取聊天记录
    */
    public void getChatHistory(String token, int talkerID, final IMethodCallback callback)
    {
        setMethod(METHOD_GETCHATHISTORY);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("talker_id",talkerID+""));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        MessageListModel messageList=dismantleModel((String)o,new TypeToken<ResponseModel<MessageListModel>>(){}.getType());
                        callback.onSuccesse(messageList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }

    }

    /*
    标记消息为已读
    */
    public void readMessage(String token, ReachedMessageList messageList, final IMethodCallback callback)
    {
        setMethod(METHOD_READMESSAGE);

        String json=new Gson().toJson(messageList);

        RequestParams params = new RequestParams();

        params.put("model", json);
        params.put("token",token);

        try
        {
            http.postRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        MessageListModel messageList=dismantleModel((String)o,new TypeToken<ResponseModel<MessageListModel>>(){}.getType());
                        callback.onSuccesse(messageList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }

    }

    /*
    获取购买运输历史记录列表
    */
    public void getBuyCargoHistoryList(String token,final IMethodCallback callback)
    {
        setMethod(METHOD_GETBUYCARGOHISTORY);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        CargoOrderHistoryListModel orderHistoryList=dismantleModel((String)o,new TypeToken<ResponseModel<CargoOrderHistoryListModel>>(){}.getType());
                        callback.onSuccesse(orderHistoryList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取销售运输历史记录列表
     */
    public void getSellCargoHistoryList(String token,final IMethodCallback callback)
    {
        setMethod(METHOD_GETSELLCARGOHISTORY);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        try
        {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        CargoOrderHistoryListModel orderHistoryList=dismantleModel((String)o,new TypeToken<ResponseModel<CargoOrderHistoryListModel>>(){}.getType());
                        callback.onSuccesse(orderHistoryList);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }

                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        }catch (Exception ex)
        {
            callback.onException(ex.getMessage());
        }
    }

    /*
    获取指定用户信息
     */
    public void getUserinfo(String token,int userID,final IMethodCallback callback)
    {
        setMethod(METHOD_GETUSERINFO);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("user_id", userID+""));

        try {
            http.getRequest(params, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        UserinfoModel userInfo=dismantleModel((String)o,new TypeToken<ResponseModel<UserinfoModel>>(){}.getType());
                        callback.onSuccesse(userInfo);
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }
                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        } catch (Exception e) {
            callback.onException(e.getMessage());
        }
    }

    /*
    获取当前发布版本
     */
    public void getCurrentVersion(final IMethodCallback callback)
    {
        setMethod(METHOD_GETCURRENTVERSION);

        try {
            http.getRequest(null, new IHttpCallBack() {
                @Override
                public void onSuccesse(Object o) {
                    try {
                        dismantlehtHTTPResponse((String)o);
                        CurrentVersionModel currentVersion=dismantleModel((String)o,new TypeToken<ResponseModel<CurrentVersionModel>>(){}.getType());
                        callback.onSuccesse(currentVersion);
                    } catch (ServerException e) {
                        callback.onServerFailure(e.getMessage());
                    } catch (JSONException e) {
                        callback.onException(e.getMessage());
                    } catch (ModelException e) {
                        callback.onException(e.getMessage());
                    } catch (UnfulfilException e) {
                        callback.onUnfulfil(e.getMessage());
                    }
                }

                @Override
                public void onException(String errorMessage) {
                    callback.onException(errorMessage);
                }

                @Override
                public void onFinish() {
                    callback.onFinish();
                }
            });
        } catch (Exception e) {
            callback.onException(e.getMessage());
        }
    }
}
