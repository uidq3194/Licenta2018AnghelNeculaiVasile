package com.sda.pacient.apk;

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
import android.widget.Toast;

public class StableActivity extends AppCompatActivity {

    public Network networktask;
    private int backIsPressed=0;
    public void GoBackToSosInfo(View view) {
        SOSinfoActivity.stableFlag = false;
        Intent intent = new Intent(this, SOSinfoActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stable);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button stableYesBTN = findViewById(R.id.stableYesBTN);
        stableYesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new StableActivity.Network();
                networktask.execute(view);
                networktask.stableYES(view);
            }
        });
        Button stableNoBTN = findViewById(R.id.stableNoBTN);
        stableNoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new StableActivity.Network();
                networktask.execute(view);
                networktask.stableNO(view);
            }
        });

    }
    @Override
    public void onBackPressed() {
        backIsPressed++;
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);

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

        public void stableYES(View view){
            System.out.println("heartBeat");
            SOSinfoActivity.SosInfoStr.append("Stabil : Da \n");
            GoBackToSosInfo(view);
        }
        public void stableNO(View view){
            System.out.println("heartBeat");
            SOSinfoActivity.SosInfoStr.append("Stabil : Nu \n");
            GoBackToSosInfo(view);
        }

        @Override
        protected Boolean doInBackground (View... params) {
            return true;
        }
    }
}
