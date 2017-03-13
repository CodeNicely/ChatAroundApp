package com.fame.plumbum.chataround.splash_screen.view;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.splash_screen.presenter.SplashScreenPresenter;

import butterknife.BindView;

public class SplashScreenActivity extends AppCompatActivity {
    @BindView(R.id.one_mile_logo)
    ImageView one_mile_logo;
    @BindView(R.id.tag_line)
    TextView tag_line;
    @BindView(R.id.codeNicelyLogo)
    ImageView codenicely_logo;
    private Handler handler;
    private SplashScreenPresenter splashScreenPresenter;
    private SharedPrefs sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
}
