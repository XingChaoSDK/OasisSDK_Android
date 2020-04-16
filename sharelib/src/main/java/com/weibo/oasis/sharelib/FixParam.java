package com.weibo.oasis.sharelib;

import android.content.Context;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public class FixParam {
    private final Context context;
    private final String appKey;
    // todo 主题，位置，同步微博能否传入，界面一致吗

    public FixParam(Builder builder) {
        context = builder.context;
        appKey = builder.appKey;
    }

    public Context getContext() {
        return context;
    }

    public String getAppKey() {
        return appKey;
    }


    public final static class Builder {
        private Context context;
        private String appKey;

        public Builder(@NonNull Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        public Builder appKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        public FixParam build() {
            return new FixParam(this);
        }
    }
}
