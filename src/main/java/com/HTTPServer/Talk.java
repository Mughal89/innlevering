package com.HTTPServer;

public class Talk {
    private String _title;
    private String _description;

    public int id;

    public Talk(String title, String description){
        _title = title;
        _description = description;
    }

    public String title() {
        return _title;
    }

    public String description() {
        return _description;
    }
}
