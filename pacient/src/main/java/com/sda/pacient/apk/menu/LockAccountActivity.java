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

public class LockAccountActivity extends AppCompatActivity {
    public Network networktask;
    private TextInputEditText cnpLockView;
    private TextInputEditText uidLockView;
    private TextInputEditText passwordLockView;

    public void GoBackToPacient(View view) {
        Intent intent = new Intent(this, PacientActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockaccount);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Button LockBTN = findViewById(R.id.LockBTN);
        LockBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new LockAccountActivity.Network();
                networktask.execute(view);
                networktask.lockAccount(view);
            }
        });

        cnpLockView = (TextInputEditText) findViewById(R.id.cnpLock);
        uidLockView = (TextInputEditText) findViewById(R.id.uidLock);
        passwordLockView = (TextInputEditText) findViewById(R.id.passwordLock);

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

        public void lockAccount(View view){
            System.out.println("Lock Account ");
            byte[] cryptedPacientAction = null;
            String pacientAction = "lockAccount";

            byte[] cryptedCnpLock = null;
            byte[] cryptedUidLock = null;
            byte[] cryptedPasswordLock = null;

            String cnpLock = cnpLockView.getText().toString();
            String uidLock = uidLockView.getText().toString();
            String passwordLock = passwordLockView.getText().toString();
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();

                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);


                cryptedCnpLock = LoginActivity.encrypt(ServerPubKey,cnpLock);
                cryptedUidLock = LoginActivity.encrypt(ServerPubKey,uidLock);
                cryptedPasswordLock = LoginActivity.encrypt(ServerPubKey,passwordLock);

                LoginActivity.oop.writeObject(cryptedCnpLock);
                LoginActivity.oop.writeObject(cryptedUidLock);
                LoginActivity.oop.writeObject(cryptedPasswordLock);

                String answerLock = (String) LoginActivity.oin.readObject();

                if(answerLock.equals("0"))
                {//lock failed
                    Toast.makeText(getApplicationContext(), "Blocare esuata!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Pacientul nu exista sau conexiunea cu serverul e inchisa!", Toast.LENGTH_SHORT).show();
                    GoBackToPacient(view);
                } else if(answerLock.equals("1"))
                {//lock succeeded
                    Toast.makeText(getApplicationContext(), "Pacient blocat cu succes!", Toast.LENGTH_SHORT).show();
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
