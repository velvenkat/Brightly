package com.purplefront.brightly.Activities;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Adapters.ViewCardFragmentPagerAdapter;
import com.purplefront.brightly.Adapters.ViewPagerAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Fragments.ItemsAddFragment;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CardsListResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.SharedDataModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;


public class MySetCards extends BaseActivity {

    // Declare Variables

    ViewPager viewPager_Cards;
    ViewCardFragmentPagerAdapter cardsPagerAdapter;
    ArrayList<CardsListModel> cardsListModels = new ArrayList<>();

    Realm realm;
    RealmResults<RealmModel> realmModel;

    TextView pager_count;
    String count;
    String pager_size;

    String Created_By = "";
    String userId;
    String channel_id = "";
    String channel_name = "";
    String set_description = "";
    String set_name = "";
    String set_id = "";
    String share_link = "";
    int Cur_PagrPosition;
    int UPDATECARD = 102;
    ImageView image_createCard;
    ArrayList<SharedDataModel> sharedDataModels;
    ChannelListModel chl_list_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_set_cards);

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for(RealmModel model:realmModel){
            userId = model.getUser_Id();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        chl_list_obj=getIntent().getParcelableExtra("model_obj");

        userId = getIntent().getStringExtra("userId");
        channel_id=chl_list_obj.getChannel_id();
        channel_name=chl_list_obj.getChannel_name();
        Created_By = chl_list_obj.getCreated_by();
        set_description = getIntent().getStringExtra("set_description");
        set_name = getIntent().getStringExtra("set_name");
        set_id = getIntent().getStringExtra("set_id");
        share_link = getIntent().getStringExtra("share_link");
        sharedDataModels = getIntent().getParcelableArrayListExtra("sharedDataModels");
        setTitle(set_name);

        pager_count = (TextView) findViewById(R.id.pager_count);

        // Locate the ViewPager in viewpager_main.xml
        viewPager_Cards = (ViewPager) findViewById(R.id.viewPager_Cards);
        image_createCard = (ImageView) findViewById(R.id.image_createCard);
        if (!userId.equalsIgnoreCase(Created_By)) {
            image_createCard.setVisibility(View.GONE);
        } else {
            image_createCard.setVisibility(View.VISIBLE);
        }
        image_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Fragment card_frag = new ItemsAddFragment();
                Bundle bundle = new Bundle();
                bundle.putString("set_id", set_id);
                bundle.putString("userId", userId);
                bundle.putString("set_name", set_name);
                card_frag.setArguments(bundle);*/
                Intent intent = new Intent(MySetCards.this, CreateCards.class);
                intent.putExtra("userId", userId);
                intent.putExtra("set_id", set_id);
                intent.putExtra("set_name", set_name);
                intent.putExtra("model_obj", chl_list_obj);
                startActivityForResult(intent,UPDATECARD);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        getCardsLists();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UPDATECARD) {
                getCardsLists();
            }
        }
    }

    public void getCardsLists() {
        try {

            if (CheckNetworkConnection.isOnline(MySetCards.this)) {
                showProgress();
                Call<CardsListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(MySetCards.this).getCardsList(set_id);
                callRegisterUser.enqueue(new ApiCallback<CardsListResponse>(MySetCards.this) {
                    @Override
                    public void onApiResponse(Response<CardsListResponse> response, boolean isSuccess, String message) {
                        CardsListResponse cardsListResponse = response.body();
                        if (isSuccess) {

                            if (cardsListResponse != null && cardsListResponse.getData() != null && cardsListResponse.getData().size() != 0) {

                                cardsListModels = new ArrayList<>(cardsListResponse.getData());
                                dismissProgress();

                            } else {
                                /*channelSet_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);*/
                                dismissProgress();
                            }
                /*            CardsListModel dummyCardObj=new CardsListModel();
                            cardsListModels.add(dummyCardObj);*/
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
        // cardsPagerAdapter = new ViewPagerAdapter(MySetCards.this, cardsListModels, set_id, userId, set_name);
        // Binds the Adapter to the ViewPager
        cardsPagerAdapter = new ViewCardFragmentPagerAdapter(this, getSupportFragmentManager(), cardsListModels, set_id, userId, set_name);
        viewPager_Cards.setAdapter(cardsPagerAdapter);
        // cardsPagerAdapter.notifyDataSetChanged();
        viewPager_Cards.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                pager_size = String.valueOf(cardsListModels.size());
                count = String.valueOf(position+1);
                pager_count.setText(count +"/"+ pager_size);

            }

            @Override
            public void onPageSelected(int position) {
                Cur_PagrPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (userId.equalsIgnoreCase(Created_By)) {
            getMenuInflater().inflate(R.menu.my_set_cards, menu);
        } else
            getMenuInflater().inflate(R.menu.my_sub_cards, menu);
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
                intent.putExtra("set_id", set_id);
                intent.putExtra("set_name", set_name);
                intent.putExtra("set_description", set_description);
                intent.putExtra("share_link", share_link);
                intent.putExtra("model_obj", chl_list_obj);
                intent.putParcelableArrayListExtra("sharedDataModels", sharedDataModels);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                return true;

            case R.id.cardInfo_Edit:

                if (Cur_PagrPosition < cardsListModels.size() - 1) {
                    Intent intent1 = new Intent(MySetCards.this, CreateCards.class);

                    intent1.putExtra("userId", userId);
                    intent1.putExtra("set_id", set_id);
                    intent1.putExtra("set_name", set_name);
                    intent1.putExtra("model_obj", chl_list_obj);
                    intent1.putExtra("Card_Dtls", cardsListModels.get(Cur_PagrPosition));

                    //intent1.putExtra("my_card_bundle",bundle);

                    startActivityForResult(intent1, UPDATECARD);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);


                } else {
                    showLongToast(MySetCards.this, "No Card to Edit");

                }

                return true;
            case R.id.card_reorder:

                Intent intent2 = new Intent(MySetCards.this, CardList.class);
                intent2.putExtra("userId", userId);
                intent2.putExtra("set_id", set_id);
                intent2.putExtra("set_name", set_name);
                startActivity(intent2);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
