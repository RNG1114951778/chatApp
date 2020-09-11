package com.example.framework.helper;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.framework.R;

/**
 * FileName : GlideHelper
 * Founder  : jyt
 * Create Date : 2020/9/9 8:27 PM
 * Profile :
 */
public class GlideHelper {
    public static void loadUrl(Context mContext, String url, ImageView imageView){
       Glide.with(mContext)
                .load(url)
//                .placeholder(R.drawable.img_glide_load_ing)
//                .error(R.drawable.img_glide_load_error)
//                .format(DecodeFormat.PREFER_RGB_565)
//                // 取消动画，防止第一次加载不出来
//                .dontAnimate()
//                //加载缩略图
//                .thumbnail(0.3f)
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }
}
