package com.digital_easy.info_share.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digital_easy.info_share.Adapters.CardListAdapter;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.CardOptionsMenuAdapter;
import com.digital_easy.info_share.Adapters.ViewCardFragmentPagerAdapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.CardsListModel;
import com.digital_easy.info_share.Modules.CardsListResponse;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Modules.OptionsModel;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;


public class CardDetailFragment extends BaseFragment implements CardOptionsMenuAdapter.OptsMenuInterface, CardListAdapter.Card_sel_interface {

    // Declare Variables
    Dialog mCardOptsMenuDlg;
    ViewPager viewPager_Cards;
    ViewCardFragmentPagerAdapter cardsPagerAdapter;
    public ArrayList<CardsListModel> cardsListModels = new ArrayList<>();
    public static final String CMDPAUSE = "pause";
    ImageView imgPrev, imgNext;
    //  BottomNavigationView bottom_navigation;
    ArrayList<OptionsModel> opts_List = new ArrayList<>();


    public MediaPlayer cur_scrn_mediaPlayerObj = null, first_mediaPlayerObj = null;
    //ImageView card_list_icon;

    Realm realm;
    RealmResults<RealmModel> realmModel;

    TextView pager_count;
    String count;
    String pager_size;
    TextView view_nodata;
    // public boolean isVideo_Media_ctrl_show;
    String Created_By = "";
    //  String userId;
    String channel_id = "";
    String channel_name = "";
    public boolean isYouTubeInitializing = false;
    public int Card_CurrentPos = 0;


    String set_description = "";
    String set_name = "";
    String set_id = "";
    String share_link = "";
    boolean isNotification;
    int Cur_PagrPosition;
    int UPDATECARD = 102;
    boolean isAddCardPressed = false;
    ImageView image_createCard;

    SimpleDraweeView image_Comment;
    SimpleDraweeView image_Share;
    SetsListModel setsListModel;
    ChannelListModel chl_list_obj;
    NotificationsModel notificationsModel;
    View rootView;
    RealmModel userObj;

    String card_order_position = null;
    RelativeLayout rl_pgrContr;

