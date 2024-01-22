package com.suhel.wallpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    private EditText searchEdt;
    private ImageView searchIV;
    private RecyclerView categoryRV, wallpaperRV;
    private ProgressBar loadingPB;
    private ArrayList<String> wallpaperArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private WallpaperRVAdapter wallpaperRVAdapter;

    // API key: MX8yiGoSntr21uVRfXkE0q1hDU3PcVAnxCeU4Jpo0xcPeupmyWM8NcaR

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEdt = findViewById(R.id.idEdtSearch);
        searchIV = findViewById(R.id.idIVSearch);
        categoryRV = findViewById(R.id.idRVCategory);
        wallpaperRV = findViewById(R.id.idRVWallpaper);
        loadingPB = findViewById(R.id.idPBLoading);
        wallpaperArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        categoryRV.setLayoutManager(linearLayoutManager);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList, this, this::onCategoryClick);
        categoryRV.setAdapter(categoryRVAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        wallpaperRV.setLayoutManager(gridLayoutManager);
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList, this);
        wallpaperRV.setAdapter(wallpaperRVAdapter);

        getCategories();

        getWallpapers();

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchStr = searchEdt.getText().toString();
                if (searchStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your search query", Toast.LENGTH_SHORT).show();
                } else {
                    getWallpapersByCategory(searchStr);
                }
            }
        });


    }

    private void getWallpapersByCategory(String category) {
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/search?query=" + category + "&per_page=40&page=1";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray photoArray = null;
                loadingPB.setVisibility(View.GONE);
                try {
                    photoArray = response.getJSONArray("photos");
                    for (int i = 0; i < photoArray.length(); i++) {
                        JSONObject photoObj = photoArray.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to load wallpapers...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "MX8yiGoSntr21uVRfXkE0q1hDU3PcVAnxCeU4Jpo0xcPeupmyWM8NcaR");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getWallpapers() {
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/curated?page=1&per_page=40";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                try {
                    JSONArray photoArray = response.getJSONArray("photos");
                    for (int i = 0; i < photoArray.length(); i++) {
                        JSONObject photoObj = photoArray.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to load wallpapers...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "MX8yiGoSntr21uVRfXkE0q1hDU3PcVAnxCeU4Jpo0xcPeupmyWM8NcaR");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getCategories() {
        categoryRVModalArrayList.add(new CategoryRVModal("Technology", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTJ8fHRlY2hub2xvZ318ZW58MHX8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArrayList.add(new CategoryRVModal("Programming", "https://images.unsplash.com/photo-1542831371-29b0f74f9713?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXX8cHJvZ3JhbW1pbmd8ZW58MHx8MHx8&ixlib-rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArrayList.add(new CategoryRVModal("Nature", "https://images.pexels.com/photos/2387873/pexels-photo-2387873.jpeg?auto=compress&cs-tinysrgb&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Travel", "https://images.pexels.com/photos/672358/pexels-photo-672358.jpeg?auto-compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Architecture", "https://images.pexels.com/photos/256150/pexels-photo-256150.jpeg?auto-compress&cs-tinysrgb&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Arts", "https://images.pexels.com/photos/1194420/pexels-photo-1194420.jpeg?auto-compress&cs-tinysrgb&dpr=1&w=500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Music", "https://images.pexels.com/photos/4348093/pexels-photo-4348093.jpeg?auto-compress&cs-tinysrgb&dpr-18-500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Abstract", "https://images.pexels.com/photos/2110951/pexels-photo-2110951.jpeg?auto-compress&cs-tinysrgb&dpr-18-500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Cars", "https://images.pexels.com/photos/3802510/pexels-photo-3802510.jpeg?auto-compress&cs-tinysrgb&dpr-18-500"));
        categoryRVModalArrayList.add(new CategoryRVModal("Flowers", "https://images.pexels.com/photos/1086178/pexels-photo-1086178.jpeg?auto-compress&cs-tinysrgb&dpr=1&w=500"));

        categoryRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalArrayList.get(position).getCategory();
        getWallpapersByCategory(category);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Cancle", null)
                .show();
    }
}