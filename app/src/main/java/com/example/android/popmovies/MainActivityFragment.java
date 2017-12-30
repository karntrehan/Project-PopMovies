package com.example.android.popmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    GridView gridView;
    private static MovieImagesAdapter imagesAdapter;
    private static TrailersAdapter trailersAdapter;

    /**
     * ArrayList to store
     * original title
     * movie poster image thumbnail
     * A plot synopsis (called overview in the api)
     * user rating (called vote_average in the api)
     * release date
     */
    private static ArrayList<HashMap<String, String>> moviesInfo;

    public MainActivityFragment() {
        //empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Initialization and load images to show it in GridView
        if (moviesInfo == null) {
            moviesInfo = new ArrayList<>();
            imagesAdapter = new MovieImagesAdapter(getActivity(), moviesInfo);
            //Calling the AsyncTask class to start to execute.
            new FetchLoadingTask().execute(getString(R.string.sort_order_upcoming));
        }

        // Get a reference to the ListView, and attach this adapter to it.
        gridView = rootView.findViewById(R.id.image_grid);
        gridView.setAdapter(imagesAdapter);

        //When grid item is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                HashMap<String, String> currentData;
                //Get item at position
                currentData = moviesInfo.get(position);

                //Pass the data to new activity "DetailsActivity"
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(getString(R.string.currentMovieData), currentData);
                //Start details activity
                startActivity(intent);
            }
        });
        return rootView;
    }


    // AsyncTask<Params, Progress, Result>
    public static class FetchLoadingTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

        private String API_KEY = "13a909a18259e3653d6885c366f6b369";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //        Start loading images on background
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortOrder = params[0];

            // clear the data of ArrayList if it's not empty
            if (moviesInfo != null) {
                moviesInfo.clear();
            }

            URL requestUrl = NetworkUtils.buildUrl(API_KEY, sortOrder);

            try {
                //Store JsonResponse
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(requestUrl);

                Log.v(TAG, "jsonResponse: " + jsonResponse);

                //Find items in "Result"
                JSONObject getMovieInfo = new JSONObject(jsonResponse);
                JSONArray resultDetail = getMovieInfo.getJSONArray("results");

                Log.v(TAG, "JSON Result: " + resultDetail.toString());

                // looping through All Contacts
                for (int i = 0; i < resultDetail.length(); i++) {

                    JSONObject r = resultDetail.getJSONObject(i);

                    String id = r.getString("id");
                    String original_title = r.getString("original_title");
                    String vote_average = r.getString("vote_average");
                    String overview = r.getString("overview");
                    String poster_path = r.getString("poster_path");
                    String release_date = r.getString("release_date");
                    String imageUrl = "http://image.tmdb.org/t/p/w185/" + poster_path;

                    HashMap<String, String> movieInfo = new HashMap<>();
                    // adding each child node to HashMap key => value
                    movieInfo.put("id", id);
                    movieInfo.put("original_title", original_title);
                    movieInfo.put("vote_average", vote_average);
                    movieInfo.put("overview", overview);
                    movieInfo.put("release_date", release_date);
                    movieInfo.put("imageUrl", imageUrl);

                    // adding contact to movieInfo list
                    moviesInfo.add(movieInfo);
                }
//                Log.v(TAG, "MoviesInfo: "+ moviesInfo.toString());
                return moviesInfo;
            } catch (Exception e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        // Post the loading data
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
            if (hashMaps != null) {
                Log.v(TAG, "onPost is executed" + hashMaps.toString());
                imagesAdapter.setGridData(hashMaps);
            }
        }
    }
}