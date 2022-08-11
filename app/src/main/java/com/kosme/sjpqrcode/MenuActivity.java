package com.kosme.sjpqrcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kosme.sjpqrcode.model.Login;
import com.kosme.sjpqrcode.model.User;

public class MenuActivity extends AppCompatActivity {

    View palet, box, inner, product;
    TextView txtPalet, txtBox, txtInner, txtProduct, nama, email;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Login", null);
        Login login = gson.fromJson(json, Login.class);

        palet = findViewById(R.id.menu_palet);
        box = findViewById(R.id.menu_carton);
        inner = findViewById(R.id.menu_inner);
        product = findViewById(R.id.menu_product);
        nama = findViewById(R.id.txt_name);
        email = findViewById(R.id.txt_email);

        nama.setText("Hello, " + login.getUser().getName() + "!");
        email.setText(login.getUser().getEmail());


        palet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                i.putExtra("level", "4");
                startActivity(i);
            }
        });

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                i.putExtra("level", "3");
                startActivity(i);
            }
        });

        inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                i.putExtra("level", "2");
                startActivity(i);
            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                i.putExtra("level", "1");
                startActivity(i);
            }
        });
    }
}
