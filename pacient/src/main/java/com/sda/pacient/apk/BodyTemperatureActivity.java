package com.sda.pacient.apk;

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

public class BodyTemperatureActivity  extends AppCompatActivity {
    public Network networktask;
    private int backIsPressed=0;
    private TextInputEditText TemperaturaSOS;
    public void GoBackToSosInfo(View view) {
        SOSinfoActivity.bodyTemperatureFlag = false;
        Intent intent = new Intent(this, SOSinfoActivity.class);
        startActivity(intent);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodytemperature);

        TemperaturaSOS = (TextInputEditText) findViewById(R.id.temperature);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button temperatureSendBTN = findViewById(R.id.temperatureSendBTN);
        temperatureSendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new BodyTemperatureActivity.Network();
                networktask.execute(view);
                networktask.bodyTemperature(view);
            }
        });

    }
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

        public void bodyTemperature(View view){
            System.out.println("Body Temperature");
            SOSinfoActivity.SosInfoStr.append("Temperatura : " + TemperaturaSOS.getText().toString() + " \n");
            GoBackToSosInfo(view);
        }

        @Override
        protected Boolean doInBackground (View... params) {
            return true;
        }
    }
}
