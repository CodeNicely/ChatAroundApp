package com.fame.plumbum.chataround.gallery;

import com.fame.plumbum.chataround.gallery.model.GalleryData;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public interface OnGalleryApiResponse {

    void onSuccess(GalleryData galleryData);

    void onFailed(String message);

}
