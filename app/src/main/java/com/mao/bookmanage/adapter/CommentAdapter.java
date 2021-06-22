package com.mao.bookmanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mao.bookmanage.R;
import com.mao.bookmanage.entity.Comment;

import java.util.List;

/**
 * Created by Administrator on 2021/6/12.
 */

public class CommentAdapter extends BaseAdapter {

    private List<Comment> mData;
    private Context mContext;

    public CommentAdapter(List<Comment> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).get_id();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_listview,parent,false);
        TextView comment_item_username = convertView.findViewById(R.id.comment_item_username);
        TextView comment_item_commentTime = convertView.findViewById(R.id.comment_item_commentTime);
        TextView comment_item_comment =  convertView.findViewById(R.id.comment_item_comment);
        comment_item_username.setText(mData.get(position).getUsername());
        comment_item_commentTime.setText(mData.get(position).getCommentTime());
        comment_item_comment.setText(mData.get(position).getComment());
        return convertView;
    }
}
