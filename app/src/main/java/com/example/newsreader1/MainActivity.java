package com.example.newsreader1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsArticle> newsList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, R.layout.news_list_article, newsList);
        listView.setAdapter(newsAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> showDetails(newsList.get(position)));

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

    private  class FetchNewsTask extends AsyncTask<String, Void, ArrayList<NewsArticle>> {
        // Implementation of AsyncTask to fetch news in the background

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show ProgressBar before starting the task
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsArticle> doInBackground(String... urls) {
            try {
                return NewsUtils.fetchNewsData(urls[0]);
            } catch (Exception e) {
                Log.e("FetchNewsTask", "Error fetching news data", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<NewsArticle> result) {
            super.onPostExecute(result);
            // Update UI with the fetched news
            newsList.clear();
            if (result != null) {
                newsList.addAll(result);
            }
            newsAdapter.notifyDataSetChanged();

            // Hide progressbar after the task
            progressBar.setVisibility(View.GONE);
        }
    }
}
