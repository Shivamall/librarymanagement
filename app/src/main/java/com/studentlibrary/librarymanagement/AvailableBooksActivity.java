package com.studentlibrary.librarymanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.studentlibrary.librarymanagement.adapters.AvailableBooksAdapter;
import com.studentlibrary.librarymanagement.pojo.AllBooks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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

public class AvailableBooksActivity extends AppCompatActivity {
private AvailableBooksAdapter mAdapter;
    private  ArrayList<AllBooks> mAllBooks;
    private RecyclerView recyclerView;
    String key,value;
    ProgressBar progressBar;
    TextView noBooksAvailable;
    LinearLayout searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_books);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        noBooksAvailable=(TextView)findViewById(R.id.no_books);
        searchBar=(LinearLayout)findViewById(R.id.search_view);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AvailableBooksActivity.this,SearchActivity.class);
                startActivityForResult(intent,10);
            }
        });
        mAllBooks=new  ArrayList<>();
        MyTask task = new MyTask();
        task.execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null)
        {
            key = data.getStringExtra("key");
           value = data.getStringExtra("value");

       MyTaskPostRequest task = new MyTaskPostRequest();  // post request
            task.execute();        }

    }


    class MyTaskPostRequest extends AsyncTask<Void,Void,String>
    {

        private ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected void onPreExecute() {

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key", key));
            nameValuePairs.add(new BasicNameValuePair("value", value));

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL+"Searchbooks.php");
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
            try {
                JSONObject mainObject = new JSONObject(s);
                JSONArray mbooks = mainObject.getJSONArray("book");

                if (mbooks.length()==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    noBooksAvailable.setVisibility(View.VISIBLE);
                }
                else {
                    mAllBooks.clear();
                    recyclerView.setVisibility(View.VISIBLE);
                    noBooksAvailable.setVisibility(View.GONE);
                    for (int i = 0; i < mbooks.length(); i++) {
                        JSONObject book = mbooks.getJSONObject(i);
                        int id = book.getInt("id");
                        String barcode = book.getString("barcode");
                        String name = book.getString("name");
                        String author = book.getString("author");
                        String publisher = book.getString("publish");
                        String edition = book.getString("edition");
                        String dept = book.getString("dept");
                        String type = book.getString("type");
                        int copy = book.getInt("copy");
                        AllBooks books1=new AllBooks();
                        books1.setBarcode(barcode);
                        books1.setBookId(id);
                        books1.setBookAuthor(author);
                        books1.setBookName(name);
                        books1.setBookEdition(edition);
                        books1.setDepartment(dept);
                        books1.setBookPublisher(publisher);
                        books1.setBookType(type);
                        books1.setTotalCopies(copy);
                        mAllBooks.add(books1);
                    }
                }
                mAdapter = new AvailableBooksAdapter(AvailableBooksActivity.this,mAllBooks);
//
                    recyclerView.setLayoutManager(new LinearLayoutManager(AvailableBooksActivity.this));
                    recyclerView.setAdapter(mAdapter);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

    }


    class MyTask extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPreExecute() {
          progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(Global.BASE_URL+"showbooks.php");
                URLConnection con = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                return reader.readLine();
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
                JSONArray mbooks = mainObject.getJSONArray("books");

                if (mbooks.length()==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    noBooksAvailable.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noBooksAvailable.setVisibility(View.GONE);
                    for (int i = 0; i < mbooks.length(); i++) {
                        JSONObject books = mbooks.getJSONObject(i);
                        String bookName = books.getString("bookName");
                        String bookAuthor = books.getString("bookAuthor");
                        String bookPublisher = books.getString("bookPublisher");
                        String bookEdition = books.getString("bookEdition");
                        String department = books.getString("department");
                        String bookType = books.getString("bookType");
                        String barcode = books.getString("barcode");
                        int totalCopies = books.getInt("totalCopies");
                        AllBooks books1=new AllBooks();
                        books1.setBarcode(barcode);
                        books1.setBookAuthor(bookAuthor);
                        books1.setBookName(bookName);
                        books1.setBookEdition(bookEdition);
                        books1.setDepartment(department);
                        books1.setBookPublisher(bookPublisher);
                        books1.setBookType(bookType);
                        books1.setTotalCopies(totalCopies);
                        mAllBooks.add(books1);
                    }
                }

               mAdapter = new AvailableBooksAdapter(AvailableBooksActivity.this,mAllBooks);
//
                recyclerView.setLayoutManager(new LinearLayoutManager(AvailableBooksActivity.this));
                recyclerView.setAdapter(mAdapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
