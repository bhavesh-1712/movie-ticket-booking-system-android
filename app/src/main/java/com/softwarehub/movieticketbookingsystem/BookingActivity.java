package com.softwarehub.movieticketbookingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softwarehub.movieticketbookingsystem.SharedPreference.SharedPreferenceKeys;
import com.softwarehub.movieticketbookingsystem.SharedPreference.Utilities;
import com.softwarehub.movieticketbookingsystem.config.BaseURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    private String movieName, movieId, movieDetails, movieCharges, movieBanner, movieTimeSlot, slotId;

    private char seat_no[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private int count = 1, totalAmount = 0;

    private LinearLayout linearLayout;
    private TextView tvMovieName, tvMovieSlot, tvMovieCharges;
    private Button btnBooked;
    ArrayList<Integer> allotedSeat = new ArrayList<Integer>();
    ArrayList<Integer> bookedSeat = new ArrayList<Integer>();
    ArrayList<String> allotedLabel = new ArrayList<String>();

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent intent = getIntent();
        movieId = intent.getStringExtra("MOVIE_ID");
        movieName = intent.getStringExtra("MOVIE_NAME");
        movieDetails = intent.getStringExtra("MOVIE_DETAILS");
        movieCharges = intent.getStringExtra("MOVIE_CHARGES");
        movieBanner = intent.getStringExtra("MOVIE_BANNER");
        movieTimeSlot = intent.getStringExtra("TIME_SLOT");
        slotId = intent.getStringExtra("SLOT_ID");

        linearLayout = findViewById(R.id.linear_layout);
        btnBooked = findViewById(R.id.btn_booked);
        tvMovieName = findViewById(R.id.tv_movie_name);
        tvMovieSlot = findViewById(R.id.tv_time_slot);
        tvMovieCharges = findViewById(R.id.tv_charges);

        tvMovieName.setText(movieName);
        tvMovieSlot.setText(movieTimeSlot);
        tvMovieCharges.setText("Rs." + totalAmount + "/-");

        btnBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Show Summary
                for (int i = 0; i < allotedLabel.size(); i++) {
                    Log.d("SEAT = ", allotedLabel.get(i));
                    bookedSeat(allotedSeat.get(i), slotId, movieId);
                }
                dialog.dismiss();
                Toast.makeText(BookingActivity.this, "Your all seat booked", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(BookingActivity.this,ThankYouActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        dialog = new ProgressDialog(BookingActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

    }

    @Override
    public void onClick(View view) {
        btnBooked.setVisibility(View.VISIBLE);
        Button button = (Button) findViewById(view.getId());
        if (allotedSeat.contains(button.getId())) {
            button.setBackgroundColor(getResources().getColor(R.color.green));
            allotedSeat.remove(new Integer(button.getId()));
            allotedLabel.remove(new String(button.getText().toString()));
            totalAmount = Integer.valueOf(movieCharges) * allotedSeat.size();
            tvMovieCharges.setText("Rs." + totalAmount + "/-");
            return;
        } else {
            if (allotedSeat.size() < 5) {
                button.setBackgroundColor(getResources().getColor(R.color.teal_700));
                allotedSeat.add(button.getId());
                allotedLabel.add(button.getText().toString());
                totalAmount = Integer.valueOf(movieCharges) * allotedSeat.size();
                tvMovieCharges.setText("Rs." + totalAmount + "/-");
            } else {
                Toast.makeText(BookingActivity.this, "Sorry! Your Booking Limit reached", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBooking();
        dialog.setMessage("Updating Seat Booking Status..");
        dialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                refreshSeat();
            }
        }, 3000);
    }

    private void bookedSeat(int seatNo, String slotId, String movieId) {
        dialog.setMessage("Booking Your Seat..");
        dialog.show();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", Utilities.getInstance().getPreference(this, SharedPreferenceKeys.user_id));
            jsonBody.put("seat_no", seatNo);
            jsonBody.put("slot_id", slotId);
            jsonBody.put("movie_id", movieId);
            String requestBody = jsonBody.toString();
            RequestQueue requestQueue = Volley.newRequestQueue(BookingActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseURL.BOOKED_SEAT, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        boolean status = obj.getBoolean("status");
                        String msg = obj.getString("message");
                        if (!status) {
                            Toast.makeText(BookingActivity.this, msg, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    } catch (Throwable e) {
                        dialog.dismiss();
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

    private void getBooking() {
        dialog.setMessage("Wait fetching vacant seat..");
        dialog.show();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("movie_id", movieId);
            jsonBody.put("time_slot_id", slotId);
            String requestBody = jsonBody.toString();
            RequestQueue requestQueue = Volley.newRequestQueue(BookingActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseURL.GET_BOOKING, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        boolean status = obj.getBoolean("status");
                        String msg = obj.getString("message");
                        JSONArray array = obj.getJSONArray("data");
                        if (status) {
                            Log.e("ARRAY_LENGTH", String.valueOf(array.length()));
                            for (int i = 0; i < array.length(); i++) {

                                int seatNo = array.getJSONObject(i).getInt("seat_no");
                                bookedSeat.add(seatNo);
                                Log.e("SEAT_NO =", String.valueOf(seatNo));
                            }
                            dialog.setMessage("Updating Seat Booking Status..");
                            dialog.show();
                        } else {
                            Toast.makeText(BookingActivity.this, msg, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    } catch (Throwable e) {
                        dialog.dismiss();
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

    private void refreshSeat() {
        linearLayout.removeAllViews();
        for (int i = 0; i < 10; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 4; j++) {
                Button btnTag = new Button(this);

                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                btnTag.setOnClickListener(this);
                btnTag.setText(seat_no[i] + "" + count);
                if (bookedSeat.contains(count)) {
                    btnTag.setBackgroundColor(getResources().getColor(R.color.red));
                    btnTag.setEnabled(false);
                } else {
                    btnTag.setBackgroundColor(getResources().getColor(R.color.green));
                }
                btnTag.setId(count);
                row.addView(btnTag);
                count++;
            }
            linearLayout.addView(row);

            for (int j = 0; j < bookedSeat.size(); j++) {
                Log.d("BOOKED_SEAT = ", bookedSeat.get(j).toString());
            }
        }
    }
}