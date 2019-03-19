package com.ashiba.neon.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.ashiba.neon.R;
import com.ashiba.neon.adapters.BottomInfoRecyclerAdapter;
import com.ashiba.neon.app.AppController;
import com.ashiba.neon.dataModel.MainPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ashiba.neon.utils.Const.URL_JSON_ELNASHRA;
import static com.ashiba.neon.utils.Const.URL_JSON_OBJECT;

public class SearchActivity extends AppCompatActivity {

    private Toolbar searchToolBar;
    private ProgressBar searchProgressBar;
    private RecyclerView searchRecyclerResult;
    private BottomInfoRecyclerAdapter bottomInfoRecyclerAdapter;
    private List<MainPost> post_list;
    private LinearLayoutManager layoutManager;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchToolBar = findViewById(R.id.search_toolbar);
        setSupportActionBar(searchToolBar);
        getSupportActionBar().setTitle("NeoN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchProgressBar = findViewById(R.id.search_progress_bar);

        post_list = new ArrayList<>();

        searchRecyclerResult = findViewById(R.id.search_recycler_view);
        bottomInfoRecyclerAdapter = new BottomInfoRecyclerAdapter(post_list, this);
        layoutManager = new LinearLayoutManager(this);
        searchRecyclerResult.setLayoutManager(layoutManager);
        searchRecyclerResult.setAdapter(bottomInfoRecyclerAdapter);
        searchRecyclerResult.setHasFixedSize(true);

        makeJsonObjReq();

    }

    private String dictionaryEntries() {
        String searchText = getIntent().getStringExtra("message");
        return "https://neonsci.com/api/get_search_results/?search=" + searchText;
    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq() {

        searchProgressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, dictionaryEntries(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d("search_response", String.valueOf(response));

                if (response != null) {

                    searchProgressBar.setVisibility(View.GONE);

                    try {


                        JSONArray array = response.getJSONArray("posts");

                        if (array.length() > 0) {

                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                                MainPost post = new MainPost();
                                post.setTitle(o.getString("title"));

                                post.setContent(o.getString("content"));

                                JSONObject object = o.getJSONObject("thumbnail_images");
                                JSONObject objectTwo = object.getJSONObject("full");

                                post.setUrl(objectTwo.getString("url"));


                                post_list.add(post);
                                bottomInfoRecyclerAdapter.notifyDataSetChanged();

                            }
                        } else {

                            TextView searchTextError = findViewById(R.id.search_no_data);
                            searchTextError.setVisibility(View.VISIBLE);
                            searchTextError.setText("No data found\n Press to back button");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", String.valueOf(error));

            }
        });

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

}
