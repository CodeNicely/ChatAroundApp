package com.fame.plumbum.chataround.add_restroom.model.data;

/**
 * This class is for EventBus library
 * This is a event and it is used to notify service of new image directly from Presenter.
 * <p>
 * Created by meghal on 7/7/16.
 */
public class ImageEvent {

    String file;

    public ImageEvent(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
