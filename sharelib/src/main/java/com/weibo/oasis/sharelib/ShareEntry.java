package com.weibo.oasis.sharelib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;


import com.weibo.oasis.sharelib.inner.ShareActivity;
import com.weibo.oasis.sharelib.inner.UriUtil;
import com.weibo.oasis.sharelib.inner.Util;
import com.weibo.oasis.sharelib.inner.entity.ShareBundle;

import java.util.ArrayList;
import java.util.List;

public class ShareEntry {
    private static final String OASIS_PACKAGE_NAME = "com.sina.oasis";
    private final String TAG = "ShareEntry";
    private FixParam fixParam;
    private ChangableParam changableParam;

    public ShareEntry(@NonNull FixParam fixParam) {
        this(fixParam, null);
    }

    public ShareEntry(@NonNull FixParam fixParam, @Nullable ChangableParam changableParam) {
        this.changableParam = changableParam;
        this.fixParam = fixParam;
        checkParam();
    }

    private void checkParam() {
        if (fixParam == null) throw new RuntimeException("fixParam can not null");
        if (fixParam.getContext() == null) throw new RuntimeException("context can not null");
    }

    public void shareVideo(String title, String content, @NonNull Uri video, @NonNull Callback callback) {
        shareVideo(title, content, UriUtil.getPath(fixParam.getContext(), video), callback);
    }

    public void shareVideo(String title, String content, @NonNull String path, @NonNull Callback callback) {
        share(new ShareBundle(title, content, path), callback);
    }

    public void shareImagesByPath(String title, String content, @NonNull List<String> images, @NonNull Callback callback) {
        share(new ShareBundle(title, content, images), callback);
    }

    public void shareImages(String title, String content, @NonNull List<Uri> images, @NonNull Callback callback) {
        List<String> paths = new ArrayList<>();
        for (Uri uri : images) paths.add(UriUtil.getPath(fixParam.getContext(), uri));
        shareImagesByPath(title, content, paths, callback);
    }

    private void share(ShareBundle bundle, Callback callback) {
        if (callback == null) throw new RuntimeException("callback can not null");
        if (!hasInstalled()) {
            startDownloadH5();
            callback.onFinish(Callback.NO_INSTALL);
            return;
        }

        if (getOASISVersion(fixParam.getContext()) < 39) {
            callback.onFinish(Callback.OASIS_VERSION_TOO_OLD);
            return;
        }

        if (!hasAuth()) {
            callback.onFinish(Callback.NO_AUTH);
            return;
        }

        if (!validContent(bundle)) {
            callback.onFinish(Callback.DATA_INVALID_SDK);
            return;
        }

        Intent intent = new Intent(fixParam.getContext(), ShareActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ShareActivity.DATA_KEY, bundle
                .setAppKey(fixParam.getAppKey())
                .setPackageName(fixParam.getContext().getPackageName()));
        try {
            fixParam.getContext().startActivity(intent);
            registerShareFinishReceiver(fixParam.getContext(), callback);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Have you put %s.ShareActivity in AndroidManifest.xml?", getClass().getPackage().getName()));
        }
    }

    private void registerShareFinishReceiver(Context context, final Callback callback) {
        IntentFilter filter = new IntentFilter();
        final String ACTION = ShareActivity.FINISH_ACTION;
        filter.addAction(ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                if (intent == null) return;
                int code = intent.getIntExtra(ShareActivity.FINISH_CODE, Callback.DATA_BACK_NULL_SDK);
                Log.i(TAG, "Receive code:" + code);
                if (!ACTION.equals(intent.getAction())) return;
                if (FORCE_UN_REGISTER_RETURN == code) return;
                callback.onFinish(code);
            }
        }, filter);
    }

    private static int FORCE_UN_REGISTER_RETURN = 7;

    public static void forceUnRegisterReceiver(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ShareActivity.FINISH_ACTION)
                .putExtra(ShareActivity.FINISH_CODE, FORCE_UN_REGISTER_RETURN));
    }

    public static void onShareFinish(Context context, int code) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ShareActivity.FINISH_ACTION)
                .putExtra(ShareActivity.FINISH_CODE, code));
    }


    private void startDownloadH5() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://oasis.weibo.cn/v1/h5/download?appkey=" + fixParam.getAppKey()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            fixParam.getContext().startActivity(intent);
        } catch (Exception e) {
        }
    }

    private boolean validContent(ShareBundle bundle) {
        if (bundle == null) return false;
        if (bundle.isImage()) {
            Util.filter(bundle.getImages());
            return ((bundle.getImages() != null) && (bundle.getImages().size() > 0));
        }
        return Util.validLocalFile(bundle.getVideo());
    }

    private boolean hasAuth() {
        return true;
    }

    private long getOASISVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(OASIS_PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS);
            if (info == null) return -1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return info.getLongVersionCode();
            } else {
                return info.versionCode;
            }
        } catch (Throwable e) {
            return -2;
        }
    }

    private boolean hasInstalled() {
        return Util.isPkgInstalled(fixParam.getContext(), OASIS_PACKAGE_NAME);
    }

    public int getSdkVersion() {
        return BuildConfig.VERSION_CODE;
    }
}
