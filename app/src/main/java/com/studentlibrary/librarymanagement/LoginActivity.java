package com.studentlibrary.librarymanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class LoginActivity extends AppCompatActivity {
EditText username,password;
    Button login;

    private SharedPreferenceUtility sharedPreferenceUtility;
    private AlertDialog mAlertDialog;
    ProgressBar progressBar;
    String mUsername,mPassword,registeredEmail;
    TextView forgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        forgetPassword=(TextView)findViewById(R.id.forget_password);
        sharedPreferenceUtility=new SharedPreferenceUtility(this);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgetPasswordDialog();
            }
        });
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     checkDetails();
                    }
                });
    }

    private void checkDetails() {
        mUsername=username.getText().toString().trim();
        mPassword=password.getText().toString();
        if (TextUtils.isEmpty(mUsername))
        {
            username.setError("Enter username");
            return;
        }
        if (TextUtils.isEmpty(mPassword))
        {
            password.setError("Enter password");
            return;
        }
 MyTaskPostRequest task = new MyTaskPostRequest();  // post request
        task.execute();
    }


    public void showForgetPasswordDialog() {
        View dialogView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.custom_dialog, null);
        ImageView sumbit = (ImageView) dialogView.findViewById(R.id.submit);
ImageView cancel=(ImageView)dialogView.findViewById(R.id.cancel);
        final EditText email=(EditText) dialogView.findViewById(R.id.email);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });
        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           registeredEmail=email.getText().toString().trim();
                forgetPasswordRequest forgetpasswordtask = new forgetPasswordRequest();  // post request
                forgetpasswordtask.execute();
                hideDialog();
            }
        });

        showDialog(dialogView);
    }

    private void showDialog(View dialogView) {

        mAlertDialog = new AlertDialog.Builder(LoginActivity.this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        mAlertDialog.show();
    }

    public void hideDialog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }



    class forgetPasswordRequest extends AsyncTask<Void,Void,String>
    {

        private ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected void onPreExecute() {



            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", registeredEmail));


            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL+"forgot_password.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity);
                return response;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            Toast.makeText(LoginActivity.this,""+s,Toast.LENGTH_LONG).show();
        }
        }








    class MyTaskPostRequest extends AsyncTask<Void,Void,String>
    {

        private ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected void onPreExecute() {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("n", mUsername));
            nameValuePairs.add(new BasicNameValuePair("p", mPassword));

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL+"login.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity);
                return response;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressBar.setVisibility(View.GONE);
            if (s.equalsIgnoreCase("success"))
            {
                sharedPreferenceUtility.setName(mUsername);
                Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_LONG).show();
                Intent in=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(in);
                username.setText("");
                password.setText("");

                finish();
            }

            else
            {
                Toast.makeText(LoginActivity.this,"invalid login",Toast.LENGTH_LONG).show();
            }
        }
    }

}
