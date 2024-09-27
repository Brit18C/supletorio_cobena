package com.example.supletorio_cobena;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class DetallePaisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle_pais);

        String codigoAlpha2 = getIntent().getStringExtra("codigoAlpha2");
        if (codigoAlpha2 != null) {
            cargarDetallePais(codigoAlpha2);
        } else {
            Toast.makeText(this, "Error al cargar los datos del país.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarDetallePais(String codigoAlpha2) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.geognos.com/api/en/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PaisDetalleApi api = retrofit.create(PaisDetalleApi.class);

        api.getDetallePais(codigoAlpha2).enqueue(new Callback<DetallePaisResponse>() {
            @Override
            public void onResponse(Call<DetallePaisResponse> call, Response<DetallePaisResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().Results != null) {
                    DetallePaisResponse detalle = response.body();
                    ((TextView) findViewById(R.id.nombreTextView)).setText(detalle.Results.Name);
                    ((TextView) findViewById(R.id.capitalTextView)).setText("Capital: " + detalle.Results.Capital.Name);
                    ((TextView) findViewById(R.id.codigoTextView)).setText("Código ISO: " + codigoAlpha2);

                    Glide.with(DetallePaisActivity.this)
                            .load("http://www.geognos.com/api/en/countries/flag/" + codigoAlpha2 + ".png")
                            .into((ImageView) findViewById(R.id.banderaImageView));
                } else {
                    Toast.makeText(DetallePaisActivity.this, "No se pudo cargar la información del país.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetallePaisResponse> call, Throwable t) {
                Toast.makeText(DetallePaisActivity.this, "Error al cargar la información del país.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

interface PaisDetalleApi {
    @GET("countries/info/{alpha2code}.json")
    Call<DetallePaisResponse> getDetallePais(@Path("alpha2code") String alpha2code);
}

class DetallePaisResponse {
    Results Results;
}

class Results {
    String Name;
    Capital Capital;
}

class Capital {
    String Name;
}