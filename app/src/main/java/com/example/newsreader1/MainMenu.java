package com.example.newsreader1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private ListView listView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsArticle> newsList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        listView = findViewById(R.id.listViewMainMenu);
        progressBar = findViewById(R.id.progressBarMainMenu);

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, R.layout.news_list_article, newsList);
        listView.setAdapter(newsAdapter);

        // Set up item click listener
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

    private class FetchNewsTask extends AsyncTask<String, Void, ArrayList<NewsArticle>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show ProgressBar before starting the task
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsArticle> doInBackground(String... urls) {
            try {
                NewsUtils.fetchNewsData(urls[0], new NewsUtils.NewsAsyncResponse() {
                    @Override
                    public void processFinish(ArrayList<NewsArticle> output) {
                        // This method will be called when the news data fetching is complete
                        // Update UI with the fetched news
                        newsList.clear();
                        if (output != null) {
                            newsList.addAll(output);
                        }
                        newsAdapter.notifyDataSetChanged();

                        // Hide progressbar after the task
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } catch (Exception e) {
                Log.e("FetchNewsTask", "Error fetching news data", e);
                return null;
            }
            return null; // Modify as needed
        }
    }
}
