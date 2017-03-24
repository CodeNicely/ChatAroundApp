package com.fame.plumbum.chataround.add_restroom.model;

import android.util.Log;


import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.add_restroom.UploadCallback;
import com.fame.plumbum.chataround.add_restroom.api.ImageUploadApi;
import com.fame.plumbum.chataround.add_restroom.model.data.FileUploadData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is retrofit client implementation of calling api
 * it implements {@link FileUploader}
 * Created by Meghal on 5/27/2016.
 */
public class RetrofitFileUploader implements FileUploader {

    private static final String TAG = "RetrofitFileUploader";
    private Retrofit retrofit;
    private HttpLoggingInterceptor interceptor;
    private ImageUploadApi imageUploadApi;

    /**
     * In constructor we are initializing HttpLoggingInterceptor and Retrofit
     * to optimize code , as we dont want the variable to get initialized again and again
     */
    public RetrofitFileUploader() {
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true)
                .connectTimeout(2, TimeUnit.MINUTES).writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES).addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder().baseUrl(Urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        imageUploadApi = retrofit.create(ImageUploadApi.class);
    }

    @Override
    public void uploadImage(String user_id, String restroom_id, File file, final UploadCallback uploadCallback) {

/*
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://photoshoto.me")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody photo = RequestBody.create(MediaType.parse("application/image"), file);
        RequestBody body = new MultipartBody.Builder()
                .type(MultipartBody.FORM)
                .addFormDataPart("photo", file.getUsername(), photo)
                .build();

        Call<JsonObject> call = userService.changeUserPhoto(token, user_id, body);

        */


        Log.i(TAG, "Retrofit file uploader called ");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Keys.KEY_ADMIN_TOKEN, user_id);
            jsonObject.put(Keys.KEY_ORDER_ID, restroom_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        RequestBody userId =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), user_id);
        RequestBody restroomId =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), String.valueOf(restroom_id));

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), fbody);


        Call<FileUploadData> call = imageUploadApi.uploadImage(userId, restroomId, body);
        call.enqueue(new Callback<FileUploadData>() {

            @Override
            public void onResponse(Call<FileUploadData> call, Response<FileUploadData> response) {

                Log.i(TAG, "On Response" + response.message() + response.body());

                uploadCallback.onUploadSuccess(response.body());

            }

            @Override
            public void onFailure(Call<FileUploadData> call, Throwable t) {

                Log.i(TAG, "On Failure");

                uploadCallback.onUploadFailed();
                t.printStackTrace();

            }
        });

    }
}
