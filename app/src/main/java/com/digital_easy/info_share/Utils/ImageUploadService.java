package com.digital_easy.info_share.Utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.digital_easy.info_share.API.RestApiMethods;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Modules.AddChannelResponse;
import com.digital_easy.info_share.R;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ImageUploadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    Context scrn_contxt;
    Notification.Builder notificationBuilder;

    public ImageUploadService() {
        super("Image_Uploader");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Bundle bundle = intent.getExtras();
        String user_id = bundle.getString("user_id");
        String chl_name = bundle.getString("chl_name");
        String chl_desc = bundle.getString("chl_desc");
        String encoded_str = bundle.getString("encoded_str");
        String img_name = bundle.getString("img_name");


        Integer notificationID = 100;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//Set notification information:
        notificationBuilder = new Notification.Builder(getApplicationContext());
        notificationBuilder.setOngoing(true)
                .setContentTitle("Brightly Image Upload")
                .setContentText("Image upload in progress")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true);

//Send the notification:
        Notification notification = notificationBuilder.build();
        notificationManager.notify(notificationID, notification);

        Scheduler intentScheduler = AndroidSchedulers.from(Looper.myLooper());
        RestApiMethods requestInterface = RetrofitInterface.getRestApiMethods(this);



        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(requestInterface.getAddChannels(user_id, chl_name, chl_desc, encoded_str, img_name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableObserver<AddChannelResponse>() {
                    @Override
                    public void onNext(AddChannelResponse genResModel) {
                        // dialog.dismiss();
                        //dismissProgress();
                        //AddChannelResponse addChannelResponse = genResModel.body();


                        //setAddChannelCredentials(genResModel);
                        // dismissProgress();
                        Log.e("Success", "Error");
                        notificationBuilder.setOngoing(false);
                        notificationBuilder.setProgress(100, 100, false);
                        notificationBuilder.setContentText("Upload Complete");
                        Notification notification = notificationBuilder.build();
                        notificationManager.notify(notificationID, notification);

                    }

                    @Override
                    public void onError(Throwable e) {
                        //dialog.dismiss();
                        //dismissProgress();
                        //     Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Error Upload Service", "Error" + e.getMessage());
                        notificationBuilder.setOngoing(false);
                        notificationBuilder.setProgress(100, 100, false);
                        notificationBuilder.setContentText("Upload Error");
                        Notification notification = notificationBuilder.build();
                        notificationManager.notify(notificationID, notification);

                    }

                    @Override
                    public void onComplete() {
                        //dialog.dismiss();
                        //    dismissProgress();
                        /*notificationBuilder.setOngoing(false);
                        notificationBuilder.setProgress(100,100,false);*/
                    }
                }));



        //Observable<AddMessageResponse> sd=mCompositeDisposable.add()

    }

}

