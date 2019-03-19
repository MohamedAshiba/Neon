package com.ashiba.neon.views;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.ashiba.neon.R;
import com.ashiba.neon.adapters.BottomInfoRecyclerAdapter;
import com.ashiba.neon.adapters.InfoRecyclerAdapter;
import com.ashiba.neon.app.AppController;
import com.ashiba.neon.dataModel.MainPost;
import com.ashiba.neon.views.fragments.ElnashraFragment;
import com.ashiba.neon.views.fragments.HagsFragment;
import com.ashiba.neon.views.fragments.HayFragment;
import com.ashiba.neon.views.fragments.LehFragment;
import com.ashiba.neon.views.fragments.NorhaFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ashiba.neon.utils.Const.URL_JSON_OBJECT;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView postListView;
    private List<MainPost> post_list;
    private InfoRecyclerAdapter infoRecyclerAdapter;
    private ProgressBar cardProgressBar;
    private GridLayoutManager gridLayoutManager;

    private TextView lehBtn, norhaBtn, elnashraBtn, hagsBtn;

    private CircleImageView mProfileImage;
    private TextView mProfileUsername;

    NavigationView navigationView;

    Fragment selectedFragment;

    GoogleApiClient googleApiClient;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NeoN");

        lehBtn = findViewById(R.id.leh_btn);
        norhaBtn = findViewById(R.id.norha_btn);
        elnashraBtn = findViewById(R.id.elnashra_btn);
        hagsBtn = findViewById(R.id.hags_btn);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        cardProgressBar = findViewById(R.id.card_progress_bar);

        postListView = findViewById(R.id.post_recycler_view);
        postListView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this , 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        postListView.setLayoutManager(gridLayoutManager);

        makeJsonObjReq();


        post_list = new ArrayList<>();
        infoRecyclerAdapter = new InfoRecyclerAdapter(post_list, this);
        postListView.setAdapter(infoRecyclerAdapter);
        infoRecyclerAdapter.notifyDataSetChanged();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {

            setStyleBottomNav(lehBtn);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new LehFragment()).commit();
        }

            selectedFragment  = null;

        lehBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setStyleBottomNav(lehBtn);

                selectedFragment = new LehFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new LehFragment()).commit();

            }
        });

        norhaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setStyleBottomNav(norhaBtn);

                selectedFragment = new NorhaFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new NorhaFragment()).commit();

            }
        });

        elnashraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setStyleBottomNav(elnashraBtn);

                selectedFragment = new ElnashraFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ElnashraFragment()).commit();


            }
        });

        hagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setStyleBottomNav(hagsBtn);

                selectedFragment = new HayFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HagsFragment()).commit();

            }
        });

        setUserDataWithFB();
        setUserDataWithGoogle();

    }

    private void setStyleBottomNav(TextView item) {

        hagsBtn.setBackgroundResource(R.color.colorWhite);
        hagsBtn.setTextColor(Color.BLACK);

        norhaBtn.setBackgroundResource(R.color.colorWhite);
        norhaBtn.setTextColor(Color.BLACK);

        elnashraBtn.setBackgroundResource(R.color.colorWhite);
        elnashraBtn.setTextColor(Color.BLACK);

        lehBtn.setBackgroundResource(R.color.colorWhite);
        lehBtn.setTextColor(Color.BLACK);

        item.setBackgroundResource(R.color.colorPrimary);
        item.setTextColor(Color.WHITE);

    }

    private void setUserDataWithFB() {

        mProfileUsername = navigationView.getHeaderView(0).findViewById(R.id.profile_user_name);
        mProfileImage = navigationView.getHeaderView(0).findViewById(R.id.profile_image);

        if (AccessToken.getCurrentAccessToken() != null) {

            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

                    try {
                        String userId = object.getString("id");
                        final String userName = object.getString("name");
                        String userImage = "https://graph.facebook.com/" + userId + "/picture?type=normal";

                        mProfileUsername.setText(userName);

                        Picasso.with(MainActivity.this).load(userImage).into(mProfileImage);


                    } catch (Exception e) {

                        Log.d("MainActivity", e.getMessage());

                    }

                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

    }

    private void setUserDataWithGoogle() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);

        if (account != null) {

            String userId = account.getId();
            String userName = account.getDisplayName();
            Uri userImage = account.getPhotoUrl();

          //  Log.d("MainActivity", userImage.toString());

            mProfileUsername.setText(userName);
           Picasso.with(MainActivity.this).load(userImage).into(mProfileImage);


        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_btn) {

          showAlertDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news_one) {

        } else if (id == R.id.nav_news_two) {

            Intent secondMainIntent = new Intent(MainActivity.this, SecondMainActivity.class);
            startActivity(secondMainIntent);
            finish();

        } else if (id == R.id.nav_news_three) {

            Intent thirdMainIntent = new Intent(MainActivity.this, ThirdMainActivity.class);
            startActivity(thirdMainIntent);
            finish();

        } else if (id == R.id.logout) {

            if (googleApiClient.isConnected()) {

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {

                                sendToLogin();
                            }
                        });
            }
                LoginManager.getInstance().logOut();
                sendToLogin();

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq() {

        cardProgressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL_JSON_OBJECT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Mainresponse" , String.valueOf(response));

                cardProgressBar.setVisibility(View.GONE);

                try {
                    JSONArray array = response.getJSONArray("posts");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject o = array.getJSONObject(i);
                        MainPost post = new MainPost();
                        post.setTitle(o.getString("title"));
                        post.setUrl(o.getString("thumbnail"));
                        post.setContent(o.getString("content"));

                        post_list.add(post);
                        infoRecyclerAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Main_error" , String.valueOf(error));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }


    private void showAlertDialog() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText searchText = view.findViewById(R.id.search_text);
        Button searchBtn = view.findViewById(R.id.dialog_search_btn);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                String searchMessage = searchText.getText().toString();

                if (!TextUtils.isEmpty(searchMessage)) {
                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                    searchIntent.putExtra("message", searchMessage);
                    startActivity(searchIntent);
                }
            }
        });
    }
}