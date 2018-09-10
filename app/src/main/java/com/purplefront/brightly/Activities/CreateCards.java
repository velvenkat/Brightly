package com.purplefront.brightly.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Application.UserInterface;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Fragments.AudioType;
import com.purplefront.brightly.Fragments.FileType;
import com.purplefront.brightly.Fragments.ImageType;
import com.purplefront.brightly.Fragments.YoutubeType;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.UserModule;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.ImageChooser_Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

public class CreateCards extends BaseActivity implements UserInterface {

    String userId;
    String set_id;
    String set_name;

    UserModule userModule;
    private TabLayout tabs_creatCard;
    private ViewPager viewpager_creatCard;
    String Created_By;
    CardsListModel cardModelObj;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cards);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Create Card");

        userId = getIntent().getStringExtra("userId");
        set_id = getIntent().getStringExtra("set_id");
        set_name = getIntent().getStringExtra("set_name");
        Created_By = getIntent().getStringExtra("created_by");
        userModule = new UserModule();
        userModule.setSet_id(set_id);
        userModule.setSet_name(set_name);
        userModule.setUserId(userId);

        if (Created_By != null) {
            cardModelObj = getIntent().getParcelableExtra("Card_Dtls");
            userModule.setCard_id(cardModelObj.getCard_id());
            userModule.setCard_name(cardModelObj.getTitle());
            userModule.setCard_description(cardModelObj.getDescription());
            userModule.setCard_multimedia_url(cardModelObj.getUrl());
            userModule.setImage_name(cardModelObj.getName());
            userModule.setType(cardModelObj.getType());

        }


        viewpager_creatCard = (ViewPager) findViewById(R.id.viewpager_creatCard);

        setupViewPager(viewpager_creatCard);
        tabs_creatCard = (TabLayout) findViewById(R.id.tabs_creatCard);
        tabs_creatCard.setupWithViewPager(viewpager_creatCard);
        setupTabIcons();

    }

    private void setupTabIcons() {

        final TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Image");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(0).setCustomView(tabOne);

        // Supplier id for free Version "RcJ1L4mWaZeIe2wRO3ejHOmcSxf2" =====
        final TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Video");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(1).setCustomView(tabTwo);

        final TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Audio");
        // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(2).setCustomView(tabThree);

        final TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("File");
        // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(3).setCustomView(tabFour);
    }

    private void setupViewPager(ViewPager viewpager_creatCard) {

        CreateCards.ViewPagerAdapter adapter = new CreateCards.ViewPagerAdapter(getSupportFragmentManager());
        Fragment create_imgType = new ImageType();
        Bundle bundle = new Bundle();
        if (Created_By == null)
            bundle.putBoolean("isCreate", true);
        else {
            bundle.putBoolean("isCreate", false);
            bundle.putString("created_by", Created_By);
        }


        create_imgType.setArguments(bundle);
        adapter.addFrag(create_imgType, "image");


        Fragment create_UTubeType = new YoutubeType();
        create_UTubeType.setArguments(bundle);
        adapter.addFrag(create_UTubeType, "youtube");


//        adapter.addFrag(new YoutubeType(), "youtube");

        Fragment create_audioType = new AudioType();

        create_audioType.setArguments(bundle);
        adapter.addFrag(create_audioType, "audio");

        //   adapter.addFrag(new AudioType(), "audio");
//        adapter.addFrag(new FileType(), "file");
        Fragment create_fileType = new FileType();

        create_fileType.setArguments(bundle);
        adapter.addFrag(create_fileType, "file");


        viewpager_creatCard.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    @Override
    public void setUserMode(UserModule userMode) {
        userModule = userMode;

    }

    @Override
    public UserModule getUserMode() {
        return userModule;
    }
}
