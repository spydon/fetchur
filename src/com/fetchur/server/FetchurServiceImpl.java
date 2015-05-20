package com.fetchur.server;

import com.fetchur.client.Keyword;
import com.fetchur.client.Page;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.fetchur.client.FetchurService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchurServiceImpl extends RemoteServiceServlet implements FetchurService {

    public Page parsePage(String urlString, ArrayList<Keyword> keywords) {
        Page page = new Page(urlString);
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL(urlString);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                for (Keyword keyword : keywords) {
                    page.incrementRelevance(keyword.getRelevance() * getCount(keyword.getName(), line));
                }
                addChildrenCount(page.getChildren(), line);
            }
            System.out.println("\n" + page.getUrl());
            System.out.println(page.getRelevance());
            System.out.println(page.getChildren() + "\n");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        return page;
    }

    private void addChildrenCount(ArrayList<String> children, String line) {
        Pattern p = Pattern.compile("<a href=[\"'](.*?)[\"']>");
        Matcher m = p.matcher(line);
        while (m.find() && m.group(1).contains("http")) {
            children.add(m.group(1));
        }
    }

    private int getCount(String keyword, String line) {
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(line);
        int count = 0;
        while (m.find()) {
            count += 1;
        }
        return count;
    }
}