package com.hendro.android_java_themealdb;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class FragmentFavourites extends Fragment {
    ArrayList<Meal> meals;
    ProgressBar pb;
    SwipeRefreshLayout srl;
    View view;
    String searchStr;
    String idList;
    ArrayList<String> savedList = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_random, container, false);
        idList = getArguments().getString("idList", "");

        StringTokenizer st = new StringTokenizer(idList, ",");
        meals = new ArrayList<>();
//        Log.d("**9","In fav fragment " + idList);
//        Log.d("**9","size " + st.countTokens());
        int temp = st.countTokens();
        Toast.makeText(getContext(), "In fav fragment " + idList, Toast.LENGTH_SHORT).show();


        for (int i = 0; i < temp; i++) {
            savedList.add((st.nextToken()));
        }
        pb = (ProgressBar) view.findViewById(R.id.progress_horizontal);

        searchStr = getArguments().getString("searchStr", "");

        for(int i=0; i<savedList.size(); i++){
            load(savedList.get(i));
        }

        Log.d("**9", "in meals show");
        showRecyclerGrid();


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void showRecyclerGrid() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }

        MyAdapter mAdapter = new MyAdapter(getContext(), meals);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void load(String id) {
        pb.setVisibility(ProgressBar.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String id, meal, photo;


                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");

                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    id = data.getString("idMeal").toString().trim();
                                    meal = data.getString("strMeal").toString().trim();
                                    photo = data.getString("strMealThumb").toString().trim();
                                    meals.add(new Meal(id, meal, photo));
                                }

                                showRecyclerGrid();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pb.setVisibility(ProgressBar.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(ProgressBar.GONE);

                Toast.makeText(getContext(), "Connection problem!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }
}