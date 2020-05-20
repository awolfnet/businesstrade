package net.awolf.businesstrade.http;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ta.TASyncHttpClient;
import com.ta.util.TALogger;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;


import net.awolf.businesstrade.R;
import net.awolf.businesstrade.model.response.ResponseModel;

import org.apache.http.NameValuePair;
import org.apache.http.conn.scheme.Scheme;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;


public class HttpClient {

	public enum PostType
	{
		JSON(0,"json"),MODEL(1,"model");

		private String name;
		private int index;

		private PostType(int index,String name)
		{
			this.name = name;
			this.index = index;
		}
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	private static final String TAG = "HttpClient";
	
	
	private TASyncHttpClient syncHttpClient=null;
	private AsyncHttpClient asyncHttpClient=null;

	private String mURL="";

	public void setURL(String mAccessURL) {
		this.mURL = mAccessURL;
	}

	public HttpClient(String URL)
	{
		syncHttpClient = new TASyncHttpClient();
		asyncHttpClient=new AsyncHttpClient();


		setURL(URL);
	}

	public HttpClient()
	{
		syncHttpClient = new TASyncHttpClient();	
		asyncHttpClient=new AsyncHttpClient();
	}

	public String getRequest(List<NameValuePair> getParameters) throws UnsupportedEncodingException
	{
		TALogger.d(TAG,"HTTP get request start.");
		String query="";
		
		if (getParameters!=null)
		{
			query="?";
			int i=0;
			for(i=0;i<getParameters.size();i++)
			{
				String key= URLEncoder.encode(getParameters.get(i).getName(), "utf-8");
				String value= URLEncoder.encode(getParameters.get(i).getValue(), "utf-8");
				
				query+= String.format("%s=%s&", key,value);
			}
			
			query+="time="+ System.currentTimeMillis();
		}
		
		String url=mURL+query;
		TALogger.d(TAG,"Get url:\""+url+"\"");
		
		String content=syncHttpClient.get(url);

		return content;
	}
	public void getRequest(List<NameValuePair> getParameters, final IHttpCallBack callback)
	{
		TALogger.d(TAG,"HTTP get request start.");
		String query="";
		
		if (getParameters!=null)
		{
			query="?";
			int i=0;
			for(i=0;i<getParameters.size();i++)
			{
				try
				{
					String key= URLEncoder.encode(getParameters.get(i).getName(), "utf-8");
					String value= URLEncoder.encode(getParameters.get(i).getValue(), "utf-8");
					query+= String.format("%s=%s&", key,value);
				}catch(Exception e)
				{
					callback.onException("URL参数编码错误");
					return;
				}
			}
			
			query+="time="+ System.currentTimeMillis();
		}
		
		String url=mURL+query;
		TALogger.d(TAG,"Get url:\""+url+"\"");
		
		asyncHttpClient.get(url, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
					TALogger.d(TAG,"Response:\""+content+"\"");
					callback.onSuccesse(content);
			}
			@Override
			public void onFailure(Throwable error) {
				callback.onException(error.getMessage());
			}
			@Override
			public void onFinish() {
				callback.onFinish();
			}

		});

		return;
		
	}
	public String postRequest(String postData,PostType postType) throws UnsupportedEncodingException
	{
		RequestParams params = new RequestParams();
		
		//String postData = new String(json.getBytes(Charset.defaultCharset()),"gbk");
		//String postData=new String(json.getBytes("gbk"),"utf-8");
		//String postData=new String(json.getBytes(),"utf-8");
		//String postData=URLEncoder.encode(json, "utf-8");

		params.put(postType.toString(),postData);
		TALogger.d(TAG, "Post(" + postType.toString() + "):\"" + postData + "\"");
		return this.postRequest(params);
	}
	public void postRequest(String postData,PostType postType, final IHttpCallBack callback)
	{
		RequestParams params = new RequestParams();

		params.put(postType.toString(), postData);
		TALogger.d(TAG, "Post(" + postType.toString() + "):\"" + postData + "\"");
		this.postRequest(params,callback);
		return;
	}
	
	public String postRequest(RequestParams params)
	{
		TALogger.d(TAG,"HTTP post request start.");
		TALogger.d(TAG,"Post url:\""+ mURL+"\"");

		String content = syncHttpClient.post(mURL,params);
		return content;
	}
	
	public void postRequest(RequestParams params,final IHttpCallBack callback)
	{
		TALogger.d(TAG,"HTTP post request start.");
		TALogger.d(TAG,"Post url:\""+ mURL+"\"");

		asyncHttpClient.post(mURL, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
					TALogger.d(TAG,"Response:\""+content+"\"");
					callback.onSuccesse(content);
			}
			@Override
			public void onFailure(Throwable error) {
				callback.onException(error.getMessage());
			}
			@Override
			public void onFinish() {
				callback.onFinish();
			}
			
		});	
		
		return;
	}
	

	
	private String getErrorResponse(String response)
	{
		ResponseModel<String> res=null;
		try
		{
			res=new Gson().fromJson(response, new TypeToken<ResponseModel<String>>(){}.getType());
			
		}catch(Exception e)
		{

		}
		return res.getMessage();
	}
//
//	private void loadCertificate(Context context)
//	{
//		InputStream ins = null;
//		String result = "";
//		SSLSocketFactory socketFactory=null;
//		try {
//			ins = context.getAssets().open("app_pay.cer"); //下载的证书放到项目中的assets目录中
//
//		CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
//			Certificate cer = cerFactory.generateCertificate(ins);
//			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
//			keyStore.load(null, null);
//			keyStore.setCertificateEntry("trust", cer);
//
//			socketFactory = new SSLSocketFactory(keyStore);
//
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (CertificateException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (KeyStoreException e) {
//			e.printStackTrace();
//		} catch (NoSuchProviderException e) {
//			e.printStackTrace();
//		}
//
//		asyncHttpClient.setSSLSocketFactory(socketFactory);
//	}
//
//	private void disableSSLCheck(Context context)
//	{
//		try {
//		String certStr = context.getString(R.string.caApi);
//		X509Certificate ca = SecurityHelper.readCert(certStr);
//
//		TrustManagerFactory tmf = null;
//
//			tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//
//		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//		ks.load(null);
//		ks.setCertificateEntry("caCert", ca);
//
//		tmf.init(ks);
//
//		SSLContext sslContext = SSLContext.getInstance("TLS");
//		sslContext.init(null, tmf.getTrustManagers(), null);
//		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (CertificateException e) {
//			e.printStackTrace();
//		} catch (KeyStoreException e) {
//			e.printStackTrace();
//		} catch (KeyManagementException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
