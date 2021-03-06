package com.example.meet.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.framework.base.BasePagetAdapter;
import com.example.framework.base.BaseUIActivity;
import com.example.framework.manager.MediaPlayerManager;
import com.example.framework.utils.AnimUtls;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName : GuideActivity
 * Founder  : jyt
 * Create Date : 2020/9/2 3:45 PM
 * Profile : 引导页
 */
public class GuideActivity extends BaseUIActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private ImageView iv_music_switch;
    private TextView tv_guide_skip;
    private ImageView iv_guide_point_1;
    private ImageView iv_guide_point_2;
    private ImageView iv_guide_point_3;

    /**
     * -BaseActivity:所有的统一功能：语言切换，请求权限
     *      -BaseUIActivity： 沉浸式
     *      -BaseBackActivity 返回键
     *      -......
     */


    /**
     * 1:viewpager :适配器，帧动画
     * 2：小圆点
     * 3：歌曲的播放
     * 4：属性动画旋转
     * 5：跳转
     *
     * @param savedInstanceState
     */


    private View view1;
    private View view2;
    private View view3;

    private List<View> mPageList = new ArrayList<>();
    private BasePagetAdapter mPageAdapter;

    private MediaPlayerManager mGuidMusic;

    private ObjectAnimator mAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        iv_music_switch = (ImageView) findViewById(R.id.iv_music_switch);
        tv_guide_skip = (TextView) findViewById(R.id.tv_guide_skip);
        iv_guide_point_1 = (ImageView) findViewById(R.id.iv_guide_point_1);
        iv_guide_point_2 = (ImageView) findViewById(R.id.iv_guide_point_2);
        iv_guide_point_3 = (ImageView) findViewById(R.id.iv_guide_point_3);

        view1 = View.inflate(this,R.layout.layout_pager_guide_1,null);
        view2 = View.inflate(this,R.layout.layout_pager_guide_2,null);
        view3 = View.inflate(this,R.layout.layout_pager_guide_3,null);

        mPageList.add(view1);
        mPageList.add(view2);
        mPageList.add(view3);

        iv_music_switch.setOnClickListener(this);
        tv_guide_skip.setOnClickListener(this);

        //预加载
        mViewPager.setOffscreenPageLimit(mPageList.size());


        mPageAdapter =  new BasePagetAdapter(mPageList);
        mViewPager.setAdapter(mPageAdapter);


        //小圆点逻辑
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                
                seletePoint(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        startMusic();
    }

    //歌曲
    //旋转
    private void startMusic(){

        mGuidMusic = new MediaPlayerManager();
        mGuidMusic.setLooping(true);
        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.guide);
        mGuidMusic.startPlay(file);

        mAnim  = AnimUtls.rotation(iv_music_switch);

        mAnim.start();

    }



    private void seletePoint(int position) {

        switch (position){
            case 0:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point_p);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point);

                break;

            case 1:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point_p);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point);
                break;

            case 2:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point_p);
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_music_switch:
                if(mGuidMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PAUSE){
                    mAnim.start();
                    mGuidMusic.continuePlay();
                    iv_music_switch.setImageResource(R.drawable.img_guide_music);

                }
                else if(mGuidMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PLAY){
                    mAnim.pause();
                    mGuidMusic.pausePlay();
                    iv_music_switch.setImageResource(R.drawable.img_guide_music_off);
                }
                break;

            case R.id.tv_guide_skip:

                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGuidMusic.stopPlay();
    }
}
