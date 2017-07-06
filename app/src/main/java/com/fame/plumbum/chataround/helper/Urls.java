package com.fame.plumbum.chataround.helper;

import com.fame.plumbum.chataround.BuildConfig;

/**
 * Created by meghal on 19/2/17.
 */

public class Urls {
    //public static final String BASE_URL_RESTROOM = "http://www.refugerestrooms.org:80/";

    public static final String SUB_URL_RESTROOM = "RestroomList";
    public static final String SUB_URL_IMAGE_UPLOAD_API = "AddRestroomImage";
    public static final String SUB_URL_ADD_RESTROOM = "AddRestroom";
    public static final String SUB_URL_NEWS_LIST ="NewsList" ;
    public static final String SUB_URL_SPLASH_SCREEN ="SplashScreen" ;
    public static final String SUB_URL_SHOUTS = "ShowPost";
    public static final String SUB_URL_GALLERY_API ="ShowGallery" ;
    public static final String SUB_URL_PHOTO_UPLOAD_API ="AddPhoto" ;
    public static final String SUB_URL_ADD_REFERRAL ="AddReferral";
    public static final String SUB_URL_VERIFY_DEVICE = "VerifyDevice";
    public static final String SUB_URL_EMERGENCY_CONTACTS="EmergencyContacts";
    public static final String SUB_URL_ADD_CONTACTS ="AddContacts" ;



    //    public static String BASE_URL = "http://52.66.45.251:5000/"; // AWS
//    public static String BASE_URL = "http://139.59.30.208:5000/"; // Digital Ocean

    public static final String BASE_URL = BuildConfig.BASE_URL;


}

