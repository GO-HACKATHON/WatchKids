package setia.example.com.watchkids.Activity;

import setia.example.com.watchkids.Helper.PreferenceManager;
import setia.example.com.watchkids.KidsActivity.KidsHomeActivity;
import setia.example.com.watchkids.Model.Respond;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import setia.example.com.watchkids.APIHelper.WatchClient;
import setia.example.com.watchkids.ParentActivity.ParentHomeActivity;
import setia.example.com.watchkids.R;
import setia.example.com.watchkids.SQLHelper.SQLManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etCode;
    private Button btnLogin;
    private Button btnLoginAnak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PreferenceManager.loadManager(getApplicationContext());
        SQLManager.getReady(getApplicationContext());
        if(PreferenceManager.getRole().equals("parent")){
            Intent intent = new Intent(LoginActivity.this, ParentHomeActivity.class);
            startActivity(intent);
            finish();
        } else if(PreferenceManager.getRole().equals("kids")){
            Intent intent = new Intent(LoginActivity.this, KidsHomeActivity.class);
            startActivity(intent);
            finish();
        }
        etUsername = (EditText)findViewById(R.id.et_username);
        etPassword = (EditText)findViewById(R.id.et_password);
        etCode = (EditText)findViewById(R.id.et_code);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLoginAnak = (Button)findViewById(R.id.btn_login_anak);
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Terdapat kolom kosong", Toast.LENGTH_LONG).show();
                } else {
                    WatchClient.get().Login(etUsername.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<Respond>() {
                        @Override
                        public void onResponse(Call<Respond> call, Response<Respond> response) {
                            if(response.body().getError().equals(true)){
                                Toast.makeText(getApplicationContext(), "Akun tidak ditemukan", Toast.LENGTH_LONG).show();
                            }else{
                                PreferenceManager.setId(response.body().getProfile().get(0).getIdProfile());
                                PreferenceManager.setNama(response.body().getProfile().get(0).getFirstName() + " " + (response.body().getProfile().get(0).getLastName()));
                                PreferenceManager.setRole("parent");
                                finish();
                                startActivity(new Intent(getApplicationContext(), ParentHomeActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<Respond> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Tidak terhubung dengan jaringan", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnLoginAnak.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), etCode.getText().toString(), Toast.LENGTH_SHORT).show();
                if(etCode.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Kolom kode anak kosong", Toast.LENGTH_LONG).show();
                } else {
                    WatchClient.get().LoginKids(etCode.getText().toString()).enqueue(new Callback<Respond>() {
                        @Override
                        public void onResponse(Call<Respond> call, Response<Respond> response) {
                        //    Log.d("coba", response.body().getDataKids().get(0).getId());
                            Log.d("coba2", "test");
                            if(response.body().getError().equals(true)){
                                Log.d("cobaGetText" ,etCode.getText().toString());
                                Toast.makeText(getApplicationContext(), "Kode anak tidak ditemukan", Toast.LENGTH_LONG).show();
                            }else{
                                PreferenceManager.setId(response.body().getDataKids().get(0).getId());
                                PreferenceManager.setNama(response.body().getDataKids().get(0).getFirstName() + " " + (response.body().getDataKids().get(0).getLastName()));
                                PreferenceManager.setRole("kids");
                                finish();
                                startActivity(new Intent(getApplicationContext(), KidsHomeActivity.class));

                            }
                        }

                        @Override
                        public void onFailure(Call<Respond> call, Throwable t) {
                            Log.d("coba", t.getMessage());
                            Toast.makeText(getApplicationContext(), "Tidak terhubung dengan jaringan" + t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
