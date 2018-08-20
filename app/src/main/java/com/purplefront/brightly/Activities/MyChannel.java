package com.purplefront.brightly.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Adapters.MyChannelsAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Fragments.LoginFragment;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.ChannelListResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.SharedPrefUtils;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

public class MyChannel extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<ChannelListModel>  channelListModels = new ArrayList<>();
    MyChannelsAdapter myChannelsAdapter;
    TextView view_nodata;
    ImageView image_createChannel;
    RecyclerView channels_listview;
    Realm realm;
    RealmResults<RealmModel> realmModel;
    String userId;
    String userName;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_channel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Channel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for(RealmModel model:realmModel){
            userId = model.getUser_Id();
            userName = model.getUser_Name().toUpperCase();
            userEmail = model.getUser_Email();
        }

        image_createChannel = (ImageView) findViewById(R.id.image_createChannel);
        image_createChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyChannel.this, CreateChannel.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

        view_nodata = (TextView) findViewById(R.id.view_nodata);
        channels_listview = (RecyclerView) findViewById(R.id.channels_listview);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView headerText_Name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.User_Name);
        headerText_Name.setText(userName);
        TextView headerText_Email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.User_Email);
        headerText_Email.setText(userEmail);
        navigationView.setNavigationItemSelectedListener(this);

        getChannelsLists();
    }


    public void getChannelsLists() {
        try {

            if (CheckNetworkConnection.isOnline(MyChannel.this)) {
                showProgress();
                Call<ChannelListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(MyChannel.this).getMyChannelsList(userId);
                callRegisterUser.enqueue(new ApiCallback<ChannelListResponse>(MyChannel.this) {
                    @Override
                    public void onApiResponse(Response<ChannelListResponse> response, boolean isSuccess, String message) {
                        ChannelListResponse channelListResponse = response.body();
                        if (isSuccess) {

                            if (channelListResponse != null && channelListResponse.getChannels() != null && channelListResponse.getChannels().size() != 0) {

                                channelListModels = channelListResponse.getChannels();
                                setAdapter(channelListModels);
                                dismissProgress();

                            } else {
                                channels_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);
                                dismissProgress();
                            }

                        } else {
                            showLongToast(MyChannel.this, message);
                            dismissProgress();
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
                    }
                });
            } else {

                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();

            dismissProgress();
        }

    }

    private void setAdapter(List<ChannelListModel> channelListModels) {

        channels_listview.setLayoutManager(new GridLayoutManager(MyChannel.this,3));
        myChannelsAdapter = new MyChannelsAdapter(MyChannel.this, channelListModels);
        channels_listview.setAdapter(myChannelsAdapter);
        myChannelsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_mysubscription) {

        } else if (id == R.id.nav_logout) {

            realm.beginTransaction();

        // delete all realm objects
            realm.deleteAll();

            //commit realm changes
            realm.commitTransaction();
            /*realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realmModel.deleteAllFromRealm();// use to delete all

                    //**OR** use in for loop to delete perticulr record as a location
                    realmModel.deleteFromRealm();

                }
            });*/
            finishIntent(MyChannel.this, Login.class);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
