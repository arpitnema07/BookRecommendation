package com.android.booksrecommendation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

   private ArrayList<Books> mBooksData;
   private Context mContext;

    BookAdapter(Context context, ArrayList<Books> booksArrayList){
        this.mBooksData = booksArrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BookAdapter.ViewHolder holder, int position) {
            Books currentBook = mBooksData.get(position);
            holder.bindTo(currentBook);
    }

    @Override
    public int getItemCount() {
        return mBooksData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitle;
        private TextView mAuthor;
        private TextView mPrice;
        private ImageView mThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize the Views
            mTitle = itemView.findViewById(R.id.book_title);
            mAuthor = itemView.findViewById(R.id.book_author);
            mPrice = itemView.findViewById(R.id.book_price);
            mThumbnail = itemView.findViewById(R.id.BookThumbnail);

            //set on click listener
            itemView.setOnClickListener(this);
        }
        void bindTo(Books currentBook) {
            if (currentBook.getTitle() != null) {
                String temp = currentBook.getTitle();
                if (temp.length()>40) temp =(temp.substring(0, 35)+"...");
                mTitle.setText(temp);
            }
            if (currentBook.getPrice()>0) {
                String temp = mContext.getString(R.string.price_preffix)+ String.valueOf(currentBook.getPrice());
                mPrice.setText(temp);
            }
            if (currentBook.getBookThumb() != null) {
                Glide.with(mContext).load(currentBook.getBookThumb()).into(mThumbnail);
            }
            if (currentBook.getAuthor() != null) {
                String temp = mContext.getString(R.string.author_preffix) + String.valueOf(currentBook.getAuthor());
                mAuthor.setText(temp);
            }
        }
        @Override
        public void onClick(View v) {
            Snackbar.make(v, mBooksData.get(getAdapterPosition()).getTitle(), Snackbar.LENGTH_SHORT);
        }



    }
}
