package com.hendro.android_java_themealdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.StringTokenizer;
public class MainActivity extends AppCompatActivity {
//    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs =  PreferenceManager.getDefaultSharedPreferences(this);

        // Load Random food on start
        loadFragment(new FragmentRandom());
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void onClickSearch(View v){
        EditText text = (EditText)findViewById(R.id.searchText);
        text.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);
    }

    public void onClickFavorites(View v){
        String savedString = prefs.getString("idList", "");
        EditText text = (EditText)findViewById(R.id.searchText);
        Log.d("**9", "From main act" + savedString);
        Fragment fragment = new FragmentFavourites();
        Button favButton = (Button)findViewById(R.id.searchBtn);
        Bundle args = new Bundle();
        args.putString("idList", savedString);
        fragment.setArguments(args);

        loadFragment(fragment);
        v.setVisibility(View.INVISIBLE);
        favButton.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
    }

    public void onClickBtn(View v)
    {
        EditText text = (EditText)findViewById(R.id.searchText);
        String value = text.getText().toString();
        Button favButton = (Button)findViewById(R.id.favorites);

        Fragment fragment = new FragmentSearch();
        Bundle args = new Bundle();
        args.putString("searchStr", value);
        fragment.setArguments(args);

        loadFragment(fragment);

        text.setVisibility(View.INVISIBLE);
        v.setVisibility(View.INVISIBLE);
        favButton.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(this,MainActivity.class));
        this.finish();
    }

}