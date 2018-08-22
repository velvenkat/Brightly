package com.purplefront.brightly.API;

import com.purplefront.brightly.Modules.AddChannelResponse;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.CardsListResponse;
import com.purplefront.brightly.Modules.ChannelListResponse;
import com.purplefront.brightly.Modules.DeleteChannelResponse;
import com.purplefront.brightly.Modules.SetListResponse;
import com.purplefront.brightly.Modules.SignInResponse;
import com.purplefront.brightly.Modules.SignUpResponse;
import com.purplefront.brightly.Modules.UpdateChannelResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApiMethods {

    String CACHE_CONTROL = "cache-control: no-cache";
    String CONTENT_TYPE = "content-type:application/json";
    //Register or SignUp
    @POST("users/newregister.php")
    Call<SignUpResponse> getSignup(@Query("name") String name, @Query("email_id") String email_id, @Query("mobile_no") String mobile_no, @Query("company_name") String company_name, @Query("password") String password);

    //SignIn
    @POST("users/newlogin.php")
    Call<SignInResponse> getSignIn(@Query("mobile_no") String mobile_no, @Query("password") String password);


    //ChannelsList
    @POST("channels/list_channels.php")
    Call<ChannelListResponse> getMyChannelsList(@Query("user_id") String user_id, @Query("type") String type);

    //Add Channels
    @POST("channels/add_channel.php")
    @FormUrlEncoded
    Call<AddChannelResponse> getAddChannels(@Query("user_id") String user_id, @Query("channel_name") String channel_name, @Query("channel_description") String channel_description, @Field("encoded_string") String encoded_string, @Query("image_name") String image_name);

    //Add Channels
    @POST("channels/update_channel.php")
    @FormUrlEncoded
    Call<UpdateChannelResponse> getUpdateChannels(@Query("user_id") String user_id, @Query("channel_name") String channel_name, @Query("channel_description") String channel_description, @Field("encoded_string") String encoded_string, @Query("image_name") String image_name, @Query("channel_id") String channel_id);


    //Add Channels
    @POST("channels/delete_channel.php")
    Call<DeleteChannelResponse> getDeleteChannels(@Query("channel_id") String channel_id);

    //SetList
    @POST("sets/list_set.php")
    Call<SetListResponse> getMySetsList(@Query("user_id") String user_id, @Query("channel_id") String channel_id);

    //Add Sets
    @POST("sets/add_set.php")
    Call<AddMessageResponse> getAddSet(@Query("user_id") String user_id, @Query("channel_id") String channel_id, @Query("set_name") String set_name, @Query("set_description") String set_description);

    //Update Sets
    @POST("sets/update_set.php")
    Call<AddMessageResponse> getUpdateSet(@Query("user_id") String user_id, @Query("channel_id") String channel_id, @Query("set_name") String set_name, @Query("set_description") String set_description, @Query("set_id") String set_id);

    //Update Sets
    @POST("sets/delete_set.php")
    Call<AddMessageResponse> getDeleteSet(@Query("set_id") String set_id);

    //CardList
    @POST("images/list_image.php")
    Call<CardsListResponse> getCardsList(@Query("set_id") String set_id);

    //SetList
    @POST("images/add_card.php")
    @FormUrlEncoded
    Call<AddMessageResponse> getAddCardsList(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("title") String title, @Query("description") String description, @Field("encoded_string") String encoded_string, @Query("name") String name);


    //Set_Reorder
    @POST("sets/display_setorder.php?")
    Call<AddMessageResponse> set_reorder_set(@Query("user_id") String user_id,  @Query("set_id") String set_id);


    //Update Card
    @POST("images/update_image.php")
    @FormUrlEncoded
    Call<AddMessageResponse> getUpdateCardsList(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("image_id") String image_id, @Query("title") String title, @Query("description") String description, @Field("encoded_string") String encoded_string, @Query("name") String name);


    //Delete Card
    @POST("images/delete_image.php")
    Call<AddMessageResponse> getDeleteCard(@Query("image_id") String image_id);

    //Card_Reorder
    @POST("images/display_order.php?")
    Call<AddMessageResponse> card_reorder_set(@Query("user_id") String user_id,  @Query("image_id") String image_id);

}