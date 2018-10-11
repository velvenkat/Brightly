package com.purplefront.brightly.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.R;

import java.util.Locale;


public class Multimedia_CardFragment extends BaseFragment implements YouTubePlayer.OnInitializedListener {
    View rootView;
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
    boolean isUTubePlayFullScreen=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.items_multimedia_pager, container, false);

        Bundle bundle = getArguments();
        cardModelObj = bundle.getParcelable("card_mdl_obj");

        TextView text_cardName;
        TextView text_cardDescription;
        ImageView image_cardImage;

        // Locate the TextViews in viewpager_item.xml
        text_cardName = (TextView) rootView.findViewById(R.id.text_cardName);
        rl_audio_player = (RelativeLayout) rootView.findViewById(R.id.rl_audio_player);
        text_cardDescription = (TextView) rootView.findViewById(R.id.text_cardDescription);
        image_cardImage = (ImageView) rootView.findViewById(R.id.image_cardImage);
        frame_youtube = (FrameLayout) rootView.findViewById(R.id.frame_youtube);
        img_audio_play_stop = (ImageView) rootView.findViewById(R.id.img_play_stop);
        audio_seek_bar = (SeekBar) rootView.findViewById(R.id.seek_audio_rec);
        txt_PlayProgTime = (TextView) rootView.findViewById(R.id.txt_PlayProgTime);
        file_cardLink = (TextView) rootView.findViewById(R.id.file_cardLink);


        if (cardModelObj.getTitle() != null) {
            text_cardName.setText(cardModelObj.getTitle());
        }

        if (cardModelObj.getDescription() != null) {
            text_cardDescription.setText(cardModelObj.getDescription());
        }
        if (cardModelObj.getType().equalsIgnoreCase("image")) {
            image_cardImage.setVisibility(View.VISIBLE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);

            if (!cardModelObj.getUrl().isEmpty() && cardModelObj.getUrl() != null) {

                dialog = new ProgressDialog(getContext());
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Glide.with(getContext())
                        .load(cardModelObj.getUrl())
                        .fitCenter()
                        /*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*/
                        .into(image_cardImage);
                dialog.cancel();
                dialog.dismiss();

                image_cardImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        View imgEntryView = inflater.inflate(R.layout.dialog_fullscreen, null);
                        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen); //default fullscreen titlebar
                        ImageView img = (ImageView) imgEntryView.findViewById(R.id.usericon_large);

                        Glide.with(getContext())
                                .load(cardModelObj.getUrl())
                                .fitCenter()
                                /*.transform(new CircleTransform(HomeActivity.this))
                                .override(50, 50)*/
                                .into(img);

                        dialog.setContentView(imgEntryView);
                        dialog.show();

                        imgEntryView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
                                dialog.cancel();
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
        } else if (cardModelObj.getType().equalsIgnoreCase("video")) {
            image_cardImage.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.VISIBLE);
            rl_audio_player.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.GONE);
            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

            if (getUserVisibleHint()) {

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_youtube, youTubePlayerFragment).commit();
                //  mYouTubePlayerSupportFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);

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
            image_cardImage.setImageResource(R.drawable.audio_hdr_img);

            audio_player_initialize(audio_seek_bar, txt_PlayProgTime, img_audio_play_stop);
            setMediaPlayer(null, cardModelObj.getUrl());
        } else if (cardModelObj.getType().equalsIgnoreCase("file")) {

            image_cardImage.setVisibility(View.GONE);
            frame_youtube.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.GONE);
            file_cardLink.setVisibility(View.VISIBLE);
            file_cardLink.setText(cardModelObj.getUrl());
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
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && UTubePlayer != null) {
            // Log.v (TAG, "Releasing youtube player, URL : " + getArguments().getString(KeyConstant.KEY_VIDEO_URL));
            UTubePlayer.release();
            if (getChildFragmentManager() != null) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.remove(youTubePlayerFragment).commit();
            }
            UTubePlayer = null;
            ((BrightlyNavigationActivity)getActivity()).uTubePlayer=null;
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
            //  Log.v (TAG, "Initializing youtube player, URL : " + getArguments().getString(KeyConstant.KEY_VIDEO_URL));
            if (getChildFragmentManager() != null) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_youtube, youTubePlayerFragment).commit();
                youTubePlayerFragment.initialize(DEVELOPER_KEY, this);
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
//                            Utils.logDebug(TAG, "onInitializationSuccess");

        UTubePlayer = youTubePlayer;
        ((BrightlyNavigationActivity)getActivity()).uTubePlayer=UTubePlayer;
        UTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {

             if(b){
                 isUTubePlayFullScreen=true;
                 ((BrightlyNavigationActivity)getActivity()).isUTubePlayerFullScreen=true;
             }
             else {
                 isUTubePlayFullScreen = false;
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

    }

}
