package com.digital_easy.info_share.Custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.Display;

import java.lang.reflect.Field;

public class CustomTabLayout extends TabLayout {

    private static final int WIDTH_INDEX = 0;
    private int DIVIDER_FACTOR = 1;
    private static final String SCROLLABLE_TAB_MIN_WIDTH = "mScrollableTabMinWidth";
    Context scrn_context;


    public CustomTabLayout(Context context) {
        super(context);
        initTabMinWidth();
    }

    public void setDIVIDER_FACTOR(int Value) {
        DIVIDER_FACTOR = Value;
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrn_context = context;
        initTabMinWidth();
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTabMinWidth();
    }

    public void initTabMinWidth() {
        Display display = ((Activity) scrn_context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        // int[] wh = Utils.getScreenSize(getContext());
        int tabMinWidth = size.x / DIVIDER_FACTOR;

        Field field;
        try {
            field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}