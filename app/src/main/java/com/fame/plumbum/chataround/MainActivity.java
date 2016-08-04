package com.fame.plumbum.chataround;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.fame.plumbum.chataround.models.ImageSendData;
import com.fame.plumbum.chataround.queries.ServerAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    public final String LOG_TAG = getClass().getSimpleName();

    String phone = "", name = "", editing = "0";
    TextView phone_text, name_text;
    CircleImageView user;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Bitmap bitmap;
    File file;
    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

//    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor = sp.edit();
        ImageView edit_button = (ImageView) findViewById(R.id.edit_button);
        ImageView image_button = (ImageView) findViewById(R.id.add_image);
        phone_text = (TextView) findViewById(R.id.phone_text);
        name_text = (TextView) findViewById(R.id.name_text);
        user = (CircleImageView) findViewById(R.id.image_user);
        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, 1);
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogLogout = new Dialog(MainActivity.this);
                dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogLogout.setContentView(R.layout.dialog_edit);
                final EditText name_edit = (EditText) dialogLogout.findViewById(R.id.name);
                final EditText phone_edit = (EditText) dialogLogout.findViewById(R.id.phone);

                Button update = (Button) dialogLogout.findViewById(R.id.update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (phone_edit.getText().toString().length() == 10 && name_edit.getText().toString().length() != 0) {
                            name = name_edit.getText().toString();
                            phone = phone_edit.getText().toString();
                            dialogLogout.dismiss();
                            sendData();
                        } else
                            Toast.makeText(MainActivity.this, "Invalid Entries!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogLogout.show();
            }
        });
        getImage();
        receiveData();
//        if (sp.contains("edited")) {
//            receiveData();
//        }
    }

    private void getImage() {
        Picasso.with(MainActivity.this).load("http://52.66.45.251:8080/ImageReturn?UserId=" + sp.getString("uid", "578b119a7c4ec26dcab64a21")).resize(512, 512).error(R.drawable.user).into(user);
    }
        //3. How To Upload
        void sendImage_one() {
            ServerAPI api = ServerAPI.retrofit.create(ServerAPI.class);

            RequestBody to_server = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(),
                    to_server);

            api.upload(sp.getString("uid", "578b119a7c4ec26dcab64a21"), body).enqueue(new Callback<ImageSendData>() {
                @Override
                public void onResponse(Call<ImageSendData> call, retrofit2.Response<ImageSendData> response) {
                    ImageSendData formSever = response.body();
                    if (formSever.getStatus()==200){
                        Toast.makeText(MainActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                        editor.putString("image", "1");
                        editor.apply();
                    }else{
                        Toast.makeText(MainActivity.this, "Error uploading image!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ImageSendData> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error uploading image!", Toast.LENGTH_SHORT).show();
                    Log.e("TAG_FAILURE", Log.getStackTraceString(t));
                }
            });
        }

    public String getStringImage(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = (float) 0.25;
        float scaleHeight = (float) 0.25;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bmp, 0, 0, width, height, matrix, false);
        bmp = resizedBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //dialogLogout.dismiss();
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, 256, 256);
                    bitmapOptions.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    user.setImageBitmap(bitmap);
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    String iname = String.valueOf(System.currentTimeMillis());
                    File file = new File(path, iname + ".png");

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    Log.e("PATH", getRealPathFromURI(getImageUri(getApplicationContext(), bitmap)));
                    this.file = new File(getRealPathFromURI(getImageUri(getApplicationContext(), bitmap)));
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, bitmapOptions);
                bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, 256, 256);
                bitmapOptions.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(picturePath, bitmapOptions);
                Log.e("path of image", picturePath + "");
                user.setImageBitmap(bitmap);
            }
//            sendImage();
            try {
                sendImage_one();
            } catch (Exception e) {
                Log.e("TAG_ActivityResult", Log.getStackTraceString(e));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String idxx = cursor.getString(idx);
        cursor.close();
        return idxx;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private void receiveData() {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251:8080/GetProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            Log.e("response", jO.getString("Status"));
                            if (jO.getString("Status").contentEquals("200")){
                                editor.putString("user_name", jO.getString("Name"));
                                Log.e("user_name", jO.getString("Name"));
                                editor.apply();
                                name_text.setText(jO.getString("Name"));
                                phone_text.setText(jO.getString("Mobile"));
                            }else{
                                //  if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(MainActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.getStackTraceString(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.getStackTraceString(error);
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                params.put("UserId", sp.getString("uid", null));
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }

    private void sendData() {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251:8080/AddProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("addprofile_1", "gotr in repsonse");
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                Log.e("ADDPROFILE", jO.getString("Status"));
                                name_text.setText(name);
                                phone_text.setText(phone);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("edited", "1");
                                editor.putString("user_name", name);
                                editor.apply();
                                Toast.makeText(MainActivity.this, "Data sent!", Toast.LENGTH_SHORT).show();
                            }else if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(MainActivity.this, "Couldnt update entries!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.getStackTraceString(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.getStackTraceString(error);
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                Log.e("TAG", sp.getString("uid", null)+" "+ phone+" "+ name+" "+ editing);
                params.put("UserId", sp.getString("uid", null));
                params.put("Mobile", phone.replace(" ", "%20"));
                params.put("Name", name.replace(" ", "%20"));
                params.put("IsEditing", editing);
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout){
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}