package com.example.manankedia.comicreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adsnative.ads.ANAdPositions;
import com.adsnative.ads.ANAdViewBinder;
import com.adsnative.ads.ANRecyclerAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SharedPreferences sharedPreferences;
    private static final String JSON_URL = "https://xkcd.com/info.0.json";
    private ANRecyclerAdapter anRecyclerAdapter;
    final ANAdViewBinder anAdViewBinder = new
            ANAdViewBinder.Builder(R.layout.native_ads_layout)
            .bindTitle(R.id.ad_title)
            .bindSummary(R.id.ad_summary)
            .bindIconImage(R.id.ad_icon_image)
            .bindCallToAction(R.id.ad_call_to_action)
            .bindPromotedBy(R.id.ad_promoted_by)
            .build();

    final ANAdViewBinder anAdViewBinderGrid = new
            ANAdViewBinder.Builder(R.layout.native_ads_layout_grid)
            .bindTitle(R.id.ad_title)
            .bindSummary(R.id.ad_summary)
            .bindIconImage(R.id.ad_icon_image)
            .bindCallToAction(R.id.ad_call_to_action)
            .bindPromotedBy(R.id.ad_promoted_by)
            .build();

    List<Comic> comics;

    private int currentViewMode = 0;

    final static int VIEW_MODE_LIST = 0;
    final static int VIEW_MODE_GRID = 1;

    private ViewStub stubList;

    private Parcelable recyclerViewState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stubList = findViewById(R.id.stub_list);

        stubList.inflate();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        comics = new ArrayList<>();

        loadComicsList();







    }

    private void setAdapters() {
        if (VIEW_MODE_LIST == currentViewMode){


            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ComicAdapter adapter = new ComicAdapter(getApplicationContext(), comics);

            //ANAdPositions.ServerPositions serverPositions = ANAdPositions.serverPositioning();
            ANAdPositions.ClientPositions clientPositions = ANAdPositions.clientPositioning();
// add fixed positions
            clientPositions.addFixedPosition(5);
            clientPositions.enableRepeatingPositions(5);

            anRecyclerAdapter = new ANRecyclerAdapter(MainActivity.this, adapter,
                    "2Pwo1otj1C5T8y6Uuz9v-xbY1aB09x8rWKvsJ-HI", clientPositions);
            // Register the renderer with the ANRecyclerAdapter
            anRecyclerAdapter.registerViewBinder(anAdViewBinder);
            // Set your original view's adapter to ANRecyclerAdapter instance
            mRecyclerView.setAdapter(anRecyclerAdapter);
            // Start loading ads
            anRecyclerAdapter.loadAds();
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

            ComicAdapter adapter = new ComicAdapter(getApplicationContext(), comics);

            ANAdPositions.ClientPositions clientPositions = ANAdPositions.clientPositioning();
// add fixed positions
            clientPositions.addFixedPosition(4);
            clientPositions.enableRepeatingPositions(12);

            anRecyclerAdapter = new ANRecyclerAdapter(MainActivity.this, adapter,
                    "2Pwo1otj1C5T8y6Uuz9v-xbY1aB09x8rWKvsJ-HI", clientPositions);
            // Register the renderer with the ANRecyclerAdapter
            anRecyclerAdapter.registerViewBinder(anAdViewBinderGrid);
            // Set your original view's adapter to ANRecyclerAdapter instance
            mRecyclerView.setAdapter(anRecyclerAdapter);
            // Start loading ads
            anRecyclerAdapter.loadAds();

            Log.d("Switch View", "Grid View");
        }

    }

    private void loadComicsList() {

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);

                            final int num = obj.getInt("num");
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            for(int i = 1 ; i <= num; i++) {

                                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://xkcd.com/"+i+"/info.0.json", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            Comic comic = new Comic(obj.getString("title"), obj.getString("month"),obj.getString("year"),obj.getString("day"), obj.getString("alt"), obj.getString("transcript"), obj.getString("img"), obj.getInt("num"));
                                            comics.add(comic);

                                            Log.d("IN FOR", "Process No. " + obj.getInt("num"));
                                                if (obj.getInt("num") == num || (obj.getInt("num")%150 == 0)) {

                                                    sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
                                                    currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LIST);
                                                    recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
                                                    setAdapters();
                                                    mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                                    if(obj.getInt("num") == num){
                                                        Thread.sleep(1000);
                                                    }
                                                }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                requestQueue.add(stringRequest1);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_menu_1:
                if(VIEW_MODE_LIST == currentViewMode){
                    currentViewMode = VIEW_MODE_GRID;
                } else {
                    currentViewMode = VIEW_MODE_LIST;
                }

                setAdapters();
                SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentViewMode", currentViewMode);
                editor.apply();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(anRecyclerAdapter != null){
            anRecyclerAdapter.loadAds();
        }
        }

}
