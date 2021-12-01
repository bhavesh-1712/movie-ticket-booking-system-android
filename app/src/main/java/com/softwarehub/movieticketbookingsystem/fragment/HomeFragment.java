package com.softwarehub.movieticketbookingsystem.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softwarehub.movieticketbookingsystem.MainActivity;
import com.softwarehub.movieticketbookingsystem.R;
import com.softwarehub.movieticketbookingsystem.RegisterActivity;
import com.softwarehub.movieticketbookingsystem.SharedPreference.SharedPreferenceKeys;
import com.softwarehub.movieticketbookingsystem.SharedPreference.Utilities;
import com.softwarehub.movieticketbookingsystem.adapter.MovieAdapter;
import com.softwarehub.movieticketbookingsystem.config.BaseURL;
import com.softwarehub.movieticketbookingsystem.model.BookModel;
import com.softwarehub.movieticketbookingsystem.model.MovieModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView rvMovie;
    private ArrayList<MovieModel> movieModelList = new ArrayList<>();
    private MovieAdapter movieAdapter;

    private ArrayList<BookModel> bookModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvMovie = view.findViewById(R.id.rv_movie);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvMovie.setLayoutManager(linearLayoutManager);
        movieAdapter = new MovieAdapter(movieModelList, bookModelList,getActivity());
        rvMovie.setAdapter(movieAdapter);

        getCustomerBookinById();

//        if(movieModelList.isEmpty()){
//            getMovies();
//        }

        return view;
    }

    private void getMovies(){
        movieModelList.clear();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.GET_MOVIE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean status = obj.getBoolean("status");
                            if(status) {
                                JSONArray arr = obj.getJSONArray("data");
                                for (int i = 0; i < arr.length(); i++) {
                                    String movieId = arr.getJSONObject(i).getString("id");
                                    String movieName = arr.getJSONObject(i).getString("movie_name");
                                    String movieDetails = arr.getJSONObject(i).getString("movie_details");
                                    String movieBanner = arr.getJSONObject(i).getString("movie_image");
                                    String movieCharges = arr.getJSONObject(i).getString("movie_charges");

                                    movieModelList.add(new MovieModel(movieId,movieName,movieDetails,
                                            movieCharges,BaseURL.BASE_IMG+movieBanner));
                                }
                                movieAdapter.notifyDataSetChanged();
                            }else {
                                Log.e("HOME_FRAGMENT","Something went wrong !");
                                //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Throwable t) {
                            Log.e("My App", t.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG_VOLLEY_ERROR", error.toString());
            }
        });

        queue.add(stringRequest);
    }

    private void getCustomerBookinById(){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", Utilities.getInstance().getPreference(getActivity(), SharedPreferenceKeys.user_id));
            String requestBody = jsonBody.toString();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseURL.GET_BOOKING_BY_CUTOMER_ID, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        boolean status = obj.getBoolean("status");
                        String msg = obj.getString("message");
                        JSONArray array = obj.getJSONArray("data");
                        if(status){
                            if(array.length() > 0){
                                for (int i = 0; i < array.length(); i++) {
                                    String slotId = array.getJSONObject(i).getString("time_slot");
                                    String movieId = array.getJSONObject(i).getString("movie_id");
                                    String noOfSlotBooked = array.getJSONObject(i).getString("COUNT(seat_no)");

                                    bookModelList.add(new BookModel(slotId,movieId,noOfSlotBooked));
                                }
                            }
                        }
                        getMovies();
                    } catch (Throwable e) {
                        Log.e("JSON_ARRAY", e.toString());
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
}