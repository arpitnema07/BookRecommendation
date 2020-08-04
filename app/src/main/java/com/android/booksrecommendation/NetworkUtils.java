package com.android.booksrecommendation;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String myApi = "AIzaSyAV3D1xYESd3HwWuZJFw09gbczb1WB4irM";
    private static final String QUERY_PARAM = "q";
    // Parameter that limits search results.
    private static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type.
    private static final String PRINT_TYPE = "printType";

    static String getBookInfo(String querySearch){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJson = null;

        try {
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, querySearch)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .appendQueryParameter("key", myApi)
                    .build();
            URL requestUrl = new URL(builtURI.toString());

            urlConnection =(HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line= reader.readLine())!=null){
                builder.append(line);

                builder.append("\n");
            }

            if (builder.length() ==0){
                return null;
            }
            bookJson = builder.toString();

        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if (reader!= null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, bookJson);

        return bookJson;
    }
}
