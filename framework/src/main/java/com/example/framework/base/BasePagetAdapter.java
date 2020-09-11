package com.example.framework.base;

import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.List;

/**
 * FileName : BasePaegtAdapter
 * Founder  : jyt
 * Create Date : 2020/9/2 9:58 PM
 * Profile :
 */
public class BasePagetAdapter extends PagerAdapter {

    private List<View> mList;

    public BasePagetAdapter(List<View> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject( View view,  Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        ( (ViewPager)container).addView(mList.get(position));

        return mList.get(position);
    }

    @Override
    public void destroyItem( ViewGroup container, int position,  Object object) {
        ( (ViewPager)container).removeView(mList.get(position));

    }
}
