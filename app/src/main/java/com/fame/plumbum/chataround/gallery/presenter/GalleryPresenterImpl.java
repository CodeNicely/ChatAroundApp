package com.fame.plumbum.chataround.gallery.presenter;

import com.fame.plumbum.chataround.gallery.OnGalleryApiResponse;
import com.fame.plumbum.chataround.gallery.model.GalleryData;
import com.fame.plumbum.chataround.gallery.provider.GalleryProvider;
import com.fame.plumbum.chataround.gallery.view.GalleryView;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public class GalleryPresenterImpl implements GalleryPresenter {

    private GalleryView galleryView;
    private GalleryProvider galleryProvider;

    public GalleryPresenterImpl(GalleryView galleryView, GalleryProvider galleryProvider) {
        this.galleryView = galleryView;
        this.galleryProvider = galleryProvider;
    }

    @Override
    public void getImages(String userId,String mobile,double latitude, double longitude) {

        galleryView.showLoader(true);

        galleryProvider.getImages(userId,mobile,latitude, longitude, new OnGalleryApiResponse() {
            @Override
            public void onSuccess(GalleryData galleryData) {
                if(galleryData.isSuccess()){

                    galleryView.showLoader(false);
                    galleryView.onGalleryData(galleryData.getImage_list());

                }else{

                    galleryView.showLoader(false);

                }
            }
            @Override
            public void onFailed(String message)
            {

                galleryView.showLoader(false);
                galleryView.showMessage(message);


            }
        });


    }


}
