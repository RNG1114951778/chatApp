package com.example.framework.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framework.R;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

//import com.example.meet.model.StarModel;
//import com.example.framework.helper.GlideHelper;


/**
 * FileName: CloudTagAdapter
 * Founder: LiuGuiLin
 * Profile: 3D星球适配器
 */
public class CloudTagAdapter extends TagsAdapter {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater inflater;

    public CloudTagAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        // StarModel model = mList.get(position);

//        ViewHolder viewHolder = null;
//        if (mView == null) {
//            viewHolder = new ViewHolder();
        //初始化View
        View mView = inflater.inflate(R.layout.layout_star_view_item, null);
        //初始化控件
        ImageView iv_star_icon = mView.findViewById(R.id.iv_star_icon);
        TextView tv_star_name = mView.findViewById(R.id.tv_star_name);
        //赋值
        tv_star_name.setText(mList.get(position));

        switch (position % 10) {

            case 0:

                iv_star_icon.setImageResource(R.drawable.img_null_card);

                break;

            default:
                iv_star_icon.setImageResource(R.drawable.img_null_card);
        }
        //初始化控件
//            viewHolder.iv_star_icon = mView.findViewById(R.id.iv_star_icon);
//            viewHolder.tv_star_name = mView.findViewById(R.id.tv_star_name);
//            mView.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder) mView.getTag();
//        }
//        if (!TextUtils.isEmpty(model.getPhotoUrl())) {
//            GlideHelper.loadSmollUrl(mContext, model.getPhotoUrl(), 30, 30, viewHolder.iv_star_icon);
//        } else {
//            viewHolder.iv_star_icon.setImageResource(com.example.meet.R.drawable.img_star_icon_3);
//        }
//        viewHolder.tv_star_name.setText(model.getNickName());



//    @Override
//    public Object getItem(int position) {
//        return mList.get(position);
//    }
//
//    @Override
//    public int getPopularity(int position) {
//        return 7;
//    }
//
//    @Override
//    public void onThemeColorChanged(View view, int themeColor) {
//
//    }
//
//    class ViewHolder {
//        private ImageView iv_star_icon;
//        private TextView tv_star_name;
        return mView;
   }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
