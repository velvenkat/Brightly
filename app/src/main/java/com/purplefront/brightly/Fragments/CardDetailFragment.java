package com.purplefront.brightly.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.ViewCardFragmentPagerAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CardsListResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;


public class CardDetailFragment extends BaseFragment {

    // Declare Variables

    ViewPager viewPager_Cards;
    ViewCardFragmentPagerAdapter cardsPagerAdapter;
    ArrayList<CardsListModel> cardsListModels = new ArrayList<>();

    //ImageView card_list_icon;

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
    public boolean isYouTubeInitializing = false;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
       Toast.makeText(getContext(), "isHidden" + hidden, Toast.LENGTH_LONG).show();
        if (!hidden) {
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
            boolean isCardClicked = ((BrightlyNavigationActivity) getActivity()).isCardClicked;
            if (isCardClicked) {
                int card_toPosition = ((BrightlyNavigationActivity) getActivity()).card_toPosition;
                viewPager_Cards.setCurrentItem(card_toPosition);
            }
            boolean isCardRefresh = ((BrightlyNavigationActivity) getActivity()).isCardRefresh;
            if (isCardRefresh) {
                ((BrightlyNavigationActivity) getActivity()).isCardRefresh = false;

                getCardsLists();
            }
            if (isNotification) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(notificationsModel.getChannel_name());
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(notificationsModel.getNotificationsSetDetail().getName());
            } else {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(channel_name);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(setsListModel.getSet_name());

            }
        }

    }

    String set_description = "";
    String set_name = "";
    String set_id = "";
    String share_link = "";
    boolean isNotification;
    int Cur_PagrPosition;
    int UPDATECARD = 102;
    ImageView image_createCard, image_Comment;
    SetsListModel setsListModel;
    ChannelListModel chl_list_obj;
    NotificationsModel notificationsModel;
    View rootView;
    String card_order_position;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_my_set_cards, container, false);
        //  setContentView(R.layout.activity_my_set_cards);
       Toast.makeText(getContext(), "On Create Called", Toast.LENGTH_LONG).show();
        userId = ((BrightlyNavigationActivity) getActivity()).userId;
        setHasOptionsMenu(true);
        //  userId = getIntent().getStringExtra("userId");
