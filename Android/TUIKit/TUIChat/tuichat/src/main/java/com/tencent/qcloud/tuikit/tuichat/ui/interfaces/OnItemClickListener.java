package com.tencent.qcloud.tuikit.tuichat.ui.interfaces;

import android.view.View;

import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;

public interface OnItemClickListener {
    void onMessageLongClick(View view, int position, TUIMessageBean messageInfo);

    default void onMessageClick(View view, int position, TUIMessageBean messageInfo) {};

    void onUserIconClick(View view, int position, TUIMessageBean messageInfo);

    void onUserIconLongClick(View view, int position, TUIMessageBean messageInfo);

    default void onReplyMessageClick(View view, int position, String originMsgId) {}

    default void onSendFailBtnClick(View view, int position, TUIMessageBean messageInfo) {};
}
