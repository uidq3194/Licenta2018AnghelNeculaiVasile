package com.sda.pacient.apk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sda.pacient.apk.menu.ChangeAddressActivity;
import com.sda.pacient.apk.menu.ChangeMedicalHistoryActivity;
import com.sda.pacient.apk.menu.ChangePassActivity;
import com.sda.pacient.apk.menu.LockAccountActivity;

import java.io.IOException;
import java.security.PublicKey;

/**
 * Created by necul on 12/25/2018.
 */

public class PacientActivity extends AppCompatActivity implements LocationListener {

    private Network networktask;
    private long then = 0;
    private ProgressBar progressBar;
    private ProgressBar cyclicProgressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();
    private Button SOSBtn;
    private int backIsPressed=0;
    private static int MY_PERMISSION_ACCESS_COURSE_LOCATION = 111;
    private LocationManager mLocationManager = null;
    private Location pacientLocation = null;
    public static double durata;


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
    public void StartSOSinfoActivity(View view) {
        Intent intent = new Intent(this, SOSinfoActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient);

        progressBar = (ProgressBar) findViewById(R.id.pb);
        progressBar.setVisibility(View.INVISIBLE);
        cyclicProgressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        cyclicProgressBar.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.textView);


        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        pacientLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        SOSBtn = findViewById(R.id.SOSBtn);

        SOSBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MessageBox("Tineti apasat cel putin 5 secunde pentru a trimite semnalul SOS!");
            }
        });
        SOSBtn.setOnLongClickListener(new View.OnLongClickListener() {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            @Override
            public boolean onLongClick(View view) {
                if(view == SOSBtn) {
                    vibrator.vibrate(5000);
                }
                    return false;
            }
        });

        then = 0;
        SOSBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    then = (Long) System.currentTimeMillis();
                    cyclicProgressBar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    cyclicProgressBar.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    if(((Long) System.currentTimeMillis() - then) >= 5000){
                            networktask = new PacientActivity.Network();
                            networktask.SOS(v);
                            networktask.execute(v);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
//        MessageBox("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }
    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("disable");
    }
    @Override
    public void onProviderEnabled(String provider) {
        System.out.println("enable");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        System.out.println("status");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_file, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.i1) {
            Toast.makeText(this, "Setari", Toast.LENGTH_SHORT).show();

        } else if (i == R.id.i2) {
            Toast.makeText(this, "Schimbare parola", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ChangePassActivity.class);
            startActivity(intent);

        } else if (i == R.id.i3) {
            Toast.makeText(this, "Schimbare resedinta", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ChangeAddressActivity.class);
            startActivity(intent);

        } else if (i == R.id.i4) {
            Toast.makeText(this, "Schimbare Stare de sanatate", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ChangeMedicalHistoryActivity.class);
            startActivity(intent);

        } else if (i == R.id.i5) {
            Toast.makeText(this, "Blocare cont (telefon pierdut)", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LockAccountActivity.class);
            startActivity(intent);

        } else if (i == R.id.i7) {
            Toast.makeText(this, "Deconectare", Toast.LENGTH_SHORT).show();
            byte[] cryptedPacientAction = null;
            String pacientAction = "Logout";
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);

                String answerLock = (String) LoginActivity.oin.readObject();
                if(answerLock.equals("0"))
                {//lock failed
                    Toast.makeText(getApplicationContext(), "Deconectarea a esuat!", Toast.LENGTH_SHORT).show();
                } else if(answerLock.equals("1"))
                {//lock succeeded
                    Toast.makeText(getApplicationContext(), "Deconectare cu succes!", Toast.LENGTH_SHORT).show();
                    LoginActivity.setUsr("-");
                    LoginActivity.setPass("-");
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


    public class Network extends AsyncTask<View,Void,Boolean> {

        public void SOS(View view){
            byte[] cryptedPacientAction = null;
            String pacientAction = "SOS";
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);
                System.out.println("Pacient lat : " + pacientLocation.getLatitude() +"long : "+ pacientLocation.getLongitude());
                byte[] pacientLocationBytes = ParcelableUtil.marshall(pacientLocation);
                LoginActivity.oop.writeObject(pacientLocationBytes);
                System.out.println(pacientLocationBytes);
                LoginActivity.oop.writeObject(pacientLocation.getLatitude());
                LoginActivity.oop.writeObject(pacientLocation.getLongitude());
                double min = (double) LoginActivity.oin.readObject();
                System.out.println(min);
                durata = (min * 60) / 35 ;
                durata = durata + 10 ;
                System.out.println("Durata = " + durata );
                String answerLock = (String) LoginActivity.oin.readObject();



                if(answerLock.equals("0"))
                {//lock failed
                    MessageBox("Urgenta a fost inregistrata deja!");
                    Toast.makeText(getApplicationContext(), "Semnalul SOS nu a fost trimis! Serviciul SdA nu este disponibil! Apelati 112 pentru urgente!", Toast.LENGTH_SHORT).show();
                } else if(answerLock.equals("1"))
                {//lock succeeded
                    Toast.makeText(getApplicationContext(), "SOS trimis!", Toast.LENGTH_SHORT).show();
                    System.out.println("SOS sent!!");
                    SOSinfoActivity.heartBeatFlag = true;
                    SOSinfoActivity.consciousFlag = true;
                    SOSinfoActivity.stableFlag = true;
                    SOSinfoActivity.bodyTemperatureFlag = true;
                    SOSinfoActivity.blodPressureFlag = true;
                    SOSinfoActivity.immoFlag = true;
                    SOSinfoActivity.otherFlag = true;
                    StartSOSinfoActivity(view);
                }

            } catch (IOException e) {
                e.printStackTrace();
                //exceptie read write socket
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                //exceptie class not found
            } catch (Exception e) {
                e.printStackTrace();
                //exceptie criptare
            }
        }

        @Override
        protected Boolean doInBackground (View... params) {
            return true;
        }
    }
}
