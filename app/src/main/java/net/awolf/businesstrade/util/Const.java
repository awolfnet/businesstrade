package net.awolf.businesstrade.util;

public class Const {

	/*
	Host configuration
	 */
	public class Config
	{
		/*
		外部接口
		 */
		public static final String HOST="http://app.awolf.net:60000";
		public static final String METHOD_PATH="/method/";
		public static final String HOME="/";

		/*
		本地接口
		 */
//		public static final String HOST="http://192.168.27.5";
//		public static final String METHOD_PATH="/OliveFramework/method/";
//		public static final String HOME="/OliveFramework/";

		public static final String EXTENTION=".ashx";

		public static final String ADPAGE="AdPage.aspx";
		public static final String SHARE_PAGE="QRCodeShare.aspx";


	}

	/*
	Cache key
	 */
	public static final String PHONENUM="PHONENUM";
	public static final String PASSWORD="PASSWORD";
	public static final String REMEMBERME="REMEMBERME";
	public static final String TOKEN="TOKEN";
	public static final String SYSTEMCACHE="SYSTEMCACHE";
	public static final String USERID="USERID";

	public static final String UPDATE_APKFILE="businesstrade.apk";

	public class ResponseCode
	{
		public static final int SUCCESS=1;
		public static final int FAIL=2;
		public static final int UNFULFIL=3;
		public static final int EXCEPTION=4;
		public static final int UNKNOW=255;
	}
}
