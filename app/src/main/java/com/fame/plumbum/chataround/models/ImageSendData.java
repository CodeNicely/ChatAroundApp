package com.fame.plumbum.chataround.models;

public class ImageSendData {
    // Write names of all arguments which are being returned on calling the url for image upload
    // Make sure of the case and name of all variables. It should be exactly same as that being returned.
            String Message;
            int Status;
            String filename;

            public int getStatus() {
                return Status;
            }

            public String getMessage() {
                return Message;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public void setStatus(int status) {
                Status = Status;
            }

            public void setMessage(String message) {
                Message = Message;
            }
        }