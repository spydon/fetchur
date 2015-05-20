package com.fetchur.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class Fetchur implements EntryPoint {
    /**
     * Create a remote service proxy to talk to the server-side picture
     * service.
     */
    private final FetchurServiceAsync parseService = GWT.create(FetchurService.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Image logo = new Image("./logo.png");
        final FlowPanel searchPanel = new FlowPanel();
        final FlowPanel framePanel = new FlowPanel();
        final TextBox searchBox = new TextBox();
        final TextBox startBox = new TextBox();
        final TextBox minRelevanceBox = new TextBox();
        final Button searchButton = new Button("Search!");
        final ArrayList<String> children = new ArrayList<String>();
        final ArrayList<String> checked = new ArrayList<String>();
        searchPanel.addStyleName("search-panel");
        framePanel.addStyleName("frame-panel");
        searchBox.addStyleName("search-box");
        startBox.addStyleName("start-box");
        searchButton.addStyleName("search-button");
        logo.addStyleName("logo");
        searchBox.getElement().setPropertyString("placeholder", "Search keywords");
        startBox.getElement().setPropertyString("placeholder", "Starting site");
        minRelevanceBox.getElement().setPropertyString("placeholder", "Minimum relevance");
        KeyPressHandler enterHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                boolean enterPressed = KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode();
                if (enterPressed) {
                    searchButton.click();
                }
            }
        };
        searchBox.addKeyPressHandler(enterHandler);
        startBox.addKeyPressHandler(enterHandler);
        framePanel.setVisible(false);

        searchPanel.add(logo);
        searchPanel.add(searchBox);
        searchPanel.add(startBox);
        searchPanel.add(minRelevanceBox);
        searchPanel.add(searchButton);

        final AsyncCallback<Page> callback = new AsyncCallback<Page>() {
            @Override
            public void onFailure(Throwable caught) {
                // Handle failure
            }

            @Override
            public void onSuccess(final Page page) {
                children.addAll(page.getChildren());
                final AsyncCallback<Page> callback = this;
                if (children.size() > 0) {
                    new Timer() {
                        @Override
                        public void run() {
                            while (checked.contains(children.get(0))) {
                                children.remove(0);
                            }
                            String child = children.get(0);
                            parseService.parsePage(child, getKeywords(searchBox.getText()), callback);
                            checked.add(child);
                            children.remove(0);
                        }
                    }.schedule(2000);
                }
                if (Double.parseDouble(minRelevanceBox.getText()) <= page.getRelevance()) {
                    addFrame(framePanel, page);
                }
            }
        };

        searchButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                searchPanel.setVisible(false);
                framePanel.setVisible(true);
                final String startPage = (startBox.getText().startsWith("http") ? startBox.getText() : "http://" + startBox.getText());
                parseService.parsePage(startPage, getKeywords(searchBox.getText()), callback);
            }
        });

        RootPanel.get().add(searchPanel);
        RootPanel.get().add(framePanel);
    }

    private void addFrame(Panel panel, Page page) {
        final Frame frame = getFrame(page.getUrl());
        panel.add(frame);
        panel.add(new Label("Relevance of <a href='" + page.getUrl() + "'>" + page.getUrl() + "</a>: " + page.getRelevance()));
    }

    private ArrayList<Keyword> getKeywords(String text) {
        ArrayList<Keyword> keywords = new ArrayList<Keyword>();
        String[] parts = text.split(",");
        for (String part : parts) {
            Keyword keyword = new Keyword();
            String[] splitted = part.split(":");
            keyword.setName(splitted[0].trim());
            if (splitted.length > 1) {
                double relevance = Double.parseDouble(splitted[1].trim());
                keyword.setRelevance(relevance);
            }
            keywords.add(keyword);
        }
        return keywords;
    }

    private Frame getFrame(String url) {
        final Frame frame = new Frame(url);
        frame.addStyleName("web-frame");
        return frame;
    }
}