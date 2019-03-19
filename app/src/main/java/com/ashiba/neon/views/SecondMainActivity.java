package com.ashiba.neon.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ashiba.neon.R;
import com.ashiba.neon.adapters.BottomInfoRecyclerAdapter;
import com.ashiba.neon.views.fragments.HayFragment;
import com.ashiba.neon.views.fragments.HedrogenaFragment;
import com.ashiba.neon.views.fragments.MalfFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView mProfileImage;
    private TextView mProfileUsername;

    private TextView hayBtn, malafBtn, hedrogenaBtn;

    Fragment selectedFragment;

    GoogleApiClient googleApiClient;
    NavigationView navigationView;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NeoN");

        hayBtn = findViewById(R.id.hay_btn);
        malafBtn = findViewById(R.id.malf_btn);
        hedrogenaBtn = findViewById(R.id.hedrogena_btn);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProfileUsername = navigationView.getHeaderView(0).findViewById(R.id.profile_user_name);
        mProfileImage = navigationView.getHeaderView(0).findViewById(R.id.profile_image);

        if (savedInstanceState == null) {


            setStyleBottomNav(hayBtn);

            getSupportFragmentManager().beginTransaction().replace(R.id.second_main_container, new HayFragment()).commit();
        }

        selectedFragment  = null;

        hayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setStyleBottomNav(hayBtn);

                selectedFragment = new HayFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.second_main_container, new HayFragment()).commit();

            }
        });

        malafBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setStyleBottomNav(malafBtn);

                selectedFragment = new MalfFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.second_main_container, new MalfFragment()).commit();

            }
        });

        hedrogenaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setStyleBottomNav(hedrogenaBtn);

                selectedFragment = new HedrogenaFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.second_main_container, new HedrogenaFragment()).commit();

            }
        });

        setUserDataWithFB();
        setUserDataWithGoogle();

    }

    private void setStyleBottomNav(TextView item) {

        hedrogenaBtn.setBackgroundResource(R.color.colorWhite);
        hedrogenaBtn.setTextColor(Color.BLACK);

        malafBtn.setBackgroundResource(R.color.colorWhite);
        malafBtn.setTextColor(Color.BLACK);

        hayBtn.setBackgroundResource(R.color.colorWhite);
        hayBtn.setTextColor(Color.BLACK);

        item.setBackgroundResource(R.color.colorPrimary);
        item.setTextColor(Color.WHITE);

    }

    public void setUserDataWithFB() {

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

                        Picasso.with(SecondMainActivity.this).load(userImage).into(mProfileImage);


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

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SecondMainActivity.this);

        if (account != null) {

            String userId = account.getId();
            String userName = account.getDisplayName();
            Uri userImage = account.getPhotoUrl();

            //  Log.d("MainActivity", userImage.toString());

            mProfileUsername.setText(userName);
            Picasso.with(SecondMainActivity.this).load(userImage).into(mProfileImage);


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

            Intent mainIntent = new Intent(SecondMainActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();

        } else if (id == R.id.nav_news_two) {

        } else if (id == R.id.nav_news_three) {

            Intent thirdMainIntent = new Intent(SecondMainActivity.this, ThirdMainActivity.class);
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
                    Intent searchIntent = new Intent(SecondMainActivity.this, SearchActivity.class);
                    searchIntent.putExtra("message", searchMessage);
                    startActivity(searchIntent);
                }
            }
        });
    }

}

