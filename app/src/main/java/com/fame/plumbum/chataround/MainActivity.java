package com.fame.plumbum.chataround;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String phone = "", name = "", editing = "0";
    TextView phone_text, name_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        FloatingActionButton edit_button = (FloatingActionButton) findViewById(R.id.edit_button);
        phone_text = (TextView)findViewById(R.id.phone_text);
        name_text = (TextView) findViewById(R.id.name_text);
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
                        }else
                            Toast.makeText(MainActivity.this, "Invalid Entries!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogLogout.show();
            }
        });
    }

    private void sendData() {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://ec2-52-66-45-251.ap-south-1.compute.amazonaws.com:8080/AddProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            if (jO.getString("Status").contentEquals("200")){
                                name_text.setText(name);
                                phone_text.setText(phone);
                            }else if(jO.getString("Status").contentEquals("400")){
                                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                Log.e("TAG", sp.getString("uid", null)+" "+ phone+" "+ name+" "+ editing);
                params.put("UserID", sp.getString("uid", null));
                params.put("Mobile", phone);
                params.put("Name", name);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
