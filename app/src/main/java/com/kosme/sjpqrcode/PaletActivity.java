package com.kosme.sjpqrcode;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.kosme.sjpqrcode.adapter.BatchAdapter;
import com.kosme.sjpqrcode.adapter.ChildAdapter;
import com.kosme.sjpqrcode.api.Api;
import com.kosme.sjpqrcode.api.ApiInterface;
import com.kosme.sjpqrcode.model.Login;
import com.kosme.sjpqrcode.model.Replace;
import com.kosme.sjpqrcode.model.Response;
import com.kosme.sjpqrcode.model.ResponseStatus;
import com.kosme.sjpqrcode.model.User;

import retrofit2.Call;
import retrofit2.Callback;

public class PaletActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    String barcode, token;
    TextView produk, level, scan, ed, md, station, bar, count, username, pcs, txtreject, txtkarantina, txtlulus, status, logname, logtime, lognote, nie, sku;
    RecyclerView rvChild, rvBatch;
    ProgressDialog loading;
    FloatingActionButton reject, karantina, lulus;
    SharedPreferences sharedPreferences;
    ExtendedFloatingActionButton action;
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_palet);

        barcode = getIntent().getStringExtra("barcode");
        apiInterface = Api.getClient().create(ApiInterface.class);
        loading = ProgressDialog.show(PaletActivity.this, null, "loading", true, false);

        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Login", null);
        Login login = gson.fromJson(json, Login.class);

        token = login.getAuth().getToken();

        getData(barcode, token);

        action = findViewById(R.id.add_fab);
        reject = findViewById(R.id.reject_fab);
        karantina = findViewById(R.id.karantina_fab);
        lulus = findViewById(R.id.lulus_fab);
        txtreject = findViewById(R.id.reject_action_text);
        txtkarantina = findViewById(R.id.karantina_action_text);
        txtlulus = findViewById(R.id.lulus_action_text);


        reject.setVisibility(View.GONE);
        karantina.setVisibility(View.GONE);
        lulus.setVisibility(View.GONE);
        txtreject.setVisibility(View.GONE);
        txtkarantina.setVisibility(View.GONE);
        txtlulus.setVisibility(View.GONE);
        isAllFabsVisible = false;

        if (login.getAuth().getPermission().contains("dataprint.karantina")){
            action.setVisibility(View.VISIBLE);
        } else {
            action.setVisibility(View.GONE);
        }

        action.shrink();

        action.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            reject.show();
                            karantina.show();
                            lulus.show();
                            txtreject
                                    .setVisibility(View.VISIBLE);
                            txtkarantina
                                    .setVisibility(View.VISIBLE);
                            txtlulus
                                    .setVisibility(View.VISIBLE);
                            action.extend();
                            isAllFabsVisible = true;
                        } else {
                            reject.hide();
                            karantina.hide();
                            lulus.hide();
                            txtreject
                                    .setVisibility(View.GONE);
                            txtkarantina
                                    .setVisibility(View.GONE);
                            txtlulus
                                    .setVisibility(View.GONE);
                            action.shrink();
                            isAllFabsVisible = false;
                        }
                    }
                });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(PaletActivity.this, "reject", "4", barcode);
            }
        });

        karantina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(PaletActivity.this, "karantina", "4", barcode);
            }
        });

        lulus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(PaletActivity.this, "lulus", "4", barcode);
            }
        });


        logname = findViewById(R.id.log_name);
        lognote = findViewById(R.id.log_note);
        logtime = findViewById(R.id.log_time);

        produk = findViewById(R.id.txt_nama_produk);
        pcs = findViewById(R.id.txt_pcs);
        username = findViewById(R.id.txt_username);
        level = findViewById(R.id.txt_level);
        scan = findViewById(R.id.txt_scan_date);
        ed = findViewById(R.id.txt_expired_date);
        status = findViewById(R.id.txt_status);
        md = findViewById(R.id.txt_make_date);
        station = findViewById(R.id.txt_station_name);
        count = findViewById(R.id.txt_count);
        bar = findViewById(R.id.barcode);
        nie = findViewById(R.id.txt_nie);
        sku = findViewById(R.id.txt_sku);
        rvChild = findViewById(R.id.rv_child);
        rvBatch = findViewById(R.id.rv_batch);
        rvBatch.setLayoutManager(new LinearLayoutManager(this));
        rvChild.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onResume(){
        super.onResume();
        loading.show();
        getData(barcode, token);
    }


    void getData(String barcode, String token){
        Call<Response> response = apiInterface.getDetails("Bearer " + token, barcode, "4");
        response.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                loading.dismiss();
                Response data = response.body();
                if (data == null){
                    Toast.makeText(PaletActivity.this, "No data available!", Toast.LENGTH_SHORT).show();
                } else {
                    if (data.getStatus().equals("success")){
                        produk.setText(data.getProducts().getProduk());
                        if (data.getProducts().getProductData().getUsername() == null){
                            username.setText("-");
                        } else {
                            username.setText(data.getProducts().getProductData().getUsername());
                        }

                        logname.setText(data.getProducts().getProductData().getLog().getName());
                        lognote.setText(data.getProducts().getProductData().getLog().getNote());
                        logtime.setText(data.getProducts().getProductData().getLog().getDate());

                        status.setText(data.getProducts().getProductData().getStatus());
                        bar.setText(data.getProducts().getProductData().getSerialisasi());
                        bar.setTextIsSelectable(true);
                        level.setText(data.getProducts().getProductData().getLevel());
                        scan.setText(data.getProducts().getProductData().getScannedDate());
                        ed.setText(data.getProducts().getProductData().getExpiredDate().replace("00:00:00",""));
                        md.setText(data.getProducts().getProductData().getProductionDate().replace("00:00:00",""));
                        station.setText(data.getProducts().getProductData().getLine());
                        count.setText(String.valueOf(data.getProducts().getInheritance().getChild().size()));
                        pcs.setText(data.getProducts().getProductData().getTotal() + " Pcs");

                        nie.setText(data.getProducts().getProductData().getNie());
                        sku.setText(data.getProducts().getProductData().getSku());
                        BatchAdapter batchAdapter = new BatchAdapter(PaletActivity.this, data.getProducts().getProductData().getPcs());
                        rvBatch.setAdapter(batchAdapter);
                        ChildAdapter adapter = new ChildAdapter(PaletActivity.this, data.getProducts().getInheritance().getChild());
                        rvChild.setAdapter(adapter);
                        adapter.setOnItemClickListener(new ChildAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                TextView bc = v.findViewById(R.id.txt_barcode);
                                Intent i = new Intent(PaletActivity.this, MasterBoxActivity.class);
                                i.putExtra("barcode", bc.getText().toString());
                                startActivity(i);
                            }
                        });

                    } else {
                        Toast.makeText(PaletActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                loading.dismiss();
                Log.d("errordataAPI", t.getLocalizedMessage());
                Toast.makeText(PaletActivity.this, "Server Maintenance : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void checkProduct(String status, String level, String barcode, String note){
        Call<ResponseStatus> responseStatusCall = apiInterface.statusRequest(status, level, barcode, note, "Bearer " + token);
        responseStatusCall.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, retrofit2.Response<ResponseStatus> response) {
                loading.dismiss();
                ResponseStatus data = response.body();
                if (data.getStatus().equals("success")){
                    Toast.makeText(PaletActivity.this, "Status berhasil diupdate!" , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaletActivity.this, data.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                Toast.makeText(PaletActivity.this, t.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }




        public void showDialog(Activity activity, String title, String level, String barcode){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_replace);

            TextView text = (TextView) dialog.findViewById(R.id.title);
            EditText edtnote = (EditText) dialog.findViewById(R.id.edt_desc);
            Button btnok = (Button) dialog.findViewById(R.id.btn_ok);
            Button btncancel = (Button) dialog.findViewById(R.id.btn_cancel);
            text.setText(title);

            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtnote.getText().toString().equals("") || edtnote.getText().toString().equals(null)){
                        Toast.makeText(PaletActivity.this, "Keterangan harus diisi!", Toast.LENGTH_SHORT).show();
                    } else {
                        loading.show();
                        checkProduct(title, level, barcode, edtnote.getText().toString());
                        dialog.dismiss();
                        getData(barcode, token);
                    }
                }
            });

            dialog.show();

        }

}
