package com.ashiba.neon.dataModel;

import java.io.Serializable;

public class MainPost implements Serializable {

    private String title;

    private String url;

    private String content;

    private int count;

    public MainPost() {

    }

    public MainPost(String title, String url, String content, int count) {
        this.title = title;
        this.url = url;
        this.content = content;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