/*
        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        if(userId == null) {
            for (RealmModel model : realmModel) {
                userId = model.getUser_Id();
            }
        }*/

        Bundle bundle = getArguments();
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
        if (bundle != null) {
            isNotification = bundle.getBoolean("isNotification", false);
        }

        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            channel_id = notificationsModel.getChannel_id();
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
            Created_By = notificationsModel.getNotificationsSetDetail().getCreated_by();
            card_order_position = notificationsModel.getCard_order_position();
            channel_name = notificationsModel.getChannel_name();


        } else {

            chl_list_obj = bundle.getParcelable("model_obj");
            channel_id = chl_list_obj.getChannel_id();
            channel_name = chl_list_obj.getChannel_name();
            Created_By = chl_list_obj.getCreated_by();

            setsListModel = bundle.getParcelable("setsListModel");
            set_description = setsListModel.getDescription();
            set_name = setsListModel.getSet_name();
            set_id = setsListModel.getSet_id();
            share_link = setsListModel.getShare_link();
        }

        //setTitle(set_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(channel_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);
        pager_count = (TextView) rootView.findViewById(R.id.pager_count);
        view_nodata = (TextView) rootView.findViewById(R.id.view_nodata);

        // Locate the ViewPager in viewpager_main.xml
        viewPager_Cards = (ViewPager) rootView.findViewById(R.id.viewPager_Cards);
        image_createCard = (ImageView) rootView.findViewById(R.id.image_createCard);
        image_Comment = (ImageView) rootView.findViewById(R.id.image_Comment);

        if (!userId.equalsIgnoreCase(Created_By)) {
            image_createCard.setVisibility(View.GONE);
            image_Comment.setVisibility(View.VISIBLE);
        } else {
            image_createCard.setVisibility(View.VISIBLE);
            image_Comment.setVisibility(View.GONE);
        }
        image_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Fragment card_frag = new ItemsAddFragment();
                Bundle bundle = new Bundle();
                bundle.putString("set_id", set_id);
                bundle.pu`tString("userId", userId);
                bundle.putString("set_name", set_name);
                card_frag.setArguments(bundle);*/
               /* Intent intent = new Intent(MySetCards.this, CreateCardsFragment.class);
                intent.putExtra("userId", userId);
                intent.putExtra("setsListModel", setsListModel);
                intent.putExtra("model_obj", chl_list_obj);
                intent.putExtra("isCreate_Crd",true);
                startActivityForResult(intent,UPDATECARD);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                setBottomDialog();

            }
        });

        image_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("set_id", set_id);
                bundle.putString("set_name", set_name);
                bundle.putString("userId", userId);
                bundle.putString("channel_name", channel_name);
                Fragment cmt_frag = new CommentsFragment();
                cmt_frag.setArguments(bundle);
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Comments, cmt_frag, true);

            }
        });


        getCardsLists();

      /*  rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    *//*getActivity().finish();*//*
                    Fragment set_frag = new SetsFragment();
                    Bundle bundle1=new Bundle();

                    bundle1.putParcelable("model_obj", chl_list_obj);
                    set_frag.setArguments(bundle1);

                    ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.Set_List, set_frag, false);
                    return true;
                }
                return false;
            }
        });*/

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //         getActivity().finish();

                    if (isNotification) {
                        /* Fragment chnl_frag = new ChannelFragment();
                         *//*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new Notifications(),
                            Util.NOTIFICATIONS).commit();
*//*
                        ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.CHANNELS, chnl_frag, false);*/
                        getActivity().finish();
                        simpleIntent(getActivity(), BrightlyNavigationActivity.class);
                    } else {

                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false);
                    }
                    return true;
                }
                return false;
            }
        });

        return rootView;
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

    public void setBottomDialog() {
        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_view_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        menu_list.add("Create new card");
        menu_list.add("Create card from existing set");

        ArrayAdapter<String> menu_itmes = new ArrayAdapter<String>(getContext(), R.layout.menu_row_diualog, R.id.dialog_menu_textView,
                menu_list);
        list_SettingsMenu.setAdapter(menu_itmes);
        list_SettingsMenu.requestFocus();
        Button btnCancel = (Button) mBottomSheetDialog.getWindow().findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        list_SettingsMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBottomSheetDialog.dismiss();
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:

                        bundle.putParcelable("setsListModel", setsListModel);
                        bundle.putParcelable("model_obj", chl_list_obj);
                        bundle.putBoolean("isCreate_Crd", true);
                        Fragment crt_crd_frag = new CreateCardsFragment();
                        crt_crd_frag.setArguments(bundle);
                        ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Card, crt_crd_frag, true);
                        break;
                    case 1:

                        bundle.putString("Set_ID_toCreateCard",setsListModel.getSet_id());

                        bundle.putString("type","all");
                        Fragment channel_frag = new ChannelFragment();
                        channel_frag.setArguments(bundle);

                        ((BrightlyNavigationActivity) getActivity()).isHide_frag=true;
                        ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.CHANNELS, channel_frag, true);
                        break;
                }
            }
            }
        );

    }

    public void getCardsLists() {
        try {
            showProgress();
            if (CheckNetworkConnection.isOnline(getContext())) {

                Call<CardsListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getCardsList(set_id);
                callRegisterUser.enqueue(new ApiCallback<CardsListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<CardsListResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        ((BrightlyNavigationActivity)getActivity()).DontRun=false;
                        CardsListResponse cardsListResponse = response.body();
                        if (isSuccess) {

                            if (cardsListResponse != null && cardsListResponse.getData() != null && cardsListResponse.getData().size() != 0) {

                                cardsListModels = new ArrayList<>(cardsListResponse.getData());
                                view_nodata.setVisibility(View.GONE);


                            } else {
                                view_nodata.setVisibility(View.VISIBLE);
                            }
                /*            CardsListModel dummyCardObj=new CardsListModel();
                            cardsListModels.add(dummyCardObj);*/
                            setAdapter(cardsListModels);

                        } else {
                            showLongToast(getActivity(), message);
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
            dismissProgress();
            e.printStackTrace();


        }

    }

    private void setAdapter(List<CardsListModel> cardsListModels) {

        // Pass results to ViewPagerAdapter Class
        // cardsPagerAdapter = new ViewPagerAdapter(MySetCards.this, cardsListModels, set_id, userId, set_name);
        // Binds the Adapter to the ViewPager
        List<Fragment> cardFragList = new ArrayList<>();
        for (int i = 0; i < cardsListModels.size(); i++) {
            Fragment card_frag = null;

            CardsListModel cardObj = cardsListModels.get(i);

            Bundle bundle = new Bundle();
            bundle.putParcelable("card_mdl_obj", cardObj);


            card_frag = new Multimedia_CardFragment();


            bundle.putString("set_id", set_id);
            bundle.putString("userId", userId);
            bundle.putString("set_name", set_name);
            card_frag.setArguments(bundle);
            cardFragList.add(card_frag);
        }
        cardsPagerAdapter = new ViewCardFragmentPagerAdapter(getContext(), getChildFragmentManager(), cardFragList, set_id, userId, set_name);
        viewPager_Cards.setAdapter(cardsPagerAdapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isYouTubeInitializing) {
                    if (isNotification && card_order_position != null) {
                        viewPager_Cards.setCurrentItem(Integer.parseInt(card_order_position));
                        card_order_position = null;
                    }
                }
            }
        }, 100);

//        viewPager_Cards.setCurrentItem(3);
        // cardsPagerAdapter.notifyDataSetChanged();
        viewPager_Cards.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                pager_size = String.valueOf(cardsListModels.size());
                count = String.valueOf(position + 1);
                pager_count.setText(count + "/" + pager_size);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        if (userId.equalsIgnoreCase(Created_By)) {
            inflater.inflate(R.menu.my_set_cards, menu);
        } else
            inflater.inflate(R.menu.my_sub_cards, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {

            case R.id.set_CommentsList:

                Bundle bundle_list = new Bundle();
                bundle_list.putString("set_id", set_id);
                bundle_list.putString("set_name", set_name);
                bundle_list.putString("userId", userId);
                bundle_list.putString("channel_name", channel_name);
                Fragment cmt_list_frag = new CommentListFragment();
                cmt_list_frag.setArguments(bundle_list);
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Comments_List, cmt_list_frag, true);

                return true;

            case R.id.setInfo_Edit:

                if (isNotification) {

                   /* Intent intent = new Intent(CardDetailFragment.this, EditSetInfo.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("notfy_modl_obj", notificationsModel);
                    intent.putExtra("isNotification", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/

                    Fragment edit_set_info = new EditSetInfo();
                    bundle.putParcelable("notfy_modl_obj", notificationsModel);
                    bundle.putBoolean("isNotification", true);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag=true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Set, edit_set_info, true);

                } else {
                    /*Intent intent = new Intent(CardDetailFragment.this, EditSetInfo.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("model_obj", chl_list_obj);
                    intent.putExtra("setsListModel", setsListModel);
                    intent.putExtra("isNotification", false);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                    Fragment edit_set_info = new EditSetInfo();
                    bundle.putParcelable("model_obj", chl_list_obj);
                    bundle.putParcelable("setsListModel", setsListModel);
                    bundle.putBoolean("isNotification", false);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag=true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Set, edit_set_info, true);
                }

                return true;

            case R.id.cardInfo_Edit:

                if (cardsListModels.size() != 0) {

                   /* Intent intent1 = new Intent(CardDetailFragment.this, CreateCardsFragment.class);

                    intent1.putExtra("userId", userId);
                    intent1.putExtra("setsListModel", setsListModel);
                    intent1.putExtra("model_obj", chl_list_obj);
                    intent1.putExtra("isCreate_Crd",false);
                    intent1.putExtra("Card_Dtls", cardsListModels.get(Cur_PagrPosition));

                    //intent1.putExtra("my_card_bundle",bundle);
                    startActivityForResult(intent1, UPDATECARD);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                    Fragment frag = new CreateCardsFragment();
                    bundle.putParcelable("setsListModel", setsListModel);
                    bundle.putParcelable("model_obj", chl_list_obj);
                    bundle.putBoolean("isCreate_Crd", false);
                    bundle.putParcelable("Card_Dtls", cardsListModels.get(Cur_PagrPosition));
                    frag.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Card, frag, true);

                } else {
                    showLongToast(getActivity(), "No Card to Edit");

                }

                return true;
            case R.id.card_reorder:

               /* Intent intent2 = new Intent(CardDetailFragment.this, CardList.class);
                intent2.putExtra("userId", userId);
                intent2.putExtra("setsListModel", setsListModel);
                intent2.putExtra("re_order", true);
                startActivity(intent2);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
               */
                Fragment frag = new CardList();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("setsListModel", setsListModel);
                bundle1.putBoolean("re_order", true);
                bundle1.putString("chl_name", channel_name);
                bundle1.putInt("card_position", Cur_PagrPosition);
                frag.setArguments(bundle1);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag=true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, frag, false);

                return true;
            case R.id.action_card_list:
                Fragment frag1 = new CardList();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("setsListModel", setsListModel);
                bundle2.putParcelable("notfy_modl_obj", notificationsModel);
                if (isNotification) {
                    bundle2.putBoolean("isNotification", true);
                } else {
                    bundle2.putBoolean("isNotification", false);
                }
                bundle2.putBoolean("re_order", false);
                bundle2.putString("chl_name", channel_name);
                bundle2.putInt("card_position", Cur_PagrPosition);
                frag1.setArguments(bundle2);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag=true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, frag1, false);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void move_card() {

        if (isNotification && card_order_position != null) {
            viewPager_Cards.setCurrentItem(Integer.parseInt(card_order_position));
            card_order_position = null;

        }
        isYouTubeInitializing = false;
    }
}
