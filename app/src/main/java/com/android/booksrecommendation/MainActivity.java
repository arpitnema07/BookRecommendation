package com.android.booksrecommendation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private static ArrayList<Books> mBookData;
    private static BookAdapter mAdapter;
    private EditText searchQuery;
    private static String[] BooksTitleList = new String[10];
    private static String[] BooksAuthorList = new String[10];
    private static Double[] BooksPriceList = new Double[10];
    private static Bitmap[] BooksThumbResourcesList = new Bitmap[10];
    private static String[] links = new String[10];



    public static class ImageDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream in = connection.getInputStream();
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
                return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.dummy_img);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchQuery = findViewById(R.id.search_edit_frame);

        mRecyclerView = findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBookData = new ArrayList<>();
        mAdapter = new BookAdapter(this,mBookData);

        mRecyclerView.setAdapter(mAdapter);

    }

    static void initializeData(JSONObject jsonObject) {

        try{
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            int i =0;

            while (i<itemsArray.length()){
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo= book.getJSONObject("volumeInfo");
                    BooksTitleList[i]=volumeInfo.getString("title");
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                        BooksAuthorList[i]=(authors.get(0).toString());
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                        links[i]=(imageLinks.getString("smallThumbnail"));
                        load(i);
                JSONObject saleInfo = book.getJSONObject("saleInfo");
                    if (saleInfo.getString("saleability").equals("NOT_FOR_SALE")){
                        BooksPriceList[i]=((double)-1);
                    } else {
                        JSONObject price = saleInfo.getJSONObject("retailPrice");
                        BooksPriceList[i]=(price.getDouble("amount"));
                    }
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mBookData.clear();

        for (int i =0;i<BooksTitleList.length;i++){

            mBookData.add(new Books(BooksTitleList[i], BooksAuthorList[i],BooksPriceList[i],BooksThumbResourcesList[i]));
        }
        mAdapter.notifyDataSetChanged();
    }

    private static void load(int i) {
        ImageDownloader task = new ImageDownloader();
        Bitmap bitmap;
        try{
            String link = links[i].substring(0,4)+"s"+links[i].substring(4);
            bitmap =task.execute(link).get();
            BooksThumbResourcesList[i]=bitmap;
        } catch (Exception e) {
            BooksThumbResourcesList[i]=BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.dummy_img);
            e.printStackTrace();
        }
    }

    public void SearchBooks(View view) {

        Arrays.fill(links, null);
        Arrays.fill(BooksAuthorList, "Author Unavailable");
        Arrays.fill(BooksTitleList, "Title Unavailable");
        Arrays.fill(BooksPriceList, 0d);
        Arrays.fill(BooksThumbResourcesList, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.dummy_img));
        String search = searchQuery.getText().toString();
        new FetchBook().execute(search);


    }
}