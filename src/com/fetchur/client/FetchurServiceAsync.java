package com.fetchur.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public interface FetchurServiceAsync {
    void parsePage(String uri, ArrayList<Keyword> keywords, AsyncCallback<Page> async);
}
