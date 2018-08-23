package com.purplefront.brightly.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Adapters.CardListAdapter;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CardsListResponse;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CardList extends BaseActivity implements BaseActivity.alert_dlg_interface,CardListAdapter.Card_sel_interface {

    List<CardsListModel> cardsListModels = new ArrayList<>();
    CardListAdapter cardListAdapter;
    RecyclerView card_listview;
    TextView view_nodata;
    String userId;
    String set_name = "";
    String set_id = "";
    ArrayList<String> del_sel_id = new ArrayList<>();
    RelativeLayout del_contr;
    Button btn_cancel, btn_delete;
    String strDelSelId = "";
    TextView txtItemSel;
    CheckBox chk_sel_all;
    boolean is_on_set_chg_chk_status=false; //SELECT ALL CHECK BOX CHANGE BASED ON SET SELECTION


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userId = getIntent().getStringExtra("userId");
        set_name = getIntent().getStringExtra("set_name");
        set_id = getIntent().getStringExtra("set_id");
        setTitle(set_name);

        view_nodata = (TextView) findViewById(R.id.view_nodata);
        card_listview = (RecyclerView) findViewById(R.id.card_listview);
        del_contr = (RelativeLayout) findViewById(R.id.set_del_contr);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        chk_sel_all = (CheckBox) findViewById(R.id.chk_sel_all);
        txtItemSel = (TextView) findViewById(R.id.txtCntSelected);

        setDlgListener(this);
        chk_sel_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(is_on_set_chg_chk_status){

                    is_on_set_chg_chk_status=false;
                }

                else{
                    del_sel_id=new ArrayList<>();
                    if (isChecked) {
                        chk_sel_all.setText("Unselect all");
                        btn_delete.setEnabled(true);

                        for(int i=0;i<cardsListModels.size();i++){
                            CardsListModel modelObj=cardsListModels.get(i);
                            modelObj.setDelSel(true);
                            del_sel_id.add(modelObj.getCard_id());
                            cardsListModels.remove(i);
                            cardsListModels.add(i,modelObj);

                        }
                        txtItemSel.setText(del_sel_id.size() + " items selected");
                    } else {
                        chk_sel_all.setText("Select all");
                        btn_delete.setEnabled(false);
                        for(int i=0;i<cardsListModels.size();i++){
                            CardsListModel modelObj=cardsListModels.get(i);
                            modelObj.setDelSel(false);
                            cardsListModels.remove(i);
                            cardsListModels.add(i,modelObj);
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
            }
        });



        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Collections.swap(cardsListModels, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                cardListAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                //Toast.makeText(MyChannelsSet.this,"OnMoved",Toast.LENGTH_LONG).show();
                call_card_reorder();
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
        ith.attachToRecyclerView(card_listview);



        getCardsLists();
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

            if (CheckNetworkConnection.isOnline(CardList.this)) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(CardList.this).card_reorder_set(userId, cardId);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(CardList.this) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        Toast.makeText(CardList.this, "Message:" + response.message(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
                    }
                });
            } /*else {

                dismissProgress();
            }*/ else {
                Toast.makeText(CardList.this, "Check network connection", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();

            dismissProgress();
        }

    }



    public void reset_view() {
        getSupportActionBar().show();
        del_contr.setVisibility(View.GONE);
        del_sel_id = new ArrayList<>();

        chk_sel_all.setVisibility(View.GONE);

    }

    public void getCardsLists() {
        try {

            if (CheckNetworkConnection.isOnline(CardList.this)) {
                showProgress();
                Call<CardsListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(CardList.this).getCardsList(set_id);
                callRegisterUser.enqueue(new ApiCallback<CardsListResponse>(CardList.this) {
                    @Override
                    public void onApiResponse(Response<CardsListResponse> response, boolean isSuccess, String message) {
                        CardsListResponse cardsListResponse = response.body();
                        if (isSuccess) {

                            if (cardsListResponse != null && cardsListResponse.getImages() != null && cardsListResponse.getImages().size() != 0) {

                                cardsListModels = cardsListResponse.getImages();
                                setAdapter(cardsListModels);
                                dismissProgress();

                            } else {
                                card_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);
                                dismissProgress();
                            }


                        } else {
                            showLongToast(CardList.this, message);
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

    private void setAdapter(List<CardsListModel> cardsListModels) {
        card_listview.setLayoutManager(new LinearLayoutManager(CardList.this));
        cardListAdapter = new CardListAdapter(CardList.this, cardsListModels,this);
        card_listview.setAdapter(cardListAdapter);

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
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    @Override
    public void onSelect(int position, CardsListModel modelObj) {
        getSupportActionBar().hide();
        del_contr.setVisibility(View.VISIBLE);
        if (modelObj.isDelSel()) {
            modelObj.setDelSel(false);
            int i = 0;
            for (String sel_ID : del_sel_id) {
                if (sel_ID.equals(modelObj.getCard_id())) {
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
                is_on_set_chg_chk_status=true;
                chk_sel_all.setChecked(false);
            }
        } else {
            modelObj.setDelSel(true);

            chk_sel_all.setVisibility(View.VISIBLE);
            del_sel_id.add(modelObj.getCard_id());
            if(cardsListModels.size()==del_sel_id.size()){
                chk_sel_all.setText("Unselect all");
                is_on_set_chg_chk_status=true;
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

    public void call_api_del_multi_cards() {
        try {

            if (CheckNetworkConnection.isOnline(CardList.this)) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(CardList.this).getDeleteCard(strDelSelId);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(CardList.this) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse deleteSetResponse = response.body();
                        dismissProgress();
                        if (isSuccess) {

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
