package com.android.booksrecommendation;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class FetchBook extends AsyncTask<String,Void,String> {



    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            MainActivity.initializeData(jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    
}
