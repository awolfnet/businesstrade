package net.awolf.businesstrade.helper;

import android.content.Context;
import android.widget.ImageView;

import com.ta.TAApplication;

import net.awolf.businesstrade.app.BaseApplication;
import net.awolf.businesstrade.model.datatable.AdModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdCacheHelper {
	private static AdCacheHelper mInstance = new AdCacheHelper(BaseApplication.getContext(), BaseApplication.getTAApplication());

	private Context mContext;
	private ImageLoaderHelper imageLoader;

	/** 装在数据的集合 文字描述 */
	private ArrayList<String> imageDescList;
	/** 装在数据的集合 图片地址 */
	private ArrayList<String> urlList;
	/** 装在数据的点击跳转地址 */
	private ArrayList<String> hrefList;

	public AdCacheHelper(Context context, TAApplication application) {
		mContext = context;
		imageLoader = new ImageLoaderHelper(context, application);

		imageDescList = new ArrayList<>();
		urlList = new ArrayList<>();
		hrefList = new ArrayList<>();
	}

	public static AdCacheHelper getInstance() {

		if (mInstance == null)
			mInstance = new AdCacheHelper(BaseApplication.getContext(), BaseApplication.getTAApplication());

		return mInstance;
	}

	public void setAdList(List<Map> adList) {
		for (Map map : adList) {
			urlList.add(map.get("source").toString());
			hrefList.add(map.get("link").toString());
			imageDescList.add("");
		}
	}

	public ArrayList<String> getAdList() {
		return urlList;
	}

	public ArrayList<String> getAdList(int pageLevel)
	{
		AdCacheDao adCacheDao=new AdCacheDao();
		ArrayList<AdModel> adList= adCacheDao.query(pageLevel);

		return null;
	}

	public ArrayList<String> getHrefList() {
		return hrefList;
	}

	public ArrayList<String> getDescList() {
		return imageDescList;
	}

	public void loadImage(String url, ImageView imageView) {
		imageLoader.loadImage(url, imageView);
	}
}
