package com.purplefront.brightly.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Adapters.SetsAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.SetListResponse;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

public class MyChannelsSet extends BaseActivity {

    List<SetsListModel> setsListModelList = new ArrayList<>();
    SetsAdapter channelsSetAdapter;

    TextView view_nodata;
    ImageView image_createChannelSet;
    RecyclerView channelSet_listview;
    Realm realm;
    RealmResults<RealmModel> realmModel;

    String userId;
    String channel_name = "";
    String channel_description = "";
    String encoded_string = "";
    String image_name = "";
    String channel_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_channels_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for(RealmModel model:realmModel){
            userId = model.getUser_Id();
        }

        channel_id = getIntent().getStringExtra("channel_id");
        channel_name = getIntent().getStringExtra("channel_name");
        channel_description = getIntent().getStringExtra("channel_description");
        encoded_string = getIntent().getStringExtra("encoded_string");
        image_name = getIntent().getStringExtra("image_name");

        view_nodata = (TextView) findViewById(R.id.view_nodata);
        channelSet_listview = (RecyclerView) findViewById(R.id.channelSet_listview);
        setTitle(channel_name);

        image_createChannelSet = (ImageView) findViewById(R.id.image_createChannelSet);
        image_createChannelSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyChannelsSet.this, CreateSet.class);
                intent.putExtra("userId", userId);
                intent.putExtra("channel_id", channel_id);
                intent.putExtra("channel_name", channel_name);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

        getSetLists();

    }

    public void getSetLists() {
        try {

            if (CheckNetworkConnection.isOnline(MyChannelsSet.this)) {
                showProgress();
                Call<SetListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(MyChannelsSet.this).getMySetsList(channel_id);
                callRegisterUser.enqueue(new ApiCallback<SetListResponse>(MyChannelsSet.this) {
                    @Override
                    public void onApiResponse(Response<SetListResponse> response, boolean isSuccess, String message) {
                        SetListResponse setListResponse = response.body();
                        if (isSuccess) {

                            if (setListResponse != null && setListResponse.getSets() != null && setListResponse.getSets().size() != 0) {

                                setsListModelList = setListResponse.getSets();
                                setAdapter(setsListModelList);
                                dismissProgress();

                            } else {
                                channelSet_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);
                                dismissProgress();
                            }

                        } else {
                            showLongToast(MyChannelsSet.this, message);
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

    private void setAdapter(List<SetsListModel> setsListModels) {

        channelSet_listview.setLayoutManager(new GridLayoutManager(MyChannelsSet.this,3));
        channelsSetAdapter = new SetsAdapter(MyChannelsSet.this, setsListModels, channel_id);
        channelSet_listview.setAdapter(channelsSetAdapter);
        channelsSetAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_channel_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;

            case R.id.channelInfo_Edit:
                Intent intent = new Intent(MyChannelsSet.this, EditChannelInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("channel_name", channel_name);
                intent.putExtra("channel_description", channel_description);
                intent.putExtra("encoded_string", encoded_string);
                intent.putExtra("image_name", image_name);
                intent.putExtra("channel_id", channel_id);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
