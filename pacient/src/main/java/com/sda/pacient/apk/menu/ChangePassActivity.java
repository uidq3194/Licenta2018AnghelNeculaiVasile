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

public class ChangePassActivity extends AppCompatActivity {
    public Network networktask;
    private TextInputEditText cnpChPassView;
    private TextInputEditText uidChPassView;
    private TextInputEditText passwordOldChPassView;
    private TextInputEditText passwordNewChPassView;

    public void GoBackToPacient(View view) {
        Intent intent = new Intent(this, PacientActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button ChangePassBTN = findViewById(R.id.ChangePassBTN);
        ChangePassBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new ChangePassActivity.Network();
                networktask.execute(view);
                networktask.changePass(view);
            }
        });

        cnpChPassView = (TextInputEditText) findViewById(R.id.cnpChangePass);
        uidChPassView = (TextInputEditText) findViewById(R.id.uidChangePass);
        passwordOldChPassView = (TextInputEditText) findViewById(R.id.passwordOld);
        passwordNewChPassView = (TextInputEditText) findViewById(R.id.passwordNew);
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

        public void changePass(View view){
            System.out.println("Parola Schimbata");
            byte[] cryptedPacientAction = null;
            String pacientAction = "changePass";

            byte[] cryptedCnpChPass = null;
            byte[] cryptedUidChPass = null;
            byte[] cryptedPasswordOldChPass = null;
            byte[] cryptedPasswordNewChPass = null;

            String cnpChPass = cnpChPassView.getText().toString();
            String uidChPass = uidChPassView.getText().toString();
            String passwordOldChPass = passwordOldChPassView.getText().toString();
            String passwordNewChPass = passwordNewChPassView.getText().toString();
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();

                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);


                cryptedCnpChPass = LoginActivity.encrypt(ServerPubKey,cnpChPass);
                cryptedUidChPass = LoginActivity.encrypt(ServerPubKey,uidChPass);
                cryptedPasswordOldChPass = LoginActivity.encrypt(ServerPubKey,passwordOldChPass);
                cryptedPasswordNewChPass = LoginActivity.encrypt(ServerPubKey,passwordNewChPass);

                LoginActivity.oop.writeObject(cryptedCnpChPass);
                LoginActivity.oop.writeObject(cryptedUidChPass);
                LoginActivity.oop.writeObject(cryptedPasswordOldChPass);
                LoginActivity.oop.writeObject(cryptedPasswordNewChPass);

                String answerLock = (String) LoginActivity.oin.readObject();

                if(answerLock.equals("0"))
                {//lock failed
                    Toast.makeText(getApplicationContext(), "Schimbarea parolei a esuat!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Pacientul nu exista sau conexiunea cu serverul e inchisa!", Toast.LENGTH_SHORT).show();
                    GoBackToPacient(view);
                } else if(answerLock.equals("1"))
                {//lock succeeded
                    Toast.makeText(getApplicationContext(), "Parola schimbata cu succes!", Toast.LENGTH_SHORT).show();
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
