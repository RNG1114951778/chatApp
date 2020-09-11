package com.example.meet.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.utils.CommonUtils;
import com.example.meet.R;


import com.example.meet.adapter.AddFriendAdapter;
import com.example.meet.base.BaseBackActivity;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;

import com.example.meet.model.AddFriendModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * FileName: AddFriendActivity
 * Founder: example
 * Profile: 添加好友
 */
public class AddFriendActivity extends BaseBackActivity implements View.OnClickListener {

    //标题
    public static final int TYPE_TITLE = 0;
    //内容
    public static final int TYPE_CONTENT = 1;

    /**
     * 1.模拟用户数据
     * 2.根据条件查询
     * 3.推荐好友
     */

    private LinearLayout ll_to_contact;
    private EditText et_phone;
    private ImageView iv_search;
      private RecyclerView mSearchResultView;



    private View include_empty_view;

    private AddFriendAdapter mAddFriendAdapter;
    private List<AddFriendModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {

        include_empty_view = findViewById(R.id.include_empty_view);

        ll_to_contact = (LinearLayout) findViewById(R.id.ll_to_contact);

        et_phone = (EditText) findViewById(R.id.et_phone);
        iv_search = (ImageView) findViewById(R.id.iv_search);

        mSearchResultView = (RecyclerView) findViewById(R.id.mSearchResultView);

        ll_to_contact.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        //列表的实现
        mSearchResultView.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Log.d("imuser", "00000 ");
        mAddFriendAdapter = new AddFriendAdapter(this, mList);
        mSearchResultView.setAdapter(mAddFriendAdapter);

        mAddFriendAdapter.setOnclicklistener(new AddFriendAdapter.onClickListener() {
            @Override
            public void onclick(int position) {
                Toast.makeText(AddFriendActivity.this, position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.iv_search:
                queryPhoneUser();
                break;
        }
    }

    /**
     * 通过电话号码查询
     */
    private void queryPhoneUser() {
        //1.获取电话号码
        String phone = et_phone.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.text_login_phone_null),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //过滤自己
       String phonrNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
        if (phone.equals(phonrNumber)){
            Toast.makeText(this, "查自己干啥", Toast.LENGTH_SHORT).show();
            return;
        }



        //2.查询
        BmobManager.getInstance().queryPhoneUsr(phone, new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (CommonUtils.isEmpty(list)) {

                    IMUser imUser = list.get(0);


                    include_empty_view.setVisibility(View.GONE);
                    mSearchResultView.setVisibility(View.VISIBLE);
                    Log.d("imuser", String.valueOf( mList.size()));


                    mList.clear();

                    addTitle("结果");


                    addContent(imUser);
                    mAddFriendAdapter.notifyDataSetChanged();

                    pushUser();



                }else {
                    include_empty_view.setVisibility(View.VISIBLE);
                    mSearchResultView.setVisibility(View.GONE);
                }

            }
        });
    }

    public void pushUser(){
        //查询所有好友 取100个
        BmobManager.getInstance().queryAllUser(new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if(e == null){
                    if(CommonUtils.isEmpty(list)){
                        addTitle("好友推荐");
                        if(list.size() <= 100){
                            int num = (list.size() <= 100)?  list.size():100;
                                    for(int i = 0;i < num;i++){
                                        String phonrNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
                                        if(list.get(i).getMobilePhoneNumber().equals(phonrNumber)){
                                            continue;
                                        }

                                        addContent(list.get(i));
                                    }
                            mAddFriendAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void addTitle(String title){

        AddFriendModel model = new AddFriendModel();
        model.setType(AddFriendAdapter.TYPE_TITLE);
        model.setTitle(title);
        mList.add(model);


    }


    private String photo;
    private boolean sex;
    private int age;
    private String nickName;
    private String desc;
    private String userId;




    private void addContent(IMUser imUser){
        AddFriendModel model = new AddFriendModel();
        model.setType(AddFriendAdapter.TYPE_CONTENT);

        model.setUserId(imUser.getObjectId());
        model.setPhoto(imUser.getPhoto());
        model.setSex(imUser.isSex());
        model.setAge(imUser.getAge());
        model.setNickName(imUser.getNickName());
        model.setDesc(imUser.getDesc());

        mList.add(model);

        //mAddFriendAdapter.notifyDataSetChanged();

    }
}




