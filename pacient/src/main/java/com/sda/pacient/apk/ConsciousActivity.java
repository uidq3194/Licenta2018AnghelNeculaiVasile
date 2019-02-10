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

import com.sda.pacient.apk.menu.ChangeMedicalHistoryActivity;

public class ConsciousActivity  extends AppCompatActivity {
    public Network networktask;
    private int backIsPressed=0;
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
    public void GoBackToSosInfo(View view) {
        SOSinfoActivity.consciousFlag = false;
        Intent intent = new Intent(this, SOSinfoActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conscious);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button consciousYesBTN = findViewById(R.id.consciousYesBTN);
        consciousYesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new ConsciousActivity.Network();
                networktask.execute(view);
                networktask.consciousYES(view);
            }
        });
        Button consciousNoBTN = findViewById(R.id.consciousNoBTN);
        consciousNoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new ConsciousActivity.Network();
                networktask.execute(view);
                networktask.consciousNO(view);
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

        public void consciousYES(View view){
            System.out.println("ConsciousYES");
            SOSinfoActivity.SosInfoStr.append("Constient : Da \n");
            GoBackToSosInfo(view);
        }
        public void consciousNO(View view){
            System.out.println("ConsciousNO");
            SOSinfoActivity.SosInfoStr.append("Constient : Nu \n");
            GoBackToSosInfo(view);
        }

        @Override
        protected Boolean doInBackground (View... params) {
            return true;
        }
    }
}
