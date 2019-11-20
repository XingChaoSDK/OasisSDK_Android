package com.weibo.oasis.sharelib.inner.entity;

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
        this.title = title;
        this.content = content;
        this.images = images;
        isImage = true;
    }

    public ShareBundle(String title, String content, String video) {
        this.title = title;
        this.content = content;
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
}
