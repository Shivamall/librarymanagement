package com.studentlibrary.librarymanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void id(View view) {
        Intent in = new Intent();
        in.putExtra("key","id");
      EditText et = (EditText)findViewById(R.id.edit1);
        String id = et.getText().toString();
        in.putExtra("value",id);
        setResult(1,in);
        finish();    }

    public void name(View view) {
        Intent in = new Intent();
        in.putExtra("key","name");
        EditText et = (EditText)findViewById(R.id.edit2);
        String name = et.getText().toString();
        in.putExtra("value",name);
        setResult(1,in);
        finish();
    }

    public void author(View view) {
        Intent in = new Intent();
        in.putExtra("key","author");
        EditText et = (EditText)findViewById(R.id.edit3);
        String author = et.getText().toString();
        in.putExtra("value",author);
        setResult(1,in);
        finish();

    }

    public void dept(View view) {
        Intent in = new Intent();
        in.putExtra("key","dept");
        EditText et = (EditText)findViewById(R.id.edit4);
        String dept = et.getText().toString();
        in.putExtra("value",dept);
        setResult(1,in);
        finish();
    }
}
