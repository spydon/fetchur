package com.fetchur.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by spydon on 18/05/15.
 */
public class Keyword implements IsSerializable {
    private double relevance;
    private String name;

    public Keyword() {
        relevance = 1;
    }

    public Keyword(String name, double relevance) {
        this.name = name;;
        this.relevance = relevance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    public String getName() {
        return name.toLowerCase();
    }

    public double getRelevance() {
        return relevance;
    }
}
