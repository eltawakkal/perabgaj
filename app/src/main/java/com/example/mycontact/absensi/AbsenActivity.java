package com.example.mycontact.absensi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mycontact.R;
import com.example.mycontact.model.User;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.example.mycontact.pref.PrimaPref;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsenActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static int PERMISSION_CODE = 1;
    private static int SETTING_LOCATION_CODE = 2;

    private ApiEndPoint apiEndPoint;
    private PrimaPref pref;
    private User user;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String deviceName;
    private Location userLocation;
    private LatLng userLocationLatLong;
    private LatLng officeLocation;
    private ProgressDialog progressDialog;

    private int userDistance;

    private TextView tvUserName, tvUserDistance, tvMessageAbsen, tvAbsenType;
    private ImageView imgUser;
    private FloatingActionButton fabAbsen;

    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;
    private int absenType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);

        initView();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_absen);
        mapFragment.getMapAsync(this);

        deviceName = getDeviceName();
    }

    private void initView() {
        getGpsNetworkState();

        pref = new PrimaPref(this);
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tunggu");
        progressDialog.setMessage("Meyiapkan Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        tvUserName = findViewById(R.id.tv_user_name_absen);
        tvUserDistance = findViewById(R.id.tv_user_distance_absen);
        tvMessageAbsen = findViewById(R.id.tv_message_absen);
        tvAbsenType = findViewById(R.id.tv_absen_type_absen_activity);
        imgUser = findViewById(R.id.img_user_absen);
        fabAbsen = findViewById(R.id.fab_checkin_absen);

        user = pref.getUser();
        tvUserName.setText(user.getName());

        try {
            Picasso.get().load(user.getPhoto()).into(imgUser);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.user).into(imgUser);
        }

        fabAbsen.setOnClickListener(v -> {
            absenNow();
        });
    }

    private void checkAbsen() {
        Call<List<User>> checkUser = apiEndPoint.checkAbsen(user.getId());
        checkUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> listAbsen = response.body();


                if (listAbsen.size() == 0) {
                    absenType = 0;
                    tvAbsenType.setText("Check In");
                } else if (listAbsen.size() == 1) {
                    absenType = 1;
                    tvAbsenType.setText("Check Out");
                } else {
                    tvAbsenType.setVisibility(View.GONE);
                    fabAbsen.setVisibility(View.GONE);
                    tvMessageAbsen.setText("Anda sudah melakukan absen hari ini");
                    tvUserDistance.setText("N/A"); //kasi gini aja kalau dah absen kwkw tau terserah abgn
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                fabAbsen.setVisibility(View.GONE);
                progressDialog.dismiss();
                Toast.makeText(AbsenActivity.this, "Jaringan Tidak Ditemukan , Tolong Periksa Internet Anda @PrimaGroup", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void absenNow() {
        Call<User> callAbsen = apiEndPoint.addAbsen(user.getId(), absenType);
        callAbsen.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(AbsenActivity.this, "Absensi Selesai", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("kesalahanAbsen", t.getMessage());
                Toast.makeText(AbsenActivity.this, "Jaringan Tidak Ditemukan , Tolong Periksa Internet Anda @PrimaGroup", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String readPhoneState = Manifest.permission.READ_PHONE_STATE;
            String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
            String coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;

            requestPermissions(new String[]{readPhoneState, fineLocation, coarseLocation}, PERMISSION_CODE);
        } else {
            getUserCurrentLocation();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                getUserCurrentLocation();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Tidak dapat menggunakan Fitur ini, jika lokasi tidak di aktifkan", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getUserCurrentLocation() {
        LocationRequest locationReq = new LocationRequest();
        locationReq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationReq.setInterval(10000);
        locationReq.setFastestInterval(2000);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                progressDialog.dismiss();
                userLocation = locationResult.getLastLocation();
                userLocationLatLong = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                createLineBetweenUserAndOffice();
                mFusedLocationProviderClient.removeLocationUpdates(this);
            }
        };
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationProviderClient.requestLocationUpdates(locationReq, locationCallback, Looper.getMainLooper());
//        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
//            progressDialog.show();
//
//            while (location != null) {
//                progressDialog.dismiss();
//                userLocation = location;
//                userLocationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
//                createLineBetweenUserAndOffice();
//                break;
//            }
//            if (location != null) {
//
//            } else {
//                Toast.makeText(this, "Anda baru saja menyalakan lokasi, silahkan tunggu beberapa saat", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        if (gpsEnabled && networkEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();
            }
        } else {
            requestLocationService();
        }

        createCirclePoint();
    }

    private void getGpsNetworkState() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {}

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {}
    }

    private void requestLocationService() {
        progressDialog.dismiss();

        if (!gpsEnabled && !networkEnabled) {
             new AlertDialog.Builder(this)
                     .setTitle("Kesalahan")
                     .setMessage("Wajib Mengaktifkan Lokasi Saat Absensi")
                     .setCancelable(false)
                     .setPositiveButton("Pengaturan", (dialog, which) -> {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTING_LOCATION_CODE);
                     }).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getGpsNetworkState();
        if (gpsEnabled && networkEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();
            }
        } else {
            requestLocationService();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void createCirclePoint() {
        LatLng perumOffice = new LatLng(-5.786365, 112.601270);
        officeLocation = perumOffice;
        CircleOptions circleOptions = new CircleOptions()
                .center(perumOffice)
                .radius(40)
                .fillColor(0x40ff0000)
                .strokeColor(Color.BLUE)
                .strokeWidth(5);

        mGoogleMap.addCircle(circleOptions);
    }

    boolean isMockLocation(Location location) {
        boolean isMock;
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            isMock = location.isFromMockProvider();
        } else {
            isMock = !Settings.Secure.getString(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
        }
        return isMock;
    }

    private void createLineBetweenUserAndOffice() {

        mGoogleMap.addPolyline(new PolylineOptions()
                .add(userLocationLatLong)
                .add(officeLocation)
                .clickable(false));

        float[] result = new float[1];
        Location.distanceBetween(userLocationLatLong.latitude, userLocationLatLong.longitude, officeLocation.latitude, officeLocation.longitude, result);

        userDistance = Math.round(result[0]);
        tvUserDistance.setText(userDistance + " M");
        addMarker(officeLocation);

        if (isMockLocation(userLocation)) {
            tvMessageAbsen.setText("Anda terdeteksi menggunakan alamat palsu, matikan lokasi palsu lalu coba lagi");
            fabAbsen.setVisibility(View.GONE);
        } else {
            if (userDistance > 41) {
                tvMessageAbsen.setText("Anda berada di luar jangkauan Absensi");
                fabAbsen.setVisibility(View.GONE);
            } else {
                tvMessageAbsen.setText("Anda dapat melakukan Absen sekarang");
                fabAbsen.setVisibility(View.VISIBLE);
            }
        }

        checkAbsen();

    }

    private void addMarker(LatLng latLng) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.home);
        Bitmap b = bitmapDrawable.getBitmap();
        Bitmap small = Bitmap.createScaledBitmap(b, 100, 100, false);

        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Lokasi Kantor").icon(BitmapDescriptorFactory.fromBitmap(small)));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(officeLocation, 18.0f));
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

}
