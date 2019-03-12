package com.studentlibrary.librarymanagement;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.studentlibrary.librarymanagement.adapters.ReturnBookAdapter;
import com.studentlibrary.librarymanagement.pojo.AllBooks;

import org.json.JSONArray;
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

public class ReturnBookActivity extends AppCompatActivity {
private ReturnBookAdapter mAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    EditText rollNo;
    ImageView submit;
    String mRollNo;
    TextView noBooksIssuedText;
    private ArrayList<AllBooks> mAllBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        rollNo=(EditText)findViewById(R.id.rollno_edit_text);
        submit=(ImageView)findViewById(R.id.submit);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        noBooksIssuedText=(TextView)findViewById(R.id.no_books_issued);
      recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(ReturnBookActivity.this);
                return false;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditText();
            }
        });
        mAllBooks=new  ArrayList<>();

    }

    public void hideKeyboard(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(((Activity) context).getWindow()
                    .getCurrentFocus().getWindowToken(), 0);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void checkEditText() {
        mRollNo=rollNo.getText().toString().trim();
        if (TextUtils.isEmpty(mRollNo))
        {
            rollNo.setError("Enter Roll No");
            return;
        }
        IssuedBookList task = new IssuedBookList();
        task.execute();
    }


    class IssuedBookList extends AsyncTask<Void,Void,String>
    {
        private ArrayList<NameValuePair> nameValuePairs;


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("rollno", mRollNo));
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL+"IssuedBookList.php");
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
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject mainObject = new JSONObject(s);
                JSONArray studentDetails = mainObject.getJSONArray("studentdetails");

                if (studentDetails.length()==0)
                {

                    recyclerView.setVisibility(View.GONE);
                    noBooksIssuedText.setVisibility(View.VISIBLE);
                }
                else {
                    mAllBooks.clear();
                    recyclerView.setVisibility(View.VISIBLE);
                    noBooksIssuedText.setVisibility(View.GONE);
                    for (int i = 0; i < studentDetails.length(); i++) {
                        JSONObject mStudentDetail = studentDetails.getJSONObject(i);
                        String bookName = mStudentDetail.getString("bookname");
                        String bookAuthor = mStudentDetail.getString("author");
                        String bookPublisher = mStudentDetail.getString("publish");
                        String bookEdition = mStudentDetail.getString("edition");
                        String department = mStudentDetail.getString("dept");
                        String bookType = mStudentDetail.getString("type");
                        String barcode = mStudentDetail.getString("bookbarcode");
                        int totalCopies = mStudentDetail.getInt("copy");
                        int fine=mStudentDetail.getInt("fine");
                        int id=mStudentDetail.getInt("id");
                        int bookNo=mStudentDetail.getInt("bookno");
                        int rollNo=mStudentDetail.getInt("rollno");
                        int issueId=mStudentDetail.getInt("issueId");
                        String issueDate = mStudentDetail.getString("issuedate");
                        String returnDate = mStudentDetail.getString("returndate");
                        String actualDate = mStudentDetail.getString("actualdate");
                        AllBooks studentDetails1=new AllBooks();
                        studentDetails1.setBarcode(barcode);
                        studentDetails1.setBookAuthor(bookAuthor);
                        studentDetails1.setBookName(bookName);
                        studentDetails1.setBookEdition(bookEdition);
                        studentDetails1.setDepartment(department);
                        studentDetails1.setBookPublisher(bookPublisher);
                        studentDetails1.setBookType(bookType);
                        studentDetails1.setTotalCopies(totalCopies);
                        studentDetails1.setFine(fine);
                        studentDetails1.setBookId(id);
                        studentDetails1.setBookNo(bookNo);
                        studentDetails1.setRollNo(rollNo);
                        studentDetails1.setIssuedate(issueDate);
                        studentDetails1.setReturnDate(returnDate);
                        studentDetails1.setActualDate(actualDate);
                        studentDetails1.setIssueId(issueId);
                        mAllBooks.add(studentDetails1);

                    }
                }

                mAdapter = new ReturnBookAdapter(ReturnBookActivity.this,mAllBooks);
//
                recyclerView.setLayoutManager(new LinearLayoutManager(ReturnBookActivity.this));
                recyclerView.setAdapter(mAdapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
