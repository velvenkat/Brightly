package com.digital_easy.info_share.Fragments;

import android.Manifest;
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
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digital_easy.info_share.API.RestApiMethods;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.Blog_Adapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Custom.AdapterMultipleViewCreation;
import com.digital_easy.info_share.Custom.SiriWaveView;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.BlogResponseModel;
import com.digital_easy.info_share.Modules.CardsListModel;
import com.digital_easy.info_share.Modules.ContactHelper;
import com.digital_easy.info_share.Modules.ContactHelperModule;
import com.digital_easy.info_share.Modules.JSONObjectReqModule;
import com.digital_easy.info_share.Modules.JSONReqMVModule;
import com.digital_easy.info_share.Modules.MultipleViewCreationModel;
import com.digital_easy.info_share.Modules.MultipleImageModel;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.Modules.TemplateModule;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.ImageChooser_Crop;
import com.digital_easy.info_share.Utils.PermissionUtil;
import com.digital_easy.info_share.Utils.TimeFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


public class CreateMultimediaCard extends BaseFragment implements AdapterMultipleViewCreation.MultiviewAdapterListener {

    CountDownTimer countDownTimer = null;
    View rootView;
    RecyclerView ll_contr_multi_vw_creation;
    //List<T> jsonReqMVModulesListObj=new ArrayList<T>();
    // static int multiview_sel_Adapter_Pos;
    int AudioFileReq = 100;
    int PICK_IMAGE_REQ = 101;
    int PICK_VIDEO_REQ = 104;
    boolean isRecordAudio = false;
    boolean isCameraChoosed = false;
    int temp_Thumbnail_sel_position;
    List<MultipleImageModel> list_multi_image = new ArrayList<>();
    Uri picUri, crop_result_uri;
    boolean isCreateCard;
    MultipleViewCreationModel sel_multiview_creation_model_obj;
    //int Thumbnail_sel_position;
    JSONObjectReqModule JsonReqObj;
    Map<String, String> contact_helpr_map = new HashMap<>();

    String Compressed_filename;

    MediaRecorder myAudioRecorder;


    public static enum enum_MultipleViewType {
        _ADD, TITLE, IMAGE, AUDIO, VIDEO, UTUBE, WEBLINK, CONTACT, TEXT;

    }

    RelativeLayout fab_contr;

    AdapterMultipleViewCreation adapterMultipleViewCreation;

    private RapidFloatingActionLayout rfaLayout;

    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    RealmModel user_obj;
    File tempMp3File = null;
    public static ResizeOptions mResizeOptions;
    //  List<MultipleViewCreationModel> multipleViewCreationModelList;
    LinearLayout audio_rec_contr;
    ImageView img_start_rec;
    SiriWaveView waveView;
    TextView txtRecSeconds;
    ImageView siriwave_snaps;
    ImageChooser_Crop imgImageChooser_crop;
    //boolean isCapture_video = false;

    boolean isChoose_New_image;
    SetsListModel setsListModelObj;

