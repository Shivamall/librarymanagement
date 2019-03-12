package com.studentlibrary.librarymanagement;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.studentlibrary.librarymanagement.pojo.AllBooks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class DetailofReturnBook extends AppCompatActivity {
private AllBooks mAllBooks;
    TextView bookName,bookNo,bookAuthor,bookPublisher,bookEdition,department,bookType,barcode,rollNo,issueDate,returnDate,actualDate,fine;
   String mActualDate,mReturnDate,mIssueId,mBookId;
    ProgressBar progressbar;
    long mFine;
    String studentFine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailof_return_book);
        mAllBooks=new AllBooks();
        mAllBooks = (AllBooks) getIntent().getSerializableExtra(Global.BOOK_DETAILS);
bookName=(TextView)findViewById(R.id.book_name);
        bookNo=(TextView)findViewById(R.id.book_no);
        bookAuthor=(TextView)findViewById(R.id.book_author);
        bookPublisher=(TextView)findViewById(R.id.book_publisher);
        bookEdition=(TextView)findViewById(R.id.book_edition);
        department=(TextView)findViewById(R.id.department);
        bookType=(TextView)findViewById(R.id.book_type);
        barcode=(TextView)findViewById(R.id.barcode);
        rollNo=(TextView)findViewById(R.id.roll_no);
        issueDate=(TextView)findViewById(R.id.issue_date);
        progressbar=(ProgressBar)findViewById(R.id.progress_bar);
        returnDate=(TextView)findViewById(R.id.return_date);
        actualDate=(TextView)findViewById(R.id.actual_date);
        fine=(TextView)findViewById(R.id.fine);
        fetchDate();
        init();
    }

    private void fetchDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String currentDate=dateFormat.format(date);
        mActualDate = currentDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date()); // Now use today date.
//        c.add(Calendar.DATE, 15); // Adding 5 days
//        String output = sdf.format(c.getTime());
//        mReturnDate = output;

        actualDate.setText(currentDate);
        returnDate.setText(mAllBooks.getReturnDate());
        mReturnDate=returnDate.getText().toString();
        long days=getDaysBetween(mReturnDate,mActualDate);

        if (days>0)
        {
           fine.setText("Rs. "+(days*4)+" (Rs. 4/day)");
            mFine=days*4;
            studentFine=""+mFine;
        }else
        {
           fine.setText("0");
        }

    }


    public long getDaysBetween(String inputString1,String inputString2) {
        SimpleDateFormat myformat = new SimpleDateFormat("dd/MM/yyyy");
        try {

            Date date1 = myformat.parse(inputString1);
            Date date2 = myformat.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            return  days;

//}


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private void init() {
        bookName.setText(mAllBooks.getBookName());
        bookNo.setText(""+mAllBooks.getBookNo());
        bookAuthor.setText(mAllBooks.getBookAuthor());
        bookPublisher.setText(mAllBooks.getBookPublisher());
        bookEdition.setText(mAllBooks.getBookEdition());
        department.setText(mAllBooks.getDepartment());
        bookType.setText(mAllBooks.getBookType());
        barcode.setText(mAllBooks.getBarcode());
        rollNo.setText(""+mAllBooks.getRollNo());
        issueDate.setText(mAllBooks.getIssuedate());
        mIssueId=""+mAllBooks.getIssueId();
mBookId=""+mAllBooks.getBookId();

    }

    public void returnBook(View view) {
       ReturnBook task = new ReturnBook();
        task.execute();
    }


    class ReturnBook extends AsyncTask<Void,Void,String>
    {
        private ArrayList<NameValuePair> nameValuePairs;


        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("fine", studentFine));
            nameValuePairs.add(new BasicNameValuePair("issueId", mIssueId));
            nameValuePairs.add(new BasicNameValuePair("bookId", mBookId));
            nameValuePairs.add(new BasicNameValuePair("actualDate", mActualDate));
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL+"returnBook.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity);
                return response;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressbar.setVisibility(View.GONE);

            Toast.makeText(DetailofReturnBook.this,""+s,Toast.LENGTH_LONG).show();
        }
    }



}
