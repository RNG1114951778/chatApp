package com.example.framework.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * FileName : Animutls
 * Founder  : jyt
 * Create Date : 2020/9/2 11:42 PM
 * Profile :动画工具类
 */
public class AnimUtls {
    public static ObjectAnimator rotation(View v){
        ObjectAnimator mAnim = ObjectAnimator.ofFloat(v,"rotation",0f,360f);
        mAnim.setDuration(2*1000);
        mAnim.setRepeatMode(ValueAnimator.RESTART);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAnim.setInterpolator(new LinearInterpolator());
        return mAnim;
    }
}
