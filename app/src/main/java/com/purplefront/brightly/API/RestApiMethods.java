package com.purplefront.brightly.API;

import com.purplefront.brightly.Modules.AddChannelResponse;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.CardsListResponse;
import com.purplefront.brightly.Modules.ChannelListResponse;
import com.purplefront.brightly.Modules.DeleteChannelResponse;
import com.purplefront.brightly.Modules.EditProfileResponse;
import com.purplefront.brightly.Modules.MyProfileResponse;
import com.purplefront.brightly.Modules.NotificationsResponse;
import com.purplefront.brightly.Modules.SetInfoSharedResponse;
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

    //MyProfile
    @POST("users/my_profile.php")
    Call<MyProfileResponse> getMyProfile(@Query("user_id") String user_id);

    //MyProfile
    @POST("users/profile_edit.php")
    @FormUrlEncoded
    Call<EditProfileResponse> getEditProfile(@Query("user_id") String user_id, @Query("name") String name, @Query("email_id") String email_id, @Query("mobile_no") String mobile_no, @Query("company_name") String company_name, @Field("image") String image, @Query("image_name") String image_name);


    //Register or SignUp
    @POST("users/newregister.php")
    Call<SignUpResponse> getSignup(@Query("name") String name, @Query("email_id") String email_id, @Query("mobile_no") String mobile_no, @Query("company_name") String company_name, @Query("password") String password, @Query("token") String token, @Query("os") String os_type);

    //SignIn
    @POST("users/newlogin.php")
    Call<SignInResponse> getSignIn(@Query("mobile_no") String mobile_no, @Query("password") String password, @Query("token") String token, @Query("os") String os_type);

    //ForgotPassword
    @POST("users/forgot_password.php")
    Call<AddMessageResponse> getForgetPassword(@Query("email_id") String email_id);

    //Validate OTP
    @POST("users/validateotp.php")
    Call<AddMessageResponse> getValidateOtp(@Query("user_id") String user_id);

    //Resend OTP
    @POST("users/resendotp.php")
    Call<AddMessageResponse> getResendOtp(@Query("user_id") String user_id, @Query("mobile_no") String mobile_no);


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
    @POST("sets/set_info.php")
    Call<SetInfoSharedResponse> getSetSharedInfo(@Query("user_id") String user_id, @Query("channel_id") String channel_id, @Query("set_id") String set_id);


    //delete Sets
    @POST("sets/delete_set.php")
    Call<AddMessageResponse> getDeleteSet(@Query("set_id") String set_id);

    //Share Sets
    @POST("share/share_set_multiple.php")
    Call<AddMessageResponse> getShareSet(@Query("user_id") String user_id, @Query("set_id") String set_id, @Query("phone_no") String phone_no, @Query("names") String names);

    //Revoke Sets
    @POST("share/revoke_set.php")
    Call<AddMessageResponse> getRevokeSet(@Query("set_id") String set_id, @Query("assigned_to") String assigned_to, @Query("user_id") String user_id);


    //CardList
    @POST("cards/card_list.php")
    Call<CardsListResponse> getCardsList(@Query("user_id") String User_id,@Query("set_id") String set_id);

    //SetList
    @POST("cards/add_card.php")
    @FormUrlEncoded
    Call<AddMessageResponse> getAddCardsList(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("title") String title, @Query("description") String description, @Field("encoded_string") String encoded_string, @Query("name") String name);


    //Set_Reorder
    @POST("sets/display_setorder.php")
    Call<AddMessageResponse> set_reorder_set(@Query("user_id") String user_id, @Query("set_id") String set_id);


    //Update Card
    @POST("cards/update_card.php")
    @FormUrlEncoded
    Call<AddMessageResponse> getUpdateCardsList(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("card_id") String card_id, @Query("title") String title, @Query("description") String description, @Field("encoded_string") String encoded_string, @Query("name") String name);


    //Delete Card
    @POST("cards/delete_card.php")
    Call<AddMessageResponse> getDeleteCard(@Query("set_id") String SetId,@Query("user_id") String UserId,@Query("created_by") String CardCreatedBy,@Query("card_id") String card_id);

    //Card_Reorder
    @POST("cards/display_order.php")
    Call<AddMessageResponse> card_reorder_set(@Query("user_id") String user_id, @Query("card_ids") String card_id,@Query("set_id") String SetId);

    //Notifications
    @POST("notification/in_app.php")
    Call<NotificationsResponse> getNotifications(@Query("user_id") String user_id);

    //Notifications
    @POST("notification/count.php")
    Call<NotificationsResponse> getNotificationCounts(@Query("user_id") String user_id);

    @POST("users/logout.php")
    Call<AddMessageResponse> call_logout_user(@Query("user_id") String user_id, @Query("token") String token);

    @POST("sets/share_link.php")
    Call<AddMessageResponse> call_share_access_update(@Query("user_id") String user_id, @Query("set_id") String set_id, @Query("value") String acc_value, @Query("assigned_to") String assigned_to);

    @POST("cardsetassoc/link_card.php")
    Call<AddMessageResponse> call_copy_card(@Query("user_id") String user_id, @Query("set_id") String set_id, @Query("card_id") String card_ids);


    //Comments Set
    @POST("comment/add_comment.php")
    Call<AddMessageResponse> getSetComments(@Query("user_id") String user_id, @Query("set_id") String set_id, @Query("comment") String comment);

}