package net.awolf.businesstrade.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ta.TAActivity;
import com.ta.annotation.TAInjectView;

import net.awolf.businesstrade.R;
import net.awolf.businesstrade.adapter.ChatListAdapter;
import net.awolf.businesstrade.method.IMethodCallback;
import net.awolf.businesstrade.method.Method;
import net.awolf.businesstrade.controller.Chat;
import net.awolf.businesstrade.controller.UserInfo;
import net.awolf.businesstrade.model.local.ChatItem;
import net.awolf.businesstrade.model.response.UserinfoModel;
import net.awolf.businesstrade.popwindow.ImageSource;
import net.awolf.businesstrade.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Activity_ChatActivity extends TAActivity {
    private final String TAG = "Activity_ChatActivity";
    private static Activity_ChatActivity instance = null;

    public static Activity_ChatActivity getInstance() {
        return instance;
    }

    private ImageSource imageSourcePopWindow;

    private String fileName = "";
    private File tempFile;
    private int crop = 300;// 裁剪大小
    private static final int OPEN_CAMERA_CODE = 10;
    private static final int OPEN_GALLERY_CODE = 11;
    private static final int CROP_PHOTO_CODE = 12;

    @TAInjectView(id = R.id.btnOrder)
    private Button btnNewOrder;

    @TAInjectView(id = R.id.etChat)
    private EditText etChat;

    @TAInjectView(id = R.id.btnSend)
    private Button btnSend;

    @TAInjectView(id = R.id.tvTalker)
    private TextView tvTalker;

    @TAInjectView(id = R.id.lvChatList)
    private ListView lvChatList;

    @TAInjectView(id = R.id.ibImage)
    private ImageButton ibImage;

    @TAInjectView(id = R.id.ibVoice)
    private ImageButton ibVoice;

    private int talkerType = 0;
    private String talkerName = "";
    private int talkerID = 0;

    private String userToken = UserInfo.getInstance().getUserToken();
    private int userID = UserInfo.getInstance().getUserID();

    ChatListAdapter chatListAdapter = null;

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public void onBackPressed() {
        Chat.getInstance().endChat();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadChat();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.makeToast("OnStart");
//        loadChat();
    }

    @Override
    protected void onAfterSetContentView() {
        loadChat();
    }

    private void loadChat()
    {
        talkerType = Chat.getInstance().getTalkerType();
        talkerName = Chat.getInstance().getTalkerName();
        talkerID = Chat.getInstance().getTalkerUserID();

        tvTalker.setText("正在与" + talkerName + "聊天");

        imageSourcePopWindow = new ImageSource(Activity_ChatActivity.this, itemsOnClick);

        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (talkerType == 0) {
                    doActivity(R.string.NewOrderActivity);
                } else {
                    doActivity(R.string.NewCargoOrderActivity);
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etChat.getText().toString();

                if (content.isEmpty()) {
                    Utils.makeToast("请输入聊天内容");
                    return;
                }

                SendChat(content);
            }
        });

        ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSourcePopWindow.showAtLocation(getInstance().findViewById(R.id.etChat),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        ibVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChatVoice();
            }
        });

        chatListAdapter = new ChatListAdapter(this, Chat.getInstance().newChatList());
        lvChatList.setAdapter(chatListAdapter);
        Chat.getInstance().beginChat(chatListAdapter);
        chatListAdapter.setMyName("我");
        chatListAdapter.setTalkerName(talkerName);


    }

    private void SendImage()
    {

    }
    private void SendVoice()
    {

    }
    private void SendChat(final String content) {
        showProgress();
        Method.getInstance().sendChat(userToken, talkerID, userID, content, new IMethodCallback() {
            @Override
            public void onSuccesse(Object o) {
                Utils.makeToast((String) o);
                Chat.getInstance().addRightChat(content);
                etChat.getText().clear();
            }

            @Override
            public void onServerFailure(String response) {

            }

            @Override
            public void onUnfulfil(String unfulfilMessage) {

            }

            @Override
            public void onException(String errorMessage) {

            }

            @Override
            public void onFinish() {
                hideProgress();
            }
        });
    }

    public void addRightChat(String content) {
        addChatItem(1, content);
    }

    public void addLeftChat(String content) {
        addChatItem(0, content);
    }

    private void addChatItem(int direction, String content) {
        if (chatListAdapter != null) {
            ChatItem chat = new ChatItem();
            chat.setDirection(direction);
            chat.setContent(content);
            chatListAdapter.addChat(chat);
        }
    }

    //TODO: 临时添加
    private void addChatVoice()
    {
        if (chatListAdapter != null) {
            ChatItem chat = new ChatItem();
            chat.setDirection(ChatItem.ChatDirection.RIGHT);
            chat.setContent(null);
            chat.setChatType(ChatItem.ChatType.VOICE);
            chatListAdapter.addChat(chat);
        }
    }

    //TODO: 临时添加
    private void addImageChat()
    {
        if (chatListAdapter != null) {
            ChatItem chat = new ChatItem();
            chat.setDirection(ChatItem.ChatDirection.RIGHT);
            chat.setContent(null);
            chat.setChatType(ChatItem.ChatType.IMAGE);
            chat.setImageFile(fileName);
            chatListAdapter.addChat(chat);
        }
    }

    /*
    准备图片文件
     */
    private void initImageFile()
    {
        fileName=Utils.getTempImageFile();
        tempFile=new File(fileName);
    }

    /**
     * 调用相机
     */
    private void openCamera() {
        Utils.getCameraPermission(getInstance());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 打开相机
        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, OPEN_CAMERA_CODE);
    }

    /**
     * 打开相册
     */
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, OPEN_GALLERY_CODE);
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("output", Uri.fromFile(tempFile));
        intent.putExtra("crop", true);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", crop);
//        intent.putExtra("outputY", crop);
        startActivityForResult(intent, CROP_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1)
            return;

        switch (requestCode) {
            case OPEN_CAMERA_CODE:
                cropPhoto(Uri.fromFile(tempFile));
                break;
            case OPEN_GALLERY_CODE:
                cropPhoto(data.getData());
                break;
            case CROP_PHOTO_CODE:
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

                    if (bitmap != null) {
//                        iv_user_photo.setImageBitmap(bitmap);
//                        CommonUitl.sharedPreferences(context, AppConstants.USER_PHOTO, fileName);
                        addImageChat();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageSourcePopWindow.dismiss();

            switch (v.getId()) {
                case R.id.popupwindow_Button_Camera:
                    Utils.makeToast("Camera");
                    initImageFile();
                    openCamera();
                    break;
                case R.id.popupwindow_Button_Gallery:
                    Utils.makeToast("Gallery");
                    initImageFile();
                    openGallery();
                    break;
                case R.id.popupwindow_Button_Cancel:
                    imageSourcePopWindow.dismiss();
                    break;
            }

        }

    };
}