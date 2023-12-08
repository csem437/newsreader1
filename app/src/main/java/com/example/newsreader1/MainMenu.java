package com.example.newsreader1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private ListView listView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsArticle> newsList;
    private ProgressBar progressBar;
    private static final int PROGRESS_INCREMENT = 1;
    private static final int MAX_PROGRESS = 100;
    private static int progress = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        listView = findViewById(R.id.listViewMainMenu);
        progressBar = findViewById(R.id.progressBar);

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, R.layout.news_list_article, newsList);
        listView.setAdapter(newsAdapter);

        // Set up item click listener -- To be implemented... favs and delete.


        // Fetch news
        new FetchNewsTask().execute("https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

        //Start pointless progressBar
        startProgressBar();
    }

    private void showDetails(NewsArticle newsArticle) {
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("title", newsArticle.getTitle());
        intent.putExtra("description", newsArticle.getDescription());
        intent.putExtra("date", newsArticle.getDate());
        intent.putExtra("link", newsArticle.getLink());
        startActivity(intent);
    }

    private void startProgressBar() {
        // Create a new thread to update the progress bar
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (progress = 0; progress <= MAX_PROGRESS; progress += PROGRESS_INCREMENT) {


                    try {
                        // Delay for one second
                        Thread.sleep(200);
                        updateProgressBar(progress);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void updateProgressBar(int progress) {
        progressBar.setProgress(progress);

        // Check if the progress bar is full
        if (progress == MAX_PROGRESS) {
            // Reset the progress bar
           resetProgressBar();
        }
    }

    // Reset the progress bar to 0.. probably a bad idea. we'll just let it stay dead.
    private void resetProgressBar() {
        progressBar.setProgress(0);
        //progress = 0;
        Looper.prepare(); //required for toasting on a thread
        Toast.makeText(this, R.string.toasty, Toast.LENGTH_SHORT).show();

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
                        // method will be called when the news data grabbing is done
                        // push news to UI
                        newsList.clear();
                        if (output != null) {
                            newsList.addAll(output);
                        }
                        newsAdapter.notifyDataSetChanged();


                    }
                });
            } catch (Exception e) {
                Log.e("FetchNewsTask", "Error fetching news data", e);
                return null;
            }
            return null;
        }
    }
}
