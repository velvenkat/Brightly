package com.digital_easy.info_share.API;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiCallback<T> implements Callback<T> {

    Activity context;

    public ApiCallback(final Activity context) {
        this.context = context;
    }

    public abstract void onApiResponse(Response<T> response, boolean isSuccess, String message);

    public abstract void onApiFailure(boolean isSuccess, String message);

    @Override
    public void onResponse(Call<T> call, final Response<T> response) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response.isSuccessful()) {
                    onApiResponse(response, true, "");
                } else {
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody().string();
                        JSONObject errorBodyObject = new JSONObject(errorBody);
                        onApiFailure(false, errorBodyObject.optString("message"));
                    } catch (IOException e) {
                        onApiFailure(false, response.message());
                    } catch (JSONException e) {
                        onApiFailure(false, response.message());
                    }

                }
            }
        });
    }

    @Override
    public void onFailure(Call<T> call, final Throwable t) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onApiFailure(false, t.getMessage());
            }
        });

    }
}
