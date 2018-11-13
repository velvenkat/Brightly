package com.purplefront.brightly.Fragments;

import android.opengl.Visibility;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.CardListAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CardsListResponse;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CardList extends BaseFragment implements BaseFragment.alert_dlg_interface, CardListAdapter.Card_sel_interface {

    List<CardsListModel> cardsListModels = new ArrayList<>();
    CardListAdapter cardListAdapter;
    RecyclerView card_listview;
    TextView view_nodata;
    String userId;
    String set_name = "";
    String chl_name;
    String set_id = "";
    ArrayList<String> del_sel_id = new ArrayList<>();
    ArrayList<String> list_Created_by = new ArrayList<>();
    RelativeLayout del_contr;
    Button btn_cancel, btn_delete;
    String strDelSelId = "";
    String strDelCrtdBy = "";
    TextView txtItemSel;
    CheckBox chk_sel_all;
    TextView txtHintReorder;
    ItemTouchHelper ith;
    //ImageView img_mutli_sel;
    boolean isReorder;
    boolean is_on_set_chg_chk_status = false; //SELECT ALL CHECK BOX CHANGE BASED ON SET SELECTION
    SetsListModel setsListModel;
    View rootView;
    NotificationsModel notificationsModel;
    int CurPagrPos;
    boolean isNotification;
    RealmModel user_obj;
    String set_id_toCreateCard;


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(isReorder) {
            MenuItem del_menu = menu.findItem(R.id.action_multi_sel);

            if (cardsListModels.size() == 0) {
                del_menu.setVisible(false);
                txtHintReorder.setVisibility(View.GONE);
            } else {
                del_menu.setVisible(true);
                txtHintReorder.setVisibility(View.VISIBLE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_card_list, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        txtHintReorder=(TextView)rootView.findViewById(R.id.txtHintReorder);
        // setContentView(R.layout.activity_card_list);
        /*Toolbar toolbar = (Toolbar)rootView. findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        userId = user_obj.getUser_Id();
        isNotification = bundle.getBoolean("isNotification", false);
        set_id_toCreateCard = bundle.getString("set_id_toCreateCard", null); //Create card from existing set


        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            if (notificationsModel != null) {
                set_name = notificationsModel.getNotificationsSetDetail().getName();
                set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
                chl_name = notificationsModel.getChannel_name();
            }
            /*channel_id = notificationsModel.getChannel_id();
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            Created_By = notificationsModel.getNotificationsSetDetail().getCreated_by();
            card_order_position = notificationsModel.getCard_order_position();*/


        } else {

            setsListModel = bundle.getParcelable("setsListModel");
            isReorder = bundle.getBoolean("re_order", false);
            chl_name = bundle.getString("chl_name");
            set_name = setsListModel.getSet_name();
            set_id = setsListModel.getSet_id();
        }
        //setTitle(set_name);
        if(isReorder){
            txtHintReorder.setVisibility(View.VISIBLE);
        }
        CurPagrPos = bundle.getInt("card_position");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(chl_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);
        view_nodata = (TextView) rootView.findViewById(R.id.view_nodata);
        card_listview = (RecyclerView) rootView.findViewById(R.id.card_listview);
        del_contr = (RelativeLayout) rootView.findViewById(R.id.set_del_contr);
        btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btn_delete = (Button) rootView.findViewById(R.id.btn_delete);
        chk_sel_all = (CheckBox) rootView.findViewById(R.id.chk_sel_all);
        txtItemSel = (TextView) rootView.findViewById(R.id.txtCntSelected);
        //  img_mutli_sel = (ImageView) rootView.findViewById(R.id.menu_multi_sel);

        setDlgListener(this);
        chk_sel_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (is_on_set_chg_chk_status) {

                    is_on_set_chg_chk_status = false;
                } else {
                    del_sel_id = new ArrayList<>();
                    list_Created_by = new ArrayList<>();
                    if (isChecked) {
                        chk_sel_all.setText("Unselect all");
                        btn_delete.setEnabled(true);

                        for (int i = 0; i < cardsListModels.size(); i++) {
                            CardsListModel modelObj = cardsListModels.get(i);
                            modelObj.setDelSel(true);
                            del_sel_id.add(modelObj.getCard_id());
                            list_Created_by.add(modelObj.getCreated_by());

                            cardsListModels.remove(i);
                            cardsListModels.add(i, modelObj);

                        }
                        txtItemSel.setText(del_sel_id.size() + " items selected");
                    } else {
                        chk_sel_all.setText("Select all");
                        btn_delete.setEnabled(false);
                        for (int i = 0; i < cardsListModels.size(); i++) {
                            CardsListModel modelObj = cardsListModels.get(i);
                            modelObj.setDelSel(false);
                            cardsListModels.remove(i);
                            cardsListModels.add(i, modelObj);
                        }
                        txtItemSel.setText("");
                    }
                }
                cardListAdapter.notifyDataSetChanged();
            }
        });


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strDelSelId = android.text.TextUtils.join(",", del_sel_id);
                strDelCrtdBy = android.text.TextUtils.join(",", list_Created_by);
                if (set_id_toCreateCard != null) {
                    //showAlertDialog("You are about to delete selected Cards. All the information contained in the Cards will be lost.", "Confirm Delete...", "Delete", "Cancel");
                    call_copy_card(strDelSelId);
                } else
                    showAlertDialog("You are about to delete selected Cards. All the information contained in the Cards will be lost.", "Confirm Delete...", "Delete", "Cancel");
                // Toast.makeText(MyChannelsSet.this,"Set Id:"+csv,Toast.LENGTH_LONG).show();
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
                if (set_id_toCreateCard != null) {
                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();
                    ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(false);
                } else {
                    reset_view();
                    for (int i = 0; i < cardsListModels.size(); i++) {
                        CardsListModel modelObj = cardsListModels.get(i);
                        if (modelObj.isDelSel()) {
                            modelObj.setDelSel(false);
                            cardsListModels.remove(i);
                            cardsListModels.add(i, modelObj);
                        }
                    }
                    cardListAdapter.set_SelToDel(false);
                    cardListAdapter.notifyDataSetChanged();
                    if (isReorder)
                        ith.attachToRecyclerView(card_listview);

                }
            }
        });


        if (isReorder) {
            ItemTouchHelper.Callback dragCallback = new ItemTouchHelper.Callback() {

                int dragFrom = -1;
                int dragTo = -1;

                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
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
                    Collections.swap(cardsListModels, fromPosition, toPosition);
                    // and notify the adapter that its dataset has changed
                    cardListAdapter.notifyItemMoved(fromPosition, toPosition);
                    cardListAdapter.notifyItemChanged(fromPosition);
                    cardListAdapter.notifyItemChanged(toPosition);
                    return true;

                }

                private void reallyMoved(int from, int to) {
                    // I guessed this was what you want...
                    call_card_reorder();
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
            ith.attachToRecyclerView(card_listview);

        }

        getCardsLists();

        return rootView;
    }

    public void multi_sel_actions() {
        if (cardsListModels.size() > 0) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            del_contr.setVisibility(View.VISIBLE);
            txtHintReorder.setVisibility(View.GONE);
            txtItemSel.setText("");
            btn_delete.setEnabled(false);
            chk_sel_all.setVisibility(View.VISIBLE);
            if (set_id_toCreateCard != null) {
                btn_delete.setText("Done");
                btn_cancel.setText("Back");
            }
            if (isReorder)
                ith.attachToRecyclerView(null);
            cardListAdapter.set_SelToDel(true);
            cardListAdapter.notifyDataSetChanged();
            // isMultiSelChoosed=true;
            //   ith = new ItemTouchHelper(dragCallback);

        } else {

        }


    }

    public void call_card_reorder() {
        String cardId = "";
        int i = 0;
        for (CardsListModel cardModelObj : cardsListModels) {
            if (i == 0) {
                i = 1;
            } else {
                cardId = cardId + ",";
            }
            cardId = cardId + cardModelObj.getCard_id();
        }
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).card_reorder_set(userId, cardId,setsListModel.getSet_id());
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        ((BrightlyNavigationActivity) getActivity()).isCardRefresh = true;
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
        list_Created_by = new ArrayList<>();
        txtHintReorder.setVisibility(View.VISIBLE);
        chk_sel_all.setVisibility(View.GONE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (isReorder) {
            inflater.inflate(R.menu.del_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_multi_sel) {

            multi_sel_actions();
        }
        return super.onOptionsItemSelected(item);
    }

    public void call_copy_card(String CardIds) {
        {
            try {

                if (CheckNetworkConnection.isOnline(getContext())) {
                    showProgress();
                    Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).call_copy_card(user_obj.getUser_Id(), set_id_toCreateCard, CardIds);
                    callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                        @Override
                        public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {

                            dismissProgress();
                            if (isSuccess) {
                                ((BrightlyNavigationActivity) getActivity()).DontRun = true;
                                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true, 3);
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
    }

    public void getCardsLists() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<CardsListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getCardsList(user_obj.getUser_Id(), set_id);
                callRegisterUser.enqueue(new ApiCallback<CardsListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<CardsListResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        CardsListResponse cardsListResponse = response.body();
                        if (isSuccess) {

                            if (cardsListResponse != null && cardsListResponse.getData() != null && cardsListResponse.getData().size() != 0) {

                                cardsListModels = cardsListResponse.getData();

                                setAdapter(cardsListModels);
                                if (set_id_toCreateCard != null) {
                                    multi_sel_actions();
                                }


                            } else {
                                card_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);
                                cardsListModels=new ArrayList<>();
                            }

                            getActivity().invalidateOptionsMenu();
                        } else {
                            showLongToast(getActivity(), message);

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

    private void setAdapter(List<CardsListModel> cardsListModels) {
        card_listview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        cardListAdapter = new CardListAdapter(getContext(), cardsListModels, this);
        card_listview.setAdapter(cardListAdapter);
        card_listview.getLayoutManager().scrollToPosition(CurPagrPos);
        cardListAdapter.isCardHighlight = true;
        cardListAdapter.SelectedPos = CurPagrPos;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cardListAdapter.isCardHighlight = false;
                cardListAdapter.notifyItemChanged(CurPagrPos);
            }
        }, 2000);

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
    }
*/
    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }*/

    @Override
    public void onSelect(int position, CardsListModel modelObj) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        del_contr.setVisibility(View.VISIBLE);
        if (modelObj.isDelSel()) {
            modelObj.setDelSel(false);
            int i = 0;
            for (String sel_ID : del_sel_id) {
                if (sel_ID.equals(modelObj.getCard_id())) {
                    del_sel_id.remove(i);
                    list_Created_by.remove(i);
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
            del_sel_id.add(modelObj.getCard_id());
            list_Created_by.add(modelObj.getCreated_by());
            if (cardsListModels.size() == del_sel_id.size()) {
                chk_sel_all.setText("Unselect all");
                is_on_set_chg_chk_status = true;
                chk_sel_all.setChecked(true);
            }
            txtItemSel.setText(del_sel_id.size() + " items selected");
            btn_delete.setEnabled(true);
        }
        cardsListModels.remove(position);

        /*if (del_sel_id.equals("")) {
            del_sel_id = modelObj.getSet_id();
        } else
            del_sel_id = del_sel_id + "," + modelObj.getSet_id();*/
        cardsListModels.add(position, modelObj);
        cardListAdapter.notifyDataSetChanged();
        // Toast.makeText(MyChannelsSet.this,"Selected set id:"+Sel_id,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCardClick(int position) {
        if (set_id_toCreateCard == null) {
            ((BrightlyNavigationActivity) getActivity()).isCardClicked = true;
            ((BrightlyNavigationActivity) getActivity()).card_toPosition = position;
            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
        } else {
            if (cardListAdapter.isSelToDel())
                onSelect(position, cardsListModels.get(position));
        }

    }

    public void call_api_del_multi_cards() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getDeleteCard(set_id, user_obj.getUser_Id(), strDelCrtdBy, strDelSelId);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse deleteSetResponse = response.body();
                        dismissProgress();
                        if (isSuccess) {
                            ((BrightlyNavigationActivity) getActivity()).isCardRefresh = true;
                            if (deleteSetResponse != null) {

                                if (deleteSetResponse.getMessage().equalsIgnoreCase("success")) {
                                    reset_view();
                                    getCardsLists();
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
    public void postive_btn_clicked() {
//      Toast.makeText(MyChannelsSet.this,"Working",Toast.LENGTH_LONG).show();
        call_api_del_multi_cards();
    }

    @Override
    public void negative_btn_clicked() {

    }

}
