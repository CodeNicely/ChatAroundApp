package com.fame.plumbum.chataround.gallery.presenter;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.gallery.OnGalleryApiResponse;
import com.fame.plumbum.chataround.gallery.model.GalleryData;
import com.fame.plumbum.chataround.gallery.provider.GalleryProvider;
import com.fame.plumbum.chataround.gallery.view.GalleryView;
import com.fame.plumbum.chataround.helper.Keys;

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
    public void getImages(final String userId, final String mobile, final double latitude, final double longitude) {

        galleryView.showLoader(true);

        galleryProvider.getImages(userId, mobile, latitude, longitude, new OnGalleryApiResponse() {
            @Override
            public void onSuccess(GalleryData galleryData) {
                galleryView.showLoader(false);

                if (galleryData != null) {
                    if (galleryData.isSuccess()) {

                        galleryView.onGalleryData(galleryData.getImage_list());

                    } else {

                        Answers.getInstance().logCustom(new CustomEvent("Gallery Module Loading Failed - Server end")
                                .putCustomAttribute(Keys.KEY_USER_ID, userId)
                                .putCustomAttribute(Keys.KEY_LATITUDE, latitude)
                                .putCustomAttribute(Keys.KEY_LONGITUDE, longitude)
                                .putCustomAttribute(Keys.KEY_USER_MOBILE, mobile)

                        );

                    }
                }else{
                    Answers.getInstance().logCustom(new CustomEvent("Gallery Module Loading Failed - Null data")
                            .putCustomAttribute(Keys.KEY_USER_ID, userId)
                            .putCustomAttribute(Keys.KEY_LATITUDE, latitude)
                            .putCustomAttribute(Keys.KEY_LONGITUDE, longitude)
                            .putCustomAttribute(Keys.KEY_USER_MOBILE, mobile)

                    );
                }
            }

            @Override
            public void onFailed(String message) {

                Answers.getInstance().logCustom(new CustomEvent("Gallery Module Loading Failed - Local")
                        .putCustomAttribute(Keys.KEY_USER_ID, userId)
                        .putCustomAttribute(Keys.KEY_LATITUDE, latitude)
                        .putCustomAttribute(Keys.KEY_LONGITUDE, longitude)
                        .putCustomAttribute(Keys.KEY_USER_MOBILE, mobile)

                );

                galleryView.showLoader(false);
                galleryView.showMessage(message);


            }
        });


    }

    @Override
    public void onDestroy() {
        if (galleryProvider != null) {
            galleryProvider.onDestroy();
        }
    }


}
