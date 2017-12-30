package com.example.android.popmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by toda on 2017/10/11.
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    // Set variables
    Context mContext;
    TextView mMovieTitle;
    TextView mMovieReleaseDate;
    ImageView mMovieImage;
    TextView mMovieOverview;
    RatingBar mMovieRating;
    TextView mRatingNumber;
    Button mTrailers;

    String mReleaseDate;
    String mImage;
    String mTitle;
    String mOverview;
    String mRating;
    String mId;
    HashMap<String, String> currentMovieData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.v(TAG, "onCreate is called");

        mMovieTitle = (TextView) findViewById(R.id.mv_display_title);
        mMovieReleaseDate = (TextView) findViewById(R.id.mv_releaseDate);
        mMovieImage = (ImageView) findViewById(R.id.mv_image);
        mMovieOverview = (TextView) findViewById(R.id.mv_overview);
        mMovieRating = (RatingBar) findViewById(R.id.ratingBar);
        mRatingNumber = (TextView) findViewById(R.id.mv_rating);
        mTrailers = (Button) findViewById(R.id.action_trailers);

        currentMovieData = (HashMap<String, String>) getIntent().getSerializableExtra(getString(R.string.currentMovieData));


//        When user click button to show trailers, open new activity
        mTrailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Pass the data to new activity "TrailersActivity"
                Intent intent = new Intent(DetailsActivity.this, TrailersDetail.class);
                intent.putExtra(Intent.EXTRA_TEXT, currentMovieData.get("id"));
                //Start details activity
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currentMovieData != null) {
            mTitle = currentMovieData.get("original_title");
            mMovieTitle.setText(mTitle);

            mReleaseDate = currentMovieData.get("release_date");
            mMovieReleaseDate.setText(mReleaseDate);

            mImage = currentMovieData.get("imageUrl");
            Picasso.with(mContext).load(mImage).into(mMovieImage);

            mOverview = currentMovieData.get("overview");
            mMovieOverview.setText(mOverview);

            mRating = currentMovieData.get("vote_average");
            mMovieRating.setNumStars(10);
            mMovieRating.setStepSize((float) 0.2);
            mMovieRating.setRating(Float.valueOf(mRating));
            mRatingNumber.setText(mRating);
        }else{
            Toast.makeText(this, "currentMOvieData is null!", Toast.LENGTH_LONG).show();
        }
    }
}
