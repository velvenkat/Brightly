package com.digital_easy.info_share.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Application.Brightly;
import com.digital_easy.info_share.Fragments.CreateMultimediaCard;
import com.digital_easy.info_share.Fragments.Multimedia_CardFragment;
import com.digital_easy.info_share.Modules.BlogResponseModel;
import com.digital_easy.info_share.Modules.CardsListModel;
import com.digital_easy.info_share.Modules.ContactHelperModule;
import com.digital_easy.info_share.Modules.MultipleImageModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.RichLinkViewTelegram;
import com.digital_easy.info_share.Utils.TimeFormat;
import com.digital_easy.info_share.Utils.ViewListener;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Blog_Adapter extends RecyclerView.Adapter {
    public List<BlogResponseModel> BlogResponseModelListObj;

    // AppCompatActivity _activity;

    Blog_Adapter.BlogAdapterListener mListener;
    String DEVELOPER_KEY = "AIzaSyDPwTq4xr0Fq-e1z0tDEBaj3qgAgi5VJ44";

    Activity scrn_activity;
    Multimedia_CardFragment cur_fragment;
    List<BlogResponseModel> uTubeBlogResponseModelListObj = new ArrayList<>();
    DisplayMetrics displayMetrics = new DisplayMetrics();

    public Blog_Adapter(Multimedia_CardFragment fragment, List<BlogResponseModel> model_list, Activity activity, Blog_Adapter.BlogAdapterListener listener) {
        BlogResponseModelListObj = model_list;
        // _activity = activity;
        mListener = listener;
        cur_fragment = fragment;
        scrn_activity = activity;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        BlogResponseModel blogResponseModel = BlogResponseModelListObj.get(viewType);
        String TYPE = blogResponseModel.getType();
        View crt_view;
        if (TYPE != null) {
            switch (TYPE) {
          /*  case _ADD:

                crt_view = inflater.inflate(R.layout.lo_btn_add_to_create, parent, false);

                return new Blog_Adapter.Add_Holder(crt_view);*/
                // Return a new holder instance
                case "img":

                    // Inflate the custom layout
                    crt_view = inflater.inflate(R.layout.lo_image_slide, parent, false);
                    return new Blog_Adapter.ImageViewHolder(crt_view, viewType);
                case "title":

                    // Inflate the custom layout
                    crt_view = inflater.inflate(R.layout.lo_title_cont, parent, false);
                    return new Blog_Adapter.TitleViewHolder(crt_view, viewType);
                case "audio":

                    // Inflate the custom layout
                    crt_view = inflater.inflate(R.layout.lo_audio_cont, parent, false);
                    return new Blog_Adapter.AudioViewHolder(crt_view, viewType);
                case "video":
                    crt_view = inflater.inflate(R.layout.lo_video_cont, parent, false);
                    return new Blog_Adapter.VideoViewHolder(crt_view, viewType, scrn_activity, mListener);
                case "weblink":
                    crt_view = inflater.inflate(R.layout.lo_weblink_cont, parent, false);
                    return new Blog_Adapter.WeblinkHolder(crt_view, viewType);

                case "youtube":
                    crt_view = inflater.inflate(R.layout.lo_utube_cont, parent, false);
                    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youtube_list, parent, false));
                case "text":
                    crt_view = inflater.inflate(R.layout.lo_text_cont, parent, false);
                    return new Blog_Adapter.TextViewHolder(crt_view, viewType);

                case "":
                    crt_view = inflater.inflate(R.layout.lo_dummy, parent, false);
                    return new Blog_Adapter.DummyHolder(crt_view, viewType);


            }
        } else if (blogResponseModel.getContactHelperModule() != null) {

            crt_view = inflater.inflate(R.layout.lo_contact_cont, parent, false);
            return new Blog_Adapter.ContactHolder(crt_view, viewType);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BlogResponseModel blogResponseModel = BlogResponseModelListObj.get(position);
        String TYPE = blogResponseModel.getType();
        if (TYPE != null) {
            switch (TYPE) {
                case "audio":

                    ((AudioViewHolder) holder).setMediaPlayer(null, blogResponseModel.getValue(), false);
                    break;
                case "img":

                    set_card_image(holder.itemView.getContext(), ((ImageViewHolder) holder).img_blog, blogResponseModel.getValue());
                    ((ImageViewHolder) holder).img_blog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // load_card_image(cardModelObj.getMultiple_img_url().get(Position).getImg_url(), , true)

                            LayoutInflater inflater = LayoutInflater.from(scrn_activity);
                            View imgEntryView = inflater.inflate(R.layout.dialog_fullscreen, null);
                            final Dialog dialog = new Dialog(scrn_activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen); //default fullscreen titlebar
                            PhotoView img = (PhotoView) imgEntryView.findViewById(R.id.usericon_large);

                            // load_card_image(cardModelObj.getMultiple_img_url().get(Position).getImg_url(), img, true);
                            //  img.setImageURI(Uri.parse(cardModelObj.getMultiple_img_url().get(Position).getImg_url()));

                            ImageRequest imageRequest = ImageRequestBuilder
                                    .newBuilderWithSource(Uri.parse(blogResponseModel.getValue()))
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
                    });
                    break;
                case "video":
                    HttpProxyCacheServer proxy = Brightly.getProxy(scrn_activity);
                    String proxyUrl = proxy.getProxyUrl(blogResponseModel.getValue());

                    ((VideoViewHolder) holder).setVideo(((VideoViewHolder) holder).video_vw, proxyUrl);

                    break;
                case "youtube":
                    ((ViewHolder) holder).onBind(position);
                    break;
                case "text":
                    ((TextViewHolder) holder).txt_Text.setText(blogResponseModel.getValue());
                    break;
                case "title":
                    if (position != 0) {
                        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 250);


                        params1.topMargin = 15;
                        params1.rightMargin = 10;
                        params1.leftMargin = 10;
                        params1.bottomMargin = 10;
                        ((TitleViewHolder) holder).txt_Title.setLayoutParams(params1);
                    }
                    ((TitleViewHolder) holder).txt_Title.setText(blogResponseModel.getValue());

                    break;
                case "weblink":
                    if (blogResponseModel.getValue() != null && blogResponseModel.getValue() != "") {
                        ((WeblinkHolder) holder).file_card_link.setText(blogResponseModel.getValue());


                        if (!cur_fragment.isVisible()) {

                        } else {
                            //file_progress = showProgress();

                        }

                        //     isFileLoaded = false;
                        ((WeblinkHolder) holder).richLinkViewTelegram.setLink(((WeblinkHolder) holder).file_card_link.getText().toString(), new ViewListener() {
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
                                ((WeblinkHolder) holder).richLinkViewTelegram.setVisibility(View.GONE);
                                ((WeblinkHolder) holder).file_card_link.setVisibility(View.VISIBLE);
                                //  file_cardLink.setTextColor(Color.argb(0, 100, 20, 20));
                                ((WeblinkHolder) holder).file_card_link.setText(e.getMessage());
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
                    ((WeblinkHolder) holder).file_card_link.setMovementMethod(LinkMovementMethod.getInstance());
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

                    break;

            }
        } else if (blogResponseModel.getContactHelperModule() != null) {
            ContactHelperModule contactHelperModuleObj = blogResponseModel.getContactHelperModule();
            ((ContactHolder) holder).txt_dtls.setText(" : " + contactHelperModuleObj.values);
            switch (contactHelperModuleObj.type) {
                case "1":
                    ((ContactHolder) holder).txt_caption.setText("Name");
                    break;
                case "2":
                    ((ContactHolder) holder).txt_caption.setText("Company");
                    break;
                case "3":
                    ((ContactHolder) holder).txt_caption.setText("Title");
                    break;
                case "4":
                    ((ContactHolder) holder).txt_caption.setText("Mobile No");
                    break;
                case "5":
                    ((ContactHolder) holder).txt_caption.setText("Office No");
                    break;
                case "6":
                    ((ContactHolder) holder).txt_caption.setText("Email Id");
                    break;
                case "7":
                    ((ContactHolder) holder).txt_caption.setText("Notes");

                    break;

            }

        }

    }

    @Override
    public int getItemCount() {
        return BlogResponseModelListObj.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class TextViewHolder extends RecyclerView.ViewHolder {

        TextView txt_Text;

        public TextViewHolder(View itemView, int Position) {
            super(itemView);
            txt_Text = itemView.findViewById(R.id.txt_text);
        }
    }


    class WeblinkHolder extends RecyclerView.ViewHolder {

        TextView file_card_link;
        RichLinkViewTelegram richLinkViewTelegram;

        public WeblinkHolder(View itemView, int AdapterPos) {
            super(itemView);
            file_card_link = itemView.findViewById(R.id.file_cardLink);
            richLinkViewTelegram = itemView.findViewById(R.id.preview);
        }
    }

  /*  class YouTubeHolder extends RecyclerView.ViewHolder implements YouTubePlayer.OnInitializedListener {

        FrameLayout frameLayout;
        YouTubePlayer UTubePlayer;
        boolean isUTubePlayFullScreen;
        String URL;


        public YouTubeHolder(View itemView, int AdapterPos) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.frame_youtube);


        }

        public void youtubeInit(String Url) {
            URL = Url;
            if (cur_fragment.getUserVisibleHint()) {


                YouTubePlayerSupportFragment youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
                FragmentTransaction transaction = cur_fragment.getChildFragmentManager().beginTransaction();
                transaction.replace(frameLayout.getId(), youTubePlayerSupportFragment).commit();

                youTubePlayerSupportFragment.initialize(DEVELOPER_KEY, YouTubeHolder.this);
            }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
            //parent_frag_Card_dtl.move_card();
            UTubePlayer = youTubePlayer;
            if (scrn_activity != null)
                ((BrightlyNavigationActivity) scrn_activity).uTubePlayer = UTubePlayer;
            UTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean b) {

                    if (b) {
                        isUTubePlayFullScreen = true;
                        if (scrn_activity != null)
                            ((BrightlyNavigationActivity) scrn_activity).isUTubePlayerFullScreen = true;
                    } else {
                        isUTubePlayFullScreen = false;
                        if (scrn_activity != null)
                            ((BrightlyNavigationActivity) scrn_activity).isUTubePlayerFullScreen = false;
                    }
                }
            });
            if (!wasRestored) {

                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.cueVideo(URL);
                // youTubePlayer.loadVideo(image_name);
                youTubePlayer.setShowFullscreenButton(true);
                youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                youTubePlayer.pause();
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        }
    }*/

    class ContactHolder extends RecyclerView.ViewHolder {

        TextView txt_caption;
        TextView txt_dtls;

        public ContactHolder(View itemView, int AdapterPos) {
            super(itemView);
            txt_caption = itemView.findViewById(R.id.txt_cap);
            txt_dtls = itemView.findViewById(R.id.txt_dtls);
        }
    }

    class DummyHolder extends RecyclerView.ViewHolder {

        TextView txt_caption;
        TextView txt_dtls;

        public DummyHolder(View itemView, int AdapterPos) {
            super(itemView);
            txt_caption = itemView.findViewById(R.id.txt_cap);
            txt_dtls = itemView.findViewById(R.id.txt_dtls);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView txt_Title;

        public TitleViewHolder(View itemView, int Position) {
            super(itemView);
            txt_Title = itemView.findViewById(R.id.txt_title);
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public VideoView video_vw, dlg_video_vw;
        boolean isFullScreen_videoVw = false;
        ImageView img_play_pause_video;
        public FullScreenMediaController customMediaController, dlg_MediaController;
        FrameLayout fl_video_contr;
        TextView txtBuffer;
        Context scrn_contxt;
        Handler buffer_handler;
        Activity scrn_activity;
        Runnable buffer_updaterunnable;
        BlogAdapterListener mListener;
        String VIDEO_URL;

        public VideoViewHolder(View itemView, int Position, Activity activity, BlogAdapterListener listener) {
            super(itemView);
            //video_vw.invalidate();
            scrn_contxt = itemView.getContext();
            scrn_activity = activity;
            mListener = listener;
            video_vw = itemView.findViewById(R.id.video_vw);
            fl_video_contr = itemView.findViewById(R.id.video_contr);
            img_play_pause_video = itemView.findViewById(R.id.play_pause);
            img_play_pause_video.setVisibility(View.VISIBLE);
            txtBuffer = itemView.findViewById(R.id.txt_buffer);
            customMediaController = new FullScreenMediaController(itemView.getContext(), false);
            customMediaController.setAnchorView(fl_video_contr);
            customMediaController.requestFocus();
            video_vw.setMediaController(customMediaController);


            //     video_vw.setZOrderOnTop(true);


            video_vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //   customMediaController.show(0);
                    setBufferUpdateListener(txtBuffer, mp);
                    mp.setLooping(true);
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            img_play_pause_video.setVisibility(View.VISIBLE);
                            video_vw.seekTo(1);
                            video_vw.pause();

                            if (!isFullScreen_videoVw) {

                            }

                            // customMediaController.show();
                        }
                    });
                }

            });


            img_play_pause_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (video_vw.isPlaying()) {
                        customMediaController.hide();
                        img_play_pause_video.setBackground(scrn_contxt.getResources().getDrawable(R.drawable.play_btn));

                        video_vw.pause();
                    } else {
                        customMediaController.show();
                        img_play_pause_video.setBackground(scrn_contxt.getResources().getDrawable(R.drawable.pause));
                        video_vw.start();
                    }
                }
            });


            video_vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    //vidView.start();
                    txtBuffer.setVisibility(View.GONE);
                    img_play_pause_video.setBackground(scrn_contxt.getResources().getDrawable(R.drawable.play_btn));
                }
            });


        }

        public void setVideo(VideoView _video_vw, String URL) {
            VIDEO_URL = URL;
            _video_vw.setVideoURI(Uri.parse(URL));
            _video_vw.requestFocus();
            _video_vw.start();
        }

        private void setBufferUpdateListener(TextView _txtBuffer, MediaPlayer mp) {
            mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    _txtBuffer.setText(String.valueOf(percent) + "%");
                    _txtBuffer.setVisibility(View.VISIBLE);
                    if (buffer_handler == null)
                        buffer_handler = new Handler();
                    else
                        buffer_handler.removeCallbacks(buffer_updaterunnable);
                    buffer_updaterunnable = new Runnable() {
                        @Override
                        public void run() {
                            _txtBuffer.setVisibility(View.GONE);
                        }
                    };
                    buffer_handler.postDelayed(buffer_updaterunnable, 2000);
                }
            });
        }

        public class FullScreenMediaController extends MediaController {

            private ImageButton fullScreen;
            boolean isDialogCalled = false;

            public FullScreenMediaController(Context context, boolean _isDialogCalled) {
                super(context);
                isDialogCalled = _isDialogCalled;
            }

            @Override
            public void show() {
                super.show(0);//Default no auto hide timeout
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
                      /*  if (!isFullScreen_videoVw) {
                            isFullScreen_videoVw = true;
                            ((BrightlyNavigationActivity) scrn_activity).getSupportActionBar().hide();
                            mListener.Update_Video_onFullScreen(true);

                            fullScreen.setImageResource(R.drawable.full_screen_exit);
                            scrn_activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            fl_video_contr.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


                            video_vw.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                        } else {
                            isFullScreen_videoVw = false;
                            ((BrightlyNavigationActivity) scrn_activity).getSupportActionBar().show();
                            scrn_activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            fullScreen.setImageResource(R.drawable.full_size);

                            mListener.Update_Video_onFullScreen(false);
                            fl_video_contr.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250));
                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 250);
                            params1.gravity = Gravity.CENTER;

                            params1.bottomMargin = 5;
                            params1.topMargin = 5;
                            params1.rightMargin = 5;
                            params1.leftMargin = 5;
                            params1.height = 250;

                            video_vw.setLayoutParams(params1);


                        }
*/
                        if (!isDialogCalled) {
                            LayoutInflater inflater = LayoutInflater.from(scrn_activity);
                            View imgEntryView = inflater.inflate(R.layout.lo_video_cont, null);
                            final Dialog dialog = new Dialog(scrn_activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen); //default fullscreen titlebar
                            dialog.setContentView(imgEntryView);
                            dialog.show();
                            FrameLayout dlg_fl_video_contr = imgEntryView.findViewById(R.id.video_contr);
                            dlg_video_vw = imgEntryView.findViewById(R.id.video_vw);


                            fullScreen.setImageResource(R.drawable.full_screen_exit);

                            dlg_fl_video_contr.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


                            dlg_video_vw.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                            ImageView dlg_img_play_pause_video = imgEntryView.findViewById(R.id.play_pause);
                            TextView dlg_txtBuffer = imgEntryView.findViewById(R.id.txt_buffer);
                            dlg_MediaController = new FullScreenMediaController(itemView.getContext(), true);
                            dlg_MediaController.setAnchorView(dlg_fl_video_contr);
                            dlg_MediaController.requestFocus();
                            dlg_video_vw.setMediaController(dlg_MediaController);


                            //     video_vw.setZOrderOnTop(true);


                            dlg_video_vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    dlg_MediaController.show(0);
                                    setBufferUpdateListener(dlg_txtBuffer, mp);
                                    mp.setLooping(true);
                                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                        @Override
                                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                            dlg_img_play_pause_video.setVisibility(View.VISIBLE);
                                            dlg_video_vw.seekTo(1);
                                            dlg_video_vw.pause();

                                            if (!isFullScreen_videoVw) {

                                            }

                                            // customMediaController.show();
                                        }
                                    });
                                }

                            });


                            dlg_img_play_pause_video.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (dlg_video_vw.isPlaying()) {
                                        dlg_MediaController.hide();
                                        dlg_img_play_pause_video.setBackground(scrn_contxt.getResources().getDrawable(R.drawable.play_btn));

                                        dlg_video_vw.pause();
                                    } else {
                                        dlg_MediaController.show();
                                        dlg_img_play_pause_video.setBackground(scrn_contxt.getResources().getDrawable(R.drawable.pause));
                                        dlg_video_vw.start();
                                    }
                                }
                            });


                            dlg_video_vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    //vidView.start();
                                    dlg_txtBuffer.setVisibility(View.GONE);
                                    dlg_img_play_pause_video.setBackground(scrn_contxt.getResources().getDrawable(R.drawable.play_btn));
                                }
                            });
                            setVideo(dlg_video_vw, VIDEO_URL);

                        }
                    }
                });

            }
        }
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img_blog;

        public ImageViewHolder(View itemView, int Position) {
            super(itemView);
            img_blog = itemView.findViewById(R.id.img_blog);

        }
    }


    public void set_card_image(Context context, SimpleDraweeView imageView, String URI_value) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        builder.setProgressBarImage(R.drawable.loader);

        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        imageView.setHierarchy(hierarchy);


        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(URI_value))
                        .setResizeOptions(CreateMultimediaCard.mResizeOptions)

                        .build();
        imageView.setImageRequest(imageRequest);
    }

    public interface BlogAdapterListener {


        // public void youtube_init(FrameLayout frameLayout,BlogResponseModel moodelObj);


        public void Update_Video_onFullScreen(boolean isFullScreen);

        public void uTube_setLifeCycleObserver(YouTubePlayerView youTubePlayerView);


    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        private int mCurrentPosition;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        protected abstract void clear();

        public void onBind(int position) {
            mCurrentPosition = position;
            clear();
        }

        public int getCurrentPosition() {
            return mCurrentPosition;
        }

    }

    public class ViewHolder extends BaseViewHolder {

        TextView textWaveTitle;


        BlogResponseModel youtubeModelObj;

        SimpleDraweeView imageViewItems;
        String youtube_id = "";
        public YouTubePlayerView youTubePlayerView;
        Context scrn_contxt;
        public com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer holder_UTubePlayer, dlg_UTubePlayer;

        public ViewHolder(View itemView) {
            super(itemView);
            //  ButterKnife.bind(this, itemView);
            //youtubeModelObj = modelObj;
            scrn_contxt = itemView.getContext();
            textWaveTitle = itemView.findViewById(R.id.textViewTitle);

            imageViewItems = itemView.findViewById(R.id.imageViewItem);
            youTubePlayerView = itemView.findViewById(R.id.youtube_view);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            // final YoutubeVideo mYoutubeVideo = mYoutubeVideos.get(position);
            youtubeModelObj = BlogResponseModelListObj.get(position);
            /*((Activity) itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
*/
            textWaveTitle.setVisibility(View.GONE);


            String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(youtubeModelObj.getValue());

            if (matcher.find()) {
                youtube_id = matcher.group();
            } else {

            }
            if (youtube_id != "") {
                /*Glide.with(itemView.getContext())
                        .load(mYoutubeVideo.getImageUrl()).
                        apply(new RequestOptions().override(width - 36, 200))
                        .into(imageViewItems);*/
                //  set_card_image(scrn_contxt, imageViewItems, "https://img.youtube.com/vi/" + youtube_id + "/hqdefault.jpg");
            }
            imageViewItems.setVisibility(View.GONE);

            youTubePlayerView.setVisibility(View.VISIBLE);
            youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    holder_UTubePlayer = initializedYouTubePlayer;

                    // youTubePlayer.loadVideo(image_name);


                    initializedYouTubePlayer.cueVideo(youtube_id, 1);
                }
            }), true);
            mListener.uTube_setLifeCycleObserver(youTubePlayerView);
            youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
                @Override
                public void onYouTubePlayerEnterFullScreen() {

                    //Toast.makeText(scrn_contxt, "IsFullScreen", Toast.LENGTH_LONG).show();
                    LayoutInflater inflater = LayoutInflater.from(scrn_activity);
                    View imgEntryView = inflater.inflate(R.layout.dialog_utube_fullscreen, null);
                    final Dialog dialog = new Dialog(scrn_activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen); //default fullscreen titlebar
                    dialog.setContentView(imgEntryView);
                    dialog.show();
                    YouTubePlayerView youTubePlayerViewObj = imgEntryView.findViewById(R.id.dialog_uTube);
                    mListener.uTube_setLifeCycleObserver(youTubePlayerViewObj);
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {


                                dialog.dismiss();


                                youTubePlayerView.exitFullScreen();

                                return true;
                            }
                            return false;
                        }
                    });


                    youTubePlayerViewObj.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            dlg_UTubePlayer = initializedYouTubePlayer;

                            // youTubePlayer.loadVideo(image_name);
                            youTubePlayerViewObj.enterFullScreen();

                            initializedYouTubePlayer.cueVideo(youtube_id, 1);
                        }
                    }), true);

                    youTubePlayerViewObj.addFullScreenListener(new YouTubePlayerFullScreenListener() {

                        @Override
                        public void onYouTubePlayerEnterFullScreen() {

                        }

                        @Override
                        public void onYouTubePlayerExitFullScreen() {
                            dialog.dismiss();
                            youTubePlayerView.exitFullScreen();
                        }
                    });
                }

                @Override
                public void onYouTubePlayerExitFullScreen() {
                    //Toast.makeText(scrn_contxt, "IsFullScreen FALSE", Toast.LENGTH_LONG).show();
                }
            });

        }
    }


    public class AudioViewHolder extends RecyclerView.ViewHolder implements MediaPlayer.OnBufferingUpdateListener {

        SeekBar audio_seek_bar;


        public ImageView img_play_stop;
        TextView txt_PlayProgTime;
        String TotalTime;
        public boolean isAudioPlay;
        int mediaFileLengthInMilliseconds;
        String cur_audio_Pos;

        public MediaPlayer mediaPlayer;

        Context scrn_context;
        Handler handler = new Handler();
        int AdapterPos;

        public AudioViewHolder(View itemView, int Position) {
            super(itemView);
            AdapterPos = Position;
            scrn_context = itemView.getContext();
            img_play_stop = itemView.findViewById(R.id.img_play_stop);
            audio_seek_bar = itemView.findViewById(R.id.seek_audio_rec);
            txt_PlayProgTime = itemView.findViewById(R.id.txt_PlayProgTime);
            img_play_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAudioPlay) {
                        mediaPlayer.pause();
                        img_play_stop.setImageResource(R.drawable.play_rec);
                        isAudioPlay = false;
                        //mediaPlayer.prepareAsync();
                    } else {

                        img_play_stop.setImageResource(R.drawable.stop_rec);

                        mediaPlayer.start();
                        isAudioPlay = true;
                        primarySeekBarProgressUpdater();

                    }
                }
            });

