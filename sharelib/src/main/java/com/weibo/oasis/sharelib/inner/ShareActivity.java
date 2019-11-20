package com.weibo.oasis.sharelib.inner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import com.google.gson.Gson;
import com.weibo.oasis.sharelib.Callback;
import com.weibo.oasis.sharelib.ShareEntry;

import java.io.Serializable;

public class ShareActivity extends Activity {
    public static final String DATA_KEY = "third_part_share_data";
    public static final String FINISH_ACTION = "finish_action";
    public static final String FINISH_CODE = "result_key";
    private final int REQUEST_CODE = 2345;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent origin = getIntent();
        if (origin == null) {
            finishWithCode(Callback.DATA_NULL_SDK);
            return;
        }

        Serializable data = origin.getSerializableExtra(DATA_KEY);
        Intent intent = new Intent("OASIS.SHARE.PUBLISH.ACTION", Uri.parse("oasicshare://share.medias?" + DATA_KEY + "=" + new Gson().toJson(data)));
        intent.putExtra(DATA_KEY, data);
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (Exception e) {
            finishWithCode(Callback.OASIS_ACTIVITY_NOT_FOUND);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent result) {
        if (result != null && requestCode == REQUEST_CODE) {
            finishWithCode(result.getIntExtra(FINISH_CODE, Callback.DATA_BACK_NULL_SDK));
        } else {
            finish();
        }
    }

    private void finishWithCode(int code) {
        ShareEntry.onShareFinish(this, code);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        ShareEntry.forceUnRegisterReceiver(this);
    }
}
