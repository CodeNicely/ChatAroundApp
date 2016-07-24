package com.fame.plumbum.chataround.models;

public class ImageSendData {
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