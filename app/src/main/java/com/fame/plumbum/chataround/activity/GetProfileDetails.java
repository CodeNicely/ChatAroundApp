package com.fame.plumbum.chataround.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fame.plumbum.chataround.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pankaj on 19/8/16.
 */
public class GetProfileDetails extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit);
        final EditText name_edit = (EditText) findViewById(R.id.name);
        final EditText phone_edit = (EditText) findViewById(R.id.phone);

        Button update = (Button) findViewById(R.id.update);
        assert update != null;
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_edit.getText().toString().length() > 9 && name_edit.getText().toString().length() != 0) {
                    String name = name_edit.getText().toString();
                    name = convertToUpperCase(name);
                    String phone = phone_edit.getText().toString();
                    sendData(name, phone);
                } else
                    Toast.makeText(GetProfileDetails.this, "Invalid Entries!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertToUpperCase(String name) {
        if (name!=null && name.length()>0) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            for (int i = 0; ; ) {
                i = name.indexOf(" ", i + 1);
                if (i < 0)
                    break;
                else {
                    if (i < name.length()-2)
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase() + name.substring(i + 2);
                    else if (i == name.length()-2) {
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase();
                        break;
                    }
                }
            }
        }
        return name;
    }

    private void sendData(final String name, final String phone) {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://52.66.45.251/AddProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(GetProfileDetails.this);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("edited", "1");
                                editor.putString("user_name", name.replace("%20", " "));
                                editor.putString("user_phone", phone);
                                editor.apply();
                                Toast.makeText(GetProfileDetails.this, "Data sent!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(GetProfileDetails.this, OTP.class);
                                startActivity(intent);
                                finish();
                            }else if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(GetProfileDetails.this, "Couldnt update entries!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ignored) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GetProfileDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(GetProfileDetails.this);
                params.put("UserId", sp.getString("uid", ""));
                params.put("Mobile", phone);
                params.put("Name", name);
                params.put("IsEditing", "0");
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(GetProfileDetails.this);
        requestQueue.add(myReq);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
