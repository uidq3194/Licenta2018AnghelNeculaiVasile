package com.sda.medic;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class LoginActivity extends AppCompatActivity {

    public static int functionREGorSIN = 1;
    private TextInputEditText mEmailView;
    private TextInputEditText mPasswordView;
    private   Network networktask;
    public static int id = 1;
    public static ObjectInputStream oin = null;
    public static ObjectOutputStream oop = null;
    public static PublicKey serverPubKey = null ;
    public static Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private int backIsPressed=0;
    private String PREFS_NAME = "Credentials";
    private static SharedPreferences settings = null;
    private static  SharedPreferences.Editor editor = null;

    public static void setUsr (String usr){
        editor.putString("usr", usr);
        editor.apply();
    }
    public static void setPass (String Pass){
        editor.putString("pass", Pass);
        editor.apply();
    }
    private String getUsr (){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("usr", null);
    }
    private String getPass () {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("pass", null);
    }
    private void setCredentials(String authentificationKey, String apkType) {
        editor.putString("storred_authentificationKey", authentificationKey);
        editor.putString("type", apkType);
        editor.apply();
    }
    private String getStorred_authentificationKey() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("storred_authentificationKey", null);
    }
    private String getStorred_apkType() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("type", null);
    }
    private int    getPortNumber(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return Integer.parseInt(settings.getString("portNumber", null));
    }
    private String getHostName(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("hostName", null);
    }
    private void   setHostPort(String hostName, int portNumber) {
        editor.putString("hostName", hostName);
        editor.putString("portNumber", Integer.toString(portNumber));
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        editor =  settings.edit();
        View view = null;

        networktask = new Network();
        networktask.execute(view);


        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);


        Button signInBTN = findViewById(R.id.signInBTN);
        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networktask = new Network();
                functionREGorSIN = 2;
                networktask.signIn(view);
                networktask.execute(view);
            }
        });

    }

    public void StartMedicActivity(View view) {
        Intent intent = new Intent(this, MedicActivity.class);
        startActivity(intent);
    }
    public static byte[] encrypt(PublicKey publicKey, @NotNull String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message.getBytes());
    }
    public static byte[] decrypt(PrivateKey privateKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
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


        private int authKey_flag = -1;
        public int dispecerLogat = 0;

        public void signIn(View view){
            byte[] cryptedUsername = null;
            byte[] cpryptedPassword = null;
            String clientLoginAnswer = "0";
            PublicKey ServerPubKey = null;
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();
            try {
                ServerPubKey = (PublicKey) oin.readObject();


                System.out.println("pubkey : " + ServerPubKey);
                System.out.println("username : " + email);
                System.out.println("password : " + password);

                cryptedUsername = encrypt(ServerPubKey,email);
                cpryptedPassword = encrypt(ServerPubKey, password);
//                mEmailView.setText("");
//                mPasswordView.setText("");
                oop.writeObject(cryptedUsername);
                oop.writeObject(cpryptedPassword);

                clientLoginAnswer = oin.readObject().toString();

                if(clientLoginAnswer.equals("1")) {
                    System.out.println("Logare reusita!");
                    setUsr(email);
                    setPass(password);
                    dispecerLogat = 1;
                    StartMedicActivity(view);
                }else if (clientLoginAnswer.equals("0"))
                {
                    MessageBox("Datele de logare sunt gresite");
                    MessageBox("ATENTIE! Orice incercare de corupere a sistemului SERVICIUL DE AMBULANTE se pedepseste penal!");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        private void instantiateMembers(){
            try {
                socket = new Socket(getHostName(), getPortNumber());
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                oin = new ObjectInputStream(socket.getInputStream());
                oop = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

        }
        public void apkAuthentification() {
            String apkType;
            PublicKey pubKey = null;
            byte[] cpryptedAuthkey = null;
            byte[] cpryptedApkType = null;
            try {
                apkType = getStorred_apkType();
                pubKey = (PublicKey) oin.readObject();
                serverPubKey = pubKey;
                cpryptedAuthkey = encrypt(pubKey,getStorred_authentificationKey());
                cpryptedApkType = encrypt(pubKey,apkType);
                oop.writeObject(cpryptedAuthkey);
                oop.writeObject(cpryptedApkType);
                String authKey_flag_str = (String) oin.readObject();
                System.out.println(authKey_flag_str);
                authKey_flag = Integer.parseInt(authKey_flag_str);
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        private int userLogged() {
            try {

                byte[] cryptedUsername = null;
                byte[] cpryptedPassword = null;
                String clientLoginAnswer = null;

                PublicKey ServerPubKey = (PublicKey) oin.readObject();

                System.out.println("PublicKEY : " + ServerPubKey);
                System.out.println("username : " + getUsr());
                System.out.println("password : " + getPass());
                cryptedUsername = encrypt(ServerPubKey,getUsr());
                cpryptedPassword = encrypt(ServerPubKey,getPass());

                oop.writeObject(cryptedUsername);
                oop.writeObject(cpryptedPassword);
                clientLoginAnswer = oin.readObject().toString();
                System.out.println("clientLoginAnswer : " + clientLoginAnswer);

                if (clientLoginAnswer.equals("1")) {
                    return 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }
        public void mentenanceMode() {
            setCredentials("911 911 911","Medic");
            setUsr("ana");
            setPass("are");
            String hostName = "10.0.2.2";
//          String hostName = "localhost";
//            String hostName = "192.168.1.102";
//          String hostName = "10.0.0.1";
            int portNumber = 9555;
            setHostPort(hostName,portNumber);
            System.out.println("Preferences were saved.");
        }
        public void productionMode(View view) {
            instantiateMembers();
            apkAuthentification();

            int userLoggedInt = userLogged();

            if(this.authKey_flag == 4)
            {
                System.out.println("Aplicatia s-a autentificat cu succes!");
//                MessageBox("Aplicatia s-a autentificat cu succes!");
//                Toast.makeText(LoginActivity.this, "Aplicatia s-a autentificat cu succes!", Toast.LENGTH_LONG).show();


                if( userLoggedInt == 0)
                {


                    System.out.println("Client login failed!");
//                    MessageBox("Client login failed!");
                }
                else
                {
                    System.out.println("Client logged in with success!");
                    StartMedicActivity(view);
//                    MessageBox("Client logged in with success!");
                }

            }
            else
            {
                System.out.println("Aplicatia nu s-a autentificat cu succes!");
                System.out.println("ATENTIE! Orice incercare de corupere a sistemului SERVICIUL DE AMBULANTE se pedepseste penal!");
//                MessageBox("Aplicatia nu s-a autentificat cu succes!");
//                MessageBox("ATENTIE! Orice incercare de corupere a sistemului SERVICIUL DE AMBULANTE se pedepseste penal!");
                System.exit(0);
            }
            System.out.println("Final Medic");

        }
        @Override
        protected Boolean doInBackground (View... params) {
            boolean result = false;
            if (functionREGorSIN == 1) {
                if (id == 1) {
                    int productionMode = 1;
                    if (productionMode == 1) {
//                        mentenanceMode();
                        productionMode(params[0]);

                    } else {
                        mentenanceMode();
                    }
                }
            }

            result = true;
            return result;
        }

    }
}
