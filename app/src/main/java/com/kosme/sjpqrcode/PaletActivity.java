package com.kosme.sjpqrcode;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.kosme.sjpqrcode.model.ResponseCheckUpdate;
import com.kosme.sjpqrcode.model.ResponseStatus;
import com.kosme.sjpqrcode.model.User;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

public class PaletActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    String barcode, token;
    TextView produk, level, scan, ed, md, station, bar, count, username, pcs, txtreject, txtkarantina, txtlulus, status, logname, logtime, lognote, nie, sku, info;
    RecyclerView rvChild, rvBatch;
    ProgressDialog loading;
    FrameLayout frameLayout;
    SharedPreferences sharedPreferences;
    ResponseCheckUpdate test;
    ImageButton add, back;
    ImageView img, imgInfo;

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

        back = findViewById(R.id.btn_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        logname = findViewById(R.id.log_name);
        lognote = findViewById(R.id.log_note);
        logtime = findViewById(R.id.log_time);

        frameLayout = findViewById(R.id.frame);

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
        add = findViewById(R.id.btn_add);
        img = findViewById(R.id.imageView);
        info = findViewById(R.id.txt_info);
        imgInfo = findViewById(R.id.img_info);

        if (login.getAuth().getPermission().contains("dataprint.karantina")){
            add.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(PaletActivity.this, barcode);
            }
        });

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

                        updatedb(barcode);

                    } else {
                        Toast.makeText(PaletActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                        updatedb(barcode);
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

    void updatedb(String barcode){
        Api.getClient2().create(ApiInterface.class).checkdata(barcode).enqueue(new Callback<ResponseCheckUpdate>() {
            @Override
            public void onResponse(Call<ResponseCheckUpdate> call, retrofit2.Response<ResponseCheckUpdate> response) {
                loading.dismiss();
                frameLayout.setVisibility(View.VISIBLE);
                test = response.body();
                info.setText(test.getDesc());
                if (test.getStatus().equals("0")){
                    imgInfo.setImageResource(R.drawable.status_ok);
                } else {
                    imgInfo.setImageResource(R.drawable.status_not);
                }
            }

            @Override
            public void onFailure(Call<ResponseCheckUpdate> call, Throwable t) {
                loading.dismiss();
                frameLayout.setVisibility(View.VISIBLE);
                if(t instanceof SocketTimeoutException){
                    info.setText(t.getMessage());
                }
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
                Toast.makeText(PaletActivity.this, "Status berhasil diupdate!" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                Toast.makeText(PaletActivity.this, t.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }




        public void showDialog(Activity activity, String barcode){
            final Dialog dialog = new Dialog(activity);


            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.pop_up_status);

            ImageButton btnLulus = (ImageButton) dialog.findViewById(R.id.btn_lulus);
            ImageButton btnKarantina = (ImageButton) dialog.findViewById(R.id.btn_karantina);
            ImageButton btnReject = (ImageButton) dialog.findViewById(R.id.btn_reject);

            btnLulus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheetDialog("lulus", barcode);
                    dialog.dismiss();
                }
            });

            btnKarantina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheetDialog("karantina", barcode);
                    dialog.dismiss();
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheetDialog("reject", barcode);
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

    private void showBottomSheetDialog(String status, String barcode) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.form_desc);

        EditText notes = bottomSheetDialog.findViewById(R.id.input_desc);
        ImageView img = bottomSheetDialog.findViewById(R.id.img_title);
        Button ok = bottomSheetDialog.findViewById(R.id.btn_ok);
        Button kembali = bottomSheetDialog.findViewById(R.id.btn_kembali);

        if (status == "lulus"){
            img.setImageResource(R.drawable.form_desc_lulus);
        } else if (status == "karantina"){
            img.setImageResource(R.drawable.form_desc_karantina);
        } else {
            img.setImageResource(R.drawable.form_desc_reject);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notes.getText().toString() == "" || notes.getText().toString().isEmpty() || notes.getText().toString() == null){
                    Toast.makeText(v.getContext(), "Harap mengisikan deskripsi!", Toast.LENGTH_SHORT).show();
                } else {
                    checkProduct(status, "4", barcode, notes.getText().toString());
                }
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

}
