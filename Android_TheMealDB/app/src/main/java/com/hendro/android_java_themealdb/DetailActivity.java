package com.hendro.android_java_themealdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    ImageView iv;
    TextView tvInstruction;
    CollapsingToolbarLayout toolbar;
    FloatingActionButton fab;
    String meal, photo, instruction;
    SharedPreferences prefs;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        prefs =  PreferenceManager.getDefaultSharedPreferences(this);

        toolbar = findViewById(R.id.collapsingToolbar);
        iv = (ImageView) findViewById(R.id.iv_image);
        tvInstruction = (TextView) findViewById(R.id.detail_tv_instruction);
        fab = findViewById(R.id.fab);

        Intent i = getIntent();
        id = i.getStringExtra("i_idMeal");

        load(id);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String savedString = prefs.getString("idList", "");
                Log.d("**9", "in details "+ savedString);
                if(!id.isEmpty()){
                    prefs.edit().putString("idList", savedString+","+id).apply();
                }

                Toast.makeText(getApplicationContext(), savedString+","+id,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void load(String id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("Events: ", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            JSONObject data = jsonArray.getJSONObject(0);

                            meal = data.getString("strMeal");
                            instruction = data.getString("strInstructions");
                            photo = data.getString("strMealThumb");

                            toolbar.setTitle(meal);

                            Glide.with(getApplicationContext())
                                    .load(photo)
                                    .into(iv);

                            tvInstruction.setText(instruction);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Events: ", error.toString());
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}