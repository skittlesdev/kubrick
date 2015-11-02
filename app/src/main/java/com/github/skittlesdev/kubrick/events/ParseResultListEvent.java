package com.github.skittlesdev.kubrick.events;

import com.parse.ParseObject;

import java.util.List;

public class ParseResultListEvent {
    private List<ParseObject> results;

    public ParseResultListEvent(List<ParseObject> results) {
        this.results = results;
    }

    public List<ParseObject> getResults() {
        return results;
    }
}
