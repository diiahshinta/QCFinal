package com.kosme.sjpqrcode.msglow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kosme.sjpqrcode.InnerboxActivity;
import com.kosme.sjpqrcode.ProductActivity;
import com.kosme.sjpqrcode.R;
import com.kosme.sjpqrcode.api.Api;
import com.kosme.sjpqrcode.api.ApiInterface;
import com.kosme.sjpqrcode.model.LevelProduct;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MsglowActivity extends AppCompatActivity {

    String barcode;
    TextView produk, bar, nie, sku, batch, ed, md, parent, sjp, total;
    ArrayList<String> pcs;
    ApiInterface apiInterface;
    ProgressDialog loading;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msglow);

        barcode = replaceString(getIntent().getStringExtra("barcode").replace(" ",""));
        apiInterface = ApiMsglow.getClient().create(ApiInterface.class);
        loading = ProgressDialog.show(MsglowActivity.this, null, "Loading", true, false);

        getData(barcode);

        produk = findViewById(R.id.txt_nama_produk);
        bar = findViewById(R.id.txt_barcode);
        nie = findViewById(R.id.txt_nie);
        sku = findViewById(R.id.txt_sku);
        batch = findViewById(R.id.txt_batch);
        ed = findViewById(R.id.txt_ed);
        md = findViewById(R.id.txt_md);
        parent = findViewById(R.id.txt_parent);
        sjp = findViewById(R.id.txt_sjp);
        total = findViewById(R.id.txt_total);
        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public void onResume(){
        super.onResume();
        loading.show();
        getData(barcode);
    }

    void getData(String barcode){
        Call<ModelData> response = apiInterface.getmsglow(barcode);
        response.enqueue(new Callback<ModelData>() {
            @Override
            public void onResponse(Call<ModelData> call, retrofit2.Response<ModelData> response) {
                ModelData data = response.body();
                loading.dismiss();
                if (data == null){
                    Toast.makeText(MsglowActivity.this, "No data available!", Toast.LENGTH_SHORT).show();
                } else {
                    produk.setText(data.getScan().getProduct());
                    bar.setText(data.getScan().getBarcode());
                    nie.setText(data.getScan().getNie());
                    sku.setText(data.getScan().getSku());
                    batch.setText(data.getScan().getBatch());
                    ed.setText(data.getScan().getEd());
                    md.setText(data.getScan().getMd());
                    parent.setText(data.getScan().getParent());
                    sjp.setText(data.getScan().getSjp());
                    total.setText(String.valueOf(data.getScan().getPcs().size()));
                    MsglowAdapter adapter = new MsglowAdapter(MsglowActivity.this, data.getScan().getPcs());
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ModelData> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(MsglowActivity.this, "Maintenance : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    String replaceString(String string) {
        return string.replaceAll("[^a-zA-Z0-9-/,.]","");
    }
}
