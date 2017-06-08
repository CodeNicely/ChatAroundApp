package com.fame.plumbum.chataround.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.fame.plumbum.chataround.activity.MainActivity;

/**
 * Created by meghalagrawal on 08/06/17.
 */

public abstract class BaseActivity extends Activity {


    private static final int MOCK_LOCATION_OFF_REQUEST = 201;

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(requestCode==MOCK_LOCATION_OFF_REQUEST){

            Intent intent=new Intent(BaseActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean isMockLocation(Location location){
        if(location.isFromMockProvider()){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Turn off mock location");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS), MOCK_LOCATION_OFF_REQUEST);
                    Toast.makeText(BaseActivity.this, "Toast", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.show();
            return true;
        }else{
            return false;
        }
    }



}