    CardsListModel cardListModelObj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.vw_multi_creation_oncard, container, false);

        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        audio_rec_contr = rootView.findViewById(R.id.audio_rec_contr);
        JsonReqObj = new JSONObjectReqModule();

        setHasOptionsMenu(true);
        img_start_rec = rootView.findViewById(R.id.img_play_stop_rec);
        Bundle bundle = getArguments();
        setsListModelObj = bundle.getParcelable("setsListModel");
        cardListModelObj = bundle.getParcelable("Card_Dtls");


        isCreateCard = bundle.getBoolean("isCreateCard");

        siriwave_snaps = (ImageView) rootView.findViewById(R.id.cur_default);
        LinearLayoutManager L_manager = new LinearLayoutManager(getContext());

        waveView = (SiriWaveView) rootView.findViewById(R.id.siriWaveView);
        waveView.stopAnimation();
        ll_contr_multi_vw_creation = rootView.findViewById(R.id.ll_multi_create);
        txtRecSeconds = (TextView) rootView.findViewById(R.id.txtRecSeconds);
        ll_contr_multi_vw_creation.setLayoutManager(L_manager);
        MultipleViewCreationModel model = new MultipleViewCreationModel();
        model.TYPE = enum_MultipleViewType._ADD.ordinal();
        ArrayList<MultipleViewCreationModel> multipleViewCreationModelList = new ArrayList<>();
        multipleViewCreationModelList.add(model);

        if (!isCreateCard && cardListModelObj.getData_obj() != null) {
            List<BlogResponseModel> blogResponseModelsObj = cardListModelObj.getData_obj();
            List<String> str_contactType_rept_search = new ArrayList<>();
            boolean isContactRepeated = true;
            MultipleViewCreationModel contactMultipleViewCreationModelObj = null;
            for (BlogResponseModel blogResponseModel : blogResponseModelsObj) {


                if (blogResponseModel.getType() != null && blogResponseModel.getType() != "") {
                    if (!isContactRepeated) {
                        MultipleViewCreationModel addMultipleViewCreationModel = new MultipleViewCreationModel();
                        model.TYPE = enum_MultipleViewType._ADD.ordinal();
                        multipleViewCreationModelList.add(addMultipleViewCreationModel);

                    }
                    MultipleViewCreationModel multipleViewCreationModelObj = new MultipleViewCreationModel();
                    multipleViewCreationModelObj.txt_Val = blogResponseModel.getValue();
                    switch (blogResponseModel.getType()) {
                        case "img":

                            // Inflate the custom layout
                            isContactRepeated = true;
                            list_multi_image = new ArrayList<>();
                            MultipleImageModel addimg_model_obj = new MultipleImageModel();
                            addimg_model_obj.setImg_url("");
                            list_multi_image.add(addimg_model_obj);
                            MultipleImageModel img_model_obj = new MultipleImageModel();
                            img_model_obj.setImg_url(blogResponseModel.getValue());
                            list_multi_image.add(img_model_obj);
                            multipleViewCreationModelObj.TYPE = enum_MultipleViewType.IMAGE.ordinal();
                            multipleViewCreationModelObj.Thumbnail_sel_pos = 1;
                            multipleViewCreationModelObj.multipleImageModelListObj = list_multi_image;
                            break;
                        case "title":
                            isContactRepeated = true;
                            multipleViewCreationModelObj.TYPE = enum_MultipleViewType.TITLE.ordinal();
                            break;

                        case "audio":
                            isContactRepeated = true;
                            multipleViewCreationModelObj.TYPE = enum_MultipleViewType.AUDIO.ordinal();
                            multipleViewCreationModelObj.file_path = blogResponseModel.getValue();
                            break;
                        case "video":
                            isContactRepeated = true;
                            multipleViewCreationModelObj.Video_file_uri = Uri.parse(blogResponseModel.getValue());
                            multipleViewCreationModelObj.TYPE = enum_MultipleViewType.VIDEO.ordinal();
                            break;
                        case "weblink":
                            isContactRepeated = true;

                            multipleViewCreationModelObj.TYPE = enum_MultipleViewType.WEBLINK.ordinal();
                            break;
                        case "youtube":
                            isContactRepeated = true;

                            multipleViewCreationModelObj.TYPE = enum_MultipleViewType.UTUBE.ordinal();
                            break;
                        case "text":
                            isContactRepeated = true;

                            multipleViewCreationModelObj.TYPE = enum_MultipleViewType.TEXT.ordinal();
                            break;


                    }
                    multipleViewCreationModelList.add(multipleViewCreationModelObj);
                    MultipleViewCreationModel addMultipleViewCreationModel = new MultipleViewCreationModel();
                    model.TYPE = enum_MultipleViewType._ADD.ordinal();
                    multipleViewCreationModelList.add(addMultipleViewCreationModel);


                } else {
                    if (blogResponseModel.getContactHelperModule() != null) {
                        if (!isContactRepeated && str_contactType_rept_search.size() != 0) {
                            for (String TYPE : str_contactType_rept_search) {
                                if (TYPE.equalsIgnoreCase(blogResponseModel.getContactHelperModule().type)) {
                                    isContactRepeated = true;
                                    break;
                                }
                            }
                        }
                        if (isContactRepeated) {
                            contactMultipleViewCreationModelObj = new MultipleViewCreationModel();
                            multipleViewCreationModelList.add(contactMultipleViewCreationModelObj);
                            contactMultipleViewCreationModelObj.TYPE = enum_MultipleViewType.CONTACT.ordinal();
                            isContactRepeated = false;
                            str_contactType_rept_search = new ArrayList<>();
                        }
                        switch (blogResponseModel.getContactHelperModule().type) {
                            case "1":
                                contactMultipleViewCreationModelObj.name_val = blogResponseModel.getContactHelperModule().values;
                                str_contactType_rept_search.add("1");
                                break;
                            case "2":
                                contactMultipleViewCreationModelObj.comp_val = blogResponseModel.getContactHelperModule().values;
                                str_contactType_rept_search.add("2");
                                break;
                            case "3":
                                contactMultipleViewCreationModelObj.title_val = blogResponseModel.getContactHelperModule().values;
                                str_contactType_rept_search.add("3");
                                break;
                            case "4":
                                contactMultipleViewCreationModelObj.mob__val = blogResponseModel.getContactHelperModule().values;
                                str_contactType_rept_search.add("4");
                                break;
                            case "5":
                                contactMultipleViewCreationModelObj.off_val = blogResponseModel.getContactHelperModule().values;
                                str_contactType_rept_search.add("5");
                                break;
                            case "6":
                                contactMultipleViewCreationModelObj.email_val = blogResponseModel.getContactHelperModule().values;
                                str_contactType_rept_search.add("6");
                                break;
                            case "7":
                                contactMultipleViewCreationModelObj.notes_val = blogResponseModel.getContactHelperModule().values;
                                str_contactType_rept_search.add("7");
                                break;
                        }

                    }
                }
            }
            if (!isContactRepeated) {
                MultipleViewCreationModel addMultipleViewCreationModel = new MultipleViewCreationModel();
                model.TYPE = enum_MultipleViewType._ADD.ordinal();
                multipleViewCreationModelList.add(addMultipleViewCreationModel);

            }

        }
        img_start_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlayBtnClicked) {

                    if (PermissionUtil.hasPermission(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, getContext(), BrightlyNavigationActivity.PERMISSION_REQ_CODE_AUDIO)) {
                        checkPermissionsAndStart();
                    }

                } else {

                    isPlayBtnClicked = false;
                    waveView.stopAnimation();
                    Rec_finish();

                }
            }
        });

        adapterMultipleViewCreation = new AdapterMultipleViewCreation((AppCompatActivity) getActivity(), multipleViewCreationModelList, this, isCreateCard);
        ll_contr_multi_vw_creation.setAdapter(adapterMultipleViewCreation);

        rootView.addOnLayoutChangeListener(
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
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    if ((audio_rec_contr.getVisibility() == View.VISIBLE)) {

                        audio_rec_contr.setVisibility(View.GONE);

                        ll_contr_multi_vw_creation.setVisibility(View.VISIBLE);
                        if (isPlayBtnClicked) {
                            myAudioRecorder.stop();
                            myAudioRecorder.release();
                        }
                        tempMp3File = null;
                        isPlayBtnClicked = false;
                        isRecordAudio = false;
                        getActivity().invalidateOptionsMenu();
                        return false;
                    } else {
                        release_media();
/*
                        getActivity().setResult(Activity.RESULT_CANCELED);
                        getActivity().finish();
*/
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    }
                }
                return true;
            }
        });


        /*ll_contr_multi_vw_creation.addView(new CustomFAB(getContext(), this));
        ll_contr_multi_vw_creation.addView(new CustomFAB(getContext(), this));*/
