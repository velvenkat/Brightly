package com.purplefront.brightly.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.Activities.BaseActivity;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.TimeFormat;

public class BaseFragment extends Fragment implements MediaPlayer.OnBufferingUpdateListener {

    Context context;
    ProgressDialog mProgress;
    alert_dlg_interface mListener;
    SeekBar audio_seek_bar;
    MediaPlayer mediaPlayer;
    boolean  isAudioPlay = false;
    String TotalTime;
    private final Handler handler = new Handler();
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    ImageView img_play_stop;
    TextView txt_PlayProgTime;
    boolean isPlayBtnClicked = false;


    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void setDlgListener(alert_dlg_interface listener){
        mListener=listener;
    }


    public void showAlertDialog(String message,String Title,String Pos_Title,String Neg_Title)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(Title);

        // Setting Dialog Message
        //alertDialog.setMessage("You are about to delete the Set. All the information contained in the Sets will be lost. ");
        alertDialog.setMessage(message);
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(Pos_Title, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                dialog.dismiss();
                mListener.postive_btn_clicked();
                //getDeleteSet();
                // Write your code here to invoke YES event
//                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(Neg_Title, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
//                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                mListener.negative_btn_clicked();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }



    /**
     * @param fragment fragment transaction for all fragments
     */
    protected void fragmentTransaction(BaseFragment fragment) {
        fragmentTransaction(fragment, false);
    }

    /**
     * @param fragment fragment transaction for all fragments
     */
    protected void fragmentTransaction(BaseFragment fragment, boolean allowStateLoss) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
        fragmentTransaction.replace(R.id.frameContainer, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
//    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (allowStateLoss) {
            fragmentTransaction.commitAllowingStateLoss();
        } else
            fragmentTransaction.commit();
    }

    public void audio_player_initialize(SeekBar seekBar,TextView txtProgTime,ImageView imgPlyStop){
        audio_seek_bar=seekBar;
        txt_PlayProgTime=txtProgTime;
        img_play_stop=imgPlyStop;
        setSeek_Bar();
        setImg_play_stop_Listener();

    }
    public void setImg_play_stop_Listener(){
        img_play_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAudioPlay) {
                    mediaPlayer.pause();
                    img_play_stop.setImageResource(R.drawable.play_rec);
                    isAudioPlay = false;
                    //mediaPlayer.prepareAsync();
                } else {

                    img_play_stop.setImageResource(R.drawable.stop_rec);

                    mediaPlayer.start();
                    isAudioPlay = true;
                    primarySeekBarProgressUpdater();

                }
            }
        });
    }

    public void setSeek_Bar(){

        audio_seek_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (v.getId() == R.id.seek_audio_rec) {
                        /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
                        if (mediaPlayer.isPlaying()) {
                            SeekBar sb = (SeekBar) v;
                            int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                            mediaPlayer.seekTo(playPositionInMillisecconds);
                        } else {
                            img_play_stop.setImageResource(R.drawable.stop_rec);

                            mediaPlayer.start();
                            SeekBar sb = (SeekBar) v;
                            int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                            mediaPlayer.seekTo(playPositionInMillisecconds);
                            isAudioPlay = true;
                            primarySeekBarProgressUpdater();

                        }
                    }
                }
                return false;
            }
        });


    }

    public void release_media() {
        if (mediaPlayer != null) {
            if (isAudioPlay) {
                isAudioPlay = false;
                mediaPlayer.pause();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void primarySeekBarProgressUpdater() {
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


    public void setMediaPlayer(Uri audio_uri,String filePath) {

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


    }

    // Intent Methods
    public void simpleIntent(Activity activity, Class activityClass)
    {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
    }
    public void stackClearIntent(Activity activity, Class activityClass)
    {
        Intent intent = new Intent(activity, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void finishIntent(Activity activity, Class activityClass)
    {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        activity.finish();

    }

    public void frwdAnimIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
    }

    public void backAnimIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    //Show Toast Methods
    public void showLongToast(Activity activity, String massage )
    {
        Toast.makeText( activity, massage, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(Activity activity, String massage )
    {
        Toast.makeText( activity, massage, Toast.LENGTH_LONG).show();
    }


    /**
     * show progress dialog while api calls
     */
    protected void showProgress() {
        try {
            mProgress = new ProgressDialog(getActivity());
            mProgress.setIndeterminate(false);
            mProgress.setTitle("Sending....");
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setCancelable(true);
            mProgress.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dissmiss progresss dialog for api calls
     */
    protected void dismissProgress() {
        try {
            mProgress.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface alert_dlg_interface{
        public void postive_btn_clicked();
        public void negative_btn_clicked();
    }
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        audio_seek_bar.setSecondaryProgress(percent);
    }
}

