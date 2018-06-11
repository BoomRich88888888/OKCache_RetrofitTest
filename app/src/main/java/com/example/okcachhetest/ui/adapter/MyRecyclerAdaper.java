package com.example.okcachhetest.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.okcachhetest.R;
import com.example.okcachhetest.di.data.Bean;

import java.util.List;

public class MyRecyclerAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Bean.ResultBean.DataBean> mList;
    public static final int ONE_TYPE=1;
    public static final int TWO_TYPE=2;
    public MyRecyclerAdaper(Context context, List<Bean.ResultBean.DataBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType) {
            case ONE_TYPE:
                view = from.inflate(R.layout.one_item, parent, false);
                viewHolder = new ViewHolder1(view);
                break;

            case TWO_TYPE:
                view = from.inflate(R.layout.two_item, parent, false);
                viewHolder = new ViewHolder2(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1) {
            Glide.with(context).load(mList.get(position).getThumbnail_pic_s()).into(((ViewHolder1) holder).mImageView);
            Glide.with(context).load(mList.get(position).getThumbnail_pic_s02()).into(((ViewHolder1) holder).mImageView);
            Glide.with(context).load(mList.get(position).getThumbnail_pic_s03()).into(((ViewHolder1) holder).mImageView);
            ((ViewHolder1) holder).mTitle.setText(mList.get(position).getTitle());
            ((ViewHolder1) holder).mContent.setText(mList.get(position).getCategory());
        } else {
            Glide.with(context).load(mList.get(position).getThumbnail_pic_s()).into(((ViewHolder2) holder).mImageView);
            Glide.with(context).load(mList.get(position).getThumbnail_pic_s02()).into(((ViewHolder2) holder).mImageView);
            Glide.with(context).load(mList.get(position).getThumbnail_pic_s03()).into(((ViewHolder2) holder).mImageView);
            ((ViewHolder2) holder).mTitle.setText(mList.get(position).getTitle());
            ((ViewHolder2) holder).mContent.setText(mList.get(position).getCategory());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getThumbnail_pic_s02() != null) {
            return TWO_TYPE;
        } else {
            return ONE_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        public View rootView;
        public ImageView mImageView;
        public TextView mTitle;
        public TextView mContent;

        public ViewHolder1(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.mImageView = (ImageView) rootView.findViewById(R.id.mImageView);
            this.mTitle = (TextView) rootView.findViewById(R.id.mTitle);
            this.mContent = (TextView) rootView.findViewById(R.id.mContent);
        }

    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder{
        public View rootView;
        public ImageView mImageView;
        public TextView mTitle;
        public TextView mContent;

        public ViewHolder2(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.mImageView = (ImageView) rootView.findViewById(R.id.mImageView);
            this.mTitle = (TextView) rootView.findViewById(R.id.mTitle);
            this.mContent = (TextView) rootView.findViewById(R.id.mContent);
        }

    }
}
