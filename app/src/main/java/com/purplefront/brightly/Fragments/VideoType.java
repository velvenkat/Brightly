package com.purplefront.brightly.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;

import java.net.URI;

import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import com.facebook.imagepipeline.common.ResizeOptions;
import com.purplefront.brightly.API.RestApiMethods;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;

import com.purplefront.brightly.Adapters.Thumbnail_ImageAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.BuildConfig;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.Modules.MultipleImageModel;
import com.purplefront.brightly.Modules.SetEntryModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.ImageChooser_Crop;
import com.purplefront.brightly.Utils.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoType extends BaseFragment implements BrightlyNavigationActivity.PermissionResultInterface {
    CustomMediaController customMediaController;
    View frag_rootView;
    EditText create_cardName;
    EditText create_cardDescription;
    Button btn_createCard;
    //SimpleDraweeView image_createCard;
    ResizeOptions img_resize_opts;
    ImageChooser_Crop imgImageChooser_crop;

    int PICK_VIDEO_REQ = 77;
    ResizeOptions mResizeOptions;
    Context context;
    ImageView img_video_upload;
    RealmModel user_obj;
    Uri crop_result_uri = null;
    Uri videoUri;
    String userId;
    String set_id;
    String set_name;
    String card_name = "";
    String card_description = "";
    /* ArrayList<String> encoded_string;
     ArrayList<String> image_name;*/
    String type = "";
    String Created_by;
    boolean isCreateCard;
    boolean isImageChanging;
    boolean isCameraChoosed;
    // RecyclerView recycler_list_thumbnail;
    SetEntryModel setEntryModelObj;
    int img_count = 1;

    List<MultipleImageModel> list_multi_image;
    Thumbnail_ImageAdapter thumbnail_imageAdapter;
    int Thumbnail_sel_position = 1;
    boolean isChoose_New_image;
    String remove_img_id = "";
    VideoView video_vw;

    ImageView play_pause;
    FrameLayout video_contr;

    public VideoType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag_rootView = inflater.inflate(R.layout.lo_video_type, container, false);
        video_vw = frag_rootView.findViewById(R.id.video_vw);
        play_pause = frag_rootView.findViewById(R.id.play_pause);
        video_contr = frag_rootView.findViewById(R.id.video_contr);

        //video_vw.setZOrderOnTop(false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        context = frag_rootView.getContext();
//        Bundle bundle=getArguments();


        img_video_upload = (ImageView) frag_rootView.findViewById(R.id.img_video_upload);
        create_cardName = (EditText) frag_rootView.findViewById(R.id.create_cardName);
        create_cardDescription = (EditText) frag_rootView.findViewById(R.id.create_cardDescription);
        btn_createCard = (Button) frag_rootView.findViewById(R.id.btn_createCard);

        create_cardName.setHint(((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " name");
        create_cardDescription.setHint(((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR + " description");
        clear_edit_text_focus(create_cardDescription);
        clear_edit_text_focus(create_cardName);

        Bundle bundle = getArguments();
        isCreateCard = bundle.getBoolean("isCreate");
        Created_by = bundle.getString("created_by");
        boolean load_def_img = true;
        setEntryModelObj = bundle.getParcelable("set_entry_obj");

        userId = user_obj.getUser_Id();
        set_id = setEntryModelObj.getSet_id();
        set_name = setEntryModelObj.getSet_name();


        if (!isCreateCard) {
            Uri downloadvideo_Uri = Uri.parse(setEntryModelObj.getCard_multimedia_url());
            set_video_view(downloadvideo_Uri);
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


        video_contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideoIntent();
            }
        });


        img_video_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideoIntent();
            }
        });

        if (!isCreateCard) {
            btn_createCard.setText("UPDATE " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);
            create_cardName.setText(setEntryModelObj.getCard_name());
            create_cardDescription.setText(setEntryModelObj.getCard_description());
            video_vw.setVisibility(View.VISIBLE);
            img_video_upload.setVisibility(View.INVISIBLE);
            if (!Created_by.equalsIgnoreCase(userId)) {

                create_cardName.setEnabled(false);
                create_cardDescription.setEnabled(false);
                btn_createCard.setVisibility(View.INVISIBLE);
            }

        } else {
            video_vw.setVisibility(View.INVISIBLE);

            img_video_upload.setVisibility(View.VISIBLE);
            btn_createCard.setText("CREATE " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);
        }
        btn_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });


        frag_rootView.setFocusableInTouchMode(true);
        frag_rootView.requestFocus();
        frag_rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                }
                return true;
            }
        });
        return frag_rootView;
    }


    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        card_name = create_cardName.getText().toString();
        card_description = create_cardDescription.getText().toString();

        // Check if all strings are null or not
        if (card_name.trim().equals("")) {
            //  || card_description.equals("") || card_description.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), create_cardName,
                    "Card name is required.");
        } else if (isCreateCard && videoUri == null) {
            new CustomToast().Show_Toast(getActivity(), create_cardName,
                    "Choose videos to upload");
        } else {
            uploadFile(videoUri);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQ) {
            if (resultCode == RESULT_OK) {

                // Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",fileImagePath);

                videoUri = getPickImageResultUri(data);
                if (videoUri != null) {

                    set_video_view(null);

                   /* video_vw.setMediaController(new MediaController(getContext()) {
                        @Override
                        public void hide() {
                            show();
                        }


                    });*/
                    /*customMediaController = new CustomMediaController(getContext());
                    //customMediaController.setAnchorView(video_vw);
                    video_vw.setMediaController(customMediaController);

                    // video_vw.setMediaController(mc);


                    video_vw.start();
                    customMediaController.show();
                    //  video_vw.setVideoURI(picUri);
                    customMediaController.setAnchorView(video_vw);*/
                 /*   video_vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                @Override
                                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                    *//*
                     * add media controller
                     *//*
                     *//*   customMediaController = new CustomMediaController(getContext());
                                    video_vw.setMediaController(customMediaController);
                                    *//**//*
                     * and set its position on screen
                     *//**//*
                                    customMediaController.setAnchorView(video_contr);
                                    customMediaController.show();*//*
                                }
                            });
                        }

                    });*/
                }
            }
        }
    }

    private void set_video_view(Uri fileUri) {
        Uri file_uri;
        if (videoUri == null)
            file_uri = fileUri;
        else
            file_uri = videoUri;
        img_video_upload.setVisibility(View.INVISIBLE);
        video_vw.setVisibility(View.VISIBLE);
        //MediaController mc = new MediaController(getContext());


        video_vw.setVideoURI(file_uri);
        video_vw.seekTo(1);
        play_pause.setVisibility(View.VISIBLE);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_vw.isPlaying()) {
                    play_pause.setBackground(getContext().getResources().getDrawable(R.drawable.play_btn));
                    video_vw.pause();
                } else {
                    play_pause.setBackground(getContext().getResources().getDrawable(R.drawable.pause));
                    video_vw.start();
                }
            }
        });
        play_pause.setBackground(getContext().getResources().getDrawable(R.drawable.play_btn));

    }

    @Override
    public void onPause() {
        super.onPause();
        if (customMediaController != null) {
            customMediaController.setVisibility(View.INVISIBLE);
        }

    }


    private class CustomMediaController extends MediaController {

        public CustomMediaController(Context context) {
            super(context);
            show();
        }

        @Override
        public void hide() {
            //super.hide();
            show();
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
                RestApiMethods requestInterface = RetrofitInterface.getRestApiMethods(getContext());
                CompositeDisposable mCompositeDisposable = new CompositeDisposable();
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
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoUri != null) {
            // customMediaController.setVisibility(View.VISIBLE);

            if (!video_vw.isPlaying()) {
                Toast.makeText(getContext(), "Is Not Playing", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser && customMediaController != null)
            customMediaController.setVisibility(View.INVISIBLE);
        if (isVisibleToUser && customMediaController != null) {
            if (videoUri != null) {
                video_vw.setZOrderOnTop(false);
                video_vw.setVideoURI(videoUri);
                video_vw.start();
            }
            video_vw.setVisibility(View.VISIBLE);
            img_video_upload.setVisibility(View.INVISIBLE);
            customMediaController.setVisibility(View.VISIBLE);
            video_vw.setMediaController(customMediaController);
            //customMediaController.setZ(100.0f);
        }
    }

    private void uploadFile(Uri fileUri) {
        Cursor cursor;
        if (videoUri != null) {
            File file = null;
            if (!isCameraChoosed) {
                String filePath;
                String[] filePathColumn = {MediaStore.MediaColumns.DATA};

                cursor = getContext().getContentResolver().query(videoUri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();

                file = new File(filePath);
            } else {

                try {
                    String path = videoUri.toString(); // "/mnt/sdcard/FileName.mp3"
                    file = new File(new URI(path));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);


                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                showProgress();
                Call<AddMessageResponse> responseBodyCall;
                if (isCreateCard)
                    responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).addRecord("video_file", userId, set_id, card_name, card_description, "video_00", multipartBody);
                else
                    responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).updateRecord("video_file", userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, multipartBody);
                responseBodyCall.enqueue(new Callback<AddMessageResponse>() {
                    @Override
                    public void onResponse(Call<AddMessageResponse> call, Response<AddMessageResponse> response) {
                        dismissProgress();
                   /* Log.d("Success", "success " + response.code());
                    Log.d("Success", "success " + response.message());*/
                        AddMessageResponse messageResponse = response.body();
                        if (messageResponse.getMessage().equals("success")) {
                            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                        } else {
                            Toast.makeText(getContext(), "Error:" + messageResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<AddMessageResponse> call, Throwable t) {
                        dismissProgress();
                        Log.d("failure", "message = " + t.getMessage());
                        Log.d("failure", "cause = " + t.getCause());
                    }
                });
            }
        } else {
            if (!isCreateCard) {
                String[] old_filename = setEntryModelObj.getCard_multimedia_url().split("/");
                Call<AddMessageResponse> responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).updateRecord_old("video_file", userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, old_filename[old_filename.length - 1]);
                responseBodyCall.enqueue(new Callback<AddMessageResponse>() {
                    @Override
                    public void onResponse(Call<AddMessageResponse> call, Response<AddMessageResponse> response) {
                        dismissProgress();
                   /* Log.d("Success", "success " + response.code());
                    Log.d("Success", "success " + response.message());*/
                        AddMessageResponse messageResponse = response.body();
                        if (messageResponse.getMessage().equals("success")) {
                            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                        } else {
                            Toast.makeText(getContext(), "Error:" + messageResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<AddMessageResponse> call, Throwable t) {
                        dismissProgress();
                        Log.d("failure", "message = " + t.getMessage());
                        Log.d("failure", "cause = " + t.getCause());
                    }
                });
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
            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
        } else {
            showLongToast(getActivity(), message);
        }
    }

    public void getVideoIntent() {

        if (PermissionUtil.hasPermission(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, getContext(), BrightlyNavigationActivity.PERMISSION_REQ_CODE_IMAGE)) {
            setBottomDialog();
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
        menu_list.add("Camera");
        menu_list.add("Gallery");

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
                                isCameraChoosed = false;
                                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, PICK_VIDEO_REQ);
                                break;
                            case 0:
                                isCameraChoosed = true;
                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                StrictMode.setVmPolicy(builder.build());
                                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCaptureImageOutputUri());


                                startActivityForResult(takeVideoIntent, PICK_VIDEO_REQ);
                                break;

                        }
                    }
                }
        );

    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;

        File video_dir = getContext().getExternalCacheDir();


        if (video_dir != null) {
            //  File file = new File(getImage.getPath(), File_Name);

            outputFileUri = Uri.fromFile(new File(video_dir.getPath(), "video_00.mp4"));
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_VIDEO_CAPTURE);
        }
        if (!isCamera) {
            if (data.getData() != null) {
                return data.getData();
            } else {
                return getCaptureImageOutputUri();
            }
        } else {
            return getCaptureImageOutputUri();
        }

        //return isCamera ? getCaptureImageOutputUri() : data.getData();
    }


    @Override
    public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list) {
        getVideoIntent();

    }
}
