package com.example.newsreader1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {
    private Context context;
    private int resource;

    public NewsAdapter(Context context, int resource, List<NewsArticle> newsList) {
        super(context, resource, newsList);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        TextView linkTextView = view.findViewById(R.id.linkTextView);

        NewsArticle newsArticle = getItem(position);
        if (newsArticle != null) {
            titleTextView.setText(newsArticle.getTitle());
            descriptionTextView.setText(newsArticle.getDescription());

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
            try {
                // Format the Date object to a String
                String formattedDate = dateFormat.format(newsArticle.getDate());
                dateTextView.setText(formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the formatting error appropriately
            }

            linkTextView.setText(newsArticle.getLink());
        }

        return view;
    }
}
