package com.weibo.oasis.sharelib.inner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Util {

    public static void filter(List<String> files) {
        if (files == null) return;
        Iterator<String> iterator = files.iterator();
        while (iterator.hasNext()) {
            String p = iterator.next();
            if (!validLocalFile(p)) iterator.remove();
        }
    }

    public static boolean validLocalFile(String path) {
        if (path == null) return false;
        File file = new File(path);
        if (!file.exists()) return false;
        return file.length() > 0;
    }

    public static boolean hasMatchedActivity(Context context, Intent i) {
        if (i == null) return false;
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(i, 0);
        return !activities.isEmpty();
    }


    public static String getSignature(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature sign = info.signatures[0];
            return sign.toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static boolean isPkgInstalled(Context context, String pkgName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            return packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
