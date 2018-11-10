package com.HTTPServer;

import java.util.List;

public class Utility {
    public static String toString(List<Talk> talks){
        StringBuilder sb = new StringBuilder();
        for(Talk t: talks){
            sb.append(t.title());
            sb.append(",");
            sb.append(t.description());
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String toString(Talk t){
        return "Title = " + t.title() + ", Description = " + t.description();
    }
}