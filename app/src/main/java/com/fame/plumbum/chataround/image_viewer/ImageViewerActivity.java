package com.fame.plumbum.chataround.image_viewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.Keys;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity is for showing images in a viewpager
 * The images displayed are fetched from image url
 */
public class ImageViewerActivity extends AppCompatActivity {

    private static final String TAG = "Image Viewer Activity";
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.counter)
    TextView counter;
    private ImageViewerPagerAdapter imageViewerPagerAdapter;
    private ArrayList<String> imageUrlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            imageUrlList = intent.getStringArrayListExtra(Keys.KEY_IMAGE_LIST);
        }
        imageViewerPagerAdapter = new ImageViewerPagerAdapter(this,
                imageUrlList);
        initialize();
        assert intent != null;
        viewPager.setCurrentItem(intent.getIntExtra(Keys.KEY_POSITION_IMAGE, 0));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                counter.setText(String.valueOf(position + 1));
                counter.append(" Out Of ");
                counter.append(String.valueOf(imageUrlList.size()));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * This method is for initialization of widgets and class objects
     */
    void initialize() {

        viewPager.setAdapter(imageViewerPagerAdapter);
        imageViewerPagerAdapter.notifyDataSetChanged();

    }
    protected void enableScroll(GestureImageView gestureView){
        gestureView.getController().enableScrollInViewPager(viewPager);

    }
/*
    protected void enableScroll(GestureImageView gestureView){
        gestureView.getController().enableScrollInViewPager(viewPager);

    }
*/

}
