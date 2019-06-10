package com.digital_easy.info_share.Fragments;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Custom.CustomTabLayout;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.CardsListModel;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.Modules.SetEntryModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CreateCardsFragment extends BaseFragment implements BaseFragment.alert_dlg_interface {

    String set_id;
    String set_name;

    SetEntryModel setEntryModel;
    private CustomTabLayout tabs_creatCard;
    private ViewPager viewpager_creatCard;
    String Created_By;
    boolean isCreate_Crd = false;
    CardsListModel cardModelObj;
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;
    RealmModel user_obj;


    View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_create_cards, container, false);
        setHasOptionsMenu(true);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        setDlgListener(this);
        Bundle bundle = getArguments();
        isCreate_Crd = bundle.getBoolean("isCreate_Crd", false);

        //  chl_list_obj = bundle.getParcelable("model_obj");
        chl_list_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;
        Created_By = chl_list_obj.getCreated_by();

        setsListModel = bundle.getParcelable("setsListModel");
        set_id = setsListModel.getSet_id();
        set_name = setsListModel.getSet_name();


        setEntryModel = new SetEntryModel();
        setEntryModel.setSet_id(set_id);
        setEntryModel.setSet_name(set_name);
        //setEntryModel.setUserId(userId);
        if (!isCreate_Crd) {
            //setTitle("Edit Card Info");
            //  actionBarUtilObj.setTitle("Edit Card Info");

            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Edit " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " Info");
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(null);
            if (Created_By != null) {
                cardModelObj = bundle.getParcelable("Card_Dtls");
                if (cardModelObj != null) {
                    setEntryModel.setCard_id(cardModelObj.getCard_id());
                    setEntryModel.setCard_name(cardModelObj.getTitle());
                    setEntryModel.setCard_description(cardModelObj.getDescription());
                    setEntryModel.setCard_multimedia_url(cardModelObj.getUrl());
                    setEntryModel.setImage_name(cardModelObj.getName());
                    setEntryModel.setMultipleImageModelList(cardModelObj.getMultiple_img_url());
                    setEntryModel.setContact_name(cardModelObj.getContact_name());
                    setEntryModel.setContact_cell_phone(cardModelObj.getContact_cell_phone());
                    setEntryModel.setContact_company(cardModelObj.getContact_company());
                    setEntryModel.setContact_email(cardModelObj.getContact_email());
                    setEntryModel.setContact_office_phone(cardModelObj.getContact_office_phone());
                    setEntryModel.setType(cardModelObj.getType());
                }
            }
        } else {

            //  actionBarUtilObj.setTitle("Create Card");
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Create " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        }

        viewpager_creatCard = (ViewPager) rootView.findViewById(R.id.viewpager_creatCard);

        setupViewPager(viewpager_creatCard);

        tabs_creatCard = (CustomTabLayout) rootView.findViewById(R.id.tabs_creatCard);


        tabs_creatCard.setDIVIDER_FACTOR(5);

        tabs_creatCard.initTabMinWidth();
        tabs_creatCard.invalidate();

        tabs_creatCard.setupWithViewPager(viewpager_creatCard);
        setupTabIcons();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //         getActivity().finish();
                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    return true;
                }
                return false;
            }
        });
        return rootView;
    }

    private void setup_tab() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        String type[] = new String[]{"Image", "Youtube", "Audio", "File", "Video", "Contact"};

        for (int i = 0; i < type.length; i++) {
            tabs_creatCard.getTabAt(i).setText(type[i]);

            // tab_catg_type.setMinimumWidth(70);
        }
        /*try {
            Field txtTab = ((TabLayout)tab_catg_type.getClass()).getDeclaredField("mScrollableTabMinWidth");
            txtTab.setAccessible(true);
            txtTab.set(this, (int) (size.x / (float) type.size()));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
*/
    }


    private void setupTabIcons() {

        final TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Image");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(0).setCustomView(tabOne);

        // Supplier id for free Version "RcJ1L4mWaZeIe2wRO3ejHOmcSxf2" =====
        final TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Youtube");
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

        final TextView tabFive = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabFive.setText("Video");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(4).setCustomView(tabFive);

        final TextView tabSix = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabSix.setText("Contact");
        // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_google, 0, 0);
        tabs_creatCard.getTabAt(5).setCustomView(tabSix);

    }

    private void setupViewPager(ViewPager viewpager_creatCard) {

        CreateCardsFragment.ViewPagerAdapter adapter = new CreateCardsFragment.ViewPagerAdapter(this.getChildFragmentManager());

        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        if (isCreate_Crd) {
            bundle.putBoolean("isCreate", true);
            bundle2.putBoolean("isCreate", true);
        } else {
            bundle.putBoolean("isCreate", false);
            bundle.putString("created_by", Created_By);
            bundle2.putBoolean("isCreate", false);
            bundle2.putString("created_by", Created_By);
        }
        bundle.putParcelable("set_entry_obj", setEntryModel);
        bundle2.putParcelable("set_entry_obj", setEntryModel);

        Fragment create_imgType = new ImageType();
        create_imgType.setArguments(bundle);
        adapter.addFrag(create_imgType, "image");


        Fragment create_UTubeType = new YoutubeType();
        create_UTubeType.setArguments(bundle);
        adapter.addFrag(create_UTubeType, "Youtube");

//        adapter.addFrag(new YoutubeType(), "youtube");

        Fragment create_audioType = new AudioType();
        create_audioType.setArguments(bundle);
        adapter.addFrag(create_audioType, "audio");

        //   adapter.addFrag(new AudioType(), "audio");
//        adapter.addFrag(new FileType(), "file");
        Fragment create_fileType = new FileType();
        create_fileType.setArguments(bundle);
        adapter.addFrag(create_fileType, "file");


        Fragment create_VideoType = new VideoType();
        create_VideoType.setArguments(bundle);
        adapter.addFrag(create_VideoType, "Video");


        Fragment create_ContactType = new ImageType();
        bundle2.putBoolean("isContactCard", true);
        create_ContactType.setArguments(bundle2);
        adapter.addFrag(create_ContactType, "Contact");


        viewpager_creatCard.setAdapter(adapter);
        if (!isCreate_Crd) {
            int pos = -1;
            switch (cardModelObj.getType()) {
                case "multiple_images":
                    pos = 0;
                    break;

                case "video":
                    pos = 1;
                    break;

                case "audio":
                    pos = 2;
                    break;

                case "video_file":
                    pos = 4;
                    break;

                case "contact":
                    pos = 5;
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

                showAlertDialog("Your about to delete the " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + ", the information contained in the " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " will be lost", "Confirm Delete....", "Delete", "Cancel");

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


   /* @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear(); // Remove all existing items from the menu, leaving it empty as if it had just been created.
        if (!isCreate_Crd) {
            inflater.inflate(R.menu.delete, menu);


        }
    }
*/

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear(); // Remove all existing items from the menu, leaving it empty as if it had just been created.
        MenuInflater inflater = getActivity().getMenuInflater();
        if (!isCreate_Crd) {
            inflater.inflate(R.menu.delete, menu);


        }
    }

    public void getDeleteCard() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getDeleteCard(set_id, user_obj.getUser_Id(), cardModelObj.getCreated_by(), cardModelObj.getCard_id());
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

        if (message.equals("success")) {
            /*Intent intent = new Intent(CreateCardsFragment.this, MySetCards.class);
            intent.putExtra("model_obj", chl_list_obj);
            intent.putExtra("setsListModel", setsListModel);
            intent.putExtra("userId", userId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            showShortToast(getActivity(), ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " " + cardModelObj.getTitle() + " is deleted");
            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
            /*onBackPressed();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/

        } else {
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
