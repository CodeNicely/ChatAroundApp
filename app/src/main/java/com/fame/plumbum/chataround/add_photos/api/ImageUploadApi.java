package com.fame.plumbum.chataround.add_photos.api;


import com.fame.plumbum.chataround.add_photos.model.data.ImageUploadData;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.services.UploadRestroomImageService;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * This is api for uploading images to server and it is called from {@link UploadRestroomImageService}
 * Created by Meghal on 5/27/2016.
 */
public interface ImageUploadApi {
    /**
     * We are using Multipart annotation here for image uploading.
     *
     * @param user_id is unique admin token for admin verification.
     * @param userMobile  mobile of user in which he want paytm cash
     * @param file    is Image file to be uploaded
     * @return
     */
    @Multipart
    @POST(Urls.SUB_URL_PHOTO_UPLOAD_API)
    Call<ImageUploadData> uploadImage(@Part("userId") RequestBody user_id,
                                      @Part("userMobile") RequestBody userMobile,
                                      @Part("latitude") RequestBody latitude,
                                      @Part("longitude") RequestBody longitude,
                                      @Part MultipartBody.Part file);

}