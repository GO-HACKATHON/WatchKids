package setia.example.com.watchkids.KidsActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import setia.example.com.watchkids.APIHelper.WatchClient;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import setia.example.com.watchkids.Activity.LoginActivity;
import setia.example.com.watchkids.Helper.PreferenceManager;
import setia.example.com.watchkids.Model.Kids;
import setia.example.com.watchkids.Model.Respond;
import setia.example.com.watchkids.ParentActivity.ParentHomeActivity;
import setia.example.com.watchkids.R;
import setia.example.com.watchkids.Service.KidsPushLocationService;

public class KidsHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnPanic;
    private GoogleMap map;
    private Marker limitMarker;
    private Marker parentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnPanic = (Button)findViewById(R.id.btn_panic);
        btnPanic.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Gagal "+ PreferenceManager.getId(), Toast.LENGTH_SHORT).show();
                WatchClient.get().getPanic(PreferenceManager.getId()).enqueue(new Callback<Respond>() {
                    @Override
                    public void onResponse(Call<Respond> call, Response<Respond> response) {
                        if(response.body().getError().equals(false)){
                            Toast.makeText(getApplicationContext(), "You pressed panic button", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Respond> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Tidak terhubung dengan jaringan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        startService(new Intent(KidsHomeActivity.this, KidsPushLocationService.class));

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        if(map != null){
            WatchClient.get().getLimitKids(PreferenceManager.getId()).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(Call<Respond> call, Response<Respond> response) {
                    if(response.body().getError().equals(false)){
                        LatLng latLng = new LatLng(Double.valueOf(response.body().getDataLimit().get(0).getLatitude()), Double.valueOf(response.body().getDataLimit().get(0).getLongitude()));
                        limitMarker = map.addMarker(new MarkerOptions().position(latLng).title("Batas Anda").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        Circle circle = map.addCircle(new CircleOptions()
                                .center(latLng)
                                .radius(1000)
                                .strokeColor(Color.BLACK));
                    }
                }

                @Override
                public void onFailure(Call<Respond> call, Throwable t) {

                }
            });

            WatchClient.get().GetParentsLocation(PreferenceManager.getId()).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(Call<Respond> call, Response<Respond> response) {
                    if(response.body().getError().equals(false)){
                        LatLng latLng = new LatLng(Double.valueOf(response.body().getProfile().get(0).getLatitude()), Double.valueOf(response.body().getProfile().get(0).getLongitude()));
                        parentMarker = map.addMarker(new MarkerOptions().position(latLng).title("Orang Tua Anda").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    }
                }

                @Override
                public void onFailure(Call<Respond> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_logout) {
            PreferenceManager.logout();
            startActivity(new Intent(KidsHomeActivity.this, LoginActivity.class));
            finish();
        } else if(id == R.id.nav_message) {
            startActivity(new Intent(KidsHomeActivity.this,KidsMessageActivity.class ));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
