package com.tencent.qcloud.tuikit.tuicontact.ui.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.component.TitleBarLayout;
import com.tencent.qcloud.tuicore.component.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tuicore.component.interfaces.ITitleBarLayout;
import com.tencent.qcloud.tuicore.component.interfaces.IUIKitCallback;
import com.tencent.qcloud.tuicore.component.popupcard.PopupInputCard;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuicontact.R;
import com.tencent.qcloud.tuikit.tuicontact.bean.ContactItemBean;
import com.tencent.qcloud.tuikit.tuicontact.bean.FriendApplicationBean;
import com.tencent.qcloud.tuikit.tuicontact.bean.ContactGroupApplyInfo;
import com.tencent.qcloud.tuikit.tuicontact.bean.GroupInfo;
import com.tencent.qcloud.tuikit.tuicontact.presenter.FriendProfilePresenter;
import com.tencent.qcloud.tuicore.component.LineControllerView;
import com.tencent.qcloud.tuikit.tuicontact.ui.interfaces.IFriendProfileLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendProfileLayout extends LinearLayout implements View.OnClickListener, IFriendProfileLayout {

    private static final String TAG = FriendProfileLayout.class.getSimpleName();

    private final int CHANGE_REMARK_CODE = 200;

    private TitleBarLayout mTitleBar;
    private ImageView mHeadImageView;
    private TextView mNickNameView;
    private TextView mIDView;
    private TextView mSignatureView;
    private TextView mSignatureTagView;
    private TextView remarkAndGroupTip;
    private LineControllerView mRemarkView;
    private LineControllerView mAddBlackView;
    private LineControllerView mChatTopView;
    private LineControllerView mMessageOptionView;
    private LineControllerView addFriendRemarkLv;
    private LineControllerView addFriendGroupLv;
    private Button deleteFriendBtn;
    private Button chatBtn;
    private Button audioCallBtn;
    private Button videoCallBtn;
    private Button addFriendSendBtn;
    private Button acceptFriendBtn;
    private Button refuseFriendBtn;
    private View addFriendArea;
    private EditText addWordingEditText;
    private View friendApplicationVerifyArea;
    private TextView friendApplicationAddWording;

    private ContactItemBean mContactInfo;
    private FriendApplicationBean friendApplicationBean;
    private OnButtonClickListener mListener;
    private String mId;
    private String mNickname;
    private String mRemark;
    private String mAddWords;
    private boolean isFriend;
    private boolean isGroup = false;

    private FriendProfilePresenter presenter;

    public FriendProfileLayout(Context context) {
        super(context);
        init();
    }

    public FriendProfileLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FriendProfileLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPresenter(FriendProfilePresenter presenter) {
        this.presenter = presenter;
    }

    private void init() {
        inflate(getContext(), R.layout.contact_friend_profile_layout, this);

        mHeadImageView = findViewById(R.id.friend_icon);
        mNickNameView = findViewById(R.id.friend_nick_name);
        mIDView = findViewById(R.id.friend_account);
        mRemarkView = findViewById(R.id.remark);
        mRemarkView.setOnClickListener(this);
        mSignatureTagView = findViewById(R.id.friend_signature_tag);
        mSignatureView = findViewById(R.id.friend_signature);
        mMessageOptionView = findViewById(R.id.msg_rev_opt);
        mMessageOptionView.setOnClickListener(this);
        mChatTopView = findViewById(R.id.chat_to_top);
        mAddBlackView = findViewById(R.id.blackList);
        deleteFriendBtn = findViewById(R.id.btn_delete);
        deleteFriendBtn.setOnClickListener(this);
        chatBtn = findViewById(R.id.btn_chat);
        chatBtn.setOnClickListener(this);

        audioCallBtn = findViewById(R.id.btn_voice);
        audioCallBtn.setOnClickListener(this);
        videoCallBtn = findViewById(R.id.btn_video);
        videoCallBtn.setOnClickListener(this);

        addFriendSendBtn = findViewById(R.id.add_friend_send_btn);
        addFriendSendBtn.setOnClickListener(this);

        acceptFriendBtn = findViewById(R.id.accept_friend_send_btn);
        refuseFriendBtn = findViewById(R.id.refuse_friend_send_btn);

        addFriendArea = findViewById(R.id.add_friend_verify_area);
        addWordingEditText = findViewById(R.id.add_wording_edit);

        friendApplicationVerifyArea = findViewById(R.id.friend_application_verify_area);
        friendApplicationAddWording = findViewById(R.id.friend_application_add_wording);

        addFriendRemarkLv = findViewById(R.id.friend_remark_lv);
        addFriendRemarkLv.setOnClickListener(this);
        addFriendGroupLv = findViewById(R.id.friend_group_lv);
        addFriendGroupLv.setContent(getContext().getString(R.string.contact_my_friend));

        remarkAndGroupTip = findViewById(R.id.remark_and_group_tip);

        mTitleBar = findViewById(R.id.friend_titlebar);
        mTitleBar.setLeftIcon(R.drawable.contact_title_bar_back);
        mTitleBar.setTitle(getResources().getString(R.string.profile_detail), ITitleBarLayout.Position.MIDDLE);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
    }

    private void initEvent() {
        mChatTopView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if (presenter != null) {
                    presenter.setConversationTop(mId, isChecked);
                }
            }
        });

        mAddBlackView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addBlack();
                } else {
                    deleteBlack();
                }
            }
        });

        refuseFriendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refuse();
            }
        });

        acceptFriendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                accept();
            }
        });
    }

    public void initData(Object data) {
        initEvent();

        if (data instanceof String) {
            mId = (String) data;
            loadUserProfile(mId);
        } else if (data instanceof ContactItemBean) {
            setViewContentFromItemBean((ContactItemBean) data);
        } else if (data instanceof FriendApplicationBean) { // 从好友申请界面进入
            setViewContentFromFriendApplicationBean((FriendApplicationBean) data);
        } else if (data instanceof ContactGroupApplyInfo) {
            setViewContentFromContactGroupApplyInfo((ContactGroupApplyInfo) data);
        } else if (data instanceof GroupInfo) {
            setViewContentFromGroupInfo((GroupInfo) data);
        }

        if (!TextUtils.isEmpty(mNickname)) {
            mNickNameView.setText(mNickname);
        } else {
            mNickNameView.setText(mId);
        }
        mIDView.setText(mId);
    }

    private void setViewContentFromContactGroupApplyInfo(ContactGroupApplyInfo data) {
        final ContactGroupApplyInfo info = data;
        mId = info.getFromUser();
        mNickname = info.getFromUserNickName();
        mRemarkView.setVisibility(GONE);
        mAddBlackView.setVisibility(GONE);
        mMessageOptionView.setVisibility(GONE);
        deleteFriendBtn.setText(R.string.refuse);
        deleteFriendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refuseJoinGroupApply(info);
            }
        });
        chatBtn.setText(R.string.accept);
        chatBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptJoinGroupApply(info);
            }
        });
    }

    private void setViewContentFromGroupInfo(GroupInfo groupInfo) {
        mTitleBar.setTitle(getResources().getString(R.string.add_group), ITitleBarLayout.Position.MIDDLE);
        isGroup = true;
        mId = groupInfo.getId();
        mNickname = groupInfo.getGroupName();
        mSignatureTagView.setText(getResources().getString(R.string.contact_group_type_tag));
        mSignatureView.setText(groupInfo.getGroupType());
        int radius = getResources().getDimensionPixelSize(R.dimen.contact_profile_face_radius);
        GlideEngine.loadUserIcon(mHeadImageView, groupInfo.getFaceUrl(), R.drawable.default_group_icon, radius);
        addFriendSendBtn.setVisibility(VISIBLE);
        addFriendArea.setVisibility(VISIBLE);
        remarkAndGroupTip.setVisibility(GONE);
        addFriendRemarkLv.setVisibility(GONE);
        addFriendGroupLv.setVisibility(GONE);
    }

    private void setViewContentFromFriendApplicationBean(FriendApplicationBean data) {
        friendApplicationBean = data;
        mId = friendApplicationBean.getUserId();
        mNickname = friendApplicationBean.getNickName();
        mSignatureTagView.setVisibility(GONE);
        mSignatureView.setVisibility(GONE);
        mRemarkView.setVisibility(GONE);
        mAddBlackView.setVisibility(GONE);
        mMessageOptionView.setVisibility(GONE);

        friendApplicationVerifyArea.setVisibility(VISIBLE);
        friendApplicationAddWording.setText(friendApplicationBean.getAddWording());

        refuseFriendBtn.setVisibility(VISIBLE);
        acceptFriendBtn.setVisibility(VISIBLE);

        int radius = getResources().getDimensionPixelSize(R.dimen.contact_profile_face_radius);
        GlideEngine.loadUserIcon(mHeadImageView, friendApplicationBean.getFaceUrl(), radius);
    }

    private void setViewContentFromItemBean(ContactItemBean data) {
        mContactInfo = data;
        isFriend = mContactInfo.isFriend();
        mId = mContactInfo.getId();
        mNickname = mContactInfo.getNickName();
        mSignatureView.setText(mContactInfo.getSignature());
        int radius = getResources().getDimensionPixelSize(R.dimen.contact_profile_face_radius);
        GlideEngine.loadUserIcon(mHeadImageView, mContactInfo.getAvatarUrl(), radius);
        mChatTopView.setChecked(presenter.isTopConversation(mId));
        mAddBlackView.setChecked(mContactInfo.isBlackList());
        mRemarkView.setContent(mContactInfo.getRemark());

        // 是自己
        if (TextUtils.equals(mContactInfo.getId(), TUILogin.getLoginUser())) {
            // 添加了自己为好友
            if (isFriend) {
                mRemarkView.setVisibility(VISIBLE);
                chatBtn.setVisibility(VISIBLE);
                audioCallBtn.setVisibility(VISIBLE);
                videoCallBtn.setVisibility(VISIBLE);
                deleteFriendBtn.setVisibility(VISIBLE);
                mAddBlackView.setVisibility(VISIBLE);
                mChatTopView.setVisibility(View.VISIBLE);
                updateMessageOptionView();
            } else {
                // DO NOTHING
            }
        } else {
            if (mContactInfo.isBlackList()) {
                deleteFriendBtn.setVisibility(VISIBLE);
                chatBtn.setVisibility(VISIBLE);
                audioCallBtn.setVisibility(VISIBLE);
                videoCallBtn.setVisibility(VISIBLE);
                mRemarkView.setVisibility(VISIBLE);
                mAddBlackView.setVisibility(VISIBLE);
                mMessageOptionView.setVisibility(VISIBLE);
                mChatTopView.setVisibility(VISIBLE);
            } else {
                if (!isFriend) {
                    if (isGroup) {
                        addFriendRemarkLv.setVisibility(GONE);
                        addFriendGroupLv.setVisibility(GONE);
                    }
                    mTitleBar.setTitle(getResources().getString(R.string.add_friend), ITitleBarLayout.Position.MIDDLE);
                    addFriendSendBtn.setVisibility(VISIBLE);
                    addFriendArea.setVisibility(VISIBLE);
                } else {
                    mRemarkView.setVisibility(VISIBLE);
                    chatBtn.setVisibility(VISIBLE);
                    audioCallBtn.setVisibility(VISIBLE);
                    videoCallBtn.setVisibility(VISIBLE);
                    deleteFriendBtn.setVisibility(VISIBLE);
                    mAddBlackView.setVisibility(VISIBLE);
                    mChatTopView.setVisibility(View.VISIBLE);
                    updateMessageOptionView();
                }
            }
        }

    }

    private void updateMessageOptionView() {
        mMessageOptionView.setVisibility(VISIBLE);
        //get
        final ArrayList<String> userIdList = new ArrayList<>();
        userIdList.add(mId);

        presenter.getC2CReceiveMessageOpt(userIdList, new IUIKitCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                mMessageOptionView.setChecked(data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                mMessageOptionView.setChecked(false);
            }
        });

        //set
        mMessageOptionView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setC2CReceiveMessageOpt(userIdList, isChecked);
            }
        });
    }

    @Override
    public void onDataSourceChanged(ContactItemBean bean) {
        setViewContentFromItemBean(bean);

        if (bean.isFriend()) {
            updateMessageOptionView();
        }

        if (!TextUtils.isEmpty(mNickname)) {
            mNickNameView.setText(mNickname);
        } else {
            mNickNameView.setText(mId);
        }

        if (!TextUtils.isEmpty(bean.getAvatarUrl())) {
            int radius = getResources().getDimensionPixelSize(R.dimen.contact_profile_face_radius);
            GlideEngine.loadUserIcon(mHeadImageView, bean.getAvatarUrl(), radius);
        }
        mIDView.setText(mId);
    }

    private void loadUserProfile(String id) {
        final ContactItemBean bean = new ContactItemBean();
        presenter.getUsersInfo(id, bean);
    }

    private void accept() {
        presenter.acceptFriendApplication(friendApplicationBean, FriendApplicationBean.FRIEND_ACCEPT_AGREE_AND_ADD, new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                chatBtn.setText(R.string.accepted);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastShortMessage("accept Error code = " + errCode + ", desc = " + errMsg);
            }
        });
    }

    private void refuse() {
        presenter.refuseFriendApplication(friendApplicationBean, new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                deleteFriendBtn.setText(R.string.refused);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastShortMessage("refuse Error code = " + errCode + ", desc = " + errMsg);

            }
        });
    }

    public void acceptJoinGroupApply(final ContactGroupApplyInfo item) {
        presenter.acceptJoinGroupApply(item, new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                ((Activity) getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    public void refuseJoinGroupApply(final ContactGroupApplyInfo item) {
        presenter.refuseJoinGroupApply(item, "", new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                ((Activity) getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    private void delete() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add(mId);

        presenter.deleteFriend(identifiers, new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                if (mListener != null) {
                    mListener.onDeleteFriendClick(mId);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastShortMessage("deleteFriend Error code = " + errCode + ", desc = " + errMsg);
            }
        });
    }

    private void chat() {
        if (mListener != null || mContactInfo != null) {
            mListener.onStartConversationClick(mContactInfo);
        }
        ((Activity) getContext()).finish();
    }

    @Override
    public void onClick(View v) {
        if (v  == chatBtn) {
            chat();
        } else if (v == deleteFriendBtn) {
            delete();
        } else if (v == videoCallBtn) {
            Map<String, Object> map = new HashMap<>();
            map.put(TUIConstants.TUICalling.PARAM_NAME_USERIDS, new String[]{mId});
            map.put(TUIConstants.TUICalling.PARAM_NAME_TYPE, TUIConstants.TUICalling.TYPE_VIDEO);
            TUICore.callService(TUIConstants.TUICalling.SERVICE_NAME, TUIConstants.TUICalling.METHOD_NAME_CALL, map);
        }  else if (v == audioCallBtn) {
            Map<String, Object> map = new HashMap<>();
            map.put(TUIConstants.TUICalling.PARAM_NAME_USERIDS, new String[]{mId});
            map.put(TUIConstants.TUICalling.PARAM_NAME_TYPE, TUIConstants.TUICalling.TYPE_AUDIO);
            TUICore.callService(TUIConstants.TUICalling.SERVICE_NAME, TUIConstants.TUICalling.METHOD_NAME_CALL, map);
        } else if (v == addFriendSendBtn) {
            String addWording  = addWordingEditText.getText().toString();
            String friendGroup = "";
            String remark = addFriendRemarkLv.getContent();

            if (isGroup) {
                presenter.joinGroup(mId, addWording, new IUIKitCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        ToastUtil.toastShortMessage(getContext().getString(R.string.success));
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        ToastUtil.toastShortMessage(getContext().getString(R.string.contact_add_failed) + " " + errMsg);
                    }
                });
            } else {
                presenter.addFriend(mId, addWording, friendGroup, remark, new IUIKitCallback<Pair<Integer, String>>() {
                    @Override
                    public void onSuccess(Pair<Integer, String> data) {
                        ToastUtil.toastShortMessage(data.second);
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        ToastUtil.toastShortMessage(getContext().getString(R.string.contact_add_failed));
                    }
                });
            }
        } else if (v == mRemarkView) {
            PopupInputCard popupInputCard = new PopupInputCard((Activity) getContext());
            popupInputCard.setContent(mRemarkView.getContent());
            popupInputCard.setTitle(getResources().getString(R.string.profile_remark_edit));
            popupInputCard.setOnPositive((result -> {
                mRemarkView.setContent(result);
                if (TextUtils.isEmpty(result)) {
                    result = "";
                }
                modifyRemark(result);
            }));
            popupInputCard.show(mRemarkView, Gravity.BOTTOM);

        } else if (v == addFriendRemarkLv) {
            PopupInputCard popupInputCard = new PopupInputCard((Activity) getContext());
            popupInputCard.setContent(addFriendRemarkLv.getContent());
            popupInputCard.setTitle(getResources().getString(R.string.contact_friend_remark));
            popupInputCard.setOnPositive((result -> {
                addFriendRemarkLv.setContent(result);
                if (TextUtils.isEmpty(result)) {
                    result = "";
                }
                modifyRemark(result);
            }));
            popupInputCard.show(addFriendRemarkLv, Gravity.BOTTOM);
        }
    }

    private void modifyRemark(final String txt) {
        presenter.modifyRemark(mId, txt, new IUIKitCallback<String>() {
            @Override
            public void onSuccess(String data) {
                mContactInfo.setRemark(txt);
                Map<String, Object> param = new HashMap<>();
                param.put(TUIConstants.TUIContact.FRIEND_ID, mId);
                param.put(TUIConstants.TUIContact.FRIEND_REMARK, txt);
                TUICore.notifyEvent(TUIConstants.TUIContact.EVENT_FRIEND_INFO_CHANGED, TUIConstants.TUIContact.EVENT_SUB_KEY_FRIEND_REMARK_CHANGED, param);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    private void addBlack() {
        String[] idStringList = mId.split(",");
        List<String> idList = new ArrayList<>(Arrays.asList(idStringList));
        presenter.addToBlackList(idList);
    }

    private void deleteBlack() {
        String[] idStringList = mId.split(",");
        List<String> idList = new ArrayList<>(Arrays.asList(idStringList));
        presenter.deleteFromBlackList(idList);
    }

    public void setOnButtonClickListener(OnButtonClickListener l) {
        mListener = l;
    }

    public interface OnButtonClickListener {
        void onStartConversationClick(ContactItemBean info);

        void onDeleteFriendClick(String id);
    }

}
