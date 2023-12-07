package com.example.newsreader1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsArticle> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_article);

        listView = findViewById(R.id.listView);
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, R.layout.news_list_item, newsList);
        listView.setAdapter(newsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(newsList.get(position));
            }
        });

        // Fetch news
        new FetchNewsTask().execute("https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
    }

    private void showDetails(NewsArticle newsArticle) {
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("title", newsArticle.getTitle());
        intent.putExtra("description", newsArticle.getDescription());
        intent.putExtra("date", newsArticle.getDate());
        intent.putExtra("link", newsArticle.getLink());
        startActivity(intent);
    }


}
