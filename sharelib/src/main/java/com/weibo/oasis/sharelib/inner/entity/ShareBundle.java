package com.weibo.oasis.sharelib.inner.entity;

import android.net.Uri;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;

@Keep
public class ShareBundle implements Serializable {
    private static final long serialVersionUID = 1L;
    private final boolean isImage;
    private String appKey;
    private String title;
    private String content;
    private String packageName;
    private List<String> images;
    private String video;

    public ShareBundle(String title, String content, List<String> images) {
        this.title = Uri.encode(title);
        this.content = Uri.encode(content);
        this.images = images;
        isImage = true;
    }

    public ShareBundle(String title, String content, String video) {
        this.title = Uri.encode(title);
        this.content = Uri.encode(content);
        this.video = video;
        isImage = false;
    }

    public ShareBundle setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public ShareBundle setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public boolean isImage() {
        return isImage;
    }

    public List<String> getImages() {
        return images;
    }

    public String getVideo() {
        return video;
    }

    @Override
    public String toString() {
        return "ShareBundle{" +
                "isImage=" + isImage +
                ", appKey='" + appKey + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", packageName='" + packageName + '\'' +
                ", images=" + images +
                ", video='" + video + '\'' +
                '}';
    }
}
