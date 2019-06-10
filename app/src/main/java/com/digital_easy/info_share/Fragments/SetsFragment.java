package com.digital_easy.info_share.Fragments;

import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.SetAdapterNew;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.SetListResponse;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.Util;

import java.util.ArrayList;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

public class SetsFragment extends BaseFragment implements SetAdapterNew.Set_sel_interface, BaseFragment.alert_dlg_interface, TextWatcher {

    ArrayList<SetsListModel> setsListModelList = new ArrayList<>();
    ArrayList<SetsListModel> selected_set_list;
    SetAdapterNew channelsSetAdapter;
    ArrayList<SetsListModel> search_set_list = new ArrayList<>();
    TextView view_nodata;
    ImageView image_createChannelSet;
    RecyclerView channelSet_listview;
    Realm realm;
    RealmResults<RealmModel> realmModel;
    EditText edt_srch_str;

    String userId;
    String channel_name = "";
    String channel_description = "";
    String encoded_string = "";
    String image_name = "";
    String Created_By = "";
    ArrayList<String> del_sel_id = new ArrayList<>();
    String filter_key;
    String channel_id = "";
    RelativeLayout del_contr;
    ItemTouchHelper ith;

    Button btn_cancel, btn_delete;
    String strDelSelId = "";
    TextView txtItemSel;
    CheckBox chk_sel_all;
    ChannelListModel chl_list_obj;

    //ImageView img_mutli_sel;
    View rootView;
    boolean is_on_set_chg_chk_status = false; //SELECT ALL CHECK BOX CHANGE BASED ON SET SELECTION
    // ActionBarUtil actionBarUtilObj;
    String set_id_toCreateCard = null;
    // TextView txtHintReorder;

    RealmModel user_obj;
    String level1_title_singular;


    // SwipeRefreshLayout swipeRefresh;
    // boolean isSwipeRefresh = false;

