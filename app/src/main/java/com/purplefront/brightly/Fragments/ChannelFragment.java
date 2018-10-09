package com.purplefront.brightly.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.MyChannelsAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.BadgeDrawable;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.ChannelListResponse;
import com.purplefront.brightly.Modules.NotificationsResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.CircleTransform;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;



public class ChannelFragment extends BaseFragment implements MyChannelsAdapter.ChannelListItemClickListener {
    List<ChannelListModel> channelListModels = new ArrayList<>();
    MyChannelsAdapter myChannelsAdapter;
    ImageView image_createChannel;
    TextView view_nodata;
    String channel_type;
    RecyclerView channels_listview;
    View rootView;
    RealmModel user_obj;

    Realm realm;
    RealmResults<RealmModel> realmModel;

  //  ActionBarUtil actionBarUtilObj;
    String count="0";
    String userName;
    String userPhone;
    String userPicture;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.lo_scrn_channel,container,false);
        user_obj=((BrightlyNavigationActivity)getActivity()).getUserModel();
        image_createChannel = (ImageView)rootView. findViewById(R.id.image_createChannel);
        channels_listview=(RecyclerView)rootView.findViewById(R.id.channels_listview);
        setHasOptionsMenu(true);
        Bundle bundle=getArguments();
        channel_type=bundle.getString("type");

        if(channel_type.equalsIgnoreCase("all")){
            setActionBarTitle("All Channel");
        }
        else if(channel_type.equalsIgnoreCase("subscribe")){
            setActionBarTitle("Shared with me");
        }
        else if(channel_type.equalsIgnoreCase("my")){
            setActionBarTitle("My Channel");
        }
        getChannelsLists(channel_type);
        image_createChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         /*       Intent intent = new Intent(BrightlyNavigationActivity.this, CreateChannel.class);

                intent.putExtra("userId", user_obj.getUser_Id());
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                Fragment fragment=new CreateChannelFragment();
                Bundle bundle=new Bundle();
                bundle.putString("type",channel_type);
                fragment.setArguments(bundle);
                ((BrightlyNavigationActivity)getActivity()).onFragmentCall( Util.Create_Channel,fragment,true);
            }
        });

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();

        realmModel.load();
        for(RealmModel model:realmModel){
            userName =  model.getUser_Name();
            userPhone = model.getUser_PhoneNumber();
            userPicture = model.getImage();

        }

        ((BrightlyNavigationActivity)getActivity()).headerText_Name.setText(userName);
        ((BrightlyNavigationActivity)getActivity()).headerText_Phone.setText(userPhone);

        if (userPicture != null) {

            Glide.with(getActivity())
                    .load(userPicture)
                    .centerCrop()
                    .transform(new CircleTransform(getActivity()))
//                        .override(50, 50)
                    .into(((BrightlyNavigationActivity) getActivity()).headerImage_Profile);
        } else {
            Glide.with(getActivity())
                    .load(R.drawable.default_user_image)
                    .centerCrop()
                    .transform(new CircleTransform(getActivity()))
                    /*.override(50, 50)*/
                    .into(((BrightlyNavigationActivity) getActivity()).headerImage_Profile);
        }


        view_nodata = (TextView)rootView.findViewById(R.id.view_nodata);
        channels_listview = (RecyclerView)rootView. findViewById(R.id.channels_listview);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }
    private void setActionBarTitle(String titleVal){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(titleVal);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(null);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getActivity().invalidateOptionsMenu();
        getNotificationCount();
        getActivity().getMenuInflater().inflate(R.menu.my_channel, menu);
        MenuItem itemCart = menu.findItem(R.id.action_bell);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(getContext(), icon, count);

        super.onPrepareOptionsMenu(menu);

    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
    private void setNotificationCounts(NotificationsResponse notificationsResponse) {

        String message = notificationsResponse.getMessage();

        if (message.equals("success")) {
            count = notificationsResponse.getCount();
        } else {
            count = "0";
        }
    }

    public void getNotificationCount() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
//                showProgress();
                Call<NotificationsResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getNotificationCounts(user_obj.getUser_Id());
                callRegisterUser.enqueue(new ApiCallback<NotificationsResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<NotificationsResponse> response, boolean isSuccess, String message) {
                        NotificationsResponse notificationsResponse = response.body();

                        if (isSuccess) {

                            if (notificationsResponse != null) {

                                setNotificationCounts(notificationsResponse);
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
//                        showLongToast(MyChannel.this, message);
                        dismissProgress();
                    }
                });
            } else {

//                showLongToast(MyChannel.this, "Network Error");
                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            showLongToast(MyChannel.this, "Something went Wrong, Please try Later");


        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bell) {

           /* fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new Notifications(),
                            Util.NOTIFICATIONS).commit();
*/
            Fragment nfy_frag = new Notifications();
            ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.NOTIFICATIONS, nfy_frag,false);
            //          frag_container.setVisibility(View.VISIBLE);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getChannelsLists(String type) {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
//                showProgress();
                Call<ChannelListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getMyChannelsList(user_obj.getUser_Id(), type);
                callRegisterUser.enqueue(new ApiCallback<ChannelListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<ChannelListResponse> response, boolean isSuccess, String message) {
                        ChannelListResponse channelListResponse = response.body();
                        if (isSuccess) {

                            if (channelListResponse != null && channelListResponse.getChannels() != null && channelListResponse.getChannels().size() != 0) {

                                channelListModels.clear();
                                channelListModels = channelListResponse.getChannels();
                                setAdapter(channelListModels);
                                channels_listview.setVisibility(View.VISIBLE);
                                view_nodata.setVisibility(View.GONE);
                                dismissProgress();

                            } else {
                                channels_listview.setVisibility(View.GONE);
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

    private void setAdapter(List<ChannelListModel> channelListModels) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        channels_listview.setLayoutManager(gridLayoutManager);
        myChannelsAdapter = new MyChannelsAdapter(getActivity(), channelListModels,this);
        channels_listview.setAdapter(myChannelsAdapter);
        myChannelsAdapter.notifyDataSetChanged();

    }

    @Override
    public void OnChannelItemClick(Fragment frag_args) {
        ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.Set_List,frag_args,false);
    }
}
