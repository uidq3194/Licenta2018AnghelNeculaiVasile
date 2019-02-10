package com.sda.medic;

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

import java.io.IOException;
import java.security.PublicKey;

public class SpecialRequirementsActivity extends AppCompatActivity {
    public Network networktask;
    private TextInputEditText mesajView;
    public void GoBackToAmbulance(View view) {
        Intent intent = new Intent(this, MedicActivity.class);
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

        public void requirements(View view){
            System.out.println("Cerinte catre dispecer");

            String mesajPac = mesajView.getText().toString();

            if (mesajPac.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Mesajele goale nu vor fi trimise catre dispecer.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                byte[] cryptedPacientAction = null;
                String pacientAction = "CerinteDispecer";
                PublicKey ServerPubKey = null;
                try {
                    ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
                    cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
                    LoginActivity.oop.writeObject(cryptedPacientAction);
                    byte[] cryptedMsg = null;
                    cryptedMsg = LoginActivity.encrypt(ServerPubKey,mesajPac);
                    LoginActivity.oop.writeObject(cryptedMsg);
                    System.out.println(mesajPac);


                    String answerLock = (String) LoginActivity.oin.readObject();

                    if(answerLock.equals("0"))
                    {//lock failed
                        Toast.makeText(getApplicationContext(), "Mesaj netrimis", Toast.LENGTH_SHORT).show();
                    }
                    else if(answerLock.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Mesaj trimis!", Toast.LENGTH_SHORT).show();
                        GoBackToAmbulance(view);
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
        }

        @Override
        protected Boolean doInBackground (View... params) {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements);

        mesajView = (TextInputEditText) findViewById(R.id.requirementsTxt);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button requiremntsBTN = findViewById(R.id.requiremntsBTN);
        requiremntsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SpecialRequirementsActivity.Network();
                networktask.execute(view);
                networktask.requirements(view);
            }
        });

    }
}

