package com.digital_easy.info_share.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.CustomToast;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.ChannelListResponse;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;

public class CreateSet extends BaseFragment {

    EditText create_setName;
    EditText create_setDescription;
    Button btn_createSet;
    TextView chs_catg_lbl;

    String userId;
    String channel_id = "";
    String set_name = "";
    String set_description = "";
    String channel_name;

    ChannelListModel chl_list_obj;
    View rootView;
    boolean isSetTop = false;
    LinearLayout new_catg_contr;
    // EditText edt_catg_name;
    Spinner spinner_catg_list;
    RealmModel user_obj;
    List<ChannelListModel> channelListModels = new ArrayList<>();
    String level2_title_singular;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_create_set, container, false);

        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        setHasOptionsMenu(true);

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Create Set");
*/
        new_catg_contr = (LinearLayout) rootView.findViewById(R.id.new_catg_contr);
        create_setName = (EditText) rootView.findViewById(R.id.create_setName);

        create_setDescription = (EditText) rootView.findViewById(R.id.create_setDescription);
        spinner_catg_list = (Spinner) rootView.findViewById(R.id.spinner_catg_list);
        chs_catg_lbl = (TextView) rootView.findViewById(R.id.chs_catg_lbl);
        btn_createSet = (Button) rootView.findViewById(R.id.btn_createSet);
        Bundle bundle = getArguments();
        userId = user_obj.getUser_Id();
        level2_title_singular = (((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel2title().getSingular());
        isSetTop = bundle.getBoolean("isSetTop", false);
        spinner_catg_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (channelListModels != null) {
                    if (position != 0) {
                        chl_list_obj = channelListModels.get(position - 1);
                        channel_id = chl_list_obj.getChannel_id();
                    } else {
                        if (isSetTop)
                            channel_id = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (!isSetTop) {
            //chl_list_obj = bundle.getParcelable("model_obj");
            chl_list_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;
            channel_id = chl_list_obj.getChannel_id();
            channel_name = chl_list_obj.getChannel_name();
            new_catg_contr.setVisibility(View.GONE);
            chs_catg_lbl.setVisibility(View.GONE);
        } else {

            get_my_catg();
        }

        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Create " + level2_title_singular);
        create_setName.setHint(level2_title_singular + " Name");
        create_setDescription.setHint(level2_title_singular + " Description");
        btn_createSet.setText("Create " + level2_title_singular);
        btn_createSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        clear_edit_text_focus(create_setName);
        clear_edit_text_focus(create_setDescription);

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

    private void checkValidation() {

        // Get all edittext texts
        set_name = create_setName.getText().toString();
        set_description = create_setDescription.getText().toString();

        // Check if all strings are null or not
        if (set_name.trim().equals("") || set_name.trim().length() == 0)
        //|| set_description.equals("") || set_description.length() == 0) {
        {
            new CustomToast().Show_Toast(getContext(), create_setName,
                    level2_title_singular + " name is required.");
        }

        // Else do signup or do your stuff
        else {
            getAddSet();
        }
    }

    private void get_my_catg() {

        try {

            if (CheckNetworkConnection.isOnline(getContext())) {

                Call<ChannelListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getMyChannelsList(user_obj.getUser_Id(), "my");
                callRegisterUser.enqueue(new ApiCallback<ChannelListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<ChannelListResponse> response, boolean isSuccess, String message) {
                        ChannelListResponse channelListResponse = response.body();

                        if (isSuccess) {

                            if (channelListResponse != null && channelListResponse.getChannels() != null && channelListResponse.getChannels().size() != 0) {
                                //  swipeRefresh.setRefreshing(true);
                                channelListModels.clear();
                                channelListModels = channelListResponse.getChannels();
                                ArrayList<String> catg_name = new ArrayList<>();
                                catg_name.add("Select");
                                for (ChannelListModel model : channelListModels) {
                                    catg_name.add(model.getChannel_name());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, catg_name);
                                spinner_catg_list.setAdapter(adapter);

                            } else {
                                // swipeRefresh.setRefreshing(false);

                            }

                        } else {
                            showLongToast(getActivity(), message);

                            //     swipeRefresh.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        // swipeRefresh.setRefreshing(true);
                    }
                });
            } else {
              /*  else
                    dismissProgress();*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            //swipeRefresh.setRefreshing(true);
        }


    }

    private void getAddSet() {

        try {
            String chl_name = "";
            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                if (isSetTop && channel_id.equals("")) {
                    //   channel_id = "noname";
                    chl_name = "noname";
                } else
                    chl_name = "";
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getAddSet(userId, channel_id, set_name, set_description, chl_name);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                dismissProgress();

                                setAddSetCredentials(addMessageResponse);

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


    private void setAddSetCredentials(AddMessageResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {
           /* Intent intent = new Intent(CreateSet.this, com.purplefront.brightly.Fragments.SetsFragment.class);
            intent.putExtra("model_obj", chl_list_obj);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
         /*   Fragment fragment=new SetsFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable("model_obj", chl_list_obj);
            ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.Set_List,fragment,false);*/
            showShortToast(getActivity(), level2_title_singular + " " + set_name + " has been created");
            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
        } else {
            showLongToast(getActivity(), message);
        }
    }

   /* @Override
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
    }*/


}
