package com.digital_easy.info_share.Fragments;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Modules.SetListResponse;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.Util;

import java.util.ArrayList;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;

public class SharePage extends BaseFragment {

    Button share_inApp;
    Button share_aLink;

    String set_description = "";
    String set_name = "";
    String userId;
    String set_id = "";
    String share_link = "";
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;
    NotificationsModel notificationsModel;
    boolean isNotification;
    RealmModel user_obj;
    boolean isScrnSetList;
    LinearLayout ll_PublicLinkContr;
    boolean isCardShare;
    // String card_shr_card_id;
    //  String card_shr_set_name, card_shr_set_desc;

    View rootView;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Share Set");
            setBackKeyHandler();
            if (isScrnSetList) {
                ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = true;
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_share_page, container, false);
        ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = false;
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        setHasOptionsMenu(true);
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
        share_inApp = (Button) rootView.findViewById(R.id.share_inApp);
        share_aLink = (Button) rootView.findViewById(R.id.share_aLink);
        userId = user_obj.getUser_Id();
        // setContentView(R.layout.activity_share_page);

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
        // setTitle("Share Set");
        TextView txtCapShrAccess = (TextView) rootView.findViewById(R.id.txtCapShrAccess);
        ll_PublicLinkContr = (LinearLayout) rootView.findViewById(R.id.ll_public_link_contr);

        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Share Set");
        Bundle bundle = getArguments();

        if (bundle != null) {
            isNotification = bundle.getBoolean("isNotification", false);
            isScrnSetList = bundle.getBoolean("isScrnSetList", false);
            isCardShare = bundle.getBoolean("isCardShare", false);
            if (isCardShare) {
              /*  card_shr_set_name = bundle.getString("card_shr_set_name");
                card_shr_set_desc = bundle.getString("card_shr_set_desc");*/
                //  card_shr_card_id = bundle.getString("card_shr_card_id");
            }
            if (isScrnSetList) {
                ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = true;
            }
        }


        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();

