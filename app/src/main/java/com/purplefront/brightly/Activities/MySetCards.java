package com.purplefront.brightly.Activities;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Adapters.ViewPagerAdapter;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CardsListResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MySetCards extends BaseActivity {

    // Declare Variables
    ViewPager viewPager_Cards;
    PagerAdapter cardsPagerAdapter;
    List<CardsListModel> cardsListModels = new ArrayList<>();

    String userId;
    String channel_id = "";
    String set_description = "";
    String set_name = "";
    String set_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_set_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        channel_id = getIntent().getStringExtra("channel_id");
        set_description = getIntent().getStringExtra("set_description");
        set_name = getIntent().getStringExtra("set_name");
        set_id = getIntent().getStringExtra("set_id");
        userId = getIntent().getStringExtra("userId");
        setTitle(set_name);

        // Locate the ViewPager in viewpager_main.xml
        viewPager_Cards = (ViewPager) findViewById(R.id.viewPager_Cards);
        getSetLists();
    }

    public void getSetLists() {
        try {

            if (CheckNetworkConnection.isOnline(MySetCards.this)) {
                showProgress();
                Call<CardsListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(MySetCards.this).getCardsList(set_id);
                callRegisterUser.enqueue(new ApiCallback<CardsListResponse>(MySetCards.this) {
                    @Override
                    public void onApiResponse(Response<CardsListResponse> response, boolean isSuccess, String message) {
                        CardsListResponse cardsListResponse = response.body();
                        if (isSuccess) {

                            if (cardsListResponse != null && cardsListResponse.getImages() != null && cardsListResponse.getImages().size() != 0) {

                                cardsListModels = cardsListResponse.getImages();
                                dismissProgress();

                            } else {
                                /*channelSet_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);*/
                                dismissProgress();
                            }
                            CardsListModel dummyCardObj=new CardsListModel();
                            cardsListModels.add(dummyCardObj);
                            setAdapter(cardsListModels);

                        } else {
                            showLongToast(MySetCards.this, message);
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

    private void setAdapter(List<CardsListModel> cardsListModels) {

        // Pass results to ViewPagerAdapter Class
        cardsPagerAdapter = new ViewPagerAdapter(MySetCards.this, cardsListModels, set_id, userId, set_name);
        // Binds the Adapter to the ViewPager
        viewPager_Cards.setAdapter(cardsPagerAdapter);
        cardsPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_set_cards, menu);
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

            case R.id.setInfo_Edit:
                Intent intent = new Intent(MySetCards.this, EditSetInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("channel_id", channel_id);
                intent.putExtra("set_id", set_id);
                intent.putExtra("set_name", set_name);
                intent.putExtra("set_description", set_description);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                return true;

            case R.id.cardInfo_Edit:

                return true;

            case R.id.card_reorder:

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
