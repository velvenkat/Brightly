package com.purplefront.brightly.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Custom.SiriWaveView;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.Modules.SetEntryModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.PermissionUtil;
import com.purplefront.brightly.Utils.TimeFormat;
import com.purplefront.brightly.Utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioType extends BaseFragment implements BrightlyNavigationActivity.PermissionResultInterface {

    View view;
    TextView text_audioFile;
    ImageView upload_audioFile;
    // ImageView image_deleteAudioFile;
    EditText create_cardName;
    EditText create_cardDescription;
    Button btn_createCard;
    Uri audio_uri = null;
    String CreatedBy;
    /*Context context;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    boolean  isAudioPlay = false;
    String TotalTime;*/
    SetEntryModel setEntryModel;

    String userId;
    String set_id;
    String set_name;
    String card_name = "";
    String card_description = "";
    String encoded_string = "";
    String image_name;
    String type = "";
    SiriWaveView waveView;
    MediaRecorder myAudioRecorder = null;
    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 1;
    View rootView;
    ImageView img_play_stop_rec, cur_default;
    TextView txtRecSeconds;
    CountDownTimer countDownTimer = null;
    File tempMp3File = null;
    SeekBar audio_seek_bar;


    ImageView img_play_stop;
    TextView txt_PlayProgTime;
    boolean isPlayBtnClicked = false;
    RelativeLayout rl_audio_player;

    LinearLayout rec_contr;
    LinearLayout crt_contr;
    boolean isCreateScreen;
    RealmModel user_obj;

    @Override
    public void onResume() {
        super.onResume();
        if (audio_uri != null || tempMp3File != null) {
            rl_audio_player.setVisibility(View.VISIBLE);
            text_audioFile.setVisibility(View.GONE);
            setAudioProgText();
        }
    }

    public AudioType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_audio_type, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        Bundle bundle = getArguments();

        isCreateScreen = bundle.getBoolean("isCreate");
        setEntryModel = bundle.getParcelable("set_entry_obj");
        CreatedBy = bundle.getString("created_by");


        userId = user_obj.getUser_Id();
        set_id = setEntryModel.getSet_id();
        set_name = setEntryModel.getSet_name();

        img_play_stop = (ImageView) rootView.findViewById(R.id.img_play_stop);
        audio_seek_bar = (SeekBar) rootView.findViewById(R.id.seek_audio_rec);
        txt_PlayProgTime = (TextView) rootView.findViewById(R.id.txt_PlayProgTime);
        text_audioFile = (TextView) rootView.findViewById(R.id.text_audioFile);
        //   image_deleteAudioFile = (ImageView) rootView.findViewById(R.id.image_deleteAudioFile);
        upload_audioFile = (ImageView) rootView.findViewById(R.id.upload_audioFile);
        create_cardName = (EditText) rootView.findViewById(R.id.create_cardName);
        create_cardDescription = (EditText) rootView.findViewById(R.id.create_cardDescription);
        btn_createCard = (Button) rootView.findViewById(R.id.btn_createCard);
        rec_contr = (LinearLayout) rootView.findViewById(R.id.audio_rec_contr);
        crt_contr = (LinearLayout) rootView.findViewById(R.id.ll_cr_contr);
        waveView = (SiriWaveView) rootView.findViewById(R.id.siriWaveView);
        img_play_stop_rec = (ImageView) rootView.findViewById(R.id.img_play_stop_rec);
        txtRecSeconds = (TextView) rootView.findViewById(R.id.txtRecSeconds);
        cur_default = (ImageView) rootView.findViewById(R.id.cur_default);
        rl_audio_player = (RelativeLayout) rootView.findViewById(R.id.rl_audio_player);
        waveView.stopAnimation();

        rec_contr.setVisibility(View.GONE);
        crt_contr.setVisibility(View.VISIBLE);
        clear_edit_text_focus(create_cardDescription);
        clear_edit_text_focus(create_cardName);
        audio_player_initialize(audio_seek_bar, txt_PlayProgTime, img_play_stop);
        ((BrightlyNavigationActivity) getActivity()).permissionResultInterfaceObj = this;
        if (isCreateScreen) {
            rl_audio_player.setVisibility(View.GONE);
            text_audioFile.setVisibility(View.VISIBLE);
        } else {
            if (!CreatedBy.equalsIgnoreCase(userId)) {

            }
            if (setEntryModel.getType().equalsIgnoreCase("audio")) {
                rl_audio_player.setVisibility(View.VISIBLE);
                text_audioFile.setVisibility(View.GONE);
                setMediaPlayer(null, setEntryModel.getCard_multimedia_url());
            }
            create_cardName.setText(setEntryModel.getCard_name());
            create_cardDescription.setText(setEntryModel.getCard_description());
            btn_createCard.setText("UPDATE CARD");
        }


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();


        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    if ((rec_contr.getVisibility() == View.VISIBLE)) {

                        rec_contr.setVisibility(View.GONE);

                        crt_contr.setVisibility(View.VISIBLE);
                        if (isPlayBtnClicked) {
                            myAudioRecorder.stop();
                            myAudioRecorder.release();
                        }
                        tempMp3File = null;
                        isPlayBtnClicked = false;
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

        img_play_stop_rec.setOnClickListener(new View.OnClickListener() {
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


        upload_audioFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_youtube, youTubePlayerFragment).commit();*/
                //  mYouTubePlayerSupportFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
               /* Fragment recordFrag=new Fragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
                fragmentTransaction.replace(R.id.frameContainer, recordFrag, recordFrag.getClass().getSimpleName());
                fragmentTransaction.addToBackStack(recordFrag.getClass().getSimpleName());
//    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.commit();*/
              /* rec_contr.setVisibility(View.VISIBLE);
               crt_contr.setVisibility(View.GONE);*/
                setBottomDialog();
            }
        });

        btn_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAudioPlay)
                    mediaPlayer.pause();
                isAudioPlay = false;
                checkValidation();
            }
        });

        return rootView;
    }

    /* public void release_media() {
         if (mediaPlayer != null) {
             if (isAudioPlay) {
                 isAudioPlay = false;
                 mediaPlayer.pause();
             }
             mediaPlayer.release();
             mediaPlayer = null;
         }
     }*/
    /*private void primarySeekBarProgressUpdater() {
        if (isAudioPlay) {
            audio_seek_bar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
            String cur_Pos = TimeFormat.formateMilliSeccond(mediaPlayer.getCurrentPosition());
            txt_PlayProgTime.setText(cur_Pos + "/" + TotalTime);
            if (mediaPlayer.isPlaying()) {
                Runnable notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater();
                    }
                };
                handler.postDelayed(notification, 1000);
            }
        }
    }
*/
    public void Rec_finish() {
        countDownTimer.cancel();
        if (myAudioRecorder != null) {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            text_audioFile.setVisibility(View.GONE);
            rl_audio_player.setVisibility(View.VISIBLE);
        /*    Bundle bundle = new Bundle();
            bundle.putString("filePath", tempMp3File.getAbsolutePath());
            bundle.putString("PetID", PetId);
            bundle.putString("PetImage", PetIMG);
            bundle.putString("scrn_from","Rec_voice");
        */    // fragmentCall_mainObj.Fragment_call(this,new Create_PetActivity(), "act_crt", bundle);
            rec_contr.setVisibility(View.GONE);
            crt_contr.setVisibility(View.VISIBLE);
            setMediaPlayer(null, tempMp3File.getAbsolutePath());
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
                        //audio_uri = null;
                        //tempMp3File = null;
                        //release_media();
                        //text_audioFile.setVisibility(View.VISIBLE);
                        // rl_audio_player.setVisibility(View.GONE);
                        switch (position) {
                            case 1:

                                rec_contr.setVisibility(View.VISIBLE);
                                crt_contr.setVisibility(View.GONE);
                                break;
                            case 0:
                                Intent intent_upload = new Intent();
                                intent_upload.setType("audio/*");
                                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent_upload, 1);
                                break;

                        }
                    }
                }
        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                //the selected audio.
                audio_uri = data.getData();
                text_audioFile.setVisibility(View.GONE);
                rl_audio_player.setVisibility(View.VISIBLE);
              /*  String path = audio_uri.getPath(); // "/mnt/sdcard/FileName.mp3"
                File file = null;
                try {
                    file = new File(new URI(path));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }*/
                setMediaPlayer(audio_uri, null);
                //Toast.makeText(getContext(), "Audio URI" + audio_uri, Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void audio_rec() {
        myAudioRecorder = new MediaRecorder();
        cur_default.setVisibility(View.GONE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //  finish();
                }
                if (PermissionUtil.hasPermission(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE}, getContext(), BrightlyNavigationActivity.PERMISSION_REQ_CODE_AUDIO)) {
                    checkPermissionsAndStart();
                }
        }

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
        img_play_stop_rec.setImageResource(R.drawable.stop_rec);
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

    public byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer, 0, read);
        }
        out.close();
        return out.toByteArray();
    }


    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        card_name = create_cardName.getText().toString();
        card_description = create_cardDescription.getText().toString();
        InputStream inputStream = null;
        try {

            if (tempMp3File != null || audio_uri != null) {
                if (tempMp3File != null) {
                    inputStream =
                            getActivity().getContentResolver().openInputStream(Uri.fromFile(new File(tempMp3File.getAbsolutePath())));
                } else if (audio_uri != null) {
                    inputStream = getActivity().getContentResolver().openInputStream(audio_uri);
                }


                byte[] soundBytes = new byte[inputStream.available()];
                soundBytes = toByteArray(inputStream);
                encoded_string = Base64.encodeToString(soundBytes, Base64.DEFAULT);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                image_name = sdf.format(new Date());

            } else {
                if (isCreateScreen)
                    encoded_string = "";
                else if (setEntryModel.getType().equalsIgnoreCase("audio"))
                    encoded_string = "old";
                else
                    encoded_string = "";

            }
        } catch (Exception e) {

        }

        // Check if all strings are null or not
        if (card_name.equals("") || card_name.length() == 0
                || card_description.equals("") || card_description.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), create_cardName,
                    "Both fields are required.");
        } else if (encoded_string.equals("") || encoded_string.length() == 0) {

            if (setEntryModel.getType().equalsIgnoreCase("audio")) {
                encoded_string = "old";
            } else {
                new CustomToast().Show_Toast(getActivity(), text_audioFile,
                        "Audio File is required.");
            }

        }

        // Else do signup or do your stuff
        else {
            getAddCards();
        }
    }

    private void getAddCards() {

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser;
                if (isCreateScreen)
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getAddCardsList("audio", userId, set_id, card_name, card_description, encoded_string, image_name);
                else

                    callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getUpdateCardsList("audio", userId, set_id, setEntryModel.getCard_id(), card_name, card_description, encoded_string, image_name);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                dismissProgress();
                                setAddSetCredentials(addMessageResponse);


                            } else {
                                dismissProgress();

                            }

                        } else {

                            dismissProgress();
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        if (message.equals("timeout")) {
                            showLongToast(getActivity(), "Internet is slow, please try again.");
                        }
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser)
            if (mediaPlayer != null) {
                if (isAudioPlay) {
                    isAudioPlay = false;
                    mediaPlayer.pause();
                }
            }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            if (isAudioPlay) {
                isAudioPlay = false;
                mediaPlayer.pause();
            }
        }
    }
