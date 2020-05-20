package net.awolf.businesstrade.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.controller.Market;
import net.awolf.businesstrade.controller.Messages;
import net.awolf.businesstrade.fragment.CityFragment;
import net.awolf.businesstrade.fragment.HomeFragment;
import net.awolf.businesstrade.fragment.MineFragment;
import net.awolf.businesstrade.helper.AdCacheHelper;
import net.awolf.businesstrade.model.datatable.MessageModel;
import net.awolf.businesstrade.util.Utils;
import net.awolf.businesstrade.view.Message.MessageView;
import net.awolf.businesstrade.view.circlelibrary.ImageCycleView;
import java.util.ArrayList;

public class Activity_MainActivity extends TAActivity {
    private final String TAG="Activity_MainActivity";
    private static Activity_MainActivity instance=null;

    public static final String DAEMON_WAKE_ACTION="Activity_MainActivity";

    /** 滑动广告 */
    private ImageCycleView imageCycleView;

    private boolean isExit=false;
    private boolean isTopActivity=false;

    @TAInjectView(id=R.id.messageBox)
    private MessageView messageView;

    @TAInjectView(id = R.id.ibShare)
    private ImageButton ibShare;


    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        instance=this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isTopActivity=true;

        //检查是否已经选择地区
        if(Market.getInstance().getLocation().isEmpty()==false)
        {
            showHomeFragment();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isTopActivity=false;
    }

    public static Activity_MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
        isTopActivity=true;

        //显示选择城市Fragment
        getFragmentManager().beginTransaction().replace(R.id.fl_content, new CityFragment()).commit();

//        if(Market.getInstance().getLocation().isEmpty()) {
//            showCityFragment();
//        }else
//        {
//            showHomeFragment();
//        }

        imageCycleView = (ImageCycleView)findViewById(R.id.cycleView);

        loadAdPage();

        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doActivity(R.string.MessageActivity);
            }
        });

//        MessageModel message=Messages.GetInstance().getLastMessage();
        messageView.showMessage(Messages.GetInstance().getLastMessage());


        ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareActivity();
            }
        });



    }

    public void Button_OnClick(View view)
    {
        showMarketActivity();
    }

    public void showCityFragment()
    {
        switchFragment(new CityFragment());
    }
    public void showHomeFragment()
    {
        switchFragment(new HomeFragment());
    }
    public void showMineFragment()
    {
        switchFragment(new MineFragment());
    }
    public void showMarketActivity()
    {
        switchActivity(R.string.MarketActivity);
    }
    public void showSellHistoryActivity()
    {
        switchActivity(R.string.SellHistoryActivity);
    }
    public void showBuyHistoryActivity()
    {
        switchActivity(R.string.BuyHistoryActivity);
    }
    public void showFavoriteActivity()
    {
        switchActivity(R.string.FavoriteActivity);
    }
    public void showCargoActivity()
    {
        switchActivity(R.string.CargoActivity);
    }
    public void showShareActivity()
    {
        switchActivity(R.string.ShareActivity);
    }
    public void showPublishActivity(){
        switchActivity(R.string.PublishActivity);
    }

    public boolean isTopActivity()
    {
        return this.isTopActivity;
    }

    public void showMessage(MessageModel message)
    {
        messageView.showMessage(message);
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
                Utils.openURL(Activity_MainActivity.instance,url);
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                /** 在此方法中，显示图片，可以用自己的图片加载库，也可以用本demo中的（Imageloader） */
                AdCacheHelper.getInstance().loadImage(imageURL, imageView);
            }
        };
        /** 设置数据 */
        imageCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
//        imageCycleView.startImageCycle();
    }

    public void switchFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.fl_content, fragment).addToBackStack(TAG).commit();
    }

    public void switchActivity(int activityResID) {
        imageCycleView.stopImageCycle();
        doActivity(activityResID);
    }

    public void loadAdPage()
    {

        initCarsuelView(AdCacheHelper.getInstance().getDescList(), AdCacheHelper.getInstance().getAdList());
    }

    @Override
    public void onBackPressed() {
        int fragmentCount = getFragmentManager().getBackStackEntryCount();

        if (fragmentCount > 0) {
            getFragmentManager().popBackStack();
            return;
        }

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
        Messages.GetInstance().stopPullingService(this);
        Activity_MainActivity.getInstance().exitApp();
    }

}
