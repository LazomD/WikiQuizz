package com.marietton.WikiLearning;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lyon on 14/07/2017.
 */
public class Page {
    int content_id;
    String chemin;
    String question;
    String url;

    public Page() {
    }

    public Page(int content_id, String chemin, String question, String url) {
        this.content_id = content_id;
        this.chemin = chemin;
        this.question = question;
        if(url != null){
            try {
                this.url = URLEncoder.encode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUrl() {
        return "http://164.132.43.182:8090/display/QUIZ/" + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }
}
