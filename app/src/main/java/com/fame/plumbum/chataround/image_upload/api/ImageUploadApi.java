package com.fame.plumbum.chataround.image_upload.api;


import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.image_upload.model.data.FileUploadData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * This is api for uploading images to server and it is called from {@link UploadService}
 * Created by Meghal on 5/27/2016.
 */
public interface ImageUploadApi {
    /**
     * We are using Multipart annotation here for image uploading.
     *
     * @param admin_token is unique admin token for admin verification.
     * @param orderId     orderId with which admin wants to attach image.
     * @param file        is Image file to be uploaded
     * @return
     */
    @Multipart
    @POST(Urls.SUB_URL_IMAGE_UPLOAD_API)
    Call<FileUploadData> uploadImage(@Part("admin_token") RequestBody admin_token,
                                     @Part("order_id") RequestBody orderId, @Part MultipartBody.Part file);
}