//        ll_contr_multi_vw_creation.addView(View_Inflater_helper(R.layout.lo_btn_add_to_create, _ADD));
        //View_Inflater_helper(R.layout.lo_btn_add_to_create, _ADD);
        return rootView;
    }


    private void checkPermissionsAndStart() {


        try {

            tempMp3File = File.createTempFile("audio_bark", ".3gp", getActivity().getCacheDir());

            // prints absolute path
            //  System.out.println("File path: " + f.getAbsolutePath());

            // deletes file when the virtual machine terminate
            tempMp3File.deleteOnExit();


        } catch (Exception e) {

            e.printStackTrace();
        }


        audio_rec();
        isPlayBtnClicked = true;
        img_start_rec.setImageResource(R.drawable.stop_rec);
        final int totalSeconds = 31;
        countDownTimer = new CountDownTimer(totalSeconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                txtRecSeconds.setText(TimeFormat.formateMilliSeccond((totalSeconds * 1000 - millisUntilFinished)));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                //  mTextField.setText("done!");
                Rec_finish();
            }

        }.start();

    }

    public void audio_rec() {
        isRecordAudio = true;

        myAudioRecorder = new MediaRecorder();
        siriwave_snaps.setVisibility(View.GONE);
        waveView.startAnimation();
        try {
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(tempMp3File.getAbsolutePath());
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Rec_finish() {
        isRecordAudio = false;
        getActivity().invalidateOptionsMenu();
        List<MultipleViewCreationModel> multipleViewCreationModelList;
        multipleViewCreationModelList = adapterMultipleViewCreation.multipleViewCreationModelList;
        try {


            countDownTimer.cancel();
            if (myAudioRecorder != null) {

                myAudioRecorder.stop();
                myAudioRecorder.release();


        /*    Bundle bundle = new Bundle();
            bundle.putString("filePath", tempMp3File.getAbsolutePath());
            bundle.putString("PetID", PetId);
            bundle.putString("PetImage", PetIMG);
            bundle.putString("scrn_from","Rec_voice");
        */    // fragmentCall_mainObj.Fragment_call(this,new Create_PetActivity(), "act_crt", bundle);
                audio_rec_contr.setVisibility(View.GONE);
                ll_contr_multi_vw_creation.setVisibility(View.VISIBLE);
                int Adapter_Pos = ((BrightlyNavigationActivity) getActivity()).multiview_sel_Adapter_Pos;
                MultipleViewCreationModel model_obj = multipleViewCreationModelList.get(Adapter_Pos);
                model_obj.URI_Val = null;
                model_obj.file_path = tempMp3File.getAbsolutePath();
                //  multipleCreationModelList.remove(multiview_sel_Adapter_Pos);
                //  multipleCreationModelList.add(multiview_sel_Adapter_Pos, model_obj);
                adapterMultipleViewCreation.multipleViewCreationModelList.set(Adapter_Pos, model_obj);
                adapterMultipleViewCreation.notifyItemChanged(Adapter_Pos);
                Compressed_filename = tempMp3File.getAbsolutePath();
                // save_item(Adapter_Pos, enum_MultipleViewType.AUDIO.ordinal());
                uploadFile(new File(Compressed_filename), enum_MultipleViewType.AUDIO.ordinal(), Adapter_Pos);
                //  setMediaPlayer(null, tempMp3File.getAbsolutePath(), true);
            }
        } catch (Exception w) {
            w.printStackTrace();
        }
    }

    @Override
    public void on_type_choosed(int position_to_add, int TYPE) {
        ArrayList<MultipleViewCreationModel> multipleViewCreationModelList;
        multipleViewCreationModelList = (ArrayList<MultipleViewCreationModel>) adapterMultipleViewCreation.multipleViewCreationModelList;
        enum_MultipleViewType enum_multi_types = CreateMultimediaCard.enum_MultipleViewType.values()[TYPE];
        int Choosen_type = 0;
        MultipleViewCreationModel initialiae_model_obj = new MultipleViewCreationModel();


        switch (enum_multi_types) {
            case _ADD:
                Choosen_type = enum_MultipleViewType._ADD.ordinal();
                break;
            case TITLE:
                /*temp_jsonReq_obj.type = "title";
                temp_jsonReq_obj.values = "";*/
                initialiae_model_obj.txt_Val = "";
                Choosen_type = enum_MultipleViewType.TITLE.ordinal();
                break;
            case IMAGE:
                /*temp_jsonReq_obj.type = "image";
                temp_jsonReq_obj.values = "";*/
                Choosen_type = enum_MultipleViewType.IMAGE.ordinal();
                list_multi_image = new ArrayList<>();
                MultipleImageModel img_model_obj = new MultipleImageModel();
                img_model_obj.setImg_url("");
                list_multi_image.add(0, img_model_obj);
                initialiae_model_obj.multipleImageModelListObj = list_multi_image;
                break;
            case AUDIO:

                Choosen_type = enum_MultipleViewType.AUDIO.ordinal();

                break;
            case VIDEO:

                Choosen_type = enum_MultipleViewType.VIDEO.ordinal();

                break;
            case UTUBE:
                initialiae_model_obj.txt_Val = "";
                Choosen_type = enum_MultipleViewType.UTUBE.ordinal();
                break;
            case WEBLINK:
                initialiae_model_obj.txt_Val = "";
                Choosen_type = enum_MultipleViewType.WEBLINK.ordinal();
                break;
            case CONTACT:
                initialiae_model_obj.name_val = "";
                initialiae_model_obj.comp_val = "";
                initialiae_model_obj.title_val = "";
                initialiae_model_obj.mob__val = "";
                initialiae_model_obj.off_val = "";
                initialiae_model_obj.email_val = "";
                initialiae_model_obj.notes_val = "";


                /*temp_jsonReq_obj.type = "contact";
                temp_jsonReq_obj.values = "";
                temp_jsonReq_obj.contact = null;*/
                /*List<ContactHelperModule> contactHelperModuleListObj = new ArrayList<>();
                ContactHelperModule name_type = new ContactHelperModule();
                name_type.values = "";
                name_type.type = "1";
                contactHelperModuleListObj.add(name_type);
                ContactHelperModule comp_type = new ContactHelperModule();
                comp_type.values = "";
                comp_type.type = "2";
                contactHelperModuleListObj.add(comp_type);
                ContactHelperModule title_type = new ContactHelperModule();
                title_type.values = "";
                title_type.type = "3";
                contactHelperModuleListObj.add(title_type);
                ContactHelperModule mob_type = new ContactHelperModule();
                mob_type.values = "";
                mob_type.type = "4";
                contactHelperModuleListObj.add(mob_type);
                ContactHelperModule off_type = new ContactHelperModule();
                off_type.values = "";
                off_type.type = "5";
                contactHelperModuleListObj.add(off_type);
                ContactHelperModule email_type = new ContactHelperModule();
                email_type.values = "";
                email_type.type = "6";
                contactHelperModuleListObj.add(email_type);
                ContactHelperModule notes_type = new ContactHelperModule();
                notes_type.values = "";
                notes_type.type = "7";
                contactHelperModuleListObj.add(notes_type);
*/
               /* Map<String, List<ContactHelperModule>> contactMap = new HashMap<>();
                contactMap.put(String.valueOf(position_to_add), contactHelperModuleListObj);*/
                //              contactHelperObj.add_contact_all_field(position_to_add, contactHelperModuleListObj);
                Choosen_type = enum_MultipleViewType.CONTACT.ordinal();

                break;

            case TEXT:
                initialiae_model_obj.txt_Val = "";
                Choosen_type = enum_MultipleViewType.TEXT.ordinal();
                break;

        }

        initialiae_model_obj.TYPE = Choosen_type;


        MultipleViewCreationModel add_modelObj = new MultipleViewCreationModel();
        add_modelObj.TYPE = enum_MultipleViewType._ADD.ordinal();
        if (position_to_add == multipleViewCreationModelList.size()) {
            multipleViewCreationModelList.add(initialiae_model_obj);
            multipleViewCreationModelList.add(add_modelObj);
            if (adapterMultipleViewCreation != null) {

                adapterMultipleViewCreation.multipleViewCreationModelList = multipleViewCreationModelList;
                // adapterMultipleViewCreation.notifyDataSetChanged();
        /*    adapterMultipleViewCreation.notifyItemInserted(position_to_add);
            adapterMultipleViewCreation.notifyItemInserted(position_to_add + 1);*/

                adapterMultipleViewCreation.notifyItemRangeInserted(position_to_add, 2);
                ll_contr_multi_vw_creation.getLayoutManager().smoothScrollToPosition(ll_contr_multi_vw_creation, new RecyclerView.State(), position_to_add + 1);


            }
        } else {
            ArrayList<MultipleViewCreationModel> ada_coll = adapterMultipleViewCreation.multipleViewCreationModelList;
            // multipleViewCreationModelList.clear();

            ArrayList<MultipleViewCreationModel> item_to_add = new ArrayList<>();
            item_to_add.add(initialiae_model_obj);
            item_to_add.add(add_modelObj);
            ada_coll.addAll(position_to_add, item_to_add);
            adapterMultipleViewCreation.multipleViewCreationModelList = ada_coll;
            ll_contr_multi_vw_creation.setAdapter(adapterMultipleViewCreation);
            ll_contr_multi_vw_creation.getLayoutManager().smoothScrollToPosition(ll_contr_multi_vw_creation, new RecyclerView.State(), position_to_add + 1);
            //  adapterMultipleViewCreation.notifyItemRangeInserted(position_to_add, item_to_add.size());
            //  adapterMultipleViewCreation.notifyItemChanged(position_to_add, item_to_add.size());
            // adapterMultipleViewCreation.notifyDataSetChanged();
          /*  if (adapterMultipleViewCreation == null)
                multipleViewCreationModelList.add(position_to_add, initialiae_model_obj);
            if (adapterMultipleViewCreation != null) {
                adapterMultipleViewCreation.multipleViewCreationModelList.add(position_to_add, initialiae_model_obj);
                adapterMultipleViewCreation.notifyItemInserted(position_to_add);
            }
            if (adapterMultipleViewCreation == null)
                multipleViewCreationModelList.add(position_to_add + 1, add_modelObj);
            else {
                adapterMultipleViewCreation.multipleViewCreationModelList.add(position_to_add + 1, add_modelObj);
                adapterMultipleViewCreation.notifyItemInserted(position_to_add + 1);
            }*/
         /*   if (adapterMultipleViewCreation != null) {

                adapterMultipleViewCreation.multipleViewCreationModelList = multipleViewCreationModelList;
                // adapterMultipleViewCreation.notifyDataSetChanged();
        *//*    adapterMultipleViewCreation.notifyItemInserted(position_to_add);
            adapterMultipleViewCreation.notifyItemInserted(position_to_add + 1);*//*
                adapterMultipleViewCreation.notifyItemInserted(position_to_add);
                adapterMultipleViewCreation.notifyItemInserted(position_to_add + 1);
                adapterMultipleViewCreation.notifyItemChanged(position_to_add + 2);
                adapterMultipleViewCreation.notifyItemChanged(position_to_add + 3);
                //        adapterMultipleViewCreation.notifyItemRangeInserted(position_to_add, position_to_add + 1);
                adapterMultipleViewCreation.notifyDataSetChanged();

            }*/
        }
        if (adapterMultipleViewCreation == null) {
            adapterMultipleViewCreation = new AdapterMultipleViewCreation((AppCompatActivity) getActivity(), multipleViewCreationModelList, this, isCreateCard);
        }

        /*if (Choosen_type != enum_MultipleViewType.CONTACT.ordinal())
            JsonReqModelUpdate(position_to_add, temp_jsonReq_obj);
        else
            contact_update(position_to_add, contactHelperObj);*/

    }

    @Override
    public void call_action(int position, int TYPE) {
        ((BrightlyNavigationActivity) getActivity()).multiview_sel_Adapter_Pos = position;

        if (TYPE == enum_MultipleViewType.AUDIO.ordinal()) {
            setDialog_AudioOptions(position);
        }
        if (TYPE == enum_MultipleViewType.IMAGE.ordinal()) {
            imageChooserIntent();
        }
        if (TYPE == enum_MultipleViewType.VIDEO.ordinal()) {
            getVideoIntent();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.crt_done) {
            MergeJson();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void MergeJson() {
       /* Map<String, List<ContactHelperModule>> contactMap = contactHelperObj.getContactMap();
        List<String> l = new ArrayList<String>(contactHelperObj.getContactMap().keySet());
        if (l.size() > 0) {
            for (int i = 0; i < l.size(); i++) {
                List<ContactHelperModule> contactHelperModuleList = contactMap.get(l.get(i));
                for (ContactHelperModule contactHelperModuleObj : contactHelperModuleList) {
                    int Index = Integer.parseInt(l.get(i));
                    JsonReqObj.populate(Index, contactHelperModuleObj);
                }
            }
        }*/
        List<MultipleViewCreationModel> multipleViewCreationModelList;
        JsonReqObj = new JSONObjectReqModule();
        multipleViewCreationModelList = adapterMultipleViewCreation.multipleViewCreationModelList;

        for (MultipleViewCreationModel multipleViewCreationModelObj : multipleViewCreationModelList) {
            CreateMultimediaCard.enum_MultipleViewType crt_type = CreateMultimediaCard.enum_MultipleViewType.values()[multipleViewCreationModelObj.TYPE];
            JSONReqMVModule model_obj;
            switch (crt_type) {
                case CONTACT:
                    ContactHelperModule nam_model_obj = new ContactHelperModule();
                    nam_model_obj.values = multipleViewCreationModelObj.name_val;
                    nam_model_obj.type = "1";

                    add_con(new ContactHelper(nam_model_obj));

                    ContactHelperModule comp_model_obj = new ContactHelperModule();
                    comp_model_obj.values = multipleViewCreationModelObj.comp_val;
                    comp_model_obj.type = "2";
                    add_con(new ContactHelper(comp_model_obj));
                    ContactHelperModule title_model_obj = new ContactHelperModule();
                    title_model_obj.values = multipleViewCreationModelObj.title_val;
                    title_model_obj.type = "3";
                    add_con(new ContactHelper(title_model_obj));
                    ContactHelperModule mob_model_obj = new ContactHelperModule();
                    mob_model_obj.values = multipleViewCreationModelObj.mob__val;
                    mob_model_obj.type = "4";
                    add_con(new ContactHelper(mob_model_obj));
                    ContactHelperModule off_model_obj = new ContactHelperModule();
                    off_model_obj.values = multipleViewCreationModelObj.off_val;
                    off_model_obj.type = "5";
                    add_con(new ContactHelper(off_model_obj));
                    ContactHelperModule email_model_obj = new ContactHelperModule();
                    email_model_obj.values = multipleViewCreationModelObj.email_val;
                    email_model_obj.type = "6";
                    add_con(new ContactHelper(email_model_obj));
                    ContactHelperModule notes_model_obj = new ContactHelperModule();
                    notes_model_obj.values = multipleViewCreationModelObj.notes_val;
                    notes_model_obj.type = "7";
                    add_con(new ContactHelper(notes_model_obj));


                    break;
                case IMAGE:
                    model_obj = new JSONReqMVModule();
                    //List<MultipleImageModel> multipleImageModelListObj = multipleViewCreationModelObj.txt_Val;
                    model_obj.value = multipleViewCreationModelObj.txt_Val;
                    model_obj.type = "img";
                    JsonReqObj.jsonReqList.add(model_obj);
                    break;
                case AUDIO:
                    model_obj = new JSONReqMVModule();
                    model_obj.value = multipleViewCreationModelObj.txt_Val;
                    model_obj.type = "audio";
                    JsonReqObj.jsonReqList.add(model_obj);
                    break;
                case VIDEO:
                    model_obj = new JSONReqMVModule();
                    model_obj.value = multipleViewCreationModelObj.txt_Val;
                    model_obj.type = "video";
                    JsonReqObj.jsonReqList.add(model_obj);
                    break;
                case WEBLINK:
                    model_obj = new JSONReqMVModule();
                    model_obj.value = multipleViewCreationModelObj.txt_Val;
                    model_obj.type = "weblink";
                    JsonReqObj.jsonReqList.add(model_obj);
                    break;
                case TEXT:
                    model_obj = new JSONReqMVModule();
                    model_obj.value = multipleViewCreationModelObj.txt_Val;
                    model_obj.type = "text";
                    JsonReqObj.jsonReqList.add(model_obj);
                    break;
                case TITLE:
                    model_obj = new JSONReqMVModule();
                    model_obj.value = multipleViewCreationModelObj.txt_Val;
                    model_obj.type = "title";
                    JsonReqObj.jsonReqList.add(model_obj);
                    break;
                case UTUBE:
                    model_obj = new JSONReqMVModule();
                    model_obj.value = multipleViewCreationModelObj.txt_Val;
                    model_obj.type = "youtube";
                    JsonReqObj.jsonReqList.add(model_obj);
                    break;


            }
        }
        getAddCards();
    }

    public void add_con(ContactHelper contact) {
        JsonReqObj.jsonReqList.add(contact);

    }

    private void getAddCards() {
        //   isCreateCard = true;

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
            Gson gson = new Gson();
            String data_str = gson.toJson(JsonReqObj);
            try {
                JSONObject jsonData = new JSONObject(data_str);
                data_str = jsonData.getString("jsonReqList");

            } catch (Exception e) {

            }

            if (isCreateCard) {
                RestApiMethods requestInterface = RetrofitInterface.getRestApiMethods(getContext());
                Call<ResponseBody> responseBodyCall = null;

                responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).call_create_card(user_obj.getUser_Id(), "1", setsListModelObj.getSet_id(), data_str);
            /*else
                responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).updateRecord("video_file", userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, multipartBody);*/
                responseBodyCall.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dismissProgress();
                        // AddMessageResponse responseObj=(AddMessageResponse) response.body().;
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dismissProgress();
                        //Toast.makeText(getContext(), "error"+call., Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                RestApiMethods requestInterface = RetrofitInterface.getRestApiMethods(getContext());
                Call<ResponseBody> responseBodyCall = null;

                responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).call_update_blog(user_obj.getUser_Id(), "2", setsListModelObj.getSet_id(), data_str, cardListModelObj.getCard_id());
            /*else
                responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).updateRecord("video_file", userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, multipartBody);*/
                responseBodyCall.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dismissProgress();
                        // AddMessageResponse responseObj=(AddMessageResponse) response.body().;
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dismissProgress();
                        //Toast.makeText(getContext(), "error"+call., Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        if (!isRecordAudio) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_create_multi, menu);
        }
    }

   /* private void uploadFile(int Position, Uri fileUri, boolean isCreateCard) {
        Cursor cursor;
        if (fileUri != null) {
            File file = null;
            if (!isCapture_video) {
                String filePath;
                String[] filePathColumn = {MediaStore.MediaColumns.DATA};

                cursor = getContext().getContentResolver().query(fileUri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();

                file = new File(filePath);
            } else {


                try {
                    String path = fileUri.toString(); // "/mnt/sdcard/FileName.mp3"
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
                    responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).addRecord("video_file", user_obj.getUser_Id(), set_id, card_name, card_description, "video_00", multipartBody);
                else
                    responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).updateRecord("video_file", userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, multipartBody);
                responseBodyCall.enqueue(new Callback<AddMessageResponse>() {
                    @Override
                    public void onResponse(Call<AddMessageResponse> call, Response<AddMessageResponse> response) {
                        dismissProgress();
                   *//* Log.d("Success", "success " + response.code());
                    Log.d("Success", "success " + response.message());*//*
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
          *//*  if (!isCreateCard) {
                String[] old_filename = setEntryModelObj.getCard_multimedia_url().split("/");
                Call<AddMessageResponse> responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).updateRecord_old("video_file", userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, old_filename[old_filename.length - 1]);
                responseBodyCall.enqueue(new Callback<AddMessageResponse>() {
                    @Override
                    public void onResponse(Call<AddMessageResponse> call, Response<AddMessageResponse> response) {
                        dismissProgress();
                   *//**//* Log.d("Success", "success " + response.code());
                    Log.d("Success", "success " + response.message());*//**//*
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
            }*//*
        }


    }*/

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


    public void imageChooserIntent() {

        //if (PermissionUtil.hasPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, getContext(), BrightlyNavigationActivity.PERMISSION_REQ_CODE_IMAGE)) {
        imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "Image_" + System.currentTimeMillis());
        Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
        if (intent == null) {
            //PermissionUtil.
        } else {
            startActivityForResult(intent, PICK_IMAGE_REQ);
        }
        //}
    }


    @Override
    public void call_image_dialog(int Adapter_pos) {
        ((BrightlyNavigationActivity) getActivity()).multiview_sel_Adapter_Pos = Adapter_pos;
        isChoose_New_image = true;
        //  set_image_BottomDialog();
        imageChooserIntent();
    }

    @Override
    public void update_image_thumbnail_pos(int AdapterPos, int Thumbnail_pos) {
        // List<MultipleViewCreationModel> multipleViewCreationModelList=adapterMultipleViewCreation.multipleViewCreationModelList;
        MultipleViewCreationModel modelObj = adapterMultipleViewCreation.multipleViewCreationModelList.get(AdapterPos);
        modelObj.Thumbnail_sel_pos = Thumbnail_pos;
        //  multipleViewCreationModelList.set(AdapterPos, modelObj);
        adapterMultipleViewCreation.multipleViewCreationModelList.set(AdapterPos, modelObj);
        adapterMultipleViewCreation.notifyItemChanged(AdapterPos);

    }

    @Override
    public void del_view(int Position) {
        ArrayList<MultipleViewCreationModel> multipleViewCreationModelListObj = adapterMultipleViewCreation.multipleViewCreationModelList;
        multipleViewCreationModelListObj.remove(Position);
        multipleViewCreationModelListObj.remove(Position - 1);
        adapterMultipleViewCreation.multipleViewCreationModelList = multipleViewCreationModelListObj;
      /*  adapterMultipleViewCreation.notifyItemRemoved(Position);
        adapterMultipleViewCreation.notifyItemRemoved(Position - 1);*/
        //adapterMultipleViewCreation.notifyItemRangeRemoved(Position - 1, Position);
        ll_contr_multi_vw_creation.setAdapter(adapterMultipleViewCreation);
    }

    @Override
    public void contact_update(int Position, ContactHelperModule modelObj) {

        MultipleViewCreationModel multipleViewCreationModelObj = adapterMultipleViewCreation.multipleViewCreationModelList.get(Position);
        switch (modelObj.type) {
            case "1":
                multipleViewCreationModelObj.name_val = modelObj.values;
                break;
            case "2":
                multipleViewCreationModelObj.comp_val = modelObj.values;
                break;
            case "3":
                multipleViewCreationModelObj.title_val = modelObj.values;
                break;
            case "4":
                multipleViewCreationModelObj.mob__val = modelObj.values;
                break;
            case "5":
                multipleViewCreationModelObj.off_val = modelObj.values;
                break;
            case "6":
                multipleViewCreationModelObj.email_val = modelObj.values;
                break;
            case "7":
                multipleViewCreationModelObj.notes_val = modelObj.values;
                break;


        }
        adapterMultipleViewCreation.multipleViewCreationModelList.set(Position, multipleViewCreationModelObj);

        //adapterMultipleViewCreation.notifyItemChanged(Position);

        // contactHelperObj.add_contact(Position, modelObj);
    }


   /* public void save_item(int position, int Type) {
        File file = new File(Compressed_filename);
        isCreateCard = true;
        uploadFile(file);
    }*/

    @Override
    public void JsonReqModelUpdate(int Position, Object modelObj) {
       /* if (jsonReqMVModulesListObj == null) {
            jsonReqMVModulesListObj = new ArrayList<>();
            jsonReqMVModulesListObj.add(modelObj);
        } else if (Position - 2 < jsonReqMVModulesListObj.size()) {
            jsonReqMVModulesListObj.set(Position - 2, modelObj);
        } else {
            jsonReqMVModulesListObj.add(modelObj);
        }*/
        //  JsonReqObj.populate(Position, modelObj);
        MultipleViewCreationModel multipleViewCreationModelObj = adapterMultipleViewCreation.multipleViewCreationModelList.get(Position);
        multipleViewCreationModelObj.txt_Val = ((JSONReqMVModule) modelObj).value;
        adapterMultipleViewCreation.multipleViewCreationModelList.set(Position, multipleViewCreationModelObj);
//        adapterMultipleViewCreation.notifyItemChanged(Position);

    }


/*

    @Override
    public void showPopup(int PositionToAdd, View cur_button) {
        PopupMenu popup = new PopupMenu(getContext(), cur_button);
        //inflating menu from xml resource
        popup.inflate(R.menu.vw_cr_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_Title:

                        on_type_choosed(PositionToAdd + 1, CreateMultimediaCard.enum_MultipleViewType.TITLE.ordinal());

                        return true;
                    case R.id.menu_Image:
                        on_type_choosed(PositionToAdd + 1, CreateMultimediaCard.enum_MultipleViewType.IMAGE.ordinal());

                        return true;
                    case R.id.menu_Audio:
                        on_type_choosed(PositionToAdd + 1, CreateMultimediaCard.enum_MultipleViewType.AUDIO.ordinal());
                        return true;
                    case R.id.menu_Video:
                        on_type_choosed(PositionToAdd + 1, CreateMultimediaCard.enum_MultipleViewType.VIDEO.ordinal());
                        return true;
                    case R.id.menu_utube:
                        on_type_choosed(PositionToAdd + 1, CreateMultimediaCard.enum_MultipleViewType.UTUBE.ordinal());
                        return true;
                    case R.id.menu_weblink:
                        on_type_choosed(PositionToAdd + 1, CreateMultimediaCard.enum_MultipleViewType.WEBLINK.ordinal());
                        return true;
                    case R.id.menu_contact:
                        on_type_choosed(PositionToAdd + 1, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal());
                        return true;
                    case R.id.menu_text:
                        on_type_choosed(PositionToAdd + 1, CreateMultimediaCard.enum_MultipleViewType.TEXT.ordinal());
                        return true;
                    default:
                        return false;
                }
            }
        });
        //displaying the popup
        popup.show();

    }
*/

    public void setDialog_AudioOptions(int Position) {

        final Dialog mBottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_view_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        menu_list.add("Choose audio file");
        menu_list.add("Record audio");
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

                        switch (position) {
                            case 1:

                                isRecordAudio = true;
                                getActivity().invalidateOptionsMenu();
                                audio_rec_contr.setVisibility(View.VISIBLE);
                                ll_contr_multi_vw_creation.setVisibility(View.GONE);
                                break;
                            case 0:
                                isRecordAudio = false;
                                Intent intent_upload = new Intent();
                                intent_upload.setType("audio/*");


                                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent_upload, AudioFileReq);
                                break;

                        }
                    }
                }
        );

    }


    @Override
    public void onPause() {
        super.onPause();

       /* if (mediaPlayer != null) {
            if (isAudioPlay) {
                isAudioPlay = false;
                img_play_stop.setImageResource(R.drawable.play_rec);
                mediaPlayer.pause();
            }
        }*/
        List<MultipleViewCreationModel> multipleViewCreationModelListObj = adapterMultipleViewCreation.multipleViewCreationModelList;
        if (multipleViewCreationModelListObj != null) {
            for (int i = 0; i < multipleViewCreationModelListObj.size(); i++) {
                MultipleViewCreationModel multipleViewCreationModelObj = multipleViewCreationModelListObj.get(i);
                if ((multipleViewCreationModelObj.TYPE == enum_MultipleViewType.AUDIO.ordinal())) {
                    AdapterMultipleViewCreation.AudioViewHolder holder = (AdapterMultipleViewCreation.AudioViewHolder) ll_contr_multi_vw_creation.findViewHolderForAdapterPosition(i);
                    if (holder != null && holder.isAudioPlay) {
                        holder.mediaPlayer.pause();
                        holder.isAudioPlay = false;
                        holder.img_play_stop.setImageResource(R.drawable.play_rec);
                    }

                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AudioFileReq) {

            if (resultCode == RESULT_OK) {

                getAudioResult(data);

                //adapterMultipleViewCreation.setAudioURI(multiview_sel_Adapter_Pos, data);
                //Toast.makeText(getContext(), "Audio URI" + audio_uri, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICK_IMAGE_REQ) {
            if (resultCode == RESULT_OK) {

                try {

                    picUri = imgImageChooser_crop.getPickImageResultUri(data);
                    //  compressImage(picUri.toString());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setCropMenuCropButtonTitle("Done")
                                .setMinCropResultSize(250, 250)

//                                .setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .start(getContext(), this);
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

                    compressImage(crop_result_uri.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (requestCode == PICK_VIDEO_REQ) {
            if (resultCode == RESULT_OK) {

                // Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",fileImagePath);

                int AdapterPos = ((BrightlyNavigationActivity) getActivity()).multiview_sel_Adapter_Pos;
                // videoUri = getPickImageResultUri(data);

                MultipleViewCreationModel sel_model_obbj = adapterMultipleViewCreation.multipleViewCreationModelList.get(AdapterPos);
                sel_model_obbj.Video_file_uri = getPickImageResultUri(data);
                Uri videoUri = data.getData();
                Cursor cursor;
                File file = null;
                if (videoUri != null) {

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

                }

                adapterMultipleViewCreation.multipleViewCreationModelList.set(AdapterPos, sel_model_obbj);
                adapterMultipleViewCreation.notifyItemChanged(AdapterPos);
                uploadFile(file, enum_MultipleViewType.VIDEO.ordinal(), AdapterPos);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadFile(File file, int Type, int Position) {
/*

        Cursor cursor;
        if (FileUri != null) {
            File file = null;
            if (!isCamera) {
                String filePath;
                String[] filePathColumn = {MediaStore.MediaColumns.DATA};

                cursor = getContext().getContentResolver().query(FileUri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();

                file = new File(filePath);
            } else {


                try {
                    String path = FileUri.toString(); // "/mnt/sdcard/FileName.mp3"
                    file = new File(new URI(path));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
*/

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);


            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            showProgress();
            Call<AddMessageResponse> responseBodyCall = null;

            responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).upload_diff_type(user_obj.getUser_Id(), setsListModelObj.getSet_id(), multipartBody);
            /*else
                responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).updateRecord("video_file", userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, multipartBody);*/
            responseBodyCall.enqueue(new Callback<AddMessageResponse>() {
                @Override
                public void onResponse(Call<AddMessageResponse> call, Response<AddMessageResponse> response) {
                    dismissProgress();
                   /* Log.d("Success", "success " + response.code());
                    Log.d("Success", "success " + response.message());*/
                    AddMessageResponse messageResponse = response.body();
                    //  Toast.makeText(getContext(), "Path" + messageResponse.getFile_path(), Toast.LENGTH_LONG).show();
                    Log.e("Resonse", "Vsle" + response.body());
                    JSONReqMVModule modelObj = new JSONReqMVModule();
                    modelObj.value = messageResponse.getFile_path();
                    CreateMultimediaCard.enum_MultipleViewType enum_multipleViewType = CreateMultimediaCard.enum_MultipleViewType.values()[Type];
                    switch (enum_multipleViewType) {
                        case VIDEO:
                            modelObj.type = "video";
                            break;
                        case AUDIO:
                            modelObj.type = "audio";
                            break;
                        case IMAGE:
                            modelObj.type = "image";
                            break;

                    }
                    // save_item(Position, Type);
                    JsonReqModelUpdate(Position, modelObj);

                 /*   if (messageResponse.getMessage().equals("success")) {
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    } else {
                        Toast.makeText(getContext(), "Error:" + messageResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }*/

                }

                @Override
                public void onFailure(Call<AddMessageResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("failure", "message = " + t.getMessage());
                    Log.d("failure", "cause = " + t.getCause());
                }
            });
        }
    }/* else

    {
        if (!isCreateCard) {
            String[] old_filename = setEntryModelObj.getCard_multimedia_url().split("/");
            Call<AddMessageResponse> responseBodyCall = RetrofitInterface.getRestApiMethods(getContext()).updateRecord_old("video_file", userId, set_id, setEntryModelObj.getCard_id(), card_name, card_description, old_filename[old_filename.length - 1]);
            responseBodyCall.enqueue(new Callback<AddMessageResponse>() {
                @Override
                public void onResponse(Call<AddMessageResponse> call, Response<AddMessageResponse> response) {
                    dismissProgress();
                   *//* Log.d("Success", "success " + response.code());
                    Log.d("Success", "success " + response.message());*//*
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
    }*/


    public String compressImage(String Uri_val) {
        int AdapterPos = ((BrightlyNavigationActivity) getActivity()).multiview_sel_Adapter_Pos;
        MultipleViewCreationModel multi_view_cr_model_obj = adapterMultipleViewCreation.multipleViewCreationModelList.get(AdapterPos);
        List<MultipleImageModel> list_multi_image = multi_view_cr_model_obj.multipleImageModelListObj;
        int img_count;
        if (list_multi_image != null) {
            img_count = multi_view_cr_model_obj.multipleImageModelListObj.size();
        }
        String filePath = getRealPathFromURI(Uri_val);
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
        Compressed_filename = getFilename();
        try {
            out = new FileOutputStream(Compressed_filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            //--------->  set_card_image(filename);

            MultipleImageModel model_Obj = new MultipleImageModel();
            Uri file_compressed_uri = Uri.fromFile(new File(Compressed_filename));


            model_Obj.setImg_url(file_compressed_uri.toString());
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), file_compressed_uri);
            model_Obj.setImg_encode_string(getStringImage(bitmap));
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            model_Obj.setImg_name(sdf.format(new Date()));
            if (isChoose_New_image) {
                sel_multiview_creation_model_obj = adapterMultipleViewCreation.multipleViewCreationModelList.get(AdapterPos);
                isChoose_New_image = false;
                // list_multi_image.remove(Thumbnail_sel_position);
                temp_Thumbnail_sel_position = 1;
                list_multi_image.set(temp_Thumbnail_sel_position, model_Obj);
                sel_multiview_creation_model_obj.multipleImageModelListObj = list_multi_image;
                sel_multiview_creation_model_obj.Thumbnail_sel_pos = temp_Thumbnail_sel_position;


            } else {
                sel_multiview_creation_model_obj = adapterMultipleViewCreation.multipleViewCreationModelList.get(AdapterPos);
                list_multi_image.add(model_Obj);
                sel_multiview_creation_model_obj.Thumbnail_sel_pos = list_multi_image.size() - 1;
                sel_multiview_creation_model_obj.multipleImageModelListObj = list_multi_image;


            }
            adapterMultipleViewCreation.multipleViewCreationModelList.set(AdapterPos, sel_multiview_creation_model_obj);
            adapterMultipleViewCreation.notifyItemChanged(AdapterPos);
            uploadFile(new File(Compressed_filename), enum_MultipleViewType.IMAGE.ordinal(), AdapterPos);
            //  save_item(AdapterPos, enum_MultipleViewType.IMAGE.ordinal());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return Compressed_filename;

    }

    public void set_image_BottomDialog() {
        int Adapter_pos = ((BrightlyNavigationActivity) getActivity()).multiview_sel_Adapter_Pos;
        sel_multiview_creation_model_obj = adapterMultipleViewCreation.multipleViewCreationModelList.get(Adapter_pos);
        temp_Thumbnail_sel_position = sel_multiview_creation_model_obj.Thumbnail_sel_pos;

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
                                int img_count = sel_multiview_creation_model_obj.multipleImageModelListObj.size();

                                String remove_img_id = sel_multiview_creation_model_obj.remove_img_id;


                                img_count = img_count - 1;
                                /*if (img_count == 1)
                                //encoded_string = new ArrayList<>();
                                {
                                    list_multi_image = new ArrayList<>();
                                    sel_multiview_creation_model_obj.multipleImageModelListObj = list_multi_image;
                                } else {*/
                                MultipleImageModel multipleImageModel = list_multi_image.get(temp_Thumbnail_sel_position);
                                if (!multipleImageModel.getImg_id().equals("")) {
                                    if (!remove_img_id.equals(""))
                                        remove_img_id = remove_img_id + ",";
                                    sel_multiview_creation_model_obj.remove_img_id = remove_img_id + multipleImageModel.getImg_id();
                                }
                                list_multi_image.remove(temp_Thumbnail_sel_position);
                                sel_multiview_creation_model_obj.multipleImageModelListObj = list_multi_image;

                                if (temp_Thumbnail_sel_position == list_multi_image.size())
                                    temp_Thumbnail_sel_position--;
                                sel_multiview_creation_model_obj.Thumbnail_sel_pos = temp_Thumbnail_sel_position;

                                //}
                                adapterMultipleViewCreation.multipleViewCreationModelList.set(Adapter_pos, sel_multiview_creation_model_obj);
                                adapterMultipleViewCreation.notifyItemChanged(Adapter_pos);

                                /*if (img_count == 1) {

                                } else {
                                    MultipleImageModel multipleImageModelOBj = list_multi_image.get(Thumbnail_sel_position);
                                    *//*Glide.with(getActivity())
                                            .load(multipleImageModelOBj.getImg_url())
                                            .fitCenter()
                                            *//**//*.transform(new CircleTransform(HomeActivity.this))
                                            .override(50, 50)*//**//*
                                            .into(image_createCard);*//*
                                    set_card_image(multipleImageModelOBj.getImg_url());
                                }*/
                                break;
                            case 0:
                                isChoose_New_image = true;
                                imageChooserIntent();

                                break;

                        }
                    }
                }
        );

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;


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

    private String getRealPathFromURI(Uri datacontentURI) {
       /* Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }*/
        final String docId = DocumentsContract.getDocumentId(datacontentURI);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{split[1]};
        Compressed_filename = getDataColumn(getContext(), contentUri, selection, selectionArgs);
        return Compressed_filename;
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

    public void getAudioResult(Intent data) {
        int Adapter_Pos = ((BrightlyNavigationActivity) getActivity()).multiview_sel_Adapter_Pos;
        MultipleViewCreationModel model_obj = adapterMultipleViewCreation.multipleViewCreationModelList.get(Adapter_Pos);
        model_obj.file_path = null;
        model_obj.URI_Val = data.getData();
        //  multipleCreationModelList.remove(multiview_sel_Adapter_Pos);
        //  multipleCreationModelList.add(multiview_sel_Adapter_Pos, model_obj);
        adapterMultipleViewCreation.multipleViewCreationModelList.set(Adapter_Pos, model_obj);
        adapterMultipleViewCreation.notifyItemChanged(Adapter_Pos);
        /*Uri audioUri = data.getData();
        String[] filePathColumn = {MediaStore.Audio.AudioColumns.DATA};
        Cursor cursor = getContext().getContentResolver().query(audioUri, filePathColumn, null, null, null);

        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        File file = new File(filePath);
*/
        /*String[] projection = {MediaStore.Audio..DATA};
        Cursor cursor = getContext().getContentResolver().query(data.getData(), projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        uploadFile(new File(filePath));*/
        /*File file = new File(data.getData().getPath());
        uploadFile(file);*/
/*
        final String docId = DocumentsContract.getDocumentId(data.getData());
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{split[1]};
        Compressed_filename = getDataColumn(getContext(), contentUri, selection, selectionArgs);*/
        getRealPathFromURI(data.getData());
        uploadFile(new File(Compressed_filename), enum_MultipleViewType.AUDIO.ordinal(), Adapter_Pos);
        //isCreateCard = true;
        //uploadFile(new File(filePath));

    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


}
