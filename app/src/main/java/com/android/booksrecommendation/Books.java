package com.android.booksrecommendation;

import android.graphics.Bitmap;

public class Books {

    private String title;
    private String author;
    private Double price;
    private final Bitmap bookThumb;

    public Books(String title,String author,Double price,Bitmap bookThumb){
        this.title = title;
        this.author = author;
        this.price = price;
        this.bookThumb = bookThumb;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getBookThumb() {
        return bookThumb;
    }

    public Double getPrice() {
        return price;
    }

    public String getAuthor() {
        return author;
    }
}
