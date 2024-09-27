package com.example.supletorio_cobena;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PaisAdapter paisAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.geognos.com/api/en/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PaisApi api = retrofit.create(PaisApi.class);

        api.getPaises().enqueue(new Callback<AllCountriesResponse>() {
            @Override
            public void onResponse(Call<AllCountriesResponse> call, Response<AllCountriesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pais> paises = new ArrayList<>();

                    for (Map.Entry<String, InfoPais> entry : response.body().Results.entrySet()) {
                        Pais pais = new Pais(entry.getKey(), entry.getValue().getName());
                        paises.add(pais);
                    }

                    paisAdapter = new PaisAdapter(paises, pais -> {
                        Intent intent = new Intent(MainActivity.this, DetallePaisActivity.class);
                        intent.putExtra("codigoAlpha2", pais.getCodigoAlpha2());
                        startActivity(intent);
                    });

                    recyclerView.setAdapter(paisAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllCountriesResponse> call, Throwable t) {
            }
        });
    }
}

interface PaisApi {
    @GET("countries/info/all.json")
    Call<AllCountriesResponse> getPaises();
}

class InfoPais {
    private String Name;

    public String getName() {
        return Name;
    }
}
