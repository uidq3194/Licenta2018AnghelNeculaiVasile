package com.sda.pacient.apk;

/**
 * This class extends Activity to handle a picture preview, process the preview
 * for a red values and determine a heart beat.
 *
 * @author Justin Wetherell <phishman3579@gmail.com>
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sda.pacient.apk.menu.ChangeMedicalHistoryActivity;

import java.util.concurrent.atomic.AtomicBoolean;

public class HeartBeatActivity extends AppCompatActivity {


    public Network networktask;
    private int backIsPressed=0;
    private TextInputEditText HeartBeatSOS;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartbeat);

        HeartBeatSOS = findViewById(R.id.HeartBeatSOS);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button getHeartBBTN = findViewById(R.id.getHeartBBTN);
        getHeartBBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new HeartBeatActivity.Network();
                networktask.execute(view);
                networktask.getHeartB(view);
            }
        });

    }
    public void GoBackToSosInfo(View view) {
        SOSinfoActivity.heartBeatFlag = false;
        Intent intent = new Intent(this, SOSinfoActivity.class);
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

        public void getHeartB(View view){
            System.out.println("Puls ");
            SOSinfoActivity.SosInfoStr.append("Puls : " + HeartBeatSOS.getText().toString() + " \n");

            GoBackToSosInfo(view);
        }

        @Override
        protected Boolean doInBackground (View... params) {
            return true;
        }
    }


}
