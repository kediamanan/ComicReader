package com.example.manankedia.comicreader;

import java.util.Date;

public class Comic {

    private String title;
    private String alt;
    private int num;
    private String transcript;
    private String mDate;
    private String url;


    public Comic(String title,  String month, String year, String day, String alt, String transcript, String url, int num) {
        this.title = title;
        this.alt = alt;
        this.mDate = day+"/"+month+"/"+year;
        this.transcript = transcript;
        this.url = url;
        this.num = num;


    }

    public String getTitle() {
        return title;
    }

    public String getAlt() {
        return alt;
    }

    public int getNum() {
        return num;
    }

    public String getTranscript() {
        return transcript;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return url;
    }
}
