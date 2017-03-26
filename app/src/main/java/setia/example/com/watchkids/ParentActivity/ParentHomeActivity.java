package setia.example.com.watchkids.ParentActivity;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
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
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
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
    private int alerted = 0;
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
                                .radius(100)
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
        } else if (id == R.id.nav_message) {
            startActivity(new Intent(ParentHomeActivity.this, MessageActivity.class));
            finish();
        } else if (id == R.id.nav_logout) {
            PreferenceManager.logout();
            startActivity(new Intent(ParentHomeActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void giveNotification(String message) {
        //Setting notification yang ingin ditampilkan
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("Anak Anda Melewati Batas")
                        .setContentText(message)
                        .setVibrate(new long[]{ 1000, 1000, 1000, 1000, 1000});
        // Membuat intent yang dituju ketika notifikasi ditekan
        Intent resultIntent = new Intent(this, ParentHomeActivity.class);
        // objek stack builder digunakan untuk memastikan ketika back ditekan dari, aplikasi anda akan menuju homescreen
        TaskStackBuilder stackBuilder = TaskStackBuilder. create(this);
        stackBuilder.addParentStack(ParentHomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent. FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager)
                        getSystemService(Context. NOTIFICATION_SERVICE);
        // 101 adalah ID notifikasi, digunakan jika kita ingin mengupdate notifikasi.
        mNotificationManager.notify(101, mBuilder.build());
    }

    private void giveNotification2(String message) {
        //Setting notification yang ingin ditampilkan
        NotificationCompat.Builder mBuilders =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("Anak Anda Menekan Panic Button!")
                        .setContentText(message)
                        .setVibrate(new long[]{ 1000, 1000, 1000, 1000, 1000});
        // Membuat intent yang dituju ketika notifikasi ditekan
        Intent resultIntent = new Intent(this, ParentHomeActivity.class);
        // objek stack builder digunakan untuk memastikan ketika back ditekan dari, aplikasi anda akan menuju homescreen
        TaskStackBuilder stackBuilder = TaskStackBuilder. create(this);
        stackBuilder.addParentStack(ParentHomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent. FLAG_UPDATE_CURRENT);
        mBuilders.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager)
                        getSystemService(Context. NOTIFICATION_SERVICE);
        // 101 adalah ID notifikasi, digunakan jika kita ingin mengupdate notifikasi.
        mNotificationManager.notify(101, mBuilders.build());
    }
    private void cekPanics(){
        WatchClient.get().getAllKids(PreferenceManager.getId()).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(Call<Respond> call, Response<Respond> response) {
                if(response.body().getError().equals(false)){
                    if(response.body().getDataKids().get(0).getPanic().equals("1")){
                        giveNotification2("Anak anda menekan Panic Button di Latitude= "+response.body().getDataKids().get(0).getLatitude()+" Longitude " + response.body().getDataKids().get(0).getLongitude());
                    }
                }
                else{
                    Toast.makeText(ParentHomeActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respond> call, Throwable t) {
                Toast.makeText(ParentHomeActivity.this, "Gagal menghubungkan ke internet", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void startTimerThread() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            private long startTime = System.currentTimeMillis();
            public void run() {
                while (onScreen == 1) {
                    try {
                        Thread.sleep(1000);
                        cekPanics();
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
                                                limitKids.put(response.body().getDataLimit().get(0).getKidsId(), new LatLng(Double.valueOf(response.body().getDataLimit().get(0).getLatitude()), Double.valueOf(response.body().getDataLimit().get(0).getLongitude())));
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
                                        double temp = SphericalUtil.computeDistanceBetween(markerKids.get(loop).getPosition(), limitKids.get(listKids.get(loop).getId()));
                                        if(temp > 100){
                                            //Toast.makeText(ParentHomeActivity.this, temp + " " + alerted, Toast.LENGTH_SHORT).show();
                                            if(alerted == 0){
                                                temp = Math.floor(temp * 100) / 100000;
                                                giveNotification("Anak anda berada " + temp + " kilometer dari posisi yang anda tentukan");
                                            }
                                        }
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
