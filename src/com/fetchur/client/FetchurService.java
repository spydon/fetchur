package com.fetchur.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("fetchurService")
public interface FetchurService extends RemoteService {
    Page parsePage(String uri, ArrayList<Keyword> keywords);
}