    // boolean isMultiSelChoosed;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_my_channels_set, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        edt_srch_str = (EditText) rootView.findViewById(R.id.edt_srch_set);
        //   txtHintReorder = rootView.findViewById(R.id.txtHintReorder);
        edt_srch_str.setHint("Search " + ((BrightlyNavigationActivity) getActivity()).SET_PLURAL);
        boolean dontRun = ((BrightlyNavigationActivity) getActivity()).DontRun;
        boolean dontRunoneTime = ((BrightlyNavigationActivity) getActivity()).DontRunOneTime;
        level1_title_singular = (((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel1title().getSingular());
     /*   swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipeRefresh.setRefreshing(false);
                isSwipeRefresh = true;
                getSetLists();
            }
        });*/
        // realm = Realm.getDefaultInstance();

        edt_srch_str.addTextChangedListener(this);

        if (!dontRun && !dontRunoneTime) {

            userId = user_obj.getUser_Id();
            del_contr = (RelativeLayout) rootView.findViewById(R.id.set_del_contr);
            btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
            btn_delete = (Button) rootView.findViewById(R.id.btn_delete);
            chk_sel_all = (CheckBox) rootView.findViewById(R.id.chk_sel_all);
            view_nodata = (TextView) rootView.findViewById(R.id.view_nodata);
            txtItemSel = (TextView) rootView.findViewById(R.id.txtCntSelected);
            channelSet_listview = (RecyclerView) rootView.findViewById(R.id.channelSet_listview);
            image_createChannelSet = (ImageView) rootView.findViewById(R.id.image_createChannelSet);
            setHasOptionsMenu(true);
            view_nodata.setText("No " + ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel2title().getPlural() + " available");
            Bundle bundle = getArguments();
            if (getArguments() != null) {
                set_id_toCreateCard = bundle.getString("Set_ID_toCreateCard");

                filter_key = bundle.getString("filter_key", null);
            }


            if (filter_key == null) {
                if (set_id_toCreateCard != null) {
                    chl_list_obj = bundle.getParcelable("chsed_catg");
                } else
                    chl_list_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;

                channel_id = chl_list_obj.getChannel_id();
                channel_name = chl_list_obj.getChannel_name();
                channel_description = chl_list_obj.getDescription();
                encoded_string = chl_list_obj.getCover_image();
                image_name = chl_list_obj.getImage_name();
                Created_By = chl_list_obj.getCreated_by();
                ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(channel_name);
                if (!userId.equalsIgnoreCase(chl_list_obj.getCreated_by())) {
                    //img_mutli_sel.setVisibility(View.GONE);

                }


            } else {
                image_createChannelSet.setVisibility(View.GONE);
            }

            getSetLists(filter_key);

      /*  realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for (RealmModel model : realmModel) {
            userId = model.getUser_Id();
        }
*/
       /* channel_id = getIntent().getStringExtra("channel_id");
        channel_name = getIntent().getStringExtra("channel_name");
        channel_description = getIntent().getStringExtra("channel_description");
        encoded_string = getIntent().getStringExtra("encoded_string");
        image_name = getIntent().getStringExtra("image_name");*/


            //setTitle(channel_name);
       /* actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);*/


            if (set_id_toCreateCard != null) {
                image_createChannelSet.setVisibility(View.INVISIBLE);
                //  txtHintReorder.setVisibility(View.GONE);
                ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = false;
                ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle("Select " + ((BrightlyNavigationActivity) getActivity()).SET_SINGULAR + " to import " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);
            } else
                ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(null);

            setDlgListener(this);

            chk_sel_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (is_on_set_chg_chk_status) {

                        is_on_set_chg_chk_status = false;
                    } else {
                        del_sel_id = new ArrayList<>();
                        if (isChecked) {
                            chk_sel_all.setText("Unselect all");
                            btn_delete.setEnabled(true);

                            for (int i = 0; i < setsListModelList.size(); i++) {
                                SetsListModel modelObj = setsListModelList.get(i);
                                modelObj.setDelSel(true);
                                del_sel_id.add(modelObj.getSet_id());
                                setsListModelList.remove(i);
                                setsListModelList.add(i, modelObj);

                            }
                            txtItemSel.setText(del_sel_id.size() + " items selected");
                        } else {
                            chk_sel_all.setText("Select all");
                            btn_delete.setEnabled(false);
                            for (int i = 0; i < setsListModelList.size(); i++) {
                                SetsListModel modelObj = setsListModelList.get(i);
                                modelObj.setDelSel(false);
                                setsListModelList.remove(i);
                                setsListModelList.add(i, modelObj);
                            }
                            txtItemSel.setText("");
                        }
                    }
                    channelsSetAdapter.notifyDataSetChanged();
                }
            });
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //txtHintReorder.setVisibility(View.GONE);
                    strDelSelId = android.text.TextUtils.join(",", del_sel_id);
                    showAlertDialog("You are about to delete the Set. All the information contained in the Sets will be lost.", "Confirm Delete...", "Delete", "Cancel");
                    // Toast.makeText(MyChannelsSet.this,"Set Id:"+csv,Toast.LENGTH_LONG).show();
                    //ith.attachToRecyclerView(channelSet_listview);
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            /*    getSupportActionBar().show();
                del_contr.setVisibility(View.GONE);
                del_sel_id=new ArrayList<>();
                delete_count=0;
                for(int i=0;i<setsListModelList.size();i++){
                    SetsListModel modelObj=setsListModelList.get(i);
                    if(modelObj.isDelSel()){
                        modelObj.setDelSel(false);
                        setsListModelList.remove(i);
                        setsListModelList.add(i,modelObj);
                    }
                }
                channelsSetAdapter.set_SelToDel(false);
                channelsSetAdapter.notifyDataSetChanged();
            */

