package com.movietime.minaz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by minaz on 03/10/15.
 */
public class ReviewListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Review> reviewList;
    private int layoutResourceId;

    public ReviewListAdapter(Context context, ArrayList<Review> reviewList, int layoutResourceId) {
        this.context = context;
        this.reviewList = reviewList;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Review getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final Review review = getItem(position);
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.reviewContent = (TextView) row.findViewById(R.id.movie_review_list_item_content);
        holder.reviewAuthor = (TextView) row.findViewById(R.id.movie_review_list_item_author);

        holder.reviewContent.setText(review.getContent());
        holder.reviewAuthor.setText(review.getAuthor());

        return row;
    }

    private class ViewHolder {
        TextView reviewContent;
        TextView reviewAuthor;
    }
}
