package setia.example.com.watchkids.KidsActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import setia.example.com.watchkids.APIHelper.WatchClient;
import setia.example.com.watchkids.Helper.PreferenceManager;
import setia.example.com.watchkids.Model.Respond;
import setia.example.com.watchkids.R;

public class KidsMessageActivity extends AppCompatActivity {

    private Button buttonSend;
    private EditText chatText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_message);

        buttonSend = (Button) findViewById(R.id.send);

        chatText = (EditText) findViewById(R.id.msg);

        buttonSend.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                WatchClient.get().CreateMessage(PreferenceManager.getId(),String.valueOf(1),chatText.getText().toString(),String.valueOf("2017-02-11 12:12:12")).enqueue(new Callback<Respond>() {
                    @Override
                    public void onResponse(Call<Respond> call, Response<Respond> response) {
                        if(response.body().getError().equals(false)){
                            Toast.makeText(getApplicationContext(), "Pesan Terkirim", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal Mengirim Pesan", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Respond> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Tidak terhubung dengan jaringan", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("send","berhasil");
            }
        });

    }
}
