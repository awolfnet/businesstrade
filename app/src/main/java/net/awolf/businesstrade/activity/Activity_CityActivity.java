package net.awolf.businesstrade.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ta.TAActivity;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.app.BaseApplication;
import net.awolf.businesstrade.helper.AdCacheHelper;
import net.awolf.businesstrade.util.Utils;
import net.awolf.businesstrade.view.circlelibrary.ImageCycleView;

import java.util.ArrayList;

public class Activity_CityActivity extends TAActivity {

    /** 滑动广告 */
    private ImageCycleView imageCycleView;

    private boolean isExit=false;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);

        imageCycleView = (ImageCycleView)findViewById(R.id.cycleView);
        initCarsuelView(AdCacheHelper.getInstance().getDescList(), AdCacheHelper.getInstance().getAdList());
    }

    /** 初始化轮播图的关键方法 */
    private void initCarsuelView(ArrayList<String> imageDescList, ArrayList<String> urlList) {
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        imageCycleView.setLayoutParams(cParams);
        ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                /** 实现点击事件 */
                String url= AdCacheHelper.getInstance().getHrefList().get(position).toString();
                Utils.openURL(BaseApplication.getContext(),url);
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                /** 在此方法中，显示图片，可以用自己的图片加载库，也可以用本demo中的（Imageloader） */
                AdCacheHelper.getInstance().loadImage(imageURL, imageView);
            }
        };
        /** 设置数据 */
        imageCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
        imageCycleView.startImageCycle();
    }

    @Override
    public void onBackPressed() {
        if (isExit == true) {
            endApp();
        }

        isExit = true;
        Utils.makeToast("再次按下返回将退出");

        new Handler().postDelayed(new Runnable() {
            public void run() {
                isExit = false;
            }
        }, 2000);
    }

    public void endApp() {
        Activity_CityActivity.this.exitApp();
    }

}
