package com.purplefront.brightly.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.Modules.SetEntryModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CreateCardsFragment extends BaseFragment implements  BaseFragment.alert_dlg_interface {

    String set_id;
    String set_name;

    SetEntryModel setEntryModel;
    private TabLayout tabs_creatCard;
    private ViewPager viewpager_creatCard;
    String Created_By;
    boolean isCreate_Crd=false;
    CardsListModel cardModelObj;
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;
    RealmModel user_obj;


    View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.activity_create_cards,container,false);
        user_obj=((BrightlyNavigationActivity)getActivity()).getUserModel();
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        setDlgListener(this);
        Bundle bundle =getArguments();
        isCreate_Crd=bundle.getBoolean("isCreate_Crd",false);

        chl_list_obj=bundle.getParcelable("model_obj");
        Created_By = chl_list_obj.getCreated_by();

        setsListModel=bundle.getParcelable("setsListModel");
        set_id = setsListModel.getSet_id();
        set_name = setsListModel.getSet_name();


        setEntryModel = new SetEntryModel();
        setEntryModel.setSet_id(set_id);
        setEntryModel.setSet_name(set_name);
        //setEntryModel.setUserId(userId);
        if(!isCreate_Crd) {
            //setTitle("Edit Card Info");
          //  actionBarUtilObj.setTitle("Edit Card Info");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Card Info");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(null);
            if (Created_By != null) {
                cardModelObj = bundle.getParcelable("Card_Dtls");
                if (cardModelObj != null) {
                    setEntryModel.setCard_id(cardModelObj.getCard_id());
                    setEntryModel.setCard_name(cardModelObj.getTitle());
                    setEntryModel.setCard_description(cardModelObj.getDescription());
                    setEntryModel.setCard_multimedia_url(cardModelObj.getUrl());
                    setEntryModel.setImage_name(cardModelObj.getName());
                    setEntryModel.setType(cardModelObj.getType());
                }
            }
        }
        else
        {

          //  actionBarUtilObj.setTitle("Create Card");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Card");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(null);
        }

        viewpager_creatCard = (ViewPager)rootView. findViewById(R.id.viewpager_creatCard);

        setupViewPager(viewpager_creatCard);
        tabs_creatCard = (TabLayout) rootView.findViewById(R.id.tabs_creatCard);
        tabs_creatCard.setupWithViewPager(viewpager_creatCard);
        setupTabIcons();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //         getActivity().finish();
                    ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
                    return true;
                }
                return false;
            }
        });
        return rootView;
    }


    private void setupTabIcons() {

        final TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Image");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(0).setCustomView(tabOne);

        // Supplier id for free Version "RcJ1L4mWaZeIe2wRO3ejHOmcSxf2" =====
        final TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Video");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(1).setCustomView(tabTwo);

        final TextView tabThree = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Audio");
        // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(2).setCustomView(tabThree);

        final TextView tabFour = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabFour.setText("File");
        // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(3).setCustomView(tabFour);
    }

    private void setupViewPager(ViewPager viewpager_creatCard) {

        CreateCardsFragment.ViewPagerAdapter adapter = new CreateCardsFragment.ViewPagerAdapter(this.getChildFragmentManager());

        Bundle bundle = new Bundle();
        if (isCreate_Crd)
            bundle.putBoolean("isCreate", true);
        else {
            bundle.putBoolean("isCreate", false);
            bundle.putString("created_by", Created_By);
        }
        bundle.putParcelable("set_entry_obj",setEntryModel);


        Fragment create_imgType = new ImageType();
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
        if(!isCreate_Crd) {
            int pos=-1;
            switch (cardModelObj.getType()) {
                case "image":
                    pos = 0;
                    break;

                case "video":
                    pos = 1;
                    break;

                case "audio":
                    pos = 2;
                    break;

                case "file":
                    pos = 3;
                    break;
            }
            viewpager_creatCard.setCurrentItem(pos);
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete:

                showAlertDialog("Your about to delete the Card, the information contained in the Card will be lost", "Confirm Delete....", "Delete", "Cancel");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear(); // Remove all existing items from the menu, leaving it empty as if it had just been created.
        if (!isCreate_Crd) {
            inflater.inflate(R.menu.delete, menu);


        }
    }


    public void getDeleteCard() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getDeleteCard(cardModelObj.getCard_id());
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
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
            /*Intent intent = new Intent(CreateCardsFragment.this, MySetCards.class);
            intent.putExtra("model_obj", chl_list_obj);
            intent.putExtra("setsListModel", setsListModel);
            intent.putExtra("userId", userId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
            /*onBackPressed();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/

        }
        else {
            showLongToast(getActivity(), message);
        }
    }





    @Override
    public void postive_btn_clicked() {
        getDeleteCard();
    }

    @Override
    public void negative_btn_clicked() {

    }



}
