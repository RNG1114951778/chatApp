package com.example.meet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.framework.helper.GlideHelper;
import com.example.meet.R;
//import com.example.meet.ui.MeInfoActivity;
//import com.example.meet.ui.NewFriendActivity;
//import com.example.meet.ui.NoticeActivity;
//import com.example.meet.ui.PrivateSetActivity;
//import com.example.meet.ui.SettingActivity;
//import com.example.meet.ui.ShareImgActivity;
import com.example.framework.base.BaseFragment;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;
//import com.example.framework.cloud.CloudManager;
import com.example.framework.event.EventManager;
//import com.example.framework.event.MessageEvent;
//import com.example.framework.helper.GlideHelper;
import com.example.framework.utils.LogUtils;

import de.hdodenhof.circleimageview.CircleImageView;
//
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import io.rong.imlib.RongIMClient;

/**
 * FileName: MeFragment
 * Founder: example
 * Profile: 我的
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView iv_me_photo;
    private TextView tv_nickname;
    private LinearLayout ll_me_info;
    private LinearLayout ll_new_friend;
    private LinearLayout ll_private_set;
    private LinearLayout ll_share;
    private LinearLayout ll_setting;
    private LinearLayout ll_notice;

    private TextView tv_server_status;

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        iv_me_photo = view.findViewById(R.id.iv_me_photo);
        tv_nickname = view.findViewById(R.id.tv_nickname);

        ll_me_info = view.findViewById(R.id.ll_me_info);
        ll_new_friend = view.findViewById(R.id.ll_new_friend);
        ll_private_set = view.findViewById(R.id.ll_private_set);
        ll_share = view.findViewById(R.id.ll_share);
        ll_setting = view.findViewById(R.id.ll_setting);
        ll_notice = view.findViewById(R.id.ll_notice);
        tv_server_status = view.findViewById(R.id.tv_server_status);

        ll_me_info.setOnClickListener(this);
        ll_new_friend.setOnClickListener(this);
        ll_private_set.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_notice.setOnClickListener(this);

        loadMeInfo();

//        //监听连接状态
//        CloudManager.getInstance().setConnectionStatusListener(connectionStatus -> {
//            if(isAdded()){
//                if (null != connectionStatus) {
//                    LogUtils.i("connectionStatus:" + connectionStatus);
//                    if (connectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
//                        //连接成功
//                        tv_server_status.setText(getString(R.string.text_server_status_text_1));
//                    } else if (connectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) {
//                        //连接中
//                        tv_server_status.setText(getString(R.string.text_server_status_text_2));
//                    } else if (connectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED) {
//                        //断开连接
//                        tv_server_status.setText(getString(R.string.text_server_status_text_3));
//                    } else if (connectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
//                        //用户在其他地方登陆
//                    } else if (connectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE) {
//                        //网络不可用
//                        tv_server_status.setText(getString(R.string.text_server_status_text_4));
//                    } else if (connectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.SERVER_INVALID) {
//                        //服务器异常
//                        tv_server_status.setText(getString(R.string.text_server_status_text_5));
//                    } else if (connectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.TOKEN_INCORRECT) {
//                        //Token不正确
//                        tv_server_status.setText(getString(R.string.text_server_status_text_6));
//                    }
//                }
//            }
//        });
   }
//
    /**
     * 加载我的个人信息
     */
    private void loadMeInfo( ) {
        IMUser imUser = BmobManager.getInstance().getUser();
        GlideHelper.loadUrl(getActivity(), imUser.getPhoto(), iv_me_photo);
        tv_nickname.setText(imUser.getNickName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_me_info:
                //个人信息
               // startActivity(new Intent(getActivity(), MeInfoActivity.class));
                break;
            case R.id.ll_new_friend:
                //新朋友
               // startActivity(new Intent(getActivity(), NewFriendActivity.class));
                break;
            case R.id.ll_private_set:
                //隐私设置
               // startActivity(new Intent(getActivity(), PrivateSetActivity.class));
                break;
            case R.id.ll_share:
                //分享
               // startActivity(new Intent(getActivity(), ShareImgActivity.class));
                break;
            case R.id.ll_notice:
                //通知
               // startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
            case R.id.ll_setting:
                //设置
              //  startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        switch (event.getType()) {
//            case EventManager.EVENT_REFRE_ME_INFO:
//                loadMeInfo();
//                break;
//        }
//    }
    }

