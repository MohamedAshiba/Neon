package com.ashiba.neon.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ashiba.neon.R;
import com.ashiba.neon.adapters.BottomInfoRecyclerAdapter;
import com.ashiba.neon.adapters.EndlessRecyclerViewScrollListener;
import com.ashiba.neon.app.AppController;
import com.ashiba.neon.dataModel.MainPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import static com.ashiba.neon.utils.Const.URL_JSON_LEH;

/**
 * A simple {@link Fragment} subclass.
 */
public class LehFragment extends Fragment {

    private RecyclerView bottomPostRecyclerView;
    private LinearLayoutManager layoutManager;
    private List<MainPost> bottom_post_list;
    private BottomInfoRecyclerAdapter bottomInfoRecyclerAdapter;
    private ProgressBar mBottomPostProgressBar;
    private EndlessRecyclerViewScrollListener scrollListener;
    private View mLoadData;
    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    public LehFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_leh, container, false);

        bottom_post_list = new ArrayList<>();

        bottomPostRecyclerView = mView.findViewById(R.id.leh_post_recycler_view);
        bottomInfoRecyclerAdapter = new BottomInfoRecyclerAdapter(bottom_post_list, getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        bottomPostRecyclerView.setLayoutManager(layoutManager);
        bottomPostRecyclerView.setAdapter(bottomInfoRecyclerAdapter);
        bottomPostRecyclerView.setHasFixedSize(true);
        bottomPostRecyclerView.setItemAnimator(new DefaultItemAnimator());
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.v("LehFragmentTest", String.valueOf(page));
                loadMoreDataFromApi(page);

            }
        };
        bottomPostRecyclerView.addOnScrollListener(scrollListener);

        mBottomPostProgressBar = mView.findViewById(R.id.bottom_post_progress_bar);
        mLoadData = mView.findViewById(R.id.load_more_data);

        makeJsonObjReq();

        return mView;

    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq() {

        mBottomPostProgressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL_JSON_LEH, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mBottomPostProgressBar.setVisibility(View.GONE);

                try {

                    JSONArray array = response.getJSONArray("posts");

                    for (int i = 0; i < array.length(); i++) {

                        Log.v("LehFragment", "why");

                        JSONObject o = array.getJSONObject(i);
                        MainPost post = new MainPost();
                        post.setTitle(o.getString("title"));

                        post.setContent(o.getString("content"));

                        JSONObject object = o.getJSONObject("thumbnail_images");
                        JSONObject objectTwo = object.getJSONObject("full");

                        post.setUrl(objectTwo.getString("url"));

                        bottom_post_list.add(post);
                        bottomInfoRecyclerAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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


    private String urlWithPages(int page) {
        String url = "https://neonsci.com/api/get_category_posts/?id=20&page=" + page;
        return url;
    }

    private void loadMoreDataFromApi(final int page) {

        mLoadData.setVisibility(View.VISIBLE);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, urlWithPages(page), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", String.valueOf(response));

                mLoadData.setVisibility(View.GONE);

                try {

                    JSONArray array = response.getJSONArray("posts");

                    for (int i = 0; i < array.length(); i++) {


                        JSONObject o = array.getJSONObject(i);
                        MainPost post = new MainPost();
                        post.setTitle(o.getString("title"));

                        post.setContent(o.getString("content"));

                        JSONObject object = o.getJSONObject("thumbnail_images");
                        JSONObject objectTwo = object.getJSONObject("full");

                        post.setUrl(objectTwo.getString("url"));

                        bottom_post_list.add(post);
                        bottomInfoRecyclerAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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