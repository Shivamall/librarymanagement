package com.studentlibrary.librarymanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studentlibrary.librarymanagement.DetailofReturnBook;
import com.studentlibrary.librarymanagement.Global;
import com.studentlibrary.librarymanagement.R;
import com.studentlibrary.librarymanagement.pojo.AllBooks;

import java.util.ArrayList;

/**
 * Created by Shiva XiD on 31-10-2017.
 */

public class ReturnBookAdapter extends RecyclerView.Adapter<ReturnBookAdapter.ViewHolderItem>  {
    private Context mContext;
    private ArrayList<AllBooks> mAllBooks;
    private final LayoutInflater inflator;
    public ReturnBookAdapter(Context mContext, ArrayList<AllBooks> mAllBooks) {
        this.mContext=mContext;
        this.mAllBooks=mAllBooks;
        inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.book_issued_design,parent,false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        final AllBooks mBooks = mAllBooks.get(position);
        holder.mBookName.setText(mBooks.getBookName());
     holder.mRollno.setText(""+mBooks.getRollNo());
        holder.mIssueDate.setText(mBooks.getIssuedate());
        String actualDate=mBooks.getActualDate();
        if (actualDate==null)
        {
            holder.bookReturnedText.setVisibility(View.GONE);


        }
        else
        {
            holder.bookReturnedText.setVisibility(View.VISIBLE);
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(mContext, DetailofReturnBook.class);
                in.putExtra(Global.BOOK_DETAILS,mBooks);
               mContext.startActivity(in);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mAllBooks.size();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView mBookName,mRollno,mIssueDate,bookReturnedText;
        RelativeLayout mainLayout;
        public ViewHolderItem(View itemView) {
            super(itemView);
            mBookName = (TextView)itemView.findViewById(R.id.book_name);
            mRollno = (TextView)itemView.findViewById(R.id.roll_no);
            mIssueDate = (TextView)itemView.findViewById(R.id.issue_date);
            bookReturnedText = (TextView)itemView.findViewById(R.id.book_returned_text);
mainLayout=(RelativeLayout)itemView.findViewById(R.id.main_layout);

        }
    }


}
