package com.fame.plumbum.chataround.image_upload.view;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.RxSchedulersHook;
import com.fame.plumbum.chataround.image_upload.model.ImageDataProviderImp;
import com.fame.plumbum.chataround.image_upload.model.data.ImageData;
import com.fame.plumbum.chataround.image_upload.presenter.ImagePresenter;
import com.fame.plumbum.chataround.image_upload.presenter.ImagePresenterImpl;
import com.fame.plumbum.chataround.services.UploadService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadImageActivity extends AppCompatActivity implements UploadImageView {

    private static final String TAG = "UploadImageActivity";
    private static final int CAMERA_REQUEST_ID = 1100;
    private static final String FILE_KEY = "IMAGE_KEY";
    private static final String LIST_KEY = "Image_List";
    private final int RESULT_LOAD_IMAGE = 1;
    @BindView(R.id.galleryButton)
    Button galleryButton;
    @BindView(R.id.cameraButton)
    Button cameraButton;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.uploadButton)
    Button uploadButton;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    private File image;
    private boolean CAMERA_REQUEST = false;
    private boolean GALLERY_REQUEST = false;
    private ImageAdapter imageAdapter;
    private ImagePresenter imagePresenter;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        UploadService.ACTIVITY_DESTROYED = false;
//        setSupportActionBar(toolbar);
//        toolbar=getSupportActionBar();
//        toolbar.setTitle(R.string.activity_add_restroom);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_100_percent));
//        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        InitializeObjects();
        Dexter.initialize(getApplicationContext());

        Intent getIntent = getIntent();
     /*   if (getIntent != null) {
            orderId = getIntent.getExtras().getString(Keys.KEY_ORDER_ID);
        }
*/
        Intent uploadServiceIntent = new Intent(UploadImageActivity.this, UploadService.class);
        uploadServiceIntent.putExtra(Keys.KEY_ORDER_ID, orderId);
        getApplicationContext().startService(uploadServiceIntent);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                imagePresenter.openGallery();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                imagePresenter.openCamera();
            }

        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UploadService.ACTIVITY_DESTROYED = true;
        if (UploadService.QUEUE_EMPTY) {
            Intent i = new Intent(this, UploadService.class);
            stopService(i);
        }
    }


    private void InitializeObjects() {
        imagePresenter = new ImagePresenterImpl(this
                , new ImageDataProviderImp(getApplicationContext())
                , new RxSchedulersHook(), EventBus.getDefault());
        imageAdapter = new ImageAdapter(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Uri> uriList = new ArrayList<>();
        switch (requestCode) {
            case CAMERA_REQUEST_ID:
                if (resultCode == Activity.RESULT_OK) {

                    Log.i(TAG, "Result code for Camera : " + resultCode);
                    if (image != null)
                        uriList.add(Uri.fromFile(image));

                }
                break;
            case RESULT_LOAD_IMAGE:

                if (resultCode == Activity.RESULT_OK) {

                    // Clipdata is used for receiving multiple images from Gallery
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {

                            ClipData.Item item = clip.getItemAt(i);

                            uriList.add(item.getUri());

                        }
                    } else {

                        uriList.add(data.getData());
                    }
                }
                break;
            default:
                break;
        }
        if (uriList.size() > 0) {
            imagePresenter.onImagesSelected(uriList);
        }
    }

    @Override
    public void setData(List<ImageData> imageDataList) {
        //    constructImageUploader(imageDataList);
        Log.i(TAG, "Size :" + imageDataList.size());
        imageAdapter.setData(imageDataList);
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCamera() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        Log.i(TAG, image.getPath());

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAMERA_REQUEST_ID);
        }


    }

    @Override
    public void showGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);


    }

    @Override
    public boolean checkPermissionForCamera() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    @Override
    public boolean checkPermissionForGallery() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    @Override
    public boolean requestCameraPermission() {


        Dexter.checkPermissions(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {


                if (multiplePermissionsReport.areAllPermissionsGranted()) {

                    CAMERA_REQUEST = true;
                } else {
                    CAMERA_REQUEST = false;
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        return CAMERA_REQUEST;
    }

    @Override
    public boolean requestGalleryPermission() {

        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */

                GALLERY_REQUEST = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */

                GALLERY_REQUEST = false;
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
        }, Manifest.permission.READ_EXTERNAL_STORAGE);


        return GALLERY_REQUEST;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (image != null) {
            outState.putString(FILE_KEY, image.getPath());
            List<ImageData> imageDataList;
            imageDataList = imagePresenter.getImageDataList();
            JsonArray jsonArray = new JsonArray();

            for (int i = 0; i < imageDataList.size(); i++) {
                jsonArray.add(imageDataList.get(i).toJson());
            }

            outState.putString(LIST_KEY, jsonArray.toString());
        }
//        outState.putString(LIST_KEY, imagePresenter.getImageDataList().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FILE_KEY))
                image = new File(savedInstanceState.getString(FILE_KEY));

            List<ImageData> imageDataList = new ArrayList<>();
            String json = null;
            if (savedInstanceState.containsKey(LIST_KEY)) {
                json = savedInstanceState.getString(LIST_KEY);
            }
            JsonParser parser = new JsonParser();
            JsonElement tradeElement = null;
            if (json != null) {
                tradeElement = parser.parse(json);
            }
            JsonArray trade = null;
            if (tradeElement != null) {
                trade = tradeElement.getAsJsonArray();
            }
            if (trade != null) {
                for (int i = 0; i < trade.size(); i++) {
                    JsonObject jsonObject;
                    jsonObject = trade.get(i).getAsJsonObject();
                    ImageData imageData = new ImageData(jsonObject.get("file").getAsString());
                    imageDataList.add(imageData);
                }
            }

            imagePresenter.setImageDataList(imageDataList);
            imageAdapter.setData(imageDataList);
            imageAdapter.notifyDataSetChanged();
        }
    }
    //    imagePresenter.setImageDataList(savedInstanceState.getString(LIST_KEY));


    public void fileFromPath(String filePath) {

        image = new File(filePath);

    }

}
