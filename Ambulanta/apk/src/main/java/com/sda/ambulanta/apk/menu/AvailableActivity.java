package com.sda.ambulanta.apk.menu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sda.ambulanta.apk.AmbulantaActivity;
import com.sda.ambulanta.apk.LoginActivity;
import com.sda.ambulanta.apk.UrgenteActivity;
import com.sda.pacient.apk.R;

import java.io.IOException;
import java.security.PublicKey;

public class AvailableActivity extends AppCompatActivity {
    public Network networktask;
    public void GoBackToAmbulance(View view) {
        Intent intent = new Intent(this, AmbulantaActivity.class);
        startActivity(intent);
    }
    public void GoToUrgente(View view) {
        Intent intent = new Intent(this, UrgenteActivity.class);
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

        public void available(View view) throws Exception {
            System.out.println("Disponibil");
            AmbulantaActivity.available = true;
            byte[] cryptedPacientAction = null;
            String pacientAction = "Disponibil";
            PublicKey ServerPubKey = null;
            ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
            cryptedPacientAction = LoginActivity.encrypt(ServerPubKey, pacientAction);
            LoginActivity.oop.writeObject(cryptedPacientAction);
            AmbulantaActivity.ocupat = false;

            GoBackToAmbulance(view);
        }
        public void busy(View view) throws Exception {
            System.out.println("Ocupat");
            AmbulantaActivity.available = false;
            byte[] cryptedPacientAction = null;
            String pacientAction = "Ocupat";
            PublicKey ServerPubKey = null;
            ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
            cryptedPacientAction = LoginActivity.encrypt(ServerPubKey, pacientAction);
            LoginActivity.oop.writeObject(cryptedPacientAction);
            AmbulantaActivity.ocupat = true;
            GoBackToAmbulance(view);
        }

        @Override
        protected Boolean doInBackground (View... params) {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button availableYesBTN = findViewById(R.id.availableYesBTN);
        availableYesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new AvailableActivity.Network();
                networktask.execute(view);
                try {
                    networktask.available(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button availableNoBTN = findViewById(R.id.availableNoBTN);
        availableNoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new AvailableActivity.Network();
                networktask.execute(view);
                try {
                    networktask.busy(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
