package com.weibo.oasis.sharelib;


import androidx.annotation.Keep;

@Keep
public interface Callback {
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

    int OASIS_VERSION_TOO_OLD = 4; // 绿洲版本过低
    int PUBLISH_BUSY = 3; // 绿洲可能正在发布一个未完，此时来的分享发布
    int PUBLISH_SAVE_DRAFT = 2; // 用户选择了保持草稿
    int PUBLISH_CANCEL = 1; // 用户取消发布
    int PUBLISH_SUCCESS = 0; // 发布成功

    void onFinish(int code);
}
