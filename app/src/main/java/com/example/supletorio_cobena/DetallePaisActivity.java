package com.example.supletorio_cobena;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class DetallePaisActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String codigoAlpha2;
    private PaisModels.DetallePaisResponse detallePais;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle_pais);

        codigoAlpha2 = getIntent().getStringExtra("codigoAlpha2");
        if (codigoAlpha2 != null) {
            cargarDetallePais(codigoAlpha2);
        } else {
            Toast.makeText(this, "Error al cargar los datos del país.", Toast.LENGTH_SHORT).show();
            finish();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void cargarDetallePais(String codigoAlpha2) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.geognos.com/api/en/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PaisDetalleApi api = retrofit.create(PaisDetalleApi.class);
        api.getDetallePais(codigoAlpha2).enqueue(new Callback<PaisModels.DetallePaisResponse>() {
            @Override
            public void onResponse(Call<PaisModels.DetallePaisResponse> call, Response<PaisModels.DetallePaisResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    detallePais = response.body();
                    mostrarDetallePais();
                } else {
                    Toast.makeText(DetallePaisActivity.this, "No se pudo cargar la información del país.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaisModels.DetallePaisResponse> call, Throwable t) {
                Toast.makeText(DetallePaisActivity.this, "Error al cargar la información del país.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDetallePais() {
        ((TextView) findViewById(R.id.nombreTextView)).setText(detallePais.Results.Name);
        ((TextView) findViewById(R.id.capitalTextView)).setText("Capital: " + detallePais.Results.Capital.Name);
        ((TextView) findViewById(R.id.codigoIso2TextView)).setText("Código ISO 2: " + detallePais.Results.CountryCodes.iso2);
        ((TextView) findViewById(R.id.codigoIso3TextView)).setText("Código ISO 3: " + detallePais.Results.CountryCodes.iso3);
        ((TextView) findViewById(R.id.codigoIsoNumTextView)).setText("Código ISO Num: " + detallePais.Results.CountryCodes.isoN);
        ((TextView) findViewById(R.id.codigoFipsTextView)).setText("Código FIPS: " + detallePais.Results.CountryCodes.fips);
        ((TextView) findViewById(R.id.telPrefixTextView)).setText("Prefijo telefónico: " + detallePais.Results.TelPref);
        Glide.with(this)
                .load("http://www.geognos.com/api/en/countries/flag/" + codigoAlpha2 + ".png")
                .into((ImageView) findViewById(R.id.banderaImageView));

        if (mMap != null) {
            actualizarMapa();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (detallePais != null) {
            actualizarMapa();
        }
    }

    private void actualizarMapa() {
        LatLng center = new LatLng(detallePais.Results.GeoPt[0], detallePais.Results.GeoPt[1]);
        mMap.addMarker(new MarkerOptions().position(center).title(detallePais.Results.Name));

        LatLngBounds bounds = new LatLngBounds(
                new LatLng(detallePais.Results.GeoRectangle.South, detallePais.Results.GeoRectangle.West),
                new LatLng(detallePais.Results.GeoRectangle.North, detallePais.Results.GeoRectangle.East)
        );

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude),
                        new LatLng(bounds.northeast.latitude, bounds.southwest.longitude),
                        new LatLng(bounds.northeast.latitude, bounds.northeast.longitude),
                        new LatLng(bounds.southwest.latitude, bounds.northeast.longitude))
                .strokeColor(0xFFFF0000)
                .fillColor(0x44FF0000));

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }
}

interface PaisDetalleApi {
    @GET("countries/info/{alpha2code}.json")
    Call<PaisModels.DetallePaisResponse> getDetallePais(@Path("alpha2code") String alpha2code);
}