                    reset_view();
                    for (int i = 0; i < setsListModelList.size(); i++) {
                        SetsListModel modelObj = setsListModelList.get(i);
                        if (modelObj.isDelSel()) {
                            modelObj.setDelSel(false);
                            setsListModelList.remove(i);
                            setsListModelList.add(i, modelObj);
                        }
                    }
                    channelsSetAdapter.set_SelToDel(false);
                    channelsSetAdapter.notifyDataSetChanged();
                    /**
                     * while reorder needed then uncomment this
                     */
                    // if (userId.equalsIgnoreCase(chl_list_obj.getCreated_by()))
                    //  ith.attachToRecyclerView(channelSet_listview);
                }
            });
// Extend the Callback class
        /*ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Collections.swap(setsListModelList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                channelsSetAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                //Toast.makeText(MyChannelsSet.this,"OnMoved",Toast.LENGTH_LONG).show();
                Handler handler =new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call_set_reorder();
                    }
                },500);

                return true;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
                //  Toast.makeText(MyChannelsSet.this,"OnSwiped",Toast.LENGTH_LONG).show();
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //     Toast.makeText(MyChannelsSet.this,"Make Flag",Toast.LENGTH_LONG).show();
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };

        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(channelSet_listview);

            if (userId.equalsIgnoreCase(chl_list_obj.getCreated_by())) {
                ItemTouchHelper.Callback dragCallback = new ItemTouchHelper.Callback() {

                    int dragFrom = -1;
                    int dragTo = -1;

                    @Override
                    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                                0);
                    }

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {


                        int fromPosition = viewHolder.getAdapterPosition();
                        int toPosition = target.getAdapterPosition();


                        if (dragFrom == -1) {
                            dragFrom = fromPosition;
                        }
                        dragTo = toPosition;

                        //channelsSetAdapter.onItemMove(fromPosition, toPosition);
                        Collections.swap(setsListModelList, fromPosition, toPosition);
                        // and notify the adapter that its dataset has changed
                        channelsSetAdapter.notifyItemMoved(fromPosition, toPosition);
                        channelsSetAdapter.notifyItemChanged(fromPosition);
                        channelsSetAdapter.notifyItemChanged(toPosition);
                        return true;

                    }

                    private void reallyMoved(int from, int to) {
                        // I guessed this was what you want...
                        call_set_reorder();
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    }

                    @Override
                    public boolean isLongPressDragEnabled() {
                        return true;
                    }

                    @Override
                    public boolean isItemViewSwipeEnabled() {
                        return false;
                    }

                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);

                        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                            reallyMoved(dragFrom, dragTo);
                        }

                        dragFrom = dragTo = -1;
                    }

                };
                // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
                /**
                 * while reorder uncomment this
                 */
                /*ith = new ItemTouchHelper(dragCallback);
                ith.attachToRecyclerView(channelSet_listview);*/