    RecyclerView card_listview;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_my_set_cards, container, false);
        //  setContentView(R.layout.activity_my_set_cards);
        userObj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        imgPrev = (ImageView) rootView.findViewById(R.id.img_back);
        image_Share = rootView.findViewById(R.id.image_Share);
        imgNext = (ImageView) rootView.findViewById(R.id.img_next);
        card_listview = (RecyclerView) rootView.findViewById(R.id.card_listview);
        rl_pgrContr = (RelativeLayout) rootView.findViewById(R.id.rl_pgrContr);
        viewPager_Cards = (ViewPager) rootView.findViewById(R.id.viewPager_Cards);
        //  bottom_navigation = (BottomNavigationView) rootView.findViewById(R.id.bottom_navigation);

        setHasOptionsMenu(true);

       /* rl_pgrContr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomNavigation();
            }
        });*/


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

        /*new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.list_shurela_tracks,getActivity()))
                .setStyle(R.style.YourStyle)

                .setContentTitle("Just touch")
                .setContentText("Touch to ....")
                .hideOnTouchOutside().build();*/

      /*  new MaterialShowcaseView.Builder(getActivity())
                .setTarget(viewPager_Cards)
                .setDismissText("GOT IT")
                .setDismissTextColor(R.color.black)
                .setContentText("View Pager")
                .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("2") // provide a unique ID used to ensure it is only shown once
                .show();
*/
        Bundle bundle = getArguments();
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
       /* viewPager_Cards.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showBottomNavigation();
                return true;
            }
        });*/
        /*

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

            // chl_list_obj = bundle.getParcelable("model_obj");
            chl_list_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;
            if (chl_list_obj == null) {
                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
            } else {
                channel_id = chl_list_obj.getChannel_id();
                channel_name = chl_list_obj.getChannel_name();
                Created_By = chl_list_obj.getCreated_by();

                setsListModel = bundle.getParcelable("setsListModel");
                set_description = setsListModel.getDescription();
                set_name = setsListModel.getSet_name();
                set_id = setsListModel.getSet_id();
                share_link = setsListModel.getShare_link();
            }
        }

        //setTitle(set_name);
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(set_name);
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(channel_name);
        pager_count = (TextView) rootView.findViewById(R.id.pager_count);
        view_nodata = (TextView) rootView.findViewById(R.id.view_nodata);

        // Locate the ViewPager in viewpager_main.xml

        view_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomNavigation();
            }
        });
        image_createCard = (ImageView) rootView.findViewById(R.id.image_createCard);
        image_Comment = (SimpleDraweeView) rootView.findViewById(R.id.image_Comment);
        //  bottom_navigation.setVisibility(View.GONE);
        if (!userObj.getUser_Id().equalsIgnoreCase(Created_By)) {
            image_createCard.setVisibility(View.GONE);

            // image_Comment.setVisibility(View.VISIBLE);

        } else {
            image_createCard.setVisibility(View.VISIBLE);
            //  image_Comment.setVisibility(View.GONE);
        }
        image_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (share_link.trim().equals("")) {
                    //   call_api_card_share(card_shr_card_id, chl_list_obj.getChannel_id(), false);
                } else {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                    // Add data to the intent, the receiving app will decide
                    // what to do with it.
                    share.putExtra(Intent.EXTRA_SUBJECT, "Brightly Set Share link");
                    share.putExtra(Intent.EXTRA_TEXT, share_link);

                    startActivity(Intent.createChooser(share, "Share link!"));
                }
            }
        });
        if (setsListModel.getWeb_sharing().equalsIgnoreCase("1")) {
            image_Share.setVisibility(View.VISIBLE);
            set_SimplaeDraweeImage(image_Share, ((BrightlyNavigationActivity) getActivity()).SHARE_DEF_IMAGE);

        } else {
            image_Share.setVisibility(View.GONE);
        }
        imgNext.setVisibility(View.GONE);
        imgPrev.setVisibility(View.GONE);


        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card_CurrentPos++;
                //   Toast.makeText(getContext(), "CardPos" + Card_CurrentPos, Toast.LENGTH_LONG).show();
                if (Card_CurrentPos <= cardsListModels.size() - 1) {
                    // Card_CurrentPos = 0;
                    viewPager_Cards.setCurrentItem(Card_CurrentPos);
                } else {
                    Card_CurrentPos = 0;
                    viewPager_Cards.setCurrentItem(Card_CurrentPos);
                }

                /*if (Card_CurrentPos == 0) {
                    imgPrev.setVisibility(View.GONE);
                } else
                    imgPrev.setVisibility(View.VISIBLE);
*/


            }
        });
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card_CurrentPos--;
                if (Card_CurrentPos == -1) {
                    Card_CurrentPos = cardsListModels.size() - 1;
                }
  /*              if (Card_CurrentPos == 0) {
                    imgPrev.setVisibility(View.GONE);
                } else
                    imgPrev.setVisibility(View.VISIBLE);*/
                viewPager_Cards.setCurrentItem(Card_CurrentPos);
            }
        });

        viewPager_Cards.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              /*  pager_size = String.valueOf(cardsListModels.size());
                count = String.valueOf(position + 1);
                pager_count.setText(count + "/" + pager_size);*/

            }

            @Override
            public void onPageSelected(int position) {

                Card_CurrentPos = position;
                pager_size = String.valueOf(cardsListModels.size());
                count = String.valueOf(position + 1);
                pager_count.setText(count + "/" + pager_size);
                setComment_Image();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
               /* if (state == SCROLL_STATE_IDLE) {
                    showBottomNavigation();
                }*/
            }
        });
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
                //  setBottomDialog();
                Bundle cc_bundle = new Bundle();
                cc_bundle.putParcelable("setsListModel", setsListModel);
                //      bundle.putParcelable("model_obj", chl_list_obj);
                cc_bundle.putBoolean("isCreateCard", true);
                Fragment crt_crd_frag = new CreateMultimediaCard();
                crt_crd_frag.setArguments(cc_bundle);
                cardsListModels = null;
                Card_CurrentPos = 0;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Card, crt_crd_frag, true);
            }
        });

        image_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String CardID = cardsListModels.get(Card_CurrentPos).getCard_id();

                String card_created_by = cardsListModels.get(Card_CurrentPos).getCreated_by();

                Bundle bundle_comnt = new Bundle();
                if (userObj.getUser_Id().equals(card_created_by)) {
                    bundle_comnt.putBoolean("isOwnCard", true);
                } else {
                    bundle_comnt.putBoolean("isOwnCard", false);
                }

                bundle_comnt.putString("card_id", CardID);
                bundle_comnt.putString("set_name", set_name);
                bundle_comnt.putString("set_id", set_id);
                bundle_comnt.putString("userId", userObj.getUser_Id());
                bundle_comnt.putString("card_crtd_by", card_created_by);
                bundle_comnt.putString("channel_name", channel_name);
                if (!card_created_by.equals(userObj.getUser_Id())) {

                    Fragment cmt_frag = new CommentsFragment();
                    cmt_frag.setArguments(bundle_comnt);
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Comments, cmt_frag, true);
                } else {
                    Fragment cmt_frag = new ChatListFragment();
                    cmt_frag.setArguments(bundle_comnt);
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Chat_list, cmt_frag, true);
                }

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
                } else
                    return false;
            }
        });

        return rootView;
    }

    public void showHidePageNavigation() {
        imgNext.setVisibility(View.VISIBLE);
        imgPrev.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgNext.setVisibility(View.GONE);
                imgPrev.setVisibility(View.GONE);
            }
        }, 2000);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        //   Toast.makeText(getContext(),"Menu Prepared",Toast.LENGTH_LONG).show();
        MenuInflater inflater = getActivity().getMenuInflater();
        if (cardsListModels != null)
            if (cardsListModels.size() == 0) {
                if (Created_By.equalsIgnoreCase(userObj.getUser_Id())) {
                    inflater.inflate(R.menu.my_set_cards_empty, menu);
                } else {
                    inflater.inflate(R.menu.my_sub_cards_empty, menu);
                }
            } else if (!isNotification) {
                if (cardsListModels.get(Card_CurrentPos).getCreated_by().equalsIgnoreCase(userObj.getUser_Id())) {
                    inflater.inflate(R.menu.my_set_cards, menu);
                } else if (chl_list_obj.getCreated_by().equalsIgnoreCase(userObj.getUser_Id())) {
                    inflater.inflate(R.menu.my_set_cards_other, menu);
                } else if (!setsListModel.getShare_access().equalsIgnoreCase("1"))
                    inflater.inflate(R.menu.my_sub_cards, menu);
                else {
                    // inflater.inflate(R.menu.my_sub_card_share, menu);
                }

            } else {
                if (!notificationsModel.getNotificationsSetDetail().getShare_access().equalsIgnoreCase("1"))
                    inflater.inflate(R.menu.my_sub_cards, menu);
                else {
                    //   inflater.inflate(R.menu.my_sub_card_share, menu);
                }
            }

      /*  Menu navigationMenu = bottom_navigation.getMenu();
        navigationMenu.clear();*/
        //navigationMenu.add(100,1001,)
        // MenuItem item;
        // OptionsModel model = new OptionsModel();
        if (cardsListModels != null)
            if (cardsListModels.size() == 0) {
                if (Created_By.equalsIgnoreCase(userObj.getUser_Id())) {


                    //  inflater.inflate(R.menu.my_set_cards_empty, menu);
                    opts_List = new ArrayList<>();
                    OptionsModel model = new OptionsModel();
                    model.opts_name = "Edit " + ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR;
                    model.img_url = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
                    model.Opts_Type = TYPES.SET.ordinal();
                    opts_List.add(model);
                  /*  OptionsModel model2 = new OptionsModel();
                    model2.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " list";
                    model2.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model2.Opts_Type = TYPES.CARD_LIST.ordinal();
                    opts_List.add(model2);
                  */  // OptionsModel model1=new OptionsModel();
                    OptionsModel model1 = new OptionsModel();
                    model1.opts_name = "Share settings";
                    model1.img_url = ((BrightlyNavigationActivity) getActivity()).SHARE_SETTING_IMG;
                    model1.Opts_Type = TYPES.SHARE_SETTINGS.ordinal();
                    opts_List.add(model1);
                } else {
                    //inflater.inflate(R.menu.my_sub_cards_empty, menu);
                    opts_List = new ArrayList<>();
                    OptionsModel model2 = new OptionsModel();
                    model2.opts_name = ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR + " details";
                    model2.img_url = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
                    model2.Opts_Type = TYPES.SET.ordinal();
                    opts_List.add(model2);


                    if (!isNotification) {
                        if (setsListModel.getShare_access().equalsIgnoreCase("1")) {
                            //inflater.inflate(R.menu.my_sub_cards, menu);
                            OptionsModel model3 = new OptionsModel();
                            model3.opts_name = "Share settings";
                            model3.img_url = ((BrightlyNavigationActivity) getActivity()).SHARE_SETTING_IMG;
                            model3.Opts_Type = TYPES.SHARE_SETTINGS.ordinal();
                            opts_List.add(model3);
                        }
                    } else {
                        if (notificationsModel.getNotificationsSetDetail().getShare_access().equalsIgnoreCase("1")) {
                            //inflater.inflate(R.menu.my_sub_cards, menu);
                            OptionsModel model3 = new OptionsModel();
                            model3.opts_name = "Share settings";
                            model3.img_url = ((BrightlyNavigationActivity) getActivity()).SHARE_SETTING_IMG;
                            model3.Opts_Type = TYPES.SHARE_SETTINGS.ordinal();
                            opts_List.add(model3);
                        }
                    }
                }
            } else if (!isNotification) {
                if (cardsListModels.get(Card_CurrentPos).getCreated_by().equalsIgnoreCase(userObj.getUser_Id())) {
                    //  inflater.inflate(R.menu.my_set_cards, menu);
                    opts_List = new ArrayList<>();
                    OptionsModel model = new OptionsModel();
                    model.opts_name = "Edit " + ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR;
                    model.img_url = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
                    model.Opts_Type = TYPES.SET.ordinal();
                    opts_List.add(model);
                    OptionsModel model1 = new OptionsModel();
                    model1.opts_name = "Edit " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR;
                    model1.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model1.Opts_Type = TYPES.EDIT_CARD.ordinal();
                    opts_List.add(model1);
                    OptionsModel model4 = new OptionsModel();
                    model4.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " list";
                    model4.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model4.Opts_Type = TYPES.CARD_LIST.ordinal();
                    opts_List.add(model4);
                    OptionsModel model2 = new OptionsModel();
                    model2.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " delete/Reorder";
                    model2.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model2.Opts_Type = TYPES.CARD_DELETE.ordinal();
                    opts_List.add(model2);
                    OptionsModel model3 = new OptionsModel();
                    model3.opts_name = "Share settings";
                    model3.img_url = ((BrightlyNavigationActivity) getActivity()).SHARE_SETTING_IMG;
                    model3.Opts_Type = TYPES.SHARE_SETTINGS.ordinal();
                    opts_List.add(model3);
                    OptionsModel model5 = new OptionsModel();
                    model5.opts_name = "Card Share";
                    model5.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model5.Opts_Type = TYPES.CARD_SHARE.ordinal();
                    opts_List.add(model5);
                } else if (chl_list_obj.getCreated_by().equalsIgnoreCase(userObj.getUser_Id())) {
                    // inflater.inflate(R.menu.my_set_cards_other, menu);
                    opts_List = new ArrayList<>();
                    OptionsModel model = new OptionsModel();
                    model.opts_name = "Edit " + ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR;
                    model.img_url = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
                    model.Opts_Type = TYPES.SET.ordinal();
                    opts_List.add(model);
                    OptionsModel model3 = new OptionsModel();
                    model3.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " list";
                    model3.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model3.Opts_Type = TYPES.CARD_LIST.ordinal();
                    opts_List.add(model3);
                    OptionsModel model1 = new OptionsModel();
                    model1.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " delete/Reorder";
                    model1.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model1.Opts_Type = TYPES.CARD_DELETE.ordinal();
                    opts_List.add(model1);
                    OptionsModel model2 = new OptionsModel();
                    model2.opts_name = "Share settings";
                    model2.img_url = ((BrightlyNavigationActivity) getActivity()).SHARE_SETTING_IMG;
                    model2.Opts_Type = TYPES.SHARE_SETTINGS.ordinal();
                    opts_List.add(model2);
                    OptionsModel model5 = new OptionsModel();
                    model5.opts_name = "Card Share";
                    model5.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model5.Opts_Type = TYPES.CARD_SHARE.ordinal();
                    opts_List.add(model5);
                } else if (!setsListModel.getShare_access().equalsIgnoreCase("1")) {
                    //inflater.inflate(R.menu.my_sub_cards, menu);
                    opts_List = new ArrayList<>();
                    OptionsModel model2 = new OptionsModel();
                    model2.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " list";
                    model2.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model2.Opts_Type = TYPES.CARD_LIST.ordinal();
                    opts_List.add(model2);
                    OptionsModel model = new OptionsModel();
                    model.opts_name = ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR + " details";
                    model.img_url = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
                    model.Opts_Type = TYPES.SET.ordinal();
                    opts_List.add(model);
                } else {
                    // inflater.inflate(R.menu.my_sub_card_share, menu);
                    opts_List = new ArrayList<>();
                    OptionsModel model = new OptionsModel();
                    model.opts_name = ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR + " details";
                    model.img_url = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
                    model.Opts_Type = TYPES.SET.ordinal();
                    opts_List.add(model);
                    OptionsModel model2 = new OptionsModel();
                    model2.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " list";
                    model2.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model2.Opts_Type = TYPES.CARD_LIST.ordinal();
                    opts_List.add(model2);
                    OptionsModel model1 = new OptionsModel();
                    model1.opts_name = "Share settings";
                    model1.img_url = ((BrightlyNavigationActivity) getActivity()).SHARE_SETTING_IMG;
                    model1.Opts_Type = TYPES.SHARE_SETTINGS.ordinal();
                    opts_List.add(model1);
                    OptionsModel model5 = new OptionsModel();
                    model5.opts_name = "Card Share";
                    model5.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model5.Opts_Type = TYPES.CARD_SHARE.ordinal();
                    opts_List.add(model5);
                }

            } else {
                if (!notificationsModel.getNotificationsSetDetail().getShare_access().equalsIgnoreCase("1")) {
                    //inflater.inflate(R.menu.my_sub_cards, menu);
                    opts_List = new ArrayList<>();
                    OptionsModel model2 = new OptionsModel();
                    model2.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " list";
                    model2.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model2.Opts_Type = TYPES.CARD_LIST.ordinal();
                    opts_List.add(model2);
                    OptionsModel model = new OptionsModel();
                    model.opts_name = ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR + " details";
                    model.img_url = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
                    model.Opts_Type = TYPES.SET.ordinal();
                    opts_List.add(model);
                } else {
                    //   inflater.inflate(R.menu.my_sub_card_share, menu);
                    // inflater.inflate(R.menu.my_sub_card_share, menu);
                    opts_List = new ArrayList<>();
                    OptionsModel model = new OptionsModel();
                    model.opts_name = ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR + " details";
                    model.img_url = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
                    model.Opts_Type = TYPES.SET.ordinal();
                    opts_List.add(model);
                    OptionsModel model2 = new OptionsModel();
                    model2.opts_name = ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " list";
                    model2.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model2.Opts_Type = TYPES.CARD_LIST.ordinal();
                    opts_List.add(model2);
                    OptionsModel model1 = new OptionsModel();
                    model1.opts_name = "Share settings";
                    model1.img_url = ((BrightlyNavigationActivity) getActivity()).SHARE_DEF_IMAGE;
                    model1.Opts_Type = TYPES.SHARE_SETTINGS.ordinal();
                    opts_List.add(model1);
                    OptionsModel model5 = new OptionsModel();
                    model5.opts_name = "Card Share";
                    model5.img_url = ((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE;
                    model5.Opts_Type = TYPES.CARD_SHARE.ordinal();
                    opts_List.add(model5);
                }
            }


    }


    /*  public void set_menu_icon(MenuItem item, String imgUrl) {
          Glide.with(this).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>(30, 30) {
              @Override
              public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                  item.setIcon(new BitmapDrawable(getResources(), resource));
              }
          });
      }
  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        if (first_mediaPlayerObj != null) {
            if (first_mediaPlayerObj.isPlaying()) {
                first_mediaPlayerObj.pause();
            }
        }
        if (cur_scrn_mediaPlayerObj != null) {
            if (cur_scrn_mediaPlayerObj.isPlaying()) {
                if (cur_scrn_mediaPlayerObj.isPlaying()) {
                    cur_scrn_mediaPlayerObj.pause();
                }
            }
        }
        switch (item.getItemId()) {


//            case R.id.action_shre_settings:
//                if (isNotification) {
//
//                   *//* Intent intent = new Intent(CardDetailFragment.this, EditSetInfo.class);
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("notfy_modl_obj", notificationsModel);
//                    intent.putExtra("isNotification", true);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*//*
//
//                    Fragment edit_set_info = new ShareSettings();
//                    bundle.putParcelable("notfy_modl_obj", notificationsModel);
//                    bundle.putBoolean("isNotification", true);
//                    edit_set_info.setArguments(bundle);
//                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
//                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Set_Share_settings, edit_set_info, true);
//
//                } else {
//                    *//*Intent intent = new Intent(CardDetailFragment.this, EditSetInfo.class);
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("model_obj", chl_list_obj);
//                    intent.putExtra("setsListModel", setsListModel);
//                    intent.putExtra("isNotification", false);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*//*
//                    Fragment edit_set_info = new ShareSettings();
//                    //  bundle.putParcelable("model_obj", chl_list_obj);
//                    bundle.putParcelable("setsListModel", setsListModel);
//                    bundle.putBoolean("isNotification", false);
//                    edit_set_info.setArguments(bundle);
//                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
//                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Set_Share_settings, edit_set_info, true);
//                }
//                return true;

            case R.id.setInfo_Edit:

                if (isNotification) {


                    Fragment edit_set_info = new EditSetInfo();
                    bundle.putParcelable("notfy_modl_obj", notificationsModel);
                    bundle.putBoolean("isNotification", true);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Set, edit_set_info, true);

                } else {

                    Fragment edit_set_info = new EditSetInfo();
                    //  bundle.putParcelable("model_obj", chl_list_obj);
                    bundle.putParcelable("setsListModel", setsListModel);
                    bundle.putBoolean("isNotification", false);
                    bundle.putBoolean("isCardDtlPage", true);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Set, edit_set_info, true);
                }

                return true;

            case R.id.cardInfo_Edit:


                Fragment frag = new CreateMultimediaCard();
                bundle.putParcelable("setsListModel", setsListModel);
                //  bundle.putParcelable("model_obj", chl_list_obj);
                bundle.putBoolean("isCreateCard", false);
                bundle.putParcelable("Card_Dtls", cardsListModels.get(Card_CurrentPos));
                frag.setArguments(bundle);
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Card, frag, true);


                return true;
//            case R.id.card_reorder:
//
//               *//* Intent intent2 = new Intent(CardDetailFragment.this, CardList.class);
//                intent2.putExtra("userId", userId);
//                intent2.putExtra("setsListModel", setsListModel);
//                intent2.putExtra("re_order", true);
//                startActivity(intent2);
//                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
//               *//*
//                Fragment cl_frag = new CardList();
//                Bundle bundle1 = new Bundle();
//                bundle1.putParcelable("setsListModel", setsListModel);
//                bundle1.putBoolean("re_order", true);
//                bundle1.putString("chl_name", chl_list_obj.getChannel_name());
//                bundle1.putInt("card_position", Card_CurrentPos);
//                cl_frag.setArguments(bundle1);
//                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
//                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, cl_frag, false);
//
//                return true;
            /*case R.id.action_card_list:
                Fragment frag1 = new CardList();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("setsListModel", setsListModel);
                bundle2.putParcelable("notfy_modl_obj", notificationsModel);
                if (isNotification) {
                    bundle2.putBoolean("isNotification", true);
                    bundle2.putString("chl_name", notificationsModel.getChannel_name());
                } else {
                    bundle2.putBoolean("isNotification", false);
                    bundle2.putString("chl_name", chl_list_obj.getChannel_name());
                }
                bundle2.putBoolean("re_order", false);

                bundle2.putInt("card_position", Card_CurrentPos);
                frag1.setArguments(bundle2);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, frag1, false);
                return true;
*/

            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onOptsMenuSelected(int Type) {
        TYPES typeChoosed = TYPES.values()[Type];

        mCardOptsMenuDlg.dismiss();
        Bundle bundle = new Bundle();
        if (first_mediaPlayerObj != null) {
            if (first_mediaPlayerObj.isPlaying()) {
                first_mediaPlayerObj.pause();
            }
        }
        if (cur_scrn_mediaPlayerObj != null) {
            if (cur_scrn_mediaPlayerObj.isPlaying()) {
                if (cur_scrn_mediaPlayerObj.isPlaying()) {
                    cur_scrn_mediaPlayerObj.pause();
                }
            }
        }
        switch (typeChoosed) {

                   /* case R.id.set_CommentsList:
                        Bundle bundle_list = new Bundle();
                        Fragment cmt_list_frag;
                        if (!isNotification) {

                            bundle_list.putString("set_id", setsListModel.getSet_id());
                            bundle_list.putString("set_name", setsListModel.getSet_name());
                            bundle_list.putString("userId", userObj.getUser_Id());
                            bundle_list.putString("channel_name", chl_list_obj.getChannel_name());
                            cmt_list_frag = new CommentListFragment();
                            cmt_list_frag.setArguments(bundle_list);

                        } else {
                            bundle_list.putString("set_id", notificationsModel.getNotificationsSetDetail().getSet_id());
                            bundle_list.putString("set_name", notificationsModel.getNotificationsSetDetail().getName());
                            bundle_list.putString("userId", userObj.getUser_Id());
                            bundle_list.putString("channel_name", notificationsModel.getChannel_name());
                            cmt_list_frag = new CommentListFragment();
                            cmt_list_frag.setArguments(bundle_list);
                        }
                        ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Comments_List, cmt_list_frag, true);
                        return true;
*/
            case SHARE_SETTINGS:
                if (isNotification) {

                   /* Intent intent = new Intent(CardDetailFragment.this, EditSetInfo.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("notfy_modl_obj", notificationsModel);
                    intent.putExtra("isNotification", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/

                    Fragment edit_set_info = new ShareSettings();
                    bundle.putParcelable("notfy_modl_obj", notificationsModel);
                    bundle.putBoolean("isNotification", true);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Set_Share_settings, edit_set_info, true);

                } else {
                    /*Intent intent = new Intent(CardDetailFragment.this, EditSetInfo.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("model_obj", chl_list_obj);
                    intent.putExtra("setsListModel", setsListModel);
                    intent.putExtra("isNotification", false);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                    Fragment edit_set_info = new ShareSettings();
                    //  bundle.putParcelable("model_obj", chl_list_obj);
                    bundle.putParcelable("setsListModel", setsListModel);
                    bundle.putBoolean("isNotification", false);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Set_Share_settings, edit_set_info, true);
                }
                break;
            case CARD_SHARE:


                Fragment shar_frag = new CardList();
                Bundle shr_bundle_arg = new Bundle();
                shr_bundle_arg.putParcelable("setsListModel", setsListModel);
                shr_bundle_arg.putBoolean("isCardShare", true);
                shr_bundle_arg.putParcelable("notfy_modl_obj", notificationsModel);
                if (isNotification) {
                    shr_bundle_arg.putBoolean("isNotification", true);
                    shr_bundle_arg.putString("chl_name", notificationsModel.getChannel_name());
                } else {
                    shr_bundle_arg.putBoolean("isNotification", false);
                    shr_bundle_arg.putString("chl_name", chl_list_obj.getChannel_name());
                }
                shr_bundle_arg.putBoolean("re_order", false);

                shr_bundle_arg.putInt("card_position", Card_CurrentPos);
                shar_frag.setArguments(shr_bundle_arg);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, shar_frag, false);
/*

                if(isNotification){
                    Fragment c_frag = new CardList();
                    Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("isCardShare", true);
                    bundle3.putParcelable("setsListModel", setsListModel);
                    bundle3.putBoolean("re_order", false);
                    bundle3.putString("chl_name", chl_list_obj.getChannel_name());
                    bundle3.putInt("card_position", Card_CurrentPos);
                    c_frag.setArguments(bundle3);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, c_frag, false);
                }
                else {
                    Fragment c_frag = new CardList();
                    Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("isCardShare", true);
                    bundle3.putParcelable("setsListModel", setsListModel);
                    bundle3.putBoolean("re_order", false);
                    bundle3.putString("chl_name", chl_list_obj.getChannel_name());
                    bundle3.putInt("card_position", Card_CurrentPos);
                    c_frag.setArguments(bundle3);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, c_frag, false);
                }*/
                break;
            case SET:

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
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
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
                    //  bundle.putParcelable("model_obj", chl_list_obj);
                    bundle.putParcelable("setsListModel", setsListModel);
                    bundle.putBoolean("isNotification", false);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Set, edit_set_info, true);
                }

                break;

            case EDIT_CARD:



                   /* Intent intent1 = new Intent(CardDetailFragment.this, CreateCardsFragment.class);

                    intent1.putExtra("userId", userId);
                    intent1.putExtra("setsListModel", setsListModel);
                    intent1.putExtra("model_obj", chl_list_obj);
                    intent1.putExtra("isCreate_Crd",false);
                    intent1.putExtra("Card_Dtls", cardsListModels.get(Cur_PagrPosition));

                    //intent1.putExtra("my_card_bundle",bundle);
                    startActivityForResult(intent1, UPDATECARD);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                Fragment frag = new CreateMultimediaCard();
                bundle.putParcelable("setsListModel", setsListModel);
                //  bundle.putParcelable("model_obj", chl_list_obj);
                bundle.putBoolean("isCreate_Crd", false);
                bundle.putParcelable("Card_Dtls", cardsListModels.get(Card_CurrentPos));
                frag.setArguments(bundle);
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Card, frag, true);


                break;
            case CARD_DELETE:

             /* Intent intent2 = new Intent(CardDetailFragment.this, CardList.class);
                intent2.putExtra("userId", userId);
                intent2.putExtra("setsListModel", setsListModel);
                intent2.putExtra("re_order", true);
                startActivity(intent2);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
               */
                Fragment cl_frag = new CardList();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("setsListModel", setsListModel);
                bundle1.putBoolean("re_order", true);
                bundle1.putString("chl_name", chl_list_obj.getChannel_name());
                bundle1.putInt("card_position", Card_CurrentPos);
                cl_frag.setArguments(bundle1);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, cl_frag, false);

                break;
            case CARD_LIST:
                Fragment frag1 = new CardList();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("setsListModel", setsListModel);
                bundle2.putParcelable("notfy_modl_obj", notificationsModel);
                if (isNotification) {
                    bundle2.putBoolean("isNotification", true);
                    bundle2.putString("chl_name", notificationsModel.getChannel_name());
                } else {
                    bundle2.putBoolean("isNotification", false);
                    bundle2.putString("chl_name", chl_list_obj.getChannel_name());
                }
                bundle2.putBoolean("re_order", false);

                bundle2.putInt("card_position", Card_CurrentPos);
                frag1.setArguments(bundle2);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, frag1, false);
                break;
            ///
            //      return true;


                  /*  default:
                        return super.onOptionsItemSelected(item);*/

        }
    }

    public void setGlob_mediaPlayerObj(MediaPlayer mp_obj) {
        if (this.first_mediaPlayerObj != null) {
            if (this.cur_scrn_mediaPlayerObj != null) {
                first_mediaPlayerObj = cur_scrn_mediaPlayerObj;
                cur_scrn_mediaPlayerObj = mp_obj;
            } else {
                cur_scrn_mediaPlayerObj = mp_obj;
            }
        } else {
            first_mediaPlayerObj = mp_obj;
        }

    }


    @Override
    public void onSelect(int position, CardsListModel modelObj) {

    }

    @Override
    public void onCardClick(int position) {

    }

    public static enum TYPES {
        SET, EDIT_CARD, CARD_DELETE, SHARE_SETTINGS, CARD_LIST, CARD_ADD, CARD_SHARE
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cur_scrn_mediaPlayerObj != null) {
            if (cur_scrn_mediaPlayerObj.isPlaying()) {
                cur_scrn_mediaPlayerObj.pause();
            }
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //     Toast.makeText(getContext(), "isHidden" + hidden, Toast.LENGTH_LONG).show();

        if (!hidden) {
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
            ((BrightlyNavigationActivity) getActivity()).isHide_frag = false;
            boolean isCardClicked = ((BrightlyNavigationActivity) getActivity()).isCardClicked;
            if (isCardClicked) {
                int card_toPosition = ((BrightlyNavigationActivity) getActivity()).card_toPosition;
                ((BrightlyNavigationActivity) getActivity()).isCardClicked = false;
                viewPager_Cards.setCurrentItem(card_toPosition);
            }
            boolean isCardRefresh = ((BrightlyNavigationActivity) getActivity()).isCardRefresh;
            if (setsListModel.getWeb_sharing().equalsIgnoreCase("1")) {
                image_Share.setVisibility(View.VISIBLE);
                set_SimplaeDraweeImage(image_Share, ((BrightlyNavigationActivity) getActivity()).SHARE_DEF_IMAGE);

            } else {
                image_Share.setVisibility(View.GONE);
            }
            if (isCardRefresh) {
                //  cardsListModels=new ArrayList<>();
                ((BrightlyNavigationActivity) getActivity()).isCardRefresh = false;
                if (cardsListModels.size() == 0) {
                    viewPager_Cards.setVisibility(View.GONE);
                    pager_count.setVisibility(View.GONE);
                    view_nodata.setVisibility(View.VISIBLE);
                    if (Created_By.equalsIgnoreCase(userObj.getUser_Id())) {
                        view_nodata.setVisibility(View.VISIBLE);
                        // view_nodata.setText(R.string.add_cards_suggestion);
                        view_nodata.setText("Add " + ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel3title().getSingular() + " using below \" + \" button");
                    }
                } else {
                    setAdapter(cardsListModels);
                    pager_size = String.valueOf(cardsListModels.size());
                    count = String.valueOf(Card_CurrentPos + 1);
                    pager_count.setText(count + "/" + pager_size);
                }
                //getCardsLists();
            }
            if (isNotification) {
                ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(notificationsModel.getNotificationsSetDetail().getName());
                ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(notificationsModel.getChannel_name());
            } else {
                ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(setsListModel.getSet_name());
                ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(channel_name);

            }
        }

    }

    public void showBottomNavigation() {

        //bottom_navigation.setVisibility(View.VISIBLE);
        mCardOptsMenuDlg = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mCardOptsMenuDlg.setContentView(R.layout.card_bottomdialog_opts); // your custom view.
        mCardOptsMenuDlg.setCancelable(true);
        mCardOptsMenuDlg.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mCardOptsMenuDlg.getWindow().setGravity(Gravity.BOTTOM);
        mCardOptsMenuDlg.show();
        RecyclerView recyclerView_opts_menu = mCardOptsMenuDlg.getWindow().findViewById(R.id.recycler_card_mnu_option);
        recyclerView_opts_menu.setLayoutManager(new GridLayoutManager(getContext(), 4));
        CardOptionsMenuAdapter adapter = new CardOptionsMenuAdapter(getContext(), this);
        adapter.models_list = opts_List;
        recyclerView_opts_menu.setAdapter(adapter);


    }

    public void video_onFullscrreen(boolean isFullScreen) {
        if (isFullScreen) {
            image_createCard.setVisibility(View.GONE);
            image_Comment.setVisibility(View.GONE);
            pager_count.setVisibility(View.GONE);
        } else {
            pager_count.setVisibility(View.VISIBLE);
            if (!userObj.getUser_Id().equalsIgnoreCase(Created_By)) {
                image_createCard.setVisibility(View.GONE);

                // image_Comment.setVisibility(View.VISIBLE);

            } else {
                image_createCard.setVisibility(View.VISIBLE);
                //  image_Comment.setVisibility(View.GONE);
            }
            image_Comment.setVisibility(View.GONE);
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
        menu_list.add("Create new " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);
        menu_list.add("Import " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);

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
                                                         isAddCardPressed = true;
                                                         Bundle bundle = new Bundle();
                                                         switch (position) {
                                                             case 0:

                                                                 bundle.putParcelable("setsListModel", setsListModel);
                                                                 //      bundle.putParcelable("model_obj", chl_list_obj);
                                                                 bundle.putBoolean("isCreate_Crd", true);
                                                                 Fragment crt_crd_frag = new CreateMultimediaCard();
                                                                 crt_crd_frag.setArguments(bundle);
                                                                 cardsListModels = null;
                                                                 Card_CurrentPos = 0;
                                                                 ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Card, crt_crd_frag, true);
                                                                 break;
                                                             case 1:

                                                                 bundle.putString("Set_ID_toCreateCard", setsListModel.getSet_id());

                                                                 bundle.putString("type", "all");
                                                                 Fragment channel_frag = new ViewPager_Category();
                                                                 channel_frag.setArguments(bundle);

                                                                 ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
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

                Call<CardsListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getCardsList(userObj.getUser_Id(), set_id);
                callRegisterUser.enqueue(new ApiCallback<CardsListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<CardsListResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        ((BrightlyNavigationActivity) getActivity()).DontRun = false;
                        CardsListResponse cardsListResponse = response.body();
                        if (isSuccess) {

                            if (cardsListResponse != null && cardsListResponse.getData() != null && cardsListResponse.getData().size() != 0) {

                                cardsListModels = new ArrayList<>(cardsListResponse.getData());

                                view_nodata.setVisibility(View.GONE);

                                setCardListAdapter();


                                if (isAddCardPressed) {
                                    //viewPager_Cards.setCurrentItem(cardsListModels.size());
                                    if (cardsListModels.size() == 1) {
                                        Card_CurrentPos = 0;
                                        pager_size = String.valueOf(cardsListModels.size());
                                        count = String.valueOf(Card_CurrentPos + 1);
                                        pager_count.setText(count + "/" + pager_size);

                                    }
                                } else {
                                    Card_CurrentPos = 0;
                                    pager_size = String.valueOf(cardsListModels.size());
                                    count = String.valueOf(Card_CurrentPos + 1);
                                    pager_count.setText(count + "/" + pager_size);
                                }

                                setAdapter(cardsListModels);
                                showHidePageNavigation();
                                if (isAddCardPressed) {
                                    viewPager_Cards.setCurrentItem(cardsListModels.size());
                                    isAddCardPressed = false;
                                }
                                setComment_Image();


                            } else {
                                viewPager_Cards.setVisibility(View.GONE);
                                pager_count.setVisibility(View.GONE);
                                cardsListModels = new ArrayList<>();
                                if (Created_By.equalsIgnoreCase(userObj.getUser_Id())) {
                                    view_nodata.setVisibility(View.VISIBLE);
                                    // view_nodata.setText(R.string.add_cards_suggestion);
                                    view_nodata.setText("Add " + ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel3title().getSingular() + " using below \" + \" button");
                                } else {
                                    view_nodata.setVisibility(View.VISIBLE);
                                    //  view_nodata.setText("No Cards Available");
                                    view_nodata.setText("No " + ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel3title().getPlural() + " available");
                                }

                            }
                /*            CardsListModel dummyCardObj=new CardsListModel();
                            cardsListModels.add(dummyCardObj);*/


                        } else {
                            showLongToast(getActivity(), message);
                        }
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
                        if (message.equals("timeout")) {
                            showLongToast(getActivity(), "Internet is too slow.");
                            //       ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                        }
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


    private void setCardListAdapter() {
        image_createCard.setZ(50.0f);
        image_Comment.setZ(50.0f);
        pager_count.setZ(50.0f);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        card_listview.setLayoutManager(linearLayoutManager);
        CardListAdapter cardListAdapter = new CardListAdapter(getContext(), cardsListModels, this);
        cardListAdapter.audio_def_image = ((BrightlyNavigationActivity) getActivity()).CARD_AUDIO_IMAGE;
        cardListAdapter.file_def_image = ((BrightlyNavigationActivity) getActivity()).CARD_FILE_IMAGE;
        cardListAdapter.uTube_def_image = ((BrightlyNavigationActivity) getActivity()).CARD_UTUBE_IMAGE;
        cardListAdapter.def_image = ((BrightlyNavigationActivity) getActivity()).CARD_DEF_IMAGE;
        card_listview.setAdapter(cardListAdapter);
    }

    private void setComment_Image() {
        String isComments = cardsListModels.get(Card_CurrentPos).getComment();
        String card_CreatedBY = cardsListModels.get(Card_CurrentPos).getCreated_by();
        String IMAGEURL = "";
        IMAGEURL = ((BrightlyNavigationActivity) getActivity()).NO_COMMENT_IMG;
        if (!userObj.getUser_Id().equalsIgnoreCase(card_CreatedBY)) {
            if (isComments.equals("1")) {
                IMAGEURL = ((BrightlyNavigationActivity) getActivity()).HAVING_COMMENT_IMG;

            } else {
                IMAGEURL = ((BrightlyNavigationActivity) getActivity()).NO_COMMENT_IMG;
            }
        } else {
            if (isComments.equals("1")) {
                IMAGEURL = ((BrightlyNavigationActivity) getActivity()).HAVING_COMMENT_IMG;
            } else {
                // image_Comment.setVisibility(View.GONE);
            }
        }
        if (!IMAGEURL.equals("")) {
           /* Glide.with(getContext())
                    .load(IMAGEURL)
                    .asBitmap()


//                    .transform(new CircleTransform(scrn_context))
                    *//*.override(50, 50)*//*
                    .into(image_Comment);*/
            set_SimplaeDraweeImage(image_Comment, IMAGEURL);


        }

    }

    private void set_SimplaeDraweeImage(SimpleDraweeView imgView, String IMAGEURL) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getContext().getResources());
        builder.setProgressBarImage(R.drawable.loader);
        ResizeOptions mResizeOptions = new ResizeOptions(65, 65);
        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        imgView.setHierarchy(hierarchy);


        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(IMAGEURL))
                        .setResizeOptions(mResizeOptions)

                        .build();
        imgView.setImageRequest(imageRequest);
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

            /*bundle.putString("set_createdby",Created_By);
            bundle.putString("set_id", set_id);
            bundle.putString("userId", userId);
            bundle.putString("set_name", set_name);
            bundle.putString("chl_name",channel_name);*/
            //   bundle.putParcelable("model_obj", chl_list_obj);
            bundle.putParcelable("setListModel", setsListModel);
            bundle.putBoolean("isNotification", isNotification);
            bundle.putParcelable("notfy_modl_obj", notificationsModel);
            bundle.putString("card_position", String.valueOf(i));
            card_frag.setArguments(bundle);
            cardFragList.add(card_frag);
        }
        cardsPagerAdapter = new ViewCardFragmentPagerAdapter(getContext(), getChildFragmentManager(), cardFragList, set_id, userObj.getUser_Id(), set_name);
        viewPager_Cards.setAdapter(cardsPagerAdapter);
        viewPager_Cards.setOffscreenPageLimit(2);
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

    }

    public void move_card() {

        if (isNotification && card_order_position != null) {
            viewPager_Cards.setCurrentItem(Integer.parseInt(card_order_position));
            card_order_position = null;

        }
        isYouTubeInitializing = false;
    }


}
