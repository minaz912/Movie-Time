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
 * Created by minaz on 29/09/15.
 */
public class TrailerListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Trailer> trailerList;
    int layoutResourceId;

    public TrailerListAdapter(Context context, ArrayList<Trailer> trailerList, int layoutResourceId) {
        this.context = context;
        this.trailerList = trailerList;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return trailerList.size();
    }

    @Override
    public Trailer getItem(int position) {
        return trailerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final Trailer trailer = getItem(position);
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.trailerName = (TextView) row.findViewById(R.id.movie_trailer_list_item_name);

        holder.trailerName.setText(trailer.getName());

        return row;
    }

    private class ViewHolder {
        TextView trailerName;
    }
}
