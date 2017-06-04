package com.fame.plumbum.chataround.add_photos.model.data;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

/**
 * This class is a POJO for ImageData
 * Created by Meghal on 5/24/2016.
 */
public class ImageData {

    @Expose
    private String file;

    private boolean footer;

    public ImageData() {

    }

    public ImageData(String file, boolean footer) {
        this.file = file;
        this.footer = footer;
    }

    public ImageData(String file) {
        this.file = file;
        this.footer=false;
    }

    public static ImageData fromJson(JsonObject jsonObject) {

        ImageData imageData = new ImageData();
        imageData.file = jsonObject.get("file").getAsString();
        return imageData;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public String getFile() {
        return file;
    }

    /**
     * This method is useful for convertinf imageData to json form.
     * Json form is required in various places like when passing on data using intern we cannot as
     * file directly we can however send json.
     *
     * @return
     */
    public JsonObject toJson() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("file", file);
        return jsonObject;
    }

}
