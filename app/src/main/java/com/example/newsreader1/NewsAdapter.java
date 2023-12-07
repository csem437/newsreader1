package com.example.newsreader1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;


public class NewsAdapter extends ArrayAdapter<NewsArticle> {

    public NewsAdapter(Context context, int resource, List<NewsArticle> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        final NewsArticle newsArticle = getItem(position);
        if (newsArticle != null) {
            TextView titleTextView = view.findViewById(android.R.id.text1);
            titleTextView.setText(newsArticle.getTitle());
        }

        return view;
    }
}