/*public void setMediaPlayer(String filePath) {

        try {
            mediaPlayer = new MediaPlayer();
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


            if (audio_uri != null) {
                mediaPlayer.setDataSource(getContext(), audio_uri);
            } else {
                mediaPlayer.setDataSource(filePath);
            }
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.prepareAsync();
            mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // mp.stop();

                    img_play_stop.setImageResource(R.drawable.play_rec);
                    isAudioPlay = false;
                    mediaPlayer = mp;
                    //   mp.prepareAsync();
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // to get total duration in milliseconds
                    mediaPlayer = mp;
                    long currentDuration = mediaPlayer.getCurrentPosition();
                    TotalTime = TimeFormat.formateMilliSeccond(mediaFileLengthInMilliseconds);
                    String cur_Pos = TimeFormat.formateMilliSeccond(currentDuration);
                    txt_PlayProgTime.setText(cur_Pos + "/" + TotalTime);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/

    private void setAddSetCredentials(AddMessageResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {
            /*Intent intent = new Intent(getActivity(), MySetCards.class);
            intent.putExtra("set_id", set_id);
            intent.putExtra("set_name", set_name);
            intent.putExtra("userId", userId);*/
            release_media();
           /* getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();

            getActivity().overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
            if (isCreateScreen) {
                showShortToast(getActivity(), "Card " + card_name + " has been Created.");
            } else {
                showShortToast(getActivity(), "Card " + card_name + " has been Updated.");
            }
            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
        } else {
            showLongToast(getActivity(), message);
        }
    }

    @Override
    public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list) {

    }

  /*  @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        audio_seek_bar.setSecondaryProgress(percent);
    }*/
}
