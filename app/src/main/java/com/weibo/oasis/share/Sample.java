package com.weibo.oasis.share;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weibo.oasis.sharelib.Callback;
import com.weibo.oasis.sharelib.ChangableParam;
import com.weibo.oasis.sharelib.FixParam;
import com.weibo.oasis.sharelib.ShareEntry;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;


import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Sample extends Activity {
    private static final int REQUEST_CODE_IMAGE = 123;
    private static final int REQUEST_CODE_VIDEO = 456;

    private ShareEntry shareEntry;
    private final String BeautyCameraKey = "wm=90137_90001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe();

        View image = findViewById(R.id.image);
        View video = findViewById(R.id.video);

        image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Matisse.from(Sample.this)
                                .choose(MimeType.ofImage())
                                .countable(true)
                                .maxSelectable(13)
                                .thumbnailScale(0.85f)
                                .forResult(REQUEST_CODE_IMAGE);
                    }
                });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(Sample.this)
                        .choose(MimeType.ofVideo())
                        .countable(true)
                        .maxSelectable(2)
                        .thumbnailScale(0.85f)
                        .forResult(REQUEST_CODE_VIDEO);
            }
        });

        shareEntry = getInstance();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_IMAGE) {
                List<Uri> uris = Matisse.obtainResult(data);

                shareEntry.shareImages(((EditText) findViewById(R.id.title)).getText().toString(),
                        ((EditText) findViewById(R.id.content)).getText().toString(),
                        uris,
                        new Callback() {
                            @Override
                            public void onFinish(int code) {
                                Toast.makeText(Sample.this, "finish with:" + code, Toast.LENGTH_LONG).show();
                            }
                        });
            } else if (requestCode == REQUEST_CODE_VIDEO) {
                List<Uri> uris = Matisse.obtainResult(data);
                if (uris == null || uris.isEmpty()) return;
                shareEntry.shareVideo(((EditText) findViewById(R.id.title)).getText().toString(),
                        ((EditText) findViewById(R.id.content)).getText().toString(),
                        uris.get(0),
                        new Callback() {
                            @Override
                            public void onFinish(int code) {
                                Toast.makeText(Sample.this, "finish with:" + code, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }


    private ShareEntry getInstance() {
        FixParam fixParam = new FixParam.Builder(getApplicationContext())
                .appKey(BeautyCameraKey)// "这里是你的AppKey"
                .build();

        // 这是可能一直变化着的参数，需要实时获取的，可选
        ChangableParam changableParam = new ChangableParam() {
            @Override
            public double getLongitude() {
                return 0;
            }

            @Override
            public double getLatitude() {
                return 0;
            }
        };
        return new ShareEntry(fixParam, changableParam);
    }
}
