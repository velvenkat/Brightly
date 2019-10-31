package com.digital_easy.info_share.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.digital_easy.info_share.Adapters.Blog_Adapter;
import com.digital_easy.info_share.Modules.BlogResponseModel;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.SlidingImage_Adapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.CardsListModel;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.RichLinkViewTelegram;
import com.digital_easy.info_share.Utils.Util;
import com.digital_easy.info_share.Utils.ViewListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.viewpagerindicator.CirclePageIndicator;

public class Multimedia_CardFragment extends BaseFragment implements SlidingImage_Adapter.SlideImageCLicked, YouTubePlayer.OnInitializedListener, Blog_Adapter.BlogAdapterListener, LifecycleOwner {
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
    Blog_Adapter blog_adapter;
    SeekBar audio_seek_bar;
    FrameLayout fl_video_contr;
    FullScreenMediaController customMediaController;
    TextView txt_PlayProgTime;
    TextView file_cardLink;
    VideoView video_vw;
    private boolean isFullScreen_videoVw;
    int currentPage;
    RealmModel userObj;
    boolean isFileLoaded = false;
    // String setCreatedBy,channel_name;
    CardDetailFragment parent_frag_Card_dtl;
    boolean isUTubePlayFullScreen = false;
    NotificationsModel notificationsModelObj;
    SetsListModel setsListModelObj;
    ChannelListModel chl_list_obj;
    TextView text_cardName;
    TextView text_cardDescription;
    ImageView img_play_pause_video;
    boolean isNotification;
    String card_pos;
    int NUM_PAGES;
    Runnable buffer_updaterunnable;
    Timer swipeTimer;
    TextView txtBuffer;

