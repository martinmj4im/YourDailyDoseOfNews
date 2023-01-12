package com.example.yourdailydoseofnews;

import android.content.Context;
import android.util.Log;
import android.content.AsyncTaskLoader;
import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader{
    String mNewsUrl;

    public NewsLoader(Context context, String newsUrl){
        super(context);
        mNewsUrl = newsUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Story> loadInBackground() {
        Log.e("loadinBackground", "Background thread is running");
         ArrayList<Story>  result = QueryUtils.fetchNewsData(mNewsUrl);
        return result;
    }
}