//            }

            image_createChannelSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

              /*  Intent intent = new Intent(SetsFragment.this, CreateSet.class);
                intent.putExtra("userId", userId);
                intent.putExtra("model_obj", chl_list_obj);
                startActivity(intent);
                finish();
                onBackPressed();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                    Bundle bundle1 = new Bundle();
                    if (channel_id.equals("")) {
                        bundle1.putBoolean("isSetTop", true);
                    } else {
                        bundle1.putBoolean("isSetTop", false);
                        // bundle1.putParcelable("model_obj", chl_list_obj);
                    }
                    Fragment create_set_frag = new CreateSet();


                    create_set_frag.setArguments(bundle1);
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Set, create_set_frag, true);
                }
            });


        }


        return rootView;
    }


    public void getSetLists(String filter_keys) {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {

                showProgress();
                if (filter_keys != null) {
                    channel_id = "";
                } else {
                    filter_keys = "";
                }
                Call<SetListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getMySetsList(userId, channel_id, filter_keys);
                callRegisterUser.enqueue(new ApiCallback<SetListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<SetListResponse> response, boolean isSuccess, String message) {
                        SetListResponse setListResponse = response.body();

                        dismissProgress();
                        if (isSuccess) {

                            if (setListResponse != null && setListResponse.getSets() != null && setListResponse.getSets().size() != 0) {

                                setsListModelList = setListResponse.getSets();
                                if (set_id_toCreateCard != null) {
                                    int i = 0;
                                    for (SetsListModel model : setsListModelList) {
                                        if (model.getSet_id().equals(set_id_toCreateCard)) {
                                            setsListModelList.remove(i);
                                            break;
                                        }
                                        i++;
                                    }
                                }
                                if (setsListModelList.size() == 0) {
                                    channelSet_listview.setVisibility(View.GONE);
                                    view_nodata.setVisibility(View.VISIBLE);
                                    edt_srch_str.setVisibility(View.GONE);
                                    setsListModelList = new ArrayList<>();
                                    //txtHintReorder.setVisibility(View.GONE);
                                } else
                                    setAdapter(setsListModelList);
                                // txtHintReorder.setVisibility(View.VISIBLE);


                            } else {
                                channelSet_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);
                                edt_srch_str.setVisibility(View.GONE);
                                setsListModelList = new ArrayList<>();
                                //  txtHintReorder.setVisibility(View.GONE);

                            }

                        } else {
                            showLongToast(getActivity(), message);

                        }
                        getActivity().invalidateOptionsMenu();
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

    public void call_set_reorder() {
        String setId = "";
        int i = 0;
        for (SetsListModel setsModelObj : setsListModelList) {
            if (i == 0) {
                i = 1;
            } else {
                setId = setId + ",";
            }
            setId = setId + setsModelObj.getSet_id();
        }
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).set_reorder_set(userId, setId);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        Toast.makeText(getContext(), "Sets Successfully Reordered", Toast.LENGTH_LONG).show();
                        getSetLists(null);
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
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

    public void reset_view() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        del_contr.setVisibility(View.GONE);
        del_sel_id = new ArrayList<>();
        //txtHintReorder.setVisibility(View.VISIBLE);

        chk_sel_all.setVisibility(View.GONE);

    }

    private void setAdapter(ArrayList<SetsListModel> setsListModels) {
        selected_set_list = setsListModels;
        channelSet_listview.setLayoutManager(new LinearLayoutManager(getContext()));
        channelsSetAdapter = new SetAdapterNew(getContext(), selected_set_list, this);
        channelsSetAdapter.set_default_img = ((BrightlyNavigationActivity) getActivity()).SET_DEF_IMAGE;
        //channelsSetAdapter.setsListModelsList = setsListModelList;
        channelSet_listview.setAdapter(channelsSetAdapter);
        //  channelsSetAdapter.notifyDataSetChanged();
    }

    public void call_api_del_multi_set() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getDeleteSet(strDelSelId);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse deleteSetResponse = response.body();
                        dismissProgress();
                        if (isSuccess) {

                            if (deleteSetResponse != null) {

                                if (deleteSetResponse.getMessage().equalsIgnoreCase("success")) {
                                    Toast.makeText(getContext(), "Selected Set(s) Deleted Successfully", Toast.LENGTH_LONG).show();
                                    reset_view();
                                    getSetLists(null);
                                    /**
                                     * while reorder uncomment this
                                     */
                                    // ith.attachToRecyclerView(channelSet_listview);
                                }


                            }
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuInflater inflater = getActivity().getMenuInflater();
        menu.clear();
        if (filter_key == null)
            if (set_id_toCreateCard == null) {
                if (userId.equalsIgnoreCase(Created_By)) {
                    if (setsListModelList != null && setsListModelList.size() == 0) {
                        //inflater.inflate(R.menu.my_channel_empty_set, menu);
                        menu.add(100, 1001, 200, level1_title_singular + " info/Edit");
                    } else
                        // inflater.inflate(R.menu.my_channel_set, menu);
                        //menu.add(R.string.info_edit);
                        menu.add(100, 1001, 200, level1_title_singular + " info/Edit");
                } else
                    // inflater.inflate(R.menu.my_channel_sub_set, menu);
                    menu.add(100, 1001, 200, level1_title_singular + " info");
                //   return true;
            }
    }




   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // if(userId.equalsIgnoreCase(chl_list_obj.getCreated_by())) {

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
               /* this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                return true;
           /* case R.id.action_multi_sel:

                if (setsListModelList.size() > 0) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                    del_contr.setVisibility(View.VISIBLE);
                    chk_sel_all.setVisibility(View.VISIBLE);
                    //  txtHintReorder.setVisibility(View.GONE);
                    txtItemSel.setText("");
                    chk_sel_all.setText("Select All");
                    del_sel_id = new ArrayList<>();
                    btn_delete.setEnabled(false);
                    *//**
             * while reorder uncomment this
             *//*
             *//* if (userId.equalsIgnoreCase(chl_list_obj.getCreated_by()))
                        ith.attachToRecyclerView(null);*//*
                    channelsSetAdapter.set_SelToDel(true);
                    channelsSetAdapter.notifyDataSetChanged();
                    // isMultiSelChoosed=true;
                    //   ith = new ItemTouchHelper(dragCallback);

                } else {

                }

                return true;
*/
            case 1001://R.id.channelInfo_Edit:
               /* Intent intent = new Intent(SetsFragment.this, EditChannelInfo.class);
                intent.putExtra("userId", userId);
              *//*  intent.putExtra("channel_name", channel_name);
                intent.putExtra("channel_description", channel_description);
                intent.putExtra("encoded_string", encoded_string);
                intent.putExtra("image_name", image_name);
                intent.putExtra("channel_id", channel_id);*//*
                intent.putExtra("model_obj", chl_list_obj);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                Fragment fragment = new EditChannelInfo();

                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Channel, fragment, true);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }*/

    /*@Override
    public void onSelect(int position, SetsListModel modelObj)
*/

    public void onInfoShareClicked(boolean isShare, Bundle bundle) {
        //Bundle bundle = new Bundle();

        //  bundle.putParcelable("setsListModel", modelObj);
        Fragment fragment = null;
        String Tag = "";
        if (isShare) {
            fragment = new SharePage();
            bundle.putBoolean("isScrnSetList", true);
            Tag = Util.share_page;
            //  ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(modelObj.getSet_name());
        } else {
            fragment = new EditSetInfo();
            Tag = Util.Edit_Set;
        }
        fragment.setArguments(bundle);

        ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Tag, fragment, true);

    }


    /*  @Override
      public void showPopUp(View c, Bundle bundle_args)
  */
    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    @Override
    public void postive_btn_clicked() {
//      Toast.makeText(MyChannelsSet.this,"Working",Toast.LENGTH_LONG).show();
        call_api_del_multi_set();
    }

    @Override
    public void negative_btn_clicked() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String srchStr = edt_srch_str.getText().toString().trim().toLowerCase();
        search_set_list = new ArrayList<>();
        if (setsListModelList != null && setsListModelList.size() > 0)
            for (SetsListModel model : setsListModelList) {
                if (model.getSet_name().toLowerCase().contains(srchStr)) {
                    search_set_list.add(model);
                }
            }
        setAdapter(search_set_list);
    }

    @Override
    public void onSelect(int position, View pop_up_sel_view, boolean isShare) {
        {
            SetsListModel set_modelObj = selected_set_list.get(position);
            Bundle bundle_args = new Bundle();
            bundle_args.putParcelable("setsListModel", set_modelObj);
            if (channel_id.equals("")) {
                chl_list_obj = set_modelObj.getChl_model_obj();
                ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj = chl_list_obj;
            }
            //bundle_args.putParcelable("model_obj", chl_list_obj);
            View v = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow, null, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
          /*  final PopupWindow pw = new PopupWindow(v, params.width, params.height, true);
            pw.setBackgroundDrawable(new BitmapDrawable(getContext().getResources()));
            pw.setOutsideTouchable(true);
            pw.setAnimationStyle(R.anim.popup_show);
            final ListView list_popup = v.findViewById(R.id.list_popup);
            ArrayList<String> popup_dataset = new ArrayList<>();
            popup_dataset.add("Info");
            popup_dataset.add("Share");

            ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, popup_dataset);
            list_popup.setAdapter(adapter);
            list_popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pw.dismiss();
          */
            if (!isShare)

                onInfoShareClicked(false, bundle_args);
            else
                onInfoShareClicked(true, bundle_args);


            /*    }
            });
            Rect locate = locateView(pop_up_sel_view);


            pw.showAtLocation(pop_up_sel_view, Gravity.TOP, locate.centerX() + 10, locate.centerY() - 20);
        }*/
        }
    }

    @Override
    public void onCardClick(int position) {
        SetsListModel modelObj = selected_set_list.get(position);
        if (channelsSetAdapter.isSelToDel()) {


            if (modelObj.isDelSel()) {
                modelObj.setDelSel(false);
                int i = 0;
                for (String sel_ID : del_sel_id) {
                    if (sel_ID.equals(modelObj.getSet_id())) {
                        del_sel_id.remove(i);
                        txtItemSel.setText(del_sel_id.size() + " items selected");
                        break;
                    }
                    i++;
                }
                if (del_sel_id.size() == 0) {
                    btn_delete.setEnabled(false);
                    txtItemSel.setText("");
                    chk_sel_all.setText("Select all");
                    is_on_set_chg_chk_status = true;
                    chk_sel_all.setChecked(false);
                }
            } else {
                modelObj.setDelSel(true);

                chk_sel_all.setVisibility(View.VISIBLE);
                del_sel_id.add(modelObj.getSet_id());
                if (setsListModelList.size() == del_sel_id.size()) {
                    chk_sel_all.setText("Unselect all");
                    is_on_set_chg_chk_status = true;
                    chk_sel_all.setChecked(true);
                }
                txtItemSel.setText(del_sel_id.size() + " items selected");
                btn_delete.setEnabled(true);
            }
            setsListModelList.remove(position);

        /*if (del_sel_id.equals("")) {
            del_sel_id = modelObj.getSet_id();
        } else
            del_sel_id = del_sel_id + "," + modelObj.getSet_id();*/
            setsListModelList.add(position, modelObj);
            channelsSetAdapter.notifyDataSetChanged();
            // Toast.makeText(MyChannelsSet.this,"Selected set id:"+Sel_id,Toast.LENGTH_LONG).show();
        } else {
            Fragment fragment;
            Bundle bundle_args = new Bundle();
            if (channel_id.equals("")) {
                chl_list_obj = modelObj.getChl_model_obj();
                ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj = chl_list_obj;
            }
            //bundle_args.putParcelable("model_obj", chl_list_obj);
            bundle_args.putParcelable("setsListModel", modelObj);
            bundle_args.putBoolean("isNotification", false);
            bundle_args.putString("chl_name", chl_list_obj.getChannel_name());
            if (set_id_toCreateCard != null) {
                int card_count = Integer.parseInt(modelObj.getTotal_card_count());
                if (card_count > 0) {
                    fragment = new CardList();
                    bundle_args.putString("set_id_toCreateCard", set_id_toCreateCard);
                    bundle_args.putParcelable("chsed_catg", chl_list_obj);
                    fragment.setArguments(bundle_args);
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, fragment, true);
                } else {
                    showLongToast(getActivity(), "Card is not available");
                }
            } else {
                fragment = new CardDetailFragment();
                fragment.setArguments(bundle_args);
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.view_card, fragment, false);
            }
        }
    }

   /* @Override
    public void onUnSelect(int position, SetsListModel modelObj) {
        modelObj.setDelSel(false);
        setsListModelList.remove(position);

        if (del_sel_id.equals("")) {
            del_sel_id = modelObj.getSet_id();
        } else
            del_sel_id = del_sel_id + "," + modelObj.getSet_id();
        setsListModelList.add(position, modelObj);
        channelsSetAdapter.notifyDataSetChanged();
    }*/
}
