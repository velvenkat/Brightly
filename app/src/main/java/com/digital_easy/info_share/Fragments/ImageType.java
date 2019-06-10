package com.digital_easy.info_share.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
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

import com.digital_easy.info_share.Adapters.Thumbnail_ImageAdapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.CustomToast;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.ChannelListResponse;
import com.digital_easy.info_share.Modules.ContactShare;
import com.digital_easy.info_share.Modules.MultipleImageModel;
import com.digital_easy.info_share.Modules.SetEntryModel;
import com.digital_easy.info_share.Modules.SetListResponse;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.ImageChooser_Crop;
import com.digital_easy.info_share.Utils.Util;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageType extends BaseFragment implements BrightlyNavigationActivity.PermissionResultInterface, Thumbnail_ImageAdapter.Thumbnail_interface {

    View frag_rootView;
    EditText create_cardName;
    EditText create_cardDescription;
    Button btn_createCard;
    SimpleDraweeView image_createCard;
    ResizeOptions img_resize_opts;
    ImageChooser_Crop imgImageChooser_crop;

    int PICK_IMAGE_REQ = 77;
    ResizeOptions mResizeOptions;
    Context context;
    RealmModel user_obj;
    Uri crop_result_uri = null;

    String userId;
    String set_id = "";
    String set_name;
    String card_name = "";
    String email_val = "";
    String office_no = "";
    String mob_no = "";
    String company = "";
    String title = "";
    String card_description = "";
    /* ArrayList<String> encoded_string;
     ArrayList<String> image_name;*/
    String type = "";
    String Created_by;
    boolean isCreateCard;
    boolean isImageChanging;
    RecyclerView recycler_list_thumbnail;
    SetEntryModel setEntryModelObj;
    int img_count = 1;
    Spinner spinner_catg_list, spinner_set_list;

    List<ChannelListModel> channelListModels = new ArrayList<>();
    List<SetsListModel> setListModels = new ArrayList<>();
    List<MultipleImageModel> list_multi_image;
    Thumbnail_ImageAdapter thumbnail_imageAdapter;
    int Thumbnail_sel_position = 1;
    boolean isChoose_New_image;
    String remove_img_id = "";
    boolean isOpenFromGallery;
    ChannelListModel chl_list_obj;
    SetsListModel set_list_obj;
    String channel_id = "";
    EditText edt_comp_name, edt_title, edt_mob_no, edt_off_no, edt_email;
    LinearLayout contact_contr;
    LinearLayout gallery_contr;
    boolean isContactCard;

    public ImageType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag_rootView = inflater.inflate(R.layout.fragment_image_type, container, false);

        recycler_list_thumbnail = (RecyclerView) frag_rootView.findViewById(R.id.list_thumbnail);
        spinner_catg_list = (Spinner) frag_rootView.findViewById(R.id.spinner_catg_list);
        spinner_set_list = (Spinner) frag_rootView.findViewById(R.id.spinner_set_list);
        edt_comp_name = (EditText) frag_rootView.findViewById(R.id.edt_comp_name);
        edt_title = (EditText) frag_rootView.findViewById(R.id.edt_title);
        edt_mob_no = (EditText) frag_rootView.findViewById(R.id.edt_mob_no);
        edt_off_no = (EditText) frag_rootView.findViewById(R.id.edt_off_no);
        edt_email = (EditText) frag_rootView.findViewById(R.id.edt_email);
        contact_contr = (LinearLayout) frag_rootView.findViewById(R.id.contact_contr);
        gallery_contr = (LinearLayout) frag_rootView.findViewById(R.id.gallery_contr);
        LinearLayoutManager L_manager = new LinearLayoutManager(getContext());
        L_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        L_manager.setReverseLayout(true);
        recycler_list_thumbnail.setLayoutManager(L_manager);

        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        context = frag_rootView.getContext();
//        Bundle bundle=getArguments();


        image_createCard = (SimpleDraweeView) frag_rootView.findViewById(R.id.image_createCard);
        create_cardName = (EditText) frag_rootView.findViewById(R.id.create_cardName);
        create_cardDescription = (EditText) frag_rootView.findViewById(R.id.create_cardDescription);
        btn_createCard = (Button) frag_rootView.findViewById(R.id.btn_createCard);


        Bundle bundle = getArguments();
        isCreateCard = bundle.getBoolean("isCreate");
        Created_by = bundle.getString("created_by");
        isOpenFromGallery = bundle.getBoolean("isOpenFromGallery");
        isContactCard = bundle.getBoolean("isContactCard");


        if (isOpenFromGallery) {
            gallery_contr.setVisibility(View.VISIBLE);

            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Create card");
            isCreateCard = true;
            ArrayList<String> image_uri_list = bundle.getStringArrayList("uri_list");
            list_multi_image = new ArrayList<>();
            setSpinnerEmptySet();
            try {
                for (String uriStr : image_uri_list) {
                    MultipleImageModel model = new MultipleImageModel();
                    model.setImg_url(uriStr);
                    list_multi_image.add(model);
                    Uri fileUri = Uri.parse(uriStr);

                    String filePath;
                    String[] filePathColumn = {MediaStore.MediaColumns.DATA};

                    Cursor cursor = getContext().getContentResolver().query(fileUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();

                    ExifInterface exif = new ExifInterface(filePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), fileUri);
                    Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                    model.setImg_encode_string(getStringImage(bmRotated));
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

                    model.setImg_name(sdf.format(new Date()));
                }
            } catch (Exception e) {
                e.getMessage();
            }
            MultipleImageModel model_obj = new MultipleImageModel();
            model_obj.setImg_url("");
            list_multi_image.add(0, model_obj);
            img_count = list_multi_image.size();
            recycler_list_thumbnail.setVisibility(View.VISIBLE);
            thumbnail_imageAdapter = new Thumbnail_ImageAdapter();
            thumbnail_imageAdapter.mListener = this;
            Thumbnail_sel_position = list_multi_image.size() - 1;
            thumbnail_imageAdapter.adapter_Thumbnail_sel_position = Thumbnail_sel_position;
            thumbnail_imageAdapter.multipleImageModelList = list_multi_image;
            recycler_list_thumbnail.setAdapter(thumbnail_imageAdapter);
            MultipleImageModel multipleImageModelOBj = list_multi_image.get(Thumbnail_sel_position);
            set_card_image(multipleImageModelOBj.getImg_url());
            get_my_catg();

            spinner_catg_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (channelListModels != null) {
                        if (position != 0) {
                            chl_list_obj = channelListModels.get(position - 1);
                            channel_id = chl_list_obj.getChannel_id();
                            set_id = "";
                            getSetLists(null);
                        } else {

                            channel_id = "";
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner_set_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (setListModels != null) {
                        if (position != 0) {
                            set_list_obj = setListModels.get(position - 1);
                            set_id = set_list_obj.getSet_id();
                            // getSetLists(null);
                        } else {

                            set_id = "";
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } else {
            gallery_contr.setVisibility(View.GONE);
        }


        boolean load_def_img = true;
        setEntryModelObj = bundle.getParcelable("set_entry_obj");

        userId = user_obj.getUser_Id();
        if (setEntryModelObj != null) {
            set_id = setEntryModelObj.getSet_id();
            set_name = setEntryModelObj.getSet_name();
        } else {

        }
        create_cardName.setHint(((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " name");
        create_cardDescription.setHint(((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " description");
        clear_edit_text_focus(create_cardDescription);
        clear_edit_text_focus(create_cardName);
        if (isContactCard) {
            contact_contr.setVisibility(View.VISIBLE);
            create_cardName.setHint("Name");
            create_cardDescription.setHint("Notes");
        }

        frag_rootView.addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(
                            View view,
                            int left,
                            int top,
                            int right,
                            int bottom,
                            int oldLeft,
                            int oldTop,
                            int oldRight,
                            int oldBottom) {
                        final int imageSize = ((right - 10) - (left - 10));
                        mResizeOptions = new ResizeOptions(imageSize, imageSize);
                    }
                });

        ((BrightlyNavigationActivity) getActivity()).permissionResultInterfaceObj = this;

        if (!isCreateCard) {
            btn_createCard.setText("UPDATE " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);
            if (isContactCard) {
                create_cardName.setText(setEntryModelObj.getContact_name());
                edt_mob_no.setText(setEntryModelObj.getContact_cell_phone());
                edt_off_no.setText(setEntryModelObj.getContact_office_phone());
                edt_email.setText(setEntryModelObj.getContact_email());
                edt_title.setText(setEntryModelObj.getCard_name());
                edt_comp_name.setText(setEntryModelObj.getContact_company());
            } else
                create_cardName.setText(setEntryModelObj.getCard_name());
            create_cardDescription.setText(setEntryModelObj.getCard_description());

            if (!Created_by.equalsIgnoreCase(userId)) {
                image_createCard.setClickable(false);
                create_cardName.setEnabled(false);
                create_cardDescription.setEnabled(false);
                btn_createCard.setVisibility(View.GONE);
            }
            if (!setEntryModelObj.getType().equalsIgnoreCase("text")) {
                if (list_multi_image == null)
                    list_multi_image = setEntryModelObj.getMultipleImageModelList();
                if (list_multi_image != null && thumbnail_imageAdapter == null) {

                    MultipleImageModel model_obj = new MultipleImageModel();
                    model_obj.setImg_url("");
                    list_multi_image.add(0, model_obj);
                    img_count = list_multi_image.size();
                    recycler_list_thumbnail.setVisibility(View.VISIBLE);
                    thumbnail_imageAdapter = new Thumbnail_ImageAdapter();
                    thumbnail_imageAdapter.mListener = this;
                    Thumbnail_sel_position = list_multi_image.size() - 1;
                    thumbnail_imageAdapter.adapter_Thumbnail_sel_position = Thumbnail_sel_position;
                    thumbnail_imageAdapter.multipleImageModelList = list_multi_image;
                    recycler_list_thumbnail.setAdapter(thumbnail_imageAdapter);

                }
                load_def_img = false;

               /* Glide.with(getActivity())
                        .load(setEntryModelObj.getCard_multimedia_url())
                        .centerCrop()
                        *//*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*//*
                        .into(image_createCard);*/
                MultipleImageModel last_image_model_obj = list_multi_image.get(list_multi_image.size() - 1);

                set_image(last_image_model_obj.getImg_url());
               /* RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //  layoutParams.setMargins(0,0,0,0);
                image_createCard.setLayoutParams(layoutParams);*/

                //   image_createCard.setScaleType(ImageView.ScaleType.FIT_XY);


                /*final ImageRequest imageRequest =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(userModule.getCard_multimedia_url()))
                                .setResizeOptions(mResizeOptions)
                                .build();
                image_createCard.setImageRequest(imageRequest);*/

            } else {

                load_def_img = true;

            }
        } else {
            btn_createCard.setText("CREATE " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);
        }
        if (load_def_img) {
           /* Glide.with(getActivity())
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    *//*.transform(new CircleTransform(HomeAct/ivity.this))
                    .override(50, 50)*//*
                    .into(image_createCard);*/
            //  image_createCard.setImageResource(R.drawable.no_image_available);

            if (!isOpenFromGallery) {


                set_image(((BrightlyNavigationActivity) getActivity()).CARD_CREATE_IMAGE);
            }
            /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            //  layoutParams.setMargins(0,0,0,0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);*/
            //  image_createCard.setLayoutParams(layoutParams);


            // image_createCard.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        btn_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        image_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (img_count != 1) {
                    // isImageChanging=true;
                    setBottomDialog();
                } else
                    imageChooserIntent();

            }
        });

        frag_rootView.setFocusableInTouchMode(true);
        frag_rootView.requestFocus();
        frag_rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    if (isOpenFromGallery) {


                        Fragment fragment = new ViewPager_Category();
                        // fragment.setArguments(bundle);
                        ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.CHANNELS, fragment, false);
                    } else
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                }
                return true;
            }
        });
        return frag_rootView;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void setSpinnerEmptySet() {
        ArrayList<String> set_name = new ArrayList<>();
        //spinner_set_list.removeAllViews();
        spinner_set_list.setAdapter(null);

        set_name.clear();
        set_name.add("Empty");
        spinner_set_list.setEnabled(false);
        for (SetsListModel model : setListModels) {
            set_name.add(model.getSet_name());
        }
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, set_name);
        spinner_set_list.setAdapter(adapter);

    }

    private void get_my_catg() {

        try {

            if (CheckNetworkConnection.isOnline(getContext())) {

                Call<ChannelListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getMyChannelsList(user_obj.getUser_Id(), "my");
                callRegisterUser.enqueue(new ApiCallback<ChannelListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<ChannelListResponse> response, boolean isSuccess, String message) {
                        ChannelListResponse channelListResponse = response.body();

                        if (isSuccess) {

                            if (channelListResponse != null && channelListResponse.getChannels() != null && channelListResponse.getChannels().size() != 0) {
                                //  swipeRefresh.setRefreshing(true);
                                channelListModels.clear();
                                channelListModels = channelListResponse.getChannels();
                                ArrayList<String> catg_name = new ArrayList<>();
                                catg_name.add("Select");
                                for (ChannelListModel model : channelListModels) {
                                    catg_name.add(model.getChannel_name());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, catg_name);
                                spinner_catg_list.setAdapter(adapter);

                            } else {
                                // swipeRefresh.setRefreshing(false);

                            }

                        } else {
                            showLongToast(getActivity(), message);

                            //     swipeRefresh.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        // swipeRefresh.setRefreshing(true);
                    }
                });
            } else {
              /*  else
                    dismissProgress();*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            //swipeRefresh.setRefreshing(true);
        }


    }

    public void getSetLists(String filter_keys) {
        // String channel_id = null;
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

                                setListModels = setListResponse.getSets();
                                if (setListModels != null && setListModels.size() > 0) {
                                    ArrayList<String> set_name = new ArrayList<>();
                                    set_name.add("Select");
                                    for (SetsListModel model : setListModels) {
                                        set_name.add(model.getSet_name());
                                    }
                                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, set_name);
                                    spinner_set_list.setAdapter(adapter);
                                    spinner_set_list.setEnabled(true);
                                }
                            } else {
                                setSpinnerEmptySet();
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

    public void set_image(String URL) {
        String img_url = URL;
        ResizeOptions resizeOptions = new ResizeOptions(350, 250);
        if (img_url != null && !img_url.trim().equals("")) {
            /*GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(getContext().getResources());
            builder.setProgressBarImage(R.drawable.loader);

            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            image_createCard.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img_url))
                            .setResizeOptions(resizeOptions)

                            .build();
            image_createCard.setImageRequest(imageRequest);
*/
            set_card_image(img_url);
        } else {
           /* Glide.with(getContext())
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    *//*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*//*
                    .into(image_createCard);*/

            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.no_image_available))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(mResizeOptions)
                            .build();
            image_createCard.setImageRequest(imageRequest2);
        }

    }

    public void imageChooserIntent() {

        //if (PermissionUtil.hasPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, getContext(), BrightlyNavigationActivity.PERMISSION_REQ_CODE_IMAGE)) {
        imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "Img_0001" + img_count);
        Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
        if (intent == null) {
            //PermissionUtil.
        } else {
            startActivityForResult(intent, PICK_IMAGE_REQ);
        }
        //}
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
                                img_count = img_count - 1;

                                if (img_count == 1 && !isContactCard)
                                //encoded_string = new ArrayList<>();
                                {
                                    list_multi_image = new ArrayList<>();
                                    recycler_list_thumbnail.setVisibility(View.GONE);
                                } else {
                                    MultipleImageModel multipleImageModel = list_multi_image.get(Thumbnail_sel_position);
                                    if (!multipleImageModel.getImg_id().equals("")) {
                                        if (!remove_img_id.equals(""))
                                            remove_img_id = remove_img_id + ",";
                                        remove_img_id = remove_img_id + multipleImageModel.getImg_id();
                                    }
                                    list_multi_image.remove(Thumbnail_sel_position);
                                    thumbnail_imageAdapter.multipleImageModelList = list_multi_image;

                                    if (Thumbnail_sel_position == list_multi_image.size())
                                        Thumbnail_sel_position--;
                                    thumbnail_imageAdapter.adapter_Thumbnail_sel_position = Thumbnail_sel_position;
                                    thumbnail_imageAdapter.notifyDataSetChanged();

                                }

                                if (img_count == 1) {
                                   /* Glide.with(getActivity())
                                            .load(R.drawable.no_image_available)
                                            .centerCrop()
                                            *//*.transform(new CircleTransform(HomeActivity.this))
                                            .override(50, 50)*//*
                                            .into(image_createCard);*/

                                    Uri uri = new Uri.Builder()
                                            .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                                            .path(String.valueOf(R.drawable.no_image_available))
                                            .build();
                                    final ImageRequest imageRequest2 =
                                            ImageRequestBuilder.newBuilderWithSource(uri)

                                                    .build();
                                    image_createCard.setImageRequest(imageRequest2);

                                } else {
                                    MultipleImageModel multipleImageModelOBj = list_multi_image.get(Thumbnail_sel_position);
                                    /*Glide.with(getActivity())
                                            .load(multipleImageModelOBj.getImg_url())
                                            .fitCenter()
                                            *//*.transform(new CircleTransform(HomeActivity.this))
                                            .override(50, 50)*//*
                                            .into(image_createCard);*/
                                    set_card_image(multipleImageModelOBj.getImg_url());
                                }
                                break;
                            case 0:
                                imageChooserIntent();
                                isChoose_New_image = true;
                                break;

                        }
                    }
                }
        );

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        card_name = create_cardName.getText().toString().trim();
        Matcher m = null;
        card_description = create_cardDescription.getText().toString().trim();
        if (isContactCard) {
            email_val = edt_email.getText().toString().trim();
            office_no = edt_off_no.getText().toString().trim();
            mob_no = edt_mob_no.getText().toString().trim();
            company = edt_comp_name.getText().toString().trim();
            title = edt_title.getText().toString().trim();
            Pattern p = Pattern.compile(Util.regEx);
            m = p.matcher(email_val);
        }
        // Check if all strings are null or not
        if (card_name.trim().equals("")) {
            //  || card_description.equals("") || card_description.length() == 0) {
            if (!isContactCard)
                new CustomToast().Show_Toast(getActivity(), create_cardName,
                        "Card name is required.");
            else
                new CustomToast().Show_Toast(getActivity(), create_cardName,
                        "Name is required.");
        } else if (isOpenFromGallery && channel_id.trim().equals("")) {
            new CustomToast().Show_Toast(getActivity(), create_cardName,
                    "Please choose one category");
        } else if (isOpenFromGallery && set_id.trim().equals("")) {
            new CustomToast().Show_Toast(getActivity(), create_cardName,
                    "Please choose one set");
        }
        // Pattern match for email id

        else if (isContactCard && (!email_val.trim().equals("") && !m.find())) {
            new CustomToast().Show_Toast(getActivity(), edt_email,
                    "Your Email Id is Invalid.");
        } else if (img_count == 1) {
           /* new CustomToast().Show_Toast(getActivity(), image_createCard,
                    "Image is required.");*/
            if (!isContactCard) {
                if (!isCreateCard && setEntryModelObj.getType().equals("multiple_images")) {

                    type = "text";
                    getAddCards("image_to_text", "", "");
                } else {
                    type = "text";

                    getAddCards("", "", "");
                }
            } else {
                type = "contact";
                getAddCards("", "", remove_img_id);
            }
        }

        // Else do signup or do your stuff

        else {
            if (!isContactCard)
                type = "multiple_images";
            else
                type = "contact";
            String str_encoded = "", img_name = "";
            int temp_count = 0;
            for (MultipleImageModel multipleImageModel : list_multi_image) {
                if (temp_count == 1) {
                    if (!isCreateCard && (setEntryModelObj.getType().equals("multiple_images") || isContactCard) && multipleImageModel.getImg_id() != null && !multipleImageModel.getImg_id().equals("")) {
                       /* if (!remove_img_id.equals(""))
                            remove_img_id = remove_img_id + ",";
                        remove_img_id = remove_img_id + multipleImageModel.getImg_id();*/
                    } else {
                        if (!str_encoded.equals("")) {
                            str_encoded = str_encoded + ",";
                            img_name = img_name + ",";
                        }

                        str_encoded = str_encoded + multipleImageModel.getImg_encode_string();
                        img_name = img_name + multipleImageModel.getImg_name();
                    }
                } else
                    temp_count = 1;
            }
            getAddCards(str_encoded, img_name, remove_img_id);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri picUri;
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

                    picUri = imgImageChooser_crop.getPickImageResultUri(data);
                    //  compressImage(picUri.toString());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setCropMenuCropButtonTitle("Done")
                                .setMinCropResultSize(250, 250)

//                                .setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .start(context, this);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {


                    crop_result_uri = result.getUri();

                   /* if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgPet.setImageDrawable(drawable);
                    }*/
                    /*final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)
                                    .setResizeOptions(mResizeOptions)
                                    .build();
                    image_createCard.setImageRequest(imageRequest2);*/

                 /*   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //     layoutParams.setMargins(0,0,0,0);
                    image_createCard.setLayoutParams(layoutParams);*/
                    //   image_createCard.setScaleType(ImageView.ScaleType.FIT_XY);
                   /* final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)
                                    .setResizeOptions(mResizeOptions)
                                    .build();
                    image_createCard.setImageRequest(imageRequest2);*/
                    /*Glide.with(getActivity())
                            .load(crop_result_uri)
                            .fitCenter()
                            *//*.transform(new CircleTransform(HomeActivity.this))
                            .override(50, 50)*//*
                            .into(image_createCard);
*/
                    compressImage(crop_result_uri.toString());
                    // set_card_image(getFilename());
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

    private void getAddCards(String encoded_str, String img_name_str, String remove_img_id) {


        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++) {
                File inside_file = new File(file, children[i]);
                if (inside_file.exists()) {
                    inside_file.delete();
                }
                // new File(file, children[i]).delete();
            }
        }

        if (CheckNetworkConnection.isOnline(getActivity())) {
            showProgress();
                /*Call<AddMessageResponse> callRegisterUser;
                if (isCreateCard) {
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getAddCardsList(type, userId, set_id, card_name, card_description, encoded_string, image_name);
                } else
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getUpdateCardsList(type, userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, encoded_string, image_name);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {*/
            if (isCreateCard) {
                RestApiMethods requestInterface = RetrofitInterface.getRestApiMethods(getContext());
                CompositeDisposable mCompositeDisposable = new CompositeDisposable();
                if (!isContactCard) {
                    mCompositeDisposable.add(requestInterface.getAddCardsList(type, userId, set_id, card_name, card_description, encoded_str, img_name_str)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(new DisposableObserver<AddMessageResponse>() {
                                @Override
                                public void onNext(AddMessageResponse genResModel) {

                                    dismissProgress();

                                    if (genResModel != null) {

                                        setAddSetCredentials(genResModel);


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
                } else {
                    mCompositeDisposable.add(requestInterface.getAddContacts(type, userId, set_id, card_name, card_description, encoded_str, img_name_str, title, mob_no, email_val, company, office_no)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(new DisposableObserver<AddMessageResponse>() {
                                @Override
                                public void onNext(AddMessageResponse genResModel) {

                                    dismissProgress();

                                    if (genResModel != null) {

                                        setAddSetCredentials(genResModel);


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
            } else {
                RestApiMethods requestInterface = RetrofitInterface.getRestApiMethods(getContext());
                CompositeDisposable mCompositeDisposable = new CompositeDisposable();
                if (!isContactCard) {
                    mCompositeDisposable.add(requestInterface.getUpdateCardsList(type, userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, encoded_str, img_name_str, remove_img_id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(new DisposableObserver<AddMessageResponse>() {
                                @Override
                                public void onNext(AddMessageResponse genResModel) {

                                    dismissProgress();

                                    if (genResModel != null) {

                                        setAddSetCredentials(genResModel);


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
                } else {
                    mCompositeDisposable.add(requestInterface.getUpdateContacts(type, userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, encoded_str, img_name_str, title, mob_no, email_val, company, office_no, remove_img_id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(new DisposableObserver<AddMessageResponse>() {
                                @Override
                                public void onNext(AddMessageResponse genResModel) {

                                    dismissProgress();

                                    if (genResModel != null) {

                                        setAddSetCredentials(genResModel);


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
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (crop_result_uri != null) {
/*
            Glide.with(getActivity())
                    .load(crop_result_uri)
                    .fitCenter()
                    */
/*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*//*

                    .into(image_createCard);
*/
            set_card_image(crop_result_uri.toString());
        }
        if (img_count > 1) {
            if (thumbnail_imageAdapter == null) {
                if (list_multi_image != null) {

                    MultipleImageModel model_obj = new MultipleImageModel();
                    model_obj.setImg_url("");
                    list_multi_image.add(0, model_obj);
                    img_count = list_multi_image.size();
                    recycler_list_thumbnail.setVisibility(View.VISIBLE);
                    thumbnail_imageAdapter = new Thumbnail_ImageAdapter();
                    thumbnail_imageAdapter.mListener = this;
                    Thumbnail_sel_position = list_multi_image.size() - 1;
                    thumbnail_imageAdapter.adapter_Thumbnail_sel_position = Thumbnail_sel_position;
                    thumbnail_imageAdapter.multipleImageModelList = list_multi_image;
                    recycler_list_thumbnail.setAdapter(thumbnail_imageAdapter);
                }

                recycler_list_thumbnail.setVisibility(View.VISIBLE);
                recycler_list_thumbnail.setAdapter(thumbnail_imageAdapter);
            } else {
                recycler_list_thumbnail.setVisibility(View.VISIBLE);
                recycler_list_thumbnail.setAdapter(thumbnail_imageAdapter);
            }
        }
    }


    private void setAddSetCredentials(AddMessageResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {
           /* Intent intent = new Intent(getActivity(), MySetCards.class);
            intent.putExtra("set_id", set_id);
            intent.putExtra("set_name", set_name);
            intent.putExtra("userId", userId);*/
            /*getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
           *//* getActivity().finish();
            startActivity(intent);*//*
            getActivity().overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
            //Fragment fragment=new CardDetailFragment();
            if (isCreateCard) {
                showShortToast(getActivity(), ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " " + card_name + " has been Created.");

            } else {
                showShortToast(getActivity(), ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " " + card_name + " has been Updated.");
            }
            if (isOpenFromGallery) {


                Fragment fragment = new ViewPager_Category();
                // fragment.setArguments(bundle);
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.CHANNELS, fragment, false);
            } else
                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
        } else {
            showLongToast(getActivity(), message);
        }
    }


    @Override
    public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list) {
        imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "Img_0001" + img_count);
        Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
        if (intent == null) {
            //PermissionUtil.
        } else {
            startActivityForResult(intent, PICK_IMAGE_REQ);
        }
    }

    @Override
    public void Add_Thumbnail() {
        isChoose_New_image = false;
        if (img_count <= 10) {
          /*  imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "Img_0001" + img_count);
            Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
            if (intent == null) {
                //PermissionUtil.
            } else {
                startActivityForResult(intent, PICK_IMAGE_REQ);
            }*/
            imageChooserIntent();
        }
    }

    @Override
    public void onSelect(int Position, MultipleImageModel sel_multi_model) {
        // image_createCard.
        if (Position >= 1)
            Thumbnail_sel_position = Position;
        set_card_image(sel_multi_model.getImg_url());
        thumbnail_imageAdapter.adapter_Thumbnail_sel_position = Thumbnail_sel_position;
        thumbnail_imageAdapter.notifyDataSetChanged();

    }

    public void set_card_image(String URI_value) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        builder.setProgressBarImage(R.drawable.loader);

        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        image_createCard.setHierarchy(hierarchy);


        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(URI_value))
                        .setResizeOptions(mResizeOptions)

                        .build();
        image_createCard.setImageRequest(imageRequest);
    }

    /**
     * COMPRESS IMAGE
     **/

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            set_card_image(filename);
            if (img_count == 1) {

                thumbnail_imageAdapter = new Thumbnail_ImageAdapter();
                thumbnail_imageAdapter.mListener = this;
                recycler_list_thumbnail.setVisibility(View.VISIBLE);
                list_multi_image = new ArrayList<>();
                MultipleImageModel model_obj = new MultipleImageModel();
                model_obj.setImg_url("");


                list_multi_image.add(model_obj);
                thumbnail_imageAdapter.multipleImageModelList = list_multi_image;
                recycler_list_thumbnail.setAdapter(thumbnail_imageAdapter);

            }
            MultipleImageModel model_Obj = new MultipleImageModel();
            Uri file_compressed_uri = Uri.fromFile(new File(filename));


            model_Obj.setImg_url(file_compressed_uri.toString());
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), file_compressed_uri);
            model_Obj.setImg_encode_string(getStringImage(bitmap));
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            model_Obj.setImg_name(sdf.format(new Date()));
            if (isChoose_New_image) {
                isChoose_New_image = false;
                // list_multi_image.remove(Thumbnail_sel_position);
                list_multi_image.set(Thumbnail_sel_position, model_Obj);

                thumbnail_imageAdapter.multipleImageModelList.set(Thumbnail_sel_position, model_Obj);
                thumbnail_imageAdapter.adapter_Thumbnail_sel_position = Thumbnail_sel_position;


                thumbnail_imageAdapter.notifyItemChanged(Thumbnail_sel_position);
            } else {

                list_multi_image.add(model_Obj);
                Thumbnail_sel_position = list_multi_image.size() - 1;
                img_count = img_count + 1;
                thumbnail_imageAdapter.multipleImageModelList = list_multi_image;
                thumbnail_imageAdapter.adapter_Thumbnail_sel_position = Thumbnail_sel_position;
                thumbnail_imageAdapter.notifyDataSetChanged();

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        } else {

        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}
