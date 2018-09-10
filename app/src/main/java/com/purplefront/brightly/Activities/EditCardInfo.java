package com.purplefront.brightly.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Application.UserInterface;
import com.purplefront.brightly.Fragments.AudioType;
import com.purplefront.brightly.Fragments.FileType;
import com.purplefront.brightly.Fragments.ImageType;
import com.purplefront.brightly.Fragments.YoutubeType;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.UserModule;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EditCardInfo extends BaseActivity implements BaseActivity.alert_dlg_interface, UserInterface{



    UserModule userModule;
    private TabLayout tabs_editCard;
    private ViewPager viewpager_editCard;

    String userId = "";
    String card_id = "";
    String set_id = "";
    String set_name = "";
    String card_name = "";
    String card_description = "";
    String encoded_string = "";
    String image_name = "";
    String type = "";
    String created_by;
    CardsListModel cardsListModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle=getIntent().getBundleExtra("my_card_bundle");
        cardsListModels=bundle.getParcelable("Card_Dtls");

        userId = bundle.getString("userId");
        set_id = bundle.getString("set_id");
        set_name = bundle.getString("set_name");
        created_by=bundle.getString("created_by");
        userModule = new UserModule();
        userModule.setSet_id(set_id);
        userModule.setSet_name(set_name);
        userModule.setUserId(userId);
        userModule.setCard_id(cardsListModels.getCard_id());
        userModule.setCard_name(cardsListModels.getTitle());
        userModule.setCard_description(cardsListModels.getDescription());
        userModule.setCard_multimedia_url(cardsListModels.getUrl());
        userModule.setImage_name(cardsListModels.getName());
        userModule.setType(cardsListModels.getType());

        card_id = cardsListModels.getCard_id();
        type = cardsListModels.getType();
        if(created_by.equalsIgnoreCase(userId)){
            setTitle("Edit Card Info");
        }
        else
            setTitle("Card Info");
        setDlgListener(this);
        viewpager_editCard = (ViewPager)
                findViewById(R.id.viewpager_editCard);

        setupViewPager(viewpager_editCard);
        tabs_editCard = (TabLayout) findViewById(R.id.tabs_editCard);
        tabs_editCard.setupWithViewPager(viewpager_editCard);
        setupTabIcons();

    }


    private void setupTabIcons() {

        switch (type) {

            case "image":

                final TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                tabOne.setText("Image");
                // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
                tabs_editCard.getTabAt(0).setCustomView(tabOne);

                break;

            case "video":

                final TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                tabTwo.setText("Video");
                // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
                tabs_editCard.getTabAt(0).setCustomView(tabTwo);

                break;

            case "audio":

                final TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                tabThree.setText("Audio");
                // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
                tabs_editCard.getTabAt(0).setCustomView(tabThree);

                break;

            case "file":

                final TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                tabFour.setText("File");
                // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
                tabs_editCard.getTabAt(0).setCustomView(tabFour);

                break;

            case "text":

                    final TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                    tabFive.setText("Text");
                    // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
                    tabs_editCard.getTabAt(0).setCustomView(tabFive);
                    break;
        }

        /*final TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Image");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_editCard.getTabAt(0).setCustomView(tabOne);

        final TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Video");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_editCard.getTabAt(1).setCustomView(tabTwo);

        final TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Audio");
        // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_editCard.getTabAt(2).setCustomView(tabThree);

        final TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("File");
        // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_editCard.getTabAt(3).setCustomView(tabFour);*/
    }

    private void setupViewPager(ViewPager viewpager_creatCard) {

        EditCardInfo.ViewPagerAdapter adapter = new EditCardInfo.ViewPagerAdapter(getSupportFragmentManager());


        switch (type) {

            case "image":

                Fragment edit_imgType=new ImageType();
                Bundle bundle2 =new Bundle();
                bundle2.putBoolean("isCreate",false);
                bundle2.putString("created_by",created_by);
                edit_imgType.setArguments(bundle2);
                adapter.addFrag(edit_imgType, "image");


                //adapter.addFrag(new EditImageType(), "image");

                break;

            case "video":

               // adapter.addFrag(new YoutubeType(), "youtube");

                Fragment edit_UTubeType=new YoutubeType();
                Bundle bundle =new Bundle();
                bundle.putBoolean("isCreate",false);
                bundle.putString("created_by",created_by);
                edit_UTubeType.setArguments(bundle);
                adapter.addFrag(edit_UTubeType, "youtube");



                break;

            case "audio":

                Fragment edit_audioType=new AudioType();
                Bundle bundle1 =new Bundle();
                bundle1.putBoolean("isCreate",false);
                bundle1.putString("created_by",created_by);
                edit_audioType.setArguments(bundle1);
                adapter.addFrag(edit_audioType, "audio");

                break;

            case "file":

                Fragment edit_fileType=new FileType();
                Bundle bundle4 =new Bundle();
                bundle4.putBoolean("isCreate",false);
                bundle4.putString("created_by",created_by);
                edit_fileType.setArguments(bundle4);
                adapter.addFrag(edit_fileType, "file");


                break;

            case "text":

/*                adapter.addFrag(new EditImageType(), "image");*/
                Fragment edit_imgType1=new ImageType();
                Bundle bundle3 =new Bundle();
                bundle3.putBoolean("isCreate",false);
                bundle3.putString("created_by",created_by);
                edit_imgType1.setArguments(bundle3);
                adapter.addFrag(edit_imgType1, "image");

                break;
        }

       /* adapter.addFrag(new EditImageType(), "image");
        adapter.addFrag(new EditYoutubeType(), "youtube");
        adapter.addFrag(new EditAudioType(), "audio");
        adapter.addFrag(new EditFileType(), "file");*/
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;

            case R.id.delete:

               showAlertDialog("Your about to delete the Card, the information contained in the Card will be lost", "Confirm Delete....", "Delete", "Cancel");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDeleteCard() {
        try {

            if (CheckNetworkConnection.isOnline(EditCardInfo.this)) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(EditCardInfo.this).getDeleteCard(card_id);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(EditCardInfo.this) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse deleteSetResponse = response.body();
                        if (isSuccess) {

                            if (deleteSetResponse != null) {

                                setDeleteCredentials(deleteSetResponse);
                                dismissProgress();

                            } else {
                                dismissProgress();

                            }

                        } else {

                            dismissProgress();
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
                    }
                });
            } else {

                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();

            dismissProgress();
        }

    }

    private void setDeleteCredentials(AddMessageResponse deleteSetResponse) {

        String message = deleteSetResponse.getMessage();

        if(message.equals("success"))
        {
            Intent intent = new Intent(EditCardInfo.this, MySetCards.class);
            intent.putExtra("set_id", set_id);
            intent.putExtra("set_name", set_name);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);


        }
        else {
            showLongToast(EditCardInfo.this, message);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }



    @Override
    public void postive_btn_clicked() {
        getDeleteCard();
    }

    @Override
    public void negative_btn_clicked() {

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

