package com.fame.plumbum.chataround.add_photos.model.data;

/**
 * This class is for EventBus library
 * This is a event and it is used to notify service of new image directly from Presenter.
 * <p>
 * Created by meghal on 7/7/16.
 */
public class PhotoEvent {

    String file;

    public PhotoEvent(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
