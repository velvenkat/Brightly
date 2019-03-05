package com.purplefront.brightly.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.RichLinkViewTelegram;
import com.purplefront.brightly.Utils.Util;
import com.purplefront.brightly.Utils.ViewListener;

import uk.co.senab.photoview.PhotoViewAttacher;


public class Multimedia_CardFragment extends BaseFragment implements YouTubePlayer.OnInitializedListener {
    View rootView;
    RichLinkViewTelegram richLinkView1;
    CardsListModel cardModelObj;
    ProgressDialog dialog;
    String user_id, set_id, set_name;
    FrameLayout frame_youtube;
    YouTubePlayer UTubePlayer;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    RelativeLayout rl_audio_player;
    String DEVELOPER_KEY = "AIzaSyDPwTq4xr0Fq-e1z0tDEBaj3qgAgi5VJ44";
    ImageView img_audio_play_stop;
    SeekBar audio_seek_bar;
    TextView txt_PlayProgTime;
    TextView file_cardLink;
    RealmModel userObj;
    boolean isFileLoaded = false;
    // String setCreatedBy,channel_name;
    CardDetailFragment parent_frag_Card_dtl;
    boolean isUTubePlayFullScreen = false;
    NotificationsModel notificationsModelObj;
    SetsListModel setsListModelObj;
    ChannelListModel chl_list_obj;
    boolean isNotification;
    String card_pos;
    LinearLayout ll_card_contr;
    ProgressDialog file_progress = null;
    RelativeLayout scroll_card;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.items_multimedia_pager, container, false);
        scroll_card = (RelativeLayout) rootView.findViewById(R.id.scroll_card);
        ll_card_contr = rootView.findViewById(R.id.ll_card_contr);
        userObj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        Bundle bundle = getArguments();
        //  setHasOptionsMenu(true);
        cardModelObj = bundle.getParcelable("card_mdl_obj");
        //  chl_list_obj = bundle.getParcelable("model_obj");
        chl_list_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;
        setsListModelObj = bundle.getParcelable("setListModel");
        notificationsModelObj = bundle.getParcelable("notfy_modl_obj");
        isNotification = bundle.getBoolean("isNotification");
        card_pos = bundle.getString("card_position");

        ll_card_contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_frag_Card_dtl.showBottomNavigation();
            }
        });
        //setCreatedBy=bundle.getString("set_createdby");
        //channel_name=bundle.getString("chl_name");
        parent_frag_Card_dtl = (CardDetailFragment) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.view_card);
        parent_frag_Card_dtl.isYouTubeInitializing = true;

        TextView text_cardName;
        TextView text_cardDescription;
        SimpleDraweeView image_cardImage;


        // Locate the TextViews in viewpager_item.xml
        text_cardName = (TextView) rootView.findViewById(R.id.text_cardName);
        rl_audio_player = (RelativeLayout) rootView.findViewById(R.id.rl_audio_player);
        text_cardDescription = (TextView) rootView.findViewById(R.id.text_cardDescription);
        image_cardImage = (SimpleDraweeView) rootView.findViewById(R.id.image_cardImage);
        frame_youtube = (FrameLayout) rootView.findViewById(R.id.frame_youtube);
        img_audio_play_stop = (ImageView) rootView.findViewById(R.id.img_play_stop);
        audio_seek_bar = (SeekBar) rootView.findViewById(R.id.seek_audio_rec);
        txt_PlayProgTime = (TextView) rootView.findViewById(R.id.txt_PlayProgTime);
        file_cardLink = (TextView) rootView.findViewById(R.id.file_cardLink);
        richLinkView1 = (RichLinkViewTelegram) rootView.findViewById(R.id.preview);

        text_cardName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_frag_Card_dtl.showBottomNavigation();
            }
        });
        scroll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_frag_Card_dtl.showBottomNavigation();
            }
        });

        //mPreview=new Preview(getContext());

        if (cardModelObj.getTitle() != null) {
            text_cardName.setText(cardModelObj.getTitle());
        }

        if (cardModelObj.getDescription() != null) {
            text_cardDescription.setText(cardModelObj.getDescription());
            text_cardDescription.setMovementMethod(LinkMovementMethod.getInstance());
            text_cardDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent_frag_Card_dtl.showBottomNavigation();
                }
            });
        }
        if (cardModelObj.getType().equalsIgnoreCase("image")) {
            image_cardImage.setVisibility(View.VISIBLE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);

            if (!cardModelObj.getUrl().isEmpty() && cardModelObj.getUrl() != null) {


               /* Glide.with(getContext())
                        .load(cardModelObj.getUrl())
                        .placeholder(R.drawable.card_progress_loading)
                        .centerCrop()
                        *//*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*//*
                        .into(image_cardImage);*/
                load_card_image(cardModelObj.getUrl(), image_cardImage, false);

                image_cardImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        View imgEntryView = inflater.inflate(R.layout.dialog_fullscreen, null);
                        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen); //default fullscreen titlebar
                        SimpleDraweeView img = (SimpleDraweeView) imgEntryView.findViewById(R.id.usericon_large);

                     /*   Glide.with(getContext())
                                .load(cardModelObj.getUrl())
//                                .placeholder(R.drawable.card_progress_loading)
                                .fitCenter()
                                *//*.transform(new CircleTransform(HomeActivity.this))
                                .override(50, 50)*//*
                                .into(img);*/
                        load_card_image(cardModelObj.getUrl(), img, true);

                        dialog.setContentView(imgEntryView);
                        dialog.show();

                        imgEntryView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
//                                dialog.cancel();
                                PhotoViewAttacher photoAttacher;
                                photoAttacher = new PhotoViewAttacher(img);
                                photoAttacher.update();
                            }
                        });

                    }
                });

            } else {
                Glide.with(getContext())
                        .load(R.drawable.no_image_available)
                        .centerCrop()
                        /*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*/
                        .into(image_cardImage);
            }
        } else if (cardModelObj.getType().equalsIgnoreCase("text")) {
            image_cardImage.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);
        } else if (cardModelObj.getType().equalsIgnoreCase("video")) {
            image_cardImage.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.VISIBLE);
            rl_audio_player.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);
            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

            if (getUserVisibleHint()) {

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_youtube, youTubePlayerFragment).commit();
                //  mYouTubePlayerSupportFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
                parent_frag_Card_dtl = (CardDetailFragment) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.view_card);
                parent_frag_Card_dtl.isYouTubeInitializing = true;
                youTubePlayerFragment.initialize(DEVELOPER_KEY, Multimedia_CardFragment.this);

            }

            frame_youtube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UTubePlayer != null) {
                        if (UTubePlayer.isPlaying()) {
                            UTubePlayer.pause();
                        } else {
                            UTubePlayer.play();
                        }
                        Toast.makeText(getContext(), "Not NULL", Toast.LENGTH_LONG).show();
                    } else {


                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_youtube, youTubePlayerFragment,cardModelObj.getName()).addToBackStack(null).commit();

                    }


                }
            });
        } else if (cardModelObj.getType().equalsIgnoreCase("audio")) {
            image_cardImage.setVisibility(View.VISIBLE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.VISIBLE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);
            image_cardImage.setImageResource(R.drawable.audio_hdr_img);
            image_cardImage.getLayoutParams().height = 360;

            audio_player_initialize(audio_seek_bar, txt_PlayProgTime, img_audio_play_stop);
            MediaPlayer mp = setMediaPlayer(null, cardModelObj.getUrl(), false);
            parent_frag_Card_dtl.setGlob_mediaPlayerObj(mp);
        } else if (cardModelObj.getType().equalsIgnoreCase("file")) {

            image_cardImage.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.VISIBLE);
            file_cardLink.setText(cardModelObj.getUrl());
            if (file_cardLink != null && file_cardLink.getText() != null && !file_cardLink.getText().toString().equals("")) {

                file_cardLink.setVisibility(View.GONE);
                richLinkView1.setVisibility(View.VISIBLE);


                if (!this.isVisible()) {

                } else {
                    file_progress = showProgress();
                }

                //     isFileLoaded = false;
                richLinkView1.setLink(file_cardLink.getText().toString(), new ViewListener() {
                    @Override
                    public void onSuccess(boolean status) {
                        // Toast.makeText(getContext(),"file loaded success",Toast.LENGTH_LONG).show();
                        Log.e("file_log", "FileLoaded");
                        // isFileLoaded = true;
                        /*if (file_progress != null)
                            file_progress.dismiss();
                        file_progress = null;*/
                    }

                    @Override
                    public void onError(Exception e) {
                       /* isFileLoaded = true;

                        file_progress.dismiss();
                        file_progress = null;*/
                        //new CustomToast().Show_Toast(getContext(), richLinkView1, e.getMessage());
                        richLinkView1.setVisibility(View.GONE);
                        file_cardLink.setVisibility(View.VISIBLE);
                        //  file_cardLink.setTextColor(Color.argb(0, 100, 20, 20));
                        file_cardLink.setText(e.getMessage());
                    }
                });


               /* mPreview.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                       *//* String url = file_cardLink.toString();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setPackage("com.google.android.youtube");
                        startActivity(intent);*//*

                        String fullPath = cardModelObj.getUrl();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullPath));
                        startActivity(browserIntent);
                    }
                });*/

            }
            file_cardLink.setMovementMethod(LinkMovementMethod.getInstance());
            /*Glide.with(getActivity())
                    .load(R.drawable.open_pdf_icon)
                    .centerCrop()
                    *//*.transform(new CircleTransform(HomeAct/ivity.this))
                    .override(50, 50)*//*
                    .into(image_cardImage);*/

           /* file_cardLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String format = "https://docs.google.com/gview?embedded=true&url=%s";
                    String fullPath = String.format(Locale.ENGLISH, cardModelObj.getUrl());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullPath));
                    startActivity(browserIntent);
                }
            });*/
        }

        // Add viewpager_item.xml to ViewPager
