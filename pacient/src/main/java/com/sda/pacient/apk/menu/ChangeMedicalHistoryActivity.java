package com.sda.pacient.apk.menu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sda.pacient.apk.LoginActivity;
import com.sda.pacient.apk.PacientActivity;
import com.sda.pacient.apk.R;
import com.sda.pacient.apk.SOSinfoActivity;

import java.io.IOException;
import java.security.PublicKey;

public class ChangeMedicalHistoryActivity extends AppCompatActivity {
    public Network networktask;
    private TextInputEditText cnpView;
    private TextInputEditText uidView;
    private TextInputEditText passwordView;
    private TextInputEditText medView;


    public void GoBackToPacient(View view) {
        Intent intent = new Intent(this, PacientActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changemedical);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button ChangeMedBTN = findViewById(R.id.ChangeMedBTN);
        ChangeMedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new ChangeMedicalHistoryActivity.Network();
                networktask.execute(view);
                networktask.changeMedicalHistory(view);
            }
        });
        cnpView = (TextInputEditText) findViewById(R.id.cnpMed);
        uidView = (TextInputEditText) findViewById(R.id.uidMed);
        passwordView = (TextInputEditText) findViewById(R.id.passwordMed);
        medView = (TextInputEditText) findViewById(R.id.medNew);

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

        public void changeMedicalHistory(View view){
            System.out.println("Change Medical History");
            byte[] cryptedPacientAction = null;
            String pacientAction = "changeMedicalHistory";

            byte[] cryptedCnp = null;
            byte[] cryptedUid = null;
            byte[] cryptedPassword = null;
            byte[] cryptedMed = null;

            String cnp = cnpView.getText().toString();
            String uid = uidView.getText().toString();
            String password = passwordView.getText().toString();
            String med = medView.getText().toString();
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();

                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);


                cryptedCnp = LoginActivity.encrypt(ServerPubKey,cnp);
                cryptedUid = LoginActivity.encrypt(ServerPubKey,uid);
                cryptedPassword = LoginActivity.encrypt(ServerPubKey,password);
                cryptedMed = LoginActivity.encrypt(ServerPubKey,med);

                LoginActivity.oop.writeObject(cryptedCnp);
                LoginActivity.oop.writeObject(cryptedUid);
                LoginActivity.oop.writeObject(cryptedPassword);
                LoginActivity.oop.writeObject(cryptedMed);

                String answerLock = (String) LoginActivity.oin.readObject();

                if(answerLock.equals("0"))
                {//lock failed
                    Toast.makeText(getApplicationContext(), "Schimbare istoric medical - esuata!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Pacientul nu exista sau conexiunea cu serverul e inchisa!", Toast.LENGTH_SHORT).show();
                    GoBackToPacient(view);
                } else if(answerLock.equals("1"))
                {//lock succeeded
                    Toast.makeText(getApplicationContext(), "Istoric medical schimbat cu succes!", Toast.LENGTH_SHORT).show();
                    GoBackToPacient(view);
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
