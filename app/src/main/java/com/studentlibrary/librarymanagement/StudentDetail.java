package com.studentlibrary.librarymanagement;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.studentlibrary.librarymanagement.pojo.AllBooks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class StudentDetail extends AppCompatActivity {
    private AllBooks mAllbook;
    EditText rollNo, issueDate, returnDate,bookNo;
    String mRollNo,mIssueDate,mReturnDate,barcode,mBookNo;
    TextView bookName;
    String bookId;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        mAllbook = new AllBooks();

        rollNo = (EditText) findViewById(R.id.roll_no);
        issueDate = (EditText) findViewById(R.id.issue_date);
        returnDate = (EditText) findViewById(R.id.return_date);
        bookName = (TextView) findViewById(R.id.book_name);
        progressbar=(ProgressBar) findViewById(R.id.progress_bar);
        bookNo=(EditText)findViewById(R.id.book_no);
        mAllbook = (AllBooks) getIntent().getSerializableExtra(Global.BOOK_DETAILS);
        bookName.setText(mAllbook.getBookName());
        bookId=""+mAllbook.getBookId();
        barcode=mAllbook.getBarcode();
        fetchDate();
    }

    private void fetchDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String currentDate=dateFormat.format(date);
        mIssueDate = currentDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 15); // Adding 5 days
        String output = sdf.format(c.getTime());
        mReturnDate = output;

        issueDate.setText(currentDate);
        returnDate.setText(mReturnDate);
    }


    public void issue(View view) {
        checkDetails();
    }

    private void checkDetails() {
        mBookNo=bookNo.getText().toString().trim();
        mRollNo=rollNo.getText().toString().trim();
        if (TextUtils.isEmpty(mRollNo))
        {
            rollNo.setError("Enter Roll No");
            return;
        }
        if (TextUtils.isEmpty(mBookNo))
        {
            bookNo.setError("Enter book no");
            return;
        }

        IssueBook task = new IssueBook();  // post request
        task.execute();


    }


    class IssueBook extends AsyncTask<Void,Void,String>
    {

        private ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected void onPreExecute() {

            progressbar.setVisibility(View.VISIBLE);

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", bookId));
            nameValuePairs.add(new BasicNameValuePair("barcode", barcode));
            nameValuePairs.add(new BasicNameValuePair("returndate", mReturnDate));
            nameValuePairs.add(new BasicNameValuePair("rollno", mRollNo));
            nameValuePairs.add(new BasicNameValuePair("bookno", mBookNo));



//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL+"issuebook.php");
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
            progressbar.setVisibility(View.GONE);

            Toast.makeText(StudentDetail.this,""+s,Toast.LENGTH_LONG).show();
        }

    }



}