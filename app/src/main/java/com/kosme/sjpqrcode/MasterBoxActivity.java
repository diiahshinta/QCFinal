package com.kosme.sjpqrcode;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.kosme.sjpqrcode.adapter.BatchAdapter;
import com.kosme.sjpqrcode.adapter.ChildAdapter;
import com.kosme.sjpqrcode.adapter.ExpiredAdapter;
import com.kosme.sjpqrcode.adapter.SerialisasiAdapter;
import com.kosme.sjpqrcode.api.Api;
import com.kosme.sjpqrcode.api.ApiInterface;
import com.kosme.sjpqrcode.model.DataSerialisasi;
import com.kosme.sjpqrcode.model.Login;
import com.kosme.sjpqrcode.model.Replace;
import com.kosme.sjpqrcode.model.Response;
import com.kosme.sjpqrcode.model.ResponseCheckUpdate;
import com.kosme.sjpqrcode.model.ResponseStatus;
import com.kosme.sjpqrcode.model.Serialisasi;
import com.kosme.sjpqrcode.model.User;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MasterBoxActivity extends AppCompatActivity {

    ApiInterface apiInterface, apiInterface2;
    String barcode, token;
    TextView info, produk, level, scan, ed, md, station, bar, parent, count, username, pcs,status, logname, logtime, lognote, nie, sku;
    RecyclerView rvChild, rvBatch, rvEd;
    ProgressDialog loading;
    ImageButton back, add;
    ImageView img, imgInfo;
    FrameLayout frameLayout;
    ResponseCheckUpdate test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_master_box);

        barcode = getIntent().getStringExtra("barcode");
        apiInterface = Api.getClient().create(ApiInterface.class);
        apiInterface2 = Api.getClient3().create(ApiInterface.class);
        loading = ProgressDialog.show(MasterBoxActivity.this, null, "loading", true, false);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
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

        username = findViewById(R.id.txt_username);
        produk = findViewById(R.id.txt_nama_produk);
        level = findViewById(R.id.txt_level);
        scan = findViewById(R.id.txt_scan_date);
        pcs = findViewById(R.id.txt_pcs);
        ed = findViewById(R.id.txt_expired_date);
        md = findViewById(R.id.txt_make_date);
        station = findViewById(R.id.txt_station_name);
        bar = findViewById(R.id.barcode);
        parent = findViewById(R.id.txt_parent);
        count = findViewById(R.id.txt_count);
        status = findViewById(R.id.txt_status);
        nie = findViewById(R.id.txt_nie);
        sku = findViewById(R.id.txt_sku);
        add = findViewById(R.id.btn_add);
        img = findViewById(R.id.imageView);
        imgInfo = findViewById(R.id.img_info);
        info = findViewById(R.id.txt_info);

        rvChild = findViewById(R.id.rv_child);
        rvChild.setLayoutManager(new LinearLayoutManager(this));
        rvBatch = findViewById(R.id.rv_batch);
        rvBatch.setLayoutManager(new LinearLayoutManager(this));
        rvEd = findViewById(R.id.rv_ed);
        rvEd.setLayoutManager(new LinearLayoutManager(this));

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
                showDialog(MasterBoxActivity.this, barcode);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        loading.show();
        getData(barcode, token);
    }

    void getDoubleSerial(String serialisasi, String barcode){
        Call<Serialisasi> response = apiInterface2.doubleSerialisasi(serialisasi);
        response.enqueue(new Callback<Serialisasi>() {
            @Override
            public void onResponse(Call<Serialisasi> call, retrofit2.Response<Serialisasi> response) {
                loading.dismiss();
                Serialisasi data = response.body();
                if (data.getTotal() == 0){
                    Toast.makeText(MasterBoxActivity.this, data.getResult(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MasterBoxActivity.this, data.getResult(), Toast.LENGTH_SHORT).show();
                    showDialog2(MasterBoxActivity.this, data.getDataSerialisasi(), barcode);
                }

            }

            @Override
            public void onFailure(Call<Serialisasi> call, Throwable t) {
                Toast.makeText(MasterBoxActivity.this, "Server Maintenance : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getData(String barcode, String token){
        Call<Response> response = apiInterface.getDetails("Bearer " + token, barcode, "3");
        response.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                loading.dismiss();
                Response data = response.body();
                updatedb(barcode);
                if (data == null){
                    Toast.makeText(MasterBoxActivity.this, "No data available!", Toast.LENGTH_SHORT).show();
                } else {
                    if (data.getStatus().equals("success")){
                        loading.show();
                        getDoubleSerial(data.getProducts().getProductData().getSerialisasi(), data.getProducts().getProductData().getBarcode());
                        logname.setText(data.getProducts().getProductData().getLog().getName());
                        lognote.setText(data.getProducts().getProductData().getLog().getNote());
                        logtime.setText(data.getProducts().getProductData().getLog().getDate());
                        bar.setText(data.getProducts().getProductData().getSerialisasi());
                        bar.setTextIsSelectable(true);
                        status.setText(data.getProducts().getProductData().getStatus());
                        produk.setText(data.getProducts().getProduk());
                        level.setText(data.getProducts().getProductData().getLevel());
                        scan.setText(data.getProducts().getProductData().getScannedDate());
                        nie.setText(data.getProducts().getProductData().getNie());
                        sku.setText(data.getProducts().getProductData().getSku());
                        //ed.setText(data.getProducts().getProductData().getExpiredDate().replace("00:00:00",""));
                        md.setText(data.getProducts().getProductData().getProductionDate().replace("00:00:00",""));
                        station.setText(data.getProducts().getProductData().getLine());
                        pcs.setText(data.getProducts().getProductData().getTotal() + " Pcs");
                        if (data.getProducts().getProductData().getUsername() == null){
                            username.setText("-");

                        } else {
                            username.setText(data.getProducts().getProductData().getUsername());
                        }
                        count.setText(String.valueOf(data.getProducts().getInheritance().getChild().size()));
                        if (data.getProducts().getInheritance().getParent() != null){
                            SpannableString content = new SpannableString(data.getProducts().getInheritance().getParent().getSerialisasi());
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            parent.setText(content);
                            parent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(MasterBoxActivity.this, PaletActivity.class);
                                    i.putExtra("barcode", data.getProducts().getInheritance().getParent().getBarcode());
                                    startActivity(i);
                                    finish();
                                }
                            });
                        } else {
                            parent.setText("-");
                        }
                        BatchAdapter batchAdapter = new BatchAdapter(MasterBoxActivity.this, data.getProducts().getProductData().getPcs());
                        rvBatch.setAdapter(batchAdapter);
                        ExpiredAdapter expiredAdapter = new ExpiredAdapter(MasterBoxActivity.this, data.getProducts().getProductData().getPcs());
                        rvEd.setAdapter(expiredAdapter);
                        ChildAdapter adapter = new ChildAdapter(MasterBoxActivity.this, data.getProducts().getInheritance().getChild());
                        rvChild.setAdapter(adapter);
                        adapter.setOnItemClickListener(new ChildAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                TextView bc = v.findViewById(R.id.txt_barcode);
                                Toast.makeText(MasterBoxActivity.this, bc.getText().toString(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MasterBoxActivity.this, InnerboxActivity.class);
                                startActivity(i);
                            }
                        });

                    } else {
                        Toast.makeText(MasterBoxActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                loading.dismiss();
                Log.d("errordataAPI", t.getLocalizedMessage());
                Toast.makeText(MasterBoxActivity.this, "Server Maintenance : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void updatedb(String barcode){
        Api.getClient2().create(ApiInterface.class).checkdata(barcode).enqueue(new Callback<ResponseCheckUpdate>() {
            @Override
            public void onResponse(Call<ResponseCheckUpdate> call, retrofit2.Response<ResponseCheckUpdate> response) {
                loading.dismiss();
                Log.d("responseok", new Gson().toJson(response.body()));
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
                if(t instanceof SocketTimeoutException){
                    Log.d("responseoktimeout", t.getMessage());
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
                Log.d("--tesdata", new Gson().toJson(data));
//                if (data.getStatus().equals("success")){
//                    Toast.makeText(MasterBoxActivity.this, "Status berhasil diupdate!" , Toast.LENGTH_SHORT).show();
//                } else {
//
//                    Toast.makeText(MasterBoxActivity.this, data.getMessage() , Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(MasterBoxActivity.this, t.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialog2(Activity activity, ArrayList<DataSerialisasi> data, String bar){
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_duplicate);

        ImageView back = (ImageView) dialog.findViewById(R.id.btn_close);
        RecyclerView rvDuplicate = (RecyclerView) dialog.findViewById(R.id.rv_duplicate);
        rvDuplicate.setLayoutManager(new LinearLayoutManager(this));
        SerialisasiAdapter serialisasiAdapter = new SerialisasiAdapter(dialog.getContext(), data);
        rvDuplicate.setAdapter(serialisasiAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Status Box akan otomatis menjadi Karantina!")
                        .setTitle("Duplicate Data!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                loading.show();
                                checkProduct("karantina", "3", bar, "Duplicate Data");
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        dialog.show();

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

        if (status.equals("lulus")){
            img.setImageResource(R.drawable.form_desc_lulus);
        } else if (status.equals("karantina")){
            img.setImageResource(R.drawable.form_desc_karantina);
        } else {
            img.setImageResource(R.drawable.form_desc_reject);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkProduct(status, "3", barcode, notes.getText().toString());
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
