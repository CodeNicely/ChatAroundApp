package com.fame.plumbum.chataround.gallery.provider;

import com.fame.plumbum.chataround.gallery.OnGalleryApiResponse;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public interface GalleryProvider {

    void getImages(String userId,String mobile,double latitude, double longitude, OnGalleryApiResponse onGalleryApiResponse);

}
