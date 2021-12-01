package com.softwarehub.movieticketbookingsystem.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softwarehub.movieticketbookingsystem.BookingActivity;
import com.softwarehub.movieticketbookingsystem.R;
import com.softwarehub.movieticketbookingsystem.model.BookModel;
import com.softwarehub.movieticketbookingsystem.model.MovieModel;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieModel> movieModelList;
    private List<BookModel> bookModelList;
    private Activity activity;

    public MovieAdapter(List<MovieModel> movieModelList, List<BookModel> bookModelList ,Activity activity) {
        this.movieModelList = movieModelList;
        this.bookModelList = bookModelList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie, parent, false);

        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String movieId = movieModelList.get(position).getMovieId();
        String movieName = movieModelList.get(position).getMovieName();
        String movieDetails = movieModelList.get(position).getMovieDetails();
        String movieBannerImg = movieModelList.get(position).getMovieImage();
        String movieCharges = movieModelList.get(position).getMovieCharges();

        holder.setCourse(movieId,movieName,movieDetails,movieBannerImg,movieCharges,position);
    }

    @Override
    public int getItemCount() {
        return movieModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMovieName,tvMovieDetails,tvMovieCharges,tv09_12, tv01_04, tv06_09;
        private ImageView ivBanner;
        private LinearLayout llBookingState;
        private View divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
            tvMovieDetails = itemView.findViewById(R.id.tv_movie_details);
            tvMovieCharges = itemView.findViewById(R.id.tv_movie_charges);
            ivBanner = itemView.findViewById(R.id.iv_movie_banner);
            llBookingState = itemView.findViewById(R.id.llBookingState);
            divider = itemView.findViewById(R.id.divider);

            tv09_12 = itemView.findViewById(R.id.tv_09_12);
            tv01_04 = itemView.findViewById(R.id.tv_01_04);
            tv06_09 = itemView.findViewById(R.id.tv_06_09);
        }

        private void setCourse(String id, String movieName, String movieDetails, String movieBanner, String movieCharges, int pos){
            Glide.with(itemView.getContext())
                    .load(movieBanner)
                    .into(ivBanner);

            tvMovieName.setText(movieName);
            tvMovieDetails.setText(movieDetails);
            tvMovieCharges.setText("Rs."+movieCharges+"/-");


            for(int i = 0; i < bookModelList.size(); i++){
                if(bookModelList.get(i).getMovieId().equals(id)){
                    if(bookModelList.get(i).getSlotId().equals("3")){
                        tv09_12.setEnabled(false);
                        tv09_12.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                        addTextView("09:00 to 12:00",bookModelList.get(i).getNoOfSlotBooked());
                    }else if(bookModelList.get(i).getSlotId().equals("4")){
                        tv01_04.setEnabled(false);
                        tv01_04.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                        addTextView("01:00 to 04:00",bookModelList.get(i).getNoOfSlotBooked());
                    }else if(bookModelList.get(i).getSlotId().equals("5")){
                        tv06_09.setEnabled(false);
                        tv06_09.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                        addTextView("06:00 to 09:00",bookModelList.get(i).getNoOfSlotBooked());
                    }
                }
            }

            tv09_12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToBookingActivity(id, movieName, movieDetails, movieCharges, movieBanner,"09:00 to 12:00","3");
                }
            });

            tv01_04.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToBookingActivity(id, movieName, movieDetails, movieCharges, movieBanner,"01:00 to 04:00","4");
                }
            });

            tv06_09.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToBookingActivity(id, movieName, movieDetails, movieCharges, movieBanner,"06:00 to 09:00","5");
                }
            });
        }

        private void goToBookingActivity(String movieId,String movieName, String movieDetails, String movieCharges, String movieBanner,String timeSlot, String slotId){
            Intent intent = new Intent(activity, BookingActivity.class);
            intent.putExtra("MOVIE_ID",movieId);
            intent.putExtra("MOVIE_NAME",movieName);
            intent.putExtra("MOVIE_DETAILS",movieDetails);
            intent.putExtra("MOVIE_CHARGES",movieCharges);
            intent.putExtra("MOVIE_BANNER",movieBanner);
            intent.putExtra("TIME_SLOT",timeSlot);
            intent.putExtra("SLOT_ID",slotId);
            activity.startActivity(intent);
        }

        private void addTextView(String timeSlot, String noOfSeat){
            divider.setVisibility(View.VISIBLE);
            llBookingState.setVisibility(View.VISIBLE);

            LinearLayout row = new LinearLayout(activity);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvTimeSlot = new TextView(activity);
            tvTimeSlot.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
            tvTimeSlot.setTextSize(16.00f);
            tvTimeSlot.setTextColor(activity.getResources().getColor(R.color.black));
            tvTimeSlot.setText(timeSlot);

            TextView tvNoOfSeat = new TextView(activity);
            tvNoOfSeat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
            tvNoOfSeat.setTextSize(16.00f);
            tvNoOfSeat.setTextColor(activity.getResources().getColor(R.color.black));
            tvNoOfSeat.setText(noOfSeat);

            row.addView(tvTimeSlot);
            row.addView(tvNoOfSeat);

            llBookingState.addView(row);
        }

        public void AlertDialogPopUp(String str, String str2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(str);
            builder.setMessage(str2);
            builder.setCancelable(true);
            builder.setNeutralButton("Ok !", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
    }
}
