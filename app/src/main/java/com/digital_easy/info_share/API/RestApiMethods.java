package com.digital_easy.info_share.API;

import com.digital_easy.info_share.Modules.AddChannelResponse;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.AdminSettingsModel;
import com.digital_easy.info_share.Modules.AppVarModule;
import com.digital_easy.info_share.Modules.CardsListResponse;
import com.digital_easy.info_share.Modules.ChannelListResponse;
import com.digital_easy.info_share.Modules.CommentsListResponse;
import com.digital_easy.info_share.Modules.DeleteChannelResponse;
import com.digital_easy.info_share.Modules.EditProfileResponse;
import com.digital_easy.info_share.Modules.JSONObjectReqModule;
import com.digital_easy.info_share.Modules.JSONReqMVModule;
import com.digital_easy.info_share.Modules.MyProfileResponse;
import com.digital_easy.info_share.Modules.NotificationsResponse;
import com.digital_easy.info_share.Modules.SetInfoSharedResponse;
import com.digital_easy.info_share.Modules.SetListResponse;
import com.digital_easy.info_share.Modules.SignInResponse;
import com.digital_easy.info_share.Modules.SignUpResponse;
import com.digital_easy.info_share.Modules.UpdateChannelResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Observable<EditProfileResponse> getEditProfile(@Query("user_id") String user_id, @Query("name") String name, @Query("email") String email_id, @Query("mobile_no") String mobile_no, @Query("company_name") String company_name, @Field("image") String image, @Query("image_name") String image_name);


    //Register or SignUp
    @POST("users/newregister.php")
    Call<SignUpResponse> getSignup(@Query("name") String name, @Query("mobile_no") String mobile_no, @Query("password") String password, @Query("email_id") String email, @Query("token") String token, @Query("os") String os_type);

    //SignIn
    @POST("users/newlogin.php")
    Call<SignInResponse> getSignIn(@Query("mobile_no") String mobile_no, @Query("password") String Password, @Query("token") String token, @Query("os") String os_type);

    //ForgotPassword
    @POST("users/verify_phone.php")
    Call<AddMessageResponse> getForgetPassword_1(@Query("mobile_no") String mobileNo);

    @POST("users/forgot_password_otp.php")
    Call<AddMessageResponse> getForgetPassword_2(@Query("mobile_no") String mobileNo, @Query("otp") String otp, @Query("password") String password);


    //Validate OTP
    @POST("users/validateotp.php")
    Call<SignInResponse> getValidateOtp(@Query("mobile_no") String mobile_no, @Query("otp") String otp);

    //Resend OTP
    @POST("users/resendotp.php")
    Call<AddMessageResponse> getResendOtp(@Query("user_id") String user_id, @Query("mobile_no") String mobile_no);

    @POST("users/resendotp.php")
    Call<AddMessageResponse> getResendOtp(@Query("mobile_no") String mobile_no);


    //ChannelsList
    @POST("channels/list_channels.php")
    Call<ChannelListResponse> getMyChannelsList(@Query("user_id") String user_id, @Query("type") String type);

    //Add Channels
    @POST("channels/add_channel.php")
    @FormUrlEncoded
    Observable<AddChannelResponse> getAddChannels(@Query("user_id") String user_id, @Query("channel_name") String channel_name, @Query("channel_description") String channel_description, @Field("encoded_string") String encoded_string, @Query("image_name") String image_name);

    //Add Channels
    @POST("channels/update_channel.php")
    @FormUrlEncoded
    Observable<UpdateChannelResponse> getUpdateChannels(@Query("user_id") String user_id, @Query("channel_name") String channel_name, @Query("channel_description") String channel_description, @Field("encoded_string") String encoded_string, @Query("image_name") String image_name, @Query("channel_id") String channel_id);


    //Add Channels
    @POST("channels/delete_channel.php")
    Call<DeleteChannelResponse> getDeleteChannels(@Query("channel_id") String channel_id);

    //SetList
    @POST("sets/list_set.php")
    Call<SetListResponse> getMySetsList(@Query("user_id") String user_id, @Query("channel_id") String channel_id, @Query("type") String type);

    //Add Sets
    @POST("sets/add_set.php")
    Call<AddMessageResponse> getAddSet(@Query("user_id") String user_id, @Query("channel_id") String channel_id, @Query("set_name") String set_name, @Query("set_description") String set_description, @Query("channel_name") String channel_name);

    //Update Sets
    @POST("sets/update_set.php")
    Call<AddMessageResponse> getUpdateSet(@Query("user_id") String user_id, @Query("channel_id") String channel_id, @Query("set_name") String set_name, @Query("set_description") String set_description, @Query("set_id") String set_id);

    //Update Sets
    @POST("sets/set_info.php")
    Call<SetInfoSharedResponse> getSetSharedInfo(@Query("user_id") String user_id, @Query("channel_id") String channel_id, @Query("set_id") String set_id);

    //Toggle Share Link

    /**
     * @param set_id
     * @param toggle_value 0 means not allow 1 means  allow to share link
     * @return
     */
    @POST("sets/toggle_share_link.php")
    Call<AddMessageResponse> setToggleShareLink(@Query("set_id") String set_id, @Query("value") String toggle_value);


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
    Call<CardsListResponse> getCardsList(@Query("user_id") String User_id, @Query("set_id") String set_id);

    //SetList
    @POST("cards/add_card.php")
    @FormUrlEncoded
    Observable<AddMessageResponse> getAddCardsList(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("title") String title, @Query("description") String description, @Field("encoded_string") String encoded_string, @Query("name") String name);


    @POST("cards/add_card.php")
    @FormUrlEncoded
    Observable<AddMessageResponse> getAddContacts(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("contact_name") String cont_name, @Query("description") String notes, @Field("encoded_string") String encoded_string, @Query("name") String img_name, @Query("title") String title, @Query("cell_phone") String mob_no, @Query("email") String email_id, @Query("company") String company_name, @Query("office_phone") String off_phone);


    @POST("cards/update_card.php")
    @FormUrlEncoded
    Observable<AddMessageResponse> getUpdateContacts(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("card_id") String card_id, @Query("contact_name") String cont_name, @Query("description") String notes, @Field("encoded_string") String encoded_string, @Query("name") String img_name, @Query("title") String title, @Query("cell_phone") String mob_no, @Query("email") String email_id, @Query("company") String company_name, @Query("office_phone") String off_phone, @Query("remove_img_id") String remove_img_id);


    @POST("cards/add_card.php")
    @FormUrlEncoded
    Call<AddMessageResponse> getAddCardsList_call(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("title") String title, @Query("description") String description, @Field("encoded_string") String encoded_string, @Query("name") String name);


    @Multipart
    @POST("cards/add_card.php")
    Call<AddMessageResponse> addRecord(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("title") String title, @Query("description") String description, @Query("name") String name, @Part MultipartBody.Part file);


    //Set_Reorder
    @POST("sets/display_setorder.php")
    Call<AddMessageResponse> set_reorder_set(@Query("user_id") String user_id, @Query("set_id") String set_id);


    //Update Card
    @POST("cards/update_card.php")
    @FormUrlEncoded
    Observable<AddMessageResponse> getUpdateCardsList(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("card_id") String card_id, @Query("title") String title, @Query("description") String description, @Field("encoded_string") String encoded_string, @Query("name") String name, @Query("remove_img_id") String remove_img_id);


    @Multipart
    @POST("cards/update_card.php")
    Call<AddMessageResponse> updateRecord(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("card_id") String card_id, @Query("title") String title, @Query("description") String description, @Part MultipartBody.Part file);

    @POST("cards/update_card.php")
    Call<AddMessageResponse> updateRecord_old(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("card_id") String card_id, @Query("title") String title, @Query("description") String description, @Query("name") String old_file_name);

    //Update Card
    @POST("cards/update_card.php")
    @FormUrlEncoded
    Call<AddMessageResponse> getUpdateCardsList_call(@Query("type") String type, @Query("user_id") String user_id, @Query("set_id") String set_id, @Query("card_id") String card_id, @Query("title") String title, @Query("description") String description, @Field("encoded_string") String encoded_string, @Query("name") String name);

    //Delete Card
    @POST("cards/delete_card.php")
    Call<AddMessageResponse> getDeleteCard(@Query("set_id") String SetId, @Query("user_id") String UserId, @Query("created_by") String CardCreatedBy, @Query("card_id") String card_id);

    //Card_Reorder
    @POST("cards/display_order.php")
    Call<AddMessageResponse> card_reorder_set(@Query("user_id") String user_id, @Query("card_ids") String card_id, @Query("set_id") String SetId);

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
    Call<AddMessageResponse> call_copy_card(@Query("user_id") String user_id, @Query("set_id") String set_id, @Query("org_set_id") String card_org_set_id, @Query("card_id") String card_ids);


    //Comments Set
    @POST("comment/add_comment.php")
    Call<AddMessageResponse> getCardComments(@Query("user_id") String user_id, @Query("card_id") String card_id, @Query("set_id") String set_id, @Query("comment") String comment, @Query("replied_to") String reply_to);

    @POST("appvariables/getappvar.php")
    Call<AppVarModule> getAppVariable(@Query("user_id") String usr_id);

    @POST("cards/share_cards.php")
    Call<AddMessageResponse> call_share_card(@Query("user_id") String usr_id, @Query("channel_id") String chl_id, @Query("card_id") String card_id, @Query("org_set_id") String set_id, @Query("phone_no") String mob_no, @Query("names") String names);


    @POST("cards/set_from_cards.php")
    Call<SetListResponse> call_set_from_cards(@Query("user_id") String usr_id, @Query("card_id") String card_id, @Query("org_set_id") String set_id, @Query("set_name") String set_name);


    @POST("comment/list_comment.php")
    Call<CommentsListResponse> call_comment_list(@Query("card_id") String card_id, @Query("user_id") String user_Id);

    @POST("sets/set_default_name.php")
    Call<AddMessageResponse> call_create_def_set(@Query("user_id") String user_id);

    @POST("admin_settings/list_customization_labels.php")
    Call<AdminSettingsModel> list_custom_label(@Query("user_id") String user_id);


    //NEW APIS

    @Multipart
    @POST("cards/upload_card_files.php")
    Call<AddMessageResponse> upload_diff_type(@Query("user_id") String user_id, @Query("set_id") String set_id, @Part MultipartBody.Part file);

    @GET("cards/add_new_card.php")
    Call<ResponseBody> call_create_card(@Query("user_id") String user_id, @Query("flag") String FlagVal, @Query("set_id") String set_id, @Query("data") String jsonBody);

    @GET("cards/add_new_card.php")
    Call<ResponseBody> call_update_blog(@Query("user_id") String user_id, @Query("flag") String FlagVal, @Query("set_id") String set_id, @Query("data") String jsonBody, @Query("card_id") String card_id);


}