/*
            setSeek_Bar();
            setImg_play_stop_Listener();
*/


        }

        private void primarySeekBarProgressUpdater() {
            if (isAudioPlay) {
                audio_seek_bar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
                cur_audio_Pos = TimeFormat.formateMilliSeccond(mediaPlayer.getCurrentPosition());
                txt_PlayProgTime.setText(cur_audio_Pos + "/" + TotalTime);
                if (mediaPlayer.isPlaying()) {
                    Runnable notification = new Runnable() {
                        public void run() {
                            primarySeekBarProgressUpdater();
                        }
                    };
                    handler.postDelayed(notification, 1000);
                }
            } else {
                //   cur_audio_Pos = TotalTime;
            }
        }

        public void setAudioProgText() {
            txt_PlayProgTime.setText(cur_audio_Pos + "/" + TotalTime);
        }

        public MediaPlayer setMediaPlayer(Uri audio_uri, String filePath, boolean isCreateCard) {
            mediaPlayer = new MediaPlayer();


            try {

                AudioManager am = (AudioManager) scrn_context.getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


                if (audio_uri != null) {
                    mediaPlayer.setDataSource(scrn_context, audio_uri);
                } else {
                    mediaPlayer.setDataSource(filePath);
                }
                if (!isCreateCard) {
                    txt_PlayProgTime.setVisibility(View.INVISIBLE);
                    mediaPlayer.setOnBufferingUpdateListener(this);
                } else
                    audio_seek_bar.setSecondaryProgress(audio_seek_bar.getMax());
                mediaPlayer.prepareAsync();
                mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // mp.stop();
                        cur_audio_Pos = TotalTime;
                        setAudioProgText();
                        img_play_stop.setImageResource(R.drawable.play_rec);
                        isAudioPlay = false;
                        audio_seek_bar.setProgress(100);
                        mediaPlayer = mp;
                        //   mp.prepareAsync();
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // to get total duration in milliseconds
                        mediaPlayer = mp;
                        long currentDuration = mediaPlayer.getCurrentPosition();
                        TotalTime = TimeFormat.formateMilliSeccond(mediaFileLengthInMilliseconds);
                        cur_audio_Pos = TimeFormat.formateMilliSeccond(currentDuration);
                        txt_PlayProgTime.setText(cur_audio_Pos + "/" + TotalTime);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
            return mediaPlayer;

        }


        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            audio_seek_bar.setSecondaryProgress(percent);
            if (percent == 100) {
                txt_PlayProgTime.setVisibility(View.VISIBLE);
            }
        }
    }

}
