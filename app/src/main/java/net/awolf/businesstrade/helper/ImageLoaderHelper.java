package net.awolf.businesstrade.helper;

import android.content.Context;
import android.widget.ImageView;

import com.ta.TAApplication;
import com.ta.util.bitmap.TABitmapCacheWork;
import com.ta.util.bitmap.TABitmapCallBackHanlder;
import com.ta.util.bitmap.TADownloadBitmapHandler;
import com.ta.util.extend.draw.DensityUtils;

import net.awolf.businesstrade.R;

/**
 * 图片加载工具类
 * 
 * @author：Jorge on 2015/10/13 12:14
 */
public class ImageLoaderHelper {

	private TABitmapCacheWork imageFetcher;

	public ImageLoaderHelper(Context context, TAApplication application)
	{
		TADownloadBitmapHandler downloadBitmapFetcher = new TADownloadBitmapHandler(context, DensityUtils.dipTopx(context, 128), DensityUtils.dipTopx(context, 128));
		TABitmapCallBackHanlder taBitmapCallBackHanlder = new TABitmapCallBackHanlder();
		taBitmapCallBackHanlder.setLoadingImage(context, R.drawable.empty_photo);
		imageFetcher = new TABitmapCacheWork(context);
		imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
		imageFetcher.setCallBackHandler(taBitmapCallBackHanlder);
		imageFetcher.setFileCache(application.getFileCache());
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @param imageView
	 */
	public void loadImage(String url, ImageView imageView) {
		url = url.trim();
		imageFetcher.loadFormCache(url, imageView);
	}
}
