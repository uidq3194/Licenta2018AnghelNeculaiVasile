package com.sda.ambulanta.apk;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sda.pacient.apk.R;

import java.io.IOException;

public class RegisterActivity extends LoginActivity {

    private int countC = 1;
    private int function = 3;
    private TextInputEditText mCNPView;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mNameView;
    public  Network networktask;
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


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mCNPView = (TextInputEditText) findViewById(R.id.cnp);
        mUsernameView = (EditText) findViewById(R.id.uid);
        mPasswordView = (EditText) findViewById(R.id.password);
        mNameView = (EditText) findViewById(R.id.Name);

        Button registerBTN = findViewById(R.id.registerBTN);
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networktask = new Network();
                function = 1;
                networktask.execute(view);
                networktask.register(view);
            }
        });

        Button closeBTN = findViewById(R.id.closeBTN);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networktask = new Network();
                function = 2;
                networktask.execute(view);
                networktask.close(view);
            }
        });

    }

    public void GoBackToLogin(View view) {
        LoginActivity.id = 2;
        Intent intent = new Intent(this, LoginActivity.class);
        LoginActivity.functionREGorSIN = 4;
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void MessageBox(String MyMessage)
    {

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



        protected void register(View view){
//            Toast.makeText(RegisterActivity.this, "Register called!", Toast.LENGTH_LONG).show();
            System.out.println("Register called!");
            byte[] cryptedUid = null;
            byte[] cryptedCnp = null;
            byte[] cryptedPass =  null;
            byte[] cryptedName =  null;
            try {
                if(countC == 1)
                {
                    LoginActivity.oop.writeObject("0");
                }
                if (mCNPView.getText().toString().equals("") || mUsernameView.getText().toString().equals("") || mPasswordView.getText().toString().equals("") || mNameView.getText().toString().equals("")) {

//                    Toast.makeText(RegisterActivity.this, "Please fill all the fields below!", Toast.LENGTH_LONG).show();
//                    System.out.println("Please fill all the fields below!");
                    MessageBox("Please fill all the fields below!");
                    countC --;
                }
                else
                {
                    cryptedCnp = LoginActivity.encrypt(LoginActivity.serverPubKey,mCNPView.getText().toString());
                    LoginActivity.oop.writeObject(cryptedCnp);
                    String cnpUsed = null;

                    cnpUsed = (String) LoginActivity.oin.readObject();
                    System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMM");

                    if(cnpUsed.equals("0"))
                    {
//                        Toast.makeText(RegisterActivity.this, "CNP already used!", Toast.LENGTH_LONG).show();

                        MessageBox("CNP already used!");

//                        System.out.println("CNP already used!");
                    }
                    else if(cnpUsed.equals("1"))
                    {

                        cryptedUid =  LoginActivity.encrypt(LoginActivity.serverPubKey,mUsernameView.getText().toString());
                        cryptedPass = LoginActivity.encrypt(LoginActivity.serverPubKey,mPasswordView.getText().toString());
                        cryptedName = LoginActivity.encrypt(LoginActivity.serverPubKey,mNameView.getText().toString());
                        LoginActivity.oop.writeObject(cryptedUid);
                        LoginActivity.oop.writeObject(cryptedPass);
                        LoginActivity.oop.writeObject(cryptedName);

                        String recordInserted = (String) LoginActivity.oin.readObject();

                        if (recordInserted.equals("1"))
                        {
//                            Toast.makeText(RegisterActivity.this, "Welcome to SdA", Toast.LENGTH_LONG).show();
//                            System.out.println("Welcome to SdA");
                            MessageBox("Welcome to SdA!");
                        }
                        else if (recordInserted.equals("0"))
                        {
//                            Toast.makeText(RegisterActivity.this, "Registration failed! \n Please contact the adimn or try again!", Toast.LENGTH_LONG).show();
//                            System.out.println("Registration failed! \n Please contact the adimn or try again!");
                            MessageBox("Registration failed! \n Please contact the adimn or try again!");
                        }
                        GoBackToLogin(view);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        protected void close(View view){
//            Toast.makeText(RegisterActivity.this, "Close called!", Toast.LENGTH_LONG).show();
            System.out.println("Close called!");
            try {
                LoginActivity.oop.writeObject("1");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            GoBackToLogin(view);
        }
        @Override
        protected Boolean doInBackground (View... params) {
//            if(function == 1)
//            {
//                register(params[0]);
//            } else if (function == 2)
//            {
//                close(params[0]);
//            }
            return true;
        }
    }

}
