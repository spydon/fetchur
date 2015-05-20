package com.fetchur.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by spydon on 18/05/15.
 */
public class Page implements IsSerializable {
    private String url;
    private double relevance;
    private ArrayList<String> children;

    private Page() {
        relevance = 0;
        children = new ArrayList<String>();
    }

    public Page(String url) {
        this();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public double getRelevance() {
        return relevance;
    }

    public void incrementRelevance(double incrementation) {
        relevance += incrementation;
    }

    public void addChild(String uri) {
        children.add(uri);
    }

    public ArrayList<String> getChildren() {
        return children;
    }
}
