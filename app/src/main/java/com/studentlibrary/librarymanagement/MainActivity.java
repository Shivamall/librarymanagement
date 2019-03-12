package com.studentlibrary.librarymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
Button View,Add;
   ImageView logout;
    private SharedPreferenceUtility sharedPreferenceUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View=(Button)findViewById(R.id.view);
        Add=(Button)findViewById(R.id.add);
        sharedPreferenceUtility=new SharedPreferenceUtility(this);

        logout=(ImageView) findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferenceUtility.setName(null);
                Intent login=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(login);
                finish();

            }
        });
        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open=new Intent(MainActivity.this,AvailableBooksActivity.class);
                startActivity(open);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(MainActivity.this,AddBooks.class);
                startActivity(in);
            }
        });
    }

    public void isuuebook(android.view.View view) {
        Intent in=new Intent(MainActivity.this,IssueBook.class);
        startActivity(in);
    }

    public void ReturnBook(android.view.View view) {
        Intent in=new Intent(MainActivity.this,ReturnBookActivity.class);
        startActivity(in);
    }
}
