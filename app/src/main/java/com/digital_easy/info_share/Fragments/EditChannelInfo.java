package com.digital_easy.info_share.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RestApiMethods;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.CustomToast;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.ContactShare;
import com.digital_easy.info_share.Modules.DeleteChannelResponse;
import com.digital_easy.info_share.Modules.UpdateChannelResponse;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.ImageChooser_Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EditChannelInfo extends BaseFragment implements BrightlyNavigationActivity.PermissionResultInterface {

    SimpleDraweeView imageView_editChannelImage;
    EditText edit_channelName;
    EditText edit_channelDescription;
    Button btn_editChannel;

    String userId;
    String channel_name = "";
    String channel_description = "";
    String encoded_string = "";
    String image_name = "";
    String channel_id = "";

    int PICK_IMAGE_REQ = 77;
    ResizeOptions img_resize_opts;
    ChannelListModel chl_modl_obj;
    ImageChooser_Crop imgImageChooser_crop;
    View rootView;
    TextView shared_by;

    String sharedName;
    String shareTime;

    RealmModel user_obj;
    // String level1_Title_singular;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_edit_channel_info, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        setHasOptionsMenu(true);

//        setContentView(R.layout.activity_edit_channel_info);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/

        ((BrightlyNavigationActivity) getActivity()).permissionResultInterfaceObj = this;

        userId = user_obj.getUser_Id();
        /*channel_id = getIntent().getStringExtra("channel_id");
        channel_name = getIntent().getStringExtra("channel_name");
        channel_description = getIntent().getStringExtra("channel_description");
        encoded_string = getIntent().getStringExtra("encoded_string");
        image_name = getIntent().getStringExtra("image_name");*/

        //   chl_modl_obj = bundle.getParcelable("model_obj");
        chl_modl_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;
        channel_id = chl_modl_obj.getChannel_id();
        channel_name = chl_modl_obj.getChannel_name();
        channel_description = chl_modl_obj.getDescription();
        encoded_string = chl_modl_obj.getCover_image();
        image_name = chl_modl_obj.getImage_name();
        sharedName = chl_modl_obj.getShared_by();
        shareTime = chl_modl_obj.getShared_time();


        imageView_editChannelImage = (SimpleDraweeView) rootView.findViewById(R.id.imageView_editChannelImage);
        edit_channelName = (EditText) rootView.findViewById(R.id.edit_channelName);
        edit_channelDescription = (EditText) rootView.findViewById(R.id.edit_channelDescription);
        btn_editChannel = (Button) rootView.findViewById(R.id.btn_editChannel);
        shared_by = (TextView) rootView.findViewById(R.id.shared_by);
        //  level1_Title_singular = ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel1title().getSingular();
        edit_channelName.setHint(((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR + " Name");
        edit_channelDescription.setHint(((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR + " Description");
        btn_editChannel.setText("Edit " + ((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR);
        edit_channelName.setText(channel_name);
        edit_channelDescription.setText(channel_description);

        clear_edit_text_focus(edit_channelName);
        clear_edit_text_focus(edit_channelDescription);

        if (!userId.equalsIgnoreCase(chl_modl_obj.getCreated_by())) {
            edit_channelName.setEnabled(false);
            edit_channelDescription.setEnabled(false);
            btn_editChannel.setVisibility(View.GONE);
            shared_by.setVisibility(View.VISIBLE);
            shared_by.setText("Shared by : " + sharedName + "\n" + "On : " + shareTime);
            //  setTitle("Channel Info");
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR + " Info");
        } else {
            shared_by.setVisibility(View.GONE);
            btn_editChannel.setVisibility(View.VISIBLE);
            //   setTitle("Edit Channel Info");
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Edit " + ((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR + " Info");
        }
        btn_editChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        imageView_editChannelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.equalsIgnoreCase(chl_modl_obj.getCreated_by())) {
                  /*  imgImageChooser_crop = new ImageChooser_Crop(getActivity());
                    Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                    if (intent == null) {
                        //PermissionUtil.
                    } else {
                        startActivityForResult(intent, PICK_IMAGE_REQ);
                    }*/
                    setBottomDialog();
                }
            }
        });
        setChl_image(encoded_string);

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

    public void setChl_image(String URL) {
        String img_url;
        ResizeOptions resizeOptions = new ResizeOptions(450, 300);
        if (URL != null && !URL.trim().equals(""))
            img_url = URL;
        else
            img_url = ((BrightlyNavigationActivity) getActivity()).CATG_DEF_IMAGE;


        if (img_url != null && !img_url.trim().equals("")) {
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(getContext().getResources());
            builder.setProgressBarImage(R.drawable.loader);

            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            imageView_editChannelImage.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img_url))
                            .setResizeOptions(resizeOptions)

                            .build();
            imageView_editChannelImage.setImageRequest(imageRequest);

        }

    }

    public void setBottomDialog() {

        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_view_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        menu_list.add("Choose new image");
        menu_list.add("Remove image");

        ArrayAdapter<String> menu_itmes = new ArrayAdapter<String>(getContext(), R.layout.menu_row_diualog, R.id.dialog_menu_textView,
                menu_list);
        list_SettingsMenu.setAdapter(menu_itmes);
        list_SettingsMenu.requestFocus();
        Button btnCancel = (Button) mBottomSheetDialog.getWindow().findViewById(R.id.btnCancel);
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

                                encoded_string = "remove_image";
                              /*  Glide.with(getActivity())
                                        .load(R.drawable.no_image_available)
                                        .centerCrop()
                                        *//*.transform(new CircleTransform(HomeActivity.this))
                                        .override(50, 50)*//*
                                        .into(imageView_editChannelImage);*/
                                setChl_image("");
                                break;
                            case 0:

                                imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "default");
                                Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                                if (intent == null) {
                                    //PermissionUtil.
                                } else {
                                    startActivityForResult(intent, PICK_IMAGE_REQ);
                                }

                                break;

                        }
                    }
                }
        );

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        channel_name = edit_channelName.getText().toString();
        channel_description = edit_channelDescription.getText().toString();
        if (encoded_string == "") {
            encoded_string = "old";
        }

        // Check if all strings are null or not
        if (channel_name.trim().equals("")) {

            new CustomToast().Show_Toast(getContext(), edit_channelName,
                    ((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR + " name is required");
        }

        // Else do signup or do your stuff
        else {
            getUpdateChannels();
        }
    }

    public void showAlertDialog() {
        String setTitle = ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel2title().getPlural();
        String cardTitle = ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel3title().getPlural();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete....");

        // Setting Dialog Message
        alertDialog.setMessage("You are about to delete the " + ((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR + ".All the information contained in the " + ((BrightlyNavigationActivity) getActivity()).SET_PLURAL + " & " + ((BrightlyNavigationActivity) getActivity()).CARD_PLURAL + " will be lost. ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                getDeleteChannel();
                // Write your code here to invoke YES event
//                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
//                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        if (chl_modl_obj.getCreated_by().equalsIgnoreCase(userId))
            inflater.inflate(R.menu.delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;*/

            case R.id.delete:

                showAlertDialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDeleteChannel() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<DeleteChannelResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getDeleteChannels(channel_id);
                callRegisterUser.enqueue(new ApiCallback<DeleteChannelResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<DeleteChannelResponse> response, boolean isSuccess, String message) {
                        DeleteChannelResponse deleteChannelResponse = response.body();
                        if (isSuccess) {

                            if (deleteChannelResponse != null) {
                                dismissProgress();
                                setDeleteChannelCredentials(deleteChannelResponse);


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

    private void setDeleteChannelCredentials(DeleteChannelResponse deleteChannelResponse) {

        String message = deleteChannelResponse.getMessage();

        if (message.equals("success")) {
/*            stackClearIntent(EditChannelInfo.this, BrightlyNavigationActivity.class);
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
          /*  Fragment fragment=new ChannelFragment();
            ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.CHANNELS,fragment,false);*/
            showShortToast(getActivity(), ((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR + " " + channel_name + " is Deleted");
            ((BrightlyNavigationActivity) getActivity()).DontRunOneTime = true;
            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true, 2);


        } else {
            showLongToast(getActivity(), message);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == OPEN_SETTINGS) {
            Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
            if (intent == null) {
                //PermissionUtil.
            } else {
                startActivityForResult(intent, PICK_IMAGE_REQ);
            }

        }*/
        if (requestCode == PICK_IMAGE_REQ) {
            if (resultCode == RESULT_OK) {

                try {

                    Uri picUri = imgImageChooser_crop.getPickImageResultUri(data);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setCropMenuCropButtonTitle("Done")
                                .setMinCropResultSize(250, 250)
                                .setMaxCropResultSize(bitmap.getWidth(), bitmap.getHeight())
//                                .setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .start(getContext(), this);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {


                    Uri resultUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);

                    encoded_string = getStringImage(bitmap);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    image_name = sdf.format(new Date());
                   /* if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgPet.setImageDrawable(drawable);
                    }*/
                  /*  final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)
                                    .setResizeOptions(img_resize_opts)
                                    .build();
                    imageView_editChannelImage.setImageRequest(imageRequest2);*/

                    /*Glide.with(getContext())
                            .load(resultUri)
                            .fitCenter()
                            *//*.transform(new CircleTransform(HomeActivity.this))
                            .override(50, 50)*//*
                            .into(imageView_editChannelImage);*/

                    GenericDraweeHierarchyBuilder builder =
                            new GenericDraweeHierarchyBuilder(getResources());
                    builder.setProgressBarImage(R.drawable.loader);

                    builder.setProgressBarImage(
                            new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
                    builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
                    GenericDraweeHierarchy hierarchy = builder
                            .setFadeDuration(100)
                            .build();

                    imageView_editChannelImage.setHierarchy(hierarchy);


                    final ImageRequest imageRequest =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)


                                    .build();
                    imageView_editChannelImage.setImageRequest(imageRequest);

                    //   imgPet.getHierarchy().setRoundingParams(roundingParams);
                    // Call_pet_photo_update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
        /*try {
        InputStream inputStream = null;//You can get an inputStream using any IO API

            inputStream = new FileInputStream(fileName);

        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encodedString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }*/

    }

    public void getUpdateChannels() {

        if (CheckNetworkConnection.isOnline(getContext())) {
            showProgress();
                /*Call<UpdateChannelResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getUpdateChannels(userId, channel_name, channel_description, encoded_string, image_name, channel_id);
                callRegisterUser.enqueue(new ApiCallback<UpdateChannelResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<UpdateChannelResponse> response, boolean isSuccess, String message) {*/
            RestApiMethods requestInterface = RetrofitInterface.getRestApiMethods(getContext());
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface.getUpdateChannels(user_obj.getUser_Id(), channel_name, channel_description, encoded_string, image_name, channel_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<UpdateChannelResponse>() {
                        @Override
                        public void onNext(UpdateChannelResponse genResModel) {
                            // dialog.dismiss();

                            dismissProgress();
                            // UpdateChannelResponse updateChannelResponse = response.body();


                            if (genResModel != null) {

                                setAddChannelCredentials(genResModel);


                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissProgress();
                            Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {
                            dismissProgress();

                        }

                    }));
        }


    }

    private void setAddChannelCredentials(UpdateChannelResponse updateChannelResponse) {

        String message = updateChannelResponse.getMessage();

        if (message.equals("success")) {
          /*  stackClearIntent(EditChannelInfo.this, BrightlyNavigationActivity.class);
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
            showShortToast(getActivity(), ((BrightlyNavigationActivity) getActivity()).CATEGORY_SINGULAR + " " + channel_name + " Updated");
            ((BrightlyNavigationActivity) getActivity()).DontRunOneTime = true;
            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true, 2);
            //       ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.CHANNELS,new ChannelFragment(),false);


        } else {
            showLongToast(getActivity(), message);
        }
    }

    @Override
    public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list) {
        imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "default");
        Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
        if (intent == null) {
            //PermissionUtil.
        } else {
            startActivityForResult(intent, PICK_IMAGE_REQ);
        }
    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }*/
}
