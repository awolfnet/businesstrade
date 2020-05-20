package net.awolf.businesstrade.model.local;

import net.awolf.businesstrade.util.Utils;

/**
 * Created by zhaohai on 2017/10/25.
 */

public class ChatItem {
    /*
0:Left,1:Right
 */
    public int direction=0;

    public static class ChatDirection{
        public static final int LEFT=0;
        public static final int RIGHT=1;
    }
    public String content="";

    /*
    0:TEXT
    1:IMAGE
    2:VOICE
     */
    public int chatType=0;

    public static class  ChatType{
        public static final int TEXT=0;
        public static final int IMAGE=1;
        public static final int VOICE=2;
    }

    public String voiceFile="";
    public String imageFile="";

    public String chatTime="";

    public ChatItem()
    {
        chatTime= Utils.getNow("yyyy-MM-dd hh:mm");
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getVoiceFile() {
        return voiceFile;
    }

    public void setVoiceFile(String voiceFile) {
        this.voiceFile = voiceFile;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
}
