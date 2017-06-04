package com.fame.plumbum.chataround.gallery.model;

import java.util.List;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public class GalleryData {

    private boolean success;
    private String message;
    private List<ImageData> image_list;

    public GalleryData(boolean success, String message, List<ImageData> image_list) {
        this.success = success;
        this.message = message;
        this.image_list = image_list;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<ImageData> getImage_list() {
        return image_list;
    }
}
