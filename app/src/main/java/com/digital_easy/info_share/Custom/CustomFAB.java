package com.digital_easy.info_share.Custom;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digital_easy.info_share.R;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionContent;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingActionListener;

import java.util.ArrayList;
import java.util.List;

public class CustomFAB extends LinearLayout implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    LayoutInflater mInflater;
    RelativeLayout fab_contr;
    RapidFloatingActionContentLabelList rfaContent;

    private RapidFloatingActionLayout rfaLayout;

    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    View view;
    FABitem_choosed_listener mListener;

    public CustomFAB(Context context, FABitem_choosed_listener listener) {
        super(context);
        mInflater = LayoutInflater.from(context);
        mListener = listener;
        view = mInflater.inflate(R.layout.lo_btn_add_to_create, null);
        init();

    }

    public CustomFAB(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.lo_btn_add_to_create, null);
        init();
    }

    public CustomFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.lo_btn_add_to_create, null);

        init();
    }

    public void init() {
        this.addView(view);
        View root = view.findViewById(R.id.rfab__content_label_list_root_view);

       // fab_contr = view.findViewById(R.id.fab_contr);

        //    rfaLayout = view.findViewById(R.id.activity_main_rfal);
        //  rfaBtn = view.findViewById(R.id.activity_main_rfab);


        rfaBtn.setOnClickListener(new RapidFloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean tag = rfaLayout.isExpanded();

                if (tag) {
                    fab_contr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                } else {
                    fab_contr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1500));
                }
                rfabHelper.toggleContent();
            }
        });


        rfaContent = new RapidFloatingActionContentLabelList(getContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Title")
                .setResId(R.drawable.pencil_edit_button)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(0xff283593)

                .setWrapper(3)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Image")
                .setResId(R.drawable.image_icon)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Audio")
                .setResId(R.drawable.audio_type)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(getContext(), 4)))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Video")
                .setResId(R.drawable.upload_video)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Youtube")
                .setResId(R.drawable.upload_video)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Weblink")
                .setResId(R.drawable.pencil_edit_button)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Contact")
                .setResId(R.drawable.pencil_edit_button)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(0xff283593)

                .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(getContext(), 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(getContext(), 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout root_ = (LinearLayout) rfaContent.findViewById(R.id.rfab__content_label_list_root_view);
                root_.setClickable(false);
                root_.setFocusable(false);
                root_.setFocusableInTouchMode(false);
                root_.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Root clicked", Toast.LENGTH_LONG).show();
                    }
                });
                rfaContent.invalidate();

            }
        }, 2000);


    }


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        fab_contr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mListener.onItemChoosed(position);
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        fab_contr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mListener.onItemChoosed(position);
        rfabHelper.toggleContent();
    }

    public interface FABitem_choosed_listener {
        public void onItemChoosed(int sel_item);
    }


}
