package com.fame.plumbum.chataround;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pankaj on 10/7/16.
 */
public class SplashScreen extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains("uid")) {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
