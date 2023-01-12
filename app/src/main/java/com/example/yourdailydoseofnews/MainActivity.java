package com.example.yourdailydoseofnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** appended a show-tags parameter to the uri builder in the onCreateLoader method
     * also in res/list_Item_Layout, changed android:text attribute to tools:text to see the placeholder text only in the preview and not in the emulator **/

    String newsUrl = "https://content.guardianapis.com/search?api-key=test";
    NewsAdapter adapter;
    TextView noInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noInternet = findViewById(R.id.noInternet);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
        getLoaderManager().initLoader(0,null,loaderCallbacks).forceLoad();
        }
        else {

            noInternet.setText("No Internet Connection");
        }

    }

    public void updateUi(ArrayList<Story> data){
        ListView lv = findViewById(R.id.listView);

        if (data != null){
        adapter = new NewsAdapter(this,data);
        lv.setAdapter(adapter);
        

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story clickedItem = adapter.getItem(position);
                String clicked_url = clickedItem.mIntentUrl;
                Intent openStoryInBrowser = new Intent(Intent.ACTION_VIEW);
                openStoryInBrowser.setData(Uri.parse(clicked_url));

                /** added a check to see if the user has an app that can handle the intent **/
                if (openStoryInBrowser.resolveActivity(getPackageManager()) != null) {
                    startActivity(openStoryInBrowser);
                }
            }
        };
        lv.setOnItemClickListener(onItemClickListener);
        }

        TextView emptyView = findViewById(R.id.emptyView);
        emptyView.setText("No News Found.");
        lv.setEmptyView(emptyView);

    }

    public void search (View view){
       LoaderManager loadermanager = getLoaderManager();
       loadermanager.restartLoader(0,null,loaderCallbacks).forceLoad();
        getLoaderManager().initLoader(0,null,loaderCallbacks).forceLoad();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_icon,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks loaderCallbacks = new LoaderManager.LoaderCallbacks<ArrayList>() {
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.e("onCreateLoader", "On create loader is called");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String fromDate = sharedPref.getString
                (getString(R.string.settings_from_date_key),
                getString(R.string.setting_from_date_default));


        EditText searchTerm = findViewById(R.id.search);
        String userQuery = searchTerm.getText().toString() ;

        Uri baseUri = Uri.parse(newsUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("q",userQuery);
        uriBuilder.appendQueryParameter("from-date",fromDate);
        uriBuilder.appendQueryParameter("show-tags","contributor");

        return new NewsLoader(getApplicationContext(),uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
        updateUi(data);
        noInternet.setVisibility(View.GONE);
    }


    @Override
    public void onLoaderReset(Loader loader) {
        Log.e("OnLoaderReset", "Data has been reset");
        adapter.addAll(new ArrayList<Story>());
    }
};
}
