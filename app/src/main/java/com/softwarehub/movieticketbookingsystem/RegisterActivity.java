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
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView btnSwitchLogin;
    private TextInputEditText etUserName, etEmail,etMobileNo,etPassword,etCofirmPassword;
    private Button btnRegister;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dialog = new ProgressDialog(RegisterActivity.this);

        btnSwitchLogin = findViewById(R.id.btn_switch_login);
        etUserName = findViewById(R.id.et_username_register);
        etEmail = findViewById(R.id.et_email_register);
        etMobileNo = findViewById(R.id.et_mobile_number_register);
        etPassword = findViewById(R.id.et_password_register);
        etCofirmPassword = findViewById(R.id.et_confirm_password_register);
        btnRegister = findViewById(R.id.btn_register);

        btnSwitchLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String mobileNo = etMobileNo.getText().toString();
                String pass = etPassword.getText().toString();
                String confirmPass = etCofirmPassword.getText().toString();

                if(!TextUtils.isEmpty(userName)){
                if (!TextUtils.isEmpty(email) || verifyEmail(email)) {
                    if (!TextUtils.isEmpty(mobileNo) || verifyMobileNo(mobileNo)) {
                        if (pass.equals(confirmPass)) {

                            createUser(userName, email, mobileNo, pass);

                        } else {
                            etCofirmPassword.setError("Password not match");
                        }
                    } else {
                        etMobileNo.setError("Invalid Moblie Number");
                    }
                } else {
                    etEmail.setError("Invalid Email");
                }
                }else {
                    etUserName.setError("Please fill blank field");
                }
            }
        });
    }

    private void createUser(String userName, String email, String mobileNo, String password) {
        dialog.setMessage("Creating Your Account..");
        dialog.show();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_name", userName);
            jsonBody.put("gmail", email);
            jsonBody.put("mobile",mobileNo);
            jsonBody.put("password",password);
            String requestBody = jsonBody.toString();
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseURL.REGISTER_USER, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        boolean status = obj.getBoolean("status");
                        String msg = obj.getString("message");
                        JSONArray array = obj.getJSONArray("data");
                        if(status){
                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                            String userId = array.getJSONObject(0).getString("insertedId");

                            setPreference(userId,userName,email,mobileNo);

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private boolean verifyMobileNo(String moNo) {
        String regex = "(0/91)?[7-9][0-9]{9}";
        return moNo.matches(regex);
    }

    private boolean verifyEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}