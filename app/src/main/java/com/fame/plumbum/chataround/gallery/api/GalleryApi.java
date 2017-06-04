package com.fame.plumbum.chataround.gallery.api;

import com.fame.plumbum.chataround.gallery.model.GalleryData;
import com.fame.plumbum.chataround.helper.Urls;
import com.fame.plumbum.chataround.news.model.NewsListData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public interface GalleryApi {

    @GET(Urls.SUB_URL_GALLERY_API)
    Call<GalleryData> getGalleryImages(@Query("userId") String userId,
                                       @Query("modile") String mobile,
                                       @Query("latitude") double latitude,
                                       @Query("longitude") double longitude

    );

}
