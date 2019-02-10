package com.sda.ambulanta.apk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sda.ambulanta.apk.menu.AvailableActivity;
import com.sda.pacient.apk.R;

import java.io.IOException;
import java.security.PublicKey;

public class UrgenteActivity extends AppCompatActivity {

    private ProgressDialog pd = null;
    private String data = null;
    private static PublicKey ServerPubKey;
    private TextView textView2 = null;
    private static String result1 = null;

    private TextInputEditText pulsURGView;
    private TextInputEditText constientURGView;
    private TextInputEditText stabilURGView;
    private TextInputEditText temperaturaURGView;
    private TextInputEditText tensiuneURGView;
    private TextInputEditText otherURGView;
    private TextInputEditText cancelReasonView;
    private double latAmbulanta;
    private double lonAmbulanta;
    private double latPacient;
    private double lonPacient;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgente);
        Bundle extras = getIntent().getExtras();
        Intent i = getIntent();
        latAmbulanta = extras.getDouble("latAmbulanta");
        lonAmbulanta = extras.getDouble("lonAmbulanta");
        latPacient = extras.getDouble("latPacient");
        lonPacient = extras.getDouble("lonPacient");

        pulsURGView = findViewById(R.id.pulsURG);
        constientURGView = findViewById(R.id.constientURG);
        stabilURGView = findViewById(R.id.stabilURG);
        temperaturaURGView = findViewById(R.id.temperaturaURG);
        tensiuneURGView = findViewById(R.id.tensiuneURG);
        otherURGView = findViewById(R.id.otherURG);
        cancelReasonView = findViewById(R.id.cancelReason);



        textView2 = findViewById(R.id.textView2);
        try {
            this.pd = ProgressDialog.show(this, "Loading..", "SdA asteapta date despre starea pacientului.", true, false);
            new Network().execute();


        } catch (Exception e) {
            e.printStackTrace();
        }
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button sendinfoUrgBTN = findViewById(R.id.sendinfoUrgBTN);
        sendinfoUrgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                new Network().SOSInfoprimit(view);
            }
        });
        Button showinfopacientBTN = findViewById(R.id.showinfopacientBTN);
        showinfopacientBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                textView2.setText(result1);
            }
        });
        Button cancelUrgBTN = findViewById(R.id.cancelUrgBTN);
        cancelUrgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                new Network().cancelUrg(view);

            }
        });

    }

    public void GoBackToAmbulance(View view) {
        Intent intent = new Intent(this, AmbulantaActivity.class);
        startActivity(intent);
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


        public void SOSInfoprimit(View view){

            String puls = pulsURGView.getText().toString();
            String constient = constientURGView.getText().toString();
            String stabil = stabilURGView.getText().toString();
            String temperatura = temperaturaURGView.getText().toString();
            String tensiune = tensiuneURGView.getText().toString();
            String other = otherURGView.getText().toString();

            StringBuffer SOSInfoUrgentaAmbulanta = new StringBuffer();
            SOSInfoUrgentaAmbulanta.append("Date primite de la Ambulanta despre pacient :\n");

            if (puls.isEmpty())
            {
                SOSInfoUrgentaAmbulanta.append("Puls : --\n");
            }
            else
            {
                SOSInfoUrgentaAmbulanta.append("Puls : " + puls + "\n");
            }
            if (constient.isEmpty())
            {
                SOSInfoUrgentaAmbulanta.append("Constient : --\n");
            }
            else
            {
                SOSInfoUrgentaAmbulanta.append("Constient : " + constient + "\n");
            }
            if (stabil.isEmpty())
            {
                SOSInfoUrgentaAmbulanta.append("Stabil : --\n");
            }
            else
            {
                SOSInfoUrgentaAmbulanta.append("Stabil : " + stabil + "\n");
            }
            if (temperatura.isEmpty())
            {
                SOSInfoUrgentaAmbulanta.append("Temperatura : --\n");
            }
            else
            {
                SOSInfoUrgentaAmbulanta.append("Temperatura : " + temperatura + "\n");
            }
            if (tensiune.isEmpty())
            {
                SOSInfoUrgentaAmbulanta.append("Tensiune : --\n");
            }
            else
            {
                SOSInfoUrgentaAmbulanta.append("Tensiune : " + tensiune + "\n");
            }
            if (other.isEmpty())
            {
                SOSInfoUrgentaAmbulanta.append("Alte detalii : --\n");
            }
            else
            {
                SOSInfoUrgentaAmbulanta.append("Alte detalii : " + other + "\n");
            }
            byte[] cryptedPacientAction = null;
            byte[] cryptedSOSInfoUrgentaAmbulanta = null;
            String pacientAction = "Simptome";

            try {

                cryptedPacientAction = LoginActivity.encrypt(LoginActivity.serverPubKey,pacientAction);
                cryptedSOSInfoUrgentaAmbulanta = LoginActivity.encrypt(LoginActivity.serverPubKey,SOSInfoUrgentaAmbulanta.toString());
                LoginActivity.oop.writeObject(cryptedPacientAction);


                double auxDistanta = distance(latAmbulanta,lonAmbulanta,latPacient,lonPacient);
                LoginActivity.oop.writeObject(auxDistanta);
                LoginActivity.oop.writeObject(cryptedSOSInfoUrgentaAmbulanta);

                GoBackToAmbulance(view);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        public void cancelUrg(View view){

            byte[] cryptedPacientAction = null;
            String pacientAction = "Anulare";
            try {
                cryptedPacientAction = LoginActivity.encrypt(LoginActivity.serverPubKey,pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);
                LoginActivity.oop.writeObject(cancelReasonView.getText().toString());
                GoBackToAmbulance(view);
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
            StringBuffer SOSINFOreceived;

            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
//                double auxDistanta = distance(latAmbulanta,lonAmbulanta,latPacient,lonPacient);
//                LoginActivity.oop.writeObject(auxDistanta);
                SOSINFOreceived = (StringBuffer) LoginActivity.oin.readObject();
                System.out.println(SOSINFOreceived);
                result1 = SOSINFOreceived.toString();


                UrgenteActivity.this.onPostExecute(SOSINFOreceived.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }


    }


    protected void onPostExecute(String result) {
        //resultis the data returned from doInbackground
        UrgenteActivity.this.data = result;
        if (result != null) {
            UrgenteActivity.this.pd.dismiss();

        }

    }


}
