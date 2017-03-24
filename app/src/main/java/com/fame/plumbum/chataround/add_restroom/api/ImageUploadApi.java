package com.fame.plumbum.chataround.add_restroom.api;


import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.add_restroom.model.data.FileUploadData;
import com.fame.plumbum.chataround.services.UploadService;

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
     * @param user_id    is unique admin token for admin verification.
     * @param restroom_id restroom_id with which admin wants to attach image.
     * @param file        is Image file to be uploaded
     * @return
     */
    @Multipart
    @POST(Urls.SUB_URL_IMAGE_UPLOAD_API)
    Call<FileUploadData> uploadImage(@Part("userId") RequestBody user_id,
                                     @Part("restroomId") RequestBody restroom_id, @Part MultipartBody.Part file);
}
