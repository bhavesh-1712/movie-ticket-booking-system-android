package com.softwarehub.movieticketbookingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.softwarehub.movieticketbookingsystem.SharedPreference.SharedPreferenceKeys;
import com.softwarehub.movieticketbookingsystem.SharedPreference.Utilities;
import com.softwarehub.movieticketbookingsystem.config.BaseURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private TextView btnSwitchRegister;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(LoginActivity.this);

        etEmail = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);
        btnSwitchRegister = findViewById(R.id.btn_switch_register);
        btnLogin = findViewById(R.id.btn_login);

        btnSwitchRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!TextUtils.isEmpty(email)) {
                    if (!TextUtils.isEmpty(password)) {
                        signInUser(email, password);
                    } else {
                        etPassword.setError("Fill the blank field");
                    }
                } else {
                    etEmail.setError("Fill the blank field");
                }
            }
        });
    }

    private void signInUser(String email, String password) {
        dialog.setMessage("Validating your details...");
        dialog.show();

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gmail", email);
            jsonBody.put("password", password);
            String requestBody = jsonBody.toString();
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseURL.LOGIN_USER, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("Login_USER",String.valueOf(response));
                        boolean status = obj.getBoolean("status");
                        String msg = obj.getString("message");

                        if(!status){
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONArray arr = obj.getJSONArray("data");
                            String userId  = arr.getJSONObject(0).getString("user_id");
                            String userName = arr.getJSONObject(0).getString("user_name");
                            String gmail = arr.getJSONObject(0).getString("gmail");
                            String mob = arr.getJSONObject(0).getString("mobile_no");

                            setPreference(userId,userName,gmail,mob);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } catch (Throwable e) {
                        Log.e("JSON_ARRAY", e.toString());
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json;";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setPreference(String id,String userName,String gmail,String mob) {
        Utilities.getInstance().setPreference(this, SharedPreferenceKeys.user_id, id);
        Utilities.getInstance().setPreference(this, SharedPreferenceKeys.user_name, userName);
        Utilities.getInstance().setPreference(this, SharedPreferenceKeys.user_email, gmail);
        Utilities.getInstance().setPreference(this, SharedPreferenceKeys.user_phone, mob);
    }

}