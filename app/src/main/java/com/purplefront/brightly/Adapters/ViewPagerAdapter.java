package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.purplefront.brightly.Activities.CreateCards;
import com.purplefront.brightly.Activities.MySetCards;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private ProgressDialog dialog;
    // Declare Variables
    Activity context;
    List<CardsListModel> cardsListModels;
    LayoutInflater inflater;
    String set_id;
    String userId;
    String set_name;
    String image_name;
    String image_id;


    public ViewPagerAdapter(MySetCards mySetCards, List<CardsListModel> cardsListModels, String set_id, String userId, String set_name) {
        this.context = mySetCards;
        this.cardsListModels = cardsListModels;
        this.set_id = set_id;
        this.userId = userId;
        this.set_name = set_name;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return cardsListModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ScrollView) object);

    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Context context1 = container.getContext();
        LayoutInflater inflater = LayoutInflater.from(context1);


        if (position < cardsListModels.size() - 1) {
            // Inflate the custom layout

            CardsListModel cardsListModel = cardsListModels.get(position);
            //file_type
            String type = cardsListModel.getType();


            if (type.equals("image")) {

                // Inflate the custom layout
                View itemView = inflater.inflate(R.layout.items_cards_pager, container, false);


                // Declare Variables
                TextView text_cardName;
                TextView text_cardDescription;
                ImageView image_cardImage;

                // Locate the TextViews in viewpager_item.xml
                text_cardName = (TextView) itemView.findViewById(R.id.text_cardName);
                text_cardDescription = (TextView) itemView.findViewById(R.id.text_cardDescription);
                image_cardImage = (ImageView) itemView.findViewById(R.id.image_cardImage);

                if (cardsListModel.getTitle() != null) {
                    text_cardName.setText(cardsListModel.getTitle());
                }

                if (cardsListModel.getDescription() != null) {
                    text_cardDescription.setText(cardsListModel.getDescription());
                }
                image_cardImage.setVisibility(View.VISIBLE);

                image_id = cardsListModel.getCard_id();
                image_name = cardsListModel.getName();

                if (!cardsListModel.getUrl().isEmpty() && cardsListModel.getUrl() != null) {

                    dialog = new ProgressDialog(context);
                    dialog.setMessage("Please wait...");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Glide.with(context)
                            .load(cardsListModel.getUrl())
                            .centerCrop()
                            /*.transform(new CircleTransform(HomeActivity.this))
                            .override(50, 50)*/
                            .into(image_cardImage);
                    dialog.cancel();
                    dialog.dismiss();

                } else {
                    Glide.with(context)
                            .load(R.drawable.no_image_available)
                            .centerCrop()
                            /*.transform(new CircleTransform(HomeActivity.this))
                            .override(50, 50)*/
                            .into(image_cardImage);
                }
                // Add viewpager_item.xml to ViewPager
                ((ViewPager) container).addView(itemView);
                return itemView;

            } else if (type.equals("video")) {

                // Inflate the custom layout
                View itemView = inflater.inflate(R.layout.items_cards_youtube_pager, container, false);


                // Declare Variables
                TextView text_cardName;
                TextView text_cardDescription;
                FrameLayout containerYouTubePlayer;

                // Locate the TextViews in viewpager_item.xml
                text_cardName = (TextView) itemView.findViewById(R.id.text_cardName);
                text_cardDescription = (TextView) itemView.findViewById(R.id.text_cardDescription);
                containerYouTubePlayer = (FrameLayout) itemView.findViewById(R.id.youtube_holder);
                if (cardsListModel.getTitle() != null) {
                    text_cardName.setText(cardsListModel.getTitle());
                }

                if (cardsListModel.getDescription() != null) {
                    text_cardDescription.setText(cardsListModel.getDescription());
                }
                image_id = cardsListModel.getCard_id();
                image_name = cardsListModel.getName();

                // Add the YouTube fragment to view
                YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
                (context).getFragmentManager().beginTransaction().add(R.id.youtube_holder, youTubePlayerFragment).commit();
                youTubePlayerFragment.initialize("AIzaSyDPwTq4xr0Fq-e1z0tDEBaj3qgAgi5VJ44", new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
//                            Utils.logDebug(TAG, "onInitializationSuccess");

                        if (!wasRestored) {
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                            youTubePlayer.loadVideo(image_name);
                            youTubePlayer.setShowFullscreenButton(true);
                            //                            youTubePlayer.play();
                        }

                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                            Utils.logError(TAG, "Could not initialize YouTubePlayer");
                        containerYouTubePlayer.setVisibility(View.VISIBLE);

                    }
                });


                containerYouTubePlayer.setVisibility(View.VISIBLE);
                // Add viewpager_item.xml to ViewPager
                ((ViewPager) container).addView(itemView);
                return itemView;
            }


        } else {

            View itemView = inflater.inflate(R.layout.items_add, container, false);

            ImageView add_cardImage;

            add_cardImage = (ImageView) itemView.findViewById(R.id.add_cardImage);

            Glide.with(context)
                    .load(R.drawable.add_image_black)
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(add_cardImage);

            add_cardImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, CreateCards.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("set_id", set_id);
                    intent.putExtra("set_name", set_name);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }
            });


            // Add viewpager_item.xml to ViewPager
            ((ViewPager) container).addView(itemView);
            return itemView;

        }
        return container;

    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((ScrollView) object);

    }

}
