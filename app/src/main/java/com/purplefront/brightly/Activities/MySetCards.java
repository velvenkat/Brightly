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
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Fragments.ItemsAddFragment;
import com.purplefront.brightly.Fragments.Notifications;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CardsListResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.NotificationsSetDetail;
import com.purplefront.brightly.Modules.SetsListModel;
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

    ImageView card_list_icon;

    Realm realm;
    RealmResults<RealmModel> realmModel;

    TextView pager_count;
    String count;
    String pager_size;
    TextView view_nodata;
    String Created_By = "";
    String userId;
    String channel_id = "";
    String channel_name = "";
    String set_description = "";
    String set_name = "";
    String set_id = "";
    String share_link = "";
    boolean isNotification;
    int Cur_PagrPosition;
    int UPDATECARD = 102;
    ImageView image_createCard;
    SetsListModel setsListModel;
    ChannelListModel chl_list_obj;
    NotificationsModel notificationsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_set_cards);

        userId = getIntent().getStringExtra("userId");

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        if(userId == null) {
            for (RealmModel model : realmModel) {
                userId = model.getUser_Id();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        isNotification = getIntent().getBooleanExtra("isNotification", false);

        if(isNotification)
        {
            notificationsModel = getIntent().getParcelableExtra("notfy_modl_obj");
            channel_id = notificationsModel.getChannel_id();
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
            Created_By = notificationsModel.getNotificationsSetDetail().getCreated_by();

        }
        else {

            chl_list_obj = getIntent().getParcelableExtra("model_obj");
            channel_id = chl_list_obj.getChannel_id();
            channel_name = chl_list_obj.getChannel_name();
            Created_By = chl_list_obj.getCreated_by();

            setsListModel = getIntent().getParcelableExtra("setsListModel");
            set_description = setsListModel.getDescription();
            set_name = setsListModel.getSet_name();
            set_id = setsListModel.getSet_id();
            share_link = setsListModel.getShare_link();
        }

        setTitle(set_name);

        pager_count = (TextView) findViewById(R.id.pager_count);
        view_nodata = (TextView) findViewById(R.id.view_nodata);

        // Locate the ViewPager in viewpager_main.xml
        viewPager_Cards = (ViewPager) findViewById(R.id.viewPager_Cards);
        image_createCard = (ImageView) findViewById(R.id.image_createCard);
        card_list_icon = (ImageView) findViewById(R.id.card_list_icon);

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
                intent.putExtra("setsListModel", setsListModel);
                intent.putExtra("model_obj", chl_list_obj);
                intent.putExtra("isCreate_Crd",true);
                startActivityForResult(intent,UPDATECARD);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

        card_list_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(MySetCards.this, CardList.class);
                intent2.putExtra("userId", userId);
                intent2.putExtra("setsListModel", setsListModel);
                intent2.putExtra("re_order", false);
                intent2.putExtra("card_position", Cur_PagrPosition);
                startActivity(intent2);
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
                                view_nodata.setVisibility(View.GONE);
                                dismissProgress();

                            } else {
                                view_nodata.setVisibility(View.VISIBLE);
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

                if(isNotification) {

                    Intent intent = new Intent(MySetCards.this, EditSetInfo.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("notfy_modl_obj", notificationsModel);
                    intent.putExtra("isNotification", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }
                else
                {
                    Intent intent = new Intent(MySetCards.this, EditSetInfo.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("model_obj", chl_list_obj);
                    intent.putExtra("setsListModel", setsListModel);
                    intent.putExtra("isNotification", false);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }

                return true;

            case R.id.cardInfo_Edit:

                if (cardsListModels.size()!=0) {
                    Intent intent1 = new Intent(MySetCards.this, CreateCards.class);

                    intent1.putExtra("userId", userId);
                    intent1.putExtra("setsListModel", setsListModel);
                    intent1.putExtra("model_obj", chl_list_obj);
                    intent1.putExtra("isCreate_Crd",false);
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
                intent2.putExtra("setsListModel", setsListModel);
                intent2.putExtra("re_order", true);
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
