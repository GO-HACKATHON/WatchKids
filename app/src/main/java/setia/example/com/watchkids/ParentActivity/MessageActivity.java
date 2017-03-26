package setia.example.com.watchkids.ParentActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import setia.example.com.watchkids.APIHelper.WatchClient;
import setia.example.com.watchkids.Helper.PreferenceManager;
import setia.example.com.watchkids.KidsActivity.MessageAdapter;
import setia.example.com.watchkids.Model.DataMessage;
import setia.example.com.watchkids.Model.Respond;
import setia.example.com.watchkids.R;

public class MessageActivity extends AppCompatActivity {
    ListView listView;
    private ArrayList<DataMessage> akuns;
    private MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listMessage);
        Toast.makeText(getApplicationContext(), "Loading Data", Toast.LENGTH_SHORT);
        WatchClient.get().GetMessageParent(PreferenceManager.getId()).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(Call<Respond> call, Response<Respond> response) {
                if(response.body().getError().equals(false)){
                    akuns = (ArrayList<DataMessage>) response.body().getDataMessage();
                    adapter = new MessageAdapter(MessageActivity.this, akuns);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Respond> call, Throwable t) {

            }
        });
    }

}
