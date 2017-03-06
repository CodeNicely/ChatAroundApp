package com.fame.plumbum.chataround.image_upload.model.data;


import com.fame.plumbum.chataround.image_upload.api.ImageUploadApi;

/**
 * This class is a POJO for Response data from {@link ImageUploadApi}
 * Created by meghal on 2/7/16.
 */
public class FileUploadData {

    private boolean success;
    private String message;

    /**
     * @param success if success is true image is uploaded successfully.
     * @param message messages are indicators with reason of failure of operation when success is false
     */
    public FileUploadData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }


    public String getMessage() {
        return message;
    }
}
