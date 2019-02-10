package com.sda.ambulanta.apk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.sda.ambulanta.apk.menu.*;
import com.sda.pacient.apk.R;
import java.io.IOException;
import java.security.PublicKey;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;

public class AmbulantaActivity extends AppCompatActivity implements LocationListener {

    public Network networktask;
    public static boolean available = true;
    private int backIsPressed=0;
    private ProgressBar progressBar;
    private ProgressBar cyclicProgressBar;
    public static boolean ocupat = false;
    private int MY_PERMISSION_ACCESS_COURSE_LOCATION = 111;
    private LocationManager mLocationManager = null;
    private Location ambulantaLocation = null;
    private double latitude;
    private double longitude;
    private PublicKey ServerPubKey = null;


    private double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344; // distance in Kilometers

        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    @Override
    public void onBackPressed() {
        backIsPressed++;
        if(backIsPressed>=2) {
            MessageBox("Serviciul de ambulante se va inchide!");
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }else
        {
            Toast.makeText(getApplicationContext(), "Apasati BACK inca o data pentru a inchide aplicatia!", Toast.LENGTH_SHORT).show();
        }
//        super.onBackPressed(); //commented this line in order to disable back press
    }
    public void navigatie(View view, double lat, double longi){
        System.out.println("Ambulanta!!");

//        String urlnav = "http://maps.google.com/maps?saddr=" + String.valueOf(lat) + "," + String.valueOf(longi) + "&daddr="+latitude+","+longitude;
        String urlnav = "http://maps.google.com/maps?saddr=" + latitude+","+longitude + "&daddr=" + String.valueOf(lat) + "," + String.valueOf(longi);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(urlnav));
        startActivity(intent);
    }
    public void GoToUrgente(View view, double lat, double longi) {
        Intent intent = new Intent(this, UrgenteActivity.class);
        intent.putExtra("latAmbulanta",latitude);
        intent.putExtra("lonAmbulanta",longitude);
        intent.putExtra("latPacient",lat);
        intent.putExtra("lonPacient",longi);
        startActivity(intent);
        navigatie(view,lat,longi);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulanta);
        progressBar = (ProgressBar) findViewById(R.id.pbURG);
        progressBar.setVisibility(View.INVISIBLE);
        cyclicProgressBar = (ProgressBar) findViewById(R.id.progressBar_cyclicURG);
        cyclicProgressBar.setVisibility(View.INVISIBLE);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ambulantaLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        latitude = ambulantaLocation.getLatitude();
        longitude = ambulantaLocation.getLongitude();

        System.out.println("Ambulanta lat : " + latitude +"long : "+ longitude);

        Button urgenteBTN = findViewById(R.id.urgenteBTN);
        if(ocupat == false)
        {
            urgenteBTN.setEnabled(true);
            urgenteBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_color_urg));
        }else {
            urgenteBTN.setEnabled(false);
            urgenteBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.round_shape_pressed_urg));
        }

        urgenteBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    cyclicProgressBar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
//                    networktask = new Network();
//                    networktask.SOSprimit(v);

                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    cyclicProgressBar.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    networktask = new Network();
                    networktask.SOSprimit(v);
                    return true;
                }
                return false;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_file_a, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.id1) {
            Toast.makeText(this, "Setare disponibilitate", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AvailableActivity.class);
            startActivity(intent);

        } else if (i == R.id.id2) {
            Toast.makeText(this, "Cerinte catre dispecer", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SpecialRequirementsActivity.class);
            startActivity(intent);

        } else if (i == R.id.id3) {
            Toast.makeText(this, "Delogare", Toast.LENGTH_SHORT).show();
            byte[] cryptedAmbulantaAction = null;
            String ambulantaAction = "Logout";
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
                cryptedAmbulantaAction = LoginActivity.encrypt(ServerPubKey,ambulantaAction);
                LoginActivity.oop.writeObject(cryptedAmbulantaAction);

                String answerLock = (String) LoginActivity.oin.readObject();
                if(answerLock.equals("0"))
                {//lock failed
                    Toast.makeText(getApplicationContext(), "Deconectarea a esuat!", Toast.LENGTH_SHORT).show();
                } else if(answerLock.equals("1"))
                {//lock succeeded
                    LoginActivity.setUsr("-");
                    LoginActivity.setPass("-");
                    Toast.makeText(getApplicationContext(), "Deconectare cu succes!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    this.startActivity(intent);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            return true;
    }
    @Override
    public void finish() {
        super.finish();
    }
    public void MessageBox(String MyMessage) {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("---Warning!---");
        builder.setMessage(MyMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", null);
        Dialog dialog = builder.create();
        dialog.show();
        return;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class Network extends AsyncTask<View,Void,Boolean> {



        public void getEmergency(final View view) throws Exception {
            String SOSreceived = null;
            try {
                SOSreceived = (String) LoginActivity.oin.readObject();
                byte[] pacientLocationBytes = (byte[]) LoginActivity.oin.readObject();
                System.out.println(pacientLocationBytes);
                Location locationPacient = ParcelableUtil.unmarshall(pacientLocationBytes, Location.CREATOR);

                System.out.println("Mesaj primit de la Pacient : " + SOSreceived);
                System.out.println("Pacient lat : " + locationPacient.getLatitude() + "long : " + locationPacient.getLongitude());


                GoToUrgente(view, locationPacient.getLatitude(), locationPacient.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void SOSprimit(final View view){
            byte[] cryptedPacientAction = null;
            String pacientAction = "SOSprimit";
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey, pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);

                LoginActivity.oop.writeObject(latitude);
                LoginActivity.oop.writeObject(longitude);

                getEmergency(view);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected Boolean doInBackground (View... params) {
            return true;
        }
    }

}
