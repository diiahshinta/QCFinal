package com.kosme.sjpqrcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kosme.sjpqrcode.api.Api;
import com.kosme.sjpqrcode.api.ApiInterface;
import com.kosme.sjpqrcode.model.LevelProduct;
import com.kosme.sjpqrcode.model.Login;
import com.kosme.sjpqrcode.model.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductActivity extends AppCompatActivity {


    ApiInterface apiInterface;
    String barcode, token;
    TextView produk, batch, level, scan, ed, md, station, bar, parent, username, nie, sku;
    ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        barcode = getIntent().getStringExtra("barcode").replace("http://","");
        apiInterface = Api.getClient().create(ApiInterface.class);
        loading = ProgressDialog.show(ProductActivity.this, null, "loading", true, false);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Login", null);
        Login login = gson.fromJson(json, Login.class);

        token = login.getAuth().getToken();
        getData(barcode, token);

        produk = findViewById(R.id.txt_nama_produk);
        username = findViewById(R.id.txt_username);
        batch = findViewById(R.id.txt_batch_number);
        level = findViewById(R.id.txt_level);
        scan = findViewById(R.id.txt_scan_date);
        ed = findViewById(R.id.txt_expired_date);
        md = findViewById(R.id.txt_make_date);
        station = findViewById(R.id.txt_station_name);
        bar = findViewById(R.id.barcode);
        parent = findViewById(R.id.txt_parent);
        nie = findViewById(R.id.txt_nie);
        sku = findViewById(R.id.txt_sku);



    }
    @Override
    public void onResume(){
        super.onResume();
        loading.show();
        getData(barcode, token);
    }
    void getData(String barcode, String token){
        Call<LevelProduct> response = apiInterface.getDetailsProduk("Bearer " + token, barcode, "1");
        response.enqueue(new Callback<LevelProduct>() {
            @Override
            public void onResponse(Call<LevelProduct> call, retrofit2.Response<LevelProduct> response) {
                LevelProduct data = response.body();
                loading.dismiss();
                if (data == null){
                    Toast.makeText(ProductActivity.this, "No data available!", Toast.LENGTH_SHORT).show();
                } else {
                    if (data.getStatus().equals("success")){
                        bar.setText(data.getProducts().getProductData().getBarcode());
                        bar.setTextIsSelectable(true);
                        produk.setText(data.getProducts().getProduk());
                        batch.setText(data.getProducts().getProductData().getBatch());
                        level.setText(data.getProducts().getProductData().getLevel());
                        if (data.getProducts().getProductData().getUsername() == null){
                            username.setText("-");

                        } else {
                            username.setText(data.getProducts().getProductData().getUsername());
                        }
                        scan.setText(data.getProducts().getProductData().getScannedDate());
                        ed.setText(data.getProducts().getProductData().getExpiredDate().replace("00:00:00",""));
                        md.setText(data.getProducts().getProductData().getProductionDate().replace("00:00:00",""));
                        station.setText(data.getProducts().getProductData().getLine());
                        nie.setText(data.getProducts().getProductData().getNie());
                        sku.setText(data.getProducts().getProductData().getSku());
                        if (data.getProducts().getInheritance().getParent() != null){
                            SpannableString content = new SpannableString(data.getProducts().getInheritance().getParent().getSerialisasi());
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            parent.setText(content);
                            parent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(ProductActivity.this, InnerboxActivity.class);
                                    i.putExtra("barcode", data.getProducts().getInheritance().getParent().getBarcode());
                                    startActivity(i);
                                    finish();
                                }
                            });
                        } else {
                            parent.setText("-");
                        }
                    } else {
                        Toast.makeText(ProductActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<LevelProduct> call, Throwable t) {
                loading.dismiss();
                Log.d("errordataAPI", t.getLocalizedMessage());
                Toast.makeText(ProductActivity.this, "Server Maintenance : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
