package net.awolf.businesstrade.model.response;

import net.awolf.businesstrade.util.Utils;

/**
 * Created by zhaohai on 2018/2/6.
 */

public class CurrentVersionModel {
    public int VersionCode;
    public String VersionName;
    public String DownloadURL;
    public boolean ForceUpdate;

    public int getVersionCode() {
        return VersionCode;
    }
    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }
    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getDownloadURL() {
        String url= Utils.base64Decode(DownloadURL);
        return url;
    }
    public void setDownloadURL(String downloadURL) {
        DownloadURL = downloadURL;
    }

    public boolean isForceUpdate() {
        return ForceUpdate;
    }
    public void setForceUpdate(boolean forceUpdate) {
        ForceUpdate = forceUpdate;
    }
}