    LinearLayout ll_card_contr;
    ProgressDialog file_progress = null;
    RelativeLayout scroll_card;
    LinearLayout ll_image_card_contr;
    ViewPager mPager;
    // View btn_temp;
    CirclePageIndicator indicator;
    Handler buffer_handler;
    NestedScrollView scroll_mv_contr;
    RecyclerView recycle_mv_contr;
    LinearLayout ll_contacts_contr;
    BlogResponseModel sel_blog_res_modelObj;
    TextView txtCont_name, txt_Company, txtTitle, txtMob_no, txtOff_no, txtEmail;
    boolean toggleFullScreen = false;
    int Scroll_Y_POS;
    boolean isObserverDone;
    LifecycleOwner lifecycleOwner;


    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    }*/

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    }
*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ///  getActivity().getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        rootView = inflater.inflate(R.layout.items_multimedia_pager, container, false);

        scroll_card = (RelativeLayout) rootView.findViewById(R.id.scroll_card);
        ll_card_contr = rootView.findViewById(R.id.ll_card_contr);


        ll_contacts_contr = rootView.findViewById(R.id.ll_contacts_contr);
        scroll_mv_contr = rootView.findViewById(R.id.scroll_mv_contr);
        recycle_mv_contr = rootView.findViewById(R.id.recycle_mv_contr);
        recycle_mv_contr.setLayoutManager(new LinearLayoutManager(getContext()));
        ll_image_card_contr = rootView.findViewById(R.id.ll_image_card_contr);
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        video_vw = rootView.findViewById(R.id.video_vw);

        txtBuffer = rootView.findViewById(R.id.txt_buffer);
        img_play_pause_video = rootView.findViewById(R.id.play_pause);
        fl_video_contr = rootView.findViewById(R.id.video_contr);
        txtCont_name = rootView.findViewById(R.id.txt_cont_name);
        txt_Company = rootView.findViewById(R.id.txt_cont_company);
        txtMob_no = rootView.findViewById(R.id.txt_cont_mob_no);
        txtOff_no = rootView.findViewById(R.id.txt_cont_off_no);
        txtEmail = rootView.findViewById(R.id.txt_email);
        txtTitle = rootView.findViewById(R.id.txt_cont_title);
        CirclePageIndicator indicator = (CirclePageIndicator)
                rootView.findViewById(R.id.indicator);

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
        scroll_mv_contr.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // do something when Scroll
                // Toast.makeText(getContext(),"I am Scrolled",Toast.LENGTH_LONG).show();
                // int ChildCount = scroll_mv_contr.getChildCount();
                if (scroll_mv_contr.getChildAt(0).getBottom()
                        <= (scroll_mv_contr.getHeight() + scroll_mv_contr.getScrollY())) {
                    //scroll view is at bottom
                    //Toast.makeText(getContext(),"I am Bottom",Toast.LENGTH_LONG).show();

                    if (!isObserverDone) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (getActivity() != null && scroll_mv_contr.getChildAt(0).getBottom()
                                        <= (scroll_mv_contr.getHeight() + scroll_mv_contr.getScrollY()))
                                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                                ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();
                            }


                        }, 500);


                    }
                    isObserverDone = true;

                } else
                    isObserverDone = false;

                //  scrollView_post.requestFocus();

            }
        });
        scroll_mv_contr.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (getActivity() != null) {
                    if (!isObserverDone && scrollY == 0) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (getActivity() != null && !isObserverDone && scrollY == 0) {
                                    /*getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
*/
                                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                                    ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();
                                }
                            }
                        }, 500);
                        isObserverDone = true;

                    } else {
                        isObserverDone = false;
                    }
                    if (scrollY > oldScrollY) {
                        // Toast.makeText(getContext(), "Scroll DOWN", Toast.LENGTH_LONG).show();
                        if (!isObserverDone) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (getActivity() != null && !isObserverDone && scrollY > oldScrollY) {
                                        View decorView = getActivity().getWindow().getDecorView();
// Hide the status bar.
                                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
                                      /*  ActionBar actionBar = ((BrightlyNavigationActivity) getActivity()).getSupportActionBar();
                                        actionBar.hide();*/
                                        ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(-112).setDuration(600L)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().hide();
                                                    }
                                                }).start();

                                    }
                                }

                            }, 500);
                            isObserverDone = true;

                        } else {
                            isObserverDone = false;
                        }
                    }
                    if (!isObserverDone && scrollY < oldScrollY) {
                        //Log.i(TAG, "Scroll UP");
                        //Toast.makeText(getContext(), "Scroll UP", Toast.LENGTH_LONG).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (getActivity() != null && !isObserverDone && scrollY < oldScrollY) {

                                    View decorView = getActivity().getWindow().getDecorView();
// Hide the status bar.
                                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                    decorView.setSystemUiVisibility(uiOptions);
                                    ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(-112).setDuration(600L)
                                            .withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().hide();
                                                }
                                            }).start();

                                }
                            }
                        }, 500);
                        isObserverDone = true;
                    } else {
                        isObserverDone = false;
                    }


                }

            }
        });

        recycle_mv_contr.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*if (newState == RecyclerView.SCROLL_INDICATOR_END) {
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                    ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();
                }
                if (newState == RecyclerView.SCROLL_INDICATOR_TOP) {
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                    ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();
                }
                if (newState == RecyclerView.SCROLL_INDICATOR_START) {
                    View decorView = getActivity().getWindow().getDecorView();
// Hide the status bar.
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                    ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(-112).setDuration(600L)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().hide();
                                }
                            }).start();

                }*/
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {

                    //Implement code for expanding fragment here
/*
                    View decorView = getActivity().getWindow().getDecorView();
// Hide the status bar.
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                    ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(-112).setDuration(600L)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().hide();
                                }
                            }).start();*/


                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                   /* getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                    ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();*/
                    if (cardModelObj.getData_obj() != null) {
                        List<BlogResponseModel> list_blogResponseModelObj = cardModelObj.getData_obj();
                        for (int i = 0; i < list_blogResponseModelObj.size(); i++) {
                            BlogResponseModel blogResponseModelObj = list_blogResponseModelObj.get(i);
                            if (blogResponseModelObj.getType() != null && blogResponseModelObj.getType().equals("video")) {
                                Blog_Adapter.VideoViewHolder holder = (Blog_Adapter.VideoViewHolder) recycle_mv_contr.findViewHolderForAdapterPosition(i);
                                if (holder != null && holder.customMediaController != null && holder.video_vw != null) {
                                    holder.video_vw.pause();
                                    holder.customMediaController.hide();
                                }
                            }
                        }
                    }
                    LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();
                    int TopPosition = layoutManager.findFirstVisibleItemPosition();
                    View v = recycle_mv_contr.getLayoutManager().findViewByPosition(0);
                    if (TopPosition == 0 && (recycle_mv_contr.computeVerticalScrollOffset() < 50)) {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                        parent_frag_Card_dtl.image_Share.setVisibility(View.VISIBLE);
                        parent_frag_Card_dtl.image_createCard.setVisibility(View.VISIBLE);
                        parent_frag_Card_dtl.pager_count.setVisibility(View.VISIBLE);
                        ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();
                    } else if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        // End of the list is here.
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                        parent_frag_Card_dtl.image_Share.setVisibility(View.VISIBLE);
                        parent_frag_Card_dtl.image_createCard.setVisibility(View.VISIBLE);
                        parent_frag_Card_dtl.pager_count.setVisibility(View.VISIBLE);
                        ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();
                    } /*else if (visibleItemCount == FirstPosition) {
                     *//* getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                        ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();*//*
                    } */ else {
                        View decorView = getActivity().getWindow().getDecorView();
// Hide the status bar.
                        parent_frag_Card_dtl.image_Share.setVisibility(View.GONE);
                        parent_frag_Card_dtl.image_createCard.setVisibility(View.GONE);
                        parent_frag_Card_dtl.pager_count.setVisibility(View.GONE);
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                        ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(-112).setDuration(600L)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().hide();
                                    }
                                }).start();

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //   Toast.makeText(getContext(), "Current:" + recycle_mv_contr.computeVerticalScrollOffset(), Toast.LENGTH_LONG).show();
            }
        });

        ll_card_contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_frag_Card_dtl.showBottomNavigation();
            }
        });
        recycle_mv_contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_frag_Card_dtl.showBottomNavigation();
            }
        });
        //setCreatedBy=bundle.getString("set_createdby");
        //channel_name=bundle.getString("chl_name");
        parent_frag_Card_dtl = (CardDetailFragment) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.view_card);
        parent_frag_Card_dtl.isYouTubeInitializing = true;


        SimpleDraweeView image_audioImage;


        // Locate the TextViews in viewpager_item.xml
        text_cardName = (TextView) rootView.findViewById(R.id.text_cardName);
        rl_audio_player = (RelativeLayout) rootView.findViewById(R.id.rl_audio_player);
        text_cardDescription = (TextView) rootView.findViewById(R.id.text_cardDescription);
        image_audioImage = (SimpleDraweeView) rootView.findViewById(R.id.image_audio_image);
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


        video_vw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                customMediaController.show();


                return true;
            }


        });
        //mPreview=new Preview(getContext());

        if (cardModelObj.getTitle() != null) {
            if (!cardModelObj.getType().equals("contact"))
                text_cardName.setText(cardModelObj.getTitle());
            else
                text_cardName.setVisibility(View.GONE);
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
        if (cardModelObj.getType().equalsIgnoreCase("multiple_images")) {
            ll_image_card_contr.setVisibility(View.VISIBLE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            fl_video_contr.setVisibility(View.GONE);
            ll_contacts_contr.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);

            mPager.setAdapter(new SlidingImage_Adapter(getContext(), cardModelObj.getMultiple_img_url(), this));


            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
            indicator.setRadius(5 * density);

            NUM_PAGES = cardModelObj.getMultiple_img_url().size();
            if (NUM_PAGES > 1)
                indicator.setViewPager(mPager);
            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });


        } else if (cardModelObj.getType().equalsIgnoreCase("text")) {
            ll_image_card_contr.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.GONE);
            image_audioImage.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            ll_contacts_contr.setVisibility(View.GONE);
            fl_video_contr.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);
            text_cardDescription.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000));
        } else if (cardModelObj.getType().equalsIgnoreCase("video_file")) {
            ll_image_card_contr.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.GONE);
            ll_contacts_contr.setVisibility(View.GONE);
            img_play_pause_video.setBackground(getContext().getResources().getDrawable(R.drawable.play_btn));
            image_audioImage.setVisibility(View.GONE);

            //video_vw.invalidate();
            customMediaController = new FullScreenMediaController(getContext());
            video_vw.setMediaController(customMediaController);


            customMediaController.setAnchorView(fl_video_contr);


            //     video_vw.setZOrderOnTop(true);
            video_vw.setVideoURI(Uri.parse(cardModelObj.getUrl()));
            video_vw.requestFocus();

            video_vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    setBufferUpdateListener(mp);
                    mp.setLooping(true);
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            img_play_pause_video.setVisibility(View.VISIBLE);
                            video_vw.seekTo(1);
                            video_vw.pause();

                            if (!isFullScreen_videoVw) {
                                text_cardDescription.setVisibility(View.VISIBLE);
                                text_cardName.setVisibility(View.VISIBLE);
                                parent_frag_Card_dtl.video_onFullscrreen(false);
                            }

                            // customMediaController.show();
                        }
                    });
                }

            });
            video_vw.start();

            img_play_pause_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (video_vw.isPlaying()) {
                        img_play_pause_video.setBackground(getContext().getResources().getDrawable(R.drawable.play_btn));
                        video_vw.pause();
                    } else {
                        img_play_pause_video.setBackground(getContext().getResources().getDrawable(R.drawable.pause));
                        video_vw.start();
                    }
                }
            });


            video_vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    //vidView.start();
                    txtBuffer.setVisibility(View.GONE);
                    img_play_pause_video.setBackground(getContext().getResources().getDrawable(R.drawable.play_btn));
                }
            });

            rl_audio_player.setVisibility(View.GONE);
            fl_video_contr.setVisibility(View.VISIBLE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);


        } else if (cardModelObj.getType().equalsIgnoreCase("contact")) {
            ll_contacts_contr.setVisibility(View.VISIBLE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            fl_video_contr.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);


            txtCont_name.setText(" : " + cardModelObj.getContact_name());
            if (cardModelObj.getContact_cell_phone().equals(""))
                txtMob_no.setVisibility(View.GONE);
            else
                txtMob_no.setText(" : " + cardModelObj.getContact_cell_phone());

            if (cardModelObj.getTitle().equals(""))
                txtTitle.setVisibility(View.GONE);
            else
                txtTitle.setText(" : " + cardModelObj.getTitle());


            if (cardModelObj.getContact_office_phone().equals(""))
                txtOff_no.setVisibility(View.GONE);
            else
                txtOff_no.setText(" : " + cardModelObj.getContact_office_phone());

            if (cardModelObj.getContact_company().equals(""))
                txt_Company.setVisibility(View.GONE);
            else
                txt_Company.setText(" : " + cardModelObj.getContact_company());

            if (cardModelObj.getContact_email().equals(""))
                txtEmail.setVisibility(View.GONE);
            else
                txtEmail.setText(" : " + cardModelObj.getContact_email());


            if (cardModelObj.getMultiple_img_url().size() > 0) {
                ll_image_card_contr.setVisibility(View.VISIBLE);
                mPager.setAdapter(new SlidingImage_Adapter(getContext(), cardModelObj.getMultiple_img_url(), this));


                indicator.setViewPager(mPager);

                final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                indicator.setRadius(5 * density);

                NUM_PAGES = cardModelObj.getMultiple_img_url().size();

                // Auto start of viewpager
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == NUM_PAGES) {
                            currentPage = 0;
                        }
                        mPager.setCurrentItem(currentPage++, true);
                    }
                };
                swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 3000, 3000);

                // Pager listener over indicator
                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        currentPage = position;

                    }

                    @Override
                    public void onPageScrolled(int pos, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int pos) {

                    }
                });

            } else
                ll_image_card_contr.setVisibility(View.GONE);


        } else if (cardModelObj.getType().equalsIgnoreCase("video")) {
            ll_image_card_contr.setVisibility(View.GONE);
            ll_contacts_contr.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.VISIBLE);
            fl_video_contr.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            image_audioImage.setVisibility(View.GONE);
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
            ll_image_card_contr.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.VISIBLE);
            ll_contacts_contr.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            fl_video_contr.setVisibility(View.GONE);
            richLinkView1.setVisibility(View.GONE);
            image_audioImage.setVisibility(View.VISIBLE);
            image_audioImage.setImageResource(R.drawable.audio_hdr_img);
            image_audioImage.getLayoutParams().height = 360;

            audio_player_initialize(audio_seek_bar, txt_PlayProgTime, img_audio_play_stop);
            MediaPlayer mp = setMediaPlayer(null, cardModelObj.getUrl(), false);
            parent_frag_Card_dtl.setGlob_mediaPlayerObj(mp);
        } else if (cardModelObj.getType().equalsIgnoreCase("file")) {
            image_audioImage.setVisibility(View.GONE);
            ll_image_card_contr.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.GONE);
            fl_video_contr.setVisibility(View.GONE);
            ll_contacts_contr.setVisibility(View.GONE);
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
                    .into(image_audioImage);*/

           /* file_cardLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String format = "https://docs.google.com/gview?embedded=true&url=%s";
                    String fullPath = String.format(Locale.ENGLISH, cardModelObj.getUrl());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullPath));
                    startActivity(browserIntent);
                }
            });*/
        } else if ((cardModelObj.getType().equalsIgnoreCase("new_card"))) {
            scroll_mv_contr.setVisibility(View.GONE);
            recycle_mv_contr.setVisibility(View.VISIBLE);

            List<BlogResponseModel> blogResponseModelsList = cardModelObj.getData_obj();
            BlogResponseModel modelObj = new BlogResponseModel();
            modelObj.setType("");
            blogResponseModelsList.add(modelObj);

            blog_adapter = new Blog_Adapter(this, blogResponseModelsList, getActivity(), this);
            recycle_mv_contr.setAdapter(blog_adapter);
        }

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //
                    //       getActivity().finish();
                    if (isFullScreen_videoVw) {
                        isFullScreen_videoVw = false;
                    /*((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
                        // fullScreen.setImageResource(R.drawable.full_size);
                        text_cardDescription.setVisibility(View.VISIBLE);
                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                        text_cardName.setVisibility(View.VISIBLE);

                        parent_frag_Card_dtl.video_onFullscrreen(false);
                        fl_video_contr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250));
                        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 250);
                        params1.gravity = Gravity.CENTER;

                        params1.bottomMargin = 5;
                        params1.topMargin = 5;
                        params1.rightMargin = 5;
                        params1.leftMargin = 5;
                        params1.height = 250;

                        video_vw.setLayoutParams(params1);

                        return true;

                    } else if (!((BrightlyNavigationActivity) getActivity()).getSupportActionBar().isShowing()) {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                        ((BrightlyNavigationActivity) getActivity()).toolbar.animate().translationY(0).setDuration(600L).start();
                        return true;


                    }
                    return false;


                } else
                    return false;
            }

        });

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

    private void setBufferUpdateListener(MediaPlayer mp) {
        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                txtBuffer.setText(String.valueOf(percent) + "%");
                txtBuffer.setVisibility(View.VISIBLE);
                if (buffer_handler == null)
                    buffer_handler = new Handler();
                else
                    buffer_handler.removeCallbacks(buffer_updaterunnable);
                buffer_updaterunnable = new Runnable() {
                    @Override
                    public void run() {
                        txtBuffer.setVisibility(View.GONE);
                    }
                };
                buffer_handler.postDelayed(buffer_updaterunnable, 2000);
            }
        });
    }


   /* @Override
    public void audio_init(SeekBar audio_seekBar, TextView txtPlayProgress, ImageView img_play_stop, String URL) {

        audio_player_initialize(audio_seekBar, txtPlayProgress, img_play_stop);
        MediaPlayer mp = setMediaPlayer(null, URL, false);
        parent_frag_Card_dtl.setGlob_mediaPlayerObj(mp);
    }*/


    @Override
    public void Update_Video_onFullScreen(boolean isFullScreen) {
        parent_frag_Card_dtl.video_onFullscrreen(isFullScreen);
        if (isFullScreen) {
            // btn_temp.setVisibility(View.GONE);
        } else {
            //   btn_temp.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void uTube_setLifeCycleObserver(YouTubePlayerView youTubePlayerView) {
        this.getLifecycle().addObserver(youTubePlayerView);
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


    public class FullScreenMediaController extends MediaController {

        private ImageButton fullScreen;


        public FullScreenMediaController(Context context) {
            super(context);
        }


        @Override
        public void setAnchorView(View view) {

            super.setAnchorView(view);

            //image button for full screen to be added to media controller
            fullScreen = new ImageButton(super.getContext());

            FrameLayout.LayoutParams params =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            params.rightMargin = 80;
            addView(fullScreen, params);


            isFullScreen_videoVw = false;

            fullScreen.setImageResource(R.drawable.full_size);


            //add listener to image button to handle full screen and exit full screen events
            fullScreen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFullScreen_videoVw) {
                        isFullScreen_videoVw = true;
                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().hide();
                        parent_frag_Card_dtl.video_onFullscrreen(true);
                        text_cardDescription.setVisibility(View.GONE);
                        text_cardName.setVisibility(View.GONE);
                        fullScreen.setImageResource(R.drawable.full_screen_exit);
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        fl_video_contr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        // btn_temp.setVisibility(GONE);

                        video_vw.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    } else {
                        isFullScreen_videoVw = false;
                        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        fullScreen.setImageResource(R.drawable.full_size);
                        text_cardDescription.setVisibility(VISIBLE);
                        text_cardName.setVisibility(VISIBLE);
                        //btn_temp.setVisibility(INVISIBLE);
                        parent_frag_Card_dtl.video_onFullscrreen(false);
                        fl_video_contr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250));
                        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 250);
                        params1.gravity = Gravity.CENTER;

                        params1.bottomMargin = 5;
                        params1.topMargin = 5;
                        params1.rightMargin = 5;
                        params1.leftMargin = 5;
                        params1.height = 250;

                        video_vw.setLayoutParams(params1);


                    }

                }
            });
        }
    }

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
        if (!isVisibleToUser && customMediaController != null && video_vw != null) {
            video_vw.suspend();
        }

        if (isVisibleToUser && customMediaController != null && video_vw != null) {
            video_vw.resume();
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
        if (!isVisibleToUser && cardModelObj != null && cardModelObj.getData_obj() != null) {
            List<BlogResponseModel> list_blogResponseModelObj = cardModelObj.getData_obj();
            for (int i = 0; i < list_blogResponseModelObj.size(); i++) {
                BlogResponseModel blogResponseModelObj = list_blogResponseModelObj.get(i);
                if (blogResponseModelObj.getType() != null && blogResponseModelObj.getType().equals("audio")) {
                    Blog_Adapter.AudioViewHolder holder = (Blog_Adapter.AudioViewHolder) recycle_mv_contr.findViewHolderForAdapterPosition(i);
                    if (holder != null && holder.isAudioPlay) {
                        holder.mediaPlayer.pause();
                        holder.isAudioPlay = false;
                        holder.img_play_stop.setImageResource(R.drawable.play_rec);
                    }

                }
                if (blogResponseModelObj.getType() != null && blogResponseModelObj.getType().equals("video")) {
                    Blog_Adapter.VideoViewHolder holder = (Blog_Adapter.VideoViewHolder) recycle_mv_contr.findViewHolderForAdapterPosition(i);
                    if (holder != null && holder.customMediaController != null && holder.video_vw != null) {
                        holder.customMediaController.hide();
                        if (getContext() != null)
                            img_play_pause_video.setBackground(getContext().getResources().getDrawable(R.drawable.play_btn));
                        holder.video_vw.pause();
                    }
                    if (holder != null && holder.dlg_MediaController != null && holder.dlg_video_vw != null) {
                        holder.dlg_MediaController.hide();
                        if (getContext() != null)
                            img_play_pause_video.setBackground(getContext().getResources().getDrawable(R.drawable.play_btn));
                        holder.dlg_video_vw.pause();
                    }
                }
                if (blogResponseModelObj.getType() != null && blogResponseModelObj.getType().equals("youtube")) {
                    Blog_Adapter.ViewHolder holder = (Blog_Adapter.ViewHolder) recycle_mv_contr.findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        if (holder.holder_UTubePlayer != null) {
                            // Log.v (TAG, "Releasing youtube player, URL : " + getArguments().getString(KeyConstant.KEY_VIDEO_URL));
                            holder.holder_UTubePlayer.pause();

                        }
                        if (holder.dlg_UTubePlayer != null) {
                            // Log.v (TAG, "Releasing youtube player, URL : " + getArguments().getString(KeyConstant.KEY_VIDEO_URL));
                            holder.dlg_UTubePlayer.pause();

                        }
                    }

                }
            }
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
            youTubePlayer.cueVideo(sel_blog_res_modelObj.getValue());
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
        if (cardModelObj != null && cardModelObj.getData_obj() != null) {
            for (int i = 0; i < cardModelObj.getData_obj().size(); i++) {
                BlogResponseModel blogResponseModelObj = cardModelObj.getData_obj().get(i);
                if (blogResponseModelObj.getType() != null && blogResponseModelObj.getType().equals("audio")) {
                    Blog_Adapter.AudioViewHolder holder = (Blog_Adapter.AudioViewHolder) recycle_mv_contr.findViewHolderForAdapterPosition(i);
                    if (holder != null && holder.isAudioPlay) {
                        holder.mediaPlayer.pause();
                        holder.isAudioPlay = false;
                        holder.img_play_stop.setImageResource(R.drawable.play_rec);
                    }

                }
            }
        }

    }


    @Override
    public void onSlideImageClick(int Position) {
        // load_card_image(cardModelObj.getMultiple_img_url().get(Position).getImg_url(), , true)

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View imgEntryView = inflater.inflate(R.layout.dialog_fullscreen, null);
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen); //default fullscreen titlebar
        PhotoView img = (PhotoView) imgEntryView.findViewById(R.id.usericon_large);

        // load_card_image(cardModelObj.getMultiple_img_url().get(Position).getImg_url(), img, true);
        //  img.setImageURI(Uri.parse(cardModelObj.getMultiple_img_url().get(Position).getImg_url()));

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(cardModelObj.getMultiple_img_url().get(Position).getImg_url()))
                .setAutoRotateEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {

            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (dataSource.isFinished() && bitmap != null) {
                    Log.d("Bitmap", "has come");
                    Bitmap bmp = Bitmap.createBitmap(bitmap);
                    img.setImageBitmap(bmp);
                    dataSource.close();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (dataSource != null) {
                    dataSource.close();
                }
            }
        }, CallerThreadExecutor.getInstance());

        dialog.setContentView(imgEntryView);
        dialog.show();

    }
}