            share_link = notificationsModel.getNotificationsSetDetail().getShare_link();
            if (notificationsModel.getNotificationsSetDetail().getWeb_sharing().equalsIgnoreCase("0")) {
                //share_aLink.setEnabled(false);
                ll_PublicLinkContr.setVisibility(View.GONE);
          /*  ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.file_list_icon);
            txtCapShrAccess.setText("Set Share access permission as in private");*/
                //share_aLink.setBackgroundColor(Color.LTGRAY);
            } else {
                //txtCapShrAccess.setText("Set Share access permission as in public");
                share_aLink.setEnabled(true);
            }


        } else {

            //  chl_list_obj = bundle.getParcelable("model_obj");
            chl_list_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;
            setsListModel = bundle.getParcelable("setsListModel");
            set_description = setsListModel.getDescription();
            set_name = setsListModel.getSet_name();
            set_id = setsListModel.getSet_id();

            share_link = setsListModel.getShare_link();
            if (setsListModel.getWeb_sharing().equalsIgnoreCase("0")) {
                // share_aLink.setEnabled(false);
                ll_PublicLinkContr.setVisibility(View.GONE);
          /*  ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.file_list_icon);
            txtCapShrAccess.setText("Set Share access permission as in private");*/
                // share_aLink.setBackgroundColor(Color.LTGRAY);
            } else {
                //txtCapShrAccess.setText("Set Share access permission as in public");
                share_aLink.setEnabled(true);
            }


        }


        share_inApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent intent = new Intent(SharePage.this, ShareWithContacts.class);
                intent.putExtra("userId", userId);
                intent.putExtra("model_obj", chl_list_obj);
                intent.putExtra("setsListModel", setsListModel);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                setBottomDialog();
            }
        });
        share_aLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCardShare) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                    // Add data to the intent, the receiving app will decide
                    // what to do with it.
                    share.putExtra(Intent.EXTRA_SUBJECT, "Brightly Set Share link");
                    share.putExtra(Intent.EXTRA_TEXT, share_link);

                    startActivity(Intent.createChooser(share, "Share link!"));
                } else {
                    //  share_link = "";
                    if (share_link.trim().equals("")) {
                        //   call_api_card_share(card_shr_card_id, chl_list_obj.getChannel_id(), false);
                    } else {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                        // Add data to the intent, the receiving app will decide
                        // what to do with it.
                        share.putExtra(Intent.EXTRA_SUBJECT, "Brightly Set Share link");
                        share.putExtra(Intent.EXTRA_TEXT, share_link);

                        startActivity(Intent.createChooser(share, "Share link!"));
                    }
                }
            }
        });

        setBackKeyHandler();

        return rootView;
    }

    public void call_api_card_share(String CardIds, String chl_id, boolean isCreateSet) {
        try {
            Call<SetListResponse> callRegisterUser;
            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();


                callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).call_set_from_cards(userId, chl_id, CardIds, setsListModel.getSet_id());

                //else
                //callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).call_share_card(userId, chl_id, CardIds, setsListModel.getSet_id(), "9566099458", "venkat", set_name, description);
                callRegisterUser.enqueue(new ApiCallback<SetListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<SetListResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        SetListResponse message_resObj = response.body();
                        if (message_resObj.getMessage().trim().equals("success")) {
                            //Toast.makeText(getContext(), ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR + " " + set_name + " successfully created", Toast.LENGTH_LONG).show();
                            //  ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                            share_link = message_resObj.getSet_obj().getShare_link();
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                            // Add data to the intent, the receiving app will decide
                            // what to do with it.
                            share.putExtra(Intent.EXTRA_SUBJECT, "Brightly Set Share link");
                            share.putExtra(Intent.EXTRA_TEXT, message_resObj.getSet_obj().getShare_link());

                            startActivity(Intent.createChooser(share, "Share link!"));
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
            } /*else {

                dismissProgress();
            }*/ else {
                Toast.makeText(getContext(), "Check network connection", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();

            dismissProgress();
        }
    }

    public void setBackKeyHandler() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //         getActivity().finish();
                    if (isScrnSetList)
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    else
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false);
                    return true;
                }
                return false;
            }
        });
    }

    public void set_new_contact_dlg() {

        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_contact_add); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.CENTER);
        mBottomSheetDialog.show();
        Button btn_cancel = (Button) mBottomSheetDialog.getWindow().findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) mBottomSheetDialog.getWindow().findViewById(R.id.btn_submit);
        EditText edt_name = (EditText) mBottomSheetDialog.getWindow().findViewById(R.id.edt_name);
        EditText edt_no = (EditText) mBottomSheetDialog.getWindow().findViewById(R.id.edt_mob_no);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edt_name.getText().toString();
                String mob_no = edt_no.getText().toString();
                if (mob_no.trim().length() == 10 && !name.trim().equals("")) {
                    mBottomSheetDialog.dismiss();

                    getShareSet(name, mob_no);


                } else {
                    if (mob_no.trim().equals("")) {
                        Toast.makeText(getContext(), "Please enter the 10-digit mobile number", Toast.LENGTH_LONG).show();
                    } else if (mob_no.trim().length() < 10) {
                        Toast.makeText(getContext(), "Incomplete mobile no", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Please enter the contact name", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void getShareSet(String name, String mob_no) {

        try {
            Call<AddMessageResponse> callRegisterUser;
            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                if (!isCardShare)
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getShareSet(userId, set_id, mob_no, name);
                else
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getShareSet(userId, set_id, mob_no, name);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                dismissProgress();
                                //  setAddSetCredentials(addMessageResponse);
                                Toast.makeText(getContext(), addMessageResponse.getMessage(), Toast.LENGTH_LONG).show();
                                if (isCardShare) {
                                    ((BrightlyNavigationActivity) getActivity()).DontRun = true;
                                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true, 2);

                                } else if (isScrnSetList)
                                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                                else {
                                    ShareSettings parent_frag = (ShareSettings) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.Set_Share_settings);
                                    parent_frag.isShareSetChgd = true;
                                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false);
                                }
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
                        if (message.equals("timeout")) {
                            showLongToast(getActivity(), "Internet is too slow.");
                            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false, 2);
                        }
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

    public void setBottomDialog() {

        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_set_share_opts); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        menu_list.add("Select Contact");
        menu_list.add("New Contact");

        ArrayAdapter<String> menu_itmes = new ArrayAdapter<String>(getContext(), R.layout.menu_row_diualog, R.id.dialog_menu_textView,
                menu_list);
        list_SettingsMenu.setAdapter(menu_itmes);
        list_SettingsMenu.requestFocus();
        Button btnCancel = (Button) mBottomSheetDialog.getWindow().findViewById(R.id.btnCancel);
        TextView txt_sel_options = (TextView) mBottomSheetDialog.getWindow().findViewById(R.id.sel_options);
        txt_sel_options.setVisibility(View.VISIBLE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        list_SettingsMenu.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getContext(),"Position :"+position,Toast.LENGTH_SHORT).show();
                        mBottomSheetDialog.dismiss();
                        //audio_uri = null;
                        //tempMp3File = null;
                        //release_media();
                        //text_audioFile.setVisibility(View.VISIBLE);
                        // rl_audio_player.setVisibility(View.GONE);
                        switch (position) {
                            case 1:
                                set_new_contact_dlg();
                                break;
                            case 0:
                                Fragment fragment = new ShareWithContacts();
                                Bundle bundle1 = new Bundle();
                                if (isNotification) {
                                    bundle1.putBoolean("isNotification", true);
                                    bundle1.putParcelable("notfy_modl_obj", notificationsModel);
                                    if (isCardShare) {
                                        bundle1.putBoolean("isCardShare", isCardShare);
                                        //   bundle1.putString("card_shr_card_id", card_shr_card_id);
                                      /*  bundle1.putString("card_shr_set_name", card_shr_set_name);
                                        bundle1.putString("card_shr_set_desc", card_shr_set_desc);*/

                                    }
                                } else {
                                    bundle1.putBoolean("isNotification", false);
                                    //    bundle1.putParcelable("model_obj", chl_list_obj);
                                    bundle1.putBoolean("isScrnSetList", isScrnSetList);
                                    bundle1.putParcelable("setsListModel", setsListModel);
                                    if (isCardShare) {
                                        bundle1.putBoolean("isCardShare", isCardShare);
                                        //bundle1.putString("card_shr_card_id", card_shr_card_id);
                                        /*bundle1.putString("card_shr_set_name", card_shr_set_name);
                                        bundle1.putString("card_shr_set_desc", card_shr_set_desc);
*/
                                    }
                                }
                                fragment.setArguments(bundle1);
                                //if (!isCardShare)
                                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.share_with_contact, fragment, true);

                                break;

                        }
                    }
                }
        );

    }


    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }*/
}
