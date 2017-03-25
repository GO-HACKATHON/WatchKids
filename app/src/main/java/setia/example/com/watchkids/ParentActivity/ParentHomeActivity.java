package setia.example.com.watchkids.ParentActivity;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import setia.example.com.watchkids.APIHelper.WatchClient;
import setia.example.com.watchkids.Activity.LoginActivity;
import setia.example.com.watchkids.Helper.PreferenceManager;
import setia.example.com.watchkids.Model.Kids;
import setia.example.com.watchkids.Model.Respond;
import setia.example.com.watchkids.R;
import setia.example.com.watchkids.SQLHelper.SQLManager;
import setia.example.com.watchkids.Service.ParentGetKidsService;
import setia.example.com.watchkids.Service.ParentPushLocationService;

public class ParentHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private GoogleMap map;
    private Circle circle;
    private Marker marker;
    private ArrayList<Marker> markerKids = new ArrayList<Marker>();
    private int addLimitMode = 0;
    private View layoutPutLimit;
    private TextView tvNavName;
    private TextView tvNavRole;
    private TextView tvStart;
    private Spinner kidsSpinner;
    private Button btnCreateLimit;
    private EditText etDuration;
    private int mSelectedHour;
    private int mSelectedMinute;
    private int loop;
    private HashMap<String, LatLng> limitKids = new HashMap<String, LatLng>();
    private ArrayList<Kids> listKids = new ArrayList<Kids>();
    //private List<String> kidsName = new ArrayList<String>();
    private int onScreen = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutPutLimit = findViewById(R.id.layout_put_limit);
        layoutPutLimit.setVisibility(View.INVISIBLE);

//        kidsSpinner = (Spinner)findViewById(R.id.kidsSpinner);
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kidsName);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        kidsSpinner.setAdapter(dataAdapter);
//
//        SQLManager.openKC();
//        List<Kids> listKids = SQLManager.getDataKC();
//        for(int loop = 0; loop < listKids.size(); loop++){
//            kidsName.add(listKids.get(loop).getFirstName() + " " + listKids.get(loop).getLastName());
//        }
//        SQLManager.closeKC();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        tvNavName = (TextView)header.findViewById(R.id.tv_nav_name);
        tvNavRole = (TextView)header.findViewById(R.id.tv_nav_role);
        tvStart = (TextView)findViewById(R.id.tv_start);
        btnCreateLimit = (Button)findViewById(R.id.btn_create_limit);
        etDuration = (EditText)findViewById(R.id.et_duration);

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ParentHomeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvStart.setText( selectedHour + ":" + selectedMinute);
                        mSelectedHour = selectedHour;
                        mSelectedMinute = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Start Time");
                mTimePicker.show();
            }
        });

        btnCreateLimit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etDuration.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Durasi masih kosong", Toast.LENGTH_SHORT).show();
                } else {
                    int duration = mSelectedHour + Integer.valueOf(etDuration.getText().toString());
                    WatchClient.get().CreateLimit(PreferenceManager.getId(), String.valueOf(3), tvStart.getText().toString(), duration + "" + mSelectedMinute, String.valueOf(marker.getPosition().latitude), String.valueOf(marker.getPosition().longitude)).enqueue(new Callback<Respond>() {
                        @Override
                        public void onResponse(Call<Respond> call, Response<Respond> response) {
                            if(response.body().getError().equals(false)){
                                Toast.makeText(getApplicationContext(), "Berhasil membuat aturan", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ParentHomeActivity.this, ParentHomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal membuat aturan", Toast.LENGTH_SHORT).show();
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

        tvNavName.setText(PreferenceManager.getNama());
        tvNavRole.setText(PreferenceManager.getRole());

        WatchClient.get().getAllKids(PreferenceManager.getId()).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(Call<Respond> call, Response<Respond> response) {
                if(response.body().getError().equals(false)){
                    SQLManager.openKC();
                    SQLManager.deleteDataKC();
                    Log.d("coba", response.body().toString());
                    SQLManager.insertDataKC(response.body().getDataKids());
                    SQLManager.closeKC();
                }
            }

            @Override
            public void onFailure(Call<Respond> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Tidak terhubung dengan jaringan", Toast.LENGTH_SHORT).show();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 2000);//delay 2 detik
    }

    public void init(){
        startService(new Intent(ParentHomeActivity.this, ParentPushLocationService.class));
        startService(new Intent(ParentHomeActivity.this, ParentGetKidsService.class));

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

        if (map != null) {
            startTimerThread();
//            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//                @Override
//                public void onMyLocationChange(Location arg0) {
//                    // TODO Auto-generated method stub
//                    if (marker != null) {
//                        marker.remove();
//                    }
//
//                    LatLng latLng = new LatLng(arg0.getLatitude(),arg0.getLongitude());
//
//                    marker = map.addMarker(new MarkerOptions().position(latLng).title(""));
//                    circle = map.addCircle(new CircleOptions()
//                            .center(latLng)
//                            .radius(100)
//                            .strokeColor(Color.BLACK));
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
//                    map.animateCamera(cameraUpdate);
//                }
//            });

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(addLimitMode == 1){
                        if(circle != null){
                            circle.remove();
                        }
                        if (marker != null) {
                            marker.remove();
                        }
                        circle = map.addCircle(new CircleOptions()
                                .center(latLng)
                                .radius(1000)
                                .strokeColor(Color.BLACK));
                        marker = map.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.parent_home, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            layoutPutLimit.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_put_limit) {
            addLimitMode = 1;
            layoutPutLimit.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_logout) {
            PreferenceManager.logout();
            startActivity(new Intent(ParentHomeActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startTimerThread() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            private long startTime = System.currentTimeMillis();
            public void run() {
                while (onScreen == 1) {
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            SQLManager.openKC();
                            listKids = (ArrayList<Kids>) SQLManager.getDataKC();
                            SQLManager.closeKC();
                            for(loop = 0; loop < listKids.size(); loop++){
                                if(markerKids.size() < listKids.size()){
                                    markerKids.add(loop, map.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(listKids.get(loop).getLatitude()),Double.valueOf(listKids.get(loop).getLongitude()))).title(listKids.get(loop).getFirstName())));
                                    WatchClient.get().getLimitKids(listKids.get(loop).getId()).enqueue(new Callback<Respond>() {
                                        @Override
                                        public void onResponse(Call<Respond> call, Response<Respond> response) {
                                            if(response.body().getError().equals(false)){
                                                limitKids.put(listKids.get(loop).getId(), new LatLng(Double.valueOf(response.body().getDataLimit().get(0).getLatitude()), Double.valueOf(response.body().getDataLimit().get(0).getLongitude())));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Respond> call, Throwable t) {

                                        }
                                    });
                                } else {
                                    markerKids.get(loop).remove();
                                    markerKids.set(loop, map.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(listKids.get(loop).getLatitude()),Double.valueOf(listKids.get(loop).getLongitude()))).title(listKids.get(loop).getFirstName())));
                                    if(limitKids.containsKey(listKids.get(loop).getId())){

                                    }
                                }
                            }
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }
}
