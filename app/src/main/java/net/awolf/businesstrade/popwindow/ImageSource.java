package net.awolf.businesstrade.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import net.awolf.businesstrade.R;

/**
 * Created by zhaohai on 2018/1/2.
 */

public class ImageSource extends PopupWindow {
    private static final String TAG = "ImageSource";

    private View mView;
    public Button btnCamera;
    public Button btnGallery;
    public Button btnCancel;

    public ImageSource(Activity context,View.OnClickListener itemsOnClick) {
        super(context);

        Log.i(TAG, "ImageSource方法已被调用");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.view_popwindow_imagesource, null);

        btnCamera = (Button) mView.findViewById(R.id.popupwindow_Button_Camera);
        btnGallery = (Button) mView.findViewById(R.id.popupwindow_Button_Gallery);
        btnCancel = (Button) mView.findViewById(R.id.popupwindow_Button_Cancel);

        // 设置按钮监听
        btnCamera.setOnClickListener(itemsOnClick);
        btnGallery.setOnClickListener(itemsOnClick);
        btnCancel.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "取消项目");
                dismiss();
            }
        });

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);

        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

}
