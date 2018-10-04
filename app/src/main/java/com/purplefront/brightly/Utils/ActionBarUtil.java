package com.purplefront.brightly.Utils;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.purplefront.brightly.R;



/**
 * Created by Niranjan Reddy on 12-02-2018.
 */

public class ActionBarUtil {
    AppCompatActivity CallActivity;
    ImageView imgBack, imgSettings, imgSearch,imgNotify,imgSharePet;
    TextView txtTitle;
    EditText edtSearch;
    ActionBar actionBar;

    public ActionBarUtil(AppCompatActivity activity,RelativeLayout relativeLayout) {
        CallActivity = activity;
        actionBar = CallActivity.getSupportActionBar();
        SetView(relativeLayout);
        setViewInvisible();
    }
    public void setViewInvisible(){
        imgSettings.setVisibility(View.INVISIBLE);
        imgBack.setVisibility(View.INVISIBLE);
        imgSearch.setVisibility(View.INVISIBLE);
        imgNotify.setVisibility(View.INVISIBLE);
        edtSearch.setVisibility(View.INVISIBLE);
        txtTitle.setVisibility(View.INVISIBLE);
        imgSharePet.setVisibility(View.INVISIBLE);
        edtSearch.setText("");
        txtTitle.setClickable(false);

        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
    }


    public void setActionBarVisible() {
        actionBar.show();
    }

    public void SetActionBarHide() {
        actionBar.hide();
    }

    public void SetView(RelativeLayout relativeLayout) {
        actionBar.setCustomView(R.layout.actionbar_view);
//        actionBar.setDisplayShowCustomEnabled(true);
  //      View v = actionBar.getCustomView();
        txtTitle = (TextView) relativeLayout.findViewById(R.id.textBarTitle);
        imgBack = (ImageView) relativeLayout.findViewById(R.id.img_actionBarBack);
        imgSettings = (ImageView) relativeLayout.findViewById(R.id.img_settings_menu);
        edtSearch = (EditText) relativeLayout.findViewById(R.id.edtSearchText);
        imgSearch = (ImageView) relativeLayout.findViewById(R.id.imgSearch);
        imgNotify= (ImageView) relativeLayout.findViewById(R.id.img_Notify);
        imgSharePet=(ImageView)relativeLayout.findViewById(R.id.imgSharePet);


    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public EditText getEdtSearch(){
        return edtSearch;
    }
    public TextView getTitle(){
        return txtTitle;
    }
    public ImageView getImgBack() {
        return imgBack;
    }
    public ImageView getImgSharePet(){
        return imgSharePet;
    }

    public ImageView getImgSettings() {
        return imgSettings;
    }

    public ImageView getImgNotify(){
        return imgNotify;
    }

    public ImageView getImgSearch() {
        return imgSearch;
    }
}
