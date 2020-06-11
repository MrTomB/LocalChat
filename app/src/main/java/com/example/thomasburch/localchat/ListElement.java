package com.example.thomasburch.localchat;

/**
 * Created by thomasburch on 2/14/16.
 */
public class ListElement {
    ListElement() {};

    ListElement(String nl, String ml, String ul, String tl) {
        nickname = nl;
        message = ml;
        user_id = ul;
        timestamp = tl;
    }

    public String nickname;
    public String message;
    public String user_id;
    public String timestamp;
}