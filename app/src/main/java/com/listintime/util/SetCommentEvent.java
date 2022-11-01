package com.listintime.util;

public class SetCommentEvent {
    public String comment;
    public int position;

    public SetCommentEvent(String comment, int position) {
        this.comment = comment;
        this.position = position;
    }
}
