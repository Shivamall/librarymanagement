package com.studentlibrary.librarymanagement.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.studentlibrary.librarymanagement.AddBooks;
import com.studentlibrary.librarymanagement.Global;
import com.studentlibrary.librarymanagement.R;
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

/**
 * Created by Shiva XiD on 27-10-2017.
 */

public class AvailableBooksAdapter extends RecyclerView.Adapter<AvailableBooksAdapter.ViewHolderItem> {
    private Context mContext;
    private  ArrayList<AllBooks> mAllBooks;
    private final LayoutInflater inflator;
    private int bookId;
    private int itemPosition;
    public AvailableBooksAdapter(Context mContext, ArrayList<AllBooks> mAllBooks) {
this.mAllBooks=mAllBooks;
        this.mContext=mContext;
        inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.show_all_books,parent,false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, final int position) {
        final AllBooks mBooks = mAllBooks.get(position);
        holder.mBookName.setText(mBooks.getBookName());
        holder.mBookAuthor.setText(mBooks.getBookAuthor());
        holder.mBookPublisher.setText(mBooks.getBookPublisher());
        holder.mBookEdition.setText(mBooks.getBookEdition());
        holder.mDepartment.setText(mBooks.getDepartment());
        holder.mBookType.setText(mBooks.getBookType());
        holder.mBarcode.setText(mBooks.getBarcode());
        holder.mTotalCopies.setText(""+mBooks.getTotalCopies());


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AddBooks.class);
                intent.putExtra(Global.BOOK_DETAILS,mBooks);
                mContext.startActivity(intent);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .

                                setTitle("Delete Book ")
                        .

                                setMessage("Are you sure you want to delete?")
                        .

                                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteBook(mBooks,position);
                                    }
                                }).

                        setNegativeButton("No", null).show();
            }
        });

    }

    private void deleteBook(AllBooks mBooks, int position) {
        itemPosition =position;
        bookId = mBooks.getBookId();
        BookDeleteRequest task= new BookDeleteRequest();
        task.execute();

    }

    class BookDeleteRequest extends AsyncTask<Void,Void,String>
    {

        private ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected void onPreExecute() {

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("bookId", ""+bookId));


//           holder.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL +"delete_book.php");
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
            Toast.makeText(mContext, ""+s, Toast.LENGTH_SHORT).show();
            if(s.equalsIgnoreCase("success"))
            {
                mAllBooks.remove(itemPosition);
                notifyDataSetChanged();

            }

        }
    }

    @Override
    public int getItemCount() {
        return mAllBooks.size();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView mBookName,mBookAuthor,mBookPublisher,mBookEdition,mDepartment,mBookType,mTotalCopies,mBarcode;
       Button editButton,deleteButton;
        public ViewHolderItem(View itemView) {
            super(itemView);
            mBookName = (TextView)itemView.findViewById(R.id.book_name);
            mBookAuthor = (TextView)itemView.findViewById(R.id.book_author);
            mBookPublisher = (TextView)itemView.findViewById(R.id.book_publisher);
            mBookEdition = (TextView) itemView.findViewById(R.id.book_edition);
            mDepartment = (TextView)itemView.findViewById(R.id.department_name);
            mBookType = (TextView) itemView.findViewById(R.id.book_type);
            mTotalCopies = (TextView)itemView.findViewById(R.id.total_copies);
            mBarcode = (TextView) itemView.findViewById(R.id.barcode);
            editButton = (Button) itemView.findViewById(R.id.edit);
            deleteButton = (Button) itemView.findViewById(R.id.delete);

        }
    }


}
