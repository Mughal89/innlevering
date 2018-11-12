package com.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HTTPQuery {
    public Map<String, String> parameters = new HashMap<>();


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> kv : parameters.entrySet()){
            if(sb.length() > 0 ){
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(kv.getKey(), "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(kv.getValue(), "UTF-8"));
            }
            catch(UnsupportedEncodingException e){
            }
        }
        return sb.toString();
    }
}
