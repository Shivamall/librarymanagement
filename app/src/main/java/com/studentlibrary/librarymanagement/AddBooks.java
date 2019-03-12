package com.studentlibrary.librarymanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.studentlibrary.librarymanagement.pojo.AllBooks;

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

public class AddBooks extends AppCompatActivity  {
EditText bookName,bookAuthor,bookPublisher,bookEdition,noOfCopies,qrCodeEditText;
    String mBookName,mBookAuthor,mBookPublisher,mBookEdition,mDepartment,mTotalCopies,bookType,qrCode,qrCodeInEditText;
    Spinner bookTypeSpinner,departmentNameSpinner;
    Button addBookButton,scanButton;
    private SharedPreferenceUtility sharedPreferenceUtility;
    ProgressBar progressBar;
    private AllBooks AllBooks;
    String bookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);
        bookName=(EditText)findViewById(R.id.book_name);
        bookAuthor=(EditText)findViewById(R.id.book_author);
        bookPublisher=(EditText)findViewById(R.id.book_publisher);
        bookEdition=(EditText)findViewById(R.id.book_edition);
        qrCodeEditText=(EditText)findViewById(R.id.qrcode_edittext) ;
        departmentNameSpinner=(Spinner) findViewById(R.id.department_name);
        noOfCopies=(EditText)findViewById(R.id.no_of_copies);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        bookTypeSpinner=(Spinner)findViewById(R.id.book_type);
        addBookButton=(Button)findViewById(R.id.add_book_button);
        sharedPreferenceUtility=new SharedPreferenceUtility(this);

        AllBooks=new AllBooks();

        Intent in=getIntent();
        int position=0;
        final AllBooks AllBooks=(AllBooks)in.getSerializableExtra(Global.BOOK_DETAILS);

        if(AllBooks!=null) {
            bookName.setText(AllBooks.getBookName());
            bookAuthor.setText(AllBooks.getBookAuthor());
            bookPublisher.setText(AllBooks.getBookPublisher());
            bookEdition.setText(AllBooks.getBookEdition());
            qrCodeEditText.setText(AllBooks.getBarcode());
            noOfCopies.setText("" + AllBooks.getTotalCopies());
            bookId = String.valueOf(AllBooks.getBookId());
            position = findSelectedItemPosition(AllBooks.getDepartment());
            addBookButton.setText("Update");
        }

        else {
            addBookButton.setText("Add");
 }
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkFilledDetails();
            }
        });


        scanButton=(Button)findViewById(R.id.scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.books_category_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bookTypeSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.department_name_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        departmentNameSpinner.setAdapter(adapter2);

        departmentNameSpinner.setSelection(position);
    }




    private int findSelectedItemPosition(String department) {
        String departments[] = getResources().getStringArray(R.array.department_name_array);
        for(int i=0;i<department.length();i++)
        {
            if(departments[i].equalsIgnoreCase(department))
            {
                return i;
            }
        }
        return 0;

    }



    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    private void checkFilledDetails() {
        mBookName=bookName.getText().toString();
        mBookAuthor=bookAuthor.getText().toString();
        mBookPublisher=bookPublisher.getText().toString();
        mBookEdition=bookEdition.getText().toString();
        mTotalCopies=noOfCopies.getText().toString();
        bookType=bookTypeSpinner.getSelectedItem().toString();
        mDepartment=departmentNameSpinner.getSelectedItem().toString();
        qrCodeInEditText=qrCodeEditText.getText().toString();
        if (TextUtils.isEmpty(mBookName))
        {
            bookName.setError("Enter Book Name");
            return;
        }
        if (TextUtils.isEmpty(mBookAuthor))
        {
            bookAuthor.setError("Enter Author Name");
            return;
        }
        if (TextUtils.isEmpty(mBookPublisher))
        {
            bookPublisher.setError("Enter Publisher Name");
            return;
        }
        if (TextUtils.isEmpty(mBookEdition))
        {
            bookEdition.setError("Enter Book Edition");
            return;
        }
        if (TextUtils.isEmpty(mTotalCopies))
        {
            noOfCopies.setError("Enter No of copies");
            return;
        }

        if (TextUtils.isEmpty(qrCodeInEditText))
        {
            Toast.makeText(AddBooks.this,"Scan QR code of Book or Enter Manually",Toast.LENGTH_LONG).show();
            return;

        }
        else {
            MyTaskPostRequest task = new MyTaskPostRequest();  // post request
            task.execute();
        }

    }

    public void back(View view) {
        finish();
    }


    class MyTaskPostRequest extends AsyncTask<Void,Void,String>
    {

        private ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name", mBookName));
            nameValuePairs.add(new BasicNameValuePair("author", mBookAuthor));
            nameValuePairs.add(new BasicNameValuePair("publisher", mBookPublisher));
            nameValuePairs.add(new BasicNameValuePair("edition", mBookEdition));
            nameValuePairs.add(new BasicNameValuePair("department", mDepartment));
            nameValuePairs.add(new BasicNameValuePair("totalcopies", mTotalCopies));
            nameValuePairs.add(new BasicNameValuePair("type", bookType));
            nameValuePairs.add(new BasicNameValuePair("qrcode", qrCodeInEditText));

//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL+"addbooks.php");
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
            Toast.makeText(AddBooks.this,""+s,Toast.LENGTH_SHORT).show();
            }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode==RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                String result = intent.getStringExtra("SCAN_RESULT");
                Toast.makeText(this, "Success " + result, Toast.LENGTH_SHORT).show();
                Log.d("msg", result);
                qrCode = result;
                qrCodeEditText.setText(qrCode);

            } else {
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
        }


    }


}
