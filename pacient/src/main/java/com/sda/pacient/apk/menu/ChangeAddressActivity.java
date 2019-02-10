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

import java.io.IOException;
import java.security.PublicKey;

public class ChangeAddressActivity extends AppCompatActivity {
    public Network networktask;
    private TextInputEditText cnpView;
    private TextInputEditText uidView;
    private TextInputEditText passwordView;
    private TextInputEditText addressView;

    public void GoBackToPacient(View view) {
        Intent intent = new Intent(this, PacientActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeaddress);



        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button NewAddressBTN = findViewById(R.id.NewAddressBTN);
        NewAddressBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new ChangeAddressActivity.Network();
                networktask.execute(view);
                networktask.changeAddress(view);
            }
        });
        cnpView = (TextInputEditText) findViewById(R.id.cnpAddr);
        uidView = (TextInputEditText) findViewById(R.id.uidAddr);
        passwordView = (TextInputEditText) findViewById(R.id.passwordAddr);
        addressView = (TextInputEditText) findViewById(R.id.newAddress);

    }


    public class Network extends AsyncTask<View,Void,Boolean> {

        public void changeAddress(View view){
            System.out.println("ChangeAddress");
            byte[] cryptedPacientAction = null;
            String pacientAction = "changeAddress";

            byte[] cryptedCnp = null;
            byte[] cryptedUid = null;
            byte[] cryptedPassword = null;
            byte[] cryptedAddress = null;

            String cnp = cnpView.getText().toString();
            String uid = uidView.getText().toString();
            String password = passwordView.getText().toString();
            String address = addressView.getText().toString();
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();

                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);


                cryptedCnp = LoginActivity.encrypt(ServerPubKey,cnp);
                cryptedUid = LoginActivity.encrypt(ServerPubKey,uid);
                cryptedPassword = LoginActivity.encrypt(ServerPubKey,password);
                cryptedAddress = LoginActivity.encrypt(ServerPubKey,address);

                LoginActivity.oop.writeObject(cryptedCnp);
                LoginActivity.oop.writeObject(cryptedUid);
                LoginActivity.oop.writeObject(cryptedPassword);
                LoginActivity.oop.writeObject(cryptedAddress);

                String answerLock = (String) LoginActivity.oin.readObject();

                if(answerLock.equals("0"))
                {//lock failed
                    Toast.makeText(getApplicationContext(), "Schimbare adresa - esuata!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Pacientul nu exista sau conexiunea cu serverul e inchisa!", Toast.LENGTH_SHORT).show();
                    GoBackToPacient(view);
                } else if(answerLock.equals("1"))
                {//lock succeeded
                    Toast.makeText(getApplicationContext(), "Adresa schimbata cu succes!", Toast.LENGTH_SHORT).show();
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
