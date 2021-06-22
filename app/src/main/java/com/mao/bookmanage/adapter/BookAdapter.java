package com.mao.bookmanage.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mao.bookmanage.R;
import com.mao.bookmanage.entity.Book;
import com.mao.bookmanage.tools.FileHelper;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2021/6/11.
 */

public class BookAdapter extends BaseAdapter {
    private List<Book> mData;
    private Context mContext;

    public BookAdapter(List<Book> mData, Context mContext) {
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

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.book_listview,parent,false);
        ImageView book_image = (ImageView) convertView.findViewById(R.id.book_image);
        TextView book_name = (TextView) convertView.findViewById(R.id.book_name);
        TextView book_author = (TextView) convertView.findViewById(R.id.book_author);
        TextView book_category = (TextView) convertView.findViewById(R.id.book_category);
        TextView book_info = (TextView) convertView.findViewById(R.id.book_info);
        book_image.setImageBitmap(FileHelper.getInstance().loadImageBitmap(
                mContext,mData.get(position).get_id()+ ""
        ));
        book_name.setText(mData.get(position).get_bookName());
        book_author.setText(mData.get(position).get_author());
        book_category.setText(mData.get(position).get_category());
        book_info.setText(mData.get(position).get_info());
        return convertView;
    }
}
