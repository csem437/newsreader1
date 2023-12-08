package com.example.newsreader1;

import android.os.AsyncTask;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsUtils {

    public interface NewsAsyncResponse {
        void processFinish(ArrayList<NewsArticle> output);
    }

    public static void fetchNewsData(String url, NewsAsyncResponse asyncResponse) {
        new FetchNewsTask(asyncResponse).execute(url);
    }

    private static class FetchNewsTask extends AsyncTask<String, Void, ArrayList<NewsArticle>> {

        private final NewsAsyncResponse asyncResponse;

        public FetchNewsTask(NewsAsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }

        @Override
        protected ArrayList<NewsArticle> doInBackground(String... urls) {
            try {
                return parseXml(downloadUrl(urls[0]));
            } catch (Exception e) {
                Log.e("FetchNewsTask", "Error grabbing news", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<NewsArticle> result) {
            asyncResponse.processFinish(result);
        }
    }

    private static ArrayList<NewsArticle> parseXml(String xml) {
        ArrayList<NewsArticle> newsList = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new InputStreamReader(xmlToInputStream(xml)));

            int eventType = parser.getEventType();
            NewsArticle currentArticle = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        name = parser.getName();

                        if (name.equals("item")) {
                            currentArticle = new NewsArticle("", "", "", new Date());
                        } else if (currentArticle != null) {
                            if (name.equals("title")) {
                                currentArticle.setTitle(parser.nextText());
                            } else if (name.equals("link")) {
                                currentArticle.setLink(parser.nextText());
                            } else if (name.equals("description")) {
                                currentArticle.setDescription(parser.nextText());
                            } else if (name.equals("pubDate")) {
                                String dateString = parser.nextText();
                                Date date = parseDate(dateString);
                                currentArticle.setDate(date);
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equals("item") && currentArticle != null) {
                            newsList.add(currentArticle);
                            currentArticle = null;
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e("NewsUtils", "Error with XML", e);
        }

        return newsList;
    }

    private static InputStream xmlToInputStream(String xml) {
        return new java.io.ByteArrayInputStream(xml.getBytes());
    }

    private static String downloadUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            int data = isw.read();
            StringBuilder response = new StringBuilder();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                response.append(current);
            }
            return response.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    private static Date parseDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
            return format.parse(dateString);
        } catch (ParseException e) {
            Log.e("NewsUtils", "Error with date", e);
            return null;
        }
    }
}
