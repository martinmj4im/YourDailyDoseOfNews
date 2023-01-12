package com.example.yourdailydoseofnews;

public class Story {
    public String mTitle;
    public String mSection;
    public String mDate;
    public String mIntentUrl;
    public String mAuthor;


    public Story (String title, String section, String date, String intentUrl, String author ){
        mTitle = title;
        mSection = section;
        mDate = date;
        mIntentUrl = intentUrl;
        mAuthor = author;
    }
}
