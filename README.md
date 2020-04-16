# 绿洲分享SDK——Android

支持向绿洲应用分享图片或视频的SDK

## SDK有哪些功能？

- 分享视频单个视频（超过60秒会被自动裁剪前60秒）
- 分享多张图片（超过12张将会取前面12张）

## 有哪些在注意点？

- 未下载绿洲将会启动下载引导页
- 未登录将会绿洲的登录流程，原先带进来的图片或视频会丢弃


## 接入步骤：

1. 依赖：

项目根目录的build.gradle文件中，allprojects如下加：

```gradle
    allprojects {
        repositories {
             maven { url "https://raw.githubusercontent.com/XingChaoSDK/OasisSDK_Android/master" } // 用于androidx版本
             //或：maven { url "https://raw.githubusercontent.com/XingChaoSDK/OasisSDK_Android/support" }// 用于不支持androidx低版本
        }
    }
```


App目录下的build.gradle文件中，加依赖：

```gradle
    implementation 'com.sina.oasis.sdk:share:1.0.6' // 用于androidx
    //或：implementation 'com.sina.oasis.sdk:share:1.0.6' // 用于不支持androidx版本
```

2. 混淆: 暂无

3. AndroidManifest.xml加：ShareActivity

4. Java:

```Java

    //准备参数和全局实例
    FixParam fixParam = new FixParam.Builder(getApplicationContext())
           .appKey("这里是你的AppKey")
           .build();
    ShareEntry entry = new ShareEntry(fixParam);

    //随时分享
    entry.shareImages("title图片", "content", imagesList,
                     new Callback() {
                         @Override
                         public void onFinish(int code) {}
                     });

    entry.shareVideo("发视频标题", "视频时候内容", videoPath,
                               new Callback() {
                                   @Override
                                   public void onFinish(int code) {}
                               });

```

## 返回码说明

    // 100 以下是和iOS保持一致的，其他为安卓端特有
    int OASIS_ACTIVITY_NOT_FOUND = 160; // 跳转绿洲失败
    int DATA_NULL_SDK = 153; // SDK没收到数据
    int DATA_NULL_PUBLISH = 152; // 发布时候没有数据

    int DATA_BACK_NULL_SDK = 150; // SDK内部未返回数据
    int DATA_BACK_NULL_OASIS = 60; // 绿洲未返回数据

    int DATA_NULL_OASIS = 51; // 绿洲收不到数据
    int DATA_INVALID_SDK = 50; // 无效数据，比如，给的文件路径找不到文件
    int NO_LOGIN = 30; // 未登录，走绿洲登录流程，传过来的数据丢失
    int NO_AUTH = 20; // 授权失败
    int NO_INSTALL = 10; // 未安装

    int INTERNAL_ERROR = 3; // 内部出错，如：绿洲可能正在发布一个未完，此时来的分享发布
    int PUBLISH_SAVE_DRAFT = 2; // 用户选择了保持草稿
    int PUBLISH_CANCEL = 1; // 用户取消发布
    int PUBLISH_SUCCESS = 0; // 发布成功


使用样例参见：Sample.java

Change Log:

support 版本：
1.0.6: fix share bug
1.0.5: 更改获取应用信息方式（美颜支持）