package com.purplefront.brightly.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.purplefront.brightly.Custom.SiriWaveView;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.TimeFormat;

import java.io.File;
import java.io.IOException;

/**
 * Created by Niranjan Reddy on 10-04-2018.
 */

public class RecordVoice extends BaseFragment {
    SiriWaveView waveView;
    MediaRecorder myAudioRecorder = null;
    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 1;
    View rootView;
    ImageView img_play_stop_rec,cur_default;
    TextView txtRecSeconds;
    String PetId, PetIMG;
    CountDownTimer countDownTimer = null;
    File tempMp3File;
    boolean isPlayBtnClicked = false;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof InterfaceUserModel) {
            usrModelobj = ((InterfaceUserModel) context).getUserModel();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();
        }
        if (context instanceof FragmentCallInterface) {
            fragmentCall_mainObj = ((FragmentCallInterface) context).Get_GenFragCallMainObj();
        }*/

    }

  /*  @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState!=null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "RECORDVOICE_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "rec_voice");


            fragmentTransaction.addToBackStack("rec_voice");

            fragmentTransaction.commit();

        }
    }*/


    /*@Override
    public void onSaveInstanceState(Bundle outState) {


        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "RECORDVOICE_STATE", this); super.onSaveInstanceState(outState);
    }*/



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_rec_voice, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);
        waveView = (SiriWaveView) rootView.findViewById(R.id.siriWaveView);
        img_play_stop_rec = (ImageView) rootView.findViewById(R.id.img_play_stop_rec);
        txtRecSeconds = (TextView) rootView.findViewById(R.id.txtRecSeconds);
        cur_default=(ImageView)rootView.findViewById(R.id.cur_default);
        Bundle bundle = getArguments();
        PetId = bundle.getString("PetID");
        PetIMG = bundle.getString("PetImage");


        waveView.stopAnimation();
/*

        // creates temporary file
        actionBarUtilObj.setViewInvisible();
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.setTitle("Back");

        actionBarUtilObj.getTitle().setGravity(Gravity.CENTER_VERTICAL);
        actionBarUtilObj.getTitle().setClickable(true);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarUtilObj.getTitle().setClickable(false);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    myAudioRecorder.release();
                }
               // ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();
            }
        });

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     AlertDialogHandler.showDialog(getContext(),"Do you want to exit?",true);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    myAudioRecorder.release();
                }
              //  ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                hide_fragment();

            }
        });
*/
        img_play_stop_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlayBtnClicked) {
                    checkPermissionsAndStart();

                } else {

                    isPlayBtnClicked = false;
                    waveView.stopAnimation();
                    Rec_finish();

                }
            }
        });


        try {

            tempMp3File = File.createTempFile("audio_bark", ".3gp", getActivity().getCacheDir());

            // prints absolute path
            //  System.out.println("File path: " + f.getAbsolutePath());

            // deletes file when the virtual machine terminate
            tempMp3File.deleteOnExit();


        } catch (Exception e) {

            e.printStackTrace();
        }
        return rootView;
    }

/*
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionBarUtilObj.getTitle().setClickable(false);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        myAudioRecorder.release();
                    }
                    // ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    hide_fragment();
                }
            });

            actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //     AlertDialogHandler.showDialog(getContext(),"Do you want to exit?",true);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        myAudioRecorder.release();
                    }
                    //  ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    hide_fragment();

                }
            });
            cur_default.setVisibility(View.VISIBLE);
            waveView.stopAnimation();
            img_play_stop_rec.setImageResource(R.drawable.record_voice);
        }
    }
*/

    public void hide_fragment() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev_frag = null;

        prev_frag = getActivity().getSupportFragmentManager().findFragmentByTag("frag_act");
        //Fragment frag_feed = getActivity().getSupportFragmentManager().findFragmentByTag("frag_feed");
        //  Toast.makeText(getContext(), "I am ", Toast.LENGTH_LONG).show();
        if (prev_frag.isAdded()) { // if the fragment is already in container
            //    Toast.makeText(getContext(), "I am Here", Toast.LENGTH_LONG).show();
            ft.remove(this);
            ft.show(prev_frag);

            ft.commit();
        }

    }

    public void Rec_finish() {
        countDownTimer.cancel();
        if (myAudioRecorder != null) {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            Bundle bundle = new Bundle();
            bundle.putString("filePath", tempMp3File.getAbsolutePath());
            bundle.putString("PetID", PetId);
            bundle.putString("PetImage", PetIMG);
            bundle.putString("scrn_from","Rec_voice");
           // fragmentCall_mainObj.Fragment_call(this,new Create_PetActivity(), "act_crt", bundle);

        }
    }

   // @Override
   /* public void onResume() {
        super.onResume();
        // AlertDialogHandler._fragment_handelBackKey(rootView,getContext(),"",false);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        myAudioRecorder.release();
                    }
                   // ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    hide_fragment();

                    return true;
                }
                return false;
            }
        });
    }*/

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
                checkPermissionsAndStart();
        }

    }

    private void checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSION_RECORD_AUDIO);
        } else {
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
    }

}