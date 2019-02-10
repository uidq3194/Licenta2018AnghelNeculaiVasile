package com.sda.medic;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicActivity extends AppCompatActivity {

    private Network networktask;
    private long then = 0;
    private ProgressBar progressBar;
    private ProgressBar cyclicProgressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();
    private Button SOSBtn;
    private int backIsPressed=0;
    private static int MY_PERMISSION_ACCESS_COURSE_LOCATION = 111;
    private LocationManager mLocationManager = null;
    private Location pacientLocation = null;
    public static double durata;
    private String pacientAction = "MoniotrURG";
    private static List<String>  urgenteUpdate = new ArrayList<>();
    private static LinearLayout layout = null;
    private static Map<String,String>  SOSinfo = new HashMap<>();

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic);
        progressBar = findViewById(R.id.progressBar_cyclic);
        layout = findViewById(R.id.UrgLayout1);
        new Network().execute();
    }

    private void updateUrgente (final List<String> urgenteUpdate){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Button btn[] = new Button[1000];
                int i=0;
                layout.removeAllViews();
                for (String urg : urgenteUpdate) {
                    System.out.println(urgenteUpdate.size());
                    btn[i] = new Button(MedicActivity.this);
                    btn[i].setText(urg);
                    btn[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("URGENTA");
                            Button b = (Button)v;
                            MessageBox(SOSinfo.get(b.getText().toString()));
                        }
                    });
                    layout.addView(btn[i]);
                    i++;

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_file, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.i1) {
//            Toast.makeText(this, "Deconectare", Toast.LENGTH_SHORT).show();
//            byte[] cryptedPacientAction = null;
            pacientAction = "Logout";
//            PublicKey ServerPubKey = null;
//            try {
//                ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
//                cryptedPacientAction = LoginActivity.encrypt(ServerPubKey,pacientAction);
//                LoginActivity.oop.writeObject(cryptedPacientAction);
//
//                String answerLock = (String) LoginActivity.oin.readObject();
//                if(answerLock.equals("0"))
//                {//lock failed
//                    Toast.makeText(getApplicationContext(), "Deconectarea a esuat!", Toast.LENGTH_SHORT).show();
//                } else if(answerLock.equals("1"))
//                {//lock succeeded
//                    Toast.makeText(getApplicationContext(), "Deconectare cu succes!", Toast.LENGTH_SHORT).show();
//                    LoginActivity.setUsr("-");
//                    LoginActivity.setPass("-");
//                    Intent intent = new Intent(this, LoginActivity.class);
//                    this.startActivity(intent);
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }else if (i == R.id.i2) {

            pacientAction = "Cerinte";
        }
        return true;
    }
    private List<String> parcurgereUrgente (StringBuilder urg){
        List<String>  auxURG = new ArrayList<String>();
        if(urg.equals("----------"))
        {
            auxURG.add("----------");
            return auxURG;
        }

        StringBuilder aux = new StringBuilder();
        String copyUrgenta = null;
        for(int i=0; i < urg.length() ; i++){
            if(urg.charAt(i)!=';' && urg.charAt(i)!='#' )
            {
                aux.append(urg.charAt(i));
            }
            else if(urg.charAt(i)=='#')
            {
                auxURG.add(aux.toString());
                SOSinfo.put(aux.toString(),"--");
                copyUrgenta = aux.toString();
                aux.setLength(0);
            }
            else if(urg.charAt(i)==';')
            {
                SOSinfo.replace(copyUrgenta,aux.toString());
                aux.setLength(0);
            }

        }

        return auxURG;
    }
    public class Network extends AsyncTask<View,Void,Boolean> {
        @Override
        protected Boolean doInBackground (View... params) {
            while (true) {
                byte[] cryptedPacientAction = null;
                PublicKey ServerPubKey = null;
                try {
                    ServerPubKey = (PublicKey) LoginActivity.oin.readObject();
                    cryptedPacientAction = LoginActivity.encrypt(ServerPubKey, pacientAction);
                    LoginActivity.oop.writeObject(cryptedPacientAction);

                    if (pacientAction.equals("Logout")) {
                        String answerLock = (String) LoginActivity.oin.readObject();
                        if (answerLock.equals("0")) {//lock failed
//                            Toast.makeText(getApplicationContext(), "Deconectarea a esuat!", Toast.LENGTH_SHORT).show();
                        } else if (answerLock.equals("1")) {//lock succeeded
//                            Toast.makeText(getApplicationContext(), "Deconectare cu succes!", Toast.LENGTH_SHORT).show();
                            LoginActivity.setUsr("-");
                            LoginActivity.setPass("-");
                            Intent intent = new Intent(MedicActivity.this, LoginActivity.class);
                            MedicActivity.this.startActivity(intent);
                            return true;
                        }
                    } else if (pacientAction.equals("MoniotrURG")) {
                        StringBuilder Urgente = (StringBuilder) LoginActivity.oin.readObject();
                        System.out.println(Urgente);
                        urgenteUpdate = parcurgereUrgente(Urgente);
                        System.out.println(urgenteUpdate);
                        updateUrgente(urgenteUpdate);
                        String answerLock = (String) LoginActivity.oin.readObject();
                    }
                    else if (pacientAction.equals("Cerinte"))
                    {
                        Intent intent = new Intent(MedicActivity.this, SpecialRequirementsActivity.class);
                        startActivity(intent);
                        pacientAction = "MoniotrURG";
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
