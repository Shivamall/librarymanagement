package com.studentlibrary.librarymanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.studentlibrary.librarymanagement.pojo.AllBooks;

import org.json.JSONObject;

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

public class IssueBook extends AppCompatActivity {
EditText getName,getAuthor,getPublisher,getEdition,getDepartment,getBookType,getNoOfCopies,qrcodeEditText;
    ProgressBar progressBar;
    String result,barcode,name;
    Button next;
    int id;
    private AllBooks books1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book);
        getName=(EditText)findViewById(R.id.book_name);
        getAuthor=(EditText)findViewById(R.id.book_author);
        getPublisher=(EditText)findViewById(R.id.book_publisher);
        getEdition=(EditText)findViewById(R.id.book_edition);
        getDepartment=(EditText)findViewById(R.id.department_name);
        getBookType=(EditText)findViewById(R.id.book_type);
        getNoOfCopies=(EditText)findViewById(R.id.no_of_copies);
        qrcodeEditText=(EditText)findViewById(R.id.qrcode_edittext);
next=(Button)findViewById(R.id.next);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        checkDetails();
    }
});
    }

    private void checkDetails() {

        String c = getNoOfCopies.getText().toString();
        if (TextUtils.isEmpty(c)) {

            Toast.makeText(IssueBook.this, "Enter roll no", Toast.LENGTH_LONG).show();
        } else

        {
            int i = Integer.parseInt(c);
            if (i == 0) {
                Toast.makeText(this, "No book is available", Toast.LENGTH_SHORT).show();
            } else {
                Intent in = new Intent(IssueBook.this, StudentDetail.class);
                in.putExtra(Global.BOOK_DETAILS, books1);
                startActivity(in);
            }

        }
    }



    public void scan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();

    }
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (intent != null) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (scanResult != null) {
                    result = intent.getStringExtra("SCAN_RESULT");
                   Barcode task = new Barcode();  // post request
                    task.execute();
                    Barcode task1 = new Barcode();
                    task1.execute();
                }
                else
                {
                    Toast.makeText(this, "Barcode not found in database " , Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            }


        }

    public void SearchButton(View view) {
        result=qrcodeEditText.getText().toString().trim();
        Barcode task = new Barcode();  // post request
        task.execute();
    }





    class Barcode extends AsyncTask<Void,Void,String> {

        private ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("scanresult", result));


//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL + "getbookdetails.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {


            progressBar.setVisibility(View.GONE);
            try {
                JSONObject mainObject = new JSONObject(s);
                JSONObject mbooks = mainObject.getJSONObject("book");
                if (mbooks.length()>0) {
                    id = mbooks.getInt("id");
                    barcode = mbooks.getString("bookbarcode");
                       name = mbooks.getString("name");
                    String author = mbooks.getString("author");
                    String publisher = mbooks.getString("publish");
                    String edition = mbooks.getString("edition");
                    String dept = mbooks.getString("dept");
                    String type = mbooks.getString("type");
                    int copy = mbooks.getInt("copy");
                    books1 = new AllBooks();

                    books1.setBarcode(barcode);
                    books1.setBookId(id);
                    books1.setBookAuthor(author);
                    books1.setBookName(name);
                    books1.setBookEdition(edition);
                    books1.setDepartment(dept);
                    books1.setBookPublisher(publisher);
                    books1.setBookType(type);
                    books1.setTotalCopies(copy);


                    getName.setText(name);
                    getAuthor.setText(author);
                    getPublisher.setText(publisher);
                    getEdition.setText(edition);
                    getDepartment.setText(dept);
                    getBookType.setText(type);
                    getNoOfCopies.setText("" + copy);


                }

                else {
                    Toast.makeText(IssueBook.this,"No book found with this barcode",Toast.LENGTH_LONG).show();
                }
            }



            catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }
}
