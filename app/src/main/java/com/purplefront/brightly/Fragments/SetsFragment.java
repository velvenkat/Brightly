package com.purplefront.brightly.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.SetsAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.SetListResponse;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

public class SetsFragment extends BaseFragment implements SetsAdapter.Set_sel_interface, BaseFragment.alert_dlg_interface {

    ArrayList<SetsListModel> setsListModelList = new ArrayList<>();
    SetsAdapter channelsSetAdapter;

    TextView view_nodata;
    ImageView image_createChannelSet;
    RecyclerView channelSet_listview;
    Realm realm;
    RealmResults<RealmModel> realmModel;

    String userId;
    String channel_name = "";
    String channel_description = "";
    String encoded_string = "";
    String image_name = "";
    String Created_By = "";
    ArrayList<String> del_sel_id = new ArrayList<>();
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

    RealmModel user_obj;

    // boolean isMultiSelChoosed;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_my_channels_set, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();

        boolean dontRun=((BrightlyNavigationActivity)getActivity()).DontRun;
        // realm = Realm.getDefaultInstance();
        if(!dontRun) {
            Bundle bundle = getArguments();
            del_contr = (RelativeLayout) rootView.findViewById(R.id.set_del_contr);
            btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
            btn_delete = (Button) rootView.findViewById(R.id.btn_delete);
            chk_sel_all = (CheckBox) rootView.findViewById(R.id.chk_sel_all);
            setHasOptionsMenu(true);
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

            userId = user_obj.getUser_Id();
            chl_list_obj = bundle.getParcelable("model_obj");
            set_id_toCreateCard = bundle.getString("Set_ID_toCreateCard");


            channel_id = chl_list_obj.getChannel_id();
            channel_name = chl_list_obj.getChannel_name();
            channel_description = chl_list_obj.getDescription();
            encoded_string = chl_list_obj.getCover_image();
            image_name = chl_list_obj.getImage_name();
            Created_By = chl_list_obj.getCreated_by();

            view_nodata = (TextView) rootView.findViewById(R.id.view_nodata);
            txtItemSel = (TextView) rootView.findViewById(R.id.txtCntSelected);
            channelSet_listview = (RecyclerView) rootView.findViewById(R.id.channelSet_listview);
            //setTitle(channel_name);
       /* actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);*/
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(channel_name);
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(null);
            image_createChannelSet = (ImageView) rootView.findViewById(R.id.image_createChannelSet);
            if (set_id_toCreateCard != null) {
                image_createChannelSet.setVisibility(View.INVISIBLE);
                ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = false;
            }
            setDlgListener(this);
            if (!userId.equalsIgnoreCase(chl_list_obj.getCreated_by())) {
                //img_mutli_sel.setVisibility(View.GONE);
                image_createChannelSet.setVisibility(View.GONE);
            }

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
                    if (userId.equalsIgnoreCase(chl_list_obj.getCreated_by()))
                        ith.attachToRecyclerView(channelSet_listview);
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
*/
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
                ith = new ItemTouchHelper(dragCallback);
                ith.attachToRecyclerView(channelSet_listview);
            }

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
                    Fragment create_set_frag = new CreateSet();
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("model_obj", chl_list_obj);
                    create_set_frag.setArguments(bundle1);
                    ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Create_Set, create_set_frag, true);
                }
            });

            getSetLists();
        }
        return rootView;
    }


    public void getSetLists() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<SetListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getMySetsList(userId, channel_id);
                callRegisterUser.enqueue(new ApiCallback<SetListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<SetListResponse> response, boolean isSuccess, String message) {
                        SetListResponse setListResponse = response.body();
                        if (isSuccess) {

                            if (setListResponse != null && setListResponse.getSets() != null && setListResponse.getSets().size() != 0) {

                                setsListModelList = setListResponse.getSets();
                                setAdapter(setsListModelList);
                                dismissProgress();

                            } else {
                                channelSet_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);
                                dismissProgress();
                            }

                        } else {
                            showLongToast(getActivity(), message);
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
                        Toast.makeText(getContext(), "Message:" + response.message(), Toast.LENGTH_LONG).show();
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

        chk_sel_all.setVisibility(View.GONE);

    }

    private void setAdapter(ArrayList<SetsListModel> setsListModels) {

        channelSet_listview.setLayoutManager(new GridLayoutManager(getContext(), 3));
        channelsSetAdapter = new SetsAdapter(getActivity(), setsListModels, chl_list_obj, this);
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
                                    reset_view();
                                    getSetLists();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // if(userId.equalsIgnoreCase(chl_list_obj.getCreated_by())) {
        if(set_id_toCreateCard==null) {
            if (userId.equalsIgnoreCase(Created_By)) {
                inflater.inflate(R.menu.my_channel_set, menu);
            } else
                inflater.inflate(R.menu.my_channel_sub_set, menu);
            //   return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
               /* this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                return true;
            case R.id.action_multi_sel:

                if (setsListModelList.size() > 0) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                    del_contr.setVisibility(View.VISIBLE);
                    txtItemSel.setText("");
                    btn_delete.setEnabled(false);
                    if (userId.equalsIgnoreCase(chl_list_obj.getCreated_by()))
                        ith.attachToRecyclerView(null);
                    channelsSetAdapter.set_SelToDel(true);
                    channelsSetAdapter.notifyDataSetChanged();
                    // isMultiSelChoosed=true;
                    //   ith = new ItemTouchHelper(dragCallback);

                } else {

                }

                return true;
            case R.id.channelInfo_Edit:
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
                Bundle bundle = new Bundle();
                bundle.putParcelable("model_obj", chl_list_obj);
                fragment.setArguments(bundle);
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

    @Override
    public void onSelect(int position, SetsListModel modelObj) {

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
    }

    @Override
    public void onCardShow(Bundle bundle_args) {
        Fragment fragment;
        if (set_id_toCreateCard != null) {
            fragment = new CardList();
            bundle_args.putString("set_id_toCreateCard", set_id_toCreateCard);
            fragment.setArguments(bundle_args);
            ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Card_List, fragment, true);
        } else {
            fragment = new CardDetailFragment();
            fragment.setArguments(bundle_args);
            ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.view_card, fragment, false);
        }
    }

    @Override
    public void postive_btn_clicked() {
//      Toast.makeText(MyChannelsSet.this,"Working",Toast.LENGTH_LONG).show();
        call_api_del_multi_set();
    }

    @Override
    public void negative_btn_clicked() {

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
