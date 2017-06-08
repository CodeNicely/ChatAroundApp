package com.fame.plumbum.chataround.gallery.presenter;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public interface GalleryPresenter {

    void getImages(String userId, String mobile, double latitude, double longitude);
    void onDestroy();
}
