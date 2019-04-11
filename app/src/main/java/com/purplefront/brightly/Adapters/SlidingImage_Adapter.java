package com.purplefront.brightly.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.purplefront.brightly.Modules.MultipleImageModel;
import com.purplefront.brightly.R;

import java.util.ArrayList;
import java.util.List;

public class SlidingImage_Adapter extends PagerAdapter {


    private List<MultipleImageModel> IMAGES;
    private LayoutInflater inflater;
    private Context scrn_contxt;
    SlideImageCLicked mListener;

    public SlidingImage_Adapter(Context context, List<MultipleImageModel> IMAGES, SlideImageCLicked listener) {
        this.scrn_contxt = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.img_card_slider, view, false);

        assert imageLayout != null;
        final SimpleDraweeView imageView = (SimpleDraweeView) imageLayout
                .findViewById(R.id.image_cardImage);

        MultipleImageModel multipleImageModelObj = IMAGES.get(position);

        //imageView.setImageResource(IMAGES.get(position));
        load_card_image(multipleImageModelObj.getImg_url(), imageView, false);
        view.addView(imageLayout, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSlideImageClick(position);
            }
        });
        return imageLayout;
    }

    public void load_card_image(String URL, SimpleDraweeView imgView, boolean isFullScreen) {
        String img_url = URL;
        ResizeOptions resizeOptions = new ResizeOptions(350, 250);
        if (img_url != null && !img_url.trim().equals("")) {
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(scrn_contxt.getResources());
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
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


    public interface SlideImageCLicked {
        public void onSlideImageClick(int Position);

    }
}