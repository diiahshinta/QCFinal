package com.kosme.sjpqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kosme.sjpqrcode.api.Api;
import com.kosme.sjpqrcode.api.ApiInterface;
import com.kosme.sjpqrcode.model.Login;
import com.kosme.sjpqrcode.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edtemail, edtpassword;
    Button login;
    ApiInterface apiInterface;
    ProgressDialog loading;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.btn_login);

        edtemail = findViewById(R.id.edt_email);
        edtpassword = findViewById(R.id.edt_pass);
        apiInterface = Api.getClient().create(ApiInterface.class);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(LoginActivity.this, null, "loading", true, false);
                loginReq(edtemail.getText().toString(), edtpassword.getText().toString());
            }
        });


    }

    void loginReq(String email, String pass){
        Call<Login> response = apiInterface.loginRequest(email, pass);
        response.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                loading.dismiss();
                Login data = response.body();
                Log.d("datalogin", new Gson().toJson(data));
                if (data == null){
                    Toast.makeText(LoginActivity.this, "No data available!", Toast.LENGTH_SHORT).show();
                } else {
                    if (data.getStatus().equals("success")){
                        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(data);
                        editor.putString("Login", json);
                        editor.commit();

                        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Maintenance : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}