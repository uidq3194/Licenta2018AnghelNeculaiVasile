package com.sda.pacient.apk;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.PublicKey;

public class SOSinfoActivity extends AppCompatActivity {

    public Network networktask;
    private int backIsPressed = 0;
    public static StringBuffer SosInfoStr = new StringBuffer("\n ------Date primite de la pacient------ \n");
    public static boolean heartBeatFlag = true;
    public static boolean consciousFlag = true;
    public static boolean stableFlag = true;
    public static boolean bodyTemperatureFlag = true;
    public static boolean blodPressureFlag = true;
    public static boolean immoFlag = true;
    public static boolean otherFlag = true;
    private static int MY_PERMISSION_CALL_PHONE = 333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosinfo);
        backIsPressed = 0;



        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button heartBeatBTN = findViewById(R.id.heartBeatBTN);
        if (heartBeatFlag == true) {
            heartBeatBTN.setEnabled(true);
            heartBeatBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_color_sos));
        } else {
            heartBeatBTN.setEnabled(false);
            heartBeatBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.round_shape_pressed_sos));
        }
        heartBeatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SOSinfoActivity.Network();
                networktask.execute(view);
                networktask.heartBeat(view);
            }
        });
        Button consciousBTN = findViewById(R.id.consciousBTN);
        if (consciousFlag == true) {
            consciousBTN.setEnabled(true);
            consciousBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_color_sos));
        } else {
            consciousBTN.setEnabled(false);
            consciousBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.round_shape_pressed_sos));
        }
        consciousBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SOSinfoActivity.Network();
                networktask.execute(view);
                networktask.conscious(view);
            }
        });
        Button stableBTN = findViewById(R.id.stableBTN);
        if (stableFlag == true) {
            stableBTN.setEnabled(true);
            stableBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_color_sos));
        } else {
            stableBTN.setEnabled(false);
            stableBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.round_shape_pressed_sos));
        }
        stableBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SOSinfoActivity.Network();
                networktask.execute(view);
                networktask.stable(view);
            }
        });
        Button bodyTemperatureBTN = findViewById(R.id.bodyTemperatureBTN);
        if (bodyTemperatureFlag == true) {
            bodyTemperatureBTN.setEnabled(true);
            bodyTemperatureBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_color_sos));
        } else {
            bodyTemperatureBTN.setEnabled(false);
            bodyTemperatureBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.round_shape_pressed_sos));
        }
        bodyTemperatureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SOSinfoActivity.Network();
                networktask.execute(view);
                networktask.bodyTemperature(view);
            }
        });
        Button blodPressureBTN = findViewById(R.id.blodPressureBTN);
        if (blodPressureFlag == true) {
            blodPressureBTN.setEnabled(true);
            blodPressureBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_color_sos));
        } else {
            blodPressureBTN.setEnabled(false);
            blodPressureBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.round_shape_pressed_sos));
        }
        blodPressureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SOSinfoActivity.Network();
                networktask.execute(view);
                networktask.blodPressure(view);
            }
        });
        Button pictureBTN = findViewById(R.id.pictureBTN);
        if (immoFlag == true) {
            pictureBTN.setEnabled(true);
            pictureBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_color_sos));
        } else {
            pictureBTN.setEnabled(false);
            pictureBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.round_shape_pressed_sos));
        }
        pictureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SOSinfoActivity.Network();
                networktask.execute(view);
                networktask.immo(view);
            }
        });
        Button otherBTN = findViewById(R.id.otherBTN);
        if (otherFlag == true) {
            otherBTN.setEnabled(true);
            otherBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_color_sos));
        } else {
            otherBTN.setEnabled(false);
            otherBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.round_shape_pressed_sos));
        }
        otherBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SOSinfoActivity.Network();
                networktask.execute(view);
                networktask.other(view);
            }
        });
        Button sendInfoBTN = findViewById(R.id.sendInfoBTN);
        sendInfoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
                networktask = new SOSinfoActivity.Network();
                networktask.execute(view);
                networktask.sendInfo(view);
            }
        });

    }

    @Override
    public void onBackPressed() {
        backIsPressed++;
        if (backIsPressed >= 2) {
            MessageBox("Serviciul de ambulante se va inchide!");
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Toast.makeText(getApplicationContext(), "Apasati BACK inca o data pentru a inchide aplicatia!", Toast.LENGTH_SHORT).show();
        }
//         super.onBackPressed(); //commented this line in order to disable back press
    }

    public void GoBackToPacient(View view) {
        final Intent intent = new Intent(this, PacientActivity.class);
        final Intent intent1 = new Intent(Intent.ACTION_CALL);
        intent1.setData(Uri.parse("tel:0753677562"));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Timp de asteptare pentru ambulanta")
                .setMessage(PacientActivity.durata + " minute .")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Apel 112", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (ContextCompat.checkSelfPermission(SOSinfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(SOSinfoActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSION_CALL_PHONE);
                        }
                        startActivity(intent);
                        startActivity(intent1);

                    }
                })
                .setNegativeButton("Asteapta Ambulanta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                    }
                })
                .show();

    }
    public void StartHeartBeatActivity(View view) {
        Intent intent = new Intent(this, HeartBeatActivity.class);
        startActivity(intent);
    }
    public void StartConsciousActivity(View view) {
        Intent intent = new Intent(this, ConsciousActivity.class);
        startActivity(intent);
    }
    public void StartStableActivity(View view) {
        Intent intent = new Intent(this, StableActivity.class);
        startActivity(intent);
    }
    public void StartBodyTemperatureActivity(View view) {
        Intent intent = new Intent(this, BodyTemperatureActivity.class);
        startActivity(intent);
    }
    public void StartBlodPressureActivity(View view) {
        Intent intent = new Intent(this, BloodPressureActivity.class);
        startActivity(intent);
    }
    public void StartPictureActivity(View view) {
        Intent intent = new Intent(this, ImmoActivity.class);
        startActivity(intent);
    }
    public void StartOtherActivity(View view) {
        Intent intent = new Intent(this, OtherActivity.class);
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

        public void heartBeat(View view){

            System.out.println("heartBeat");
            StartHeartBeatActivity(view);

        }
        public void conscious(View view){

            System.out.println("conscious");
            StartConsciousActivity(view);

        }
        public void stable(View view){

            System.out.println("activity_stable");
            StartStableActivity(view);

        }
        public void bodyTemperature(View view){

            System.out.println("bodyTemperature");
            StartBodyTemperatureActivity(view);

        }
        public void blodPressure(View view){

            System.out.println("blodPressure");
            StartBlodPressureActivity(view);

        }
        public void immo(View view){

            System.out.println("picture");
            StartPictureActivity(view);

        }
        public void other(View view){

            System.out.println("other");
            StartOtherActivity(view);

        }
        public void sendInfo(View view){
            System.out.println("SOSinfoSend");
            byte[] cryptedPacientAction = null;
            String pacientAction = "SOSinfoSend";
            PublicKey ServerPubKey = null;
            try {
                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
                LoginActivity.oop.writeObject(cryptedPacientAction);
                LoginActivity.oop.writeObject(SOSinfoActivity.SosInfoStr);
                System.out.println(SosInfoStr);
                String answerLock = (String) LoginActivity.oin.readObject();

                if(answerLock.equals("0"))
                {//lock failed
                    Toast.makeText(getApplicationContext(), "SOS INFO netrimis", Toast.LENGTH_SHORT).show();
                } else if(answerLock.equals("1"))
                {//lock succeeded
                    Toast.makeText(getApplicationContext(), "SOS INFO netrimis!", Toast.LENGTH_SHORT).show();
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
