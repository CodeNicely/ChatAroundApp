package com.fame.plumbum.chataround.add_photos.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.fame.plumbum.chataround.add_photos.model.data.ImageData;
import com.fame.plumbum.chataround.helper.utils.BitmapUtils;
import com.fame.plumbum.chataround.helper.utils.FileUtils;
import com.fame.plumbum.chataround.helper.utils.UriUtils;

import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.functions.Func1;

/**
 * This class is implemetation of {@link ImageDataProvider}
 * Created by Meghal on 5/24/2016.
 */
public class ImageDataProviderImp implements ImageDataProvider {


    private static final String TAG = "Bitmap Loader";
    private Context context;

    public ImageDataProviderImp(Context context) {
        this.context = context;
    }

    @Override
    public Observable<ImageData> getImageData(final Uri uri) {

        /*
        return UriUtils.UriToFilePathObservable(context, uri)
                .flatMap(new Func1<String, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(String s) {
                        return BitmapUtils.filePathToBitmapObservable(s);
                    }
                }).flatMap(new Func1<Bitmap, Observable<ImageData>>() {
                    @Override
                    public Observable<ImageData> call(final Bitmap bitmap) {
                        return Observable.just(new ImageData(bitmap,
                                FileUtils.BitmapToFileConverter(context, bitmap).toString()));
                    }
                });
*/

        return UriUtils.UriToFilePathObservable(context, uri).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                try {
                    return BitmapUtils.filePathToBitmapConverter(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).map(new Func1<Bitmap, File>() {
            @Override
            public File call(Bitmap bitmap) {
                File file = FileUtils.BitmapToFileConverter(context, bitmap);
                bitmap.recycle();
                return file;
            }
        }).map(new Func1<File, String>() {
            @Override
            public String call(File file) {

                return file.getPath();
            }
        }).map(new Func1<String, ImageData>() {
            @Override
            public ImageData call(String s) {
                return new ImageData(s);
            }
        });

    }

}



        /*
        observable = Observable.create(new Observable.OnSubscribe<List<ImageData>>() {
            @Override
            public void call(Subscriber<? super List<ImageData>> subscriber) {

                for (int i = 0; i < uriList.size(); i++) {

                    Observable<String> uriObservable;

                    uriObservable = uriUtils.UriToFilePathObservable(context, uriList.get(i));

                    uriObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String filePath) {

                            Observable<Bitmap> bitmapObservable;

                            bitmapObservable = bitmapUtils.filePathToBitmapObservable(filePath);

                            bitmapObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Bitmap>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Bitmap bitmap) {

                                    ImageData imageData = new ImageData(bitmap);
                                    imageDataList.add(imageData);

                                }
                            });


                        }
                    });


                }

                subscriber.onNext(imageDataList);
                subscriber.onCompleted();
            }
        });
*/



/*
    public Bitmap ConvertUriToBitmap(Uri uri) throws IOException {

        String filePath;
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        filePath = getPath(imageViewerImpl, uri);
        BitmapFactory.decodeFile(filePath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        String imageType = options.outMimeType;

        BitmapFactory.Options options2 = new BitmapFactory.Options();
    //    Log.i(TAG, "Image Details before Conversion is : \n Height :" + height + "\nWidth : " + width + "\n Image Type : " + imageType);

        options2.inSampleSize = 2;  //Math.max(options.outWidth / width, options.outHeight / height);

        is = new FileInputStream(filePath);
        return BitmapFactory.decodeStream(is, null, options2);
    }
*/
// imageBitmap=Bitmap.createScaledBitmap(bitmap,400,800,true);

//     Bitmap bitmap = MediaStore.Images.Media.getBitmap(imageViewerImpl.getContentResolver(), uri);


/*
  @Override
    public void filePathToBitmap(List<String> filePath,BitmapLoaderCallback bitmapLoaderCallback) {

        List<Bitmap> imageBitmapList=new ArrayList<>();

        for(int i=0;i<filePath.size();i++) {
        }
    }

    @Override
    public List<ImageData> uriToFile(List<Uri> uri) {

        List<String> filePathList=new ArrayList<>();

        for(int i=0;i<uri.size();i++) {

                   filePathList.add(getPath(imageViewerImpl,uri.get(i)));
        }
        //filePathLoaderCallback.onPathLoadingSuccessful(filePathList);

        return null;


          }

*/



/*
    public Bitmap ConvertUriToBitmap(Uri uri) throws IOException {

// using PArcel file Discriptor
        ParcelFileDescriptor parcel = null;

        parcel = imageViewerImpl.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor imageSource = parcel.getFileDescriptor();


        String filePath;
        filePath = uri.getPath();

        //     filePath=uri.getPath();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFileDescriptor(imageSource, Rect.unflattenFromString("r"), options);

        int height = options.outHeight;
        int width = options.outWidth;

        String imageType = options.outMimeType;

        Log.i(TAG, "Image Details before Conversion is : \n Height :" + height + "\nWidth : " + width + "\n Image Type : " + imageType);

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(imageViewerImpl.getContentResolver(), uri);

        return bitmap;

    }
*/