/*
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
                   Toast.makeText(getContext(),"Utubefull"+isUTubePlayFullScreen,Toast.LENGTH_LONG).show();
                    if(UTubePlayer!=null){
                        if(isUTubePlayFullScreen){
                            UTubePlayer.setFullscreen(false);
                        }
                    }
                }
                return true;
            }
        });
*/
      /*  rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_frag_Card_dtl.showBottomNavigation();
            }
        });*/
        return rootView;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        if (!isNotification) {
            if (cardModelObj.getCreated_by().equalsIgnoreCase(userObj.getUser_Id())) {
                inflater.inflate(R.menu.my_set_cards, menu);
            } else if (chl_list_obj.getCreated_by().equalsIgnoreCase(userObj.getUser_Id())) {
                inflater.inflate(R.menu.my_set_cards_other, menu);
            } else
                inflater.inflate(R.menu.my_sub_cards, menu);

        } else
            inflater.inflate(R.menu.my_sub_cards, menu);


    }*/


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {

            case R.id.set_CommentsList:
                Bundle bundle_list = new Bundle();
                Fragment cmt_list_frag;
                if(!isNotification) {

                    bundle_list.putString("set_id", setsListModelObj.getSet_id());
                    bundle_list.putString("set_name", setsListModelObj.getSet_name());
                    bundle_list.putString("userId", userObj.getUser_Id());
                    bundle_list.putString("channel_name", chl_list_obj.getChannel_name());
                     cmt_list_frag = new CommentListFragment();
                    cmt_list_frag.setArguments(bundle_list);

                }
                else{
                    bundle_list.putString("set_id", notificationsModelObj.getNotificationsSetDetail().getSet_id());
                    bundle_list.putString("set_name", notificationsModelObj.getNotificationsSetDetail().getName());
                    bundle_list.putString("userId", userObj.getUser_Id());
                    bundle_list.putString("channel_name", notificationsModelObj.getChannel_name());
                    cmt_list_frag = new CommentListFragment();
                    cmt_list_frag.setArguments(bundle_list);
                }
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Comments_List, cmt_list_frag, true);
                return true;

            case R.id.setInfo_Edit:

                if (isNotification) {

                   *//* Intent intent = new Intent(CardDetailFragment.this, EditSetInfo.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("notfy_modl_obj", notificationsModel);
                    intent.putExtra("isNotification", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*//*

                    Fragment edit_set_info = new EditSetInfo();
                    bundle.putParcelable("notfy_modl_obj", notificationsModelObj);
                    bundle.putBoolean("isNotification", true);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Set, edit_set_info, true);

                } else {
                    *//*Intent intent = new Intent(CardDetailFragment.this, EditSetInfo.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("model_obj", chl_list_obj);
                    intent.putExtra("setsListModel", setsListModel);
                    intent.putExtra("isNotification", false);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*//*
                    Fragment edit_set_info = new EditSetInfo();
                    bundle.putParcelable("model_obj", chl_list_obj);
                    bundle.putParcelable("setsListModel", setsListModelObj);
                    bundle.putBoolean("isNotification", false);
                    edit_set_info.setArguments(bundle);
                    ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Set, edit_set_info, true);
                }

                return true;

            case R.id.cardInfo_Edit:



                   *//* Intent intent1 = new Intent(CardDetailFragment.this, CreateCardsFragment.class);

                    intent1.putExtra("userId", userId);
                    intent1.putExtra("setsListModel", setsListModel);
                    intent1.putExtra("model_obj", chl_list_obj);
                    intent1.putExtra("isCreate_Crd",false);
                    intent1.putExtra("Card_Dtls", cardsListModels.get(Cur_PagrPosition));

                    //intent1.putExtra("my_card_bundle",bundle);
                    startActivityForResult(intent1, UPDATECARD);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);*//*
                Fragment frag = new CreateCardsFragment();
                bundle.putParcelable("setsListModel", setsListModelObj);
                bundle.putParcelable("model_obj", chl_list_obj);
                bundle.putBoolean("isCreate_Crd", false);
                bundle.putParcelable("Card_Dtls", cardModelObj);
                frag.setArguments(bundle);
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Card, frag, true);


                return true;
            case R.id.card_reorder:

               *//* Intent intent2 = new Intent(CardDetailFragment.this, CardList.class);
                intent2.putExtra("userId", userId);
                intent2.putExtra("setsListModel", setsListModel);
                intent2.putExtra("re_order", true);
                startActivity(intent2);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
               *//*
                Fragment cl_frag = new CardList();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("setsListModel", setsListModelObj);
                bundle1.putBoolean("re_order", true);
                bundle1.putString("chl_name", chl_list_obj.getChannel_name());
                bundle1.putInt("card_position", Integer.parseInt(card_pos));
                cl_frag.setArguments(bundle1);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, cl_frag, false);

                return true;
            case R.id.action_card_list:
                Fragment frag1 = new CardList();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("setsListModel", setsListModelObj);
                bundle2.putParcelable("notfy_modl_obj", notificationsModelObj);
                if (isNotification) {
                    bundle2.putBoolean("isNotification", true);
                    bundle2.putString("chl_name", notificationsModelObj.getChannel_name());
                } else {
                    bundle2.putBoolean("isNotification", false);
                    bundle2.putString("chl_name", chl_list_obj.getChannel_name());
                }
                bundle2.putBoolean("re_order", false);

                bundle2.putInt("card_position", Integer.parseInt(card_pos));
                frag1.setArguments(bundle2);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, frag1, false);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    public void load_card_image(String URL, SimpleDraweeView imgView, boolean isFullScreen) {
        String img_url = URL;
        ResizeOptions resizeOptions = new ResizeOptions(350, 250);
        if (img_url != null && !img_url.trim().equals("")) {
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(getContext().getResources());
            builder.setProgressBarImage(R.drawable.loader);
            if (isFullScreen)
                builder.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            imgView.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img_url))
                            //.setResizeOptions(resizeOptions)

                            .build();
            imgView.setImageRequest(imageRequest);

        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            parent_frag_Card_dtl = (CardDetailFragment) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.view_card);

        }

        if (isVisibleToUser && mediaPlayer != null) {
            parent_frag_Card_dtl.setGlob_mediaPlayerObj(mediaPlayer);
        }
        if (!isVisibleToUser && UTubePlayer != null) {
            // Log.v (TAG, "Releasing youtube player, URL : " + getArguments().getString(KeyConstant.KEY_VIDEO_URL));
            UTubePlayer.release();
            if (isAdded()) {
                if (getChildFragmentManager() != null) {
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.remove(youTubePlayerFragment).commit();
                }
            }
            UTubePlayer = null;
            if (getActivity() != null)
                ((BrightlyNavigationActivity) getActivity()).uTubePlayer = null;
        }
        if (!isVisibleToUser && mediaPlayer != null) {

            //release_media();
            if (isAudioPlay) {
                mediaPlayer.pause();
                isAudioPlay = false;
                img_play_stop.setImageResource(R.drawable.play_rec);


            }
        }
      /*  if (cardModelObj!=null && cardModelObj.getType().equalsIgnoreCase("audio")) {

            if (isVisibleToUser && mediaPlayer == null && img_play_stop!=null) {
               // isAudioPlay=false;
              //  setMediaPlayer(null, cardModelObj.getUrl());
            }

        }*/
        if (isVisibleToUser && youTubePlayerFragment != null) {

            if (!isAdded()) return;
            //  Log.v (TAG, "Initializing youtube player, URL : " + getArguments().getString(KeyConstant.KEY_VIDEO_URL));
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_youtube, youTubePlayerFragment).commit();
            parent_frag_Card_dtl = (CardDetailFragment) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.view_card);

            parent_frag_Card_dtl.isYouTubeInitializing = true;
            youTubePlayerFragment.initialize(DEVELOPER_KEY, this);
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
//                            Utils.logDebug(TAG, "onInitializationSuccess");

        parent_frag_Card_dtl.move_card();
        UTubePlayer = youTubePlayer;
        if (getActivity() != null)
            ((BrightlyNavigationActivity) getActivity()).uTubePlayer = UTubePlayer;
        UTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {

                if (b) {
                    isUTubePlayFullScreen = true;
                    if (getActivity() != null)
                        ((BrightlyNavigationActivity) getActivity()).isUTubePlayerFullScreen = true;
                } else {
                    isUTubePlayFullScreen = false;
                    if (getActivity() != null)
                        ((BrightlyNavigationActivity) getActivity()).isUTubePlayerFullScreen = false;
                }
            }
        });
        if (!wasRestored) {
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            youTubePlayer.cueVideo(cardModelObj.getName());
            // youTubePlayer.loadVideo(image_name);
            youTubePlayer.setShowFullscreenButton(true);
            youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
            youTubePlayer.pause();
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                            Utils.logError(TAG, "Could not initialize YouTubePlayer");

        parent_frag_Card_dtl.move_card();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isAudioPlay) {
            mediaPlayer.pause();
            isAudioPlay = false;
            img_play_stop.setImageResource(R.drawable.play_rec);
        }
    }


}
