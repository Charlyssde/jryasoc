package com.jrya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.jrya.conf.Env;
import com.jrya.conf.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText username, password;
    private Spinner estacion;
    private Button ingresar;
    private VolleyService volleyService;
    private String estacionSelected;
    private List<String> estaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        volleyService = VolleyService.getInstance(this);

        getEstaciones();

        loadUI();
    }

    private void getEstaciones() {
        estaciones = new ArrayList<>();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Env.URL + "Estaciones", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE->", response.toString());
                try {
                    JSONArray res = response.getJSONArray("estaciones");
                    estaciones.add("Seleccione una estación");
                    for (int i = 0; i < res.length(); i++){
                        estaciones.add(res.getJSONObject(i).getString("Estacion"));
                    }
                    estacion.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.option, estaciones));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volleyService.addToQueue(req);

    }

    private void loadUI() {

        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        estacion = findViewById(R.id.estaciones);
        ingresar = findViewById(R.id.btn_ingresar);

        estacion.setAdapter(new ArrayAdapter<String>(this, R.layout.option, estaciones));
        estacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estacionSelected = (String) adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {

        Map<String, String> param = new HashMap<>();
        param.put("username", username.getText().toString());
        param.put("password", password.getText().toString());

        JSONObject jsonObject = new JSONObject(param);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Env.URL + "Login", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Response->", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR->", error.toString());
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volleyService.addToQueue(request);


    }
}