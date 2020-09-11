package com.example.meet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.helper.GlideHelper;
import com.example.meet.R;
import com.example.meet.model.AddFriendModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * FileName : AddFriendAdapter
 * Founder  : jyt
 * Create Date : 2020/9/9 11:32 AM
 * Profile :
 */
public class AddFriendAdapter extends RecyclerView.Adapter  <RecyclerView.ViewHolder>{

    private Context mContext;
    private List<AddFriendModel> mList;
    private LayoutInflater inflater;

    public onClickListener onclicklistener;

    public void setOnclicklistener(onClickListener onclicklistener) {
        this.onclicklistener = onclicklistener;
    }

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT =1;


    public AddFriendAdapter(Context mContext, List<AddFriendModel> mList) {

        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_TITLE) {

            return new TitleViewHolder(inflater.inflate(R.layout.layout_search_title_item, null));
        }
        else if (viewType == TYPE_CONTENT) {

            return new ContentViewHolder(inflater.inflate(R.layout.layout_search_user_item, null));

        }

       return null;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        AddFriendModel model = mList.get(position);
        if(model.getType() == TYPE_TITLE){
            ((TitleViewHolder)holder).tv_title.setText(model.getTitle());

        }
        else if(model.getType() == TYPE_CONTENT){
            GlideHelper.loadUrl(mContext,model.getPhoto(), ((ContentViewHolder)holder).iv_photo);

            ( (ContentViewHolder)holder).iv_sex.setImageResource
                    (model.isSex()?R.drawable.img_boy_icon : R.drawable.img_girl_icon);

            ((ContentViewHolder)holder).tv_nickname.setText(model.getNickName());
            ((ContentViewHolder)holder).tv_age.setText(model.getAge() +"岁");
            ((ContentViewHolder)holder).tv_desc.setText(model.getDesc());



        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onclicklistener != null)
                    onclicklistener.onclick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;

        public TitleViewHolder(View itemView){
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);

        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView iv_photo;
        private ImageView iv_sex;
        private TextView tv_nickname;
        private TextView tv_age;
        private TextView tv_desc;
        public ContentViewHolder(View itemView){
            super(itemView);
            iv_photo = itemView.findViewById(R.id.iv_photo);
            iv_sex = itemView.findViewById(R.id.iv_sex);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_desc = itemView.findViewById(R.id.tv_desc);

        }
    }

    public interface onClickListener{
        void onclick(int position);
    }
}
